package tech.edwyn.mowitnow.features.steps;

import io.cucumber.java.fr.Alors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import tech.edwyn.mowitnow.domain.entities.Mower;
import tech.edwyn.mowitnow.domain.entities.Position;
import tech.edwyn.mowitnow.domain.entities.Size;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
public class CheckProgram {
  private static final Logger log = LoggerFactory.getLogger(CheckProgram.class);
  
  @Autowired
  private CompileProgram compileProgram;
  
  @Autowired
  private ExecuteProgram executeProgram;
  
  @Alors("la pelouse doit être de taille {size}")
  public void lawnMustBeOfSize(Size expectedLawnSize) {
    log.trace("lawnMustBeOfSize - size:{}", expectedLawnSize);
    
    var program  = compileProgram.getProgram();
    var lawnSize = program.lawnSize();
    assertThat(lawnSize).isEqualTo(expectedLawnSize);
  }
  
  @Alors("les tondeuses doivent être")
  public void mowersMustBe(List<Mower> expectedMowers) {
    log.trace("mowersMustBe - expectedMowers:{}", expectedMowers);
    
    var program = compileProgram.getProgram();
    var mowers  = program.mowers();
    assertThat(mowers).containsAll(expectedMowers);
  }
  
  @Alors("aucune tondeuse n'est configurée")
  public void noMowerIsConfigured() {
    log.trace("noMowerIsConfigured");
    
    var mowingProgram = compileProgram.getProgram();
    assertThat(mowingProgram.mowers()).isEmpty();
  }
  
  @Alors("les positions des tondeuses doivent être")
  public void mowerPositionsMustBe(List<Position> expectedPositions) {
    log.trace("mowerPositionsMustBe - expectedPositions:{}", expectedPositions);
    
    var output         = executeProgram.getOutput();
    var mowerPositions = output.mowerPositions();
    assertThat(mowerPositions).containsAll(expectedPositions);
  }
  
  @Alors("l'application doit émettre le message d'erreur")
  public void applicationMustThrowErrorMessage(String expectedErrorMessage) {
    log.trace("applicationMustThrowErrorMessage - expectedErrorMessage:{}", expectedErrorMessage);
    
    var compileException = compileProgram.getCompileException();
    assertThat(compileException).hasMessage(expectedErrorMessage);
  }
}
