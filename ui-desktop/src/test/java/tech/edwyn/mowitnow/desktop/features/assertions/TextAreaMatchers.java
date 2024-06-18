package tech.edwyn.mowitnow.desktop.features.assertions;

import javafx.scene.control.TextArea;
import org.hamcrest.Matcher;

import java.util.Objects;
import java.util.Optional;

import static org.testfx.matcher.base.GeneralMatchers.typeSafeMatcher;

public class TextAreaMatchers {
  public static Matcher<TextArea> hasTextTrimmed(String expectedText) {
    String descriptionText = "has text \"" + expectedText + "\" trimmed";
    return typeSafeMatcher(TextArea.class, descriptionText,
      TextAreaMatchers::toText,
      textArea -> {
        var trimmedText = Optional.ofNullable(textArea.getText())
                                  .map(String::trim)
                                  .orElse(null);
        return Objects.equals(expectedText, trimmedText);
      });
  }
  
  private static String toText(TextArea textArea) {
    return "%s with text: \"%s\"".formatted(
      textArea.getClass()
              .getSimpleName(),
      textArea.getText());
  }
  
}
