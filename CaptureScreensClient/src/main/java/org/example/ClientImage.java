package org.example;

import java.io.*;
import java.net.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Logger;


public class ClientImage {

    Logger logger = Logger.getLogger(ClientImage.class.getName());

    private final double identity = (Math.random()*1000000000);

    private static final int port = 1234;
    private static final String ip = "192.168.1.102" ;

    private static final int serverPort = 1236;

    public void getSocketImage() {
        try {
            if(this.socketImage == null || this.socketImage.isClosed())
                this.socketImage = new Socket(ip, port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void initSocketImage( ) {
        this.socketImage = socketImage;
    }

    private Socket socketImage;

    public ClientImage() {
        try {
            this.socketImage = new Socket(ip, port);
            this.socketImage.setKeepAlive(true);
//            this.sendMessage(""+identity);
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

    public void sendImage(BufferedImage image) throws IOException, NoSuchAlgorithmException {
//        this.socket = new Socket(ip, port);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "png", baos);
        byte[] imageData = baos.toByteArray();

        OutputStream os = socketImage.getOutputStream();
        os.write(getChecksum());
        os.write(imageData);
        os.flush();

        BufferedReader in = null;
            in = new BufferedReader(
                new InputStreamReader(
                        socketImage.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                System.out.printf(
                    " Sent from the server: %s\n",
                    line);
                System.out.println(line);
            }
        os.close();
        deleteImg();
    }

//
//    @Override
//    public void run() {
//        try {
//            String message = this.readMessage();
//            System.out.println(message);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }
    public String readMessage() throws IOException {
        String commandes = "";
        BufferedReader in = new BufferedReader(new InputStreamReader(this.socketImage.getInputStream()));
        String ligne;
        while ((ligne = in.readLine()) != null) {
            commandes += ligne +"\r";
        }
        in.close();
        return commandes;
    }


    public void sendMessage(String message) throws IOException {
        OutputStream os = this.socketImage.getOutputStream();
        os.write((message).getBytes(StandardCharsets.UTF_8));
        os.flush();
        os.close();
    }

    private byte[] getChecksum() throws NoSuchAlgorithmException {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] messageDigest = md.digest((identity+"").getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : messageDigest) {
                hexString.append(String.format("%02x", 0xff & b));
            }
            hexString.append(";");
            return hexString.toString().getBytes();
    }

    public static void deleteImg() {
        File repertoireCourant = new File(".");

        // Créer un filtre pour les fichiers .jpg
        FilenameFilter filtreJPG = new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.toLowerCase().endsWith(".png");
            }
        };

        // Lister tous les fichiers JPG
        File[] fichiersJPG = repertoireCourant.listFiles(filtreJPG);

        if (fichiersJPG != null && fichiersJPG.length > 0) {
            System.out.println("Suppression des fichiers JPG :");
            int compteur = 0;

            for (File fichier : fichiersJPG) {
                if (fichier.delete()) {
                    System.out.println("  - Fichier supprimé : " + fichier.getName());
                    compteur++;
                } else {
                    System.out.println("  - Impossible de supprimer : " + fichier.getName());
                }
            }

            System.out.println("\nTotal: " + compteur + " fichier(s) JPG supprimé(s).");
        } else {
            System.out.println("Aucun fichier JPG trouvé dans le répertoire courant.");
        }
    }
}