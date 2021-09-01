package com.sip.ams.controllers;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.sip.ams.configuration.JwtTokenUtil;
import com.sip.ams.models.JwtRequest;
import com.sip.ams.models.JwtResponse;
import com.sip.ams.entities.User;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import com.sip.ams.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

@CrossOrigin(origins = "*")
@RestController
public class BasicAuthRestController {
	@Autowired
	private UserService userService;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@RequestMapping(value = { "/basicauth" }, method = RequestMethod.POST)

	public JwtResponse generateAuthenticationToken(@RequestBody JwtRequest authenticationRequest) throws Exception {

		final UserDetails userDetails = userService.findUserByEmail(authenticationRequest.getUsername());

		// System.out.println(userDetails);
		// génération du Token

		User user = (User) userDetails;
		final String token = jwtTokenUtil.generateToken(userDetails);

		return new JwtResponse(token,user.getName(), user.getLastName(), user.getRoles(), user.getTemp(), user.getId(),user.getPicture());
	}

	/*
	 * private void authenticate(String username, String password) throws Exception
	 * { try {
	 * 
	 * authenticationManager.authenticate(new
	 * UsernamePasswordAuthenticationToken(username, password));
	 * 
	 * } catch (DisabledException e) { throw new Exception("USER_DISABLED", e); }
	 * catch (BadCredentialsException e) { throw new
	 * Exception("INVALID_CREDENTIALS", e); }
	 * 
	 * catch (Exception e) { // throw new Exception("INVALID_CREDENTIALS", e);
	 * e.printStackTrace(); } }
	 */

}