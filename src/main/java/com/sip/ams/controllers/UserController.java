package com.sip.ams.controllers;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.sip.ams.entities.Provider;
import com.sip.ams.entities.Role;
import com.sip.ams.entities.User;
import com.sip.ams.repositories.ProviderRepository;
import com.sip.ams.repositories.UserRepository;
import com.sip.ams.repositories.RoleRepository;
@RestController
@RequestMapping({ "/users", "/hom*" })
@CrossOrigin(origins = "*")
public class UserController {
	// private final Path root = Paths.get(System.getProperty("user.dir") +
	// "/src/main/resources/static/uploads");

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private RoleRepository roleRepository;
	private final Path root = Paths.get(System.getProperty("user.dir") + "/src/main/resources/static/images/user");
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	@GetMapping("/list")
	public List<User> getAllUsers() {
		return (List<User>) userRepository.findAll();
	}

	@PutMapping("/{userId}")
	public User updateUser(@PathVariable Integer userId,

			@RequestParam("name") String name, @RequestParam("email") String email,
			@RequestParam("lastName") String lastname, @RequestParam("password") String password
			

	// @Valid @RequestBody Provider providerRequest

	) {
		return userRepository.findById(userId).map(user -> {
			

			user.setName(name);
			user.setEmail(email);
			user.setLastName(lastname);
			user.setPassword(bCryptPasswordEncoder.encode(password));
		
			return userRepository.save(user);
		}).orElseThrow(() -> new IllegalArgumentException("UserId " + userId + " not found"));
	}

	@PutMapping("/pic/{userId}")
	public User updateUserPicture(@PathVariable Integer userId,

			@RequestParam("imageFile") MultipartFile[] files, @RequestParam("imageName") String imageName

	

	) {
		return userRepository.findById(userId).map(user -> {
			
			
			
			

		
			StringBuilder fileName = new StringBuilder();
			MultipartFile file = files[0];
			Path fileNameAndPath = Paths.get(System.getProperty("user.dir") + "/src/main/resources/static/images/user",
					file.getOriginalFilename());
			fileName.append(file.getOriginalFilename());
			try {
				Files.write(fileNameAndPath, file.getBytes());
			} catch (IOException e) {
				e.printStackTrace();
			}

			

				user.setPicture(fileName.toString());
			
			return userRepository.save(user);
		}).orElseThrow(() -> new IllegalArgumentException("UserId " + userId + " not found"));
	}

	@GetMapping("/{userId}")
	public User getUser(@PathVariable int userId) {
		Optional<User> u = userRepository.findById(userId);
		return u.get();
	}
	
	
	

}
