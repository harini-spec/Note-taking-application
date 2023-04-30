package io.notes.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import io.notes.entity.Notes;
import io.notes.entity.UserDtls;
import io.notes.repository.NotesRepository;
import io.notes.repository.UserRepository;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private NotesRepository notesRepository;
	
	// to get user data from login form data
	@ModelAttribute
	public void addCommonData(Principal p, Model model) { 
		String email = p.getName();
		UserDtls user = userRepository.findByEmail(email);
		model.addAttribute("user", user);
	}

	@GetMapping("/addNotes")
	public String addNotes() {
		return "/user/add_notes";
	}
	
	@GetMapping("/viewNotes/{page}")
	public String viewNotes(@PathVariable int page, Model m, Principal p) {
		String email = p.getName();
		UserDtls user = userRepository.findByEmail(email);
		
		// id of notes, takes 5 notes in one page 
		Pageable pageable = PageRequest.of(page, 5, Sort.by("id").descending());
		
		Page<Notes> notes = notesRepository.findNotesByUser(user.getId(), pageable);
		
		m.addAttribute("pageNo", page);
		m.addAttribute("totalPages", notes.getTotalPages());
		m.addAttribute("Notes", notes);
		m.addAttribute("totalElements", notes.getTotalElements());
		
		return "/user/view_notes";
	}
	
	@GetMapping("/editNotes")
	public String editNotes() {
		return "/user/edit_notes";
	}
	
	@GetMapping("/viewProfile")
	public String viewProfile() {
		return "/user/view_profile";
	}
	
	@PostMapping("/saveNotes")
	public String saveNotes(@ModelAttribute Notes notes, HttpSession session, Principal p) {
		
		String email = p.getName();
		UserDtls user = userRepository.findByEmail(email);
		notes.setUserDtls(user);
		
		Notes note = (Notes) notesRepository.save(notes);
		System.out.println(note);
		
		if(note!=null) {
			session.setAttribute("msg", "Successfully added");
		}
		else {
			session.setAttribute("msg", "Problem in the server");
		}
		
		return "redirect:/user/addNotes";
	}
	
}
