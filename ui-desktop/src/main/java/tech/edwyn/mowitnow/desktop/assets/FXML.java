package tech.edwyn.mowitnow.desktop.assets;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.Optional;

import static javafx.scene.control.Alert.AlertType.ERROR;
import static javafx.scene.control.ButtonType.CLOSE;

public enum FXML {
  MAIN_PANEL("fxml/MainPanel.fxml"),
  PROGRAM_PANEL("fxml/ProgramPanel.fxml"),
  SIMULATION_PANEL("fxml/SimulationPanel.fxml"),
  LAWN_CELL_PANEL("fxml/LawnCellPanel.fxml");
  
  private final String path;
  
  FXML(String path) {
    this.path = path;
  }
  
  public void load(Parent parent) {
    URL url = Objects.requireNonNull(getClass().getClassLoader()
                                               .getResource(path));
    FXMLLoader fxmlLoader = new FXMLLoader(url);
    fxmlLoader.setRoot(parent);
    fxmlLoader.setController(parent);
    
    try {
      fxmlLoader.load();
      
    } catch (IOException exception) {
      Alert alert = new Alert(ERROR, """
        L'application n'a pas pu charger %s.
        Erreur: %s
        %s
        L'application va quitter.
        """.formatted(
        url,
        exception.getMessage(),
        Optional.ofNullable(exception.getCause())
                .map(Throwable::getMessage)
                .map(errorMessage -> "Cause: " + errorMessage)
                .orElse("Détails absents")),
        CLOSE);
      
      alert.showAndWait();
      Platform.exit();
    }
  }
}
