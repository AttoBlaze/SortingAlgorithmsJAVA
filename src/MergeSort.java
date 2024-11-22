import java.awt.Color;

public class MergeSort implements SortingAlgorithm {
	public void Sort(int[] list) {
		int[] workArray = list.clone();	//acts as a working array
		
		//sort
		TopDownSplitMerge(list,0,list.length,workArray);
	}
	private void TopDownSplitMerge(int[] workArray, int begin, int end, int[] list) {
		//if the list is size 1 then there is nothing to sort
		if (end-begin<=1) return;
		
		//split list in 2 and mergesort sublists
		int middle = (begin+end)/2;
		TopDownSplitMerge(list,begin,middle,workArray);
		TopDownSplitMerge(list,middle,end,workArray);
		
		//merge sublists
		TopDownMerge(workArray,begin,middle,end,list);
	}
	private void TopDownMerge(int[] workArray, int begin, int middle, int end, int[] list) {
		int i=begin, j=middle;
		
		//merge by getting first element of each list and inserting the smallest until both lists have been exhausted
		for (int k=begin;k<end;k++) {
			if (i<middle && (j>=end || list[i]<=list[j])) {
				workArray[k] = list[i];
				i++;
			} 
			else {
				workArray[k] = list[j];
				j++;
			}
		}
		
	}
	
	
	
	private static int[] list;
	private static int check, check1, check2, beginIndex, endIndex;
	private static boolean copying;
	public void Import(int[] List) {
		list = List;
	}
	public void VisualizedSort() {
		Runnable r = new Runnable() {
			public void run() {
				SortingAlgorithmGUI.UpdateMarkerSetup();
				copying = false;
				TopDownSplitMerge(list,0,list.length,list.clone());
				SortingAlgorithmGUI.Finish();
			}
			
			private static void TopDownSplitMerge(int[] list, int begin, int end, int[] workArray) {
				//if the list is size 1 then there is nothing to sort
				if (end-begin<=1) return;
				
				//split list in 2 and mergesort sublists
				int middle = (begin+end)/2;
				TopDownSplitMerge(list,begin,middle,workArray);
				TopDownSplitMerge(list,middle,end,workArray);
				
				//merge sublists
				TopDownMerge(list,begin,middle,end,workArray);
			}
			private static void TopDownMerge(int[] list, int begin, int middle, int end, int[] workArray) {
				beginIndex=begin;
				endIndex=end;
				check1=begin;
				check2=middle;
				
				//merge by getting first element of each list and inserting the smallest until both lists have been exhausted
				for (check=begin;check<end;check++) {
					if (check1<middle && (check2>=end || list[check1]<=list[check2])) {
						afv.setAux(workArray,check,afv.get(list,check1));
						check1++;
					} 
					else {
						afv.setAux(workArray,check,afv.get(list,check2));
						check2++;
					}
					SortingAlgorithmGUI.UpdateMarker();
				}
				copying = true; //for visuals
				
				//copy over to list
				for (check=begin;check<end;check++) {
					afv.setMain(list,check,afv.get(workArray,check));
					SortingAlgorithmGUI.UpdateMarker();
				}
				copying = false; //for visuals
			}
		};
		new Thread(r).start();
	}
	
	public Color GetColor(int index) {
		if (copying) {
			if (index==check) return Color.red;
		}
		else if (index==check1 || index==check2) return Color.red;
		if (index<beginIndex || index>=endIndex) return SortingAlgorithmGUI.secondaryColor;
		return SortingAlgorithmGUI.primaryColor;
	}
	
	public int[] getList() {
		return list;
	}
	
	public int[] GetSelectedIndexes() {
		if (copying) {
			return new int[] {check};
		}
		return new int[] {
			check1,
			check2};
	}
}
