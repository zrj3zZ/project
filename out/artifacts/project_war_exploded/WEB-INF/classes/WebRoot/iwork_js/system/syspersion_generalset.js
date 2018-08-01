//保存表格
function doSubmit(){
		var options = {
			error:errorFunc,
			success:showResponse 
		};
		$("#editForm").ajaxSubmit(options);
	}
errorFunc = function(){
		lhgdialog.tips("保存失败",2);
	}
showResponse = function(){
		lhgdialog.tips("保存成功",2);
	}
//设置工作状态
$(document).ready(function(){
		$("#workStatusOption").bind("click", function(){
			if($("#workStatusOption").val()=="其他"){
				$("#editForm_userModel_workStatus").val("");
				$("#editForm_userModel_workStatus").show();
			}else{
				$("#editForm_userModel_workStatus").val($("#workStatusOption").val());
				$("#editForm_userModel_workStatus").hide();
			}
		});
		if($("#editForm_userModel_workStatus").val()=="工作中"){$("#editForm_userModel_workStatus").hide();$("#workStatusOption").val("工作中");}
		else if($("#editForm_userModel_workStatus").val()=="出差中"){$("#editForm_userModel_workStatus").hide();$("#workStatusOption").val("出差中");}
		else if($("#editForm_userModel_workStatus").val()=="休假中"){$("#editForm_userModel_workStatus").hide();$("#workStatusOption").val("休假中");}
		else if($("#editForm_userModel_workStatus").val()=="加班中"){$("#editForm_userModel_workStatus").hide();$("#workStatusOption").val("加班中");}
		else{
			$("#editForm_userModel_workStatus").show();
			$("#workStatusOption").val("其他");
		}
	});
//弹出上传头像窗口
function add_image(){
		var userid = $('#editForm_userModel_userid').val();
		var pageUrl = "syspersion_photo.action?userid="+userid;
		art.dialog.open(pageUrl,{
	        id:'dg_addImage',  
	        title:'上传头像',
	        iconTitle:false, 
	        width:500,
	        heigh:500,
	        close:function(){
	        	window.location.reload();
	        }
		});
	}