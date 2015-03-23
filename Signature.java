
/*
 * 
 * This class is a file signature
 * file signature is a way to label a click to determine
 * from which file it belongs to.
 * 
 * 
 * 
 */


public class Signature {
	
	//instance variables
	private int sessionNumber;
	private int userNumber;
	
	
	//constructor
	public Signature(int userNumber, int sessionNumber){
		this.userNumber = userNumber;
		this.sessionNumber = sessionNumber;
	}
	
	public boolean compareSignatures(Signature sig1, Signature sig2){
		//check if signatures match.
		if (sig1.getSessionNumber() == sig2.getSessionNumber() && 
				sig1.getUserNumber() == sig2.getUserNumber()){
			
			return true;
			
		}
		return false;
	}

	private int getUserNumber() {
		
		return userNumber;
	}

	private int getSessionNumber() {
		
		return sessionNumber;
	}
	
	public String toString(){
		
		return "<userNumber: "+userNumber+", sessionNumber: "+sessionNumber+">";
	}
	
	public boolean equals(Signature sig){
		if (this.toString().equals(sig.toString()))
		return true;
		return false;
	}

}
