import java.util.Random

class RagaAnalyzer {

  private Random = new Random()
  static valid_notes = "sSrRgGmMpPdDnNzZ".collect{it}
  private walk_list = []
  private abs_nodes = []
  private abs_edges = []
  private rel_edges = []
  private abs_nodes_histo
  private abs_edge_histo
  private rel_edge_histo
  private notes_by_octave = [:]
  private raga_map = ['forward':[:], 'inverse':[:]]


  RagaAnalyzer(){ }


  RagaAnalyzer(composition){ 
    parseComposition(composition) 
  }


  public parseComposition(composition){
    def parser = new NotationParser()
    def sequence = parser.parse(composition)
    def toks = []
    parser.get_seq_list().each { tok -> 
      try {
        if ("java.util.ArrayList".equals(tok[0].class.name) &&
            tok[0][0] in valid_notes) {
          def note = tok[0][0]
          def octave = tok[0][1]
          if (note in ['s', 'S', 'p', 'P', 'z', 'Z']) {
            note = note.toUpperCase()
          }
          if (note in ['z', 'Z']) { octave = '0' }
          toks.push([note, octave])
        }  else {
          toks.push(['-', '0'])
        }
      } catch (e){
        println e
      }
    }
    (1..toks.size()-1).each{ i -> 
      def start_note = toks[i-1][0]
      def start_octave = Integer.decode(toks[i-1][1])
      def end_note = toks[i][0]
      def end_octave = Integer.decode(toks[i][1])
      if (![start_note, end_note].contains('-')) {
        this.abs_edges.push([[start_note, start_octave], [end_note, end_octave]])
        this.abs_nodes.push([start_note, start_octave])
        this.abs_nodes.push([end_note, end_octave])
      }
    }
    this.abs_nodes_histo = computeHistogram(this.abs_nodes)
    this.abs_edge_histo = computeHistogram(this.abs_edges)
    this.computeRelativeEdges()
    this.rel_edge_histo = computeHistogram(this.rel_edges)
    this.generateRagaMap()
  }


  public printRagaMap() {
    this.raga_map.each { mk, mv ->
      println mk
      mv.each { sk, sv ->
        if ('forward'.equals(mk)) {
          println '\t' + sk
          sv.each {println '\t\t' + it}
        } else {
          sv.each {println '\t' + it}
          println '\t\t\t' + sk
        }
      }
    }
  }

  public getRagaMap() { return this.raga_map }
  private initWalk (){ this.walk_list = [] }
  private printWalkList () {
    def lines = this.walk_list.size()/4.0
    lines.each { l ->
      (0..3).each { i ->
        try {
          println this.walk_list[l*4 + i]
        }catch(e){
          return
        }
      }
    }
  }

  private allPossibleCases(c){
    return [c, c.toUpperCase(), c.toLowerCase()].unique()
  }
  private walkAstep(direction, swara){
    def next_possibilities = nextInRagaMap(direction, swara)
    def n = next_possibilities.values().sort().unique().reverse()
    def lim = n.size() - 1
    def rand_loop = true;
    def x;
    while (rand_loop) {
      x = Math.round(Math.abs(Random.nextGaussian()))
      rand_loop = !(x <= lim ) 
    }
    def m = n[x.intValue()]
    n = next_possibilities.collectMany {k, v -> (v == m) ? [k] : []}
    if (n.size() > 1) {
       x = (Random.nextInt(2*(n.size()-1)) - (n.size()-1)).abs() 
    } else { x = 0; }
    def next = n[x]
  }

  private nextInRagaMap(direction, swara) {
    def ss = this.allPossibleCases(swara[0])
    def oo = [swara[1], 0].unique()
    def keys = [ss,oo].combinations().findAll {
      this.raga_map[direction].containsKey(it)
    }
    
    if (keys.size() <= 0) {
      keys = [['Z','z','S','s'],[0]].combinations().findAll {
        this.raga_map[direction].containsKey(it) }
    } 
    return this.raga_map[direction][keys[0]]
  }

  public walkRagaMap(direction, swara, length){
    if (this.walk_list.size() == 0) {
      this.walk_list.push(swara) 
    }
    def next = walkAstep(direction, swara)
    if (this.walk_list[-1] != next) this.walk_list.push(next)
    while (this.walk_list.size() < length ) {
      //println "length unmatched"
      while (this.walk_list[-1].indexOf(next)) {
        // retry if this is a repeat production
        this.walkRagaMap(direction, next, length)
        //print 'retrying next :'; println next
        if (this.walk_list.size() >= length ) {
          //println "length matched"
          //println 'walk_list :'
          this.printWalkList()
          if ('inverse'.equals(direction)) { return this.walk_list.reverse() }
          else { return this.walk_list }
          break;
        }
      }
    }
  }
  public generateRagaMap() {
    this.rel_edge_histo.each { redge, wt ->
      def L = redge[0]
      def R = redge[1]
      def Lf = [L[0], 0]
      def Rf = R
      if (L[1] > 0) {
        Rf = [R[0], R[1]-L[1]] 
      } else  if (L[1] <0) {
        Rf = [R[0], R[1] + L[1]*-1] 
      }
      def Lr = L
      def Rr = [R[0], 0]
      if (R[1] > 0) {
        Lr = [L[0], L[1]-R[1]] 
      } else  if (R[1] < 0) {
        Lr = [L[0], L[1] + R[1]*-1] 
      }

      this.raga_map['forward'][Lf] = this.raga_map['forward'][Lf] ?: [:]
      this.raga_map['forward'][Lf][Rf] = wt
      this.raga_map['inverse'][Rr] = this.raga_map['inverse'][Rr] ?: [:]
      this.raga_map['inverse'][Rr][Lr] = wt
    }
  }


