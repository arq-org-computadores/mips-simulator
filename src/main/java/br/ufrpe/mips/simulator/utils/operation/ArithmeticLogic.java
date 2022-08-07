package br.ufrpe.mips.simulator.utils.operation;

import br.ufrpe.mips.data.IMemoryManager;
import br.ufrpe.mips.data.IRegister;
import br.ufrpe.mips.simulator.utils.disassembler.MIPSDisassembler.AssemblyInstruction;
import br.ufrpe.mips.simulator.utils.instruction.InstructionFields.RField;

/**
 * Classe utilitária, executa instruções lógicas e aritméticas envolvendo registradores.
 * 
 * @version 1.0
 */
public class ArithmeticLogic {

  // Memória principal e registradores
  private IMemoryManager memory;

  public ArithmeticLogic(IMemoryManager memory) {
    this.memory = memory;
  }

  public void ADD(AssemblyInstruction instruction, StringBuffer buffer) {
    // Lendo campos como sendo de uma instrução tipo R
    RField rField = instruction.fields().asRField();

    // Adquirindo registradores envolvidos na operação
    IRegister dest = this.memory.getRegisterFromNumber(rField.rd());
    IRegister s1 = this.memory.getRegisterFromNumber(rField.rs());
    IRegister s2 = this.memory.getRegisterFromNumber(rField.rt());

    // Lendo valores dos registradores
    int v1 = s1.read();
    int v2 = s2.read();

    try {
      // Gera exceção em caso de overflow
      Math.addExact(v1, v2);

      // Armazenar resultado
      dest.write(v1 + v2);
    } catch (ArithmeticException e) {
      buffer.append("overflow");
    }
  }

  public void DIVU(AssemblyInstruction instruction, StringBuffer buffer) {
    // Lendo campos como sendo de uma instrução tipo R
    RField rField = instruction.fields().asRField();

    // Adquirindo registradores envolvidos na operação
    IRegister rs = this.memory.getRegisterFromNumber(rField.rs());
    IRegister rt = this.memory.getRegisterFromNumber(rField.rt());

    // Lendo valores dos registradores
    int v1 = rs.read();
    int v2 = rt.read();

    // Convertendo valores para versões sem sinal
    long uV1 = Integer.toUnsignedLong(v1);
    long uV2 = Integer.toUnsignedLong(v2);

    // Calculando quociente e resto
    long quotient = 0; // Inicialmente, 0
    long remainder = 0; // Inicialmente, 0

    // Caso o divisor seja diferente de 0, podemos calcular
    if (uV2 != 0) {
      quotient = uV1 / uV2;
      remainder = uV1 % uV2;
    }

    // Escrevendo na memória
    this.memory.getLO().write((int) quotient);
    this.memory.getHI().write((int) remainder);
  }

  public void SUBU(AssemblyInstruction instruction, StringBuffer buffer) {
    // Lendo campos como sendo de uma instrução tipo R
    RField rField = instruction.fields().asRField();

    // Adquirindo registradores envolvidos na operação
    IRegister dest = this.memory.getRegisterFromNumber(rField.rd());
    IRegister rs = this.memory.getRegisterFromNumber(rField.rs());
    IRegister rt = this.memory.getRegisterFromNumber(rField.rt());

    // Lendo valores dos registradores
    int v1 = rs.read();
    int v2 = rt.read();

    // Convertendo valores para versões sem sinal
    long uV1 = Integer.toUnsignedLong(v1);
    long uV2 = Integer.toUnsignedLong(v2);

    // Calculando resultado
    long result = uV1 - uV2;

    // Escrevendo na memória
    dest.write((int) result);
  }

  public void MULTU(AssemblyInstruction instruction, StringBuffer buffer) {
    // Lendo campos como sendo de uma instrução tipo R
    RField rField = instruction.fields().asRField();

    // Adquirindo registradores envolvidos na operação
    IRegister rs = this.memory.getRegisterFromNumber(rField.rs());
    IRegister rt = this.memory.getRegisterFromNumber(rField.rt());

    // Lendo valores dos registradores
    int v1 = rs.read();
    int v2 = rt.read();

    // Convertendo valores para versões sem sinal
    long uV1 = Integer.toUnsignedLong(v1);
    long uV2 = Integer.toUnsignedLong(v2);

    // Calculando resultado
    long result = uV1 * uV2;

    // Escrevendo na memória
    this.memory.getLO().write((int) result);
    this.memory.getHI().write((int) (result >> 32));
  }

  public void SLLV(AssemblyInstruction instruction, StringBuffer buffer) {
    // Lendo campos como sendo de uma instrução tipo R
    RField rField = instruction.fields().asRField();

    // Adquirindo registradores envolvidos na operação
    IRegister dest = this.memory.getRegisterFromNumber(rField.rd());
    IRegister rt = this.memory.getRegisterFromNumber(rField.rt());
    IRegister rs = this.memory.getRegisterFromNumber(rField.rs());

    // Lendo valores dos registradores
    int v1 = rt.read();
    int v2 = rs.read();

    // Considerando apenas os últimos 5 bits de rs
    v2 = v2 & 0b11111;

    // Calculando resultado
    int result = v1 << v2;

    // Escrevendo na memória
    dest.write(result);
  }

