package fvm;

import java.util.*;

/**
 * Created by mltaskova on 4/9/19.
 */
public class ASMLine {
    /**
     * Operation Code.
     */
    Opcode op;
    /**
     * Address or null.
     */
    String address;
    /**
     * Immediate value or null.
     */
    Integer immediate;
    /**
     * Whether or not this instruction is conditioned on "current" being zero.
     */
    boolean ifZero = false;

    String label;

    public ASMLine(Opcode op, String address, Integer im, boolean ifz, String label){
        this.op = op;
        assert(address == null || im == null);
        //assert either label or op?
        this.address = address;
        this.immediate = im;
        this.ifZero = ifz;
        this.label = label;
    }

    public static HashMap<String, Integer> varNum(List<ASMLine> code){
        HashMap<String, Integer> vars = new HashMap<>();
        for (ASMLine line : code){
            if (line.op != null) {
                if (line.op.equals(Opcode.Store)) {
                    if (vars.isEmpty())
                        vars.put(line.address, 0);
                    else if (!vars.containsKey(line.address)) {
                        vars.put(line.address, Collections.max(vars.values()) + 1);
                    }
                }
            }
        }
        return vars;
    }

    public static HashMap<String ,Integer> labelNum(List<ASMLine> code){
        HashMap<String, Integer> labels = new HashMap<>();
        int lineNum = 0;
        int temp = 1;
        for (ASMLine line: code){
            lineNum++;
            if (line.op == null && line.label != null){
                labels.put(line.label, lineNum-temp);
                temp++;
            }
        }
        return labels;
    }

    public Instruction toInstruction(HashMap<String, Integer> vars, HashMap<String, Integer> labels){
        if (this.op == null){
            //its a label
            return null;
        }
        else{
            if (this.op.equals(Opcode.GotoAbs) || this.op.equals(Opcode.GotoRel))
                return new Instruction(this.op, vars.get(this.label), labels.get(this.address), this.ifZero);
            return new Instruction(this.op, vars.get(this.address), this.immediate, this.ifZero);
        }
    }

    public static ASMLine parse (int lineNum, String input){
        LinkedList<String> words = new LinkedList<>(Arrays.asList(input.trim().split("\\s+")));
        if (words.isEmpty()) {
            return null;
        }

        Integer imm = null;
        String address = null;
        String label = null;
        boolean ifz = false;

        String first = words.poll();
        if (first.endsWith(":")){
            //it's a label
            return new ASMLine(null, address,imm,ifz, first.replace(":",""));
        }
        else if (first.equals("goto")){
            if (words.size() == 2)
                first = "goto-rel";
            else
                first = "goto-abs";
        }
        Opcode op = Opcode.fromString(first);

        while(!words.isEmpty()) {
            String next = words.poll().trim();
            if (next.matches("\\d")){
                // better be an immediate value!
                if (imm != null) {
                    throw new RuntimeException(lineNum+": Too many immediate values! "+ input);
                }
                imm = Integer.parseInt(next);
            }else if (next.equals("ifz")) {
                if (ifz) {
                    throw new RuntimeException(lineNum+": Too many ifz! "+ input);
                }
                ifz = true;
            }
            else{
                // address
                if (address != null) {
                    throw new RuntimeException(lineNum+": Too many addresses! "+ input);
                }
                address = next;
            }
        }
        return new ASMLine(op, address, imm, ifz, label);
    }

}
