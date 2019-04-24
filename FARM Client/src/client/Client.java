package client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Client {
    protected static ObjectOutputStream os;
    protected static ObjectInputStream is;
    private Socket connection;
    
    public Client(){
        
    }
    
    public Client(String host, int port){
        initConnection(host, port);
    }

    private boolean initConnection(String host, int port) {
        closeConnection();
        try{
            connection = new Socket(host, port);
            initConnectionStreams();
            System.out.println("connection successful");
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void initConnectionStreams() throws IOException {
        if (connection != null && connection.isConnected()){
            os = new ObjectOutputStream(connection.getOutputStream());
            is = new ObjectInputStream(connection.getInputStream());
        }
    }

    protected void closeConnection() {
        if(connection != null){
            try{
                closeConnectionStreams();
                connection.close();
                connection = null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void closeConnectionStreams() throws IOException {
        if(os != null) os.close();
        if(is != null) is.close();
    }
}
