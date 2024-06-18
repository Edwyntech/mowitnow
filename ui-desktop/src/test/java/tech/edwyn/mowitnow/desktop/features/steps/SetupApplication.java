package tech.edwyn.mowitnow.desktop.features.steps;

import io.cucumber.java.fr.Etantdonnéque;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testfx.api.FxToolkit;

import static tech.edwyn.mowitnow.desktop.features.assertions.Assertions.assertThat;

public class SetupApplication {
  private static final Logger log = LoggerFactory.getLogger(SetupApplication.class);
  
  @Etantdonnéque("l'application se lance")
  public void applicationStarts() {
    log.trace("applicationStarts");
    
    assertThat(FxToolkit.isFXApplicationThreadRunning()).isTrue();
  }
  
}
