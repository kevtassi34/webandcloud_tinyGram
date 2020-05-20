package foo;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import com.google.api.server.spi.auth.common.User;
import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiMethod.HttpMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.config.Named;
import com.google.api.server.spi.config.Nullable;
import com.google.api.server.spi.response.CollectionResponse;
import com.google.api.server.spi.response.UnauthorizedException;
import com.google.api.server.spi.auth.EspAuthenticator;

import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.PropertyProjection;
import com.google.appengine.api.datastore.PreparedQuery.TooManyResultsException;
import com.google.appengine.api.datastore.Query.CompositeFilter;
import com.google.appengine.api.datastore.Query.CompositeFilterOperator;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.appengine.api.datastore.QueryResultList;
import com.google.appengine.api.datastore.Transaction;

@Api(name = "myApi",
     version = "v1",
     audiences = "927375242383-t21v9ml38tkh2pr30m4hqiflkl3jfohl.apps.googleusercontent.com",
  	 clientIds = "927375242383-t21v9ml38tkh2pr30m4hqiflkl3jfohl.apps.googleusercontent.com",
     namespace =
     @ApiNamespace(
		   ownerDomain = "helloworld.example.com",
		   ownerName = "helloworld.example.com",
		   packagePath = "")
     )

public class ScoreEndpoint {

	Random r = new Random();

	@ApiMethod(name = "getRandom", httpMethod = HttpMethod.GET)
	public RandomResult random() {
		return new RandomResult(r.nextInt(6) + 1);
	}


	
	@ApiMethod(name = "userConnected", httpMethod = HttpMethod.POST)
	public Entity userconnected(PostMessage pm) {
		
		foo.UsersServlet connect = new foo.UsersServlet();
		String user = connect.getNicknameUser();
		Entity e = new Entity("UsersConnected");
		
		e.setProperty("userconnected", user);
		e.setProperty("body", pm.body);
		e.setProperty("date", new Date());

		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		Transaction txn = datastore.beginTransaction();
		datastore.put(e);
		txn.commit();
		return e;
	}
	
	
	@ApiMethod(name = "userC", httpMethod = HttpMethod.GET)
	public CollectionResponse<Entity> userC(@Nullable @Named("next") String cursorString) {
		                                    //@Named("name") String name,
	    //Query q = new Query("Post").setFilter(new FilterPredicate("owner", FilterOperator.EQUAL, name));
	    Query q = new Query("UsersConnected");

	    
	    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
	    PreparedQuery pq = datastore.prepare(q);
	    
	    FetchOptions fetchOptions = FetchOptions.Builder.withLimit(2);
	    
	    if (cursorString != null) {
		fetchOptions.startCursor(Cursor.fromWebSafeString(cursorString));
		}
	    
	    QueryResultList<Entity> results = pq.asQueryResultList(fetchOptions);
	    cursorString = results.getCursor().toWebSafeString();
	    
	    return CollectionResponse.<Entity>builder().setItems(results).setNextPageToken(cursorString).build();
	    
	}
	
	
	@ApiMethod(name = "postComment", httpMethod = HttpMethod.POST)
	public Entity postComment(PostComment pc) {
		
		HashSet<String> likeur = new HashSet<String>();
		
		foo.UsersServlet connect = new foo.UsersServlet();
		String user = connect.getNicknameUser();
		HashSet<String> commentaires = new HashSet<String>();
		//Long NbFollowers = (long) 0;
		//commentaires.add(pm.body);
		
		Entity e = new Entity("Comment");
		commentaires.add("Commentaire de " + user + " : " + pc.body);
	
		e.setProperty("owner", user);
		e.setProperty("body", commentaires);
		e.setProperty("date", new Date());
		

		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		Transaction txn = datastore.beginTransaction();
		datastore.put(e);
		txn.commit();
		return e;
	}
	
	
	
