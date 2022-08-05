package br.ufrpe.mips.simulator.utils.operation;

import br.ufrpe.mips.data.IMemoryManager;
import br.ufrpe.mips.simulator.utils.disassembler.MIPSDisassembler.AssemblyInstruction;

/**
 * Classe utilitária, executa chamadas ao sistema.
 * 
 * @version 1.0
 */
public class Syscall {

  // Memória principal e registradores
  private IMemoryManager memory;

  public Syscall(IMemoryManager memory) {
    this.memory = memory;
  }

  public void SYSCALL(AssemblyInstruction instruction, StringBuffer buffer) {

  }

}
