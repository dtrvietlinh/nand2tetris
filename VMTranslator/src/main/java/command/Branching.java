package command;

public class Branching {
	public String jump(String[] args) {
		String command = args[0];
		String label = args[1];
		String rs = "";

		switch (command) {
		case "label":
			rs = "(" + label + ")";
			break;
		case "goto":
			rs = "@" + label + "\n" + "0;JMP";
			break;
		case "if-goto":
			String pop = "@SP\nAM=M-1\nD=M\n";
			rs = pop + "@" + label + "\n" + "D;JNE";
			break;
		}
		
		return rs;
	}
}
