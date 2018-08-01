/*$(function(){
	var setting = {
			check:{
				enable:true
			},
			view: {
				showLine: true, 
				selectedMulti: false
			},
			data: { 
				simpleData: {
					enable: true,
					idKey: "id",
					pIdKey: "pid",
					rootPId: 0
				}
			},  
			async: {
				enable:true,
				url:"ireport_designer_showFieldJson.action",
				dataType:"json",
				otherParam:{'reportId':'<s:property value='reportId' escape='false'/>','type':'<s:property value="showtype" escape='false'/>'}
			},
			callback: {
				onClick: onClick,
				onAsyncSuccess: onAsyncSuccess
			}
		};
				
	    function onClick(event, treeId, treeNode){
	    	var treeObj = $.fn.zTree.getZTreeObj(treeId);
	        if(treeNode.checked){
	        	treeObj.checkNode(treeNode, false, true);
	        }else{
	        	treeObj.checkNode(treeNode, true, true);
	        }
	    } 
	    function onAsyncSuccess(event, treeId, treeNode, msg){
	    	var feildsChoosen = new Array();
	    	feildsChoosen = <s:property value='fieldsChoosen' escape='false'/>; //已经字段变灰色
	    	for(var i=0;i<feildsChoosen.length;i++){
	    		var treeObj = $.fn.zTree.getZTreeObj(treeId);
	    		var nodes=treeObj.getNodesByParam('fieldName',feildsChoosen[i],null);
	    		for(var j=0;j<nodes.length;j++){
	    			if(nodes[j].type=='field'){
	    				treeObj.checkNode(nodes[j], true, true);
	    				treeObj.setChkDisabled(nodes[j], true);
	    			}
	    		}
	    	}  
	    }
	    $.fn.zTree.init($("#fieldTree"), setting);
});*/

//添加字段
function add(){
	var treeObj = $.fn.zTree.getZTreeObj("fieldTree");
	var nodes = treeObj.getCheckedNodes(true); 
	var fieldNameArray = new Array();  
	var fieldTitleArray = new Array();
	for(var i=0,j=0;i<nodes.length;i++){	
		if(nodes[i].type=='field'){  
			var fieldName = "";
			if(nodes[i].entityname!=""){
				fieldName = nodes[i].entityname+"."+nodes[i].fieldName;
			}else{
				fieldName = nodes[i].fieldName;
			}
			fieldNameArray[j] = fieldName;
			fieldTitleArray[j] =fieldName;  
			j++;
			//doAddItem(fieldName,nodes[i].fieldTitle);
		}
	}
	if(fieldNameArray.length==0){
		$.dialog.tips("请选择要添加的字段",1);
	}else{
		doAdd(fieldNameArray.join(),fieldTitleArray.join());
	}
}

function doAdd(fieldNameStr,fieldTitleStr){
	var reportId = $("#reportId").val();
	var type = $("#type").val();
	var url = '';
	if(type==1){
		url='ireport_designer_condition_quickadd.action';
	} else if(type==2){
		url='ireport_designer_showField_quickadd.action';
	} else{
		url='ireport_designer_orderField_quickadd.action';
	}

	$.post(url,{reportId:reportId,fieldName:fieldNameStr,fieldTitle:fieldTitleStr},function(data){
		if(data=='success'){
			location.reload();		             
		}else{
			$.dialog.tips("保存失败",1);
		}
	});
}  