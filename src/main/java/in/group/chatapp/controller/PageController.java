package in.group.chatapp.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PageController {

	@GetMapping("/register")
	public String registerPage() {
		
		return "register";
	}

	@GetMapping("/login")
	public String loginPage(@RequestParam(value = "logout", required = false) String logout, Model model) {
		if (logout != null) {
			model.addAttribute("logoutMessage", "You have been logged out successfully.");
		}
		return "login"; // Return the login.html template
	}

	@GetMapping("/chat")
	public String chatPage(Model model) {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username = authentication.getName();

		model.addAttribute("username", username);
		return "chat";
	}

	@GetMapping("/logout")
	public String logout() {
		return "login";
	}

}
