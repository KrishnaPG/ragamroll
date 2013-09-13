// Copyright (c) 2010, Mani Balasubramanian <ragamroll@gmail.com>
// All rights reserved.

import org.jfugue.extras.IntervalPatternTransformer
import org.jfugue.Pattern
import org.jfugue.Player
import org.jfugue.MusicStringParser

/*-------------------------------------------------------------------------*/
class NotationParser {
  def MP = new MusicStringParser();
  String input;
  InputStream inputStream  = getClass().getResourceAsStream('raga_base.txt')
  def raga_base = new ConfigSlurper().parse(inputStream.getText())
  
  // I - Laghu, O - Dhrutam, U - Anudhrutam
  // Jati only affects Laghu. Values of Laghu for various Jatis are:
  // Jati - Chatusra - 4, Thisra - 3, Khanda - 5, Misra - 7, Sankeerna - 9
  // Dhrutam - 2
  // Anudhrutam - 1
  // Gati - sub-divisions of a beat
  // 
  // In notation if we see 
  // 'Triputa,4' - we take 4 as Gati and use default Thisra Jati
  // 'Triputa,4,4' - first 4 is Gati, Second four is Jati
  def tala_map = [           
     // TODO: Convert this into a class
    'dhruva' : [14,[1,5,7,11]],  // I O I I - 4 2 4 4 - Chatusra Jati
    'matya' : [10,[1,5,7]],      // I O I   - 4 2 4   - Chatusra Jati
    'rupaka' : [6,[1,3]],        // O I     - 2 4     - Chatusra Jati
    'rupaka3' : [5,[1,3]],        // O I     - 2 3     - Tisra Jati
    'jhampa' : [10,[1,8,9]],     // I U O   - 7 1 2   - Misra Jati
    'triputa' : [7,[1,4,6]],     // I O O   - 3 2 2   - Thisra Jati
    'ata' : [14,[1,6,11,13]],    // I I O O - 5 5 2 2 - Khanda Jati
    'eka' : [4,[1]],             // I       - 4       - Chatusra Jati
    'adi' : [8,[1,5,7]],         // Triputa           - Chatusra Jati
    'khandachapu' : [10,[1]],    // accents ??
    'misrachapu' : [14,[1]],     // accents ??
    ]

  // Defaults
  def cur_octave = 0; // O=0
  def cur_length_mod = 1; // Integral Note Length L=1
  def tala_gati_tuple = ['adi','4']
  def raga_key_tuple = ['c12','0']
  def c12_srg_abc_map = raga_base.ragas[raga_key_tuple[0].toLowerCase()]['C12_SWARAS']
  def cur_srg_abc_map = c12_srg_abc_map
  def cur_raga_swaras = cur_srg_abc_map.keySet()

  def SWARA_REGEX = /(>*|<*)(s|S|r|R|g|G|m|M|p|P|d|D|n|N|z|Z)(\d*)/

  def swara_seq = [];
  def seq_props = [:];
  // this holds sequence like [[s,5,2],[jfugue,'jfugue_raw'],['tala']] etc
  // change it into a list of objects that implement a consistent interface
  // the interface should provide methods like 
  // renderRoll, renderJFM, renderNotationCM
  // in somecases the method would be a no-op.
  // This will be the model. 
  // ragamroll, notationCM, input, midi are all various views of the same model
  // each view can also provide some interface to change the model.
  // For now only the inputArea acts as a controller to modify the model.
  

  Boolean debug = false

  /*-------------------------------------------------------------------------*/
  // Convenience function to print output for debugging
  /*-------------------------------------------------------------------------*/
  def log(s) { if (this.debug == true) println (s) }

  /*-------------------------------------------------------------------------*/
  // Convenience function to interpret > < to determine octave
  /*-------------------------------------------------------------------------*/
  private octshift(y) {
    try {
      y.getChars().each { x->
        if (x == '>') this.cur_octave++
        else if (x == '<') this.cur_octave--
      }
    }catch(e) { /* don't crash */}
  }
    
  /*-------------------------------------------------------------------------*/
  // Compute sequence properties like midi note range and sequence length
  /*-------------------------------------------------------------------------*/
  private get_seq_props() {
    def midi_notes = []
    def note_lengths = []
    swara_seq.each { v -> 
      if(v[0] != 'jfugue' && v[0] != 'raga' && v[0] != 'tala') {
        midi_notes.push(v[1])
        note_lengths.push(v[2])
      }
    }

    def lowest_midi_note = midi_notes.min()
    // Rest is always midi note 0. 
    // We are only interested in lowest non-rest midi note
    lowest_midi_note = ( lowest_midi_note > 0 ) ? lowest_midi_note : 
                            midi_notes.sort().unique()[1]

    def highest_midi_note = midi_notes.max()
    seq_props = ['lowest_midi_note' : lowest_midi_note, 
                  'highest_midi_note' : highest_midi_note , 
                  'seq_len' : note_lengths.sum()]
  }

