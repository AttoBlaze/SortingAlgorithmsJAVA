import java.awt.Color;
import java.util.Arrays;

public class HeapSort implements SortingAlgorithm {
	private void maxHeapify(int[] list, int length, int pos) {
		while (true) {
			int largest = pos,
				left = 2*pos+1,
				right = 2*pos+2;
			
			//left side
			if (left<length && list[largest]<list[left]) {
				largest = left;
			}
			
			//right side
			if (right<length && list[largest]<list[right]) {
				largest = right;
			}
			
			//if the largest needs to be switched
			if (largest!=pos) {
				//swap
				SortingAlgorithm.Swap(list,largest,pos);
				
				//heapify the rest
				pos = largest;
			} else break;
		}
	}
	public void Sort(int[] list) {
		int length = list.length;
		
		//make list into heap
		for (int i=list.length/2-1;i>=0;i--) {
			maxHeapify(list,length,i);
		}
		
		//continually place top element first
		for (int i=length-1;i>=0;i--) {
			//swap top element and end of list
			int temp = list[0];
			list[0] = list[i];
			list[i] = temp;
			
			//reheap
			maxHeapify(list,i,0);
		}
	}
	
	
	private static int[] list;
	private static int sortingLength, currentPos;
	public void Import(int[] List) {
		list = List;
	}
	public void VisualizedSort() {
		Runnable r = new Runnable() {
			public void run() {
				SortingAlgorithmGUI.UpdateMarkerSetup();
				sortingLength = list.length;
				
				//make list into heap
				for (int i=list.length/2-1;i>=0;i--) {
					currentPos = i; //for visuals
					SortingAlgorithmGUI.UpdateMarker();
					maxHeapify(list,sortingLength,i);
				}
				
				//continually place top element first
				for (sortingLength-=1;sortingLength>=0;sortingLength--) {
					currentPos = sortingLength; //for visuals
					
					//swap top element and end of list
					afv.swapMain(list,0,sortingLength);
					SortingAlgorithmGUI.UpdateMarker();
					
					//reheap
					maxHeapify(list,sortingLength,0);
				}
				SortingAlgorithmGUI.Finish();
			}
			
			private static void maxHeapify(int[] list, int length, int pos) {
				currentPos = pos;
				while (true) {					
					int largest = currentPos,
						left = 2*currentPos+1,
						right = 2*currentPos+2;
					
					//left side
					if (left<length && afv.compare(list,largest,left)) {
						largest = left;
					}
					
					//right side
					if (right<length && afv.compare(list,largest,right)) {
						largest = right;
					}
					
					//if the largest needs to be switched
					if (largest!=currentPos) {
						//swap
						afv.swapMain(list,largest,currentPos);
						
						//heapify the rest
						currentPos = largest;
						SortingAlgorithmGUI.UpdateMarker();
					} else break;
				}
			}
		};
		new Thread(r).start();
	}
	
	public static Color[] HeapColors = Arrays.asList(Color.cyan,Color.green,Color.magenta,Color.pink).toArray(new Color[4]);
	public Color GetColor(int index) {
		if (index==currentPos) return Color.red;
		if (index==sortingLength)  return Color.red;
		if (index>sortingLength) return SortingAlgorithmGUI.secondaryColor;
		int temp = 0;
		do temp++; while ((1<<temp)-1<=index);
		return HeapColors[temp%HeapColors.length];
	}
	
	public int[] getList() {
		return list;
	}
	
	public int[] GetSelectedIndexes() {
		return new int[] {currentPos};
	}
}
	