package tech.edwyn.mowitnow.desktop.features.steps;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.BeforeAll;
import io.cucumber.java.Scenario;
import io.cucumber.java.fr.Etantdonné;
import io.cucumber.java.fr.Lorsqu;
import org.assertj.core.api.Assertions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit5.ApplicationAdapter;
import org.testfx.framework.junit5.ApplicationTest;
import tech.edwyn.mowitnow.desktop.DesktopApplication;
import tech.edwyn.mowitnow.desktop.features.config.MowItNowTest;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import static org.assertj.core.api.Assertions.assertThat;

public class SetupApplication extends MowItNowTest {
  private static final Logger log = LoggerFactory.getLogger(SetupApplication.class);
  
  @BeforeAll
  public static void setupJavaFx() {
    System.setProperty("java.awt.headless", "true");
    System.setProperty("testfx.headless", "true");
    System.setProperty("testfx.robot", "glass");
    System.setProperty("prism.order", "sw");
  }
  
  @Before
  public void setupScenario(Scenario scenario) {
    setScenario(scenario);
  }
  
  @After
  public void tearDown() throws TimeoutException {
    if (FxToolkit.isFXApplicationThreadRunning()) {
      FxToolkit.cleanupAfterTest(this, new ApplicationAdapter(this));
    }
  }
  
  @Lorsqu("la fenêtre de l'application est capturée")
  public void applicationIsCaptured() throws IOException {
    log.trace("applicationIsCaptured");
    
    var screenshot = attachSnapshotTitled("La fenêtre de l'application");
    assertThat(screenshot).exists();
  }
  
  @Etantdonné("l'application se lance")
  public void applicationStarts() throws Exception {
    log.trace("applicationStarts");
    
    ApplicationTest.launch(DesktopApplication.class);
    Assertions.assertThat(FxToolkit.isFXApplicationThreadRunning())
              .isTrue();
  }
  
}
