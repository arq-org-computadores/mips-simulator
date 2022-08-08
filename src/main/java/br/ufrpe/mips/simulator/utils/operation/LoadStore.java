package br.ufrpe.mips.simulator.utils.operation;

import br.ufrpe.mips.data.IMemoryLocation;
import br.ufrpe.mips.data.IMemoryManager;
import br.ufrpe.mips.data.IRegister;
import br.ufrpe.mips.simulator.utils.disassembler.MIPSDisassembler.AssemblyInstruction;
import br.ufrpe.mips.simulator.utils.instruction.InstructionFields.IField;

/**
 * Classe utilitária, executa instruções de interação entre memória principal e registradores.
 * 
 * @version 1.0
 */
public class LoadStore {

  // Memória principal e registradores
  private IMemoryManager memory;

  public LoadStore(IMemoryManager memory) {
    this.memory = memory;
  }

  public void SW(AssemblyInstruction instruction, StringBuffer buffer) {
    // Lendo campos como sendo de uma instrução tipo I
    IField iField = instruction.fields().asIField();

    // Obtendo registradores envolvidos na operação
    IRegister data = this.memory.getRegisterFromNumber(iField.rt());
    IRegister r = this.memory.getRegisterFromNumber(iField.rs());

    // Obtendo endereço base e offset
    long baseAddress = Integer.toUnsignedLong(r.read());
    int offset = iField.immediate();

    // Calculando novo endereço
    long address = baseAddress + offset;

    // Obtendo localização de memória com 4 bytes
    IMemoryLocation<Integer> l = this.memory.getWordMemoryLocationFromAddress(address);

    // Escrevendo o valor do registrador na memória
    l.write(data.read());
  }

  public void LW(AssemblyInstruction instruction, StringBuffer buffer) {
    // Lendo campos como sendo de uma instrução tipo I
    IField iField = instruction.fields().asIField();

    // Obtendo registradores envolvidos na operação
    IRegister dest = this.memory.getRegisterFromNumber(iField.rt());
    IRegister r = this.memory.getRegisterFromNumber(iField.rs());

    // Obtendo endereço base e offset
    long baseAddress = Integer.toUnsignedLong(r.read());
    int offset = iField.immediate();

    // Calculando novo endereço
    long address = baseAddress + offset;

    // Obtendo localização de memória com 4 bytes
    IMemoryLocation<Integer> l = this.memory.getWordMemoryLocationFromAddress(address);

    // Escrevendo valor armazenado nessa posição ao registrador
    dest.write(l.read());
  }

  public void SB(AssemblyInstruction instruction, StringBuffer buffer) {
    // Lendo campos como sendo de uma instrução tipo I
    IField iField = instruction.fields().asIField();

    // Obtendo registradores envolvidos na operação
    IRegister data = this.memory.getRegisterFromNumber(iField.rt());
    IRegister r = this.memory.getRegisterFromNumber(iField.rs());

    // Obtendo endereço base e offset
    long baseAddress = Integer.toUnsignedLong(r.read());
    int offset = iField.immediate();

    // Calculando novo endereço
    long address = baseAddress + offset;

    // Obtendo localização de memória com 4 bytes
    IMemoryLocation<Byte> l = this.memory.getByteMemoryLocationFromAddress(address);

    // Escrevendo o valor do registrador na memória
    l.write((byte) data.read());
  }

  public void LB(AssemblyInstruction instruction, StringBuffer buffer) {
    // Lendo campos como sendo de uma instrução tipo I
    IField iField = instruction.fields().asIField();

    // Obtendo registradores envolvidos na operação
    IRegister dest = this.memory.getRegisterFromNumber(iField.rt());
    IRegister r = this.memory.getRegisterFromNumber(iField.rs());

    // Obtendo endereço base e offset
    long baseAddress = Integer.toUnsignedLong(r.read());
    int offset = iField.immediate();

    // Calculando novo endereço
    long address = baseAddress + offset;

    // Obtendo localização de memória com 4 bytes
    IMemoryLocation<Byte> l = this.memory.getByteMemoryLocationFromAddress(address);

    // Escrevendo valor armazenado nessa posição ao registrador
    dest.write((int) l.read());
  }

  public void LBU(AssemblyInstruction instruction, StringBuffer buffer) {
    // Lendo campos como sendo de uma instrução tipo I
    IField iField = instruction.fields().asIField();

    // Obtendo registradores envolvidos na operação
    IRegister dest = this.memory.getRegisterFromNumber(iField.rt());
    IRegister r = this.memory.getRegisterFromNumber(iField.rs());

    // Obtendo endereço base e offset
    long baseAddress = Integer.toUnsignedLong(r.read());
    int offset = iField.immediate();

    // Calculando novo endereço
    long address = baseAddress + offset;

    // Obtendo localização de memória com 4 bytes
    IMemoryLocation<Byte> l = this.memory.getByteMemoryLocationFromAddress(address);

    // Escrevendo valor armazenado nessa posição ao registrador
    dest.write((int) Byte.toUnsignedInt(l.read()));
  }

  public void LUI (AssemblyInstruction instruction, StringBuffer buffer) {
    // Lendo campos como sendo de uma instrução tipo I
    IField iField = instruction.fields().asIField();

    // Obtendo registradores envolvidos na operação
    IRegister dest = this.memory.getRegisterFromNumber(iField.rt());

    // Obtendo valor
    int immediate = iField.immediate() << 16;

    // Escrevendo valor armazenado nessa posição ao registrador
    dest.write(immediate);
  }

}
