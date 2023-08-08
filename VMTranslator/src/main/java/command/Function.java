package command;

public class Function {
	
	private Memory memory;
	
	public Function() {
		memory = new Memory();
	}
	
	public String handle(String[] args) {
		String command = args[0];
		String rs = "";
		
		switch (command) {
		case "function":
			rs=func(args[1], Integer.parseInt(args[2]));
			break;
		case "call":
			
			break;		
		case "return":
			rs=ret();
			break;
		}
		
		return rs;
	}
	
	private String func(String name, int nVars) {
		StringBuilder strb = new StringBuilder();
		strb.append("("+name+")\n");
		
		for (int i=nVars; i>0; i--)
			strb.append(memory.push("constant", "0", null)+"\n");
		
		return strb.toString();
	}
	
	private String ret() {
		StringBuilder strb = new StringBuilder();
		// endFrame = LCL
		strb.append("@LCL\nD=M\n@endFrame\nM=D\n");
		// retAddr = *(endFrame - 5)
		strb.append(restore("R14", "5"));
		// ARG = pop()
		strb.append(memory.pop("argument", "0", null)+"\n");
		// SP = ARG + 1
		strb.append("D=A+1\n@SP\nM=D\n");
		// restore THIS, THAT, LCL, ARG
		strb.append(restore("THAT", "1"));
		strb.append(restore("THIS", "2"));
		strb.append(restore("ARG", "3"));
		strb.append(restore("LCL", "4"));
		// goto retAddr
		strb.append("@R14\nA=M\n0;JMP");
		
		return strb.toString();
	}
	
	private String restore(String segm, String i) {
		return String.format("@%s\nD=A\n@endFrame\nA=M-D\nD=M\n@%s\nM=D\n", i, segm);
	}
}
