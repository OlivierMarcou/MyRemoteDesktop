package fr.aikane;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.*;
import java.util.Date;
import javax.imageio.ImageIO;
import javax.swing.*;

public class ServerImage extends JFrame implements KeyListener {

    private  static boolean escape = false;
    public ServerImage() {
        this.setVisible(true);
        this.setSize(200,200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        addKeyListener(this);
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode()
                == KeyEvent.VK_ESCAPE) {
            System.out.println("La touche Esc a été pressée !");
            this.escape = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    public static void main(String[] args) throws IOException, AWTException {
        ServerImage serverImage = new ServerImage();
        serverImage.run();
    }
    public void run() throws IOException {

        ServerSocket serverSocket = new ServerSocket(1234);
        System.out.println("Serveur en attente de connexion...");

        Socket socket = serverSocket.accept();

        JLabel screen = new JLabel();
        this.add(screen);
        this.setVisible(true);
        while (!escape) {
            System.out.println("Client connecté");

            InputStream is = socket.getInputStream();
          //  OutputStream os = new FileOutputStream("image_recu_" + (new Date()).getTime() + ".png");

            BufferedImage image = ImageIO.read(is);
            ImageIcon icone = new ImageIcon(image);
            screen.setIcon(icone);screen.repaint();
            this.setSize(image.getWidth(), image.getHeight());
            ImageIO.write(image , "jpg", new File("img"+(new Date()).getTime()+".jpg"));

            is.close();
            socket = serverSocket.accept();
            this.repaint();
        }
        socket.close();
        serverSocket.close();
    }
}