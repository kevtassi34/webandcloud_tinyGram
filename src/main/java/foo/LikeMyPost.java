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

@WebServlet(name = "LikeMyPost", urlPatterns = { "/likemypost/*" })
public class LikeMyPost extends HttpServlet {

	
	@SuppressWarnings("unchecked")
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");

		StringBuffer post = request.getRequestURL();
		
		//String res = post.substring(33); // En local
		String res = post.substring(47); // En déploiement
		
		//http://localhost:8080/likemypost/5383208929591296
		//36/https://tinygram-272817.appspot.com/
		//22/http://localhost:8080/
		
		Boolean trouve = false;
	
		long lp = Long.parseLong(res);
		
		Date date = null;
		Long nblike = Long.parseLong("0");
		String body = "";
		String image = "";
		String owner = "";
		Long idPost = (long) 0 ;
	

		ArrayList<String> likeurbis = new ArrayList<String>();
		ArrayList<String> Followersbis = new ArrayList<String>();
		
				
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

		Entity likePost = new Entity("Post", lp);
		Key keyPost = likePost.getKey();
		
		foo.UsersServlet connect = new foo.UsersServlet();
		
		String user = connect.getNicknameUser();
		response.getWriter().print( "<br><li> Utilisateur: " + user + "");
		
		//Entity Personne = new Entity("friend", user);
		
		//Query p = new Query("Friend").setFilter(new FilterPredicate("lastName", FilterOperator.EQUAL, user));
				Query q = new Query("Post");
				PreparedQuery pqs = datastore.prepare(q);
				List<Entity> resultat = pqs.asList(FetchOptions.Builder.withDefaults());
				
				//response.getWriter().print( "<li>" + keyPost.getId()+ " key post getname " + "<br>");
				//response.getWriter().print( "<li>" + keyPost + " key post " + "<br><br>");
				
				Entity e = new Entity("Post", lp);	
		
