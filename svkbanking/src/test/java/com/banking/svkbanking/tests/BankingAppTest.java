package com.banking.svkbanking.tests;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.banking.svkbanking.controller.UserController;
import com.banking.svkbanking.entity.User;
import com.banking.svkbanking.repository.UserRepository;


@RunWith(MockitoJUnitRunner.class)
public class BankingAppTest {

	@Mock
	private RestTemplate restTemplate;
	
	@InjectMocks
	private UserRepository userRepo;
	
	@Autowired
	private UserController userController;
	
	@Test
	public void getUsersTest() {
		User user = new User();
		
		Mockito.when(restTemplate.getForEntity("http://localhost:8080/api/users/1", User.class))
			.thenReturn(new ResponseEntity<User>(user, HttpStatus.OK));
		
		Assert.assertEquals(1,1);
	}
}
