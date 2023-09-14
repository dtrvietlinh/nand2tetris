package compiler.engine;

public final class OS {
	public final static String DIVIDE = "Math.divide()";
	public final static String MULTIPLY = "Math.multiply()";
	
	public static String newString(int length) {
		return "String.new("+length+")";
	}
	
	public static String appendString(String c) {
		return "String.append("+c+")";
	}
	
	public static String alloc(int size) {
		return "Memory.alloc("+size+")";
	}
}
