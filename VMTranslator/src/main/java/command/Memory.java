package command;

public class Memory {
	private Segment segment;
	
	public Memory() {
		segment = new Segment();
	}
	
	public String push(String[] args, String filename) {
		StringBuilder strb = new StringBuilder();
		String addr = threeSegments(args[1], args[2], filename, 1);
		if (addr==null) 
			addr = fourSegments(args[1], args[2], 1);
		
		strb.append(addr);
		strb.append("\n");
		strb.append("@SP\nAM=M+1\nA=A-1\nM=D");
		return strb.toString();
	}
	
	public String pop(String[] args, String filename) {
		StringBuilder strb = new StringBuilder();		
		String segm = args[1];
		
		if (segm.equals("static") || segm.equals("pointer") || segm.equals("temp")) {
			strb.append("@SP\nAM=M-1\nD=M\n");
			strb.append(threeSegments(segm, args[2], filename, 0));
		} else {
			strb.append(fourSegments(args[1], args[2], 0));
			strb.append("\n");
			strb.append("@SP\nAM=M-1\nD=M\n@R13\nA=M\nM=D");
		}
		
		return strb.toString();
	}
	
	private String threeSegments(String segm, String i, String filename, int num) {
		String rs=null;
		switch (segm) {
		case "temp":
			rs=segment.temp(i, num);
			break;
		case "pointer":
			rs=segment.pointer(i, num);
			break;
		case "static":
			rs=segment.s_static(i, filename, num);
			break;
		}
		return rs;
	}
	
	private String fourSegments(String segm, String i, int num) {
		String rs=null;
		switch (segm) {
		case "temp":
			rs=segment.temp(i, num);
			break;
		case "constant":
			rs=segment.constant(i);
			break;
		default:
			rs=segment.fourSegments(segm, i, num);
		}
		return rs;
	}
}
