function query(){
	 var url='loadQuery.action'	 
            document.forms[0].action=url;  
            document.forms[0].method="post";  
            document.forms[0].enctype="multipart/form-data" 
            document.forms[0].submit();
            
}