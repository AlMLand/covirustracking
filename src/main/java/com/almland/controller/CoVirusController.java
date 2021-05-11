package com.almland.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.almland.services.CoVirusDataService;

@Controller
public class CoVirusController {

	@Autowired
	private CoVirusDataService coVirusDataService;
	
	@GetMapping
	public String getHome(Model model) {
		model.addAttribute("TotalReportedDeltaTheDayBefore", coVirusDataService.getTotalReportedDeltaTheDayBefore());
		model.addAttribute("totalReportedCases", coVirusDataService.getTotalReportedCases());
		model.addAttribute("locationStatistics", coVirusDataService.getAllStats());
		return "home";
	}
	
}
