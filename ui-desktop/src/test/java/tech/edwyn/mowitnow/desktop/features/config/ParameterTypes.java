package tech.edwyn.mowitnow.desktop.features.config;

import io.cucumber.core.internal.com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.DefaultDataTableCellTransformer;
import io.cucumber.java.DefaultDataTableEntryTransformer;
import io.cucumber.java.DefaultParameterTransformer;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Type;

@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
public class ParameterTypes {
  
  @Autowired private ObjectMapper objectMapper;
  
  @DefaultParameterTransformer
  @DefaultDataTableEntryTransformer
  @DefaultDataTableCellTransformer
  public Object transformer(Object fromValue, Type toValueType) {
    return objectMapper.convertValue(fromValue, objectMapper.constructType(toValueType));
  }
  
}
