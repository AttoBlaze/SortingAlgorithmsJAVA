/* 
 * afv stands for A.rray F.unctions for V.isuals.
 * These functions exist to bundle together array functions and operation counters. 
 * They may be a little less readable, but i think its worth it for the lines and headaches saved by not updating operation counters manually.
 */


public class afv {
	public static void swapMain(int[] list, int i, int k) {
		int temp = list[i];
		list[i] = list[k];
		list[k] = temp;
		SortingAlgorithmGUI.swaps++;
		SortingAlgorithmGUI.writesToMain+=2;
		SortingAlgorithmGUI.arrayAccesses+=4;
	}
	
	public static void swapAux(int[] list, int i, int k) {
		int temp = list[i];
		list[i] = list[k];
		list[k] = temp;
		SortingAlgorithmGUI.swaps++;
		SortingAlgorithmGUI.writesToAux+=2;
		SortingAlgorithmGUI.arrayAccesses+=4;
	}
	
	public static boolean compare(int[] list, int i, int k) {
		SortingAlgorithmGUI.arrayAccesses+=2;
		SortingAlgorithmGUI.comparisons++;
		if (list[i]<list[k]) return true;
		return false;
	}
	
	public static boolean compareToValue(int[] list, int i, int value, boolean bigger) {
		SortingAlgorithmGUI.arrayAccesses++;
		SortingAlgorithmGUI.comparisons++;
		if (bigger) {if (list[i]>value) return true;}
		else if (list[i]<value) return true;
		return false;
	}
	
	public static int get(int[] list, int i) {
		SortingAlgorithmGUI.arrayAccesses++;
		return list[i];
	}
	
	public static void setMain(int[] list, int i, int value) {
		list[i] = value;
		SortingAlgorithmGUI.writesToMain++;
		SortingAlgorithmGUI.arrayAccesses++;
	}
	
	public static void setAux(int[] list, int i, int value) {
		list[i] = value;
		SortingAlgorithmGUI.writesToAux++;
		SortingAlgorithmGUI.arrayAccesses++;
	}
	
	public static void incrementMain(int[] list, int i, int value) {
		list[i]+=value;
		SortingAlgorithmGUI.arrayAccesses+=2;
		SortingAlgorithmGUI.writesToMain++;
	}
	
	public static void incrementAux(int[] list, int i, int value) {
		list[i]+=value;
		SortingAlgorithmGUI.arrayAccesses+=2;
		SortingAlgorithmGUI.writesToMain++;
	}
}
