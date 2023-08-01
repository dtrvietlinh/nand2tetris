package main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MyAssembler {
	
	private static BufferedReader br;
	private static BufferedWriter bw;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		if (args.length != 1) {
			System.err.println("Usage: java MyAssembler <input file>");
			return;
		}
		
		String input = args[0];

		if (!input.toLowerCase().endsWith(".asm")) {
			System.err.println("Only .asm files are allowed");
			return;
		}
		
		String output = input.replace(".asm", ".hack");
		Parser parser = new Parser();

		try {
			br = new BufferedReader(new FileReader(input));
			bw = new BufferedWriter(new FileWriter(output));
			
			String line;
			List<String> list = new ArrayList<>();
			int lines=0;
			
			while ((line = br.readLine()) != null) {
				String newLine = parser.symbolParse(line, lines);
				if (newLine.equals("")) continue;
				list.add(newLine);
				lines++;
			}
			
			int value = 16;
			for (int i=0; i<list.size(); i++) {
				String s = list.get(i);
				String rs = parser.varParse(s, value);
				if (rs!=null) value++;
			}
			
			for (int i=0; i<list.size(); i++) {
				String s = list.get(i);
				String rs = parser.parse(s);
				bw.write(rs);
				bw.newLine();
			}
			
			bw.close();
			
		} catch (IOException e) {
			System.err.println("Error reading file: " + e.getMessage());
		}
	}
	
	

}
