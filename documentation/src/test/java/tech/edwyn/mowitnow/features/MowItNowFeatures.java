package tech.edwyn.mowitnow.features;

import io.cucumber.core.internal.com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.platform.suite.api.*;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import tech.edwyn.mowitnow.domain.services.ProgramParser;

import java.time.Duration;

import static io.cucumber.junit.platform.engine.Constants.*;

@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("features")
@ConfigurationParameters({
  @ConfigurationParameter(key = GLUE_PROPERTY_NAME, value = "tech.edwyn.mowitnow.features"),
  @ConfigurationParameter(key = PLUGIN_PROPERTY_NAME, value = "json:target/cucumber/report.json"),
  @ConfigurationParameter(key = PLUGIN_PUBLISH_QUIET_PROPERTY_NAME, value = "true"),
  @ConfigurationParameter(key = JUNIT_PLATFORM_NAMING_STRATEGY_PROPERTY_NAME, value = "long")
})
@SpringBootConfiguration
public class MowItNowFeatures {
  @Bean
  public ObjectMapper objectMapper() {
    return new ObjectMapper();
  }
  
  @Bean
  public Duration mowerLatency() {
    return Duration.ZERO;
  }
  
  @Bean
  public ProgramParser programParser(Duration mowerLatency) {
    return new ProgramParser(mowerLatency);
  }
  
}
