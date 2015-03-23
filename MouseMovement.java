



class MouseMovement extends UserEvent {
    private double xLoc; 
    private double yLoc;
    
  public MouseMovement(){
      super();
      xLoc = 0;
      yLoc = 0;
  }
  
  public MouseMovement(double xLoc, double yLoc){
	  super();
	  this.xLoc = xLoc;
	  this.yLoc = yLoc;
  }
  
 
   
    /**
     * @return the xLoc
     */
    public double getxLoc() {
        return xLoc;
    }

    /**
     * @param xLoc the xLoc to set
     */
    public void setxLoc(double xLoc) {
        this.xLoc = xLoc;
    }

    /**
     * @return the yLoc
     */
    public double getyLoc() {
        return yLoc;
    }

    /**
     * @param yLoc the yLoc to set
     */
    public void setyLoc(double yLoc) {
        this.yLoc = yLoc;
    }
    
    @Override
    public String toString(){
        //return super.toString()+":M:"+xLoc+","+yLoc + "|";
    	return xLoc+","+yLoc + "|";
        
    }
}
