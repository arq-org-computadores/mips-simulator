package br.ufrpe.mips.simulator.imp;

import java.util.List;
import java.util.Map;

import br.ufrpe.mips.data.IMemoryManager;
import br.ufrpe.mips.simulator.IMIPS32;

/**
 * Essa classe representa um processador MIPS32.
 * 
 * @version 1.0
 */
public class MIPS32Processor implements IMIPS32 {

  // Memória principal e registradores
  private IMemoryManager memory;

  @Override
  public void reset() {
    // TODO Auto-generated method stub

  }

  @Override
  public void loadData(Map<Long, Integer> data) {
    // TODO Auto-generated method stub

  }

  @Override
  public void loadMemory(Map<Long, Integer> mem) {
    // TODO Auto-generated method stub

  }

  @Override
  public void loadRegisters(Map<String, Integer> regs) {
    // TODO Auto-generated method stub

  }

  @Override
  public String output() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public String toAssembly() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Map<String, Integer> registers() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Map<Long, Integer> memory() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void loadInstructions(List<String> hexInstructions) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void runNexInstruction() {
    // TODO Auto-generated method stub
    
  }

  @Override
  public boolean hasNextInstruction() {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public String toHex() {
    // TODO Auto-generated method stub
    return null;
  }

}
