// Generated from srgm.g by ANTLR 4.0

import java.util.ArrayList;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.antlr.v4.runtime.tree.ErrorNode;

public class srgmBaseListener implements srgmListener {
  private class State {
    public int cur_octave;
    public int cur_note_length;
    public String cur_raga;
    public String cur_tala;
    @Override public String toString() {
      return "octave: " + cur_octave + 
             ", nlength: " + cur_note_length +
             ", raga: " + cur_raga +
             ", tala: " + cur_tala;
    }
  }
  private State state;
  private ArrayList<String> sequence;
  private String whitespace_chars =  ""       /* dummy empty string for homogeneity */
                        + "\\u0009" // CHARACTER TABULATION
                        + "\\u000A" // LINE FEED (LF)
                        + "\\u000B" // LINE TABULATION
                        + "\\u000C" // FORM FEED (FF)
                        + "\\u000D" // CARRIAGE RETURN (CR)
                        + "\\u0020" // SPACE
                        + "\\u0085" // NEXT LINE (NEL) 
                        + "\\u00A0" // NO-BREAK SPACE
                        + "\\u1680" // OGHAM SPACE MARK
                        + "\\u180E" // MONGOLIAN VOWEL SEPARATOR
                        + "\\u2000" // EN QUAD 
                        + "\\u2001" // EM QUAD 
                        + "\\u2002" // EN SPACE
                        + "\\u2003" // EM SPACE
                        + "\\u2004" // THREE-PER-EM SPACE
                        + "\\u2005" // FOUR-PER-EM SPACE
                        + "\\u2006" // SIX-PER-EM SPACE
                        + "\\u2007" // FIGURE SPACE
                        + "\\u2008" // PUNCTUATION SPACE
                        + "\\u2009" // THIN SPACE
                        + "\\u200A" // HAIR SPACE
                        + "\\u2028" // LINE SEPARATOR
                        + "\\u2029" // PARAGRAPH SEPARATOR
                        + "\\u202F" // NARROW NO-BREAK SPACE
                        + "\\u205F" // MEDIUM MATHEMATICAL SPACE
                        + "\\u3000" // IDEOGRAPHIC SPACE
                        ;        
  /* A \s that actually works for Java’s native character set: Unicode */
  private String     whitespace_charclass = "["  + whitespace_chars + "]";    
  /* A \S that actually works for  Java’s native character set: Unicode */
  private String not_whitespace_charclass = "[^" + whitespace_chars + "]";

	@Override public void enterTala_directive(srgmParser.Tala_directiveContext ctx) { }
	@Override public void exitTala_directive(srgmParser.Tala_directiveContext ctx) { 
    state.cur_tala = ctx.getText().replaceAll(whitespace_charclass + "+", "");
    //dumpContext(ctx); 
  }

	@Override public void enterPhrase_separator(srgmParser.Phrase_separatorContext ctx) { }
	@Override public void exitPhrase_separator(srgmParser.Phrase_separatorContext ctx) { /* dumpContext(ctx); */ }

  private int octaveDelta(String shifter) {
    int sign = 1;
    if (shifter.indexOf('<') >= 0) { sign = -1 ;}
    return sign * shifter.length();
  }

	@Override public void enterShort_octave_directive(srgmParser.Short_octave_directiveContext ctx) { }
	@Override public void exitShort_octave_directive(srgmParser.Short_octave_directiveContext ctx) {
    state.cur_octave += octaveDelta(ctx.OCTAVE_SHIFTER().getText());
    dumpContext(ctx);
  }

	@Override public void enterTime_consumer(srgmParser.Time_consumerContext ctx) { }
	@Override public void exitTime_consumer(srgmParser.Time_consumerContext ctx) { }

	@Override public void enterRaga_directive(srgmParser.Raga_directiveContext ctx) { }
	@Override public void exitRaga_directive(srgmParser.Raga_directiveContext ctx) { 
    state.cur_raga = ctx.getText().replaceAll(whitespace_charclass + "+", "");
    //dumpContext(ctx); 
  }

	@Override public void enterLength_directive(srgmParser.Length_directiveContext ctx) { }
	@Override public void exitLength_directive(srgmParser.Length_directiveContext ctx) {
    state.cur_note_length = Integer.decode(ctx.NUMBER().getText());
    dumpContext(ctx);
  }

	@Override public void enterComposition(srgmParser.CompositionContext ctx) {
    sequence = new ArrayList<String>();
    state = new State();
    state.cur_octave = 5;
    state.cur_note_length = 1;
    state.cur_raga = "c12,0";
    state.cur_tala = "adi,4";
    System.out.println(state);
  }

	@Override public void exitComposition(srgmParser.CompositionContext ctx) {
    System.out.println(sequence);
  }

	@Override public void enterOctave_directive(srgmParser.Octave_directiveContext ctx) { }
	@Override public void exitOctave_directive(srgmParser.Octave_directiveContext ctx) {
    state.cur_octave = Integer.decode(ctx.NUMBER().getText());
    dumpContext(ctx);
  }

	@Override public void enterRest(srgmParser.RestContext ctx) { }
	@Override public void exitRest(srgmParser.RestContext ctx) { dumpContext(ctx); }

	@Override public void enterDirective(srgmParser.DirectiveContext ctx) { }
	@Override public void exitDirective(srgmParser.DirectiveContext ctx) { }

	@Override public void enterJfugue_passthru(srgmParser.Jfugue_passthruContext ctx) { }
	@Override public void exitJfugue_passthru(srgmParser.Jfugue_passthruContext ctx) { 
      
      //dumpContext(ctx); 
   }

	@Override public void enterNote(srgmParser.NoteContext ctx) { }
	@Override public void exitNote(srgmParser.NoteContext ctx) {
    int oct_delta = 0;
    if (ctx.OCTAVE_SHIFTER() != null) {
      oct_delta = octaveDelta(ctx.OCTAVE_SHIFTER().getText());
    }
    state.cur_octave += oct_delta;
    dumpContext(ctx);
    state.cur_octave -= oct_delta;
  }

	@Override public void enterEveryRule(ParserRuleContext ctx) { }
	@Override public void exitEveryRule(ParserRuleContext ctx) { }
	@Override public void visitTerminal(TerminalNode node) { }
	@Override public void visitErrorNode(ErrorNode node) { }

  private void dumpContext(ParserRuleContext ctx) {
    System.out.print(ctx.getStart().getLine() + ":" +
      ctx.getStart().getCharPositionInLine() + " " );
    String t = ctx.getText().replaceAll(whitespace_charclass + "+", "");
    System.out.println(state.toString() + " " + t);
    sequence.add(t);
  }
}