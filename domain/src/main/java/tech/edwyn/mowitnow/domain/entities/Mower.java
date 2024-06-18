package tech.edwyn.mowitnow.domain.entities;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Queue;

import static java.lang.Thread.currentThread;
import static java.lang.Thread.sleep;

public class Mower {
  private static final Logger log = LoggerFactory.getLogger(Position.class);
  
  private       Position               position;
  private final Queue<Command>         instructions;
  private final Duration               latency;
  private final List<PositionListener> positionListeners = new ArrayList<>();
  
  public Mower(Position position, Queue<Command> instructions, Duration latency) {
    this.position = position;
    this.instructions = instructions;
    this.latency = latency;
  }
  
  public Position getPosition() {
    return position;
  }
  
  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Mower mower = (Mower) o;
    return Objects.equals(position, mower.position) && Objects.equals(instructions, mower.instructions);
  }
  
  @Override
  public int hashCode() {
    return Objects.hash(position, instructions);
  }
  
  @Override
  public String toString() {
    return "Mower{" +
      "position=" + position +
      ", instructions=" + instructions +
      '}';
  }
  
  public void addPositionListener(PositionListener positionListener) {
    log.trace("addPositionListener - positionListener:{}", positionListener);
    
    positionListeners.add(positionListener);
    positionListener.onPositionChanged(null, position);
  }
  
  public void removePositionListener(PositionListener positionListener) {
    log.trace("removePositionListener - positionListener:{}", positionListener);
    
    positionListeners.remove(positionListener);
  }
  
  public void notifyPositionUpdate(Position oldPosition, Position newPosition) {
    positionListeners.forEach(positionListener -> positionListener.onPositionChanged(oldPosition, newPosition));
  }
  
  public Position mow(Size lawnSize) {
    log.trace("mow - lawnSize:{}", lawnSize);
    
    while (!instructions.isEmpty() && !currentThread().isInterrupted()) {
      Command command = instructions.poll();
      log.debug("mow - execute command:{}", command);
      
      Position nextPosition = command.applyTo(position);
      if (nextPosition.liesWithin(lawnSize)) {
        log.debug("mow - move to nextPosition:{}", nextPosition);
        
        try {
          sleep(latency);
        } catch (InterruptedException e) {
          log.warn("cancelled sleeping");
          return position;
        }
        
        Position oldPosition = position;
        position = nextPosition;
        notifyPositionUpdate(oldPosition, nextPosition);
      }
    }
    return position;
  }
  
}
