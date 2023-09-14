package compiler.engine;

import java.util.List;

public class Statements extends Grammar {
	private Expressions exp;
	
	Statements() {
		exp = new Expressions();
	}
	
	public void compile(List<String> list) {
		compileStatements(list);
	}
	
	public void compileStatements(List<String> list) {
		String token = list.get(index);
		if (token.indexOf(XML.KEYWORD)==0) {
			if (token.contains("let")) {
				compileLet(list);
			} else if (token.contains("if")) {
				compileIf(list);
			} else if (token.contains("while")) {
				compileWhile(list);
			} else if (token.contains("do")) {
				compileDo(list);
			} else if (token.contains("return")) {
				compileReturn(list);
			} else {
				return;
			}
		} else {
			return;
		}
		compileStatements(list);
	}
	
	private void compileLet(List<String> list) {
		String let = list.get(index++);
		if (!checkIfPresent(let, XML.KEYWORD, "let"))
			return;		
		// append var name
		String name = list.get(index++);	
		String token = list.get(index);
		if (token.indexOf(XML.SYMBOL)==0) {
			if (token.contains("[")) {
				index++;
				exp.compile(list);
				openAndClose(list, "]");
			}
		}
		
		openAndClose(list, "=");
		exp.compile(list);
		openAndClose(list, ";");
		writePop(name);
	}
	
	private void compileIf(List<String> list) {
		String IF = list.get(index++);
		if (!checkIfPresent(IF, XML.KEYWORD, "if"))
			return;			
		// compile expression
		compileExpression(list);
		String label1 = "IF-COND"+index;
		append("not");
		// if-goto L1
		append(getWriter().writeIf(label1));
		// compile statement
		compileStatement(list);
		
		String ELSE = list.get(index);
		if (ELSE.indexOf(XML.KEYWORD)==0) {
			if (ELSE.contains("else")) {
				index++;
				String label2 = "IF-COND"+index;
				// goto L2
				append(getWriter().writeGoto(label2));
				// L1
				append(getWriter().writeLabel(label1));
				// compile statement
				compileStatement(list);
				// L2
				append(getWriter().writeLabel(label2));
			}
		} else {
			// L1
			append(getWriter().writeLabel(label1));
		}
	}
	
	private void compileWhile(List<String> list) {
		String WHILE = list.get(index++);
		if (!checkIfPresent(WHILE, XML.KEYWORD, "while"))
			return;
		String label1 = "WHILE-COND "+index;
		// L1
		append(getWriter().writeLabel(label1));
		// compile expression
		compileExpression(list);
		append("not");
		String label2 = "WHILE-COND "+index;
		// if-goto L2
		append(getWriter().writeIf(label2));
		// compile statement
		compileStatement(list);
		// goto L1
		append(getWriter().writeGoto(label1));
		// L2
		append(getWriter().writeLabel(label2));
	}
	
	private void compileDo(List<String> list) {
		String DO = list.get(index++);
		if (!checkIfPresent(DO, XML.KEYWORD, "do"))
			return;		
		String token = list.get(index++);
		if (token.indexOf(XML.IDENTIFIER)==0) {
			String next = list.get(index);
			if (next.indexOf(XML.SYMBOL)==0) {
				// subroutine call case
				if (next.contains(".") || next.contains("(")) {
					exp.compileSubroutineCall(list, token);
				}
			}
		}
		openAndClose(list, ";");
	}
	
	private void compileReturn(List<String> list) {
		String RET = list.get(index++);
		if (!checkIfPresent(RET, XML.KEYWORD, "return"))
			return;	
		String token = list.get(index);
		if (!(token.indexOf(XML.SYMBOL)==0)) {
			exp.compile(list);
		}
		openAndClose(list, ";");
	}
	
	private void compileExpression(List<String> list) {
		openAndClose(list, "(");
		exp.compile(list);
		openAndClose(list, ")");
	}
	
	private void compileStatement(List<String> list) {
		openAndClose(list, "{");
		compile(list);
		openAndClose(list, "}");
	}
}
