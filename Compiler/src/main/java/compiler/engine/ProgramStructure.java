package compiler.engine;

import java.util.List;

import compiler.table.SymbolTable;
import compiler.writer.VMWriter;

public class ProgramStructure extends Grammar {

	private Statements statement;
	private SymbolTable symtab;
	private VMWriter writer;

	public ProgramStructure() {
		statement = new Statements();
		symtab = getSymtab();
		writer = getWriter();
	}

	public String compile(List<String> list) {
		Grammar.index = 0;
		return compileClass(list);
	}

	private String compileClass(List<String> list) {
		// check if class token is missing
		String CLASS = list.get(index++);
		if (!checkIfPresent(CLASS, XML.KEYWORD, "class"))
			return null;

		// check if className is missing
		String className = list.get(index++);
		if (!checkIfPresent(className, XML.IDENTIFIER, ""))
			super.className = className;

		openAndClose(list, "{");
		// compile class variables if has some
		compileClassVarDec(list);
		// compile subroutine
		compileSubroutineDec(list);
		openAndClose(list, "}");

		return null;
	}

	// ('static'|'field') type varName (',' varName)* ';'
	private void compileClassVarDec(List<String> list) {
		String k = list.get(index);
		if (!k.contains("static") && !k.contains("field"))
			return;
		String t = list.get(index+1);
		boolean left = k.indexOf(XML.KEYWORD) == 0;
		boolean right = isValidType(t, null);

		if (left && right) {
			index+=2;
			handleVars(list, k, t);
		} else if (left) {
			System.err.println("Type must be 'int' or 'char' or 'boolean' or className");
		} else if (right) {
			System.err.println("Syntax error on " + k + " token");
		}
		compileClassVarDec(list);
	}

	private void compileSubroutineDec(List<String> list) {
		String s = list.get(index);
		if (!s.contains("constructor") && !s.contains("function") && !s.contains("method"))
			return;

		String type = list.get(index + 1);
		boolean left = s.indexOf(XML.KEYWORD) == 0;
		boolean right = isValidType(type, "");

		if (left && right) {
			index+=2;
			String name = list.get(index++);
			if (!checkIfPresent(name, XML.IDENTIFIER, ""))
				return;

			// compile parameter list
			StringBuilder strb = new StringBuilder();
			openAndClose(list, "(");
			int args = compileParameterList(list, strb, 0);
			openAndClose(list, ")");
			// append subroutine name			
			handleSubroutine(name, args);
			// append parameter list		
			append(strb.toString());
			// compile subroutine body
			compileSubroutineBody(list);

			if (index < list.size())
				compileSubroutineDec(list);
		}
	}

	private int compileParameterList(List<String> list, StringBuilder strb, int i) {
		// check if type is valid
		String type = list.get(index);
		if (!isValidType(type, null))
			return i;
		else {
			index++;
			// append variable's name
			String name = list.get(index++);
			if (define(name, "arg", type)) {
				strb.append(writer.writePush(symtab.kindOf(name), symtab.indexOf(name))+"\n");
			}
		}
		String token = list.get(index);
		// check if ')' is present
		if (token.contains(")"))
			return i;
		else if (checkIfPresent(token, XML.SYMBOL, ",")) {
			index++;
		}
		return compileParameterList(list, strb, i++);
	}

	private void compileSubroutineBody(List<String> list) {
		openAndClose(list, "{");
		// compile variables
		compileVarDec(list);
		// compile statements
		statement.compile(list);
		openAndClose(list, "}");
	}

	private void compileVarDec(List<String> list) {
		String token = list.get(index);
		if (!token.contains("var")) 
			return;
		if (!checkIfPresent(token, XML.KEYWORD, "var"))
			return;
		index++;
		// check and append variable type
		String type = list.get(index++);
		if (!isValidType(type, null)) {
			System.err.println("Type must be 'int' or 'char' or 'boolean' or className");
			return;
		}
		handleVars(list, "var", type);

		compileVarDec(list);
	}

	private boolean isValidType(String token, String rule) {
		boolean rs = false;
		if (token.indexOf(XML.KEYWORD) == 0) {
			if (token.contains("int") || token.contains("char") || token.contains("boolean"))
				rs = true;
			if (rule != null && token.contains("void"))
				rs = true;
		}
		if (token.indexOf(XML.IDENTIFIER) == 0)
			rs = true;
		return rs;
	}

	@Override
	void handleVars(List<String> list, String k, String t) {
		super.handleVars(list, k, t);
		String token = list.get(index++);
		if (token.indexOf(XML.SYMBOL) == 0)
			if (token.contains(",") || token.contains(";")) {
				if (token.contains(","))
					handleVars(list, k, t);
				else
					return;
			} else
				System.err.println("Only ',' or ';' is allowed");
		else
			System.err.println("',' or ';' is expected after variables");
	}

}
