package tech.edwyn.mowitnow.domain.entities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.EnumSet;
import java.util.function.Function;
import java.util.function.UnaryOperator;

public enum Command {
  RIGHT("D", Position::right),
  LEFT("G", Position::left),
  FORWARD("A", Position::forward);
  
  private static final Logger                       log = LoggerFactory.getLogger(Command.class);
  private final        String                       label;
  private final        Function<Position, Position> move;
  
  Command(String label, UnaryOperator<Position> move) {
    this.label = label;
    this.move = move;
  }
  
  public static Command labelled(String label) {
    log.trace("labelled - label:{}", label);
    
    return EnumSet.allOf(Command.class)
                  .stream()
                  .filter(c -> c.label.equals(label))
                  .findFirst()
                  .orElse(null);
  }
  
  public Position applyTo(Position position) {
    log.trace("applyTo - position:{}", position);
    
    return move.apply(position);
  }
}
