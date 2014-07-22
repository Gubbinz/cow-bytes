
public class AudioProcessor {
	private final int bufferSize;
	private final int amplitudePositive;
	private final int amplitudeNegative;
	private final int topFreqs;
	
	private final int bandRange = 20;
	private int freqMatches = 10;
	int band = 0;
	int count = 0;
	
	
	public AudioProcessor() {
		this.bufferSize = 4096;
		this.amplitudePositive = 20;
		this.amplitudeNegative = amplitudePositive - (amplitudePositive*2);
		this.topFreqs = 10;
	}
	
	public AudioProcessor(int ampPos) {
		this.bufferSize = 4096;
		this.amplitudePositive = ampPos;
		this.amplitudeNegative = amplitudePositive - (amplitudePositive*2);
		this.topFreqs = 10;
	}
	
	
	public void process(byte buffer[], long bufferIndex) {
		// array of complex numbers
		// used to hold buffer values as complex numbers
		Complex complexBuffer[] = new Complex[bufferSize];
		
		// test that the buffer is above the threshold
		if (testAmplitude(buffer)) {
			// test
			System.out.println("Window " +bufferIndex +" passed amp test");
			
			
			
			Complex[] complex = new Complex[bufferSize];
			
			// convert to complex number, with imaginary part as 0
			for (int i = 0; i < bufferSize; i++) {
				complex[i] = new Complex(buffer[i], 0);
			}
			
			complexBuffer = FFT.fft(complex);
			
			//test for 20 values
			//for (int j = 0; j <= 20; j++) {
			//	System.out.println(complex[j]);
			//}
			// test last value
			//System.out.println("last value is: " +complex[bufferSize-1]);
			
			
			
			double freq[] = new double[bufferSize];
			
			for (int i = 0; i < bufferSize; i ++) {
				double freqAmp = Math.log(complexBuffer[i].abs()+1);
				freq[i] = freqAmp;
			}
			
			findFreqs(freq);
			
			
			// test
			//System.out.println("Buffer " +bufferIndex +"'s first FFT value is " +complexBuffer[0]);
			//System.out.println("Buffer " +bufferIndex +"'s first Freq value is " +freq[0]);
			
			
			
			
		}
	}
	
	
	private boolean testAmplitude(byte buffer[]) {
		long sum = 0;
		
		
		for (int i = 0; i < buffer.length; i++) {
			sum += buffer[i];
		}
		
		if (sum >= amplitudePositive) {
			
			return true;
		}
		else if (sum <= amplitudeNegative) {
			return true;
		}
		else
			return false;
		
	}
	
	
	private void findFreqs(double buffer[]) {
	
		
		double highest = 0;
		int highestIndex = 0;
		double highFreqs[] = new double[206];
		int highFreqsIndex[] = new int[206];
		
		band = 0;
		
		// go through all values
		for (int i = 0; i < buffer.length; i++) {
			
			// check a band range
			
			//for (int j = 0; j < bandRange; j++) {
					
				if (buffer[i] > highest) {
					highest = buffer[i];
					highestIndex = i;
				}
		//	}
				
			// if a range has been checked...
			if (((i+1) % bandRange) == 0) {
				
				// save it
				highFreqs[band] = highest;
				highFreqsIndex[band] = highestIndex;
				
				band++;
				// reset
				highest = 0;
				highestIndex = 0;
			}
			
		}
		// test
	//	for (int i = 0; i < 10; i++)
	//		System.out.println("Highest in band was index: " +highFreqsIndex[i] +", with score of " +highFreqs[i]);
		
		
		double topFreqs[] = new double[freqMatches];
		int topFreqsBand[] = new int[freqMatches];
		boolean flag = false;
		
		for (int i = 0; i < 206; i++) {
			// initialise with the first 10
			if (i < 10) {
				topFreqs[i] = highFreqs[i];
				topFreqsBand[i] = highFreqsIndex[i];
			}
			
			// check for highest with the rest
			else {
			
			for (int j = 0; j < freqMatches; j++) {
				if (flag == false && highFreqs[i] > topFreqs[j]) {
					topFreqs[j] = highFreqs[i];
					topFreqsBand[j] = highFreqsIndex[i];
					flag = true;
				}
			}
			flag = false;
			
			}
		}
		
		
		
		double orderedValues[] = new double[freqMatches];
		int orderedBands[] = new int[freqMatches];
		int k = 0;
		boolean flag2 = false;
		int match = 0;
		
		// initialise
		for (int i = 0; i < freqMatches; i++) {
			orderedBands[i] = 300;
		}
		
		
		System.out.println("The top bands were:");
		for (int i = 0; i < freqMatches; i++) {
			for (int j = 0; j < freqMatches; j++) {
				
				if (flag2 == false && topFreqs[i] > topFreqs[j]) {
					System.out.println("Band " +topFreqsBand[i] +": " +topFreqs[i]);
					flag = true;
					break;
				}
			}
			flag = false;
			topFreqs[i] = 0;
			topFreqsBand[i] = 0;
		}
		

		// test
		//System.out.println("Top 10 Frequency bands and values:");
		//for (int i = 0; i < freqMatches; i++)
		//	System.out.println("Band " +orderedBands[i] +": " +orderedValues[i]);
		
	}
	
	
	
	
	
	
	
	
	
	private void getFreqBand(int index) {
		if (index < bandRange) {
			band = 1;
			count++;
		}
		
		else if (count == bandRange) {
			band++;
			count = 0;
		}
			
		else
			count++;	
	}
	
}
