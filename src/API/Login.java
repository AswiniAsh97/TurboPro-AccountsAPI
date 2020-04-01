package API;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpPost;
import AccountDetail.MainClass;
import Utils.EntityBuilder;


public class Login {

	private String API_Url;
	private Map<String, String> formData;
	private EntityBuilder entitybuilder;
	private HttpPost post;
	private HttpEntity entity;

	private Map<String, String> getAPIConstants () {
		formData = new HashMap<>();
		formData.put("userName", "Admin");
		formData.put("password", "D3m0");
		formData.put("terminateOldSession", "false");
		return formData;
	}

	public Login(String API_Url) {
		
		this.API_Url = API_Url;
	}

	public void login() {
		post = new HttpPost(API_Url);
		entitybuilder = new EntityBuilder(getAPIConstants());
		entity = entitybuilder.getEntity();
		post.setEntity(entity);
		try {
			MainClass.getHttpClient().execute(post);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}



}
