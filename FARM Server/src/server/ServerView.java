package server;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ServerView extends JFrame implements ActionListener {

	private JButton btnStopServer;

	public ServerView(){
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		btnStopServer = new JButton("Stop Server");
		btnStopServer.addActionListener(this);
		this.add(btnStopServer);
		
		pack();
		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent ev) {
		if(ev.getSource().equals(btnStopServer)){
			setVisible(false);
		    dispose();
			System.exit(0);
		}
		
	}

}
