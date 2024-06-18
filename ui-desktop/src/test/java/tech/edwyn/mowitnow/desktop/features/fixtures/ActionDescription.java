package tech.edwyn.mowitnow.desktop.features.fixtures;

import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.input.KeyCombination;

public record ActionDescription(String text, KeyCombination accelerator) {
  public static ActionDescription fromItem(MenuItem menuItem) {
    if (menuItem instanceof SeparatorMenuItem) {
      return null;
    }
    return new ActionDescription(menuItem.getText(), menuItem.getAccelerator());
  }
}
