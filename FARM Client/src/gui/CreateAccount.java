package gui;

import java.awt.Color;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

import client.RequestSender;
import communication.Response;
import model.Customer;
import model.Farmer;
//import server.Server;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JSeparator;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class CreateAccount extends JFrame {

	private RequestSender requestSender = RequestSender.getInstance();

	private JPanel contentPane;
	private JTextField tfName;
	private JTextField tfSurname;
	private JTextField tfEmail;
	private JTextField tfPassword;
	private JTextField tf_p2;
	private JTextField tf_FAddress;
	private static String Apath;
	int counter = 0;
	
	Farmer farmer =new Farmer();
	Customer customer = new Customer();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					CreateAccount frame = new CreateAccount();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	
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
	
	//Connection conn = null;
	public CreateAccount() {
		
		//conn = Server.ServerConnection();
		final String filename;
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 810, 526);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		

		Border border = BorderFactory.createLineBorder(Color.GREEN, 1);
		JLabel lbPic = new JLabel("");
		lbPic.setBorder(border);
		lbPic.setBounds(517, 90, 218, 183);
		contentPane.add(lbPic);
		
		JButton btnNewButton = new JButton("Upload Image");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				 try {
				    JFileChooser chooser = new JFileChooser();
				    chooser.showOpenDialog(null);
				    File f = chooser.getSelectedFile();
				    String filename = f.getAbsolutePath();
				    setApath(filename);
				    ImageIcon ii=new ImageIcon(scaleImage(218, 183, ImageIO.read(new File(f.getAbsolutePath()))));//get the image from file chooser and scale it to match JLabel size
				    lbPic.setIcon(ii);
				    } catch (Exception ex) {
				        JOptionPane.showMessageDialog(null, "Please select an uncorrupted image");
				    }
			}
		});
		btnNewButton.setFont(new Font("Times New Roman", Font.PLAIN, 20));
		btnNewButton.setBounds(522, 304, 213, 39);
		contentPane.add(btnNewButton);
		
		JSeparator separator = new JSeparator();
		separator.setBounds(479, 284, 292, 9);
		contentPane.add(separator);
		
		
		ImageIcon icon = new ImageIcon("Images\\CR.png");
		JLabel CR_Logo = new JLabel("");
		CR_Logo.setIcon(icon);
		CR_Logo.setBounds(211, 11, 362, 59);
		contentPane.add(CR_Logo);
		
		JSeparator separator_1 = new JSeparator();
		separator_1.setBounds(10, 81, 774, 7);
		contentPane.add(separator_1);
		
		JLabel lblFirstName = new JLabel("First Name:");
		lblFirstName.setForeground(Color.WHITE);
		lblFirstName.setFont(new Font("Times New Roman", Font.PLAIN, 15));
		lblFirstName.setBounds(10, 115, 98, 31);
		contentPane.add(lblFirstName);
		
		tfName = new JTextField();
		tfName.setHorizontalAlignment(SwingConstants.CENTER);
		tfName.setFont(new Font("Times New Roman", Font.PLAIN, 14));
		tfName.setBounds(139, 115, 200, 31);
		contentPane.add(tfName);
		tfName.setColumns(10);
		
		tfSurname = new JTextField();
		tfSurname.setHorizontalAlignment(SwingConstants.CENTER);
		tfSurname.setFont(new Font("Times New Roman", Font.PLAIN, 14));
		tfSurname.setColumns(10);
		tfSurname.setBounds(139, 157, 200, 31);
		contentPane.add(tfSurname);
		
		JLabel lblLastName = new JLabel("Last Name:");
		lblLastName.setForeground(Color.WHITE);
		lblLastName.setFont(new Font("Times New Roman", Font.PLAIN, 15));
		lblLastName.setBounds(10, 157, 98, 31);
		contentPane.add(lblLastName);
		
		tfEmail = new JTextField();
		tfEmail.setHorizontalAlignment(SwingConstants.CENTER);
		tfEmail.setFont(new Font("Times New Roman", Font.PLAIN, 14));
		tfEmail.setColumns(10);
		tfEmail.setBounds(139, 242, 200, 31);
		contentPane.add(tfEmail);
		
		JLabel lblEmail = new JLabel("Email Address:");
		lblEmail.setForeground(Color.WHITE);
		lblEmail.setFont(new Font("Times New Roman", Font.PLAIN, 15));
		lblEmail.setBounds(10, 242, 98, 31);
		contentPane.add(lblEmail);
		
		tfPassword = new JTextField();
		tfPassword.setHorizontalAlignment(SwingConstants.CENTER);
		tfPassword.setFont(new Font("Times New Roman", Font.PLAIN, 14));
		tfPassword.setColumns(10);
		tfPassword.setBounds(139, 284, 200, 31);
		contentPane.add(tfPassword);
		
		JLabel lblPassword = new JLabel("Password:");
		lblPassword.setForeground(Color.WHITE);
		lblPassword.setFont(new Font("Times New Roman", Font.PLAIN, 15));
		lblPassword.setBounds(10, 284, 98, 31);
		contentPane.add(lblPassword);
		
		tf_p2 = new JTextField();
		tf_p2.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent arg0) {
				if (tf_p2.getText().compareTo(tfPassword.getText())!=0)
				{
					JOptionPane.showMessageDialog(null, "Passwords do not match");
				}
			}
		});
		tf_p2.setHorizontalAlignment(SwingConstants.CENTER);
		tf_p2.setFont(new Font("Times New Roman", Font.PLAIN, 14));
		tf_p2.setColumns(10);
		tf_p2.setBounds(139, 326, 200, 31);
		contentPane.add(tf_p2);
		
		JLabel lblVerifyPassword = new JLabel("Verify Password:");
		lblVerifyPassword.setForeground(Color.WHITE);
		lblVerifyPassword.setFont(new Font("Times New Roman", Font.PLAIN, 15));
		lblVerifyPassword.setBounds(10, 326, 119, 31);
		contentPane.add(lblVerifyPassword);
		
		JLabel lblFarmAddress = new JLabel("Farm Address:");
		lblFarmAddress.setForeground(Color.WHITE);
		lblFarmAddress.setFont(new Font("Times New Roman", Font.PLAIN, 15));
		lblFarmAddress.setBounds(10, 424, 119, 31);
		lblFarmAddress.setVisible(false);
		contentPane.add(lblFarmAddress);
		
		
		String[] types = { "-Select One-", "Farmer", "Customer"};
		JComboBox cbType = new JComboBox(types);
		cbType.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (cbType.getSelectedIndex() == 1)
				{
					lblFarmAddress.setVisible(true);
					tf_FAddress.setVisible(true);
				}
				else
				{
					tf_FAddress.setText("");
					lblFarmAddress.setVisible(false);
					tf_FAddress.setVisible(false);
				}
			}
		});
		cbType.setAlignmentY(CENTER_ALIGNMENT);
		cbType.setFont(new Font("Times New Roman", Font.PLAIN, 14));
		cbType.setBounds(139, 393, 200, 20);
		contentPane.add(cbType);
		
		JLabel lblAccountType = new JLabel("Account Type:");
		lblAccountType.setForeground(Color.WHITE);
		lblAccountType.setFont(new Font("Times New Roman", Font.PLAIN, 15));
		lblAccountType.setBounds(10, 387, 119, 31);
		contentPane.add(lblAccountType);
		
		
		tf_FAddress = new JTextField();
		tf_FAddress.setEnabled(true);
		tf_FAddress.setHorizontalAlignment(SwingConstants.CENTER);
		tf_FAddress.setFont(new Font("Times New Roman", Font.PLAIN, 14));
		tf_FAddress.setColumns(10);
		tf_FAddress.setBounds(139, 424, 200, 31);
		tf_FAddress.setVisible(false);
		contentPane.add(tf_FAddress);
		
		JButton btnNewButton_1 = new JButton("Create Account");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(cbType.getSelectedIndex()==1)
				{
					/*String query01 = "SELECT * FROM Farmer WHERE (Email)=? ";
					  PreparedStatement pst01;
					try {
						pst01 = conn.prepareStatement(query01);
				
					  pst01.setString(1, tfEmail.getText().toLowerCase() );
					  
					  ResultSet rs1 = pst01.executeQuery();
					  
					  while (rs1.next())
					  {
						  counter++;
					  }
					  
					  
					} catch (SQLException e1) {
						e1.printStackTrace();
					}*/
					Response<Integer> response = requestSender.checkIfFarmerEmailAlreadyExists(tfEmail.getText().toLowerCase());
					boolean success = requestSender.responseCheck(response);
					if(success)
						counter = response.getData();
					else
						counter = -1;
				}
				else if(cbType.getSelectedIndex()==2)
				{
					/*String query02 = "SELECT * FROM Customer WHERE (Email)=? ";
					  PreparedStatement pst02;
					try {
						pst02 = conn.prepareStatement(query02);
				
					  pst02.setString(1, tfEmail.getText().toLowerCase() );
					  
					  ResultSet rs2 = pst02.executeQuery();
					  
					  while (rs2.next())
					  {
						  counter++;
					  }
					  
					  
					} catch (SQLException e1) {
						e1.printStackTrace();
					}  */

					Response<Integer> response = requestSender.checkIfCustomerEmailAlreadyExists(tfEmail.getText().toLowerCase());
					boolean success = requestSender.responseCheck(response);
					if(success)
						counter = response.getData();
					else
						counter = -1;
				}
				
				if ( ((cbType.getSelectedIndex()==1) && (tf_FAddress.getText().equals("")) || (tfName.getText().equals("")) ||(tfSurname.getText().equals(""))||(tfEmail.getText().equals(""))||(lbPic.getIcon()==null)|| (cbType.getSelectedIndex()==0)||(tfPassword.getText().equals(""))))
				{
					   JOptionPane.showMessageDialog(null, "Ensure that all fields are filled");
				}
				else{
					if(counter < 0){
						counter = 0;
					}
					else if(counter>0)
					{
						JOptionPane.showMessageDialog(null, "The Email entered already exist, please try using another");
						counter=0;
					}
					else if(tfPassword.getText().equals(tf_p2.getText()))
					{
					   if(cbType.getSelectedIndex()==1)
					   {
							farmer.setFirstName(tfName.getText());
							farmer.setLastName(tfSurname.getText());
							farmer.setEmail(tfEmail.getText());
							farmer.setAddress(tf_FAddress.getText());
							farmer.setPassword(tfPassword.getText());
							farmer.setPhotoPath(Apath);
						 	
						try
						  {
							/*String query1 = "INSERT INTO Farmer (Name,Surname,Email,Photopath,password,Earnings,FarmAddress) VALUES(?,?,?,?,?,?,?)";
							PreparedStatement pst1 = conn.prepareStatement(query1);
							
							pst1.setString(1, farmer.getFirstName());
							pst1.setString(2, farmer.getLastName());
							pst1.setString(3, farmer.getEmail().toLowerCase());
							
							pst1.setString(5, farmer.getPassword());
							pst1.setString(6, "0.00");
							pst1.setString(7, farmer.getAddress());*/
							
							
							File image = new File(farmer.getPhotoPath());
					        FileInputStream fis = new FileInputStream ( image );
					        byte[] userimage;
					        
					        ByteArrayOutputStream baos= new ByteArrayOutputStream();
					        byte[] buff = new byte[1024];
					        for(int readNum; (readNum=fis.read(buff)) !=-1 ; ){
					            baos.write(buff,0,readNum);
					        }
					        userimage=baos.toByteArray();


					        
					        /*pst1.setBytes(4, userimage);
					        
							
							
							pst1.execute();
							
							
							pst1.close();*/
					        Response<Boolean> response = requestSender.createFarmer(farmer, userimage);
					        boolean success = requestSender.responseCheck(response);
					        if(success)
					        	if(response.getData())
									JOptionPane.showMessageDialog(null, "Farmer Account Successfully created");

							  cbType.setSelectedIndex(0);
							  tf_FAddress.setText("");
							  tfName.setText("");
							  tfSurname.setText("");
							  tfEmail.setText("");
							  lbPic.setIcon(null);
							  tfPassword.setText("");
							  tf_p2.setText("");
					      }
						  catch(Exception e){
								JOptionPane.showMessageDialog(null, e);
								e.printStackTrace();
						  }
					   }
					   else if(cbType.getSelectedIndex()==2)
					   {
						    customer.setFirstName(tfName.getText());
						    customer.setLastName(tfSurname.getText());
						    customer.setEmail(tfEmail.getText());
						    customer.setPassword(tfPassword.getText());
						    customer.setPhotoPath(Apath);
						 	
						try
						  {
							/*String query2 = "INSERT INTO Customer (Name,Surname,Email,Password,Photopath,Money) VALUES(?,?,?,?,?,?)";
							PreparedStatement pst2 = conn.prepareStatement(query2);
							pst2.setString(1, customer.getFirstName());
							pst2.setString(2, customer.getLastName());
							pst2.setString(3, customer.getEmail().toLowerCase());
							pst2.setString(4, customer.getPassword());
							pst2.setString(6, "0.00");*/
							
							
							File image = new File(customer.getPhotoPath());
					        FileInputStream fis = new FileInputStream ( image );
					        byte[] userimage;
					        
					        ByteArrayOutputStream baos= new ByteArrayOutputStream();
					        byte[] buff = new byte[1024];
					        for(int readNum; (readNum=fis.read(buff)) !=-1 ; ){
					            baos.write(buff,0,readNum);
					        }
					        userimage=baos.toByteArray();
					        
					        /*pst2.setBytes(5, userimage);
					        

							
							pst2.execute();
							
							pst2.close();*/
							  Response<Boolean> response = requestSender.createCustomer(customer, userimage);
							  boolean success = requestSender.responseCheck(response);

							  if(success)
								  if(response.getData())
									  JOptionPane.showMessageDialog(null, "Customer Account Successfully created");
							 
							   

						  }
						  catch(Exception e){
								JOptionPane.showMessageDialog(null, e);
						  }
					   }
				   }
				   else
				   {
					   JOptionPane.showMessageDialog(null, "Please check to ensure both passwords match");
				   }
				}
			}
		});
		btnNewButton_1.setFont(new Font("Times New Roman", Font.PLAIN, 15));
		btnNewButton_1.setBounds(505, 434, 128, 31);
		contentPane.add(btnNewButton_1);
		
		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				dispose();
				Login l = new Login();
				l.setVisible(true);
			}
		});
		btnCancel.setFont(new Font("Times New Roman", Font.PLAIN, 15));
		btnCancel.setBounds(643, 433, 128, 31);
		contentPane.add(btnCancel);
		
		ImageIcon icon2 = new ImageIcon("Images\\crbg.jpg");
		JLabel label_1 = new JLabel("");
		label_1.setIcon(icon2);
		label_1.setBounds(0, 0, 804, 497);
		contentPane.add(label_1);
	}

	public String getApath() {
		return Apath;
	}

	public void setApath(String apath) {
		Apath = apath;
	}
}
