//sus
/** author: Seth Amico, Java docs tutorials(yeah i know it's not necessary)
 * exp4j library from Frank Asseg, repo at github.com/fasseg/exp4j.
 * more info at www.objecthunter.net/exp4j/
 * dependencies.
 * Clear, Color, and Launch compiled by Adrian Veliz
 * further information on discrete sources in their respective files.
 * */
import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.lang.reflect.*;
import javax.script.*;
import net.objecthunter.exp4j.*;
import dependencies.*;

public class Finalscs1{
	
	public static void main(String[]args) throws FileNotFoundException, ScriptException{
		UIBase();
	}
	private static void UIBase() throws ScriptException, FileNotFoundException {
		Scanner input = new Scanner(System.in);
		System.out.println("started!");
		System.out.println("how many functions should be graphed? (max 4)");
		int functionNum = input.nextInt();
		int savedfuncnum = 0;
		String[] notation = {""," second"," third"," fourth"};
		for(int i = functionNum ;i>0;i--){
			String inputFunction = "";
			do{
				System.out.println
				("Please input your" + notation[savedfuncnum] +" function>");
				inputFunction = input.next();
				functionList[savedfuncnum] = inputFunction;
			}
			while(functionNum >4 || functionNum <1);
			savedfuncnum++ ;
			//System.out.println(savedfuncnum);
		}
		int fileNum = 0;
		int[] savefile = new int[4];
		char[] tracerSet = {'.', '*', ',', '+'};
		boolean graphRoll = false;
		System.out.println
		("type 'cus' to input a custom window size,\ntype 'load' to select a slot to load,\nor input 'def' for default window size.");
		String windowtype = input.next();
		String[] prompts = {"X maximum: ", "Y maximum: ","X minimum: ","Y minimum: ","saved to slot:"};
		if(windowtype.equals("cus")){
			System.out.println
			("input an integer window size, according to the parameters.");
			for(int i = 0; i<4;i++){
				System.out.println(prompts[i]);
				savefile[i] = input.nextInt();
			}
			System.out.println(prompts[4]);
			fileNum = input.nextInt();
			saveState(1,savefile,fileNum);
		}
		else if(windowtype.equals("load")){
			boolean deletemode = true;
			do{
				int[] emptyTest = new int[4];
				saveCheck("windowSettings.txt");
				System.out.println
				("select a slot to load, or input 'edit' to enter slot editing mode. ");
				for(int i = 1;i<5;i++){ 
					emptyTest = loadState(i);
					System.out.print ("File "+ (i)+ " "+ Arrays.toString(loadState(i)));
					if(isEmpty(emptyTest)){
						 System.out.print(" (empty)\n");
					 }
					 else{
						 System.out.print("\n");
					 }
				}
				String fileMode = input.next();
				if(fileMode.equals("edit")){
					System.out.println
					("type a function to edit/delete then selected slot(enter each individually unfortunately), or input 'reset' to reset all slots.");
					String editmode = input.next();
					//tried to use a nextline to take the whole line, 
					//but it kept grabbing part of the line or skipping the whole thing.
					//later found out it was  tripping on /n.
					if(editmode.equals("reset")){
						resetSaveFile();
						Clear.clearScreen();
						System.out.println("file deleted.");
						Clear.sleep(1);
					}
					else if(editmode.equals("edit")){
						System.out.println("file num? ");
						int file = input.nextInt();
						System.out.println("input new information for file.");
							for(int i = 0; i<4;i++){
								System.out.println(prompts[i]);
								savefile[i] = input.nextInt();
							}
							fileNum = file;
							saveState(1,savefile,fileNum);
							System.out.println("slot overwritten.");
							Clear.sleep(2);
							Clear.clearScreen();
							Clear.sleep(1);
					}
					else if(editmode.equals("exit")){
						System.exit(0);
					}
					else{
						System.out.print(" filenum?");
						int file = input.nextInt();
						System.out.println("deleting slot "+ file);
						eraseSlot(file);
						Clear.sleep(2);
						Clear.clearScreen();
						Clear.sleep(1);
					}
				}
				else if(fileMode.equals("never_gonna")){
					Clear.clearScreen();
					System.out.println(Color.BG_CYAN+
					Color.BLACK+"GIVE YOU UP"+Color.RESET);
					graphRoll =true;
				}
				else if(fileMode.equals("exit")){
					System.exit(0);
				}
				else if(isEmpty(loadState(Integer.valueOf(fileMode)))){
					Clear.clearScreen();
					System.out.println("it's empty!");
					Clear.sleep(1);
				}
				else{
					fileNum = Integer.valueOf(fileMode);
					deletemode = false;
				}
			}while(deletemode);
		}
		else if(windowtype.equals("exit")){
			System.exit(0);
		}
		else{
			System.out.println("default size loaded.");
			fileNum = 0;
		}
		char[][] graphMatrix = createGraphMatrix(fileNum);
		 /*that graph bug i showed in the demo was caused by me organizing this method and initializing
		  *  the array too early, before any save window sizes were implemented. 
		  * this resulted with it reading 0 no matter what, and loading the default size.
		  * */
		savefile = loadState(fileNum);
		for(int i = 0; i<functionNum;i++){
			fillGraph(1,loadState(fileNum),functionList[i],graphMatrix, tracerSet[i], tracerColors[i]);
		}
		Clear.clearScreen();
		System.out.println("function(s): ");
		for(int i = 1; i< functionNum+1;i++){
			System.out.print(" <" + i + "> " + functionList[i-1]);
		}
		System.out.println("| |");
		
		graph(graphMatrix,loadState(fileNum),graphRoll);
		boolean functionMenu = true;
		do{
		System.out.println("Functions: enter a command listed below to activate.");
		System.out.println
		("| 'pan' (pan window) \n| 'valueAt' (calculate value at a point) \n| 'refresh' (exactly what you think it does) \n| 'editFunc' (edit the current functions graphed)");
		String func = input.next();
			if(func.equals("pan")){
				System.out.println("by how much on the x axis?");
				int xMax = input.nextInt();
				System.out.println("by how much on the y axis?");
				int yMax = input.nextInt();
				moveGraph(fileNum, functionNum,xMax,yMax);
			}
			else if(func.equals("valueAt")){
				boolean isPaused = true;
				do{
					System.out.println("which function?");
					int funcValueAt = input.nextInt();
					System.out.println("find the value at x= ");
					double valueAtX = input.nextDouble();
					System.out.println("the value of function "+funcValueAt
					+ " at " + valueAtX + " is "+
					functionOut(1,valueAtX,functionList[funcValueAt-1]));
					System.out.println("calculate another?");
					String pause = input.next();
					if(pause.equals("n") || pause.equals("no")){
						isPaused = false;
					}
				 }while(isPaused);
				 Clear.sleep(1);
				 Clear.clearScreen();
				 refreshGraphs(fileNum,functionNum);
			}
			else if(func.equals("refresh")){
				Clear.clearScreen();
				refreshGraphs(fileNum,functionNum);
				
			}
			else if(func.equals("editFunc")){
				System.out.println("select a function to edit:");
				for(int i = 1; i< functionNum+1;i++){
					System.out.println(functionList[i-1]);
				}
				int selectedFile = input.nextInt();
				System.out.println("input your new function");
				if(selectedFile <= functionNum & selectedFile>0){
					functionList[selectedFile] = input.next();
				}
				System.out.println("replaced function.");
				Clear.sleep(1);
				Clear.clearScreen();
				refreshGraphs(fileNum,functionNum);
			}
			else if(func.equals("exit")){
				System.out.println("exiting...");
				Clear.sleep(1);
				Clear.clearScreen();
				System.exit(1);
			}
			else{
				Clear.clearScreen();
				refreshGraphs(fileNum,functionNum);
			}
		}while(functionMenu);
	}
	
