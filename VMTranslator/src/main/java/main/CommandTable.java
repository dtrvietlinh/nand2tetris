package main;

import java.util.HashMap;
import java.util.Map;

public class CommandTable {
	private final Map<String, String> map = new HashMap<String, String>();
	
	public CommandTable() {
		// TODO Auto-generated constructor stub
		map.put("push", "C_PUSH");
		map.put("pop", "C_POP");
		map.put("add", "C_ARITHMETIC");
		map.put("sub", "C_ARITHMETIC");
		map.put("neg", "C_ARITHMETIC");
		map.put("eq", "C_ARITHMETIC");
		map.put("gt", "C_ARITHMETIC");
		map.put("lt", "C_ARITHMETIC");
		map.put("and", "C_ARITHMETIC");
		map.put("or", "C_ARITHMETIC");
		map.put("not", "C_ARITHMETIC");
		map.put("label", "C_BRANCHING");
		map.put("if-goto", "C_BRANCHING");
		map.put("goto", "C_BRANCHING");
	}
	
	public String commandType(String command) {
		return map.get(command);
	}
}
