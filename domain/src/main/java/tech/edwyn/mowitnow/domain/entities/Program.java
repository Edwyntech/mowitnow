package tech.edwyn.mowitnow.domain.entities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public record Program(Size lawnSize, List<Mower> mowers) {
  private static final Logger log = LoggerFactory.getLogger(Program.class);
  
  public Output execute() {
    log.trace("execute");
    
    List<Position> positions = mowers.stream()
                                     .map(mower -> mower.mow(lawnSize))
                                     .toList();
    
    return new Output(positions);
  }
  
}