  /*-------------------------------------------------------------------------*/
  // Compute midi note number
  /*-------------------------------------------------------------------------*/
  private jfm_element(elem){  // swara, octave, absolute length
    def octave = elem[1]
    def rel_len = elem[2]
    def swara = elem[0]

    // Be a litte more forgiving about case
    if (swara in cur_raga_swaras) swara
    else if (swara.toUpperCase() in cur_raga_swaras ) swara=swara.toUpperCase()
    else if (swara.toLowerCase() in cur_raga_swaras ) swara=swara.toLowerCase()
    else swara = 'Z'

    def jfm
    if (swara.equals('Z') || swara.equals('z')) {
      jfm = " R "
    } else {
      jfm = " " + cur_srg_abc_map[swara] + octave 
    }
    def semitones_above_C = Integer.decode(raga_key_tuple[1])

    // Since swara_map assumes C as the tonic, transpose appropriately
    def tr_jfm = new IntervalPatternTransformer(semitones_above_C).transform(
                      new Pattern(jfm) ).getMusicString().split()[1]

    return [] << elem << MP.getNote(tr_jfm).value << cur_length_mod * rel_len
  }

  /*-------------------------------------------------------------------------*/
  // This is the main entry point for this class
  // Consider making this a private method called by constructor
  /*-------------------------------------------------------------------------*/
  def parse(input){
    log(input)
    swara_seq = []
    swara_seq << ['raga',raga_key_tuple ]
    swara_seq << ['tala',tala_elem(tala_gati_tuple) ]

    // Before tokenize remove comments
    def in_str = input.split('\n').collect { s -> 
                    s.startsWith('%') ? " " : s }.join(" ")

    // We parse each token as SRGM and pass through others to jfugue
    // eg: s R g m4 p D n >s s <n D p m g R s <n8 d4 <<p Z
    // Consider also allowing segment labels like pallavi, anupallavi
    // STRETCH : repeats and measure number within segment

    in_str.tokenize().each { token ->
      log('token : ' + token)
      def matcher = (token =~ this.SWARA_REGEX)
      if (matcher.matches()) {
        def x = matcher.collect{ m -> 
          log(m)
          octshift(m[1])  // process '>', '<' 

          // if an integer follows swaras - S4 R2 etc - that is note length
          // otherwise note length is 1
          def rel_note_len = (m[3].isInteger()) ? m[3].toInteger(): 1
          log(rel_note_len)

          swara_seq << jfm_element([m[2],cur_octave,rel_note_len])
        }
      } else { 
        // if no match for swara then check for directives
        // if not directives then pass it through to JFugue
        def splt = token.split('=')

        if (splt.size() == 2) {
          def val = splt[1]

          switch (splt[0]){ // convert to statemachine
            case "L":
            case 'l':
              if(val.isNumber()) {
                val = val.toFloat()
                this.cur_length_mod = (val > 0) ? val : this.cur_length_mod
                log('cur_length_mod : ' + this.cur_length_mod)
              }
              break;

            case "O":
            case 'o':
              if (val.isInteger()) {
                this.cur_octave = val
                log('cur_octave : ' + this.cur_octave)
              }
              break;

            case "Raga":
            case "raga":
              raga_key_tuple = val.split(',')
              swara_seq << ['raga', raga_key_tuple]
              cur_srg_abc_map = raga_base.ragas[raga_key_tuple[0]]['C12_SWARAS']
              cur_raga_swaras = cur_srg_abc_map.keySet()
              break;

            case "Tala":
            case "tala":
              swara_seq << ['tala',tala_elem(val.split(','))]
              // tala is a directive that only affects notation
              // it is not a sound modifying directive
              // perhaps it should be part of a separate datastructure
              // not swara_seq
              break;

            default:
              log "default: ${token}"
              // has equals sign but don't understand
              // pass it through assuming a JFugue token
              swara_seq << ['jfugue',token]
          }
        } else { 
          // pass it through assuming a JFugue token
          // log "else: ${token}"
          swara_seq << ['jfugue',token]
        }
      }
    }
    get_seq_props()
  }
  
