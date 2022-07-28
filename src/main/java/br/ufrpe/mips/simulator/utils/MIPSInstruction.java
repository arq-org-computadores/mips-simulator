package br.ufrpe.mips.simulator.utils;

import java.util.Optional;

/**
 * Classe utilitária contém uma lista de todas as
 * instruções MIPS suportadas pelo simulador.
 * 
 * @version 1.0
 */
public enum MIPSInstruction {
  SLL(InstructionType.R, 0, 0),
  SRL(InstructionType.R, 0, 2),
  SRA(InstructionType.R, 0, 3),
  SLLV(InstructionType.R, 0, 4),
  SRLV(InstructionType.R, 0, 6),
  SRAV(InstructionType.R, 0, 7),
  JR(InstructionType.R, 0, 8),
  MFHI(InstructionType.R, 0, 16),
  MFLO(InstructionType.R, 0, 18),
  MULT(InstructionType.R, 0, 24),
  MULTU(InstructionType.R, 0, 25),
  DIV(InstructionType.R, 0, 26),
  DIVU(InstructionType.R, 0, 27),
  ADD(InstructionType.R, 0, 32),
  ADDU(InstructionType.R, 0, 33),
  SUB(InstructionType.R, 0, 34),
  SUBU(InstructionType.R, 0, 35),
  AND(InstructionType.R, 0, 36),
  OR(InstructionType.R, 0, 37),
  XOR(InstructionType.R, 0, 38),
  NOR(InstructionType.R, 0, 39),
  SLT(InstructionType.R, 0, 42),
  BLTZ(InstructionType.I, 1, null),
  BNE(InstructionType.I, 5, null),
  BEQ(InstructionType.I, 4, null),
  BLEZ(InstructionType.I, 6, null),
  BGTZ(InstructionType.I, 7, null),
  ADDI(InstructionType.I, 8, null),
  ADDIU(InstructionType.I, 9, null),
  SLTI(InstructionType.I, 10, null),
  ANDI(InstructionType.I, 12, null),
  ORI(InstructionType.I, 13, null),
  XORI(InstructionType.I, 14, null),
  LUI(InstructionType.I, 15, null),
  LB(InstructionType.I, 32, null),
  LW(InstructionType.I, 35, null),
  LBU(InstructionType.I, 36, null),
  SB(InstructionType.I, 40, null),
  SW(InstructionType.I, 43, null),
  J(InstructionType.J, 2, null),
  JAL(InstructionType.J, 3, null),
  SYSCALL(InstructionType.SYSCALL, 0, 12);

  private InstructionType instructionType;
  private int opcode;
  private Optional<Integer> funct;

  private MIPSInstruction(InstructionType type, int opcode, Integer funct) {
    this.instructionType = type;
    this.opcode = opcode;
    this.funct = Optional.ofNullable(funct);
  }

  public int opcode() {
    return this.opcode;
  }

  public Optional<Integer> funct() {
    return this.funct;
  }

  public InstructionType type() {
    return this.instructionType;
  }

}
