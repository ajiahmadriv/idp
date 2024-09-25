package com.example.aeon.service;

import java.util.Map;

import com.example.aeon.model.oauth.User;

public interface UserService {

	public Map insert(User obj);
	
	public Map sendOTP(User obj);
	
	public Map confirmOTP(String otp, User obj);
	
	public Map sendEmail(User obj);
	
	public Map validateOTP(User obj);
	
	public Map changePassword(User obj);
}
