package main;

import java.io.File;

import command.Arithmetic;
import command.Branching;
import command.Function;
import command.Memory;

public class Parser {
	
	private CommandTable cmdTab;
	private Memory memory;
	private Arithmetic arithmetic;
	private Branching branching;
	private Function function;
	private int lines;
	
	Parser() {
		cmdTab = new CommandTable();
		memory = new Memory();
		arithmetic = new Arithmetic();
		branching = new Branching();
		function = new Function();
		lines=-1;
	}

	String parse(String line, File file) {
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
		line = whichCommand(command, args, file, lines);
		String[] arr = line.split("\n");
		this.lines+=arr.length;
		
		return line;
	}

	private String whichCommand(String command, String[] args, File file, int lines) {
		String rs="";
		switch (command) {
		case "C_PUSH":
			rs=memory.push(args[1], args[2], file);
			break;
		case "C_POP":
			rs=memory.pop(args[1], args[2], file);
			break;
		case "C_ARITHMETIC":
			rs=arithmetic.doMath(args[0], lines);
			break;
		case "C_BRANCHING":
			rs=branching.jump(args);
			break;
		case "C_FUNCTION":
			rs=function.handle(args);
			break;
		}
		return rs;
	}

}
