package br.ufrpe.mips.presentation.gui;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.apache.commons.io.FileUtils;

public class PythonInit {

  private Path venvDirectory;
  private Path mainPath;
  private Path pythonPath;

  public PythonInit() {
    this.venvDirectory = Path.of(FileUtils.getTempDirectoryPath());

    try {
      this.mainPath = Path.of(PythonInit.class.getResource("/main.py").toURI());
    } catch (URISyntaxException e) {
      // Silent catch.
    }
  }

  public Process runGUI() throws IOException {
    List<String> command = List.of("python", this.mainPath.toString());
    ProcessBuilder builder = new ProcessBuilder(command);
    return builder.start();
  }

  public void initialize() {
    this.createVirtualEnvironment();
    this.initializeVirtualEnvironment();
  }

  private void createVirtualEnvironment() {
    ProcessBuilder builder = new ProcessBuilder();
    builder.command(List.of("python", "-m", "venv", this.venvDirectory.toString()));


    try {
      Process p = builder.start();
      p.waitFor();
    } catch (InterruptedException | IOException e) {
      // Silent catch.
    }

    try {
      if (FileUtils.isEmptyDirectory(this.venvDirectory.toFile())) {
        String msg = "Virtual environment is empty in %s.".formatted(this.venvDirectory);
        throw new RuntimeException(msg);
      }
    } catch (IOException e) {
      String msg = "Couldn't find venv directory.";
      throw new RuntimeException(msg);
    }
  }

  private void initializeVirtualEnvironment() {
    // POSIX venv scripts path
    Path scriptsDir = Path.of(this.venvDirectory.toString(), "bin");

    if (!Files.exists(scriptsDir)) {
      // Windows venv scripts path
      scriptsDir = Path.of(this.venvDirectory.toString(), "Scripts");
    }

    this.pythonPath = Path.of(scriptsDir.toString(), "python");
    if (!Files.exists(this.pythonPath)) {
      // Windows venv scripts path
      this.pythonPath = Path.of(scriptsDir.toString(), "python.exe");
    }

    String python = this.pythonPath.toString();

    ProcessBuilder builder = new ProcessBuilder();
    builder.command(List.of(python, "-m", "pip", "install", "dearpygui==1.6.2"));
    try {
      Process p = builder.start();
      p.waitFor();
    } catch (InterruptedException | IOException e) {
      String msg = "Couldn't install packages with pip.\n%s".formatted(e.getMessage());
      throw new RuntimeException(msg);
    }
  }

  private void addCleanUpHook() {
    Thread deleteRecursively = new Thread(() -> {
      for (int i = 0; i < 5; i++)
        if (FileUtils.deleteQuietly(this.venvDirectory.toFile())) {
          break;
        }
    });

    Runtime.getRuntime().addShutdownHook(deleteRecursively);
  }
}
