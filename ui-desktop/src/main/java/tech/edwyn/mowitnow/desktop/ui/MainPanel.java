package tech.edwyn.mowitnow.desktop.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Dialog;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.edwyn.mowitnow.desktop.services.DesktopService;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.ResourceBundle;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static javafx.scene.control.ButtonType.CLOSE;
import static tech.edwyn.mowitnow.desktop.DesktopApplication.ABOUT_DIALOG_TITLE;
import static tech.edwyn.mowitnow.desktop.DesktopApplication.PDF_USER_GUIDE_JAR_PATH;
import static tech.edwyn.mowitnow.desktop.assets.FXML.MAIN_PANEL;
import static tech.edwyn.mowitnow.desktop.assets.Images.LOGO;

public class MainPanel extends VBox implements Initializable {
  private static final Logger log = LoggerFactory.getLogger(MainPanel.class);
  
  private final ProgramPanel    programPanel;
  private final SimulationPanel simulationPanel;
  
  private String         userGuidePath;
  private DesktopService desktopService;
  
  @FXML public HBox     contentPanel;
  @FXML public MenuBar  menuBar;
  @FXML public Menu     fileMenu;
  @FXML public MenuItem openMenuItem;
  @FXML public MenuItem saveAsMenuItem;
  @FXML public MenuItem quitMenuItem;
  @FXML public Menu     simulationMenu;
  @FXML public MenuItem startMenuItem;
  @FXML public MenuItem stopMenuItem;
  @FXML public Menu     helpMenu;
  @FXML public MenuItem documentationMenuItem;
  @FXML public MenuItem aboutMenuItem;
  
  public MainPanel(DesktopService desktopService, String userGuidePath, Duration mowerLatency) {
    log.trace("MainPanel");
    
    this.userGuidePath = userGuidePath;
    
    this.desktopService = desktopService;
    this.programPanel = new ProgramPanel(mowerLatency);
    this.simulationPanel = new SimulationPanel();
    
    MAIN_PANEL.load(this);
  }
  
  public void setDesktopService(DesktopService desktopService) {
    this.desktopService = desktopService;
  }
  
  @Override
  public void initialize(URL location, ResourceBundle resources) {
    log.trace("initialize - location:{}, resources:{}", location, resources);
    
    contentPanel.getChildren()
                .addAll(programPanel, simulationPanel);
    
    startMenuItem.disableProperty()
                 .bind(programPanel.programProperty()
                                   .isNull()
                                   .or(simulationPanel.runningProperty()
                                                      .isNotNull()));
    
    stopMenuItem.disableProperty()
                .bind(simulationPanel.runningProperty()
                                     .isNull());
    
    saveAsMenuItem.disableProperty()
                  .bind(programPanel.programProperty()
                                    .isNull()
                                    .or(simulationPanel.runningProperty()
                                                       .isNotNull()));
    
    programPanel.disableProperty()
                .bind(simulationPanel.runningProperty()
                                     .isNotNull());
    
    programPanel.programProperty()
                .addListener(simulationPanel);
  }
  
  @FXML
  public void onOpenAction(ActionEvent event) throws IOException {
    log.trace("onOpenAction - event:{}", event);
    
    desktopService.openFileForContent(programPanel.programTextArea::setText);
  }
  
  @FXML
  public void onSaveAsAction(ActionEvent event) throws IOException {
    log.trace("onSaveAsAction - event:{}", event);
    
    desktopService.saveContentAsFile(programPanel.programTextArea.getText());
  }
  
  @FXML
  public void onQuitAction(ActionEvent event) {
    log.trace("onQuitAction - event:{}", event);
    
    desktopService.exit();
  }
  
  @FXML
  public void onStartAction(ActionEvent event) {
    log.trace("onStartAction - event:{}", event);
    
    simulationPanel.start();
  }
  
  @FXML
  public void onStopAction(ActionEvent event) {
    log.trace("onStopAction - event:{}", event);
    
    simulationPanel.stop();
  }
  
  @FXML
  public void onDocumentationAction(ActionEvent event) {
    log.trace("onDocumentationAction - event:{}", event);
    
    if (userGuidePath != null) {
      log.debug("onDocumentationAction - using userGuidePath:{}", userGuidePath);
      desktopService.showDocument(Paths.get(userGuidePath)
                                       .toUri()
                                       .toString());
    } else try (var is = getClass().getClassLoader()
                                   .getResourceAsStream(PDF_USER_GUIDE_JAR_PATH)) {
      log.debug("onDocumentationAction - using JAR path:{}", PDF_USER_GUIDE_JAR_PATH);
      if (is != null) {
        var tempFile = Files.createTempFile("guide", ".pdf");
        Files.copy(is, tempFile, REPLACE_EXISTING);
        userGuidePath = tempFile.toAbsolutePath()
                                .toString();
        log.debug("onDocumentationAction - extracted userGuidePath:{}", userGuidePath);
        desktopService.showDocument(tempFile.toUri()
                                            .toString());
      }
    } catch (IOException e) {
      log.error("Could not read user guide", e);
    }
  }
  
  @FXML
  public void onAboutAction(ActionEvent event) {
    log.trace("onAboutAction - event:{}", event);
    
    Dialog<Void> dialog = new Dialog<>();
    dialog.setContentText("MowItNow, par Edwyn Tech.");
    dialog.setTitle(ABOUT_DIALOG_TITLE);
    dialog.getDialogPane()
          .getButtonTypes()
          .add(CLOSE);
    dialog.setGraphic(new ImageView(LOGO.image()));
    dialog.showAndWait();
  }
  
}
