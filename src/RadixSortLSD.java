import java.awt.Color;
import java.util.ArrayList;

public class RadixSortLSD implements SortingAlgorithm {
	//2^n base used for digits. We use a 2^n base as it means we can use bit shifting and logical and operators as opposed to the much slower modulus and divison.
	public static int nBase = 2;
	private static int shiftAmount, modAmount, maxShift;
	
	public void Sort(int[] list) {
		//get variables
		modAmount = (int)Math.pow(2,nBase)-1;
		shiftAmount = nBase;
		
		//get largest element so we can know the max amount of shifting to do
		int max = list[0];
		for (int i=0;i<list.length;i++) {
			if (max<list[i]) max=list[i];
		}
		
		//get the max amount of shifting
		maxShift=0;
		do maxShift+=shiftAmount; while ((max=max>>shiftAmount)>0);
		
		//sort for each digit
		for (int shift=0;shift<maxShift;shift+=shiftAmount) {
			//intialize
			int[] workArray = list.clone();
			int[] count = new int[modAmount+1];	
			
			//get count of all digits
			for (int i=0;i<list.length;i++) {
				count[(workArray[i]>>shift)&modAmount]++;
			}
			
			//make count equivalent to position in actual array
			for (int i=1;i<count.length;i++) {
				count[i]+=count[i-1];
			}
			
			//sort based on digit
			for (int i=workArray.length-1;i>=0;i--) {
				//get index in count array
				int index = (workArray[i]>>shift)&modAmount;
				//insert into list at appropriate location and reduce count
				list[--count[index]] = workArray[i];
			}
		}
	}
	
	
	
	private static int[] list, count;
	private static int currentPos;
	private static boolean counting = true;
	public void Import(int[] List) {
		list = List;
	}
	public void VisualizedSort() {
		Runnable r = new Runnable() {
			public void run() {
				SortingAlgorithmGUI.UpdateMarkerSetup();
				//get variables
				modAmount = (int)Math.pow(2,nBase)-1;
				shiftAmount = nBase;
				
				//get largest element so we can know the max amount of shifting to do
				int max = afv.get(list,0);
				for (int i=0;i<list.length;i++) {
					if (afv.compareToValue(list,i,max,true)) max=afv.get(list,i);
				}
				
				//get the max amount of shifting
				maxShift=0;
				do maxShift+=shiftAmount; while ((max=max>>shiftAmount)>0);
				
				//sort for each digit
				for (int shift=0;shift<maxShift;shift+=shiftAmount) {
					//intialize
					int[] workArray = list.clone();
					count = new int[modAmount+1];	
					
					//get count of all digits
					counting = true;
					for (currentPos=0;currentPos<list.length;currentPos++) {
						afv.incrementAux(count,(afv.get(workArray,currentPos)>>shift)&modAmount,1);
						SortingAlgorithmGUI.UpdateMarker();
					}
					
					//make count equivalent to position in actual array
					for (int i=1;i<count.length;i++) {
						afv.incrementAux(count,i,afv.get(count,i-1));
					}
					
					//sort based on digit
					counting = false;
					for (int i=workArray.length-1;i>=0;i--) {
						//get index in count array
						int index = (afv.get(workArray,i)>>shift)&modAmount;
						//insert into list at appropriate location and reduce count
						afv.incrementAux(count,index,-1);
						currentPos = afv.get(count,index); //for visuals
						afv.setMain(list,currentPos,afv.get(workArray,i));
						SortingAlgorithmGUI.UpdateMarker();
					}
				}
				SortingAlgorithmGUI.Finish();
			}
		};
		new Thread(r).start();
	}	
	
	public Color GetColor(int index) {
		if (counting) {
			if (index==currentPos) return Color.red;
		}
		else {
			for (int i=0;i<count.length;i++) {
				if (index==count[i]) return Color.pink;
			}
		}
		return SortingAlgorithmGUI.primaryColor;
	}
	
	public int[] getList() {
		return list;
	}
	
	public static boolean singleSound = false;
	public int[] GetSelectedIndexes() {
		if (counting || singleSound) {
			return new int[] {currentPos};
		}
		
		// alt version for non-counting
		ArrayList<Integer> temp = new ArrayList<Integer>();
		for (int i=0;i<count.length;i++) if (count[i]<list.length) temp.add(count[i]);
		int[] temp1 = new int[temp.size()];
		for (int i=0;i<temp.size();i++) temp1[i]=temp.get(i);
		return temp1;
	}
}
