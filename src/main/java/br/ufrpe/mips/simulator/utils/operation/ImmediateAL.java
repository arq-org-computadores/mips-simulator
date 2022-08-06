package br.ufrpe.mips.simulator.utils.operation;

import br.ufrpe.mips.data.IMemoryManager;
import br.ufrpe.mips.data.IRegister;
import br.ufrpe.mips.simulator.utils.disassembler.MIPSDisassembler.AssemblyInstruction;
import br.ufrpe.mips.simulator.utils.instruction.InstructionFields.IField;

/**
 * Classe utilitária, executa instruções lógicas e aritméticas envolvendo imediatos
 * 
 * @version 1.0
 */
public class ImmediateAL {

  // Memória principal e registradores
  private IMemoryManager memory;

  public ImmediateAL(IMemoryManager memory) {
    this.memory = memory;
  }

  public void ADDI(AssemblyInstruction instruction, StringBuffer buffer) {
    // Lendo campos como sendo de uma instrução tipo I
    IField iField = instruction.fields().asIField();

    // Adquirindo registradores envolvidos na operação
    IRegister dest = this.memory.getRegisterFromNumber(iField.rt());
    IRegister r1 = this.memory.getRegisterFromNumber(iField.rs());

    // Obtendo valores
    int v1 = r1.read();
    int immediate = iField.immediate();

    // Checar por overflow
    try {
      Math.addExact(v1, immediate);
    } catch (ArithmeticException e) {
      buffer.append("overflow");
    }

    // Armazenar resultado
    dest.write(v1 + immediate);
  }

  public void ORI(AssemblyInstruction instruction, StringBuffer buffer) {
    // Lendo campos como sendo de uma instrução tipo I
    IField iField = instruction.fields().asIField();

    // Adquirindo registradores envolvidos na operação
    IRegister dest = this.memory.getRegisterFromNumber(iField.rt());
    IRegister rs = this.memory.getRegisterFromNumber(iField.rs());

    // Obtendo valores
    int v = rs.read();
    int immediate = iField.immediate();

    // Armazenar resultado (bitwise OR)
    dest.write(v | immediate);
  }

  public void XORI(AssemblyInstruction instruction, StringBuffer buffer) {
    // Lendo campos como sendo de uma instrução tipo I
    IField iField = instruction.fields().asIField();

    // Adquirindo registradores envolvidos na operação
    IRegister dest = this.memory.getRegisterFromNumber(iField.rt());
    IRegister rs = this.memory.getRegisterFromNumber(iField.rs());

    // Obtendo valores
    int v = rs.read();
    int immediate = iField.immediate();

    // Armazenar resultado (bitwise XOR)
    dest.write(v ^ immediate);
  }

}
