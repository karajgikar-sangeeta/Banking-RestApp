package com.banking.svkbanking.controller;


import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.banking.svkbanking.entity.User;
import com.banking.svkbanking.entity.UserRole;
import com.banking.svkbanking.repository.UserRepository;
import com.banking.svkbanking.repository.UserRoleRepository;


@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api")
public class UserController {
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired 
	private UserRoleRepository userRoleRepo;
	
	@CrossOrigin(origins = "http://localhost:4200")
	@GetMapping("/users/{id}")
	public ResponseEntity<User> getUserById(@PathVariable(value = "id") Long userId) throws ResourceNotFoundException {
		
		User user = userRepo
				.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User not found " + userId));
		
		return ResponseEntity.ok(user);
	}
	
	@CrossOrigin(origins = "http://localhost:4200")
	@GetMapping("/users/roles")
	public ResponseEntity<List<UserRole>> getAllRoles() throws ResourceNotFoundException {
		List<UserRole> userRoles = userRoleRepo.findAll();
		return ResponseEntity.ok(userRoles);
	}
	
	@CrossOrigin(origins = "http://localhost:4200")
	@PostMapping("/users")
	public ResponseEntity<?> postUser(@RequestBody User user) {
		
		var userEmail = user.getEmail();		
		var found = userRepo.findByEmail(userEmail);
		if (found.isPresent()) {
			return ResponseEntity.badRequest().body("User with email " + userEmail + " alread exists");
		}
		
		Optional<UserRole> userRoleOptional = userRoleRepo.findById(user.getUserRole().getRoleId());
		if (!userRoleOptional.isPresent()) {
			return ResponseEntity.badRequest().body("User Role " + user.getUserRole().getRoleId() + " not found");
		}
		
		Date localDate = new Date();
		user.setDateCreated(localDate);
		user.setUserRole(userRoleOptional.get());
		try	{
			var savedUser = userRepo.save(user);
			return ResponseEntity.ok(savedUser);	
		} catch (Exception ex) {
			throw ex;			
		}
	}
	
	@CrossOrigin(origins = "http://localhost:4200")
	@PostMapping("/users/login")
	public ResponseEntity<?> loginUser(@RequestBody User user) {
		
		var userEmail = user.getEmail();
		var found = userRepo.findByEmail(userEmail);
		if (found.isPresent()) {
			User optUser = found.get();
			
			if (optUser.equals(user)) {
				return ResponseEntity.ok(optUser);
			} else {
				return ResponseEntity.badRequest().body("User Email " + userEmail + " not found");
			}
		} else {
			return ResponseEntity.badRequest().body("User Email " + userEmail + " not found");
		}
	}

}

