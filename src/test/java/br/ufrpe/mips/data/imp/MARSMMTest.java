package br.ufrpe.mips.data.imp;

import java.util.Comparator;
import br.ufrpe.mips.data.IMemoryManager;

public class MARSMMTest {

  public static void main(String[] args) {
    MARSMemoryManager memory = new MARSMemoryManager();

    memory.getRegisterFromNumber(1).write(8);
    memory.getRegisterFromNumber(2).write(12);
    memory.getRegisterFromNumber(29).write((int) 2147489648L);
    memory.getRegisterFromNumber(31).write(4195360);
    memory.getPC().write(4194400);
    memory.getLO().write(100);

    memory.getWordMemoryLocationFromAddress(2147479600).write(0);
    memory.getWordMemoryLocationFromAddress(2147479576).write(4194376);
    memory.getWordMemoryLocationFromAddress(2147479508).write(1);
    memory.getWordMemoryLocationFromAddress(2147479884).write(4194376);

    memory.getWordMemoryLocationFromAddress(268500992).write(192029);
    memory.getWordMemoryLocationFromAddress(268508176).write(4990970);
    memory.getWordMemoryLocationFromAddress(268502144).write(4990977);

    printRegisters(memory);
    printMemory(memory);
  }

  private static void printRegisters(IMemoryManager m) {
    System.out.println("--- REGISTRADORES ---");
    m.registers().stream().filter(r -> r.read() != 0).forEach(r -> {
      int n = r.number();
      int v = r.read();

      if (n == -1) {
        System.out.println("%s: %d".formatted(r.type(), v));
      } else {
        System.out.println("%d: %d".formatted(n, v));
      }
    });
  }

  private static void printMemory(IMemoryManager m) {
    System.out.println("--- MEMÓRIA ---");
    m.wordMemoryLocations().stream().filter(l -> l.read() != 0)
        .sorted(Comparator.comparing(l -> l.address())).forEach(l -> {
          System.out.println("%d: %d".formatted(l.address(), l.read()));
        });
  }

}
