package br.ufrpe.mips.presentation.cli;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import br.ufrpe.mips.simulator.IMIPS32;

public class CLITest {

  private static class MockMIPS implements IMIPS32 {

    private int i = 0;

    @Override
    public void reset() {
      System.out.println("Resetando memória...");
    }

    @Override
    public void loadInstructions(List<String> hexInstructions) {
      System.out.println("Carregando instruções...");
      System.out.println(String.join("\n", hexInstructions));
    }

    @Override
    public void loadData(Map<Long, Integer> data) {
      System.out.println("Carregando dados...");
      System.out.println(data);
    }

    @Override
    public void loadMemory(Map<Long, Integer> mem) {
      System.out.println("Carregando memória principal...");
      System.out.println(mem);
    }

    @Override
    public void loadRegisters(Map<String, Integer> regs) {
      System.out.println("Carregando registradores...");
      System.out.println(regs);
    }

    @Override
    public boolean hasNextInstruction() {
      return ++i < 5;
    }

    @Override
    public void runNexInstruction() {
      System.out.println("Executando próxima instrução...");
    }

    @Override
    public String output() {
      return "overflow";
    }

    @Override
    public String toAssembly() {
      return "add $0 $1 $2";
    }

    @Override
    public String toHex() {
      return "0x59838059";
    }

    @Override
    public Map<String, Integer> registers() {
      return Map.of("$0", 0, "$12", 50);
    }

    @Override
    public Map<Long, Integer> memory() {
      return Map.of(40921L, 100, 49127L, 0);
    }

  }

  public static void main(String[] args) throws IOException {
    Main.setSimulator(new MockMIPS());
    Main.main(args);
  }

}
