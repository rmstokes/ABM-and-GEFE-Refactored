import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;


/*
 * 
 * Feed is the class that accesses pacList
 * by reading the names from a pacFileListNames file.
 * pacList = list of all PointAndClick actions for each user over all sessions.
 * PointAndClick = continuous movements followed by a click
 * 
 * Feed will transform pacList into MouseMovement objects to be sent to Preprocessor
 */



public class Feed {

	private String pacFileListNames;
	private String pathToCWD; //path to current working directory
	private ArrayList<PointAndClick> pacList;
	private String fileListLocation;
	
	public Feed(String pacFileListNames) {
		this.pacFileListNames = pacFileListNames;
		pathToCWD = System.getProperty("user.dir");
		fileListLocation = pathToCWD+"\\"+pacFileListNames;
		//System.out.println("fileListLocation: "+fileListLocation);
	}
	
	public ArrayList<PointAndClick> generatePACObjects() throws IOException{
		//input is list of file names; output: list of pac objects w/ movements embedded
		
		//read every name from pacFileListNames
		//open the files and write the contents into a stream/list of mouse movement objects
		//associate each mouse movement list with a pac object
		
		System.out.println("pacListToMovementStream");
		BufferedReader br = null;
		ArrayList <String> fileList = new ArrayList<String>();
		ArrayList <MouseMovement> movementList = null;
		ArrayList <PointAndClick> pacList = new ArrayList<PointAndClick>();
		fileList = generateFileList();
		
		String moveString = "";
		for (String title: fileList){
			movementList = new ArrayList<MouseMovement>();
			//open every file and read it's movements into pac objects
			File pacFile = new File(title+".txt");
			try {
				BufferedReader br2 = new BufferedReader(new FileReader(pacFile));
				
				while (true){
					//convert file contents into a string.
					String line = br2.readLine();
					if (line == null) break;
					moveString += line;
				}
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			//change moveString into a list of moves
			//create a pac object based on name of s and subject#, instance#
			movementList = generateMovementList(moveString);
			int subjectNumber = obtainSubjectNumber(title);
			int instanceNumber = obtainInstanceNumber(title);
			PointAndClick pac = new PointAndClick(subjectNumber, instanceNumber);
			pac.setMoves(movementList);
			pacList.add(pac);			
		}//end fileList loop
		//System.exit(0);
		System.out.println("pacList.size: "+pacList.size());
		return pacList;
	}

	private ArrayList<MouseMovement> generateMovementList(String moveString) {
		//traverse string to create mouse movement objects and append to movement list
		ArrayList<MouseMovement> moves = new ArrayList<MouseMovement>();
		
		//System.out.println("inside generate movementList...");
		
		while (true){
			try{
			  int x = (int) Double.parseDouble(moveString.substring(0, moveString.indexOf(",")).trim());
			  int y = (int) Double.parseDouble(moveString.substring(moveString.indexOf(",")+1, 
					moveString.indexOf("|")).trim());
			  MouseMovement move = new MouseMovement(x, y);
			  moves.add(move);
			
			  //advance the moveString forward past next pipe
			  moveString = moveString.substring(moveString.indexOf("|")+1);
			} catch (IndexOutOfBoundsException e){
				break;
			}
		}
		
		return moves;
	}

	private int obtainInstanceNumber(String title) {
		//obtain instance# from title
		int instanceNumber = Integer.parseInt(title.substring(19, title.indexOf("F")).trim());
		return instanceNumber;
	}

	private int obtainSubjectNumber(String title) {
		//obtain subject# from title		
		int subjectNumber = Integer.parseInt(title.substring(8, title.indexOf("I")).trim());		
		return subjectNumber;
	}

	private ArrayList<String> generateFileList() {
		ArrayList <String> fileList = new ArrayList<String>();
		BufferedReader br = null;
		try {
			File file = new File(pacFileListNames);
			br = new BufferedReader(new FileReader(file));
			//use br to build up a fileList
			//then use fileList to open up pac files and read moves into objects.
			while (true){				
				String line = br.readLine();
				if (line == null) break;
				fileList.add(line); //add names of all pac files to fileList
			}			
			br.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return fileList;
	}
	
	

}
