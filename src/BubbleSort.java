import java.awt.Color;

public class BubbleSort implements SortingAlgorithm {
	public void Sort(int[] list) {
		int sortingLength = list.length-1;
		//swap neighboring values who are not in order
		while (sortingLength>0) {
			int newSortLength = 0;
			//go through list
			for (int i=0;i<sortingLength;i++) {
				//swap unordered neighboring indexes
				if (list[i]>list[i+1]) {
					SortingAlgorithm.Swap(list,i,i+1);
					newSortLength = i;
				}
			}
			sortingLength = newSortLength;
		}
	}
	
	private static int currentPos, sortingLength;
	private static int[] list;
	public void Import(int[] List) {
		list = List;
	}
	public void VisualizedSort() {
		Runnable r = new Runnable() {
			public void run() {
				SortingAlgorithmGUI.UpdateMarkerSetup();
				sortingLength = list.length-1;
				//swap neighboring values who are not in order
				while (sortingLength>0) {
					int newSortLength = 0;
					//go through list
					for (currentPos=0;currentPos<sortingLength;currentPos++) {
						//swap unordered neighboring indexes
						if (afv.compare(list,currentPos+1,currentPos)) {
							afv.swapMain(list,currentPos,currentPos+1);
							newSortLength = currentPos;
						}
						SortingAlgorithmGUI.UpdateMarker();
					}
					sortingLength = newSortLength;
				}
				SortingAlgorithmGUI.Finish();
			}
		};
		new Thread(r).start();
	}
	
	public Color GetColor(int index) {
		if (index==currentPos || index==currentPos+1) return Color.red;
		if (index>sortingLength) return SortingAlgorithmGUI.secondaryColor;
		return SortingAlgorithmGUI.primaryColor;
	}
	
	public int[] getList() {
		return list;
	}
	
	public int[] GetSelectedIndexes() {
		return new int[] {
			currentPos,
			currentPos+1};
	}
}
