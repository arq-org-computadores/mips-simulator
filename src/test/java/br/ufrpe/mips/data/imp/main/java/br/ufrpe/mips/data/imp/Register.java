package br.ufrpe.mips.data.imp;

import java.util.Objects;
import br.ufrpe.mips.data.IRegister;
import br.ufrpe.mips.data.utils.RegisterType;

/**
 * Implementação básica de um IRegister.
 * 
 * @see IRegister
 * 
 * @version 1.0
 */
public class Register implements IRegister {

  private final RegisterType regType; // Tipo do registrador
  private final int regNumber; // Número do registrador
  private int value; // Valor armazenado (32 bits)

  public Register(RegisterType regType, int regNumber) {
    this.regType = Objects.requireNonNull(regType);
    this.regNumber = Objects.requireNonNull(regNumber);
    this.value = 0;
  }

  @Override
  public RegisterType type() {
    return this.regType;
  }

  @Override
  public int number() {
    return this.regNumber;
  }

  @Override
  public int read() {
    return this.value;
  }

  @Override
  public void write(int content) {
    this.value = content;
  }

}
