package fr.aikane;

import java.net.Socket;

public class DualConnexion {

    private String Identity;
    private Socket socketReader;
    private Socket socketSender;

    public DualConnexion(String identity){
        this.Identity = identity;
    }

    public DualConnexion(String Identity, Socket socketReader, Socket socketSender) {
        this.Identity = Identity;
        this.socketReader = socketReader;
        this.socketSender = socketSender;
    }

    public void setSocketReader(Socket socketReader) {
        this.socketReader = socketReader;
    }

    public void setSocketSender(Socket socketSender) {
        this.socketSender = socketSender;
    }
}
