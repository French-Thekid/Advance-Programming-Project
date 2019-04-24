package gui;

import controller.CustomerLiveChatController;


public class CustomerLiveChatView extends LiveChatView {

    private CustomerLiveChatController controller;

    public CustomerLiveChatView(){
        super();
        controller = new CustomerLiveChatController(table, currentUserLabel, textArea, messageTextField);

    }

    @Override
    public void startLiveChat(){
        model = controller.getAvailableFarmers();
        table.setModel(model);
        currentUserLabel.setText("");
        textArea.setText("");
        setVisible(true);
    }

    @Override
    protected void send() {
        controller.sendMessage();
        System.out.println("clicked");
    }

    @Override
    protected void onTableMouseClick(){
        int selectedRowIndex = table.getSelectedRow();
        controller.changeCurrentUser((String) table.getModel().getValueAt(selectedRowIndex, 0));
    }

    @Override
    public void stopLiveChat() {
        setVisible(false);
        controller.closeLiveChat();
    }


}
