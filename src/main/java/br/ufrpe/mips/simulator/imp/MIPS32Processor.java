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
import br.ufrpe.mips.simulator.utils.MIPSDisassembler;
import br.ufrpe.mips.simulator.utils.MIPSInstruction;
import br.ufrpe.mips.simulator.utils.RegisterMapper;
import br.ufrpe.mips.simulator.utils.InstructionFields.IField;
import br.ufrpe.mips.simulator.utils.InstructionFields.JField;
import br.ufrpe.mips.simulator.utils.InstructionFields.RField;
import br.ufrpe.mips.simulator.utils.MIPSDisassembler.AssemblyInstruction;

/**
 * Essa classe representa um processador MIPS32.
 * 
 * @version 1.0
 */
public class MIPS32Processor implements IMIPS32 {

  private static List<MIPSInstruction> branchesJump = List.of(MIPSInstruction.J,
      MIPSInstruction.JAL, MIPSInstruction.BEQ, MIPSInstruction.BGTZ,
      MIPSInstruction.BLEZ, MIPSInstruction.BLTZ, MIPSInstruction.BNE,
      MIPSInstruction.JR);

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
    List<IRegister> registers = this.memory.registers();
    IRegister pc = this.memory.getPC();
    IRegister lo = this.memory.getLO();
    IRegister hi = this.memory.getHI();

    Map<String, Integer> regs = registers.stream()
        .sorted(Comparator.comparing(r -> r.number()))
        .collect(Collectors.toMap(r -> "$%d".formatted(r.number()), r -> r.read()));
    regs = new LinkedHashMap<>(regs);

    if (pc.read() != 0) {
      regs.put("pc", pc.read());
    }

    if (lo.read() != 0) {
      regs.put("lo", lo.read());
    }

    if (hi.read() != 0) {
      regs.put("hi", hi.read());
    }

    return regs;
  }

  @Override
  public Map<Long, Integer> memory() {

    return this.memory.wordMemoryLocations().stream()
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
    // Limpando saída da instrução anterior
    this.output = "";

    // Obter localização atual do programa
    long address = Integer.toUnsignedLong(this.memory.getPC().read());

    // Obter instrução atual
    int instruction = this.memory.getWordMemoryLocationFromAddress(address).read();
    String hex = Integer.toHexString(instruction);
    this.lastInstruction = MIPSDisassembler.toAssembly("0x%s".formatted(hex));

    switch (this.lastInstruction.instruction()) {
      case ADD -> this.runADD();
      case ADDI -> this.runADDI();
      case J -> this.runJ();
      default -> System.out.println("Instrução não implementada");
    }

    // Só atualizar PC caso não seja instrução de desvio
    if (!MIPS32Processor.branchesJump.contains(this.lastInstruction.instruction())) {
      // Ir para próxima instrução
      address += 4;

      // Atualizar PC
      this.memory.getPC().write((int) address);
    }
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

  private void runADD() {
    // Lendo campos como sendo de uma instrução tipo R
    RField rField = this.lastInstruction.fields().asRField();

    // Adquirindo registradores envolvidos na operação
    IRegister dest = this.memory.getRegisterFromNumber(rField.rd());
    IRegister s1 = this.memory.getRegisterFromNumber(rField.rs());
    IRegister s2 = this.memory.getRegisterFromNumber(rField.rt());

    // Lendo valores dos registradores
    int v1 = s1.read();
    int v2 = s2.read();

    // Checar por overflow
    try {
      Math.addExact(v1, v2);
    } catch (ArithmeticException e) {
      this.output = "overflow";
    }

    // Armazenar resultado
    dest.write(v1 + v2);
  }

  private void runADDI() {
    // Lendo campos como sendo de uma instrução tipo I
    IField iField = this.lastInstruction.fields().asIField();

    // Adquirindo registradores envolvidos na operação
    IRegister dest = this.memory.getRegisterFromNumber(iField.rt());
    IRegister r1 = this.memory.getRegisterFromNumber(iField.rs());

    // Obtendo valores
    int v1 = r1.read();
    int immediate = iField.immediate();

    // Checar por overflow
    try {
      Math.addExact(v1, immediate);
    } catch (ArithmeticException e) {
      this.output = "overflow";
    }

    // Armazenar resultado
    dest.write(v1 + immediate);
  }

  private void runJ() {
    // Lendo campos como sendo de uma instrução tipo J
    JField jField = this.lastInstruction.fields().asJField();

    // Atualizando PC
    this.memory.getPC().write(jField.address());
  }

}
