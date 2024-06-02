package tech.edwyn.mowitnow.desktop.features.config;

import io.cucumber.java.Scenario;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testfx.framework.junit5.ApplicationTest;
import tech.edwyn.mowitnow.desktop.ui.MainPanel;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.testfx.util.DebugUtils.*;

public abstract class MowItNowTest extends ApplicationTest {
  private static final Logger log                = LoggerFactory.getLogger(MowItNowTest.class);
  private static final Path   SCREENSHOTS_FOLDER = Paths.get("target/cukedoctor/screenshots");
  
  public static final String MAIN_PANEL = "#mainPanel";
  
  private Scenario scenario;
  
  protected MainPanel mainPanel() {
    return lookup(MAIN_PANEL).queryAs(MainPanel.class);
  }
  
  protected Path attachSnapshotTitled(String title) throws IOException {
    Files.createDirectories(SCREENSHOTS_FOLDER);
    Path savedScreenshot = SCREENSHOTS_FOLDER.resolve(scenario.getId() + ".png");
    
    var indent = " ".repeat(3);
    var message = compose(
      saveWindow(() -> savedScreenshot, indent),
      showKeysPressedAtTestFailure(this, indent),
      showMouseButtonsPressedAtTestFailure(this, indent),
      showFiredEvents()
    ).apply(new StringBuilder());
    
    log.debug("attachSnapshotTitled - debug information:{}", message);
    scenario.attach("image::%s[]".formatted(savedScreenshot.toAbsolutePath()), "text/x-asciidoc", title);
    
    return savedScreenshot;
  }
  
  public void setScenario(Scenario scenario) {
    this.scenario = scenario;
  }
}
