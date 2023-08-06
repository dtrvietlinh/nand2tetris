package command;

import java.io.File;

public class Segment {
	public String fourSegments(String segment, String i, int num) {
		StringBuilder strb = new StringBuilder();
		
		strb.append(constant(i));
		nl(strb);
		strb.append(whichSegment(segment));
		nl(strb);
		if (num==1)
			strb.append("A=D+M\n");
		else 
			strb.append("D=D+M\n");
		strb.append(pushOrPop(num));
		
		return strb.toString();
	}
	
	private String whichSegment(String segment) {
		String rs="";
		switch (segment) {
		case "local":
			rs="@LCL";
			break;
		case "argument":
			rs="@ARG";
			break;
		case "that":
			rs="@THAT";
			break;
		case "this":
			rs="@THIS";
			break;
		}
		return rs;
	}
	
	public String constant(String i) {
		return "@"+i+"\nD=A";
	}
	
	public String s_static(String i, File file, int num) {
		StringBuilder strb = new StringBuilder();
		
		String filename = file.getName();
		filename = filename.replace(".vm", "."+i);
		
		strb.append("@"+filename+"\n");
		strb.append(pushOrPopSP(num));
		return strb.toString();
	}
	
	public String pointer(String i, int num) {
		StringBuilder strb = new StringBuilder();
		if (i.equals("0"))
			strb.append("@THIS\n");
		else
			strb.append("@THAT\n");
		strb.append(pushOrPopSP(num));
		return strb.toString();	
	}
	
	public String temp(String i, int num) {
		StringBuilder strb = new StringBuilder();
		int addr = 5 + Integer.parseInt(i);
		strb.append("@"+addr+"\n");
		strb.append(pushOrPopSP(num));
		return strb.toString();
	}
	
	private void nl(StringBuilder strb) {
		strb.append("\n"); 
	}
	
	private String pushOrPop(int num) {
		return num==1 ? "D=M" : "@R13\nM=D";
	}
	
	private String pushOrPopSP(int num) {
		return num==1 ? "D=M" : "M=D";
	}
}