	@ApiMethod(name = "postMessage", httpMethod = HttpMethod.POST)
	public Entity postMessage(PostMessage pm) {
		
		HashSet<String> likeur = new HashSet<String>();
		
		foo.UsersServlet connect = new foo.UsersServlet();
		String user = connect.getNicknameUser();
		HashSet<String> commentaires = new HashSet<String>();
		//Long NbFollowers = (long) 0;
		//commentaires.add(pm.body);
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		
		Query q = new Query("Friend").setFilter(new FilterPredicate("lastName", FilterOperator.EQUAL, user));
		
		
		PreparedQuery pq = datastore.prepare(q);
		List<Entity> result = pq.asList(FetchOptions.Builder.withDefaults());
		Long idPost = Long.MAX_VALUE-(new Date()).getTime();
		
		Entity e = new Entity("Post", idPost);
		
		for (Entity entity : result) {
		
			@SuppressWarnings("unchecked")
			ArrayList<String> Followers = (ArrayList<String>) entity.getProperty("Followers");
			
			commentaires.add("Commentaire de " + user + " : " + pm.body);
			e.setProperty("idPost", idPost);
			e.setProperty("owner", user);
			e.setProperty("url", pm.url);
			e.setProperty("body", "Commentaire de " + user + " : " + pm.body);
			e.setProperty("comments", commentaires);
			e.setProperty("likeur", likeur);
			e.setProperty("likec", 0);
			e.setProperty("date", new Date());
			e.setProperty("NbFollowers", Followers.size());
			e.setProperty("Followers", Followers);
	     }
		//DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		Transaction txn = datastore.beginTransaction();
		datastore.put(e);
		txn.commit();
		return e;
	}

	
	
	@ApiMethod(name = "myprofile", httpMethod = HttpMethod.GET)
	public CollectionResponse<Entity> myprofile(@Nullable @Named("next") String cursorString) {
		                                    //@Named("name") String name,
	    //Query q = new Query("Post").setFilter(new FilterPredicate("owner", FilterOperator.EQUAL, name));
		foo.UsersServlet connect = new foo.UsersServlet();
		String user = connect.getNicknameUser();
		
		Query q = new Query("Post").setFilter(new FilterPredicate("owner", FilterOperator.EQUAL, user));

	    
	    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
	    PreparedQuery pq = datastore.prepare(q);
	    
	    FetchOptions fetchOptions = FetchOptions.Builder.withLimit(30);
	    //FetchOptions fetchOptions = FetchOptions.Builder.withDefaults();
	    
	    if (cursorString != null) {
		fetchOptions.startCursor(Cursor.fromWebSafeString(cursorString));
		}
	    
	    QueryResultList<Entity> results = pq.asQueryResultList(fetchOptions);
	    cursorString = results.getCursor().toWebSafeString();
	    
	    return CollectionResponse.<Entity>builder().setItems(results).setNextPageToken(cursorString).build();
	    
	}
	
	
	
	@ApiMethod(name = "mypost", httpMethod = HttpMethod.GET)
	public CollectionResponse<Entity> mypost(@Nullable @Named("next") String cursorString) {
		                                    //@Named("name") String name,
	    //Query q = new Query("Post").setFilter(new FilterPredicate("owner", FilterOperator.EQUAL, name));
	   //Query q = new Query("Post");
	    
		foo.UsersServlet connect = new foo.UsersServlet();
		String user = connect.getNicknameUser();
		Query q = new Query("Post").setFilter(new FilterPredicate("Followers", FilterOperator.EQUAL, user));
	    
	    
	    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
	    PreparedQuery pq = datastore.prepare(q);
	    
	    FetchOptions fetchOptions = FetchOptions.Builder.withLimit(30);
	    //FetchOptions fetchOptions = FetchOptions.Builder.withDefaults();
	    
	    if (cursorString != null) {
		fetchOptions.startCursor(Cursor.fromWebSafeString(cursorString));
		}
	    
	    QueryResultList<Entity> results = pq.asQueryResultList(fetchOptions);
	    cursorString = results.getCursor().toWebSafeString();
	    
	    return CollectionResponse.<Entity>builder().setItems(results).setNextPageToken(cursorString).build();
	    
	}
    
