package fr.aikane;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

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

    private ServerSocket serverSocket;
    private HashMap<String,Socket> socketList = new HashMap<>();

    public static void main(String[] args) throws IOException{
        ServerImage serverImage = new ServerImage();
        serverImage.run();
    }
    public void init() throws IOException {

        this.serverSocket = new ServerSocket(1234);
        System.out.println("Serveur en attente de connexion...");

        JLabel screen = new JLabel();
        this.add(screen);
        this.setVisible(true);
        this.run();

    }
    public void run() throws IOException {

        Socket socket = serverSocket.accept();
        String Identity = this.readMessage(socket);
        socketList.put(identity, socket);
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
        return commandes;
    }
}