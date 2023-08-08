package main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

class Writer {
	private BufferedReader br;
	private BufferedWriter bw;
	private File input;
	private Parser parser;
	
	Writer(String output) {
		try {
			parser = new Parser();
			bw = new BufferedWriter(new FileWriter(output));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	void write() {
		if (!input.getName().endsWith(".vm")) {
			System.err.println("Input file must have .vm extension");
			return;
		}
	
		try {
			String line;
			while ((line = br.readLine()) != null) {
				String newLine = parser.parse(line, input);

				if (newLine == null)
					continue;
				bw.write("// " + line);
				bw.newLine();
				bw.write(newLine);
				bw.newLine();
			}
			System.out.println("Processed: " + input.getName());
		} catch (IOException e) {
			System.err.println("Error reading/writing file: " + e.getMessage());
		}
	}
	
	void writeInit() {
		try {
			bw.write(parser.sysInit());
			bw.newLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	void setBr(File input) {
		try {
			this.input = input;
			br = new BufferedReader(new FileReader(input));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	void close() {
		try {
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
