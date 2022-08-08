package br.ufrpe.mips.presentation.entity;

import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonProperty;

public class InputJSON {

  @JsonProperty("config")
  public Configs configs;

  @JsonProperty("data")
  public Map<Long, String> data;

  @JsonProperty("text")
  public List<String> instructions;

  public static class Configs {
    @JsonProperty("regs")
    public Map<String, String> registers;

    @JsonProperty("mem")
    public Map<Long, String> memory;
  }

  /**
   * Método utilitário, retorna uma versão dos registradores onde todos os valores são inteiros de
   * 32-bits.
   * 
   * @return um mapa de String para inteiros.
   */
  public Map<String, Integer> registersMap() {
    return this.configs.registers.entrySet().stream().map(e -> {
      String k = e.getKey();

      // Conversão para inteiro de 64-bits
      // seguido por conversão para 32-bits
      Integer v = (int) Long.parseLong(e.getValue());

      return new AbstractMap.SimpleEntry<String, Integer>(k, v);
    }).collect(Collectors.toMap(Entry::getKey, Entry::getValue));
  }

  /**
   * Método utilitário, uma versão da memória onde todos os valores são inteiros de 32-bits.
   * 
   * @return um mapa de inteiros para inteiros.
   */
  public Map<Long, Integer> memoryMap() {
    return this.configs.memory.entrySet().stream().map(e -> {
      Long k = e.getKey();

      // Conversão para inteiro de 64-bits
      // seguido por conversão para 32-bits
      Integer v = (int) Long.parseLong(e.getValue());

      return new AbstractMap.SimpleEntry<Long, Integer>(k, v);
    }).collect(Collectors.toMap(Entry::getKey, Entry::getValue));
  }

  /**
   * Método utilitário, uma versão da memória onde todos os valores são inteiros de 32-bits.
   * 
   * @return um mapa de inteiros para inteiros.
   */
  public Map<Long, Integer> dataMap() {
    return this.data.entrySet().stream().map(e -> {
      Long k = e.getKey();

      // Conversão para inteiro de 64-bits
      // seguido por conversão para 32-bits
      Integer v = (int) Long.parseLong(e.getValue());

      return new AbstractMap.SimpleEntry<Long, Integer>(k, v);
    }).collect(Collectors.toMap(Entry::getKey, Entry::getValue));
  }
}
