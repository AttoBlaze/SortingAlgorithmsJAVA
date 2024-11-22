import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import net.beadsproject.beads.core.AudioContext;
import net.beadsproject.beads.core.io.JavaSoundAudioIO;

public class Main {
	public enum Mode {
		Benchmark,
		SampleSort,
		Visualize;
	}
	public enum Visualization {
		BarGraph,
		ColorCircle,
		ScatterPlot,
		DisparityDots;
	}
	
	//TODO:
	//	midi sounds?
	//	more sorting algorithms
	//		wikisort, circle sort (swap opposing indexes in list split recursively until no swaps are made), cycle sort (?), strandsort (find sorted subsequences, and merge each sorted sequence found), pancake sort, binary insertion sort, timsort, pidgeonhole sort
	
	//top 5 fastest algorithms (real) (you wont believe number 7) (speed actually varies alot alot but this is generally correct):
	//	1. CountingSort - almost feels like cheating to be honest
	//	2. RadixSortLSD - one of the fastest because of using bit operators and low calculation amount. Funnily, one of the oldest and most analogue algorithms is still one of the fastest.
	//	3. RadixSortMSD - slightly slower than RadixSortLSD due to more calculations. Still extremely effective, and more importantly, much more usable in real-world scenarios because of its flexibility and expandability.
	//	4. QuickSort - I mean its quicksort come on, it has to be quick. Also very consistant.
	//	5. MergeSort - more fun to look at than quicksort imo. Very fast, too.
	
	
	
	//VARIABLES
	public static Algorithm 	sortingAlgorithm = Algorithm.GravitySort		; 	//initially selected algorithm
	public static Mode			programMode = 			Mode.Visualize		;	//program mode
	public static Visualization	visualization =Visualization.ColorCircle		;	//visualization type
	public static Lists.ListType listType =   Lists.ListType.Linear			;	//type of list used
	//
	public static Algorithm[] ignored = new Algorithm[]{ 	//list of ignored algorithms. Ignored algoritms are not used in cycling or benchmark all.
								Algorithm.BozoSort,
								Algorithm.BogoSort,
	};
	public static boolean  	invertIgnored  =  false	;	//inverts the ignored list so ignored algorithms become unignored and vice-versa
	
	
	//List
	public static int 		size =				1000		;	//list size
	public static int		lowerBound =		0			;	//lower bound for list values
	public static int		upperBound =		size		;	//upper bound for list values
	public static boolean	reversedList =		false		;	//if list is reversed
	public static boolean 	scrambledList =		true		;	//if list is scrambled
	public static float		shuffleRate =		0.00f		;	//amount of random shuffling made. While scramble scrambles every element in the list, shuffle shuffles random indexes, and is therefore useful to simulate a partially sorted list.
	public static boolean	preSortedList =		false		;	//if the list is pre-sorted before modifiers are added. Mostly useful for list types like cubic
	//- presets
	public static boolean	usePresetSize =		true		;	//if algorithm preset size is used
	public static boolean	usePresetSpeed =	true		;	//if algorithm preset speed is used
	public static boolean	presetAsMult =		true		;	//if the preset settings act as a multiplier (*preset/1000) or as a static value.
	public static boolean	upperBoundIsSize =	true		;	//if the upper bound is the size
	public static boolean	smallVisualPreset =	false		;	//if the preset used for visuals is the small preset. Otherwise, the big preset is used.
	
	//SampleSort
	public static int 		maxSampleSize =		100			;	//max amount of values shown in console of list when not visualizing or benchmarking.
	
	//Benchmark
	public static boolean 	benchmarkAll = 		true		;	//whether to benchmark all algorithms except the specified ignored or just the selected algorithm.
	public static int		benchmarksDone =	100			;	//amount of sorts each algorithm will complete when benchmarking all algorithms. 1 sort gives a simple list, >1 makes a dataset and calculates some basic descriptors for it.
	public static boolean	showRawData =		false		;	//if the raw runtimes are listed when benchmarking algorithm
	
