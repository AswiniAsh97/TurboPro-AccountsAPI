package AccountDetail;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import Environment.Bartos;


public class MainClass {
	private static CloseableHttpClient httpclient;

	public static void main (String args[]) {
		httpclient = HttpClients.createDefault();	
		}

	public static CloseableHttpClient getHttpClient() {
		return httpclient;
	}

}
