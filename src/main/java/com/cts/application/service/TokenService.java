package com.cts.application.service;

import java.io.IOException;
import java.util.Properties;

import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.cts.application.to.TokenResp;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class TokenService {
	
	@Value("${wso2.token.uri}")
	private String wso2TokenUri;
	
	@Value("${wso2.token.key}")
	private String wso2TokenKey;
	
	/*@Autowired
	private Environment environment;*/
	@Configuration
	static class TokenServiceTestContextConfiguration {
		@Bean
		public RestTemplate restTemplate() {
			return new RestTemplate();
		}

	}
	@Autowired
	private RestTemplate restTemplate;
	
	
	public TokenResp getToken() {
		//final String uri = "https://gateway.api.cloud.wso2.com:443/token?grant_type=client_credentials";
		System.out.println("wso2TokenUri: " + wso2TokenUri);
		
		// RestTemplate restTemplate = new RestTemplate();

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		headers.set("Authorization",
				"Basic " + wso2TokenKey);
		HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);

		ResponseEntity<String> result = restTemplate.exchange(wso2TokenUri, HttpMethod.POST, entity, String.class);
		TokenResp token = null;
		System.out.println(result);
		try {
			token = new ObjectMapper().readValue(result.getBody(), TokenResp.class);
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			// throw new PolicyException(e.getMessage());
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			// throw new PolicyException(e.getMessage());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			// throw new PolicyException(e.getMessage());
		}
		return token;
	}
}
