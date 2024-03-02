package com.springboot.ContactManager.controllers;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import com.springboot.ContactManager.Dao.ContactRepository;
import com.springboot.ContactManager.Dao.UserRepository;
import com.springboot.ContactManager.entities.Contact;
import com.springboot.ContactManager.entities.User;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ContactRepository contactRepository;

	// method to adding comman data to response
	@ModelAttribute
	public void addCommanData(ModelMap model, Principal principal) {

		// get the user from using username(email)
		String userName = principal.getName();
		System.out.println("USERNAME " + userName);

		User user = userRepository.getUserByUserName(userName);

		System.out.println("user " + user);
		model.addAttribute("user", user);

	}

	// Home dashboard - handler
	@GetMapping("/dashboard")
	public String dashboard(ModelMap model, Principal principal) {

		model.addAttribute("title", "User Dashboard");
		return "user/dashboard";
	}

	// add contact form - handler
	@GetMapping("/add-contact")
	public String addContact(ModelMap model) {
		model.addAttribute("title", "Add Contact");
		model.addAttribute("contact", new Contact());

		return "user/add_contact";
	}

	// processing add contact form handler
	@PostMapping("/process-contact")
	public String processContact(@ModelAttribute("contact") Contact contact, BindingResult bindingResult,
			Principal principal, ModelMap model, @RequestParam("imageUrl") MultipartFile file)

			throws IOException

	{
		try {

			if (bindingResult.hasErrors()) {
				System.out.println(bindingResult.toString());
				System.out.println("the errors are " + bindingResult.getAllErrors());

				model.addAttribute("contact", contact);
				return "user/add_contact";
			}

			String name = principal.getName();
			User user = this.userRepository.getUserByUserName(name);

			// processing and uploading file
			if (file.isEmpty()) {

				System.out.print("File is empty");
			}

			else {
				// upload the file to the folder and update the name to contact
				contact.setImageUrl(file.getOriginalFilename());

				File savefile = new ClassPathResource("static/image").getFile();

				Path path = Paths.get(savefile.getAbsolutePath() + File.separator + file.getOriginalFilename());
				Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

			}

			contact.setUser(user);
			// System.out.println(contact);

			user.getContact().add(contact);

			this.userRepository.save(user);

			// System.out.println("data " + contact);

			// System.out.println("cid is " + contact.getcId());

			// System.out.println("added to database");
		} catch (Exception e) {
			e.printStackTrace();
			return "error";
		}

		return "user/add_contact";
	}

}
