package analyzer.grammar;

import java.util.List;

public class ProgramStructure {

	private Statements statement;
	private int index = 0;
	private final int classIndentation = 2;

	public ProgramStructure() {
		statement = new Statements();
	}

	public String compileClass(List<String> list) {
		StringBuilder strb = new StringBuilder();

		strb.append("<class>\n");
		// check if class token is missing
		String CLASS = list.get(index++);
		if (checkIfPresent(CLASS, XML.KEYWORD, "class"))
			strb.append(CLASS.indent(classIndentation));

		// check if className is missing
		String className = list.get(index++);
		if (checkIfPresent(className, XML.IDENTIFIER, ""))
			strb.append(className.indent(classIndentation));

		// check if missing '{' symbol
		String openSymbol = list.get(index++);
		if (checkIfPresent(openSymbol, XML.SYMBOL, "{"))
			strb.append(openSymbol.indent(classIndentation));
		// compile class variables if has some
		compileClassVarDec(list, strb);
		// compile subroutine
		compileSubroutineDec(list, strb);

		strb.append("</class>\n");

		return strb.toString();
	}

	// ('static'|'field') type varName (',' varName)* ';'
	private void compileClassVarDec(List<String> list, StringBuilder strb) {
		String s = list.get(index);
		if (!s.contains("static") && !s.contains("field"))
			return;

		String type = list.get(index + 1);
		boolean left = s.indexOf(XML.KEYWORD) == 0;
		boolean right = isValidType(type, null);

		if (left && right) {
			strb.append("<classVarDec>\n".indent(classIndentation * 2));
			// check if variable's type is valid
			for (int i = index + 2; index < list.size(); index++) {
				String t = list.get(index);
				// append 'static' or 'field' and variable's type
				if (index < i)
					strb.append(t.indent(classIndentation * 3));
				// append variables name
				else {
					handleVars(list, strb, "", 3);
					break;
				}
			}
			strb.append("</classVarDec>\n".indent(classIndentation * 2));
		} else if (left) {
			System.err.println("Type must be 'int' or 'char' or 'boolean' or className");
		} else if (right) {
			System.err.println("Syntax error on " + s + " token");
		}
		compileClassVarDec(list, strb);
	}

	private void compileSubroutineDec(List<String> list, StringBuilder strb) {
		String s = list.get(index);
		if (!s.contains("constructor") && !s.contains("function") && !s.contains("method"))
			return;

		String type = list.get(index + 1);
		boolean left = s.indexOf(XML.KEYWORD) == 0;
		boolean right = isValidType(type, "");

		if (left && right) {
			strb.append("<subroutineDec>\n".indent(classIndentation * 2));

			for (int i=index+2; index < i; index++)
					strb.append(list.get(index).indent(classIndentation * 3));
			// append subroutine name
			handleVars(list, strb, null, 3);
			// check if open parenthesis is present
			String open = list.get(index++);
			if (checkIfPresent(open, XML.SYMBOL, "("))
				strb.append(open.indent(classIndentation * 3));
			// compile parameter list
			strb.append("<parameterList>".indent(classIndentation*3));
			compileParameterList(list, strb);
			strb.append("</parameterList>".indent(classIndentation*3));
			// check if close parenthesis is present
			String close = list.get(index++);
			if (checkIfPresent(close, XML.SYMBOL, ")"))
				strb.append(close.indent(classIndentation * 3));
			strb.append("</subroutineDec>\n".indent(classIndentation * 2));

		}
	}

	private void compileParameterList(List<String> list, StringBuilder strb) {
		// check if type is valid
		String type = list.get(index);
		if (!isValidType(type, null)) return;
		else {
			index++;
			strb.append(type.indent(classIndentation*4));
			// append variable's name
			handleVars(list, strb, null, 4);
		}
		String token = list.get(index);
		if (token.contains(")")) return;
		else if (checkIfPresent(token, XML.SYMBOL, ",")) {
			index++;
			strb.append(token.indent(classIndentation*4));
		}
		// check if ')' is present
		compileParameterList(list, strb);
	}

	private void compileSubroutineBody() {
		compileVarDec();

		statement.compileStatements();
	}

	private void compileVarDec() {

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
	

	private int handleVars(List<String> list, StringBuilder strb, String rule, int num) {
		String s = list.get(index++);

		if (s.indexOf(XML.IDENTIFIER) == 0) {
			strb.append(s.indent(classIndentation * num));
		} else {
			System.err.println("Variable's name is expected");
			return index;
		}

		if (rule != null) {
			String token = list.get(index++);
			if (token.indexOf(XML.SYMBOL) == 0)
				if (token.contains(",") || token.contains(";")) {
					strb.append(token.indent(classIndentation * num));
					if (token.contains(","))
						return handleVars(list, strb, "", num);
					else
						return index;
				} else
					System.err.println("Only ',' or ';' is allowed");
			else
				System.err.println("',' or ';' is expected after variables");
		}

		return index;
	}
	
	private boolean checkIfPresent(String token, String xml, String symbol) {
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
}
