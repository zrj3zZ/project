function getChecked(name){
	 var rusult="";
           var check_array=document.getElementsByName(name);
           for(var i=0;i<check_array.length;i++)
           {
               if(check_array[i].checked==true)
               {         
                  rusult=parseInt(rusult)+parseInt(check_array[i].value);
               }
           }
           return rusult;
}

function selectChecked(form){
	var x="";
	for ( var i=0; i < form.elements.length; i++ ){		
		if (( true==form.elements[i].checked) && (form.elements[i].type == 'checkbox' )&&(form.elements[i].value!='ȫѡ')){
		 	x=(x +" "+ form.elements[i].value);
		}
	}	
	return x;
		
}
function AutoSelectList(form){
	for (var i=0; i < form.elements.length; i++ ){
		form.elements[i].checked=!form.elements[i].checked;
	}
}

