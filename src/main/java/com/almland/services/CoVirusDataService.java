package com.almland.services;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Collection;

import javax.annotation.PostConstruct;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.almland.data.Location;

@Service
public class CoVirusDataService {

	private final static String CO_VIRUS_DATASOURCE_JOHNS_HOPKINS_UNIVERSITY_URL = "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_time_series/time_series_covid19_recovered_global.csv";

	private Collection<Location> allStats = new ArrayList<>();
	private Collection<Location> newStats = new ArrayList<>();

	@Scheduled(cron = "* * 1 * * *")
	@PostConstruct
	private void fetchCoVirusData() throws IOException, InterruptedException {
		HttpResponse<String> response = callOfUrl();
		readDataFromResponse(response);

		this.allStats = this.newStats;
	}

	private void readDataFromResponse(HttpResponse<String> response) throws IOException {
		Reader reader = new StringReader(response.body());
		Iterable<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(reader);

		addNewDataToNewStats(records);
	}

	private void addNewDataToNewStats(Iterable<CSVRecord> records) {
		for (CSVRecord record : records) {
			this.newStats
					.add(Location.builder().state(record.get("Province/State")).country(record.get("Country/Region"))
							.latestTotalCases(Integer.parseInt(record.get(record.size() - 1)))
							.deltaTheDayBefore(Integer.parseInt(record.get(record.size() - 1))
									- Integer.parseInt(record.get(record.size() - 2)))
							.build());
		}
	}

	private HttpResponse<String> callOfUrl() throws IOException, InterruptedException {
		HttpClient client = HttpClient.newHttpClient();
		HttpRequest request = HttpRequest.newBuilder().uri(URI.create(CO_VIRUS_DATASOURCE_JOHNS_HOPKINS_UNIVERSITY_URL))
				.build();
		return client.send(request, HttpResponse.BodyHandlers.ofString());
	}

	public int getTotalReportedCases() {
		return this.allStats.stream().mapToInt(stat -> stat.getLatestTotalCases()).sum();
	}

	public int getTotalReportedDeltaTheDayBefore() {
		return this.allStats.stream().mapToInt(stat -> stat.getDeltaTheDayBefore()).sum();
	}

	public Collection<Location> getAllStats() {
		return allStats;
	}

}