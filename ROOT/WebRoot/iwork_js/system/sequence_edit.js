var api = art.dialog.open.api, W = api.opener;
//关闭窗口
 function win_close(){
        api.close();
}
//异步保存
function save(){
    //先验证
    var sequencekey=$.trim($('#sequence_save_model_sequencekey').val());
    var sequencevalue=$.trim($('#sequence_save_model_sequencevalue').val());
    var sequencedesc=$.trim($('#sequence_save_model_sequencedesc').val());
    $('#sequence_save_model_sequencekey').val(sequencekey);
    $('#sequence_save_model_sequencevalue').val(sequencevalue);
    $('#sequence_save_model_sequencedesc').val(sequencedesc);
    
     if(sequencekey==""){
                 art.dialog.tips("序列键值不能为空！",2);
                 $('#sequence_save_model_sequencekey').focus();
                 return;
          }
     if(sequencevalue==""){
                 art.dialog.tips("序列号不能为空！",2);
                 $('#sequence_save_model_sequencevalue').focus();
                 return;
          }
     if(length2(sequencekey)>100){
                 art.dialog.tips('序列键值过长!',2);
                 $('#sequence_save_model_sequencekey').focus();
                 return ;
          }
     if(!/^[0-9]*[1-9][0-9]*$/.test(sequencevalue)){
                 art.dialog.tips('请输入数字格式的序列号!',2);
                 $('#sequence_save_model_sequencevalue').focus();
                 return ;
     }
     if(length2(sequencedesc)>200){
                 art.dialog.tips('序列描述过长!',2);
                 $('#sequence_save_model_sequencedesc').focus();
                 return ;
          }         
    var queryString = $('#sequence_save').serialize();
    $.post('sequence_save.action',queryString,function(data){
             art.dialog.tips(data,2);
		     api.close();
    });
}