package API;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import AccountDetail.MainClass;
import Utils.EntityBuilder;


public class AccountsPayable {
	private String API_URL; 
	private Map<String, String> formData;
	private HttpPost post;
	private HttpEntity entity;
	HttpResponse API_Response;
	private BigDecimal accountsPayable;

	
	public AccountsPayable(String API_URL) {
		this.API_URL = API_URL;
	}


	private Map<String, String> getAPIConstants () {
		formData = new HashMap<>();
		formData.put("_search", "false");
		formData.put("rows", "1000000");
		formData.put("page","1");
		formData.put("sidx", "payableTo");
		formData.put("sord", "asc");
		return formData;
	}

	public void executePost() {
		post = new HttpPost(API_URL);
		entity = new EntityBuilder(getAPIConstants()).getEntity();
		post.setEntity(entity);
		try {
			API_Response = MainClass.getHttpClient().execute(post);
		} catch (IOException e) {
			e.printStackTrace();
		}
		parseResponse();		 
	}

	public void parseResponse() {
		JSONParser parser = new JSONParser();
		JSONArray allVeBills = new JSONArray();
		accountsPayable = new BigDecimal(0);
		try {
			BufferedReader rd = new BufferedReader(new InputStreamReader(API_Response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
				//Parse our JSON response                           
				JSONObject responseJSON = (JSONObject)parser.parse(line);
				allVeBills = (JSONArray) responseJSON.get("rows");

				for(Object VeBill : allVeBills) {
					JSONObject invoiceDetail = (JSONObject) VeBill;
					Double totalInvAmount = (Double) invoiceDetail.get("billAmount");
					BigDecimal TotalInvAmount = BigDecimal.valueOf(totalInvAmount.doubleValue());
					accountsPayable = TotalInvAmount.add(accountsPayable);
				}
			}
		}

		catch (IOException | ParseException e) {
			e.printStackTrace();
		}
	}
	
	public BigDecimal getAccountsPayable() {
		return accountsPayable;
	}
}
