package tech.edwyn.mowitnow.domain.entities;


import java.util.Objects;
import java.util.Queue;

public class Mower {
  private       Position       position;
  private final Queue<Command> instructions;
  
  public Mower(Position position, Queue<Command> instructions) {
    this.position = position;
    this.instructions = instructions;
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
  
  public Position mow(Size lawnSize) {
    while (!instructions.isEmpty()) {
      Command  command      = instructions.poll();
      Position nextPosition = command.applyTo(position);
      if (nextPosition.liesWithin(lawnSize)) {
        this.position = nextPosition;
      }
    }
    return position;
  }
  
}
