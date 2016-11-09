package com.bridgelabz.results;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.Set;

import com.bridgelabz.model.SecretFileModel;
import com.google.common.collect.Multimap;

public class Operations {

	SecretFileModel secretFileModel = new SecretFileModel();

	public Operations() {
	}

	public void fileCreation(Multimap<String, String> multiMapId, Multimap<String, String> multiMapEvent,
			Multimap<String, Collection<String>> multiMapvalue) throws IOException {

		// String filePath = SecretFileModel.getCsvFilePath();
		String filePath = "/home/bridgeit/Music/AndroidId/";
		System.out.println("Filepath is: " + filePath);
		File file = new File(filePath + "AndroidOperations.txt");
		if (file.exists())
			file.delete();

		if (!file.exists())
			file.createNewFile();

		// System.out.println(file.exists());
		FileWriter fw = new FileWriter(file.getAbsoluteFile(), true);
		BufferedWriter bw = new BufferedWriter(fw);

		Set<String> keys = multiMapId.keySet();
		//System.out.println(keys.size());

		for (String key : keys) {
			//System.out.println("\nKey = " + key);
			// System.out.println("Values = " + multiMapId.keys() + "n");

			// System.out.println("Values = " + multiMapDate.get(key) + "n");
			bw.write("Android Id: " + key + "\n");
			String dateStr = multiMapId.get(key).toString();
			String dateString = dateStr.substring(1, (dateStr.length() - 1));
			String[] dateStringArray = dateString.split(",");

			String eventstr = multiMapEvent.get(key).toString();
			String eventString = eventstr.substring(1, (eventstr.length() - 1));
			String[] eventstringArray = eventString.split(",");

			String valuestr = multiMapvalue.get(key).toString();
			String valueString = valuestr.substring(2, (valuestr.length() - 2));
			String[] valuestringArray = valueString.split("],");

			// System.out.println("valueArray: "+valueString);

			
			for (int i = 0; i < dateStringArray.length; i++) {
				//System.out.print(dateStringArray[i].trim() + " : " + eventstringArray[i].trim() + "\n");
				bw.write(dateStringArray[i].trim() + " : " + eventstringArray[i].trim() + "\n");
				if (valuestringArray[i].trim().length()<5) {
					//System.out.print("total events:	" + valuestringArray[i].replace("[", "").trim());
					bw.write("total events:	" + valuestringArray[i].replace("[", "").trim()+"\n");
				} else {
					String[] valuestringArray1 = valuestringArray[i].split(",");
					//System.out.println("Length: " + valuestringArray1.length);
					
					//System.out.println("sessions:	"+valuestringArray1[1].replace("[", "").trim());
					bw.write("sessions:	"+valuestringArray1[1].replace("[", "").trim()+"\n");
					//System.out.println("screenviews:	"+valuestringArray1[2].replace("[", "").trim());
					bw.write("screenviews:	"+valuestringArray1[2].replace("[", "").trim()+"\n");
					//System.out.println("exits:		"+valuestringArray1[0].replace("[", "").trim());
					bw.write("exits:		"+valuestringArray1[0].replace("[", "").trim()+"\n");
					//System.out.println("exitRate:	"+valuestringArray1[3].replace("[", "").trim());
					bw.write("exitRate:	"+valuestringArray1[3].replace("[", "").trim()+"\n");

				}

				//System.out.println("\n");
				bw.write("\n");
				
			}
			bw.write("-------------------------------------------\n");
			
			//System.out.println("-----------------------------------------");
		}
		bw.close();
	}
}