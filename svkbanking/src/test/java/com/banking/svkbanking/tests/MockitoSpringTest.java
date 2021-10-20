package com.banking.svkbanking.tests;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import com.banking.svkbanking.controller.AccountController;
import com.banking.svkbanking.controller.UserController;
import com.banking.svkbanking.entity.User;
import com.banking.svkbanking.repository.AccountRepository;
import com.banking.svkbanking.repository.RoleRepository;
import com.banking.svkbanking.repository.UserAddressRepository;
import com.banking.svkbanking.repository.UserRepository;
import com.banking.svkbanking.security.SecurityService;
import com.banking.svkbanking.security.UserDetailsServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(SpringExtension.class)
@WebMvcTest
public class MockitoSpringTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@MockBean
	private UserRepository userRepo;
	
	@MockBean
	private AccountRepository accountRepo;
	
	@MockBean
	private UserController userController;
	
	@MockBean
	private AccountController accountController;
	
	@MockBean
	private SecurityService securityService;
	
	@MockBean
	private UserDetailsServiceImpl userDetailsService;
	
	@MockBean
	private UserAddressRepository userAddressRepo;
	
	@MockBean
	private RoleRepository roleRepo;
	
	@MockBean
	private PasswordEncoder encoder;
	
	@Test
	public void whenValidInput_ThenReturns200() throws Exception {
		
		User user = new User();
		user.setEmail("rust@domain.com");
		user.setFullName("Rust Domain");
		user.setPassword("rust123");
		user.setPhoneNumber("phonenumber");
		
		
		mockMvc.perform(post("/api/users")
				.contentType("application/json")
				.content(objectMapper.writeValueAsString(user)))
			.andExpect(status().isOk());
	}
	

}
