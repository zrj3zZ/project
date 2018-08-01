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
		
	function ReSizeiFrame(iframe)
 	{
   		if(iframe)
   		{
     		iframe.style.display = "block";
      		if(iframe.contentDocument && iframe.contentDocument.body.offsetHeight)
      		{
        		iframe.height = iframe.contentDocument.body.offsetHeight + FFextraHeight;
      		}
      			else if (iframe.Document && iframe.document.body.offsetHeight)
      		{
	
        		iframe.height = iframe.document.body.offsetHeight-99;
      		}
   		}
 	}	
		
	function readit(id){
		 var obj = {id:id,cmd:"READ_ONE"};
	     $.post("sysmsg_setsutaus.action",obj,function(data){  
	    	   $("#readBtn_"+id).hide();
	           $("#icon_"+id).attr("src","iwork_img/sysmsg/sys_msg_icon_read.png");
	     });
	}
	function silentReadIt(id) {
		frmMain.cmd.value="READ_ONE_SILENT";
		frmMain.id.value=id;
		frmMain.submit();
	}
	function deleteit(id) {
		var obj = {id:id,cmd:"DEL_ONE"}; 
	     $.post("sysmsg_remove.action",obj,function(data){
	           $("#div_item_"+id).slideUp('slow');
	     });
	}	