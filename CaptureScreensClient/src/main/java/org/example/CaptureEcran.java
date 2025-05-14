package org.example;

import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CaptureEcran {

    private int screensNumber = 0;

    public CaptureEcran(){
        getScreens();
    }
    public BufferedImage capture(Integer screenNumber) throws AWTException, IOException {
        Robot robot = new Robot();
        GraphicsDevice[] screens = getScreens();
        Dimension screenSize = screens[screenNumber].getDefaultConfiguration().getBounds().getSize();

        Rectangle screenRectangle = new Rectangle(screenSize);
        BufferedImage image = robot.createScreenCapture(screenRectangle);
        String filename = "capture_"+screenNumber+"_"+(new Date()).getTime()+".png";
        File imageFile = new File(filename);
        ImageIO.write(image, "png", imageFile);
        return image;
    }

    public List<BufferedImage> captureAll(){
        List<BufferedImage> files = new ArrayList<>();
        for(int i=0; i<screensNumber+1;i++){
            try {
                files.add(capture(i));
            } catch (AWTException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return files;
    }

    public Integer howMuchScreens() {
        return this.screensNumber;
    }

    public GraphicsDevice[] getScreens() {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        this.screensNumber = ge.getScreenDevices().length;
        return ge.getScreenDevices();
    }

}