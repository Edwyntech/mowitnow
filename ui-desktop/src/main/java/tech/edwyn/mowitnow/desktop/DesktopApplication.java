package tech.edwyn.mowitnow.desktop;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.edwyn.mowitnow.desktop.ui.MainPanel;

import java.util.Arrays;

public class DesktopApplication extends javafx.application.Application {
  private static final Logger log = LoggerFactory.getLogger(DesktopApplication.class);
  
  public static void main(String[] args) {
    log.trace("main - args:{}", log.isTraceEnabled() ? Arrays.toString(args) : null);
    launch(args);
  }
  
  @Override
  public void start(Stage primaryStage) {
    log.trace("start - primaryStage:{}", primaryStage);
    MainPanel mainPanel = new MainPanel();
    mainPanel.setId("mainPanel");
    Scene     scene            = createSceneContaining(mainPanel);
    
    primaryStage.setTitle("MowItNow");
    primaryStage.setResizable(true);
    primaryStage.centerOnScreen();
    primaryStage.setScene(scene);
    primaryStage.show();
  }
  
  public static Scene createSceneContaining(Parent parent) {
    return new Scene(parent, 1280, 720);
  }
  
}
