package br.ufrpe.mips.simulator;

import java.util.Map;

/**
 * Essa interface todas as funcionalidade do simulador
 * MIPS32, permitindo a execução de diferentes instruções
 * e leitura dos diferentes estados da memória.
 * 
 * @version 1.0
 */
public interface IMIPS32 {

  /**
   * Limpa toda memória do simulador MIPS para os valores padrão.
   */
  void reset();

  /**
   * Carrega dados no segmento `data` da memória principal.
   * 
   * @param data um mapa de inteiro (endereço) para inteiro (valor).
   */
  void loadData(Map<Integer, Integer> data);

  /**
   * Carrega dados no segmento `data` da memória principal.
   * 
   * @param mem um mapa de inteiro (endereço) para inteiro (valor).
   */
  void loadMemory(Map<Integer, Integer> mem);

  /**
   * Carrega dados nos registros.
   * 
   * @param regs um mapa de Strings (identificador) para inteiro (valor).
   */
  void loadRegisters(Map<String, Integer> regs);

  /**
   * Executa uma instrução codificada em hexadecimal.
   * 
   * @param hexInstruction instrução em hexadecimal.
   */
  void runInstruction(String hexInstruction);

  /**
   * Retorna representação em Assembly para MIPS da última instrução.
   * 
   * @return representação em assembly.
   */
  String toAssembly();

  /**
   * Estado atual dos registradores.
   * 
   * @return Mapa de String (identificador) para inteiro (valor).
   */
  Map<String, Integer> registers();

  /**
   * Estado atual da memória principal de acordo com os limites
   * das palavras.
   * 
   * @return Mapa de inteiro (endereço) para inteiro (valor).
   */
  Map<Integer, Integer> memory(); 
}
