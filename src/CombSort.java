import java.awt.Color;

public class CombSort implements SortingAlgorithm {
	public static float shrinkFactor = 1.3f;
	
	public void Sort(int[] list) {
		boolean swappedValues=true;
		int gap = list.length;
		
		while (swappedValues) {
			//get gap
			int temp = (int)(gap/shrinkFactor);
			if (temp<1) {
				gap=1;
				swappedValues=false;
			}
			else if (temp==9 || temp==10) gap=11; //rule of 11
			else gap=temp;
			//go through list and swap values
			for (int i=0;i+gap<list.length;i++) {
				if (list[i]>list[i+gap]) {
					SortingAlgorithm.Swap(list,i,i+gap);
					swappedValues=true;
				}
			}
		}
		
	}
	
	private static int[] list;
	private static int currentCheck, gapSize;
	public void Import(int[] List) {
		list = List;
	}
	public void VisualizedSort() {
		Runnable r = new Runnable() {
			public void run() {
				SortingAlgorithmGUI.UpdateMarkerSetup();
				boolean swappedValues=true;
				gapSize = list.length;
				
				while (swappedValues) {
					//get gap
					int temp = (int)(gapSize/shrinkFactor);
					if (temp<1) {
						gapSize=1; 
						swappedValues=false;
					} else if (temp==9 || temp==10) gapSize=11; //rule of 11
					else gapSize=temp;
					//go through list and swap values
					for (currentCheck=0;currentCheck+gapSize<list.length;currentCheck++) {
						if (afv.compare(list,currentCheck+gapSize,currentCheck)) {
							afv.swapMain(list,currentCheck,currentCheck+gapSize);
							swappedValues=true;
						}
						SortingAlgorithmGUI.UpdateMarker();
					}
				}
				SortingAlgorithmGUI.Finish();
			}
		};
		new Thread(r).start();
	}
	
	public Color GetColor(int index) {
		if (index==currentCheck || index==currentCheck+gapSize) return Color.red;
		if (index>currentCheck && index<currentCheck+gapSize) return Color.pink;
		if (index<currentCheck) return SortingAlgorithmGUI.secondaryColor;
		return SortingAlgorithmGUI.primaryColor;
	}
	
	public int[] getList() {
		return list;
	}
	
	public int[] GetSelectedIndexes() {
		return new int[] {
			currentCheck,
			currentCheck+gapSize};
	}
}
