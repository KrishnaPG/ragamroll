// Copyright (c) 2010, Mani Balasubramanian <ragamroll@gmail.com>
// All rights reserved.

import java.awt.*
import javax.swing.*
import javax.swing.JFileChooser
import javax.swing.filechooser.FileFilter
import javax.swing.text.DefaultHighlighter
import javax.swing.text.Highlighter
import groovy.swing.SwingBuilder
import org.jfugue.Pattern
import org.jfugue.Player

TEMPO = 300
def K_RROLL = 150
def K_NOTATION = 250

Thread th_render, th_scroller, th_player
def CurrentInputFile = "ragamroll_new.srgm"
def jfs 
def jft
def midiSeq
def midiSeqLen
def music 
def np
def pattern = new Pattern()
def player = new Player()

def B = new SwingBuilder()
def inputArea = B.textArea(editable:true)
inputArea.setFont( new Font("Monospaced", Font.PLAIN, 18 ) );
inputArea.append(' '.multiply(80));

def notationArea = B.textArea(editable:false)
notationArea.setFont( new Font("Monospaced", Font.PLAIN, 18 ) );

def rollArea = B.textArea(editable:false)
rollArea.setFont( new Font("Monospaced", Font.PLAIN, 18 ) );

def inputScroller = B.scrollPane{
        widget(inputArea)
}

def ragaScroller = B.scrollPane{
        widget(rollArea)
}
def notationScroller = B.scrollPane(constraints: "bottom"){
        widget(notationArea)
}


def splt =  B.splitPane(orientation:JSplitPane.HORIZONTAL_SPLIT){
      widget(inputScroller)
      widget(ragaScroller)
    } 

def v_splt =  B.splitPane(orientation:JSplitPane.VERTICAL_SPLIT){
      widget(splt)
      widget(notationScroller)
    } 



def  hamsa = """
% Lines starting with % are comments and are ignored

% Vathapi Ganapathim Bhaje

% Jfugue tempo and instrument
T160 I[ALTO_SAX] 

% All talas in their default jati are supported
Tala=adi,4

% A limited set of ragas are only supported. However using C12 notation
% all ragas can be notated. 
Raga=hamsadhwani,4

% Set the octave to 5 and unit length to 1
L=1 O=5

% empty measure to get prepared
z32
G6 R4 R2                   S2 <N2          P3 >R3 <N2 >R4 S <N >S R
G2 S R G P G4 R2           G R S <N        P3 >R3 <N2 >R4 S <N >S R
G4 P2        G4 R2         G R S <N        P3 >R3 <N2 >R4 S <N >S R
G P G G R S R G R R S <N  >G R S <N        P3 >R3 <N2 >R4 S <N >S R
G2 P2 N2 P2 G2 R2          G R S <N        P3 >R3 <N2 >R4 S <N >S R
G P N >S R2 S R S <N P P   G R S <N        P3 >R3 <N2 >R4 S <N >S R
G P N >S R G G R S <N P P  G R S <N        P3 >R3 <N2 >R4 S <N >S R
G2 R2 G2 P6 N4 				   >S R S <N P2 G P G R3 S <N >S R
G2 R2 G2 P6 P N >S R G R S R S <N >S <N P G N P G R S R
>G4 R2 S <N P2 G R G R S <N                P3 >R3 <N2 >R4 S <N >S R
G12 G R R S R14 <N2
>S32
"""

music = hamsa
inputArea.setText(music)
//def hl_ia = inputArea.getHighlighter()
//hl_ia.removeAllHighlights()
//hl_ia.addHighlight(0,100, DefaultHighlighter.DefaultPainter)

np = new NotationParser()


About = {
    def pane = B.optionPane(message:'RagaM-Roll v0.010 \nA tool to Notate & Render Traditional Indian Music\nCopyright \u00a9 2010, Mani Balasubramanian <ragamroll@gmail.com>, All rights reserved.')
    def dialog = pane.createDialog(frame, 'About RagaM-Roll')
    dialog.show()
}

Render = {
  th_render = new Thread ({
    //println "Render Thread"
    input = inputArea.getDocument()
    inpText = input.getText(0,input.getLength())
    //println inpText

    np.parse(inpText)

    // process raga name and avoid passing raga to np
    jfs = np.jfm_string(len=0.125)
    jft = np.jfm_tala_string(len=0.125)
    //jft = ""

    pattern.setMusicString(jfs)
    midiSeq = player.getSequence(pattern)
    midiSeqLen = player.getSequenceLength(midiSeq)

    roll = rollArea.getDocument()
    rollArea.replaceRange(np.SeqToRoll(),0,roll.getLength())

    exp = notationArea.getDocument()
    notationArea.replaceRange('\n'*3 + np.SeqToLine(1,3,true),0,
                                                          exp.getLength())

    //frame.pack()
  })
  println "render thread: ${th_render.toString()}"
  th_render.start()
}

