<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!doctype html>
<html lang="en">

<head>
  <meta charset="utf-8" />
  <link rel="apple-touch-icon" sizes="76x76" href="assets/img/apple-icon.png">
  <link rel="icon" type="image/png" href="assets/img/favicon.png">
  <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
 
 
 <meta charset="UTF-8">

<meta name="viewport" content="width=device-width, initial-scale=1">
 	<link rel="stylesheet"
		href="https://cdn.jsdelivr.net/npm/bulma@0.8.0/css/bulma.min.css">
	<script defer
		src="https://use.fontawesome.com/releases/v5.3.1/js/all.js"></script>

	<script src="https://unpkg.com/mithril/mithril.js"></script>
	
			<link rel="stylesheet" href="/dist/css/bootstrap.min.css">
<script src="/dist/js/bootstrap.min.js"></script>

  <meta http-equiv="content-type" content="application/xhtml+xml; charset=UTF-8" />
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
  <meta name="description" content="">
  <meta name="author" content="">
 
 
  <title>
    TinyGram Cloud
  </title>
  <meta content='width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0, shrink-to-fit=no' name='viewport' />
  <!--     Fonts and icons     -->
  <link href="https://fonts.googleapis.com/css?family=Montserrat:400,700,200" rel="stylesheet" />
  <link href="https://maxcdn.bootstrapcdn.com/font-awesome/latest/css/font-awesome.min.css" rel="stylesheet">
  <!-- CSS Files -->
  <link href="assets/css/bootstrap.min.css" rel="stylesheet" />
  <link href="assets/css/paper-dashboard.css?v=2.0.1" rel="stylesheet" />
 
</head>

