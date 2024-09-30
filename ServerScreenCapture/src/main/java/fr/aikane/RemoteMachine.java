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
    private Socket socketReader;
    private Socket socketSender;

    public RemoteMachine(String identity) throws IOException {
        this.identity = identity;
        this.init();
    }

    public RemoteMachine(Socket socket) throws IOException {
        HashMap<String, String> commandes = this.getCommandes(this.readMessage(socket));
        String[] identity = this.getIds(commandes);
        socket.close();

        this.identity = identity[0];
        if(identity[1].equals(TYPE_SENDER))
            this.socketSender = socket;

        if(identity[1].equals(TYPE_READER))
            this.socketReader = socket;
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
        Thread image = new Thread(){
        while (!escape) {
                System.out.println("Client connecté");

                InputStream is = socketReader.getInputStream();
                //  OutputStream os = new FileOutputStream("image_recu_" + (new Date()).getTime() + ".png");

                BufferedImage image = ImageIO.read(is);
                ImageIcon icone = new ImageIcon(image);
                screen.setIcon(icone);
                screen.repaint();
                setSize(image.getWidth(), image.getHeight());
                ImageIO.write(image , "jpg", new File("img"+(new Date()).getTime()+".jpg"));

                is.close();
                repaint();
            }
        };
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

    public void setSocketReader(Socket socketReader) {
        this.socketReader = socketReader;
    }

    public void setSocketSender(Socket socketSender) {
        this.socketSender = socketSender;
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
}
