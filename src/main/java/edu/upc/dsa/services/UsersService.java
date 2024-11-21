package edu.upc.dsa.services;

import edu.upc.dsa.Manager;
import edu.upc.dsa.ManagerImpl;
import edu.upc.dsa.exceptions.UserNotFoundException;
import edu.upc.dsa.exceptions.WrongPasswordException;
import edu.upc.dsa.models.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.ArrayList;

@Api("/users")
@Path("/users")
public class UsersService {

    Manager manager = ManagerImpl.getInstance();

    public UsersService()  {
        if(manager.sizeUsers() == 0){
            manager.addUser("jan", "jan", "jan.vinas@estudiantat.upc.edu");
            try {
                manager.addPuntos("jan", 5);
            } catch (UserNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @PUT
    @ApiOperation("Create and logs in a new user")
    @Path("/register")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "User created", response = User.class),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 409, message = "User already exists")
    })
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createUser(User user) {
        if(!user.validate()){ return Response.status(Response.Status.BAD_REQUEST).build(); }

        // only add the user if it doesn't exist
        try{
            manager.getUser(user.getUsername());
            return Response.status(Response.Status.CONFLICT).build();
        } catch (UserNotFoundException e) {
            manager.register(user.getUsername(), user.getPassword(), user.getMail());

            // creating a user also logs in
            UserToken token = manager.generateToken(user.getUsername());
            NewCookie cookie = new NewCookie("token", token.getToken(), "/", null, null, UserToken.MAX_AGE, false);
            GenericEntity<User> entity = new GenericEntity<User>(user){};
            return Response
                    .status(Response.Status.CREATED)
                    .cookie(cookie)
                    .entity(entity)
                    .build();
        }
    }

    @POST
    @ApiOperation("Attempts to log in")
    @Path("/login")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = User.class),
            @ApiResponse(code = 403, message = "Incorrect Credentials"),
            @ApiResponse(code = 400, message = "Bad Request")
    })
    @Consumes(MediaType.APPLICATION_JSON)
    public Response login(LoginRequest login) {
        User user;
        try{
            if(login.getUsername() != null){
                user = manager.login1(login.getUsername(), login.getPassword());
            }else if(login.getPassword() != null){
                user = manager.login2(login.getEmail(), login.getPassword());
            }else{
                return Response.status(Response.Status.BAD_REQUEST).build();
            }
        }catch(UserNotFoundException | WrongPasswordException e){
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        UserToken token = manager.generateToken(user.getUsername());
        NewCookie cookie = new NewCookie("token", token.getToken(), "/", null, null, UserToken.MAX_AGE, false);
        GenericEntity<User> entity = new GenericEntity<User>(user){};
        return Response
                .status(Response.Status.OK)
                .cookie(cookie)
                .entity(entity)
                .build();
    }

    @GET
    @ApiOperation("Gets objects from user")
    @Path("/getObjects/{username}")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = InventoryObject.class, responseContainer = "List"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "User not found")
    })
    @Produces(MediaType.APPLICATION_JSON)
    public Response getObjects(@PathParam("username") String username, @CookieParam("token") Cookie token) {
        if(token == null || !manager.validateToken(username, token.getValue())){
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        try{
            GenericEntity<ArrayList<InventoryObject>> entity = new GenericEntity<ArrayList<InventoryObject>>(manager.getUserObjects(username)) {};
            return Response
                    .status(Response.Status.OK)
                    .entity(entity)
                    .build();
        }catch(UserNotFoundException e){
            // crec que mai hauriem d'arribar aquí
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }
    @GET
    @Path("/puntos/{username}")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = String.class),
            @ApiResponse(code = 403, message = "Incorrect credentials")
    })
    @Produces(MediaType.TEXT_PLAIN)
    public Response getPuntos(@PathParam("username") String username, @CookieParam("token") Cookie token){
        if(token == null || !manager.validateToken(username, token.getValue())){
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        try{
            GenericEntity<Integer> entity = new GenericEntity<Integer>(manager.getUser(username).getPuntos()) {};
            return Response.ok(entity).build();
        }catch(UserNotFoundException ignored){
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

    }

}
