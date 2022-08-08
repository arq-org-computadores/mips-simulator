package br.ufrpe.mips.data.utils;

public enum MemoryLocationType {
  RESERVED, MAPPED_IO, KERNEL_DATA, KERNEL_TEXT,
  STACK_SEGMENT, DYNAMIC_DATA, STATIC_DATA,
  TEXT_SEGMENT;
}
