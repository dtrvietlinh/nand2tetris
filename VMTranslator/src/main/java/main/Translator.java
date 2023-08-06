package main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Translator {

	private static BufferedReader br;
	private static BufferedWriter bw;

	public static void main(String[] args) {
		if (args.length != 1) {
			System.err.println("Usage: java -jar PROGRAM_NAME <input>");
			return;
		}

		String inputPath = args[0];
		File input = new File(inputPath);

		if (input.isFile()) {
			String output = input.getPath().replace(".vm", ".asm");
			processFile(input, output);
		} else if (input.isDirectory()) {
			processDirectory(input);
		} else {
			System.err.println("Input should be a valid file or directory.");
		}
	}

	private static void processFile(File input, String output) {
		if (!input.getName().endsWith(".vm")) {
			System.err.println("Input file must have .vm extension");
			return;
		}
		
		Parser parser = new Parser();
		
		try {
			br = new BufferedReader(new FileReader(input));
			bw = new BufferedWriter(new FileWriter(output));
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

			bw.close();
			System.out.println("Processed: " + input.getName());
		} catch (IOException e) {
			System.err.println("Error reading/writing file: " + e.getMessage());
		}
	}

	private static void processDirectory(File dir) {
		File[] files = dir.listFiles();

		if (files == null) {
			System.err.println("Error listing files in directory.");
			return;
		}
		String path = files[0].getPath();
		String output = path.replace(files[0].getName(), dir.getName()).concat(".asm");
		
		for (File file : files) {
			if (file.isFile() && file.getName().endsWith(".vm")) {
				processFile(file, output);
			}
		}
	}
}
