package tech.edwyn.mowitnow.desktop.features.assertions;

import com.sun.javafx.scene.control.ContextMenuContent.MenuItemContainer;
import javafx.scene.control.TextArea;

public class Assertions extends org.testfx.assertions.api.Assertions {
  public static MenuItemContainerAssert assertThat(MenuItemContainer actual) {
    return new MenuItemContainerAssert(actual);
  }
  
  public static TextAreaAssert assertThat(TextArea actual) {
    return new TextAreaAssert(actual);
  }
}
