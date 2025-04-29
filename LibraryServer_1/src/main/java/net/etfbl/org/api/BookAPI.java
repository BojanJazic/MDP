package net.etfbl.org.api;

import java.util.ArrayList;
import java.util.logging.Level;

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
import net.etfbl.org.model.Book;
import net.etfbl.org.services.BookService;

@Path("/books")
public class BookAPI {

	private BookService bookService = null;
	
	
	  public BookAPI() { 
		  bookService = new BookService(); 
	  }
	
	  @GET
	  @Produces(MediaType.APPLICATION_JSON) 
	  @Path("/getBooks")
	  public Response getAllBooks() { 
		  try {
			  ArrayList<Book> books = bookService.getAllBooks();
	  
			  if(books.isEmpty()) {
				  LibraryServerLogger.LOGGER.info("Method getAllBooks() in BookAPI: returned list is empty!"); 
				  return Response.status(204).entity("List is empty!").build(); 
			  }
	  
			  return Response.status(200).entity(books).build(); 
		  } catch (Exception e) { 
			  LibraryServerLogger.LOGGER.log(Level.SEVERE, "An error occured while getting all books!", e); 
			  return Response.status(500).entity("An error occured while fetching books!").build(); 
		  } 
	  }
	  	
	 
	  @GET
	  @Path("/getByTitle/{title}")
	  @Produces(MediaType.APPLICATION_JSON) 
	  public Response getBookByTitle(@PathParam("title") String title) {
		  Book book = bookService.getBookByTitle(title);
	  
		  if(book.getBookTitle() == null) { 
			  LibraryServerLogger.LOGGER.info("The book " + title + " doesn't exist!"); 
			  return Response.status(404).entity("Book not found!").build(); 
		  }
	  
		  return Response.status(200).entity(book).build(); 
	  }
	  
	  
	  @GET
	  @Path("/getByWriter/{writerName}")
	  @Produces(MediaType.APPLICATION_JSON) 
	  public Response getBooksByWriter(@PathParam("writerName") String writerName) { 
		  try {
			  ArrayList<Book> booksByWriter = bookService.getBooksByWriter(writerName);
	  
			  if(booksByWriter.isEmpty()) {
				  LibraryServerLogger.LOGGER.info("There are no books for the wanted writer!");
				  return Response.status(404).entity("Writer not found!").build(); 
			  }
	  
			  return Response.status(200).entity(booksByWriter).build(); 
		  } catch (Exception e) { 
			  LibraryServerLogger.LOGGER.log(Level.SEVERE, "An error occured while fetching books for wanted writer.", e); 
			  return Response.status(500).build(); 
		  } 
	  }
	  
	  @GET
	  @Path("/getByPublicationYear/{publicationYear}")
	  @Produces(MediaType.APPLICATION_JSON) 
	  public Response getBooksByPublicationYear(@PathParam("publicationYear") String publicationYear) { 
		  try { ArrayList<Book> books = bookService.getBooksByPublicationYear(publicationYear);
	  
		  if(books.isEmpty()) {
			  LibraryServerLogger.LOGGER.info("There are no books for the requested year!"); 
			  return Response.status(404).entity("There are no books for the requested year!").build(); 
		  }
	  
		  return Response.status(200).entity(books).build();
	  
		  } catch (Exception e) {
			  LibraryServerLogger.LOGGER.log(Level.SEVERE, "An error occured while fetching books for requested year.", e); 
			  return Response.status(500).build(); 
		  } 
	  }
	  
	  
	  @POST 
	  @Produces(MediaType.APPLICATION_JSON)
	  @Consumes(MediaType.APPLICATION_JSON) 
	  @Path("/addNewBook")
	  public Response addNewBook(Book book) {
		  if(bookService.addNewBook(book)) { 
			  LibraryServerLogger.LOGGER.info("Book " + book.getBookTitle() + " added successfully."); 
			  return Response.status(201).entity(book).build(); 
		  }
	  
		  LibraryServerLogger.LOGGER.info("The book already exists."); 
		  return Response.status(409).entity("The book already exists.").build();
	  }
	  
	  
	  @PUT
	  @Produces(MediaType.APPLICATION_JSON)
	  @Consumes(MediaType.APPLICATION_JSON) 
	  @Path("/update")
	  public Response updateBook(Book book) {
		  if(bookService.updateExistingBook(book)) {
			  LibraryServerLogger.LOGGER.info("The book " + book.getBookTitle() + " has been updated successfully."); 
			  return Response.status(200).entity(book).build(); 
		  }
	  
		  LibraryServerLogger.LOGGER.info("The book " + book.getBookTitle() + " hasn't been updated."); 
		  return Response.status(400).build();
	  }
	  
	  
	  @DELETE
	  @Produces(MediaType.APPLICATION_JSON)
	  @Consumes(MediaType.APPLICATION_JSON)
	  @Path("/delete")
	  public Response deleteBook(Book book) {
		  if(bookService.deleteBook(book)) {
			  LibraryServerLogger.LOGGER.info("The book " + book.getBookTitle() + " has been deleted successfully."); 
			  return Response.status(200).entity(book).build(); 
		  }
	  
		  LibraryServerLogger.LOGGER.info("The book " + book.getBookTitle() + " hasn't been deleted."); 
		  return Response.status(400).entity(book).build(); 
	  }
	 
}