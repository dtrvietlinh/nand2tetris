package command;

import java.util.HashMap;
import java.util.Map;

public class Arithmetic {
	
	private static Map<String, Integer> map = new HashMap<>();
	
	public String doMath(String command) {
		String rs = "@SP\n" + "AM=M-1\n" + "D=M\n" + "A=A-1\n";
		switch (command) {
		case "add":
			rs+="M=D+M";
			break;
		case "sub":
			rs+="M=M-D";
			break;
		case "neg":
			rs="@SP\n" + "A=M-1\n" + "M=-M";
			break;
		case "and":
			rs+="M=D&M";
			break;
		case "or":
			rs+="M=D|M";
			break;
		case "not":
			rs="@SP\n" + "A=M-1\n" + "M=!M";
			break;
		default:
			rs+=compare(command);
		}
		return rs;
	}
	
	private String compare(String command) {
		
		Integer num = map.get(command);
		String line1="";
		String line2="";
		if (num==null) {
			line1 = "T."+command.toUpperCase();
			line2 = "F."+command.toUpperCase();
			map.put(command, 1);
		} else {
			line1 = "T."+command.toUpperCase()+num;
			line2 = "F."+command.toUpperCase()+num;
			map.put(command, num+1);
		}

		String cond = "";
		
		switch (command) {
		case "eq":
			cond="D;JEQ\n";
			break;
		case "lt":	
			cond="D;JLT\n";
			break;
		case "gt":		
			cond="D;JGT\n";
			break;
		}
		
		StringBuilder strb = new StringBuilder();
		strb.append("D=M-D\n");
		// true jump condition
		strb.append("@"+line1+"\n"+cond);
		strb.append("@SP\nA=M-1\nM=0\n");
		// false jump 
		strb.append("@"+line2+"\n0;JMP\n");
		// true jump addr
		strb.append("("+line1+")\n");
		strb.append("@SP\nA=M-1\nM=-1\n");
		// false jump addr
		strb.append("("+line2+")");

		return strb.toString();
	}
}
