package tech.edwyn.mowitnow.desktop.ui;

import javafx.beans.Observable;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.edwyn.mowitnow.domain.entities.Program;
import tech.edwyn.mowitnow.domain.services.ParsingException;
import tech.edwyn.mowitnow.domain.services.ProgramParser;

import java.net.URL;
import java.time.Duration;
import java.util.ResourceBundle;

import static tech.edwyn.mowitnow.desktop.assets.FXML.PROGRAM_PANEL;

public class ProgramPanel extends VBox implements Initializable {
  private static final Logger log = LoggerFactory.getLogger(ProgramPanel.class);
  
  private final SimpleObjectProperty<Program> program;
  private final ProgramParser                 programParser;
  
  @FXML public TextArea programTextArea;
  @FXML public Label    errorMessagesLabel;
  
  public ProgramPanel(Duration mowerLatency) {
    log.trace("ProgramPanel");
    
    programParser = new ProgramParser(mowerLatency);
    program = new SimpleObjectProperty<>();
    
    PROGRAM_PANEL.load(this);
  }
  
  @Override
  public void initialize(URL location, ResourceBundle resources) {
    log.trace("initialize - location:{}, resources:{}", location, resources);
    
    programTextArea.textProperty()
                   .addListener(this::onProgramTextChanged);
    errorMessagesLabel.visibleProperty()
                      .bind(errorMessagesLabel.textProperty()
                                              .isNotEmpty());
  }
  
  private void onProgramTextChanged(Observable observable) {
    log.trace("onProgramTextChanged - observable:{}", observable);
    
    var programText = programTextArea.getText();
    try {
      var newProgram = programParser.parse(programText);
      program.set(newProgram);
      errorMessagesLabel.setText("");
    } catch (ParsingException parsingException) {
      program.set(null);
      errorMessagesLabel.setText(parsingException.getMessage());
    }
  }
  
  public SimpleObjectProperty<Program> programProperty() {
    return program;
  }
}