<body class="">
<script>



  var AbonnementList = {
  		list: [],
  	    nextToken: "",
  	   
  	    loadList: function() {
  	        return m.request({
  	            method: "GET",
  	            url: "./_ah/api/myApi/v1/abonementList/"})
  	        .then(function(result) {
  	        	console.log("got:",result)
  	        	AbonnementList.list=result.items
  	            if ('nextPageToken' in result) {
  		        	AbonnementList.nextToken= result.nextPageToken
  	            } else {
  	            	AbonnementList.nextToken=""
  	            }})
  	    },
  	    next: function() {
  	        return m.request({
  	            method: "GET",
  	            url: "./_ah/api/myApi/v1/abonementList/"+"?next="+AbonnementList.nextToken})
  	        .then(function(result) {
  	        	console.log("got:",result)
  	        	result.items.map(function(item){AbonnementList.list.push(item)})
  	            if ('nextPageToken' in result) {
  		        	AbonnementList.nextToken= result.nextPageToken
  	            } else {
  	            	AbonnementList.nextToken=""
  	            }})
  	    },
   	    postMessage: function() {
   			var data={//'owner':me,
   					'url':PostForm.url,
   					'body':PostForm.body}
   	    	console.log("post:"+data)
       		return m.request({
           		method: "POST",
           		url: "./_ah/api/myApi/v1/postMessage",
               	params: data,
           	})
    	    	.then(function(result) {
       	 			console.log("got:",result)
       	 			AbonnementList.loadList()
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
         	 
         	Actualite : function(){
        		 var url = "/actualite" 
         		 window.location = url;
         	 },
         	 
         	addUser : function(e){
        		 var url = "/adduser/" + e 
         		 window.location = url;
         	 },
         	 
         	unfollow : function(e){
       		 var url = "/unfollow/" + e 
     		 window.location = url;
     	 },
     	 
     	showfollowers : function(e){  
    		 var url = "/showfollowers/" + e 
    		 window.location = url;
    		
     	},
     	
    	 showAbonnes : function(){  
    			 var url = "/abonnements"
         		 window.location = url;
       	 },
         	 
  }

  var AbonnementView = {
    oninit: AbonnementList.loadList,
    view: function() {
     	return m('div', [
  	  m('div',{class:'subtitle'},"Mes Abonnemmets"),
  	
  	  m("center", m("button", {
		  class : 'button is-primary',
		  onclick: function(e) {
		  AbonnementList.showAbonnes();
		
         }},"Vos Abonnés ici")),
         
      m('div',{class:'subtitle'},""),
  	  m('table', {class:'table is-striped',"table":"is-striped"},[
  	    m('tr', [
  	      m('th', {width:"50px"}, " - "),
  		  m('th', {width:"50px"}, "fisrtName"),
  		  m('th', {width:"50px"}, "lastName"),
  	      m('th', {width:"50px"}, "age"),
  	      m('th', {width:"50px"}, "NbFollowBy"),
  	     
  	    ]),
  	    
  	    AbonnementList.list.map(function(item) {
  	      return m("tr", [
              
  	    	  m("td", m("button", {
  	    		  class : 'button is-danger',
  	    		  onclick: function(e) {
  	    		  AbonnementList.unfollow(item.key.id);
  				
                   }},"Unfollow")
                 ),
                          
  	        m('td', m('label', item.properties.firstName)),
  	        m('td', m('label', item.properties.lastName)),
  	        m('td', m('label', item.properties.age)),
  	       // m('td', m('label', item.properties.NbFollowBy)),
  	       
  	        m("td", m("button", {class : 'button is-activered',
    		  
  	        	onclick: function(e) {
    			  
  	        		AbonnementList.showfollowers(item.key.id);
    		  
           }}, "" + item.properties.NbFollowers )
         ),
  	        
  	        
  	      ])
  	    }),
	  
  		  
  	   ]),
  	   
   	  m("center",m("h1", m("td", m('button',{
	      class: 'button is-link glyphicon glyphicon-circle-arrow-down',
	      onclick: function(e) {AbonnementList.next()},"style":{"width":"150vh","height":"5vh"}
	      },
	  "Next")))),
  	 ])
    }
  }






var Hello = {

view: function() {

 return [m("div", {"class":"wrapper "},
    [
      m("div", {"class":"sidebar","data-color":"white","data-active-color":"danger"},
        [
          m("div", {"class":"logo"},
            [
              m("a", {"class":"simple-text logo-mini","href":"/paper-dashboard-master/MyProfile.jsp"},
                [
                  m("div", {"class":"logo-image-small"}, 
                    m("img", {"src":"assets/img/avatar.png"})
                  )
               
                ]
              ),
              m("a", {"class":"simple-text logo-normal","href":"/paper-dashboard-master/mainActu.jsp"}, 
                " Accueil "
              )
            ]
          ),
          m("div", {"class":"sidebar-wrapper"}, 
            m("ul", {"class":"nav"},
              [
                m("li", {"class":"active "}, 
                  m("a", {"href":"/paper-dashboard-master/usersPage.jsp"},
                    [
                      m("i", {"class":"glyphicon glyphicon-user"}),
                      m("p", 
                        "Liste des Utilisateurs"
                      )
                    ]
                  )
                ),
                m("li", 
                  m("a", {"href":"/paper-dashboard-master/abonnementsPage.jsp"},
                    [
                      m("i", {"class":"glyphicon glyphicon-heart-empty"}),
                      m("p", 
                        "Mes Abonnements " 
                      )
                    ]
                  )
                ),
   
                m("li", 
                  m("a", {"href":"../post.jsp"},
                    [
                      m("i", {"class":"glyphicon glyphicon-plus-sign"}),
                      m("p", 
                        "Faire un Post"
                      )
                    ]
                  )
                )
              ]
            )
          )
        ]
      ),
     
      
      
      
      
      
      m("div", {"class":"main-panel"},
        [
          m("nav", {"class":"navbar navbar-expand-lg navbar-absolute fixed-top navbar-transparent fixed-top"}, 
            m("div", {"class":"container-fluid"},
              [
                m("div", {"class":"navbar-wrapper"},
                  [
                    m("div", {"class":"navbar-toggle"}, 
                      m("button", {"class":"navbar-toggler","type":"button"},
                        [
                          m("span", {"class":"navbar-toggler-bar bar1"}),
                          m("span", {"class":"navbar-toggler-bar bar2"}),
                          m("span", {"class":"navbar-toggler-bar bar3"})
                        ]
                      )
                    ),
                    m("a", {"class":"navbar-brand","href":"javascript:;"}, 
                      "TinyGram"
                    )
                  ]
                ),
                m("button", {"class":"navbar-toggler","type":"button","data-toggle":"collapse","data-target":"#navigation","aria-controls":"navigation-index","aria-expanded":"false","aria-label":"Toggle navigation"},
                  [
                    m("span", {"class":"navbar-toggler-bar navbar-kebab"}),
                    m("span", {"class":"navbar-toggler-bar navbar-kebab"}),
                    m("span", {"class":"navbar-toggler-bar navbar-kebab"})
                  ]
                ),
                m("div", {"class":"collapse navbar-collapse justify-content-end","id":"navigation"},
                  [
                    m("form", 
                      m("div", {"class":"input-group no-border"},
                        [
                          m("input", {"class":"form-control","type":"text","value":"","placeholder":"Search..."}),
                          m("div", {"class":"input-group-append"}, 
                            m("div", {"class":"input-group-text"}, 
                              m("i", {"class":"nc-icon nc-zoom-split"})
                            )
                          )
                        ]
                      )
                    ),
                    m("ul", {"class":"navbar-nav"}, 
                      m("li", {"class":"nav-item btn-rotate dropdown"},
                        [
                          m("a", {"class":"nav-link dropdown-toggle","href":"/userapi","id":"navbarDropdownMenuLink","data-toggle":"dropdown","aria-haspopup":"true","aria-expanded":"false"},
                            [
                              m("i", {"class":"glyphicon glyphicon-log-out"}),
                              m("p", 
                                m("span", {"class":"d-lg-none d-md-block"}, 
                                  "Some Actions"
                                )
                              )
                            ]
                          ),
                          m("div", {"class":"dropdown-menu dropdown-menu-right","aria-labelledby":"navbarDropdownMenuLink"},
                            [
                              m("a", {"class":"dropdown-item","href":"#"}, 
                                "Se déconnecter"
                              ),
                              m("a", {"class":"dropdown-item","href":"#"}, 
                                "Another action"
                              ),
                              m("a", {"class":"dropdown-item","href":"#"}, 
                                "Something else here"
                              )
                            ]
                          )
                        ]
                      )
                    )
                  ]
                )
              ]
            )
          )
        ]
      ),
          
        m("div", {"class":"main-panel"}, 
        [		
          m("div", {"class":"content"}, 
            m("div", {"class":"row"}, 
              m("div", {"class":"col-md-12"}, 
            
            	                
  
      // m("div", {class: 'tile'}, m('div',{class:'tile is-child box'},m(PostForm))),
        	   m("div", {class: 'tile'}, m('div',{class:'tile is-child box'},m(AbonnementView))),
                    
                
              )
            )
          )
         ]
        ),
         
          
        m("div", {"class":"main-panel","style":{"height":"100vh"}},
        [		
          m("footer", {"class":"footer","style":{"position":"absolute","bottom":"0","width":"-webkit-fill-available"}}, 
            m("div", {"class":"container-fluid"}, 
              m("div", {"class":"row"},
                [
                  m("nav", {"class":"footer-nav"}, 
                  ),
                  m("div", {"class":"credits ml-auto"}, 
                    m("span", {"class":"copyright"},
                      [
                        " © 2020, made with ",
                        m("i", {"class":"fa fa-heart heart"}),
                        " by Kévin Tassi - Antoine Renard - Pierre Fouillet"
                      ]
                    )
                  )
                ]
              )
            )
          )
        ]
      )
    ]
  )
 ]
 
  m("script", {"src":"assets/js/core/jquery.min.js"}), 
  m("script", {"src":"assets/js/core/popper.min.js"}), 
  m("script", {"src":"assets/js/core/bootstrap.min.js"}), 
  m("script", {"src":"assets/js/plugins/perfect-scrollbar.jquery.min.js"}), 
  m("script", {"src":"https://maps.googleapis.com/maps/api/js?key=YOUR_KEY_HERE"}), 
  m("script", {"src":"assets/js/plugins/chartjs.min.js"}), 
  m("script", {"src":"assets/js/plugins/bootstrap-notify.js"}), 
  m("script", {"src":"assets/js/paper-dashboard.min.js?v=2.0.1","type":"text/javascript"})

}
}

m.mount(document.body, Hello)

  </script>
</body>

</html>
