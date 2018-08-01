var mainFormValidator;
$(document).ready(function(){
	mainFormValidator =  $("#editForm").validate({
		 errorPlacement: function (error, element) {
            error.appendTo(element.parent());    //将错误信息添加当前元素的父结点后面               
       }
  }); 
   mainFormValidator.resetForm();
	$("#cn_to").autocomplete('iwork_mail_showaddress_json.action', {
		multiple: true,
		minChars:2,
		dataType: "json",
		matchContains: true,    //包含匹配，就是data参数里的数据，是否只要包含文本框里的数据就显示
		multipleSeparator:",",
		width: 500, 
		autoFill:false,    //自动填充
		parse: function(data) {
		return $.map(data, function(row) {
			return {
				data: row,
				value: row.name,
				result: row.username
			}
		});
	},
	formatItem: function(item) {
		return item.name;
	}
	}).result(function(e, item) {
		var type = item.type;
		var fullname = item.to;
		try{
			 var toVal = $("#_to").val();
			 if(toVal!=''){
				 toVal = toVal+","+fullname;
				 $("#_to").val(toVal);
			 }else{
				 $("#_to").val(fullname);
			 }
		}catch(e){
			
		}
	}); 
	$("#_cc").autocomplete('iwork_mail_showaddress_json.action', {
		multiple: true,
		minChars:2,
		dataType: "json",
		matchContains: true,    //包含匹配，就是data参数里的数据，是否只要包含文本框里的数据就显示
		multipleSeparator:",",
		width: 500, 
		autoFill:true,    //自动填充
		parse: function(data) {
		return $.map(data, function(row) {
			return {
				data: row,
				value: row.name,
				result: row.username
			}
		});
	},
	formatItem: function(item) {
		return format(item);
	}
	}).result(function(e, item) {
		var fullname = item.to;
		try{
			 var toVal = $("#_to").val();
			 if(toVal!=''){
				 toVal = toVal+","+fullname;
				 $("#_to").val(toVal);
			 }else{
				 $("#_to").val(fullname);
			 }
			 alert(fullname);
		}catch(e){
			
		}
	}); 
 });

function format(row) {
	return row.name + " &lt;" + row.to + "&gt";
}

/**
 * 
 */
	var editor;
	KindEditor.ready(function(K) {
		editor = K.create('textarea[name="model._content"]', {
			cssPath : 'iwork_css/plugs/mailContent.css',
			resizeType : 1,
			width:"98%",
			height:"400px",
			allowPreviewEmoticons : false,
			filterMode:true,
			allowImageUpload : false,
			newlineTag:'br',
			items : [
				'fontname', 'fontsize', '|', 'forecolor', 'hilitecolor', 'bold', 'italic', 'underline',
				'removeformat', '|', 'justifyleft', 'justifycenter', 'justifyright', 'insertorderedlist',
				'insertunorderedlist', '|', 'emoticons', 'image', 'link']
		});
	});
	
function doSender(){
	var valid = mainFormValidator.form(); //执行校验操作
	if(!valid){ 
		return false;
	}else{ 
		editor.sync();//同步html编辑器
		$("#editForm").attr("action","iwork_email_dosend.action");
		$("#editForm").submit(); 
	}
}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	