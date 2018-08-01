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
	
	var FFextraHeight = 0;
 	if(window.navigator.userAgent.indexOf("Firefox")>=1)
 	{
  		FFextraHeight = 400;
  	}
  	if(window.navigator.userAgent.indexOf("Chrome")>=1)
 	{
  		FFextraHeight = 300;
  	}
  	if(window.navigator.userAgent.indexOf("Opera")!=-1)
 	{
  		FFextraHeight = 320;
  	}	
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
		
	function getMsgAll() {
		document.getElementById("divsysmsgall").style.display = "";
		document.getElementById("divsysmsgcommon").style.display = "none";
		document.getElementById("divsysmsgbirth").style.display = "none";
		document.getElementById("divsysmsgworkflow").style.display = "none";
		document.getElementById("divmsgunread").style.display = "none";
		document.getElementById("divmsgread").style.display = "none";
		
		frmMain.type.value = "";
		frmMain.status.value = ""; // 默认显示所有状态，已读|未读, 删除不显示
		frmMain.target="message_list";
		frmMain.submit();
	}
	function getMsgCommon() {		
		document.getElementById("divsysmsgall").style.display = "none";
		document.getElementById("divsysmsgcommon").style.display = "";
		document.getElementById("divsysmsgbirth").style.display = "none";	
		document.getElementById("divsysmsgworkflow").style.display = "none";	
		document.getElementById("divmsgunread").style.display = "none";
		document.getElementById("divmsgread").style.display = "none";
		
		frmMain.type.value = "0";
		frmMain.status.value = ""; // 默认显示所有状态，已读|未读, 删除不显示
		frmMain.target="message_list";
		frmMain.submit();
	}
	function getMsgBirth() {
		document.getElementById("divsysmsgall").style.display = "none";
		document.getElementById("divsysmsgcommon").style.display = "none";
		document.getElementById("divsysmsgbirth").style.display = "";
		document.getElementById("divsysmsgworkflow").style.display = "none";
		document.getElementById("divmsgunread").style.display = "none";
		document.getElementById("divmsgread").style.display = "none";
		
		frmMain.type.value = "1";
		frmMain.status.value = ""; // 默认显示所有状态，已读|未读, 删除不显示
		frmMain.target="message_list";
		frmMain.submit();
	}
	function getMsgWorkFlow() {
		document.getElementById("divsysmsgall").style.display = "none";
		document.getElementById("divsysmsgcommon").style.display = "none";
		document.getElementById("divsysmsgbirth").style.display = "none";
		document.getElementById("divsysmsgworkflow").style.display = "";
		document.getElementById("divmsgunread").style.display = "none";
		document.getElementById("divmsgread").style.display = "none";
		
		frmMain.type.value = "2";
		frmMain.status.value = ""; // 默认显示所有状态，已读|未读, 删除不显示
		frmMain.target="message_list";
		frmMain.submit();
	}
	function getMsgRead() {
		document.getElementById("divsysmsgall").style.display = "none";
		document.getElementById("divsysmsgcommon").style.display = "none";
		document.getElementById("divsysmsgbirth").style.display = "none";
		document.getElementById("divsysmsgworkflow").style.display = "none";
		document.getElementById("divmsgunread").style.display = "none";
		document.getElementById("divmsgread").style.display = "";
		
		frmMain.type.value = "";
		frmMain.status.value = "1"; // 默认显示所有状态，已读|未读, 删除不显示
		frmMain.target="message_list";
		frmMain.submit();
	}
	function refresh() {
		frmMain.cmd.value = "Kingsoft_Message_Query_Exec";
		frmMain.target="message_list";
		frmMain.submit();
	}
	function getUnread() {
		document.getElementById("divsysmsgall").style.display = "none";
		document.getElementById("divsysmsgcommon").style.display = "none";
		document.getElementById("divsysmsgbirth").style.display = "none";
		document.getElementById("divsysmsgworkflow").style.display = "none";
		document.getElementById("divmsgunread").style.display = "";
		document.getElementById("divmsgread").style.display = "none";
		
		frmMain.type.value = "";
		frmMain.status.value = "0"; // 默认显示所有状态，已读|未读, 删除不显示
		frmMain.target="message_list";
		frmMain.submit();
	}
	function setAllRead() {
		if (true==confirm("您是否确定将全部系统消息设置为已读状态？") ){
			$.post("sysmsg_setsutaus.action",{},function(data){ 
				window.location.reload();
		     });
		}else {
			return;
		}
		
	}
	function setAllDel() {
		if (true==confirm("是否要清空全部消息?") ){
			$.post("sysmsg_removeAll.action",{},function(data){ 
				window.location.reload();
		     });
		}else {
			return;
		}
	}