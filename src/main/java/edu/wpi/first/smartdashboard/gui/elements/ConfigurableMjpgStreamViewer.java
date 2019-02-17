package edu.wpi.first.smartdashboard.gui.elements;

import edu.wpi.first.smartdashboard.properties.Property;
import edu.wpi.first.smartdashboard.properties.StringProperty;
import edu.wpi.first.wpilibj.tables.ITable;
import edu.wpi.first.wpilibj.tables.ITableListener;
import edu.wpi.first.smartdashboard.robot.Robot;

public class ConfigurableMjpgStreamViewer extends MjpgStreamViewerImpl {
  static int viewerCount = 0;
  static final String NAME = "Configurable MJPG Stream Viewer";

  public final StringProperty nameProperty = new StringProperty(this, "Name", "stream" + viewerCount++);
  
  @Override
  public void onPropertyChanged(Property property) {
    super.onPropertyChanged(property);
    if(property == nameProperty) {
      String newUrl = Robot.getTable().getString(nameProperty.getValue() + "-url", "");
      if(newUrl != "") {
        urlProperty.setValue(newUrl);
        onPropertyChanged(urlProperty);
      }
    }
  }
  @Override
  public void onInit() {
    super.onInit();
    
    ITable table = Robot.getTable();
    table.addTableListenerEx((source, key, value, isNew) -> {
      if(key.equals(nameProperty.getValue() + "-url")) {
        String newUrl = (String) value;
        urlProperty.setValue(newUrl);
        onPropertyChanged(urlProperty);
      }
    }, ITable.NOTIFY_IMMEDIATE | ITable.NOTIFY_NEW | ITable.NOTIFY_UPDATE);
  }
}
