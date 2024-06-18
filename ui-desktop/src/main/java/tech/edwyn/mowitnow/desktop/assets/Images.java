package tech.edwyn.mowitnow.desktop.assets;

import javafx.scene.image.Image;

import java.net.URL;
import java.util.Objects;

public enum Images {
  LOGO("images/logo.png"),
  MOWN_GRASS("images/mown-grass.png"),
  GROWN_GRASS("images/grown-grass.png"),
  MOWER("images/mower.png");
  
  private final Image image;
  
  Images(String path) {
    URL url = Objects.requireNonNull(getClass().getClassLoader()
                                               .getResource(path));
    this.image = new Image(url.toExternalForm());
  }
  
  public Image image() {
    return image;
  }
  
}