	//Visualize
	public static float		speed =				1500f		;	//calculations made per second when visualizing. Note that all algorithms will be doing an equal amount of steps every frame, not an equal amount of sorting.
	public static boolean	resetVisual =		true		;	//if a new visual sort is made when finished
	public static int		resetVisualWait =	3000		;	//wait time in ms before new visualized sort is made
	public static boolean	showModifiers =		true		;	//if list modifiers are shown at start of sort a new sort.
	public static float		modifierSpeed =		75f			;	//speed of modifiers. Speed is proportional to the list size.
	public static int		modifierWait =		500			;	//wait time in ms after modifying before sorting starts.
	public static boolean 	cycle =				true		;	//cycles between sorting algorithms when using visual, starting with the specified algorithm. Ignores specified ignored algorithms. 
	public static float		completionSpeed =	18f			;	//how fast the completed sort animation moves
	public static int		width =				1100		;	//width of visualized frame
	public static int		height =			700			;	//height of visualized frame
	public static boolean	showInfo =			true		;	//if info is shown in upper left corner
	public static boolean	simpleInfo =		false		;	//if turned on, only the algorithm is shown. Otherwise, a detailed info box is shown.
	public static float		fontSize =			17.5f		;	//font size for info box
	//- BarGraph
	public static float		barMax =			0.88f		;	//the maximum amount of the screen height a column can be in height
	public static boolean	gradient =			true		;	//if the list is shown as a gradient
	public static float		gradientMin =		0.45f		;	//lowest value of gradient
	//- ColorCircle
	public static float		circleRadius =		0.85f		;	//radius of cirlce compared to screen
	public static boolean	showPointers =		true		;	//if pointers are shown. Pointers are placed at the location of sound indexes.
	public static int		maxSlices =			2000		;	//max amount of slices
	public static float		pointerColorBlend = 0.0f		;	//blend of pointer color with algorithm index color.
	public static float		pointerSize =		0.065f		;	//size of pointers
	public static float		pointerWidth =		0.045f		;	//width of pointers
	public static float		pointerDistance =	0.04f		;	//distance of pointer
	//- ScatterPlot
	public static boolean	pointAlgorithmColor=false		;	//if points are colored as in algorithm
	public static boolean	selectedColors =	true		;	//if selected indexes are marked
	public static float		pointSize =			3.5f		;	//size of points.
	public static float		minPointSize =		0.5f		;	//min point size
	public static int		pointScaleBaseline= 1000		;	//basis point amount for scaling size of dots
	public static float		pointScaling =		0.97f		;	//how size of points scale according to list size.
	public static int		maxPoints =			10000		;	//max amount of points shown.
	public static float		pointMax =			0.995f		;	//max y pos of points relative to screen height
	public static float		pointMin =			0.005f		;	//max y pos of points relative to screen height
	//- DisparityDots
	public static boolean	showSelectedDots =	true		;	//if selected indexes are shown
	public static float		dotSize =			4.2f		;	//size of dots.
	public static float		minDotSize =		1.0f		;	//min dot size
	public static int		dotScaleBaseline=	1000		;	//basis dot amount for scaling size of dots
	public static float		dotScaling =		0.98f		;	//how size of dots scale according to list size.
	public static int		maxDots =			10000		;	//max amount of dots shown
	public static float		dotRadius =			0.85f		;	//radius of dot circle
	
