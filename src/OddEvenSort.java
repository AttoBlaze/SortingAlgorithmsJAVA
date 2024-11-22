import java.awt.Color;

public class OddEvenSort implements SortingAlgorithm {
	public void Sort(int[] list) {
		boolean swapped = true, odd = true;
		while (swapped) {
			swapped = false;
			for (int i=odd?1:0;i<list.length-1;i+=2) {
				//if neighbors are not in order swap them
				if (list[i]>list[i+1]) {
					SortingAlgorithm.Swap(list,i,i+1);
					swapped = true;
				}
			}
			odd = !odd;
		}
	}
	
	private static int[] list;
	private static int currentPos;
	public void Import(int[] List) {
		list = List;
	}
	public void VisualizedSort() {
		Runnable r = new Runnable() {
			public void run() {
				SortingAlgorithmGUI.UpdateMarkerSetup();
				boolean swapped = true, odd = true;
				while (swapped) {
					swapped = false;
					for (currentPos=odd?1:0;currentPos<list.length-1;currentPos+=2) {
						//if odd neighbors are not in order swap them
						if (afv.compare(list,currentPos+1,currentPos)) {
							afv.swapMain(list,currentPos,currentPos+1);
							swapped = true;
						}
						SortingAlgorithmGUI.UpdateMarker();
					}
					odd = !odd;
				}
				SortingAlgorithmGUI.Finish();
			}
		};
		new Thread(r).start();
	}
	
	public Color GetColor(int index) {
		if (index==currentPos || index==currentPos+1) return Color.red;
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
