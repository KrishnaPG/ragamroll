## Browser version ##
[Click here to try it!](http://online-compute.rhcloud.com/ragamroll)

Above link is known to work on desktop versions of Google Chrome or Firefox on a computer with audio output.

## Java version ##

The following steps are known to work on a Linux/Mac/Windows PC with soundcard and a working java installation

  * Download ragamroll.jar from the Downloads link above or on the left
  * Run the jar file by double-clicking or java -jar ragamroll.jar
  * From the menubar on ragamroll application choose RagaM-Roll > Play
  * If it all went fine, you should now hear some music playing as shown in the video below



## Click on screenshot below to see a video snippet of RagaM Roll in action ##

<a href='http://www.youtube.com/v/YT4dZE0Vatc'>
<img src='http://ragamroll.googlecode.com/files/ragamroll_screenshot.png' height='600' width='800'>
</img></a>

Ragamroll can map the [note transitions in a Raga](https://sites.google.com/site/ragamroll/raga). A raga's arohana and avarhonana (ascent and descent) alone are not sufficient to infer valid phrases. In such cases, looking at the note transitions in existing compositions can be useful.

<img src='https://ragamroll.googlecode.com/files/vathapi.png' height='600' width='800></img'>

<h3>Introduction</h3>
Sa Ri Ga Ma Pa Da Ni are the seven syllables that are used in the solfa system of Indian Music ( Carnatic and Hindustani ). They are called swara ( note ).<br>
<br>
Sa is the tonic note, and is not fixed in pitch. Typically it is set to match the pitch of one of the 12-EDO steps and the remaining notes fall in place depending on the <a href='http://en.wikipedia.org/wiki/Raga'>Raga</a> chosen.<br>
<br>
Indian music is traditionally learnt by listening and imitating the teacher, because it is extremely difficult to notate the richness of Indian music in great detail and any such notation that captures all such details runs the risk of becoming unwieldy.<br>
<br>
However, written material does find widespread use, mostly as a mnemonic framework.<br>
<br>
Memorizing is essential for beginners, and in later stages improvisation becomes important. Sight-reading is not emphasized and is even considered distracting.<br>
<br>
An approximate rendering of this notation is possible by mapping Indian notes into 12-EDO (12 equal divisions of octave, also called equal tempered, 12-ET, ET12 etc. ) system and generating a midi sequence.<br>
<br>
This <a href='http://ragamroll.googlecode.com/files/ragamroll.jar'>tool</a> introduces a simple and compact notation system.<br>
Musical compositions written using this notation can be visualized as a RagaM-Roll, which resembles a vertical piano roll in ascii with the names of the swaras displayed.<br>
This tool also provides a visualization in a somewhat limited form of traditional Carnatic notation followed in music primers.<br>
<br>
This tool also plays the composition along with auto-generated strum in the style of veena playing.<br>
<br>
This tool transforms the compact notation into JFugue music string and uses JFugue to play it or export to a midi file. The excellent work of  <a href='http://code.google.com/p/jfugue/'>Jfugue</a> project is acknowleged with gratitude.<br>
<br>
Future versions will attempt to render Indian music in its microtonal form using 22 sruti system, and provide facilities to render gamakas and display lyrics. In the longer term, transcribing audio into Indian notation is also envisioned. <a href='http://tarsos.0110.be/'>Tarsos</a> looks very promising in making such a feature possible.<br>
<br>
Another distant goal, is to analyze Raga characteristics and document it in some form, perhaps as <a href='http://bolprocessor.sourceforge.net/bp2intro.htm'>Bol_Processor</a> grammar and try algorithmic composition.<br>
<br>
<h3>Input</h3>
Here is a sample input<br>
<pre><code>% Lines starting with % are comments and are ignored<br>
<br>
% Vathapi Ganapathim Bhaje<br>
<br>
% Jfugue tempo and instrument<br>
T160 I[ALTO_SAX] <br>
<br>
% All talas in their default jati are supported<br>
Tala=adi,4<br>
<br>
% All melakarta raga names are supported as well as mela_X where X = 1, 72 <br>
% Raga=mela_29,4<br>
% To use C12 notation set Raga=c12<br>
 <br>
Raga=hamsadhwani,4<br>
<br>
<br>
<br>
% Set the octave to 5 and unit length to 1<br>
L=1 O=5<br>
<br>
% empty measure to get prepared<br>
z32<br>
G6 R4 R2                   S2 &lt;N2          P3 &gt;R3 &lt;N2 &gt;R4 S &lt;N &gt;S R<br>
G2 S R G P G4 R2           G R S &lt;N        P3 &gt;R3 &lt;N2 &gt;R4 S &lt;N &gt;S R<br>
G4 P2        G4 R2         G R S &lt;N        P3 &gt;R3 &lt;N2 &gt;R4 S &lt;N &gt;S R<br>
G P G G R S R G R R S &lt;N  &gt;G R S &lt;N        P3 &gt;R3 &lt;N2 &gt;R4 S &lt;N &gt;S R<br>
G2 P2 N2 P2 G2 R2          G R S &lt;N        P3 &gt;R3 &lt;N2 &gt;R4 S &lt;N &gt;S R<br>
G P N &gt;S R2 S R S &lt;N P P   G R S &lt;N        P3 &gt;R3 &lt;N2 &gt;R4 S &lt;N &gt;S R<br>
G P N &gt;S R G G R S &lt;N P P  G R S &lt;N        P3 &gt;R3 &lt;N2 &gt;R4 S &lt;N &gt;S R<br>
G2 R2 G2 P6 N4 				   &gt;S R S &lt;N P2 G P G R3 S &lt;N &gt;S R<br>
G2 R2 G2 P6 P N &gt;S R G R S R S &lt;N &gt;S &lt;N P G N P G R S R<br>
&gt;G4 R2 S &lt;N P2 G R G R S &lt;N                P3 &gt;R3 &lt;N2 &gt;R4 - S &lt;N &gt;S R -<br>
G12 G R R S R14 &lt;N2<br>
&gt;S32<br>
</code></pre>

L=1 denotes the default note length. This should be set so as to represent all the notes in the composition as an integral mutiple, since setting fractional values of L are not allowed.<br>
O=5 denotes the default octave.<br>
<br>
s S r R g G m M p P d D n N are the names of the swaras, which acquire their values based on the Raga chosen and the pitch of S.<br>
<br>
The default Raga is C12, which stands for the commonly used mapping of carnatic swaras onto 12-EDO pitches.<br>
The default pitch of S is C5.<br>
<br>
Lowercase represents flat note, except in the case of S and P where both cases mean the same.<br>
<br>
z and Z represent a rest.<br>
<br>
- separates musical phrases. It does not affect visual or musical output. It is only used while analyzing a composition to map a Raga's note transitions.<br>
<br>
When S is set to C, r = C#, R = D, g = Eb, G = E, m = F, M = F#, P = G, d = Ab, D = A, n = Bb, N=B.<br>
<br>
The octave can be absolutely chosen by setting a value like O=3, or by shifting to the next octave by the '>'. This applies to all following notes, until another octave change is encountered. '<' shifts to the previous octave.<br>
<br>
Tala=Adi,4 stands for the name of the <a href='http://en.wikipedia.org/wiki/Tala_(music)'>tala</a> and the subdivisions of the beat. Adi tala has 8 beats per measure, with accents on beats 1,5,7. This is the default, mainly because it is commonly used in Carnatic music.<br>
<br>
Here is a midi file generated from the above input.<br>
<br>
<a href='http://ragamroll.googlecode.com/files/vathapi.mid'>http://ragamroll.googlecode.com/files/vathapi.mid</a>

Here is the output in limited carnatic primer notation, limited because it avoids the dots and lines placed above and below the notes to indicate octaves and duration.  This mainly serves as a mnemonic aid, to memorize the swaras of a composition, while listening to it on the computer for timing and approximate pitch.<br>
<br>
The absence of octave marks shouldn't be a problem since the tool makes it possible to hear the composiiton. Also RagaM-Roll shows the octave clearly.<br>
<br>
The display width of the notes width is made proportional to duration, so that no over or under lines are needed to represent halving or doubling duration.<br>
<br>
Also . and ' just add one unit of time to the duration. Both are equivalent, they just alternate to make it easier to count in twos. The number at the end of the line is the measure number.<br>
<br>
Note: Since I've displayed the following text as a verbatim code block, wiki tries to be smart and colors text within single quotes. ragamroll doesn't display anything in colors yet.<br>
<br>
<h3>Output - Carnatic Notation</h3>

<pre><code>| z . ' .   ' . ' .   ' . ' .   ' . ' .  |  ' . ' .   ' . ' .  |  ' . ' .   ' . ' .  | 1<br>
<br>
| G . ' .   ' . R .   ' . R .   S . N .  |  P . ' R   . ' N .  |  R . ' .   S N S R  | 2<br>
<br>
| G . S R   G P G .   ' . R .   G R S N  |  P . ' R   . ' N .  |  R . ' .   S N S R  | 3<br>
<br>
| G . ' .   P . G .   ' . R .   G R S N  |  P . ' R   . ' N .  |  R . ' .   S N S R  | 4<br>
<br>
| G P G G   R S R G   R R S N   G R S N  |  P . ' R   . ' N .  |  R . ' .   S N S R  | 5<br>
<br>
| G . P .   N . P .   G . R .   G R S N  |  P . ' R   . ' N .  |  R . ' .   S N S R  | 6<br>
<br>
| G P N S   R . S R   S N P P   G R S N  |  P . ' R   . ' N .  |  R . ' .   S N S R  | 7<br>
<br>
| G P N S   R G G R   S N P P   G R S N  |  P . ' R   . ' N .  |  R . ' .   S N S R  | 8<br>
<br>
| G . R .   G . P .   ' . ' .   N . ' .  |  S R S N   P . G P  |  G R . '   S N S R  | 9<br>
<br>
| G . R .   G . P .   ' . ' .   P N S R  |  G R S R   S N S N  |  P G N P   G R S R  | 10<br>
<br>
| G . ' .   R . S N   P . G R   G R S N  |  P . ' R   . ' N .  |  R . ' .   S N S R  | 11<br>
<br>
| G . ' .   ' . ' .   ' . ' .   G R R S  |  R . ' .   ' . ' .  |  ' . ' .   ' . N .  | 12<br>
<br>
| S . ' .   ' . ' .   ' . ' .   ' . ' .  |  ' . ' .   ' . ' .  |  ' . ' .   ' . ' .  | 13<br>
<br>
</code></pre>

Here is a RagaM-Roll snippet.<br>
<br>
==== represents the starting beat of each measure<br>
<br>
----- represents the accented beats<br>
<br>
.......... represents all the other beats<br>
<br>
<br>
The notes S R etc are positioned left to right in the order of ascending pitch, just like in a  keyboard.<br>
The duration of the note is represented by the number of rows occupied by note, followed by !'s.<br>
In the case of notes longer than 4 units, the second row shows the length of the note, saving the effort of counting the number of rows to determine the of duration notes.<br>
<br>
<h3>Output - RagaM-Roll</h3>
<pre><code>z==============================1<br>
32                              <br>
!                              <br>
!                              <br>
!..............................<br>
!                              <br>
!                              <br>
!                              <br>
!..............................<br>
!                              <br>
!                              <br>
!                              <br>
!..............................<br>
!                              <br>
!                              <br>
!                              <br>
!------------------------------<br>
!                              <br>
!                              <br>
!                              <br>
!..............................<br>
!                              <br>
!                              <br>
!                              <br>
!------------------------------<br>
!                              <br>
!                              <br>
!                              <br>
!..............................<br>
!                              <br>
!                              <br>
!                              <br>
==========G====================2<br>
          6                    <br>
          !                    <br>
          !                    <br>
..........!....................<br>
          !                    <br>
        R                      <br>
        !                      <br>
........!......................<br>
        !                      <br>
        R                      <br>
        !                      <br>
......S........................<br>
      !                        <br>
     N                         <br>
     !                         <br>
-P-----------------------------<br>
 !                             <br>
 !                             <br>
        R                      <br>
........!......................<br>
        !                      <br>
     N                         <br>
     !                         <br>
--------R----------------------<br>
        !                      <br>
        !                      <br>
        !                      <br>
......S........................<br>
     N                         <br>
      S                        <br>
        R                      <br>
==========G====================3<br>
          !                    <br>
      S                        <br>
        R                      <br>
..........G....................<br>
             P                 <br>
          G                    <br>
          !                    <br>
..........!....................<br>
          !                    <br>
        R                      <br>
        !                      <br>
..........G....................<br>
        R                      <br>
      S                        <br>
     N                         <br>
-P-----------------------------<br>
 !                             <br>
 !                             <br>
        R                      <br>
........!......................<br>
        !                      <br>
     N                         <br>
     !                         <br>
--------R----------------------<br>
        !                      <br>
        !                      <br>
        !                      <br>
......S........................<br>
     N                         <br>
      S                        <br>
        R                      <br>
==========G====================4<br>
          !                    <br>
          !                    <br>
          !                    <br>
.............P.................<br>
             !                 <br>
          G                    <br>
          !                    <br>
..........!....................<br>
          !                    <br>
        R                      <br>
        !                      <br>
..........G....................<br>
        R                      <br>
      S                        <br>
     N                         <br>
-P-----------------------------<br>
 !                             <br>
 !                             <br>
        R                      <br>
........!......................<br>
        !                      <br>
     N                         <br>
     !                         <br>
--------R----------------------<br>
        !                      <br>
        !                      <br>
        !                      <br>
......S........................<br>
     N                         <br>
      S                        <br>
        R                      <br>
==========G====================5<br>
             P                 <br>
          G                    <br>
          G                    <br>
........R......................<br>
      S                        <br>
        R                      <br>
          G                    <br>
........R......................<br>
        R                      <br>
      S                        <br>
     N                         <br>
..........G....................<br>
        R                      <br>
      S                        <br>
     N                         <br>
-P-----------------------------<br>
 !                             <br>
 !                             <br>
        R                      <br>
........!......................<br>
        !                      <br>
     N                         <br>
     !                         <br>
--------R----------------------<br>
        !                      <br>
        !                      <br>
        !                      <br>
......S........................<br>
     N                         <br>
      S                        <br>
        R                      <br>
==========G====================6<br>
          !                    <br>
             P                 <br>
             !                 <br>
.................N.............<br>
                 !             <br>
             P                 <br>
             !                 <br>
..........G....................<br>
          !                    <br>
        R                      <br>
        !                      <br>
..........G....................<br>
        R                      <br>
      S                        <br>
     N                         <br>
-P-----------------------------<br>
 !                             <br>
 !                             <br>
        R                      <br>
........!......................<br>
        !                      <br>
     N                         <br>
     !                         <br>
--------R----------------------<br>
        !                      <br>
        !                      <br>
        !                      <br>
......S........................<br>
     N                         <br>
      S                        <br>
        R                      <br>
==========G====================7<br>
             P                 <br>
                 N             <br>
                  S            <br>
....................R..........<br>
                    !          <br>
                  S            <br>
                    R          <br>
..................S............<br>
                 N             <br>
             P                 <br>
             P                 <br>
..........G....................<br>
        R                      <br>
      S                        <br>
     N                         <br>
-P-----------------------------<br>
 !                             <br>
 !                             <br>
        R                      <br>
........!......................<br>
        !                      <br>
     N                         <br>
     !                         <br>
--------R----------------------<br>
        !                      <br>
        !                      <br>
        !                      <br>
......S........................<br>
     N                         <br>
      S                        <br>
        R                      <br>
==========G====================8<br>
             P                 <br>
                 N             <br>
                  S            <br>
....................R..........<br>
                      G        <br>
                      G        <br>
                    R          <br>
..................S............<br>
                 N             <br>
             P                 <br>
             P                 <br>
..........G....................<br>
        R                      <br>
      S                        <br>
     N                         <br>
-P-----------------------------<br>
 !                             <br>
 !                             <br>
        R                      <br>
........!......................<br>
        !                      <br>
     N                         <br>
     !                         <br>
--------R----------------------<br>
        !                      <br>
        !                      <br>
        !                      <br>
......S........................<br>
     N                         <br>
      S                        <br>
        R                      <br>
==========G====================9<br>
          !                    <br>
        R                      <br>
        !                      <br>
..........G....................<br>
          !                    <br>
             P                 <br>
             6                 <br>
.............!.................<br>
             !                 <br>
             !                 <br>
             !                 <br>
.................N.............<br>
                 !             <br>
                 !             <br>
                 !             <br>
------------------S------------<br>
                    R          <br>
                  S            <br>
                 N             <br>
.............P.................<br>
             !                 <br>
          G                    <br>
             P                 <br>
----------G--------------------<br>
        R                      <br>
        !                      <br>
        !                      <br>
......S........................<br>
     N                         <br>
      S                        <br>
        R                      <br>
==========G====================10<br>
          !                    <br>
        R                      <br>
        !                      <br>
..........G....................<br>
          !                    <br>
             P                 <br>
             6                 <br>
.............!.................<br>
             !                 <br>
             !                 <br>
             !                 <br>
.............P.................<br>
                 N             <br>
                  S            <br>
                    R          <br>
----------------------G--------<br>
                    R          <br>
                  S            <br>
                    R          <br>
..................S............<br>
                 N             <br>
                  S            <br>
                 N             <br>
-------------P-----------------<br>
          G                    <br>
                 N             <br>
             P                 <br>
..........G....................<br>
        R                      <br>
      S                        <br>
        R                      <br>
======================G========11<br>
                      !        <br>
                      !        <br>
                      !        <br>
....................R..........<br>
                    !          <br>
                  S            <br>
                 N             <br>
.............P.................<br>
             !                 <br>
          G                    <br>
        R                      <br>
..........G....................<br>
        R                      <br>
      S                        <br>
     N                         <br>
-P-----------------------------<br>
 !                             <br>
 !                             <br>
        R                      <br>
........!......................<br>
        !                      <br>
     N                         <br>
     !                         <br>
--------R----------------------<br>
        !                      <br>
        !                      <br>
        !                      <br>
......S........................<br>
     N                         <br>
      S                        <br>
        R                      <br>
==========G====================12<br>
          12                    <br>
          !                    <br>
          !                    <br>
..........!....................<br>
          !                    <br>
          !                    <br>
          !                    <br>
..........!....................<br>
          !                    <br>
          !                    <br>
          !                    <br>
..........G....................<br>
        R                      <br>
        R                      <br>
      S                        <br>
--------R----------------------<br>
        14                      <br>
        !                      <br>
        !                      <br>
........!......................<br>
        !                      <br>
        !                      <br>
        !                      <br>
--------!----------------------<br>
        !                      <br>
        !                      <br>
        !                      <br>
........!......................<br>
        !                      <br>
     N                         <br>
     !                         <br>
======S========================13<br>
      32                        <br>
      !                        <br>
      !                        <br>
......!........................<br>
      !                        <br>
      !                        <br>
      !                        <br>
......!........................<br>
      !                        <br>
      !                        <br>
      !                        <br>
......!........................<br>
      !                        <br>
      !                        <br>
      !                        <br>
------!------------------------<br>
      !                        <br>
      !                        <br>
      !                        <br>
......!........................<br>
      !                        <br>
      !                        <br>
      !                        <br>
------!------------------------<br>
      !                        <br>
      !                        <br>
      !                        <br>
......!........................<br>
      !                        <br>
      !                        <br>
      !                        <br>
<br>
</code></pre>
This site is a member of WebRing.<br>
To browse visit <a href='http://ss.webring.com/navbar?f=l;y=ragamroll;u=defurl'>Here.</a>