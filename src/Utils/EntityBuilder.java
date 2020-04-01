package Utils;

import java.util.Map;
import org.apache.http.HttpEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;

public class EntityBuilder {
	private HttpEntity entity;

	public EntityBuilder(Map<String, String> formData) {
		MultipartEntityBuilder builder = MultipartEntityBuilder.create();
		for(String key : formData.keySet()) {
			builder.addTextBody(key, formData.get(key));
		}
		entity = builder.build();
	}

	public HttpEntity getEntity() {
		return entity;	
	}
}