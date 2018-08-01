//保存表格
function doSubmit(){
		var options = {
			error:errorFunc,
			success:showResponse 
		};
		$("#editForm").ajaxSubmit(options);
	}
errorFunc = function(){
		art.dialog.tips("保存失败",2);
	}
showResponse = function(){
		art.dialog.tips("保存成功",2);
	}


function setFormLayout(obj,val){
	$("#formLayoutSet").val(val);
	var list = $("#formLayoutDiv").find(".flowbox_select");
	 for(var i=0;i<list.length;i++){
	 	var item = list.get(i);
	 	$(item).attr("class","flowbox");
	 }
	 $(obj).attr("class","flowbox_select");
}
function setLookAndFeel(obj,val){
	$("#skinLayoutSet").val(val);
	var list = $("#skinLayoutDiv").find(".flowbox_select");
	 for(var i=0;i<list.length;i++){
	 	var item = list.get(i);
	 	$(item).attr("class","flowbox");
	 }
	 $(obj).attr("class","flowbox_select");
}