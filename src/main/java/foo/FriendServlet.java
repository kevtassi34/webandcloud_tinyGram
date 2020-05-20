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
import java.util.concurrent.ThreadLocalRandom;

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

@WebServlet(name = "FriendServlet", urlPatterns = { "/friends" })
public class FriendServlet extends HttpServlet {

	
	static Random r = new Random();

	
	public LocalDate between(LocalDate startInclusive, LocalDate endExclusive) {
	    long startEpochDay = startInclusive.toEpochDay();
	    long endEpochDay = endExclusive.toEpochDay();
	    long randomDay = ThreadLocalRandom
	    	      .current()
	    	      .nextLong(startEpochDay, endEpochDay);
	    return LocalDate.ofEpochDay(randomDay);
	}
	
	
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");

		Random r = new Random();
		

		LocalDate start = LocalDate.of(2019, Month.OCTOBER, 14);
		LocalDate end = LocalDate.now();
		
		//Entity e = new Entity("Friend");
		
		// Create users
		for (int i = 1; i < 101; i++) {
			//Entity e = new Entity("Friend","user" + i);
			Entity e = new Entity("Friend", i);
			
			HashSet<String> FollowersTab = new HashSet<String>();
			HashSet<String> PostTab = new HashSet<String>();
			//Long NbFollowers = (long) 100;
			while (FollowersTab.size() < 30) {
				FollowersTab.add("last " + (r.nextInt(100)+1));
				}
			
			
			e.setProperty("firstName", "first " + i);
			e.setProperty("lastName", "last " + i);
			e.setProperty("age",r.nextInt(50) + 10);
			e.setProperty("NbFollowers", FollowersTab.size());
			e.setProperty("Followers", FollowersTab);



			DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
			datastore.put(e);

			
			
			// Create user Posts
			HashSet<String> commentaires = new HashSet<String>();
			
			LocalDate rdate = this.between(start, end);
			
			Entity p = new Entity("Post", Long.MAX_VALUE-(new Date()).getTime());
			
			p.setProperty("idPost", Long.MAX_VALUE-(new Date()).getTime());
			p.setProperty("body", "Commentaire de: blabla" + rdate.toString());
			p.setProperty("url", "http://cochyse30.c.o.pic.centerblog.net/o/ee3cee0e.jpg");
			p.setProperty("owner", "last " + i);
			p.setProperty("date",new Date());
			p.setProperty("comments", commentaires);


			HashSet<String> like = new HashSet<String>();
			while (like.size() < 10) {
				like.add("last " + (r.nextInt(100) + 1));
			}
			
			p.setProperty("likeur", like);
			p.setProperty("likec",like.size());
			
			
			HashSet<String> Followers = new HashSet<String>();
			while (Followers.size() < 30) {
				Followers.add("last " + (r.nextInt(50)+1));
			}
			p.setProperty("Followers", Followers);
			p.setProperty("NbFollowers", Followers.size());
			
			
			datastore.put(p);
			response.getWriter()
		     .println( "<p> <a href=/index.html> <h2>Utilisateurs Ajoutés! cliquez ici pour revenir à la page d'acceuil et Connectez vous! :)</h2> </a> </p><br>");
			response.getWriter().print("<li> created post instagram:" + e.getKey() + "<br>" + like + "<br>");
			
			
			
			
		}
		
		
		response.getWriter()
	     .println( "<p> <a href=/index.html> <h2>Utilisateurs Ajoutés! cliquez ici pour revenir à la page d'acceuil et Connectez vous! :)</h2> </a> </p>");
	}
}