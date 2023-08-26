package analyzer.grammar;

import java.util.List;

public class Expressions extends IGrammar {

	public void compile(List<String> list, StringBuilder strb, int num) {
		compileExpression(list, strb, num);
	}

	private void compileExpression(List<String> list, StringBuilder strb, int num) {
		indent(strb, "<expression>", num);
		compileTerm(list, strb, num + 1);
		indent(strb, "</expression>", num);
		// check if ',' is present
		String token = list.get(index);
		if (token.indexOf(XML.SYMBOL) == 0)
			if (token.contains(",")) {
				index++;
				indent(strb, token, num);
				compileExpression(list, strb, num);
			}
	}

	public void compileTerm(List<String> list, StringBuilder strb, int num) {
		int indentation = num + 1;
		indent(strb, "<term>", num);
		String term = list.get(index++);
		// integer and string constant cases
		if (term.indexOf(XML.INT_CONST) == 0 || term.indexOf(XML.STRING_CONST) == 0)
			indent(strb, term, indentation);
		// keyword constant case
		if (term.indexOf(XML.KEYWORD) == 0)
			if (term.contains("true") || term.contains("false") || term.contains("null") || term.contains("this"))
				indent(strb, term, indentation);
		if (term.indexOf(XML.SYMBOL) == 0) {
			// unaryOp term case
			if (term.contains("-") || term.contains("~")) {
				indent(strb, term, indentation);
				compileTerm(list, strb, indentation);
			}
			// expression case
			if (term.contains("(")) {
				indent(strb, term, indentation);
				compileExpression(list, strb, indentation);
				openAndClose(list, strb, ")", indentation);
			}
		}
		// varName cases
		if (term.indexOf(XML.IDENTIFIER) == 0) {
			indent(strb, term, indentation);
			String next = list.get(index);
			if (next.indexOf(XML.SYMBOL) == 0) {
				// varName '[' expression ']' case
				if (next.contains("[")) {
					index++;
					indent(strb, next, indentation);
					compileExpression(list, strb, indentation);
					openAndClose(list, strb, "]", indentation);
				}
				// subroutine call case
				else if (next.contains(".") || next.contains("(")) {
					compileSubroutineCall(list, strb, indentation);
				}
			}
		}
		indent(strb, "</term>", num);
		String op = list.get(index);
		if (op.indexOf(XML.SYMBOL) == 0) {
			String[] arr = op.split(" ");
			if (arr[1].equals("&lt;") || arr[1].equals("&amp;") || arr[1].equals("&gt;") || "+-*/|=".contains(arr[1])) {
				indent(strb, op, num);
				index++;
				compileTerm(list, strb, num);
			}	
		}
	}

	private void compileExpressionList(List<String> list, StringBuilder strb, int num) {
		indent(strb, "<expressionList>", num);
		String token = list.get(index);
		if (!(token.indexOf(XML.SYMBOL) == 0) || token.contains("("))
			compileExpression(list, strb, num + 1);
		indent(strb, "</expressionList>", num);
	}

	public void compileSubroutineCall(List<String> list, StringBuilder strb, int num) {
		String next = list.get(index);
		if (next.indexOf(XML.SYMBOL) == 0) {
			if (next.contains(".")) {
				index++;
				indent(strb, next, num);
				handleVars(list, strb, num);
			}
		}
		// check if '(' is present
		openAndClose(list, strb, "(", num);
		// compile expression list
		compileExpressionList(list, strb, num);
		// check if ')' is present
		openAndClose(list, strb, ")", num);
	}
}
