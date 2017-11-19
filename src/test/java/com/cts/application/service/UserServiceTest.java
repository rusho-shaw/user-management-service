package com.cts.application.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import com.cts.application.dao.UserRepository;
import com.cts.application.document.User;
import com.cts.application.to.DateInRequest;
import com.cts.application.to.DateRequest;
import com.cts.application.to.Policy;
import com.cts.application.to.UserRequest;

@RunWith(SpringRunner.class)
@ContextConfiguration
public class UserServiceTest {
	@Configuration
	static class USerServiceTestContextConfiguration {
		@Bean
		public UserService userService() {
			return new UserService();
		}

		@Bean
		public UserRepository userRepository() {
			return Mockito.mock(UserRepository.class);
		}
	}

	@Autowired
	private UserService userService;
	@Autowired
	private UserRepository userRepository;

	@Before
	public void setUp() {
		// userService = new UserService();
	}

	@Test
	public void saveUser() throws Exception {
		UserRequest userRequest = new UserRequest();
		userRequest.setFirstName("Testabcd");
		userRequest.setLastName("Testlast");
		DateRequest dateReq = new DateRequest();
		DateInRequest date = new DateInRequest();
		date.setDay("1");
		date.setMonth("1");
		date.setYear("2017");
		dateReq.setDate(date);
		userRequest.setDateOfBirth(dateReq);
		User userResponse = new User();
		userResponse.setUserName("Testabcd0101");

		Mockito.when(userRepository.insert(Matchers.any(User.class))).thenReturn(userResponse);
		userRequest = userService.saveUser(userRequest);
		System.out.println(userRequest.getUserName());
		assertThat(userRequest.getUserName()).isEqualTo("Testabcd0101");
	}
	
	@Test(expected = ParseException.class)
	public void saveUserExceptionScenario() throws Exception {
		UserRequest userRequest = new UserRequest();
		userRequest.setFirstName("Testabcd");
		userRequest.setLastName("Testlast");
		DateRequest dateReq = new DateRequest();
		DateInRequest date = new DateInRequest();
		date.setDay("1");
		date.setMonth("aa");
		date.setYear("2017");
		dateReq.setDate(date);
		userRequest.setDateOfBirth(dateReq);
		User userResponse = new User();
		userResponse.setUserName("Testabcd0101");

		Mockito.when(userRepository.insert(Matchers.any(User.class))).thenReturn(userResponse);
		userService.saveUser(userRequest);
	}
	
	@Test
	public void addPolicyForUser() {
		//User user = convertRequestToUser(requestedUser);
		// String userName, Policy policy
		User userResponse = new User();
		userResponse.setUserName("Testabcd0101");
		Mockito.when(userRepository.findOne(Matchers.any(String.class))).thenReturn(userResponse);
		
		Mockito.when(userRepository.save(Matchers.any(User.class))).thenReturn(userResponse);
		userService.addPolicyForUser("Testabcd0101", new Policy());
		assertThat(userService.addPolicyForUser("Testabcd0101", new Policy())).isEqualTo(true);
	}
	@Test
	public void getAllUsers() {
		//User user = convertRequestToUser(requestedUser);
		// String userName, Policy policy
		User userResponse = new User();
		userResponse.setUserName("Testabcd0101");
		List<User> userList = new ArrayList<User>();
		userList.add(userResponse);
		
		Mockito.when(userRepository.findAll()).thenReturn(userList);
		assertThat(userService.getAllUsers().size()).isEqualTo(1);
	}
	
	@Test
	public void createAdmin() {
		User user = new User();
		user.setUserName("Admin");
		user.setPassword("Admin");
		user.setRole("admin");
		Mockito.when(userRepository.save(Matchers.any(User.class))).thenReturn(user);
		UserRequest userRequest = userService.createAdmin();
		assertThat(userRequest.getUserName()).isEqualTo("Admin");
		
	}
	
	@Test
	public void validateUser() {
		User userResponse = new User();
		userResponse.setUserName("Admin");
		userResponse.setPassword("Admin");
		userResponse.setRole("admin");
		Mockito.when(userRepository.findOne(Matchers.any(String.class))).thenReturn(userResponse);
		UserRequest userRequest = userService.validateUser("Admin", "Admin");
		assertThat(userRequest.getUserError()).isEqualTo("N");
	}
	@Test
	public void validateAdminFail() {
		User userResponse = new User();
		userResponse.setUserName("Admin");
		userResponse.setPassword("Admin");
		userResponse.setRole("admin");
		Mockito.when(userRepository.findOne(Matchers.any(String.class))).thenReturn(userResponse);
		UserRequest userRequest = userService.validateUser("Admin", "ABCDE");
		assertThat(userRequest.getUserError()).isEqualTo("Contact Admin Service");
	}
	@Test
	public void validateLoginUserFail() {
		User userResponse = new User();
		userResponse.setUserName("ABCUSer");
		userResponse.setPassword("xxyy");
		userResponse.setRole("user");
		Mockito.when(userRepository.findOne(Matchers.any(String.class))).thenReturn(userResponse);
		UserRequest userRequest = userService.validateUser("ABCUser", "ABCDE");
		assertThat(userRequest.getUserError()).isEqualTo("You are a not registered User. Register to login");
	}
	@Test
	public void validateLoginGenericUserFail() {
		Mockito.when(userRepository.findOne(Matchers.any(String.class))).thenReturn(null);
		UserRequest userRequest = userService.validateUser("ABCUser", "ABCDE");
		assertThat(userRequest.getUserError()).isEqualTo("You are a not registered User. Register to login");
	}
	
}
