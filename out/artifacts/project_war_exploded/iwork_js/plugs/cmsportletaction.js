	//类型转换
	function expandSub(){
		var type=document.getElementsByName("portlettype");
	    var a;
		for(var i=0;i<type.length;i++){
			if(type[i].checked){
				a=type[i].value;
			}
		}
	    if(a==0){	
	 		eval("GROUP_SUB").style.display="none"; 	
	 		eval("ROW_SUB").style.display=""; 	
	 	}else if(a==1){
	 	    document.getElementById("paramtitle").innerHTML="外部链接URL：";
	 		eval("GROUP_SUB").style.display="";
	 		eval("ROW_SUB").style.display="none";
	 	}else{
	 	    document.getElementById("paramtitle").innerHTML="接口实现类：";
	 	    eval("GROUP_SUB").style.display="";
	 	    eval("ROW_SUB").style.display="none";
	 	}
	}
	
	//键值校验
	function keyVal(key){
		$.ajax({
			type: 'POST',
			url: 'cmsKeyVal.action',
			data: {'key':key,'type':document.forms[0].type.value,'portletid':document.forms[0].portletid.value},
			dataType: 'text',
			success: function(data,status){
			  document.getElementById("keyval").innerHTML=data;
			}
		});
    }