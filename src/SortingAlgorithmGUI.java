import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Ellipse2D;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

public class SortingAlgorithmGUI extends JPanel{
	private static final long serialVersionUID = 1L;	
	private static Timer timer;
	private static SortingAlgorithm algorithm;
	private static boolean isFinished = false, modifying;
	private static float animation = 0;
	private static int scrambleIndex = 0, reverseIndex = 0, shuffleIndex1, shuffleIndex2, shufflesMade = 0;
	public static long startingTime, endTime, stepsMade, comparisons, swaps, writesToMain, writesToAux, arrayAccesses, ticksDone, audioTicksDone, updateTime, prevNanoTime;
	private static int[] referenceList;
	
	SortingAlgorithmGUI(SortingAlgorithm Algorithm) {
		JFrame mainFrame = new JFrame();
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setBounds(50,50,Main.width,Main.height);
		mainFrame.add(this);
		mainFrame.setTitle("Sorting algorithms");
		mainFrame.setVisible(true);
		
		//mainFrame.setSize(Main.width-this.getWidth(),Main.height-this.getHeight());
		//System.out.println("\n\n\nDIMENSIONS: "+this.getWidth()+" x "+this.getHeight());
		
		algorithm = Algorithm;
		startingTime = System.currentTimeMillis();
		if (Main.showModifiers) modifying = true;
		else algorithm.VisualizedSort();
		if (Main.visualization==Main.Visualization.DisparityDots) {
			referenceList = algorithm.getList().clone();
			new CountingSort().Sort(referenceList);
		}
		timer = new Timer(10,new ActionListener(){
			public void actionPerformed(ActionEvent e) {if (System.currentTimeMillis()>startingTime) {
				//modifiers - start sorting when done modifying
				if (modifying && !(modifying = ApplyModifiers())) {
					stepsMade = 0;
					writesToMain = 0;
					arrayAccesses = 0;
					swaps = 0;
					startingTime = System.currentTimeMillis()+Main.modifierWait;
					algorithm.VisualizedSort();
				}
				else if (!isFinished) endTime = System.currentTimeMillis();
				ticksDone++;
				
				//update visuals
				repaint();
			}}
		});
		timer.setInitialDelay(0);
		timer.start(); 
	}
	
	public static void UpdateMarker() {
		//wait if needed
		long temp = System.nanoTime();
		while (stepsMade>=((System.currentTimeMillis()-SortingAlgorithmGUI.startingTime)*Lists.GetSpeed(Main.sortingAlgorithm))/1000) try {
			Thread.sleep(10);
		} catch (Exception e) {}	
		
		//update stuff
		stepsMade++;
		updateTime += temp-prevNanoTime;
		prevNanoTime = System.nanoTime();
	}
	
	public static void UpdateMarkerSetup() {
		prevNanoTime = System.nanoTime();
	}
	
	public static void Finish() {
		updateTime += System.nanoTime()-prevNanoTime;
		endTime = System.currentTimeMillis();
		isFinished = true;
	}
	
	
	private static int[] CurrentModifyIndexes() {
		if (Main.reversedList && reverseIndex<algorithm.getList().length/2) return new int[] {reverseIndex-1};
		if (Main.scrambledList && scrambleIndex<algorithm.getList().length) return new int[] {scrambleIndex-1};
		return new int[] {shuffleIndex1,shuffleIndex2};
	}
	
