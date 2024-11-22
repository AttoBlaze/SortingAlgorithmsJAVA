import java.util.ArrayList;

public class Benchmarking {
	
	//full benchmark, including sorts, formatting, and printing.
	public static void FullBenchmark(ArrayList<Algorithm> Algorithms, int repeats) {
		//print fancy top text
		long startBenchTime = System.nanoTime();
		String benchTitle = "Benchmark"; if (repeats>1) benchTitle+=" ("+repeats+" sorts)";
		System.out.print("\n\u001B[33m----- "+benchTitle+" -----\u001B[39m\n");
		
		//single algorithm
		if (Algorithms.size()==1) {
			//get algorithm
			SortingAlgorithm algorithm = Main.algorithms.get(Algorithms.get(0));
			
			//sort
			long[] sortTimes = Benchmark(algorithm,Lists.GetSize(Algorithms.get(0)),repeats);
			
			//print result
			if (repeats<2) 	PrintBenchmarkResult(Algorithms.get(0).toString(),sortTimes[0],Lists.GetSize(Algorithms.get(0)),Algorithms.get(0).toString().length());
			else			FormatBenchmarkResults(Algorithms.get(0).toString(),Lists.GetSize(Algorithms.get(0)),sortTimes);
			System.out.print("\nFinished benchmark of algorithm "+Algorithms.get(0).toString()+" in "+(double)(System.nanoTime()-startBenchTime)/1000000+"ms.");
		}
		
		
		//multiple algorithms
		else {
			//find longest algorithm name length for single repeat results
			int longestName=0;
			for (int i=0;i<Algorithms.size();i++) {
				if (Algorithms.get(i).toString().length()>longestName) longestName = Algorithms.get(i).toString().length();
			}
			
			//benchmark algorithms
			for (int i=0;i<Algorithms.size();i++) {
				//get algorithm
				SortingAlgorithm algorithm = Main.algorithms.get(Algorithms.get(i));
				
				//sort
				long[] sortTimes = Benchmark(algorithm,Lists.GetSize(Algorithms.get(i)),Main.benchmarksDone);
				
				//print result
				if (repeats<2) 	PrintBenchmarkResult(Algorithms.get(i).toString(),sortTimes[0],Lists.GetSize(Algorithms.get(i)),longestName);
				else			FormatBenchmarkResults(Algorithms.get(i).toString(),Lists.GetSize(Algorithms.get(i)),sortTimes);
			}
			System.out.print("\nFinished full benchmark ("+Algorithms.size()+" algorithms out of "+Algorithm.values().length+" available) in "+(double)(System.nanoTime()-startBenchTime)/1000000+"ms.");
		}
	}
	
	
	//get sort times
	public static long[] Benchmark(SortingAlgorithm Algorithm, int size, int repeats) {
		long[] sortTimes = new long[repeats];
		for (int j=0;j<repeats;j++) {
			int[] list = Lists.GetList(size);
			long startTime = System.nanoTime();
			Algorithm.Sort(list);
			sortTimes[j] = System.nanoTime()-startTime;	//save runtime
		}
		return sortTimes;
	}
	
	//print for single repeat
	public static void PrintBenchmarkResult (String algorithmName, long sortTime, int listSize, int lineLength) {
		//print single results. Line length is made for printing multiple algorithm sort times in a readable manner by keeping a consistent line length.
		String temp = algorithmName;
		while(temp.length()<lineLength+5) temp+=".";
		System.out.println(temp+(double)sortTime/1000000+"ms.  ("+listSize+" elements)");
	}
	
	//format data and print for multiple repeats
	public static void FormatBenchmarkResults(String algorithmName, int elements, long[] sortTimes) {
		System.out.println("\u001B[32m"+algorithmName+" ("+elements+" elements, "+sortTimes.length+" sorts)\u001B[37m");
		
		//get stats/descriptors
		long total = 0, min=sortTimes[0], max=sortTimes[0], sumVarSq = 0;
		for (int j=0;j<sortTimes.length;j++) {
			total+=sortTimes[j];
			if (min>sortTimes[j]) min = sortTimes[j];
			else if (max<sortTimes[j]) max = sortTimes[j];
		}
		long average = total/sortTimes.length;
		
		//variance, spread (sum of total squared average deviation), and sorting
		int[] sortedResults = new int[sortTimes.length];
		long overflowCounter = Math.max(average/1000000,1);
		for (int j=0;j<sortTimes.length;j++) {
			sumVarSq+=((sortTimes[j]-average)/1000)*((sortTimes[j]-average)/1000);
			sortedResults[j] = (int)(sortTimes[j]/overflowCounter);	//to sort later (/overflowCounter just incase nanotime might cause an overflow with integers and other stuff later)
		}
		long variance = sumVarSq/sortTimes.length,
			 spread = (long)(1000*Math.sqrt(variance));
		
		//get q1, median, q3
		new CountingSort().Sort(sortedResults); //sort raw data
		long q1 =  		sortedResults[sortedResults.length/4],
			 median =	sortedResults[sortedResults.length/2],
			 q3 =  		sortedResults[3*sortedResults.length/4];
		
		/* get consistency - this is a made up term. I made it to try to create a more directly comparable measure of consistency than fx spread, which varies alot with sort time.
		 * its scuffed as hell, i know. But hey, it seems to be a good indicator for how reliable and consistent an algorithm is, so yeah! mission accomplished?
		 * It works by trying to use a normalized variance and normalized difference between medians to get an estimated consistency. FYI, it scales with sorting amount.
		 */
		long consistency = (long)(100000*(Math.sqrt((double)(median*average)/(q1+median+q3)/variance)));
		
		//create string for the raw data
		String rawData = "";
		if (Main.showRawData) {
			rawData = "{"+(double)sortTimes[0]/1000000+"ms";
			for (int j=1;j<sortTimes.length;j++) {
				rawData+=" , "+(double)sortTimes[j]/1000000+"ms";
			}
		}
		
		//print stats
		System.out.print(Main.showRawData?"\033[3m\u001B[38mRaw data: "+rawData+"}\u001B[38m\033[0m\n":""+
						"Average runtime...."+(double)average/1000000+"ms.\n"+
						"Median runtime....."+(double)((long)median*overflowCounter)/1000000+"ms.\n"+
						"Min runtime........"+(double)min/1000000+"ms.\n"+
						"Q1 runtime........."+(double)((long)q1*overflowCounter)/1000000+"ms.\n"+
						"Q3 runtime........."+(double)((long)q3*overflowCounter)/1000000+"ms.\n"+
						"Max runtime........"+(double)max/1000000+"ms.\n"+
						"Variation.........."+(double)(max-min)/1000000+"ms.\n"+
						"Variance..........."+(double)variance/1000000+"\n"+
						"Spread............."+(double)spread/1000000+"\n"+
						"Consistency........"+(double)consistency/1000+"\n"+
						"Total runtime......"+(double)total/1000000+"ms.\n\n\n");
	}	
}
