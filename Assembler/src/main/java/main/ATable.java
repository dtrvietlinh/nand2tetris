package main;

import java.util.HashMap;
import java.util.Map;

class ATable {
	private final Map<String, String> map = new HashMap<>();
	
	ATable() {
		map.put("SCREEN", "16384");
		map.put("KBD", "24576");
		map.put("SP", "0");
		map.put("LCL", "1");
		map.put("ARG", "2");
		map.put("THIS", "3");
		map.put("THAT", "4");
		map.put("R0", "0");
		map.put("R1", "1");
		map.put("R2", "2");
		map.put("R3", "3");
		map.put("R4", "4");
		map.put("R5", "5");
		map.put("R6", "6");
		map.put("R7", "7");
		map.put("R8", "8");
		map.put("R9", "9");
		map.put("R10", "10");
		map.put("R11", "11");
		map.put("R12", "12");
		map.put("R13", "13");
		map.put("R14", "14");
		map.put("R15", "15");
	}
	
	String getSymbol(String pattern) {
		return map.get(pattern);
	}
	
	void setSymbol(String pattern, String value) {
		map.put(pattern, value);
	}
}
