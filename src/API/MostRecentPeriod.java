package API;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import AccountDetail.MainClass;

public class MostRecentPeriod {

	private HttpGet get;
	HttpResponse API_Response;
	private String recentPeriod;
	private String API_URL;
	private Map<String, String> period_Year;

	
	public MostRecentPeriod (String API_URL) {
		this.API_URL = API_URL;
	}

	public void executeGet() {
		get = new HttpGet(API_URL);
		try {
			API_Response = MainClass.getHttpClient().execute(get);
		} catch (IOException e) {
			e.printStackTrace();
		}
		parseResponse();		 
	}

	public void parseResponse() {

	
			BufferedReader rd = null;
			try {
				rd = new BufferedReader(new InputStreamReader(API_Response.getEntity().getContent()));
			} catch (UnsupportedOperationException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String line = "";
			try {
				while ((line = rd.readLine()) != null) {
					//Parse our JSON response                           
					recentPeriod = line;
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
			
			findRecentPeriod(recentPeriod);
	}
	
	private void findRecentPeriod(String recentPeriod) {
		 
		Pattern pattern = Pattern.compile("--");     
		Matcher matcher = pattern.matcher(recentPeriod); 
		period_Year = new HashMap<>();
		while (matcher.find()) {    
			int end = matcher.end();
			
			period_Year.put("period", recentPeriod.substring(end));
			period_Year.put("year", ( recentPeriod.substring(0, (end-2) ) ).trim());
			
		}    
	}
	
	public Map<String, String> getPeriodAndYear() {
		return period_Year;
	}
	
}
