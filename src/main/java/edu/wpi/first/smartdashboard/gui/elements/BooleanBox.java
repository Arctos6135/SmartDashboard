package edu.wpi.first.smartdashboard.gui.elements;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import edu.wpi.first.smartdashboard.gui.elements.bindings.AbstractValueWidget;
import edu.wpi.first.smartdashboard.properties.ColorProperty;
import edu.wpi.first.smartdashboard.properties.IntegerProperty;
import edu.wpi.first.smartdashboard.properties.Property;
import edu.wpi.first.smartdashboard.types.DataType;

/**
 * Implements a simple text box UI element with a name label.
 *
 * @author pmalmsten
 */
public class BooleanBox extends AbstractValueWidget {

  public static final DataType[] TYPES = {DataType.BOOLEAN};
  public final ColorProperty colorOnTrue
      = new ColorProperty(this, "Color to show when true", Color.GREEN);
  public final ColorProperty colorOnFalse
      = new ColorProperty(this, "Color to show when false", Color.RED);
  public final IntegerProperty fontSize = new IntegerProperty(this, "Font Size");
  private JPanel valueField;
  private boolean value;
  private JLabel nameLabel;

  public void init() {
    setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

    nameLabel = new JLabel(getFieldName());
    
    valueField = new JPanel();
    valueField.setPreferredSize(new Dimension(10, 10));

    add(valueField);
    add(nameLabel);
    revalidate();
    repaint();
  }

  @Override
  public void setValue(final boolean value) {
    this.value = value;
    valueField.setBackground(value ? colorOnTrue.getValue() : colorOnFalse.getValue());
    repaint();
  }

  @Override
  public void propertyChanged(Property property) {
    if(property == fontSize) {
      if(nameLabel != null)
        nameLabel.setFont(nameLabel.getFont().deriveFont((float) fontSize.getValue()));
      if(valueField != null)
        valueField.setPreferredSize(new Dimension(fontSize.getValue(), fontSize.getValue()));
    }
    setValue(value); //force background change
  }
}
