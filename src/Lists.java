
public class Lists {
	//enum/types
	public enum ListType{
		Random,
		Linear,
		Quadratic,
		Exponential,
		Logarithmic,
		Cubic,
		Power;
	}
	
	//get list
	public static int[] GetList(int size) {
		int[] list = GetListType(size, Main.listType);
		
		//modifiers
		if (Main.reversedList) ReverseList(list);
		if (Main.scrambledList) ScrambleList(list);
		
		//partial shuffles
		int shuffles = 0;
		while (shuffles++<list.length*Main.shuffleRate) {
			//get random index
			int randomIndex1, randomIndex2;
			do {
				randomIndex1 = (int)(Math.random()*list.length);
				randomIndex2 = (int)(Math.random()*list.length);
			} while (randomIndex1==randomIndex2);
			
			//swap
			int temp = list[randomIndex1];
			list[randomIndex1] = list[randomIndex2];
			list[randomIndex2] = temp;
		}
		
		return list;
	}
	
	public static int[] GetListType(int size, ListType listType) {
		int[] list;
		switch (listType) {
		case Random:		list = GetRandomList(		size,Main.lowerBound,Main.upperBound										); break;
		case Quadratic:		list = GetQuadraticList(	size,Main.lowerBound,Main.upperBound	,QLaVal,QLbVal,QLfrom,QLto			); break;
		case Exponential:	list = GetExponentialList(	size,Main.lowerBound,Main.upperBound	,ELfrom,ELto						); break;
		case Logarithmic:	list = GetLogarithmicList(	size,Main.lowerBound,Main.upperBound	,LLfrom,LLto						); break;
		case Cubic:			list = GetCubicList(		size,Main.lowerBound,Main.upperBound	,CLaVal,CLbVal,CLcVal,CLfrom,CLto	); break;
		case Power:			list = GetPowerList(		size,Main.lowerBound,Main.upperBound	,PLpower,PLfrom,PLto				); break;
		default: 			list = GetLinearList(		size,Main.lowerBound,Main.upperBound										); break;
		}
		if (Main.preSortedList) new CountingSort().Sort(list);
		return list;
	}
	
	//reverse list
	public static void ReverseList(int[] list) {
		//swap opposite indexes from middle until midpoint is reached
		for (int i=0;i<list.length/2;i++) {
			int temp = list[i];
			list[i] = list[list.length-1-i];
			list[list.length-1-i] = temp;
		}
	}
	public static void ScrambleList(int[] list) {
		//scramble
		for (int i=0;i<list.length;i++) {
			//swap each index with a random index
			int temp = list[i],
				random = (int)(Math.random()*list.length);
			list[i] = list[random];
			list[random] = temp;
		}

	}
	
	
	
	//randomized list
	public static int[] GetRandomList(int size, int lowerBound, int upperBound) {
		//initialize
		int[] list = new int[size];
		
		//fill with random values
		for (int i=0;i<list.length;i++) {
			list[i] = (int)(Math.random()*(Main.upperBound-Main.lowerBound) + Main.lowerBound);
		}
		
		return list;
	}
	
	//linear list
	public static int[] GetLinearList(int size, int lowerBound, int upperBound) {
		//initialize
		int[] list = new int[size];
		
		//fill with values
		for (int i=0;i<list.length;i++) {
			list[i] = (int)((long)i*(Main.upperBound-Main.lowerBound)/list.length + Main.lowerBound);
		}
		
		return list;
	}
	
	//quadratic list
	public static double QLaVal = 1, QLbVal = 0, QLfrom = 0, QLto = 1;
	public static int[] GetQuadraticList(int size, int lowerBound, int upperBound, double aVal, double bVal, double from, double to) {
		//initialize
		int[] list = new int[size];
		
		//get lowest and highest part of quadratic curve in range
		double ekstrema = -QLbVal/2/QLaVal,
				min = (from<=ekstrema && ekstrema<=to && aVal>0)?  QuadFunc(ekstrema,aVal,bVal) : Double.min(QuadFunc(from,aVal,bVal),QuadFunc(to,aVal,bVal)),
				max = (from<=ekstrema && ekstrema<=to && aVal<0)?  QuadFunc(ekstrema,aVal,bVal) : Double.max(QuadFunc(from,aVal,bVal),QuadFunc(to,aVal,bVal));
		
		//fill with values
		for (int i=0;i<list.length;i++) {
			//we get the value as the value in range, normalized, times the value bounds, plus the lower bound. This gives a list with the specified value bounds with a form matching the curve of the specified quadratic.
			list[i] = (int)((QuadFunc((to-from)*(double)i/list.length + from,aVal,bVal)-min)/(max-min)*(upperBound-lowerBound) + lowerBound);
		}
		
		return list;
	}
	private static double QuadFunc(double x, double aVal, double bVal) {
		return aVal*x*x + bVal*x;
	}
	
	//exponential list
	public static double ELfrom = 0d, ELto = 2.5d;
	public static int[] GetExponentialList(int size, int lowerBound, int upperBound, double from, double to) {
		//initialize
		int[] list = new int[size];
		
		//fill with values
		for (int i=0;i<list.length;i++) {
			//we get the value as the value in range, normalized, times the value bounds, plus the lower bound. This gives a list with the specified value bounds with a form matching the curve of the specified exponential ratio of the high end value to the low end.
			list[i] = (int)((Math.exp((to-from)*(double)i/list.length)-Math.exp(from))/Math.exp(to)*(upperBound-lowerBound) + lowerBound);
		}
		
		return list;
	}
	