	private static char[][] createGraphMatrix(int fileNum) throws FileNotFoundException{
		int[] savefile= loadState(fileNum);
		System.out.println(Arrays.toString(savefile));
		char[][] graphMatrix = new char[Math.abs(savefile[0]-savefile[2])][Math.abs(savefile[1]-savefile[3])];
		return graphMatrix;
	}
	
	private static void refreshGraphs(int windowsave,int functionNum) throws FileNotFoundException{/** not done*/
		int[] savefile = loadState(windowsave);
		//System.out.println(Arrays.toString(savefile));
		char[][] graphMatrix = createGraphMatrix(windowsave);
		char[] tracerSet = {'.', '*', ',', '+'};
		//String[] tracerColors = {Color.CYAN,Color.RED,Color.GREEN,Color.MAGENTA};
		for(int i = 0; i<functionNum;i++){
			fillGraph(1,loadState(windowsave),functionList[i],graphMatrix, tracerSet[i], tracerColors[i]);
		}
		System.out.println("function(s): ");
		for(int i = 1; i< functionNum+1;i++){
			System.out.print(" <" + i + "> " + functionList[i-1]);
		}
		graph(graphMatrix,savefile,false);
	}
	
	private static String[] tracerColors = {Color.CYAN,Color.RED,Color.GREEN,Color.MAGENTA};
	
