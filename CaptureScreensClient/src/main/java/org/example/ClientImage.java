package org.example;

import java.io.*;
import java.net.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

public class ClientImage {
    public static void sendImage(BufferedImage image) throws IOException {
        Socket socket = new Socket("localhost", 1234);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "png", baos);
        byte[] imageData = baos.toByteArray();

        OutputStream os = socket.getOutputStream();
        os.write(imageData);
        os.flush();

        os.close();
        socket.close();
    }
}