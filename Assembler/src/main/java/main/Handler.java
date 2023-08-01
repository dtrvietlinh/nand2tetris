package main;

public class Handler {

	private CTable cTable;
	private ATable aTable;
	
	Handler() {
		cTable = new CTable();
		aTable = new ATable();
	}

	String insA(String line) {
		
		if (aTable.getSymbol(line)!=null) {
			line=aTable.getSymbol(line);
		}

		return binaryStringA(line);
	}

	private String binaryStringA(String line) {
		line = Integer.toBinaryString(Integer.parseInt(line));
		final int length = 16;
		StringBuilder strb = new StringBuilder();
		while (strb.length() + line.length() < length) {
			strb.append(0);
		}
		strb.append(line);
		return strb.toString();
	}

	String insC(String line) {
		String destPattern = null;

		if (line.indexOf("=") != -1) {
			destPattern = line.substring(0, line.indexOf("="));
		}

		String dest = cTable.getDest(destPattern);

		String compPattern = line.substring(line.indexOf("=") + 1);
		String jumpPattern = null;
		if (compPattern.indexOf(";") != -1) {
			jumpPattern = compPattern.substring(line.indexOf(";") + 1);
			compPattern = compPattern.substring(0, compPattern.indexOf(";"));
		}

		String comp = cTable.getComp(compPattern);
		String jump = cTable.getJump(jumpPattern);
		return binaryStringC(dest, comp, jump);
	}

	private String binaryStringC(String dest, String comp, String jump) {
		StringBuilder strb = new StringBuilder();
		strb.append(111);
		strb.append(comp);
		strb.append(dest);
		strb.append(jump);
		return strb.toString();
	}

	boolean setSymbol(String line, int value) {
		boolean rs = false;
		if (aTable.getSymbol(line)==null) {
			aTable.setSymbol(line, value+"");
			rs=true;
		}
		return rs;
	}
}
