package com.cts.application.controller;

import static org.mockito.Mockito.mock;

import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.bind.annotation.RequestParam;

import com.cts.application.document.User;
import com.cts.application.service.TokenService;
import com.cts.application.service.UserService;
import com.cts.application.to.DateInRequest;
import com.cts.application.to.DateRequest;
import com.cts.application.to.TokenResp;
import com.cts.application.to.UserRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;

@RunWith(SpringRunner.class)
@WebMvcTest(value = UserServiceController.class, secure = false)
public class UserServiceControllerTest {
	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private UserService userService;

	@MockBean
	private TokenService tokenService;

	private static TokenResp token;
	
	
	@BeforeClass
	public static void setToken() {
		TokenResp tokenResp = new TokenResp();
		tokenResp.setAccess_token("7d790b65-4c9b-300d-b3b8-3e8ac8365e1a");
		tokenResp.setScope("am_application_scope default");
		tokenResp.setToken_type("Bearer");
		tokenResp.setExpires_in(3600);
		token = tokenResp;
	}
	@Before
	public void initializeSaveUser() {
		this.requestedUser = getReqUser();		
		this.responseUser = getResUser();
	}

	@Test
	public void saveUser() throws Exception {

		Mockito.when(userService.saveUser(Matchers.any(UserRequest.class))).thenReturn(this.responseUser);
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
		ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
		String requestJson = ow.writeValueAsString(this.requestedUser);

		System.out.println("USERJSON:" + requestJson);
		RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/user/save/").content(requestJson)
				.contentType(MediaType.APPLICATION_JSON_UTF8);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();

		String expected = "{\"message\":\"User created successfully\",\"user\":{\"userName\""
				+ ":\"Testabcd0111\",\"password\":null,\"firstName\":\"Testabcd\","
				+ "\"lastName\":\"Testlast\",\"dateOfBirth\":{\"date\":{\"year\":\"2017\",\"month\":\"11\",\"day\":\"1\"}}"
				+ ",\"address\":\"Chalsa\","
				+ "\"contactNo\":\"2345123456\",\"emailAddress\":\"test@test.com\",\"role\":"
				+ "null,\"userError\":null,\"policies\":null},\"status\":\"1\"}";
		JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);

	}

	@Before
	public void initilaizeSaveUSerExceptionScenarion() {
		this.requestedUser = getReqUser();
	}
	
	@Test
	public void saveUserThrowsException() throws Exception {

		Mockito.when(userService.saveUser(Matchers.any(UserRequest.class))).thenThrow(new Exception());
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
		ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
		String requestJson = ow.writeValueAsString(this.requestedUser);

		System.out.println("USERJSON:" + requestJson);
		RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/user/save/").content(requestJson)
				.contentType(MediaType.APPLICATION_JSON_UTF8);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		
		System.out.println("String resp Exceptoin Screnario:.....  "+ result.getResponse().getContentAsString());

		String expected = "{\"message\":\"Error: User details not saved\",\"user\":null,\"status\":\"0\"}";
		JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);

	}

	

	@Before
	public void initializeAdminLogin() {
		this.responseAdminUser = getAdminResUser();
	}

	@Test
	public void adminLogin() throws Exception {
		Mockito.when(tokenService.getToken()).thenReturn(token);
		Mockito.when(userService.validateUser(Matchers.any(String.class), Matchers.any(String.class)))
				.thenReturn(this.responseAdminUser);
		String userName = "Admin";
		String password = "Admin";

		RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/user/login/").param("userName", userName)
				.param("password", password);
		System.out.println("During admin user login....");

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();

		String expected = "{\"message\":\"User validated successfully\",\"user\":{\"userName\":\"Admin\","
				+ "\"password\":null,\"firstName\":null,\"lastName\":null,\"dateOfBirth\":null,\"address\":null,"
				+ "\"contactNo\":null,\"emailAddress\":null,\"role\":\"admin\",\"userError\":\"N\",\"policies\""
				+ ":null},\"status\":\"1\",\"token\":{\"access_token\":\"7d790b65-4c9b-300d-b3b8-3e8ac8365e1a\",\"scope\""
				+ ":\"am_application_scope default\",\"token_type\":\"Bearer\",\"expires_in\":3600}}";
		JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);
	}

	@Test
	public void adminLoginFailed() throws Exception {
		UserRequest user = new UserRequest();
		System.out.println("initializeAdminLoginFail...");
		user.setUserError("Contact Admin Service");
		Mockito.when(tokenService.getToken()).thenReturn(token);
		Mockito.when(userService.validateUser(Matchers.any(String.class), Matchers.any(String.class)))
				.thenReturn(user);
		String userName = "Admin";
		String password = "abcd";

		RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/user/login/").param("userName", userName)
				.param("password", password);
		System.out.println("During adminLoginFailed");

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();

		String expected = "{\"message\":\"Contact Admin Service\",\"user\":{\"userName\":null,\"password\":null,\"firstName\":"
				+ "null,\"lastName\":null,\"dateOfBirth\":null,\"address\":null,\"contactNo\":null,\"emailAddress\""
				+ ":null,\"role\":null,\"userError\":\"Contact Admin Service\",\"policies\""
				+ ":null},\"status\":\"0\",\"token\":{\"access_token\":\"7d790b65-4c9b-300d-b3b8-3e8ac8365e1a\",\"scope\""
				+ ":\"am_application_scope default\",\"token_type\":\"Bearer\",\"expires_in\":3600}}";
		JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);
	}
	
	private UserRequest requestedUser;
	private UserRequest responseUser;
	private UserRequest responseAdminUser;

	private UserRequest getReqUser() {
		UserRequest user = new UserRequest();
		user.setFirstName("Testabcd");
		user.setLastName("Testlast");
		DateRequest dateReq = new DateRequest();
		DateInRequest date = new DateInRequest();
		date.setDay("1");
		date.setMonth("11");
		date.setYear("2017");
		dateReq.setDate(date);
		user.setDateOfBirth(dateReq);
		user.setAddress("Chalsa");
		user.setContactNo("2345123456");
		user.setEmailAddress("test@test.com");
		return user;
	}
	private UserRequest getResUser() {
		UserRequest user1 = new UserRequest();
		user1.setUserName("Testabcd0111");
		user1.setFirstName("Testabcd");
		user1.setLastName("Testlast");
		DateRequest dateReq1 = new DateRequest();
		DateInRequest date1 = new DateInRequest();
		date1.setDay("1");
		date1.setMonth("11");
		date1.setYear("2017");
		dateReq1.setDate(date1);
		user1.setDateOfBirth(dateReq1);
		user1.setAddress("Chalsa");
		user1.setContactNo("2345123456");
		user1.setEmailAddress("test@test.com");
		return user1;
	}
	
	private UserRequest getAdminResUser() {
		UserRequest user = new UserRequest();
		user.setUserName("Admin");
		user.setRole("admin");
		user.setUserError("N");
		return user;
	}

}
