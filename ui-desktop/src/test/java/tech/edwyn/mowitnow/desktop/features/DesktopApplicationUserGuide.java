package tech.edwyn.mowitnow.desktop.features;

import io.cucumber.core.internal.com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.platform.suite.api.*;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import tech.edwyn.mowitnow.desktop.features.config.ScenarioUI;
import tech.edwyn.mowitnow.desktop.services.FakeDesktopService;

import static io.cucumber.junit.platform.engine.Constants.*;

@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("features")
@ConfigurationParameters({
  @ConfigurationParameter(key = GLUE_PROPERTY_NAME, value = "tech.edwyn.mowitnow.desktop.features"),
  @ConfigurationParameter(key = PLUGIN_PROPERTY_NAME, value = "json:target/cucumber/report.json"),
  @ConfigurationParameter(key = PLUGIN_PUBLISH_QUIET_PROPERTY_NAME, value = "true"),
  @ConfigurationParameter(key = JUNIT_PLATFORM_NAMING_STRATEGY_PROPERTY_NAME, value = "long")
})
@SpringBootConfiguration
public class DesktopApplicationUserGuide {
  
  @Bean
  public ObjectMapper objectMapper() {
    return new ObjectMapper();
  }
  
  @Bean
  public FakeDesktopService desktopService() {
    return new FakeDesktopService();
  }
  
  @Bean
  public ScenarioUI ui(FakeDesktopService desktopService) {
    return new ScenarioUI(desktopService);
  }
  
}
