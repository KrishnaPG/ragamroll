// Copyright (c) 2010, Mani Balasubramanian <ragamroll@gmail.com>
// All rights reserved.

import javax.sound.midi.*

// Extend javax.sound.midi classes to provide some useful methods

// MidiFile(file) { mf -> /* closure */ }
// MidiFile.tracks.each { tr -> / * closure */ }
// MidiFile.track[1].events { ev -> /* closure */}
// MidiEvent { ev -> ev.time }

// provide global midifile manipulation methods
// MidiFile.transpose(3) // number of semitones to transpose
// MidiFile.quantize(1/8) // 
// MidiFile.retune(intonation, key)
// 
// mf1 = new MidiFile('original.mid"
// mf2 = new MidiFile() // blank container
// mf2.add_track(mf1.extract_track)

// Build a midi file more as notes rather than note on note off events
// during its liftime it can get messages and change behaviour
// How is it different from an event ?
// MusicSequence { mobj -> }
/*
// def b = new MidiSequenceBuilder.event { }

*/

f = new File(args[0])
def s = MidiSystem.getSequence(f)

seqr = MidiSystem.getSequencer()
println "0,0,Header,${s.tracks.size()},${s.class.fields.collect { fld -> if (s.divisionType == s.class."$fld.name") fld.name }.findAll{it != null}[0]},${s.resolution}"

def tracks = s.tracks

s.tracks.size().times { xx -> 
  def t = tracks[xx]
  def zz = xx + 1
  println "${zz},0,Start_track"
  (0..t.size()-1).each { i -> e = t.get(i) 
    m = e.getMessage()
    switch (m.class.simpleName) {
      case "FastShortMessage":
        println "${zz},${e.tick},${m.class.fields.collect { fld -> if (m.command == m.class."$fld.name") fld.name }.findAll{it != null}[0]},${m.channel},${m.data1},${m.data2}"
        break

      case "MetaMessage":
        println "${zz},${e.tick},${m.class.fields.collect { fld -> fld.name }[0]},${m.type},${m.data}" 
        break

      case "SysexMessage":
        println "${zz},${e.tick},${m.data},${m.class.simpleName}"
        m.class.fields.each { fld -> 
          println fld.name }
        break

      case "ImmutableEndOfTrack":
        println "${zz},${e.tick},End_track"
        break

      default:
        println "${zz},${e.tick},${m.class.simpleName}"
        break
      }
    }
}

println "0,0,End_of_file"

/*
t = s.createTrack();
m = new ShortMessage();
m.setMessage(ShortMessage.NOTE_ON, 0, 60, 64);
e = new MidiEvent(m, 0)
t.add(e)

m = new ShortMessage();
m.setMessage(ShortMessage.NOTE_OFF, 0, 60, 64);
e = new MidiEvent(m, 4)
t.add(e)

MidiSystem.write(s, 0, f);
*/

