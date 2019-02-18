package edu.wpi.first.smartdashboard;

import java.awt.Font;
import java.awt.geom.AffineTransform;

import javax.swing.JLabel;

public class ArctosLabel extends JLabel {
    
    public static Font lemonMilk;
    static {
        try {
            AffineTransform squish = AffineTransform.getScaleInstance(0.75, 1);
            lemonMilk = Font.createFont(Font.TRUETYPE_FONT, ClassLoader.getSystemClassLoader().getResourceAsStream("lemon_milk/LemonMilklight.ttf"))
                    .deriveFont(12.0f).deriveFont(squish);
        }
        catch(Exception e) {
            lemonMilk = new Font("Comic Sans MS", Font.BOLD, 12);
        }
    }
    
    public ArctosLabel() {
        super();
        setFont(lemonMilk);
    }
    public ArctosLabel(String str) {
        super(str);
        setFont(lemonMilk);
    }
    public ArctosLabel(String str, int horizontalAlignment) {
        super(str, horizontalAlignment);
        setFont(lemonMilk);
    }
}
