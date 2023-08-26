package analyzer.grammar;

import java.util.List;

public class IGrammar {
	
	static int index;
	public final int classIndentation = 2;
	
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
	
	void openAndClose(List<String> list, StringBuilder strb, String symbol, int num) {
		String token = list.get(IGrammar.index++);
		if (checkIfPresent(token, XML.SYMBOL, symbol))
			indent(strb, token, num);
	}
	
	void indent(StringBuilder strb, String s, int num) {
		strb.append(s.indent(classIndentation*num));
	}
	
	void handleVars(List<String> list, StringBuilder strb, int num) {
		String s = list.get(IGrammar.index++);

		if (s.indexOf(XML.IDENTIFIER) == 0) {
			indent(strb, s, num);
		} else {
			System.err.println("Variable's name is expected");
			return;
		}
	}
}
