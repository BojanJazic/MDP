package net.etfbl.org.model;

import java.io.Serializable;
import java.util.Objects;

public class User implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	private String firstName;
	private String lastName;
	private String address;
	private String email;
	private String username;
	private String password;
	private Boolean accountActivated;
	private Boolean accountBlocked;
	
	
	
	public User() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public User(String username, String password) {
		this.username = username;
		this.password = password;
	}


	public User(String firstName, String lastName, String address, String eMail, String username, 
				String password, Boolean accountActivated, Boolean accountBlocked) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.address = address;
		this.email = eMail;
		this.username = username;
		this.password = password;
		this.accountActivated = accountActivated;
		this.accountBlocked = accountBlocked;
	}


	public String getFirstName() {
		return firstName;
	}


	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}


	public String getLastName() {
		return lastName;
	}


	public void setLastName(String lastName) {
		this.lastName = lastName;
	}


	public String getAddress() {
		return address;
	}


	public void setAddress(String address) {
		this.address = address;
	}


	

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUsername() {
		return username;
	}


	public void setUsername(String username) {
		this.username = username;
	}


	public String getPassword() {
		return password;
	}


	public void setPassword(String password) {
		this.password = password;
	}

	public Boolean getAccountActivated() {
		return accountActivated;
	}

	public void setAccountActivated(Boolean accountActivated) {
		this.accountActivated = accountActivated;
	}

	public Boolean getAccountBlocked() {
		return accountBlocked;
	}

	public void setAccountBlocked(Boolean accountBlocked) {
		this.accountBlocked = accountBlocked;
	}

	@Override
	public int hashCode() {
		return Objects.hash(username);
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User user = (User) obj;
		
		return Objects.equals(this.username, user.username);
	}


	@Override
	public String toString() {
		return "User [firstName=" + firstName + ", lastName=" + lastName + ", address=" + address + ", eMail=" + email
				+ ", username=" + username + ", accountActivated=" + accountActivated + ", accountBlocked="
				+ accountBlocked + "]";
	}
	
	
	
	
	
	
	
	

}
