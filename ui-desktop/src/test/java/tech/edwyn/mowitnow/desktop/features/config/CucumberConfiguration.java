package tech.edwyn.mowitnow.desktop.features.config;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.cucumber.spring.CucumberContextConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.testfx.framework.junit5.ApplicationTest;
import tech.edwyn.mowitnow.desktop.DesktopApplication;

import java.nio.file.Files;
import java.nio.file.Paths;

@CucumberContextConfiguration
@SpringBootTest
@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
public class CucumberConfiguration extends ApplicationTest {
  private static final Logger log                        = LoggerFactory.getLogger(CucumberConfiguration.class);
  public static final  String USER_GUIDE_PATH            = "target/test-classes/Guide_Utilisateur.pdf";
  public static final  long   MOWER_LATENCY_MILLISECONDS = 0;
  
  @Autowired
  private ScenarioUI scenarioUI;
  
  @Before
  public void setup(Scenario scenario) throws Exception {
    log.trace("setup - scenario:{}", scenario);
    
    var userGuidePath = Paths.get(USER_GUIDE_PATH);
    if (!Files.exists(userGuidePath)) {
      Files.createDirectories(userGuidePath.getParent());
      Files.createFile(userGuidePath);
    }
    
    internalBefore();
    launch(DesktopApplication.class,
      "--user-guide=%s".formatted(USER_GUIDE_PATH),
      "--mower-latency-ms=%s".formatted(MOWER_LATENCY_MILLISECONDS));
    scenarioUI.setup(scenario, this);
  }
  
  @After
  public void tearDown(Scenario scenario) throws Exception {
    log.trace("tearDown - scenario:{}", scenario);
    
    internalAfter();
  }
}
