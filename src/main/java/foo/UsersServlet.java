package foo;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;

//  see https://cloud.google.com/appengine/docs/standard/java/users

/* Copyright 2016 Google Inc.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

//[START users_API_example]


import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//With @WebServlet annotation the webapp/WEB-INF/web.xml is no longer required.
@WebServlet(
   name = "UserAPI",
   description = "UserAPI: Login / Logout with UserService",
   urlPatterns = "/userapi"
)
public class UsersServlet extends HttpServlet {

	 UserService userService = UserServiceFactory.getUserService();
 @Override
 public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
  

   String thisUrl = req.getRequestURI();

   resp.setContentType("text/html");
   if (req.getUserPrincipal() != null) {
     
	   
	   resp.getWriter()
	     .println( "<p><h1>Pour accéder à la page d'acceuil </h1> <a href=/paper-dashboard-master/mainActu.jsp> <h3>Cliquez ici </h3></a> </p><br>");
	     
	   
	   resp.getWriter()
         .println(
             "<p><h3>Hello, "
                 + req.getUserPrincipal().getName()
                 + "!  To log out <a href=\""
                 + userService.createLogoutURL(thisUrl)
                 + "\"> Click Here </a>.</h3></p>");
     
     
     
	   
	   Boolean newUser = true;

     DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
 	
     Entity e = new Entity("UsersConnected");
 	
		
	 	
	 	e.setProperty("userconnected", "admin");
	 	e.setProperty("date", new Date());
	 	
	 	
		datastore.put(e);
     
 	Query q = new Query("UsersConnected");
	PreparedQuery pq = datastore.prepare(q);
	List<Entity> result = pq.asList(FetchOptions.Builder.withDefaults());
		 	
			for (Entity entity : result) {
				
				String Userconnect = (String) entity.getProperty("userconnected");
				  
				
				if (req.getUserPrincipal().getName().equals(Userconnect)) {
					
					datastore.delete(entity.getKey());

				}
	
			 	
			 	e.setProperty("userconnected", req.getUserPrincipal().getName());
			 	e.setProperty("date", new Date());
			 	
			 	
				datastore.put(e);
				
			}
	
			
		 	Query p = new Query("Friend");
			PreparedQuery pqs = datastore.prepare(p);
			List<Entity> resultat = pqs.asList(FetchOptions.Builder.withDefaults());
			
			Entity F = new Entity("Friend");
			
			ArrayList<String> FollowersTab = new ArrayList<String>();
			String lastNameUserBis = "";
					
			for (Entity entityy : resultat) {
					
						String lastNameUser = (String) entityy.getProperty("lastName");
			
			  if(lastNameUser == null) {
					lastNameUserBis = userService.getCurrentUser().getNickname().toString();
						
					 FollowersTab.add(lastNameUserBis);
								
					F.setProperty("firstName", userService.getCurrentUser().getNickname().toString());
					F.setProperty("lastName", userService.getCurrentUser().getNickname().toString());
					F.setProperty("age", 25);
					F.setProperty("Followers", FollowersTab);
					F.setProperty("NbFollowers", FollowersTab.size());
		
					datastore.put(F);
					newUser=false;
				}
					
					
			else {
						
					
					if (lastNameUser.equals(userService.getCurrentUser().getNickname().toString())) {
							
						ArrayList<String> Followers = (ArrayList<String>) entityy.getProperty("Followers");
			
						if (Followers == null) {
							
							FollowersTab.add(userService.getCurrentUser().getNickname().toString());
							datastore.delete(entityy.getKey());
										
							F.setProperty("firstName", userService.getCurrentUser().getNickname().toString());
							F.setProperty("lastName", userService.getCurrentUser().getNickname().toString());
							F.setProperty("age", 25);
							F.setProperty("Followers", FollowersTab);
							F.setProperty("NbFollowers", FollowersTab.size());
				
							 datastore.put(F);
							 newUser=false;
							
	
						}
						else {
							
							datastore.delete(entityy.getKey());
							
							F.setProperty("firstName", userService.getCurrentUser().getNickname().toString());
							F.setProperty("lastName", userService.getCurrentUser().getNickname().toString());
							F.setProperty("age", 25);
							F.setProperty("Followers", Followers);
							F.setProperty("NbFollowers", Followers.size());
						
							datastore.put(F);
							
							newUser=false;
						
						}
					}
					
				else {
//						
//					FollowersTab.add(userService.getCurrentUser().getNickname().toString());
//					
//					F.setProperty("firstName", userService.getCurrentUser().getNickname().toString());
//					F.setProperty("lastName", userService.getCurrentUser().getNickname().toString());
//					F.setProperty("age", 25);
//					F.setProperty("Followers", FollowersTab);
//					F.setProperty("NbFollowers", FollowersTab.size());
//		
//					
//					 datastore.put(F);
//					 
//					// newUser=true;
//					 break;
				
					 }

			    }
		   
//			
//			     if (newUser) {
//					
//			    	 resp.getWriter()
//			         .println(
//			             "Il nya pas de user ");
//			  
//			    	break;
//				  
//			     }	
					
			}
			
			if (newUser) {
//			
//				resp.getWriter()
//		         .println(
//		             "Il nya pas de user ");
			
			FollowersTab.add(userService.getCurrentUser().getNickname().toString());
			
			F.setProperty("firstName", userService.getCurrentUser().getNickname().toString());
			F.setProperty("lastName", userService.getCurrentUser().getNickname().toString());
			F.setProperty("age", 25);
			F.setProperty("Followers", FollowersTab);
			F.setProperty("NbFollowers", FollowersTab.size());

			
			 datastore.put(F);
			
			}
			
   		}
   
   else {
     resp.getWriter()
         .println(
             "<p><h1>Please click here to <a href=\"" + userService.createLoginURL(thisUrl) + "\">sign in</a>.</h1></p>");
     
     resp.getWriter()
     .println( "<p> <a href=/index.html> Cliquez Ici pour revenir à la page d'acceuil </a> </p>");
   }
   
 
 }
 
 public String getNicknameUser() {
		return userService.getCurrentUser().getNickname().toString();
	}
 
 
}
//[END users_API_example]