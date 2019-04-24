package gui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.SoftBevelBorder;

import model.Customer;
import model.Farmer;
import client.RequestSender;
import communication.Response;
//import server.Server;

import javax.swing.border.BevelBorder;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Color;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JPasswordField;
import javax.swing.JComboBox;

public class Login extends JFrame {

	private JPanel contentPane;
	private JTextField txb_Un;
	private JPasswordField txb_Pw;
	private JComboBox cbLType;
	private RequestSender requestSender = RequestSender.getInstance();
	
    Farmer farmer1 = new Farmer();
	Customer customer1 = new Customer();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					RequestSender.getInstance();
					Login frame = new Login();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	//Connection conn = null;
	
	/**
	 * Create the frame.
	 */
	public Login() {
		//conn = Server.ServerConnection();
		
		String[] type = { "-Select One-", "Farmer", "Customer"};
		setCbLType(new JComboBox(type));
		
		
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 810, 526);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBorder(new SoftBevelBorder(BevelBorder.RAISED, null, null, null, null));
		panel.setBackground(new Color(0.0f, 0.0f, 0.0f, 0.5f));
		panel.setBounds(194, 71, 422, 340);
		contentPane.add(panel);
		panel.setLayout(null);
		
		JButton btnCancel = new JButton("Clear");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				getTxb_Un().setText("");
				getTxb_Pw().setText("");
				getCbLType().setSelectedIndex(0);
			}
		});
		btnCancel.setEnabled(false);
		btnCancel.setFont(new Font("Times New Roman", Font.PLAIN, 15));
		btnCancel.setBounds(312, 299, 89, 30);
		panel.add(btnCancel);
		
		setTxb_Un(new JTextField());
		getTxb_Un().addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent arg0) {
				if ((getTxb_Pw().getText().compareTo("")!=0) || (getTxb_Un().getText().compareTo("")!=0))
				{
					btnCancel.setEnabled(true);
				}
			}
		});
		getTxb_Un().setFont(new Font("Times New Roman", Font.PLAIN, 15));
		getTxb_Un().setHorizontalAlignment(SwingConstants.CENTER);
		getTxb_Un().setBounds(140, 161, 261, 30);
		panel.add(getTxb_Un());
		getTxb_Un().setColumns(10);
		
		JLabel lblNewLabel = new JLabel("Username:");
		lblNewLabel.setForeground(Color.WHITE);
		lblNewLabel.setFont(new Font("Times New Roman", Font.PLAIN, 20));
		lblNewLabel.setHorizontalAlignment(SwingConstants.LEFT);
		lblNewLabel.setBounds(21, 170, 151, 21);
		panel.add(lblNewLabel);
		
		JLabel lblPassword = new JLabel("Password:");
		lblPassword.setForeground(Color.WHITE);
		lblPassword.setFont(new Font("Times New Roman", Font.PLAIN, 20));
		lblPassword.setHorizontalAlignment(SwingConstants.LEFT);
		lblPassword.setBounds(21, 217, 151, 21);
		panel.add(lblPassword);
		
		JButton btn_Login = new JButton("Login");
		btn_Login.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if ((getTxb_Un().getText().compareToIgnoreCase("")==0) || (getTxb_Pw().getText().compareTo("")==0) || (getCbLType().getSelectedIndex()==0))
				{
					JOptionPane.showMessageDialog(null, "Please check to ensure that all fields are fllled");
				}
				else
				{
				  try{
					  
					//  control.ULogin();
						  if (getCbLType().getSelectedIndex() == 1) //Farmer check
						  {
							  /*String query1 = "SELECT * FROM Farmer WHERE (Email)=? and Password=? ";
							  PreparedStatement pst1 = conn.prepareStatement(query1);
							  pst1.setString(1, getTxb_Un().getText().toLowerCase() );
							  pst1.setString(2, getTxb_Pw().getText() );
							  
							  ResultSet rs = pst1.executeQuery();
							  int count = 0;
							  while (rs.next())
							  {
								  farmer1.setID(rs.getInt(1));
								  farmer1.setFirstName(rs.getString(2));
								  farmer1.setLastName(rs.getString(3));
								  farmer1.setEmail(rs.getString(4));
								  farmer1.setPhoto1(rs.getBytes(5));
								  farmer1.setTotalEarn(rs.getDouble(7));
								  farmer1.setAddress(rs.getString(8));
								  
								  count++;
							  }*/

							  int count;
							  Response<Object[]> response = requestSender.loginFarmer(getTxb_Un().getText().toLowerCase(), getTxb_Pw().getText());
							  if(requestSender.responseCheck(response)){
							  	count = (int) response.getData()[1];
							  }else{
							  	count = -1;
							  }

							  if (count ==1)
							  {
									JOptionPane.showMessageDialog(null, "Login Successful");
									dispose();
									farmer1 = (Farmer) response.getData()[0];
									Farmer_DashBoard fdb = new Farmer_DashBoard(farmer1);
									fdb.main(new String[0]);
							  }
							  else if (count > 1)
							  {
								    JOptionPane.showMessageDialog(null, "Duplicate Username and Password.");
							  }else if (count == -1)
							  {
							  	if(response.getErrorMessage().equals(Response.ALREADY_LOGGED_IN)){
									System.out.println("Already logged in");
									JOptionPane.showMessageDialog(null, "Error: User Already Logged In\n" +
											"Must log out of other computer before login in on another");
								}


							  }
							  else
							  {
								  JOptionPane.showMessageDialog(null, "Username and Password do not match, please try again");
							  }
							  
							  //rs.close();
							  //pst1.close();
						  }
						  else if (getCbLType().getSelectedIndex() == 2)//Customer check
						  {
							  /*String query2 = "SELECT * FROM Customer WHERE (Email)=? and Password=? ";
							  PreparedStatement pst2 = conn.prepareStatement(query2);
							  
							  pst2.setString(1, getTxb_Un().getText().toLowerCase() );
							  pst2.setString(2, getTxb_Pw().getText() );
							  
							  ResultSet rs1 = pst2.executeQuery();
							  int count = 0;
							  while (rs1.next())
							  {
								  customer1.setCusID(rs1.getInt(1));
								  customer1.setFirstName(rs1.getString(2));
								  customer1.setLastName(rs1.getString(3));
								  customer1.setEmail(rs1.getString(4));
								  customer1.setPhoto1(rs1.getBytes(6));
								  customer1.setFunds(rs1.getFloat(7));
								  
								  count++;
							  }*/

							  int count;
							  Response<Object[]> response = requestSender.loginCustomer(getTxb_Un().getText().toLowerCase(), getTxb_Pw().getText());
							  if(requestSender.responseCheck(response)){
								  count = (int) response.getData()[1];
							  }else{
								  count = -1;
							  }
							  if (count ==1)
							  {
									JOptionPane.showMessageDialog(null, "Login Successful");
									dispose();
								  	customer1 = (Customer) response.getData()[0];
								    Customer_Dashboard cdb = new  Customer_Dashboard(customer1);
									cdb.setVisible(true);
							  }
							  else if (count > 1)
							  {
								    JOptionPane.showMessageDialog(null, "Duplicate Username and Password.");
							  }
							  else if (count == -1)
							  {
								  if(response.getErrorMessage().equals(Response.ALREADY_LOGGED_IN)){
									  System.out.println("Already logged in");
									  JOptionPane.showMessageDialog(null, "Error: User Already Logged In\n" +
											  "Must log out of other computer before login in on another");
								  }

							  }
							  else
							  {
								  JOptionPane.showMessageDialog(null, "Username and Password do not match, please try again");
							  }
							  
							  //rs1.close();
							  //pst2.close();
						  }
						 
				  }
				  catch(Exception e){
				    e.printStackTrace();
					JOptionPane.showMessageDialog(null, e);
				  }
			    }
			}
		});
		btn_Login.setFont(new Font("Times New Roman", Font.PLAIN, 15));
		btn_Login.setBounds(21, 299, 89, 30);
		panel.add(btn_Login);
		
		contentPane.getRootPane().setDefaultButton(btn_Login);
		
		JButton btnCreateAccount = new JButton("Create Account");
		btnCreateAccount.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				dispose();
				CreateAccount ca = new CreateAccount();
				ca.setVisible(true);
			}
		});
		btnCreateAccount.setFont(new Font("Times New Roman", Font.PLAIN, 15));
		btnCreateAccount.setBounds(140, 299, 149, 30);
		panel.add(btnCreateAccount);
		
		ImageIcon icon2 = new ImageIcon("Images\\lg.png");
		JLabel lb_Icon = new JLabel("");
		lb_Icon.setIcon(icon2);
		lb_Icon.setBounds(151, 11, 149, 139);
		panel.add(lb_Icon);
		
		setTxb_Pw(new JPasswordField());
		getTxb_Pw().setEchoChar('*');
		getTxb_Pw().setHorizontalAlignment(SwingConstants.CENTER);
		getTxb_Pw().setFont(new Font("Times New Roman", Font.PLAIN, 20));
		getTxb_Pw().setBounds(140, 210, 261, 30);
		panel.add(getTxb_Pw());
		
		JLabel lblAccountType = new JLabel("Account:");
		lblAccountType.setHorizontalAlignment(SwingConstants.LEFT);
		lblAccountType.setForeground(Color.WHITE);
		lblAccountType.setFont(new Font("Times New Roman", Font.PLAIN, 20));
		lblAccountType.setBounds(21, 258, 151, 21);
		panel.add(lblAccountType);
		
		getCbLType().setBounds(140, 249, 261, 30);
		panel.add(getCbLType());
		
		ImageIcon icon = new ImageIcon("Images\\bg.jpg");
		JLabel lb_bg = new JLabel(" "); 
		lb_bg.setIcon(icon);
		lb_bg.setBounds(0, 0, 804, 497);
		contentPane.add(lb_bg);
		//lb_bg.setIcon(new ImageIcon("Images\bg.jpg"));
		
		
	} 

	public JTextField getTxb_Un() {
		return txb_Un;
	}

	public void setTxb_Un(JTextField txb_Un) {
		this.txb_Un = txb_Un;
	}

	public JPasswordField getTxb_Pw() {
		return txb_Pw;
	}

	public void setTxb_Pw(JPasswordField txb_Pw) {
		this.txb_Pw = txb_Pw;
	}

	public JComboBox getCbLType() {
		return cbLType;
	}

	public void setCbLType(JComboBox cbLType) {
		this.cbLType = cbLType;
	}

	public Farmer getFarmer1() {
		return farmer1;
	}

	public void setFarmer1(Farmer farmer1) {
		this.farmer1 = farmer1;
	}
	
	public Customer getCustomer1() {
		return customer1;
	}

	public void setCustomer1(Customer customer1) {
		this.customer1 = customer1;
	}
}
