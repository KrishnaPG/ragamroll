import java.util.Random

class RagaImitator {
  static void main(args) {
    def ra = new RagaAnalyzer()
    Random R = new Random()

    if(args.size() == 1) {

      // If a notation file is passed as arg
      def f = new File(args[0])
      def composition = f.getText()
      ra.parseComposition(composition)
      ['forward', 'inverse'].each { dir ->
        ra.getRagaMap()[dir].keySet().each { swara ->
          ra.walk_list = []
          def rev = ra.walkRagaMap(dir, swara, 16).collect{ n ->
            ((n[1] == 0) ? '': (n[1]/Math.abs(n[1]) > 0) ? '<' : '>') + n[0] +
            (R.nextBoolean()? '' : '')
          }
          println 'O=5 ' + rev.join(' ')
        }
      }
    }
  }
}
