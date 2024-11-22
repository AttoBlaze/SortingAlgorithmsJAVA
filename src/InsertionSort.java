import java.awt.Color;

public class InsertionSort implements SortingAlgorithm {
	public void Sort(int[] list) {
		//NOTE: this is actually a variation of insertionsort, but it is literally only different by swapping a single value, and speeds up everything by like 2x. So im still going to call it insertionsort, fite me.
		//To make sorting faster, we first find the smallest element in the list and place it first. We do this in order to remove a comparison in the while loop.
		int minPos=0, minVal=list[0];
		for (int i=0;i<list.length;i++) {
			if (list[i]<minVal) {
				minPos = i;
				minVal = list[i];
			}
		}
		list[minPos] = list[0];
		list[0] = minVal;
		
		
		//go through list
		for(int i=1;i<list.length;i++) {
			//get current value
			int value = list[i], track=i-1, temp;
			//move value down list until it is in order
			while (value<(temp=list[track])) {
				//move values if not in order
				list[track+1] = temp;
				track--;
			}
			list[track+1] = value;
		}
	}
	
	
	private static int[] list;
	private static int sortedLength, currentCheck;
	public void Import(int[] List) {
		list = List;
	}
	public void VisualizedSort() {
		Runnable r = new Runnable() {
			public void run() {
				SortingAlgorithmGUI.UpdateMarkerSetup();
				//go through list
				for(sortedLength=1;sortedLength<list.length;sortedLength++) {
					//get current value
					int value = afv.get(list,sortedLength), temp;
					currentCheck = sortedLength-1;
					//move value down list until it is in order
					while (currentCheck>=0 && value<(temp=afv.get(list,currentCheck))) {
						//move values if not in order
						afv.setMain(list,currentCheck+1,temp);
						currentCheck--;
						SortingAlgorithmGUI.UpdateMarker();
					}
					afv.setMain(list,currentCheck+1,value);
				}
				SortingAlgorithmGUI.Finish();
			}
		};
		new Thread(r).start();
	}
	
	public Color GetColor(int index) {
		if (index==currentCheck) return Color.red;
		if (index<=sortedLength) return SortingAlgorithmGUI.secondaryColor;
		return SortingAlgorithmGUI.primaryColor;
	}
	
	public int[] getList() {
		return list;
	}
	
	public int[] GetSelectedIndexes() {
		return new int[] {currentCheck};
	}
}