  public void SLL(AssemblyInstruction instruction, StringBuffer buffer) {
    // Lendo campos como sendo de uma instrução tipo R
    RField rField = instruction.fields().asRField();

    // Adquirindo registradores envolvidos na operação
    IRegister dest = this.memory.getRegisterFromNumber(rField.rd());
    IRegister rt = this.memory.getRegisterFromNumber(rField.rt());

    // Lendo valores dos registradores
    int v = rt.read();
    int immediate = rField.shamt();

    // Calculando resultado
    int result = v << immediate;

    // Escrevendo na memória
    dest.write(result);
  }

  public void SRL(AssemblyInstruction instruction, StringBuffer buffer) {
    // Lendo campos como sendo de uma instrução tipo R
    RField rField = instruction.fields().asRField();

    // Adquirindo registradores envolvidos na operação
    IRegister dest = this.memory.getRegisterFromNumber(rField.rd());
    IRegister rt = this.memory.getRegisterFromNumber(rField.rt());

    // Lendo valores dos registradores
    int v = rt.read();
    int immediate = rField.shamt();

    // Calculando resultado
    int result = v >>> immediate;

    // Escrevendo na memória
    dest.write(result);
  }

  public void SRA(AssemblyInstruction instruction, StringBuffer buffer) {
    // Lendo campos como sendo de uma instrução tipo R
    RField rField = instruction.fields().asRField();

    // Adquirindo registradores envolvidos na operação
    IRegister dest = this.memory.getRegisterFromNumber(rField.rd());
    IRegister rt = this.memory.getRegisterFromNumber(rField.rt());

    // Lendo valores dos registradores
    int v = rt.read();
    int immediate = rField.shamt();

    // Calculando resultado
    int result = v >> immediate;

    // Escrevendo na memória
    dest.write(result);
  }

  public void SLT(AssemblyInstruction instruction, StringBuffer buffer) {
    // Lendo campos como sendo de uma instrução tipo R
    RField rField = instruction.fields().asRField();

    // Adquirindo registradores envolvidos na operação
    IRegister dest = this.memory.getRegisterFromNumber(rField.rd());
    IRegister rs = this.memory.getRegisterFromNumber(rField.rs());
    IRegister rt = this.memory.getRegisterFromNumber(rField.rt());

    // Salvando resultado
    dest.write(rs.read() < rt.read() ? 1 : 0);
  }

  public void SRLV(AssemblyInstruction instruction, StringBuffer buffer) {
    // Lendo campos como sendo de uma instrução tipo R
    RField rField = instruction.fields().asRField();

    // Adquirindo registradores envolvidos na operação
    IRegister dest = this.memory.getRegisterFromNumber(rField.rd());
    IRegister rs = this.memory.getRegisterFromNumber(rField.rs());
    IRegister rt = this.memory.getRegisterFromNumber(rField.rt());

    // Lendo valores dos registradores
    int v1 = rt.read();
    int v2 = rs.read();

    // Considerando apenas os últimos 5 bits de rs
    v2 = v2 & 0b11111;

    // Calculando resultado
    int result = v1 >>> v2;

    // Escrevendo na memória
    dest.write(result);
  }

  public void SRAV(AssemblyInstruction instruction, StringBuffer buffer) {
    // Lendo campos como sendo de uma instrução tipo R
    RField rField = instruction.fields().asRField();

    // Adquirindo registradores envolvidos na operação
    IRegister dest = this.memory.getRegisterFromNumber(rField.rd());
    IRegister rt = this.memory.getRegisterFromNumber(rField.rt());
    IRegister rs = this.memory.getRegisterFromNumber(rField.rs());

    // Lendo valores dos registradores
    int v1 = rt.read();
    int v2 = rs.read();

    // Considerando apenas os últimos 5 bits de rs
    v2 = v2 & 0b11111;

    // Calculando resultado
    int result = v1 >> v2;

    // Escrevendo na memória
    dest.write(result);
  }

  public void DIV(AssemblyInstruction instruction, StringBuffer buffer) {

    RField rField = instruction.fields().asRField();
    IRegister r1 = this.memory.getRegisterFromNumber(rField.rs());
    IRegister r2 = this.memory.getRegisterFromNumber(rField.rt());

    int v1 = r1.read();
    int v2 = r2.read();

    long c1 = v1 / v2;
    long re1 = v1 % v2;

    this.memory.getLO().write((int) c1);
    this.memory.getHI().write((int) re1);
  }

  public void MULT(AssemblyInstruction instruction, StringBuffer buffer) {

    RField rField = instruction.fields().asRField();
    IRegister r1 = this.memory.getRegisterFromNumber(rField.rs());
    IRegister r2 = this.memory.getRegisterFromNumber(rField.rt());

    int v1 = r1.read();
    int v2 = r2.read();


    long c1 = v1 * v2;

    this.memory.getLO().write((int) (c1 & 4294967295L));
    this.memory.getHI().write((int) (c1 & -4294967296L));
  }
}
