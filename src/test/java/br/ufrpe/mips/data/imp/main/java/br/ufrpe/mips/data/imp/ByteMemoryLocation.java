package br.ufrpe.mips.data.imp;

import java.util.Objects;
import br.ufrpe.mips.data.IMemoryLocation;
import br.ufrpe.mips.data.utils.MemoryLocationType;

/**
 * Essa classe representa uma localização de memória que armazena 1 byte.
 * 
 * @version 1.0
 */
public final class ByteMemoryLocation implements IMemoryLocation<Byte> {

  private final long address;
  private final MemoryLocationType memType;
  private Byte value;

  public ByteMemoryLocation(long address, MemoryLocationType memType) {
    this.address = Objects.requireNonNull(address);
    this.memType = Objects.requireNonNull(memType);
    this.value = 0;
  }

  @Override
  public boolean isReserved() {
    // Confirmar se existem outros espaços reservados
    return this.memType == MemoryLocationType.RESERVED;
  }

  @Override
  public MemoryLocationType type() {
    return this.memType;
  }

  @Override
  public long address() {
    return this.address;
  }

  @Override
  public Byte read() {
    return this.value;
  }

  @Override
  public void write(Byte content) {
    this.value = content;
  }

}
