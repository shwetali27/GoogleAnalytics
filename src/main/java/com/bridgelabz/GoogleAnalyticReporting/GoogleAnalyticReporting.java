package com.bridgelabz.GoogleAnalyticReporting;

import java.io.IOException;
import java.util.ArrayList;

import com.bridgelabz.inputReader.InputJsonReader;
import com.bridgelabz.model.GaReportInputModel;
import com.bridgelabz.model.ResponseModel;
import com.bridgelabz.responseElementReader.ResponseElementReader;
import com.bridgelabz.responseFetcher.GaReportResponseFetcher;

public class GoogleAnalyticReporting {
	public static void main(String[] args) throws IOException{
		String jsonfilepath = args[0];
		
		System.out.println("csvfilepath:"+jsonfilepath);
		
		//reading the input json file
		InputJsonReader inputJsonReader = new InputJsonReader();
		ArrayList<GaReportInputModel> gaReportInputInfoArrayList = inputJsonReader.readInputJsonFile(jsonfilepath);
	
		ResponseModel responseModelObject = new ResponseModel();
		GaReportResponseFetcher gaReportResponseFetcherObject = new GaReportResponseFetcher(); 
		ResponseElementReader elementReader = new ResponseElementReader();
		for (int i = 0; i < gaReportInputInfoArrayList.size(); i++) {

			// making ArrayList of responseModel after passing one by one gaReportInputInfoArrayList

			responseModelObject = gaReportResponseFetcherObject
					.getResponse(gaReportInputInfoArrayList.get(i));

			elementReader.responseElementReader(responseModelObject,
					gaReportInputInfoArrayList.get(i),gaReportInputInfoArrayList.size());

		}
		System.out.println("Finished");
	}
}
