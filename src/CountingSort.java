import java.awt.Color;

public class CountingSort implements SortingAlgorithm {
	public void Sort(int[] list) {
		//we first find the lowest and highest value in the list
		int min = list[0], max = list[0];
		for (int i=1;i<list.length;i++) {
			int temp = list[i];
			if (temp<min) min = temp;
			else if (temp>max) max = temp;
		}
		
		//next, count the frequency of each key
		int[] count = new int[max-min+1];
		for (int i=0;i<list.length;i++) {
			count[list[i]-min]++;
		}
		
		//finally, place values in the list based on the count.
		int pos = 0;
		for (int i=0;i<count.length;i++) {
			for (int j=0;j<count[i];j++) {
				list[pos++] = i+min;
			}
		}
		
	}
	
	private static int[] list;
	private static int min, max, currentPos;
	public void Import(int[] List) {
		list = List;
	}
	public void VisualizedSort() {
		Runnable r = new Runnable() {
			public void run() {
				SortingAlgorithmGUI.UpdateMarkerSetup();
				//we first find the lowest and highest value in the list
				min = afv.get(list,0); max = afv.get(list,0);
				for (currentPos=1;currentPos<list.length;currentPos++) {
					if (afv.compareToValue(list,currentPos,min,false)) min = afv.get(list,currentPos);
					else if (afv.compareToValue(list,currentPos,max,true)) max = afv.get(list,currentPos);
					SortingAlgorithmGUI.UpdateMarker();
				}
				
				//next, count the frequency of each key
				int[] count = new int[max-min+1];
				for (currentPos=0;currentPos<list.length;currentPos++) { 
					int pos = afv.get(list,currentPos)-min;
					afv.incrementAux(count,pos,1);
					SortingAlgorithmGUI.UpdateMarker();
				}
				
				//finally, place values in the list based on the count.
				int pos = 0;
				for (currentPos=0;currentPos<count.length;currentPos++) {
					for (int j=0;j<afv.get(count,currentPos);j++) {
						afv.setMain(list,pos++,currentPos+min);
						SortingAlgorithmGUI.UpdateMarker();
					}
				}
				SortingAlgorithmGUI.Finish();
			}
		};
		new Thread(r).start();
	}
	
	public Color GetColor(int index) {
		if (index==currentPos) return Color.red;
		return SortingAlgorithmGUI.primaryColor;
	}
	
	public int[] getList() {
		return list;
	}
	
	public int[] GetSelectedIndexes() {
		return new int[] {currentPos};
	}
}
