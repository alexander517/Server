package server.endpoints;

import com.google.gson.Gson;
import server.authentication.Secured;
import server.controllers.StaffController;
import server.database.DBConnection;
import server.models.Order;
import server.utility.Encryption;
import server.utility.Globals;
import server.models.Item;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.ArrayList;

//Created by Nordmenn 19-10-2017 Gruppe YOLO
@Secured
@Path("/staff")
public class StaffEndpoint {
    private Encryption encryption = new Encryption();
    private StaffController staffController = new StaffController();

    /**
     *
     * @return Response ordersAsJson
     * Retrieves all orders in the system to be used for the staff side of a client.
     */
    @Secured
    @GET
    @Path("/getOrders")
    public Response getOrders() {
        ArrayList<Order> orders;
        int status = 500;
        orders = staffController.getOrders();

        if (!(orders == null)) {
            status = 200;
        }

        String ordersAsJson = new Gson().toJson(orders);

        return Response
                .status(status)
                .type("application/json")
                //encrypt response
                .entity(encryption.encryptXOR(ordersAsJson))
                .build();

    }

        @Secured
        @POST
        @Path("/createItem")
        public Response createItem(String jsonItem){
            jsonItem = encryption.encryptXOR(jsonItem);
            Item itemToBeCreated = new Gson().fromJson(jsonItem, Item.class);
            int status = 500;
            boolean result = staffController.createItem(itemToBeCreated.getItemName(), itemToBeCreated.getItemDescription(), itemToBeCreated.getItemPrice(), itemToBeCreated.getItemUrl());

            if (result) {
                status = 200;
                //Logging for order created
                Globals.log.writeLog(getClass().getName(), this, "Created item with id: " + itemToBeCreated.getItemId(), 0);

            } else if (!result) {
                status = 500;
                Globals.log.writeLog(getClass().getName(), this, "Internal Server Error 500", 1);

            }

            return Response
                    .status(status)
                    .type("plain/text")
                    //encrypt response to clien before sending
                    .entity(encryption.encryptXOR("{\"itemCreated\":\"" + result + "\"}"))
                    .build();
        }
}
