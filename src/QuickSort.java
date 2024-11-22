import java.awt.Color;

public class QuickSort implements SortingAlgorithm {
	public void Sort(int[] list) {
		//uses Hoare's partitioning scheme. Uses a median-of-3 pivot.
		QuickSortList(list,0,list.length-1);
	}
	
	private void QuickSortList(int[] list, int min, int max) {
		if (min>=max || min<0) return;
		
		//partion
		int partion = getPartion(list,min,max);
		
		//sort sublists
		QuickSortList(list,min,partion);
		QuickSortList(list,partion+1,max);
	}
	private int getPartion(int[] list, int min, int max) {
		//get pivot and left and right indexes
		
		int pivot = list[(min+max)>>1],
			leftIndex = min-1,
			rightIndex = max+1;
		
		while (true) {
			//move left index until its value is larger than the pivot
			do leftIndex++; while (list[leftIndex]<pivot);
			
			//move right index until its value is smaller than the pivot
			do rightIndex--; while (list[rightIndex]>pivot);
			
			//if the indexes have crossed, then return the rightward index as the partion.
			if (leftIndex>=rightIndex) return rightIndex;
			
			//otherwise swap the rightward and leftward index
			SortingAlgorithm.Swap(list,leftIndex,rightIndex);
		}
	}
	
	private static int[] list;
	private static int currentMax, currentMin, currentLeftIndex, currentRightIndex;
	public void Import(int[] List) {
		list = List;
	}
	public void VisualizedSort() {
		Runnable r = new Runnable() {
			public void run() {
				SortingAlgorithmGUI.UpdateMarkerSetup();
				QuickSortList(list,0,list.length-1);
				SortingAlgorithmGUI.Finish();
			}
			
			private static void QuickSortList(int[] list, int min, int max) {
				if (min>=max || min<0) return;
				
				//partion
				int partion = getPartion(list,min,max);
				
				//sort sublists
				QuickSortList(list,min,partion);
				QuickSortList(list,partion+1,max);
			}
			private static int getPartion(int[] list, int min, int max) {
				//get pivot and left and right indexes
				
				int pivot = list[(min+max)>>1];
				currentLeftIndex = min-1;
				currentRightIndex = max+1;
				currentMin = min;
				currentMax = max;
				
				while (true) {
					//move left index until its value is larger than the pivot
					do {
						SortingAlgorithmGUI.UpdateMarker();
						currentLeftIndex++;
					} while (afv.compareToValue(list,currentLeftIndex,pivot,false));
					
					//move right index until its value is smaller than the pivot
					do {
						SortingAlgorithmGUI.UpdateMarker();
						currentRightIndex--; 
					} while (afv.compareToValue(list,currentRightIndex,pivot,true));
					
					//if the indexes have crossed, then return the rightward index as the partion.
					if (currentLeftIndex>=currentRightIndex) return currentRightIndex;
					
					//otherwise swap the rightward and leftward index
					afv.swapMain(list,currentLeftIndex,currentRightIndex);
					SortingAlgorithmGUI.UpdateMarker();
				}
			}
		};
		new Thread(r).start();
	}
	
	public Color GetColor(int index) {
		if (index==currentMin || index==currentMax) return Color.red;
		if (index==currentLeftIndex || index==currentRightIndex) return Color.pink;
		if (index>currentMin && index<currentMax) return SortingAlgorithmGUI.primaryColor;
		return SortingAlgorithmGUI.secondaryColor;
	}
	
	public int[] getList() {
		return list;
	}
	
	public int[] GetSelectedIndexes() {
		return new int[] {
			currentRightIndex,
			currentLeftIndex};
	}
}
