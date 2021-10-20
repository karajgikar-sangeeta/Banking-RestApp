package com.banking.svkbanking.controller;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.banking.svkbanking.entity.Role;
import com.banking.svkbanking.entity.User;
import com.banking.svkbanking.entity.UserAddress;
import com.banking.svkbanking.repository.RoleRepository;
import com.banking.svkbanking.repository.UserAddressRepository;
import com.banking.svkbanking.repository.UserRepository;
import com.banking.svkbanking.security.SecurityService;


@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api")
public class UserController {
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private UserAddressRepository userAddressRepo;
	
	@Autowired
	private RoleRepository roleRepo;
	
	@Autowired
	private SecurityService securityService;
	
	@Autowired
	private PasswordEncoder encoder;
	
	
	@CrossOrigin(origins = "http://localhost:4200")
	@GetMapping("/users/{id}")
	public ResponseEntity<User> getUserById(@PathVariable(value = "id") Long userId) throws ResourceNotFoundException {
		
		User user = userRepo
				.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User not found " + userId));
		
		return ResponseEntity.ok(user);
	}
		
	@CrossOrigin(origins = "http://localhost:4200")
	@PostMapping("/users")
	public ResponseEntity<?> postUser(@RequestBody User user) {
		
		var userEmail = user.getEmail();		
		var found = userRepo.findByEmail(userEmail);
		if (found != null) {
			return ResponseEntity.badRequest().body("User with email " + userEmail + " alread exists");	
		}

		UserAddress userAddress = new UserAddress();
		userAddress.setCity(user.getUserAddress().getCity());
		userAddress.setState(user.getUserAddress().getState());
		userAddress.setStreetName(user.getUserAddress().getStreetName());
		userAddress.setStreetNumber(user.getUserAddress().getStreetNumber());
		userAddress.setZip(user.getUserAddress().getZip());
		UserAddress savedUserAddress = userAddressRepo.save(userAddress);

		Role userRole = roleRepo.findByRoleName("USER");
		if (userRole != null) {
			List<Role> rolesList = new ArrayList<Role>();
			rolesList.add(userRole);
			user.setRoles(rolesList);
		}
	
		
		Date localDate = new Date();
		user.setDateCreated(localDate);
		user.setPassword(encoder.encode(user.getPassword()));
		
		user.getUserAddress().setAddressId(savedUserAddress.getAddressId());
		
		
		try	{
			var savedUser = userRepo.save(user);
			return ResponseEntity.ok(savedUser);	
		} catch (Exception ex) {
			throw ex;			
		}
	}
	
	@CrossOrigin(origins = "http://localhost:4200")
	@PostMapping("/users/login")
	public ResponseEntity<?> loginUser(@RequestParam String userName, @RequestParam String password) {				
		boolean response = securityService.login(userName, password);
		if (response) {			
			User user = userRepo.findByEmail(userName);
			return ResponseEntity.ok(user);
		} else {
			return ResponseEntity.badRequest().body("User Email " + userName + " or password is invalid");
		}
	}
}