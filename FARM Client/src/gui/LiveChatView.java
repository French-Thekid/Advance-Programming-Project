package gui;


import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public abstract class LiveChatView extends JPanel {
    protected JScrollPane scrollPane;
    protected JTable table = new JTable();
    protected DefaultTableModel model;
    protected JButton send = new JButton("Send");
    protected JTextField messageTextField = new JTextField();
    protected JTextArea textArea = new JTextArea(10, 70);
    protected JLabel currentUserLabel = new JLabel("");


    public LiveChatView(){
        initialise();
    }



    protected void initialise(){
        setVisible(false);
        setBackground(new Color(0.0f, 0.0f, 0.0f, 0.5f));
        setBounds(218, 25, 576, 412);
        setLayout(new GridLayout(2, 1));
        model = (DefaultTableModel) table.getModel();
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                onTableMouseClick();
            }
        });
        table.setDefaultEditor(Object.class, null);
        send.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                send();
            }
        });
        scrollPane = new JScrollPane(table);
        scrollPane.setBounds(20, 106, 546, 272);
        messageTextField.setColumns(70);
        textArea.setEnabled(false);

        add(scrollPane);
        JPanel firstFlow = new JPanel();
        firstFlow.setLayout(new BoxLayout(firstFlow, BoxLayout.Y_AXIS));
        firstFlow.add(currentUserLabel);

        firstFlow.add(new JScrollPane(textArea));

        JPanel secondFlow = new JPanel();
        secondFlow.setLayout(new BoxLayout(secondFlow, BoxLayout.X_AXIS));


        secondFlow.add(messageTextField);

        secondFlow.add(send);
        firstFlow.add(secondFlow);
        add(firstFlow);
    }

    public abstract void startLiveChat();

    protected abstract void send();

    protected abstract void onTableMouseClick();

    public JButton getSendButton(){
        return send;
    }

    public abstract void stopLiveChat();
}
