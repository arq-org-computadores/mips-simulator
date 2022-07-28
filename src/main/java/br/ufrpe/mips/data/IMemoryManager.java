package br.ufrpe.mips.data;

import java.util.List;

/**
 * Essa interface representa toda estrutura de memória
 * necessária para um processador MIPS32. Ou seja,
 * contém registradores e memória principal.
 * 
 * É possível obter registradores e locais de memória, além
 * de realizar operações neles.
 * 
 * Existem dois tipos de local de memória para manipulação: 
 * locais que armazenam 1 byte (8 bits) e locais que armazenam
 * uma palavra MIPS (4 bytes, 32 bits).
 * 
 * Registradores podem ser de uso especial (PC, HI, LO, etc) ou geral,
 * lendo e armazenando 32-bits.
 * 
 * @see IMemoryLocation
 * @see IRegister
 * @see <a href="https://bit.ly/3PJy8xO">MIPS32 Memory Layout</a>
 * @see <a href="https://bit.ly/3vIX1lt">MIPS Memory Model</a>
 * @version 1.0
 */
public interface IMemoryManager {

  /**
   * Retorna a localização de memória associada com esse endereço
   * ou null (caso não seja encontrada uma localização de memória para
   * esse endereço).
   * 
   * @param address endereço da localização de memória.
   * @return {@link IMemoryLocation} ou null.
   */
  IMemoryLocation<Byte> getByteMemoryLocationFromAddress(int address);

  /**
   * Retorna todas as localizações de memória.
   * 
   * @return uma lista de todas localizações de memória.
   */
  List<IMemoryLocation<Byte>> byteMemoryLocations();

  /**
   * Checa se um dado endereço está alinhando com os limites das palavras.
   * 
   * @param address endereço.
   * @return true caso o endereço esteja alinhado com uma palavra, falso do
   *         contrário.
   */
  boolean isAddressWordAligned(int address);

  /**
   * Retorna a localização de memória associada com esse endereço
   * ou null (caso não seja encontrada uma localização de memória para
   * esse endereço, ou o endereço nessa alinhado com palavras).
   * 
   * @param address endereço da localização de memória (word-aligned).
   * @return {@link IMemoryLocation} ou null.
   */
  IMemoryLocation<Integer> getWordMemoryLocationFromAddress(int address);

  /**
   * Retorna todas as localizações de memória que iniciam uma palavra.
   * 
   * @return retorna uma lista de todas localizações de memória de palavra.
   */
  List<IMemoryLocation<Integer>> wordMemoryLocations();

  /**
   * Retorna o registrador com esse número ou null.
   * 
   * @param regNumber número do registrador em [0..31].
   * @return {@link IRegister} ou null.
   */
  IRegister getRegisterFromNumber(int regNumber);

  /**
   * Retorna o registrador especial HI. Escritas e leituras nesse
   * registrador geram resultados indefinidos.
   * 
   * @return {@link IRegister}.
   */
  IRegister getHI();

  /**
   * Retorna o registrador especial LO. Escritas e leituras nesse
   * registrador geram resultados indefinidos.
   * 
   * @return {@link IRegister}.
   */
  IRegister getLO();

  /**
   * Retorna o registrador especial PC. Escritas e leituras nesse
   * registrador geram resultados indefinidos.
   * 
   * @return {@link IRegister}.
   */
  IRegister getPC();

}
