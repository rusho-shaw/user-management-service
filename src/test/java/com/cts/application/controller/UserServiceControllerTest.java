package com.cts.application.controller;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
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

import com.cts.application.document.User;
import com.cts.application.service.UserService;
import com.cts.application.to.DateInRequest;
import com.cts.application.to.DateRequest;
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

	private UserRequest requestedUser;
	private UserRequest responseUser;
	private String expected;
	
	@Before
	public void initializeSaveUser() {
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
		this.requestedUser = user;
		
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
		this.responseUser = user1;
		this.expected = "{\"message\":\"User created successfully\",\"user\":{\"userName\":\"Testabcd0111\",\"password\":null,\"firstName\":\"Testabcd\","
				+ "\"lastName\":\"Testlast\",\"dateOfBirth\":{\"date\":{\"year\":\"2017\",\"month\":\"11\",\"day\":\"1\"}},\"address\":\"Chalsa\","
				+ "\"contactNo\":\"2345123456\",\"emailAddress\":\"test@test.com\",\"role\":null,\"userError\":null,\"policies\":null},\"status\":\"1\"}";
	}
	@Test
	public void saveUser() throws Exception {
		
		Mockito.when(userService.saveUser(Matchers.any(UserRequest.class))).thenReturn(this.responseUser);

		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
		ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
		String requestJson = ow.writeValueAsString(this.requestedUser);

		System.out.println("USERJSON:"+ requestJson);
		RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/user/save/").content(requestJson)
				.contentType(MediaType.APPLICATION_JSON_UTF8);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();

		System.out.println("Result is######" + result.getResponse().getContentAsString());
		
		JSONAssert.assertEquals(this.expected, result.getResponse().getContentAsString(), false);

	}

}