	private static boolean isEmpty(int[] input){ //feels like something that java would have in vanilla, but i can't remember so..
		boolean isEmpty = true;
		for(int i = 0; i< input.length;i++){
			if(input[i] !=0){
				isEmpty = false;
			}
		}
		return isEmpty;
	}
	
	private static double valueAt(int x, int funcnum, String function, int windowsave) throws FileNotFoundException{
		return functionOut(1,x,functionList[funcnum]);
	}
	
	private static double findMax(double leftBound, double rightBound, String function){/** not done, intended for the final build but ran out of time.*/
		return 3.0;
	}
	
	// both of these were intended for the final build but ran out of time.
	
	private static double findMin(double leftBound, double rightBound,String function){/** not done*/
		
		return 3.0;
	}
	
	private static String[] functionList = new String[4];

	private static int[] loadState(int fileNum) throws FileNotFoundException{
		if(fileNum == 0){
			int[] defaultpreset = {15,15,-15,-15};
			return defaultpreset;
		}
		if(fileNum>4||fileNum<1){
			throw new IllegalArgumentException("save slot number must be between 1 and 4!");
		}
		saveCheck("windowSettings.txt");
		fileNum--;
		Scanner input = new Scanner(new File("windowSettings.txt"));
		int[] saveFile = new int[4];
		String slotnum = "|slot"+fileNum;
		while(input.hasNext()){
			String currentLine = input.next();
			if(currentLine.equals(slotnum)){
				for(int i = 0; i<saveFile.length;i++){
					saveFile[i] = input.nextInt();
				}
				break;
			}
		}
		return saveFile;
	}	
	
	private static File saveCheck(String fileName) throws FileNotFoundException{
		File file = new File(fileName);
		Path filePath = Paths.get(fileName);
		if(!file.exists()){
			try {// try-catch snippet taken from java docs.
				Files.createFile(filePath);
			}
			catch (IOException x) {
				System.err.format("createFile error: %s%n", x);
			}
			System.out.println("creating new save file...");
		}
		return file;
	}
	
