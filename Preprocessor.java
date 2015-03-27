import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

/*
 * 
 * This class takes in a list of all pac objects for all users
 * MouseMovements are extracted for every pac object individually
 * and passed to functions to generate metrics and create cdf files
 * cdf files are stored in memory and passed to TemplateListGenerator
 * 
 * 
 */

public class Preprocessor {

	private ArrayList<MouseMovement> al;
	public ArrayList<Double> directionResults;
	public ArrayList<Double> curvatureAngleResults;
	public ArrayList<Double> curvatureDistanceResults;
	//private boolean flag = false;
	// private static String subjectName;
	public double x_interval;
	public double y_interval;
	public double ratio_interval;
	public static double x_max;
	public static double y_max;
	public static double ratio_max;
	private static int num_bins;
	private static int numSubjects;
	private int num_x;
	private int num_y;
	private int num_r;
	private static int numInstances;
	private static int numTemplates;
	private ArrayList<PointAndClick> pacList;
	private ArrayList<String> cdfFileList;

	public Preprocessor(ArrayList<PointAndClick> pacList, double x_interval, double y_interval, 
			double r_interval, double x_max, double y_max, double ratio_max) {
		//xyr intervals will be used for binSizes in cdf function
		this.pacList = pacList;
		directionResults = new ArrayList<Double>();
		curvatureAngleResults = new ArrayList<Double>();
		curvatureDistanceResults = new ArrayList<Double>();
		this.x_interval = x_interval;
		this.y_interval = y_interval;
		ratio_interval = r_interval;
		this.x_max = x_max;
		this.y_max = y_max;
		this.ratio_max = ratio_max;
		cdfFileList = new ArrayList<String>();
	}

	
	public String cdf(ArrayList<Double> metricList, double binSize, int subjectNumber, int instanceNumber,
			int clickRegion, double max) {
		// print out the number of values from metricList that fit into a given bin.
		// do this for all the bins until all the items in the list are assigned
		// to a bin.
		boolean flag = true;
		//add extension to filename to include the region#
		//filename +=" Region: "+clickRegion;
		String cdfType = "";
		if (metricList.equals(directionResults)){
			cdfType = "direction";
		} else if (metricList.equals(curvatureAngleResults)){
			cdfType = "curvature angle";
		} else {
			cdfType = "curvature distance";
		}
				
		//System.exit(0);
		String filename = "Subject "+subjectNumber+" Instance "+instanceNumber+" Region "+clickRegion;

		PrintWriter out;
		

		// create low and high to represent range of bin
		double low = 0.0;
		double high = 0.0;
		high = low + binSize;
		
		if (metricList.equals(directionResults)){
			//overwrite the contents of cdf file base on file name
			//but only when calculating the direction results
			//every other metric will be appended to the file that directionResults overwrote
			flag = false;
			//System.out.println("flag set to false for direction result.");
			//System.exit(0);
		}

		try {
			out = new PrintWriter(new BufferedWriter(new FileWriter(filename,
					flag)));
			if (!flag) {
				out.println(subjectNumber + " x_interval: " + x_interval
						+ " y_interval: " + y_interval + " ratio_interval: "
						+ ratio_interval + " x_max: " + x_max + " y_max: "
						+ y_max + " ratio_max: " + ratio_max + " clickRegion: "+clickRegion);
			}
			out.print("cdf " + cdfType + ": ");
			out.close();
			//flag = true;

		} catch (IOException e) {
			// exception handling left as an exercise for the reader
		}

		int count = 0;
		double percentage = 0.0;
		int bin = 0;
		while (low <= max) {
			// keep creating bins and counting how many elements fall within the
			// bins
			// until max# has been inserted and counted in a bin.

			// loop through list to find number of items below high point of
			// bin.
			for (int i = 0; i < metricList.size(); i++) {
				if ((double) metricList.get(i) <= high) {
					// found item within bin between low and high.
					count++;
				}
			}// end for loop

			// transform the count to a percentage for cdf.
			percentage = (double) (count) / metricList.size();
			System.out.println("filename: "+filename);
			System.out.println("percentage: " + percentage);
			System.out.println("count: "+count);
			System.out.println("metricList.size(): "+metricList.size());
			//System.exit(0);
			try {
				out = new PrintWriter(new BufferedWriter(new FileWriter(
						filename, true)));
				out.print(percentage + " ");
				out.close();
			} catch (IOException e) {
				// exception handling left as an exercise for the reader
			}

			count = 0;
			bin++;
			low = high;
			high = low + binSize;
			//System.out.println("low: "+low);
			//System.out.println("high: "+high);
			//System.exit(0);
		}// end while loop

		try {
			out = new PrintWriter(new BufferedWriter(new FileWriter(filename,
					true)));
			out.println();
			out.close();
		} catch (IOException e) {
			// exception handling left as an exercise for the reader
		}
		
		return filename;

	}//end cdf method
	
	
	
