package edu.wpi.first.smartdashboard.gui.elements;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

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
  private BooleanIndicator valueField;
  private boolean value;
  private JLabel nameLabel;

  private static class BooleanIndicator extends JPanel {
    private Color color;
    
    public void setColor(Color color) {
      this.color = color;
    }

    @Override
    protected void paintComponent(Graphics g) {
      super.paintComponent(g);
      Graphics2D g2d = (Graphics2D) g;
      g.setColor(color);
      g2d.fillOval(0, 0, getWidth(), getHeight());
      g.setColor(Color.BLACK);
      g2d.drawOval(0, 0, getWidth(), getHeight());
    }
  }

  public void init() {
    setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

    nameLabel = new JLabel(getFieldName());
    
    valueField = new BooleanIndicator();
    valueField.setPreferredSize(new Dimension(10, 10));
    valueField.setMinimumSize(new Dimension(10, 10));
    valueField.setMaximumSize(new Dimension(10, 10));
    setValue(false);

    fontSize.setValue(nameLabel.getFont().getSize());

    add(valueField);
    add(nameLabel);
    revalidate();
    repaint();
  }

  @Override
  public void setValue(final boolean value) {
    this.value = value;
    valueField.setColor(value ? colorOnTrue.getValue() : colorOnFalse.getValue());
    repaint();
  }

  @Override
  public void propertyChanged(Property property) {
    if(property == fontSize) {
      if(nameLabel != null)
        nameLabel.setFont(nameLabel.getFont().deriveFont((float) fontSize.getValue()));
      if(valueField != null) {
        valueField.setPreferredSize(new Dimension(fontSize.getValue(), fontSize.getValue()));
        valueField.setMaximumSize(new Dimension(fontSize.getValue(), fontSize.getValue()));
        valueField.setMinimumSize(new Dimension(fontSize.getValue(), fontSize.getValue()));
      }
    }
    setValue(value); //force background change
  }
}
