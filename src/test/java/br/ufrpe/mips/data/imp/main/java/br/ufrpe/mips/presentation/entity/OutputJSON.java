package br.ufrpe.mips.presentation.entity;

import java.util.LinkedHashMap;

import com.fasterxml.jackson.annotation.JsonProperty;

public class OutputJSON {

  @JsonProperty("hex")
  public String hexInstruction;

  @JsonProperty("text")
  public String assemblyInstruction;

  @JsonProperty("stdout")
  public String stdout;

  @JsonProperty("regs")
  public LinkedHashMap<String, Integer> registers;

  @JsonProperty("mem")
  public LinkedHashMap<String, Integer> memory;

}
