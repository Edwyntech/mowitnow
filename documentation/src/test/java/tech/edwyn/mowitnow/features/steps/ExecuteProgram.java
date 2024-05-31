package tech.edwyn.mowitnow.features.steps;

import io.cucumber.java.fr.Lorsque;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import tech.edwyn.mowitnow.domain.entities.Output;

@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
public class ExecuteProgram {
  private static final Logger log = LoggerFactory.getLogger(ExecuteProgram.class);
  private              Output output;
  
  @Autowired
  private CompileProgram compileProgram;
  
  @Lorsque("l'application exécute la programmation")
  public void applicationRunsMowingProgram() {
    log.trace("applicationRunsMowingProgram");
    
    var program = compileProgram.getProgram();
    output = program.execute();
  }
  
  public Output getOutput() {
    return output;
  }
}
