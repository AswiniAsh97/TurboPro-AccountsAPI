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


public class GLAccounts {

	private String API;
	private Map<String, String> formData;
	private HttpPost post;
	private HttpEntity entity;
	HttpResponse API_Response;
	private String  selectedAccounts;
	Map<String, BigDecimal> GLAccountBalances;
	private String recentPeriod;
	private String year;

	public GLAccounts(String API, String selectedAccounts, String recentPeriod, String year) {
		this.API = API;
		this.selectedAccounts = selectedAccounts;
		this.recentPeriod = recentPeriod;
		this.year = year;
	}

	private Map<String, String> getAPIConstants () {
		formData = new HashMap<>();
		formData.put("coAccountListIds", selectedAccounts);
		formData.put("periodsId", recentPeriod);
		formData.put("periodsToID", recentPeriod);	
		formData.put("yearID", year);
		formData.put("isAccountRange", "true");
		formData.put("_search", "false");
		formData.put("rows", "1000");
		formData.put("page", "1");
		return formData;
	}

	public void executePost() {
		post = new HttpPost(API);
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
		JSONArray array = new JSONArray();
		GLAccountBalances = new HashMap<String, BigDecimal>();
		try {
			BufferedReader rd = new BufferedReader(new InputStreamReader(API_Response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
				//Parse our JSON response                           
				JSONObject responseJSON = (JSONObject)parser.parse(line);
				array = (JSONArray) responseJSON.get("rows");

				for(Object obj: array) {
					JSONObject o2 = (JSONObject) obj;
					checkAccType(o2);
				}
			}
		}

		catch (IOException | ParseException e) {
			e.printStackTrace();
		}
	}

	private void checkAccType(JSONObject o2) { 
		
		String accName = o2.get("coAccountDesc").toString();
		
		if(accName.toLowerCase().contains("receivable") || accName.toLowerCase().contains("receiveable")) {
			GLAccountBalances.put( "Accounts Receivable", convertbalance((String)o2.get("yBalance")) );
		}
		
		else if ( accName.toLowerCase().contains("payable") ) {
			GLAccountBalances.put( "Accounts Payable", convertbalance((String)o2.get("yBalance")) );
		}
		
		else if ( accName.toLowerCase().contains("inventory") ) {
			GLAccountBalances.put( "Inventory", convertbalance((String)o2.get("yBalance")) );
		}
	}

	public Map<String, BigDecimal> getGLAccountBalances() { 
		return GLAccountBalances;
	}
	
	private BigDecimal convertbalance(String accountBalance) {
		accountBalance = accountBalance.substring(0, accountBalance.length()-3);
		BigDecimal account_Balance = new BigDecimal(accountBalance);
		return account_Balance;
	}

}
