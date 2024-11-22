import net.beadsproject.beads.core.AudioContext;
import net.beadsproject.beads.data.Buffer;
import net.beadsproject.beads.ugens.Envelope;
import net.beadsproject.beads.ugens.Gain;
import net.beadsproject.beads.ugens.WavePlayer;

public class AudioManager {
	public static AudioContext ac;
	
	public static void tone(float frequency, double volume) throws Exception {
    	Runnable r = new Runnable() {
    		float hz;
    		public void run() {
    			//get and play audio beads
    			Gain sound = PlayTone(hz, (float)volume/10);
            	ac.out.addInput(sound);
                
    			//when audio bead is done kill the bead
    			try {
    				Thread.sleep((long)((Main.audioFadeIn+Main.audioTime+Main.audioFadeOut)*Main.audioTimeTolerance)+1);
    			} catch (Exception e) {}
    			sound.kill();
    		}

            public Runnable playTone(float hz){
                this.hz = hz;
                return this;
            }
        }.playTone(frequency);
        
        new Thread(r).start();
	}

    private static Gain PlayTone(float hz, float vol) {
    	//create wave
    	WavePlayer wp = new WavePlayer(ac, hz, Buffer.SINE);
    	
    	//create fade
    	Envelope fader = new Envelope(ac,0f);
    	fader.addSegment(vol,Main.audioFadeIn,Main.audioFadeInCurve);
    	fader.addSegment(vol,Main.audioTime);
    	fader.addSegment(0f,Main.audioFadeOut,Main.audioFadeOutCurve);
    	Gain audioGain = new Gain(ac,3,fader);
    	
    	//write wave
    	audioGain.addInput(wp);
    	return audioGain;
    }
}