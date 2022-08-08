package br.ufrpe.mips.data;

import java.util.List;

/**
 * Essa interface representa toda estrutura de memória necessária para um
 * processador MIPS32. Ou
 * seja, contém registradores e memória principal.
 * 
 * É possível obter registradores e locais de memória, além de realizar
 * operações neles.
 * 
 * Existem dois tipos de local de memória para manipulação: locais que armazenam
 * 1 byte (8 bits) e
 * locais que armazenam uma palavra MIPS (4 bytes, 32 bits).
 * 
 * Registradores podem ser de uso especial (PC, HI, LO, etc) ou geral, lendo e
 * armazenando 32-bits.
 * 
 * Classes que implementam essa interface devem utilizar uma estratégia lazy
 * para alocação
 * de localizações de memória, e devem alocar automaticamente uma localização
 * quando ela for
 * solicitada.
 * 
 * @see IMemoryLocation
 * @see IRegister
 * @see <a href="https://bit.ly/3PJy8xO">MIPS32 Memory Layout</a>
 * @see <a href="https://bit.ly/3vIX1lt">MIPS Memory Model</a>
 * @version 1.0
 */
public interface IMemoryManager {

  /**
   * Retorna a localização de memória associada com esse endereço ou null (caso
   * não seja encontrada
   * uma localização de memória para esse endereço).
   * 
   * @param address endereço da localização de memória.
   * @return {@link IMemoryLocation} ou null.
   */
  IMemoryLocation<Byte> getByteMemoryLocationFromAddress(long address);

  /**
   * Retorna todas as localizações de memória que já foram alocadas.
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
  boolean isAddressWordAligned(long address);

  /**
   * Retorna a localização de memória associada com esse endereço ou null (caso
   * não seja encontrada
   * uma localização de memória para esse endereço, ou o endereço nessa alinhado
   * com palavras).
   * 
   * @param address endereço da localização de memória (word-aligned).
   * @return {@link IMemoryLocation} ou null.
   */
  IMemoryLocation<Integer> getWordMemoryLocationFromAddress(long address);

  /**
   * Retorna todas as localizações de memória que iniciam uma palavra e que já
   * foram alocadas.
   * 
   * @return retorna uma lista de todas localizações de memória de palavra.
   */
  List<IMemoryLocation<Integer>> wordMemoryLocations();

  /**
   * Retorna todos os registradores.
   * 
   * @return Lista de registradores.
   */
  List<IRegister> registers();

  /**
   * Retorna o registrador com esse número ou null.
   * 
   * @param regNumber número do registrador em [0..31].
   * @return {@link IRegister} ou null.
   */
  IRegister getRegisterFromNumber(int regNumber);

  /**
   * Retorna o registrador especial HI.
   * 
   * @return {@link IRegister}.
   */
  IRegister getHI();

  /**
   * Retorna o registrador especial LO.
   * 
   * @return {@link IRegister}.
   */
  IRegister getLO();

  /**
   * Retorna o registrador especial PC.
   * 
   * @return {@link IRegister}.
   */
  IRegister getPC();

  /**
   * Limpa a memória.
   * 
   */
  void clear();

  /**
   * Retorna endereço base do segmento de texto.
   * 
   * @return long representando o endereço base do segmento de texto.
   */
  long textBaseAddress();

  /**
   * Retorna endereço base do segmento de dados.
   * 
   * @return long representando o endereço base do segmento de dados.
   */
  long dataBaseAddress();

  /**
   * Retorna endereço base do segmento de pilha.
   * 
   * @return long representando o endereço base do segmento de pilha.
   */
  long stackBaseAddress();
}
