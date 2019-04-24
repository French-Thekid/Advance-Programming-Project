package gui;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.sql.SQLException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.SoftBevelBorder;
import javax.swing.table.DefaultTableModel;

import client.RequestSender;
import communication.Response;
import model.Crop;
import model.Customer;
//import server.Server;

import javax.swing.border.BevelBorder;
import javax.swing.JTextField;
import javax.swing.JTable;
import javax.swing.JScrollPane;

public class Customer_Dashboard extends JFrame {

	private RequestSender requestSender = RequestSender.getInstance();

	private JPanel contentPane;
	private JTextField textField;
	private JTable table;
	private JTable table_1;
	static Customer customer1 = new Customer();

	CustomerLiveChatView chat = new CustomerLiveChatView();
	JPanel panelMain = new JPanel();
	JButton btnLChat = new JButton("Live Chat");
	boolean isChatActive = false;

	Crop crops = new Crop();
	Crop FCrops = new Crop();
	Crop Ccheckout = new Crop();
	Double Total = 0.0;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Customer_Dashboard frame = new Customer_Dashboard(customer1);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	//Connection conn = null;
	
	public static BufferedImage scaleImage(int w, int h, BufferedImage img) throws Exception {
	    BufferedImage bi;
	    bi = new BufferedImage(w, h, BufferedImage.TRANSLUCENT);
	    Graphics2D g2d = (Graphics2D) bi.createGraphics();
	    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	    g2d.addRenderingHints(new RenderingHints(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY));
	    g2d.drawImage(img, 0, 0, w, h, null);
	    g2d.dispose();
	    return bi;
	}
	
	/**
	 * Create the frame.
	 */
	public Customer_Dashboard(Customer customer) {
		customer1 = customer;
		//conn = Server.ServerConnection();
		JButton btnVCClose = new JButton("Close");
		JButton btnViewCrops = new JButton("View Crops");
		JButton btnChkOut = new JButton("Check Out");
		JButton btnViewBasket = new JButton("View Basket");
		
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 810, 526);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JSeparator separator = new JSeparator();
		separator.setOrientation(SwingConstants.VERTICAL);
		separator.setBounds(205, 25, 2, 454);
		contentPane.add(separator);
		
		JLabel lblJohnBrown = new JLabel(customer1.getFirstName()+" "+customer1.getLastName());
		lblJohnBrown.setForeground(Color.WHITE);
		lblJohnBrown.setHorizontalAlignment(SwingConstants.CENTER);
		lblJohnBrown.setFont(new Font("Times New Roman", Font.PLAIN, 16));
		lblJohnBrown.setBounds(33, 151, 115, 26);
		contentPane.add(lblJohnBrown);
		
		Border border = BorderFactory.createLineBorder(Color.GREEN, 1);
		JLabel label = new JLabel("");
		label.setBorder(border);
		label.setBounds(10, 25, 178, 127);
		
		ImageIcon icon1;
		byte[] cropimage;
		
		cropimage = customer1.getPhoto1();
			
		try {
			icon1=new ImageIcon(scaleImage(178, 127, ImageIO.read(new ByteArrayInputStream(cropimage))));
			label.setIcon(icon1);			
		}
		catch (Exception e) {

			e.printStackTrace();
		}
			
		
		contentPane.add(label);
		
		
		JLabel lbCash = new JLabel("$"+customer1.getMoney());
		lbCash.setForeground(Color.WHITE);
		lbCash.setHorizontalAlignment(SwingConstants.CENTER);
		lbCash.setFont(new Font("Times New Roman", Font.ITALIC, 12));
		lbCash.setBounds(57, 200, 79, 14);
		contentPane.add(lbCash);
		
		JPanel panelVBasket = new JPanel();
		panelVBasket.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		panelVBasket.setBackground(new Color(0.0f, 0.0f, 0.0f, 0.5f));
		panelVBasket.setBounds(218, 25, 576, 412);
		contentPane.add(panelVBasket);
		panelVBasket.setLayout(null);
		
		
		
		//String total ="Select * from CustomerBasket where CustomerID='"+customer1.getCusID()+"'";
		/*try{
			PreparedStatement pst7 = conn.prepareStatement(total);
			ResultSet rst1 = pst7.executeQuery();
			while(rst1.next())
			{
					Total=(Total + rst1.getDouble(6));
			}
			pst7.close();
			rst1.close();
		}
		catch(SQLException ex)
		{
			ex.printStackTrace();
		}*/

		Response<Double> eresponse = requestSender.getCustomerTotalBasket(customer1.getCusID());
		if(requestSender.responseCheck(eresponse)){
			Total = eresponse.getData();
		}
		
