package tech.edwyn.mowitnow.domain.entities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public record Position(Coordinates coordinates, Direction direction) {
  private static final Logger log = LoggerFactory.getLogger(Position.class);
  
  public Position right() {
    log.trace("right");
    
    return new Position(coordinates, direction.right());
  }
  
  public Position left() {
    log.trace("left");
    
    return new Position(coordinates, direction.left());
  }
  
  public Position forward() {
    log.trace("forward");
    
    return new Position(coordinates.forward(direction), direction);
  }
  
  public boolean liesWithin(Size bounds) {
    return coordinates.liesWithin(bounds);
  }
}
