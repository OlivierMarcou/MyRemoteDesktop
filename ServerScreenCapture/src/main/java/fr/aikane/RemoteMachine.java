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

    public static final String IDENTITY = "identity";
    public static final String COMANDE_TYPE = "type";
    public static final String TYPE_SENDER = "sender";
    public static final String TYPE_READER = "reader";
    private  static boolean escape = false;

    private String identity;
    private Socket socket;


    public RemoteMachine(Socket socket) throws IOException {
        HashMap<String, String> commandes = this.getCommandes(this.readMessage(socket));
        String[] identity = this.getIds(commandes);
        socket.close();
        this.identity = identity[0];
            this.socket = socket;
        this.init();
    }

    public void init() throws IOException {
        this.setVisible(true);
        this.setSize(200,200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        addKeyListener(this);
        JLabel screen = new JLabel();
        this.add(screen);
        this.setVisible(true);
        Thread thread = new Thread(){
            public void run() {
                while (!escape) {
                    InputStream is = null;
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
                    screen.setIcon(icone);
                    screen.repaint();
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
            }
        };
        thread.start();
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

    private String readMessage(Socket socket) throws IOException {
        String message = "";
        InputStream in = socket.getInputStream();
        message = Arrays.toString(in.readAllBytes());
        return message;
    }

    private HashMap<String, String> getCommandes(String message){
        HashMap<String, String> commandes = new HashMap<>();
        String[] commandesLines = message.split(";");
        Arrays.stream(commandesLines).forEach(v -> commandes.put(v.split(":")[0], v.split(":")[1]));
        return commandes;
    }

    private String[] getIds(HashMap<String, String> commandes ){
        if(!commandes.containsKey(IDENTITY))
            return null;
        String[] ids = new String[2];
        ids[0] = commandes.get(IDENTITY);
        ids[1] = commandes.get(COMANDE_TYPE);
        return ids;
    }

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }
}
