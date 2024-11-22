//algorithm types
public enum Algorithm {
	BubbleSort			(4096	,256,1300f	,1024,30000f),		//technically a variation, as it only checks sorting length, not if elements have been swapped. This is because we will almost always only be done when the sorting length is close to 0, so checking for swappings is in practice useless, since the sorting length (which we have to check anyway) will pretty much always indicate just as well as a swap boolean.
	SelectionSort		(8192	,128,500f	,1024,30000f),		
	InsertionSort		(8192	,128,180f	,1024,15000f),		//performance version is technically a variation as it places the minimum element first in order to remove a comparison
	BogoSort			(8		,5,10f		,7,1000f),			//funy
	CombSort			(65536	,256,135f	,8192,15000f),		//shrink factor is set to 1.3, the thus far most efficient shrink factor found. 
	QuickSort			(65536	,512,420f	,8192,10000f),		//uses L-R pointers (Hoarse's partitioning scheme)
	GnomeSort			(4096	,128,350f	,1024,32000f),		
	CocktailShakerSort	(4096	,256,1150f	,1024,22000f),		
	MergeSort			(65536	,512,350f	,8192,10000f),		//visual version copies over from work array to, well.. visualize changes
	RadixSortLSD		(131072	,512,300f	,8192,7000f),		//nBase=8 seems best for performance, nBase=2 or nBase=3 seems best for visuals. Set to 2 by default. (note: uses a 2^n base to use bit shifting and bitwise operators for higher performance)
	RadixSortMSD		(65536	,512,380f	,8192,8500f),		//^
	ShellSort			(65536	,512,400f	,8192,10000f),		//uses marcin ciuras gap sequence, then 2^k - 1
	HeapSort			(65536	,512,350f	,8192,5500f),		
	OddEvenSort			(4096	,256,1350f	,1024,25000f),		
	BozoSort			(9		,5,10f		,7,1000f),			//funy sequal
	BitonicSort			(32768	,512,280f	,8192,18000f),		
	CountingSort		(131072	,512,130f	,16384,9000f),		
	GravitySort			(1024	,256,4000f	,1024,100000f),		//visualization is very different from performance version because the performance version visualized is really really boring. The performance version counts in a row-by-row basis, while the visual version individually pushes each bead from top to bottom
	SmoothSort			(32768	,256,130f	,8192,8000f),		
	RadixSortLSDInPlace	(2048	,512,100f	,4096,700f);		
	
	
	int size, visualSizeLow, visualSizeHigh;
	float visualSpeedLow, visualSpeedHigh;
	Algorithm(int size, int visualSizeLow, float visualSpeedLow, int visualSizeHigh, float visualSpeedHigh) {
		this.size = size;
		this.visualSizeLow = visualSizeLow;
		this.visualSizeHigh = visualSizeHigh;
		this.visualSpeedLow = visualSpeedLow;
		this.visualSpeedHigh = visualSpeedHigh;
	}
}
