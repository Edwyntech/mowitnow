package tech.edwyn.mowitnow.desktop.features.steps;

import io.cucumber.java.fr.Alors;
import io.cucumber.java.fr.Et;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import tech.edwyn.mowitnow.desktop.features.config.ScenarioUI;
import tech.edwyn.mowitnow.desktop.features.fixtures.ActionDescription;
import tech.edwyn.mowitnow.desktop.features.fixtures.ActionPath;
import tech.edwyn.mowitnow.domain.entities.Coordinates;
import tech.edwyn.mowitnow.domain.entities.Position;
import tech.edwyn.mowitnow.domain.entities.Size;

import java.io.IOException;
import java.time.Duration;
import java.util.List;

import static tech.edwyn.mowitnow.desktop.features.assertions.Assertions.assertThat;

@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
public class CheckApplication {
  private static final Logger log = LoggerFactory.getLogger(CheckApplication.class);
  
  @Autowired
  private ScenarioUI ui;
  
  @Alors("la fenêtre principale doit s'afficher")
  public void mainPanelMustBeVisible() {
    log.trace("mainPanelMustBeVisible");
    
    assertThat(ui.mainPanel()).isVisible();
  }
  
  @Et("la zone d'édition de la programmation doit s'afficher")
  public void programPanelMustBeVisible() {
    log.trace("programPanelMustBeVisible");
    
    assertThat(ui.programPanel()).isVisible();
  }
  
  @Et("la zone de simulation doit s'afficher")
  public void simulationPanelMustBeVisible() {
    log.trace("simulationPanelMustBeVisible");
    
    assertThat(ui.simulationPanel()).isVisible();
  }
  
  @Et("la barre de menu doit s'afficher")
  public void menuBarMustBeVisible() {
    log.trace("menuBarMustBeVisible");
    
    assertThat(ui.menuBar()).isVisible();
  }
  
  @Alors("l'application doit s'être arrêtée")
  public void applicationMustBeStopped() {
    log.trace("applicationMustBeStopped");
    
    assertThat(ui.applicationHasQuit()).isTrue();
  }
  
  @Alors("le menu {word} affiche les actions")
  public void menuDisplaysActions(String menuLabel, List<ActionDescription> expectedActionDescriptions) {
    log.trace("menuDisplaysActions - menuLabel:{}, actionInfos:{}", menuLabel, expectedActionDescriptions);
    
    var menuItemsAsDescriptions = ui.describeMenu(menuLabel);
    assertThat(menuItemsAsDescriptions).containsAll(expectedActionDescriptions);
  }
  
  @Alors("l'action {actionPath} doit être disponible")
  public void actionMustBeAvailable(ActionPath actionPath) {
    log.trace("actionMustBeAvailable - actionPath:{}", actionPath);
    
    var menuItemContainer = ui.action(actionPath);
    
    assertThat(menuItemContainer).hasItemEnabled();
  }
  
  @Alors("l'action {actionPath} doit être indisponible")
  public void actionMustBeUnavailable(ActionPath actionPath) {
    log.trace("actionMustBeUnavailable - actionPath:{}", actionPath);
    
    var menuItemContainer = ui.action(actionPath);
    
    assertThat(menuItemContainer).hasItemDisabled();
  }
  
  @Alors("le champ de programmation doit contenir")
  public void programTextAreaMustContain(String expectedText) {
    log.trace("programTextAreaMustContain - expectedText:{}", expectedText);
    
    assertThat(ui.programTextArea()).hasTextTrimmed(expectedText);
  }
  
  @Alors("le fichier de sauvegarde doit contenir")
  public void backupFileMustContain(String expectedText) throws IOException {
    log.trace("backupFileMustContain - expectedText:{}", expectedText);
    
    assertThat(ui.getBackupFileContent()).isEqualTo(expectedText);
  }
  
  @Alors("aucun message d'erreur ne doit s'afficher")
  public void noErrorMessageIsDisplayed() {
    log.trace("noErrorMessageIsDisplayed");
    
    assertThat(ui.errorMessagesLabel()).isInvisible();
  }
  
  @Alors("le message d'erreur doit afficher")
  public void errorLabelMustDisplayText(String expectedText) throws IOException {
    log.trace("errorLabelMustDisplayText");
    
    var snapshot = ui.takeScreenshot("Simulation invalide.");
    assertThat(snapshot).isNotNull();
    
    assertThat(ui.errorMessagesLabel()).isVisible()
                                       .hasText(expectedText);
  }
  
  @Alors("la simulation doit afficher une pelouse de taille {size}")
  public void simulationMustDisplayLawnOfSize(Size expectedSize) throws IOException {
    log.trace("simulationMustDisplayLawnOfSize - expectedSize:{}", expectedSize);
    
    var snapshot = ui.takeScreenshot("Simulation validée.");
    assertThat(snapshot).isNotNull();
    
    assertThat(ui.lawnGridPanel()).isVisible();
    assertThat(ui.lawnSize()).isEqualTo(expectedSize);
  }
  
  @Et("la simulation ne doit pas afficher la pelouse")
  public void simulationMustNotDisplayLawn() {
    log.trace("simulationMustNotDisplayLawn");
    
    assertThat(ui.lawnGridPanel()).isInvisible();
  }
  
  @Alors("un dialogue d'information doit s'afficher")
  public void aboutDialogMustBeDisplayed() {
    log.trace("aboutDialogMustBeDisplayed");
    
    assertThat(ui.aboutDialog()).isFocused();
  }
  
  @Alors("la documentation PDF doit s'afficher")
  public void pdfDocumentationMustBeDisplayed() {
    log.trace("pdfDocumentationMustBeDisplayed");
    
    assertThat(ui.isPdfUserGuideOpened()).isTrue();
  }
  
  @Alors("la simulation doit exécuter la programmation")
  public void simulationMustExecuteProgram() {
    log.trace("simulationMustExecuteProgram");
    
    var futureRunning = ui.simulationPanel()
                          .runningProperty()
                          .get();
    
    assertThat(futureRunning).succeedsWithin(Duration.ofSeconds(1));
  }
  
  @Et("la pelouse doit apparaitre tondue aux coordonnées")
  public void lawnMustBeMownAtCoordinates(List<Coordinates> expectedCoordinates) {
    log.trace("lawnMustBeMownAtCoordinates - expectedCoordinates:{}", expectedCoordinates);
    
    assertThat(ui.mownCoordinates()).containsAll(expectedCoordinates);
  }
  
  @Et("les tondeuses doivent finir aux positions")
  public void mowersMustEndAtPositions(List<Position> expectedPositions) throws IOException {
    log.trace("mowersMustEndAtPositions - expectedPositions:{}", expectedPositions);
    
    var snapshot = ui.takeScreenshot("Simulation exécutée.");
    assertThat(snapshot).isNotNull();
    
    assertThat(ui.mowerPositions()).containsAll(expectedPositions);
  }
}