  /*-------------------------------------------------------------------------*/
  // output the given sequence as a JFugue music string
  /*-------------------------------------------------------------------------*/
  def jfm_string(len=0.25, voice=0) {
    log("raga_key_tuple ${raga_key_tuple}")
    def jfm = "V${voice}";
    swara_seq.each{ e -> 
      log("seq: e ${e}")
      def swara = e[0]
      if (swara.equals('jfugue')) { jfm += " " + e[1] + " " }
      else if ( ! swara.equals('raga') && ! swara.equals('tala')) {
        jfm += " ["  + e[1] + "]/" + len*e[2] + " "
      }
    }
    return jfm
  }

  /*-------------------------------------------------------------------------*/
  // output tala strum as a JFugue music string
  // key should match the melody
  // S-P power chord played at every accented beat
  // This assumes that tala is specified at the beginning and never changes
  // rewrite this to accomodate changing talas and ragas anywhere
  // by measuring the sequence and marking tala changes
  // This can also help detect incomplete measures
  /*-------------------------------------------------------------------------*/
  def jfm_tala_string(len=0.25, voice=1) {
    log (raga_key_tuple)
    def swara
    def tala_props,tala,beat,subdiv,measure,accents
    swara_seq.each { v ->
      // tala is a list of list 
      //[beats_per_measure, divisions_per_beat, [accented_beat1,..]]
      if( v[0] == 'tala') { 
        tala_props = v[1]
        tala = tala_props['tala']
        measure = tala_props['measure']
        beat = tala_props['beat']
        accents = tala_props['accents']
      }
      if( v[0] == 'raga') { 
        raga_key_tuple = v[1]
        log("raga_key_tuple ${raga_key_tuple}")
        cur_srg_abc_map = raga_base.ragas[raga_key_tuple[0]]['C12_SWARAS']
        cur_raga_swaras = cur_srg_abc_map.keySet()
      }
    }

    def jfm_s = "V${voice} I[SITAR] "
    def jfm_p = "V${voice+1} I[SITAR] &8356 " //add 2 cents to P
    def Avarthanam = ""
    (0..measure-1).each{ t -> 
      Avarthanam += ((t+1 in accents) ?
	     "${c12_srg_abc_map['S']}/${len} " : 
	     "R/${len} ") 
    }
    log( "Avarthanam: ${Avarthanam}")
    def jfm = jfm_s + Avarthanam*(seq_props['seq_len']/measure + 1)

    Avarthanam = ""
    (0..measure-1).each{ t -> 
      Avarthanam += ((t+1 in accents) ?
	     "${c12_srg_abc_map['P']}/${len} " : 
	     "R/${len} ") 
    }
    log( "Avarthanam: ${Avarthanam}")
    jfm += jfm_p + Avarthanam*(seq_props['seq_len']/measure + 1)
    println jfm

    try {
      return (new IntervalPatternTransformer(
                Integer.decode(raga_key_tuple[1])).transform(
                  new Pattern(jfm))).getMusicString()
    } catch (ArrayIndexOutOfBoundsException e) {
      return jfm
    }
  }

  /*-------------------------------------------------------------------------*/
  // Computes tala parameters from v = [name, division]
  /*-------------------------------------------------------------------------*/
  private tala_elem(v){
    def tala_hash = [:]

    def name
    try { 
      name = v[0]
    } catch (java.lang.ArrayIndexOutOfBoundsException e) {
      name = 'adi'
    }
    def tala = tala_map[name]
    tala_hash['tala'] = tala

    def beat
    try { 
      beat = Integer.decode(v[1]) 
    } catch (java.lang.ArrayIndexOutOfBoundsException e) {
      beat = 4
    }
    tala_hash['beat'] = beat

    def measure = tala[0]*beat
    tala_hash['measure'] = measure

    def accents = tala[1].collect{ (it-1)*beat + 1}
    tala_hash['accents'] = accents
    return tala_hash
  }

