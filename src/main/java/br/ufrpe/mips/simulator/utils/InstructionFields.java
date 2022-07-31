package br.ufrpe.mips.simulator.utils;

/**
 * Classe utilitária, representa uma instrução
 * MIPS válida onde o valor de cada campo é representado
 * por um inteiro de 32 bits.
 * 
 * @version 1.0
 */
public record InstructionFields(InstructionType type, int[] fields) {

  static record RField(int opcode, int rs, int rt, int rd, int shamt, int funct) {

  }

  static record IField(int opcode, int rs, int rt, int immediate) {

  }

  static record JField(int opcode, int address) {

  }

  public RField asRField() {
    return new RField(fields[0], fields[1], fields[2], fields[3], fields[4], fields[5]);
  }

  public IField asIField() {
    return new IField(fields[0], fields[1], fields[2], fields[3]);
  }

  public JField asJField() {
    return new JField(fields[0], fields[1]);
  }

}
