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
	
	Parser() {
		cmdTab = new CommandTable();
		memory = new Memory();
		arithmetic = new Arithmetic();
		branching = new Branching();
		function = new Function();
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
		line = whichCommand(command, args, file);
		
		return line;
	}
	
	String sysInit() {
		StringBuilder strb = new StringBuilder();
		strb.append("@256\nD=A\n@SP\nM=D\n");
		String[] args = {"call", "Sys.init", "0"};
		strb.append(function.handle(args));
		return strb.toString();
	}

	private String whichCommand(String command, String[] args, File file) {
		String rs="";
		switch (command) {
		case "C_PUSH":
			rs=memory.push(args[1], args[2], file);
			break;
		case "C_POP":
			rs=memory.pop(args[1], args[2], file);
			break;
		case "C_ARITHMETIC":
			rs=arithmetic.doMath(args[0]);
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
