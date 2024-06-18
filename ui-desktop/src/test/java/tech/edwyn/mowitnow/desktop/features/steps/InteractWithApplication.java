package tech.edwyn.mowitnow.desktop.features.steps;

import io.cucumber.java.fr.Etqu;
import io.cucumber.java.fr.Lorsqu;
import io.cucumber.java.fr.Lorsque;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import tech.edwyn.mowitnow.desktop.features.config.ScenarioUI;
import tech.edwyn.mowitnow.desktop.features.fixtures.ActionPath;

import java.io.IOException;

import static java.nio.file.Files.createTempFile;
import static tech.edwyn.mowitnow.desktop.features.assertions.Assertions.assertThat;

@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
public class InteractWithApplication {
  private static final Logger log = LoggerFactory.getLogger(InteractWithApplication.class);
  
  @Autowired
  private ScenarioUI ui;
  
  @Lorsqu("un utilisateur quitte l'application")
  public void userQuitsApplication() {
    log.trace("userQuitsApplication");
    
    ui.quitApplication();
  }
  
  @Lorsque("la fenêtre principale s'affiche")
  public void mainScreenIsDisplayed() throws IOException {
    log.trace("mainScreenIsDisplayed");
    
    var screenshot = ui.takeScreenshot("La fenêtre principale.");
    assertThat(screenshot).exists();
  }
  
  @Lorsqu("le menu {word} est capturé")
  public void menuIsCaptured(String menuLabel) throws IOException {
    log.trace("menuIsCaptured - menuLabel:{}", menuLabel);
    
    ui.click(new ActionPath(menuLabel, null));
    var screenshot = ui.takeScreenshot("Le menu " + menuLabel);
    
    assertThat(screenshot).exists();
  }
  
  @Lorsqu("un utilisateur renseigne le champ de programmation avec le texte")
  public void userFillsInProgramTextAreaWithText(String programText) {
    log.trace("userFillsInProgramTextAreaWithText - programText:{}", programText);
    
    ui.typeInProgramText(programText);
    
    assertThat(ui.programTextArea()).hasText(programText);
  }
  
  @Lorsque("un utilisateur ouvre le fichier {resource}")
  public void userOpensFile(Resource fileToOpen) throws IOException {
    log.trace("userOpensFile - fileToOpen:{}", fileToOpen);
    
    ui.openFile(fileToOpen.getFile()
                          .toPath());
  }
  
  @Etqu("un utilisateur enregistre la programmation dans un fichier de sauvegarde")
  public void userSavesConfigurationToBackupFile() throws IOException {
    log.trace("userSavesConfigurationToBackupFile");
    
    ui.saveAs(createTempFile("configuration", ".txt"));
  }
  
  @Lorsqu("un utilisateur sélectionne l'action {actionPath}")
  public void userSelectsAction(ActionPath actionPath) {
    log.trace("userActions - actionPath:{}", actionPath);
    
    ui.click(actionPath);
  }
}
