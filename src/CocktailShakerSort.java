import java.awt.Color;

public class CocktailShakerSort implements SortingAlgorithm {
	public void Sort(int[] list) {
		int begin = 0, end = list.length-1;
		while (begin<=end) {
			int newBegin = begin, newEnd = end;
			
			//forwards
			for (int i=begin;i<end;i++) {
				if (list[i]>list[i+1]) {
					SortingAlgorithm.Swap(list,i,i+1);
					newEnd = i;
				}
			}
			
			end = newEnd;
			//backwards
			for (int i=end-1;i>=begin;i--) {
				if (list[i]>list[i+1]) {
					SortingAlgorithm.Swap(list,i,i+1);
					newBegin = i;
				}
			}
			begin = newBegin+1;
		}
	}
	
	private static int[] list;
	private static int currentPos, begin, end;
	public void Import(int[] List) {
		list = List;
	}
	public void VisualizedSort() {
		Runnable r = new Runnable() {
			public void run() {
				SortingAlgorithmGUI.UpdateMarkerSetup();
				begin = 0;
				end = list.length-1;
				while (begin<=end) {
					int newBegin = begin, newEnd = end;
					
					//forwards
					for (currentPos=begin;currentPos<end;currentPos++) {
						if (afv.compare(list,currentPos+1,currentPos)) {
							afv.swapMain(list,currentPos,currentPos+1);
							newEnd = currentPos;
						}
						SortingAlgorithmGUI.UpdateMarker();
					}
					
					end = newEnd;
					//backwards
					for (currentPos=end-1;currentPos>=begin;currentPos--) {
						if (afv.compare(list,currentPos+1,currentPos)) {
							afv.swapMain(list,currentPos,currentPos+1);
							newBegin = currentPos;
						}
						SortingAlgorithmGUI.UpdateMarker();
					}
					begin = newBegin+1;
				}
				SortingAlgorithmGUI.Finish();
			}
		};
		new Thread(r).start();
	}
	
	public Color GetColor(int index) {
		if (index==currentPos || index==currentPos+1) return Color.red;
		if (index<begin || index>end) return SortingAlgorithmGUI.secondaryColor;
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
