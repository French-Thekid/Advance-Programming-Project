package database;

import communication.Response;
import model.Crop;

import javax.swing.table.DefaultTableModel;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class CropTableManager implements TableManager<Crop> {

    private  static CropTableManager instance = null;

    private static final String TABLE_NAME = SQLProvider.CROPS_TABLE_NAME;

    private CropTableManager(){
        setUpTable();
    }

    public static CropTableManager getInstance(){
        if(instance == null)
            instance = new CropTableManager();
        return instance;
    }

    private void setUpTable() {
        Statement statement = null;
        try {
            statement = sql.createStatement();
            if(statement.execute("create table if not exists "+TABLE_NAME+" (\"CID\" INTEGER PRIMARY KEY NOT NULL ,\"Name\" VARCHAR,\"Weight\" DOUBLE,\"Cost\" DOUBLE,\"Quantity\" INTEGER,\"Availability\" VARCHAR,\"Photopath\" INTEGER,\"FID\" INTEGER,\"Farmer_Name\" VARCHAR DEFAULT (null) )")){
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

    @Override
    public boolean create(Crop item, byte[] image) {
        //RequestAction.ADD_CROP
        boolean success = false;
        String query0 = "INSERT INTO "+TABLE_NAME+" (Name,Weight,Cost,Quantity,Availability,Photopath,FID,Farmer_Name) VALUES(?,?,?,?,?,?,?,?)";
        PreparedStatement pst0 = null;

        try{
            pst0 = sql.prepareStatement(query0);
            pst0.setString(1, item.getcName());
            pst0.setString(2, String.valueOf(item.getWeight()));
            pst0.setString(3, item.getCost().toString());
            pst0.setString(4, String.valueOf(item.getQuantity()));
            pst0.setString(5, item.getAvailable());
            pst0.setBytes( 6, image);
            pst0.setInt(7, Integer.parseInt(item.getFarmerID()));
            pst0.setString(8, item.getFarmerName());

            success = (pst0.executeUpdate() > 0);
            if(success) System.out.println("crop created");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            sql.close(pst0);
        }


        return success;
    }

    @Override
    public Crop retrieve(int id) {
        return null;
    }

    @Override
    public List<Crop> retrieveAll() {
        return null;
    }

    public Response<DefaultTableModel> retrieveAllCropsForAFarmerView(int farmerID){
        //RequestAction.GET_FARMER_CROPS_VIEW_CROPS
        String query = "SELECT Name,Weight,Cost,Quantity,Availability,Photopath FROM "+TABLE_NAME+" WHERE FID='"+farmerID+"'";
        return sql.getTableModelFromResultSet(query);
    }

    public Response<DefaultTableModel> retrieveAllCropsForAFarmerUpdate(int farmerID){
        //RequestAction.GET_FARMER_CROPS_UPDATE_CROPS
        String query = "SELECT CID,Name,Weight,Cost,Quantity,Availability,Photopath FROM "+TABLE_NAME+" WHERE FID='"+farmerID+"'";
        return sql.getTableModelFromResultSet(query);
    }

    public Response<DefaultTableModel> retrieveCustomerViewCrops(){
        //RequestAction.GET_CUSTOMER_VIEW_CROPS
        String query = "SELECT Name, Weight, Cost, Quantity,PhotoPath,Farmer_Name FROM "+TABLE_NAME+" where Availability='Yes' ";
        return sql.getTableModelFromResultSet(query);
    }

    public Response<DefaultTableModel> retrieveCustomerViewCropsSearchResults(String farmerName){
        //RequestAction.GET_CUSTOMER_VIEW_CROPS_FARMER_SEARCH
        String query = "SELECT Name, Weight, Cost, Quantity,PhotoPath,Farmer_Name FROM Crops where Availability='Yes' AND LOWER(Farmer_Name) LIKE LOWER('"+farmerName+"') ";
        return sql.getTableModelFromResultSet(query);
    }

    @Override
    public boolean delete(int id) {
        return false;
    }

    @Override
    public boolean update(Crop item) {
        //RequestAction.UPDATE_CROP
        boolean success = false;

        String query0 = "UPDATE "+TABLE_NAME+" SET Name='"+item.getcName()+"' ,Weight='"+item.getWeight()+"',Cost='"+item.getCost()+"',Quantity='"+item.getQuantity()+"',Availability='"+item.getAvailable()+"' WHERE CID ='"+item.getCid()+"'";
        return sql.update(query0);
    }
    
    public boolean update(Crop item, byte[] image){
        //RequestAction.UPDATE_CROP_WITH_IMAGE
        boolean success = false;

        String query0 = "UPDATE "+TABLE_NAME+" SET Name='"+item.getcName()+"' ,Weight='"+item.getWeight()+"',Cost='"+item.getCost()+"',Quantity='"+item.getQuantity()+"',Availability='"+item.getAvailable()+"',Photopath=? WHERE CID ='"+item.getCid()+"'";
        PreparedStatement pst0 = null;
        try{
            pst0 = sql.prepareStatement(query0);
            pst0.setBytes(1, image);
            success = pst0.executeUpdate() > 0;
            System.out.println("Update success for query: "+query0+" worked = "+success);
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            sql.close(pst0);
        }

        return success;
    }

    public boolean updateCropQuantity(int cropID, int quantity){
        //RequestAction.UPDATE_CROP_QUANTITY_USING_ID
        String q0 = "UPDATE "+TABLE_NAME+" SET Quantity='"+quantity+"' WHERE CID='"+cropID+"' ";
        return sql.update(q0);
    }

    public boolean updateCropQuantity(String cropName, String farmerName, int quantity){
        //RequestAction.UPDATE_CROP_QUANTITY
        String query = "UPDATE "+TABLE_NAME+" SET Quantity='"+quantity+"' WHERE Name='"+cropName+"' AND Farmer_Name ='"+farmerName+"'";
        return sql.update(query);
    }

    public Response<Integer[]> checkIfFarmerCropAlreadyExists(String farmerID, String cropName){
        //RequestAction.CHECK_IF_CROP_ALREADY_EXISTS_FOR_FARMER
        int c=0;
        int quan=0;
        int id=0;
        Response<Integer[]> response = new Response<>(null, Response.GENERAL_ERROR);
        String search = "SELECT * FROM "+TABLE_NAME+" WHERE FID='"+Integer.parseInt(farmerID)+"' AND LOWER(Name) LIKE LOWER('"+cropName+"') ";
        PreparedStatement p = null;
        ResultSet r = null;
        try{
            p = sql.prepareStatement(search);
            r = p.executeQuery();
            while(r.next()){
                quan = r.getInt(5);
                id= r.getInt(1);
                c++;
            }
            Integer[] array = {c, quan, id};
            response.setData(array);
            response.setErrorMessage(Response.NO_ERROR);

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            sql.close(p, r);
        }

        return response;
    }

    public int getCropQuantity(String cropName, String farmerName) {
        int quantity = 0;
        String query = "Select * from "+TABLE_NAME+" where Name='"+cropName+"' AND Farmer_Name='"+farmerName+"'";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try{
            ps = sql.prepareStatement(query);
            rs = ps.executeQuery();
            while(rs.next()){
                quantity+= rs.getInt(5);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            sql.close(ps, rs);
        }

        return quantity;
    }
}
