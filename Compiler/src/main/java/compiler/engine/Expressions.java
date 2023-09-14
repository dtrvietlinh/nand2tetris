package compiler.engine;

import java.util.ArrayList;
import java.util.List;

public class Expressions extends Grammar {
	
	public void compile(List<String> list) {
		compileExpression(list, 0);
	}

	private int compileExpression(List<String> list, int i) {
		compileTerm(list, new ArrayList<String>());
		// check if ',' is present
		String token = list.get(index);
		if (token.indexOf(XML.SYMBOL) == 0)
			if (token.contains(",")) {
				index++;
				return compileExpression(list, i++);
			}
		return i;
	}

	public void compileTerm(List<String> list, List<String> stack) {
		String term = list.get(index++);
		// integer and string constant cases
		if (term.indexOf(XML.INT_CONST) == 0) {
			pushConstant(Integer.parseInt(term));
		}
		else if (term.indexOf(XML.STRING_CONST) == 0);
		
		// keyword constant case
		else if (term.indexOf(XML.KEYWORD) == 0) {
			if (term.contains("true")) {
				pushConstant(1);
				append("neg");
			}
			else if (term.contains("false") || term.contains("null"))
				pushConstant(0);
			else if (term.contains("this"));
		}
		else if (term.indexOf(XML.SYMBOL) == 0) {
			// unaryOp term case
			if (term.contains("-") || term.contains("~")) {
				compileTerm(list, stack);
				if (term.contains("-")) append("neg");
				else append("not");
			}
			// expression case
			if (term.contains("(")) {
				compileExpression(list, 0);
				openAndClose(list, ")");
			}
		}
		// varName cases
		else if (term.indexOf(XML.IDENTIFIER) == 0) {
			String next = list.get(index);
			if (next.indexOf(XML.SYMBOL) == 0) {
				// varName '[' expression ']' case
				if (next.contains("[")) {
					index++;
					compileExpression(list, 0);
					openAndClose(list, "]");
				}
				// subroutine call case
				else if (next.contains(".") || next.contains("(")) {
					compileSubroutineCall(list, next);
				}
			}
		}
		
		String op = list.get(index);
		if (op.indexOf(XML.SYMBOL) == 0) {
			String[] arr = op.split(" ");
			if (arr[1].equals("&lt;") || arr[1].equals("&amp;") || arr[1].equals("&gt;") || "+-*/|=".contains(arr[1])) {
				index++;
				stack.add(op);
				compileTerm(list, stack);
			}	
		} else if(stack.size()>0) {
			for (int i=stack.size()-1; i>=0; i--) {
				writeArithmetic(stack.get(i));
			}
		}
	}

	private int compileExpressionList(List<String> list) {
		String token = list.get(index);
		int args = 0;
		if (!(token.indexOf(XML.SYMBOL) == 0) || "(-~".contains(token)) {
			args = compileExpression(list, 1);
		}
		return args;
	}

	public void compileSubroutineCall(List<String> list, String name) {
		String next = list.get(index);
		if (next.indexOf(XML.SYMBOL) == 0) {
			if (next.contains(".")) {
				index++;
				name = name +"."+list.get(index++);
			}
		}
		// check if '(' is present
		openAndClose(list, "(");
		// compile expression list
		int args = compileExpressionList(list);
		// check if ')' is present
		openAndClose(list, ")");
		
		append(getWriter().writeCall(name, args));
	}
	
	private void writeArithmetic(String cmd) {
		append(getWriter().writeArithmetic(cmd));
	}
	
	private void pushConstant(int i) {
		append(getWriter().writePush("constant", i));
	}
}
