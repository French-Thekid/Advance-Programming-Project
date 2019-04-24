package database;

import communication.RequestAction;
import communication.Response;
import model.Farmer;
import net.proteanit.sql.DbUtils;

import javax.swing.table.DefaultTableModel;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class FarmerTableManager implements TableManager<Farmer> {

    private static FarmerTableManager instance = null;

    private static final String TABLE_NAME = SQLProvider.FARMER_TABLE_NAME;
    private static final String FARMER_CUSTOMERS = SQLProvider.FARMER_CUSTOMERS_TABLE_NAME;

    private FarmerTableManager(){
        setUpTable();
    }

    public static FarmerTableManager getInstance(){
        if(instance == null)
            instance = new FarmerTableManager();
        return instance;
    }

    private void setUpTable() {
        setUpFarmerTable();
        setUpFarmer_CustomersTable();
    }

    private void setUpFarmerTable(){
        Statement statement = null;
        try {
            statement = sql.createStatement();
            if(statement.execute("create table if not exists "+TABLE_NAME+" (\"ID\" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL , \"Name\" VARCHAR, \"Surname\" VARCHAR, \"Email\" VARCHAR, \"PhotoPath\" INTEGER, \"password\" VARCHAR, \"Earnings\" DOUBLE, \"FarmAddress\" VARCHAR)")){
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

    private void setUpFarmer_CustomersTable(){
        Statement statement = null;
        try {
            statement = sql.createStatement();
            if(statement.execute("create table if not exists "+FARMER_CUSTOMERS+" (\"ID\" INTEGER PRIMARY KEY NOT NULL ,\"Customers\" VARCHAR DEFAULT (null) ,\"Amount_Spent\" DOUBLE DEFAULT (null) ,\"Farmer\" VARCHAR)")){
                System.out.println(FARMER_CUSTOMERS+" Table Created");
            }else{
                System.out.println(FARMER_CUSTOMERS+" Table already created");
            }
            System.out.println(FARMER_CUSTOMERS+" Table exists");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(FARMER_CUSTOMERS+" Table not initialised");
        }finally {
            sql.close(statement);
        }
    }

    @Override
    public boolean create(Farmer item, byte[] image) {
        //RequestAction.CREATE_FARMER
        boolean success = false;
        String query1 = "INSERT INTO "+TABLE_NAME+" (Name,Surname,Email,Photopath,password,Earnings,FarmAddress) VALUES(?,?,?,?,?,?,?)";
        PreparedStatement pst1 = null;
        try {
            pst1 = sql.prepareStatement(query1);


        pst1.setString(1, item.getFirstName());
        pst1.setString(2, item.getLastName());
        pst1.setString(3, item.getEmail().toLowerCase());

        pst1.setString(5, item.getPassword());
        pst1.setString(6, "0.00");
        pst1.setString(7, item.getAddress());
        pst1.setBytes(4, image);

        success = (pst1.executeUpdate() > 0);
        if(success) System.out.println("farmer created");
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            sql.close(pst1);
        }

        return success;
    }

    public Response<Integer> checkIfEmailAlreadyExists(String email){
        //RequestAction.CHECK_IF_FARMER_EMAIL_ALREADY_EXISTS
        Response<Integer> response = new Response<>(null, Response.GENERAL_ERROR);
        String query01 = "SELECT * FROM "+TABLE_NAME+" WHERE (Email)=? ";
        PreparedStatement pst01 = null;
        ResultSet rs1 = null;
        try {
            pst01 = sql.prepareStatement(query01);
            pst01.setString(1, email);
            rs1 = pst01.executeQuery();
            int counter = 0;
            while (rs1.next())
            {
                counter++;
            }
            response.setData(counter);
            response.setErrorMessage(Response.NO_ERROR);
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            sql.close(pst01, rs1);
        }


        return response;
    }

    @Override
    public Farmer retrieve(int id) {
        return null;
    }

    @Override
    public List<Farmer> retrieveAll() {
        return null;
    }

    @Override
    public boolean delete(int id) {
        return false;
    }

    @Override
    public boolean update(Farmer item) {
        return false;
    }

    public Response<Object[]> loginFarmer(String email, String password){
        //RequestAction.LOGIN_FARMER
        Response<Object[]> response = new Response<>();
        response.setErrorMessage(Response.GENERAL_ERROR);
        Farmer farmer1 = new Farmer();
        String query1 = "SELECT * FROM "+TABLE_NAME+" WHERE (Email)=? and Password=? ";
        PreparedStatement pst1 = null;
        ResultSet rs = null;
        try {
            pst1 = sql.prepareStatement(query1);
            pst1.setString(1, email);
            pst1.setString(2, password);
            rs = pst1.executeQuery();
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
            }

            response.setData(new Object[]{farmer1, count});
            response.setErrorMessage(Response.NO_ERROR);
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error logging in farmer");
        }finally {
            sql.close(pst1, rs);
        }


        return response;
    }

    public Response<Double> updateEarnings(Farmer farmer){
        //RequestAction.UPDATE_FARMER_EARNINGS
        double earning=0.0;
        Response<Double> response = new Response<>(null, Response.GENERAL_ERROR);
        String name= (farmer.getFirstName()+" "+farmer.getLastName());
        String q1 = "SELECT * FROM "+FARMER_CUSTOMERS+" WHERE Farmer='"+name+"' ";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = sql.prepareStatement(q1);
            rs = ps.executeQuery();

            while(rs.next())
            {
                earning = earning + rs.getDouble(3);
            }

            if(updateEarnings(farmer, earning)) {
                response.setData(earning);
                response.setErrorMessage(Response.NO_ERROR);
                System.out.println("Farmer earnings updates succesfully");
            }else{
                response.setErrorMessage("Error updating earning");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            sql.close(ps, rs);
        }

        return response;
    }

    public boolean updateEarnings(Farmer farmer, double earning){
        boolean success = false;

        String UpdateDB = "Update "+TABLE_NAME+" set Earnings='"+earning+"' WHERE ID='"+farmer.getID()+"' ";
        PreparedStatement preS = null;
        try {
            preS = sql.prepareStatement(UpdateDB);

            success = (preS.executeUpdate() > 0);
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            sql.close(preS);
        }

        return success;
    }

    public Response<DefaultTableModel> getFarmerCustomers(String farmerName){
        //RequestAction.GET_FARMER_CUSTOMERS
        Response<DefaultTableModel> response = new Response<>(null, Response.GENERAL_ERROR);
        PreparedStatement ps = null;
        ResultSet rs = null;
        try{
            String q = "SELECT Customers,Amount_Spent FROM "+FARMER_CUSTOMERS+" WHERE Farmer='"+farmerName+"' ";

            ps = sql.prepareStatement(q);
            rs = ps.executeQuery();
            response.setData((DefaultTableModel) DbUtils.resultSetToTableModel(rs));
            response.setErrorMessage(Response.NO_ERROR);
        } catch (SQLException e) {
            e.printStackTrace();
        }catch(ClassCastException e){
            e.printStackTrace();
        }finally {
            sql.close(ps, rs);
        }

        return response;
    }
}
