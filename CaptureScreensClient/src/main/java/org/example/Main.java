package org.example;

import java.awt.Robot;
import java.awt.AWTException;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import static java.lang.Thread.sleep;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {

    public static void main(String [] args) throws IOException, AWTException, InterruptedException {
//        SimpleDateFormat sd = new SimpleDateFormat(".yyyy-MM-dd.");
//        System.out.println(sd.format(new Date()));
        deplacerSouris() ;
        CaptureEcran cap = new CaptureEcran();
        List<BufferedImage> images = cap.captureAll();
        images.forEach(v -> {
            try {
                ClientImage.sendImage(v);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        while(true) {
            ClientImage.sendImage(cap.capture(0));
            sleep(1000);
        }
    }
    public static void  deplacerSouris() throws AWTException {
        Robot robot = new Robot();

        // Déplacement du curseur à la position (500, 300)
        robot.mouseMove(500, 300);

    }


}