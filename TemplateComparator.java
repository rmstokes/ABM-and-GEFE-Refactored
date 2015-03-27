import java.util.ArrayList;
import java.util.HashMap;

public class TemplateComparator {

	public boolean match(Template t1, Template t2, double threshold) {
		// test if two templates appear to belong to same subject.
		double distance = computeManhattanDistance(t1.getFeatureSet(), t2.getFeatureSet());
		System.out.println("t1 subject#: "+t1.getSubjectNumber()+" t2 subject#: "+t2.getSubjectNumber());
		System.out.println("distance: "+distance+" threshold: "+threshold);
		return distance<=threshold;
	}

	private double computeManhattanDistance(
			HashMap<Integer, ArrayList<Double>> hashMap,
			HashMap<Integer, ArrayList<Double>> hashMap2) {
		// calculate NMD for 2 feature sets
		// extract and concatenate all double values into 2 feature sets (fs1 &
		// fs2).
		ArrayList<Double> fs1 = new ArrayList<Double>();
		ArrayList<Double> fs2 = new ArrayList<Double>();

		//fill fs1 and fs2 with the metrics extracted from hashMap
		// add x metrics to fs1
		for (Double x : hashMap.get(0)) {
			fs1.add(x);
		}
		// add x metrics to fs2
		for (Double x : hashMap2.get(0)) {
			fs2.add(x);
		}

		// add y metrics to fs1
		for (Double y : hashMap.get(1)) {
			fs1.add(y);
		}
		// add y metrics to fs2
		for (Double y : hashMap2.get(1)) {
			fs2.add(y);
		}
		// add r metrics to fs1
		for (Double r : hashMap.get(2)) {
			fs1.add(r);
		}
		// add r metrics to fs2
		for (Double r : hashMap2.get(2)) {
			fs2.add(r);
		}
		
		//take difference between every feature in fs1 and fs2 based on the index ex. fs1.get(0)-fs2.get(0)
		ArrayList<Double> difference = new ArrayList<Double>();
		for (int i = 0; i < fs1.size(); i++){
			difference.add(Math.abs(fs1.get(i)-fs2.get(i)));
		}
		//find sum of all items in difference list
		double sum = 0;
		for (Double value: difference){
			sum+=value;
		}

		return sum/difference.size(); //NMD
	}

}
