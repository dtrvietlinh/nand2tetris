package main;

import command.Arithmetic;
import command.Memory;

public class Parser {
	
	private CommandTable cmdTab;
	private Memory memory;
	private Arithmetic arithmetic;
	private int lines;
	
	Parser() {
		cmdTab = new CommandTable();
		memory = new Memory();
		arithmetic = new Arithmetic();
		lines=-1;
	}

	String parse(String line, String filename) {
		if (line.isBlank())
			return null;

		int comment = line.indexOf("//");
		if (comment != -1) {
			if (comment == 0)
				return null;
			line = line.substring(0, comment);
		}
		
		line=line.strip();
		String[] args = line.split(" ");
		String command = cmdTab.commandType(args[0]);
		line = whichCommand(command, args, filename, lines);
		String[] arr = line.split("\n");
		this.lines+=arr.length;
		
		return line;
	}

	private String whichCommand(String command, String[] args, String filename, int lines) {
		String rs="";
		switch (command) {
		case "C_PUSH":
			rs=memory.push(args, filename);
			break;
		case "C_POP":
			rs=memory.pop(args, filename);
			break;
		case "C_ARITHMETIC":
			rs=arithmetic.doMath(args[0], lines);
			break;
		}
		return rs;
	}

}
