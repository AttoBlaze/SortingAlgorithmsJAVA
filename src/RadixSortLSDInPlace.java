import java.awt.Color;
import java.util.ArrayList;

public class RadixSortLSDInPlace implements SortingAlgorithm {
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
			int[] count = new int[modAmount];	
			
			//set count of digits
			for (int i=0;i<count.length;i++) {
				count[i]=list.length-1;
			}
			
			//sort based on digit
			int pos = 0;
			for (int i=0;i<list.length;i++) {
				//get index in count array
				int digit = (list[pos]>>shift)&modAmount;
				if (digit==0) {
					pos++;
					continue;
				}
				
				//shift everything over
				int temp = list[pos];
				digit--; //makes into index in count array
				for (int j=pos;j<count[digit];j++) { //basically insertion sort
					list[j] = list[j+1];
				}
				list[count[digit]] = temp;
				
				//update counts
				for (int j=digit-1;j>=0;j--) {
					count[j]--;
				}
			}
		}
	}
	
	
	
	private static int[] list, count;
	private static int currentPos, pos;
	public void Import(int[] List) {
		list = List;
		count = new int[modAmount];	
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
					count = new int[modAmount];	
					
					//set count of digits
					for (int i=0;i<count.length;i++) {
						afv.setAux(count,i,list.length-1);
					}
					
					//sort based on digit
					pos = 0;
					for (int i=0;i<list.length;i++) {
						//get index in count array
						int digit = (afv.get(list,pos)>>shift)&modAmount;
						if (digit==0) {
							currentPos = pos; //for visuals
							pos++;
							SortingAlgorithmGUI.UpdateMarker();
							continue;
						}
						digit--; //makes into index in count array
						
						//shift everything over
						int temp = afv.get(list,pos);
						for (int j=pos;j<afv.get(count,digit);j++) { //basically insertion sort
							afv.setMain(list,j,afv.get(list,j+1));
						}
						afv.setMain(list,afv.get(count,digit),temp);
						currentPos = count[digit]; //for visuals
						SortingAlgorithmGUI.UpdateMarker();
						
						//update counts
						for (int j=digit-1;j>=0;j--) {
							afv.incrementAux(count,j,-1);
						}
					}
				}		
						
				SortingAlgorithmGUI.Finish();
			}
		};
		new Thread(r).start();
	}	
	
	public Color GetColor(int index) {
		if (index==pos) return Color.red;
		for (int i=0;i<count.length;i++) {
			if (index==count[i]) return Color.pink;
		}
		return SortingAlgorithmGUI.primaryColor;
	}
	
	public int[] getList() {
		return list;
	}
	
	public static boolean singleSound = false;
	public int[] GetSelectedIndexes() {
		if (singleSound) {
			return new int[] {currentPos};
		}
		
		// alt version for non-counting
		ArrayList<Integer> temp = new ArrayList<Integer>();
		temp.add(pos);
		for (int i=0;i<count.length;i++) temp.add(count[i]);
		int[] temp1 = new int[temp.size()];
		for (int i=0;i<temp.size();i++) temp1[i]=temp.get(i);
		return temp1;
	}
}
