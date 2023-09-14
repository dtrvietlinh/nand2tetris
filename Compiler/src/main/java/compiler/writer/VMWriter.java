package compiler.writer;

import compiler.engine.OS;

public class VMWriter {

	public String writePush(String segm, int i) {
		StringBuilder str = new StringBuilder();
		str.append("push ");
		str.append(segm+" ");
		str.append(i);
		return str.toString();
	}
	
	public String writePop(String segm, int i) {
		StringBuilder str = new StringBuilder();
		str.append("pop ");
		str.append(segm+" ");
		str.append(i);
		return str.toString();
	}
	
	public String writeArithmetic(String cmd) {
		String rs = "";
		switch (cmd) {
		case "&lt;":
			rs = "lt";
			break;
		case "&amp;":
			rs = "and";
			break;
		case "&gt;":
			rs = "gt";
			break;
		case "+":
			rs = "add";
			break;
		case "-":
			rs = "sub";
			break;
		case "*":
			rs = writeCall(OS.MULTIPLY, 0);
			break;
		case "/":
			rs = writeCall(OS.DIVIDE, 0);
			break;
		case "|":
			rs = "or";
			break;
		case "=":
			rs = "eq";
			break;
		}
		return rs;
	}
	
	public String writeLabel(String label) {
		StringBuilder str = new StringBuilder();
		str.append("label ");
		str.append(label);
		return str.toString();
	}
	
	public String writeGoto(String label) {
		StringBuilder str = new StringBuilder();
		str.append("goto ");
		str.append(label);
		return str.toString();
	}
	
	public String writeIf(String label) {
		StringBuilder str = new StringBuilder();
		str.append("if-goto ");
		str.append(label);
		return str.toString();
	}
	
	public String writeCall(String name, int args) {
		StringBuilder str = new StringBuilder();
		str.append("call ");
		str.append(name+" ");
		str.append(args);
		return str.toString();
	}
	
	public String writeFunction(String name, int args) {
		StringBuilder str = new StringBuilder();
		str.append("function ");
		str.append(name+" ");
		str.append(args);
		return str.toString();
	}
	
	public void writeReturn() {
		
	}
}
