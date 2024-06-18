package tech.edwyn.mowitnow.desktop.features.assertions;

import javafx.scene.control.TextArea;
import org.testfx.assertions.api.AbstractTextInputControlAssert;

import static org.testfx.assertions.api.Assertions.assertThat;
import static org.testfx.assertions.impl.Adapter.fromMatcher;

public class TextAreaAssert extends AbstractTextInputControlAssert<TextAreaAssert> {
  protected TextAreaAssert(TextArea actual) {
    super(actual, TextAreaAssert.class);
  }
  
  public TextAreaAssert hasTextTrimmed(String expectedText) {
    assertThat(actual).is(fromMatcher(TextAreaMatchers.hasTextTrimmed(expectedText)));
    return this;
  }
  
}
