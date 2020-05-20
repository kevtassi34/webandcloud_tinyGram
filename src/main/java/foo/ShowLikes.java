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

@WebServlet(name = "ShowLikes", urlPatterns = { "/showlike/*" })
public class ShowLikes extends HttpServlet {

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");

		StringBuffer post = request.getRequestURL();
		
		//String res = post.substring(31); // En local
		String res = post.substring(45); // En déploiement
		
		//http://localhost:8080/likemypost/5383208929591296
		//36/https://tinygram-272817.appspot.com/
		//22/http://localhost:8080/
		

	
		long lp = Long.parseLong(res);
		


	

		ArrayList<String> likeurbis = new ArrayList<String>();
		
		
				
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

		Entity likePost = new Entity("Post", lp);
		Key keyPost = likePost.getKey();
		
		foo.UsersServlet connect = new foo.UsersServlet();
		
		String user = connect.getNicknameUser();
		response.getWriter().print( "<br><li> Utilisateur: " + user + "");
		
		//Query q = new Query("Friend").setFilter(new FilterPredicate("lastName", FilterOperator.EQUAL, user));
		Query q = new Query("Post");
		PreparedQuery pq = datastore.prepare(q);
		List<Entity> result = pq.asList(FetchOptions.Builder.withDefaults());
		
		
		for (Entity entity : result) {
			
			if (entity.getKey().equals(keyPost)) {
			
			ArrayList<String> likeur = (ArrayList<String>) entity.getProperty("likeur");	
			
				
				if(likeur == null) {
					
					response.getWriter().print( "<h2><li> Vous avez 0 like sur ce Post.  :-( </h2><br><br>");
				}
			
				else {
					response.getWriter()
					.println( "<p><h3> <a href=/paper-dashboard-master/MyProfile.jsp> Cliquez ici pour revenir sur votre Profil. </a> </h3></p>");
					
					response.getWriter().print( "<h2> Vous avez " + likeur.size() + " like sur ce Post. :-) </h2>");
					
					for(int i = 0; i< likeur.size(); i++) {
						   // si l'utilisateur a déjà liké, on le retire du tableau likeur
						
						 response.getWriter().print( "<h3><li>" + likeur.get(i) + " a liké votre Post. :-) </h3>");
					}
				}	
			
			}	
			
		
		}
		
		response.getWriter()
		.println( "<p><h3> <a href=/paper-dashboard-master/MyProfile.jsp> Cliquez ici pour revenir sur votre Profil. </a> </h3></p>");
		
		//response.sendRedirect("/paper-dashboard-master/abonnementsPage.jsp");
	}
	
	
}
