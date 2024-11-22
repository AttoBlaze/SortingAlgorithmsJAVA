import java.awt.Color;
import java.util.BitSet;

public class GravitySort implements SortingAlgorithm {
	public void Sort(int[] list) {
		//we first find the highest value in the list
		int max = list[0], min = list[0];
		for (int i=1;i<list.length;i++) {
			if (list[i]<min) min = list[i];
			else if (list[i]>max) max = list[i];
		}
		
		//create bead boolean array
		BitSet[] beads = new BitSet[list.length];
		for (int i=0;i<list.length;i++) {
			beads[i] = new BitSet(max-min+1);
			for (int j=0;j<list[i]-min+1;j++) beads[i].set(j,true);
		}
		
		
		//slide beads
		for (int i=0;i<max-min+1;i++) {
			//get sum
			int sum = 0;
			for (int j=0;j<list.length;j++) {
				if (beads[j].get(i)) {
					sum++;
					beads[j].set(i,false);
				}
			}
			
			//move
			for (int j=list.length-1;j>=list.length-sum;j--) {
				list[j] = i-min;
			}
		}
	}
	
	private static int[] list;
	private static int currentPos, beadsPos;
	public void Import(int[] List) {
		list = List;
	}
	public void VisualizedSort() {
		Runnable r = new Runnable() {
			public void run() {
				SortingAlgorithmGUI.UpdateMarkerSetup();
				beadsPos = list.length;
				
				//we first find the highest value in the list
				int max = afv.get(list,0), min = afv.get(list,0);
				for (currentPos=1;currentPos<list.length;currentPos++) {
					if (afv.compareToValue(list,currentPos,min,false)) min = afv.get(list,currentPos);
					else if (afv.compareToValue(list,currentPos,max,true)) max = afv.get(list,currentPos);
					SortingAlgorithmGUI.UpdateMarker();
				}
				
				//create bead boolean array
				BitSet[] beads = new BitSet[list.length];
				for (int i=0;i<list.length;i++) {
					beads[i] = new BitSet(max-min+1);
					SortingAlgorithmGUI.arrayAccesses++;
					SortingAlgorithmGUI.writesToAux++;
					for (int j=0;j<list[i]-min+1;j++) {
						beads[i].set(j,true);
						SortingAlgorithmGUI.arrayAccesses++;
						SortingAlgorithmGUI.writesToAux++;
					}
				}
				
				
				//slide beads
				for (int i=max-min+1;i>=0;i--) {
					//get sum in row
					int sum = 0;
					for (int j=0;j<list.length;j++) {
						if (beads[j].get(i)) {
							sum++;
						}
						SortingAlgorithmGUI.arrayAccesses++;
					}
					
					//slide beads in row
					beadsPos = list.length-sum;	//for visuals
					for (int j=0;j<list.length;j++) {
						beads[j].set(i,j>beadsPos);
						SortingAlgorithmGUI.arrayAccesses++;
						SortingAlgorithmGUI.writesToAux++;
					}
					
					//update list
					for (currentPos=0;currentPos<list.length;currentPos++) {
						//get sum of beads in row
						sum = 0;
						for (int k=0;k<max-min+1;k++) {
							if (beads[currentPos].get(k)) sum++;
							SortingAlgorithmGUI.arrayAccesses++;
						}
						afv.setMain(list,currentPos,sum);
						SortingAlgorithmGUI.UpdateMarker();
					}
				}
				SortingAlgorithmGUI.Finish();
			}
		};
		new Thread(r).start();
	}
	
	public static boolean showBeadsPos = false;
	public Color GetColor(int index) {
		if (index==currentPos) return Color.red;
		if (showBeadsPos && index==beadsPos) return Color.red;
		return SortingAlgorithmGUI.primaryColor;
	}
	
	public int[] getList() {
		return list;
	}
	
	public int[] GetSelectedIndexes() {
		return new int[] {currentPos};
	}
}
