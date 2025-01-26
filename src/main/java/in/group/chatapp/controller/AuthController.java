package in.group.chatapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import in.group.chatapp.Entity.UserDto;
import in.group.chatapp.service.UserService;


@Controller
public class AuthController {

	@Autowired
	public UserService userService;

	@PostMapping("/register")
	public String registerUser(@ModelAttribute UserDto userDto, Model model) {
		userDto.setRole("ROLE_USER");

		// Check if the user already exists
		if (userService.findByUsername(userDto.getUsername())) {
			model.addAttribute("error", "User already exists! Please try a different username.");
			return "register"; // Return the registration page
		}

		// Proceed to register the new user
		boolean success = userService.registerUser(userDto);
		if (success) {
			model.addAttribute("success", "Registration successful! You can now log in.");
		} else {
			model.addAttribute("error", "Registration failed! Please try again later.");
		}

		return "register";
	}

}
