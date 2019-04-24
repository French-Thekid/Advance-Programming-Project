package gui;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.sql.PreparedStatement;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;

import client.RequestSender;
import communication.Response;
import model.Crop;
import model.Farmer;
import model.Person;
//import net.proteanit.sql.DbUtils;
//import server.Server;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Farmer_DashBoard extends Login{

	private RequestSender requestSender = RequestSender.getInstance();

	private JFrame frmFreeAgrirelationsManagement;
	private JTextField tfCName;
	private JTextField tfWeight;
	private JTextField tfCost;
	private JTextField tfQuantity;
	private JTable tb_Update;
	private JTextField tfTCName;
	private JTextField tfTCost;
	private JTextField tfTWeight;
	private JTextField tfTQuantity;
	private JTable tb_VCrops;
	private JTable tbVCus;
	Crop crops = new Crop();
	Farmer farmer = new Farmer();

	FarmerLiveChatView chat = new FarmerLiveChatView();
	JPanel panelMain;
	JButton btnLiveChat;
	boolean isChatActive = false;

	private String filename;
	private String filename1;
	boolean check = false;
	int temp_ID;
	
	Login login = new Login();
	static Person Tempperson = new Person();

	String tempID;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					
					Farmer_DashBoard window = new Farmer_DashBoard(Tempperson);
					window.frmFreeAgrirelationsManagement.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	//Connection conn = null;
	private JTable tb_UpdateC;
	/**
	 * Create the application.
	 */
	public Farmer_DashBoard() {
		initialize();
	}
	
	public Farmer_DashBoard(Person person) {
		Tempperson = person;
		initialize();
	}
	
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
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		//conn = Server.ServerConnection();
		frmFreeAgrirelationsManagement = new JFrame();
		frmFreeAgrirelationsManagement.setTitle("Free Agri-Relations Management (FARM)");
		frmFreeAgrirelationsManagement.setBounds(100, 100, 810, 526);
		frmFreeAgrirelationsManagement.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmFreeAgrirelationsManagement.getContentPane().setLayout(null);
		
		JSeparator separator = new JSeparator();
		separator.setOrientation(SwingConstants.VERTICAL);
		separator.setBounds(205, 25, 2, 454);
		frmFreeAgrirelationsManagement.getContentPane().add(separator);
		
		JLabel lblJohnBrown = new JLabel(Tempperson.getFirstName()+" "+Tempperson.getLastName());
		lblJohnBrown.setForeground(Color.WHITE);
		lblJohnBrown.setHorizontalAlignment(SwingConstants.CENTER);
		lblJohnBrown.setFont(new Font("Times New Roman", Font.BOLD, 16));
		lblJohnBrown.setBounds(47, 148, 115, 26);
		frmFreeAgrirelationsManagement.getContentPane().add(lblJohnBrown);
		
		CreateAccount ca = new CreateAccount();
		
		Border border = BorderFactory.createLineBorder(Color.GREEN, 1);
		JLabel label = new JLabel("");
		label.setBorder(border);
		label.setBounds(10, 25, 178, 127);
		
		ImageIcon icon1;
		byte[] cropimage;
		cropimage = Tempperson.getPhoto1();//  rst1.getBytes(6);
			
			try {
				icon1=new ImageIcon(scaleImage(178, 127, ImageIO.read(new ByteArrayInputStream(cropimage))));
				label.setIcon(icon1);
			
			}
			catch (Exception e) {

				e.printStackTrace();
			}
			JLabel lblNewLabel = new JLabel("");
			try
			{
				/*String name= (Tempperson.getFirstName()+" "+Tempperson.getLastName());
				String q1 = "SELECT * FROM Farmer_Customers WHERE Farmer='"+name+"' ";
				PreparedStatement ps = conn.prepareStatement(q1);
				ResultSet rs = ps.executeQuery();*/


				double Earning=0.0;

				/*while(rs.next())
				{
					Earning = Earning + rs.getDouble(3);
				}*/
				Response<Double> response = requestSender.updateFarmerEarnings((Farmer) Tempperson);
				if(requestSender.responseCheck(response)){
					Earning = response.getData();
				}else {
					throw new Exception("Error updating farmer earnings. "+response.getErrorMessage());
				}

				Tempperson.setMoney(Earning);			
				/*String UpdateDB = "Update Farmer set Earnings='"+Tempperson.getMoney()+"' WHERE ID='"+Tempperson.getID()+"' ";
				PreparedStatement preS = conn.prepareStatement(UpdateDB);
				preS.executeUpdate();*/
				
				lblNewLabel.setText("$"+Tempperson.getMoney());
				
				/*ps.close();
				rs.close();*/
			}
			catch(Exception ex)
			{
				ex.printStackTrace();
				JOptionPane.showMessageDialog(null, ex);
			}
		frmFreeAgrirelationsManagement.getContentPane().add(label);
		
		
		lblNewLabel.setForeground(Color.WHITE);
		lblNewLabel.setFont(new Font("Times New Roman", Font.ITALIC, 13));
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setBounds(57, 185, 84, 14);
		frmFreeAgrirelationsManagement.getContentPane().add(lblNewLabel);
		
		
        JButton btnUCrop = new JButton("Update Crops");
		JButton btnAddC = new JButton("Add Crops");
		JButton btnVCustomers = new JButton("View Customers");
		btnLiveChat = new JButton("Live Chat");
		JButton btnVCrops = new JButton("View Crops");
		
		
		JSeparator separator_1 = new JSeparator();
		separator_1.setBounds(9, 237, 186, 2);
		frmFreeAgrirelationsManagement.getContentPane().add(separator_1);

		JPanel panelViewCus = new JPanel();
		JPanel panelViewCrops = new JPanel();

		btnLiveChat.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!isChatActive){
					panelMain.setVisible(false);
					panelViewCus.setVisible(false);
					panelViewCrops.setVisible(false);
					startLiveChat();

					btnAddC.setEnabled(true);
					btnUCrop.setEnabled(true);
					btnVCrops.setEnabled(true);
					btnVCustomers.setEnabled(true);
				}else{
					endLiveChat(panelMain);
				}
			}
		});
		btnLiveChat.setFont(new Font("Times New Roman", Font.PLAIN, 15));
		btnLiveChat.setBounds(33, 442, 142, 37);
		frmFreeAgrirelationsManagement.getContentPane().add(btnLiveChat);
		
		JLabel lblAnthonybrownymailcom = new JLabel(Tempperson.getEmail().toLowerCase());
		lblAnthonybrownymailcom.setForeground(Color.WHITE);
		lblAnthonybrownymailcom.setHorizontalAlignment(SwingConstants.CENTER);
		lblAnthonybrownymailcom.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		lblAnthonybrownymailcom.setBounds(10, 200, 185, 26);
		frmFreeAgrirelationsManagement.getContentPane().add(lblAnthonybrownymailcom);
		
		JLabel lblbDaiseyAvenue = new JLabel(Tempperson.getAddress());
		lblbDaiseyAvenue.setForeground(Color.WHITE);
		lblbDaiseyAvenue.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		lblbDaiseyAvenue.setHorizontalAlignment(SwingConstants.CENTER);
		lblbDaiseyAvenue.setBounds(10, 222, 185, 19);
		frmFreeAgrirelationsManagement.getContentPane().add(lblbDaiseyAvenue);

		panelMain = new JPanel();
		panelMain.setBackground(new Color(0.0f, 0.0f, 0.0f, 0.5f));
		panelMain.setBounds(217, 25, 567, 406);
		frmFreeAgrirelationsManagement.getContentPane().add(panelMain);
		panelMain.setLayout(null);
		
		ImageIcon i = new ImageIcon("Images\\log.png");
		JLabel lbMLOGO = new JLabel("");
		lbMLOGO.setBounds(0, 85, 557, 224);
		lbMLOGO.setIcon(i);
		panelMain.add(lbMLOGO);
		panelMain.setVisible(true);
		

		panelViewCus.setBackground(new Color(0.0f, 0.0f, 0.0f, 0.5f));
		panelViewCus.setBounds(217, 25, 567, 406);
		frmFreeAgrirelationsManagement.getContentPane().add(panelViewCus);
		panelViewCus.setLayout(null);
		panelViewCus.setVisible(false);

		frmFreeAgrirelationsManagement.getContentPane().add(chat);
		

		JScrollPane SP = new JScrollPane();
		SP.setBounds(10, 86, 547, 273);
		panelViewCus.add(SP);
		
		tbVCus = new JTable();
		SP.setViewportView(tbVCus);
		tbVCus.setEnabled(false);
		tbVCus.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		
		

		panelViewCrops.setBackground(new Color(0.0f, 0.0f, 0.0f, 0.5f));
		panelViewCrops.setBounds(217, 25, 567, 406);
		frmFreeAgrirelationsManagement.getContentPane().add(panelViewCrops);
		panelViewCrops.setLayout(null);
		panelViewCrops.setVisible(false);
		
		btnVCustomers.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				endLiveChat();
				btnAddC.setEnabled(true);
				btnUCrop.setEnabled(true);
				btnVCrops.setEnabled(true);
				btnLiveChat.setEnabled(true);
				btnVCustomers.setEnabled(false);
				
				panelViewCus.setVisible(true);
				panelViewCrops.setVisible(false);
				panelMain.setVisible(false);
				
				try
				{
					String name= (Tempperson.getFirstName()+" "+Tempperson.getLastName());
                    //RequestSender.getInstance()
					/*String q = "SELECT Customers,Amount_Spent FROM Farmer_Customers WHERE Farmer='"+name+"' ";
					
					PreparedStatement ps = conn.prepareStatement(q);
					ResultSet rs = ps.executeQuery();
					tbVCus.setModel(DbUtils.resultSetToTableModel(rs));*/

                    DefaultTableModel tableModel = requestSender.getFarmerCustomers(name);
					if(tableModel == null) {
                        //set error
                    }else{
                        tbVCus.setModel(tableModel);
                    }
					//ps.close();
					//rs.close();
				}
				catch(Exception ex)
				{
					ex.printStackTrace();
					JOptionPane.showMessageDialog(null, ex);
				}
			}
		});
		btnVCustomers.setFont(new Font("Times New Roman", Font.PLAIN, 15));
		btnVCustomers.setBounds(33, 394, 142, 37);
		frmFreeAgrirelationsManagement.getContentPane().add(btnVCustomers);
		
		JSeparator separator_6 = new JSeparator();
		separator_6.setBounds(10, 73, 547, 2);
		panelViewCus.add(separator_6);
		
		ImageIcon icon01 = new ImageIcon("Images\\viewC.png");
		JLabel lb_CLogo = new JLabel("");
		lb_CLogo.setIcon(icon01);
		lb_CLogo.setBounds(169, 11, 288, 63);
		panelViewCus.add(lb_CLogo);
		
		JButton btnClose = new JButton("Close");
		btnClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				panelViewCus.setVisible(false);
				panelMain.setVisible(true);
				btnVCustomers.setEnabled(true);
			}
		});
		btnClose.setFont(new Font("Times New Roman", Font.PLAIN, 15));
		btnClose.setBounds(438, 370, 89, 23);
		panelViewCus.add(btnClose);
		
		JSeparator separator_5 = new JSeparator();
		separator_5.setBounds(10, 72, 547, 2);
		panelViewCrops.add(separator_5);
		
		ImageIcon icon00 = new ImageIcon("Images\\view.png");
		JLabel lb_VLogo = new JLabel("");
		lb_VLogo.setIcon(icon00);
		lb_VLogo.setBounds(120, 11, 333, 63);
		panelViewCrops.add(lb_VLogo);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 85, 547, 272);
		panelViewCrops.add(scrollPane);
		
		tb_VCrops = new JTable();
		scrollPane.setViewportView(tb_VCrops);
		tb_VCrops.setEnabled(false);
		tb_VCrops.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		
		JButton btnVClose = new JButton("Close");
		btnVClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				panelViewCrops.setVisible(false);
				btnVCrops.setEnabled(true);
				panelViewCus.setVisible(false);
				panelMain.setVisible(true);
				
			}
		});
		btnVClose.setFont(new Font("Times New Roman", Font.PLAIN, 14));
		btnVClose.setBounds(438, 368, 89, 23);
		panelViewCrops.add(btnVClose);
		
		JPanel panelUpdate = new JPanel();
		panelUpdate.setBackground(new Color(0.0f, 0.0f, 0.0f, 0.5f));
		panelUpdate.setBounds(217, 25, 567, 406);
		frmFreeAgrirelationsManagement.getContentPane().add(panelUpdate);
		panelUpdate.setLayout(null);
		panelUpdate.setVisible(false);
		
		JSeparator separator_4 = new JSeparator();
		separator_4.setBounds(10, 72, 547, 2);
		panelUpdate.add(separator_4);
		
		ImageIcon icon0 = new ImageIcon("Images\\update.png");
		JLabel lbl_UpdateLogo = new JLabel("");
		lbl_UpdateLogo.setIcon(icon0);
		lbl_UpdateLogo.setBounds(125, 11, 333, 63);
		panelUpdate.add(lbl_UpdateLogo);
		
		String[] types1 = { "-Select One-", "Available", "Unavailable"};
		JComboBox cmbTAvail = new JComboBox(types1);
		cmbTAvail.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		cmbTAvail.setBounds(113, 367, 134, 20);
		panelUpdate.add(cmbTAvail);
		
		
		JLabel lbTCImage = new JLabel("");
		lbTCImage.setBorder(border);
		lbTCImage.setBounds(365, 238, 135, 63);
		panelUpdate.add(lbTCImage);
		
		JLabel lblCropName_1 = new JLabel("Crop Name");
		lblCropName_1.setForeground(Color.WHITE);
		lblCropName_1.setFont(new Font("Times New Roman", Font.PLAIN, 15));
		lblCropName_1.setBounds(10, 238, 86, 21);
		panelUpdate.add(lblCropName_1);
		
		JLabel lblWeightInPounds = new JLabel("Weight (in lbs)");
		lblWeightInPounds.setForeground(Color.WHITE);
		lblWeightInPounds.setFont(new Font("Times New Roman", Font.PLAIN, 15));
		lblWeightInPounds.setBounds(10, 270, 86, 21);
		panelUpdate.add(lblWeightInPounds);
		
		JLabel lblCost = new JLabel("Cost/unit");
		lblCost.setForeground(Color.WHITE);
		lblCost.setFont(new Font("Times New Roman", Font.PLAIN, 15));
		lblCost.setBounds(10, 302, 73, 21);
		panelUpdate.add(lblCost);
		
		JLabel lblQuantity_1 = new JLabel("Quantity");
		lblQuantity_1.setForeground(Color.WHITE);
		lblQuantity_1.setFont(new Font("Times New Roman", Font.PLAIN, 15));
		lblQuantity_1.setBounds(10, 334, 73, 21);
		panelUpdate.add(lblQuantity_1);
		
		JLabel lblAvailable = new JLabel("Availability");
		lblAvailable.setForeground(Color.WHITE);
		lblAvailable.setFont(new Font("Times New Roman", Font.PLAIN, 15));
		lblAvailable.setBounds(10, 366, 73, 21);
		panelUpdate.add(lblAvailable);
		
		tfTCName = new JTextField();
		tfTCName.setHorizontalAlignment(SwingConstants.CENTER);
		tfTCName.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		tfTCName.setBounds(113, 239, 134, 20);
		panelUpdate.add(tfTCName);
		tfTCName.setColumns(10);
		
		tfTCost = new JTextField();
		tfTCost.setHorizontalAlignment(SwingConstants.CENTER);
		tfTCost.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		tfTCost.setColumns(10);
		tfTCost.setBounds(113, 303, 134, 20);
		panelUpdate.add(tfTCost);
		
		tfTWeight = new JTextField();
		tfTWeight.setHorizontalAlignment(SwingConstants.CENTER);
		tfTWeight.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		tfTWeight.setColumns(10);
		tfTWeight.setBounds(113, 271, 134, 20);
		panelUpdate.add(tfTWeight);
		
		tfTQuantity = new JTextField();
		tfTQuantity.setHorizontalAlignment(SwingConstants.CENTER);
		tfTQuantity.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		tfTQuantity.setColumns(10);
		tfTQuantity.setBounds(113, 335, 134, 20);
		panelUpdate.add(tfTQuantity);
		
		JButton btnNewButton = new JButton("Upload Image");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
				    JFileChooser chooser = new JFileChooser();
				    chooser.showOpenDialog(null);
				    File f = chooser.getSelectedFile();
				    filename = f.getAbsolutePath();
