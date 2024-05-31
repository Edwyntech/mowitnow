package tech.edwyn.mowitnow.features.config;

import io.cucumber.core.internal.com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.*;
import org.springframework.beans.factory.annotation.Autowired;
import tech.edwyn.mowitnow.domain.entities.*;

import java.lang.reflect.Type;
import java.util.*;

import static java.util.function.Predicate.not;
import static java.util.stream.Collectors.toCollection;

@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
public class ParameterTypes {
  
  @Autowired
  private ObjectMapper objectMapper;
  
  @DefaultParameterTransformer
  @DefaultDataTableEntryTransformer
  @DefaultDataTableCellTransformer
  public Object transformer(Object fromValue, Type toValueType) {
    return objectMapper.convertValue(fromValue, objectMapper.constructType(toValueType));
  }
  
  @ParameterType("(\\d+)x(\\d+)")
  public Size size(String widthString, String heightString) {
    var width  = Integer.parseInt(widthString);
    var height = Integer.parseInt(heightString);
    
    return new Size(width, height);
  }
  
  @ParameterType("\\d+, \\d+, [NEWS]")
  public Position position(String positionString) {
    Coordinates coordinates;
    Direction   direction;
    try (Scanner scanner = new Scanner(positionString)) {
      scanner.useDelimiter(", ");
      coordinates = new Coordinates(scanner.nextInt(), scanner.nextInt());
      direction = Direction.labelled(scanner.next());
    }
    return new Position(coordinates, direction);
  }
  
  @ParameterType("[DGA]+")
  public Queue<Command> instructions(String commandsString) {
    return Optional.ofNullable(commandsString)
                   .filter(not(String::isBlank))
                   .stream()
                   .flatMap(s -> Arrays.stream(s.split("")))
                   .map(Command::labelled)
                   .collect(toCollection(LinkedList::new));
  }
  
  @DataTableType
  public Mower mower(Map<String, String> entries) {
    var position     = position(entries.get("Position"));
    var instructions = instructions(entries.get("Instructions"));
    return new Mower(position, instructions);
  }
  
  @DataTableType
  public Position position(Map<String, String> entries) {
    return position(entries.get("Position"));
  }
  
}
