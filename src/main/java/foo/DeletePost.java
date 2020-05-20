package foo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.KeyRange;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.PropertyProjection;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.CompositeFilterOperator;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;

import com.google.appengine.repackaged.com.google.datastore.v1.CompositeFilter;
import com.google.appengine.repackaged.com.google.datastore.v1.Projection;
import com.google.appengine.repackaged.com.google.datastore.v1.PropertyFilter;

@WebServlet(name = "DeletePost", urlPatterns = { "/deletepost/*" })
public class DeletePost extends HttpServlet {

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		
	
		StringBuffer post = request.getRequestURL();
		
		//response.getWriter().print("identifiant Url : " + post);
		
		//String res = post.substring(33);// En Local
		String res = post.substring(47);// En déploiement
		
		//http://localhost:8080/deletepost/6051711999279104
		//36/https://tinygram-272817.appspot.com/
		//22/http://localhost:8080/
		
		long dp = Long.parseLong(res);
		
		Date date = null;
		Long nblike = Long.parseLong("0");
		String body = "";
		String image = "";
		String owner = "";
		
		//response.getWriter().print("<br><br> identifiant Post : " + dp);

		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		
		Entity deletePost = new Entity("Post", dp);
		
		//response.getWriter().print("<br><br> clée deletepost: " + deletePost);
		
		Key keyPost = deletePost.getKey();

		//response.getWriter().print("<br><br> clée KEYPOST: " + keyPost);
		
		foo.UsersServlet connect = new foo.UsersServlet();
		
		String user = connect.getNicknameUser();
		
		response.getWriter().print( "<br><li> Utilisateur: " + user + "<br><br>");
		//Query q = new Query("Post").setFilter(new FilterPredicate("owner", FilterOperator.EQUAL, user));
		Query q = new Query("Post");
		PreparedQuery pq = datastore.prepare(q);
		List<Entity> result = pq.asList(FetchOptions.Builder.withDefaults());
		
		
		for (Entity entity : result) {
			if (entity.getKey().equals(keyPost)) {
			
				nblike = (Long) entity.getProperty("likec");
				date = (Date) entity.getProperty("date");
				body = (String) entity.getProperty("body");
				image = (String) entity.getProperty("url");
				owner = (String) entity.getProperty("owner");	
				
				//response.getWriter().print( "<li>" + owner + "  proprio " + "<br>");
				
				if (owner.equals(user)) { // si l'utilisateur est le propriétaire du post
			datastore.delete(entity.getKey());			
			response.getWriter().print( "<h3> Le " + entity.getKey()+ " a été supprimé " + "</h3>");
				}
				else {
					response.getWriter().print( "<h3>Impossible de supprimer ce post Car vous n'etes pas le propriétaire " + "</h3>");
				}
			}
		}
		
		response.sendRedirect("/paper-dashboard-master/MyProfile.jsp");
//		response.getWriter()
//		.println( "<p> <a href=/post.jsp> clique Ici pour revenir à la page précédente </a> </p>");
	}
}
