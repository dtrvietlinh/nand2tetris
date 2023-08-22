package analyzer.tokenizer;

import analyzer.grammar.LexicalElements;

public class JackTokenizer {
	private LexicalElements lexEle;
	private final String symbolPattern = "{}()[].,;+-*/&|<>=~";
	private boolean isComment = false;
	
	public JackTokenizer() {
		lexEle = new LexicalElements();
	}
	
	public String parse(String line) {
		StringBuilder rs = new StringBuilder();
		if (line.isBlank() || isComment(line) || isComment)
			return null;

		line=line.strip();
		int comment = line.indexOf("//");
		if (comment != -1) {
			if (comment == 0)
				return null;
			line = line.substring(0, comment);
		}
		
		char[] arr = line.toCharArray();
		StringBuilder strb = new StringBuilder();
		
		for (int i=0; i<arr.length; i++) {
			char c = arr[i];
			
			if (Character.isLetter(c) || Character.isDigit(c) || c=='_') 
				strb.append(c);
			
			else if (c=='"') {
				int last = line.lastIndexOf(c);
				if (last==i)
					System.err.println("Invalid string!");
				else {
					while (i<last)
						strb.append(arr[i++]);
					rs.append(tokenType(strb.toString()));
					strb = new StringBuilder();
				}
				
			} else {
				if (strb.length()!=0) {
					rs.append(tokenType(strb.toString()));
					strb = new StringBuilder();
				}
				if (symbolPattern.indexOf(c)!=-1)
					rs.append(tokenType(Character.toString(c)));
			}
		}
		
		return rs.toString();
	}
	
	public String tokenType(String token) {
		String rs = null;
		String type = lexEle.getType(token);
		
		switch (type) {
		case TokenType.KEYWORD:
			rs=tokenize(token, type);
			break;
		case TokenType.SYMBOL:
			if (token.equals("<")) token="&lt;";
			else if (token.equals(">")) token="&gt;";
			else if (token.equals("\"")) token="&quot;";
			else if (token.equals("&")) token="&amp;";
			rs=tokenize(token, type);
			break;
		case TokenType.IDENTIFIER:
			rs=tokenize(token, type);
			break;
		case TokenType.INT_CONST:
			rs=tokenize(token, type);
			break;
		case TokenType.STRING_CONST:
			token=token.substring(1);
			rs=tokenize(token, type);
			break;
		}
		if (rs==null) rs="INVALID TOKEN!";
		return rs+"\n";
	}
	
	private String tokenize(String token, String type) {
		StringBuilder strb = new StringBuilder();
		strb.append("<"+type+">");
		strb.append(" "+token+" ");
		strb.append("</"+type+">");
		return strb.toString();
	}
	
	private boolean isComment(String line) {
		boolean rs=false;
		if (line.contains("/*")) isComment=true;
		if (line.contains("*/")) {
			isComment=false;
			rs=true;
		}
		return rs;
	}
}
