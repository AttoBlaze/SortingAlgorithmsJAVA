import java.awt.Color;

public class BozoSort implements SortingAlgorithm {
	public void Sort(int[] list) {
		for (int i=0;i<list.length-1;i++) {
			//if the list is unsorted swap to random elements and check again
			if (list[i]>list[i+1]) {
				//get random index
				int randomIndex1, randomIndex2;
				do {
					randomIndex1 = (int)(Math.random()*list.length);
					randomIndex2 = (int)(Math.random()*list.length);
				} while (randomIndex1==randomIndex2);
				
				//swap
				SortingAlgorithm.Swap(list,randomIndex1,randomIndex2);
				
				//start over (-1 because of ++)
				i=-1;
			}
		}
	}
	
	private static int[] list;
	private static int currentPos, swapIndex1, swapIndex2;
	public void Import(int[] List) {
		list = List;
	}
	public void VisualizedSort() {
		Runnable r = new Runnable() {
			public void run() {
				SortingAlgorithmGUI.UpdateMarkerSetup();
				for (currentPos=0;currentPos<list.length-1;currentPos++) {
					//if the list is unsorted swap to random elements and check again
					if (afv.compare(list,currentPos+1,currentPos)) {
						//get random index
						do {
							swapIndex1 = (int)(Math.random()*list.length);
							swapIndex2 = (int)(Math.random()*list.length);
						} while (swapIndex1==swapIndex2);
						
						//swap
						afv.swapMain(list,swapIndex1,swapIndex2);
						SortingAlgorithmGUI.UpdateMarker();
						
						//start over (-1 because of ++)
						currentPos=-1;
						swapIndex1=swapIndex2;	//for visuals
					}
					SortingAlgorithmGUI.UpdateMarker();
				}
				SortingAlgorithmGUI.Finish();
			}
		};
		new Thread(r).start();
	}
	
	public Color GetColor(int index) {
		if (swapIndex1!=swapIndex2) {
			if (index==swapIndex1) return Color.blue;
			if (index==swapIndex2) return Color.blue.darker();
		}
		if (index==currentPos) return Color.red; 
		if (index<currentPos) return SortingAlgorithmGUI.secondaryColor;
		return SortingAlgorithmGUI.primaryColor;
	}
	
	public int[] getList() {
		return list;
	}
	
	public int[] GetSelectedIndexes() {
		if (swapIndex1!=swapIndex2) {
			return new int[] {
				swapIndex1,
				swapIndex2};
		}
		return new int[] {currentPos};
	}
}
