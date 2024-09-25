package com.example.aeon.controller;

import java.util.Map;

import javax.validation.Valid;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.aeon.model.oauth.User;
import com.example.aeon.service.UserService;

@RestController
@RequestMapping("/forget-password")
public class ForgetPasswordController {
	
	@Autowired
    private UserService userService;
	
	@PostMapping("/send")
	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<Map> send(@Valid @RequestBody User userModel) {
		Map obj = userService.sendEmail(userModel);
		return new ResponseEntity<Map>(obj, HttpStatus.OK);
	}
	
	@PostMapping("/validate")
	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<Map> validate(@Valid @RequestBody User userModel) {
		Map obj = userService.validateOTP(userModel);
		return new ResponseEntity<Map>(obj, HttpStatus.OK);
	}
	
	@PostMapping("/change-password")
	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<Map> changePassword(@Valid @RequestBody User userModel) {
		Map obj = userService.changePassword(userModel);
		return new ResponseEntity<Map>(obj, HttpStatus.OK);
	}

}
