package Environment;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Map;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.io.File;
import API.AccountsPayable;
import API.AccountsReceivable;
import API.GLAccounts;
import API.Inventory;
import API.Login;
import API.MostRecentPeriod;

public class LiveEnvironment {
	private JSONObject envAPIProperties = null;
	private String[] ARurlParts;
	private String environment;

	protected LiveEnvironment(String environment) {
		this.environment = environment;
		this.getEnvAPIDetails();
		Login login = new Login(this.getLoginUrl());
		login.login();		
	}

	
	protected BigDecimal getAccountsReceivable() {
		getAccReceivableUrl();
		AccountsReceivable AR = new AccountsReceivable(ARurlParts[1], ARurlParts[2], ARurlParts[0]);
		AR.executePost();
		return AR.getAccountsReceivable();
	}

	
	protected BigDecimal getAccountsPayable() {
		AccountsPayable AP = new AccountsPayable(this.getAccPayableUrl());
		AP.executePost();
		return AP.getAccountsPayable();
	}

	protected Map<String, BigDecimal> getGLAccountBalances(String recentPeriod, String year) {
		GLAccounts GL = new GLAccounts(getGLAccUrl(), getGLAccJSONString(), recentPeriod, year);
		GL.executePost();
		Map<String, BigDecimal> GLAccountBalances = GL.getGLAccountBalances();
		return GLAccountBalances;
	}
	
	protected Double getInventoryValue() {
		Inventory inventory = new Inventory(getInvURL());
		inventory.executeGet();
		return inventory.getInventoryValue();
	}
	
	private String getInvURL() {
		return (String) envAPIProperties.get("InventoryURL");
	}
	
	protected Map<String, String> getPeriodAndYear() {
		MostRecentPeriod recentPeriod = new MostRecentPeriod(getMostRecentPeriodURL());
		recentPeriod.executeGet();
		return recentPeriod.getPeriodAndYear();
	}
	
	private String getMostRecentPeriodURL() {
		return (String) envAPIProperties.get("RecentPeriodURL");
	}

	private String getLoginUrl() {
		return (String) envAPIProperties.get("loginUrl");
	}

	private String getAccPayableUrl() {
		return (String) envAPIProperties.get("AccountsPayableUrl");
	}

	private void getAccReceivableUrl() {
		ARurlParts = new String[3];
		ARurlParts[0] = (String)((JSONObject)envAPIProperties.get("Accounts Receivable")).get("scheme");
		ARurlParts[1] = (String)((JSONObject)envAPIProperties.get("Accounts Receivable")).get("host");
		ARurlParts[2] = (String)((JSONObject)envAPIProperties.get("Accounts Receivable")).get("path");
	}

	private String getGLAccUrl() {
		return (String)((JSONObject)envAPIProperties.get("GLAccounts")).get("URL");
	}

	private String getGLAccJSONString() {
		JSONArray selectedAccounts = (JSONArray) ((JSONObject)envAPIProperties.get("GLAccounts")).get("coAccountListIds");
		return selectedAccounts.toJSONString();
	}

	private void  getEnvAPIDetails() {
		JSONParser jsonParser = new JSONParser();
		

		try(FileReader reader = new FileReader("resources"+File.separator+"RequestBody.json")){

			Object obj = jsonParser.parse(reader);
			JSONObject entireRequestBody = (JSONObject) obj;
			envAPIProperties =
					(JSONObject)((JSONObject) entireRequestBody.get("Environments")).get(environment);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.out.println("JSON File not found");
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
