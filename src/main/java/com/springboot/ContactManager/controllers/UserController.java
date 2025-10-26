package com.springboot.ContactManager.controllers;

import java.io.IOException;

import java.security.Principal;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.springboot.ContactManager.Dao.ContactRepository;
import com.springboot.ContactManager.Dao.UserRepository;
import com.springboot.ContactManager.entities.Contact;
import com.springboot.ContactManager.entities.User;
import com.springboot.ContactManager.helperclasses.Message;
import com.springboot.ContactManager.helperclasses.SessionHelper;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/user")
public class UserController {


	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder bcryptPasswordEncoder;

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
	public String processContact(@ModelAttribute("contact") Contact contact, Principal principal, ModelMap model,
			HttpSession session)

			throws IOException {

		try {

			String name = principal.getName();
			User user = this.userRepository.getUserByUserName(name);

			contact.setUser(user);
			// System.out.println(contact);

			user.getContact().add(contact);

			this.userRepository.save(user);

			// System.out.println("data " + contact);

			// System.out.println("cid is " + contact.getcId());

			// System.out.println("added to database");

			// success message............
			session.setAttribute("message", new Message("Your contact is added ", "success"));

		} catch (Exception e) {

			// error message..............
			session.setAttribute("message", new Message("Something went wrong", "danger"));

			e.printStackTrace();
			return "error";
		}
		return "user/add_contact";
	}

	// show contact handler
	// handler shows 5 contact/page
	@GetMapping("/show-contacts/{page}")
	public String showContacts(@PathVariable("page") Integer page, ModelMap model, Principal principal) {
		model.addAttribute("title", "Show User Contacts");

		// can also get contacts through user

		// List<Contact> contact = user.getContact();

		String userName = principal.getName();
		User user = this.userRepository.getUserByUserName(userName);

		// current page
		// Contacts per page - 5
		Pageable pageable = PageRequest.of(page, 5);

		Page<Contact> contacts = contactRepository.findContactsByUser(user.getId(), pageable);

		model.addAttribute("contacts", contacts);
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", contacts.getTotalPages());

		return "user/show_contacts";
	}

	// showing perticular contact details

	@GetMapping("/{cId}/contact")
	public String showContactDetails(@PathVariable("cId") Integer cId, ModelMap model, Principal principal) {

		System.out.println(cId);

		Optional<Contact> contactOptional = this.contactRepository.findById(cId);
		Contact contact = contactOptional.get();

		// getting principal user (who is logged in )
		String userName = principal.getName();
		User user = this.userRepository.getUserByUserName(userName);

		if (user.getId() == contact.getUser().getId()) {
			model.addAttribute("contact", contact);
			model.addAttribute("title", contact.getName());
		} else {
			System.out.println("Un_Authorize_User");
		}

		return "user/contact_details";
	}

	// delete contact handler
	@GetMapping("/delete/{cId}")
	public String deleteContact(@PathVariable("cId") Integer cId, ModelMap model, Principal principal,
			HttpSession session) {

		Optional<Contact> contactOptional = this.contactRepository.findById(cId);

		Contact contact = contactOptional.get();

		// getting principal user (who is logged in )
		String userName = principal.getName();
		User user = this.userRepository.getUserByUserName(userName);

		// check
		if (user.getId() == contact.getUser().getId()) {

			// it set's userId null in contact
			contact.setUser(null);

			user.getContact().remove(contact);

			this.userRepository.save(user);

			System.out.println("DELETED");

		} else {
			System.out.println("Un_Authorize_User");
		}

		// session.setAttribute("message", new Message("contact deleted succesfully...",
		// "success"));

		return "redirect:/user/show-contacts/0";
	}

	// open Update contact form handler
	@PostMapping("/update-contact/{cId}")
	public String updateContact(@PathVariable("cId") Integer cId, ModelMap model) {
		model.addAttribute("title", "update-contact");

		Contact contact = this.contactRepository.findById(cId).get();
		model.addAttribute("contact", contact);

		return "user/update_form";
	}

	// process-Update handler
	@PostMapping("/process-update")
	public String updateHandler(@ModelAttribute Contact contact, ModelMap model, Principal principal,
			HttpSession session) {

		try {
			System.out.println("contact name " + contact.getName());
			System.out.println("contact id " + contact.getcId());

			User user = this.userRepository.getUserByUserName(principal.getName());
			contact.setUser(user);

			// Check if a managed instance with the same cId already exists
			Contact existingContact = this.contactRepository.findById(contact.getcId()).orElse(null);

			if (existingContact != null) {
				// Update the existing contact
				existingContact.setName(contact.getName());
				existingContact.setSecondName(contact.getSecondName());
				existingContact.setWork(contact.getWork());
				existingContact.setPhoneNumber(contact.getPhoneNumber());
				existingContact.setEmail(contact.getEmail());
				existingContact.setDescription(contact.getDescription());
				// Update other fields as needed
				this.contactRepository.save(existingContact);
				session.setAttribute("message", new Message("Your contact is updated ", "success"));

			} else {
				// Save the new contact
				this.contactRepository.save(contact);
				session.setAttribute("message", new Message("Something went wrong ", "danger"));

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return "redirect:/user/" + contact.getcId() + "/contact";
	}

	// profile handler
	@GetMapping("/profile")
	public String profile(ModelMap model) {
		model.addAttribute("title", "Profile");
		return "user/profile";
	}

	// open setting handler
	@GetMapping("/settings")
	public String openSetting() {
		return "user/settings";
	}

	// change password handler

	@PostMapping("/change-password")
	public String changePassword(@RequestParam("oldPassword") String oldPassword, Principal principal,
			HttpSession session, @RequestParam("newPassword") String newPassword) {

		//System.out.println("oldpassword  " + oldPassword);
		//System.out.println("newpassword  " + newPassword);

		String userName = principal.getName();

		User currentUser = this.userRepository.getUserByUserName(userName);

		if (this.bcryptPasswordEncoder.matches(oldPassword, currentUser.getPassword())) {

			currentUser.setPassword(this.bcryptPasswordEncoder.encode(newPassword));
			this.userRepository.save(currentUser);
			session.setAttribute("message", new Message("Your Password successfully changed ", "success"));

		} else {
			session.setAttribute("message", new Message("Wrong password ", "danger"));
			
			return "redirect:/user/settings";
		}

		return "redirect:/user/dashboard";
	}

}
