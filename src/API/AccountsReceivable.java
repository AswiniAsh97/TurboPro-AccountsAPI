package API;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import AccountDetail.MainClass;


public class AccountsReceivable {
	private URI uri;
	private HttpGet get;
	private HttpResponse API_Response;
	private BigDecimal accountsReceivable;
	private String host;
	private String path;
	private String scheme;
	

	public AccountsReceivable(String host, String path, String scheme) {
		this.host = host;
		this.path = path;
		this.scheme = scheme;
	}

	public void buildURI () {
		URIBuilder uriBuilder = new URIBuilder();
		uriBuilder.setScheme(scheme)
		.setHost(host)
		.setPath(path)
		.addParameter("tsUserLoginID", "0")
		.addParameter("customerID", "0")
		.addParameter("asofardate", getCurrentDate())
		.addParameter("_search", "false")
		.addParameter("rows", "100000")
		.addParameter("page" , "1")
		.addParameter("sidx" , "customerName")
		.addParameter("sord" , "asc");
		try {
			uri = uriBuilder.build();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}

	}

	public void executePost() {
		buildURI();
		get = new HttpGet(uri);
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
		JSONArray allInvoices = new JSONArray();
		accountsReceivable = new BigDecimal(0);
		try {
			BufferedReader rd = new BufferedReader(new InputStreamReader(API_Response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
				//Parse our JSON response                           
				JSONObject API_Response = (JSONObject)parser.parse(line);
				allInvoices = (JSONArray) API_Response.get("rows");

				for(Object invoice : allInvoices) {
					JSONObject invoiceDetail = (JSONObject) invoice;
					Double totalInvAmount = (Double) invoiceDetail.get("totalDaysAmt");
					BigDecimal TotalInvAmount = BigDecimal.valueOf(totalInvAmount.doubleValue());
					accountsReceivable = TotalInvAmount.add(accountsReceivable);
				}
			}
		}

		catch (IOException | ParseException e) {
			e.printStackTrace();
		}
	}

	public BigDecimal getAccountsReceivable() {
		return accountsReceivable;
	}

	public String getCurrentDate() {
		SimpleDateFormat formatter;
		Calendar cal; 
		
		cal = Calendar.getInstance();
		formatter = new SimpleDateFormat("MM/dd/yyyy");
		Date currentDate = cal.getTime();

		String current_Date = formatter.format(currentDate);
		return current_Date;
	}
}