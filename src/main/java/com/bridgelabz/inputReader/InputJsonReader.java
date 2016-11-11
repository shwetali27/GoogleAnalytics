package com.bridgelabz.inputReader;

import java.io.FileReader;
import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.bridgelabz.model.GaReportInputModel;
import com.bridgelabz.model.SecretFileModel;

public class InputJsonReader {
	
	ArrayList<GaReportInputModel> GaReportInputModelArrayList = new ArrayList<GaReportInputModel>();

	public ArrayList<GaReportInputModel> readInputJsonFile(String jsonFilePath){
		try{

			JSONParser parser = new JSONParser();//json parser is used to read json files
			// parsing and casting to Object
			Object obj = parser.parse(new FileReader(jsonFilePath));
			// casting object into JSONObject
			JSONObject jsonObject = (JSONObject) obj;//creating json object

			// setting static values in secretFileModelObject
			SecretFileModel.setStartDate((String) jsonObject.get("startDate"));

			SecretFileModel.setEndDate((String) jsonObject.get("endDate"));

			SecretFileModel.setAPPLICATION_NAME((String) jsonObject.get("APPLICATION_NAME"));

			SecretFileModel.setKEY_FILE_LOCATION((String) jsonObject.get("KEY_FILE_LOCATION"));

			SecretFileModel.setSERVICE_ACCOUNT_EMAIL((String) jsonObject.get("SERVICE_ACCOUNT_EMAIL"));

			SecretFileModel.setVIEW_ID((String) jsonObject.get("VIEW_ID"));

			SecretFileModel.setCsvFilePath((String) jsonObject.get("CSVFilePath"));

			//System.out.println(SecretFileModel.getCsvFilePath());
			
			JSONArray gaReportInfoArray = (JSONArray) jsonObject.get("GAReportInfo");

			// reading one by one object
			for (int i = 0; i < gaReportInfoArray.size(); i++) {
				GaReportInputModel gaReportInputModelObject = new GaReportInputModel();

				// initializing all value

				ArrayList<String> metricArraList = new ArrayList<String>();
				ArrayList<String> dimensionArraList = new ArrayList<String>();
				ArrayList<String> dimensionFilterArraList = new ArrayList<String>();

				JSONObject gaReportInfoObject = (JSONObject) gaReportInfoArray.get(i);

				metricArraList.clear();
				dimensionArraList.clear();
				dimensionFilterArraList.clear();

				// setting gaid into model class

				gaReportInputModelObject.setmGaID((String) gaReportInfoObject.get("GAID"));

				// setting in model class

				gaReportInputModelObject.setmGaDiscription((String) gaReportInfoObject.get("GAdiscription"));

				// making metric array
				JSONArray metricJSONArray = (JSONArray) gaReportInfoObject.get("metric");
				// reading the metric array
				for (int k = 0; k < metricJSONArray.size(); k++) {
					// adding into metric ArrayList
					metricArraList.add((String) metricJSONArray.get(k));
				}
				// setting metric in model class
				gaReportInputModelObject.setmMetricArraList(metricArraList);

				// making dimension JSONArray
				JSONArray dimensionsJSONArray = (JSONArray) gaReportInfoObject.get("dimension");
				// reading the dimension array
				for (int j = 0; j < dimensionsJSONArray.size(); j++) {
					dimensionArraList.add((String) dimensionsJSONArray.get(j));
				}
				// setting dimension in model class
				gaReportInputModelObject.setmDimensionArraList(dimensionArraList);

				// Casting DimensionFilter into JSONArray
				JSONArray dimensionFilterJSONArray = (JSONArray) gaReportInfoObject.get("dimensionfilter");

				for (int l = 0; l < dimensionFilterJSONArray.size(); l++) {
					// adding into DimensionFilter ArrayList
					dimensionFilterArraList.add((String) dimensionFilterJSONArray.get(l));
				}
				// setting dimension filter in model class
				gaReportInputModelObject.setmDimensionFilterArraList(dimensionFilterArraList);

				GaReportInputModelArrayList.add(gaReportInputModelObject);
			}
		}
		catch (Exception e) {
			System.out.println(e);
		}
		
		return GaReportInputModelArrayList;
	}
}
