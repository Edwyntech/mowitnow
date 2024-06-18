package tech.edwyn.mowitnow.desktop.features.config;

import io.cucumber.core.internal.com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.*;
import javafx.scene.input.KeyCombination;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import tech.edwyn.mowitnow.desktop.features.fixtures.ActionDescription;
import tech.edwyn.mowitnow.desktop.features.fixtures.ActionPath;
import tech.edwyn.mowitnow.domain.entities.Coordinates;
import tech.edwyn.mowitnow.domain.entities.Direction;
import tech.edwyn.mowitnow.domain.entities.Position;
import tech.edwyn.mowitnow.domain.entities.Size;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.Scanner;

@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
public class ParameterTypes {
  
  @Autowired private ObjectMapper objectMapper;
  
  @DefaultParameterTransformer
  @DefaultDataTableEntryTransformer
  @DefaultDataTableCellTransformer
  public Object transformer(Object fromValue, Type toValueType) {
    return objectMapper.convertValue(fromValue, objectMapper.constructType(toValueType));
  }
  
  @DataTableType
  public ActionDescription menuItemDescription(Map<String, String> entries) {
    var text        = entries.get("Action");
    var accelerator = KeyCombination.valueOf(entries.get("Raccourci"));
    return new ActionDescription(text, accelerator);
  }
  
  @ParameterType(value = "(.+)\\|(.+)")
  public ActionPath actionPath(String menuLabel, String menuItemLabel) {
    return new ActionPath(menuLabel, menuItemLabel);
  }
  
  @ParameterType(value = ".+")
  public Resource resource(String resourceString) {
    return new ClassPathResource(resourceString);
  }
  
  @ParameterType("(\\d+)x(\\d+)")
  public Size size(String widthString, String heightString) {
    var width  = Integer.parseInt(widthString);
    var height = Integer.parseInt(heightString);
    
    return new Size(width, height);
  }
  
  @DataTableType
  public Coordinates coordinates(Map<String, String> entries) {
    var coordinatesString = entries.get("Coordonnées");
    try (Scanner scanner = new Scanner(coordinatesString)) {
      scanner.useDelimiter(", ");
      return new Coordinates(scanner.nextInt(), scanner.nextInt());
    }
  }
  
  @DataTableType
  public Position position(Map<String, String> entries) {
    var positionString = entries.get("Position");
    try (Scanner scanner = new Scanner(positionString)) {
      scanner.useDelimiter(", ");
      var coordinates = new Coordinates(scanner.nextInt(), scanner.nextInt());
      var direction   = Direction.labelled(scanner.next());
      return new Position(coordinates, direction);
    }
  }
  
}
