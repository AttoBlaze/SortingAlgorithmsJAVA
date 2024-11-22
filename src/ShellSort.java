import java.awt.Color;

public class ShellSort implements SortingAlgorithm {
	public static int GetGap(int k) {
		//uses marcin ciuras gap sequence until >7 then 2^k - 1
		switch (k) {
		case 0: return 1;
		case 1: return 4;
		case 2: return 10;
		case 3: return 23;
		case 4: return 57;
		case 5: return 132;
		case 6: return 301;
		case 7: return 701;
		default: return (int)(Math.pow(2,k)-1);
		}
	}
	
	public void Sort(int[] list) {
		//create gaps
		int[] gaps = new int[(int)(Math.log(list.length)/Math.log(2))];
		for (int i=0;i<gaps.length;i++) {
			int gapTemp = GetGap(i);
			gaps[i] = gapTemp<list.length?gapTemp:list.length-1;
		}
		
		//for each gap
		for (int i=gaps.length-1;i>=0;i--) {
			int gap = gaps[i];
			
			//go through list
			for (int j=gap;j<list.length;j++) {
				//save value
				int temp = list[j];
				
				//shift until in place
				int k=j;
				for (;k>=gap && list[k-gap]>temp ; k-=gap) {
					list[k] = list[k-gap];
				}
				
				//place value
				list[k] = temp; 
			}	
		}
	}
	
	private static int[] list, gaps;
	private static int gap, currentPos, currentStartPos;
	public void Import(int[] List) {
		list = List;
	}
	public void VisualizedSort() {
		Runnable r = new Runnable() {
			public void run() {
				SortingAlgorithmGUI.UpdateMarkerSetup();
				
				//create gaps
				gaps = new int[(int)(Math.log(list.length)/Math.log(2))];
				for (int i=0;i<gaps.length;i++) {
					int gapTemp = GetGap(i);
					afv.setAux(gaps,i,gapTemp<list.length?gapTemp:list.length-1);
				}
				
				//for each gap
				for (int i=gaps.length-1;i>=0;i--) {
					gap = afv.get(gaps,i);
					
					//go through list
					for (currentStartPos=gap;currentStartPos<list.length;currentStartPos++) {
						//save value
						int temp = afv.get(list,currentStartPos);
						
						//shift until in place
						currentPos = currentStartPos;
						for (;currentPos>=gap && afv.compareToValue(list,currentPos-gap,temp,true) ; currentPos-=gap) {
							afv.setMain(list,currentPos,afv.get(list,currentPos-gap));
							SortingAlgorithmGUI.UpdateMarker();
						}
						
						//place value
						afv.setMain(list,currentPos,temp); 
						SortingAlgorithmGUI.UpdateMarker();
					}	
				}
				SortingAlgorithmGUI.Finish();
			}
		};
		new Thread(r).start();
	}
	
	public Color GetColor(int index) {
		if (index==currentStartPos) return Color.red;
		if (index==currentPos || index==currentPos-gap) return Color.pink;
		if (index<currentStartPos) return SortingAlgorithmGUI.secondaryColor;
		return SortingAlgorithmGUI.primaryColor;
	}
	
	public int[] getList() {
		return list;
	}
	
	public int[] GetSelectedIndexes() {
		return new int[] {
			currentPos,
			currentPos>=gap? currentPos-gap:0,
			currentStartPos};
	}
}
	