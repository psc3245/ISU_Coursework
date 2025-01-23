/**
 * @author Peter Collins, Caleb Moe, Logan Becker
 */


package src;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Dictionary;

import java.util.Hashtable;
import java.util.Scanner;

public class Disassemble {

	public static String[] ops = {"ADD", "ADDI", "AND", "ANDI", "B", 
			"B.cond", "BL", "BR", "CBNZ", "CBZ", 
			"EOR", "EORI", "LDUR", "LSL", "LSR", 
			"ORR", "ORRI", "STUR", "SUB", "SUBI", 
			"SUBIS", "SUBS", "MUL", "PRNT", "PRNL", 
			"DUMP", "HALT"};
	public static int[][] opcodes = { {1112, 1112}, {1160, 1161}, {1104, 1104}, {1168, 1169},  {160, 191},
			{672, 679}, {1184, 1215}, {1712, 1712}, {1448, 1455}, {1440, 1447},
			{1616, 1616}, {1680, 1681}, {1986, 1986}, {1691, 1691}, {1690, 1690},
			{1360, 1360}, {1424, 1425}, {1984, 1984}, {1624, 1624}, {1672, 1673},
			{1928, 1929}, {1880, 1880}, {1240, 1240}, {2045, 2045}, {2044, 2044},
			{2046, 2046}, {2047, 2047} };

	public static String[] R = {"ADD","AND","EOR", "LSL","LSR", "ORR","SUB", "SUBS", "BR", "MUL", "PRNT", "PRNL", "DUMP", "HALT"};
	public static String[] I = {"ADDI","ANDI", "EORI", "ORRI","SUBI", "SUBIS"};
	public static String[] D = {"LDUR", "STUR"};
	public static String[] B = {"B", "BL"};
	public static String[] CB = {"B.cond", "CBNZ", "CBZ"};
	public static String[] conds = {"EQ", "NE", "HS", "LO", "MI", "PL", "VS", "VC", "HI", "LS", "GE", "LT", "GT", "LE"};
	
	


	public static void main(String[] args) throws IOException  {

		
		String filePath = "";
		
		if (args.length > 0) {
			filePath = args[0];
		}
		
		
		
		File file = new File(filePath);
		
		String filename = filePath + ".machine";
		
		File output = new File(filename);
		FileWriter outputWriter;

		int instructionCounter = 0;
		
		int currByte = 0;
		FileInputStream s;

		try {
			s = new FileInputStream(file);
			outputWriter = new FileWriter(output);

			try {
				while ((currByte = s.read()) != -1) {
					long currCode = 0b0;
					
					for (int i = 0; i < 3; i++) {
						currCode += currByte;
						currCode = currCode << 8;
						currByte = s.read();
					}
					currCode += currByte;
					
					int newInt = (int) currCode;
					
					System.out.print((instructionCounter + 1) + ": ");
					instructionCounter ++;
					String toWrite = printOneInstr(newInt, instructionCounter);
					toWrite = (instructionCounter) + ": " + toWrite + '\n';
					outputWriter.write(toWrite);
					
				}
				
				outputWriter.close();
				s.close();

				
			} catch (IOException e) {
			}
		} catch (FileNotFoundException e) {
		}
		
		


	}

	public static String printOneInstr(int code, int counter) {

		String instruction = "";
		
		try {
			instruction = getOpCode(code);
		} catch (Exception e) {
			e.printStackTrace();
		}

		String retFinal = "";

		if (contains(R, instruction)) {
			retFinal = RTypeInstruction(code, instruction);
			System.out.println(retFinal);
		}
		else if (contains(I, instruction)) {
			retFinal = ITypeInstruction(code, instruction);
			System.out.println(retFinal);
		}
		else if (contains(D, instruction)) {
			retFinal = DTypeInstruction(code, instruction);
			System.out.println(retFinal);
		}
		else if (contains(B, instruction)) {
			retFinal = BTypeInstruction(code, instruction, counter);
			System.out.println(retFinal);
		}
		else if (contains(CB, instruction)) {
			retFinal = CBTypeInstruction(code, instruction, counter);
			System.out.println(retFinal);

		}
		else {
			// this should never run! if it does that's bad
			System.out.println("bad");
		}
		
		return retFinal;

	}

