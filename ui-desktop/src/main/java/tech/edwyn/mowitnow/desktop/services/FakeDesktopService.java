package tech.edwyn.mowitnow.desktop.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

import static java.nio.file.Files.readString;
import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.TRUNCATE_EXISTING;

public class FakeDesktopService implements DesktopService {
  private static final Logger log = LoggerFactory.getLogger(FakeDesktopService.class);
  
  private       boolean     quit;
  private final Set<String> openedDocuments = new HashSet<>();
  private       Path        fileToOpen;
  private       Path        backupFile;
  
  @Override
  public void exit() {
    log.trace("exit");
    quit = true;
  }
  
  @Override
  public void openFileForContent(Consumer<String> contentConsumer) throws IOException {
    log.trace("openFile - contentConsumer:{}", contentConsumer);
    
    if (fileToOpen != null) {
      String configurationString = readString(fileToOpen);
      contentConsumer.accept(configurationString);
    }
  }
  
  @Override
  public void saveContentAsFile(String text) throws IOException {
    if (backupFile != null) {
      Files.writeString(backupFile, text, CREATE, TRUNCATE_EXISTING);
    }
  }
  
  @Override
  public void showDocument(String documentUri) {
    openedDocuments.add(documentUri);
  }
  
  public boolean hasQuit() {
    return quit;
  }
  
  public void setFileToOpen(Path fileToOpen) {
    this.fileToOpen = fileToOpen;
  }
  
  public void setBackupFile(Path backupFile) {
    this.backupFile = backupFile;
  }
  
  public Path getBackupFile() {
    return this.backupFile;
  }
  
  public boolean isDocumentOpened(String documentUri) {
    return openedDocuments.contains(documentUri);
  }
}
