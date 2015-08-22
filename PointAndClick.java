import java.util.ArrayList;
import java.util.HashMap;




public class PointAndClick {
	
	//private double[][] featureSet;
	//private HashMap<Integer, ArrayList<Double>> featureSetImproved; //dynamically sized feature set
	private int subjectNumber;
	private int instanceNumber;
	private int cmc_rank;
	double max_x; // maximum for direction angle
	double max_y; // maximum for curvature angle
	double max_r; // maximum for curvature distance
	private int numMetrics;
	private String subjectID;
	
	private int clickRegion;
	MouseClick click; //this is the click that ends a point and click action/event.
	
	private String filename; //filename will have valid value only if toString() has been called first.

	private ArrayList<MouseMovement> moves;
	
	public PointAndClick(int subjectNumber,int instanceNumber) {
		//this.featureSet = featureSet;
		//this.featureSetImproved = featureSetImproved;
		this.subjectNumber = subjectNumber;
		this.instanceNumber = instanceNumber;
		this.max_x = max_x;
		this.max_y = max_y;
		this.max_r = max_r;
		numMetrics = 3;
		clickRegion = 0;
		
		filename = null;
		moves = new ArrayList<MouseMovement>();
		// featureSetPrime = new HashMap<Integer, Double>();

	}
	
	public void addMove(MouseMovement move){
		moves.add(move);
		System.out.println("adding move");
	}
	
	public String getFileName(){
	  if (filename == null){
		  System.out.println("Error: null filename. Must call toString() first.");
		  System.exit(4);
	  }
	  return filename;
	}
	
	public void setClick(MouseClick click){
		this.click = click;
	}
	public MouseClick getClick(){
		return click;
	}

	public void setMoves(ArrayList<MouseMovement>moves){
		this.moves = moves;
	}
	public ArrayList<MouseMovement> getMoves(){
		return moves;
	}
	public void setClickRegion(){
	  clickRegion = click.getGroup();
	}
	
	public void setClickRegion(int regionNumber){
		clickRegion = regionNumber;
	}
	
	public int getClickRegion(){
		return clickRegion;
	}

	/**
	 * @return the subjectNumber
	 */
	public int getSubjectNumber() {
		return subjectNumber;
	}

	/**
	 * @param subjectNumber
	 *            the subjectNumber to set
	 */
	public void setSubjectNumber(int subjectNumber) {
		this.subjectNumber = subjectNumber;
	}

	public int numMoves(){
		return moves.size();
	}
	public int getInstanceNumber() {
		return instanceNumber;
	}
	
	public int getRank(){
		return cmc_rank;
	}

	/**
	 * @param instanceNumber
	 *            the instanceNumber to set
	 */
	public void setInstanceNumber(int instanceNumber) {
		this.instanceNumber = instanceNumber;
	}
	
	public void setRank(int rank){
		cmc_rank = rank;
	}
	
	public String returnAllMoves(){
		String output = "";
		

		for (int i = 0; i < moves.size(); i++){
			output+= moves.get(i);
		}
		
		return output;
		
	}
	
	public String getTitle(){
		String output = "Subject "+subjectNumber+" Instance "+instanceNumber+ " Final Click Region "+
				clickRegion;
		return output;
	}
		
	public String toString(){
		//Subject Number: <subjectNumber> Instance Number: <instanceNumber> Region Number: <clickRegion>
		//Moves: <moves coordinates>
		
		String output = "Subject "+subjectNumber+" Instance "+instanceNumber+ " Final Click Region "+
		clickRegion+" Moves ";
		filename = output.substring(0, output.indexOf(" Moves"));
		
		
		for (int i = 0; i < moves.size(); i++){
			output+= moves.get(i);
		}
		return output;
	}
	
	@Override
	public boolean equals(Object pac){
		//base equality on the actual moves in a pac object
		
		System.out.println("inside pac equals...");
		System.exit(0);
		boolean equality = false;
		int count = 0;
		for (MouseMovement mm: moves){
			PointAndClick x = (PointAndClick)pac;
			if (x.getMoves().get(count).getxLoc() == this.getMoves().get(count).getxLoc()
					&& x.getMoves().get(count).getyLoc() == this.getMoves().get(count).getyLoc()){
				equality = true;
			} else {
				equality = false;
				break;
			}
		}
		return equality;
	}


}


