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

@WebServlet(name = "LikePost", urlPatterns = { "/likepost/*" })
public class LikePost extends HttpServlet {

	
	@SuppressWarnings("unchecked")
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");

		StringBuffer post = request.getRequestURL();
		String res = post.substring(31);
		//http://localhost:8080/likepost/6403555720167424
		Boolean trouve = false;
		long lp = Long.parseLong(res);
		
		Date date = null;
		Long nb_like = Long.parseLong("0");
		String body = "";
		String image = "";
		
		ArrayList<String> ami = new ArrayList<String>();
		String prenom = "";
		String nom = "";
		ArrayList<String> likes = new ArrayList<String>();
		ArrayList<String> likesbis = new ArrayList<String>();
		Long age = Long.parseLong("0");
		
				
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

		Entity likePost = new Entity("Post", lp);
		Key keyPost = likePost.getKey();
		
		foo.UsersServlet connect = new foo.UsersServlet();
		
		String user = connect.getNicknameUser();
		
		
		Entity Personne = new Entity("friend", user);
		
		Query p = new Query("Friend").setFilter(new FilterPredicate("lastName", FilterOperator.EQUAL, user));
				//Query q = new Query("Post");
				PreparedQuery pqs = datastore.prepare(p);
				List<Entity> resultat = pqs.asList(FetchOptions.Builder.withDefaults());
				
		for (Entity entity : resultat) {
			age = (Long) entity.getProperty("age");
			prenom =(String) entity.getProperty("firstName");
			nom = (String) entity.getProperty("lastName");
			ami = (ArrayList<String>) entity.getProperty("friends");
			likes = (ArrayList<String>) entity.getProperty("LikePost");
			if (likes != null) {
				for(int i = 0; i< likes.size(); i++) {
					if (likes.get(i).equals(res)) {
						trouve = true;
						likes.remove(i);
					}
				}
			}
			
			if (trouve) {
				
				Query q = new Query("Post");
				
				PreparedQuery pq = datastore.prepare(q);
				List<Entity> result = pq.asList(FetchOptions.Builder.withDefaults());
				for (Entity entityy : result) {
					if(entityy.getKey().equals(keyPost)) {
						
						date = (Date) entityy.getProperty("date");
						nb_like = (Long) entityy.getProperty("likec");
						nb_like = nb_like - 1 ;
						body = (String) entityy.getProperty("body");
						image = (String) entityy.getProperty("url");
								
					}
					
				}
				Personne.setProperty("fisrtName", prenom);
				Personne.setProperty("lastName", nom);
				Personne.setProperty("age", age);
				Personne.setProperty("friends", ami);
				Personne.setProperty("LikePost", likes);
				
				datastore.put(Personne);
				
				likePost.setProperty("body", body);
				likePost.setProperty("date", date);
				likePost.setProperty("likec", nb_like);
				likePost.setProperty("owner", user);
				likePost.setProperty("url", image);
				
				datastore.put(likePost);
				
				response.getWriter().print( "<li> post deja liké " + "<br>");
			}
			else {
			

				Query q = new Query("Post");
				
				PreparedQuery pq = datastore.prepare(q);
				List<Entity> result = pq.asList(FetchOptions.Builder.withDefaults());
				for (Entity entityy : result) {
					if(entityy.getKey().equals(keyPost)) {
						
						date = (Date) entityy.getProperty("date");
						nb_like = (Long) entityy.getProperty("likec");
						nb_like = nb_like + 1 ;
						body = (String) entityy.getProperty("body");
						image = (String) entityy.getProperty("url");
					
					}
					
				}
				
				if (likes != null) {
					
					likes.add(res);
					Personne.setProperty("LikePost", likes);
				}
				else {
					likesbis.add(res);
					Personne.setProperty("LikePost", likesbis);
					
				}
				response.getWriter().print( "<br><br> tableau " + ami );
				response.getWriter().print( "<br><br> tableau " + likes );
				
				Personne.setProperty("fisrtName", prenom);
				Personne.setProperty("lastName", nom);
				Personne.setProperty("age", age);
				Personne.setProperty("friends", ami);
				//Personne.setProperty("LikePost", likes);
				
			
				
				likePost.setProperty("body", body);
				likePost.setProperty("date", date);
				likePost.setProperty("likec", nb_like);
				likePost.setProperty("owner", user);
				likePost.setProperty("url", image);
				
				datastore.put(likePost);
				datastore.put(Personne);
				response.getWriter().print( "<li> post a été liké " + "<br>");
			}
			
		}

		response.getWriter().print( "<br><br> tableau " + ami);

		
		}
		
}
