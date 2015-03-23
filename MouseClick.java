import java.util.Comparator;


public class MouseClick extends UserEvent {

	// instance variables
	private double x;
	private double y;
	private Signature signature;
	private boolean added; //was click added to event list already
	private int groupNumber; //clicks in same group are in same click region of screen
	private boolean grouped; //was click added to group already

	// constructor #1
	public MouseClick() {
		super();
		added = false;
		groupNumber = 0;
	}
	
	public MouseClick(double x, double y){
		super();
		this.x = x;
		this.y = y;
		added = false;
		groupNumber = 0;
	}
	
	//constructor #2
	public MouseClick(int timestamp) {
		setTimeStamp(timestamp);
		added = false;
		groupNumber = 0;
	}
	
	public void add(){
		added = true;
	}
	
	public boolean isAdded(){
		return added;
	}
	public boolean isGrouped(){
		return grouped;
	}
	public void assignToGroup(int groupNumber){
		grouped = true;
		this.groupNumber = groupNumber;
	}
	public int getGroup(){
		return groupNumber;
	}


	public void addSignature(int userNumber, int sessionNumber) {
		if (signature == null) {
			signature = new Signature(userNumber, sessionNumber);
		} else {
			System.out.println("click already signed.");
			System.exit(4);
		}
	}
	
	public void setX(double x){
		this.x = x;
	}
	
	public void setY(double y){
		this.y = y;
	}
	
	public double getX(){
		return x;
	}
	
	public double getY(){
		return y;
	}
	
	public Signature getSignature(){
		return signature;
	}
	
	public boolean equals(MouseClick mc){
		if (this.getSignature().equals(mc.getSignature())) return true;
		return false;
	}
	
	public String toString(){
		
		return "x coord: "+x+" y coord: "+y+" ";//+signature.toString();
	}

	

	
	
	

}