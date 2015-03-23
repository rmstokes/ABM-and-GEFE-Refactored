



public class UserEvent {
    private int timeStamp;
    private int subjectNumber;
    private int instanceNumber;
    
    public UserEvent(){
        timeStamp = 0;
    }
    
    public UserEvent(int subjectNumber, int instanceNumber, int timestamp){
    	this.subjectNumber = subjectNumber;
    	this.instanceNumber = instanceNumber;
    	this.timeStamp = timestamp;
    }

    /**
     * @return the timeStamp
     */
    public int getTimeStamp() {
        return timeStamp;
    }
    
    public int getSubjectNumber(){
    	return subjectNumber;
    }
    
    public int getInstanceNumber(){
    	return instanceNumber;
    }

    /**
     * @param timeStamp the timeStamp to set
     */
    public void setTimeStamp(int timeStamp) {
        this.timeStamp = timeStamp;
    }
    
    
    public String toString(){
        return ""+timeStamp;    
    }
    
    
    
}

