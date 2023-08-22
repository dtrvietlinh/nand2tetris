package analyzer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import analyzer.tokenizer.JackTokenizer;

public class Writer {
	private BufferedReader br;
	private BufferedWriter bw;
	private JackTokenizer tokenizer;

	Writer(File input, String output) {
		try {
			tokenizer = new JackTokenizer();
			br = new BufferedReader(new FileReader(input));
			bw = new BufferedWriter(new FileWriter(output));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	void write() {
		try {
			String line;
			bw.write("<tokens>\n");
			while ((line = br.readLine()) != null) {
				String newLine = tokenizer.parse(line);
				if (newLine==null)
					continue;
				bw.write(newLine);
			}
			bw.write("</tokens>");
			bw.close();
 		} catch (IOException e) {
			System.err.println("Error reading/writing file: " + e.getMessage());
		}
	}
}
