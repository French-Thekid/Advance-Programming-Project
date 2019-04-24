package model;


import java.io.Serializable;

public class Crop implements Serializable {

	private String farmerID;
	private String imagePath;
	private String cName;
	private float weight;
	private String available;
	private int quantity;
	private Double cost;
	private int cid;
	private String farmerName;
	
	public Crop() {
		imagePath = "";
		cName = "";
		weight = 0;
		available="";
		quantity = 0;
		setCost(0.0);
		farmerName="";
		setCid(0);
	}
	
	public Crop(String fn,int id,String imagePath,String cName, 
			float weight, String available, int quantity, String farmerID, Double c) {
		setImagePath(imagePath);
		setcName(cName);
		setWeight(weight);
		setAvailable(available);
		setQuantity(quantity);
		setFarmerID(farmerID);
		setCost(c);
		setFarmerName(fn);
		setCid(id);
	}
	
	
	public String getFarmerID() {
		return farmerID;
	}

	public void setFarmerID(String farmerID) {
		this.farmerID = farmerID;
	}

	public String getImagePath() {
		return imagePath;
	}
	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}
	public String getcName() {
		return cName;
	}
	public void setcName(String cName) {
		this.cName = cName;
	}
	public float getWeight() {
		return weight;
	}
	public void setWeight(float weight) {
		this.weight = weight;
	}
	public String getAvailable() {
		return available;
	}
	public void setAvailable(String available) {
		this.available = available;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public void printCrop() {
		System.out.println("Crop [farmerID=" + farmerID + ", imagePath=" + imagePath + ", cName=" + cName + ", weight=" + weight
				+ ", available=" + available + ", quantity=" + quantity + "]");	}

	public Double getCost() {
		return cost;
	}

	public void setCost(Double cost) {
		this.cost = cost;
	}

	public int getCid() {
		return cid;
	}

	public void setCid(int cid) {
		this.cid = cid;
	}

	public String getFarmerName() {
		return farmerName;
	}

	public void setFarmerName(String farmerName) {
		this.farmerName = farmerName;
	}
	
}

