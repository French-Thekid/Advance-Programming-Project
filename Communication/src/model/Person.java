package model;


import java.io.Serializable;

public class Person implements Serializable {
	String firstName;
	String lastName;
	String email;
	String photoPath;
	String password;
	private String Address;
	private Double money;
	private byte[] photo1;
	private int ID;
	
	public Person() {
		 firstName="Unknown";
		 lastName = "";
		 email ="";
		 photoPath="";
		 password = "";
		 this.money = 50.00;
		 this.ID = 0;
	}
	
	public Person(byte[] path,	String firstName, String lastName,String email, int id, double m, String Address,String photoPath,String password) {
		setEmail(email);
		setID(id);
		setMoney(m);
		setFirstName(firstName);
		setLastName(lastName);
		setPassword(password);
		setPhotoPath(photoPath);
		setPhoto1(path);
		setAddress(Address);
		
	}
	
	
	public Person(Person p, String Address,  double m ) {
		setEmail(p.getEmail());
		setFirstName(p.getFirstName());
		setLastName(p.getLastName());
		setPassword(p.getPassword());
		setPhotoPath(p.getPhotoPath());
		setMoney(m);
		setAddress(Address);
	}

	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getPhotoPath() {
		return photoPath;
	}
	public void setPhotoPath(String photoPath) {
		this.photoPath = photoPath;
	}

	public byte[] getPhoto1() {
		return photo1;
	}

	public void setPhoto1(byte[] photo) {
		this.photo1 = photo;
	}

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public Double getMoney() {
		return money;
	}

	public void setMoney(Double money) {
		this.money = money;
	}

	public String getAddress() {
		return Address;
	}

	public void setAddress(String address) {
		Address = address;
	}
	
	
	
}
