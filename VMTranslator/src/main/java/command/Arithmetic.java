package command;

public class Arithmetic {

	public String doMath(String command) {
		String rs = "@SP\n" + "AM=M-1\n" + "D=M\n" + "A=A-1\n";
		switch (command) {
		case "add":
			rs+="M=D+M";
			break;
		case "sub":
			rs+="M=M-D";
			break;
		case "neg":
			rs="@SP\n" + "A=M-1\n" + "M=-M";
			break;
		case "and":
			rs+="M=D&M";
			break;
		case "or":
			rs+="M=D|M";
			break;
		case "not":
			rs="@SP\n" + "A=M-1\n" + "M=!M";
			break;
		}
		return rs;
	}

	void eq() {

	}

	void gt() {

	}

	void lt() {

	}

	void not() {

	}
}
