package database;

import communication.Response;
import net.proteanit.sql.DbUtils;

import javax.swing.table.DefaultTableModel;
import java.sql.*;

public class SQLProvider {
    private Connection con = null;

    private static SQLProvider instance = null;

    private static final String DRIVER = "org.sqlite.JDBC";

    public static final String FARMER_TABLE_NAME = "Farmer";
    public static final String CUSTOMER_TABLE_NAME = "Customer";
    public static final String CROPS_TABLE_NAME = "Crops";
    public static final String CUSTOMER_BASKET_TABLE_NAME = "CustomerBasket";
    public static final String FARMER_CUSTOMERS_TABLE_NAME = "Farmer_Customers";



    public static SQLProvider getInstance(){
        if(instance == null)
            instance = new SQLProvider();
        return instance;
    }

    private SQLProvider() {

        try {
            Class.forName(DRIVER).newInstance();
            //String url = "jdbc:sqlite:../AP_Project/FarmersMarket.sqlite";
            String url = "jdbc:sqlite:FarmersMarket.sqlite";
            con = DriverManager.getConnection(url);


        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public Statement createStatement() throws SQLException {
        return con.createStatement();
    }

    public PreparedStatement prepareStatement(String query) throws SQLException {
        return con.prepareStatement(query);
    }

    public boolean update(String query){
        boolean success = false;
        PreparedStatement ps = null;
        try{
            ps = prepareStatement(query);
            success = ps.executeUpdate() > 0;
            System.out.println("Update success for query: "+query+" worked = "+success);
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            close(ps);
        }
        return success;
    }

    public Response<DefaultTableModel> getTableModelFromResultSet(String query1){
        Response<DefaultTableModel> response = new Response<>(null, Response.GENERAL_ERROR);
        PreparedStatement pst1 = null;
        ResultSet rst1 = null;
        try{
            pst1 = prepareStatement(query1);
            rst1 = pst1.executeQuery();
            response.setData((DefaultTableModel) DbUtils.resultSetToTableModel(rst1));
            response.setErrorMessage(Response.NO_ERROR);
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            close(pst1, rst1);
        }

        return response;
    }

    public void close(Statement st, ResultSet rs){
        if(rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        if(st != null){
            try {
                st.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void close(Statement st){
        close(st, null);
    }
}