	private static boolean ApplyModifiers() {
		while(stepsMade<((System.currentTimeMillis()-startingTime)*Main.modifierSpeed*algorithm.getList().length)/100000) {
			stepsMade++;
			
			//reverse
			if (Main.reversedList && reverseIndex<algorithm.getList().length/2) {
				//swap opposite indexes
				afv.swapMain(algorithm.getList(),reverseIndex,algorithm.getList().length-reverseIndex-1);
				reverseIndex++;
				continue;
			}
			
			//scramble
			if (Main.scrambledList && scrambleIndex<algorithm.getList().length) {
				//swap each index with a random index
				int random = (int)(Math.random()*algorithm.getList().length);
				afv.swapMain(algorithm.getList(),scrambleIndex,random);
				scrambleIndex++;
				continue;
			}
			
			//shuffle
			if (shufflesMade<Main.shuffleRate*algorithm.getList().length) {
				//swap random indexes
				do {
					shuffleIndex1 = (int)(Math.random()*algorithm.getList().length);
					shuffleIndex2 = (int)(Math.random()*algorithm.getList().length);
				} while (shuffleIndex1==shuffleIndex2);
				afv.swapMain(algorithm.getList(),shuffleIndex1,shuffleIndex2);
				shufflesMade++;
			}
			else return false;
		}
		return true;
	}
	
	private static int GetSortedIndex(int index) {
		int findValue = algorithm.getList()[index],
			left = 0,
			right = referenceList.length-1,
			middle = 0;
		
		//use binary search to get sorted value position
		while (left<=right) {
			middle = (left+right)>>1;
			int check = referenceList[middle];
			if (check<findValue) {
				left = middle+1;
			}
			else if (check>findValue) {
				right = middle-1;
			}
			else break;
		}
		return middle;
	}
	
	
	
	private static float[] 	diatonicScale = 	new float[]{21.8f , 32.7f , 49f , 73.42f , 110f , 164.81f,246.94f},	//diatonic:	 	F-C-G-D-A-E-B
							pentatonicScale = 	new float[]{21.8f , 32.7f , 49f ,		   110f , 164.81f		 },	//pentatonic:	F-C-G-A-E
							scale = 			Main.diatonicScale? diatonicScale:pentatonicScale;
	public void PlayTones(int[] indexes) {
		//get frequencies
		for (int i=0;i<indexes.length;i++) {
			//interpolation uses powers
			float frequency = Main.minHz + (float)((Main.maxHz-Main.minHz)*Math.pow((double)algorithm.getList()[indexes[i]]/Main.upperBound,Main.freqScaling));
			
			//round to smallest note greater than frequency in scale if specified
			if (Main.roundToNote) {
				//find smallest note greater than the frequency by moving through the scale. When we have gone through the scale, we start over by moving 4 octaves up.
				float tempFreq = frequency; int temp = 0;
				while (tempFreq<=scale[temp%scale.length]*(1<<4*(temp/scale.length))) frequency = scale[(temp++)%scale.length]*(1<<4*(temp/scale.length));
			}
			
			//play sounds
			try {
				//volume/cbrt seemed to be the most reasonable volume scaling to prevent actual earrape but still preserve the percieved volume for each sound played. Its not based on anything, i just quickly tried some stuff and saw that this worked fine.
				AudioManager.tone(frequency, Main.volume/Math.cbrt(indexes.length));	
			} catch (Exception e) {}
		}
		audioTicksDone++;
	}
	
	
	
	
	public static Color primaryColor = Color.white,
						secondaryColor = new Color(200,200,200);
	public Color GetGradientColor(int value) {
		float temp = Main.gradientMin+(float)((1-Main.gradientMin)*value)/Main.upperBound;
		return Color.getHSBColor(0f,0f,temp);
	}
	
	public void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.setColor(Color.black);
		g2d.fillRect(0,0,this.getWidth(),this.getHeight());
		