StartPlayer = {
    try {
      StopPlayer()
    } catch(e) {
      /* Nothing to stop */
    }
    th_player = new Thread({ player.play(jfs + jft); player.close() })
    println "player thread: ${th_player.toString()}"
    th_player.start()
    th_scroller = new Thread ({
      def scroll_frac = 0;
      nvp = notationScroller.getViewport()
      nht = nvp.getViewSize().height
      rvp = ragaScroller.getViewport()
      rht = rvp.getViewSize().height
      max_ratio = 1.0
      while (scroll_frac < max_ratio && 
            !Thread.currentThread().isInterrupted()) {
        yr = (scroll_frac * rht).toInteger() - K_RROLL
        rvp.setViewPosition(new Point(0,yr))
        yn = (scroll_frac * nht).toInteger() - K_NOTATION
        nvp.setViewPosition(new Point(0,yn))
        scroll_frac = player.getSequencePosition()/midiSeqLen

        //input = inputArea.getDocument()
        //inpText = input.getText(0,input.getLength())
        //def inpPos1 = (inpText.size()*scroll_frac).toInteger()
        //hl_ia.removeAllHighlights()
        //hl_ia.addHighlight(inpPos1,inPos1+1,DefaultHighlighter.DefaultPainter)
        }
      //hl_ia.removeAllHighlights()
      rvp.setViewPosition(new Point(0,0))
      nvp.setViewPosition(new Point(0,0))
    })
    println "scroller thread: ${th_scroller.toString()}"
    th_scroller.start()
  }
PausePlayer = { player.pause() }
ResumePlayer = { 
  try { 
    player.resume() 
  } catch(e) { 
    /* Nothing to resume */
  } 
}
StopPlayer = { 
  try {
    player.stop(); 
    player.close();
    th_player.interrupt();
    th_scroller.interrupt();
  } catch(e) {
    /* Nothing to stop */
  }
}

ExportMIDI = { 
  def mf = new File( CurrentInputFile.toString() + '_' +
              new Date().format('yyyyMMdd-hhmmssSSS') + ".mid")
  println "Created midi file : " + mf
  try {
    player.saveMidi(jfs + jft, mf)
  } catch(e) {
    println "Exception trying to save ${mf}"
  }
}

Save = { 
  input = inputArea.getDocument()
  inpText = input.getText(0,input.getLength())
  def outfile = new File( CurrentInputFile.toString() + '_' +
              new Date().format('yyyyMMdd-hhmmssSSS'))
  outfile.append(inpText)
  println "Saved as : " + outfile
  Render()
}

Load = { 
  fc = new JFileChooser("./pieces")
  fc.showDialog(null, "Select")
  def SelectedFile = fc.getSelectedFile()
  if (SelectedFile) {
    CurrentInputFile = SelectedFile 
    def CurrentInputFileName= CurrentInputFile.toString()
    inputArea.replaceRange(
          CurrentInputFile.getText(), 
          0, inputArea.getDocument().getLength())
    Render()
    }
  }

Reload = { 
  if ( CurrentInputFile != "ragamroll_new.srgm") {
    println CurrentInputFile
    def CurrentInputFileName= CurrentInputFile.toString()
    println CurrentInputFileName
    inputArea.replaceRange(
          CurrentInputFile.getText(), 
          0, inputArea.getDocument().getLength())
    Render()
    }
  }

frame = new SwingBuilder().frame(title:'RagaM-Roll', 
                pack:true, show:true) {
  menuBar {
    menu(mnemonic:'N', 'Notation') {
      menuItem (actionPerformed:this.&Reload, 'Reload')
      menuItem (actionPerformed:this.&Load, 'Load')
      menuItem (actionPerformed:this.&Save, 'Save')
      menuItem (actionPerformed:this.&ExportMIDI, 'Export MIDI')
      menuItem (actionPerformed:{System.exit(0)}, 'Quit immediately')
    }
    menu(mnemonic:'N', 'RagaM-Roll') {
      menuItem (actionPerformed:this.&Render, 'Render')
      separator()
      menuItem (actionPerformed:this.&StartPlayer , 'Play')
      menuItem (actionPerformed:this.&PausePlayer , 'Pause')
      menuItem (actionPerformed:this.&ResumePlayer , 'Resume')
      menuItem (actionPerformed:this.&StopPlayer , 'Stop')
    }
    menu(mnemonic:'H', 'Help') {
      menuItem(actionPerformed:this.&About, 'About')
    }
  }
  widget(v_splt)
}

frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
frame.pack()
Render()
