package br.ufrpe.mips.data.imp;

import br.ufrpe.mips.data.IMemoryLocation;
import br.ufrpe.mips.data.utils.MemoryLocationType;

/**
 * Essa classe representa uma localização de memória com 4 bytes.
 * 
 * @version 1.0
 */
public final class WordMemoryLocation implements IMemoryLocation<Integer> {

  // Internamente, essa classe manipula 4 localizações de memória
  // de 1 byte.
  private ByteMemoryLocation cell0;
  private ByteMemoryLocation cell1;
  private ByteMemoryLocation cell2;
  private ByteMemoryLocation cell3;

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
  public Integer read() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void write(Integer content) {
    // TODO Auto-generated method stub

  }

}
