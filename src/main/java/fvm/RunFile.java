package fvm;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class RunFile {
	public static List<Instruction> loadASMFile(File path) {
		List<Instruction> code = new ArrayList<>();
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
				code.add(Instruction.parse(lineNum, line));
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return code;
	}

	public static void main(String[] args) {
		String file = "src/main/resources/factorial.asm";
		if (args.length >= 1) {
			file = args[0];
		}
		System.out.println("FVM: " + file);

		List<Instruction> code = loadASMFile(new File(file));
		CPUSim sim = new CPUSim(code);

		while (!sim.done()) {
			sim.exec();
		}
		System.out.println(sim.current);

	}
}
