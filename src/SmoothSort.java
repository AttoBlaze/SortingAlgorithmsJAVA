import java.awt.Color;

public class SmoothSort implements SortingAlgorithm{
	//tree values
	private static long treeCount = Long.parseUnsignedLong("0");
	//leonardo numbers up to integer limit since when we reach this size we go out of bounds in the array
	private static final int[] leonardos = new int[]{1,			1,			3,			5,			9	
													,15,		25, 		41,			67,			109
													,177,		287,		465,		753,		1219
													,1973,		3193,		5167,		8361,		13529
													,21891,		35421,		57313,		92735,		150049
													,242785,	392835,		635621,		1028457,	1664079
													,2692537,	4356617,	7049155,	11405773,	18454929
													,29860703,	48315633,	78176337,	126491971,	204668309
													,331160281,	535828591,	866988873,	1402817465};
	
	public void Sort(int[] list) {
		//reset tree counter
		treeCount = Long.parseUnsignedLong("0");
		
		//build leonardo trees
		for (int i=0;i<list.length;i++) {
			//update tree count
			pushTree();
			
			//update roots
			updateRoots(list,i,0);
		}
		
		//pop the leonardo trees
		byte root;
		for (int i=list.length-1;i>=0;i--) {
			//if the smallest tree is a single node just update the tree count as such
			if ((treeCount&3)!=0) {
				treeCount-=(treeCount&2)-(treeCount&1);
				continue;
			}
			
			//otherwise find the root
			root = 2;
			while (((treeCount>>>root)&1)==0) root++;
			
			//update roots
			popTree();
			updateRoots(list,i-leonardos[root-2]-1,root-1);	//small branch (l-2)
			updateRoots(list,i-1,root-2);					//large branch (l-1)
		}
	}
	
	private static void updateRoots(int[] list, int currentPos, int initialRoot) {
		//find leftmost tree
		byte root = (byte)(initialRoot-1);
		int leftRootPos = currentPos, currentRootPos = currentPos, temp;
		while (++root<leonardos.length) {
			//if there is no leonardo tree then skip
			if (((treeCount>>>root)&1)==0) continue;
			
			//update left root pos
			leftRootPos-=leonardos[root];
			
			//if the left root is greater than the current root and its children, swap them
			if (leftRootPos>=0 &&
			(temp=list[leftRootPos])>list[currentRootPos] && 
			(root<2 || (temp>list[currentRootPos-1] && temp>list[currentRootPos-leonardos[root-2]-1]))) {
				//swap the roots and update values
				SortingAlgorithm.Swap(list,currentRootPos,leftRootPos);
				currentRootPos = leftRootPos;
			}
			
			//otherwise heapify the current root
			else {
				maxHeapify(list,root,currentRootPos);
				break;
			}
		}
	}
	
	//for keeping track of used leonardo numbers
	private static void pushTree() {
		//find adjacent trees by looking for a 11 in bits
		byte shifts = 0;
		while (shifts<leonardos.length) {
			//if we find an adjacent pair then merge
			if (((treeCount>>>shifts)&3)==3) {
				treeCount+=(1<<shifts);		
				return;
			}
			shifts++;
		}
		
		//otherwise just insert as a new node. We insert 1 before 0 as not doing so can result in 101 -> 111, which should be impossible.
		if ((treeCount&2)==0) treeCount = treeCount|2; else treeCount++;
	}
	private static void popTree() {//only pops leonardo trees which are not single-node
		//find the smallest tree by looking for the rightmost 1 in bits
		byte shifts = 1;
		while (++shifts<leonardos.length) {
			//when we find the bit then split the tree into 2
			if (((treeCount>>>shifts)&1)==1) {
				treeCount+=((3<<shifts)>>2) - (1<<shifts);
				return;
			}
		}
	}
	
