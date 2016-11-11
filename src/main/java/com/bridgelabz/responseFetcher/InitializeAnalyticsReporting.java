package com.bridgelabz.responseFetcher;

import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Arrays;
import com.bridgelabz.model.GaReportInputModel;
import com.bridgelabz.model.SecretFileModel;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.analyticsreporting.v4.AnalyticsReporting;
import com.google.api.services.analyticsreporting.v4.AnalyticsReportingScopes;
import com.google.api.services.analyticsreporting.v4.model.DateRange;
import com.google.api.services.analyticsreporting.v4.model.Dimension;
import com.google.api.services.analyticsreporting.v4.model.DimensionFilter;
import com.google.api.services.analyticsreporting.v4.model.DimensionFilterClause;
import com.google.api.services.analyticsreporting.v4.model.GetReportsRequest;
import com.google.api.services.analyticsreporting.v4.model.GetReportsResponse;
import com.google.api.services.analyticsreporting.v4.model.Metric;
import com.google.api.services.analyticsreporting.v4.model.ReportRequest;

public class InitializeAnalyticsReporting {
	
	// creating object of GaReportInputModel to get metric and dimension
	GaReportInputModel gaReportInputModelObject = new GaReportInputModel();

	
	//setting global variable 
	static String APPLICATION_NAME;
	static JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
	static String KEY_FILE_LOCATION;
	static String SERVICE_ACCOUNT_EMAIL ;
	static String VIEW_ID ;
	static String startDate;
	static String endDate;
	
	public InitializeAnalyticsReporting() {
	}

	public AnalyticsReporting initializeAnalyticsReporting() throws GeneralSecurityException, IOException {

		APPLICATION_NAME = SecretFileModel.getAPPLICATION_NAME();
		KEY_FILE_LOCATION = SecretFileModel.getKEY_FILE_LOCATION();
		SERVICE_ACCOUNT_EMAIL = SecretFileModel.getSERVICE_ACCOUNT_EMAIL();
		VIEW_ID = SecretFileModel.VIEW_ID;
		startDate=SecretFileModel.getStartDate();
		endDate=SecretFileModel.getEndDate();
		
		HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
		GoogleCredential credential = new GoogleCredential.Builder().setTransport(httpTransport)
				.setJsonFactory(JSON_FACTORY).setServiceAccountId(SERVICE_ACCOUNT_EMAIL)
				.setServiceAccountPrivateKeyFromP12File(new File(KEY_FILE_LOCATION))
				.setServiceAccountScopes(AnalyticsReportingScopes.all()).build();
		
		// getting access token
		String refreshToken = null;
		credential.setRefreshToken(refreshToken);
		credential.refreshToken();
		if (!credential.refreshToken()) {
			throw new RuntimeException("Failed OAuth to refresh the token");
		}
		
		// Construct the Analytics Reporting service object.
		return new AnalyticsReporting.Builder(httpTransport, JSON_FACTORY, credential)
				.setApplicationName(APPLICATION_NAME).build();
	}

	
	// method which give response after setting dimension metric and filter
	public GetReportsResponse getReport(AnalyticsReporting service, GaReportInputModel gaReportInputModel)
			throws IOException {

		ArrayList<String> metricArrayList = new ArrayList<String>();
		ArrayList<String> dimensionArrayList = new ArrayList<String>();
		ArrayList<String> dimensionFilterArrayList = new ArrayList<String>();
		
		// Creating the DateRange object.
		DateRange dateRange = new DateRange();
		dateRange.setStartDate(startDate);
		dateRange.setEndDate(endDate);

		// getting metricArray from model class
		metricArrayList = gaReportInputModel.getmMetricArraList();
		// creating object of metric ArrayList
		ArrayList<Metric> metriclist = new ArrayList<Metric>();
		metriclist.clear();
		for (int j = 0; j < metricArrayList.size(); j++) {
			// Creating the Metrics object.
			Metric metric = new Metric();
			// adding metric into metric ArrayList
			metriclist.add(metric.setExpression((String) metricArrayList.get(j)));
		}

		// getting dimensionArray from model class
		dimensionArrayList = gaReportInputModel.getmDimensionArraList();
		Dimension dimens;
		// Creating the Dimensions ArrayList.
		ArrayList<Dimension> dimensList = new ArrayList<Dimension>();
		dimensList.clear();
		for (int i = 0; i < dimensionArrayList.size(); i++) {
			// Creating the Dimensions object.
			dimens = new Dimension();
			// adding dimension after setting name into the dimension ArrayList
			dimensList.add(dimens.setName((String) dimensionArrayList.get(i)));
		}
		
		
		// getting dimenstionFilter from model class
		dimensionFilterArrayList = gaReportInputModel.getmDimensionFilterArraList();
		// creating object of DimensionFilter arrayList
		ArrayList<DimensionFilter> dimensfilterList = new ArrayList<DimensionFilter>();
		dimensfilterList.clear();
		
		if (dimensionFilterArrayList.size() >= 1) {
			for (int k = 0; k < dimensionFilterArrayList.size(); k++) {
				// created DimensionFilter object
				DimensionFilter dimensionFilter = new DimensionFilter();
				// taking DimensionFilter and converting into String
				String dimensionfilterString = (String) dimensionFilterArrayList.get(k);
				
				// checking whether exact/partial operator inside DimensionFilter
				
				if (dimensionfilterString.contains("==")) {
					// Splitting the DimensionFilter
					String[] words = dimensionfilterString.split("==");
					// adding into dimensfilterList after setting the parameter
					dimensfilterList.add(dimensionFilter.setDimensionName(words[0]).setOperator("EXACT")
							.setExpressions(Arrays.asList(words[1])));
					//System.out.println("equals");
				} else if (dimensionfilterString.contains("=@:"))

				{
					String[] words = dimensionfilterString.split("=@:");
					dimensfilterList.add(dimensionFilter.setDimensionName(words[0]).setOperator("PARTIAL")
							.setExpressions(Arrays.asList(words[1])));
					//System.out.println("at the rate");
				} else {
					String[] words = dimensionfilterString.split("=@");
					dimensfilterList.add(dimensionFilter.setDimensionName(words[0]).setOperator("PARTIAL")
							.setExpressions(Arrays.asList(words[1])));
					//System.out.println("at the rate");

				}

			}
		}

		// creating DimensionFilterClause object
		DimensionFilterClause dimensionFilterPathClause = new DimensionFilterClause();
		// making ArrayList of DimensionFilterClause
		ArrayList<DimensionFilterClause> dmfilterclauselist = new ArrayList<DimensionFilterClause>();
		// adding dimFilters to it
		dmfilterclauselist.add(dimensionFilterPathClause.setFilters(dimensfilterList).setOperator("AND"));

		// Creating the ReportRequest object.
		ReportRequest request = new ReportRequest()
				.setViewId(VIEW_ID)
				.setDateRanges(Arrays.asList(dateRange))
				.setMetrics(metriclist)
				.setDimensions(dimensList);
		
		// if dimensionFilter is available then only set it
		if (dimensionFilterArrayList.size() >= 1) {
			request.setDimensionFilterClauses(dmfilterclauselist);
		}

		// making ReportRequest ArrayList
		ArrayList<ReportRequest> requests = new ArrayList<ReportRequest>();
		requests.add(request);
		// Creating the GetReportsRequest object.
		GetReportsRequest getReport = new GetReportsRequest().setReportRequests(requests);
		// Calling the batchGet method.
		GetReportsResponse response = service.reports().batchGet(getReport).execute();
		// Returning the response.
		return response;
	}

}
