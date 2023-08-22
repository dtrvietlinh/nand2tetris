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
		String indentation = "".indent(classIndentation);
		// check if class token is missing
		String CLASS = list.get(index++);
		if (CLASS.indexOf("<keyword>") == 0) {
			if (CLASS.contains("class"))
				strb.append(indentation + CLASS);
			else
				System.err.println("Class token expected");
		} else {
			System.err.println("Class token expected");
		}

		// check if className is missing
		String className = list.get(index++);
		if (className.indexOf("<identifier>") == 0)
			strb.append(indentation + className.indent(classIndentation));
		else
			System.err.println("Syntax error: class name");

		// check if missing '{' symbol
		String openSymbol = list.get(index++);
		if (openSymbol.indexOf("<symbol>") == 0) {
			if (openSymbol.contains("{"))
				strb.append(indentation + openSymbol);
			else
				System.err.println("'{' is expected after class name");
		} else {
			System.err.println("'{' is expected after class name");
		}
		// compile class variables
		strb.append(compileClassVarDec(list));

		//compileSubroutineDec();

		strb.append("</class>\n");

		return strb.toString();
	}

	private String compileClassVarDec(List<String> list) {
		StringBuilder strb = new StringBuilder();
		String indent1 = "".indent(classIndentation * 2);
		String indent2 = "".indent(classIndentation * 3);
		boolean hadClassVarDec = false;
		strb.append(indent1 + "<classVarDec>\n");

		for (int i = index; i < list.size(); i++) {
			String s = list.get(i);
			String next = list.get(i + 1);

			boolean left = s.contains("static") || s.contains("field");
			boolean right = isValidType(next);

			if (left && right) {
				hadClassVarDec = true;
				for (int j = i; j < list.size(); j++) {
					String t = list.get(j);
					if (j < i + 2 || t.indexOf("<keyword>") == -1) {
						list.set(j, null);
						// here we go!
						strb.append(indent2 + t);
					} else
						System.err.println("';' is expected");

					if (t.contains(";"))
						if (t.indexOf("<symbol>") == 0) {
							i = j;
							break;
						} else
							System.err.println("Syntax error on " + t + " token");
				}
			} else {
				if (right)
					System.err.println("Syntax error on " + s + " token");
				if (left)
					System.err.println("Variable's type is missing");
			}

		}

		strb.append(indent1 + "</classVarDec>\n");

		return hadClassVarDec ? strb.toString() : "";
	}

	private void compileSubroutineDec(List<String> list) {
		StringBuilder strb = new StringBuilder();
		strb.append("<subroutineDec>\n");

		for (int i = index; i < list.size(); i++) {
			String s = list.get(i);
			if (s.contains("constructor") || s.contains("function") || s.contains("method")) {
				if (s.indexOf("<keyword>") == 0) {

				} else {
					System.err.println("Syntax error: subroutineDec " + s);
				}
			}

		}
		compileParameterList();

		compileSubroutineBody();
		strb.append("</subroutineDec>\n");

	}

	private void compileParameterList() {

	}

	private void compileSubroutineBody() {
		compileVarDec();

		statement.compileStatements();
	}

	private void compileVarDec() {

	}

	private boolean isValidType(String token) {
		boolean rs = false;
		if (token.indexOf("<keyword>") == 0)
			if (token.contains("int") || token.contains("char") || token.contains("boolean"))
				rs = true;
			else
				System.err.println("Type must be 'int' or 'char' or 'boolean' or className");
		if (token.indexOf("<identifier>") == 0)
			rs = true;
		return rs;
	}

	private void handleMultiVars(List<String> list, StringBuilder strb, int i) {
		String s = list.get(i++);
		if (s.indexOf("<identifier>")==0) {
			strb.append(s);
		} else {
			System.err.println("Variable's name is expected");
			return;
		}
		String token = list.get(i++);
		if (token.indexOf("<symbol>")==0) {
			if (token.contains(","))
				handleMultiVars(list, strb, i);
			else if (token.contains(";"))
				return;
		} else {
			System.err.println("',' or ';' is expected after variables");
		}
			
	}
}
