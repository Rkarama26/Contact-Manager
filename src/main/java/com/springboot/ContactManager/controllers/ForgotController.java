package com.springboot.ContactManager.controllers;

import java.util.Random;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ForgotController {

	@GetMapping("/forgot")
	public String openEmailForm() {
		return "forgot_form";
	}
	
	@PostMapping("/send-otp")
	public String sendOtp(@RequestParam("email") String email) {
		
		//generating otp of 4 digit
		
		Random random = new Random(1000);
		
		int OTP = random.nextInt(9999);
		
		System.out.println( "OTP" + OTP );
		
		return "verify_otp";
	}
	
	
}
