package edu.wpi.first.smartdashboard.gui.elements;

import java.awt.Font;

import edu.wpi.first.smartdashboard.properties.IntegerProperty;
import edu.wpi.first.smartdashboard.properties.Property;

public class GoArctos extends Label {
  public static final String NAME = "Go Arctos";

  public final IntegerProperty howMuchGoArctos = new IntegerProperty(this, "How Much Go Arctos?", 1);

  public GoArctos() {
    super();
  }

  public static Font lemonMilk;
  static {
    try {
      lemonMilk = Font.createFont(Font.TRUETYPE_FONT,
          ClassLoader.getSystemClassLoader().getResourceAsStream("lemon_milk/LemonMilkbold.ttf")).deriveFont(30.0f);
    } catch (Exception e) {
      lemonMilk = new Font("Comic Sans MS", Font.BOLD, 12);
    }
  }

  @Override
  public void init() {
    super.init();
    label.setFont(lemonMilk);
    text.setValue("GO ARCTOS");
    propertyChanged(text);
  }

  @Override
  public void propertyChanged(Property property) {
    super.propertyChanged(property);
    if (property.equals(howMuchGoArctos)) {
      int howMuch = howMuchGoArctos.getValue();
      StringBuilder goArctosBuilder = new StringBuilder();

      for (int i = 0; i < howMuch; i++) {
        goArctosBuilder.append("GO ARCTOS ");
      }

      text.setValue(goArctosBuilder.toString());
      propertyChanged(text);
    }
  }
}
