package tech.edwyn.mowitnow.desktop.features.assertions;

import com.sun.javafx.scene.control.ContextMenuContent.MenuItemContainer;
import org.hamcrest.Matcher;

import java.util.Optional;

import static org.testfx.matcher.base.GeneralMatchers.typeSafeMatcher;

public class MenuItemContainerMatchers {
  public static Matcher<MenuItemContainer> hasItemDisabled() {
    String descriptionText = "has item disabled";
    return typeSafeMatcher(MenuItemContainer.class, descriptionText,
      MenuItemContainerMatchers::toText,
      menuItemContainer -> menuItemContainer.getItem()
                                            .isDisable());
  }
  
  public static Matcher<MenuItemContainer> hasItemEnabled() {
    String descriptionText = "has item enabled";
    return typeSafeMatcher(MenuItemContainer.class, descriptionText,
      MenuItemContainerMatchers::toText,
      menuItemContainer -> !menuItemContainer.getItem()
                                             .isDisable());
  }
  
  private static String toText(MenuItemContainer menuItemContainer) {
    return Optional.ofNullable(menuItemContainer.getItem())
                   .map(menuItem -> "%s is %s".formatted(menuItem.getId(), menuItem.isDisable() ? "disabled" : "enabled"))
                   .orElse("no item");
  }
  
}
