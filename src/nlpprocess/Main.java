package nlpprocess;

import java.util.ArrayList;
import java.util.HashMap;

import nlpwrapper.OpenNlpWrapper;
import util.FileHandler;

public class Main {

	public static void main(String[] args) {
		
		if ((args.length == 1 && !args[0].equals("demo-test")) || (args.length != 1 && args.length != 4)) {
			System.out.println("Error. Incorrect parameters. Usage opennlp-process <command> <csv file> <input column #>");
			return;
		}
		
		String command = args[0];
		if (command.equals("demo-test")) {
			OpenNlpWrapper nlp = new OpenNlpWrapper();
			nlp.runDemoTest();
			return;
		}
		
		String csvFile, outFile;
		int inputColumn;
		
		csvFile = args[1];
		inputColumn = Integer.parseInt(args[2]);
		outFile = args[3];
		System.out.println("Command: "+command+" csvFile: "+csvFile+" inputColumn: "+inputColumn+" outFile: "+outFile);
		
		// Find common names
		if (command.equals("get-names")) {
			findNames(csvFile, inputColumn, outFile);
		}
		// Find common nouns and verbs
		else if (command.equals("common-nv")) {
			findNounsVerbs(csvFile, inputColumn, outFile);
		}
		
		System.out.println("END process");
	}

	private static void findNames(String filename, int columnIndex, String outFilename) {
		FileHandler fh = new FileHandler();
		OpenNlpWrapper nlp = new OpenNlpWrapper();
		
		String[] messages = fh.getMessagesFromFile(filename, columnIndex);
		HashMap<String,Integer> nameData = new HashMap<String,Integer>();
		for (int i = 0; i < messages.length; i++) {
			System.out.print("Processing message at line " + i);
			try {
				String[] names = nlp.findNames(messages[i], "models/en-ner-person.bin");
				for (int j = 0; j < names.length; j++) {
					System.out.println("  found name ["+names[j]+"]");
					if (nameData.containsKey(names[j])) {
						Integer counter = nameData.get(names[j]);
						int newCount = counter.intValue() + 1;
						nameData.put(names[j], newCount);
					}
					else {
						nameData.put(names[j], 1);
					}
				}
			}
			catch (Exception e) {
				System.out.println("Error finding name at message #" + i + ". " + e.getMessage());
			}
		}
		
		if (nameData.size() > 0) {
			ArrayList<String[]> results = new ArrayList<String[]>();
			String[] labelRow = new String[2];
			labelRow[0] = "name";
			labelRow[1] = "count";
			results.add(labelRow);
			for (String key : nameData.keySet()) {
				Integer count = nameData.get(key);
				System.out.println("KEY:"+key+" count:"+count.intValue());
				
				String[] row = new String[2];
				row[0] = key;
				row[1] = count.toString();
				results.add(row);
			}
			
			fh.writeResultsToCSV(outFilename, results);
		}
	}
	
	private static void findNounsVerbs(String filename, int columnIndex, String outFilename) {
		
	}
}
