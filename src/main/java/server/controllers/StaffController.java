package server.controllers;

import server.database.DBConnection;
import server.models.Order;
import server.endpoints.StaffEndpoint;

import java.util.ArrayList;

/**
 * Class responsible for all logic related to staff operations
 */
public class StaffController {
    private DBConnection dbConnection;

    public StaffController(){
        this.dbConnection = new DBConnection();

    }

    /**
     * Returns the list of all orders currently in the database
     */
    public ArrayList<Order> getOrders() {
        ArrayList<Order> orders = dbConnection.getOrders();
        return orders;
    }


}

