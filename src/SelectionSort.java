import java.awt.Color;

public class SelectionSort implements SortingAlgorithm {
	public void Sort(int[] list) {
		//go through list
		for (int i=0;i<list.length;i++) {
			//find minimum value index
			int minValuePos = i, minValue = list[minValuePos];
			for (int j=i;j<list.length;j++) {
				if (list[j]<minValue) {
					minValuePos = j;
					minValue = list[minValuePos];
				}
			}
			//swap minimum value index and current starting index
			SortingAlgorithm.Swap(list,minValuePos,i);
		}
	}
	
	
	private static int[] list;
	private static int currentCheck, currentMinPos, sortedLength, currentMin;
	public void Import(int[] List) {
		list = List;
	}
	public void VisualizedSort() {
		Runnable r = new Runnable() {
			public void run() {
				SortingAlgorithmGUI.UpdateMarkerSetup();
				//go through list
				for (sortedLength=0;sortedLength<list.length;sortedLength++) {
					//find minimum value index
					currentMinPos = sortedLength;
					currentMin = afv.get(list,currentMinPos);
					for (currentCheck=sortedLength;currentCheck<list.length;currentCheck++) {
						if (afv.compareToValue(list,currentCheck,currentMin,false)) {
							currentMinPos = currentCheck;
							currentMin = afv.get(list,currentMinPos);
						}
						SortingAlgorithmGUI.UpdateMarker();
					}
					//swap minimum value index and current starting index
					afv.swapMain(list,currentMinPos,sortedLength);
				}
				SortingAlgorithmGUI.Finish();
			}
		};
		new Thread(r).start();
	}
	
	public Color GetColor(int index) {
		if (index==currentCheck) return Color.red;
		if (index==currentMinPos) return Color.blue;
		if (index<sortedLength) return SortingAlgorithmGUI.secondaryColor;
		return SortingAlgorithmGUI.primaryColor;
	}
	
	public int[] getList() {
		return list;
	}
	
	public int[] GetSelectedIndexes() {
		return new int[] {
			currentCheck,
			currentMinPos};
	}
}
