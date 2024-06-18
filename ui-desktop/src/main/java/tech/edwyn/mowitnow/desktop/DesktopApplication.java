package tech.edwyn.mowitnow.desktop;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.edwyn.mowitnow.desktop.services.DesktopService;
import tech.edwyn.mowitnow.desktop.services.JavaFXDesktopService;
import tech.edwyn.mowitnow.desktop.ui.MainPanel;

import java.net.URL;
import java.time.Duration;
import java.util.Arrays;
import java.util.Objects;

public class DesktopApplication extends Application {
  private static final Logger log                      = LoggerFactory.getLogger(DesktopApplication.class);
  public static final  String APPLICATION_WINDOW_TITLE = "MowItNow - Application";
  public static final  String ABOUT_DIALOG_TITLE       = "MowItNow - À Propos";
  public static final  String PDF_USER_GUIDE_JAR_PATH  = "Guide_Utilisateur.pdf";
  
  public static void main(String[] args) {
    log.trace("main - args:{}", log.isTraceEnabled() ? Arrays.toString(args) : null);
    
    Platform.setImplicitExit(true);
    launch(args);
  }
  
  @Override
  public void start(Stage primaryStage) {
    log.trace("start - primaryStage:{}", primaryStage);
    
    MainPanel mainPanel = getMainPanel();
    var       scene     = new Scene(mainPanel);
    URL desktopStyles = Objects.requireNonNull(DesktopApplication.class.getClassLoader()
                                                                       .getResource("styles/main.css"));
    scene.getStylesheets()
         .add(desktopStyles.toExternalForm());
    
    primaryStage.setScene(scene);
    primaryStage.setTitle(APPLICATION_WINDOW_TITLE);
    primaryStage.setResizable(true);
    primaryStage.centerOnScreen();
    primaryStage.show();
  }
  
  private MainPanel getMainPanel() {
    var userGuide = getParameters().getNamed()
                                   .get("user-guide");
    log.info("user-guide: {}", userGuide);
    
    var mowerLatencyMs = getParameters().getNamed()
                                        .getOrDefault("mower-latency-ms", "0");
    var mowerLatency = Duration.ofMillis(Long.parseLong(mowerLatencyMs));
    log.info("mower-latency: {}", mowerLatency);
    
    DesktopService desktopService = new JavaFXDesktopService(getHostServices());
    return new MainPanel(desktopService, userGuide, mowerLatency);
  }
  
}