	private static void saveState(double scale, int[] currentsave ,int fileNum) throws FileNotFoundException{
		if(fileNum>4||fileNum<1){
			throw new IllegalArgumentException("save slot number must be between 1 and 4!");
		}
		fileNum--;
		File file = saveCheck("windowSettings.txt");
		Path filePath = Paths.get("windowSettings.txt");
		Scanner fileReader = new Scanner(file);
		Scanner dataCopier = new Scanner(file);
		int saveNum = 0;
		//int[] currentsave = {xMax,yMax,xMin,yMin};
		//System.out.println(Arrays.toString(currentsave));
		int[][] saveSlots = new int[4][currentsave.length];
		//String compiledslot = "|slot" + fileNum + " " + scale+ " " + xMax + " " + yMax + " " + xMin + " " + yMin + " ";
		String slotnum = "|slot"+fileNum;
		if(fileReader.hasNextLine()){
			while(fileReader.hasNextLine()){
				String currentSlot = fileReader.nextLine();
				dataCopier.next();
					for(int p = 0; p<currentsave.length;p++){
							saveSlots[saveNum][p] = dataCopier.nextInt();
					}
				if(currentSlot.startsWith(slotnum)){
					//System.out.println("found slot, overwrote!");
					for(int i = 0;i<currentsave.length;i++){
						saveSlots[saveNum][i]= currentsave[i];
						//System.out.println(saveSlots[fileNum][i]);
					}
				}
				saveNum++;
				//System.out.println(Arrays.deepToString(saveSlots) + " "+ saveNum);
			}
		}
		else{
			//System.out.println("created new slot!");
			for(int i = 0;i<currentsave.length;i++){
				saveSlots[fileNum][i]= currentsave[i];
			}
		}
		PrintStream toFile = new PrintStream(file);
		for(int i = 0;i<saveSlots.length;i++){
			toFile.print("|slot"+i+" ");
			for(int p = 0;p<saveSlots[0].length;p++){
				toFile.print(saveSlots[i][p]+" ");
				//System.out.println("wrote to file!");
			}
			toFile.print("\n");
		}
		//toFile.println(saveslot);
	}
	
	private static void eraseSlot(int fileNum) throws FileNotFoundException{
		int[]emptySlot = {0,0,0,0};
		saveState(1.0,emptySlot,fileNum);
	}
	
	private static void graphRoller(String test){
		Launch.launch(test);//your move.
	}
	
	
	private static void resetSaveFile() throws FileNotFoundException{
		/**this originally deleted the file, thus all the existence redundancy,
		* but I learned later that files.delete() doesn't play nice if anything else is accessing the file(any scanner etc.)
		* , thus it just overwrites everything.**/
		File save = saveCheck("windowSettings.txt");
		//save.delete()
		for(int i = 1; i<5;i++){
			eraseSlot(i);
		}
		//System.out.println(save.exists());    :'(
	}
	
	private static void moveGraph(int windowsave, int functionNum, int shiftX, int shiftY)throws FileNotFoundException{/** not done*/
		int[] save = loadState(windowsave);
		int[] shifts = {shiftX,shiftY};
		for(int i = 0;i<2;i++){
			 save[i] += shifts[i];
			 save[i+2] += shifts[i];
		}
		saveState(1,save,windowsave+1);
		refreshGraphs(windowsave+1,functionNum);
	}	
	
