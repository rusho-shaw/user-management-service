package com.cts.application.dao;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.transaction.annotation.Transactional;

import com.cts.application.document.User;

@Transactional
public interface UserRepository extends MongoRepository<User, String> {

	//public User findByPolicyDescription(String policyDescription);

}
