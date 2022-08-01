package br.ufrpe.mips.simulator.imp;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import br.ufrpe.mips.data.IMemoryLocation;
import br.ufrpe.mips.data.IMemoryManager;
import br.ufrpe.mips.data.IRegister;
import br.ufrpe.mips.simulator.IMIPS32;
import br.ufrpe.mips.simulator.utils.RegisterMapper;
import br.ufrpe.mips.simulator.utils.MIPSDisassembler.AssemblyInstruction;

/**
 * Essa classe representa um processador MIPS32.
 * 
 * @version 1.0
 */
public class MIPS32Processor implements IMIPS32 {

  // Memória principal e registradores
  private IMemoryManager memory;

  // Informações da última instrução executada
  private AssemblyInstruction lastInstruction;
  private String output;
  private String hex;

  public MIPS32Processor(IMemoryManager memory) {
    this.memory = memory;
    this.lastInstruction = new AssemblyInstruction("", null, null);
    this.output = "";
    this.hex = "";
    this.reset();
  }

  @Override
  public void reset() {
    // Limpar inicialmente a memória
    this.memory.clear();

    // Inicializando PC para endereço base do segmento de texto
    this.memory.getPC().write(4194304);

    // Inicializando $gp para limite do stack
    int regNumber = RegisterMapper.regNumberFromLabel("gp");
    this.memory.getRegisterFromNumber(regNumber).write(268468224);

    // Inicializando $sp
    regNumber = RegisterMapper.regNumberFromLabel("sp");
    this.memory.getRegisterFromNumber(regNumber).write(2147479548);

    // Limpando dados da última execução
    this.lastInstruction = new AssemblyInstruction("", null, null);
    this.output = "";
    this.hex = "";
  }

  @Override
  public void loadData(Map<Long, Integer> data) {
    for (Entry<Long, Integer> e : data.entrySet()) {
      long k = e.getKey();
      int v = e.getValue();

      this.memory.getWordMemoryLocationFromAddress(k).write(v);
    }
  }

  @Override
  public void loadMemory(Map<Long, Integer> mem) {
    for (Entry<Long, Integer> e : mem.entrySet()) {
      long k = e.getKey();
      int v = e.getValue();

      this.memory.getWordMemoryLocationFromAddress(k).write(v);
    }
  }

  @Override
  public void loadRegisters(Map<String, Integer> regs) {
    for (Entry<String, Integer> e : regs.entrySet()) {
      String k = e.getKey().replace("$", "");
      int v = e.getValue();

      if (k.equals("pc")) {
        this.memory.getPC().write(v);
      } else if (k.equals("lo")) {
        this.memory.getLO().write(v);
      } else if (k.equals("hi")) {
        this.memory.getHI().write(v);
      } else {
        int regNumber = RegisterMapper.regNumberFromLabel(k);
        this.memory.getRegisterFromNumber(regNumber).write(v);
      }
    }
  }

  @Override
  public String output() {
    return this.output;
  }

  @Override
  public String toAssembly() {
    return this.lastInstruction.assembly();
  }

  @Override
  public Map<String, Integer> registers() {
    List<IRegister> registers = this.memory.registers().stream().filter(r -> r.read() != 0)
        .collect(Collectors.toList());
    IRegister pc = this.memory.getPC();
    IRegister lo = this.memory.getLO();
    IRegister hi = this.memory.getHI();

    Map<String, Integer> regs = registers.stream()
        .sorted(Comparator.comparing(r -> r.number()))
        .collect(Collectors.toMap(r -> "$%d".formatted(r.number()), r -> r.read()));
    regs = new LinkedHashMap<>(regs);

    if (pc.read() != 0) {
      regs.put("$pc", pc.read());
    }

    if (lo.read() != 0) {
      regs.put("$lo", lo.read());
    }

    if (hi.read() != 0) {
      regs.put("$hi", hi.read());
    }

    return regs;
  }

  @Override
  public Map<Long, Integer> memory() {

    return this.memory.wordMemoryLocations().stream().filter(l -> l.read() != 0)
        .sorted(Comparator.comparing(l -> l.address()))
        .collect(Collectors.toMap(l -> l.address(), l -> l.read()));
  }

  @Override
  public void loadInstructions(List<String> hexInstructions) {
    long offset = 0;
    long baseAddress = Integer.toUnsignedLong(this.memory.getPC().read());

    for (String hex : hexInstructions) {
      // Obtendo endereço de memória no segmento `text`
      IMemoryLocation<Integer> l = this.memory.getWordMemoryLocationFromAddress(baseAddress + offset);

      // Escrevendo instrução hexadecimal como inteiro de 32-bits
      l.write(Integer.decode(hex));

      // Próxima palavra
      offset += 4;
    }
  }

  @Override
  public void runNexInstruction() {
    // Obter localização atual do programa
    long address = Integer.toUnsignedLong(this.memory.getPC().read());

    // Ir para próxima instrução
    address += 4;

    // Atualizar PC
    this.memory.getPC().write((int) address);
  }

  @Override
  public boolean hasNextInstruction() {
    // Obter localização atual do programa
    long address = Integer.toUnsignedLong(this.memory.getPC().read());

    // Se existir alguma instrução para ser executada, teremos o valor diferente de
    // 0
    return this.memory.getWordMemoryLocationFromAddress(address).read() != 0;
  }

  @Override
  public String toHex() {
    return this.hex;
  }

}
