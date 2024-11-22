import java.awt.Color;

public class BogoSort implements SortingAlgorithm {
	public void Sort(int[] list) {
		int check=-1;
		while(++check<list.length-1) {
			//if any neighbors are not in order, scramble the entire list, speed be damned.
			if (list[check]>list[check+1]) {
				//swap each index with a random index
				for (int i=0;i<list.length;i++) {
					int random = (int)(Math.random()*list.length);
					SortingAlgorithm.Swap(list,i,random);
				}
				check=-1;
			}
		}
	}
	
	
	private static int[] list;
	private static int currentCheck;
	public void Import(int[] List) {
		list = List;
	}
	public void VisualizedSort() {
		Runnable r = new Runnable() {
			public void run() {
				SortingAlgorithmGUI.UpdateMarkerSetup();
				currentCheck=-1;
				while(++currentCheck<list.length-1) {
					//if any neighbors are not in order, scramble the entire list, speed be damned.
					if (afv.compare(list,currentCheck+1,currentCheck)) {
						//swap each index with a random index
						for (int i=0;i<list.length;i++) {
							int random = (int)(Math.random()*list.length);
							afv.swapMain(list,i,random);
						}
						currentCheck=-1;
					}
					SortingAlgorithmGUI.UpdateMarker();
				}
				SortingAlgorithmGUI.Finish();
			}
		};
		new Thread(r).start();
	}
	
	
	public Color GetColor(int index) {
		if (index==currentCheck) return Color.red;
		if (index<currentCheck) return SortingAlgorithmGUI.secondaryColor;
		return SortingAlgorithmGUI.primaryColor;
	}
	
	public int[] getList() {
		return list;
	}
	
	public int[] GetSelectedIndexes() {
		return new int[] {currentCheck};
	}
}

