package br.ufrpe.mips.data.imp;

import java.util.List;

import br.ufrpe.mips.data.IMemoryLocation;
import br.ufrpe.mips.data.IMemoryManager;
import br.ufrpe.mips.data.IRegister;

/**
 * Classe que representa o gerenciador de memória
 * do simulador MARS MIPS.
 * 
 * A memória é divida em diferentes segmentos
 * que possuem endereçamento base e limite.
 * 
 * Cada célula de memória armazena 1 byte.
 * 
 * @version 1.0
 */
public final class MARSMemoryManager implements IMemoryManager {

  @Override
  public IMemoryLocation<Byte> getByteMemoryLocationFromAddress(int address) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public List<IMemoryLocation<Byte>> byteMemoryLocations() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public boolean isAddressWordAligned(int address) {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public IMemoryLocation<Integer> getWordMemoryLocationFromAddress(int address) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public List<IMemoryLocation<Integer>> wordMemoryLocations() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public IRegister getRegisterFromNumber(int regNumber) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public IRegister getHI() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public IRegister getLO() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public IRegister getPC() {
    // TODO Auto-generated method stub
    return null;
  }

}
