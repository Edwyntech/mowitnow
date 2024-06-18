package tech.edwyn.mowitnow.domain.entities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import static java.lang.Thread.currentThread;

public record Program(Size lawnSize, List<Mower> mowers) implements Callable<Output> {
  private static final Logger log = LoggerFactory.getLogger(Program.class);
  
  @Override
  public Output call() {
    log.trace("call");
    
    List<Position> positions = new ArrayList<>();
    for (Mower mower : mowers) {
      if (!currentThread().isInterrupted()) {
        Position endingPosition = mower.mow(lawnSize);
        positions.add(endingPosition);
      }
    }
    
    return new Output(positions);
  }
}
