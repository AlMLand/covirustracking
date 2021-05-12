package com.almland.controller;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.hasProperty;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import com.almland.data.Location;
import com.almland.services.CoVirusDataService;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = CoVirusController.class)
public class CoVirusControllerTest {

	@Autowired
	private MockMvc mockMvc;
	@MockBean
	private CoVirusDataService mockedCoVirusDataService;
	
	@BeforeEach
	public void setUp() {
		Location locationTEST1 = Location.builder().state("Queensland")
				.country("Australia").latestTotalCases(100).deltaTheDayBefore(10).build();
		Location locationTEST2 = Location.builder().state("Macau")
				.country("China").latestTotalCases(200).deltaTheDayBefore(20).build();
		
		when(mockedCoVirusDataService.getAllStats()).thenReturn(List.of(locationTEST1, locationTEST2));
		when(mockedCoVirusDataService.getTotalReportedDeltaTheDayBefore()).thenReturn(1000);
		when(mockedCoVirusDataService.getTotalReportedCases()).thenReturn(9999);
	}
	
	@Test
	public void getHome_ShouldAddThreeModelAndRenderView() throws Exception {
		mockMvc.perform(get("/"))
			.andExpect(status().isOk())
			.andExpect(view().name("home"))
			.andExpect(model().hasNoErrors())
			.andExpect(model().attribute("totalReportedDeltaTheDayBefore", is(1000)))
			.andExpect(model().attribute("totalReportedCases", is(9999)))
			.andExpect(model().attribute("locationStatistics", Matchers.hasSize(2)))
			.andExpect(model().attribute("locationStatistics", hasItem(
					allOf(
							hasProperty("state", is("Queensland")),
							hasProperty("country", is("Australia")),
							hasProperty("latestTotalCases", is(100)),
							hasProperty("deltaTheDayBefore", is(10))
							)
					)))
			.andExpect(model().attribute("locationStatistics", hasItem(
					allOf(
							hasProperty("state", is("Macau")),
							hasProperty("country", is("China")),
							hasProperty("latestTotalCases", is(200)),
							hasProperty("deltaTheDayBefore", is(20))
							)
					)));
		
		verify(mockedCoVirusDataService, timeout(1)).getAllStats();
		verify(mockedCoVirusDataService, timeout(1)).getTotalReportedCases();
		verify(mockedCoVirusDataService, timeout(1)).getTotalReportedDeltaTheDayBefore();
		verifyNoMoreInteractions(mockedCoVirusDataService);
	}
	
}
