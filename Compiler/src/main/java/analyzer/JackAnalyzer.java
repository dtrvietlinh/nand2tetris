package analyzer;

import java.io.File;

public class JackAnalyzer {
	public static void main(String[] args) {
		if (args.length != 1) {
			System.err.println("Usage: java -jar PROGRAM_NAME <input>");
			return;
		}
		
		String inputPath = args[0];
		File input = new File(inputPath);
		
		if (input.isFile()) {
			String output = input.getPath().replace(".jack", ".xml");
			Writer writer = new Writer(input, output);
			writer.write();
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
		
		for (File file : files) {
			if (file.isFile() && file.getName().endsWith(".jack")) {
				System.out.println("Processed: "+file.getName());
				String output = file.getPath().replace(".jack", ".xml");
				Writer writer = new Writer(file, output);
				writer.write();
			}
		}
	}
}
