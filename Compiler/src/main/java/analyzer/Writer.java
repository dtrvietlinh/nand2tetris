package analyzer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import analyzer.grammar.ProgramStructure;
import analyzer.tokenizer.JackTokenizer;

public class Writer {
	private BufferedReader br;
	private BufferedWriter bw;
	private JackTokenizer tokenizer;
	private ProgramStructure proStructure;

	Writer(File input, String output) {
		try {
			tokenizer = new JackTokenizer();
			proStructure = new ProgramStructure();
			br = new BufferedReader(new FileReader(input));
			bw = new BufferedWriter(new FileWriter(output));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	void write() {
		try {
			String line;
			List<String> list = new ArrayList<>();
			while ((line = br.readLine()) != null) {
				String newLine = tokenizer.parse(line);
				if (newLine==null)
					continue;
				newLine.lines().forEach(s->list.add(s));
			}
			bw.write(proStructure.compileClass(list));
			bw.close();
 		} catch (IOException e) {
			System.err.println("Error reading/writing file: " + e.getMessage());
		}
	}
}