	public void extractor() {
		// for each pac object in pacList
		// extract out all the moves and send to the start() method
		// so metrics will be calculated & cdf function generated.
		int count = 0;
		boolean last = false;
		for (PointAndClick pac : pacList) {
			if (count == pacList.size()-1) last = true; //on last loop pass in last = true to start
			start(pac.getMoves(), pac.getClickRegion(), pac.getSubjectNumber(), 
					pac.getInstanceNumber(), last);
			count++;
			
		}
	}

	private void start(ArrayList<MouseMovement> moves, int clickRegion, int subjectNumber, 
			int instanceNumber, boolean last) {
		//input: moves from a single pac object
		//output: upon return all metrics for pac are calculated
		//and stored inside the x,y,r global arraylists
		MouseMovement A;
		MouseMovement B;
		MouseMovement C;
		int count = 0;

		if (moves.size() < 3) {
			System.out.println("Not enough movements in a file.");
			System.exit(0);
		}
		while (moves.size() >= 3) {
			System.out.println("moves.size(): "+moves.size());
			A = moves.get(0);
			B = moves.get(1);
			C = moves.get(2);
			preprocess(A, B, C);
			moves.remove(0);
			count++;
		}

		// System.out.println("curvatureDistanceResults.size(): "+curvatureDistanceResults.size());
		// sort all of the metrics lists
		directionResults.sort(null);
		curvatureAngleResults.sort(null);
		curvatureDistanceResults.sort(null);

		num_x = directionResults.size();
		num_y = curvatureAngleResults.size();
		num_r = curvatureDistanceResults.size();

		System.out.println("num_x: " + num_x + " num_y: " + num_y + " num_r: "
				+ num_r);
		
		//call cdf() before returning; metrics are global
		//at this line we have xyr for single pac object
		cdf(directionResults, x_interval, subjectNumber, instanceNumber, clickRegion, x_max);
		cdf(curvatureAngleResults, y_interval, subjectNumber, instanceNumber, clickRegion, y_max);
		String fileName = cdf(curvatureDistanceResults, ratio_interval, subjectNumber, instanceNumber, clickRegion, ratio_max);
		//compile list of all file names
		cdfFileList.add(fileName);
		
		if (last) writeCDF(); //write the entire list of cdfFile names to a file
	}

