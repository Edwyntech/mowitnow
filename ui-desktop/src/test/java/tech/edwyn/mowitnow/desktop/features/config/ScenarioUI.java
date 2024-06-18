package tech.edwyn.mowitnow.desktop.features.config;

import com.sun.javafx.scene.control.ContextMenuContent;
import com.sun.javafx.scene.control.MenuBarButton;
import io.cucumber.java.Scenario;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.stage.Window;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testfx.api.FxRobot;
import tech.edwyn.mowitnow.desktop.assets.Images;
import tech.edwyn.mowitnow.desktop.features.fixtures.ActionDescription;
import tech.edwyn.mowitnow.desktop.features.fixtures.ActionPath;
import tech.edwyn.mowitnow.desktop.services.FakeDesktopService;
import tech.edwyn.mowitnow.desktop.ui.MainPanel;
import tech.edwyn.mowitnow.desktop.ui.ProgramPanel;
import tech.edwyn.mowitnow.desktop.ui.SimulationPanel;
import tech.edwyn.mowitnow.domain.entities.Coordinates;
import tech.edwyn.mowitnow.domain.entities.Position;
import tech.edwyn.mowitnow.domain.entities.Size;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import static org.testfx.util.DebugUtils.*;
import static tech.edwyn.mowitnow.desktop.DesktopApplication.ABOUT_DIALOG_TITLE;
import static tech.edwyn.mowitnow.desktop.features.config.CucumberConfiguration.USER_GUIDE_PATH;

public class ScenarioUI {
  private static final Logger log                = LoggerFactory.getLogger(ScenarioUI.class);
  private static final Path   SCREENSHOTS_FOLDER = Paths.get("target/cukedoctor/screenshots");
  
  private final FakeDesktopService desktopService;
  private       FxRobot            fxRobot;
  private       Scenario           scenario;
  
  public ScenarioUI(FakeDesktopService desktopService) {
    this.desktopService = desktopService;
  }
  
  public void setup(Scenario scenario, FxRobot fxRobot) {
    this.scenario = scenario;
    this.fxRobot = fxRobot;
    mainPanel().setDesktopService(desktopService);
  }
  
  public Window aboutDialog() {
    return fxRobot.window(ABOUT_DIALOG_TITLE);
  }
  
  public MainPanel mainPanel() {
    return fxRobot.lookup("#mainPanel")
                  .queryAs(MainPanel.class);
  }
  
  public ProgramPanel programPanel() {
    return fxRobot.lookup("#programPanel")
                  .queryAs(ProgramPanel.class);
  }
  
  public SimulationPanel simulationPanel() {
    return fxRobot.lookup("#simulationPanel")
                  .queryAs(SimulationPanel.class);
  }
  
  public GridPane lawnGridPanel() {
    return fxRobot.lookup("#lawnGridPanel")
                  .queryAs(GridPane.class);
  }
  
  public MenuBar menuBar() {
    return fxRobot.lookup("#menuBar")
                  .queryAs(MenuBar.class);
  }
  
  public MenuBarButton fileMenu() {
    return fxRobot.lookup("#fileMenu")
                  .queryAs(MenuBarButton.class);
  }
  
  public ContextMenuContent.MenuItemContainer openMenuItemContainer() {
    return fxRobot.lookup("#openMenuItem")
                  .queryAs(ContextMenuContent.MenuItemContainer.class);
  }
  
  public ContextMenuContent.MenuItemContainer saveAsMenuItemContainer() {
    return fxRobot.lookup("#saveAsMenuItem")
                  .queryAs(ContextMenuContent.MenuItemContainer.class);
  }
  
  public ContextMenuContent.MenuItemContainer quitMenuItemContainer() {
    return fxRobot.lookup("#quitMenuItem")
                  .queryAs(ContextMenuContent.MenuItemContainer.class);
  }
  
  public TextArea programTextArea() {
    return fxRobot.lookup("#programTextArea")
                  .queryAs(TextArea.class);
  }
  