  public generateDot(){
    //group notes by octave
    this.abs_nodes.each{ 
      this.notes_by_octave[it[1]] = this.notes_by_octave[it[1]] ?: []
      this.notes_by_octave[it[1]].push(it[0]) 
      this.notes_by_octave[it[1]].unique()
      }
    def note_rank = []
    def out_string = ""
    out_string += "digraph raga_analysis {\n";
    this.notes_by_octave.keySet().sort().each { octave  ->
      def notes = valid_notes[0..-3].collect{ n ->
        if (n in this.notes_by_octave[octave]) n
      }.findAll{it != null}

      out_string += "\tsubgraph cluster_" + octave + " {\n";
      notes.each { note ->
        out_string += '\t"' + note + octave + '"[label="' + note + '"];\n' 
      }
      out_string += '\tlabel="octave_' + octave + '"\n';
      out_string += "\t}\n";
      }

    this.abs_edge_histo.each { i->
      out_string += '"' + i.key[0].join() + '" -> "' + i.key[1].join() + '"[' + 
	    'label="' + i.value + '", ' +
	    'weight=' + i.value.toFloat() + ', ' +
	    'decorate=true' +
	    //'constraint=false' +
	    ']\n';
    }
    out_string += "}"
    return out_string
  }


  private computeHistogram(alist){
    def histo = [:]
    alist.each { histo[it] = (histo[it] ?: 0) + 1 }
    return histo
  }

  private computeRelativeEdges(){
    this.rel_edges = []
    this.abs_edges.each { edge ->
      def Ln = edge[0][0]
      def Lo = edge[0][1]
      def Rn = edge[1][0]
      def Ro = edge[1][1]
      def lr_delta = (Lo - Ro)
      def rl_delta = (Ro - Lo)
      if ('Z' in [Ln,Rn]) {lr_delta = 0; rl_delta = 0}
      this.rel_edges.push([[Ln, 0], [Rn, lr_delta]])
      this.rel_edges.push([[Ln, rl_delta], [Rn, 0]])
    }
  }

  static void main(args) {
    def ra = new RagaAnalyzer()

    if(args.size() == 1) {

      // If a notation file is passed as arg
      def f = new File(args[0])
      def composition = f.getText()
      ra.parseComposition(composition)
      println ra.generateDot()
      //ra.printRagaMap()
      /////////////////////////////////////////////////////////////
      // Persist the raga definition to disk for use by other code
      // why not add it to the corpus itself
      // instead of a new config
      ////////////////////////////////////////////////////////////

      def cf = args[0] + '.map'
      //create the datastructure
      def configObj = new ConfigObject()
      configObj.put(args[0], ra.ragaMap)

      //serialize it
      new File( cf ).withWriter{ writer ->
        configObj.writeTo( writer )
      }
      //println "Written map : " + cf

    } else {
      def composition = """
        T160 I[ALTO_SAX] 
        Tala=eka,4
        Raga=nattai,4

        O=5

        L=2
        m p2 m r s r2
        L=1
        s <D N2 >s4 s <N >s3 r G2

        L=2
        m p2 m r s r2
        L=1
        s <D N2 >s4 s <N >s r G2 m2

        L=1
        p N >s <N p m G m p m r s r4
        s <D N2 >s4 s <N >s r G2 m2

        p N >s2 r2 s <N p m r s r4

        p >s2 <N p2 m G m r r s <N >s r G

        m2 p2 N p p m m r r s s r3
        s <D N2 >s12

        O=5
        m2 p2 N p p D N >s s <N >s4
        r r s2 r r s2 s <N >s4

        O=5
        p N >s <N p m G m
        p D N2 >s <N >s2
        r r s2 r r s2 s <N >s2 r4

        s r2 G2 m r4 s4
        s <N p N >s <N p3 m
        N p p m r s

        m p G m p
        s r G m p m p

        p m p N p m 
        >r r s s <N p m

        p2 p m   G m r s   r s <N >s   p m G m
        p2 D N   >s r G m   m r s  r s <N p m
        >s <N p m  G m     N p m   r G m  r r s
        >r2 s   s2 <N   p2 m   r2 s   N s r G

        L=2
        m p2 m r s r2
        L=1
        s <D N2 >s12
      """
      ra.parseComposition(composition)
      //println ra.generateDot()
      ra.printRagaMap()
      /*
      ['forward', 'inverse'].each { dir ->
        this.initWalk = []
        def rev = ra.walkRagaMap( dir , ['S',0] , 32).collect{ it[0] }
        println rev.join(' ')
        0.step(rev.size(), 4) { s->
          4.times { i ->
            print rev[s+i] + ' '
          }
          println ' '
        }
        println ' '
      } */
    }
  }
}