	private static char[][] fillGraph(double scale,int[] saveFile, String function, 
	char[][] graphMatrix, char tracer, String tracerColor)throws FileNotFoundException{
		// chars don't particularly like having strings appended to them, unless they become strings, so color will have to stay out.
		int xMax = saveFile[0];
		int yMax = saveFile[1];
		int xMin = saveFile[2];
		int yMin = saveFile[3];
		//System.out.println(xMin);
		if(xMin >= xMax){
			throw new IllegalArgumentException("window's x maximum (" + xMax + ") must be greater than its x minimum (" + xMin + ").");
		}
		else if(yMin >= yMax){
			throw new IllegalArgumentException("window's y maximum (" + yMax + ") must be greater than its y minimum (" + yMin + ").");
		}
		yMax++;
		xMax++;
		int relativeX = xMin;
		//char[][] graphMatrix = new char[Math.abs(xMax-xMin)][Math.abs(yMax-yMin)];
		int prevY = (int)functionOut(scale, relativeX-1,function);
		int i = 0;
		do{
			int yLoc = (int)functionOut(scale, relativeX, function);//sets y location based on the x location
				//System.out.println("\n" +yLoc +"<yloc "+ relativeX + "x " + i+"i "+prevY+"prevY ");
					if(prevY< yLoc){
						for(int p = prevY+1; p<= yLoc; p++){
							if(p<= graphMatrix[0].length-1+yMin & p>= yMin){
								graphMatrix[i][p-yMin] = tracer;
								//System.out.print(p + "p ");
							}
						}
					}
					else if(prevY >= yLoc){
						int nextY = (int)functionOut(scale,relativeX+1,function);
						for(int p = yLoc; p>nextY; p--){
							if(p<= graphMatrix[0].length-1+yMin & p>= yMin){
								graphMatrix[i][p-yMin] = tracer;
								//System.out.print(p + "np ");
							}
						}
					}
			if(yLoc<= graphMatrix[0].length-1+yMin & yLoc>= yMin){
				graphMatrix[i][yLoc-yMin] = tracer;
				//System.out.print(p + "p ");
			}
			prevY = yLoc;
			i++;//increments i in direct relation to x
			relativeX++;
		}
		while(i<= graphMatrix[0].length-1);
		//graph(graphMatrix, saveFile);
		return graphMatrix;
	}
	
	private static double functionOut(double scale, double i, String function){
		double varX = i*scale;
		//function += "+1";
		Expression calc = new ExpressionBuilder(function)
		.variables("x")
		.build()
		.setVariable("x", varX);
		double result1=calc.evaluate(); /** Code taken from exp4j lib.*/
		//System.out.print(" " + result1);
		return result1;
	}
	
	private static void graph(char[][] graphMatrix, int[] saveFile,boolean sf){
		int xMax = saveFile[0];
		int yMax = saveFile[1];
		int xMin = saveFile[2];
		int yMin = saveFile[3];
		int relativeX = xMin;
		String se = "";
		//System.out.print(Arrays.toString(saveFile));
		System.out.println(graphMatrix.length);
		System.out.println(graphMatrix[0].length);
		yMax--;
		System.out.print(xMin);
		if(sf){
			graphRoller(se);
		}for( int r = graphMatrix.length-3; r >=0; r--){
			System.out.print(Color.RED+"__"+Color.RESET);
		}
		System.out.println(xMax);
		for( int i = graphMatrix[0].length-1; i >=0; i--){
			relativeX = xMin;
			if(yMax == 0){
				System.out.print(Color.RED+" |"+Color.RESET);
				for(int p = graphMatrix.length-1; p >=0; p--){
						if(relativeX ==0){
							System.out.print("+");
						}
						else{
							System.out.print("--");
						}
						relativeX++;
				}
				System.out.print(Color.RED+"|"+Color.RESET+"0\n");
			}
			else{
				System.out.print(Color.RED+" |"+Color.RESET);
				for(int x = 0; x <= graphMatrix.length-1; x++){
					if(relativeX == 0){
						System.out.print("|");
					}
					else{
						if(graphMatrix[x][i] != '\u0000'){
						System.out.print(graphMatrix[x][i] + " ");
						}
						else{
							System.out.print("  ");
						}
					}
					relativeX++;
				}
			System.out.print(Color.RED+"|"+Color.RESET+yMax +"\n");
			}
			yMax--;
		}
		System.out.print(" "+ xMin);
		for( int r = graphMatrix.length-3; r >=0; r--){
			System.out.print(Color.RED+"__");
		}
		System.out.println(Color.RESET+xMax);
		if(sf){
			se = "https://www.youtube.com/watch?v=dQw4w9WgXcQ";
		}
		sf = false;
	}
}
