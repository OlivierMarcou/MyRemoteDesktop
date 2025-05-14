package org.example;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import static java.awt.event.KeyEvent.VK_SPACE;
import static java.lang.Thread.sleep;



//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {

    private static boolean running = true;
    public static void main(String [] args) throws IOException, AWTException, InterruptedException {
//        SimpleDateFormat sd = new SimpleDateFormat(".yyyy-MM-dd.");
//        System.out.println(sd.format(new Date()));
       // deplacerSouris() ;
        ClientImage clientImage = new ClientImage();

                CaptureEcran cap = new CaptureEcran();
                    while(running) {
                        try {
                             BufferedImage img = cap.capture(0);                                clientImage.getSocketImage();
                                clientImage.sendImage(img);
                                ClientImage.deleteImg();

                            sleep(500);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        finally {
                            ClientImage.deleteImg();
                        }
                }

        }

      //  messageThread.start();


//        Thread threadLecteur = new Thread(() -> {
//             if (User32.INSTANCE.GetAsyncKeyState(VK_SPACE) < 0) {
//                 running=false;
//                 System.out.println("La touche Espace est pressée");
//             }
//        });

        // Démarrage du thread
//        threadLecteur.start();
      //  clientImage.close();

    public static void  deplacerSouris() throws AWTException {
        Robot robot = new Robot();
        // Déplacement du curseur à la position (500, 300)
        robot.mouseMove(500, 300);
    }
}