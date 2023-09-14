package compiler.engine;

import java.util.List;

import analyzer.grammar.XML;
import compiler.table.SymbolTable;
import compiler.writer.VMWriter;

public class Grammar {
	
	private static StringBuilder strb = new StringBuilder();
	private static SymbolTable symtab = new SymbolTable();
	private static VMWriter writer = new VMWriter();
	static int index;
	String className;
	
	void append(String s) {
		strb.append(s);
		strb.append("\n");
	}
	
	void openAndClose(List<String> list, String symbol) {
		String token = list.get(Grammar.index++);
		checkIfPresent(token, XML.SYMBOL, symbol);
	}
	
	boolean checkIfPresent(String token, String xml, String symbol) {
		boolean rs = false;
		if (token.indexOf(xml)==0)
			if (token.contains(symbol))
				rs=true;
			else 
				System.err.println("Only '"+symbol+"' is allowed");
		else
			System.err.println("Syntax error on "+token+" token");
		return rs;
	}

	void handleVars(List<String> list, String k, String t) {
		String name = list.get(Grammar.index++);
		if (define(name, k, t));
			writePush(name);
	}
	
	void handleSubroutine(String name, int i) {
		if (name.indexOf(XML.IDENTIFIER) == 0) {
			append(writer.writeFunction(className+"."+name, i));
		} else {
			System.err.println("Function's name is expected");
			return;
		}
	}
	
	boolean define(String name, String k, String t) {
		if (name.indexOf(XML.IDENTIFIER) == 0) {
			symtab.define(name, k, t);
		} else {
			System.err.println("Variable's name is expected");
			return false;
		}
		return true;
	}
	
	void writePush(String name) {
		append(writer.writePush(symtab.kindOf(name), symtab.indexOf(name)));
	}
	
	String writePop(String name) {
		return writer.writePop(symtab.kindOf(name), symtab.indexOf(name));
	}

	public SymbolTable getSymtab() {
		return symtab;
	}

	public VMWriter getWriter() {
		return writer;
	}
}
