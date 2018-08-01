		
     	//退出
		function cancel(){
			
			$('#sys_msg_send_window').window('close');
		}
		//保存提交
		function doSubmit() {

			if($("editForm").form('validate')){
				var options = { 
				        
						type: 'post',
				        url: 'sysMessageAction!send.action',
						success:       showResponse  // post-submit callback 
				        
				 
				        // other available options: 
				        //url:       url         // override for form's 'action' attribute 
				        //type:      type        // 'get' or 'post', override for form's 'method' attribute 
				        //dataType:  null        // 'xml', 'script', or 'json' (expected server response type) 
				        //clearForm: true        // clear all form fields after successful submit 
				        //resetForm: true        // reset the form after successful submit 
				 
				        // $.ajax options can be used here too, for example: 
				        //timeout:   3000 
				    }; 
				 
				
				 	$(editForm).ajaxSubmit(options);
				 	
				 }else{
					$("a[icon=pagination-load]").trigger('click');
				 }
		}
		
		function showResponse(responseText, statusText, xhr, $form)  { 
		    
			
			responseText = responseText.replace( /^\s+|\s+$/g, "" );
			if (responseText == "OK") {
				$.messager.alert('提示信息','发送成功','info'); 
				$('#sys_msg_send_window').window('close');
			}
			else {
				
			}
		    
		} 
		// 执行删除操作
		function remove(){
				 $.messager.confirm('确认','确认删除?',function(result){  
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
		
		