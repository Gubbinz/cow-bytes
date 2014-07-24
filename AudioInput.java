import javax.sound.sampled.TargetDataLine;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioInputStream;
import java.io.OutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.RandomAccessFile;
import java.util.ArrayList;


public class AudioInput {
	
	private final int bufferSize = 4096;
	private final int amplitudePositive = 20;
	private final int amplitudeNegative = -20;
	
	AudioFormat audioFormat;
	TargetDataLine targetDataLine;
	//ByteArrayOutputStream out;
	
	AudioFileFormat.Type fileType;
    File audioFile;
	
	private byte buffer[];
	private long bufferIndex;
	private byte allAudio[];
	
	private void audioInput() {
		
		buffer = new byte[bufferSize];
		
		try {
			
			audioFormat = getAudioFormat();
		
			DataLine.Info dataLineInfo = new DataLine.Info(TargetDataLine.class, audioFormat);
		
			targetDataLine = (TargetDataLine) AudioSystem.getLine(dataLineInfo);
		
			CaptureThread capture = new CaptureThread();
			capture.start();
			
			try {
				Thread.currentThread().sleep(15000);
			} catch(InterruptedException ex) {
			    Thread.currentThread().interrupt();
			}
		
			capture.stopRunning();
			
			
		
		} catch (Exception e) {
			e.printStackTrace();
		      System.exit(0);
		}
	}
	
	
	// returns AudioFormat object
	private AudioFormat getAudioFormat() {
		// AudioFormat settings, no encoding specified (PCM)
		// 8 bit / 44.1kHz
		float sampleRate = 44100;
		int sampleSizeInBits = 8;
		// mono
		int channels = 1;
		boolean signed = true;
		boolean bigEndian = true;
		
		
		return new AudioFormat(sampleRate, 
								sampleSizeInBits,
								channels,
								signed,
								bigEndian);
	}
	
	// start that shit
	public static void main(String [ ] args) {
		AudioInput ai = new AudioInput();
		ai.audioInput();
	}
	
	
	
	class CaptureThread extends Thread {
		boolean running;
		AudioProcessor audioProcessor = new AudioProcessor();
		
		
		public void run() {
				
			ByteArrayOutputStream audioStream = new ByteArrayOutputStream();

			// get some sound from mic
			try {
				targetDataLine.open(audioFormat);
				targetDataLine.start();
				
			} catch (Exception e) {
				e.printStackTrace();
			      System.exit(0);
			}
			
			
			running = true;
			bufferIndex = 1;
			
			
			
			// get a window full of data
			try {
				while (running) {
					// test
					//System.out.println("Reading from line...");
					
					// read in a window full of data
					int count = targetDataLine.read(buffer, 0, buffer.length);
					
					// test
					//System.out.println("Count is: "+count );
					
					// now write it to the buffer window
					if (count > 0) {
						// test
						//System.out.println("Recording now...");
						audioStream.write(buffer, 0, count);
						
						
						audioProcessor.process(buffer, bufferIndex);
						
						 }
						 
						 
						 bufferIndex++;
					}
						
				// test
				//System.out.println("Getting audio now...");
				
				//allAudio = new byte[audioStream.size()];
				
				//allAudio = audioStream.toByteArray();
				
				audioStream.close();
				
				// test
				//System.out.println("All audio has : "+allAudio.length+" elements");
				 
				
				
			} catch (Exception e) {
				e.printStackTrace();
			      System.exit(0);
			}
		 
			
		}
		
		
		protected void stopRunning() {
			running = false;
		}
		
		
	}

}