	@ApiMethod(name = "usersList", httpMethod = HttpMethod.GET)
	public CollectionResponse<Entity> usersList(@Nullable @Named("next") String cursorString) {
		                                    //@Named("name") String name,
	    //Query q = new Query("Post").setFilter(new FilterPredicate("owner", FilterOperator.EQUAL, name));
	    Query q = new Query("Friend");

	    
	    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
	    PreparedQuery pq = datastore.prepare(q);
	    
	    FetchOptions fetchOptions = FetchOptions.Builder.withLimit(30);
	   //FetchOptions fetchOptions = FetchOptions.Builder.withDefaults();
	    
	    if (cursorString != null) {
		fetchOptions.startCursor(Cursor.fromWebSafeString(cursorString));
		}
	    
	    QueryResultList<Entity> results = pq.asQueryResultList(fetchOptions);
	    cursorString = results.getCursor().toWebSafeString();
	    
	    return CollectionResponse.<Entity>builder().setItems(results).setNextPageToken(cursorString).build();
	    
	}
	
	
	@ApiMethod(name = "abonneList", httpMethod = HttpMethod.GET)
	public CollectionResponse<Entity> abonneList(@Nullable @Named("next") String cursorString) {
		                                    //@Named("name") String name,
	    
		foo.UsersServlet connect = new foo.UsersServlet();
		String user = connect.getNicknameUser();
		Query q = new Query("Friend").setFilter(new FilterPredicate("lastName", FilterOperator.EQUAL, user));
	    
	    
	    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
	    PreparedQuery pq = datastore.prepare(q);
	    
	    //FetchOptions fetchOptions = FetchOptions.Builder.withLimit(4);
	    FetchOptions fetchOptions = FetchOptions.Builder.withDefaults();
	    if (cursorString != null) {
		fetchOptions.startCursor(Cursor.fromWebSafeString(cursorString));
		}
	    
	    QueryResultList<Entity> results = pq.asQueryResultList(fetchOptions);
	    cursorString = results.getCursor().toWebSafeString();
	    
	    return CollectionResponse.<Entity>builder().setItems(results).setNextPageToken(cursorString).build();
	    
	}
	
	
	
	@ApiMethod(name = "abonementList", httpMethod = HttpMethod.GET)
	public CollectionResponse<Entity> abonementList(@Nullable @Named("next") String cursorString) {
		                                    //@Named("name") String name,
	    
		foo.UsersServlet connect = new foo.UsersServlet();
		String user = connect.getNicknameUser();
		Query q = new Query("Friend").setFilter(new FilterPredicate("Followers", FilterOperator.EQUAL, user));
	    
	    
	    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
	    PreparedQuery pq = datastore.prepare(q);
	    
	    FetchOptions fetchOptions = FetchOptions.Builder.withLimit(30);
	    //FetchOptions fetchOptions = FetchOptions.Builder.withDefaults();
	    if (cursorString != null) {
		fetchOptions.startCursor(Cursor.fromWebSafeString(cursorString));
		}
	    
	    QueryResultList<Entity> results = pq.asQueryResultList(fetchOptions);
	    cursorString = results.getCursor().toWebSafeString();
	    
	    return CollectionResponse.<Entity>builder().setItems(results).setNextPageToken(cursorString).build();
	    
	}
	
	@ApiMethod(name = "getPost",
		   httpMethod = ApiMethod.HttpMethod.GET)
	public CollectionResponse<Entity> getPost(User user, @Nullable @Named("next") String cursorString)
			throws UnauthorizedException {

		if (user == null) {
			throw new UnauthorizedException("Invalid credentials");
		}

		Query q = new Query("Post").
		    setFilter(new FilterPredicate("owner", FilterOperator.EQUAL, user.getEmail()));


		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		PreparedQuery pq = datastore.prepare(q);

		FetchOptions fetchOptions = FetchOptions.Builder.withLimit(2);

		if (cursorString != null) {
			fetchOptions.startCursor(Cursor.fromWebSafeString(cursorString));
		}

		QueryResultList<Entity> results = pq.asQueryResultList(fetchOptions);
		cursorString = results.getCursor().toWebSafeString();

		return CollectionResponse.<Entity>builder().setItems(results).setNextPageToken(cursorString).build();
	}

	@ApiMethod(name = "postMsg", httpMethod = HttpMethod.POST)
	public Entity postMsg(User user, PostMessage pm) throws UnauthorizedException {

		if (user == null) {
			throw new UnauthorizedException("Invalid credentials");
		}

		Entity e = new Entity("Post", Long.MAX_VALUE-(new Date()).getTime()+":"+user.getEmail());
		e.setProperty("owner", user.getEmail());
		e.setProperty("url", pm.url);
		e.setProperty("body", pm.body);
		e.setProperty("likec", 0);
		e.setProperty("date", new Date());

		
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		Transaction txn = datastore.beginTransaction();
		datastore.put(e);
//		datastore.put(pi);
		txn.commit();
		return e;
	}
}