		JTextField lbTotal = new JTextField("");
		lbTotal.setEditable(false);
		lbTotal.setEnabled(false);
		lbTotal.setBackground(Color.BLACK);
		lbTotal.setText(Total.toString());
		lbTotal.setFont(new Font("Times New Roman", Font.PLAIN, 18));
		lbTotal.setForeground(Color.WHITE);
		lbTotal.setBounds(488, 334, 78, 21);
		panelVBasket.add(lbTotal);
		panelVBasket.setVisible(false);
		
		
		JButton btn_AddF = new JButton("Add Funds");
		btn_AddF.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				endLiveChat(panelMain);
				try{
				    String value = JOptionPane.showInputDialog("Enter Amount to deposit: ");
			        if (value == null)//check if cancel was selected
			        {}
			        else{
			        	//retrieving latest update
			        	/*String cash = "Select * from Customer where ID='"+customer1.getCusID()+"' ";
			        	PreparedStatement pt0 = conn.prepareStatement(cash);
			        	ResultSet rt0 = pt0.executeQuery();*/
			        	Response<Double> response1 = requestSender.getCustomerFunds(customer1.getCusID());
			        	if(!requestSender.responseCheck(response1)){
			        		throw new Exception("Error");
						}
			        	customer1.setFunds(response1.getData());
			        	
			        	double total = Double.parseDouble(value);
			        	/*String query0 = "UPDATE Customer SET Money='"+(total+customer.getMoney())+"' WHERE ID ='"+customer1.getCusID()+"'";
			        	String q1="Select * from Customer where ID='"+customer1.getCusID()+"' ";
			        	PreparedStatement pst0,pt1;
			        	pst0 = conn.prepareStatement(query0);
			        	pst0.execute();
			        	
			        	Update cash for display from database
			        	pt1 = conn.prepareStatement(q1);
			        	ResultSet rt1 = pt1.executeQuery();
			        	customer1.setFunds(rt1.getDouble(7));*/
						Response<Double> response3 = requestSender.updateCustomerFunds((total+customer.getMoney()), customer1.getCusID());
						if(!requestSender.responseCheck(response3)){
							throw new Exception("Error");
						}
						customer1.setFunds(response3.getData());
			        	
			        	
			        	lbCash.setText("$"+(customer.getMoney()));
						//customer1.setFunds(total+customer.getMoney());
			        	JOptionPane.showMessageDialog(null,"Your new balance is: " + (customer.getMoney()));
			             
			        	/*rt0.close();
			        	pt0.close();
			        	rt1.close();
			        	pst0.close();
			        	pt1.close();*/
			         }
			       }
				   catch(Exception ex)
				   {
					   ex.printStackTrace();
					   if(!ex.toString().equals("Error"))
					   	JOptionPane.showMessageDialog(null, "Invalid Input, please try adding again");
				   }
			}
		});
		btn_AddF.setFont(new Font("Times New Roman", Font.PLAIN, 15));
		btn_AddF.setBounds(33, 250, 142, 37);
		contentPane.add(btn_AddF);
		
		JSeparator separator_1 = new JSeparator();
		separator_1.setBounds(9, 237, 186, 2);
		contentPane.add(separator_1);
		
		JLabel lblAnthonybrownymailcom = new JLabel(customer1.getEmail());
		lblAnthonybrownymailcom.setForeground(Color.WHITE);
		lblAnthonybrownymailcom.setHorizontalAlignment(SwingConstants.CENTER);
		lblAnthonybrownymailcom.setFont(new Font("Times New Roman", Font.PLAIN, 14));
		lblAnthonybrownymailcom.setBounds(10, 213, 185, 26);
		contentPane.add(lblAnthonybrownymailcom);
		

		panelMain.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		panelMain.setBackground(new Color(0.0f, 0.0f, 0.0f, 0.5f));
		panelMain.setBounds(218, 25, 576, 412);
		contentPane.add(panelMain);
		panelMain.setLayout(null);
		panelMain.setVisible(true);
		
		ImageIcon i = new ImageIcon("Images\\log.png");
		JLabel lbMLOGO = new JLabel("");
		lbMLOGO.setBounds(0, 85, 557, 224);
		lbMLOGO.setIcon(i);
		panelMain.add(lbMLOGO);
		panelMain.setVisible(true);
		
		JSeparator separator_3 = new JSeparator();
		separator_3.setBounds(20, 71, 546, 2);
		panelVBasket.add(separator_3);
	
		
		ImageIcon icon03 = new ImageIcon("Images\\basket.png");
		JLabel lbCViewBasketLogo = new JLabel("");
		lbCViewBasketLogo.setIcon(icon03);
		lbCViewBasketLogo.setBounds(148, 11, 274, 61);
		panelVBasket.add(lbCViewBasketLogo);
		
		String[] ChkOption = {"--Select Destination--","Linstead Market","Kingston Market","Papine Market"};
		JComboBox cbChkOut = new JComboBox(ChkOption);
		
		JButton btnNewButton_3 = new JButton("Checkout");
		btnNewButton_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try{
					/*String checkDB = "Select * from CustomerBasket where CustomerID='"+customer1.getCusID() +"' ";
					PreparedStatement pst37 = conn.prepareStatement(checkDB);
					ResultSet rst31 = pst37.executeQuery();*/
					int c=0;
					/*while(rst31.next())
					{
						c++;
					}*/

					Response<Integer> response = requestSender.checkIfCustomerBasketEmpty(customer1.getCusID());
					if(requestSender.responseCheck(response)){
						c = response.getData();
					}else {
						c = -1;
					}
					 
					if(c==0)
					{
						JOptionPane.showMessageDialog(null, "You need to add item to your cart first");
					}
					else if(c == -1){

					}
					else{
						Object[] option = new Object[] {};
						
						String input = (String) JOptionPane.showInputDialog(null, "Please select your Pickup Location",null,JOptionPane.QUESTION_MESSAGE,null,ChkOption,"");
						if(input==null)
						{
							
						}
						else if(input.equals("--Select Destination--"))
						{
							JOptionPane.showMessageDialog(null, "Please make a valid selection for your pickup location");
						}
						else{
							
							//try{
								//retrieving latest update
					        	/*String cash1 = "Select * from Customer where ID='"+customer1.getCusID()+"' ";
					        	PreparedStatement pt01 = conn.prepareStatement(cash1);
					        	ResultSet rt01 = pt01.executeQuery();*/
								Response<Double> response1 = requestSender.getCustomerFunds(customer1.getCusID());
								if(!requestSender.responseCheck(response1)){
									throw new Exception(response1.getErrorMessage());
								}
								customer1.setFunds(response1.getData());
					        	//customer1.setFunds(rt01.getDouble(7));
					        	
								double Total1=0.0;
								/*String total2 ="Select * from CustomerBasket where CustomerID='"+customer1.getCusID()+"'";
							
								PreparedStatement pst27 = conn.prepareStatement(total2);
								ResultSet rst21 = pst27.executeQuery();
								while(rst21.next())
								{
										Total1+=rst21.getDouble(6);
								}*/

								Response<Double> response2 = requestSender.getCustomerTotalBasket(customer1.getCusID());
								if(requestSender.responseCheck(response2)){
									Total1 = response2.getData();
								}else {
									throw new Exception(response2.getErrorMessage());
								}
								customer1.setTotal(Total1);
								
								/*pt01.close();
								rt01.close();
								pst27.close();
								rst21.close();*/
							/*}
							catch(SQLException e5)
							{
								e5.printStackTrace();
							}*/
							if(customer1.getTotal()<=customer1.getMoney())
							{
								double NetFunds= 0.0;
								JOptionPane.showMessageDialog(null, "Thank you for making it Farmer's Market, your order will be available at "+input);
								Response<Boolean> response3 = requestSender.updateForCustomerCheckout(customer1.getCusID());
								if(requestSender.responseCheck(response3)){
									if(response3.getData()){

									}else{
										throw new Exception("Error processing checkout");
									}
								}else{
									throw new Exception(response3.getErrorMessage());
								}
								/*String reset = "DELETE FROM CustomerBasket WHERE CustomerID ='"+customer1.getCusID()+"' ";
								String total ="Select * from CustomerBasket where CustomerID='"+customer1.getCusID()+"'";
								
								try {
									//UPDATING FARMERS CROPS QUANTITY AND SHOPPERS LIST
									int Quan=0;
									String crops ="Select * from CustomerBasket where CustomerID='"+customer1.getCusID()+"'";
									
									PreparedStatement pt37 = conn.prepareStatement(crops);
									ResultSet rt31 = pt37.executeQuery();
									
									while(rt31.next())
									{
										FCrops.setFarmerName(rt31.getString(7));
										FCrops.setcName(rt31.getString(3));
										FCrops.setQuantity(rt31.getInt(4));
										Double totalCostOfCrop = rt31.getDouble(6);
										
										String crop ="Select * from Crops where Name='"+FCrops.getcName()+"' AND Farmer_Name='"+FCrops.getFarmerName()+"'";
										try{
											PreparedStatement pt3 = conn.prepareStatement(crop);
											ResultSet r3 = pt3.executeQuery();
											
											while (r3.next())
											{
												Quan = r3.getInt(5);
											}
											String q0 = "UPDATE Crops SET Quantity='"+(Quan-FCrops.getQuantity())+"' WHERE Name='"+FCrops.getcName()+"' AND Farmer_Name ='"+FCrops.getFarmerName()+"'";
								        	
											PreparedStatement p0;
								        	p0 = conn.prepareStatement(q0);
											p0.execute();
											

											r3.close();
											pt3.close();
											p0.close();
										}
										catch(SQLException b)
										{
											b.printStackTrace();
										}

										//UPDATING FARMERS CROPS QUANTITY AND SHOPPERS LIST
										String q1 = "INSERT INTO Farmer_Customers (Customers,Amount_Spent,Farmer) VALUES(?,?,?)"; //todo: added this inside the loop

										try
										  {
											PreparedStatement p = conn.prepareStatement(q1);

											p.setString(1, customer1.getFirstName()+" "+customer1.getLastName());
											p.setString(2,totalCostOfCrop.toString());
											p.setString(3, FCrops.getFarmerName());

											p.execute();
											p.close();
										  }
										catch(SQLException a)
										{
											a.printStackTrace();
										}


									}
									rt31.close();


									
									
									
									
									
									
									//clearing out customer basket
									PreparedStatement pst04 = conn.prepareStatement(reset);
									pst04.executeUpdate();
									pt37.close();
									pst04.close();
									} catch (SQLException e1) {
										e1.printStackTrace();
									}*/
								
								//Refreshing Table
								//try{
									//String query1 = "SELECT Name,Quantity, Unit_Cost,Total, Farmer FROM CustomerBasket where CustomerID='"+customer1.getCusID() +"' ";
								
									//For table refreshing
									//PreparedStatement pst1 = conn.prepareStatement(query1);
									//ResultSet rst1 = pst1.executeQuery();
									DefaultTableModel tableModel = requestSender.customerViewBasket(customer1.getCusID());
									if(tableModel == null) {
										//set error
									}else{
										table_1.setModel(tableModel);
									}
									//table_1.setModel(DbUtils.resultSetToTableModel(rst1));
									

									/*//UPDATING FARMERS CROPS QUANTITY AND SHOPPERS LIST
									String q1 = "INSERT INTO Farmer_Customers (Customers,Amount_Spent,Farmer) VALUES(?,?,?)";
									
					        		try
									  {
					        			PreparedStatement p = conn.prepareStatement(q1);
										
										p.setString(1, customer1.getFirstName()+" "+customer1.getLastName());
										p.setString(2,customer1.getTotal().toString());
										p.setString(3, FCrops.getFarmerName());
										
										p.execute();
										p.close();
									  }
					        		catch(SQLException a)
					        		{
					        			a.printStackTrace();
					        		}*/
									
									//undating remaining funds
									NetFunds = (customer.getMoney() - customer1.getTotal());
									
									//recalculating total
									/*PreparedStatement pst7 = conn.prepareStatement(total);
									ResultSet rst11 = pst7.executeQuery();*/
									Total=0.0;
									/*while(rst11.next())
									{
											Total+=rst11.getDouble(6);
									}*/

									Response<Double> response4 = requestSender.getCustomerTotalBasket(customer1.getCusID());
									if(requestSender.responseCheck(response4)){
										Total = response4.getData();
									}else {
										throw new Exception(response4.getErrorMessage());
									}
									lbTotal.setText(Total.toString());
									customer1.setTotal(Total);
									/*String query0 = "UPDATE Customer SET Money='"+NetFunds+"' WHERE ID ='"+customer1.getCusID()+"'";
						        	
									PreparedStatement pst0;
						        	pst0 = conn.prepareStatement(query0);
									pst0.execute();*/

									Response<Double> response5 = requestSender.updateCustomerFunds(NetFunds, customer1.getCusID());
									if(!requestSender.responseCheck(response5)){
										throw new Exception(response5.getErrorMessage());
									}
									
									
									lbCash.setText("$"+(NetFunds));
									
									
									/*pst7.close();
									rst11.close();
									pst0.close();
									pst1.close();
									rst1.close();*/
									
									
									
								/*}
								catch(SQLException e2) {
									e2.printStackTrace();	
								}*/
							}
							else
							{
								JOptionPane.showMessageDialog(null, "Insufficient funds!, try adding funds to your account and try again");
							}
						}
					}
					
					/*rst31.close();
					pst37.close();*/
				}
				catch(Exception i)
				{
					i.printStackTrace();
				}
					
			}
		});
		btnNewButton_3.setFont(new Font("Times New Roman", Font.PLAIN, 15));
		btnNewButton_3.setBounds(320, 366, 98, 36);
		btnNewButton_3.setEnabled(true);
		panelVBasket.add(btnNewButton_3);
		
		JButton btnRemoveItem = new JButton("Remove Item");
		btnRemoveItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			  if(table_1.getSelectedRowCount()==0)
			  {
			  	 JOptionPane.showMessageDialog(null, "Please make a selection first");
			  }
			  else
			  {
				int choice1 = JOptionPane.showConfirmDialog(null,"Are you sure you want to remove this item?");
				if (choice1 == 0)
				{
					/*String delete = "DELETE FROM CustomerBasket WHERE Name='"+Ccheckout.getcName()+"' AND Farmer ='"+Ccheckout.getFarmerName()+"' AND Quantity ='"+Ccheckout.getQuantity()+"' AND CustomerID ='"+customer1.getCusID()+"' ";//todo: added cus ID
					try {
						PreparedStatement pst03 = conn.prepareStatement(delete);
						pst03.executeUpdate();*/
					Response<Boolean> response = requestSender.removeItemFromCustomerBasket(Ccheckout.getcName(), Ccheckout.getFarmerName(), Ccheckout.getQuantity(), customer1.getCusID());
					if(requestSender.responseCheck(response))
						if(response.getData())
							JOptionPane.showMessageDialog(null, Ccheckout.getcName()+" successfully removed from your cart");
                        
						/*pst03.close();
						} catch (SQLException e1) {
						e1.printStackTrace();
						}*/
					//Refreshing Data
					/*String query1 = "SELECT Name,Quantity, Unit_Cost,Total, Farmer FROM CustomerBasket where CustomerID='"+customer1.getCusID() +"' ";
					String total ="Select * from CustomerBasket where CustomerID='"+customer1.getCusID()+"'";
					
					try{
						PreparedStatement pst1 = conn.prepareStatement(query1);
						ResultSet rst1 = pst1.executeQuery();
						table_1.setModel(DbUtils.resultSetToTableModel(rst1));*/

					DefaultTableModel tableModel = requestSender.customerViewBasket(customer1.getCusID());
					if(tableModel == null) {
						//set error
					}else{
						table_1.setModel(tableModel);
					}
							
						/*PreparedStatement pst7 = conn.prepareStatement(total);
						ResultSet rst11 = pst7.executeQuery();*/
						Total=0.0;
					Response<Double> response1 = requestSender.getCustomerTotalBasket(customer1.getCusID());
					if(requestSender.responseCheck(response1)){
						Total = response1.getData();
					}
						/*while(rst11.next())
						{
								Total+=rst11.getDouble(6);
						}*/
						
						lbTotal.setText(Total.toString());
						customer1.setTotal(Total);
						
						/*rst11.close();
						pst1.close();
						pst7.close();
						rst1.close();
					}
					catch(SQLException e2) {
						e2.printStackTrace();	
					}*/
				}
				else
				{}
			  }
			}
		});
		btnRemoveItem.setFont(new Font("Times New Roman", Font.PLAIN, 15));
		btnRemoveItem.setBounds(170, 366, 140, 36);
		btnRemoveItem.setEnabled(false);
		panelVBasket.add(btnRemoveItem);
		
		JButton btnChangeQuantity = new JButton("Change Quantity"); //todo: check to see that the new value doesn't exceed the amount of crops
		btnChangeQuantity.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(table_1.getSelectedRowCount()==0)
				{
					JOptionPane.showMessageDialog(null, "Please make a selection first");
				}
				else
				{
					String value = JOptionPane.showInputDialog("Enter new quantity amount: ");
					if (value == null)
			        {}
			        else{
			        	int valueInt = 0;; 
			        	boolean compaible=true;
			        	try{
			        		valueInt = Integer.parseInt(value);
			        	}
			        	catch(NumberFormatException ex)
			        	{
			        		JOptionPane.showMessageDialog(null, "Invalid Input, Please try again");
			        		compaible=false;
			        	}
			        	if (compaible)
			        	{			        		
			        		double cost=0.0;//todo: added cus ID
			        		double T=0.0;
			        		//String QuanChange = "UPDATE CustomerBasket SET Quantity ='"+valueInt+"' WHERE Name='"+Ccheckout.getcName()+"' AND Farmer ='"+Ccheckout.getFarmerName()+"' AND CustomerID ='"+customer1.getCusID()+"' ";//todo: added cus ID";
			        		//String GetCost = "Select * from CustomerBasket WHERE Name='"+Ccheckout.getcName()+"' AND Farmer ='"+Ccheckout.getFarmerName()+"' AND CustomerID ='"+customer1.getCusID()+"' ";//todo: added cus ID";
			        		try{
			        			//Updating Quantity
								/*PreparedStatement pst1 = conn.prepareStatement(QuanChange);
								pst1.execute();*/
								Response re = requestSender.updateCustomerBasketCropQuantity(Ccheckout.getcName(), Ccheckout.getFarmerName(), valueInt, customer1.getCusID());


								
								//Retrieving cost for update purposes
								/*PreparedStatement pst2 = conn.prepareStatement(GetCost);
								ResultSet rst1 = pst2.executeQuery();*/
								
								//calculating new total
								Response<Double> response1 = requestSender.getCropUnitCostFromCustomerBasket(Ccheckout.getcName(), Ccheckout.getFarmerName(), customer1.getCusID());

								if(requestSender.responseCheck(response1)){
									cost = response1.getData();
								}else{
									throw new Exception(response1.getErrorMessage());
								}
								//cost = rst1.getDouble(5);
								T= cost*valueInt;
								
								/*pst1.close();
								pst2.close();
								rst1.close();
							}
							catch(SQLException e3) {
								e3.printStackTrace();	
							}  
							try{*/
								//Updating new total for customer basket 
								/*String TotalUpdate = "UPDATE CustomerBasket SET Total ='"+T+"' WHERE Name='"+Ccheckout.getcName()+"' AND Farmer ='"+Ccheckout.getFarmerName()+"' AND CustomerID ='"+customer1.getCusID()+"' ";//todo: added cus ID";
				        		PreparedStatement pst0 = conn.prepareStatement(TotalUpdate);
								pst0.execute();
								
								pst0.close();*/

								Response<Boolean> response2 = requestSender.updateCustomerBasketCropTotal(Ccheckout.getcName(), Ccheckout.getFarmerName(), T, customer1.getCusID());
							}
							catch(SQLException e3) {
								e3.printStackTrace();	
							} catch(Exception e6){
			        			e6.printStackTrace();
							}
							//Refreshing Table
							//String query1 = "SELECT Name,Quantity, Unit_Cost,Total, Farmer FROM CustomerBasket where CustomerID='"+customer1.getCusID() +"' ";
							//String total ="Select * from CustomerBasket where CustomerID='"+customer1.getCusID()+"'";
							//try{
								/*PreparedStatement pst1 = conn.prepareStatement(query1);
								ResultSet rst1 = pst1.executeQuery();
								table_1.setModel(DbUtils.resultSetToTableModel(rst1));*/

								DefaultTableModel tableModel = requestSender.customerViewBasket(customer1.getCusID());
								if(tableModel == null) {
									//set error
								}else{
									table_1.setModel(tableModel);
								}
								
								//retrieving new totals for recalculations
								/*PreparedStatement pst7 = conn.prepareStatement(total);
								ResultSet rst12 = pst7.executeQuery();*/
								Total=0.0;
								Response<Double> response1 = requestSender.getCustomerTotalBasket(customer1.getCusID());
								if(requestSender.responseCheck(response1)){
									Total = response1.getData();
								}
								/*while(rst12.next())
								{
										Total=(Total + rst12.getDouble(6));
								}*/
								lbTotal.setText(Total.toString());
								customer1.setTotal(Total);
								System.out.println("CQ: "+Total);
								
								/*rst12.close();
								pst1.close();
								pst7.close();
								rst1.close();*/
							/*}
							catch(SQLException e2) {
								e2.printStackTrace();	
							}*/
			        	}
				   }
				}
			}
		});
		btnChangeQuantity.setFont(new Font("Times New Roman", Font.PLAIN, 15));
		btnChangeQuantity.setBounds(20, 366, 140, 36);
		btnChangeQuantity.setEnabled(false);
		panelVBasket.add(btnChangeQuantity);
		
		JPanel panelView = new JPanel();
		panelView.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		panelView.setBackground(new Color(0.0f, 0.0f, 0.0f, 0.5f));
		panelView.setBounds(218, 25, 576, 412);
		contentPane.add(panelView);
		panelView.setLayout(null);
		panelView.setVisible(false);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(20, 130, 546, 228);
		panelView.add(scrollPane);
		
		JSeparator separator_2 = new JSeparator();
		separator_2.setBounds(20, 70, 546, 2);
		panelView.add(separator_2);
		
		ImageIcon icon01 = new ImageIcon("Images\\View.png");
		JLabel lbCVCropLogo = new JLabel("");
		lbCVCropLogo.setIcon(icon01);
		lbCVCropLogo.setBounds(151, 11, 274, 61);
		panelView.add(lbCVCropLogo);
		
		JLabel lblSeachSpecificCrop = new JLabel("Search Specific Farmer");
		lblSeachSpecificCrop.setBounds(20, 93, 174, 25);
		panelView.add(lblSeachSpecificCrop);
		lblSeachSpecificCrop.setForeground(Color.WHITE);
		lblSeachSpecificCrop.setHorizontalAlignment(SwingConstants.CENTER);
		lblSeachSpecificCrop.setFont(new Font("Times New Roman", Font.PLAIN, 18));
		
		textField = new JTextField();
		textField.setBounds(204, 93, 211, 26);
		panelView.add(textField);
		textField.setFont(new Font("Times New Roman", Font.PLAIN, 15));
		textField.setHorizontalAlignment(SwingConstants.CENTER);
		textField.setColumns(10);
		
		
		JButton btnSearch = new JButton("Search");
		btnSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(textField.getText().equals(""))
				{
					JOptionPane.showMessageDialog(null, "Enter the name of the farmer first, Resetting table.");
					try
					{
						/*String que1 = "SELECT Name, Weight, Cost, Quantity,PhotoPath,Farmer_Name FROM Crops where Availability='Yes' ";
						PreparedStatement ppst1 = conn.prepareStatement(que1);
						ResultSet st1 = ppst1.executeQuery();
						table.setModel(DbUtils.resultSetToTableModel(st1));*/

						DefaultTableModel dTableModel = requestSender.getCustomerViewCrops();
						if(dTableModel == null){
							throw new Exception("An error occurred");
						}else{
							table.setModel(dTableModel);
						}
						
						/*ppst1.close();
						st1.close();*/
					}
					catch(Exception ex)
					{
						JOptionPane.showMessageDialog(null, ex);
					}
				}
				else
				{
					try
					{
						/*String que1 = "SELECT Name, Weight, Cost, Quantity,PhotoPath,Farmer_Name FROM Crops where Availability='Yes' AND LOWER(Farmer_Name) LIKE LOWER('"+textField.getText()+"') ";
						PreparedStatement ppst1 = conn.prepareStatement(que1);
						ResultSet st1 = ppst1.executeQuery();
						table.setModel(DbUtils.resultSetToTableModel(st1));*/

						DefaultTableModel sTableModel = requestSender.getCustomerViewCropsFarmerSearch(textField.getText());
						if(sTableModel == null){
							throw new Exception("An error occurred");
						}else{
							table.setModel(sTableModel);
						}
						
						/*ppst1.close();
						st1.close();*/
					}
					catch(Exception ex)
					{
						JOptionPane.showMessageDialog(null, ex);
					}
				}
			}
		});
		btnSearch.setBounds(424, 83, 142, 37);
		panelView.add(btnSearch);
		btnSearch.setFont(new Font("Times New Roman", Font.PLAIN, 15));
		
		JButton btnAddBasket = new JButton("Add to Basket");
		btnAddBasket.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				 try{
					 if(table.getSelectedRowCount()==0)
					  {
					  	 JOptionPane.showMessageDialog(null, "Please make a selection first");
					  }
					 else
					 {
					  
					    String value = JOptionPane.showInputDialog("Enter Quantity: ");
				        if (value == null)
				        {
				        	//Cancel stuff
				        	JOptionPane.showMessageDialog(null, "Item not added to basket");
				        }
				        else
				        {
				        	int check=0;
				        	try{
				        		check=Integer.parseInt(value);
				        	}
				        	catch(Exception e1)
				        	{
				        		JOptionPane.showMessageDialog(null, "Invalid Input, Please try again");
				        	}
				        	
				        	if((check/1)!=check)
				        	{
				        		JOptionPane.showMessageDialog(null, "Invalid Input, Please try again");
				        	}
				        	if(check>crops.getQuantity())
				        	{
				        		JOptionPane.showMessageDialog(null, "Not enough in stock please enter a smaller amount");
				        	}
				        	else
				        	{
				        		//String total ="Select * from CustomerBasket where CustomerID='"+customer1.getCusID()+"'";
								//String query0 = "INSERT INTO CustomerBasket (CustomerID,Name,Quantity,Unit_Cost,Total,Farmer) VALUES(?,?,?,?,?,?)";
								
				        		try
								  {
				        			//PreparedStatement pst0 = conn.prepareStatement(query0);
									
									/*pst0.setInt(1, customer.getCusID());
									pst0.setString(2, crops.getcName());
									pst0.setInt(3, check);
									pst0.setDouble(4, crops.getCost());
									pst0.setDouble(5, (check*crops.getCost()));
									pst0.setString( 6, crops.getFarmerName());
									pst0.execute();*/

									Crop temp = new Crop();
									temp.setCid(customer.getCusID());
									temp.setcName(crops.getcName());
									temp.setQuantity(check);
									temp.setCost(crops.getCost());
									temp.setFarmerName(crops.getFarmerName());

									Response<Boolean> rAddBasket = requestSender.addCropToCustomerBasket(temp);
									if(requestSender.responseCheck(rAddBasket)){
										if(rAddBasket.getData()){

										}else{
											throw new Exception("Error adding to basket");
										}
									}else{
										throw new Exception(rAddBasket.getErrorMessage());
									}
									
									
									/*PreparedStatement pst7 = conn.prepareStatement(total);
									ResultSet rst12 = pst7.executeQuery();*/
									Total=0.0;
									/*while(rst12.next())
									{
											Total=(Total + rst12.getDouble(6));
									}*/
									Response<Double> responseTotal = requestSender.getCustomerTotalBasket(customer1.getCusID());
									if(requestSender.responseCheck(responseTotal)){
										Total = responseTotal.getData();
									}else{
										throw new Exception(responseTotal.getErrorMessage());
									}
									lbTotal.setText(Total.toString());
									customer1.setTotal(Total);
									
									/*pst7.close();
									rst12.close();
									pst0.close();
									rst12.close();*/
									JOptionPane.showMessageDialog(null, "Crop Successfully added to your Basket");
									
								  }
								  catch(Exception e2){
										e2.printStackTrace();
								  }
							   
				          	}
				        }
					  }
					 
					 
				     }
					 catch(Exception ex)
					 {
					   JOptionPane.showMessageDialog(null, "Invalid Input, please try adding again");
					 }
			}
		});
		btnAddBasket.setBounds(318, 367, 142, 37);
		panelView.add(btnAddBasket);
		btnAddBasket.setFont(new Font("Times New Roman", Font.PLAIN, 15));
		

		
		table = new JTable();
		scrollPane.setViewportView(table);
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
			DefaultTableModel mod = (DefaultTableModel)table.getModel();
			int selectedRowIndex = table.getSelectedRow();
			try{
				
				crops.setcName(mod.getValueAt(selectedRowIndex, 0).toString());
				crops.setCost(Double.parseDouble(mod.getValueAt(selectedRowIndex, 2).toString()));
				crops.setQuantity(Integer.parseInt(mod.getValueAt(selectedRowIndex, 3).toString()));
				crops.setFarmerName(mod.getValueAt(selectedRowIndex, 5).toString());
				
				
			}
			catch(Exception e)
			{
				System.out.println("Here");
				e.printStackTrace();
			}
		  }
		});
	
		//Table for Basket
		JScrollPane scrollPane1 = new JScrollPane();
		scrollPane1.setBounds(20, 106, 546, 272);
		//panelView.add(scrollPane1);
		
		table_1 = new JTable();
		scrollPane1.setViewportView(table_1);
		table_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
			DefaultTableModel mod = (DefaultTableModel)table_1.getModel();
			int selectedRowIndex = table_1.getSelectedRow();
			btnChangeQuantity.setEnabled(true);
			btnRemoveItem.setEnabled(true);
			btnNewButton_3.setEnabled(true);
			try{
				
				Ccheckout.setcName(mod.getValueAt(selectedRowIndex, 0).toString());
				Ccheckout.setCost(Double.parseDouble(mod.getValueAt(selectedRowIndex, 2).toString()));
				Ccheckout.setQuantity(Integer.parseInt(mod.getValueAt(selectedRowIndex, 1).toString()));
				Ccheckout.setFarmerName(mod.getValueAt(selectedRowIndex, 4).toString());
				
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		});
		scrollPane1.setBounds(20, 83, 546, 252);
		panelVBasket.add(scrollPane1);
		
		
		
		JButton btnLogOut = new JButton("Log Out");
		btnLogOut.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if ((btnViewCrops.isEnabled()==false)||(btnViewBasket.isEnabled()==false)||(btnChkOut.isEnabled()==false)||(btnLChat.isEnabled()==false)||(btn_AddF.isEnabled()==false) || isChatActive)
				{
					JOptionPane.showMessageDialog(null, "Please finish or cancel current processes before logging out");
				}
				else
				{
					int choice = JOptionPane.showConfirmDialog(null,"Are you sure you want to logout?");
					if (choice == 0)
					{
						requestSender.logout();
						dispose();
						Login l = new Login();
						l.setVisible(true);
					}
					else
					{}
				}
			}
		});
		btnLogOut.setFont(new Font("Times New Roman", Font.PLAIN, 15));
		btnLogOut.setBounds(443, 442, 142, 37);
		contentPane.add(btnLogOut);
	
		btnLChat.setFont(new Font("Times New Roman", Font.PLAIN, 15));
		btnLChat.setBounds(33, 442, 142, 37);
		contentPane.add(btnLChat);

		
		btnViewBasket.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				panelView.setVisible(false);
				panelVBasket.setVisible(true);
				panelMain.setVisible(false);

				endLiveChat();
				
				btnViewBasket.setBackground(Color.green);
				
				btn_AddF.setEnabled(false);
				btnLChat.setEnabled(false);
				btnChkOut.setEnabled(false);
				btnViewBasket.setEnabled(false);
				btnViewCrops.setEnabled(false);
				
				try
				{
					/*String query1 = "SELECT Name,Quantity, Unit_Cost,Total, Farmer FROM CustomerBasket where CustomerID='"+customer1.getCusID() +"' ";
					PreparedStatement pst1 = conn.prepareStatement(query1);
					ResultSet rst1 = pst1.executeQuery();
					table_1.setModel(DbUtils.resultSetToTableModel(rst1));
					
					pst1.close();
					rst1.close();*/

					DefaultTableModel tableModel = requestSender.customerViewBasket(customer1.getCusID());
					if(tableModel == null) {
						//set error
						throw new Exception("Error viewing basket");
					}else{
						table_1.setModel(tableModel);
					}
				}
				catch(Exception ex)
				{
					JOptionPane.showMessageDialog(null, ex);
				}
				
			}
		});
		btnViewBasket.setFont(new Font("Times New Roman", Font.PLAIN, 15));
		btnViewBasket.setBounds(33, 346, 142, 37);
		contentPane.add(btnViewBasket);
		
		
		btnChkOut.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				panelVBasket.setVisible(true);
				panelMain.setVisible(false);

				endLiveChat();
				
				btnChkOut.setBackground(Color.green);
				
				btn_AddF.setEnabled(false);
				btnLChat.setEnabled(false);
				btnChkOut.setEnabled(false);
				btnViewBasket.setEnabled(false);
				btnViewCrops.setEnabled(false);
				
				try
				{
					/*String query1 = "SELECT Name,Quantity, Unit_Cost,Total, Farmer FROM CustomerBasket where CustomerID='"+customer1.getCusID() +"' ";
					PreparedStatement pst1 = conn.prepareStatement(query1);
					ResultSet rst1 = pst1.executeQuery();
					table_1.setModel(DbUtils.resultSetToTableModel(rst1));
					
					pst1.close();
					rst1.close();*/

					DefaultTableModel tableModel = requestSender.customerViewBasket(customer1.getCusID());
					if(tableModel == null) {
						//set error
						throw new Exception("Error with Checkout");
					}else{
						table_1.setModel(tableModel);
					}
				}
				catch(Exception ex)
				{
					JOptionPane.showMessageDialog(null, ex);
				}
			}
		});
		btnChkOut.setFont(new Font("Times New Roman", Font.PLAIN, 15));
		btnChkOut.setBounds(33, 394, 142, 37);
		contentPane.add(btnChkOut);
		
		btnViewCrops.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				endLiveChat();

				panelView.setVisible(true);
				panelMain.setVisible(false);
				btnViewCrops.setBackground(Color.green);
				
				btn_AddF.setEnabled(false);
				btnLChat.setEnabled(false);
				btnChkOut.setEnabled(false);
				btnViewBasket.setEnabled(false);
				btnViewCrops.setEnabled(false);
				
				try
				{
					/*String query1 = "SELECT Name, Weight, Cost, Quantity,PhotoPath,Farmer_Name FROM Crops where Availability='Yes' ";
					PreparedStatement pst1 = conn.prepareStatement(query1);
					ResultSet rst1 = pst1.executeQuery();
					table.setModel(DbUtils.resultSetToTableModel(rst1));
					
					pst1.close();
					rst1.close();*/

					DefaultTableModel dTableModel = requestSender.getCustomerViewCrops();
					if(dTableModel == null){
						throw new Exception("An error occurred");
					}else{
						table.setModel(dTableModel);
					}
				}
				catch(Exception ex)
				{
					JOptionPane.showMessageDialog(null, ex);
				}
			}
		});
		btnViewCrops.setFont(new Font("Times New Roman", Font.PLAIN, 15));
		btnViewCrops.setBounds(33, 298, 142, 37);
		contentPane.add(btnViewCrops);
		
		JButton btnVBClose = new JButton("Close");
		btnVBClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				panelView.setVisible(false);
				panelVBasket.setVisible(false);
				btnViewBasket.setEnabled(true);
				btnChkOut.setEnabled(true);
				panelMain.setVisible(true);

				endLiveChat();
				
				btnViewBasket.setBackground(null);
				btnChkOut.setBackground(null);
				
				btn_AddF.setEnabled(true);
				btnLChat.setEnabled(true);
				btnChkOut.setEnabled(true);
				btnViewBasket.setEnabled(true);
				btnViewCrops.setEnabled(true);
				
			}
		});
		btnVBClose.setFont(new Font("Times New Roman", Font.PLAIN, 15));
		btnVBClose.setBounds(488, 366, 78, 36);
		panelVBasket.add(btnVBClose);
		
		JLabel lblNewLabel_1 = new JLabel("Total:   $");
		lblNewLabel_1.setForeground(Color.WHITE);
		lblNewLabel_1.setFont(new Font("Times New Roman", Font.PLAIN, 20));
		lblNewLabel_1.setBounds(414, 334, 78, 21);
		panelVBasket.add(lblNewLabel_1);
		
		btnVCClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				panelMain.setVisible(true);
				panelView.setVisible(false);
				btnViewCrops.setEnabled(true);

				endLiveChat();

				
				btnViewCrops.setBackground(null);
				
				btn_AddF.setEnabled(true);
				btnLChat.setEnabled(true);
				btnChkOut.setEnabled(true);
				btnViewBasket.setEnabled(true);
				btnViewCrops.setEnabled(true);
			}
		});
		btnVCClose.setFont(new Font("Times New Roman", Font.PLAIN, 15));
		btnVCClose.setBounds(470, 367, 96, 37);
		panelView.add(btnVCClose);

		contentPane.add(chat); /*ADD LIVE CHAT*/
		
		ImageIcon icon2 = new ImageIcon("Images\\crbg.jpg");
		JLabel label_1 = new JLabel("");
		label_1.setIcon(icon2);
		label_1.setBounds(0, 0, 804, 497);
		contentPane.add(label_1);

		btnLChat.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!isChatActive){
					panelMain.setVisible(false);
					panelView.setVisible(false);
					btnViewCrops.setEnabled(true);
					startLiveChat();

					btn_AddF.setEnabled(true);
					btnChkOut.setEnabled(true);
					btnViewBasket.setEnabled(true);
					btnViewCrops.setEnabled(true);
				}else{
					endLiveChat(panelMain);
				}
			}
		});
	
		
	
	}

	private void startLiveChat(){
		chat.startLiveChat();
		contentPane.getRootPane().setDefaultButton(chat.getSendButton());
		isChatActive = true;
		btnLChat.setText("Close Live Chat");
		//btnLChat.setEnabled(false);
		//btnLChat.setBackground(Color.green);
	}

	private void endLiveChat(){
		if(isChatActive) {
			chat.stopLiveChat();
			//btnLChat.setBackground(null);
			//btnLChat.setEnabled(true);
			contentPane.getRootPane().setDefaultButton(null);
			isChatActive = false;
			btnLChat.setText("Live Chat");
		}
	}

	private void endLiveChat(JPanel panelMain){
		if(isChatActive){
			panelMain.setVisible(true);
			endLiveChat();
		}
	}
}
