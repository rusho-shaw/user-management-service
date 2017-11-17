package com.cts.application.service;

import java.io.IOException;

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

	public TokenResp getToken() {
		final String uri = "https://gateway.api.cloud.wso2.com:443/token?grant_type=client_credentials";

		RestTemplate restTemplate = new RestTemplate();

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		headers.set("Authorization",
				"Basic Qk8yTEtZYkhTMkNxR1Z3QVRkNHRzTk53eVh3YTpYM2ZhbHd6RGhVZlhGWGowNUVnOTVHajZWdGdh");
		HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);

		ResponseEntity<String> result = restTemplate.exchange(uri, HttpMethod.POST, entity, String.class);
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