//				    textField.setText(filename);
				    
				        ImageIcon ii=new ImageIcon(scaleImage(135, 63, ImageIO.read(new File(f.getAbsolutePath()))));//get the image from file chooser and scale it to match JLabel size
				        lbTCImage.setIcon(ii);
				        check = true;
				    }catch (Exception ex) {
				        JOptionPane.showMessageDialog(null, "Please select an uncorrupted image");
				    }
			}
		});
		btnNewButton.setFont(new Font("Times New Roman", Font.PLAIN, 14));
		btnNewButton.setBounds(365, 314, 138, 23);
		panelUpdate.add(btnNewButton);
		
		JButton btnNewButton_1 = new JButton("Update");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if ((cmbTAvail.getSelectedIndex()==0)||(lbTCImage.getIcon()==null)||(tfTQuantity.getText().equals(""))||(tfTCost.getText().equals(""))||(tfTWeight.getText().equals(""))||(tfTCName.getText().equals("")))
				{
					   JOptionPane.showMessageDialog(null, "Ensure that all fields are filled");
				}
				else
				{
					try
					  {
					    crops.setcName(tfTCName.getText());
						crops.setQuantity(Integer.parseInt(tfTQuantity.getText()));
						crops.setCost(Double.parseDouble(tfTCost.getText()));
						crops.setWeight(Float.parseFloat(tfTWeight.getText()));
						if (cmbTAvail.getSelectedIndex()==1)
						{
							crops.setAvailable("Yes");
						}else
						{
							crops.setAvailable("No");
						}
						PreparedStatement pst0;
						Response<Boolean> response;
						if (check == true)
						  {
						  	check = false;
								crops.setImagePath(lbTCImage.getIcon().toString());
								
								File image = new File(filename);
						        FileInputStream fis = new FileInputStream ( image );
						        byte[] userimage;
						        
						        ByteArrayOutputStream baos= new ByteArrayOutputStream();
						        byte[] buff = new byte[1024];
						        for(int readNum; (readNum=fis.read(buff)) !=-1 ; ){
						            baos.write(buff,0,readNum);
						        }
						        userimage=baos.toByteArray();
						        
								/*String query0 = "UPDATE Crops SET Name='"+crops.getcName()+"' ,Weight='"+crops.getWeight()+"',Cost='"+crops.getCost()+"',Quantity='"+crops.getQuantity()+"',Availability='"+crops.getAvailable()+"',Photopath=? WHERE CID ='"+temp_ID+"'";
								pst0 = conn.prepareStatement(query0);
								pst0.setBytes(1, userimage);
								
								pst0.execute();*/

							  Crop temp = new Crop();
							  temp.setcName(crops.getcName());
							  temp.setQuantity(crops.getQuantity());
							  temp.setCost(crops.getCost());
							  temp.setWeight(crops.getWeight());
							  temp.setAvailable(crops.getAvailable());
							  System.out.println("crops is "+crops.getcName()+" temp is "+temp.getcName());
							  temp.setCid(temp_ID);
								response = requestSender.updateCropWithImage(temp, userimage);
							
						  }
						  else
					      {
							  /*String query0 = "UPDATE Crops SET Name='"+crops.getcName()+"' ,Weight='"+crops.getWeight()+"',Cost='"+crops.getCost()+"',Quantity='"+crops.getQuantity()+"',Availability='"+crops.getAvailable()+"' WHERE CID ='"+temp_ID+"'";
							  pst0 = conn.prepareStatement(query0);
							  pst0.execute();*/

							  Crop temp = new Crop();
							  temp.setcName(crops.getcName());
							  temp.setQuantity(crops.getQuantity());
							  temp.setCost(crops.getCost());
							  temp.setWeight(crops.getWeight());
							  temp.setAvailable(crops.getAvailable());
							  temp.setCid(temp_ID);
							  response = requestSender.updateCrop(temp);
							 
					      }
						
						
						//pst0.close();
						 
						try
						{
							/*String query1 = "SELECT CID,Name,Weight,Cost,Quantity,Availability,Photopath FROM Crops WHERE FID='"+Tempperson.getID()+"'";
							PreparedStatement pst1 = conn.prepareStatement(query1);
							ResultSet rst1 = pst1.executeQuery();*/
							DefaultTableModel tb = requestSender.getFarmerCropsUpdateCrops(Tempperson.getID());
							if(tb == null){

							}else {
								tb_UpdateC.setModel(tb);
							}
							
							/*pst1.close();
							rst1.close();*/
						}
						catch(Exception ex)
						{
							JOptionPane.showMessageDialog(null, ex);
						}

						if(requestSender.responseCheck(response))
							if(response.getData())
								JOptionPane.showMessageDialog(null, "Crop Successfully Updated");
						tfTCName.setText("");
						tfTWeight.setText("");
						tfTCost.setText("");
						tfTQuantity.setText("");
						cmbTAvail.setSelectedIndex(0);
						lbTCImage.setIcon(null);
						
				      }
					  catch(Exception e){
							JOptionPane.showMessageDialog(null, e);
					  }
				   }
				
			}
		});
		btnNewButton_1.setFont(new Font("Times New Roman", Font.PLAIN, 15));
		btnNewButton_1.setBounds(335, 366, 89, 23);
		panelUpdate.add(btnNewButton_1);
		
		JPanel panel = new JPanel();
		panel.setBackground(new Color(0.0f, 0.0f, 0.0f, 0.5f));
		panel.setBounds(218, 25, 566, 405);
		frmFreeAgrirelationsManagement.getContentPane().add(panel);
		panel.setLayout(null);
		panel.setVisible(false);
		
		btnAddC.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				endLiveChat();
				btnAddC.setEnabled(false);
				btnAddC.setBackground(Color.GREEN);
				btnUCrop.setEnabled(false);
				btnVCrops.setEnabled(false);
				btnLiveChat.setEnabled(false);
				btnVCustomers.setEnabled(false);
				
				panelUpdate.setVisible(false);
				panelViewCrops.setVisible(false);
				panelViewCus.setVisible(false);
				panelMain.setVisible(false);
				panel.setVisible(true);
				
				
				
				
			}
		});
		btnAddC.setFont(new Font("Times New Roman", Font.PLAIN, 15));
		btnAddC.setBounds(33, 250, 142, 37);
		frmFreeAgrirelationsManagement.getContentPane().add(btnAddC);
		
		
		btnUCrop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				endLiveChat();
				btnAddC.setEnabled(false);
				btnUCrop.setBackground(Color.GREEN);
				btnUCrop.setEnabled(false);
				btnVCrops.setEnabled(false);
				btnLiveChat.setEnabled(false);
				btnVCustomers.setEnabled(false);
				
				panelUpdate.setVisible(true);
				panelViewCrops.setVisible(false);
				btnUCrop.setEnabled(false);
				panel.setVisible(false);
				panelViewCus.setVisible(false);
				panelMain.setVisible(false);
				

				try
				{
					/*String query1 = "SELECT CID,Name,Weight,Cost,Quantity,Availability,Photopath FROM Crops WHERE FID='"+Tempperson.getID()+"'";
					PreparedStatement pst1 = conn.prepareStatement(query1);
					ResultSet rst1 = pst1.executeQuery();*/

					DefaultTableModel tb = requestSender.getFarmerCropsUpdateCrops(Tempperson.getID());
					if(tb == null){

					}else {
						tb_UpdateC.setModel(tb);
					}
					
					/*pst1.close();
					rst1.close();*/
				}
				catch(Exception ex)
				{
					JOptionPane.showMessageDialog(null, ex);
				}
				
			}
		});
		btnUCrop.setFont(new Font("Times New Roman", Font.PLAIN, 15));
		btnUCrop.setBounds(33, 298, 142, 37);
		frmFreeAgrirelationsManagement.getContentPane().add(btnUCrop);
		
		JLabel lblCropName = new JLabel("Crop Name:");
		lblCropName.setForeground(Color.WHITE);
		lblCropName.setFont(new Font("Times New Roman", Font.PLAIN, 15));
		lblCropName.setHorizontalAlignment(SwingConstants.CENTER);
		lblCropName.setBounds(10, 128, 107, 23);
		panel.add(lblCropName);
		
		tfCName = new JTextField();
		tfCName.setFont(new Font("Times New Roman", Font.PLAIN, 14));
		tfCName.setBounds(127, 128, 158, 23);
		panel.add(tfCName);
		tfCName.setColumns(10);
		
		tfWeight = new JTextField();
		tfWeight.setFont(new Font("Times New Roman", Font.PLAIN, 14));
		tfWeight.setColumns(10);
		tfWeight.setBounds(127, 184, 158, 23);
		panel.add(tfWeight);
		
		JLabel lblWeightinLbs = new JLabel("Weight (in lbs)");
		lblWeightinLbs.setForeground(Color.WHITE);
		lblWeightinLbs.setHorizontalAlignment(SwingConstants.CENTER);
		lblWeightinLbs.setFont(new Font("Times New Roman", Font.PLAIN, 15));
		lblWeightinLbs.setBounds(10, 184, 107, 23);
		panel.add(lblWeightinLbs);
		
		tfCost = new JTextField();
		tfCost.setFont(new Font("Times New Roman", Font.PLAIN, 14));
		tfCost.setColumns(10);
		tfCost.setBounds(127, 238, 158, 23);
		panel.add(tfCost);
		
		JLabel lblCostPerUnit = new JLabel("Cost per Unit");
		lblCostPerUnit.setForeground(Color.WHITE);
		lblCostPerUnit.setHorizontalAlignment(SwingConstants.CENTER);
		lblCostPerUnit.setFont(new Font("Times New Roman", Font.PLAIN, 15));
		lblCostPerUnit.setBounds(10, 238, 107, 23);
		panel.add(lblCostPerUnit);
		
		JLabel lblQuantity = new JLabel("Quantity");
		lblQuantity.setForeground(Color.WHITE);
		lblQuantity.setHorizontalAlignment(SwingConstants.CENTER);
		lblQuantity.setFont(new Font("Times New Roman", Font.PLAIN, 15));
		lblQuantity.setBounds(10, 294, 107, 23);
		panel.add(lblQuantity);
		
		tfQuantity = new JTextField();
		tfQuantity.setFont(new Font("Times New Roman", Font.PLAIN, 14));
		tfQuantity.setColumns(10);
		tfQuantity.setBounds(127, 294, 158, 23);
		panel.add(tfQuantity);
		
		JLabel lblAvailabilityStatus = new JLabel("Availability Status");
		lblAvailabilityStatus.setForeground(Color.WHITE);
		lblAvailabilityStatus.setHorizontalAlignment(SwingConstants.CENTER);
		lblAvailabilityStatus.setFont(new Font("Times New Roman", Font.PLAIN, 15));
		lblAvailabilityStatus.setBounds(10, 354, 107, 23);
		panel.add(lblAvailabilityStatus);
		
		JSeparator separator_2 = new JSeparator();
		separator_2.setBounds(10, 72, 546, 2);
		panel.add(separator_2);
		
		JSeparator separator_3 = new JSeparator();
		separator_3.setBounds(339, 234, 217, 2);
		panel.add(separator_3);
	
		
		JLabel lbCImage = new JLabel("");
		lbCImage.setBorder(border);
		lbCImage.setBounds(349, 83, 192, 140);
		panel.add(lbCImage);
		

		
		JButton btnUCancel = new JButton("Cancel");
		btnUCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				panelViewCrops.setVisible(false);
				panelUpdate.setVisible(false);
				panelViewCus.setVisible(false);
				panelMain.setVisible(true);
				panel.setVisible(false);
				btnUCrop.setEnabled(true);
				tfTCName.setText("");
				tfTCost.setText("");
				tfTWeight.setText("");
				tfTQuantity.setText("");
				cmbTAvail.setSelectedIndex(0);
				lbTCImage.setIcon(null);
				
				//Button Re- Enable
				btnAddC.setEnabled(true);
				btnUCrop.setBackground(null);
				btnUCrop.setEnabled(true);
				btnVCrops.setEnabled(true);
				btnLiveChat.setEnabled(true);
				btnVCustomers.setEnabled(true);
			}
		});
		btnUCancel.setFont(new Font("Times New Roman", Font.PLAIN, 15));
		btnUCancel.setBounds(436, 366, 89, 23);
		panelUpdate.add(btnUCancel);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(10, 81, 547, 143);
		panelUpdate.add(scrollPane_1);
		
		tb_UpdateC = new JTable();
		scrollPane_1.setViewportView(tb_UpdateC);
		tb_UpdateC.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				DefaultTableModel mod = (DefaultTableModel)tb_UpdateC.getModel();
				int selectedRowIndex = tb_UpdateC.getSelectedRow();
				try{
					tfTCName.setText(mod.getValueAt(selectedRowIndex, 1).toString());
					tfTWeight.setText(mod.getValueAt(selectedRowIndex, 2).toString());
					tfTCost.setText(mod.getValueAt(selectedRowIndex, 3).toString());
					tfTQuantity.setText(mod.getValueAt(selectedRowIndex, 4).toString());
					
		            temp_ID= (int) mod.getValueAt(selectedRowIndex, 0);
					String temp1;
					String temp2;
					int index=-1;
					temp1=mod.getValueAt(selectedRowIndex, 5).toString();
					if (temp1.equalsIgnoreCase("yes"))
					{
						index=1;
					}
					else if (temp1.equalsIgnoreCase("no"))
					{
						index = 2;
					}
					cmbTAvail.setSelectedIndex(index);
					
					ImageIcon icon1;
					byte[] cropimage;
					cropimage = (byte[]) mod.getValueAt(selectedRowIndex, 6);
					try {
						icon1=new ImageIcon(scaleImage(135, 63, ImageIO.read(new ByteArrayInputStream(cropimage))));
						lbTCImage.setIcon(icon1);
						
					}
					catch (Exception e) {

							e.printStackTrace();
					}
				}
				catch(Exception e)
				{
					JOptionPane.showMessageDialog(null, e);
				}
			}
		});
		tb_UpdateC.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		
		btnVCrops.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				endLiveChat();
				btnAddC.setEnabled(true);
				btnUCrop.setEnabled(true);
				btnVCrops.setEnabled(false);
				btnLiveChat.setEnabled(true);
				btnVCustomers.setEnabled(true);
				
				panelViewCrops.setVisible(true);
				panelViewCus.setVisible(false);
				panelMain.setVisible(false);
				
				try
				{
					/*String query1 = "SELECT Name,Weight,Cost,Quantity,Availability,Photopath FROM Crops WHERE FID='"+Tempperson.getID()+"'";
					PreparedStatement pst1 = conn.prepareStatement(query1);
					ResultSet rst1 = pst1.executeQuery();*/

					DefaultTableModel tb = requestSender.getFarmerCropsViewCrops(Tempperson.getID());
					if(tb == null){

					}else {
						tb_VCrops.setModel(tb);
					}
					
					/*pst1.close();
					rst1.close();*/
				}
				catch(Exception ex)
				{
					JOptionPane.showMessageDialog(null, ex);
				}
				
			}
		});
		btnVCrops.setFont(new Font("Times New Roman", Font.PLAIN, 15));
		btnVCrops.setBounds(33, 346, 142, 37);
		frmFreeAgrirelationsManagement.getContentPane().add(btnVCrops);
		
		JButton tbnUpload = new JButton("Upload Image");
		tbnUpload.addActionListener(new ActionListener() {//todo:check this
			public void actionPerformed(ActionEvent arg0) {
				try {
				    JFileChooser chooser = new JFileChooser();
				    chooser.showOpenDialog(null);
				    File f = chooser.getSelectedFile();
				    filename1 = f.getAbsolutePath();
//				    textField.setText(filename);
				    
				        ImageIcon ii=new ImageIcon(scaleImage(192, 140, ImageIO.read(new File(f.getAbsolutePath()))));//get the image from file chooser and scale it to match JLabel size
				        lbCImage.setIcon(ii);
				        
				        
				        //Saving Image to database
				        File image = new File(filename1);
				        FileInputStream fis = new FileInputStream ( image );
				        byte[] userimage;
				        
				        ByteArrayOutputStream baos= new ByteArrayOutputStream();
				        byte[] buff = new byte[1024];
				        for(int readNum; (readNum=fis.read(buff)) !=-1 ; ){
				            baos.write(buff,0,readNum);
				        }
				        userimage=baos.toByteArray();
				        			        
				    }catch (Exception ex) {
				        JOptionPane.showMessageDialog(null, ex);//"Please select an uncorrupted image");
				    }
				}
		});
		tbnUpload.setFont(new Font("Times New Roman", Font.PLAIN, 15));
		tbnUpload.setBounds(380, 247, 132, 23);
		panel.add(tbnUpload);
		
		String[] types = { "-Select One-", "Available", "Unavailable"};
		JComboBox comboBox = new JComboBox(types);
		comboBox.setFont(new Font("Times New Roman", Font.PLAIN, 14));
		comboBox.setBounds(127, 354, 158, 23);
		panel.add(comboBox);
		
		JButton btnAdd = new JButton("Add to Inventory");
		btnAdd.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent arg0) {
			if ((comboBox.getSelectedIndex()==0)||(lbCImage.getIcon()==null)||(tfQuantity.getText().equals(""))||(tfCost.getText().equals(""))||(tfWeight.getText().equals(""))||(tfCName.getText().equals("")))
			{
				   JOptionPane.showMessageDialog(null, "Ensure that all fields are filled");
			}
			else
			{
					//Checking if Item already exist for said farmer
				    int c=0;
				    int quan=0;
				    int id=0;
					/*String search = "SELECT * FROM Crops WHERE FID='"+Tempperson.getID()+"' AND LOWER(Name) LIKE LOWER('"+tfCName.getText()+"') ";
					try{
						PreparedStatement p = conn.prepareStatement(search);
						ResultSet r = p.executeQuery();
					    while(r.next())
						{
					    	quan = r.getInt(5);
							id= r.getInt(1);
					    	c++;
						}
					}
					catch(SQLException s)
					{
						s.printStackTrace();
					}*/
					Response<Integer[]> response = requestSender.checkIfCropAlreadyExistsForFarmer(String.valueOf(Tempperson.getID()), tfCName.getText());
					if(requestSender.responseCheck(response)){
						c = response.getData()[0];
						quan = response.getData()[1];
						id = response.getData()[2];
					}else{
						c = -1;
					}

					if(c==0)
					{
											
					    crops.setcName(tfCName.getText());
						crops.setImagePath(filename1);
						crops.setQuantity(Integer.parseInt(tfQuantity.getText()));
						crops.setCost(Double.parseDouble(tfCost.getText()));
						crops.setWeight(Float.parseFloat(tfWeight.getText()));
						if (comboBox.getSelectedIndex()==1)
						{
							crops.setAvailable("Yes");
						}else
						{
							crops.setAvailable("No");
						}
					 	
						try
						{
							/*String query0 = "INSERT INTO Crops (Name,Weight,Cost,Quantity,Availability,Photopath,FID,Farmer_Name) VALUES(?,?,?,?,?,?,?,?)";
							PreparedStatement pst0 = conn.prepareStatement(query0);*/
							
							File image = new File(crops.getImagePath());
					        FileInputStream fis = new FileInputStream ( image );
					        byte[] userimage;
					        
					        ByteArrayOutputStream baos= new ByteArrayOutputStream();
					        byte[] buff = new byte[1024];
					        for(int readNum; (readNum=fis.read(buff)) !=-1 ; ){
					            baos.write(buff,0,readNum);
					        }
					        userimage=baos.toByteArray();
					        
					        
							
							/*pst0.setString(1, crops.getcName());
							pst0.setString(2, tfWeight.getText());
							pst0.setString(3, crops.getCost().toString());
							pst0.setString(4, tfQuantity.getText());
							pst0.setString(5, crops.getAvailable());
							pst0.setBytes( 6, userimage);
							pst0.setInt(7, Tempperson.getID());
							pst0.setString(8, Tempperson.getFirstName()+" "+Tempperson.getLastName());
							pst0.execute();*/

							Crop temp = new Crop();
							temp.setcName(crops.getcName());
							temp.setWeight(Float.parseFloat(tfWeight.getText()));
							temp.setCost(crops.getCost());
							temp.setQuantity(Integer.parseInt(tfQuantity.getText()));
							temp.setAvailable(crops.getAvailable());
							temp.setFarmerID(String.valueOf(Tempperson.getID()));
							temp.setFarmerName(Tempperson.getFirstName()+" "+Tempperson.getLastName());

							Response<Boolean> response1 = requestSender.addCrop(temp, userimage);

							
							//fis.close();

							if(requestSender.responseCheck(response1))
								if(response1.getData())
									JOptionPane.showMessageDialog(null, "Crop Successfully added to Inventory");
							tfCName.setText("");
							tfWeight.setText("");
							tfCost.setText("");
							tfQuantity.setText("");
							comboBox.setSelectedIndex(0);
							lbCImage.setIcon(null);
							
							//pst0.close();
				         }
					     catch(Exception e){
							  JOptionPane.showMessageDialog(null, e);
					     }
			   }
			   else if(c == -1)
				{

				}
			   else
			   {
				   int option=JOptionPane.showConfirmDialog(null, tfCName.getText()+" is already in your inventory, would you like to Update the Quantity instead?");
					if (option == 0)
					{
					  String value = JOptionPane.showInputDialog("Enter Quantity to add: ");
					        if (value == null)
					        {
					        	//Cancel stuff
					        	JOptionPane.showMessageDialog(null, "Quantity not updated");
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
					        	else
					        	{
					        		//Updating Quantity
					        		/*String q0 = "UPDATE Crops SET Quantity='"+(check+quan)+"' WHERE CID='"+id+"' ";
						        	try
						        	{
										PreparedStatement p0;
							        	p0 = conn.prepareStatement(q0);
										p0.execute();
						        	}
						        	catch(SQLException e)
						        	{
						        		e.printStackTrace();
						        	}*/

					        		Response<Boolean> response1 = requestSender.updateCropQuantity(id, (check+quan));


									if(requestSender.responseCheck(response1))
										if(response1.getData())
					        				JOptionPane.showMessageDialog(null, "Crop Quantity Successfully Updated");
									tfCName.setText("");
									tfWeight.setText("");
									tfCost.setText("");
									tfQuantity.setText("");
									comboBox.setSelectedIndex(0);
									lbCImage.setIcon(null);
									
					        	}
					        }
					  }
					else
					{}
			   }
			 }
			}	
		});	
		btnAdd.setFont(new Font("Times New Roman", Font.PLAIN, 15));
		btnAdd.setBounds(312, 348, 145, 30);
		panel.add(btnAdd);
	
		ImageIcon icon3 = new ImageIcon("Images\\logo.png");
		JLabel lblogo = new JLabel("");
		lblogo.setIcon(icon3);
		lblogo.setBounds(180, 11, 277, 63);
		panel.add(lblogo);
		
		JButton btnAddCancel = new JButton("Cancel");
		btnAddCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				btnAddC.setEnabled(true);
				panelViewCus.setVisible(false);
				panel.setVisible(false);
				panelUpdate.setVisible(false);
				panelViewCrops.setVisible(false);
				panelMain.setVisible(true);
				tfCName.setText("");
				tfCost.setText("");
				tfWeight.setText("");
				tfQuantity.setText("");
				comboBox.setSelectedIndex(0);
				lbCImage.setIcon(null);
				
				
				btnAddC.setEnabled(true);
				btnAddC.setBackground(null);
				btnUCrop.setEnabled(true);
				btnVCrops.setEnabled(true);
				btnLiveChat.setEnabled(true);
				btnVCustomers.setEnabled(true);
			}
		});
		btnAddCancel.setFont(new Font("Times New Roman", Font.PLAIN, 15));
		btnAddCancel.setBounds(465, 348, 91, 30);
		panel.add(btnAddCancel);

		JButton btnLogOut = new JButton("Log Out");
		btnLogOut.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if ((btnAdd.isEnabled()==false)||(btnUCrop.isEnabled()==false)||(btnLiveChat.isEnabled()==false)||(btnLiveChat.isEnabled()==false) || isChatActive)
				{
					JOptionPane.showMessageDialog(null, "Please finish or cancel current processes before logging out");
				}
				else
				{
					int choice = JOptionPane.showConfirmDialog(null,"Are you sure you want to logout?");
					if (choice == 0)
					{
						requestSender.logout();
						frmFreeAgrirelationsManagement.setVisible(false);
						Login l = new Login();
						l.setVisible(true);
					}
					else
					{}
				}
			}
		});
		btnLogOut.setFont(new Font("Times New Roman", Font.PLAIN, 15));
		btnLogOut.setBounds(455, 442, 142, 37);
		frmFreeAgrirelationsManagement.getContentPane().add(btnLogOut);
			
		
		ImageIcon icon2 = new ImageIcon("Images\\crbg.jpg");
		JLabel label_1 = new JLabel("");
		label_1.setIcon(icon2);
		label_1.setBounds(0, 0, 794, 487);
		frmFreeAgrirelationsManagement.getContentPane().add(label_1);
	
		
	
	}

	private void startLiveChat(){
		chat.startLiveChat();
		frmFreeAgrirelationsManagement.getRootPane().setDefaultButton(chat.getSendButton());
		isChatActive = true;
		btnLiveChat.setText("Close Live Chat");
		//btnLChat.setEnabled(false);
		//btnLChat.setBackground(Color.green);
	}

	private void endLiveChat(){
		if(isChatActive) {
			chat.stopLiveChat();
			//btnLChat.setBackground(null);
			//btnLChat.setEnabled(true);
			frmFreeAgrirelationsManagement.getRootPane().setDefaultButton(null);
			isChatActive = false;
			btnLiveChat.setText("Live Chat");
		}
	}

	private void endLiveChat(JPanel panelMain){
		if(isChatActive){
			panelMain.setVisible(true);
			endLiveChat();
		}
	}
}
