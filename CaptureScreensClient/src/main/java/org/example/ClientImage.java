package org.example;

import java.io.*;
import java.net.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Random;
import java.util.logging.Logger;


public class ClientImage extends Thread {

    Logger logger = Logger.getLogger(ClientImage.class.getName());

    private static final int port = 1234;
    private static final String ip = "localhost" ;

    private Socket socketImage;
    private Socket socketReadMessage;

    public ClientImage() {
        try {
            this.socketImage = new Socket(ip, port);
            this.sendMessage("identity:"+identity+";type:sender;");

            this.socketReadMessage = new Socket(ip, port);
            this.sendMessage("identity:"+identity+";type:reader;");

        } catch (IOException e) {
            logger.severe(e.getMessage());
        }
    }

    public void close(){
        if(!this.socketImage.isClosed()) {
            try {
                this.socketImage.close();
            } catch (IOException e) {
                logger.info(e.getMessage());
            }
        }
    }

    public void sendImage(BufferedImage image) throws IOException {
//        this.socket = new Socket(ip, port);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "png", baos);
        byte[] imageData = baos.toByteArray();

        OutputStream os = socketImage.getOutputStream();
        os.write("image".getBytes(StandardCharsets.UTF_8));
        os.flush();
        os.write(imageData);
        os.flush();

        os.close();
    }


    @Override
    public void run() {
        try {
            String message = this.readMessage();
            System.out.println(message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public String readMessage() throws IOException {
        String commandes = "";

        BufferedReader in = new BufferedReader(new InputStreamReader(this.socketReadMessage.getInputStream()));

        String ligne;
        while ((ligne = in.readLine()) != null) {
            commandes += ligne +"\r";
        }

        in.close();

        return commandes;
    }

    private final double identity = (Math.random()*1000000000);

    public void sendMessage(String message) throws IOException {
        OutputStream os = this.socketReadMessage.getOutputStream();


        os.write((message).getBytes(StandardCharsets.UTF_8));
        os.flush();

        os.close();
    }
}