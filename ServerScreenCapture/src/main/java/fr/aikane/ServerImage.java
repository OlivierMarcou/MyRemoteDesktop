package fr.aikane;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.HashMap;

public class ServerImage {



    private  static boolean escape = false;

    private ServerSocket serverSocket;
    private HashMap<String,RemoteMachine> remoteClients = new HashMap<>();

    public static void main(String[] args) throws IOException{
        ServerImage serverImage = new ServerImage();
        serverImage.init();
    }

    public void init() throws IOException {
        this.serverSocket = new ServerSocket(1234);
        System.out.println("Serveur en attente de connexion...");
        Thread getConnexions = new Thread(){
            public void run(){
                while (!escape) {
                    Socket socketServer = null;
                    try {
                        socketServer = serverSocket.accept();
                        System.out.println(socketServer.getInetAddress().getHostAddress());
                        System.out.println("Client connecté");
                        ServerImage.this.initRemote(socketServer);

                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        };
        getConnexions.start();
    }
    private void initRemote(Socket socket) throws IOException {
        String id = "";
        InputStream in = socket.getInputStream();
        char c = ' ';
        while(c != ';'){
            id += Character.toChars(in.read());
        }
        if(remoteClients != null && remoteClients.containsKey(id))
            remoteClients.get(id).writeImage(in);
        else{
            RemoteMachine remoteMachine =new RemoteMachine(socket,id);
            remoteMachine.writeImage(in);
            remoteClients.put(id, remoteMachine);

        }
    }

//    private HashMap<String, String> getCommandes(String message){
//        HashMap<String, String> commandes = new HashMap<>();
//        this.identity = message.substring(0,message.indexOf(";"));
//        Arrays.stream(commandesLines).forEach(v -> commandes.put(v.split(":")[0], v.split(":")[1]));
//        return commandes;
//    }
//
//    private String[] getIds(HashMap<String, String> commandes ){
//        if(!commandes.containsKey(IDENTITY))
//            return null;
//        String[] ids = new String[2];
//        ids[0] = commandes.get(IDENTITY);
//        ids[1] = commandes.get(COMANDE_TYPE);
//        return ids;
//    }

}