package tech.edwyn.mowitnow.features.steps;

import io.cucumber.java.fr.Lorsque;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import tech.edwyn.mowitnow.domain.entities.Program;
import tech.edwyn.mowitnow.domain.services.ParsingException;
import tech.edwyn.mowitnow.domain.services.ProgramParser;

@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
public class CompileProgram {
  private static final Logger           log = LoggerFactory.getLogger(CompileProgram.class);
  private              Program          program;
  private              ParsingException compileException;
  
  @Autowired
  private SetupInput setupInput;
  
  @Autowired
  private ProgramParser programParser;
  
  @Lorsque("l'application crée la programmation")
  public void applicationCreatesMowingProgram() {
    log.trace("applicationCreatesMowingProgram");
    
    var programInput = setupInput.getInput();
    try {
      this.program = programParser.parse(programInput);
    } catch (ParsingException parsingException) {
      this.compileException = parsingException;
    }
  }
  
  public Program getProgram() {
    return program;
  }
  
  public ParsingException getCompileException() {
    return compileException;
  }
}
