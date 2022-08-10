package br.ufrpe.mips.presentation.cli;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.io.FileUtils;

import com.fasterxml.jackson.core.PrettyPrinter;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import br.ufrpe.mips.data.imp.MARSMemoryManager;
import br.ufrpe.mips.presentation.entity.InputJSON;
import br.ufrpe.mips.presentation.entity.OutputJSON;
import br.ufrpe.mips.simulator.IMIPS32;
import br.ufrpe.mips.simulator.imp.MIPS32Processor;

public final class Main {
  private static Path inputPath = Path.of("input");
  private static Path outputPath = Path.of("output");
  private static ObjectMapper mapper = new ObjectMapper();
  private static PrettyPrinter printer = new DefaultPrettyPrinter().withoutSpacesInObjectEntries();
  private static IMIPS32 simulator = new MIPS32Processor(new MARSMemoryManager());

  private Main() {
    // Essa classe não pode ser instanciada.
  }

  /**
   * Atualiza o simulador utilizado.
   * 
   * @param simulator instância de um simulador MIPS.
   */
  public static void setSimulator(IMIPS32 simulator) {
    Main.simulator = simulator;
  }

  public static void main(String[] args) throws IOException {
    // Executando todos os arquivos no diretório de entrada
    Files.walk(inputPath).forEach(Main::run);
  }

  /**
   * Recebe um caminho para um arquivo JSON e executa o algoritmo representado por ele no simulador.
   * 
   * Salva o resultado da execução em um arquivo JSON de saída.
   * 
   * @param p caminho para o arquivo JSON.
   */
  private static void run(Path p) {
    if (!Files.exists(p) || Files.isDirectory(p) || !p.toString().contains(".json")) {
      // Pular para o próximo arquivo caso não seja um json;
      return;
    }

    InputJSON input = null;

    try {
      input = Main.mapper.readValue(p.toFile(), InputJSON.class);
    } catch (IOException e) {
      // Não foi possível ler a entrada, pular arquivo;
      return;
    }

    System.out.println("\n--- Executando %s ---".formatted(p));

    // --- CARREGAMENTO DE DADOS NO SIMULADOR ---
    Main.simulator.reset(); // Limpar a memória do simulador
    Main.loadData(input);

    // --- EXECUÇÃO DO PROGRAMA ---
    while (Main.simulator.hasNextInstruction()) {
      Main.simulator.runNexInstruction();
      // Possivelmente adicionar um log aqui
    }

    // --- FINALIZAÇÃO E ESCRITA DOS RESULTADOS ---
    try {
      String result = Main.mapper.writer(Main.printer).writeValueAsString(Main.getCurrentResults());

      String fname = Main.outputFileName(p.getFileName().toString());
      Path out = Path.of(Main.outputPath.toString(), fname);

      FileUtils.writeStringToFile(out.toFile(), result, "UTF-8");
    } catch (IOException e) {
      // Silent catch.
    }
  }

  /**
   * Método utilitário, carrega todos os dados necessários no simulador na memória: registradores e
   * memória principal.
   * 
   * @param input arquivo de entrada contendo todos os dados.
   */
  private static void loadData(InputJSON input) {
    // Instruções
    Main.simulator.loadInstructions(input.instructions);

    // Registradores
    Main.simulator.loadRegisters(input.registersMap());

    // Memória principal
    Main.simulator.loadMemory(input.memoryMap());

    // Segmento de dados
    Main.simulator.loadData(input.dataMap());
  }

  /**
   * Método utilitário, converte o estado atual do simulador para um {@link OutputJSON}.
   * 
   * @return {@link OutputJSON} representando o estado atual do simulador.
   */
  private static OutputJSON getCurrentResults() {
    Map<String, Integer> registers = Main.simulator.registers();
    Map<Long, Integer> memory = Main.simulator.memory();
    String hex = Main.simulator.toHex();
    String assembly = Main.simulator.toAssembly();
    String stdout = Main.simulator.output();

    OutputJSON outputJSON = new OutputJSON();

    // Adicionando informações gerais
    outputJSON.assemblyInstruction = assembly;
    outputJSON.hexInstruction = hex;
    outputJSON.stdout = stdout;

    // Adicionando todos registradores que possuem valor != 0
    LinkedHashMap<String, Integer> regsMap = new LinkedHashMap<>();
    registers.entrySet().stream().sorted(Comparator.comparing(e -> {
      String k = e.getKey();
      if (k.equals("pc")) {
        // Depois de todos registradores
        return 900;
      } else if (k.equals("hi")) {
        // Depois de pc
        return 901;
      } else if (k.equals("lo")) {
        // Depois de hi
        return 902;
      } else {
        // De acordo com número do registrador
        return Integer.parseInt(k.replace("$", ""));
      }
    })).filter(e -> e.getValue() != 0).forEachOrdered(e -> regsMap.put(e.getKey(), e.getValue()));
    outputJSON.registers = regsMap;

    // Adicionando todas células de memória que possuem valor != 0
    LinkedHashMap<String, Integer> memMap = new LinkedHashMap<>();
    memory.entrySet().stream().sorted(Comparator.comparing(Entry::getKey))
        .filter(e -> e.getValue() != 0)
        .forEachOrdered(e -> memMap.put(e.getKey().toString(), e.getValue()));
    outputJSON.memory = memMap;

    return outputJSON;
  }

  /**
   * Retorna o nome do arquivo seguindo a especificação dos requisitos.
   * 
   * @param algorithm nome do algoritmo.
   * @return String formatada como "<Grupo>.<Alg>.output.json".
   */
  private static String outputFileName(String algorithm) {
    algorithm = algorithm.replaceAll(".json", "");
    return String.format("%s.%s.output.json", "Grupo", algorithm);
  }
}
