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
	private boolean flag = false;
	// private static String subjectName;
	public static double x_interval;
	public static double y_interval;
	public static double ratio_interval;
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

	public Preprocessor(ArrayList<PointAndClick> pacList) {
		this.pacList = pacList;
		directionResults = new ArrayList<Double>();
		curvatureAngleResults = new ArrayList<Double>();
		curvatureDistanceResults = new ArrayList<Double>();
		
	}

	public void extractor() {
		// for each pac object in pacList
		// extract out all the moves and send to the start() method
		for (PointAndClick pac : pacList) {
			start(pac.getMoves());
		}
	}

	private void start(ArrayList<MouseMovement> moves) {
		// make sure there are at least 3 items to grab.
		// then put 3 items into preprocess method
		// on return from preprocess() scrap list item at position 0.
		// then check to see if there are 3 more MouseMovements to grab and keep
		// iterating.
		
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
