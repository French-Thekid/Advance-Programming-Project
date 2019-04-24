package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private ServerSocket servSocket;
    private Socket connection;
    private ClientManager clientManager;
    
    public Server(){
        new ServerView();
        clientManager = ClientManager.getInstance();
        setUpConection();
    }

    private void setUpConection() {
        try {
            servSocket = new ServerSocket(4000, 10);
            waitForRequests();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void waitForRequests() {
        System.out.println("Waiting for requests");
        try{
            while(true) {
                connection = servSocket.accept();
                System.out.println("connection received");
                clientManager.addClient(new ClientConnection(connection));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
