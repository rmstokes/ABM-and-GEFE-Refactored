import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/*
 * 
 * Driver class contains main and will not contain much logic other than creating
 * objects and calling their methods to move through the process of 
 * reading files, processing files, and running authentication component.
 * 
 * 
 * 
 */

public class Driver {

	public static void main(String[] args) throws IOException {

		double x_max = 360;
		double y_max = 180;
		double r_max = 800;

		// change filepath to point to event files on your own machine.
		Feed feed = null;
		Preprocessor preprocessor = null;
		System.out.println("Would you like to generate new pac files? ");
		Scanner keyboard = new Scanner(System.in);
		String originalFiles = keyboard.next();
		System.out.println("How many subjects will be compared? ");
		int numSubjects = keyboard.nextInt();
		System.out
				.println("How many templates do you want to store in each subject profile? ");
		int numTemplates = keyboard.nextInt();

		System.out.println("Input NMD reject threshold: ");
		double threshold = keyboard.nextDouble();

		if (originalFiles.equalsIgnoreCase("y")
				|| originalFiles.equalsIgnoreCase("yes")) {
			String directoryPath = "C:\\Users\\Robert\\Dropbox\\Bank of America\\Mouse Movement Logs 03_13_2015\\";
			LogReader lr = new LogReader();

			System.out.println("Please enter custom xyr intervals? ");
			double x_interval = keyboard.nextDouble();
			double y_interval = keyboard.nextDouble();
			double r_interval = keyboard.nextDouble();

			// calling start will generate pacFiles
			String pacFileListNames = lr.start(directoryPath);

			feed = new Feed(pacFileListNames);

			ArrayList<PointAndClick> pacList = feed.generatePACObjects();
			preprocessor = new Preprocessor(pacList, x_interval, y_interval,
					r_interval, x_max, y_max, r_max);
			preprocessor.extractor();

		} else {
			feed = new Feed("pacFileListNames.txt");
		}

		// comment out the above 4 lines for preprocessor generating cdf files
		// to test
		// TemplateList Generator and Gallery and Probe.

		TemplateListGenerator generator = new TemplateListGenerator(
				"cdfFileList.txt");
		ArrayList<Template> templateList = generator.start();
		// System.exit(0);
		GalleryAndProbe gap = new GalleryAndProbe(templateList, numSubjects,
				numTemplates, threshold);
		gap.start();

	}

}
