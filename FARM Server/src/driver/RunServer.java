package driver;

import database.FarmerTableManager;
import database.SQLProvider;
import server.Server;

public class RunServer {
    public static void main(String[] args){
        SQLProvider.getInstance();
        new Server();
    }
}
