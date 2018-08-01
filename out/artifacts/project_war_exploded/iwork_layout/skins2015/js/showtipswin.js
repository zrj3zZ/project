	$(function(){
		 popUpSysMsg();  
	});		
	
	//系统消息
	function popUpSysMsg() {
		$.ajax({
   			url:'sysMessageAction!popUpSysMsg.action', //后台处理程序
   			type:'post',         //数据发送方式
   			dataType:'json',     //接受数据格式
   			success:showSysMsg //回传函数(这里是函数名)
 		});
	}
	function showSysMsg(json) {
		if(json!=null){
			//$('#sysmsg').html('(' + json.msgNums + ')');
			//$.messager.lays(300, 200);
			var sysTitle = "温馨提示："; 
			var sysMsg = "<div  class='tipDiv'><div class='tipTitle'>" + json.title + "</div><div  class='tipContent'>" + json.content + "</div></div>";
			art.dialog.open(sysMsg,{ 
			    id: 'msg', 
			    title: sysTitle,
			    width: 300, 
			    height: 100,
			    time:100, 
			    left: '98%',
			    top: '99%',
			    fixed: true,
			    drag: false,
			    resize: false
			});
		}
	}