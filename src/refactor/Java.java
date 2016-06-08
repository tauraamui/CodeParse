package refactor;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Java {
	public static void main(String[] args) throws IOException {
		String workingdir = System.getProperty("user.dir");
		// Read file
		BufferedReader filereader = new BufferedReader(
				new InputStreamReader(new DataInputStream(new FileInputStream(workingdir + "\\tests\\java.java"))));
		while (!filereader.ready()) {}

		String line;
		ArrayList<String> lines = new ArrayList<String>();
		while ((line = filereader.readLine()) != null) {
			lines.add(line);
		}
		filereader.close();

		// Parse
		lines= parse(lines);

		// Write result to file
		BufferedWriter out = new BufferedWriter(new FileWriter(workingdir + "\\tests\\java-result.java"));
		for (int i = 0; i < lines.size(); i++) {
			out.write(lines.get(i));
			out.newLine();
		}
		out.flush();
		out.close();
	}

	public static ArrayList<String> parse(ArrayList<String> filecontents) {
		filecontents = removeComments(filecontents);
		filecontents = reduce(filecontents);
		//String min = minify(filecontents);
		filecontents = split(filecontents);
		return filecontents;
	}

	private static ArrayList<String> removeComments(ArrayList<String> filecontents) {
		boolean opencommentblock = false;
		for (int i=0; i<filecontents.size(); i++) {
			String line = filecontents.get(i).trim();
			
			int commentlocation = line.indexOf("/*");
			if (commentlocation != -1) {
				line =line.replace(line.substring(commentlocation, line.length()), "");
				opencommentblock = true;
			}
			if (opencommentblock == true) {
				commentlocation = line.indexOf("*/");
				if (commentlocation != -1) {
					line = line.replace(line.substring(0, commentlocation+2), "");
					opencommentblock = false;
				} else {
					line = "";
				}
			} else {
				commentlocation = line.indexOf("//");
				if (commentlocation != -1) {
					line = line.replace(line.substring(commentlocation, line.length()), "");
				}
			}
			
			line = line.trim();
			filecontents.set(i, line);
		}
		
		return filecontents;
	}

	// Removes white spaces and newlines
	private static String minify(ArrayList<String> filecontents) {
		StringBuilder alllines = new StringBuilder("");
		
		for (String line : filecontents) {
			line = line.trim();
			if (line.isEmpty()) continue;
			
			alllines.append(line);
		}
		
		return alllines.toString();
	}

	// Takes a minified file and splits so each line is one signature or instruction
	private static ArrayList<String> split(ArrayList<String> filecontents) {
		char[] breakers = new char[] {'{', '}', ':', ','};
		for (int i=0; i<filecontents.size(); ) {
			String line = filecontents.get(i);
			if (line.length() == 1) {
				i++;
				continue;
			}
			
			for (char breaker : breakers) {
				int index = line.indexOf(breaker);
				if (index == -1) {
					continue;
				}
			
				String splitline = line.substring(0, index);
				filecontents.set(i, splitline);
				filecontents.add(++i, ""+breaker);
			}
			
			i++;
		}
		
		return filecontents;
	}

	// Erases variable manipulation
	private static ArrayList<String> reduce(ArrayList<String> filecontents) {
		for (int i=0; i<filecontents.size(); i++) {
			String line = filecontents.get(i);
			
			line = line.trim();
			if (line.isEmpty()) {
				filecontents.remove(i);
				i--;
			}
		}
		
		return filecontents;
	}
}