package br.ufrpe.mips.data.imp;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;
import br.ufrpe.mips.data.IMemoryLocation;
import br.ufrpe.mips.data.IMemoryManager;
import br.ufrpe.mips.data.IRegister;
import br.ufrpe.mips.data.utils.MemoryLocationType;
import br.ufrpe.mips.data.utils.RegisterType;

/**
 * Classe que representa o gerenciador de memória do simulador MARS MIPS.
 * 
 * A memória é divida em diferentes segmentos que possuem endereçamento base e
 * limite.
 * 
 * Cada célula de memória armazena 1 byte.
 * 
 * @version 1.0
 */
public final class MARSMemoryManager implements IMemoryManager {

  private final LinkedHashMap<Long, ByteMemoryLocation> memory;
  private final LinkedHashMap<Integer, Register> registers;
  private final Register lo, hi, pc;

  private static class MARSMemoryLayout {
    private MARSMemoryLayout() {

    }

    static long textBegin = 0x00400000L;
    private static long textLimit = 0x0ffffffcL;

    static long dataBegin = 0x10000000L;
    static long dataLimit = 0x7fffffffL;

    static long stackBegin = 0x10040000L;
    static long stackLimit = 0x7ffffffcL;

    public static MemoryLocationType typeFromAddress(long address) {
      if (address >= textBegin && address <= textLimit) {
        return MemoryLocationType.TEXT_SEGMENT;
      } else if (address >= dataBegin && address <= dataLimit) {
        return MemoryLocationType.STATIC_DATA;
      } else if (address >= stackBegin && address <= stackLimit) {
        return MemoryLocationType.STACK_SEGMENT;
      }

      return MemoryLocationType.RESERVED;
    }

  }

  public MARSMemoryManager() {
    this.memory = new LinkedHashMap<>();
    this.registers = new LinkedHashMap<>();

    for (int i = 0; i < 32; i++) {
      this.registers.put(i, new Register(RegisterType.REGULAR, i));
    }

    this.lo = new Register(RegisterType.LO, -1);
    this.hi = new Register(RegisterType.HI, -1);
    this.pc = new Register(RegisterType.PC, -1);
  }

  @Override
  public IMemoryLocation<Byte> getByteMemoryLocationFromAddress(long address) {
    if (MARSMemoryManager.isReserved(address)) {
      return null;
    }

    if (!this.memory.containsKey(address)) {
      MemoryLocationType t = MARSMemoryLayout.typeFromAddress(address);
      ByteMemoryLocation l = new ByteMemoryLocation(address, t);

      this.memory.put(address, l);
    }

    return this.memory.get(address);
  }

  @Override
  public List<IMemoryLocation<Byte>> byteMemoryLocations() {
    List<IMemoryLocation<Byte>> l = new ArrayList<>();

    l.addAll(this.memory.values());

    return l;
  }

  @Override
  public boolean isAddressWordAligned(long address) {
    // Todos endereços de palavra são múltiplos de 4
    return address % 4 == 0;
  }

  @Override
  public IMemoryLocation<Integer> getWordMemoryLocationFromAddress(long address) {
    if (!this.isAddressWordAligned(address) || MARSMemoryManager.isReserved(address)) {
      // Caso não seja um endereço de palavra ou seja reservado, retorna
      return null;
    }

    // Encapsulando 4 células de 8 bits nessa localização de memória
    IMemoryLocation<Byte> c0 = this.getByteMemoryLocationFromAddress(address);
    IMemoryLocation<Byte> c1 = this.getByteMemoryLocationFromAddress(address + 1);
    IMemoryLocation<Byte> c2 = this.getByteMemoryLocationFromAddress(address + 2);
    IMemoryLocation<Byte> c3 = this.getByteMemoryLocationFromAddress(address + 3);

    return new WordMemoryLocation((ByteMemoryLocation) c0, (ByteMemoryLocation) c1,
        (ByteMemoryLocation) c2, (ByteMemoryLocation) c3);
  }

  @Override
  public List<IMemoryLocation<Integer>> wordMemoryLocations() {
    return this.byteMemoryLocations().stream().filter(l -> l.address() % 4 == 0)
        .map(l -> new WordMemoryLocation((ByteMemoryLocation) l, this.memory.get(l.address() + 1),
            this.memory.get(l.address() + 2), this.memory.get(l.address() + 3)))
        .collect(Collectors.toList());
  }

  @Override
  public IRegister getRegisterFromNumber(int regNumber) {
    if (!this.registers.containsKey(regNumber)) {
      return null;
    }

    return this.registers.get(regNumber);
  }

  @Override
  public IRegister getHI() {
    return this.hi;
  }

  @Override
  public IRegister getLO() {
    return this.lo;
  }

  @Override
  public IRegister getPC() {
    return this.pc;
  }

  private static boolean isReserved(long address) {
    return MARSMemoryLayout.typeFromAddress(address) == MemoryLocationType.RESERVED;
  }

  @Override
  public List<IRegister> registers() {
    List<IRegister> regs = new ArrayList<>();

    regs.addAll(this.registers.values());

    return regs;
  }

  @Override
  public void clear() {
    // Limpar registradores comuns
    for (Register reg : this.registers.values()) {
      reg.write(0);
    }

    // Limpar registradores especiais
    this.hi.write(0);
    this.lo.write(0);
    this.pc.write(0);

    // Limpar memória principal
    this.memory.clear();
  }

  @Override
  public long textBaseAddress() {
    // Endereço base de texto/instruções.
    return MARSMemoryLayout.textBegin;
  }

  @Override
  public long dataBaseAddress() {
    // Endereço base de dados
    return MARSMemoryLayout.dataBegin;
  }

  @Override
  public long stackBaseAddress() {
    // O stack cresce de "cima" para "baixo".
    return MARSMemoryLayout.stackLimit;
  }

}
