import java.awt.Color;

public class RadixSortMSD implements SortingAlgorithm {
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
		
		//sort
		int[] workArray = list.clone();
		CountSort(list,0,list.length,maxShift-shiftAmount, workArray);
	}
	
	private void CountSort(int[] list, int begin, int end, int shift, int[] workArray) {
		if (end-begin<=1 || shift<0) return;
		int[] count = new int[modAmount+2];	
		
		//get count of all digits
		for (int i=begin;i<end;i++) {
			count[1+((workArray[i]>>shift)&modAmount)]++;
		}
		
		//make count equivalent to position in actual array
		count[0] = begin;
		for (int i=1;i<count.length;i++) {
			count[i]+=count[i-1];
		}

		//sort based on digit
		int[] countClone = count.clone();
		for (int i=end-1;i>=begin;i--) {
			//get index in count array
			int index = (workArray[i]>>shift)&modAmount;
			//insert into list at appropriate location and reduce count
			list[--count[index+1]] = workArray[i];
		}
		
		//update work array
		for (int i=begin;i<end;i++) {
			workArray[i] = list[i];
		}
		
		//sort sublists for each digit
		for (int i=0;i<count.length-1;i++) {
			CountSort(list,countClone[i],countClone[i+1],shift-shiftAmount,workArray);
		}
	}
	
	private static int[] list, count;
	private static int currentPos, begin, end;
	private static boolean counting=true;
	public void Import(int[] List) {
		list = List;
	}
	public void VisualizedSort() {
		Runnable r = new Runnable() {
			public void run() {
				SortingAlgorithmGUI.UpdateMarkerSetup();
				counting = true;
				
				//get variables
				modAmount = (int)Math.pow(2,nBase)-1;
				shiftAmount = nBase;
				
				//get largest element so we can know the max amount of shifting to do
				int max = afv.get(list,0);
				for (currentPos=0;currentPos<list.length;currentPos++) {
					if (afv.compareToValue(list,currentPos,max,true)) max=afv.get(list,currentPos);
				}
				
				//get the max amount of shifting
				maxShift=0;
				do maxShift+=shiftAmount; while ((max=max>>shiftAmount)>0);	
				
				//sort
				int[] workArray = list.clone();
				CountSort(list,0,list.length,maxShift-shiftAmount, workArray);
				SortingAlgorithmGUI.Finish();
			}
			
			private void CountSort(int[] list, int Begin, int End, int shift, int[] workArray) {
				if (End-Begin<=1 || shift<0) return;
				count = new int[modAmount+2];
				begin = Begin;
				end = End;
				
				//get count of all digits
				counting = true;
				for (currentPos=begin;currentPos<end;currentPos++) {
					afv.incrementAux(count,1+((afv.get(workArray,currentPos)>>shift)&modAmount),1);
					SortingAlgorithmGUI.UpdateMarker();
				}
				
				//make count equivalent to position in actual array
				count[0] = begin;
				for (int i=1;i<count.length;i++) {
					afv.incrementAux(count,i,afv.get(count,i-1));
				}

				//sort based on digit
				int[] countClone = count.clone();
				counting = false;
				for (int i=end-1;i>=begin;i--) {
					//get index in count array
					int index = 1+((workArray[i]>>shift)&modAmount);
					//insert into list at appropriate location and reduce count
					afv.incrementAux(count,index,-1);
					currentPos = afv.get(count,index);	//for visuals
					afv.setMain(list,currentPos,afv.get(workArray,i));
					SortingAlgorithmGUI.UpdateMarker();
				}
				
				//update work array
				for (int i=begin;i<end;i++) {
					afv.setAux(workArray,i,afv.get(list,i));
				}
				
				//sort sublists for each digit
				for (int i=0;i<count.length-1;i++) {
					CountSort(list,afv.get(countClone,i),afv.get(countClone,i+1),shift-shiftAmount,workArray);
				}
			}
		};
		new Thread(r).start();
	}
	
	public Color GetColor(int index) {
		if (counting) {
			if (index==currentPos) return Color.blue.darker();
		}
		else {
			for (int i=1;i<count.length;i++) {
				if (index==count[i]) return Color.cyan;
			}
		}
		if (index==begin-1 || index==end) return Color.red;
		if (index<begin || index>end) return SortingAlgorithmGUI.secondaryColor;
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
		int[] temp = new int[count.length-1];
		for (int i=1;i<count.length;i++) temp[i-1]=count[i]<list.length?count[i]:count[i]-1;
		return temp;
	}
}
