package model;


public class Customer extends Person{
	Double funds;
	private Double Total;
	int cusID;
	Crop[] basketCrops;
	private int photo;
	
	public Customer() {
		super();
		funds=0.0;
		cusID = 0;
		basketCrops = new Crop[100];
		photo = 0;
		setTotal(0.0);
	}
	
	public Customer(Person p, Double funds,Double t,int photo, int cusID, Crop[] basketCrops) {
		super(p,"",funds);
		setFunds(funds);
		setPhoto(photo);
		setCusID(cusID);
		setCusID(cusID);
		this.setTotal(t);
	}
	
	public Double getFunds() {
		return funds;
	}
	public void setFunds(double funds) {
		super.setMoney(funds);
	}
	public int getCusID() {
		return cusID;
	}
	public void setCusID(int cusID) {
		this.cusID = cusID;
	}
	public Crop[] getBasketCrops() {
		return basketCrops;
	}
	public void setBasketCrops(Crop[] basketCrops) {
		this.basketCrops = basketCrops;
	}

	public int getPhoto() {
		return photo;
	}

	public void setPhoto(int photo) {
		this.photo = photo;
	}

	public Double getTotal() {
		return Total;
	}

	public void setTotal(Double total) {
		Total = total;
	}
	
	
	
	
	
}
