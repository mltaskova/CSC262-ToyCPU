# CSC262 CPU/Opcode Playground

This simulated CPU has a single register, "current" and supports a very small number of instructions. Since it is so simple, it provides the ability to experiment.

There is no binary format to the instructions; only a simplified text format. Memory is available: 1024 32-bit int slots.

Here's a [python implementation via Google Colab](https://colab.research.google.com/drive/1ErPNgmszPVVzrPSJwdv_JG_Ge3boVTG6).

## Executable factorial(5) program

This is what works with the current system:

```
# the input
load 5 
# save it to locations 0 (fact) and 1 (n)
store @1
store @0

# n --
load @1
sub 1
store @1

# if zero, go to done
goto-rel 3 ifz

# fact *= n;
mul @0
store @0

# go back to n--
goto-abs 3

# load the computed factorial
load @0
```

Consider a nicer text version of this program: this is what we would like to work.

```
  # take a value as input
  prompt
  store n
  store fact

loop:

  # n -= 1
  load n
  sub 1
  store n

  # if (n == 0) finished! 
  goto done ifz

  # fact *= n
  mul fact
  store fact

  goto loop

done:
  # load the computed value to the "current" register
  load fact
  print
```

## Making better-factorial possible:

### (30) Create an input/output device for this CPU.

1. Create the "prompt" instruction. 
    1. Create an Opcode for Prompt.
    2. Support converting the "prompt" to an Opcode.
    3. Implement it in the CPU-switch statement. 
        1. Add a ``Scanner`` around ``System.in`` as a field to ``CPUSim``, call ``nextInt``?
        2. Read a number from the user, after some kind of generic prompt.
2. Create a "print" instruction, following similar steps.

### (20) Load the higher-level language bits:

- Construct a class ``ASMLine`` similar to Instruction that has its own parse method (arguments can now be strings!).

### (20) Variable Numbering
- Loop through this program, collecting all unique variables mentioned.
- Assign these variables numeric identifiers.
- Output raw assembly.

### (20) Label Numbering
- Convert your ``ASMLine`` labels to not be part of the instruction sequence, but owned by whatever instruction follows them.
- You'll need a way to mark labels on your ``ASMLine`` class.
- Convert the higher-level "goto" statement to either "goto-rel" or "goto-abs" lower-level commands.

### (10) Experience
- Support directly converting ``ASMLine`` programs to ``Instruction`` programs so that they can be run directly.
