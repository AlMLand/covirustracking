package com.almland.services;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.almland.data.Location;

import lombok.Getter;

@Service
public class CoVirusDataService {

	private final String CO_VIRUS_DATASOURCE_JOHNS_HOPKINS_UNIVERSITY_URL = "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_time_series/time_series_covid19_recovered_global.csv";
	@Getter
	private List<Location> allStats = new ArrayList<>();

	@Scheduled(cron = "* * 1 * * *")
	@PostConstruct
	public void fetchCoVirusData() throws IOException, InterruptedException {
		List<Location> newStats = new ArrayList<>();

		HttpClient client = HttpClient.newHttpClient();
		HttpRequest request = HttpRequest.newBuilder().uri(URI.create(CO_VIRUS_DATASOURCE_JOHNS_HOPKINS_UNIVERSITY_URL))
				.build();
		HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

		Reader reader = new StringReader(response.body());
		Iterable<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(reader);

		for (CSVRecord record : records) {
			newStats.add(Location.builder()
					.state(record.get("Province/State"))
					.country(record.get("Country/Region"))
					.lastestTotalCases(Integer.parseInt(record.get(record.size() - 1))).build());
		}
		this.allStats = newStats;
	}

}