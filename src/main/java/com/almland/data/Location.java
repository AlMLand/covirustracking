package com.almland.data;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Location {

	private String state;
	private String country;
	private int latestTotalCases;
	private int deltaTheDayBefore;
	
}
