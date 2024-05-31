package tech.edwyn.mowitnow.domain.entities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public record Coordinates(int x, int y) {
  private static final Logger log = LoggerFactory.getLogger(Coordinates.class);
  
  public Coordinates forward(Direction direction) {
    log.trace("forward - direction {}", direction);
    
    return switch (direction) {
      case NORTH -> new Coordinates(x, y + 1);
      case EAST -> new Coordinates(x + 1, y);
      case WEST -> new Coordinates(x - 1, y);
      case SOUTH -> new Coordinates(x, y - 1);
    };
  }
  
  public boolean liesWithin(Size bounds) {
    log.trace("liesWithin - bounds:{}", bounds);
    
    return x >= 0
      && y >= 0
      && x < bounds.width()
      && y < bounds.height();
  }
  
}
