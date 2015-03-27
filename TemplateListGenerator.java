import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Scanner;

/*
 * 
 * Input: cdf list
 * Output: list of templates
 * 
 * 
 * 
 * 
 */

public class TemplateListGenerator {

	// instance variables
	private String cdfList; // cdf file list
	private String pathToCWD; // path to current working directory
	private String fileListLocation;

	public TemplateListGenerator(String cdfList) {
		this.cdfList = cdfList;
		this.pathToCWD = System.getProperty("user.dir");
		this.fileListLocation = pathToCWD + "\\" + cdfList;
	}

	private ArrayList<Template> generateTemplateObjects() {
		// input cdfList
		// output template list

		ArrayList<String> fileList = new ArrayList<String>();
		fileList = generateFileList();
		ArrayList<Template> templateList = new ArrayList<Template>();

		for (String title : fileList) {
			// access each cdf file one by one in this loop
			int subjectNumber = extractSubjectNumber(title);
			int instanceNumber = extractInstanceNumber(title);
			int max_x = extract_max_x();
			int max_y = extract_max_y();
			int max_r = extract_max_r();
			int clickRegion = extractClickRegion(title);
			HashMap<Integer, ArrayList<Double>> featureSet = extractFeatureSet(title);
			// create Template and store into templateList
			Template template = new Template(featureSet, subjectNumber,
					instanceNumber, max_x, max_y, max_r, clickRegion);
			templateList.add(template);
		}// end fileList loop
		return templateList;
	}

	private int extractClickRegion(String title) {
		int clickRegion = 0;
		title = title.trim();
		String number = "";
		// move through string backwards
		int i = title.length() - 1;
		while (title.charAt(i) != ' ') {
			number = title.charAt(i) + number; // prepend #s
			i--;
		}
		clickRegion = Integer.parseInt(number);
		return clickRegion;
	}

	private HashMap<Integer, ArrayList<Double>> extractFeatureSet(String title) {
		// input is name of cdf file.
		// output is HashMap where K is 0,1,or2 depending on which metric we're
		// looking at
		// x=0; y=1; r=2
		// HashMap V is list of all values for a given metric K.
		HashMap<Integer, ArrayList<Double>> featureSet = new HashMap<Integer, ArrayList<Double>>();
		int key = 0;
		ArrayList<Double> featureList = new ArrayList<Double>();
		Scanner scanner = null;
		try {
			scanner = new Scanner(new File(title));
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		// skip 1st line
		scanner.nextLine();
		advancePastLabel(scanner, 2); // skip past cdf direction flag
		while (scanner.hasNext()) {
			// read through entire Instance file
			// this loop will be useful to creating template objects.
			// must first decide how to store feature sets for each
			// metric.

			try {
				featureList.add(scanner.nextDouble());
				// System.out.println(scanner.nextDouble());
			} catch (InputMismatchException e) {
				// e.printStackTrace();
				System.out.println("featureList.size(): "
						+ featureList.size());
				// System.exit(0);
				featureSet.put(key, featureList);
				key++;
				advancePastLabel(scanner, 2);

				featureList = new ArrayList<Double>();
			}

		}// end while loop

		return featureSet;
	}

	private static void advancePastLabel(Scanner scanner, int skip) {
		// advance scanner cursor beyond the label
		// System.out.println("advancing past label...");
		// label = label.trim();
		// System.out.println("inside advancePastLabel");
		while (skip > 0) {
			System.out.print(scanner.next() + " ");
			skip--;
		}
		System.out.println();
	}

	private int extract_max_r() {
		// TODO Auto-generated method stub
		return 800;
	}

	private int extract_max_y() {
		// TODO Auto-generated method stub
		return 180;
	}

	private int extract_max_x() {
		// if we ever change the functionality where we want to modify xyr we
		// will
		// implement it here.
		// for now extract xyr all return standard values.
		return 360;
	}

	private int extractInstanceNumber(String title) {
		System.out.println("title: "+title);
		//int instanceNumber = Integer.parseInt(title.substring(19,title.indexOf("R") - 1));
		int instanceNumber = 0;
		Scanner scanner = new Scanner(title);
		//skip to the 4th token
		int tokenNumber = 0;
		String token = "";
		while (true) {
			token = scanner.next();
			if (tokenNumber == 3){
				instanceNumber = Integer.parseInt(token);
				System.out.println("Instance Number: "+instanceNumber);
				break;
			}
			tokenNumber++;
			
		}
		return instanceNumber;
	}

	private int extractSubjectNumber(String title) {
		int subjectNumber = Integer.parseInt(title.substring(8,
				title.indexOf("I") - 1));
		return subjectNumber;
	}

	private ArrayList<String> generateFileList() {
		ArrayList<String> fileList = new ArrayList<String>();
		BufferedReader br = null;
		try {
			File file = new File(cdfList);
			br = new BufferedReader(new FileReader(file));
			// use br to build up a fileList
			// then use fileList to open up pac files and read moves into
			// objects.
			while (true) {
				String line = br.readLine();
				if (line == null)
					break;
				fileList.add(line); // add names of all cdf files to fileList
			}
			br.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return fileList;
	}

	public ArrayList<Template> start() {
		ArrayList<Template> templateList = generateTemplateObjects();
				
		return templateList;
	}

}
