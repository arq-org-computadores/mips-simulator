package br.ufrpe.mips.simulator.utils.operation;

import br.ufrpe.mips.data.IMemoryManager;
import br.ufrpe.mips.data.IRegister;
import br.ufrpe.mips.simulator.utils.disassembler.MIPSDisassembler.AssemblyInstruction;
import br.ufrpe.mips.simulator.utils.instruction.InstructionFields.IField;
import br.ufrpe.mips.simulator.utils.instruction.InstructionFields.JField;
import br.ufrpe.mips.simulator.utils.instruction.InstructionFields.RField;
import br.ufrpe.mips.simulator.utils.register.RegisterMapper;

/**
 * Classe utilitária, executa instruções desvio condicional e pulos.
 * 
 * @version 1.0
 */
public class JumpBranch {

  // Memória principal e registradores
  private IMemoryManager memory;

  public JumpBranch(IMemoryManager memory) {
    this.memory = memory;
  }

  public void J(AssemblyInstruction instruction, StringBuffer buffer) {
    // Lendo campos como sendo de uma instrução tipo J
    JField jField = instruction.fields().asJField();

    // Atualizando PC
    this.memory.getPC().write(jField.address());
  }

  public void JR(AssemblyInstruction instruction, StringBuffer buffer) {
    // Lendo campos como sendo de uma instrução tipo R
    RField rField = instruction.fields().asRField();

    // Obtendo registrador
    IRegister dest = this.memory.getRegisterFromNumber(rField.rs());

    // Atualizando PC
    this.memory.getPC().write(dest.read());
  }

  public void JAL(AssemblyInstruction instruction, StringBuffer buffer) {
    // Lendo campos como sendo de uma instrução tipo J
    JField jField = instruction.fields().asJField();

    // Calcular próximo endereço do PC (ou seja, PC + 4)
    long nextPC = Integer.toUnsignedLong(this.memory.getPC().read()) + 4;

    // Salvar próximo endereço no $ra
    int regNumber = RegisterMapper.regNumberFromLabel("ra");
    this.memory.getRegisterFromNumber(regNumber).write((int) nextPC);

    // Atualizar PC para novo endereço
    this.memory.getPC().write(jField.address());
  }

  public void BEQ(AssemblyInstruction instruction, StringBuffer buffer) {
    // Lendo campos como sendo de uma instrução tipo I
    IField iField = instruction.fields().asIField();

    // Obtendo registradores envolvidos na operação
    IRegister rs = this.memory.getRegisterFromNumber(iField.rs());
    IRegister rt = this.memory.getRegisterFromNumber(iField.rt());

    long offset = 4L;

    // Caso os registradores possuam mesmo valor, podemo entrar
    // na branch desejada.
    if (rs.read() == rt.read()) {
      offset += iField.immediate() * 4L;
    }

    // Obter localização atual do programa
    long address = Integer.toUnsignedLong(this.memory.getPC().read());

    // Atualizar nova localização
    address += offset;

    // Atualizar PC para nova localização
    this.memory.getPC().write((int) address);
  }

  public void BNE(AssemblyInstruction instruction, StringBuffer buffer) {
    // Lendo campos como sendo de uma instrução tipo I
    IField iField = instruction.fields().asIField();

    // Obtendo registradores envolvidos na operação
    IRegister rs = this.memory.getRegisterFromNumber(iField.rs());
    IRegister rt = this.memory.getRegisterFromNumber(iField.rt());

    long offset = 4L;

    // Caso os registradores NÃO possuam mesmo valor, podemo entrar
    // na branch desejada.
    if (rs.read() != rt.read()) {
      offset += iField.immediate() * 4L;
    }

    // Obter localização atual do programa
    long address = Integer.toUnsignedLong(this.memory.getPC().read());

    // Atualizar nova localização
    address += offset;

    // Atualizar PC para nova localização
    this.memory.getPC().write((int) address);
  }

}
