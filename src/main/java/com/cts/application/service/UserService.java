package com.cts.application.service;

import java.text.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cts.application.dao.UserRepository;
import com.cts.application.document.User;
import com.cts.application.request.DateRequest;
import com.cts.application.request.UserRequest;
import com.cts.application.util.DateUtils;

@Service
public class UserService {
	@Autowired
	public UserRepository userRepository;
	public UserRequest saveUser(UserRequest requestedUser) throws Exception{
		User user = convertRequestToUser(requestedUser);
		userRepository.save(user);
		requestedUser.setUserName(user.getUserName());
		return requestedUser;
	}
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
		return user;
	}
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
	
}
