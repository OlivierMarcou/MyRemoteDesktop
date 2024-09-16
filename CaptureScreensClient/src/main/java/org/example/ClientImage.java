package org.example;

import java.io.*;
import java.net.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.nio.charset.StandardCharsets;
import java.util.logging.Logger;


public class ClientImage extends Thread {

    Logger logger = Logger.getLogger(ClientImage.class.getName());

    private static final int port = 1234;
    private static final String ip = "localhost" ;

    private Socket socket ;

    public ClientImage() {
        try {
            this.socket = new Socket(ip, port);
        } catch (IOException e) {
            logger.severe(e.getMessage());
        }
    }

    public void close(){
        if(!this.socket.isClosed()) {
            try {
                this.socket.close();
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

        OutputStream os = socket.getOutputStream();
        os.write("image".getBytes(StandardCharsets.UTF_8));
        os.flush();
        os.write(imageData);
        os.flush();

        os.close();
    }


    public void readMessage(String message) throws IOException {
        OutputStream os = socket.getOutputStream();

        os.write("message".getBytes(StandardCharsets.UTF_8));
        os.flush();
        os.write(message.getBytes(StandardCharsets.UTF_8));
        os.flush();

        os.close();
    }
}