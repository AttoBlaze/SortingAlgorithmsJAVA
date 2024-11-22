import java.awt.Color;

public class BitonicSort implements SortingAlgorithm {
	public void Sort(int[] list) {
		BitonicSortList(list,0,list.length,true);
	}
	
	private void BitonicSortList(int[] list, int low, int cnt, boolean direction) {
		//if the list is <2 then there is nothing to sort.
		if (cnt<2) return;
		
		//sort each list in different directions
		int k = cnt/2;
		BitonicSortList(list,low,k,direction);
		BitonicSortList(list,low+k,cnt-k,!direction);
		
		//merge
		BitonicMerge(list,low,cnt,direction);
	}
	
	private void BitonicMerge(int[] list, int low, int cnt, boolean direction) {
		//if the list is <2 then there is nothing to sort.
		if (cnt<2) return;
		
		//get greatest power of 2 less than cnt
		int k = GreatestPowerOfTwoLessThan(cnt);
		
		//go through list and merge
		for (int i=low;i<low+cnt-k;i++) {
			//swap according to direction
			if (list[i]>list[i+k]) {
				if (direction) SortingAlgorithm.Swap(list,i,i+k);
			} 
			else if (!direction) SortingAlgorithm.Swap(list,i,i+k);
		}
		
		//merge sublists
		BitonicMerge(list,low,k,direction);
		BitonicMerge(list,low+k,cnt-k,direction);
	}
	
	private static int GreatestPowerOfTwoLessThan(int n) {
		int k = 1;
		while (k>0 && k<n) k=k<<1;
		return k>>1;
	}
	
	
	
	
	
	
	private static int[] list;
	private static int low, center, altCenter, check;
	public void Import(int[] List) {
		list = List;
	}
	public void VisualizedSort() {
		Runnable r = new Runnable() {
			public void run() {
				SortingAlgorithmGUI.UpdateMarkerSetup();
				BitonicSortList(list,0,list.length,true);
				SortingAlgorithmGUI.Finish();
			}
			private static void BitonicSortList(int[] list, int low, int cnt, boolean direction) {
				//if the list is <2 then there is nothing to sort.
				if (cnt<2) return;
				
				//sort each list in different directions
				int k = cnt/2;
				BitonicSortList(list,low,k,direction);
				BitonicSortList(list,low+k,cnt-k,!direction);
				
				//merge
				BitonicMerge(list,low,cnt,direction);
			}
			private static void BitonicMerge(int[] list, int Low, int cnt, boolean direction) {
				//if the list is <2 then there is nothing to sort.
				if (cnt<2) return;
				
				//get greatest power of 2 less than cnt
				int k = GreatestPowerOfTwoLessThan(cnt);
				altCenter = k;	//for visuals
				low = Low;
				center = cnt;
				
				//go through list and merge
				for (check=Low;check<Low+cnt-k;check++) {
					//swap according to direction
					if (afv.compare(list,check+k,check)) {
						if (direction) afv.swapMain(list,check,check+k);
					} 
					else if (!direction) afv.swapMain(list,check,check+k);
					SortingAlgorithmGUI.UpdateMarker();
				}
				
				//merge sublists
				BitonicMerge(list,Low,k,direction);
				BitonicMerge(list,Low+k,cnt-k,direction);
			}
		};
		new Thread(r).start();
	}
	
	
	public Color GetColor(int index) {
		if (index==check || index==check+altCenter) return Color.red;
		if (index<low || index>low+center-1) return SortingAlgorithmGUI.secondaryColor;
		return SortingAlgorithmGUI.primaryColor;
	}
	
	public int[] getList() {
		return list;
	}
	
	public int[] GetSelectedIndexes() {
		return new int[] {
			check,
			check+altCenter};
	}
}
