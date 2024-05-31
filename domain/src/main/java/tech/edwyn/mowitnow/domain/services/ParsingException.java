package tech.edwyn.mowitnow.domain.services;

public class ParsingException extends Exception {
  
  public ParsingException(int line, String errorMessage) {
    super("Ligne %d - %s".formatted(line, errorMessage));
  }
}
