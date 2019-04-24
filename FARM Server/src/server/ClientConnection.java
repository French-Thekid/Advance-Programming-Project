package server;

import communication.Request;
import communication.RequestAction;
import communication.Response;
import database.CropTableManager;
import database.CustomerTableManager;
import database.FarmerTableManager;
import model.Crop;
import model.Customer;
import model.Farmer;
import model.TextMessage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;

public class ClientConnection implements Runnable {

    private Socket connection;
    private ObjectInputStream is;
    private ObjectOutputStream os;
    private String userName;
    private ClientManager clientManager;
    private FarmerTableManager farmerTable = FarmerTableManager.getInstance();
    private CustomerTableManager customerTable = CustomerTableManager.getInstance();
    private CropTableManager cropTable = CropTableManager.getInstance();
    private int userType = 0;
    private boolean availableForLiveChat = false;
    private boolean liveChatMode = false;
    private List<String> farmersConnected = new LinkedList<>();

    public static final int NEITHER = 0;
    public static final int FARMER = 1;
    public static final int CUSTOMER = 2;

    public ClientConnection(Socket connection){
        this("temp", connection);
    }

    public ClientConnection(String userName, Socket connection){
        this.userName = userName;
        clientManager = ClientManager.getInstance();
        this.connection = connection;

        if(initStreams()){
            Thread myThread = new Thread(this);
            myThread.start();
            System.out.println("Client "+userName+" added");
        }
    }

