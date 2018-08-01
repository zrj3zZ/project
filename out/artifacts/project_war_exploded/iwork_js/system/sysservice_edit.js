var api = art.dialog.open.api, W = api.opener;
//关闭窗口
function win_close(){
        api.close();
}
//保存并验证,异步保存
function save(){ 
	
        var servicename=$.trim($('#servicename').val());
        var servicekey=$.trim($('#servicekey').val());
        var servicedesc=$.trim($('#servicedesc').val());
        var startdate=  $.trim($("#startdate").val());
        var enddate= $.trim($("#enddate").val());
        if(servicename==""){
                 art.dialog.tips("服务名称不能为空！",2);
                 $('#servicename').focus();
                 return;
          }
        if(servicekey==""){
                 art.dialog.tips("服务键值不能为空！",2);
                 $('#servicekey').focus();
                 return;
          }
        if(servicedesc==""){
                 art.dialog.tips("服务描述不能为空！",2);
                 $('#servicedesc').focus();
                 return;
          }
        if(startdate==""||enddate==""){
                 art.dialog.tips("有效日期不能为空！",2);
                 return;                
        }
        if(length2(servicename)>200){
                 art.dialog.tips('服务名称过长!',2);
                 $('#servicename').focus();
                 return ;
          }
        if(length2(servicekey)>200){
                 art.dialog.tips('服务键值过长!',2);
                 $('#servicekey').focus();
                 return ;
          }
        if(length2(servicedesc)>200){
                 art.dialog.tips('服务描述过长!',2);
                 $('#servicedesc').focus();
                 return ;
          }
        if(!IsDate(startdate)||!IsDate(enddate)){
                 art.dialog.tips("请输入yyyy-mm-dd格式的有效日期！",2);
                 return; 
        }   
        var queryString = $('#sysService_save').serialize();
        $.post('sysService_save.action',queryString,function(data){
             art.dialog.tips(data,2);
		     api.close();
           });       
}