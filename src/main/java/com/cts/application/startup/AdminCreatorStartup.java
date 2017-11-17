package com.cts.application.startup;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cts.application.service.UserService;

@Component
public class AdminCreatorStartup {

	@Autowired
	private UserService userService;
	
	/**
	 * create Admin user in DB
	 * @return
	 */
	@PostConstruct
	public Map<String, Object> createAdmin() {
		Map<String, Object> dataMap = new HashMap<String, Object>();
		System.out.println("In create Admin in Startup ######");
		dataMap.put("users", userService.createAdmin());
		return dataMap;
	}
}
