package edu.wpi.first.smartdashboard.gui.elements;

import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

public class ArctosLogo extends edu.wpi.first.smartdashboard.gui.elements.Image {

    public static BufferedImage arctosLogo;
    static {
        try {
          var imgStream = ClassLoader.getSystemClassLoader().getResourceAsStream("arctos1.PNG");
          arctosLogo = ImageIO.read(imgStream);
        }
        catch(Exception e) {
          System.out.println("I don't care");
        }
    }

    @Override
    public void init() {
        super.init();
        image = arctosLogo;

        aspectKept.setValue(true);
        propertyChanged(aspectKept);
        revalidate();
        repaint();
    }
}