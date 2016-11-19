package com.bridgelabz.GoogleAnalyticReporting;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.bridgelabz.inputReader.InputJsonReader;
import com.bridgelabz.model.GaReportInputModel;
import com.bridgelabz.model.ResponseModel;
import com.bridgelabz.responseElementReader.ResponseElementReader;
import com.bridgelabz.responseFetcher.GaReportResponseFetcher;

public class GoogleAnalyticReporting {
	public static void main(String[] args) throws IOException, GeneralSecurityException{
		String jsonfilepath = args[0];
		Logger logger = Logger.getLogger(GoogleAnalyticReporting.class);
		logger.debug("log debug");
		
		logger.debug("jsonFilepath:"+jsonfilepath);
		//reading the input json file and storing inside the list
		InputJsonReader inputJsonReader = new InputJsonReader();
		ArrayList<GaReportInputModel> gaReportInputInfoArrayList = inputJsonReader.readInputJsonFile(jsonfilepath);
	
		ResponseModel responseModelObject = new ResponseModel();
		GaReportResponseFetcher gaReportResponseFetcherObject = new GaReportResponseFetcher(); 
		ResponseElementReader elementReader = new ResponseElementReader();
		
		for (int i = 0; i < gaReportInputInfoArrayList.size(); i++) {

			// getting ArrayList of responseModel for each gaReportInputInfoArrayList
			responseModelObject = gaReportResponseFetcherObject
					.getResponse(gaReportInputInfoArrayList.get(i));

			//reading the response and finding the result
			elementReader.responseElementReader(responseModelObject,
					gaReportInputInfoArrayList.get(i),gaReportInputInfoArrayList.size());

		}
		System.out.println("Finished");
	}
}
