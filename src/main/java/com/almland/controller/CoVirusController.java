package com.almland.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CoVirusController {

	@GetMapping
	public String getHome() {
		return "home";
	}
	
}
