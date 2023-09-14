 package compiler.table;

import java.util.HashMap;
import java.util.Map;

public class SymbolTable {
	
	private Map<String, Table> classMap;
	private Map<String, Table> subMap;
	private final String STATIC = "static";
	private final String FIELD = "field";
	private final String VAR = "var";
	private final String ARG = "arg";
	private int staticIndex;
	private int fieldIndex;
	private int argIndex;
	private int varIndex;
	
	public SymbolTable() {
		classMap = new HashMap<>();
		subMap = new HashMap<>();
	}
	
	public void define(String name, String k, String t) {
		switch (k) {
		case STATIC:
			classMap.put(name, new Table(staticIndex++, k, t));
			break;
		case FIELD:
			classMap.put(name, new Table(fieldIndex++, k, t));
			break;
		case VAR:
			subMap.put(name, new Table(varIndex++, k, t));
			break;
		case ARG:
			subMap.put(name, new Table(argIndex++, k, t));
			break;
		default:
			System.err.println("Syntax error on "+k+" kind");
			break;
		}	
	}
	
	public String kindOf(String name) {
		return checkIfPresent(name).kind;
	}
	
	public int indexOf(String name) {
		return checkIfPresent(name).index;
	}
	
	public String typeOf(String name) {
		return checkIfPresent(name).type;
	}
	
	public Table checkIfPresent(String name) {
		Table rs = subMap.get(name);
		if (rs==null)
			rs = classMap.get(name);
		return rs;
	}
	
	private class Table {
		private int index;
		private String kind;
		private String type;
		
		Table(int i, String k, String t) {
			this.index=i;
			this.kind=k;
			this.type=t;
		}
	}
}
