package fvm;

public enum Opcode {
	Print, Prompt, Load, Store, AtomicIncr, AtomicDecr, Add, Mul, Div, Sub, Not, And, Or, GotoAbs, GotoRel;
	
	/**
	 * Convert a string from an assembly file into a Opcode.
	 * @param x - the string
	 * @return an enum value.
	 */
	public static Opcode fromString(String x) {
		switch (x.toLowerCase()) {
		case "print":
			return Print;
		case "prompt":
			return Prompt;
		case "load":
			return Load;
		case "store":
			return Store;
		case "atomic-incr":
			return AtomicIncr;
		case "atomic-decr":
			return AtomicDecr;
		case "add":
			return Add;
		case "mul":
			return Mul;
		case "div":
			return Div;
		case "sub":
			return Sub;
		case "not":
			return Not;
		case "and":
			return And;
		case "or":
			return Or;
		case "goto-abs":
			return GotoAbs;
		case "goto-rel":
			return GotoRel;
		default:
			throw new IllegalArgumentException("opcode=" + x);
		}
	}
}