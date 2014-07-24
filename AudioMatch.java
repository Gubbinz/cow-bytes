import java.util.Hashtable;

public class AudioMatch {
	// size of buffer window
	private final int bufferSize;
	// number of frequency points mapped
	// (most predominant)
	private final int freqPoints;
	// size / width of band range
	private final int bandRange;
	// minimum number of matches needed for pass
	private final int minMatches;
	// the range of a match 
	// (i.e. a range centred around the frequency)
	private final int matchRange;
	
	// hash to hold fingerprints
	// uses date / time for key, array of ints for key points
	private static Hashtable <Integer, int[]> audioTable;
	
	private static int[] hashKeys;
	private int keysIndex;
	
	private int lastIndex;
	
	public AudioMatch(int buf, int fp, int br) {
		this.bufferSize = buf;
		this.freqPoints = fp;
		this.bandRange = br;	
		this.lastIndex = 0;
		audioTable = new Hashtable <Integer, int[]>();
		minMatches = 5;
		matchRange = 80;
		hashKeys = new int[3];
		keysIndex = 0;
	}
	
	
	public void addData(int[] points, int index) {
		
		
		// add to an existing entry
		if ((index - this.lastIndex) == 1) {
			//int[] morePoints = new int[this.freqPoints*2];
			
			
				//morePoints = audioTable.get(this.lastIndex);
				
				//for (int i = (freqPoints-1); i < (freqPoints*2)-1; i++) {
					//morePoints[i] = points[i-(freqPoints-1)];
				//}
			
			this.lastIndex = 0;
		}
		else {
			audioTable.put(index, points);
			// test
			System.out.println("Just called put()");
			
			this.lastIndex = index;
		}
		
		
		hashKeys[keysIndex] = index;
		
		
		// test
		int test = index+1;
		System.out.println("Just added " +test);
		
		
	}
	
	

	public void testData(int[] points, int index) {
		// test
		System.out.println("testData called, for matching...");
		System.out.println("audioTable.size() = " +audioTable.size());
		
		int[] matches = new int[audioTable.size()];
		
		for (int i = 0; i< matches.length; i++) {
			matches[i] = 0;
		}
			
		
		int[] hashValues = new int[points.length];
		
		// for each point
		for (int i = 0; i < points.length; i++) {
					
			// for each audio fingerprint
			for (int j = 0; j < audioTable.size(); j++) {
				hashValues = audioTable.get(hashKeys[i]);
				
				// for each point
				for (int k = 0; k < points.length; k++) {
					if (points[i] == hashValues[k]) {
						matches[j]++;
					}
					
					// point big
					if (!((points[i] - hashValues[k]) <= 0) && (points[i] - hashValues[k]) <= (matchRange / 2)) {
						matches[j]++;
					}
							
					// points small
					if (!((points[i] - hashValues[k]) >= 0) && (points[i] - hashValues[k]) <= -(matchRange / 2)) {
						matches[j]++;
					}
					
				}
			}
		}
		
		
		// test
		for (int i = 0; i < matches.length; i++) {
			System.out.println("Window " +index +" got " +matches[i] +" matches.");
		}
		
	}
	
	
	
}