    private boolean initStreams() {
        try{
            if(connection == null)
                return false;

            os = new ObjectOutputStream(connection.getOutputStream());
            is = new ObjectInputStream(connection.getInputStream());
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    private void close(){
        if(availableForLiveChat)
            setAvailableForLiveChat(false);
        if(liveChatMode)
            setLiveChatMode(false);
        try {
            os.close();
            is.close();
            connection.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void changeUserName(String userName){
        if(clientManager.updateClientUsername(this.userName, userName))
            this.userName = userName;
    }
    
    private void notifyRequest(String request){
        System.out.println(userName+" Request: "+request);
    }
    
    private void notifyResponse(String request){
        System.out.println(userName+" "+request+" response sent");
    }

    @SuppressWarnings({"unchecked"})
    @Override
    public void run() {

        String action = "";
        try{
            while (!action.equals(RequestAction.DISCONNECT)){
                Request request = (Request) is.readObject();
                action = request.getAction();
                Response response = new Response<>(null, Response.GENERAL_ERROR);
                switch(action){
                        /*Farmer Requests*/
                    case RequestAction.LOGIN_FARMER:
                        notifyRequest(action);
                        String[] loginDetailsFarmer = (String[]) request.getData();
                        response = farmerTable.loginFarmer(loginDetailsFarmer[0], loginDetailsFarmer[1]);
                        response = checkIfFarmerAlreadyLoggedIn(response);
                        os.writeObject(response);
                        notifyResponse(action);
                        setUserNameFarmer(response);

                        break;
                    case RequestAction.CHECK_IF_FARMER_EMAIL_ALREADY_EXISTS:
                        notifyRequest(action);
                        String emailFarmer = (String) request.getData();
                        response = farmerTable.checkIfEmailAlreadyExists(emailFarmer);
                        os.writeObject(response);
                        notifyResponse(action);
                        
                        break;
                    case RequestAction.CREATE_FARMER:
                        notifyRequest(action);
                        Object[] farmerInformation = (Object[]) request.getData();
                        boolean successCreateFarmer = farmerTable.create((Farmer) farmerInformation[0], (byte[]) farmerInformation[1]);
                        response = handleBoolean(successCreateFarmer);
                        os.writeObject(response);
                        notifyResponse(action);
                        
                        break;
                    case RequestAction.UPDATE_FARMER_EARNINGS:
                        notifyRequest(action);
                        Farmer farmerUpdateEarnings = (Farmer) request.getData();
                        response = farmerTable.updateEarnings(farmerUpdateEarnings);
                        os.writeObject(response);
                        notifyResponse(action);
                        
                        break;
                    case RequestAction.GET_FARMER_CUSTOMERS:
                        notifyRequest(action);
                        String farmerName = (String) request.getData();
                        response = farmerTable.getFarmerCustomers(farmerName);
                        os.writeObject(response);
                        notifyResponse(action);
                        
                        break;
                        /*Customer Requests*/
                    case RequestAction.LOGIN_CUSTOMER:
                        notifyRequest(action);
                        String[] loginDetailsCustomer = (String[]) request.getData();
                        response = customerTable.loginCustomer(loginDetailsCustomer[0], loginDetailsCustomer[1]);
                        response = checkIfCustomerAlreadyLoggedIn(response);
                        os.writeObject(response);
                        notifyResponse(action);
                        setUserNameCustomer(response);

                        break;
                    case RequestAction.CHECK_IF_CUSTOMER_EMAIL_ALREADY_EXISTS:
                        notifyRequest(action);
                        String emailCustomer = (String) request.getData();
                        response = customerTable.checkIfEmailAlreadyExists(emailCustomer);
                        os.writeObject(response);
                        notifyResponse(action);

                        break;
                    case RequestAction.CREATE_CUSTOMER:
                        notifyRequest(action);
                        Object[] customerInformation = (Object[]) request.getData();
                        boolean successCreateCustomer = customerTable.create((Customer) customerInformation[0], (byte[]) customerInformation[1]);
                        response = handleBoolean(successCreateCustomer);
                        os.writeObject(response);
                        notifyResponse(action);

                        break;
                    case RequestAction.GET_CUSTOMER_TOTAL_BASKET:
                        notifyRequest(action);
                        int getBasketCustomerID = (int) request.getData();
                        response = customerTable.getTotalBasket(getBasketCustomerID);
                        os.writeObject(response);
                        notifyResponse(action);
                        
                        break;
                    case RequestAction.GET_CUSTOMER_FUNDS:
                        notifyRequest(action);
                        int getFundsCustomerID = (int) request.getData();
                        response = customerTable.getFunds(getFundsCustomerID);
                        os.writeObject(response);
                        notifyResponse(action);
                        
                        break;
                    case RequestAction.UPDATE_CUSTOMER_FUNDS:
                        notifyRequest(action);
                        Object[] updateCustomerFundsObjectArray = (Object[]) request.getData();
                        response = customerTable.updateFunds((Double) updateCustomerFundsObjectArray[0], (Integer) updateCustomerFundsObjectArray[1]);
                        os.writeObject(response);
                        notifyResponse(action);
                        
                        break;
                    case RequestAction.CHECK_IF_CUSTOMER_BASKET_EMPTY:
                        notifyRequest(action);
                        Integer checkBasketEmpty = (Integer) request.getData();
                        response = customerTable.checkIfBasketEmpty(checkBasketEmpty);
                        os.writeObject(response);
                        notifyResponse(action);
                        
                        break;
                    case RequestAction.UPDATE_FOR_CHECKOUT:
                        notifyRequest(action);
                        Integer updateForCheckoutID = (Integer) request.getData();
                        boolean successUpdateForCheckout = customerTable.updateForCheckOut(updateForCheckoutID);
                        response = handleBoolean(successUpdateForCheckout);
                        os.writeObject(response);
                        notifyResponse(action);
                        
                        break;
                    case RequestAction.REMOVE_ITEM_FROM_CUSTOMER_BASKET:
                        notifyRequest(action);
                        Object[] RemoveItemFromBasketObjectArray = (Object[]) request.getData();
                        boolean successRemoveItemFromBasket = customerTable.deleteItemFromBasket((String) RemoveItemFromBasketObjectArray[0], (String) RemoveItemFromBasketObjectArray[1], (Integer) RemoveItemFromBasketObjectArray[2], (Integer) RemoveItemFromBasketObjectArray[3]);
                        response = handleBoolean(successRemoveItemFromBasket);
                        os.writeObject(response);
                        notifyResponse(action);
                        
                        break;
                    case RequestAction.UPDATE_CUSTOMER_BASKET_CROP_QUANTITY:
                        notifyRequest(action);
                        Object[] obj = (Object[]) request.getData();
                        boolean successUpdateBasketCropQuantity = customerTable.updateBasketCropQuantity((String) obj[0], (String) obj[1], (Integer) obj[2], (Integer) obj[3]);
                        response = handleBoolean(successUpdateBasketCropQuantity);
                        os.writeObject(response);
                        notifyResponse(action);
                        
                        break;
                    case RequestAction.GET_CROP_UNIT_COST_FROM_CUSTOMER_BASKET:
                        notifyRequest(action);
                        Object[] obj2 = (Object[]) request.getData();
                        response = customerTable.getCropUnitCostFromBasket((String) obj2[0], (String) obj2[1], (Integer) obj2[2]);
                        os.writeObject(response);
                        notifyResponse(action);
                        
                        break;
                    case RequestAction.UPDATE_CUSTOMER_BASKET_CROP_TOTAL:
                        notifyRequest(action);
                        Object[] objUCBCT = (Object[]) request.getData();
                        boolean successUCBCT = customerTable.updateBasketCropTotal((String) objUCBCT[0], (String) objUCBCT[1], (Double) objUCBCT[2], (Integer) objUCBCT[3]);
                        response = handleBoolean(successUCBCT);
                        os.writeObject(response);
                        notifyResponse(action);
                        
                        break;
                    case RequestAction.ADD_CROP_TO_CUSTOMER_BASKET:
                        notifyRequest(action);
                        Crop addCrop = (Crop) request.getData();
                        boolean successACTCB =  customerTable.addCropToBasket(addCrop.getCid(), addCrop.getcName(), addCrop.getQuantity(), addCrop.getCost(), (addCrop.getQuantity()*addCrop.getCost()), addCrop.getFarmerName());
                        response = handleBoolean(successACTCB);
                        os.writeObject(response);
                        notifyResponse(action);
                        
                        break;
                    case RequestAction.CUSTOMER_VIEW_BASKET:
                        notifyRequest(action);
                        Integer RefreshBasketID = (Integer) request.getData();
                        response = customerTable.viewBasket(RefreshBasketID);
                        os.writeObject(response);
                        notifyResponse(action);
                        
                        break;
                    /*Crop Requests*/
                    case RequestAction.ADD_CROP:
                        notifyRequest(action);
                        Object[] cropInformation = (Object[]) request.getData();
                        boolean successAddCrop = cropTable.create((Crop) cropInformation[0], (byte[]) cropInformation[1]);
                        response = handleBoolean(successAddCrop);
                        os.writeObject(response);
                        notifyResponse(action);
                        
                        break;
                    case RequestAction.UPDATE_CROP:
                        notifyRequest(action);
                        Crop cropUpdate = (Crop) request.getData();
                        boolean successUpdateCrop = cropTable.update(cropUpdate);
                        response = handleBoolean(successUpdateCrop);
                        os.writeObject(response);
                        notifyResponse(action);
                        
                        break;
                    case RequestAction.UPDATE_CROP_WITH_IMAGE:
                        notifyRequest(action);
                        Object[] cropUpdateInfo = (Object[]) request.getData();
                        boolean successUpdateCropWithPic = cropTable.update((Crop) cropUpdateInfo[0], (byte[]) cropUpdateInfo[1]);
                        response = handleBoolean(successUpdateCropWithPic);
                        os.writeObject(response);
                        notifyResponse(action);
                        
                        break;
                    case RequestAction.GET_FARMER_CROPS_UPDATE_CROPS:
                        notifyRequest(action);
                        int gFCUC = (int) request.getData();
                        response = cropTable.retrieveAllCropsForAFarmerUpdate(gFCUC);
                        os.writeObject(response);
                        notifyResponse(action);
                        
                        break;
                    case RequestAction.GET_FARMER_CROPS_VIEW_CROPS:
                        notifyRequest(action);
                        int gFCVC = (int) request.getData();
                        response = cropTable.retrieveAllCropsForAFarmerView(gFCVC);
                        os.writeObject(response);
                        notifyResponse(action);
                        
                        break;
                    case RequestAction.GET_CUSTOMER_VIEW_CROPS:
                        notifyRequest(action);
                        response = cropTable.retrieveCustomerViewCrops();
                        os.writeObject(response);
                        notifyResponse(action);
                        
                        break;
                    case RequestAction.GET_CUSTOMER_VIEW_CROPS_FARMER_SEARCH:
                        notifyRequest(action);
                        String fNameForSearch = (String) request.getData();
                        response = cropTable.retrieveCustomerViewCropsSearchResults(fNameForSearch);
                        os.writeObject(response);
                        notifyResponse(action);
                        
                        break;
                    case RequestAction.CHECK_IF_CROP_ALREADY_EXISTS_FOR_FARMER:
                        notifyRequest(action);
                        String[] cropCheckInfo = (String[]) request.getData();
                        response = cropTable.checkIfFarmerCropAlreadyExists(cropCheckInfo[0], cropCheckInfo[1]);
                        os.writeObject(response);
                        notifyResponse(action);

                        break;
                    case RequestAction.UPDATE_CROP_QUANTITY_USING_ID:
                        notifyRequest(action);
                        Integer[] uCQUID = (Integer[]) request.getData();
                        boolean success = cropTable.updateCropQuantity(uCQUID[0], uCQUID[1]);
                        response = handleBoolean(success);
                        os.writeObject(response);
                        notifyResponse(action);
                        
                        break;

                    case RequestAction.LOGOUT:
                        notifyRequest(action);
                        response = logout();
                        os.writeObject(response);
                        notifyResponse(action);
                        break;

                    case RequestAction.GET_FARMERS_AVAILABLE_FOR_CHAT:
                        notifyRequest(action);
                        response.setData(clientManager.getFarmersAvailableForChat());
                        response.setErrorMessage(Response.NO_ERROR);
                        os.writeObject(response);
                        notifyResponse(action);
                        setLiveChatMode(true);
                        break;

                    case RequestAction.START_FARMER_LIVE_CHAT:
                        notifyRequest(action);
                        response.setData(true);
                        response.setErrorMessage(Response.NO_ERROR);
                        setAvailableForLiveChat(true);
                        os.writeObject(response);
                        notifyResponse(action);
                        break;

                    case RequestAction.END_LIVE_CHAT:
                        notifyRequest(action);
                        setLiveChatMode(false);
                        setAvailableForLiveChat(false);
                        /*response.setData(true);
                        response.setErrorMessage(Response.NO_ERROR);*/
                        os.writeObject(request);
                        notifyResponse(action);
                        break;

                    case RequestAction.FORWARD_MESSAGE:
                        notifyRequest(action);
                        TextMessage text = (TextMessage) request.getData();
                        text.setSender(userName);
                        response = clientManager.forwardMessage(text);
                        if((Boolean) response.getData() && (userType == CUSTOMER) )
                            farmersConnected.add(text.getRecipient());
                        Request req = new Request(RequestAction.HANDLE_SEND_MESSAGE_RESPONSE, response);
                        os.writeObject(req);
                        notifyResponse(action);
                        break;
                }
            }
        }catch (IOException e){
            if(e.toString().equals("java.net.SocketException: Connection reset") || e.toString().equals("java.io.EOFException"))
                System.out.println(userName+" disconnected from server");
            else
                e.printStackTrace();
        }catch (ClassNotFoundException e){
            e.printStackTrace();
        }finally {
            close();
            clientManager.removeClient(userName);
        }
    }

    private void setUserNameCustomer(Response<Object[]> response) {
        try {
            if (response.getErrorMessage().equals(Response.NO_ERROR)) {
                int count = (int) response.getData()[1];
                if(count == 1){
                    Customer customer = (Customer) response.getData()[0];
                    changeUserName(customer.getFirstName() + " " + customer.getLastName() + " (" + customer.getEmail() + ")");
                    setUserType(CUSTOMER);
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private void setUserNameFarmer(Response<Object[]> response) {
        try {
            if (response.getErrorMessage().equals(Response.NO_ERROR)) {
                int count = (int) response.getData()[1];
                if(count == 1){
                    Farmer farmer = (Farmer) response.getData()[0];
                    changeUserName(farmer.getFirstName() + " " + farmer.getLastName() + " (" + farmer.getEmail() + ")");
                    setUserType(FARMER);
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private Response<Boolean> handleBoolean(boolean success){
        return new Response<>(success, Response.NO_ERROR);
    }

    private Response<String> logout(){
        Response<String> response = new Response<>(null, Response.GENERAL_ERROR);
        clientManager.removeClient(userName);
        clientManager.addClient(this);
        setUserType(NEITHER);
        String res = "Successfully logged out";
        response.setData(res);
        response.setErrorMessage(Response.NO_ERROR);
        return response;
    }

    private Response<Object[]> checkIfFarmerAlreadyLoggedIn(Response<Object[]> response){
        if(!response.getErrorMessage().equals(Response.NO_ERROR))
            return response;

        try {

            int count = (int) response.getData()[1];
            Farmer farmer = (Farmer) response.getData()[0];
            if (count == 1) {
                String userName = farmer.getFirstName() + " " + farmer.getLastName() + " (" + farmer.getEmail() + ")";
                if (clientManager.userExists(userName)) {
                    response.setData(null);
                    response.setErrorMessage(Response.ALREADY_LOGGED_IN);
                    System.out.println(this.userName+"Error: Farmer already logged in");
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }


        return response;
    }

    private Response<Object[]> checkIfCustomerAlreadyLoggedIn(Response<Object[]> response){
        if(!response.getErrorMessage().equals(Response.NO_ERROR))
            return response;

        try {

            int count = (int) response.getData()[1];
            Customer customer = (Customer) response.getData()[0];
            if (count == 1) {
                String userName = customer.getFirstName() + " " + customer.getLastName() + " (" + customer.getEmail() + ")";
                if (clientManager.userExists(userName)) {
                    response.setData(null);
                    response.setErrorMessage(Response.ALREADY_LOGGED_IN);
                    System.out.println(this.userName+"Error: Customer already logged in");
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }


        return response;
    }

    private void setUserType(int userType){
        this.userType = userType;
        if(userType != FARMER)
            setAvailableForLiveChat(false);
        else if(userType != CUSTOMER)
            setLiveChatMode(false);
    }

    public int getUserType(){
        return userType;
    }

    public boolean isAvailableForLiveChat() {
        return availableForLiveChat;
    }

    private void setAvailableForLiveChat(boolean availableForLiveChat) {
        this.availableForLiveChat = availableForLiveChat;
        clientManager.updateFarmerOnlineList(userName, availableForLiveChat);
    }

    public boolean isLiveChatMode() {
        return liveChatMode;
    }

    private void setLiveChatMode(boolean liveChatMode) {
        if(!liveChatMode){
            clientManager.updateCustomerOnlineList(farmersConnected, userName);
            farmersConnected.clear();
        }
        this.liveChatMode = liveChatMode;
    }

    public void addToOnlineList(String farmerName){
        Request<String> request = new Request<>(RequestAction.ADD_TO_ONLINE_LIST, farmerName);
        sendRequest(request);
    }

    public void removeFromOnlineList(String userName){
        Request<String> request = new Request<>(RequestAction.REMOVE_FROM_ONLINE_LIST, userName);
        sendRequest(request);
    }

    public void updateOnlineList(String farmerName, boolean available){
        if(available)
            addToOnlineList(farmerName);
        else
            removeFromOnlineList(farmerName);
    }

    private void sendRequest(Request request){
        try{
            os.writeObject(request);
            System.out.println(userName+": Request: "+request.getAction()+" sent");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(TextMessage message){
        Request<TextMessage> request = new Request<>(RequestAction.RECEIVE_MESSAGE, message);

        sendRequest(request);
    }
}
