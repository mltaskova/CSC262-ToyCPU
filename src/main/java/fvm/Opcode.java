package fvm;

public enum Opcode {
	Load, Store, AtomicIncr, AtomicDecr, Add, Mul, Div, Sub, Not, And, Or, GotoAbs, GotoRel;
	
	public static Opcode fromString(String x) {
		switch (x.toLowerCase()) {
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