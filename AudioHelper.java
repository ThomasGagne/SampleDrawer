import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

public class AudioHelper {

	public volatile boolean mPlay = false;
	private Thread mPlayingThread;
	private float mVolume = -10f;

	public void play(byte[] sample) {
		if(!mPlay) {
			mPlay = true;
			final byte[] sampleCopy = sample;

			mPlayingThread = new Thread() {
				public void run() {
					try {
						int speed = SampleHelper.getSpeed(sampleCopy.length);
						if(speed < 44100) {
							// Messing with the sample rate here allows us to always hear how the sample would
							// sound when played back on a ~44100Hz sample rate
							int timesSampleRateMoreThanSpeed = 44100 / speed;
						    AudioFormat af =
						        new AudioFormat(
						            speed * timesSampleRateMoreThanSpeed, // sampleRate
						            8,           // sampleSizeInBits
						            1,           // channels
						            true,        // signed
						            false);      // bigEndian
						    SourceDataLine sdl = AudioSystem.getSourceDataLine(af);
						    sdl.open(af, 2048);
						    ((FloatControl) sdl.getControl(FloatControl.Type.MASTER_GAIN)).setValue(mVolume);
						    sdl.start();
						    try {
						    	byte[] sampleToPlay;
							    while(mPlay){
							    	sampleToPlay = new byte[sampleCopy.length * timesSampleRateMoreThanSpeed];
							    	// Do this every time so we can have live updating of the sample
							    	// Note that we do this so that the sample
							    	for(int i = 0; i < sampleCopy.length; i++) {
							    		for(int j = 0; j < timesSampleRateMoreThanSpeed; j++) {
							    			sampleToPlay[(i * timesSampleRateMoreThanSpeed) + j] = sampleCopy[i];
							    		}
							    	}
						    		sdl.write(sampleToPlay, 0, sampleToPlay.length);
							    }
						    } finally {
							    sdl.stop();
							    sdl.drain();
							    sdl.close();
						    }
						} else {
							// If the speed is > than 44100Hz, there's no reason to downgrade the sample rate
						    AudioFormat af =
						        new AudioFormat(
						            speed, // sampleRate
						            8,           // sampleSizeInBits
						            1,           // channels
						            true,        // signed
						            false);      // bigEndian
						    SourceDataLine sdl = AudioSystem.getSourceDataLine(af);
						    sdl.open(af, sdl.getBufferSize() / 8);
						    ((FloatControl) sdl.getControl(FloatControl.Type.MASTER_GAIN)).setValue(mVolume);
						    sdl.start();
						    try {
							    while(mPlay){
						    		sdl.write(sampleCopy, 0, sampleCopy.length);
							    }
						    } finally {
							    sdl.stop();
							    sdl.drain();
							    sdl.close();

						    }
						}
					} catch(LineUnavailableException e){
						e.printStackTrace();
					}
				}
			};

			mPlayingThread.start();
		}
	}

	public void stop() {
		mPlay = false;
	}
}
