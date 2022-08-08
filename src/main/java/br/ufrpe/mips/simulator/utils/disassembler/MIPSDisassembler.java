package br.ufrpe.mips.simulator.utils.disassembler;

import java.util.Arrays;

import br.ufrpe.mips.simulator.utils.instruction.InstructionFields;
import br.ufrpe.mips.simulator.utils.instruction.InstructionType;
import br.ufrpe.mips.simulator.utils.instruction.MIPSInstruction;
import br.ufrpe.mips.simulator.utils.instruction.InstructionFields.IField;
import br.ufrpe.mips.simulator.utils.instruction.InstructionFields.JField;
import br.ufrpe.mips.simulator.utils.instruction.InstructionFields.RField;

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

    // Variável auxiliar para ser utilizada dentro da função lambda
    InstructionFields fFields = fields;

    // Obter instrução
    MIPSInstruction instruction = Arrays.stream(MIPSInstruction.values())
        .filter(i -> {
          // Checando se as informações batem
          if (i.opcode() == opcode) {
            if (i.type() == type) {
              if (i.type() == InstructionType.R) {
                return i.funct().get() == fFields.asRField().funct();
              }

              return true;
            } else if (i.type() == InstructionType.SYSCALL && type == InstructionType.R) {
              return i.funct().get() == fFields.asRField().funct();
            }
          }

          return false;
        })
        .findFirst()
        .orElseThrow();

    // Caso a instrução seja um SYSCALL, atualizar tipo.
    if (instruction == MIPSInstruction.SYSCALL) {
      fields = new InstructionFields(InstructionType.SYSCALL, fields.fields());
    }

    String assembly = assemblyFromInstruction(instruction, fields);

    return new AssemblyInstruction(assembly, instruction, fields);
  }

  private static int[] getFields(String binary, InstructionType type, int opcode) {
    return switch (type) {
      case R, SYSCALL -> new int[] { opcode,
          fromBinary(binary, 6, 10),
          fromBinary(binary, 11, 15),
          fromBinary(binary, 16, 20),
          fromBinary(binary, 21, 25),
          fromBinary(binary, 26, 31) };
      case I -> new int[] { opcode,
          fromBinary(binary, 6, 10),
          fromBinary(binary, 11, 15),
          (short) fromBinary(binary, 16, 31) // constante 16-bits
      };
      case J -> new int[] { opcode,
          4 * fromBinary(binary, 6, 31) // endereço real é 4 vezes o valor do campo
      };
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
      case R, SYSCALL -> r = fields.asRField();
      case I -> i = fields.asIField();
      case J -> j = fields.asJField();
    }

    return switch (instruction) {
      case DIV -> "div $%d, $%d".formatted(r.rs(), r.rt());
      case MULT -> "mult $%d, $%d".formatted(r.rs(), r.rt());
      case MFLO -> "mflo $%d".formatted(r.rd());
      case MFHI -> "mfhi $%d".formatted(r.rd());
      case ADD -> "add $%d, $%d, $%d".formatted(r.rd(), r.rs(), r.rt());
      case ADDU -> "addu $%d, $%d, $%d".formatted(r.rd(), r.rs(), r.rt());
      case SUB -> "sub $%d, $%d, $%d".formatted(r.rd(), r.rs(), r.rt());
      case ADDI -> "addi $%d, $%d, %d".formatted(i.rt(), i.rs(), i.immediate());
      case J -> "j %d".formatted(Integer.toUnsignedLong(j.address()));
      case LW -> "lw $%d, %d($%d)".formatted(i.rt(), i.immediate(), i.rs());
      case SW -> "sw $%d, %d($%d)".formatted(i.rt(), i.immediate(), i.rs());
      case JR -> "jr $%d".formatted(r.rd());
      case JAL -> "jal %d".formatted(Integer.toUnsignedLong(j.address()));
      case BEQ -> "beq $%d, $%d, %d".formatted(i.rs(), i.rt(), i.immediate());
      case BNE -> "bne $%d, $%d, %d".formatted(i.rs(), i.rt(), i.immediate());
      case LB -> "lb $%d, %d($%d)".formatted(i.rt(), i.immediate(), i.rs());
      case SB -> "sb $%d, %d($%d)".formatted(i.rt(), i.immediate(), i.rs());
      case DIVU -> "divu $%d, $%d".formatted(r.rs(), r.rt());
      case SUBU -> "subu $%d, $%d, $%d".formatted(r.rd(), r.rs(), r.rt());
      case MULTU -> "multu $%d, $%d".formatted(r.rs(), r.rt());
      case SLLV -> "sllv $%d, $%d, $%d".formatted(r.rd(), r.rt(), r.rs());
      case SRLV -> "srlv $%d, $%d, $%d".formatted(r.rd(), r.rt(), r.rs());
      case SRAV -> "srav $%d, $%d, $%d".formatted(r.rd(), r.rt(), r.rs());
      case ORI -> "ori $%d, $%d, %d".formatted(i.rt(), i.rs(), i.immediate());
      case XORI -> "xori $%d, $%d, %d".formatted(i.rt(), i.rs(), i.immediate());
      case SLL -> "sll $%d, $%d, %d".formatted(r.rd(), r.rt(), r.shamt());
      case SLT -> "slt $%d, $%d, $%d".formatted(r.rd(), r.rs(), r.rt());
      case SRL -> "srl $%d, $%d, %d".formatted(r.rd(), r.rt(), r.shamt());
      case SLTI -> "slti $%d, $%d, %d".formatted(i.rt(), i.rs(), i.immediate());
      case SRA -> "sra $%d, $%d, %d".formatted(r.rd(), r.rt(), r.shamt());
      case XOR -> "xor $%d, $%d, $%d".formatted(r.rd(), r.rs(), r.rt());
      case NOR -> "nor $%d, $%d, $%d".formatted(r.rd(), r.rs(), r.rt());
      case SYSCALL -> "syscall";
      default -> "";
    };
  }

}
