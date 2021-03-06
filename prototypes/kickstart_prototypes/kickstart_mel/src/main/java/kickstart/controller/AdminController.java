package kickstart.controller;

import org.salespointframework.useraccount.Role;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import kickstart.Store;
import kickstart.model.StaffMember;

@Controller
public class AdminController {
	
	public AdminController(){}
	
	@RequestMapping("/registeremployee")
	public String registrationIndex(Model model){
		return "registeremployee";
	}
	
	
	@RequestMapping(value = "/registerEmployee", method = RequestMethod.POST)
	public String addStaffMember(@RequestParam  ("surname")   String  surname,
								 @RequestParam  ("forename")  String  forename,
								 @RequestParam  ("telnumber") String  telephonenumber,
								 @RequestParam  ("username")  String  username,
								 @RequestParam  ("password")  String  password,
								 @RequestParam  ("role") 	  String  role            ){
		
		
		System.out.println("Role" + role);
		
		if ( surname == "" || forename == ""  || 
			 telephonenumber == ""|| username == ""|| password == "" || role == "" ) {			
			return "registeremployee";
		}

		StaffMember staffMember = new StaffMember(surname,forename,telephonenumber);
		staffMember.updateUserAccount(username, password, Role.of("ROLE_" + role));
		
		return "welcome";
	}
	
}
