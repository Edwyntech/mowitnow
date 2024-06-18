package tech.edwyn.mowitnow.desktop.features.assertions;

import com.sun.javafx.scene.control.ContextMenuContent.MenuItemContainer;
import org.testfx.assertions.api.AbstractParentAssert;

import static org.testfx.assertions.api.Assertions.assertThat;
import static org.testfx.assertions.impl.Adapter.fromMatcher;

public class MenuItemContainerAssert extends AbstractParentAssert<MenuItemContainerAssert> {
  protected MenuItemContainerAssert(MenuItemContainer actual) {
    super(actual, MenuItemContainerAssert.class);
  }
  
  public MenuItemContainerAssert hasItemDisabled() {
    assertThat(actual).is(fromMatcher(MenuItemContainerMatchers.hasItemDisabled()));
    return this;
  }
  
  public MenuItemContainerAssert hasItemEnabled() {
    assertThat(actual).is(fromMatcher(MenuItemContainerMatchers.hasItemEnabled()));
    return this;
  }
  
}
