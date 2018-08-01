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