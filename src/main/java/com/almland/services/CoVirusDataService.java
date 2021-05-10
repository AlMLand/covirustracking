package com.almland.services;

import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import javax.annotation.PostConstruct;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;

@Service
public class CoVirusDataService {

	private final String CO_VIRUS_DATASOURCE_JOHNS_HOPKINS_UNIVERSITY_URL = "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_time_series/time_series_covid19_recovered_global.csv";
	
	@PostConstruct
	public void fetchCoVirusData() throws IOException, InterruptedException {
		HttpClient client = HttpClient.newHttpClient();
		HttpRequest request = HttpRequest.newBuilder()
				.uri(URI.create(CO_VIRUS_DATASOURCE_JOHNS_HOPKINS_UNIVERSITY_URL))
				.build();
		HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
		
		Iterable<CSVRecord> records = CSVFormat.DEFAULT
				.withFirstRecordAsHeader().parse(new StringReader(response.body()));
		
		for(CSVRecord csvRecord : records) {
			String provinceState = csvRecord.get("Province/State");
			System.out.println(provinceState);
		}
	}
	
}