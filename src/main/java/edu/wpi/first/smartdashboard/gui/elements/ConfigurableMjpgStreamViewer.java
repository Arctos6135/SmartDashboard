package edu.wpi.first.smartdashboard.gui.elements;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import edu.wpi.first.smartdashboard.properties.Property;
import edu.wpi.first.smartdashboard.properties.StringProperty;
import edu.wpi.first.smartdashboard.robot.Robot;
import edu.wpi.first.wpilibj.tables.ITable;

public class ConfigurableMjpgStreamViewer extends MjpgStreamViewerImpl {
  static int viewerCount = 0;
  static final String NAME = "Configurable MJPG Stream Viewer";

  public final StringProperty nameProperty = new StringProperty(this, "Name", "stream" + viewerCount++);

  public static BufferedImage noConnectionImage;
  static {
    try {
      var imgStream = ClassLoader.getSystemClassLoader().getResourceAsStream("arctos4.gif");
      noConnectionImage = ImageIO.read(imgStream);
    }
    catch(Exception e) {
      System.out.println("I don't care");
    }
  }
  
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

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    if(imageToDraw == null) {
      g.drawImage(noConnectionImage, 0, 0, getWidth(), getHeight(), null);
    }
  }
}
