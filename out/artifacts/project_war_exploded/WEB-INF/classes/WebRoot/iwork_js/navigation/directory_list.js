	    function doSearch(){
			if(document.all.sysNavSystemid.value=="")
			{	
				alert("请输入查询关键字!");
			}else{
		//	alert(document.all.sysNavSystemid.value);
				window.location.href="directory_list.action?queryName=SYSTEM_ID&&queryValue="+document.all.sysNavSystemid.value;
		 	}
		}