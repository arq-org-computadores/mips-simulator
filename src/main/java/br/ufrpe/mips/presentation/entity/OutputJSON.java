package br.ufrpe.mips.presentation.entity;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

public class OutputJSON {

  @JsonProperty("hex")
  public String hexInstruction;

  @JsonProperty("text")
  public String assemblyInstruction;

  @JsonProperty("stdout")
  public String stdout;

  @JsonProperty("regs")
  public Map<String, Integer> registers;

  @JsonProperty("mem")
  public Map<String, Integer> memory;

}
