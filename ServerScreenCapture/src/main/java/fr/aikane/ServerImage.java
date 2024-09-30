package fr.aikane;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
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
                        System.out.println("Client connecté");
                        RemoteMachine remoteMachine = null;
                        remoteMachine = new RemoteMachine(socketServer);
                        System.out.println("Client ids :" +remoteMachine.getIdentity());
                        if(remoteClients.get(remoteMachine.getIdentity()) == null){
                            remoteClients.put(remoteMachine.getIdentity(), remoteMachine);
                        }else {
                            if(socketServer != null){
                                RemoteMachine remoteMachine1 = remoteClients.get(remoteMachine.getIdentity());
                                remoteMachine1.setSocket(socketServer);
                            }
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        };
        getConnexions.start();
    }


}