package tech.edwyn.mowitnow.desktop.services;

import java.io.IOException;
import java.util.function.Consumer;

public interface DesktopService {
  
  void exit();
  
  void openFileForContent(Consumer<String> contentConsumer) throws IOException;
  
  void saveContentAsFile(String text) throws IOException;
  
  void showDocument(String documentUri);
}
