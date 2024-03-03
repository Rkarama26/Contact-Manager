package com.springboot.ContactManager.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.springboot.ContactManager.Dao.UserRepository;
import com.springboot.ContactManager.entities.User;
import com.springboot.ContactManager.helperclasses.Message;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
public class HomeController {
	

	public HomeController() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	



	public User getUserByEmail(String email) {
        return userRepository.getUserByUserName(email);
	}
	@GetMapping("/home")
	public String test(ModelMap model) {

		model.addAttribute("title", "Home - smart contact manager");

		return "home";
	}

	@GetMapping("/about")
	public String about(ModelMap model) {

		model.addAttribute("about", "about - this is about page ");

		return "about";
	}

	@GetMapping("/signup")
	public String signup(ModelMap model) {

		model.addAttribute("title", "signup - this is signup page ");
		model.addAttribute("user", new User());
		return "signup";
	}

	// handler for registering user
	@PostMapping("/do_register")

	// using ModelAttribute to writing the user data , and getting checkbox data ,
	public String register(@Valid @ModelAttribute("user") User user, BindingResult bresult,
			@RequestParam(value = "agreement", defaultValue = "false") boolean agreement, ModelMap model,
			HttpSession session) {
 
		try {
			if (!agreement) {
				System.out.println("You have not agreed the terms and conditions");
				throw new Exception("You have not agreed the terms and conditions");
			}
			
			if(bresult.hasErrors()) {
				System.out.print(bresult);
				return "signup";
			}
			
			user.setRole("ROLE_USER");
			user.setEnabled(true);
			user.setUserImage("DEFAULT.png");
			user.setPassword(passwordEncoder.encode(user.getPassword()));
			

			System.out.println("agreement " + agreement);
			System.out.println("USER  " + user);
			System.out.println();

			User result = this.userRepository.save(user);

			model.addAttribute("user", new User());
			session.setAttribute("message", new Message("Successfully Registered", "alert-success"));


			return "signup";

		} catch (Exception e) {

			String errorMessage = "something went wrong!! " + e.getMessage();
			e.printStackTrace();
			model.addAttribute("user", user);
			session.setAttribute("message", new Message(errorMessage, "alert-danger"));
			return "signup";

		}
	}
	
	@GetMapping("/login")
	public String customLoginPage(ModelMap model) {
		model.addAttribute("title", "Login-page");
		return "loginpage";
	}

}
