package br.ufrpe.mips.simulator.imp;

import java.io.IOException;

import br.ufrpe.mips.data.imp.MARSMemoryManager;
import br.ufrpe.mips.presentation.cli.Main;

public class MIPSProcessortTest {
    public static void main(String[] args) throws IOException {
        Main.setSimulator(new MIPS32Processor(new MARSMemoryManager()));
        Main.main(args);
    }
}
