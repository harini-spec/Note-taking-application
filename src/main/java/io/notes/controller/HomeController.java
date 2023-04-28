package io.notes.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import io.notes.entity.UserDetails;
import io.notes.repository.UserRepository;


@Controller
public class HomeController {
	
	@Autowired
	private UserRepository userRepository;
	
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
	public String saveUser(@ModelAttribute UserDetails user, Model m) {
		
		System.out.println(user.getId()+" "+user.getName());
		userRepository.save(user);
		
		
		return "redirect:/signup";
	}

}
