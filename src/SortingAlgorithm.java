import java.awt.Color;

/*
 * The sorting algorithms are structured as such:
 *  There are two implementations: performance, and visual.
 *  Performance sorts the given, no fancy gimmicks of any kind.
 *  The visual implementation does the exact same steps as the performance implementation, but differs in that:
 *  	Array functions are used to keep track of operations 
 *  	Variables are used to keep track of the current state of the algorithm/sort
 *  	Many function variables are made into local class variables to keep track of positions and whatnot for visuals
 * 		The sort is run on a seperate thread
 * 		Update markers are placed throughout the sort at appropriate points
 * 		
 *  Update markers operate as follows:
 *  	The first update marker usually takes the form of "SortingAlgorithmGUI.UpdateMarkerSetup();".
 *  	Subsequent update markers then take the form of "SortingAlgorithmGUI.UpdateMarker();"
 *  	At each update marker, the thread checks if the amount of updates it has performed is more than the alotted amount of updates since the starting time.
 *  	If it is so, the thread sleeps for a short time, then rechecks. 
 *  	Otherwise if not, the update amount is incremented, and variables are updated.
 *  	The sort time is updated as the time between last update marker (nano) and the current nano time. Be aware that each time the thread sleeps, some uncertainty is introduced in the sort time.
 *	
 *	The result is that the thread which the sort runs on will pass a specific amount of update markers pr second. 
 *	The speed variable decides the amount of update markers to pass every second. This value usually defines a lower bound for the amount of updates pr second.
 */

public interface SortingAlgorithm {
	//just sorting
	public void Sort(int[] list);			//sorts the list, no fancy gimmicks, just performance.
	
	//visualized sorting
	public void Import(int[] list);			//imports the list to be sorted. This isnt a static method because interfaces wouldnt let me do that without the list being final.
	public void VisualizedSort();			//performs a visual sort as described above
	public Color GetColor(int index);		//returns a color for a specified index. When using gradients, the primary and secondary algorithm color are changed to gradients. Any other colors will remain unchanged.
	public int[] getList();					//returns the current list
	public int[] GetSelectedIndexes();		//returns a list of 'selected' indexes in the list which are used as the basis for drawing pointers and creating sound.
	
	public static void Swap(int[] list, int i, int k) {
		int temp = list[i];
		list[i] = list[k];
		list[k] = temp;
	}
}