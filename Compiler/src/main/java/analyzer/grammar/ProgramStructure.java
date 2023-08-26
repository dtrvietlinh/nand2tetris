package analyzer.grammar;

import java.util.List;

public class ProgramStructure extends IGrammar {

	private Statements statement;

	public ProgramStructure() {
		statement = new Statements();
	}
	
	public String compile(List<String> list) {
		IGrammar.index=0;
		return compileClass(list);
	}

	private String compileClass(List<String> list) {
		StringBuilder strb = new StringBuilder();

		strb.append("<class>\n");
		// check if class token is missing
		String CLASS = list.get(index++);
		if (checkIfPresent(CLASS, XML.KEYWORD, "class"))
			indent(strb, CLASS, 1);

		// check if className is missing
		String className = list.get(index++);
		if (checkIfPresent(className, XML.IDENTIFIER, ""))
			indent(strb, className, 1);

		openAndClose(list, strb, "{", 1);
		// compile class variables if has some
		compileClassVarDec(list, strb);
		// compile subroutine
		compileSubroutineDec(list, strb);
		openAndClose(list, strb, "}", 1);

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
			indent(strb, "<classVarDec>", 1);
			// check if variable's type is valid
			for (int i = index + 2; index < list.size(); index++) {
				String t = list.get(index);
				// append 'static' or 'field' and variable's type
				if (index < i)
					indent(strb, t, 2);
				// append variables name
				else {
					handleVars(list, strb, "", 2);
					break;
				}
			}
			indent(strb, "</classVarDec>", 1);
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
			indent(strb, "<subroutineDec>", 1);

			for (int i=index+2; index < i; index++)
				indent(strb, list.get(index), 2);
			// append subroutine name
			handleVars(list, strb, null, 2);
			// compile parameter list
			openAndClose(list, strb, "(", 2);
			indent(strb, "<parameterList>", 2);
			compileParameterList(list, strb);
			indent(strb, "</parameterList>", 2);
			openAndClose(list, strb, ")", 2);
			
			// compile subroutine  body
			indent(strb, "<subroutineBody>", 2);
			compileSubroutineBody(list, strb);
			indent(strb, "</subroutineBody>", 2);
			
			indent(strb, "</subroutineDec>", 1);
			if (index<list.size())
				compileSubroutineDec(list, strb);
		}
	}

	private void compileParameterList(List<String> list, StringBuilder strb) {
		// check if type is valid
		String type = list.get(index);
		if (!isValidType(type, null)) return;
		else {
			index++;
			indent(strb, type, 3);
			// append variable's name
			handleVars(list, strb, 3);
		}
		String token = list.get(index);
		// check if ')' is present
		if (token.contains(")")) return;
		else if (checkIfPresent(token, XML.SYMBOL, ",")) {
			index++;
			indent(strb, token, 3);
		}
		compileParameterList(list, strb);
	}

	private void compileSubroutineBody(List<String> list, StringBuilder strb) {
		openAndClose(list, strb, "{", 3);
		// compile variables
		compileVarDec(list, strb);
		// compile statements
		statement.compile(list, strb, 3);	
		openAndClose(list, strb, "}", 3);
	}

	private void compileVarDec(List<String> list, StringBuilder strb) {
		String token = list.get(index);
		if (!token.contains("var")) return;
		if (!checkIfPresent(token, XML.KEYWORD, "var"))
			return;
		indent(strb, "<varDec>", 3);
		index++;
		// append 'var'
		indent(strb, token, 4);
		
		// check and append variable type
		String type = list.get(index++);
		if (isValidType(type, null))
			indent(strb, type, 4);
		else
			System.err.println("Type must be 'int' or 'char' or 'boolean' or className");
		handleVars(list, strb, "", 4);
		indent(strb, "</varDec>", 3);

		compileVarDec(list, strb);
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
	

	private void handleVars(List<String> list, StringBuilder strb, String rule, int num) {
		super.handleVars(list, strb, num);

		if (rule != null) {
			String token = list.get(index++);
			if (token.indexOf(XML.SYMBOL) == 0)
				if (token.contains(",") || token.contains(";")) {
					indent(strb, token, num);
					if (token.contains(","))
						handleVars(list, strb, "", num);
					else
						return;
				} else
					System.err.println("Only ',' or ';' is allowed");
			else
				System.err.println("',' or ';' is expected after variables");
		}
	}

}
