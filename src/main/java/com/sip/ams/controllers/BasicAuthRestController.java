package com.sip.ams.controllers;

import org.springframework.web.bind.annotation.CrossOrigin;
import com.sip.ams.entities.User;
import org.springframework.security.core.context.SecurityContextHolder;
import com.sip.ams.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import com.sip.ams.entities.AuthenticationBean;

@CrossOrigin(origins = "*")
@RestController
public class BasicAuthRestController {
	@Autowired
	private UserService userService;

	@GetMapping(path = "/basicauth")
	public User basicauth() {
		// return "User Amine : Role Admin";
		// return new User("ma@gmail.com","1234");
		// return "Success";
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = userService.findUserByEmail(auth.getName());
		return user;
	}
	/*
	 * public Authentication basicauth() { return new Authentication("Spring");
	 * //return "Success"; }
	 */
} 

