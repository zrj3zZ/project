 $().ready(function() {
	 $("#BZ").bind("change", function() { 
		 calculateRate();
	});
	 calculateRate();
	 function calculateRate(){
		 var txt =  $("#BZ").val(); 
		 	if(txt!=''&&txt.indexOf("—")>1){
		 		var num = txt.indexOf("—");
		 		var key = txt.substring(0,num);
		 		var pageurl = "showTestFormInfo.action";
		 		 $.post(pageurl,{codestr:key},function(msg){ 
		 			 if(msg!=''){
		 				 $("#HL").val(msg);
		 			 }
		 		 }); 
		 	}
		 
	 }
 });
 