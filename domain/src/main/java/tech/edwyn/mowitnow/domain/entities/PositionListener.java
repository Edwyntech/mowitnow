package tech.edwyn.mowitnow.domain.entities;

@FunctionalInterface
public interface PositionListener {
  void onPositionChanged(Position oldPosition, Position newPosition);
}
