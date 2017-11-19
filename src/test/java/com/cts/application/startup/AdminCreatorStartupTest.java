package com.cts.application.startup;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;


import com.cts.application.service.UserService;
import com.cts.application.to.UserRequest;

@RunWith(SpringRunner.class)
@ContextConfiguration
public class AdminCreatorStartupTest {

	@Configuration
	static class USerServiceTestContextConfiguration {
		@Bean
		public AdminCreatorStartup adminCreatorStartup() {
			return new AdminCreatorStartup();
		}
	}

	@MockBean
	private UserService userService;
	@Autowired
	private AdminCreatorStartup adminCreatorStartup;
	@Test
	public void createAdmin() {
		UserRequest user = new UserRequest();
		user.setUserName("Admin");
		user.setPassword("Admin");
		user.setRole("admin");
		Mockito.when(userService.createAdmin()).thenReturn(user);
		Map<String, Object> dataMap = adminCreatorStartup.createAdmin();
		assertThat(dataMap.get("users")).isEqualTo(user);
		
	}
}
