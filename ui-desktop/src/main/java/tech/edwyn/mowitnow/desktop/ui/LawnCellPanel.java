package tech.edwyn.mowitnow.desktop.ui;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.edwyn.mowitnow.domain.entities.Position;

import java.net.URL;
import java.util.ResourceBundle;

import static tech.edwyn.mowitnow.desktop.assets.FXML.LAWN_CELL_PANEL;
import static tech.edwyn.mowitnow.desktop.assets.Images.*;

public class LawnCellPanel extends Pane implements Initializable {
  private static final Logger log = LoggerFactory.getLogger(LawnCellPanel.class);
  
  @FXML public ImageView grassView;
  @FXML public ImageView mowerView;
  
  public LawnCellPanel() {
    log.trace("LawnCellPanel");
    
    LAWN_CELL_PANEL.load(this);
  }
  
  @Override
  public void initialize(URL location, ResourceBundle resources) {
    log.trace("initialize - location:{}, resources:{}", location, resources);
    
    grassView.fitWidthProperty()
             .bind(widthProperty());
    grassView.fitHeightProperty()
             .bind(heightProperty());
    mowerView.fitWidthProperty()
             .bind(widthProperty());
    mowerView.fitHeightProperty()
             .bind(heightProperty());
    
    grassView.setImage(GROWN_GRASS.image());
    mowerView.setImage(MOWER.image());
    mowerView.setVisible(false);
  }
  
  public void addMower(Position position) {
    log.trace("addMower - position:{}", position);
    
    grassView.setImage(MOWN_GRASS.image());
    mowerView.setRotate(switch (position.direction()) {
      case NORTH -> 0;
      case EAST -> 90;
      case WEST -> -90;
      case SOUTH -> 180;
    });
    mowerView.setVisible(true);
  }
  
  public void removeMower(Position position) {
    log.trace("removeMower - position:{}", position);
    
    mowerView.setVisible(false);
  }
}
