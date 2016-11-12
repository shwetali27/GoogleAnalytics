package com.bridgelabz.responseElementReader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;

import com.bridgelabz.model.AllElementModels;
import com.bridgelabz.model.AppOpenModel;
import com.bridgelabz.model.AppReOpenModel;
import com.bridgelabz.model.GaReportInputModel;
import com.bridgelabz.model.ResponseModel;
import com.bridgelabz.model.SecretFileModel;
import com.bridgelabz.results.Operations;
import com.bridgelabz.results.SummaryReportCsv;
import com.bridgelabz.summary.Summary;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

public class ResponseElementReader {

	Summary summaryObject = new Summary();
	SummaryReportCsv summaryReportCsvObject = new SummaryReportCsv();

	int sum = 0;

	// creating object of dimensionHashMapArrayList to store
	// dimensionHashMapArrayList
	ArrayList<HashMap<String, String>> dimensionHashMapArrayList = new ArrayList<HashMap<String, String>>();
	// valueList to store values
	ArrayList<HashMap<String, String>> valueList = new ArrayList<HashMap<String, String>>();

	Operations operationObject = new Operations();
	Multimap<String, String> multiMapId = ArrayListMultimap.create();
	Multimap<String, String> multiMapEvent = ArrayListMultimap.create();
	Multimap<String, Collection<String>> multiMapvalue = ArrayListMultimap.create();

	// hash set for app open android id
	HashSet<String> androidIdAppOpen;
	// hash set for app Reopen android id
	HashSet<String> androidIdReAppOpen;

