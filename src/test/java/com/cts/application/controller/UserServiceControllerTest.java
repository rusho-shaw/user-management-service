package com.cts.application.controller;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
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
import com.cts.application.to.UserRequest;

@RunWith(SpringRunner.class)
@WebMvcTest(value = UserServiceController.class, secure = false)
public class UserServiceControllerTest {
	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private UserService userService;
	
	//UserRequest mockUserRequest = new UserRequest("Course1", "Spring", "10 Steps");

	String exampleCourseJson = "{\"name\":\"Spring\",\"description\""
			+ ":\"10 Steps\",\"steps\":[\"Learn Maven\","
			+ "\"Import Project\",\"First Example\",\"Second Example\"]}";
	
	
	
	@Test
	public void createAdmin() throws Exception {
		Map<String, Object> mockAdminResp = new HashMap<String, Object>();
		UserRequest user = new UserRequest();
		user.setUserName("Admin");
		user.setPassword("Admin");
		user.setRole("admin");
		mockAdminResp.put("users", user);
		Mockito.when(
				userService.createAdmin()).thenReturn(user);

		RequestBuilder requestBuilder = MockMvcRequestBuilders.get(
				"/user/createAdmin/").accept(
				MediaType.APPLICATION_JSON);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();

		System.out.println("Result is######"+ result.getResponse().getContentAsString());
		String expected = "{\"users\":{\"userName\":\"Admin\",\"password\":\"Admin\",\"firstName\":null,\"lastName\":null,\"dateOfBirth\":null,\"address\":null,\"contactNo\":null,\"emailAddress\":null,\"role\":\"admin\",\"userError\":null,\"policies\":null}}";
		JSONAssert.assertEquals(expected, result.getResponse()
				.getContentAsString(), false);
	}

}
