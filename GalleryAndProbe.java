import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

/*
 * 
 * Gallery and Probe will read in TemplateList
 * and create Subject objects.
 * Comparisons can be made between subjects by calling calculateCMC() or calculateROC().
 * ROC: TAR,FAR,FRR
 * CMC: Rank
 * 
 */

public class GalleryAndProbe {

	private ArrayList<Template> templateList;
	private int numTemplates;
	private int numSubjects;
	private double threshold;

	// variables for calculating cmc & roc
	int[] rank = new int[9];
	int falseAccept = 0;
	int falseReject = 0;
	int trueAccept = 0;

	/*
	 * total# of probes where each template gets to be the probe and keeps
	 * calling match(probe, template) over all templates in gallery. Should be
	 * gallerySize*gallerySize
	 */
	double numProbes = 0;
	TemplateComparator tc = new TemplateComparator();

	public GalleryAndProbe(ArrayList<Template> templateList, int numSubjects,
			int numTemplates, double threshold) {
		this.templateList = templateList;
		this.numSubjects = numSubjects;
		this.numTemplates = numTemplates;
		this.threshold = threshold;
	}

	public void start() {
		// for all templates in templateList
		HashMap<String, Double> roc = calculateROC();
		System.out.println("calculated roc data...");
		System.out.println(roc);
		System.exit(0);
	}

	private void probe(Template probe, ArrayList<Template> gallery) {
		// make comparison of probe with each gallery template but skip the
		// gallery item that is setAsProbe
		TemplateComparator tc = new TemplateComparator();
		boolean isMatch = false;
		// boolean [] subjectDeleted = new boolean[numSubjects+1];
		for (Template galleryItem : gallery) {
			if (galleryItem.isProbe())
				continue;
			numProbes++;
			// if (subjectDeleted[galleryItem.getSubjectNumber()]) continue;
			isMatch = tc.match(probe, galleryItem, threshold);
			if (isMatch
					&& probe.getSubjectNumber() == galleryItem
							.getSubjectNumber())
				trueAccept++;
			if (!isMatch
					&& probe.getSubjectNumber() == galleryItem
							.getSubjectNumber())
				falseReject++;
			if (isMatch
					&& probe.getSubjectNumber() != galleryItem
							.getSubjectNumber())
				falseAccept++;
		}
	}

	private double[] calculateCMC(ArrayList<Subject> subjectList) {
		// this function will be used to calculate
		// cumulative match characteristic (CMC).
		// find most similar instance to a given probe/template. If they both
		// belong to the same subject
		// then this is a hit/plus for our accuracy. otherwise, it's a miss and
		// accuracy goes down
		// if we have a miss, then we will remove all templates for the subject
		// that caused the miss
		// and we will try again and find the closest template to our probe.
		// each time we have to try again our rank variable will be incremented
		// by 1.
		// rank goes from 1 to 8 on x axis of CMC graph

		System.out.println("calculateCMC not yet implemented...");
	

		System.exit(0);

		return null;

	}// end method

	private HashMap<String, Double> calculateROC() {
	// select one template as probe and compare to all the others (gallery)
		double gallerySize = templateList.size()-1; //all templates minus probe
		//double numProbes = gallerySize;
		for (Template probe : templateList) {
			probe.setAsProbe();
			ArrayList<Template> gallery = templateList;
			probe(probe, gallery);
			probe.setAsNotProbe();
		}
		//the far,frr,tar all have their counts tabulated
		double tar = trueAccept/numProbes;
		double far = falseAccept/numProbes;
		double frr = falseReject/numProbes;
		HashMap<String, Double> roc = new HashMap<String, Double>();
		roc.put("TAR", tar);
		roc.put("FAR", far);
		roc.put("FRR", frr);
		//System.out.println("trueAcceptance: "+trueAccept);
		//System.out.println("numProbes: "+numProbes);
		return roc;
	}

	private ArrayList<Subject> generateSubjectList() {
		// input templateList
		// output subjectList

		ArrayList<Subject> subjectList = new ArrayList<Subject>();

		// create all the subjects and then add corresponding templates to
		// subject's template list
		for (int i = 1; i <= numSubjects; i++) {
			subjectList.add(new Subject(i));
		}

		for (Template template : templateList) {
			int index = template.getSubjectNumber() - 1;
			subjectList.get(index).addTemplate(template);
		}
		System.out.println("generated subject list...");

		return subjectList;
	}

}
