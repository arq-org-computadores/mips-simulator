package br.ufrpe.mips.simulator.utils.operation;

import java.util.Scanner;
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
    // try with resource
    // Abre e fechar o scanner após execução do código
    try (Scanner scanner = new Scanner(System.in)) {
      // Leitura do inteiro
      int i = scanner.nextInt();

      // Escrita no registrador
      int regNumber = RegisterMapper.regNumberFromLabel("v0");
      IRegister dest = this.memory.getRegisterFromNumber(regNumber);
      dest.write(i);
    }
  }

  private void readString() {
    // Leitura da String
    Scanner scanner = new Scanner(System.in);
    String line = scanner.nextLine();
    scanner.close();

    // Leitura dos parâmetros
    int regNumber = RegisterMapper.regNumberFromLabel("a0");
    int addr = this.memory.getRegisterFromNumber(regNumber).read();

    regNumber = RegisterMapper.regNumberFromLabel("a1");
    int maxChars = this.memory.getRegisterFromNumber(regNumber).read();

    long baseAddr = Integer.toUnsignedLong(addr);
    long offset = 0L;

    if (maxChars < 1 || line.length() > maxChars - 1) {
      return;
    }

    if (maxChars == 1) {
      this.memory.getByteMemoryLocationFromAddress(baseAddr).write((byte) '\0');
      return;
    }

    for (char ch : line.toCharArray()) {
      this.memory.getByteMemoryLocationFromAddress(baseAddr + offset).write((byte) ch);
      offset += 1;
    }

    this.memory.getByteMemoryLocationFromAddress(baseAddr + offset).write((byte) '\0');
  }

}
