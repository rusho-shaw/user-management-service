package com.cts.application.service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cts.application.dao.UserRepository;
import com.cts.application.document.User;
import com.cts.application.to.DateRequest;
import com.cts.application.to.Policy;
import com.cts.application.to.UserRequest;
import com.cts.application.util.DateUtils;

@Service
public class UserService {
	@Autowired
	public UserRepository userRepository;
	
	/**
	 * 
	 * @param requestedUser
	 * @return
	 * @throws Exception
	 */
	public UserRequest saveUser(UserRequest requestedUser) throws Exception{
		User user = convertRequestToUser(requestedUser);
		user = userRepository.insert(user);
		requestedUser.setUserName(user.getUserName());
		return requestedUser;
	}
	/**
	 * 
	 * @param requestedUser
	 * @return
	 * @throws Exception
	 */
	public boolean addPolicyForUser(String userName, Policy policy) {
		//User user = convertRequestToUser(requestedUser);
		boolean policyAdded = false;
		User user = userRepository.findOne(userName);
		List<Policy> policies = user.getPolicies();
		if(policies==null) {
			policies = new ArrayList<Policy> ();
		}
		policies.add(policy);
		user.setPolicies(policies);
		userRepository.save(user);
		policyAdded = true;
		return policyAdded;
	}
	
	/**
	 * 
	 * @param requestedUser
	 * @return
	 * @throws Exception
	 */
	
	public UserRequest createAdmin() {
		User user = new User();
		user.setUserName("Admin");
		user.setPassword("Admin");
		user.setRole("admin");
		userRepository.save(user);
		UserRequest requestedUser = convertUserToRequest(user);
		
		return requestedUser;
	}
	
	/**
	 * 
	 * @param requestedUser
	 * @return
	 * @throws ParseException
	 */
	private User convertRequestToUser(UserRequest requestedUser) throws ParseException {
		User user = new User();
		user.setPassword(requestedUser.getPassword());
		String firstName = requestedUser.getFirstName();
		user.setFirstName(firstName);
		user.setLastName(requestedUser.getLastName());
		DateRequest dateRequest = requestedUser.getDateOfBirth();
		try {
			user.setDateOfBirth(DateUtils.convertDateRequestToDate(dateRequest));
		} catch (ParseException e) {
			System.out.println("In UserService:" + e.getMessage());
			throw e;
		}
		user.setUserName(generateUserName(firstName, dateRequest));
		user.setAddress(requestedUser.getAddress());
		user.setContactNo(requestedUser.getContactNo());
		user.setEmailAddress(requestedUser.getEmailAddress());
		user.setRole("user");
		return user;
	}
	
	/**
	 * 
	 * @param firstName
	 * @param dateRequest
	 * @return
	 */
	private String generateUserName(String firstName, DateRequest dateRequest) {
		String userName;
		String day = dateRequest.getDate().getDay();
		if(day!=null && day.length()==1) {
			day = "0" + day;
		}
		String month = dateRequest.getDate().getMonth();
		if(month!=null && month.length()==1) {
			month = "0" + month;
		}
		
		userName = firstName + day + month;
		return userName;
	}
	
	/**
	 * 
	 * @param userName
	 * @return
	 */
	public UserRequest validateUser(String userName, String password) {
		UserRequest userRequest = new UserRequest();
		User user = userRepository.findOne(userName);
		if(user!=null && password!=null) {
			String userPwd = user.getPassword();
			if(userPwd!=null
					&& password.length()==userPwd.length() 
					&& password.indexOf(userPwd)==0) {
				// System.out.println("Matched");
				userRequest = convertUserToRequest(user);
				userRequest.setUserError("N");
			} else if (user.getRole()!=null && user.getRole().equalsIgnoreCase("user")) {
				userRequest.setUserError("You are a not registered User. Register to login");
			} else if (user.getRole()!=null && user.getRole().equalsIgnoreCase("admin")) {
				userRequest.setUserError("Contact Admin Service");
			} 
			
		}else {
			userRequest.setUserError("You are a not registered User. Register to login");
		}
		
		return userRequest;
	}
	
	/**
	 * 
	 * @param user
	 * @return
	 */
	private UserRequest convertUserToRequest(User user) {
		UserRequest userReq = new UserRequest();
		
		userReq.setFirstName(user.getFirstName());
		userReq.setLastName(user.getLastName());
		userReq.setUserName(user.getUserName());
		userReq.setRole(user.getRole());
		userReq.setPolicies(user.getPolicies());
		return userReq;
		
	}

	/**
	 * To get all users for DB in PCF
	 * @return
	 */
	public List<User> getAllUsers() {
		// TODO Auto-generated method stub
		List<User> users = userRepository.findAll();
		return users;
	}

	
	
}
