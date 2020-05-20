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

@WebServlet(name = "Unfollow", urlPatterns = { "/unfollow/*" })
public class Unfollow extends HttpServlet {

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		
		//String url = "";
		StringBuffer post = request.getRequestURL();
		
		//response.getWriter().print("identifiant Url : " + post);
		//String res = post.substring(31); // En Local
		String res = post.substring(45); // En déploiement
		
		//http://localhost:8080/unfollow/4602555673870336
		//36/https://tinygram-272817.appspot.com/
		//22/http://localhost:8080/
		
		long dp = Long.parseLong(res);
		
		Boolean trouve = false;
		Boolean find = false;
		String firstName = ""; 
		String lastName = "";
		
		String firstNameF = "";
		String lastNameF = "";
		
		

		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		
		//Entity deletePost = new Entity("Abonnements", dp);
		
		Entity deletePost = new Entity("Friend", dp);
		
		
		Key keyPost = deletePost.getKey();

		
		
		foo.UsersServlet connect = new foo.UsersServlet();
		
		String user = connect.getNicknameUser();
		
		response.getWriter().print( "<br><li> User : " + user + "<br><br>");
		//Query q = new Query("Friend").setFilter(new FilterPredicate("Followers", FilterOperator.EQUAL, user));
		Query q = new Query("Friend");
		PreparedQuery pq = datastore.prepare(q);
		List<Entity> result = pq.asList(FetchOptions.Builder.withDefaults());
		
		
		for (Entity entity : result) {
			if (entity.getKey().equals(keyPost)) {
				
				ArrayList<String> Followers = (ArrayList<String>) entity.getProperty("Followers");
				firstName = (String) entity.getProperty("firstName");
				lastName = (String) entity.getProperty("lastName");
				Long age = (Long) entity.getProperty("age");
				
				for(int i = 0; i< Followers.size(); i++) {
					if (Followers.get(i).equals(user)) { // si l'utilisateur est déjà abonné
						trouve = true;	
						Followers.remove(i);
					}
				}
			
				
				
				  if (trouve) {
				  
				  Entity personne = new Entity("Friend", dp);
				  
				  personne.setProperty("firstName", firstName);
				  personne.setProperty("lastName", lastName); personne.setProperty("age",age);
				  personne.setProperty("NbFollowers", Followers.size());
				  personne.setProperty("Followers", Followers);
				  
				  datastore.put(personne);
				 
				  response.getWriter().print( "<h3>" + lastName + " a été supprimé de votre liste d'abonnements :-(" + "</h3>");	

					}
				
				
			}
		}
		
		
		
		// response.sendRedirect("/paper-dashboard-master/abonnementsPage.jsp");
		response.getWriter()
		.println( "<p> <a href=/paper-dashboard-master/abonnementsPage.jsp> clique Ici pour revenir à la page précédente </a> </p>");
	}
}
