		
		function initTree(){
    		var defaultTree = "dept";
			$('input:radio[name="r_type"][value="'+defaultTree+'"]').attr("checked", true);
			buildTree(defaultTree);
		}
		function unCheckChildrenNodes(obj, node) {
			$(obj).tree('uncheck', node.target);
			
			var nodes = $(obj).tree('getChildren', node.target);
			for (var i = 0; i < nodes.length; i++) {
				unCheckChildrenNodes(obj, nodes[i]);
			}
		}
		
		function changeType(button) {
			var type = button.value;
			buildTree(type);
		}		
		function buildTree(type){
			if (type == deptId) {
				$('#leftlengendtitle').html("<b><font color='808080' >"+deptName+"</font></b>");
				$('#group').hide();
				$('#role').hide();
				$('#user').hide();
				$('#dept').toggle();
			} else if (type == groupId) {
				$('#leftlengendtitle').html("<b><font color='808080' >"+groupName+"</font></b>");
				$('#dept').hide();
				$('#role').hide();
				$('#user').hide();
				$('#group').toggle();
			} else if (type == roleId) {
				$('#leftlengendtitle').html("<b><font color='808080' >"+roleName+"</font></b>");
				$('#group').hide();
				$('#dept').hide();
				$('#user').hide();
				$('#role').toggle();
			} else if (type == userId) {
				$('#leftlengendtitle').html("<b><font color='808080' >"+userName+"</font></b>");
				$('#dept').hide();
				$('#role').hide();
				$('#group').hide();
				$('#user').toggle();
			}
		}
		
		// 判断select选项中 是否存在Value="paraValue"的Item        
		function existItem(objSelect, objItemValue) {        
    		var exist = false;        
    		for (var i = 0; i < objSelect.options.length; i++) {        
       			if (objSelect.options[i].value == objItemValue) {        
            		exist = true;        
            		break;        
        		}        
    		}        
    		return exist;        
		} 
		
		function endWith(s1,s2)  
		{  
     		 if(s1.length<s2.length)  
        		return   false;  
      		 if(s1==s2)  
        		return   true;  
     		 if(s1.substring(s1.length-s2.length)==s2)  
         		 return   true;  
      		 return   false;  
		}
		
		// 添加指定位置到select
		function addAt(selectCtl,optionValue,optionText,position) { 
			var userAgent = window.navigator.userAgent;
			if (userAgent.indexOf("MSIE") > 0) {
				var option = document.createElement("option");
				option.value = optionValue;
				option.innerText = optionText;
				selectCtl.insertBefore(option, selectCtl.options[position]);
			} else {
				selectCtl.insertBefore(new Option(optionText,optionValue), selectCtl.options[position]);
			}
		}
		
		function typeAt(type) {
			var toSelect = document.getElementById('toSelect');
			
			for (var i = 0; i < toSelect.options.length; i++) {
				if (startWith(toSelect.options[i].value, type)) {					
					return i;
				}				      			       
    		}
    		return toSelect.options.length;
		}
		
		function startWith(s1, s2) {
			if(s2==null||s2==""||s1.length==0||s2.length>s1.length)
				return false;
			if(s1.substr(0,s2.length)==s2)
				return true;
			else
				return false;
			return true;
		}
		
		function addSelect(){
			var groupNodes = $('#groupTree').tree('getChecked');
			var deptNodes = $('#deptTree').tree('getChecked');
			var roleNodes = $('#roleTree').tree('getChecked');
			var userNodes = $('#userTree').tree('getChecked');
			
			var toSelect = document.getElementById('toSelect');
			
			for ( var i = 0; i < deptNodes.length; i++ ){
				if (endWith(deptNodes[i].id, 'list')) {					
					continue;
				}
				if (startWith(deptNodes[i].id, comIdPrifix)) { //选中公司
					var company = deptNodes[i].id;
					var exist = existItem(toSelect, company);
					if (exist == false) {					
						var comIndex = typeAt(comIdPrifix);
						addAt(toSelect,company,comNamePrifix + deptNodes[i].text,comIndex);
					}
					continue;
				}
				var dept = deptIdPrifix + deptNodes[i].id;
				var exist = existItem(toSelect, dept);		
				if (exist == false) {
					var index = typeAt(deptIdPrifix);				
					addAt(toSelect,dept,deptNamePrifix + deptNodes[i].text,index);
				}
			}
			for ( var i = 0; i < groupNodes.length; i++ ){
				var group = groupIdPrifix + groupNodes[i].id;
				var exist = existItem(toSelect, group);
				if (exist == false) {
					var index = typeAt(groupIdPrifix);
					addAt(toSelect,group,groupNamePrifix + groupNodes[i].text,index);
				}
			}
			
			for ( var i = 0; i < roleNodes.length; i++ ){
				var role = roleIdPrifix + roleNodes[i].id;
				var exist = existItem(toSelect, role);
				if (exist == false) {
					var index = typeAt(roleIdPrifix);
					addAt(toSelect,role,roleNamePrifix + roleNodes[i].text,index);
				}
			}
			
			for ( var i = 0; i < userNodes.length; i++ ){
				var nodeType = userNodes[i].attributes.nodeTye;
				if (nodeType != 'user'){
					continue;
				}
				var user = userIdPrifix + userNodes[i].id;
				var exist = existItem(toSelect, user);
				if (exist == false) {
					var index = typeAt(userIdPrifix);
					addAt(toSelect,user,userNamePrifix + userNodes[i].text,index);
				}
			}
			
		}
		/*
		function deleteSelect() {
			var toSelect = document.getElementById('toSelect');
			for (var i = 0; i < toSelect.options.length; i++) {  
				if(toSelect.options[i].selected) {
					toSelect.remove(i);
				}
			}
		}*/
		function deleteSelect(oSelect){
			var oSelect = document.getElementById('toSelect');
	        for(var i=0; i<oSelect.options.length; i++){
	            if(i>=0 && i<=oSelect.options.length-1 && oSelect.options[i].selected)
	            {
	                oSelect.options[i] = null;
	                i --;
	            }
	        }
		}
		
		// replaceAll 函数
		String.prototype.replaceAll = function(reallyDo, replaceWith, ignoreCase) {  
    		if (!RegExp.prototype.isPrototypeOf(reallyDo)) {  
        		return this.replace(new RegExp(reallyDo, (ignoreCase ? "gi": "g")), replaceWith);  
    		} else {  
        		return this.replace(reallyDo, replaceWith);  
    		}
    	};
		function tempModel(id,name) { 
			this.name = name; 
			this.id = id;
		}
		function ok() {		
			var toSelect = document.getElementById('toSelect');
			var comCode = "";
			var deptCode = "";
			var groupCode = "";
			var roleCode = "";
			var userCode = "";
			var jsonModelArr = new Array();
			var tempArr = new Array();
			var comArr = new Array();
			var deptArr = new Array();
			var groupArr = new Array();
			var roleArr = new Array();
			var userArr = new Array();
			for (var i = 0; i < toSelect.options.length; i++) {
				if (startWith(toSelect.options[i].value, comIdPrifix)) {					
					var id = toSelect.options[i].value.replace(comIdPrifix, "");
					var name = toSelect.options[i].text.replace(comNamePrifix, "");
					var model = new tempModel(id,name);
					comArr.push(model);
				}        			       
    		}
			for (var i = 0; i < toSelect.options.length; i++) {
				if (startWith(toSelect.options[i].value,deptIdPrifix)) {					
					var id = toSelect.options[i].value.replace(deptIdPrifix, "");
					var name = toSelect.options[i].text.replace(deptNamePrifix, "");
					var model = new tempModel(id,name);
					deptArr.push(model);					
				}        			       
    		}
    		for (var i = 0; i < toSelect.options.length; i++) {
				if (startWith(toSelect.options[i].value, groupIdPrifix)) {					
					var id = toSelect.options[i].value.replace(groupIdPrifix, "");
					var name = toSelect.options[i].text.replace(groupNamePrifix, "");
					var model = new tempModel(id,name);
					groupArr.push(model);
				}        			       
    		}
    		for (var i = 0; i < toSelect.options.length; i++) {
				if (startWith(toSelect.options[i].value, roleIdPrifix)) {					
					var id = toSelect.options[i].value.replace(roleIdPrifix, "");
					var name = toSelect.options[i].text.replace(roleNamePrifix, "");
					var model = new tempModel(id,name);
					roleArr.push(model);
				}        			       
    		}
    		for (var i = 0; i < toSelect.options.length; i++) {
				if (startWith(toSelect.options[i].value, userIdPrifix)) {					
					var id = toSelect.options[i].value.replace(userIdPrifix, "");
					var name = toSelect.options[i].text.replace(userNamePrifix, "");
					var model = new tempModel(id,name);
					userArr.push(model);
				}        			       
    		}
    		
    		var jsonObj={
    			COM:comArr,
    			DEPT:deptArr,
    			USER:userArr,
    			ROLE:roleArr,
    			GROUP:groupArr
    		}
    		var obj = null;
    		var W = null;
    		var obj = null;
    		try{		
    			var api = art.dialog.open.api;
		   		 W = api.opener;
		   		 var defaultField = $("#target").val();
		   		var origin = artDialog.open.origin; 
				 obj = origin.document.getElementById(defaultField);
			}catch(e){
				
			}		
    		var displayColName = obj.name+"_displayCol";    		
    		var displayCol = parent.document.getElementById(displayColName);  		
    		if(displayCol!=null){
    			var objSelect = document.getElementById('toSelect');
    			var displayValue='';
    			for (var i = 0; i < objSelect.options.length; i++) {        
	       			displayValue+=objSelect.options[i].text+',';  
	    		}  
    			displayCol.value = displayValue.substring(0,displayValue.lastIndexOf(','));
    		}
    		obj.value = objToJsonString(jsonObj);    		
			closeWin();
		}
		function closeWin(){
			try{
				var api = art.dialog.open.api, W = api.opener;
				api.close(); 
			}catch(e){
				window.close();
			}
		}
