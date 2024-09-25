package com.example.aeon.service.impl;

import java.util.Map;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.aeon.model.oauth.User;
import com.example.aeon.repository.oauth.UserRepository;
import com.example.aeon.service.EmailService;
import com.example.aeon.service.UserService;
import com.example.aeon.utils.TemplateResponse;

@Service
public class UserImpl implements UserService {

	@Autowired
	public UserRepository userRepository;
	@Autowired
	public EmailService emailService;

	public static final Logger log = LoggerFactory.getLogger(UserImpl.class);

	@Autowired
	public TemplateResponse templateResponse;

	@Override
	public Map insert(User obj) {
		try {
			if (templateResponse.checkNull(obj.getUsername())) {
				return templateResponse.templateError("Username Tidak boleh null");
			}
			if (templateResponse.checkNull(obj.getPassword())) {
				return templateResponse.templateError("Password Tidak boleh null");
			}
			obj.setPassword(new BCryptPasswordEncoder(13).encode(obj.getPassword()));
			obj.setAccountNonLocked(false);
			obj.setOtp(generateOTP());
			User saveObj = userRepository.save(obj);
			saveObj.setPassword(null);
			return templateResponse.templateSukses(saveObj);
		} catch (Exception e) {
			return templateResponse.templateError(e);
		}
	}

	private String generateOTP() {
		Random rnd = new Random();
		int number = rnd.nextInt(999999);
		return String.format("%06d", number);
	}

	@Override
	public Map sendOTP(User obj) {
		try {
			if (templateResponse.checkNull(obj.getUsername())) {
				return templateResponse.templateError("Username Tidak boleh null");
			}

			User user = userRepository.findOneByUsername(obj.getUsername());
			emailService.sendEmail(user.getUsername(), "Verification OTP", "Halo " + user.getFullname()
					+ " Selamat bergabung\n\nHarap konfirmasikan email kamu dengan memasukkan kode dibawah ini\n\nkode : "
					+ user.getOtp());
			return templateResponse.templateSukses("Thanks, please check your email for activation.");
		} catch (Exception e) {
			return templateResponse.templateError(e);
		}
	}

	@Override
	public Map confirmOTP(String otp, User obj) {
		try {
			if (templateResponse.checkNull(obj.getUsername())) {
				return templateResponse.templateError("Username Tidak boleh null");
			}
			if (templateResponse.checkNull(otp)) {
				return templateResponse.templateError("OTP Tidak boleh null");
			}

			User user = userRepository.findByUsernameAndOTP(obj.getUsername(), otp);
			user.setAccountNonLocked(true);
			user.setOtp(null);
			userRepository.save(user);
			return templateResponse.templateSukses("Akun anda sudah aktif, silakan melakukan login");
		} catch (Exception e) {
			return templateResponse.templateError(e);
		}
	}

	@Override
	public Map sendEmail(User obj) {
		try {
			if (templateResponse.checkNull(obj.getUsername())) {
				return templateResponse.templateError("Username Tidak boleh null");
			}

			User user = userRepository.findOneByUsername(obj.getUsername());
			user.setOtp(generateOTP());
			user.setAccountNonLocked(false);
			userRepository.save(user);
			emailService.sendEmail(user.getUsername(), "Verification OTP", "If you requested a password for "
					+ user.getUsername()
					+ " use the confirmation code below to complete the process. If you didn't make this request, ignore this email\n\nCode : "
					+ obj.getOtp());
			return templateResponse.templateSukses("Success");
		} catch (Exception e) {
			return templateResponse.templateError(e);
		}
	}

	@Override
	public Map validateOTP(User obj) {
		try {
			if (templateResponse.checkNull(obj.getUsername())) {
				return templateResponse.templateError("Username Tidak boleh null");
			}
			if (templateResponse.checkNull(obj.getOtp())) {
				return templateResponse.templateError("OTP Tidak boleh null");
			}

			User user = userRepository.findByUsernameAndOTP(obj.getUsername(), obj.getOtp());
			if (user != null) {
				return templateResponse.templateSukses("Success");
			}
			return templateResponse.templateSukses("Failed");
		} catch (Exception e) {
			return templateResponse.templateError(e);
		}
	}

	@Override
	public Map changePassword(User obj) {
		try {
			if (templateResponse.checkNull(obj.getUsername())) {
				return templateResponse.templateError("Username Tidak boleh null");
			}
			if (templateResponse.checkNull(obj.getOtp())) {
				return templateResponse.templateError("OTP Tidak boleh null");
			}
			if (templateResponse.checkNull(obj.getNewPassword())) {
				return templateResponse.templateError("New Password Tidak boleh null");
			}
			if (templateResponse.checkNull(obj.getConfirmNewPassword())) {
				return templateResponse.templateError("Confirm New Password Tidak boleh null");
			}

			User user = userRepository.findByUsernameAndOTP(obj.getUsername(), obj.getOtp());
			user.setPassword(new BCryptPasswordEncoder(13).encode(obj.getConfirmNewPassword()));
			userRepository.save(user);
			return templateResponse.templateSukses("Success");
		} catch (Exception e) {
			return templateResponse.templateError(e);
		}
	}

}
