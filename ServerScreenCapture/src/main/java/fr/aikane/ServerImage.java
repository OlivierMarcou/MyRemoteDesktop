package fr.aikane;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class ServerImage {



    private ServerSocket serverSocket;
    private HashMap<String,RemoteMachine> remoteClient = new HashMap<>();

    public static void main(String[] args) throws IOException{
        ServerImage serverImage = new ServerImage();
        serverImage.run();
    }

    public void init() throws IOException {
        this.serverSocket = new ServerSocket(1234);
        System.out.println("Serveur en attente de connexion...");

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
        this.run();
    }

    public void run() throws IOException {

        Socket socket = serverSocket.accept();

        socket = serverSocket.accept();
        serverSocket.close();
    }

}