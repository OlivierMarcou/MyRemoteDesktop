package fr.aikane;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;

public class RemoteMachine extends JFrame implements KeyListener {
    private String identity;
    public String getIdentity(){
        return identity;
    }
    public static final String IDENTITY = "identity";
    public static final String COMANDE_TYPE = "type";
    public static final String TYPE_SENDER = "sender";
    public static final String TYPE_READER = "reader";
    private  static boolean escape = false;

    private Socket socket;


    public RemoteMachine(Socket socket, String identity) throws IOException {
        this.identity = identity;
        this.socket = socket;
        this.init();
    }

    private JLabel screen = new JLabel();
    public void init() throws IOException {
        this.setVisible(true);
        this.setSize(200,200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        addKeyListener(this);
        this.add(screen);
        this.setVisible(true);
//        Thread thread = new Thread(){
//            public void run() {
//                while (!escape) {
//                    InputStream is = null;
//                    extracted(screen);
//                }
//            }
//        };
//        thread.start();
    }

    public void writeImage(InputStream is) {

        try {
            is = socket.getInputStream();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        //  OutputStream os = new FileOutputStream("image_recu_" + (new Date()).getTime() + ".png");
        BufferedImage image = null;
        try {
            image = ImageIO.read(is);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        ImageIcon icone = new ImageIcon(image);
        this.screen.setIcon(icone);
        this.screen.repaint();
        setSize(image.getWidth(), image.getHeight());
        try {
            ImageIO.write(image, "jpg", new File("img" + (new Date()).getTime() + ".jpg"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try {
            is.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        repaint();
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

    public void setSocket(Socket socket) {
        this.socket = socket;
    }


}