	//arraylist of map for date and total visited android ids
	ArrayList<Multimap<Integer, String>> list = new ArrayList<Multimap<Integer,String>>();
	//list for event performed
	ArrayList<String> task = new ArrayList<String>();
	//map for date and total visited android ids
	Multimap<Integer, String> totalCount = ArrayListMultimap.create();

	
	public void responseElementReader(ResponseModel responseModelObject, GaReportInputModel gaReportInputModel,
			int size) throws IOException {
		try {

			sum++;
			// creating object of ArrayListAppOpenModel
			ArrayList<AppOpenModel> appOpenModelArrayListObject = new ArrayList<AppOpenModel>();
			// creating object of ArrayListReAppOpenModel
			ArrayList<AppReOpenModel> appReOpenModelArrayListObject = new ArrayList<AppReOpenModel>();
			// creating object of AllElementArrayList
			ArrayList<AllElementModels> allElementModelArrayListObject = new ArrayList<AllElementModels>();
			boolean appOpenFlag = false;
			boolean appReOpenFlag = false;
			boolean allElementFlag = false;
			boolean allElementFlag1 = false;

			// assigning to dimensionHashMapArrayList
			dimensionHashMapArrayList = responseModelObject.getDimensionHashMapArrayList();
			valueList = responseModelObject.getMetricHashMapArrayList();
			// System.out.println("valuelist is is:"+valueList);

			/*-----------------------if response object have null value------------------------*/
			if (dimensionHashMapArrayList.equals("null")) {
				AllElementModels allElementModelsObject = new AllElementModels();
				allElementModelsObject.setmDate("");
				allElementModelsObject.setmAndroidId(" ");
			}

			else {
				for (int i = 0; i < dimensionHashMapArrayList.size(); i++) {
					// creating object of AppOpenModel
					AppOpenModel appOpenModelObject = new AppOpenModel();
					// creating object of AppReOpenModel
					AppReOpenModel appReOpenModelObject = new AppReOpenModel();
					// creating object of AllElementModel
					AllElementModels allElementModelsObject = new AllElementModels();

					// iterating element of hashmapArrayList
					for (Entry<String, String> m1 : dimensionHashMapArrayList.get(i).entrySet()) {

						/*----------------------------------for appOpen-----------------------------------------------------*/
						if (gaReportInputModel.getmGaID().equals("1")) {

							appReOpenFlag = true;
							allElementFlag = true;
							appOpenModelObject.setmGaId(gaReportInputModel.getmGaID());

							appOpenModelObject.setmGadiscription(gaReportInputModel.getmGaDiscription());

							if (m1.getKey().equals("ga:date")) {
								appOpenModelObject.setmDate(m1.getValue());

							}
							if (m1.getKey().equals("ga:dimension1")) {
								appOpenModelObject.setmAndroidId(m1.getValue());

							}
							if (m1.getKey().equals("ga:eventCategory")) {
								appOpenModelObject.setmEventcategory(m1.getValue());

							}

						}
						/*----------------------------------for appReOpen------------------------------------------------*/

						if (gaReportInputModel.getmGaID().equals("2")) {

							appOpenFlag = true;
							allElementFlag1 = true;
							appReOpenModelObject.setmGaId(gaReportInputModel.getmGaID());

							appReOpenModelObject.setmGadiscription(gaReportInputModel.getmGaDiscription());

							if (m1.getKey().equals("ga:date")) {
								appReOpenModelObject.setmDate(m1.getValue());

							}
							if (m1.getKey().equals("ga:dimension1")) {
								appReOpenModelObject.setmAndroidId(m1.getValue());

							}
							if (m1.getKey().equals("ga:eventCategory")) {
								appReOpenModelObject.setmEventcategory(m1.getValue());

							}

						}
						/*-------------if other than App open and ReOpen------*/
						if (!gaReportInputModel.getmGaID().equals("1") && !gaReportInputModel.getmGaID().equals("2")) {

							allElementModelsObject.setmGaid(gaReportInputModel.getmGaID());

							allElementModelsObject.setmGadiscription(gaReportInputModel.getmGaDiscription());

							if (m1.getKey().equals("ga:date")) {
								allElementModelsObject.setmDate(m1.getValue());
							}
							if (m1.getKey().equals("ga:dimension1")) {
								allElementModelsObject.setmAndroidId(m1.getValue());

							}

						}

					}
					if (!appOpenFlag)
						appOpenModelArrayListObject.add(appOpenModelObject);
					if (!appReOpenFlag)
						appReOpenModelArrayListObject.add(appReOpenModelObject);
					if (!allElementFlag && !allElementFlag1)
						allElementModelArrayListObject.add(allElementModelsObject);
				}
			}

			// System.out.println(allElementModelArrayListObject.toString());

			/*----------------------getting data for app Open and putting inside map--------------------*/
			if (gaReportInputModel.getmGaID().equals("1")) {

				for (int i = 0; i < appOpenModelArrayListObject.size(); i++) {
					// System.out.println(SecretFileModel.startDate.replace("-",""));
					// taking the id for first day app open
					if (appOpenModelArrayListObject.get(i).getmDate()
							.equals(SecretFileModel.startDate.replace("-", ""))) {
						multiMapId.put(appOpenModelArrayListObject.get(i).getmAndroidId(),
								appOpenModelArrayListObject.get(i).getmDate());
						multiMapEvent.put(appOpenModelArrayListObject.get(i).getmAndroidId(),
								appOpenModelArrayListObject.get(i).getmGadiscription());
						multiMapvalue.put(appOpenModelArrayListObject.get(i).getmAndroidId(),
								valueList.get(i).values());

					}
				}

				//checking if particular android id have opened the app again on another date
				for (int i = 0; i < appOpenModelArrayListObject.size(); i++) {
					Set<String> keys = multiMapId.keySet();
					// System.out.println(keys.size());
					for (String key : keys) {
						// if particular android id is present inside app open
						// then only add data
						if (appOpenModelArrayListObject.get(i).getmAndroidId().equals(key)
								&& !appOpenModelArrayListObject.get(i).getmDate()
										.equals(SecretFileModel.startDate.replace("-", ""))) {
							
							multiMapId.put(appOpenModelArrayListObject.get(i).getmAndroidId(),
									appOpenModelArrayListObject.get(i).getmDate());
							multiMapEvent.put(appOpenModelArrayListObject.get(i).getmAndroidId(),
									appOpenModelArrayListObject.get(i).getmGadiscription());
							multiMapvalue.put(appOpenModelArrayListObject.get(i).getmAndroidId(),
									valueList.get(i).values());
						}
					}
				}
				task.add(appOpenModelArrayListObject.get(0).getmGadiscription());
				//calling the method for summary report creation
				totalCount = summaryObject.creatReport(appOpenModelArrayListObject, multiMapId);
				list.add(totalCount);
			}

			/*----------------------getting data for app Reopen and putting inside map--------------------*/
			if (gaReportInputModel.getmGaID().equals("2")) {

				for (int i = 0; i < appReOpenModelArrayListObject.size(); i++) {
					Set<String> keys = multiMapId.keySet();
					// System.out.println(keys.size());
					for (String key : keys) {

						// if particular android id is present inside app open
						// then only add data
						if (appReOpenModelArrayListObject.get(i).getmAndroidId().equals(key)) {
							
							multiMapId.put(appReOpenModelArrayListObject.get(i).getmAndroidId(),
									appReOpenModelArrayListObject.get(i).getmDate());
							multiMapEvent.put(appReOpenModelArrayListObject.get(i).getmAndroidId(),
									appReOpenModelArrayListObject.get(i).getmGadiscription());
							multiMapvalue.put(appReOpenModelArrayListObject.get(i).getmAndroidId(),
									valueList.get(i).values());
						}
					}
				}

			}
			
			/*----------------------getting data for other task and putting inside map--------------------*/			if (!gaReportInputModel.getmGaID().equals("1") && !gaReportInputModel.getmGaID().equals("2")) {
				for (int i = 0; i < allElementModelArrayListObject.size(); i++) {
					Set<String> keys = multiMapId.keySet();
					for (String key : keys) {
						// if particular android id is present inside app open
						// then only add data
						if (allElementModelArrayListObject.get(i).getmAndroidId().equals(key)) {
							multiMapId.put(allElementModelArrayListObject.get(i).getmAndroidId(),
									allElementModelArrayListObject.get(i).getmDate());
							multiMapEvent.put(allElementModelArrayListObject.get(i).getmAndroidId(),
									allElementModelArrayListObject.get(i).getmGadiscription());
							multiMapvalue.put(allElementModelArrayListObject.get(i).getmAndroidId(),
									valueList.get(i).values());
						}
					}
				}

			}
		} catch (Exception e) {
			System.out.println("there is 0 rows in response");
			// e.printStackTrace();

		}

		//creating the report text file
		if (sum == size) {
			operationObject.fileCreation(multiMapId, multiMapEvent, multiMapvalue);
		}
		
		//calling the method for csv creation
		summaryReportCsvObject.csvCreation(task, list);

	}// end of method

}
