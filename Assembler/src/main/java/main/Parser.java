package main;

public class Parser {

	private Handler handler;

	Parser() {
		this.handler = new Handler();
		;
	}

	String symbolParse(String line, int lines) {
		if (line.isBlank())
			return "";

		int comment = line.indexOf("//");
		if (comment != -1) {
			if (comment == 0)
				return "";
			line = line.substring(0, comment);
		} else {
			if (line.indexOf("(") != -1) {
				int open = line.indexOf("(");
				int close = line.indexOf(")");
				if (close != -1) {
					line = line.substring(open + 1, close);
					handler.setSymbol(line, lines);
				}
				return "";
			}
		}

		line = line.strip();
		return line;
	}

	String parse(String line) {

		if (line.indexOf("@") != -1) {
			line = handler.insA(line.substring(line.indexOf("@") + 1));
		} else {
			line = handler.insC(line);
		}

		return line;
	}

	String varParse(String line, int value) {
		String rs = null;
		if (line.indexOf("@") != -1 && !Character.isDigit(line.charAt(1)))
			if (handler.setSymbol(line.substring(line.indexOf("@") + 1), value))
				rs = "";

		return rs;
	}

}
