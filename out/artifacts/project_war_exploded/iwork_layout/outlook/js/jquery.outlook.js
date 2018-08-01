	    function showContent(sysId){
	    	$("#sysTabs").tabs("select","我的菜单");
	    	 var menuHtml = "";
	    	 $.ajax({
		    	  url: "openjson.action?nodeType=SYS&id="+sysId,
		    	  type: "POST",  
		    	  dataType: "json",  
		    	  contentType: "application/json; charset=utf-8",  
		    	  data: "{}",  
		    	  success: function(json){
		    		 
		    		  $.each(json,function(idx,item){
		    			  if(item.isParent=="false"){
		    				  menuHtml+="<div class=\"clickItem\" onclick=\"addTab('"+item.name+"','"+item.pageurl+"')\"><img src='"+item.icon+"' border='0'>"+item.name+"</div>";
		    			   }else{
		    				   menuHtml+="<div class=\"clickMenu\" onclick=\"clickMenu(this,"+item.id+")\"><img src='"+item.icon+"' border='0'>"+item.name+"</div><div  class='subMenu'></div>";
		    			   }
		    		  });
		    	  },complete: function(x) { 
		    		  $('#outlook').html(menuHtml);
		    	  }  
		    	});
	    }
	    
	    function clickMenu(item,id){
	    	$(item).next().slideToggle(100).end().siblings(".clickMenu").next().slideUp();
	    	if($(item).attr("class")=='clickMenu'){
	    		$(item).attr("class","clickMenu_visit");
	    	}else{
	    		$(item).attr("class","clickMenu");
	    	}
	    	
			   $.ajax({  
			    	  url: "openjson.action?nodeType=NODE&id="+id, 
			    	  type: "POST",  
			    	  dataType: "json",  
			    	  contentType: "application/json; charset=utf-8",  
			    	  success: function(json){
			    		  var iteminfo = "<div class=\"subMenuItem\"><ul>";
			    		  $.each(json,function(idx,subitem){
			    			  iteminfo+="<li  onclick=\"addTab('"+subitem.name+"','"+subitem.pageurl+"')\"><img src='"+subitem.icon+"' border='0'>"+subitem.name+"</li>";
			    		  }); 
			    		  iteminfo += "</ul></div>";
			    		  $(item).next().html(iteminfo);
			    	  }
			    	});
			   
			   
	    }
	   
