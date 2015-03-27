import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/*
 * 
 * 
 * Template objects can be compared with one another to calculate the distance 
 * between their features. The features for a Template represent the cdf data for a given
 * subject and a given instance of mouse movement.
 * 
 * 
 * 
 */

public class Template extends PointAndClick {

	//private double[][] featureSet;
	private HashMap<Integer, ArrayList<Double>> featureSetImproved; //dynamically sized feature set
	private int numMetrics;
	private int clickRegion;
	private int subjectNumber;
	private int instanceNumber;
	private boolean isProbe;
	
	public Template(HashMap<Integer, ArrayList<Double>> featureSetImproved, int subjectNumber,
			int instanceNumber, double max_x, double max_y, double max_r, int clickRegion) {
		
		
		super(subjectNumber, instanceNumber);
		//this.featureSet = featureSet;
		this.featureSetImproved = featureSetImproved;
		
		numMetrics = 3;
		//clickRegion = 0;
		this.subjectNumber = subjectNumber;
		this.instanceNumber = instanceNumber;
		this.clickRegion = clickRegion;
		this.isProbe = false;
		// featureSetPrime = new HashMap<Integer, Double>();

	}
	
	public boolean isProbe(){
		return isProbe;
	}
	
	public void setAsProbe(){
		isProbe = true;
	}
	public void setAsNotProbe(){
		isProbe = false;
	}

	public int getClickRegion(){
		
		return clickRegion;
	}

	public HashMap<Integer, ArrayList<Double>> getFeatureSet() {
		return featureSetImproved;
	}

	public void setClickRegion(int clickRegion){
		this.clickRegion = clickRegion;
	}
	
	@Override
	public boolean equals(Object a2){
		//base equality on the values in the feature sets
		Template t = (Template)a2;
		boolean equality = this==a2;
		System.out.println("equals: "+equality);
		return this == a2;
	}
	
	public String toString(){
		return "Template subjectNumber: "+subjectNumber+" instanceNumber: "+instanceNumber+" region:"+clickRegion;
	}

}