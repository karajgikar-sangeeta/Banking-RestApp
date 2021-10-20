package com.banking.svkbanking.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.banking.svkbanking.entity.User;
import com.banking.svkbanking.repository.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private UserRepository userRepo;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
				
		User user = userRepo.findByEmail(username);
		if (user == null) {
			throw new UsernameNotFoundException("Invalid username " + username);
		}
		
		System.out.println("User Email: " + user.getEmail() + " UserPassword: " + user.getPassword() + " Roles: " + user.getRoles());
		
		return new org.springframework.security.core.userdetails.User(
				user.getEmail(), 
				user.getPassword(), 
				user.getRoles());	
	}
}
