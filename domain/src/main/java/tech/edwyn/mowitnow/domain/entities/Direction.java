package tech.edwyn.mowitnow.domain.entities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.EnumSet;

public enum Direction {
  NORTH("N"),
  EAST("E"),
  WEST("W"),
  SOUTH("S");
  
  private static final Logger log = LoggerFactory.getLogger(Direction.class);
  private final        String label;
  
  Direction(String label) {
    this.label = label;
  }
  
  public static Direction labelled(String label) {
    return EnumSet.allOf(Direction.class)
                  .stream()
                  .filter(direction -> direction.label.equals(label))
                  .findFirst()
                  .orElse(null);
  }
  
  public Direction left() {
    log.trace("left");
    
    return switch (this) {
      case NORTH -> WEST;
      case EAST -> NORTH;
      case WEST -> SOUTH;
      case SOUTH -> EAST;
    };
  }
  
  public Direction right() {
    log.trace("right");
    
    return switch (this) {
      case NORTH -> EAST;
      case EAST -> SOUTH;
      case WEST -> NORTH;
      case SOUTH -> WEST;
    };
  }
  
}
