 $().ready(function() {
	
	 $("#BXJE").bind("change", function() { 
		 calculateRate();
	});
	 calculateRate();
 });
 function calculateRate(){
	 var txt =  $("#BXJE").val(); 
	 if(txt!=''){
		 var  rate = 	parent.$("#HL").val();
		 $("#BWBJE").val(txt*rate);
	 }
	
 }