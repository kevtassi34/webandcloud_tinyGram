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

@WebServlet(name = "PrefixServlet", urlPatterns = { "/prefix" })
public class PrefixServlet extends HttpServlet {

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

		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

		
		LocalDate start = LocalDate.of(2019, Month.OCTOBER, 14);
		LocalDate end = LocalDate.now();

		
		HashSet<String> commentaires = new HashSet<String>();
		// Create posts
		//for (int i = 1; i < 100; i++) {
			for (int j=1;j<5;j++) {
				LocalDate rdate = this.between(start, end);
				
				Entity e = new Entity("Post", Long.MAX_VALUE-(new Date()).getTime());
				e.setProperty("body", "Commentaire de: blabla" + rdate.toString());
				e.setProperty("url", "http://cochyse30.c.o.pic.centerblog.net/o/2f9a74bd.jpg");
				e.setProperty("owner", "last " + j);
				e.setProperty("date",new Date());
				e.setProperty("comments", commentaires);

				// Create user friends
//				HashSet<String> toset = new HashSet<String>();
//				while (toset.size() < 5) {
//					toset.add("last " + r.nextInt(100));
//				}
//				e.setProperty("Followers", toset);

				HashSet<String> like = new HashSet<String>();
				while (like.size() < 5) {
					like.add("last " + (r.nextInt(9) + 1));
				}
				e.setProperty("likeur", like);
				e.setProperty("likec",like.size());
				
				datastore.put(e);
				response.getWriter().print("<li> created post instagram:" + e.getKey() + "<br>" + like + "<br>");
			}

		//ssssssss}
	}
}