package com.example.aeon.controller;

import java.util.Map;

import javax.validation.Valid;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.aeon.model.oauth.User;
import com.example.aeon.service.UserService;

@RestController
@RequestMapping("/user-register")
public class UserRegisterController {
	
	@Autowired
    private UserService userService;
	
	@PostMapping("/")
	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<Map> register(@Valid @RequestBody User userModel) {
		Map obj = userService.insert(userModel);
		return new ResponseEntity<Map>(obj, HttpStatus.OK);
	}
	
	@PostMapping("/send-otp")
	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<Map> sendOTP(@Valid @RequestBody User userModel) {
		Map obj = userService.sendOTP(userModel);
		return new ResponseEntity<Map>(obj, HttpStatus.OK);
	}
	
	@PostMapping("/register-confirm-otp/{otp}")
	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<Map> registerConfirmOTP(@PathVariable(value = "otp") String otp, @Valid @RequestBody User userModel) {
		Map obj = userService.confirmOTP(otp, userModel);
		return new ResponseEntity<Map>(obj, HttpStatus.OK);
	}

}
