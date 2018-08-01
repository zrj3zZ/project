		
     	//退出
		function cancel(){
			$('#upload_proc_def_window').window('close');
		}
		
		// 执行删除操作
		function remove(){
			 art.dialog.confirm('确认删除?',function(result){  
				 	if(result){
	                	var row = $('#metadata_grid').datagrid('getSelected');
						if (row){
							var index = $('#metadata_grid').datagrid('getRowIndex', row);
							$('#metadata_grid').datagrid('deleteRow',index);
	                    	document.forms[0].action="sysEngineMetadata!remove.action?id="+row.ID;
			 				document.forms[0].submit();
			 			}
                    }
            });
		}
		
		function doSubmit() {
			var options = { 			       
					type: 'post',
			        url: 'sysProcessDefinition!hasDifferentKey.action',
					success:showResponse  // post-submit callback 
			        
			    }; 

			if($("uploadNewForm").form('validate')){
			
				 //document.uploadNewForm.submit();
				$(uploadNewForm).ajaxSubmit(options);
				 }else{
					$("a[icon=pagination-load]").trigger('click');
				 }
		}
		
		function showResponse(responseText, statusText, xhr, $form)  { 		    
			responseText = responseText.replace( /^\s+|\s+$/g, "" );
			if (responseText == "YEP") {
				$.messager.alert('提示信息','流程KEY值不同，请修改或重新选择部署文件！','info'); 
			} else if (responseText == "SAME") {
				$.messager.alert('提示信息','流程KEY值和其他流程重复！','info'); 
			}
			else {
				//var fileName = responseText;
				var data = responseText.split("|");				
				//alert(data[0]);  // fileName
				//alert(data[1]);  // uploader
				document.uploadNewForm.fileName.value = data[0];
				document.uploadNewForm.uploader.value = data[1];
				document.uploadNewForm.submit();
			}
		    
		} 
		
		// upload deploy file
		function uploadNewDefFile() {
			
			if(document.uploadNewForm.sysProcDefId.value==''){
				$.messager.alert('提示信息','请选择左侧类别目录后执行新增操作!!','info');  
				return;
			}
			$('#upload_proc_def_window').window('open');	
			
		}
		 
		// query deploy log
		function queryDeployLog() {
			$('#deploy_log_window').window('open');
		}
		// delete the given deployment
		function delDeployments(){
			$.messager.confirm('确认','确认删除?',function(result){  
				 	if(result){
	                	document.forms["editForm"].action="sysProcessDefinition!deleteDeployments.action";
						document.forms["editForm"].submit();
                    }
            });
		}
		// delete the given deployment
		function delProcess(){
			$.messager.confirm('确认','确认删除?',function(result){  
				 	if(result){
	                	document.forms["editForm"].action="sysProcessDefinition!deleteProcess.action";
						document.forms["editForm"].submit();
                    }
            });
		}
		//全选
		function selectall(id){ //用id区分  
			var tform=document.forms["editForm"];  
			for(var i=0;i<tform.length;i++){  
				var e=tform.elements[i];  
				if(e.type=="checkbox" && e.name==id) e.checked=!e.checked;  
			}  
		}