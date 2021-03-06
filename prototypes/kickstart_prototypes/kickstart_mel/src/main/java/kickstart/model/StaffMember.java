package kickstart.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.salespointframework.useraccount.*;

import kickstart.Store;

@Entity
public class StaffMember extends Person{
	
	
	@Id @GeneratedValue private long employeeID;
	
	private String username;
	private String password;
	private UserAccount userAccount; 
	private Role role;
	
	
	public StaffMember(String surname, String forename, String telephoneNumber) {
		
		super(surname, forename, telephoneNumber);
		
	}
	
	
	public void updateUserAccount(String username, String password, Role role){
	
		
		if (this.userAccount == null){
			setUsername(username);
			setPassword(password);
			setRole(role);
			
			setUserAccount(Store.employeeAccountManager.create(this.username,this.password,this.role));
			Store.employeeAccountManager.save(this.userAccount);
		}else{
			// updateUserAccount
		}
				
		
		
		
	}
	
	public void setUserAccount(UserAccount userAccount)	{ this.userAccount = userAccount;}
	public void setUsername(String username)			{ this.username = username;}
	public void setPassword(String password)			{ this.password = password;}
	public void setRole(Role role)						{ this.role = role;}
	
	public UserAccount getUserAccount()					{ return this.userAccount;}
}
