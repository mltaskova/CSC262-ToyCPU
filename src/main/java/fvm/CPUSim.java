package fvm;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a simulated CPU.
 * 
 * @author jfoley
 *
 */
public class CPUSim {
	/**
	 * The values to store in memory.
	 */
	public int[] mem;
	/**
	 * The code to execute.
	 */
	public List<Instruction> code;
	/**
	 * The program counter.
	 */
	public int pc;
	/**
	 * The only register in my ISA.
	 */
	public int current;

	/**
	 * Construct a new simulator from a list of instructions and a memory size.
	 * 
	 * @param code    - the instructions to execute.
	 * @param memSize - the size of the memory to make available.
	 */
	public CPUSim(List<Instruction> code, int memSize) {
		this.code = code;
		this.mem = new int[memSize];
		this.current = 0;
		this.pc = 0;
	}

	/**
	 * Construct a new simulator from a list of instructions
	 * 
	 * @param code
	 */
	public CPUSim(List<Instruction> code) {
		this(code, 1024);
	}

	public boolean done() {
		return pc >= code.size();
	}

	/**
	 * Get the value referred to by an instruction, either immediate or in memory.
	 * 
	 * @param instr - the instruction object.
	 * @return the value it refers to.
	 */
	public int getValue(Instruction instr) {
		if (instr.immediate != null) {
			return instr.immediate;
		} else if (instr.address != null) {
			return this.mem[instr.getAddress()];
		}
		throw new IllegalArgumentException("What value for " + instr);
	}

	/**
	 * Run a single instruction.
	 */
	public void exec() {
		Instruction instr = this.code.get(this.pc);
		this.pc += 1;
		
		if (instr.ifZero) {
			if (this.current != 0) {
				// Skip this instruction.
				return;
			} else {
				// execute conditional; fall-through to switch.
			}
		}

		switch (instr.op) {
		// Current Register manipulation:
		case Load:
			this.current = this.getValue(instr);
			break;
		case Store:
			this.mem[instr.getAddress()] = this.current;
			break;
		
		case AtomicDecr:
			this.mem[instr.getAddress()] -= 1;
			break;
		case AtomicIncr:
			this.mem[instr.getAddress()] += 1;
			break;
			
		// Arithmetic:
		case Add:
			this.current += getValue(instr);
			break;
		case Sub:
			this.current -= getValue(instr);
			break;
		case Div:
			this.current /= getValue(instr);
			break;
		case Mul:
			this.current *= getValue(instr);
			break;
			
		// PC manipulation:
		case GotoAbs:
			this.pc = getValue(instr);
			break;
		case GotoRel:
			this.pc += getValue(instr);
			break;
		
		// Boolean operations:
		case Not:
			if (this.current == 0) {
				this.current = 1;
			} else {
				this.current = 0;
			}
			break;
		case And:
			if (this.current != 0 && getValue(instr) != 0) {
				this.current = 1;
			} else {
				this.current = 0;
			}
			break;
		case Or:
			if (this.current != 0 || getValue(instr) != 0) {
				this.current = 1;
			} else {
				this.current = 0;
			}
			break;
		
		
		default:
			throw new IllegalArgumentException("op=" + instr.op);
		}
	}
	
	/**
	 * Hardcoded factorial 5 program.
	 * @param args (none)
	 */
	public static void main(String[] args) {
		int N = 1;
		int fact = 0;
		// fib
		List<Instruction> factorial = new ArrayList<>();
		factorial.add(new Instruction(Opcode.Load, null, 5, false));
		factorial.add(new Instruction(Opcode.Store, N, null, false));
		factorial.add(new Instruction(Opcode.Store, fact, null, false));
		// 3: n--
		factorial.add(new Instruction(Opcode.Load, N, null, false));
		factorial.add(new Instruction(Opcode.Sub, null, 1, false));
		factorial.add(new Instruction(Opcode.Store, N, null, false));
		factorial.add(new Instruction(Opcode.GotoRel, null, 3, true));
		factorial.add(new Instruction(Opcode.Mul, fact, null, false));
		factorial.add(new Instruction(Opcode.Store, fact, null, false));
		factorial.add(new Instruction(Opcode.GotoAbs, null, 3, false));
		// 10: load fact:
		factorial.add(new Instruction(Opcode.Load, fact, null, false));

		CPUSim sim = new CPUSim(factorial);
		while(!sim.done()) {
			sim.exec();
		}
		System.out.println(sim.current);

	}
}
