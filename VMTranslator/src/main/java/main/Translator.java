package main;

import java.io.File;

public class Translator {

	public static void main(String[] args) {
		if (args.length != 1) {
			System.err.println("Usage: java -jar PROGRAM_NAME <input>");
			return;
		}

		String inputPath = args[0];
		File input = new File(inputPath);
		
		if (input.isFile()) {
			String output = input.getPath().replace(".vm", ".asm");
			Writer writer = new Writer(output);
			processFile(input, writer);
			writer.close();
		} else if (input.isDirectory()) {
			processDirectory(input);
		} else {
			System.err.println("Input should be a valid file or directory.");
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
		Writer writer = new Writer(output);

		for (File file : files) {
			if (file.isFile() && file.getName().endsWith(".vm")) {
				processFile(file, writer);
			}
		}
		writer.close();
	}
	
	private static void processFile(File file, Writer writer) {
		writer.setBr(file);
		writer.write();
	}
}
