package br.ufrpe.mips.data.imp;

import java.nio.ByteBuffer;
import java.util.Objects;
import br.ufrpe.mips.data.IMemoryLocation;
import br.ufrpe.mips.data.utils.MemoryLocationType;

/**
 * Essa classe representa uma localização de memória com 4 bytes.
 * 
 * @version 1.0
 */
public final class WordMemoryLocation implements IMemoryLocation<Integer> {

  private final ByteMemoryLocation cell0;
  private final ByteMemoryLocation cell1;
  private final ByteMemoryLocation cell2;
  private final ByteMemoryLocation cell3;

  public WordMemoryLocation(ByteMemoryLocation c0, ByteMemoryLocation c1, ByteMemoryLocation c2,
      ByteMemoryLocation c3) {
    this.cell0 = Objects.requireNonNull(c0);
    this.cell1 = Objects.requireNonNull(c1);
    this.cell2 = Objects.requireNonNull(c2);
    this.cell3 = Objects.requireNonNull(c3);
  }

  @Override
  public boolean isReserved() {
    return this.cell0.isReserved();
  }

  @Override
  public MemoryLocationType type() {
    return this.cell0.type();
  }

  @Override
  public int address() {
    return this.cell0.address();
  }

  @Override
  public Integer read() {
    return ByteBuffer.wrap(this.asByteArray()).getInt();
  }

  @Override
  public void write(Integer content) {
    ByteBuffer buffer = ByteBuffer.allocate(4);
    buffer.putInt(content);
    this.updateFromByteArray(buffer.array());
  }

  private byte[] asByteArray() {
    return new byte[] {this.cell0.read(), this.cell1.read(), this.cell2.read(), this.cell3.read()};
  }

  private void updateFromByteArray(byte[] arr) {
    this.cell0.write(arr[0]);
    this.cell1.write(arr[1]);
    this.cell2.write(arr[2]);
    this.cell3.write(arr[3]);
  }
}
