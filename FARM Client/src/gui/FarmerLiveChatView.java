package gui;

import controller.FarmerLiveChatController;
import model.Farmer;

public class FarmerLiveChatView extends LiveChatView {

    private FarmerLiveChatController controller;

    public FarmerLiveChatView(){
        super();
        controller = new FarmerLiveChatController(table, currentUserLabel, textArea, messageTextField);

    }
    @Override
    public void startLiveChat() {
        model = controller.startFarmerLiveChat();
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
    protected void onTableMouseClick() {
        int selectedRowIndex = table.getSelectedRow();
        controller.changeCurrentUser((String) table.getModel().getValueAt(selectedRowIndex, 0));
    }

    @Override
    public void stopLiveChat() {
        setVisible(false);
        controller.closeLiveChat();
    }
}
