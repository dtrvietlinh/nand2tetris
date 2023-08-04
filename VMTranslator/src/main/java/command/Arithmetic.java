package command;

public class Arithmetic {
	
	private final int condNum1 = 13;
	private final int condNum2 = 3;
	
	public String doMath(String command, int lines) {
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
			rs+=compare(command, lines);
		}
		return rs;
	}
	
	private String compare(String command, int lines) {	
		int line1 = lines+condNum1;
		int line2 = line1+condNum2;
		String jmpT = "@"+line1+"\n";
		String jmpF = "@"+line2+"\n";
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
		strb.append(jmpT+cond);
		strb.append("@SP\nA=M-1\nM=0\n");
		strb.append(jmpF);
		strb.append("0;JMP\n@SP\nA=M-1\nM=-1");

		return strb.toString();
	}
}
