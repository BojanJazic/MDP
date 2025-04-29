package net.etfbl.org.api;

import java.nio.file.Paths;
import java.util.ArrayList;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import net.etfbl.org.logger.LibraryServerLogger;
import net.etfbl.org.model.User;
import net.etfbl.org.propertiesLoader.PropertiesFileLoader;
import net.etfbl.org.services.UserService;


@Path("/users")
public class UserAPI {
	
	private UserService userService;
	
	public UserAPI() {
		userService = new UserService();
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getAllUsers")
	public Response getAllUsers() {	
		try {
			ArrayList<User> users = userService.getAllUsers();
		  
			if(users.isEmpty()) { 
				LibraryServerLogger.LOGGER.info("Method getAllUsers() in UserAPI: returned list is empty!");
				return Response.status(204).build(); 
			}
		  
			return Response.status(200).entity(users).build(); 
		} catch (Exception e) { 
			LibraryServerLogger.LOGGER.severe("Error fetching users: " + e); 
			return Response.status(500).entity("Error fetching users").build(); 
		}	
	}
	
	@GET
	@Path("{username}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getUserByUsername(@PathParam("username") String username) {
		User user = userService.getUserByUsername(username);
		
		if(user.getUsername() == null) {
			LibraryServerLogger.LOGGER.info("User " + username + " doesn't exist in users.xml file!");
			return Response.status(404).entity("User" + username + " doesn't exist in users.xml file!").build();
		}
		
		return Response.status(200).entity(user).build();
		
	}
	
	
	@GET
	@Path("/requests")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getRegistrationRequests() {
		ArrayList<User> userRequests = userService.getRegistrationRequests();
		
		if(userRequests.isEmpty()) {
			return Response.status(204).entity("No registration required!").build();
		}else {
			return Response.status(200).entity(userRequests).build();
		}
		
	}
	
	@POST
	@Path("/registration")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response registerNewUser(User user) {
		if(userService.registerNewUser(user)) {
			return Response.status(201).entity(user).build();
		}else {
			return Response.status(409).entity("Registration denied! User already exists!").build();	// 409 Conflict je prikladniji za postojeće korisnike
		}
	}
	
	@PUT
	@Path("/update")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateUser(User user) {
		if(userService.updateAccount(user)) {
			return Response.status(200).entity(user).build();
		}else {
			return Response.status(400).build();
		}
	}
	
	@POST
	@Path("/login")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response login(User user) {
		if(userService.login(user)) {
			return Response.status(200).entity(user).build();
		}else {
			LibraryServerLogger.LOGGER.info("Login unsuccessfull for user: " + user.getUsername());
			return Response.status(401).entity("Login unsuccessfull!").build();
		}
	}
	
	@POST
	@Path("/accept")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response acceptRegistrationRequest(User user) {
		if(userService.acceptRegistrationRequest(user)) {
			return Response.status(200).entity(user).build();
		}else {
			return Response.status(404).entity("User not found or registration request cannot be processed.").build();
		}
	}
	
	@POST
	@Path("/decline")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response declineRegistrationRequest(User user) {
		if(userService.declineRegistrationRequest(user)) {
			return Response.status(200).build();
		}else {
			return Response.status(400).build();
		}
	}
	
	
	@DELETE
	@Path("/delete")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response deleteUserAccount(User user) {
		if(userService.deleteAccount(user)) {
			return Response.status(200).entity("User account has been successfully deleted!").build();
		}else {
			return Response.status(400).entity("User account not found or could not be deleted.").build();
		}
	}
	
	
}
