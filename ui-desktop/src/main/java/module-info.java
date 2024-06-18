open module mowitnow.ui.desktop {
  exports tech.edwyn.mowitnow.desktop;
  exports tech.edwyn.mowitnow.desktop.ui;
  exports tech.edwyn.mowitnow.desktop.services;
  requires mowitnow.domain;
  requires transitive javafx.controls;
  requires transitive javafx.fxml;
  requires org.slf4j;
}
