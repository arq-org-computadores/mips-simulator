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
import br.ufrpe.mips.simulator.utils.disassembler.MIPSDisassembler;
import br.ufrpe.mips.simulator.utils.disassembler.MIPSDisassembler.AssemblyInstruction;
import br.ufrpe.mips.simulator.utils.instruction.MIPSInstruction;
import br.ufrpe.mips.simulator.utils.operation.ArithmeticLogic;
import br.ufrpe.mips.simulator.utils.operation.ImmediateAL;
import br.ufrpe.mips.simulator.utils.operation.JumpBranch;
import br.ufrpe.mips.simulator.utils.operation.LoadStore;
import br.ufrpe.mips.simulator.utils.operation.Syscall;
import br.ufrpe.mips.simulator.utils.register.RegisterMapper;

/**
 * Essa classe representa um processador MIPS32.
 * 
 * @version 1.0
 */
public class MIPS32Processor implements IMIPS32 {

  private static List<MIPSInstruction> branchesJump =
      List.of(MIPSInstruction.J, MIPSInstruction.JAL, MIPSInstruction.BEQ, MIPSInstruction.BGTZ,
          MIPSInstruction.BLEZ, MIPSInstruction.BLTZ, MIPSInstruction.BNE, MIPSInstruction.JR);

  // Memória principal e registradores
  private IMemoryManager memory;

  // Informações da última instrução executada
  private AssemblyInstruction lastInstruction;
  private String output;
  private String hex;

  // Auxiliares
  private long finalInstrAddr;

  // Runners de instruções
  private ArithmeticLogic al;
  private ImmediateAL ial;
  private JumpBranch jb;
  private LoadStore ls;
  private Syscall sc;

  public MIPS32Processor(IMemoryManager memory) {
    this.memory = memory;
    this.lastInstruction = new AssemblyInstruction("", null, null);
    this.output = "";
    this.hex = "";
    this.al = new ArithmeticLogic(memory);
    this.ial = new ImmediateAL(memory);
    this.jb = new JumpBranch(memory);
    this.ls = new LoadStore(memory);
    this.sc = new Syscall(memory);

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
    this.finalInstrAddr = 4194304L;
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

    Map<String, Integer> regs = registers.stream().sorted(Comparator.comparing(r -> r.number()))
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
      // Atualizando endereço da última instrução
      this.finalInstrAddr = baseAddress + offset;

      // Obtendo endereço de memória no segmento `text`
      IMemoryLocation<Integer> l =
          this.memory.getWordMemoryLocationFromAddress(this.finalInstrAddr);

      // Removendo prefixo 0x caso
      if (hex.contains("0x")) {
        hex = hex.substring(2);
      }

      // Escrevendo instrução hexadecimal como inteiro de 32-bits
      l.write(Integer.parseUnsignedInt(hex, 16));

      // Próxima palavra
      offset += 4;
    }
  }

  @Override
  public void runNexInstruction() {
    // Limpando saída da instrução anterior
    this.output = "";

    // Limpando instrução anterior
    this.hex = "";

    // Obter localização atual do programa
    long address = Integer.toUnsignedLong(this.memory.getPC().read());

    // Obter instrução atual
    int instruction = this.memory.getWordMemoryLocationFromAddress(address).read();
    this.hex = "0x%s".formatted(Integer.toHexString(instruction));
    this.lastInstruction = MIPSDisassembler.toAssembly(this.hex);
    AssemblyInstruction i = this.lastInstruction;

    // Criação do buffer de saída para os runners
    StringBuffer buffer = new StringBuffer();

    // Escolha da instrução
    switch (i.instruction()) {
      case ADD -> this.al.ADD(i, buffer);
      case ADDU -> this.al.ADDU(i, buffer);
      case SUB -> this.al.SUB(i, buffer);
      case DIVU -> this.al.DIVU(i, buffer);
      case SUBU -> this.al.SUBU(i, buffer);
      case DIV -> this.al.DIV(i, buffer);
      case MULT -> this.al.MULT(i, buffer);
      case MULTU -> this.al.MULTU(i, buffer);
      case MFLO -> this.al.MFLO(i, buffer);
      case MFHI -> this.al.MFHI(i, buffer);
      case SLL -> this.al.SLL(i, buffer);
      case SLT -> this.al.SLT(i, buffer);
      case SRL -> this.al.SRL(i, buffer);
      case SRA -> this.al.SRA(i, buffer);
      case SLLV -> this.al.SLLV(i, buffer);
      case SRLV -> this.al.SRLV(i, buffer);
      case SRAV -> this.al.SRAV(i, buffer);
      case ADDI -> this.ial.ADDI(i, buffer);
      case ORI -> this.ial.ORI(i, buffer);
      case XORI -> this.ial.XORI(i, buffer);
      case SLTI -> this.ial.SLTI(i, buffer);
      case J -> this.jb.J(i, buffer);
      case JR -> this.jb.JR(i, buffer);
      case JAL -> this.jb.JAL(i, buffer);
      case BEQ -> this.jb.BEQ(i, buffer);
      case BNE -> this.jb.BNE(i, buffer);
      case SW -> this.ls.SW(i, buffer);
      case LW -> this.ls.LW(i, buffer);
      case LB -> this.ls.LB(i, buffer);
      case SB -> this.ls.SB(i, buffer);
      case SYSCALL -> this.sc.SYSCALL(i, buffer);
      default -> System.out.println("\"%s\" não implementada".formatted(i.instruction().name()));
    }

    // Obter saídas escritas no Buffer
    this.output = buffer.toString();

    // Atualização do PC caso não seja instrução de desvio/pulo
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

    // Se o endereço de execução for <= que o endereço da última instrução,
    // ainda temos instruções para serem executadas.
    return address <= this.finalInstrAddr;
  }

  @Override
  public String toHex() {
    return this.hex;
  }

}
