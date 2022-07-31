# Projeto: Simulador MIPS32

MIPS32 é um conjunto de instruções 32 bits. Esse projeto permite executar diferentes instruções dessa arquitetura através de uma simulação de um microprocessador MIPS.

## Arquitetura do Sistema

![Arquitetura em camadas](docs/imgs/architecture.png)

O Simulador é organizado em 3 camadas distintas:

- Camada de **Apresentação**, responsável pela exibição dos dados e comunicação com o usuário (leitura de entradas, produção de saídas, etc);
- Camada de **Simulação**, que implementa as funcionalidades de um processador MIPS32 (instruções, controle de memória, execução, etc);
- Camada de **Dados**, implementa as estruturas de memórias utilizadas pelo processador (registradores, memória principal);

A comunicação entre as camadas ocorre através de *interfaces* que definem quais funcionalidades são oferecidas por cada uma das camadas.

```mermaid
classDiagram
direction BT
class IMIPS32 {
<<Interface>>
  + loadInstructions(List~String~) void
  + output() String
  + registers() Map~String, Integer~
  + loadMemory(Map~Integer, Integer~) void
  + memory() Map~Integer, Integer~
  + runNexInstruction() void
  + reset() void
  + loadData(Map~Integer, Integer~) void
  + loadRegisters(Map~String, Integer~) void
  + toAssembly() String
}
class IMemoryLocation~T~ {
<<Interface>>
  + read() T
  + type() MemoryLocationType
  + address() int
  + write(T) void
   boolean reserved
}
class IMemoryManager {
<<Interface>>
  + byteMemoryLocations() List~IMemoryLocation~Byte~~
  + getRegisterFromNumber(int) IRegister
  + getByteMemoryLocationFromAddress(int) IMemoryLocation~Byte~
  + isAddressWordAligned(int) boolean
  + wordMemoryLocations() List~IMemoryLocation~Integer~~
  + getWordMemoryLocationFromAddress(int) IMemoryLocation~Integer~
   IRegister HI
   IRegister LO
   IRegister PC
}
class IRegister {
<<Interface>>
  + number() int
  + write(int) void
  + type() RegisterType
  + read() int
}
```

- A interface `IMemoryManager`, e as interfaces auxiliares `IRegister` e `IMemoryLocation`, define um controlador de memória. Esse controlador (classe) é responsável pelo controle dos diferentes registradores e espaços de memória existentes.
- A interface `IMIPS32` define todas as funcionalidades disponibilizadas por um processador MIPS de 32 bits.

# Equipe

- Lucas Henrique [![Github Badge](https://img.shields.io/badge/-hipera09-100000?style=flat-square&logo=Github&logoColor=white)](https://github.com/hipera09)
- Moésio Filho [![Github Badge](https://img.shields.io/badge/-moesio--f-100000?style=flat-square&logo=Github&logoColor=white)](https://github.com/moesio-f)
- Vitor Alencar [![Github Badge](https://img.shields.io/badge/-vitohrs-100000?style=flat-square&logo=Github&logoColor=white)](https://github.com/vitohrs)

---
*Os requisitos do projeto podem ser encontrados em [Projeto Simulador MIPS](https://view.genial.ly/62cf619eccc76b0014a9441f/interactive-content-aoc20212projeto-mips).*