package io.notes.controller;

import java.security.Principal;
import java.util.Optional;

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
	
	// to get user data from login form data and use the data in any html page 
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
		if(notes.getTotalPages()==0) {
			m.addAttribute("totalPages", 1);
		}
		else {
		m.addAttribute("totalPages", notes.getTotalPages());
		}
		m.addAttribute("Notes", notes);
		m.addAttribute("totalElements", notes.getTotalElements());
		
		return "/user/view_notes";
	}
	
	@GetMapping("/editNotes/{id}")
	public String editNotes(@PathVariable int id, Model m) {
		Optional<Notes> notes = notesRepository.findById(id);
		
		if(notes!=null) {
			m.addAttribute("notes",notes.get());
		}
		
		return "/user/edit_notes";
	}
	
	
	@PostMapping("/updateNotes")
	public String updateNotes(@ModelAttribute Notes notes, HttpSession session, Principal p) {
		String email = p.getName();
		UserDtls user = userRepository.findByEmail(email);
		notes.setUserDtls(user);
		
		System.out.println(notes);
		
		Notes updateNotes = notesRepository.save(notes);
		
		if(updateNotes!=null) {
			session.setAttribute("msg", "Successfully Updated");
		}
		else {
			session.setAttribute("msg", "Problem in the server");
		}

		return "redirect:/user/viewNotes/0";
	}
	
	@GetMapping("/deleteNotes/{id}")
	public String deleteNotes(@PathVariable int id, HttpSession session) {
		Optional<Notes> notes = notesRepository.findById(id);
		
		if(notes!=null) {
			notesRepository.delete(notes.get());
			session.setAttribute("msg", "Deleted Successfully");
		}
		
		return "redirect:/user/viewNotes/0"; 
	}
	
	
	@GetMapping("/viewProfile")
	public String viewProfile() {
		return "/user/view_profile";
	}
	
	@PostMapping("/updateProfile")
	public String updateProfile(@ModelAttribute UserDtls user, Model m, HttpSession session) {
		
		Optional<UserDtls> Olduser = userRepository.findById(user.getId());
		
		if(Olduser!=null) {
			user.setEmail(Olduser.get().getEmail());
			user.setPassword(Olduser.get().getPassword());
			user.setRole(Olduser.get().getRole());
			
			UserDtls newUser = userRepository.save(user);
			if(newUser!=null) {
				m.addAttribute("user", newUser); // updates the addCommonData value
				session.setAttribute("msg", "Updated Successfully");
			}
		}
		
		return "redirect:/user/viewProfile"; 
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
