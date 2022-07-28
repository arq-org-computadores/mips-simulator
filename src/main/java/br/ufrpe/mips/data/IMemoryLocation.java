package br.ufrpe.mips.data;

import br.ufrpe.mips.data.entity.MemoryLocationType;

/**
 * Essa interface determina todas funcionalidades
 * de um local de memória que armazena dados do tipo T.
 * 
 * @version 1.0
 */
public interface IMemoryLocation<T> {
  
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
   * @return T, conteúdo nessa localização.
   */
  T read();

  /**
   * Armazena um valor nessa localização de memória.
   * 
   * @param content dado do tipo T.
   */
  void write(T content);
}