	private static void maxHeapify(int[] list, int leonardo, int Pos) {
		//initialize
		int largest, left, right, pos = Pos, leon = 0;
		//heapify
		while (leonardo>1) {
			//get child positions
			largest = pos;
			left = pos-leonardos[leonardo-2]-1;
			right = pos-1;
			
			//left side
			if (list[largest]<list[left]) {
				largest = left;
				leon = 1;
			}
			
			//right side
			if (list[largest]<list[right]) {
				largest = right;
				leon = 2;
			}
			
			if (largest!=pos) {
				//swap
				SortingAlgorithm.Swap(list,largest,pos);
				
				//heapify the rest
				pos = largest;
				leonardo -= leon;
			}
			else break;
		}
	}
	
	
	
	private static int[] list;
	private static int currentPos, sortingLength;
	public void Import(int[] List) {
		list = List;
		
	}
	public void VisualizedSort() {
		Runnable r = new Runnable() {
			public void run() {
				SortingAlgorithmGUI.UpdateMarkerSetup();
				
				//reset tree counter
				treeCount = Long.parseUnsignedLong("0");
				
				//build leonardo trees
				for (sortingLength=0;sortingLength<list.length;sortingLength++) {
					//update tree count
					pushTree();
					
					//update roots
					updateRoots(list,sortingLength,0);
				}
				
				//pop the leonardo trees
				byte root;
				for (sortingLength=list.length-1;sortingLength>=0;sortingLength--) {
					//if the smallest tree is a single node just update the tree count as such
					if ((treeCount&3)!=0) {
						treeCount-=(treeCount&2)-(treeCount&1);
						continue;
					}
					
					//otherwise find the root
					root = 2;
					while (((treeCount>>>root)&1)==0) root++;
					
					//update roots
					popTree();
					updateRoots(list,sortingLength-afv.get(leonardos,root-2)-1,root-1);
					updateRoots(list,sortingLength-1,root-2);
				}
				SortingAlgorithmGUI.Finish();
			}
			private static void updateRoots(int[] list, int pos, int initialRoot) {
				//find leftmost tree
				byte root = (byte)(initialRoot-1);
				int leftRootPos = pos, temp;
				currentPos = pos;
				while (++root<leonardos.length) {
					//if there is no leonardo tree then skip
					if (((treeCount>>>root)&1)==0) continue;
					
					//update left root pos
					leftRootPos-=afv.get(leonardos,root);
					
					//if the left root is greater than the current root and its children, swap them
					if (leftRootPos>=0 &&
					afv.compareToValue(list,currentPos,(temp=afv.get(list,leftRootPos)),false) && 
					(root<2 || (afv.compareToValue(list,currentPos-1,temp,false) && afv.compareToValue(list,currentPos-afv.get(leonardos,root-2)-1,temp,false)))) {
						//swap the roots and update values
						afv.swapMain(list,currentPos,leftRootPos);
						currentPos = leftRootPos;
						SortingAlgorithmGUI.UpdateMarker();
					}
					
					//otherwise heapify the current root
					else {
						maxHeapify(list,root,currentPos);
						break;
					}
				}
			}
			private static void maxHeapify(int[] list, int leonardo, int Pos) {
				//initialize
				int largest, left, right, leon = 0;
				currentPos = Pos;
				//heapify
				while (leonardo>1) {
					//get child positions
					largest = currentPos;
					left = currentPos-afv.get(leonardos,leonardo-2)-1;
					right = currentPos-1;
					
					//left side
					if (afv.compare(list,largest,left)) {
						largest = left;
						leon = 1;
					}
					
					//right side
					if (afv.compare(list,largest,right)) {
						largest = right;
						leon = 2;
					}
					
					if (largest!=currentPos) {
						//swap
						afv.swapMain(list,largest,currentPos);
						
						//heapify the rest
						currentPos = largest;
						leonardo -= leon;
						SortingAlgorithmGUI.UpdateMarker();
					}
					else break;
				}
			}
		};
		new Thread(r).start();
	}
	
	public Color GetColor(int index) {
		if (index==currentPos) return Color.red;
		if (index==sortingLength) return Color.red;
		if (index>sortingLength) return SortingAlgorithmGUI.secondaryColor;
		return SortingAlgorithmGUI.primaryColor;
	}
	
	public int[] getList() {
		return list;
	}
	
	public int[] GetSelectedIndexes() {
		return new int[] {currentPos};
	}
}
