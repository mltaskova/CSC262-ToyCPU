package fvm;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RunFile {
	/**
	 * Load "ASM" file for our CPU.
	 * @param path - the path to the file.
	 * @return a list of code instructions to run.
	 */
	public static List<Instruction> loadASMFile(File path) {
		List<ASMLine> code = new ArrayList<>();
		List<Instruction> asmCode = new ArrayList<>();
		HashMap<String, Integer> vars;
		HashMap<String, Integer> labels;
		try {
			int lineNum = 0;
			for (String line : Files.readAllLines(path.toPath())) {
				lineNum++;
				line = line.trim();
				int comment = line.indexOf('#');
				if (comment >= 0) {
					line = line.substring(0, comment).trim();
				}
				if (line.isEmpty()) {
					continue;
				}
				code.add(ASMLine.parse(lineNum, line));
			}
			vars = ASMLine.varNum(code);
			labels = ASMLine.labelNum(code);
			Instruction temp;
			lineNum = 0;
			for (ASMLine line : code){
				lineNum++;
				temp = line.toInstruction(vars, labels);
				if (temp != null)
					asmCode.add(temp);
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return asmCode;
	}

	/**
	 * Load the factorial program or the one provided by the user.
	 * @param args - the first argument is a file to run.
	 */
	public static void main(String[] args) {
		String file = "src/main/resources/factorial.masm";
		if (args.length >= 1) {
			file = args[0];
		}
		System.out.println("FVM: " + file);

		List<Instruction> code = loadASMFile(new File(file));
		CPUSim sim = new CPUSim(code);

		while (!sim.done()) {
			sim.exec();
		}
//		System.out.println(sim.current);

	}
}
