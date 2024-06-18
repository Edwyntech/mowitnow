package tech.edwyn.mowitnow.domain.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.edwyn.mowitnow.domain.entities.*;

import java.time.Duration;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.stream.Collectors.toCollection;

public class ProgramParser {
  private static final Logger   log                  = LoggerFactory.getLogger(ProgramParser.class);
  public static final  Pattern  INSTRUCTIONS_PATTERN = Pattern.compile("[DGA]+");
  public static final  Pattern  POSITION_PATTERN     = Pattern.compile("(\\d+ \\d+) ([NEWS])");
  public static final  Pattern  COORDINATES_PATTERN  = Pattern.compile("(\\d+) (\\d+)");
  private final        Duration mowerLatency;
  
  public ProgramParser(Duration mowerLatency) {
    this.mowerLatency = mowerLatency;
  }
  
  public Program parse(String programInput) throws ParsingException {
    log.trace("parse - programInput:{}", programInput);
    
    try (Scanner scanner = new Scanner(programInput)) {
      int lineCounter = 0;
      
      if (!scanner.hasNextLine()) {
        throw new ParsingException(lineCounter, "Pas de contenu.");
      }
      
      String topRightCornerString = scanner.nextLine();
      lineCounter++;
      Coordinates topRightCorner = parseCoordinates(lineCounter, topRightCornerString);
      Size        lawnSize       = Size.upTo(topRightCorner);
      
      List<Mower> mowers = new ArrayList<>();
      while (scanner.hasNextLine()) {
        String positionString = scanner.nextLine();
        lineCounter++;
        Position position = parsePosition(lineCounter, positionString);
        if (!position.liesWithin(lawnSize)) {
          throw new ParsingException(lineCounter, "Tondeuse en dehors de la pelouse.");
        }
        
        if (!scanner.hasNextLine()) {
          throw new ParsingException(lineCounter, "Tondeuse incomplète.");
        }
        
        String instructionsString = scanner.nextLine();
        lineCounter++;
        
        Queue<Command> instructions = parseInstructions(lineCounter, instructionsString);
        Mower mower = new Mower(position, instructions, mowerLatency);
        
        mowers.add(mower);
      }
      
      Program program = new Program(lawnSize, mowers);
      log.debug("readProgram - mowingProgram:{}", program);
      
      return program;
    }
  }
  
  private Coordinates parseCoordinates(int lineCounter, String coordinatesString) throws ParsingException {
    Matcher matcher = COORDINATES_PATTERN.matcher(coordinatesString);
    if (!matcher.matches()) {
      throw new ParsingException(lineCounter, "Coordonnées invalides: '%s'.".formatted(coordinatesString));
    }
    var x = Integer.parseInt(matcher.group(1));
    var y = Integer.parseInt(matcher.group(2));
    return new Coordinates(x, y);
  }
  
  private Position parsePosition(int lineCounter, String positionString) throws ParsingException {
    Matcher matcher = POSITION_PATTERN.matcher(positionString);
    if (!matcher.matches()) {
      throw new ParsingException(lineCounter, "Position invalide: '%s'.".formatted(positionString));
    }
    var         coordinatesString = matcher.group(1);
    var         directionString   = matcher.group(2);
    Coordinates coordinates       = parseCoordinates(lineCounter, coordinatesString);
    Direction   direction         = Direction.labelled(directionString);
    return new Position(coordinates, direction);
  }
  
  private Queue<Command> parseInstructions(int lineCounter, String instructionsString) throws ParsingException {
    Matcher matcher = INSTRUCTIONS_PATTERN.matcher(instructionsString);
    if (!matcher.matches()) {
      throw new ParsingException(lineCounter, "Instructions invalides: '%s'.".formatted(instructionsString));
    }
    return Arrays.stream(instructionsString.split(""))
                 .map(Command::labelled)
                 .collect(toCollection(LinkedList::new));
  }
  
}
