package controller;

import client.RequestSender;
import communication.Request;
import communication.RequestAction;
import communication.Response;
import model.TextMessage;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.*;

public abstract class LiveChatController implements Runnable {

    protected JTable table;
    protected JLabel currentUserLabel;
    protected JTextArea textArea;
    protected JTextField textField;
    protected HashMap<String, LinkedList<String>> messages = new LinkedHashMap<>();
    protected static final Color unread = Color.GREEN;
    protected static final Color read = Color.black;
    private HashSet<String> unReadMessages = new HashSet<>();


    protected Thread t;// = new Thread(this);
    protected RequestSender requestSender = RequestSender.getInstance();


    public LiveChatController(JTable table, JLabel label, JTextArea textArea, JTextField textField){
        this.table = table;
        this.currentUserLabel = label;
        this.textArea = textArea;
        this.textField = textField;
    }

    protected void addUserToMessages(String user){
        if(!messages.containsKey(user))
            messages.put(user, new LinkedList<>());
    }

    protected void addMessageToMessages(String user, TextMessage message, boolean sender){
        if(!messages.containsKey(user) || (userNameToRowNum(user) == -1))
            addUser(user);



        messages.get(user).add(message.getFormattedMessage(sender));
    }

    protected void addUser(String userName){
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        addUserToMessages(userName);
        model.addRow(new Object[]{userName});
    }

    protected void removeUser(String userName){
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        int index = userNameToRowNum(userName);
        if(index != -1){
            if(currentUserLabel.getText().equals(userName)){
                currentUserLabel.setText("");
                textArea.setText("");
            }
            removeFromHashSet(userName);
            model.removeRow(index);
        }
    }

    protected int userNameToRowNum(String userName){
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        Vector<Vector> outVector = model.getDataVector();
        int i = 0;
        for(Vector v : outVector){
            if(userName.equals(v.get(0))){
                return i;
            }
            i++;
        }
        return -1;
    }

    public void changeCurrentUser(String userName){
        currentUserLabel.setText(userName);
        updateTextArea(userName);
        markAsRead(userName);
    }

    protected void updateTextArea(String userName){
        StringBuilder messageLog = new StringBuilder("");

        for(String m : messages.get(userName)){
            messageLog.append(m);
            messageLog.append("\n");
        }
        textArea.setText(messageLog.toString());
    }

    public void sendMessage(){
        if(textField.getText().equals("")) {
            System.out.println("empty message");
        }else if(currentUserLabel.getText().equals("")){
            System.out.println("no recipient");
        }else if(!t.isAlive()){
            System.out.println("not connected to live chat serer");
        }else{
            TextMessage message = new TextMessage(currentUserLabel.getText(), textField.getText());
            textField.setText("");
            addMessageToMessages(currentUserLabel.getText(), message, true);
            updateTextArea(currentUserLabel.getText());
            sendMessage(message);
        }
    }

    private void sendMessage(TextMessage message){
        Request<TextMessage> request = new Request<>(RequestAction.FORWARD_MESSAGE, message);

        if(requestSender.writeObject(request)){
            System.out.println("Request: "+request.getAction()+" sent");
        }
    }

    protected void handleSendMessageResponse(Response<Boolean> response){
        if(requestSender.responseCheck(response)){
            if(response.getData()){
                System.out.println("Message Sent");
            }else{
                System.out.println("Error sending message");
            }
        }else if(response.getErrorMessage().equals(Response.USER_OFFLINE)){
            System.out.println("User not available for chat");
        }else{
            System.out.println("Error sending message");
        }
    }

    protected void receiveMessage(TextMessage message){
        addMessageToMessages(message.getSender(), message, false);
        if(currentUserLabel.getText().equals(message.getSender())) {
            updateTextArea(message.getSender());
        }else{
            markAsUnread(message.getSender());
        }
    }


    protected void receiveRequest(String request){
        System.out.println("Request: "+request+" received");
    }



    public void closeLiveChat(){
        if(t != null && t.isAlive()) {
            requestSender.endLiveChat();
            System.out.println("attempted to end thread");
        }
        int i = 0;
        while(t != null && t.isAlive()){
            System.out.println("still alive "+i); i++;
            //t.interrupt();
        }
        System.out.println("thread is not alive");
        t = null;
        //requestSender.endLiveChat();
    }

    private void markAsUnread(String userName){
        unReadMessages.add(userName);
        onHashSetChange();
    }

    private void markAsRead(String userName){
        removeFromHashSet(userName);
        onHashSetChange();
    }

    private void removeFromHashSet(String userName){
        unReadMessages.remove(userName);
    }

    private void onHashSetChange(){
        HashSet<Integer> row = new HashSet<>();
        Iterator<String> itr = unReadMessages.iterator();
        while(itr.hasNext()){
            row.add(userNameToRowNum(itr.next()));
        }
        table.getColumnModel().getColumn(0).setCellRenderer(new CellRenderer(row));
        table.repaint();
    }

    private class CellRenderer extends DefaultTableCellRenderer {

        private static final long serialVersionUID = 1L;
        private HashSet<Integer> row;

        public CellRenderer(HashSet<Integer> row){
            this.row = row;
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row,
                                                       int column) {
            super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            if(this.row.contains(row)) {
                this.setFont(this.getFont().deriveFont(Font.BOLD));
            }

            return this;
        }
    }

}
