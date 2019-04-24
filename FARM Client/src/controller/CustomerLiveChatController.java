package controller;

import communication.*;
import model.TextMessage;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.io.IOException;

public class CustomerLiveChatController extends LiveChatController {

    public CustomerLiveChatController(JTable table, JLabel label, JTextArea textArea, JTextField textField){
        super(table, label, textArea, textField);
    }

    public DefaultTableModel getAvailableFarmers(){
        DefaultTableModel model = new DefaultTableModel(new Object[]{"Online Farmers"}, 0);
        Response<String[]> response = requestSender.getFarmersAvailableForChat();
        if(requestSender.responseCheck(response)){
            for(String farmer : response.getData()){
                addUserToMessages(farmer);
                model.addRow(new Object[]{farmer});
            }
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
                    case RequestAction.ADD_TO_ONLINE_LIST:
                        receiveRequest(action);
                        String farmerNameADD = (String) request.getData();
                        addUser(farmerNameADD);
                        break;
                    case RequestAction.REMOVE_FROM_ONLINE_LIST:
                        receiveRequest(action);
                        String farmerNameREMOVE = (String) request.getData();
                        removeUser(farmerNameREMOVE);
                        break;
                    case RequestAction.RECEIVE_MESSAGE:
                        receiveRequest(action);
                        TextMessage m = (TextMessage) request.getData();
                        receiveMessage(m);
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