	//audio
	public static boolean	sortAudio =			false		;	//if audio is added to visual sorts
	public static boolean	roundToNote =		true		;	//if frequencies are rounded to the nearest note greater than it in a musical scale
	public static boolean	diatonicScale =		true		;	//if the scale used for sounds is diatonic. Otherwise, a pentatonic scale is used.
	public static float		audioTickRate =		0.5f		;	//tick rate of sound - amount of audio ticks pr. regular tick
	public static float		minHz =				300			;	//lowest possible frequency
	public static float		maxHz =				1800		;	//highest possible frequency
	public static double	freqScaling =		1.2d		;	//affects the interpolation of sound frequencies
	public static float		audioFadeIn =		22f			;	//amount of time fade in takes
	public static float		audioTime =			0f			;	//amount of time sounds play at full vol. Total duration is audio fade in + audio time + audio fade out.
	public static float		audioFadeOut =		33f			;	//amount of time fade out takes
	public static float		audioFadeInCurve =	1.25f		;	//curvature of fade in
	public static float		audioFadeOutCurve =	1.15f		;	//curvature of fade out
	public static double	audioTimeTolerance=	1.85d		;	//affects the time until termination of sounds
	public static double	volume =			0.2d		;	//volume of sounds
	public static int		audioMixer	=		4			;	//index of audio mixer. Set to -1 to get a list of mixers.
	
	
	public static Map<Algorithm,SortingAlgorithm> algorithms = new HashMap<Algorithm,SortingAlgorithm>(){private static final long serialVersionUID = 1L;{
		put(Algorithm.BubbleSort,new BubbleSort());
		put(Algorithm.SelectionSort,new SelectionSort());
		put(Algorithm.InsertionSort,new InsertionSort());
		put(Algorithm.BogoSort,new BogoSort());
		put(Algorithm.CombSort,new CombSort());
		put(Algorithm.QuickSort,new QuickSort());
		put(Algorithm.GnomeSort,new GnomeSort());
		put(Algorithm.CocktailShakerSort,new CocktailShakerSort());
		put(Algorithm.MergeSort,new MergeSort());
		put(Algorithm.RadixSortLSD,new RadixSortLSD());
		put(Algorithm.RadixSortMSD,new RadixSortMSD());
		put(Algorithm.ShellSort,new ShellSort());
		put(Algorithm.HeapSort,new HeapSort());
		put(Algorithm.OddEvenSort,new OddEvenSort());
		put(Algorithm.BozoSort,new BozoSort());
		put(Algorithm.BitonicSort,new BitonicSort());
		put(Algorithm.CountingSort,new CountingSort());
		put(Algorithm.GravitySort,new GravitySort());
		put(Algorithm.SmoothSort,new SmoothSort());
		put(Algorithm.RadixSortLSDInPlace,new RadixSortLSDInPlace());
	}};
	public static int currentSize;
	public static void main(String args[]) {
		Algorithm[] enums = Algorithm.values();
		lowerBound = Integer.max(0,lowerBound);
		//if you want to change the variables in the sorting algorithms, you can do it here if you dont want to search around in the classes.
		//CombSort.shrinkFactor = 1.3f;
		//RadixSortLSD.nBase = 2;
		//RadixSortLSD.singleSound = false;
		//RadixSortMSD.nBase = 2;
		//RadixSortMSD.singleSound = false;
		//GravitySort.showBeadsPos = false;
		
		//if you want to change the variables for list types, you can do it here instead of searching around each function.
		//Lists.QLaVal = 1; //quadratic list function a coefficient
		//Lists.QLbVal = 0; //quadratic list function b coefficient
		//Lists.QLfrom =-1; //quadratic list function from x value
		//Lists.QLto =   1; //quadratic list function to x value
		//Lists.ELfrom = 0.0; //exponential list function from x value
		//Lists.ELto =   2.5; //exponential list function to x value
		//Lists.LLlow =  0.5; //logaritmic list function from x value (natural log)
		//Lists.LLhigh = 2.5; //logaritmic list function to x value (natural log)
		//Lists.CLaVal = 1; //cubic list function a coefficient
		//Lists.CLbVal =  2; //cubic list function b coefficient
		//Lists.CLcVal = 0; //cubic list function c coefficient
		//Lists.CLfrom = 0; //cubic list function from x value
		//Lists.CLto =   0.6; //cubic list function to x value
		//Lists.PLpower = 5; //power list function power
		//Lists.PLfrom = -1; //power list function from x value
		//Lists.PLto =    1; //power list function to x value
						
		
		//invert the ignored list if specified
		if (invertIgnored) {
			ArrayList<Algorithm> temp = new ArrayList<Algorithm>();
			for (int i=0;i<enums.length;i++) temp.add(enums[i]);	
			for (int i=0;i<ignored.length;i++) temp.remove(ignored[i]);
			
			//update ignored list
			ignored = temp.toArray(new Algorithm[temp.size()]);
		}
		
		//print mixers if specified
		if (sortAudio && audioMixer<0) {
			System.out.println("FOR AUDIO, SET audioMixer TO THE NUMBER OF THE CURRENTLY USED AUDIO DEVICE MINUS ONE.\n");
			JavaSoundAudioIO.printMixerInfo();
			System.exit(0);
		}
		
		//print available sorting algorithms
		System.out.println("Sorting algorithms ("+enums.length+" available):");
		String temp = enums[0].toString();
		for (int i=1;i<enums.length;i++) temp+=",  "+enums[i].toString();
		System.out.println(temp);
		
		
		//get initial algorithm
		SortingAlgorithm algorithm = algorithms.get(sortingAlgorithm);
		if (algorithm==null) {
			System.out.print("\nCannot sort list, null map call. Update the map, damnit!");
			System.exit(0);
		}
		
		//print basic info
		System.out.print("\nCurrently selected algorithm: "+sortingAlgorithm.toString()+"\n"
							+"Selected mode: "+programMode.toString()+"\n"
							+(programMode==Mode.Visualize?"Selected visualization: "+visualization.toString()+"\n":"")
							+"Baseline list size: "+size+"\n"
							+(programMode==Mode.Visualize?"Baseline speed: "+speed+"\n":"")
							+"Preset settings: "+(programMode==Mode.Visualize?(smallVisualPreset? "small visual preset, ":"big visual preset, "):"regular preset, ")+(usePresetSize?"preset size, ":"set size, ")+(programMode==Mode.Visualize?(usePresetSpeed?"preset speed, ":"set speed, "):"")+(presetAsMult?"preset acts as multiplier":"preset acts as constant")+"\n"
							+"List type: "+listType.toString()+"\n"
							+"List modifiers: "+(reversedList?"Reversed, ":"")+(scrambledList?"Scrambled, ":"")+"ShuffleRate = "+(double)(10000*shuffleRate)/100+"%\n\n");
		
		
		//visualized sorting
		switch (programMode) {
		case Visualize:
			if (sortAudio) {
				//get mixer
				JavaSoundAudioIO jsaIO = new JavaSoundAudioIO();
				jsaIO.selectMixer(audioMixer);
				//initialize
				AudioManager.ac = new AudioContext(jsaIO);
				AudioManager.ac.start();
			}
			
			//visualized sort
			int[] visList = showModifiers? Lists.GetListType(Lists.GetSize(sortingAlgorithm),listType):Lists.GetList(Lists.GetSize(sortingAlgorithm));
			algorithm.Import(visList);				//initialize sorting algorithm
			new SortingAlgorithmGUI(algorithm);		//initialize gui/visualizer
			break;
			
		//benchmark	
		case Benchmark:
			//get algorithms to benchmark
			ArrayList<Algorithm> Algorithms = new ArrayList<Algorithm>();
			if (benchmarkAll) {
				for (int i=0;i<enums.length;i++) Algorithms.add(enums[i]);	//add all non-ignored algorithms if specified
				for (int i=0;i<ignored.length;i++) Algorithms.remove(ignored[i]);
			}
			else Algorithms.add(sortingAlgorithm);							//else just add the selected algorithm
			
			//perform full benchmark (perform benchmark, format results, print)
			Benchmarking.FullBenchmark(Algorithms,benchmarksDone);
			break;
		
		//sample sort
		case SampleSort:
			int[] list = Lists.GetList(Lists.GetSize(sortingAlgorithm));
			
			//peform sample sort (print list sample, sort, print sorted list sample and sorting time)
			SampleSort(algorithm,list,maxSampleSize);
			break;
		}
	}
	
