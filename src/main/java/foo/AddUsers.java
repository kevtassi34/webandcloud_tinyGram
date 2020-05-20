package foo;

import java.io.IOException;
import java.time.LocalDate;
import java.time.Month;
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
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import  java.util.concurrent.ThreadLocalRandom;

@WebServlet(name = "AddUsers", urlPatterns = { "/adduser/*" })
public class AddUsers extends HttpServlet {

	
	@SuppressWarnings("(unchecked, null)")
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		
		StringBuffer post = request.getRequestURL();
		//String res = post.substring(30); // En local
		String res = post.substring(44); // En Déploiement
		
		//22/http://localhost:8080/adduser/4507997673881600
		//36/https://tinygram-272817.appspot.com/adduser/4648216125505536
		//36/https://tinygram-272817.appspot.com/
		//22/http://localhost:8080/
		
		Boolean trouve = false;
	
		long lp = Long.parseLong(res);
		
		
		
		String firstName = "";
		String lastName = "";
		//Integer age = 0;
		ArrayList<String> FollowersTab = new ArrayList<String>();
		String firstNameA = "";
		String lastNameA = "";
		
		String owner = "";
		String url = "";
		String body = "";
		//String comments = "";
		ArrayList<String> FollowersPostBis = new ArrayList<String>();
		Date date = new Date();
		
		
		
				
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

		Entity addF = new Entity("Friend", lp);
		Key keyFriend = addF.getKey();
		
		
		
		foo.UsersServlet connect = new foo.UsersServlet();
		
		String user = connect.getNicknameUser();
		response.getWriter().print( "<br><li> User: " + user + "<br>");
		
		Entity Personne = new Entity("Friend", lp);
		
		//Query p = new Query("Friend").setFilter(new FilterPredicate("lastName", FilterOperator.EQUAL, user));
				Query q = new Query("Friend");
				PreparedQuery pqs = datastore.prepare(q);
				List<Entity> resultat = pqs.asList(FetchOptions.Builder.withDefaults());
			
				
		for (Entity entity : resultat) {
					
					if (entity.getKey().equals(keyFriend)) { 
						
						
				ArrayList<String> Followers = (ArrayList<String>) entity.getProperty("Followers");	
				firstName = (String) entity.getProperty("firstName");
				lastName = (String) entity.getProperty("lastName");
				Long age = (Long) entity.getProperty("age");
				
				if (Followers == null) {
					
					FollowersTab.add(user);

					Personne.setProperty("firstName", firstName);
					Personne.setProperty("lastName", lastName);
					Personne.setProperty("age",age);
					Personne.setProperty("NbFollowers", FollowersTab.size());
					Personne.setProperty("Followers", FollowersTab);
					
					datastore.put(Personne);

					response.getWriter().print( "<br><br><h3> l'Utilisateur " + firstName +"_"+ lastName  + ", a été ajouté à vos Abonnements :-)" + "</h3><br>");
					
					
				}
				
			   
				else {
					
					for(int i = 0; i< Followers.size(); i++) {
						
						if (Followers.get(i).equals(user)) { // si l'utilisateur est déjà abonné
							trouve = true;
						
							
						}
				
					}
					
					if(trouve) {
						
						response.getWriter().print( "<br><br><h3> Vous êtes déjà Abonné à l'Utilisateur:  " + lastName + " :-)</h3><br>");
						 }
					
					else {
						
						
						
						Followers.add(user);
						
						
						Personne.setProperty("firstName", firstName);
						Personne.setProperty("lastName", lastName);
						Personne.setProperty("age",age);
						Personne.setProperty("NbFollowers", Followers.size());
						Personne.setProperty("Followers", Followers);
						
						datastore.put(Personne);
						
						
						
						Query p = new Query("Post").setFilter(new FilterPredicate("owner", FilterOperator.EQUAL, lastName));
						
						
						PreparedQuery pq = datastore.prepare(p);
						List<Entity> result = pq.asList(FetchOptions.Builder.withDefaults());
					
						
						
						for (Entity entityy : result) {
							
							Long idPost = (Long) entityy.getProperty("idPost");
							
						
							
							Entity e = new Entity("Post", idPost);
							
							@SuppressWarnings("unchecked")
							ArrayList<String> FollowersPost = (ArrayList<String>) entityy.getProperty("Followers");
							@SuppressWarnings("unchecked")
							ArrayList<String> likeurPost = (ArrayList<String>) entityy.getProperty("likeur");
							ArrayList<String> comments = (ArrayList<String>) entityy.getProperty("comments");
							
							
							
							owner = (String) entityy.getProperty("owner");
							url = (String) entityy.getProperty("url");
							body = (String) entityy.getProperty("body");
							
							date = (Date) entityy.getProperty("date");
							
						if (FollowersPost == null) {
							
//							FollowersPostBis.add(user);
//							
//							e.setProperty("owner", lastName);
//							e.setProperty("url", url);
//							e.setProperty("body", body);
//							e.setProperty("comments", comments);
//							e.setProperty("likeur", likeurPost);
//							e.setProperty("likec", likeurPost.size());
//							e.setProperty("date", date);
//							e.setProperty("NbFollowers", FollowersPostBis.size());
//							e.setProperty("Followers", FollowersPostBis);
//							e.setProperty("idPost", idPost);
//							
//							datastore.put(e);
							
						}
						else {
							
							FollowersPost.add(user);
							
							e.setProperty("owner", lastName);
							e.setProperty("url", url);
							e.setProperty("body", body);
							e.setProperty("comments", comments);
							e.setProperty("likeur", likeurPost);
							e.setProperty("likec", likeurPost.size());
							e.setProperty("date", date);
							e.setProperty("NbFollowers", FollowersPost.size());
							e.setProperty("Followers", FollowersPost);
							e.setProperty("idPost", idPost);
							
							datastore.put(e);
							
						}
						
						
						
							
					}
						
						response.getWriter().print( "<br><br><h3> l'Utilisateur " + lastName +"_"+ firstName + ", a été ajouté à vos Abonnements :-)" + "</h3><br>");
							
					}
							
				}	
				
	
		}
	}
		
	response.getWriter()
	.println( "<p><h3> <a href=/paper-dashboard-master/usersPage.jsp> Cliquez ici pour revenir à la liste des Utilisateurs </a> </h3></p>");
	
  }
}