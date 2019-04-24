package database;

import communication.Response;
import model.Crop;
import model.Customer;

import javax.swing.table.DefaultTableModel;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class CustomerTableManager implements TableManager<Customer>{

    private static CustomerTableManager instance = null;

    private static final String TABLE_NAME = SQLProvider.CUSTOMER_TABLE_NAME;
    private static final String CUSTOMER_BASKET = SQLProvider.CUSTOMER_BASKET_TABLE_NAME;

    private CustomerTableManager(){
        setUpTables();
    }

    public static CustomerTableManager getInstance(){
        if(instance == null)
            instance = new CustomerTableManager();
        return instance;
    }

    private void setUpTables() {
        setUpCustomerTable();
        setUpCustomerBasketTable();
    }

    private void setUpCustomerTable(){
        Statement statement = null;
        try {
            statement = sql.createStatement();
            if(statement.execute("create table if not exists "+TABLE_NAME+" (\"ID\" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL , \"Name\" VARCHAR, \"Surname\" VARCHAR, \"Email\" VARCHAR, \"Password\" VARCHAR, \"Photopath\" INTEGER, \"Money\" DOUBLE)")){
                System.out.println(TABLE_NAME+" Table Created");
            }else{
                System.out.println(TABLE_NAME+" Table already created");
            }
            System.out.println(TABLE_NAME+" Table exists");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(TABLE_NAME+" Table not initialised");
        }finally {
            sql.close(statement);
        }
    }

    private void setUpCustomerBasketTable(){
        Statement statement = null;
        try {
            statement = sql.createStatement();
            if(statement.execute("create table if not exists "+CUSTOMER_BASKET+" (\"ID\" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL , \"CustomerID\" INTEGER, \"Name\" VARCHAR, \"Quantity\" INTEGER, \"Unit_Cost\" DOUBLE, \"Total\" DOUBLE, \"Farmer\" VARCHAR)")){
                System.out.println(CUSTOMER_BASKET+" Table Created");
            }else{
                System.out.println(CUSTOMER_BASKET+" Table already created");
            }
            System.out.println(CUSTOMER_BASKET+" Table exists");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(CUSTOMER_BASKET+" Table not initialised");
        }finally {
            sql.close(statement);
        }
    }

    @Override
    public boolean create(Customer item, byte[] image) {
        //RequestAction.CREATE_CUSTOMER
        boolean success = false;
        String query2 = "INSERT INTO Customer (Name,Surname,Email,Password,Photopath,Money) VALUES(?,?,?,?,?,?)";
        PreparedStatement pst2 = null;
        try {
            pst2 = sql.prepareStatement(query2);
            pst2.setString(1, item.getFirstName());
            pst2.setString(2, item.getLastName());
            pst2.setString(3, item.getEmail().toLowerCase());
            pst2.setString(4, item.getPassword());
            pst2.setString(6, "0.00");

            pst2.setBytes(5, image);

            success = (pst2.executeUpdate() > 0);
            if(success) System.out.println("customer created");
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            sql.close(pst2);
        }
        return success;
    }

    @Override
    public Customer retrieve(int id) {
        Customer customer1 = new Customer();
        String query1 = "SELECT * FROM "+TABLE_NAME+" WHERE ID='"+id+"'";
        PreparedStatement pst1 = null;
        ResultSet rs1 = null;
        try {
            pst1 = sql.prepareStatement(query1);
            rs1 = pst1.executeQuery();
            while (rs1.next())
            {
                customer1.setCusID(rs1.getInt(1));
                customer1.setFirstName(rs1.getString(2));
                customer1.setLastName(rs1.getString(3));
                customer1.setEmail(rs1.getString(4));
                customer1.setPhoto1(rs1.getBytes(6));
                customer1.setFunds(rs1.getFloat(7));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error retrieving customer");
        }finally {
            sql.close(pst1, rs1);
        }
        return customer1;
    }

    @Override
    public List<Customer> retrieveAll() {
        return null;
    }

    @Override
    public boolean delete(int id) {
        return false;
    }

    @Override
    public boolean update(Customer item) {
        return false;
    }

    public Response<Double> updateFunds(double funds, int customerID){
        //RequestAction.UPDATE_CUSTOMER_FUNDS
        Response<Double> response = new Response<>(null, Response.GENERAL_ERROR);
        String query = "UPDATE "+TABLE_NAME+" SET Money='"+funds+"' WHERE ID ='"+customerID+"'";
        PreparedStatement ps = null;
        try{
            ps = sql.prepareStatement(query);
            if(ps.executeUpdate() > 0){
                response = getFunds(customerID);
            }else{
                response.setErrorMessage("Error updating funds");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            sql.close(ps);
        }

        return response;
    }
    
    public Response<Integer> checkIfEmailAlreadyExists(String email){
        //RequestAction.CHECK_IF_CUSTOMER_EMAIL_ALREADY_EXISTS
        Response<Integer> response = new Response<>(null, "-1");
        String query02 = "SELECT * FROM "+TABLE_NAME+" WHERE (Email)=? ";
        PreparedStatement pst02 = null;
        ResultSet rs2 = null;
        try {
            pst02 = sql.prepareStatement(query02);
            pst02.setString(1, email);
            rs2 = pst02.executeQuery();
            int counter = 0;
            while (rs2.next())
            {
                counter++;
            }
            response.setData(counter);
            response.setErrorMessage(Response.NO_ERROR);
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            sql.close(pst02, rs2);
        }


        return response;
    }



    public Response<Object[]> loginCustomer(String email, String password){
        //RequestAction.LOGIN_CUSTOMER
        Response<Object[]> response = new Response<>(null, Response.GENERAL_ERROR);
        Customer customer1 = new Customer();
        String query1 = "SELECT * FROM "+TABLE_NAME+" WHERE (Email)=? and Password=? ";
        PreparedStatement pst1 = null;
        ResultSet rs1 = null;
        try {
            pst1 = sql.prepareStatement(query1);
            pst1.setString(1, email);
            pst1.setString(2, password);
            rs1 = pst1.executeQuery();
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
            }
            response.setData(new Object[]{customer1, count});
            response.setErrorMessage(Response.NO_ERROR);
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error logging in customer");
        }finally {
            sql.close(pst1, rs1);
        }
        return response;
    }

    public Response<Double> getTotalBasket(int customerID){
        //RequestAction.GET_CUSTOMER_TOTAL_BASKET
        Response<Double> response = new Response(null, Response.GENERAL_ERROR);
        String query = "Select * from "+CUSTOMER_BASKET+" where CustomerID='"+customerID+"'";
        double total = 0.0;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try{
            ps = sql.prepareStatement(query);
            rs = ps.executeQuery();
            while(rs.next()){
                total+= rs.getDouble(6);
            }
            response.setData(total);
            response.setErrorMessage(Response.NO_ERROR);
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            sql.close(ps, rs);
        }


        return response;
    }

    public Response<Double> getFunds(int customerID){
        //RequestAction.GET_CUSTOMER_FUNDS
        double funds = 0;
        Response<Double> response = new Response<>(null, Response.GENERAL_ERROR);
        String query = "Select * from "+TABLE_NAME+" where ID='"+customerID+"' ";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try{
            ps = sql.prepareStatement(query);
            rs = ps.executeQuery();
            funds = rs.getDouble(7);
            response.setData(funds);
            response.setErrorMessage(Response.NO_ERROR);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            sql.close(ps, rs);
        }

        return response;
    }

    public Response<Integer> checkIfBasketEmpty(int customerID){
        //RequestAction.CHECK_IF_CUSTOMER_BASKET_EMPTY
        Response<Integer> response = new Response(null, Response.GENERAL_ERROR);
        String query = "Select * from "+CUSTOMER_BASKET+" where CustomerID='"+customerID+"' ";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try{
            ps = sql.prepareStatement(query);
            rs = ps.executeQuery();
            int c = 0;
            while(rs.next()){
                c++;
            }
            response.setData(c);
            response.setErrorMessage(Response.NO_ERROR);
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            sql.close(ps);
        }

        return response;
    }

    public boolean updateForCheckOut(int customerID){
        //RequestAction.UPDATE_FOR_CHECKOUT
        boolean success = false;
        String query = "Select * from "+CUSTOMER_BASKET+" where CustomerID='"+customerID+"'";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try{
            ps = sql.prepareStatement(query);
            rs = ps.executeQuery();
            Customer customer1 = retrieve(customerID);
            String customerName = customer1.getFirstName()+" "+customer1.getLastName();
            while(rs.next()){
                Crop crop = new Crop();
                crop.setFarmerName(rs.getString(7));
                crop.setcName(rs.getString(3));
                crop.setQuantity(rs.getInt(4));
                Double totalCostOfCrop = rs.getDouble(6);
                int quantity = CropTableManager.getInstance().getCropQuantity(crop.getcName(), crop.getFarmerName());
                CropTableManager.getInstance().updateCropQuantity(crop.getcName(), crop.getFarmerName(), quantity - crop.getQuantity());
                updateFarmerCustomersTable(customerName, totalCostOfCrop, crop.getFarmerName());
            }
            removeFromCustomerBasket(customerID);
            success = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            sql.close(ps, rs);
        }

        return success;
    }

    public boolean removeFromCustomerBasket(int customerID){
        boolean success = false;
        String query = "DELETE FROM "+CUSTOMER_BASKET+" WHERE CustomerID ='"+customerID+"' ";
        PreparedStatement ps = null;
        try{
            ps = sql.prepareStatement(query);
            success = ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            sql.close(ps);
        }

        return success;
    }

    public boolean updateFarmerCustomersTable(String customerName, Double cost, String farmerName){
        boolean success = false;
        String query = "INSERT INTO "+SQLProvider.FARMER_CUSTOMERS_TABLE_NAME+" (Customers,Amount_Spent,Farmer) VALUES(?,?,?)";
        PreparedStatement ps = null;
        try{
            ps = sql.prepareStatement(query);
            ps.setString(1, customerName);
            ps.setString(2, cost.toString());
            ps.setString(3, farmerName);
            success = ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            sql.close(ps);
        }

        return success;
    }

    public boolean deleteItemFromBasket(String cropName, String farmerName, int cropQuantity, int customerID){
        //RequestAction.REMOVE_ITEM_FROM_CUSTOMER_BASKET
        String query = "DELETE FROM "+CUSTOMER_BASKET+" WHERE Name='"+cropName+"' AND Farmer ='"+farmerName+"' AND Quantity ='"+cropQuantity+"' AND CustomerID ='"+customerID+"' ";
        return sql.update(query);
    }

    public boolean updateBasketCropQuantity(String cropName, String farmerName, int cropQuantity, int customerID){
        //RequestAction.UPDATE_CUSTOMER_BASKET_CROP_QUANTITY
        String query = "UPDATE "+CUSTOMER_BASKET+" SET Quantity ='"+cropQuantity+"' WHERE Name='"+cropName+"' AND Farmer ='"+farmerName+"' AND CustomerID ='"+customerID+"' ";
        return sql.update(query);
    }

    public Response<Double> getCropUnitCostFromBasket(String cropName, String farmerName, int customerID){
        //RequestAction.GET_CROP_UNIT_COST_FROM_CUSTOMER_BASKET
        double cost = 0.0;
        Response<Double> response = new Response<>(null, Response.GENERAL_ERROR);
        String query = "Select * from "+CUSTOMER_BASKET+" WHERE Name='"+cropName+"' AND Farmer ='"+farmerName+"' AND CustomerID ='"+customerID+"' ";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try{
            ps = sql.prepareStatement(query);
            rs = ps.executeQuery();
            cost = rs.getDouble(5);
            response.setData(cost);
            response.setErrorMessage(Response.NO_ERROR);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            sql.close(ps, rs);
        }

        return response;
    }

    public boolean updateBasketCropTotal(String cropName, String farmerName, double total, int customerID){
        //RequestAction.UPDATE_CUSTOMER_BASKET_CROP_TOTAL
        String query = "UPDATE "+CUSTOMER_BASKET+" SET Total ='"+total+"' WHERE Name='"+cropName+"' AND Farmer ='"+farmerName+"' AND CustomerID ='"+customerID+"' ";
        return sql.update(query);
    }

    public boolean addCropToBasket(int customerID, String cropName, int quantity, Double cost, Double total, String farmerName){
        //RequestAction.ADD_CROP_TO_CUSTOMER_BASKET
        boolean success = false;
        String query = "INSERT INTO "+CUSTOMER_BASKET+" (CustomerID,Name,Quantity,Unit_Cost,Total,Farmer) VALUES(?,?,?,?,?,?)";
        PreparedStatement pst0 = null;
        try{
            pst0 = sql.prepareStatement(query);
            pst0.setInt(1, customerID);
            pst0.setString(2, cropName);
            pst0.setInt(3, quantity);
            pst0.setDouble(4, cost);
            pst0.setDouble(5, total);
            pst0.setString( 6, farmerName);
            success = pst0.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            sql.close(pst0);
        }
        return success;
    }

    public Response<DefaultTableModel> viewBasket(int customerID){
        //RequestAction.CUSTOMER_VIEW_BASKET
        String query = "SELECT Name,Quantity, Unit_Cost,Total, Farmer FROM "+CUSTOMER_BASKET+" where CustomerID='"+customerID +"' ";
        return sql.getTableModelFromResultSet(query);
    }

    /*public Response<DefaultTableModel> checkout(int customerID){
        //RequestAction.CUSTOMER_CHECKOUT
        return viewBasket(customerID);
    }*/
    
}