				for (Entity entity : resultat) {
					
					if (entity.getKey().equals(keyPost)) {   // si la clé du post est trouvée
				
				//response.getWriter().print( "<li>" + entity.getKey() + " entity.getKey " + "<br><br>");
				
				ArrayList<String> likeur = (ArrayList<String>) entity.getProperty("likeur");
				ArrayList<String> Followers = (ArrayList<String>) entity.getProperty("Followers");
				ArrayList<String> comments = (ArrayList<String>) entity.getProperty("comments");
				//response.getWriter().print( "<br><br><li> contenu du tableau des likeurs: " + likeur + "<br>");
			
				//on récupère le contenu des différentes colonnes
				nblike = (Long) entity.getProperty("likec");
				date = (Date) entity.getProperty("date");
				body = (String) entity.getProperty("body");
				image = (String) entity.getProperty("url");
				owner = (String) entity.getProperty("owner");
				idPost = (Long) entity.getProperty("idPost");
	
			if ( likeur == null) { //si le tableur des likeur est null
				
				//response.getWriter().print( "<br><br><li>Contenu du tableau des likeur null " + likeur + "<br>");
					
				Query p = new Query("Post");
				
				PreparedQuery pq = datastore.prepare(p);
				List<Entity> result = pq.asList(FetchOptions.Builder.withDefaults());
				for (Entity entityy : result) {
					if(entityy.getKey().equals(keyPost)) {
				
						nblike = (Long) entityy.getProperty("likec");
						nblike = nblike + 1 ;
						date = (Date) entityy.getProperty("date");
						body = (String) entityy.getProperty("body");
						image = (String) entityy.getProperty("url");
						owner = (String) entityy.getProperty("owner");
						likeur = (ArrayList<String>) entity.getProperty("likeur");
						Followers = (ArrayList<String>) entity.getProperty("Followers");
						comments = (ArrayList<String>) entity.getProperty("comments");
						idPost = (Long) entity.getProperty("idPost");
					
						//response.getWriter().print( "<br><br><li> les likeur Si c'est null: " + likeur + "<br>");
					}
				}
				
				// on ajoute le nom de l'utilisateur dans un autre tableau 
				likeurbis.add(user);
				
			//response.getWriter().print( "<br><br><li> Contenu du tableau likeurbis: " + likeurbis + "<br>");
				
			e.setProperty("body", body);
			e.setProperty("date", date);
			e.setProperty("likeur", likeurbis);
			e.setProperty("likec", nblike);
			e.setProperty("owner", owner);
			e.setProperty("url", image);
			e.setProperty("Followers", Followers);
			e.setProperty("NbFollowers", Followers.size());
			e.setProperty("comments", comments);
			e.setProperty("idPost", idPost);
			
			datastore.put(e);
			
			response.getWriter().print( "<h3><li> Le " + keyPost + " a été Liké :) </h3>");
						
						
					}
			
			else { // si le tableau des likeur n'est pas vide
				
				//response.getWriter().print( "<br><br><li>Tableau pas vide " + entity.getProperty("likeur") + "<br>");
				//response.getWriter().print( "<br><br><li>Taille du tab likeur" + likeur.size() + "<br>");
				
				for(int i = 0; i< likeur.size(); i++) {
					if (likeur.get(i).equals(user)) { // si l'utilisateur a déjà liké, on le retire du tableau likeur
						trouve = true;
						likeur.remove(i);
					
						//response.getWriter().print( "<br><br><li>On a remove le likeur: "+ likeur.get(i) + "<br>");
					}
			
				}
				
				if (trouve) { // si trouve = vrai
					
					//response.getWriter().print( "<br><br><li> les likeur après le unlike: " + likeur + "<br>");
					
					
					Query p = new Query("Post");
					
					PreparedQuery pq = datastore.prepare(p);
					List<Entity> result = pq.asList(FetchOptions.Builder.withDefaults());
					for (Entity entityy : result) {
						if(entityy.getKey().equals(keyPost)) {
					
							nblike = (Long) entityy.getProperty("likec");
							nblike = nblike - 1 ;
							date = (Date) entityy.getProperty("date");
							body = (String) entityy.getProperty("body");
							image = (String) entityy.getProperty("url");
							owner = (String) entityy.getProperty("owner");
							Followers = (ArrayList<String>) entity.getProperty("Followers");
							comments = (ArrayList<String>) entity.getProperty("comments");
							idPost = (Long) entity.getProperty("idPost");
						}
					}
				
				
				e.setProperty("body", body);
				e.setProperty("date", date);
				e.setProperty("likeur", likeur);
				e.setProperty("likec", nblike);
				e.setProperty("owner", owner);
				e.setProperty("url", image);
				e.setProperty("Followers", Followers);
				e.setProperty("NbFollowers", Followers.size());
				e.setProperty("comments", comments);
				e.setProperty("idPost", idPost);
				
				datastore.put(e);
				
				response.getWriter().print( "<h3><li> Le " + keyPost + " a été UnLiké :( </h3>");
				response.getWriter().print( "<h3> Car Vous avez déjà liké ce post </h3>");
				
			}
				else { // si trouve = false, cad que l'utilisateur n'a pas encore liké le post
				
					//response.getWriter().print( "<br><br><li> les likeur avant le nouveau like: " + likeur + "<br>");
					Query p = new Query("Post");
					
					PreparedQuery pq = datastore.prepare(p);
					List<Entity> result = pq.asList(FetchOptions.Builder.withDefaults());
					for (Entity entityy : result) {
						if(entityy.getKey().equals(keyPost)) {
					
							nblike = (Long) entityy.getProperty("likec");
							nblike = nblike + 1 ;
							date = (Date) entityy.getProperty("date");
							body = (String) entityy.getProperty("body");
							image = (String) entityy.getProperty("url");
							owner = (String) entityy.getProperty("owner");
							Followers = (ArrayList<String>) entity.getProperty("Followers");
							comments = (ArrayList<String>) entity.getProperty("comments");
							idPost = (Long) entity.getProperty("idPost");
							likeur.add(user);
						}
					}
						
				//response.getWriter().print( "<br><br><li> les likeur après le nouveau like: " + likeur + "<br>");
					
				e.setProperty("body", body);
				e.setProperty("date", date);
				e.setProperty("likeur", likeur);
				e.setProperty("likec", nblike);
				e.setProperty("owner", owner);
				e.setProperty("url", image);
				e.setProperty("Followers", Followers);
				e.setProperty("NbFollowers", Followers.size());
				e.setProperty("comments", comments);
				e.setProperty("idPost", idPost);
				
				datastore.put(e);
				
				response.getWriter().print( "<h3><li> Le " + keyPost + " a été Liké :) </h3>" + "<br>");
				
				}
				
			}
		
		}
	}
				response.sendRedirect("/paper-dashboard-master/mainActu.jsp");
//	response.getWriter()
//	.println( "<p> <a href=/paper-dashboard-master/mainActu.jsp> clique Ici pour revenir à la page précédente </a> </p>");
	
  }
}