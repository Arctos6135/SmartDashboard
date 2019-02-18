package edu.wpi.first.smartdashboard.gui.elements;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import edu.wpi.first.smartdashboard.ArctosLabel;
import edu.wpi.first.smartdashboard.gui.elements.bindings.AbstractTableWidget;
import edu.wpi.first.smartdashboard.livewindow.elements.Controller;
import edu.wpi.first.smartdashboard.livewindow.elements.NameTag;
import edu.wpi.first.smartdashboard.properties.Property;
import edu.wpi.first.smartdashboard.types.DataType;
import edu.wpi.first.smartdashboard.types.named.PIDType;

/**
 * @author Joe Grinstead
 */
public class PIDEditor extends AbstractTableWidget implements Controller {

  public static final DataType[] TYPES = {PIDType.get()};

  private static final String[] pidTypes = {"Displacement", "Rate"}; // kDisplacement, kRate

  private final boolean editType;

  private NumberTableComboBox tBox;
  private NumberTableField pField;
  private NumberTableField iField;
  private NumberTableField dField;
  private NumberTableField fField;
  private NumberTableField sField;
  private BooleanTableCheckBox eBox;
  private ArctosLabel tLabel;
  private ArctosLabel pLabel;
  private ArctosLabel iLabel;
  private ArctosLabel dLabel;
  private ArctosLabel fLabel;
  private ArctosLabel sLabel;
  private ArctosLabel eLabel;

  public PIDEditor() {
    this(true);
  }

  public PIDEditor(boolean editType) { //TODO alert user when the robot is about reset modified
    // PID values
    this.editType = editType;
  }

  @Override
  public void init() {
    setLayout(new GridBagLayout());

    if (editType) {
      tLabel = new ArctosLabel("PID type: ");
    }
    pLabel = new ArctosLabel("P:");
    iLabel = new ArctosLabel("I:");
    dLabel = new ArctosLabel("D:");
    fLabel = new ArctosLabel("F:");
    sLabel = new ArctosLabel("Setpoint:");
    eLabel = new ArctosLabel("Enabled:");
    if (editType) {
      tLabel.setHorizontalAlignment(ArctosLabel.RIGHT);
    }
    pLabel.setHorizontalAlignment(ArctosLabel.RIGHT);
    iLabel.setHorizontalAlignment(ArctosLabel.RIGHT);
    dLabel.setHorizontalAlignment(ArctosLabel.RIGHT);
    fLabel.setHorizontalAlignment(ArctosLabel.RIGHT);
    sLabel.setHorizontalAlignment(ArctosLabel.RIGHT);
    eLabel.setHorizontalAlignment(ArctosLabel.RIGHT);
    if (editType) {
      tBox = new NumberTableComboBox<>("PID Type", pidTypes);
    }
    pField = new NumberTableField("p");
    iField = new NumberTableField("i");
    dField = new NumberTableField("d");
    fField = new NumberTableField("f");
    sField = new NumberTableField("setpoint");
    eBox = new BooleanTableCheckBox("enabled");

    int columns = 10;
    pField.setColumns(columns);
    iField.setColumns(columns);
    dField.setColumns(columns);
    fField.setColumns(columns);
    sField.setColumns(columns);

    if (editType) {
      System.out.println("tbox=" + tBox);
      System.out.println("table=" + table);
      tBox.addActionListener(e -> table.putNumber("PID Type", tBox.getSelectedIndex()));
    }

    GridBagConstraints c = new GridBagConstraints();


    c.gridy = 1;
    if (editType) {
      add(tLabel, c);
    }
    c.gridy = 2;
    add(pLabel, c);
    c.gridy = 3;
    add(iLabel, c);
    c.gridy = 4;
    add(dLabel, c);
    c.gridy = 5;
    add(fLabel, c);
    c.gridy = 6;
    add(sLabel, c);
    c.gridy = 7;
    add(eLabel, c);

    c.gridx = 1;
    c.weightx = 1.0;
    c.gridy = 0;
    c.fill = GridBagConstraints.HORIZONTAL;
    add(nameTag = new NameTag(""), c);
    nameTag.setHorizontalAlignment(ArctosLabel.LEFT);
    nameTag.setText(getFieldName());
    c.gridy = 1;
    if (editType) {
      add(tBox, c);
    }
    c.gridy = 2;
    add(pField, c);
    c.gridy = 3;
    add(iField, c);
    c.gridy = 4;
    add(dField, c);
    c.gridy = 5;
    add(fField, c);
    c.gridy = 6;
    add(sField, c);
    c.gridy = 7;
    add(eBox, c);

    setMaximumSize(new Dimension(Integer.MAX_VALUE, getPreferredSize().height));

    revalidate();
    repaint();
  }

  @Override
  public void propertyChanged(Property property) {
  }

  @Override
  public void reset() {
    eBox.setBindableValue(false);
  }
}
