package tech.edwyn.mowitnow.features.steps;

import io.cucumber.java.fr.Etantdonné;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SetupInput {
  private static final Logger log = LoggerFactory.getLogger(SetupInput.class);
  private              String input;
  
  @Etantdonné("le contenu de programmation")
  public void theInput(String input) {
    log.trace("theInput - input:{}", input);
    
    this.input = input;
  }
  
  public String getInput() {
    return input;
  }
}
