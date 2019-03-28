package fvm;

import java.util.Arrays;
import java.util.LinkedList;

/**
 * This class represents an instruction.
 * 
 * @author jfoley
 *
 */
public class Instruction {
	/**
	 * Operation Code.
	 */
	Opcode op;
	/**
	 * Address or null.
	 */
	Integer address;
	/**
	 * Immediate value or null.
	 */
	Integer immediate;
	/**
	 * Whether or not this instruction is conditioned on "current" being zero.
	 */
	boolean ifZero = false;

	/**
	 * Here's an instruction!
	 * 
	 * @param op        - the operation to use.
	 * @param address   - the address of an argument.
	 * @param immediate - the immediate value of an argument.
	 * @param ifZero    - if true, only run this if current is zero.
	 */
	public Instruction(Opcode op, Integer address, Integer immediate, boolean ifZero) {
		this.op = op;
		assert(address == null || immediate == null);
		this.address = address;
		this.immediate = immediate;
		this.ifZero = ifZero;
	}

	/**
	 * Get address or crash.
	 * 
	 * @return the memory address associated with this instruction.
	 */
	public int getAddress() {
		if (address == null) {
			throw new IllegalStateException("Expected opcode: " + op + " to have an address!");
		}
		return address;
	}

	@Override
	public String toString() {
		return "Instruction(" + this.op + ", @" + this.address + ", !" + this.immediate + ", ifz=" + this.ifZero
				+ ")";
	}
	
	/**
	 * Read an assembly instruction from a given line from a file.
	 * @param lineNum - to report errors.
	 * @param input - the line the try and interpret.
	 * @return and Instruction object.
	 */
	public static Instruction parse(int lineNum, String input) {
		LinkedList<String> words = new LinkedList<>(Arrays.asList(input.trim().split("\\s+")));
		
		if (words.isEmpty()) {
			return null;
		}
		
		Opcode op = Opcode.fromString(words.poll());
		Integer imm = null;
		Integer address = null;
		boolean ifz = false;
		
		while(!words.isEmpty()) {
			String next = words.poll().trim();
			if (next.startsWith("@")) {
				// it's an address
				if (address != null) {
					throw new RuntimeException(lineNum+": Too many addresses! "+ input);
				}
				address = Integer.parseInt(next.substring(1));
			} else if (next.equals("ifz")) {
				if (ifz) {
					throw new RuntimeException(lineNum+": Too many ifz! "+ input);
				}
				ifz = true;
			} else {
				// better be an immediate value!
				if (imm != null) {
					throw new RuntimeException(lineNum+": Too many immediate values! "+ input);
				}
				imm = Integer.parseInt(next);
			}
		}
		
		return new Instruction(op, address, imm, ifz);
	}
	
}