package br.ufrpe.mips.simulator.utils;

import java.util.Arrays;

import br.ufrpe.mips.simulator.utils.InstructionFields.IField;
import br.ufrpe.mips.simulator.utils.InstructionFields.JField;
import br.ufrpe.mips.simulator.utils.InstructionFields.RField;

/**
 * Classe utilitária que implementa todo processo de tradução de uma instrução
 * em binário para Assembly para MIPS.
 * 
 * @version 1.0
 */
public final class MIPSDisassembler {

  public static record AssemblyInstruction(String assembly,
      MIPSInstruction instruction,
      InstructionFields fields) {
  }

  private MIPSDisassembler() {
    // Essa classe não pode ser instanciada,
    // apenas ofereces serviços de conversão.
  }

  /**
   * Esse método recebe uma instrução MIPS em Hexadecimal
   * e retornar a instrução equivalente em Assembly para MIPS.
   * 
   * @param hexInstruction instrução MIPS em hexadecimal.
   * @return {@link AssemblyInstruction} com representação em Assembly e campos da
   *         instrução.
   */
  public static AssemblyInstruction toAssembly(String hexInstruction) {
    long inst = Long.decode(hexInstruction);
    String binary = Long.toBinaryString(inst);

    // Adicionando 0's na frente para completar 32 bits
    binary = "0".repeat(32 - binary.length()) + binary;

    // Obtendo primeiro campo: opcode
    int opcode = fromBinary(binary, 0, 5);

    // Obtendo tipo da instrução
    InstructionType type = Arrays.stream(MIPSInstruction.values()).filter(i -> i.opcode() == opcode).findFirst()
        .orElseThrow().type();

    // Obtendo todos os campos da instrução
    int[] f = MIPSDisassembler.getFields(binary, type, opcode);
    InstructionFields fields = new InstructionFields(type, f);

    // Obter instrução
    MIPSInstruction instruction = Arrays.stream(MIPSInstruction.values())
        .filter(i -> {
          boolean eqOpcode = i.opcode() == opcode;
          boolean eqType = i.type() == type;
          boolean eqFunc = (i.funct().isEmpty() && i.type() != InstructionType.R)
              || (i.funct().get() == fields.asRField().funct());
          return eqOpcode && eqType && eqFunc;
        })
        .findFirst()
        .orElseThrow();
    String assembly = assemblyFromInstruction(instruction, fields);

    return new AssemblyInstruction(assembly, instruction, fields);
  }

  private static int[] getFields(String binary, InstructionType type, int opcode) {
    return switch (type) {
      case R -> new int[] { opcode,
          fromBinary(binary, 6, 10),
          fromBinary(binary, 11, 15),
          fromBinary(binary, 16, 20),
          fromBinary(binary, 21, 25),
          fromBinary(binary, 26, 31) };
      default -> null;
    };
  }

  private static int fromBinary(String binary, int start, int endInclusive) {
    return Integer.parseInt(binary.substring(start, endInclusive + 1), 2);
  }

  private static String assemblyFromInstruction(MIPSInstruction instruction, InstructionFields fields) {
    RField r = null;
    IField i = null;
    JField j = null;

    switch (fields.type()) {
      case R -> r = fields.asRField();
      case I -> i = fields.asIField();
      case J -> j = fields.asJField();
      case SYSCALL -> r = fields.asRField();
    }

    return switch (instruction) {
      case ADD -> "add $%d, $%d, $%d".formatted(r.rd(), r.rs(), r.rt());
      default -> "";
    };
  }

}
