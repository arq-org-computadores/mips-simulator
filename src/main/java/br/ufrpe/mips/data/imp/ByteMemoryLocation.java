package br.ufrpe.mips.data.imp;

import br.ufrpe.mips.data.IMemoryLocation;
import br.ufrpe.mips.data.utils.MemoryLocationType;

/**
 * Essa classe representa uma localização de memória que armazena 1 byte.
 * 
 * @version 1.0
 */
public final class ByteMemoryLocation implements IMemoryLocation<Byte> {

  @Override
  public boolean isReserved() {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public MemoryLocationType type() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public int address() {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public Byte read() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void write(Byte content) {
    // TODO Auto-generated method stub

  }

}
