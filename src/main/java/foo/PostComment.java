package foo;

import com.google.appengine.api.datastore.Key;
import java.util.Date;
import endpoints.repackaged.org.apache.http.client.utils.Idn;

public class PostComment {
	
	public String owner;
	public String body;
	public Date date;
	
	public PostComment() {}
}
