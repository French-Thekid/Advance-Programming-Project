package controller;

import communication.Request;
import communication.RequestAction;
import communication.Response;
import model.TextMessage;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.io.IOException;

public class FarmerLiveChatController extends LiveChatController {

    public FarmerLiveChatController(JTable table, JLabel label, JTextArea textArea, JTextField textField) {
        super(table, label, textArea, textField);
    }

    public DefaultTableModel startFarmerLiveChat(){
        DefaultTableModel model = new DefaultTableModel(new Object[]{"Customers"}, 0);
        Response<Boolean> response = requestSender.startFarmerLiveChat();
        if(requestSender.responseCheck(response)){
            if(response.getData()){
                System.out.println("Farmer Live Chat Started");
            }else {
                System.out.println("Error starting farmer live chat");
            }
        }else{
            System.out.println("Error starting farmer live chat");
        }

        t = new Thread(this);
        t.start();

        return model;
    }

    @Override
    public void run() {
        String action = "";
        try{
            while(!action.equals(RequestAction.END_LIVE_CHAT)){
                Request request = (Request) requestSender.readObject();
                action = request.getAction();
                switch (action){
                    case RequestAction.RECEIVE_MESSAGE:
                        receiveRequest(action);
                        TextMessage m = (TextMessage) request.getData();
                        receiveMessage(m);
                        break;

                    case RequestAction.REMOVE_FROM_ONLINE_LIST:
                        receiveRequest(action);
                        String customerNameREMOVE = (String) request.getData();
                        removeUser(customerNameREMOVE);
                        break;

                    case RequestAction.HANDLE_SEND_MESSAGE_RESPONSE:
                        receiveRequest(action);
                        Response<Boolean> response = (Response<Boolean>) request.getData();
                        handleSendMessageResponse(response);
                        break;

                }
            }
            receiveRequest(action);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        }finally {
            System.out.println("thread closed");
        }
    }
}