	public static String RTypeInstruction(int code, String instruction) {

		String retInstruction = "";

		// edge cases
		if (instruction.equals("LSL") || instruction.equals("LSR")) {
			// this case will use shamt and not rm
			int rn = (code >> 5) & 0x1f;
			int rd = (code >> 0) & 0x1f;
			int shamt = (code >> 10) & 0x3f;
			retInstruction = instruction + " X" + rd + ", X" + rn + ", #" + shamt;

		}
		else if (instruction.equals("BR")) {
			// assumed that rt = rd, but double checking this would not hurt
			int rn = (code >> 5) & 0x1f;
			retInstruction = instruction + " X" + rn;
		}
		else if (instruction.equals("PRNT")) {
			// this one only uses Rd
			int rd = (code >> 0) & 0x1f;
			retInstruction = instruction + " X" + rd;
		}
		else if (instruction.equals("PRNL") || instruction.equals("HALT") || instruction.equals("DUMP")) {
			// no registers used - return the instruction
			retInstruction = instruction;
		}
		else {
			// normal case and will use Rd, Rn and Rm
			int rm = (code >> 16) & 0x1f;
			int rn = (code >> 5) & 0x1f;
			int rd = (code >> 0) & 0x1f;
			retInstruction = instruction + " X" + rd + ", X" + rn + ", X" + rm;
		}


		return retInstruction;
	}

	public static String ITypeInstruction(int code, String instruction) {

		String retInstruction = "";

		int rd = (code >> 0) & 0x1f;
		int rn = (code >> 5) & 0x1f;
		int alu = (code >> 10) & 0xfff; 
		retInstruction = instruction + " X" + rd + ", X" +rn + ", #" + alu;



		return retInstruction;
	}

	public static String DTypeInstruction(int code, String instruction) {

		String retInstruction = "";

		int rt = (code >> 0) & 0x1f;
		int rn = (code >> 5) & 0x1f;
		int dtAddress = (code >> 12) & 0x1ff; 
		retInstruction = instruction + " X" + rt + ", [X" + rn + ", #" + dtAddress + "]";



		return retInstruction;
	}

	public static String BTypeInstruction(int code, String instruction, int counter) {

		String retInstruction = "";
		int offset = checkForNeg(code, instruction);
		
		int line = counter + offset;

		retInstruction = instruction + " " + line;

		return retInstruction;
	}

	public static String CBTypeInstruction(int code, String instruction, int counter) {

		String retInstruction = "";
		

		int rt = (code >> 0) & 0x1f;
		
		int offset = checkForNeg(code, instruction);
		int line = counter + offset;

		if (instruction.equals("B.cond")){

			String cond = conds[rt];
			String justb = instruction.substring(0,2); //just “B.”
			justb = justb + cond; //b.whatever
			retInstruction = justb + " " + line;
		}

		else{
			retInstruction = instruction + " X" + rt + ", " + line;
		}

		return retInstruction;
	}






	public static boolean contains(String[] arr, String key) {
		
		if (key == null) {
			return false;
		}

		for (String s : arr) {
			if (key.equals(s)) {
				return true;
			}
		}

		return false;
	}
	
	public static int checkForNeg(int code, String instruction) {
		
		int negNum = 0;
		
		if (contains(I, instruction)) {
			// I type
			// check if first digit is 1 which would indicate negative
			if (((code >> 21) & 0x1) == 1) {
				// get alu_immediate binary from code
				int imm = (code >> 10) & 0xfff;
				negNum = 0xfff - imm + 1;
				return 0 - negNum;
			}
			else {
				return (code >> 10) & 0xfff;
			}
		}
		
		else if(contains(B, instruction)) {
			// B type
			// check if first digit is 1 which would indicate negative
			if (((code >> 25) & 0x1) == 1) {
				// get BRAddr from code
				int BRAddr = (code >> 0) & 0x3ffffff;
				// subtract BRAddr from max bit value 
				negNum = 0x3ffffff - BRAddr + 1;
				return 0 - negNum;
			}
			else {
				return (code >> 0) & 0x3ffffff;
			}
		}
		
		else if (contains(CB, instruction)) {
			// CB type
			// check if first digit is 1 which would indicate negative
			if (((code >> 23) & 0x1) == 1) {
				int CBAddr = (code >> 5) & 0x7ffff;
				negNum = 0x7ffff - CBAddr + 1;
				return 0 - negNum;	
			}
			else {
				return (code >> 5) & 0x7ffff;
			}
			
		}
		
		return negNum;
	}


	public static String getOpCode(int code) {

		// function goal: given a 32 bit integer, return the opcode

		int opcode = (code >> 21) & 0x7ff; // shift the code

		String op = null;

		for (int i = 0; i < ops.length; i++) {

			int start = opcodes[i][0];
			int end = opcodes[i][1];

			if ( opcode <= end && opcode>=start ){
				op = ops[i];
				break;
			}
		}


		return op;
	}

}
