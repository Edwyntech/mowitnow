package tech.edwyn.mowitnow.desktop.features.steps;

import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.cucumber.java.fr.Alors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testfx.assertions.api.Assertions;
import tech.edwyn.mowitnow.desktop.features.config.MowItNowTest;

public class CheckApplication extends MowItNowTest {
  private static final Logger log = LoggerFactory.getLogger(CheckApplication.class);
  
  @Before
  public void setupScenario(Scenario scenario) {
    setScenario(scenario);
  }
  
  @Alors("la fenêtre principale s'affiche")
  public void applicationPanelIsVisible() {
    log.trace("applicationPanelIsVisible");
    
    Assertions.assertThat(mainPanel())
              .isVisible();
  }
  
}
