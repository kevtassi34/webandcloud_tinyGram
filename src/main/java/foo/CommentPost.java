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

@WebServlet(name = "CommentPost", urlPatterns = { "/commentpost/*" })
public class CommentPost extends HttpServlet {

	
	@SuppressWarnings("unchecked")
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");

		StringBuffer post = request.getRequestURL();
		
		
		
		//String res = post.substring(34); // En local
		String res = post.substring(48); // En déploiement
		
		//String resbis = post.substring(34, 53); // id post
		//String rester = post.substring(53); // commentaire
		
		
		String resbis = post.substring(48, 67); // id post  en deploiement
		String rester = post.substring(67); // commentaire en deploiment
		
		
		//http://localhost:8080/likemypost/5383208929591296
		//36/https://tinygram-272817.appspot.com/
		//22/http://localhost:8080/
		
		//http://localhost:8080/commentpost/9223370447097248190Commentaire%20de%20kevtassi34%20:%20Ahahahah%20mdr%20mec
		
		Boolean trouve = false;
	
		long lp = Long.parseLong(resbis);
		
		Date date = null;
		Long nblike = Long.parseLong("0");
		String body = "";
		String image = "";
		String owner = "";
		ArrayList<String> commentairesAdd = new ArrayList<String>();

//		response.getWriter().print( "<li>" + post+ " : Url post commentiare " + "<br>");
//		response.getWriter().print( "<li>" + res + " : Res " + "<br><br>");
//		response.getWriter().print( "<li>" + rester + " : Resbis " + "<br><br>");
//		response.getWriter().print( "<li>" + resbis + " : Resbis " + "<br><br>");
//		response.getWriter().print( "<li>" + lp + " : lp " + "<br><br>");
		
		
		//ArrayList<String> likeurbis = new ArrayList<String>();
		
		
				
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

		Entity ComPost = new Entity("Post", lp);
		Key keyPost = ComPost.getKey();
		
	//	response.getWriter().print( "<li>" + keyPost + " key post " + "<br><br>");
		
		
		
		foo.UsersServlet connect = new foo.UsersServlet();
		
		String user = connect.getNicknameUser();
		response.getWriter().print( "<br><li> User: " + user + "");
		
		//Entity Personne = new Entity("friend", user);
		
		//Query p = new Query("Friend").setFilter(new FilterPredicate("lastName", FilterOperator.EQUAL, user));
				Query q = new Query("Post");
				PreparedQuery pqs = datastore.prepare(q);
				List<Entity> resultat = pqs.asList(FetchOptions.Builder.withDefaults());
				
				//response.getWriter().print( "<li>" + keyPost.getId()+ " key post getname " + "<br>");
				//response.getWriter().print( "<li>" + keyPost + " key post " + "<br><br>");
			
				Entity e = new Entity("Post", lp);	
		
				for (Entity entity : resultat) {
					
					if (entity.getProperty("idPost").equals(lp)) {   // si la clé du post est trouvée
				
				ArrayList<String> FollowersPost = (ArrayList<String>) entity.getProperty("Followers");
				ArrayList<String> likeur = (ArrayList<String>) entity.getProperty("likeur");
				ArrayList<String> commentaires = (ArrayList<String>) entity.getProperty("comments");
				
			
				//on récupère le contenu des différentes colonnes
				nblike = (Long) entity.getProperty("likec");
				date = (Date) entity.getProperty("date");
				image = (String) entity.getProperty("url");
				owner = (String) entity.getProperty("owner");
				body = (String) entity.getProperty("body");
				
					if (commentaires == null || FollowersPost == null ) {
					
						String str = "*****<br>Commentaire de " + user + " : " + rester + "<br>*****";
						 str = str.replaceAll("%20", " ");
						
						
						commentairesAdd.add(str);
						//response.getWriter().print( "<h3><li> Le contenu de commentairesAdd " + commentairesAdd + "<br><br>");
	
					  e.setProperty("body", body); 
					  e.setProperty("comments", commentairesAdd); 
					  e.setProperty("date", date);
					  e.setProperty("likeur", likeur); 
					  e.setProperty("likec", nblike);
					  e.setProperty("owner", owner); 
					  e.setProperty("url", image);
					  e.setProperty("Followers", FollowersPost);
					  e.setProperty("NbFollowers", FollowersPost);
					  e.setProperty("idPost", lp);
					 
					  datastore.put(e);
				
					  response.getWriter().print( "<h3><li> Le " + entity.getKey() + " a été Commenté :) </h3>" + "<br>");
	
					}
					
					else {
						String str = "*****<br>Commentaire de " + user + " : " + rester + "<br>*****";
						 str = str.replaceAll("%20", " ");
						
						commentaires.add(str);
						//response.getWriter().print( "<h3><li> Le contenu de commentaire " + str + "<br><br>");
						
						  e.setProperty("body", body); 
						  e.setProperty("comments", commentaires); 
						  e.setProperty("date", date);
						  e.setProperty("likeur", likeur); 
						  e.setProperty("likec", nblike);
						  e.setProperty("owner", owner); 
						  e.setProperty("url", image);
						  e.setProperty("Followers", FollowersPost);
						  e.setProperty("NbFollowers", FollowersPost);
						  e.setProperty("idPost", lp);
						 
						  datastore.put(e);
					
						  response.getWriter().print( "<h3><li> Le vous avez Commenté le post. :) </h3>" + "<br>");
					}
		}
	}
		
				
				response.sendRedirect("/paper-dashboard-master/mainActu.jsp");
//	response.getWriter()
//	.println( "<p> <a href=/paper-dashboard-master/mainActu.jsp> clique Ici pour revenir à la page actu </a> </p>");
//	
  }
}