	private void writeCDF() {
		//write cdf file names to memory location
		//String pacListFileNames = "pacFileListNames.txt";
		PrintWriter pw = null;
		try {
			pw = new PrintWriter("cdfFileList.txt");
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		System.out.println("names of files in cdf file list: ");
		for (String fileName: cdfFileList){
			pw.println(fileName);
			System.out.println("fileName: "+fileName);
		}
		
		pw.close();
		
	}


	private void preprocess(MouseMovement a, MouseMovement b, MouseMovement c) {
		// this is the method that will delegate work to make sure metrics are
		// computed.
		// everything that is computed will be stored into appropriate list
		// structure.
		
		directionResults.add(direction(a, b, c));
		curvatureAngleResults.add(getCurvatureAngle(a, b, c));
		curvatureDistanceResults.add(getCurvatureDistance(a, b, c));
		
		//System.out.println("metrics have been calculated.");
		//System.out.println("directionResuls size: "+directionResults.size());
		//System.exit(0);
	}
	
	private double direction(MouseMovement A, MouseMovement B, MouseMovement C) {
		// find the angle that is formed by line AB and the horizon if we make
		// MouseMovement A at the origin (0,0)
		
		MouseMovement reflectionOfB = new MouseMovement();

		double direction = 0.0;

		// calibrate x,y so that we can refer to MouseMovement b as if it were
		// plotted
		// on
		// coordinate plane with MouseMovement A as origin
		double calibrated_X = B.getxLoc() - A.getxLoc(); // quadrants I & IV
		double calibrated_Y = A.getyLoc() - B.getyLoc(); // quadrants I & II

		// reflection point is point projected on a normal x,y plane with A at
		// (0,0)
		// instead of how pixel coordinates are laid out where y-axis increases
		// as it goes down.
		reflectionOfB.setxLoc(calibrated_X);
		reflectionOfB.setyLoc(calibrated_Y);

		// System.out.println("x,y for B if A is centered at (0,0): ("
		// + reflectionOfB.getxLoc() + "," + reflectionOfB.getyLoc() + ")");
		// System.out.println(reflectionOfB.getyLoc());
		direction = atan2(reflectionOfB.getyLoc(), reflectionOfB.getxLoc());
		
		//System.out.println("direction: " + direction);
		return direction;
	}

	private double atan2(double y, double x) {
		// calculate angle in degrees using coordinates of MouseMovement B in
		// relation
		// to MouseMovement A
		// MouseMovement A will serve as the origin so we can assess the
		// quadrant of
		// line AB.
		// angle is that between horizontal line and line AB

		double degrees = 0.0;
		degrees = Math.toDegrees(StrictMath.atan2(y, x)); // default impl.
		// delegates to
		// StrictMath
		if (degrees < 0) {
			degrees = 360 - Math.abs(degrees);
		}
		return degrees;
	}

	private double getCurvatureAngle(MouseMovement A, MouseMovement B,
			MouseMovement C) {

		// find distances a, b, and c
		double a = 0.0;
		double b = 0.0;
		double c = 0.0;

		// calculate the distances
		// distance = sqrt((x2-x1)^2+(y2-y1)^2)
		double square1 = Math.pow(B.getxLoc() - A.getxLoc(), 2);
		// System.out.println("square1: " + square1);
		// System.out.println("B.getyLoc()-A.getyLoc(): " + (B.getyLoc() -
		// A.getyLoc()));
		double square2 = Math.pow(B.getyLoc() - A.getyLoc(), 2);
		// System.out.println("square2: " + square2);
		a = Math.sqrt(square1 + square2);

		square1 = Math.pow(C.getxLoc() - B.getxLoc(), 2);
		square2 = Math.pow(C.getyLoc() - B.getyLoc(), 2);
		b = Math.sqrt(square1 + square2);

		square1 = Math.pow(C.getxLoc() - A.getxLoc(), 2);
		square2 = Math.pow(C.getyLoc() - A.getyLoc(), 2);
		c = Math.sqrt(square1 + square2);

		// System.out.println("a: " + a + " b: " + b + " c: " + c);
		// calculate curvature angle
		double curvature = 0.0;
		double a_squared = Math.pow(a, 2);
		double b_squared = Math.pow(b, 2);
		double c_squared = Math.pow(c, 2);

		curvature = Math.toDegrees(Math
				.acos((a_squared + c_squared - b_squared) / (2 * a * c)));

		if (curvature != curvature) {
			curvature = 0; // if curvature is NaN then read it as 0.
		} // System.out.println("curvature: " + curvature);
		return curvature;
	}

	private static double getCurvatureDistance(MouseMovement A,
			MouseMovement B, MouseMovement C) {
		// we want distance of perpendicular line from MouseMovement B to line
		// AC
		// perpendicular line is line with slope that is negated reciprocal of
		// AC slope.

		// find slope of AC
		double rise = A.getyLoc() - C.getyLoc();
		double run = A.getxLoc() - C.getxLoc();

		double mAC = rise / run; // slope of line AC

		// find out quadrant to determine if slope is positive or negative
		// quadrant I means positive slope
		// quadrant III means positive slope
		if (C.getxLoc() < A.getxLoc() && C.getyLoc() > A.getyLoc()) {
			// quadrant III
			// System.out.println("quadrant III");
		} else if (C.getxLoc() > A.getxLoc() && A.getyLoc() > C.getyLoc()) {
			// quadrant I
			// System.out.println("quadrant I");
			run = C.getxLoc() - A.getxLoc();
			mAC = rise / run;
		} else {
			// System.out.println("negate slope");
			mAC *= -1;
		}

		// System.out.println("slope of segment AC: " + mAC);
		// find slope of perpendicular aka slope of segment BD
		double mBD = 0.0;
		if (mAC != 0) {
			mBD = (1 / mAC) * -1;
		} else {
			mBD = 90;
		}
		// mBD = (1/mAC)*-1;
		// System.out.println("slope of segment BD: " + mBD);

		// line is the normalized B after setting A to be (0,0).
		double calibrated_X = B.getxLoc() - A.getxLoc(); // quadrants I & IV
		double calibrated_Y = A.getyLoc() - B.getyLoc(); // quadrants I & II

		// normalized B is B on a coordinate plane which has MouseMovement A as
		// origin.
		MouseMovement normalizedB = new MouseMovement(calibrated_X,
				calibrated_Y);
		MouseMovement normalizedA = new MouseMovement(0, 0);
		// line is y-y1 = m(x-x1) this is MouseMovement slope form
		// y=m(x-x1)+y1
		// y=mx-mx1+y1 intercept is mx1+y1

		// System.out.println("normalizedB.getxLoc(): " +
		// normalizedB.getxLoc());
		double BD_intercept = -normalizedB.getxLoc() * (mBD)
				+ normalizedB.getyLoc();
		double AC_intercept = -normalizedA.getxLoc() * (mAC)
				+ normalizedA.getyLoc();

		if (B.getxLoc() == C.getxLoc()) {
			BD_intercept = normalizedB.getxLoc(); // special case if BC = BD
		}
		double Dx = (AC_intercept - BD_intercept) / (mBD - mAC); // x coordinate
		// for
		// MouseMovement
		// D.
		double Dy = (mAC * Dx) + AC_intercept;

		MouseMovement D = new MouseMovement(Dx, Dy);

		// find distance from B to D
		// then find distance from A to C
		double square1 = Math.pow(Dx - normalizedB.getxLoc(), 2);
		double square2 = Math.pow(Dy - normalizedB.getyLoc(), 2);

		double distanceBD = Math.sqrt(square1 + square2);

		square1 = Math.pow(C.getxLoc() - A.getxLoc(), 2);
		square2 = Math.pow(C.getyLoc() - A.getyLoc(), 2);

		double distanceAC = Math.sqrt(square1 + square2);

		if (distanceBD == 0) {
			return 0.0;
		}
		// curvature distance is ratio of distanceAC/distanceBD

		double curvatureDistanceRatio = distanceAC / distanceBD;

		return curvatureDistanceRatio;
	}

}
