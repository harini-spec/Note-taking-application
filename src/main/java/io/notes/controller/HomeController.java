package io.notes.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import io.notes.entity.UserDtls;
import io.notes.repository.UserRepository;
import jakarta.servlet.http.HttpSession;


@Controller
public class HomeController {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private BCryptPasswordEncoder encoder;
	
	@GetMapping("/")
	public String home() {
		return "home";
	}
	
	@GetMapping("/signup")
	public String signup() {
		return "signup";
	}
	
	@GetMapping("/login")
	public String login() {
		return "login";
	}
	
	@PostMapping("/saveUser")
	public String saveUser(@ModelAttribute UserDtls user, Model model, HttpSession session) {
		
//		System.out.println(user.getId()+" "+user.getName());
		user.setPassword(encoder.encode(user.getPassword()));
		user.setRole("ROLE_USER");
		
		UserDtls userDetails = userRepository.save(user);
		
		if(userDetails!=null) {
			session.setAttribute("msg", "Registered Successfully!");
//			String val = "Registered Successfully!";
//			model.addAttribute("msg", val);
		}
		else {
//			String val = "Registered Unsuccessful!";
//			model.addAttribute("msg", val);
			session.setAttribute("msg", "Registration Unsuccessful!");
		}
		
		return "redirect:/signup";
	}

}
