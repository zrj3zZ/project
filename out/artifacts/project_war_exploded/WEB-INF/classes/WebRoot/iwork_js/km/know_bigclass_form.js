 var api = art.dialog.open.api, W = api.opener;
//关闭窗口
 function win_close(){
        api.close();
}
//异步保存
var errorFunc=function(){
           art.dialog.tips("保存失败！",2);
      }
var successFunc=function(responseText, statusText, xhr, $form){
           if(responseText=="ok"){
              art.dialog.tips("保存成功！",2);
              setTimeout('win_close();',1000);
           }
      }
function saveItem(){
    //先验证
    var cname=$('#editForm_model_cname').val();
    var cexpert=$('#cexpert').val();
     if(cname==""){
                 art.dialog.tips("分类名称不能为空！",2);
                 $('#editForm_model_cname').focus();
                 return;
          } 
     if(length2(cname)>50){
                 art.dialog.tips('分类名称过长!',2);
                 $('#editForm_model_cname').focus();
                 return ;
          }
     if(length2(cexpert)>500){
                 art.dialog.tips('分类专家过长!',2);
                 $('#cexpert').focus();
                 return ;
          }         
     var options = {
					  error:errorFunc,
					  success:successFunc 
		  }; 
	$('#editForm').ajaxSubmit(options); 
}
/*多选地址薄*/
    function multi_book(defaultField) {
    	var code = document.getElementById(defaultField).value;	
		alert(code);
		if(code=="多个被邀请回答人请用空格分隔"){
		     code="";
		}  
		var url = "multiAddressBookAction!index.action?code=" + code;
		art.dialog.open(url,{
			 id:"radioBookDialog",
				title: '多选地址簿',
						background: '#999', // 背景色
					    opacity: 0.87,	// 透明度
					    width: 650,
						height: 550
					 });
		//$.dialog.data("paramObj",obj);
	}