	//logarithmic list
	public static double LLfrom = 0.5d, LLto = 2.5d;
	public static int[] GetLogarithmicList(int size, int lowerBound, int upperBound, double from, double to) {
		//initialize
		int[] list = new int[size];
		
		//fill with values
		for (int i=0;i<list.length;i++) {
			//we get the value as the value in range, normalized, times the value bounds, plus the lower bound. This gives a list with the specified value bounds with a form matching the curve of the specified range in the natural log graph.
			list[i] = (int)((Math.log(from + (to-from)*(double)i/list.length)-Math.log(from))/(Math.log(to)-Math.log(from))*(upperBound-lowerBound) + lowerBound);
		}
		
		return list;
	}
	
	//cubic list
	public static double CLaVal = 1, CLbVal = 2, CLcVal = 0, CLfrom = -1d, CLto = 0.6d;
	public static int[] GetCubicList(int size, int lowerBound, int upperBound, double aVal, double bVal, double cVal, double from, double to) {
		//initialize
		int[] list = new int[size];
		
		//get lowest and highest part of cubic curve in range
		double	dValFindEkstrema = 4*bVal*bVal - 12*aVal*cVal,	//d = b^2 - 4ac
				ekstrema1 = dValFindEkstrema<0? from:(-2*bVal+Math.sqrt(dValFindEkstrema))/(2*aVal),	//x = (-b+sqrt(d))/2a   //if statement incase root is complex
				ekstrema2 = dValFindEkstrema<0? from:(-2*bVal-Math.sqrt(dValFindEkstrema))/(2*aVal),	//x = (-b-sqrt(d))/2a	
				min,max;
		if (from>ekstrema1 || ekstrema1>to) ekstrema1 = from;	//incase ekstrema are outside range just place at the end of ranges
		if (from>ekstrema2 || ekstrema2>to) ekstrema2 = from;
		min = Double.min(Double.min(Double.min(CubicFunc(to,aVal,bVal,cVal),CubicFunc(from,aVal,bVal,cVal)),CubicFunc(ekstrema1,aVal,bVal,cVal)),CubicFunc(ekstrema2,aVal,bVal,cVal)); 
		max = Double.max(Double.max(Double.max(CubicFunc(to,aVal,bVal,cVal),CubicFunc(from,aVal,bVal,cVal)),CubicFunc(ekstrema1,aVal,bVal,cVal)),CubicFunc(ekstrema2,aVal,bVal,cVal)); 
		
		//fill with values
		for (int i=0;i<list.length;i++) {
			//we get the value as the value in range, normalized, times the value bounds, plus the lower bound. This gives a list with the specified value bounds with a form matching the curve of the specified cubic.
			list[i] = (int)((CubicFunc((to-from)*(double)i/list.length + from,aVal,bVal,cVal)-min)/(max-min)*(upperBound-lowerBound) + lowerBound);
		}
		
		return list;
	}
	private static double CubicFunc(double x, double aVal, double bVal, double cVal) {
		return aVal*x*x*x + bVal*x*x + cVal*x;
	}
		
	//power list
	public static double PLpower = 7d, PLfrom = -1d, PLto = 1d; 
	public static int[] GetPowerList(int size, int lowerBound, int upperBound, double power, double from, double to) {
		//initialize
		int[] list = new int[size];
		
		//get max and min
		double min = from<=0 && 0<=to?  Double.min(0,Double.min(Math.pow(from,power),Math.pow(to,power))) : Double.min(Math.pow(from,power),Math.pow(to,power)),
			   max = from<=0 && 0<=to?  Double.max(0,Double.max(Math.pow(from,power),Math.pow(to,power))) : Double.max(Math.pow(from,power),Math.pow(to,power));
		
		//fill with values
		for (int i=0;i<list.length;i++) {
			//we get the value as the value in range, normalized, times the value bounds, plus the lower bound. This gives a list with the specified value bounds with a form matching the curve of the specified range in the power function.
			list[i] = (int)((Math.pow((to-from)*(double)i/list.length + from,power)-min)/(max-min)*(upperBound-lowerBound) + lowerBound);
		}
		
		return list;
	}
	
	
	
	//list size
	public static int GetSize(Algorithm algorithm) {
		//baseline
		int temp = Main.size;
		
		//presets
		if (Main.usePresetSize) {
			//visual preset
			if (Main.programMode==Main.Mode.Visualize)	temp = !Main.presetAsMult? 	(Main.smallVisualPreset? algorithm.visualSizeLow:algorithm.visualSizeHigh):					//default
																					Main.size*(Main.smallVisualPreset? algorithm.visualSizeLow:algorithm.visualSizeHigh)/1000;	//preset as mult
			
			//non-visual preset
			else 	temp = !Main.presetAsMult? 	algorithm.size:						//default
												Main.size*algorithm.size/1000;		//preset as mult
		}
		
		if (Main.upperBoundIsSize) Main.upperBound = Main.lowerBound+Integer.max(2,temp);	//update upper bound if specified
		return Integer.max(2,temp);
	}
	
	//visualized sorting speed
	public static float GetSpeed(Algorithm algorithm) {
		if (!Main.usePresetSpeed) return Main.speed;																	//non-preset
		if (!Main.presetAsMult) return (Main.smallVisualPreset? algorithm.visualSpeedLow:algorithm.visualSpeedHigh);	//preset
		return (Main.smallVisualPreset? algorithm.visualSpeedLow:algorithm.visualSpeedHigh)*Main.speed/1000;			//multiplier preset
	}
}