package tech.edwyn.mowitnow.domain.entities;

public record Size(int width, int height) {
  public static Size upTo(Coordinates topRightCorner) {
    return new Size(topRightCorner.x() + 1, topRightCorner.y() + 1);
  }
}