		//visualize algorithm
		switch (Main.visualization) {
		case BarGraph:
			for (int i=0;i<this.getWidth();i++) {
				//get shown index & drawn height (get a scaled index position for each pixel horizontally so list size doesnt effect visual draw time too much)
				int index = i*algorithm.getList().length/this.getWidth();
				int visualHeight = (int)(Main.barMax*(algorithm.getList()[index]+1)*this.getHeight()/Main.upperBound);
				
				//color according to position
				Color temp = primaryColor;
				if (modifying)  {
					int[] indexes = CurrentModifyIndexes();
					for (int j=0;j<indexes.length;j++) if (index==indexes[j]) temp = Color.red;
				}
				else if (startingTime<endTime) {
					if (!isFinished) temp = algorithm.GetColor(index);
					else if (index==(int)animation) temp = Color.red; else if (index<animation) temp = Color.green;
				}
				if (Main.gradient && (temp==primaryColor || temp==secondaryColor)) temp = GetGradientColor(algorithm.getList()[index]); //highlight colors remain
				g2d.setColor(temp);
				
				//draw list
				g2d.fillRect(i,this.getHeight()-visualHeight,1,visualHeight);
			}
			break;
			
			
		case ColorCircle:
			float dist = Main.circleRadius/2*Float.min(this.getWidth(),this.getHeight());
			int slices = Integer.min(Main.maxSlices,algorithm.getList().length);
			for (int i=0;i<slices;i++) {
				//get shown index & angle
				int index = i*algorithm.getList().length/slices;
				double angle1 = 2*Math.PI*i/slices - Math.PI/2,
						angle2 = 2*Math.PI*(i+1)/slices - Math.PI/2;
				
				//color according to whether we are finished or not
				Color temp = new Color(Color.HSBtoRGB((float)algorithm.getList()[index]/Main.upperBound,1f,1f));	
				if (isFinished && index>animation) temp = temp.darker().darker();
				g2d.setColor(temp);
				
				//create triangle for circle slice
				Polygon triangle = new Polygon();
				triangle.addPoint(this.getWidth()/2,this.getHeight()/2);
				triangle.addPoint(this.getWidth()/2 + (int)Math.round(Math.cos(angle1)*dist),this.getHeight()/2 + (int)Math.round(Math.sin(angle1)*dist));
				triangle.addPoint(this.getWidth()/2 + (int)Math.round(Math.cos(angle2)*dist),this.getHeight()/2 + (int)Math.round(Math.sin(angle2)*dist));
				
				//draw list in triangles/slices
				g2d.fill(triangle);
			}
			
			//draw pointers
			if (Main.showPointers && (!isFinished || modifying)) {
				//get pointer indexes
				int[] pointerIndexes;
				if (!modifying) pointerIndexes = algorithm.GetSelectedIndexes();
				else pointerIndexes = CurrentModifyIndexes();
				for (int i=0;i<pointerIndexes.length;i++) {
					//get shown index & angle
					int index = pointerIndexes[i];
					double angle1 = 2*Math.PI*index/algorithm.getList().length - Math.PI/2,
							angle2 = 2*Math.PI*(index+1)/algorithm.getList().length - Math.PI/2;
					
					//create triangle for pointer
					Polygon pointer = new Polygon();
					pointer.addPoint(this.getWidth()/2 + (int)(Math.cos((angle1+angle2)/2)*dist*(1+Main.pointerDistance)),this.getHeight()/2 + (int)(Math.sin((angle1+angle2)/2)*dist*(1+Main.pointerDistance)));
					pointer.addPoint(this.getWidth()/2 + (int)(Math.cos((angle1+angle2)/2 + Main.pointerWidth)*dist*(1+Main.pointerDistance)*(1+Main.pointerSize)),this.getHeight()/2 + (int)(Math.sin((angle1+angle2)/2 + Main.pointerWidth)*dist*(1+Main.pointerDistance)*(1+Main.pointerSize)));
					pointer.addPoint(this.getWidth()/2 + (int)(Math.cos((angle1+angle2)/2 - Main.pointerWidth)*dist*(1+Main.pointerDistance)*(1+Main.pointerSize)),this.getHeight()/2 + (int)(Math.sin((angle1+angle2)/2 - Main.pointerWidth)*dist*(1+Main.pointerDistance)*(1+Main.pointerSize)));
					
					//draw pointer
					g2d.setColor(new Color((int)(255*(1-Main.pointerColorBlend)+algorithm.GetColor(index).getRed()*Main.pointerColorBlend),
											(int)(255*(1-Main.pointerColorBlend)+algorithm.GetColor(index).getGreen()*Main.pointerColorBlend),
											(int)(255*(1-Main.pointerColorBlend)+algorithm.GetColor(index).getBlue()*Main.pointerColorBlend)));
					g2d.fill(pointer);
					g2d.setColor(Color.black);
					g2d.draw(pointer);
				}
			}
			break;
			
			
		case ScatterPlot:
			int pointAmount = Integer.min(Main.maxPoints,algorithm.getList().length);
			float pointSize = Float.max(Main.minPointSize,(float)(Integer.min(this.getWidth(),this.getHeight())*Main.pointSize*Math.pow((float)Main.pointScaleBaseline/pointAmount,Main.pointScaling))/2500);
			int[] selectedIndexes = algorithm.GetSelectedIndexes();
			for (int i=0;i<pointAmount;i++) {
				//get shown index & drawn height
				int index = i*algorithm.getList().length/pointAmount;
				float visualX = (float)(i*this.getWidth())/pointAmount,
					  visualY = (float)(((Main.pointMax-Main.pointMin)*(algorithm.getList()[index]+1) + Main.pointMin*Main.upperBound)*this.getHeight()/Main.upperBound);
				
				//color according to position
				Color temp = primaryColor;
				if (modifying)  {
					int[] indexes = CurrentModifyIndexes();
					for (int j=0;j<indexes.length;j++) if (index==indexes[j]) temp = Color.red;
				}
				else if (startingTime<endTime) {
					if (!isFinished) {
						if (Main.pointAlgorithmColor) temp = algorithm.GetColor(index);
						if (Main.selectedColors) {for(int j=0;j<selectedIndexes.length;j++) if(index==selectedIndexes[j]) {temp = Color.darkGray; break;}}
					}
					else if (index==(int)animation) temp = Color.red; else if (index>animation) temp = Color.darkGray;
				}
				g2d.setColor(temp);
				
				//draw list as points
				g2d.fill(new Ellipse2D.Float(visualX-pointSize/2,this.getHeight()-visualY-pointSize/2,2*pointSize,2*pointSize));
			}
			break;
			
			
		case DisparityDots:
			int dotAmount = Integer.min(Main.maxDots,algorithm.getList().length);
			float maxDist = Main.dotRadius/2*Float.min(this.getWidth(),this.getHeight()),
				  dotSize = Float.max(Main.minDotSize,(float)(Integer.min(this.getWidth(),this.getHeight())*Main.dotSize*Math.pow((float)Main.dotScaleBaseline/dotAmount,Main.dotScaling))/2500),
				  centerX = (float)this.getWidth()/2,
				  centerY = (float)this.getHeight()/2;
			int[] selectedIndexesDot = modifying? CurrentModifyIndexes():algorithm.GetSelectedIndexes();
			for (int i=0;i<dotAmount;i++) {
				//get shown index & angle
				int index = i*algorithm.getList().length/dotAmount,
					sortedIndex = GetSortedIndex(index);
				double angle = 2*Math.PI*i/dotAmount - Math.PI/2;
				
				//get disparity/distance from sorted position
				int distFromSorted = referenceList[index]==referenceList[sortedIndex]? 0:Math.abs(sortedIndex-index)-1;  
				double correctProcent = Math.abs(distFromSorted*2d/algorithm.getList().length - 1);
				
				//color according to whether we are finished or not
				Color temp = new Color(Color.HSBtoRGB((float)algorithm.getList()[index]/Main.upperBound,1f,1f));	
				if (isFinished) {if (index>animation) temp = temp.darker().darker().darker();}
				else  if (Main.showSelectedDots && startingTime<endTime) {
					for (int j=0;j<selectedIndexesDot.length;j++) if (index==selectedIndexesDot[j]) {temp = Color.white; break;}
				}
				g2d.setColor(temp);
				
				//get visual position
				float visualX = centerX+(float)(Math.cos(angle)*maxDist*correctProcent),
					  visualY = centerY+(float)(Math.sin(angle)*maxDist*correctProcent);
				
				//draw list as dots
				g2d.fill(new Ellipse2D.Float(visualX-dotSize,visualY-dotSize,2*dotSize,2*dotSize));
			}
			break;
		}
		
		
		
		
		//draw info box
		if (Main.showInfo) {
			//Yes, the info box is physically painful to adjust, but i didnt want to do any fancy settings for the info box, because tbh, if i just wanted to play around with visuals all day, i wouldve became a designer..
			
			//algorithm
			String modType; int[] Temp = CurrentModifyIndexes();
			if (Temp.length==2) modType = "Shuffling";
			else if(Main.reversedList && reverseIndex<algorithm.getList().length/2) modType = 	"Reverse";
			else modType = "Scramble";
			String Algorithm = modifying? "Modifying... ("+modType+")":Main.sortingAlgorithm.toString()+" - "+algorithm.getList().length+" elements";
			
			//simple info
			if (Main.simpleInfo) {
				g2d.setFont(new Font(Font.DIALOG,Font.PLAIN,(int)Main.fontSize));
				g2d.setColor(Color.white);
				g2d.drawString(Algorithm,Main.fontSize*0.6f	,g2d.getFontMetrics().getHeight()*0.5f+Main.fontSize*0.7f);
			}
			
			//detailed
			else {
				//get strings
				String updatetime = "";
				if (updateTime>1000000000) 	 updatetime = (double)(updateTime/1000000)/1000+"s";
				else if (updateTime>1000000) updatetime = (double)(updateTime/1000)/1000+"ms";
				else						 updatetime = (double)(updateTime/1)/1000+"µs";
				String ArrayAccess = "Total array accesses: "+ToExpString(arrayAccesses,6,4);
				
				//box & font
				g2d.setFont(new Font(Font.DIALOG_INPUT,Font.PLAIN,(int)Main.fontSize));
				g2d.setColor(new Color(40,40,40,100));
				float boxWidth = (float)Double.max(g2d.getFontMetrics().getStringBounds(Algorithm,g2d).getWidth(),g2d.getFontMetrics().getStringBounds(ArrayAccess,g2d).getWidth());
				g2d.fillRect((int)(Main.fontSize*0.4),(int)(Main.fontSize*0.4),(int)Float.max(boxWidth+Main.fontSize,Main.fontSize*18.5f),(int)(g2d.getFontMetrics().getHeight()*10.5f));
				g2d.setColor(Color.white);
				double stepsPrSec = (double)((long)((double)stepsMade/(endTime-startingTime)*10000))/10;
				String stepsPerSec = (stepsPrSec>1000000?ToExpString((long)stepsPrSec,6,4):stepsPrSec+"");
				
				//info text
				g2d.drawString(Algorithm																							,Main.fontSize*0.8f	,g2d.getFontMetrics().getHeight()*0.67f	+Main.fontSize*0.7f);
				g2d.drawString("Visual time: "+(double)(Long.max(endTime/100-startingTime/100,0))/10+"s"							,Main.fontSize*0.8f	,g2d.getFontMetrics().getHeight()*2.1f	+Main.fontSize*0.7f);
				g2d.drawString("Sort time: ~"+updatetime																			,Main.fontSize*0.8f	,g2d.getFontMetrics().getHeight()*2.975f+Main.fontSize*0.7f);
				g2d.drawString(ToExpString(stepsMade,6,4)+" updates, "+stepsPerSec+" u/s"	,Main.fontSize*0.8f	,g2d.getFontMetrics().getHeight()*3.9f	+Main.fontSize*0.7f);
				g2d.drawString(ArrayAccess																							,Main.fontSize*0.8f	,g2d.getFontMetrics().getHeight()*5.4f	+Main.fontSize*0.7f);
				g2d.drawString("Comparisons: "+ToExpString(comparisons,6,4)															,Main.fontSize*0.8f	,g2d.getFontMetrics().getHeight()*6.85f	+Main.fontSize*0.7f);
				g2d.drawString("Swaps: "+ToExpString(swaps,6,4)																		,Main.fontSize*0.8f	,g2d.getFontMetrics().getHeight()*7.7f	+Main.fontSize*0.7f);
				g2d.drawString("Writes to main array: "+ToExpString(writesToMain,6,4)												,Main.fontSize*0.8f	,g2d.getFontMetrics().getHeight()*8.95f	+Main.fontSize*0.7f);
				g2d.drawString("Writes to aux array: "+ToExpString(writesToAux,6,4)													,Main.fontSize*0.8f	,g2d.getFontMetrics().getHeight()*9.7f+Main.fontSize*0.7f);
			}
		}
		
		
		//play audio
		//try catch because of some sneaky errors that can happen due to the structure of visualized implementation (mostly just out of bounds errors)
		if (Main.sortAudio && startingTime<endTime) try {
			int[] temp = new int[1];
			
			//shuffling audio
			if (modifying) temp = CurrentModifyIndexes();
			
			//animation audio
			else if (isFinished) {
				int temp1 = (int)animation;
				temp[0] = algorithm.getList()[Integer.min(algorithm.getList().length-1,temp1)];
				if (ticksDone*Main.audioTickRate>audioTicksDone) PlayTones(temp);
			}
			
			//temp 
			else temp = algorithm.GetSelectedIndexes();
			if (ticksDone*Main.audioTickRate>audioTicksDone) PlayTones(temp);
		} catch (Exception e) {}
		
		
		//when the sort is done, do a little animation
		if (isFinished && !modifying) {
			//stop updating when the animation is finished, and if specified, start a new sort.
			if (animation>algorithm.getList().length) {
				//if we dont reset then stop the updates timer
				if (!Main.resetVisual) {
					//repaint once so the list is white
					timer.stop();
					return;
				}
				
				//if we cycle algorithms, cycle.
				if (Main.cycle) {
					//find current algorithm
					Algorithm[] temp = Algorithm.values();
					int check;
					for (check=0;check<temp.length;check++) if (temp[check]==Main.sortingAlgorithm) break;
					
					//find next algorithm, ignoring specified ignored algorithms
					do check=(check+1)%temp.length; while (Main.ignoredContains(temp[check]));
					
					//update algorithm
					Main.sortingAlgorithm = temp[check];
					algorithm = Main.algorithms.get(Main.sortingAlgorithm);
				}
				
				//reset variables					
				isFinished = false;
				updateTime = 0;
				animation = 0;
				stepsMade = 0;
				swaps = 0;
				comparisons = 0;
				writesToMain = 0;
				writesToAux = 0;
				arrayAccesses = 0;
				if (!Main.showModifiers) {
					algorithm.Import(Lists.GetList(Lists.GetSize(Main.sortingAlgorithm)));
					algorithm.VisualizedSort();
				}
				else {
					algorithm.Import(Lists.GetListType(Lists.GetSize(Main.sortingAlgorithm),Main.listType));
					modifying = true;
					reverseIndex = 0;
					scrambleIndex = 0;
					shufflesMade = 0;
				}
				if (Main.visualization==Main.Visualization.DisparityDots) {
					referenceList = algorithm.getList().clone();
					new CountingSort().Sort(referenceList);
				}
				startingTime = System.currentTimeMillis()+Main.resetVisualWait;
				return;
			}
			
			//go through the list slowly and draw it in green.
			animation+=Main.completionSpeed*algorithm.getList().length*timer.getDelay()*0.0001;
		}
	}
	
	private static String ToExpString(long val, int minDigit, int digits) {
		String temp = "";
		if (val>=Math.pow(10,minDigit)) {
			int exp = (int)Math.floor(Math.log10(val));
			String mant = Double.toString((double)(val/Math.pow(10,exp)));
			temp = mant.substring(0,Integer.min(digits+1,mant.length()))+"e"+exp;
		}
		else temp = val+"";
		return temp;
	}
}

