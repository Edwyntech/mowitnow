package tech.edwyn.mowitnow.desktop.services;

import javafx.application.HostServices;
import javafx.application.Platform;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.function.Consumer;

import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.TRUNCATE_EXISTING;

public class JavaFXDesktopService implements DesktopService {
  private static final Logger       log = LoggerFactory.getLogger(JavaFXDesktopService.class);
  private final        HostServices hostServices;
  
  public JavaFXDesktopService(HostServices hostServices) {
    this.hostServices = hostServices;
  }
  
  @Override
  public void exit() {
    log.trace("exit");
    
    Platform.exit();
    System.exit(0);
  }
  
  @Override
  public void openFileForContent(Consumer<String> contentConsumer) throws IOException {
    log.trace("openFileForContent - contentConsumer:{}", contentConsumer);
    
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Ouverture");
    File file = fileChooser.showOpenDialog(new Stage());
    if (file != null) {
      String content = Files.readString(file.toPath());
      contentConsumer.accept(content);
    }
  }
  
  @Override
  public void saveContentAsFile(String content) throws IOException {
    log.trace("saveContentAsFile - content:{}", content);
    
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Sauvegarde");
    fileChooser.getExtensionFilters()
               .addAll(new FileChooser.ExtensionFilter("All Files", "*.*"));
    File file = fileChooser.showSaveDialog(new Stage());
    if (file != null) {
      Files.writeString(file.toPath(), content, CREATE, TRUNCATE_EXISTING);
    }
  }
  
  @Override
  public void showDocument(String documentUri) {
    log.trace("showDocument - documentUri:{}", documentUri);
    
    hostServices.showDocument(documentUri);
  }
  
}
