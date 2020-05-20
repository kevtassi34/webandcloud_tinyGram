<%@ page import="com.google.appengine.api.users.UserServiceFactory"%>
<%@ page import="java.security.Principal"%>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="com.google.appengine.api.datastore.DatastoreService"%>
<%@ page import="com.google.appengine.api.datastore.Key"%>
<%@ page import="com.google.appengine.api.datastore.DatastoreServiceFactory"%>
<%@ page import="com.google.appengine.api.datastore.Entity"%>
<%@ page import="com.google.appengine.api.datastore.KeyFactory"%>
<%@ page import="com.google.appengine.api.datastore.EntityNotFoundException"%>
<%@ page import="com.google.appengine.api.users.UserService"%>


<%-- <%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> --%>


<%!UserService userService = UserServiceFactory.getUserService();%>
<%!DatastoreService datastore  = DatastoreServiceFactory.getDatastoreService(); %>
<%!SimpleDateFormat formatDate = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss"); %>
<%if(!userService.isUserLoggedIn()){ 
	response.sendRedirect("/login"); 
}else{
	//Get connected user informations
	Entity e;
	try {
		e = datastore.get( KeyFactory.createKey("User", request.getUserPrincipal().getName()));
		pageContext.setAttribute("entity", e);
	} catch (EntityNotFoundException ex) {
		// TODO Auto-generated catch block
		ex.printStackTrace();
	}
}

%>

<!DOCTYPE html>

<html>
<head>
<meta charset="UTF-8">

<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">


<link rel="stylesheet"
	href="https://cdn.jsdelivr.net/npm/bulma@0.8.0/css/bulma.min.css">
<!--    <link rel="stylesheet" href="../Css/styleButton.css"> -->
<script defer
	src="https://use.fontawesome.com/releases/v5.3.1/js/all.js"></script>


<script src="https://unpkg.com/mithril/mithril.js"></script>

  <meta http-equiv="content-type" content="application/xhtml+xml; charset=UTF-8" />
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
  <meta name="description" content="">
  <meta name="author" content="">

<title>TinyGram</title>

<!-- Bootstrap core CSS -->
  <link href="startbootstrap-one-page-wonder-gh-pages/vendor/bootstrap/css/bootstrap.min.css" rel="stylesheet">

  <!-- Custom fonts for this template -->
  <link href="https://fonts.googleapis.com/css?family=Catamaran:100,200,300,400,500,600,700,800,900" rel="stylesheet">
  <link href="https://fonts.googleapis.com/css?family=Lato:100,100i,300,300i,400,400i,700,700i,900,900i" rel="stylesheet">

  <!-- Custom styles for this template -->
  <link href="startbootstrap-one-page-wonder-gh-pages/css/one-page-wonder.min.css" rel="stylesheet">

</head>
<body>



	<script>	

// for the example
	
		
		String me = connect.getNicknameUser(); %>
		
//var me="f2"
 
var PostForm = {
		url:"",
		body:"",
		  view: function() {
		    return m("form", {
		      onsubmit: function(e) {
		        e.preventDefault()
				if (url="") {url="https://dummyimage.com/320x200/000/fff&text="+Date.now()} 
				if (body="") {body="bla bla bla \n"+Date.now()}
		        MyPost.postMessage()
		      }}, 
		      [
		    	m('div', {class:'field'},[
		         m("label", {class:'label'},"URL"),
		         m('div',{class:'control'}, m("input[type=text]", {
		          class:'input is-rounded',
		          placeholder:"Your url",
		             oninput: function(e) {PostForm.url = e.target.value}})),
//		         m("img",{"src":this.url}),
		        ]),
		      m('div',{class:'field'},[
		    	  m("label", {class: 'label'},"Body"),
		          m('div',{class:'control'},m("input[type=textarea]", {
		        class:'textarea',
		        placeholder:"your text",
		        oninput: function(e) { PostForm.body = e.target.value }})),
		        ]),
		      m('div',{class:'control'},m("button[type=submit]", {class:'button is-link'},"Post")),
		    ])
		  }
		}


