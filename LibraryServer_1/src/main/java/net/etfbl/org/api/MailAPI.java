package net.etfbl.org.api;

import java.util.ArrayList;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import net.etfbl.org.model.Book;
import net.etfbl.org.services.MailService;

@Path("/mail")
public class MailAPI {

	private MailService mailService;
	
	public MailAPI() {
		mailService = new MailService();
	}
	
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response sendMail(@QueryParam("mail") String mail, ArrayList<Book> books) {
		if(mailService.sendMail(mail, books))
			return Response.status(200).build();
		else
			return Response.status(400).build();
	}
	
	
	
}
