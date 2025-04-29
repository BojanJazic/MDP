package net.etfbl.org.services;

import java.util.ArrayList;
import java.util.Optional;
import java.util.logging.Level;
import java.util.stream.Collectors;

import com.google.gson.Gson;

import net.etfbl.org.connection.JedisConnection;
import net.etfbl.org.logger.LibraryServerLogger;
import net.etfbl.org.model.Book;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;

public class BookService {

	private Jedis jedis = JedisConnection.getInstance();
	private ArrayList<Book> books = new ArrayList<>();
	
	
	public void retrieveAllBooksFromDB() {
		try {
			
			Gson gson = new Gson();
			
			//lrange vraca sve podatke odjednom te nema potrebe za koristenjem pipline mehanizma kao kod dodavanja
			
			books.clear();
			
			jedis.lrange("books", 0, -1)
				 .stream()
				 .map(bookJson -> gson.fromJson(bookJson, Book.class))
				 .forEach(books::add);
			
			
		} catch (Exception e) {
			LibraryServerLogger.LOGGER.log(Level.SEVERE, "An error occured while reading books from Redis!", e);
		}
	}
	
	
	public void saveAllBooksToDB() {
		try {
			Gson gson = new Gson();
			//brisu se postojece knjige sa Redis kljucem "books" i ovim se osigurava da stara lista knjiga ne ostane u bazi
			jedis.del("books");
			
			Pipeline pipline = jedis.pipelined();	//inicijalizacija pipline-a
			
			books.stream().map(gson::toJson)
						  .forEach(bookJson -> pipline.rpush("books", bookJson));
			
			pipline.sync();		//izvrsavanje svih komandi u batch-u
			
			/**
			 * Pipeline omogućava da Redis komande budu izvršene odjednom, 
			 * smanjujući broj round-trip zahteva između aplikacije i Redis servera, 
			 * što može značajno poboljšati performanse.
			 */
			
		} catch (Exception e) {
			LibraryServerLogger.LOGGER.log(Level.SEVERE, "An error occured while saving books to Redis!", e);
		}
	}
	
	public ArrayList<Book> getAllBooks(){
		retrieveAllBooksFromDB();
		return books;
	}
	
	
	public Book getBookByTitle(String title) {
		retrieveAllBooksFromDB();
		
		return books.stream()
					.filter(book -> book.getBookTitle().equalsIgnoreCase(title))
					.findFirst()
					.orElse(new Book());
		
	}
	
	public ArrayList<Book> getBooksByWriter(String writer) {
		retrieveAllBooksFromDB();
		
		return books.stream()
					.filter(book -> book.getBookAuthor().equalsIgnoreCase(writer))
					.collect(Collectors.toCollection(ArrayList::new));
		
	}
	
	public ArrayList<Book> getBooksByPublicationYear(String year) {
		retrieveAllBooksFromDB();
		
		return books.stream()
					.filter(book -> book.getPublicationYear().equals(year))
					.collect(Collectors.toCollection(ArrayList::new));
	}
	
	public boolean addNewBook(Book book) {
		retrieveAllBooksFromDB();
		
		boolean bookExists = books.stream()
								  .anyMatch(b -> b.equals(book));
		
		if(!bookExists) {
			books.add(book);
			saveAllBooksToDB();
			LibraryServerLogger.LOGGER.info("Book " + book.getBookTitle() + " has been successfully added!");
			return true;
		}
		
		return false;
	}
	
	public boolean updateExistingBook(Book book) {
		retrieveAllBooksFromDB();
		
		Optional<Book> existingBook = books.stream()
			 .filter(b -> b.equals(book))
			 .findFirst();
			 
		if(existingBook.isPresent()) {
			books.remove(existingBook.get());
			
			books.add(book);
			saveAllBooksToDB();
			LibraryServerLogger.LOGGER.info("The Book " + book.getBookTitle() + " has been updated successfully!");
			
			return true;
		}
		
		LibraryServerLogger.LOGGER.info("The Book " + book.getBookTitle() + " hasn't been updated successfully!");
		return false;
		
	}
	
	public boolean deleteBook(Book book) {
		retrieveAllBooksFromDB();
		
		Optional<Book> bookToDelete = books.stream()
										   .filter(b -> b.equals(book))
										   .findFirst();
		
		if(bookToDelete.isPresent()) {
			books.remove(bookToDelete.get());
			saveAllBooksToDB();
			LibraryServerLogger.LOGGER.info("The book " + book.getBookTitle() + " has been deleted successfully!");
			
			return true;
		}
		
		return false;
		
	}
	
	
	
	
}
