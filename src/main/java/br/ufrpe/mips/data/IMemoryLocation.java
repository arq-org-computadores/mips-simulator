package br.ufrpe.mips.data;

import br.ufrpe.mips.data.entity.MemoryLocationType;

/**
 * Essa interface determina todas funcionalidades
 * de um local de memória que pode armazenar 8 bits.
 * 
 * @version 1.0
 */
public interface IMemoryLocation {
  
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
   * Qual endereço dessa localização de memória.
   * 
   * @return inteiro, indicando o endereço dessa localização de memória.
   */
  int address();

  /**
   * Qual valor armazenado nessa localização de memória.
   * 
   * @return byte, conteúdo nessa localização.
   */
  byte read();

  /**
   * Armazena um valor nessa localização de memória.
   * 
   * @param content byte.
   */
  void write(byte content);
}
