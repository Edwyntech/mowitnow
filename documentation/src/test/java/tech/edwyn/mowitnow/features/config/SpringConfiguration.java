package tech.edwyn.mowitnow.features.config;

import io.cucumber.core.internal.com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import tech.edwyn.mowitnow.domain.services.ProgramParser;

@SpringBootConfiguration
public class SpringConfiguration {
  
  @Bean
  public ObjectMapper objectMapper() {
    return new ObjectMapper();
  }
  
  @Bean
  public ProgramParser programParser() {
    return new ProgramParser();
  }
  
}
