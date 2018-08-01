

function selectMultiWBSNode(defaultField,treeNode){
	var taskName = treeNode.PROJ_NAME;
	var taskNo = treeNode.PROJ_CODE;
	
	$("#TASKNAME").val(taskName);
	$("#TASKNO").val(taskNo);
	$("#XHBH").val(treeNode.TOP_ID);
	$("#XHBT").val(treeNode.TOP_NAME);
	getHtQdzb(taskNo);
}

function getHtQdzb(taskid){
		
	var pageUrl="getTaskNoQdzb_action.action";
    $.post(pageUrl,{taskId:taskid},function(data){
   		
   		var proInfo = JSON.parse(data);
   		
   		var str = "合同签订指标："+proInfo.QDZB +"|合同支出指标："+proInfo.ZCZB;
   		$("#HTZB").val(str);

    });
 
 }

function checkJe(obj){
	
	var taskid = $("#TASKNO").val();	
	var pageUrl="getTaskNoQdzb_action.action";
    $.post(pageUrl,{taskId:taskid},function(data){
   		
   		var proInfo = JSON.parse(data);
   		
   		if(proInfo.QDZB<obj.value){
   			alert("该任务下合同金额不能超过合同签订指标");
   			$("#JE").val("");
   		}
   		

    });
}
