package br.ufrpe.mips.simulator.utils;

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
    return null;
  }

}