	//for visual cycling
	public static boolean ignoredContains(Algorithm algorithm) {
		for (int i=0;i<ignored.length;i++) if (ignored[i]==algorithm) return true;
		return false;
	}
	
	
	//#######################################################################################################################################################################################
	//	Sample sort
	
	//sort list and print sample of list
	public static void SampleSort(SortingAlgorithm Algorithm, int[] list, int sampleSize) {
		//print list. We print a specified amount of values in the list (aka a sample of the list) (roughly evenly spread) so we can more or less confirm that the sorting algorithm worked without printing the entire list.
		int sampleLength = Integer.min(sampleSize,list.length);
		String sampleList = list[0]+"";
		for (int i=1;i<sampleLength;i++) sampleList+=", "+list[(i*list.length)/sampleLength];
		System.out.print("List size: "+list.length
							+"\nList sample size: "+sampleLength+"  ("+(double)sampleLength*100d/list.length+"% of total list)"
							+"\n\n\nUNSORTED LIST SAMPLE:\n"
							+sampleList);
		
		//sort
		long startTime = System.nanoTime(), endTime;	
		Algorithm.Sort(list);
		endTime = System.nanoTime();
		
		//print list again along with sorting time
		sampleLength = Integer.min(sampleSize,list.length);
		sampleList = list[0]+"";
		for (int i=1;i<sampleLength;i++) sampleList+=", "+list[(i*list.length)/sampleLength];
		System.out.print("\n\n\nSORTED LIST SAMPLE:\n"
						  +sampleList
						  +"\n\n\nSorting took "+(double)(endTime-startTime)/1000000+"ms.");
	}
}