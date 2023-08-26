package analyzer.grammar;

import java.util.List;

public class Statements extends IGrammar {
	private Expressions exp;
	
	Statements() {
		exp = new Expressions();
	}
	
	public void compile(List<String> list, StringBuilder strb, int num) {
		indent(strb, "<statements>", num);
		compileStatements(list, strb, num+1);
		indent(strb, "</statements>", num);
	}
	
	public void compileStatements(List<String> list, StringBuilder strb, int num) {
		String token = list.get(index);
		if (token.indexOf(XML.KEYWORD)==0) {
			if (token.contains("let")) {
				compileLet(list, strb, num);
			} else if (token.contains("if")) {
				compileIf(list, strb, num);
			} else if (token.contains("while")) {
				compileWhile(list, strb, num);
			} else if (token.contains("do")) {
				compileDo(list, strb, num);
			} else if (token.contains("return")) {
				compileReturn(list, strb, num);
			} else {
				return;
			}
		} else {
			return;
		}
		compileStatements(list, strb, num);
	}
	
	private void compileLet(List<String> list, StringBuilder strb, int num) {
		int indentation = num+1;
		indent(strb, "<letStatement>", num);
		// append 'let'
		indent(strb, list.get(index++), indentation);
		// append var name
		handleVars(list, strb, indentation);
		
		String token = list.get(index);
		if (token.indexOf(XML.SYMBOL)==0) {
			if (token.contains("[")) {
				index++;
				indent(strb, token, indentation);
				exp.compile(list, strb, indentation);
				openAndClose(list, strb, "]", indentation);
			}
		}
		
		openAndClose(list, strb, "=", indentation);
		exp.compile(list, strb, indentation);
		openAndClose(list, strb, ";", indentation);
		
		indent(strb, "</letStatement>", num);
	}
	
	private void compileIf(List<String> list, StringBuilder strb, int num) {
		int indentation = num+1;
		indent(strb, "<ifStatement>", num);
		// append 'if'
		indent(strb, list.get(index++), indentation);
		// compile expression
		compileExpression(list, strb, indentation);
		// compile statement
		compileStatement(list, strb, indentation);
		
		String ELSE = list.get(index);
		if (ELSE.indexOf(XML.KEYWORD)==0) {
			if (ELSE.contains("else")) {
				index++;
				indent(strb, ELSE, indentation);
				// compile statement
				compileStatement(list, strb, indentation);
			}
		}
		indent(strb, "</ifStatement>", num);
	}
	
	private void compileWhile(List<String> list, StringBuilder strb, int num) {
		int indentation = num+1;
		indent(strb, "<whileStatement>", num);
		// append 'while'
		indent(strb, list.get(index++), indentation);
		// compile expression
		compileExpression(list, strb, indentation);
		// compile statement
		compileStatement(list, strb, indentation);
		indent(strb, "</whileStatement>", num);
	}
	
	private void compileDo(List<String> list, StringBuilder strb, int num) {
		int indentation = num+1;
		indent(strb, "<doStatement>", num);
		indent(strb, list.get(index++), indentation);
		
		String token = list.get(index++);
		if (token.indexOf(XML.IDENTIFIER)==0) {
			indent(strb, token, indentation);
			String next = list.get(index);
			if (next.indexOf(XML.SYMBOL)==0) {
				// subroutine call case
				if (next.contains(".") || next.contains("(")) {
					exp.compileSubroutineCall(list, strb, indentation);
				}
			}
		}
		openAndClose(list, strb, ";", indentation);
		indent(strb, "</doStatement>", num);
	}
	
	private void compileReturn(List<String> list, StringBuilder strb, int num) {
		int indentation = num+1;
		indent(strb, "<returnStatement>", num);
		indent(strb, list.get(index++), indentation);
		String token = list.get(index);
		if (!(token.indexOf(XML.SYMBOL)==0)) {
			exp.compile(list, strb, indentation);
		}
		openAndClose(list, strb, ";", indentation);
		indent(strb, "</returnStatement>", num);
	}
	
	private void compileExpression(List<String> list, StringBuilder strb, int indentation) {
		openAndClose(list, strb, "(", indentation);
		exp.compile(list, strb, indentation);
		openAndClose(list, strb, ")", indentation);
	}
	
	private void compileStatement(List<String> list, StringBuilder strb, int indentation) {
		openAndClose(list, strb, "{", indentation);
		compile(list, strb, indentation);
		openAndClose(list, strb, "}", indentation);
	}
}
