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
		
		//change filepath to point to event files on your own machine.
		/*
		String directoryPath = "C:\\Users\\Robert\\Dropbox\\Bank of America\\Mouse Movement Logs 03_13_2015\\";		
		LogReader lr = new LogReader();
		//calling start will generate pacFiles
		String pacFileListNames = lr.start(directoryPath);

		Feed feed = new Feed(pacFileListNames);
		feed.pacListToMovementStream();
		*/
		//skip the reading in of BAC files and generation of pacFiles
		Feed feed = new Feed("pacFileListNames.txt");
		ArrayList<PointAndClick> pacList = feed.generatePACObjects();
		Preprocessor preprocessor = new Preprocessor(pacList);
		preprocessor.extractor(); //this will call start and calculate metrics

	}

}
