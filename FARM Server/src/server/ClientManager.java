package server;

import communication.Response;
import model.TextMessage;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ClientManager {

    private Map<String, ClientConnection> clients = null;
    private static ClientManager instance = null;

    public static ClientManager getInstance(){
        if(instance == null)
            instance = new ClientManager();
        return instance;
    }

    private ClientManager(){
        clients = new HashMap<>();
    }

    public void addClient(ClientConnection con){
        String userName = "";
        do{
            userName = generateRandomName();

        }while (clients.containsKey(userName));

        con.setUserName(userName);
        clients.put(userName, con);
    }


    public void removeClient(String userName){
        clients.remove(userName);
    }

    public ClientConnection getClient(String userName){
        return clients.get(userName);
    }

    public boolean updateClientUsername(String oldUsername, String newUsername){
        if(clients.containsKey(oldUsername) && !clients.containsKey(newUsername)){
            ClientConnection temp = clients.get(oldUsername);
            clients.remove(oldUsername);
            clients.put(newUsername, temp);
            System.out.println("Username updated from "+oldUsername+" to "+newUsername);
            return true;
        }
        System.out.println("Error updating usernames");
        return  false;
    }


    public boolean userExists(String receiver){
        return clients.containsKey(receiver);
    }

    public String[] getFarmersAvailableForChat(){
        LinkedList<String> list = new LinkedList<>();
        for(ClientConnection c : clients.values()){
            if(c.isAvailableForLiveChat())
                list.add(c.getUserName());
        }

        return (String[]) list.toArray(new String[list.size()]);
    }

    private String generateRandomName(){
        return (Math.random()*4000)+"";
    }

    public void updateFarmerOnlineList(String userName, boolean availableForLiveChat) {
        for(ClientConnection c : clients.values()){
            if(c.isLiveChatMode())
                c.updateOnlineList(userName, availableForLiveChat);
        }
    }

    public void updateCustomerOnlineList(List<String> farmers, String customer){
        for(String farmer : farmers){
            if(clients.containsKey(farmer)){
                ClientConnection c = clients.get(farmer);
                if(c.isAvailableForLiveChat())
                    c.removeFromOnlineList(customer);
            }
        }
    }

    public Response<Boolean> forwardMessage(TextMessage text) {
        Response<Boolean> response = new Response<>(false, Response.GENERAL_ERROR);
        if(!clients.containsKey(text.getRecipient())){
            response.setErrorMessage(Response.USER_OFFLINE);
        }else{
            ClientConnection c = clients.get(text.getRecipient());
            if( (c.getUserType() == ClientConnection.FARMER && c.isAvailableForLiveChat()) || (c.getUserType() == ClientConnection.CUSTOMER && c.isLiveChatMode())) {
                c.sendMessage(text);
                response.setData(true);
                response.setErrorMessage(Response.NO_ERROR);
            }else{
                response.setErrorMessage(Response.USER_OFFLINE);
            }
        }


        return response;
    }
}
