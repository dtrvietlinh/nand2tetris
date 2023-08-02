package main;

public class Translator {

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
	
	}

}
