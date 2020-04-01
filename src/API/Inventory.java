package API;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import AccountDetail.MainClass;


public class Inventory {
	private HttpGet get;
	private HttpResponse API_Response;
	private Double inventoryValue;
	private String APIURI;

	public Inventory(String APIURI) {
		this.APIURI = APIURI;
	}
	
	public void executeGet() {
		
		get = new HttpGet(APIURI);
		try {
			API_Response = MainClass.getHttpClient().execute(get);
			parseResponse();

		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void parseResponse() {
		JSONParser parser = new JSONParser();
		JSONArray allRecords = new JSONArray();
		inventoryValue = 0.00;
		
		try {
			BufferedReader rd = new BufferedReader(new InputStreamReader(API_Response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
		
				//Parse our JSON response                           
				JSONObject API_Response = (JSONObject)parser.parse(line);
				allRecords = (JSONArray) API_Response.get("rows");

				for(Object inventoryItem : allRecords) {
					JSONObject inventory_item = (JSONObject) inventoryItem;
					if(inventory_item.get("averageCost")==null) {
						continue;
					}
					Double totalCost = (Double) inventory_item.get("averageCost") *
							(Double) inventory_item.get("inventoryOnHand");
					inventoryValue = totalCost+inventoryValue;			
				}
			}
		}
		

		catch (IOException | ParseException e) {
			e.printStackTrace();
		}
	}

	
	
	public Double getInventoryValue() {
		return inventoryValue;
	}
	
}