var MyPost = {
		list: [],
	    nextToken: "",
	   
	    loadList: function() {
	        return m.request({
	            method: "GET",
	            url: "_ah/api/myApi/v1/mypost/"+me})
	        .then(function(result) {
	        	console.log("got:",result)
	        	MyPost.list=result.items
	            if ('nextPageToken' in result) {
		        	MyPost.nextToken= result.nextPageToken
	            } else {
	            	MyPost.nextToken=""
	            }})
	    },
	    next: function() {
	        return m.request({
	            method: "GET",
	            url: "_ah/api/myApi/v1/mypost/"+me+"?next="+MyPost.nextToken})
	        .then(function(result) {
	        	console.log("got:",result)
	        	result.items.map(function(item){MyPost.list.push(item)})
	            if ('nextPageToken' in result) {
		        	MyPost.nextToken= result.nextPageToken
	            } else {
	            	MyPost.nextToken=""
	            }})
	    },
 	    postMessage: function() {
 			var data={'owner':me,
 					'url':PostForm.url,
 					'body':PostForm.body}
 	    	console.log("post:"+data)
     		return m.request({
         		method: "POST",
         		url: "_ah/api/myApi/v1/postMessage",
             	params: data,
         	})
  	    	.then(function(result) {
     	 			console.log("got:",result)
     	 			MyPost.loadList()
         	 	})
     	},
	    
     	deletePost: function(e){
   		 var url = "/deletepost/" + e
   		 window.location = url;
   	 },
   	 
        deleteAllPost: function(){
      		 var url = "/prefixclean" 
       		 window.location = url;
       	 }, 
       	 
       	likePost: function(e){
      		 var url = "/likemypost/" + e
       		 window.location = url;
       	 },
       	 
}

var PostView = {
  oninit: MyPost.loadList,
  view: function() {
   	return m('div', [
	  m('div',{class:'subtitle'},"My Posts"),
	  m('table', {class:'table is-striped',"table":"is-striped"},[
	    m('tr', [
		  m('th', {width:"50px"}, "like"),
		  m('th', {width:"50px"}, "del"),
	      m('th', {width:"50px"}, "Bodys"),
	      m('th', {width:"50px"}, "Urls"),
	      m('th', {width:"50px"}, "Like"),
	    ]),
	    MyPost.list.map(function(item) {
	      return m("tr", [
            
	    	  m("td", m("button", {
	    		  class: 'button is-success',
	    		  onclick: function(e) {
	    		  MyPost.likePost(item.key.id);
				console.log("like:"+item.key.id)
                 }},"like")),
                 
                 m("td", m('button',{
                     class: 'button is-warning',
                    	onclick: function(e) {
                     	MyPost.deletePost(item.key.id);
         				console.log("del:"+item.key.id)
                    	 }
                    },"Supprimer ce post")),
                 
                 
	        m('td', m('label', item.properties.body)),
	        m('td', m('img', {class: 'is-rounded', 'src': item.properties.url})),
	        m('td', m('label', item.properties.likec)),
	      ])
	    }),
//	    m("div", isError ? "An error occurred" : "Saved"),
	   m("td", m('button',{
		      class: 'button is-link',
		      onclick: function(e) {MyPost.next()}
		      },
		  "Next")),
		  
		 m("td", m('button',{
		      class: 'button is-danger',
		      onclick: function(e) {
		    	 MyPost.deleteAllPost();
   			
		    	  }
		      },
		  "Supprimer tous mes Post")),
		  
		  
	   ])
	 ])
  }
}

var Hello = {
		
// 	viewp : function(){
// 	return m('nav', {class :'navbar navbar-expand-lg navbar-dark navbar-custom fixed-top'}, [
// 		m('div', {class:'container'},[
// 			m('a', {class:"navbar-brand", href:"#"}, "TinyGram"),
// 			m("button", {class:"navbar-toggler"},
// 					[
// 						m("span", {class:"navbar-toggler-icon"})
// 					], "bonjoour"),
// 			m('div', {class:"collapse navbar-collapse", id:"navbarResponsive"}, [
// 			m('ul', {class:"navbar-nav ml-auto"}, [
// 				m('li', {class:"nav-item"}, [
// 				   m('a', {class:"nav-link", href:"#"}, "Sing up")
// 				   ]),
// 			    m('li', {class:"nav-item"}, [
// 				   m('a', {class:"nav-link", href:"#"}, "Log In")
// 					   ]),
// 			]),
			
// 			])	,	
					
// 			])	,

// 		])
		
// 	},	
	
   view: function() {

   return m('div', {class:'container'}, [
           m("h1", {class: 'title'}, 'The TinyGram post'),
           m('a', {class:"nav-link", href:'/userapi'}, "Sing up"),
           m('div',{class: 'tile is-ancestor'},[
               m("div", {class: 'tile'}, m('div',{class:'tile is-child box'},m(PostForm))),
        	   m("div", {class: 'tile'}, m('div',{class:'tile is-child box'},m(PostView))),
           ])
       ])
   }
}


m.mount(document.body, Hello)	


</script>
</body>
</html>