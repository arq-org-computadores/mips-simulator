package br.ufrpe.mips.data;

import br.ufrpe.mips.data.entity.MemoryLocationType;

/**
 * Essa interface determina todas funcionalidades
 * de um local de memória que pode armazenar 32 bits.
 * 
 * @version 1.0
 */
public interface IWordMemoryLocation {
  /**
   * Se essa localização é reservada.
   * 
   * @return bool, indica se essa localização é reservada.
   */
  boolean isReserved();

  /**
   * Qual tipo dessa localização de memória.
   * 
   * @return {@link MemoryLocationType} indicando o tipo da localização.
   */
  MemoryLocationType type();

  /**
   * Qual primeiro endereço dessa localização de memória.
   * 
   * @return inteiro, indicando o endereço dessa localização de memória.
   */
  int address();

  /**
   * Qual valor armazenado nessa localização de memória.
   * 
   * @return inteiro de 32 bits, conteúdo nessa localização.
   */
  int read();

  /**
   * Armazena um valor nessa localização de memória.
   * 
   * @param content inteiro de 32 bits.
   */
  void write(int content);
}
