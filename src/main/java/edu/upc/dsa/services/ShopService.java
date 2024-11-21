package edu.upc.dsa.services;

import com.google.common.annotations.GwtCompatible;
import edu.upc.dsa.Manager;
import edu.upc.dsa.ManagerImpl;
import edu.upc.dsa.exceptions.NotEnoughMoneyException;
import edu.upc.dsa.exceptions.ObjectNotFoundException;
import edu.upc.dsa.exceptions.UserNotFoundException;
import edu.upc.dsa.models.StoreObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import javax.ws.rs.*;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Api("/shop")
@Path("/shop")
public class ShopService {
    private final Manager manager = ManagerImpl.getInstance();

    public ShopService() {
        if(manager.sizeObjects() == 0) {
            manager.addToStore("Poció màgica", 15);
            manager.addToStore("Plàtan", 1);
            manager.addToStore("Pell de plàtan", 0.3);
            try {
                manager.buyObject("jan", "Plàtan", 4);
                manager.buyObject("jan", "Pell de plàtan", 14);
            } catch (UserNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @GET
    @Path("/listObjects")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = StoreObject.class, responseContainer = "List")
    })
    @Produces(MediaType.APPLICATION_JSON)
    public Response getItems(){
        GenericEntity<List<StoreObject>> entity = new GenericEntity<List<StoreObject>>(manager.findAllObjects()) {};
        return Response.ok(entity).build();
    }


    @POST
    @Path("/buy/{object}/{username}/{quantity}")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 403, message = "Incorrect credentials"),
            @ApiResponse(code = 404, message = "Object not found"),
            @ApiResponse(code = 402, message = "Not enough money")

    })
    public Response buyObject(@PathParam("object") String object,
                              @PathParam("username") String username,
                              @PathParam("quantity") int quantity,
                              @CookieParam("token") Cookie token) {
        if(token == null || !manager.validateToken(username, token.getValue())){
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        try{
            manager.buyObject(username, object, quantity);
        }catch (ObjectNotFoundException e){
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .build();
        }catch(UserNotFoundException e){
            // in theory, it's impossible to reach this
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .build();
        }catch(NotEnoughMoneyException e){
            return Response
                    .status(Response.Status.PAYMENT_REQUIRED)
                    .build();
        }

        return Response.ok().build();
    }


    @GET
    @Path("/money/{username}")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = String.class),
            @ApiResponse(code = 403, message = "Incorrect credentials")
    })
    @Produces(MediaType.TEXT_PLAIN)
    public Response getMoney(@PathParam("username") String username, @CookieParam("token") Cookie token){
        if(token == null || !manager.validateToken(username, token.getValue())){
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        try{
            GenericEntity<Double> entity = new GenericEntity<Double>(manager.getUser(username).getMoney()) {};
            return Response.ok(entity).build();
        }catch(UserNotFoundException ignored){
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

    }


}