  public Label errorMessagesLabel() {
    return fxRobot.lookup("#errorMessagesLabel")
                  .queryAs(Label.class);
  }
  
  public void quitApplication() {
    fxRobot.clickOn(fileMenu())
           .clickOn(quitMenuItemContainer());
  }
  
  public Path takeScreenshot(String screenshotTitle) throws IOException {
    log.trace("takeScreenshot - screenshotTitle:{}", screenshotTitle);
    
    Files.createDirectories(SCREENSHOTS_FOLDER);
    Path screenshotPath = SCREENSHOTS_FOLDER.resolve(scenario.getId() + ".png");
    var  indent         = " ".repeat(3);
    var message = compose(
      saveWindow(() -> screenshotPath, indent),
      showKeysPressedAtTestFailure(fxRobot, indent),
      showMouseButtonsPressedAtTestFailure(fxRobot, indent),
      showFiredEvents()
    ).apply(new StringBuilder());
    log.debug("takeScreenshot - message:{}", message);
    scenario.attach("image::%s[]".formatted(screenshotPath.toAbsolutePath()), "text/x-asciidoc", screenshotTitle);
    
    return screenshotPath;
  }
  
  public void click(ActionPath actionPath) {
    Optional.ofNullable(actionPath.menuLabel())
            .ifPresent(fxRobot::clickOn);
    Optional.ofNullable(actionPath.menuItemLabel())
            .ifPresent(fxRobot::clickOn);
  }
  
  public void typeInProgramText(String programText) {
    fxRobot.clickOn(programTextArea())
           .write(programText);
  }
  
  public void openFile(Path path) {
    desktopService.setFileToOpen(path);
    fxRobot.clickOn(fileMenu())
           .clickOn(openMenuItemContainer());
  }
  
  public void saveAs(Path path) {
    desktopService.setBackupFile(path);
    fxRobot.clickOn(fileMenu())
           .clickOn(saveAsMenuItemContainer());
  }
  
  public boolean isPdfUserGuideOpened() {
    return desktopService.isDocumentOpened(Paths.get(USER_GUIDE_PATH)
                                                .toUri()
                                                .toString());
  }
  
  public List<ActionDescription> describeMenu(String menuLabel) {
    MenuBarButton menuBarButton = fxRobot.lookup(menuLabel)
                                         .queryAs(MenuBarButton.class);
    
    return menuBarButton.getItems()
                        .stream()
                        .map(ActionDescription::fromItem)
                        .filter(Objects::nonNull)
                        .toList();
  }
  
  public ContextMenuContent.MenuItemContainer action(ActionPath actionPath) {
    var menuBarButton = fxRobot.lookup(actionPath.menuLabel())
                               .queryAs(MenuBarButton.class);
    fxRobot.clickOn(menuBarButton);
    var menuItemContainer = menuBarButton.getItems()
                                         .stream()
                                         .filter(menuItem -> Objects.equals(menuItem.getText(), actionPath.menuItemLabel()))
                                         .findFirst()
                                         .map(MenuItem::getStyleableNode)
                                         .map(ContextMenuContent.MenuItemContainer.class::cast)
                                         .orElse(null);
    fxRobot.clickOn(menuBarButton);
    return menuItemContainer;
  }
  
  public String getBackupFileContent() throws IOException {
    return Files.readString(desktopService.getBackupFile());
    
  }
  
  public boolean applicationHasQuit() {
    return desktopService.hasQuit();
  }
  
  public Size lawnSize() {
    var width  = lawnGridPanel().getColumnCount();
    var height = lawnGridPanel().getRowCount();
    return new Size(width, height);
  }
  
  public List<Coordinates> mownCoordinates() {
    return simulationPanel()
      .getLawnCellPanels()
      .entrySet()
      .stream()
      .filter(e -> e.getValue().grassView.getImage()
                                         .equals(Images.MOWN_GRASS.image()))
      .map(Map.Entry::getKey)
      .toList();
  }
  
  public List<Position> mowerPositions() {
    return simulationPanel().getOutput()
                            .mowerPositions();
  }
}
