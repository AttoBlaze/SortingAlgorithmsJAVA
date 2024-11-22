import java.awt.Color;

public class GnomeSort implements SortingAlgorithm {
	public void Sort(int[] list) {
		int pos = 1;
		while (pos<list.length) {
			//swap neighbors if not in order and move backwards
			if (pos!=0 && list[pos]<list[pos-1]) {
				SortingAlgorithm.Swap(list,pos,pos-1);
				pos--;
			} 
			//otherwise move forward
			else {
				pos++;
			}
		}
	}
	
	private static int[] list;
	private static int checking, maxCheck;
	public void Import(int[] List) {
		list = List;
	}
	public void VisualizedSort() {
		Runnable r = new Runnable() {
			public void run() {
				SortingAlgorithmGUI.UpdateMarkerSetup();
				checking = 1;
				maxCheck = 1;
				while (checking<list.length) {
					//swap neighbors if not in order and move backwards
					if (checking!=0 && afv.compare(list,checking,checking-1)) {
						afv.swapMain(list,checking,checking-1);
						checking--;
					} 
					//otherwise move forward
					else {
						checking++;
						if (checking>maxCheck) maxCheck = checking; //for visuals
					}
					SortingAlgorithmGUI.UpdateMarker();
				}
				SortingAlgorithmGUI.Finish();
			}
		};
		new Thread(r).start();
	}
	
	public Color GetColor(int index) {
		if (index==checking) return Color.red;
		if (index<=maxCheck) return SortingAlgorithmGUI.secondaryColor;
		return SortingAlgorithmGUI.primaryColor;
	}
	
	public int[] getList() {
		return list;
	}
	
	public int[] GetSelectedIndexes() {
		return new int[] {checking};
	}
}
