package client;

import model.Crop;
import model.Customer;
import model.Farmer;
import communication.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.io.IOException;

public class RequestSender extends Client {
    private static RequestSender instance = null;


    public static RequestSender getInstance(){
        if(instance == null)
            instance = new RequestSender();
        return instance;
    }

    private RequestSender(){
        new Client("localhost", 4000);
    }

    public Object readObject() throws IOException, ClassNotFoundException {
        return is.readObject();
    }

    public boolean writeObject(Object obj){
        boolean success = false;
        try {
            os.writeObject(obj);
            success = true;
        }catch (IOException e) {
            e.printStackTrace();
        }

        return success;
    }

    private Response sendRequest(Request request){
        Response response = new Response<>(null, Response.SERVER_CONNECTION_ERROR);
        try {
            os.writeObject(request);
            System.out.println("Request: "+request.getAction()+" sent");
            response = (Response) is.readObject();
            System.out.println("Response for "+request.getAction()+" received");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return response;
    }

    public boolean responseCheck(Response response) {
        if(response.getErrorMessage().equals(Response.NO_ERROR)){
            System.out.println("NO ERROR");
            return true;
        }

        if(response.getErrorMessage().equals(Response.SERVER_CONNECTION_ERROR)) {
            System.out.println("SERVER CONNECTION ERROR");
            JOptionPane.showMessageDialog(null, "Server Connection Error, Please Restart Server");
        }

        if(response.getErrorMessage().equals(Response.GENERAL_ERROR)) {
            System.out.println("GENERAL ERROR");
            JOptionPane.showMessageDialog(null, "An Error Occurred");
        }

        return false;
    }

    private DefaultTableModel sendTableModel(Response<DefaultTableModel> response){
        return responseCheck(response)? response.getData() : null;
    }

    /*Farmer Requests*/

    public Response<Object[]> loginFarmer(String email, String password){
        Request<String[]> request = new Request<>(RequestAction.LOGIN_FARMER, new String[]{email, password});
        Response<Object[]> response =  sendRequest(request);

        return response;
    }

    public Response<Integer> checkIfFarmerEmailAlreadyExists(String email){
        Request<String> request = new Request<>(RequestAction.CHECK_IF_FARMER_EMAIL_ALREADY_EXISTS, email);
        Response<Integer> response = sendRequest(request);

        return response;
    }

    public Response<Boolean> createFarmer(Farmer farmer, byte[] image){
        Request<Object[]> request = new Request<>(RequestAction.CREATE_FARMER, new Object[]{farmer, image});
        Response<Boolean> response = sendRequest(request);

        return response;
    }

    public Response<Double> updateFarmerEarnings(Farmer farmer){
        Request<Farmer> request = new Request<>(RequestAction.UPDATE_FARMER_EARNINGS, farmer);
        Response<Double> response = sendRequest(request);

        return response;
    }

    public DefaultTableModel getFarmerCustomers(String farmerName){
        Request<String> request = new Request<>(RequestAction.GET_FARMER_CUSTOMERS, farmerName);
        Response<DefaultTableModel> response = sendRequest(request);

        return sendTableModel(response);
    }

    /*Customer Requests*/

    public Response<Object[]> loginCustomer(String email, String password){
        Request<String[]> request = new Request<>(RequestAction.LOGIN_CUSTOMER, new String[]{email, password});
        Response<Object[]> response =  sendRequest(request);

        return response;
    }

    public Response<Integer> checkIfCustomerEmailAlreadyExists(String email){
        Request<String> request = new Request<>(RequestAction.CHECK_IF_CUSTOMER_EMAIL_ALREADY_EXISTS, email);
        Response<Integer> response = sendRequest(request);

        return response;
    }

    public Response<Boolean> createCustomer(Customer customer, byte[] image){
        Request<Object[]> request = new Request<>(RequestAction.CREATE_CUSTOMER, new Object[]{customer, image});
        Response<Boolean> response = sendRequest(request);

        return response;
    }

    public Response<Double> getCustomerTotalBasket(int customerID){
        Request<Integer> request = new Request<>(RequestAction.GET_CUSTOMER_TOTAL_BASKET, customerID);
        Response<Double> response = sendRequest(request);

        return response;
    }

    public Response<Double> getCustomerFunds(int customerID){
        Request<Integer> request = new Request<>(RequestAction.GET_CUSTOMER_FUNDS, customerID);
        Response<Double> response = sendRequest(request);

        return response;
    }

    public Response<Double> updateCustomerFunds(double funds, int customerID){
        Request<Object[]> request = new Request<>(RequestAction.UPDATE_CUSTOMER_FUNDS, new Object[]{funds, customerID});
        Response<Double> response = sendRequest(request);

        return response;
    }

    public Response<Integer> checkIfCustomerBasketEmpty(int customerID){
        Request<Integer> request = new Request<>(RequestAction.CHECK_IF_CUSTOMER_BASKET_EMPTY, customerID);
        Response<Integer> response = sendRequest(request);

        return response;
    }

    public Response<Boolean> updateForCustomerCheckout(int customerID){
        Request<Integer> request = new Request<>(RequestAction.UPDATE_FOR_CHECKOUT, customerID);
        Response<Boolean> response = sendRequest(request);

        return response;
    }

    public Response<Boolean> removeItemFromCustomerBasket(String cropName, String farmerName, int cropQuantity, int customerID){
        Request<Object[]> request = new Request<>(RequestAction.REMOVE_ITEM_FROM_CUSTOMER_BASKET, new Object[]{cropName, farmerName, cropQuantity, customerID});
        Response<Boolean> response = sendRequest(request);

        return response;
    }

    public Response<Boolean> updateCustomerBasketCropQuantity(String cropName, String farmerName, int cropQuantity, int customerID){
        Request<Object[]> request = new Request<>(RequestAction.UPDATE_CUSTOMER_BASKET_CROP_QUANTITY, new Object[]{cropName, farmerName, cropQuantity, customerID});
        Response<Boolean> response = sendRequest(request);

        return response;
    }

    public Response<Double> getCropUnitCostFromCustomerBasket(String cropName, String farmerName, int customerID){
        Request<Object[]> request = new Request<>(RequestAction.GET_CROP_UNIT_COST_FROM_CUSTOMER_BASKET, new Object[]{cropName, farmerName, customerID});
        Response<Double> response = sendRequest(request);

        return response;
    }

    public Response<Boolean> updateCustomerBasketCropTotal(String cropName, String farmerName, double total, int customerID){
        Request<Object[]> request = new Request<>(RequestAction.UPDATE_CUSTOMER_BASKET_CROP_TOTAL, new Object[]{cropName, farmerName, total, customerID});
        Response<Boolean> response = sendRequest(request);

        return response;
    }

    public Response<Boolean> addCropToCustomerBasket(Crop addCrop){
        Request<Crop> request = new Request<>(RequestAction.ADD_CROP_TO_CUSTOMER_BASKET, addCrop);
        Response<Boolean> response = sendRequest(request);

        return response;
    }

    public DefaultTableModel customerViewBasket(int customerID){
        Request<Integer> request = new Request<>(RequestAction.CUSTOMER_VIEW_BASKET, customerID);
        Response<DefaultTableModel> response = sendRequest(request);

        return sendTableModel(response);
    }

    /*Crop Requests*/

    public Response<Boolean> addCrop(Crop crop, byte[] image){
        Request<Object[]> request = new Request<>(RequestAction.ADD_CROP, new Object[]{crop, image});
        Response<Boolean> response = sendRequest(request);

        return response;
    }

    public Response<Boolean> updateCrop(Crop crop){
        Request<Crop> request = new Request<>(RequestAction.UPDATE_CROP, crop);
        System.out.println("request sender: "+crop.getcName());
        Response<Boolean> response = sendRequest(request);

        return response;
    }

    public Response<Boolean> updateCropWithImage(Crop crop, byte[] image){
        Request<Object[]> request = new Request<>(RequestAction.UPDATE_CROP_WITH_IMAGE, new Object[]{crop, image});
        Response<Boolean> response = sendRequest(request);

        return response;
    }

    public DefaultTableModel getFarmerCropsUpdateCrops(int farmerID){
        Request<Integer> request = new Request<>(RequestAction.GET_FARMER_CROPS_UPDATE_CROPS, farmerID);
        Response<DefaultTableModel> response = sendRequest(request);

        return sendTableModel(response);
    }

    public DefaultTableModel getFarmerCropsViewCrops(int farmerID){
        Request<Integer> request = new Request<>(RequestAction.GET_FARMER_CROPS_VIEW_CROPS, farmerID);
        Response<DefaultTableModel> response = sendRequest(request);

        return sendTableModel(response);
    }

    public DefaultTableModel getCustomerViewCrops(){
        Request request = new Request<>(RequestAction.GET_CUSTOMER_VIEW_CROPS, null);
        Response<DefaultTableModel> response = sendRequest(request);

        return sendTableModel(response);
    }

    public DefaultTableModel getCustomerViewCropsFarmerSearch(String farmerName){
        Request<String> request = new Request<>(RequestAction.GET_CUSTOMER_VIEW_CROPS_FARMER_SEARCH, farmerName);
        Response<DefaultTableModel> response = sendRequest(request);

        return sendTableModel(response);
    }

    public Response<Integer[]> checkIfCropAlreadyExistsForFarmer(String farmerID, String cropName){
        Request<String[]> request = new Request<>(RequestAction.CHECK_IF_CROP_ALREADY_EXISTS_FOR_FARMER, new String[]{farmerID, cropName});
        Response<Integer[]> response = sendRequest(request);

        return response;
    }

    public Response<Boolean> updateCropQuantity(int cropID, int quantity){
        Request<Integer[]> request = new Request<>(RequestAction.UPDATE_CROP_QUANTITY_USING_ID, new Integer[]{cropID, quantity});
        Response<Boolean> response = sendRequest(request);

        return response;
    }

    public void logout(){
        Request request = new Request(RequestAction.LOGOUT, null);
        Response<String> response = sendRequest(request);
        if(responseCheck(response)){
            System.out.println(response.getData());
        }
    }

    public Response<String[]> getFarmersAvailableForChat(){
        Request request = new Request(RequestAction.GET_FARMERS_AVAILABLE_FOR_CHAT);
        Response<String[]> response = sendRequest(request);

        return response;
    }

    public void endLiveChat(){
        Request request = new Request(RequestAction.END_LIVE_CHAT, null);
        writeObject(request);
        System.out.println("Request: "+request.getAction()+" sent");

        //return response;
    }


    public Response<Boolean> startFarmerLiveChat() {
        Request request = new Request(RequestAction.START_FARMER_LIVE_CHAT);
        Response<Boolean> response = sendRequest(request);

        return response;
    }
}
