// Copyright (c) 2010, Mani Balasubramanian <ragamroll@gmail.com>
// All rights reserved.

class TokenizerClass {
    private def _fsm
    private FCPOS = 0
    private COL = 0
    private ROW = 0
    def TOKEN = [:]
    def COLLECTION = []

    def incr_cpos() { FCPOS++ ; COL++ }

    def incr_lpos() { ROW++; COL=0 }

    def start_token() { 
      TOKEN = ["line":ROW+1, "column":COL+1, "fcpos": FCPOS, "token" : ''] }

    def accumulate(x) { TOKEN['token'] += CH }

    def end_token() {}

    def emit_token() { COLLECTION << TOKEN }

    def dump_collection() {
      def LINES = [:]
      COLLECTION.each { e ->
        def row = e['line']
        try { 
          LINES[row] << e 
        } catch (NullPointerException) { 
          LINES[row] = []; 
          LINES[row] << e 
        }
      }

      // uncomment - remove all lines whose first not-whitespace character is %
      return LINES.collect { k,l -> 
        if (l[0]['token'] != '%') l 
       }.grep { it }.flatten()
    }

    TokenizerClass () {
        _fsm = new TokenizerClassContext(this)

        // Uncomment to see debug output.
        // _fsm.setDebugFlag(true)
    }

    static CH = ''
    def TokenizeString (String string) {
        _fsm.enterStartState()
        for (c in string ) {
            CH = c
            switch (CH) {
                case '\n':
                    _fsm.new_line()
                    break
                case ' ':
                    _fsm.space()
                    break
                case '\t':
                    _fsm.tab()
                    break
                default:
                    _fsm.other()
            }
        }
        _fsm.eof();
        dump_collection()
    }
}
