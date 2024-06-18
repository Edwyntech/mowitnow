package tech.edwyn.mowitnow.desktop.ui;

import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.edwyn.mowitnow.domain.entities.*;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static tech.edwyn.mowitnow.desktop.assets.FXML.SIMULATION_PANEL;

public class SimulationPanel extends BorderPane implements Initializable, ChangeListener<Program>, PositionListener {
  private static final Logger                               log             = LoggerFactory.getLogger(SimulationPanel.class);
  private final        ExecutorService                      executorService = Executors.newSingleThreadExecutor();
  private              Program                              program;
  private              Output                               output;
  private final        SimpleObjectProperty<Future<Output>> running;
  
  private final Map<Coordinates, LawnCellPanel> lawnCellPanels;
  
  @FXML public GridPane lawnGridPanel;
  
  public SimulationPanel() {
    log.trace("SimulationPanel");
    
    running = new SimpleObjectProperty<>();
    lawnCellPanels = new HashMap<>();
    
    SIMULATION_PANEL.load(this);
  }
  
  @Override
  public void initialize(URL location, ResourceBundle resources) {
    log.trace("initialize - location:{}, resources:{}", location, resources);
    
    var minSize = Bindings.min(widthProperty(), heightProperty())
                          .map(Number::intValue);
    lawnGridPanel.maxHeightProperty()
                 .bind(minSize);
    lawnGridPanel.maxWidthProperty()
                 .bind(minSize);
  }
  
  public Map<Coordinates, LawnCellPanel> getLawnCellPanels() {
    return lawnCellPanels;
  }
  
  public Output getOutput() {
    return output;
  }
  
  public SimpleObjectProperty<Future<Output>> runningProperty() {
    return running;
  }
  
  public void start() {
    log.trace("start");
    
    output = null;
    running.set(executorService.submit(program));
    executorService.submit(() -> {
      try {
        output = running.get()
                        .get();
      } catch (InterruptedException | ExecutionException e) {
        log.error(e.getMessage());
      }
      running.set(null);
    });
  }
  
  public void stop() {
    log.trace("stop");
    
    running.get()
           .cancel(true);
  }
  
  private void layout(Program program) {
    log.trace("update - program:{}", program);
    
    this.program = program;
    var newSize = program.lawnSize();
    
    RowConstraints rowConstraints = new RowConstraints();
    rowConstraints.setPercentHeight(100d / newSize.height());
    rowConstraints.setFillHeight(true);
    rowConstraints.setVgrow(Priority.ALWAYS);
    for (int row = 0; row < newSize.height(); row++) {
      lawnGridPanel.getRowConstraints()
                   .add(row, rowConstraints);
    }
    
    ColumnConstraints columnConstraints = new ColumnConstraints();
    columnConstraints.setPercentWidth(100d / newSize.width());
    columnConstraints.setFillWidth(true);
    columnConstraints.setHgrow(Priority.ALWAYS);
    for (int column = 0; column < newSize.width(); column++) {
      lawnGridPanel.getColumnConstraints()
                   .add(column, columnConstraints);
    }
    
    for (int row = 0; row < newSize.height(); row++) {
      for (int column = 0; column < newSize.width(); column++) {
        var           coordinates   = new Coordinates(column, newSize.height() - row - 1);
        LawnCellPanel lawnCellPanel = new LawnCellPanel();
        lawnGridPanel.add(lawnCellPanel, column, row);
        lawnCellPanels.put(coordinates, lawnCellPanel);
      }
    }
  }
  
  @Override
  public void changed(ObservableValue<? extends Program> observable, Program oldProgram, Program newProgram) {
    log.trace("changed - oldProgram:{}, newProgram:{}", oldProgram, newProgram);
    
    if (oldProgram != null) {
      for (Mower mower : oldProgram.mowers()) {
        mower.removePositionListener(this);
      }
    }
    
    lawnCellPanels.clear();
    lawnGridPanel.setVisible(newProgram != null);
    lawnGridPanel.getChildren()
                 .clear();
    lawnGridPanel.getRowConstraints()
                 .clear();
    lawnGridPanel.getColumnConstraints()
                 .clear();
    
    if (newProgram != null) {
      layout(newProgram);
      for (Mower mower : program.mowers()) {
        mower.addPositionListener(this);
      }
    }
  }
  
  @Override
  public void onPositionChanged(Position oldPosition, Position newPosition) {
    log.trace("onPositionChanged - oldPosition:{}, newPosition:{}", oldPosition, newPosition);
    
    Optional.ofNullable(oldPosition)
            .ifPresent(position -> lawnCellPanels.get(position.coordinates())
                                                 .removeMower(position));
    Optional.ofNullable(newPosition)
            .ifPresent(position -> lawnCellPanels.get(position.coordinates())
                                                 .addMower(position));
  }
  
}
