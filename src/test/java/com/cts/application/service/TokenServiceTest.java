package com.cts.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import java.net.URI;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized.Parameters;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.GsonJsonParser;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import com.cts.application.dao.UserRepository;
import com.cts.application.to.TokenResp;
import com.fasterxml.jackson.core.JsonParser;



@RunWith(SpringRunner.class)
@ContextConfiguration
public class TokenServiceTest {
	@Configuration
	static class TokenServiceTestContextConfiguration {
		@Bean
		public TokenService tokenService() {
			return new TokenService();
		}
		@Bean
	    public static PropertySourcesPlaceholderConfigurer properties() throws Exception {
	        final PropertySourcesPlaceholderConfigurer pspc = new PropertySourcesPlaceholderConfigurer();
	        Properties properties = new Properties();

	        properties.setProperty("wso2.token.uri", "/abcd/abcd");
	        properties.setProperty("wso2.token.key", "QQWWHH");

	        pspc.setProperties(properties);
	        return pspc;
	    }
		@Bean
		public RestTemplate restTemplate() {
			return Mockito.mock(RestTemplate.class);
		}

	}

	@Autowired
	private TokenService tokenService;
	
	@Autowired
    private RestTemplate restTemplate;

    	
	@Before
	public void setUp() {
		// userService = new UserService();
	}

	 
	@Test
	public void saveUser() throws Exception {
		TokenResp token = new TokenResp();
		token.setAccess_token("ÄBCD1234");
		token.setExpires_in(123);
		token.setScope("app");
		token.setToken_type("bearer");
		HttpHeaders responseHeaders = new HttpHeaders();
		String tokenResS = "{\"access_token\":\"abcdefg\",\"scope\""
				+ ":\"am_application_scope default\",\"token_type\":\"Bearer\",\"expires_in\":3600}";
		
		
		ResponseEntity<String>result = new ResponseEntity<String>(tokenResS, responseHeaders, HttpStatus.OK);
		Mockito.when(restTemplate.exchange(
	            Matchers.any(String.class),
	            Matchers.any(HttpMethod.class),
	            Matchers.<HttpEntity<String>>any(),
	            Matchers.<Class<String>>any())
	        ).thenReturn(result);
		TokenResp tokenRes = tokenService.getToken();
		System.out.println(tokenRes.getAccess_token());
		assertThat(tokenRes.getAccess_token()).isEqualTo("abcdefg");
	}
	
	
	
}
