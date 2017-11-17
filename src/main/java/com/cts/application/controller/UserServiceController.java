package com.cts.application.controller;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.cts.application.service.TokenService;
import com.cts.application.service.UserService;
import com.cts.application.to.Policy;
import com.cts.application.to.TokenResp;
import com.cts.application.to.UserRequest;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/user")
public class UserServiceController {

	@Autowired
	private UserService userService;

	@Autowired
	private TokenService tokenService;
	
	@CrossOrigin
	@PutMapping("/save")
	public Map<String, Object> saveUser(@RequestBody UserRequest user) {

		System.out.println("SAving user: " + user.getFirstName());
		Map<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put("message", "Error: User details not saved");
		dataMap.put("status", "0");
		try {
			user = userService.saveUser(user);
			if (user != null) {
				dataMap.put("message", "User created successfully");
				dataMap.put("status", "1");
				user.setPassword(null);
			}
		} catch (Exception e) {
			user = null;
		}

		dataMap.put("user", user);
		return dataMap;
	}

	@CrossOrigin
	@RequestMapping("/login")
	public Map<String, Object> login(@RequestParam String userName, @RequestParam String password) {
		TokenResp token = tokenService.getToken();
		System.out.println("Token is: " + token);
		UserRequest user = userService.validateUser(userName, password);
		Map<String, Object> dataMap = new HashMap<String, Object>();
		String message = null;
		String status = "0";
		if (user != null && user.getUserError().equalsIgnoreCase("N")) {
			message = "User validated successfully";
			status = "1";
		} else {
			message = user.getUserError();
		}
		dataMap.put("message", message);
		dataMap.put("status", status);
		dataMap.put("user", user);
		dataMap.put("token", token);
		return dataMap;
	}

	

	/**
	 * Test operation to get all the users in DB inPCF
	 * 
	 * @return
	 */
	@CrossOrigin
	@RequestMapping("/getAllUsers")
	public Map<String, Object> getAllUsers() {
		Map<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put("users", userService.getAllUsers());
		return dataMap;
	}

	/**
	 * Test operation to add user policy in DB in PCF
	 * 
	 * @return
	 */
	@CrossOrigin
	@RequestMapping("/addUserPolicy")
	public Map<String, Object> addUserPolicy(@RequestParam String userName, @RequestParam String policyId,
			@RequestParam String amountPaid, @RequestParam String policyEndDate) {
		Map<String, Object> dataMap = new HashMap<String, Object>();
		Policy policy = new Policy();
		policy.setPolicyId(policyId);
		// policy.setPolicyName(policyName);
		policy.setAmountPaid(Float.parseFloat(amountPaid));
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		try {
			policy.setPolicyEndDate(df.parse(policyEndDate));
		} catch (ParseException e) {
			System.out.println("In DateUtils: " + e.getMessage());
			// throw e;
		}
		dataMap.put("userPolicyAdded", userService.addPolicyForUser(userName, policy));
		return dataMap;
	}

	/**
	 * Operation to create Admin for DB inPCF
	 * 
	 * @return
	 */
	@CrossOrigin
	@RequestMapping("/createAdmin")
	public Map<String, Object> createAdmin() {
		Map<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put("users", userService.createAdmin());
		return dataMap;
	}
}