  /*-------------------------------------------------------------------------*/
  // Renders the sequence in a somewhat Carnatic textbook notation fashion
  /*-------------------------------------------------------------------------*/
  def SeqToLine(charSpacing=1, lineSpacing=1, foldAccents=false) {
    def fold = (foldAccents == false) ? '' : '\n'*lineSpacing;
    def disp_txt = ""
    def spacer = ' '
    def tala,beat,measure,accents,tala_props
    swara_seq.each { v ->
      if( v[0] == 'tala') { 
        tala_props = v[1]
        tala = tala_props['tala']
        measure = tala_props['measure']
        beat = tala_props['beat']
        accents = tala_props['accents']
      }
      if(v[0] != 'jfugue' && v[0] != 'raga' && v[0] != 'tala') {
        disp_txt += v[0][0]
        if (v[2].toInteger() > 1) {
          (0..(v[2].toInteger()-2)).each { 
            xx1 -> disp_txt += (xx1%2 == 0) ? '.':"'"
          }
        }
      }
    }
    def out = []
    (0..disp_txt.size()/measure-1 ).each {i -> 
        def ll = disp_txt.getAt(i*measure..(i+1)*measure - 1).toList()
        (0..measure-1).each{ j->
          if ((j+1)%measure in accents) {
                if(j!=0) out.push(' '*charSpacing + '|' + fold)
                else out.push('|' + ' '*charSpacing)
                }
          if (j%beat == 0 && j!=0 ) out.push('  '*charSpacing)
          out.push(ll[j] + ' '*charSpacing)
          // append  ' '*charSpacing at accents at end each beat
          }
        out.push(' '*charSpacing + "| ${i+1}" + '\n'*2*lineSpacing)
        }
    return out.join('')
  }

  /*-------------------------------------------------------------------------*/
  // Renders the sequence as a RagaM-Roll
  /*-------------------------------------------------------------------------*/
  def SeqToRoll() {
    // All rolls will be 128 colums wide to cover all valid midi notes
    // Since carnatic music is usually within a 3 octave range we can reduce
    // roll width to make it appear pretty and avoid horizontal scrollbars
    def col_min = seq_props['lowest_midi_note']
    def col_max = seq_props['highest_midi_note'] + 1

    def disp_txt = ''
    def spacer = ' '
    def cur_row = 0

    def tala_props,tala,beat,measure,accents

    swara_seq.each { v ->
      log v 
      if( v[0] == 'tala') { 
        tala_props = v[1]
        tala = tala_props['tala']
        measure = tala_props['measure']
        beat = tala_props['beat']
        accents = tala_props['accents']
      }
      if (v[0] != 'jfugue' && v[0] != 'raga' && v[0] != 'tala') {
        def swara = v[0][0]
        def disp = swara
        def cols = v[1]
        cols = (cols > 0) ? cols : (col_min > 0) ? (col_min - 1) : 0
        def rows = v[2].toInteger()
        (1..rows).each { row ->
            cur_row++
            spacer = ' '
            def bb = cur_row % beat
            def aa = cur_row % measure
            spacer = (bb == 1) ? '.' : spacer
            spacer = (aa == 1) ? '=': (aa in accents ) ? '-' : spacer
            def line = spacer*(cols - col_min +1)
            switch (row){
              case 1:
                line += disp
                break
              case 2:
                line += (rows > 4) ? rows.toString(): '!'
                break
              default:
                line += '!'
                break
            }
          line += spacer*(col_max - cols) 
          if (aa == 1) line += " " + (int)(1 + cur_row / measure)
          disp_txt += line + '\n'
        }
      }
    }
    return disp_txt
  }


  def get_seq_list() {
    return swara_seq
  }


  /*-------------------------------------------------------------------------*/
  // Provide an entry point for testing this class
  // If a srgm file containing notation is given it is rendered
  // Otherwise a test string is rendered in various ragas
  // TODO: convert this into an actual test
  /*-------------------------------------------------------------------------*/
  static void main(args) {
    def np = new NotationParser()
    np.setDebug false
    def pl = new Player()

    def s
    if(args.size() == 1) {
      // If a notation file is passed as arg
      def f = new File(args[0])
      s = f.getText()
      np.parse(s)
      println np.SeqToLine( 2, 2, true )
      println np.SeqToLine( 1, 1, false )
      println np.SeqToRoll()
      println np.jfm_tala_string()
    } else {
      // If no args are passed run some test cases with some data
      //println np.jfm_element(['s',5,1])
      def r = np.raga_base.ragas.keySet().collect { k -> "Raga=${k},3 " }
      r << " "
      def t = np.tala_map.keySet().collect { k -> "Tala=${k},2 " }
      t << " "
      def in_str="""
      T900 I[PIANO] 
      L=4 O=5 S R G M P D N >S Z S <N D P M G R S Z
      L=2 O=6 s >R L=1 <<m4 L=2 M4 p P8 
      >>>s S <<<n D  g R Z
      T700 I[ALTO_SAX] s <n8 d4 D"""
      t.each { tt ->
        //println tt
        r.each { rr ->
          //println rr
          println (rr + tt + in_str)
          np.parse(rr + tt + in_str)
          println "'JFM' : '''${np.jfm_string()}''',"
          println "'LINE' : '''${np.SeqToLine(1,1,true)}''',"
          println "'ROLL' : '''${np.SeqToRoll()}''',"
        }
      }
    }
  }
}