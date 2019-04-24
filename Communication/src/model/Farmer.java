package model;

public class Farmer extends Person{
	
	Crop[] crops;
	String address;
	Double totalEarn;
	String[] cusID;
	private int ID;
	private  int photo;
	
	public Farmer()
	{
		super();
		crops = new Crop[100];
		address = "";
		totalEarn = 0.0;
		cusID = new String[100];
		ID=3;
		photo = 0;
		
	}
	
	public Farmer(Person P,	Crop[] crops, String address, int pho, double totalEarn, String[] cusID,int ID) {
		super(P,address,totalEarn);
		setAddress(address);
		setPhoto(pho);
		setTotalEarn(totalEarn);
		setCrops(crops);
		setCusID(cusID);
		setID(ID);
	}
		
	public Crop[] getCrops() {
		return crops;
	}
	public void setCrops(Crop[] crops) {
		this.crops = crops;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public double getTotalEarn() {
		return totalEarn;
	}
	public void setTotalEarn(double totalEarn) {
		super.setMoney(totalEarn);//money issue
	}
	public String[] getCusID() {
		return cusID;
	}
	public void setCusID(String[] cusID) {
		this.cusID = cusID;
	}
	
	
	public void viewCrops() {
		if(crops.length >0 ) {
			for(int x =0;x<crops.length;x++) {
				crops[x].printCrop();
			}
		}
	}

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public int getPhoto() {
		return photo;
	}

	public void setPhoto(int proto) {
		this.photo = proto;
	}
}
