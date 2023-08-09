package command;

import java.util.HashMap;
import java.util.Map;

public class Function {
	
	private static Map<String, Integer> map = new HashMap<>();
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
			rs=call(args[1], args[2]);
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
			strb.append(push("0"));
		
		return strb.toString();
	}
	
	private String call(String name, String i) {
		StringBuilder strb = new StringBuilder();
		// push retAddr
		String retAddr="";
		Integer check = check(name);
		if (check==null) 
			retAddr = name+"$ret."+i;
		else {
			String addr = name+"$ret."+i;
			retAddr = addr +"."+ check;
			String label = addr+".REPEAT";
			return String.format("@%s\nD=A\n@%s\n0;JMP\n(%s)", retAddr, label, retAddr);
		}
		strb.append(push(retAddr));
		// push LCL, ARG, THIS, THAT
		strb.append(push("LCL"));
		strb.append(push("ARG"));
		strb.append(push("THIS"));
		strb.append(push("THAT"));
		// repositons ARG
		int num = 5 + Integer.parseInt(i);
		strb.append("@"+num+"\nD=A\n@SP\nD=M-D\n@ARG\nM=D\n");
		// repositions LCL
		strb.append("@SP\nD=M\n@LCL\nM=D\n");
		// go to function
		strb.append("@"+name+"\n0;JMP\n");
		strb.append("("+retAddr+")");
		return strb.toString();
	}
		
	private String push(String segm) {
		StringBuilder strb = new StringBuilder();
		switch (segm) {
		case "LCL":
		case "ARG":
		case "THIS":
		case "THAT":
			strb.append("@"+segm+"\nD=M\n");
			break;
		default:
			strb.append("@"+segm+"\nD=A\n");
			String label = segm+".REPEAT";
			strb.append(String.format("@%s\n(%s)\n", label, label));
			break;
		}
		strb.append("@SP\nAM=M+1\nA=A-1\nM=D\n");
		return strb.toString();
	}
	
	private String ret() {
		StringBuilder strb = new StringBuilder();
		if (map.get("return")==null)
			map.put("return", 1);
		else
			return "@RETURN\n0;JMP";
		
		strb.append("(RETURN)\n");
		// endFrame = LCL
		strb.append("@LCL\nD=M\n@endFrame\nM=D\n");
		// retAddr = *(endFrame - 5)
		strb.append("@5\nD=A\n@endFrame\nAM=M-D\nD=M\n@R14\nM=D\n");
		// ARG = pop()
		strb.append(memory.pop("argument", "0", null)+"\n");
		// SP = ARG + 1
		strb.append("D=A+1\n@SP\nM=D\n");
		// restore THIS, THAT, LCL, ARG
		strb.append(restore("LCL"));
		strb.append(restore("ARG"));
		strb.append(restore("THIS"));
		strb.append(restore("THAT"));
		// goto retAddr
		strb.append("@R14\nA=M\n0;JMP");
		
		return strb.toString();
	}
	
	private String restore(String segm) {
		return String.format("@endFrame\nAM=M+1\nD=M\n@%s\nM=D\n", segm);
	}
	
	private Integer check(String name) {
		Integer num = map.get(name);
		if (num==null) {
			map.put(name, 2);
		} else {
			map.put(name, map.get(name)+1);
		}
		return num;
	}
}
