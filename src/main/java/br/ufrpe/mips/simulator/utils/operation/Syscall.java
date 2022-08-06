package br.ufrpe.mips.simulator.utils.operation;

import br.ufrpe.mips.data.IMemoryLocation;
import br.ufrpe.mips.data.IMemoryManager;
import br.ufrpe.mips.data.IRegister;
import br.ufrpe.mips.simulator.utils.disassembler.MIPSDisassembler.AssemblyInstruction;
import br.ufrpe.mips.simulator.utils.register.RegisterMapper;

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
    int v0 = this.memory.getRegisterFromNumber(RegisterMapper.regNumberFromLabel("v0")).read();

    switch (v0) {
      case 1 -> this.printInteger();
      case 4 -> this.printString();
      case 5 -> this.readInteger();
      case 8 -> this.readString();
      default -> System.out.println("SYSCALL desconhecido (v0 = %d)".formatted(v0));
    }
  }

  private void printInteger() {
    int a0 = this.memory.getRegisterFromNumber(RegisterMapper.regNumberFromLabel("a0")).read();
    System.out.println(a0);
  }

  private void printString() {
    StringBuffer buffer = new StringBuffer();

    int a0 = this.memory.getRegisterFromNumber(RegisterMapper.regNumberFromLabel("a0")).read();
    long addr = Integer.toUnsignedLong(a0);

    while (true) {
      IMemoryLocation<Byte> l = this.memory.getByteMemoryLocationFromAddress(addr);
      byte value = l.read();

      if (value == 0) {
        // Terminamos de ler a string
        break;
      }

      buffer.append((char) value);

      // Buscar próximo caractere
      addr += 1;
    }

    System.out.println(buffer);
  }


  private void readInteger() {

  }

  private void readString() {

  }

}
