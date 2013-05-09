package util;

import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;

public class FileHandler {
	/**
	 * 
	 * @param filename CSV file that contains the message to parse from
	 * @param msgColumn Column index 
	 * @return Array of strings where each index a single message from the csv file
	 */
	public String[] getMessagesFromFile(String filename, int msgColumn) {
		try {
			FileInputStream is = new FileInputStream(filename);
			CSVReader reader = new CSVReader(new InputStreamReader(is));
			
			ArrayList<String> messages = new ArrayList<String>();
			boolean readFirstLine = false;
			String[] readLine;
			while ((readLine = reader.readNext()) != null) {
				if (!readFirstLine) {
					readFirstLine = true;
					continue;
				}
				
				if (readLine.length > msgColumn) {
					messages.add(readLine[msgColumn]);
				}
			}
			
			String[] arrMessages = new String[messages.size()];
			return messages.toArray(arrMessages);
		}
		catch (Exception e) {
			return null;
		}
	}
	
	public void writeResultsToCSV(String filename, List<String[]> allLines) {
		try {
			FileWriter fw = new FileWriter(filename);
			CSVWriter writer = new CSVWriter(fw, ',');
			writer.writeAll(allLines);
			writer.close();
		}
		catch (Exception e) {
			System.out.println("Error writing to file: "+filename+". "+e.getMessage());
			return;
		}
	}
}
