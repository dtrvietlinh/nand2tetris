package main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.IOException;


public class Translator {
	
	private static BufferedReader br;
	private static BufferedWriter bw;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		if (args.length != 1) {
			System.err.println("Usage: java VMTranslator <input file>");
			return;
		}
		
		String input = args[0];

		if (!input.toLowerCase().endsWith(".vm")) {
			System.err.println("Only .vm files are allowed");
			return;
		}
		
		String output = input.replace(".vn", ".asm");
		Parser parser = new Parser();
	
		try {
			br = new BufferedReader(new FileReader(input));
			
			String line;
			while ((line=br.readLine()) != null) {
				String newLine = parser.parse(line, input);
				if (newLine==null) continue;
				System.out.println("// "+line);
				System.out.println(newLine);
			}
		} catch (IOException e) {
			// TODO: handle exception
		}
	}

}
