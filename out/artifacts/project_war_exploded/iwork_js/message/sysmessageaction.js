	$(function(){			
				
				$('#editForm').form({  
				    onSubmit:function(){  
				        return $(this).form('validate');  
				    },  
				    success:function(data){  
				        alert(data);  
				    }  
				});
				
				$('#editForm').ajaxForm(); 
				
		});
		
		function sendSysMsg(actProcDefId, title) {
			$('#sys_msg_send_window').window('open');
		}
		
		function doSubmit(){
			 if($("editForm").form('validate')){
			 	document.forms[0].submit();
			 }
			 
		}
		
		function cancel(){
			
			$('#sys_msg_send_window').window('close');
		}
		
		function refresh() {
			window.location.href='sysMessageAction!index.action';
		}