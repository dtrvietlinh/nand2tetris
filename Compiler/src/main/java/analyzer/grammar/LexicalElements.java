package analyzer.grammar;

import java.util.HashMap;
import java.util.Map;

import analyzer.tokenizer.TokenType;

public class LexicalElements {
	private Map<String, String> map = new HashMap<>();
	private final String keyword = TokenType.KEYWORD;
	private final String symbol = TokenType.SYMBOL;
	private final String identifer = TokenType.IDENTIFIER;
	private final String int_const = TokenType.INT_CONST;
	private final String string_const = TokenType.STRING_CONST;

	public LexicalElements() {
		map.put("class", keyword);
		map.put("constructor", keyword);
		map.put("function", keyword);
		map.put("method", keyword);
		map.put("field", keyword);
		map.put("static", keyword);
		map.put("var", keyword);
		map.put("int", keyword);
		map.put("char", keyword);
		map.put("boolean", keyword);
		map.put("void", keyword);
		map.put("true", keyword);
		map.put("false", keyword);
		map.put("null", keyword);
		map.put("this", keyword);
		map.put("let", keyword);
		map.put("do", keyword);
		map.put("if", keyword);
		map.put("else", keyword);
		map.put("while", keyword);
		map.put("return", keyword);

	}

	public String getType(String token) {
		String rs = null;
		// check if token has already encountered 
		if (rs == null) rs = map.get(token);
		// check if symbol
		final String symbolPattern = "{}()[].,;+-*/&|<>=~";
		if (rs == null) {
			if (token.length()==1 && symbolPattern.contains(token)) {
				rs = symbol;
				map.put(token, symbol);
			}
		}
		// check if integer
		if (rs == null) {
			try {
				int num = Integer.parseInt(token);
				if (num>=0 && num<=32767) rs = int_const;
			} catch (NumberFormatException e) {
			
			}
		}
		// check if string
		if (rs == null) {
			if (token.charAt(0) == '"') {
				char[] arr = token.toCharArray();
				boolean valid = true;
				
				for (int i = 1; i < arr.length; i++) {
					if (arr[i] == '"' || arr[i] == '\n') {
						valid = false;
						break;
					}
				}
				
				if (valid) rs = string_const;
			}
		}
		// check if identifer
		if (rs == null) {
			if (!Character.isDigit(token.charAt(0))) {				
				rs = identifer;
				map.put(token, identifer);
			}
		}
		
		return rs;
	}
}
