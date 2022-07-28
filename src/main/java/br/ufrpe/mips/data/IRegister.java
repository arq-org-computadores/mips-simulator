package br.ufrpe.mips.data;

import br.ufrpe.mips.data.utils.RegisterType;

/**
 * Essa interface determina todas funcionalidades
 * de um registrador MIPS.
 * 
 * @version 1.0
 */
public interface IRegister {

  /**
   * Qual tipo desse registro (i.e., especial ou regular).
   * 
   * @return {@link RegisterType} indicando o tipo do registro.
   */
  RegisterType type();

  /**
   * Número desse registro.
   * 
   * @return inteiro, número desse registro.
   */
  int number();

  /**
   * Retorna o valor armazenado nesse registro.
   * 
   * @return inteiro 32-bits, valor armazenado
   */
  int read();

  /**
   * Escreve um valor para esse registro.
   * 
   * @param content inteiro 32-bits.
   */
  void write(int content);
}
