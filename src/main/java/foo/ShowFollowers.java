package foo;

import java.io.IOException;
import java.util.ArrayList;
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

@WebServlet(name = "ShowFollowers", urlPatterns = { "/showfollowers/*" })
public class ShowFollowers extends HttpServlet {

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");


        StringBuffer post = request.getRequestURL();
		
		//String res = post.substring(36); // En local
		String res = post.substring(50); // En déploiement
		
		//http://localhost:8080/likemypost/5383208929591296
		//36/https://tinygram-272817.appspot.com/
		//22/http://localhost:8080/
		

	
		long lp = Long.parseLong(res);
		String firstName = "";
		String lastName ="";
		ArrayList<String> FollowersNull  = new ArrayList<String>();


				

		Entity Friendtag = new Entity("Friend", lp);
		Key keyPost = Friendtag.getKey();
		
		foo.UsersServlet connect = new foo.UsersServlet();
		
		String user = connect.getNicknameUser();
		//response.getWriter().print( "<br><li> User: " + user + "");
		
		
		
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

		Query q = new Query("Friend");
		PreparedQuery pq = datastore.prepare(q);
		List<Entity> result = pq.asList(FetchOptions.Builder.withDefaults());
		
		
		for (Entity entity : result) {
			if (entity.getKey().equals(keyPost)) {
			
			ArrayList<String> Followers = (ArrayList<String>) entity.getProperty("Followers");	
			firstName = (String) entity.getProperty("firstName");
			lastName = (String) entity.getProperty("lastName");
			Long age = (Long) entity.getProperty("age");
			
			
			
			if (lastName.equals(user)) { 
				
				if (Followers == null) {
				
					
				response.getWriter().print( "<br><h2><li>" + user +" Vous avez " + FollowersNull.size() + " Abonné. :-( </h2><br><br>");
				//response.getWriter().print( "<li> les voici " + Followers + " Abonnés <br><br>");
				
				}
				else {
					response.getWriter()
					.println( "<p><h3> <a href=/paper-dashboard-master/usersPage.jsp> Cliquez ici pour revenir à la liste des Utilisateurs </a> </h3></p></br>");
					
					response.getWriter()
					.println( "<p><h3> <a href=/paper-dashboard-master/abonnementsPage.jsp> Cliquez ici pour revenir à la liste de vos abonnements </a> </h3></p></br>");
					 //response.getWriter().print( "<h2> Vous avez " + Followers.size() + " Abonnés </h2>");
					
					for(int i = 0; i< Followers.size(); i++) {
						   // si l'utilisateur a déjà liké, on le retire du tableau likeur
						
						 response.getWriter().print( "<h3><li>" + Followers.get(i) + " est Abonné à vous :-) </h3>");
					}
					
					
					
					
				}
				
			}	
			
			else {
				
				if (Followers == null) {
				
					
				response.getWriter().print( "<br><h2><li>" + lastName +" a " + FollowersNull.size() + " Abonné. :-( </h2><br><br>");
				//response.getWriter().print( "<li> les voici " + Followers + " Abonnés <br><br>");
				
				}
				else {
					response.getWriter()
					.println( "<p><h3> <a href=/paper-dashboard-master/usersPage.jsp> Cliquez ici pour revenir à la liste des Utilisateurs </a> </h3></p></br>");
					
					response.getWriter()
					.println( "<p><h3> <a href=/paper-dashboard-master/abonnementsPage.jsp> Cliquez ici pour revenir à la liste de vos abonnements </a> </h3></p></br>");
					
					 //response.getWriter().print( "<h2> Vous avez " + Followers.size() + " Abonnés </h2>");
					
					for(int i = 0; i< Followers.size(); i++) {
						   // si l'utilisateur a déjà liké, on le retire du tableau likeur
						
						 response.getWriter().print( "<h3><li>" + Followers.get(i) + " est Abonné à " + lastName + ". </h3>");
					}
			
				}
			}
			
		}	

	}
		
		response.getWriter()
		.println( "<p><h3> <a href=/paper-dashboard-master/usersPage.jsp> Cliquez ici pour revenir à la liste des Utilisateurs </a> </h3></p>");
		
		response.getWriter()
		.println( "<p><h3> <a href=/paper-dashboard-master/abonnementsPage.jsp> Cliquez ici pour revenir à la liste de vos abonnements </a> </h3></p></br>");
		
		
		//response.sendRedirect("/paper-dashboard-master/abonnementsPage.jsp");
	}
	
	
}
