package br.ufrpe.mips.data.imp;

public class MARSMMTest {
  
  public static void main(String[] args) {
    MARSMemoryManager memory = new MARSMemoryManager();
    memory.wordMemoryLocations();
    memory.getWordMemoryLocationFromAddress(0x0004L);
    memory.wordMemoryLocations();
  }

}
