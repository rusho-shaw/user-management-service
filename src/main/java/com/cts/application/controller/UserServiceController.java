package com.cts.application.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cts.application.dao.UserRepository;
import com.cts.application.document.User;
import com.cts.application.request.UserRequest;
import com.cts.application.service.UserService;

@RestController
@RequestMapping("/user")
public class UserServiceController {

	private static final String template = "Hello, %s!";
	private final AtomicLong counter = new AtomicLong();

	/*@Autowired
	public UserRepository userRepository;*/
	
	@Autowired
	private UserService userService;

	@CrossOrigin
	@PutMapping("/save")
	public Map<String, Object> saveUser(@RequestBody UserRequest user){
		
		System.out.println("SAving user: "+user.getFirstName());
		Map<String, Object> dataMap = new HashMap<String, Object>();
		try {
			user = userService.saveUser(user);
			dataMap.put("message", "User created successfully");
			dataMap.put("status", "1");
			user.setPassword(null);
		} catch (Exception e) {
			dataMap.put("message", "Error: User details not saved");
			dataMap.put("status", "0");
			user = null;
		}
		
		dataMap.put("user", user);
		return dataMap;
	}

	/*@RequestMapping("/read")
	public Map<String, Object> read(@RequestParam String policyId) {
		User user = userRepository.findOne(policyId);
		Map<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put("message", "User found successfully");
		dataMap.put("status", "1");
		dataMap.put("policy > ", user);
		return dataMap;
	}

	@RequestMapping("/update")
	public Map<String, Object> update(@RequestParam String policyId, @RequestParam String policyDetails) {
		User user = userRepository.findOne(policyId);
		//user.setPolicyDetails(policyDetails);
		user = userRepository.save(user);
		Map<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put("message", "User updated successfully");
		dataMap.put("status", "1");
		dataMap.put("User", user);
		return dataMap;
	}

	@RequestMapping("/delete")
	public Map<String, Object> delete(@RequestParam String policyId) {
		userRepository.delete(policyId);
		Map<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put("message", "User deleted successfully");
		dataMap.put("status", "1");
		return dataMap;
	}

	@RequestMapping("/read-all")
	public Map<String, Object> readAll() {
		List<User> users = userRepository.findAll();
		Map<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put("message", "User found successfully");
		dataMap.put("totalPolicy", users.size());
		dataMap.put("status", "1");
		dataMap.put("policies", users);
		return dataMap;
	}*/
}
