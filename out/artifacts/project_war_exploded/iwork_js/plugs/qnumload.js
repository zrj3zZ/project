function dataEdit(cid){
	$.ajax({
		type: 'POST',
		url: 'dataedit.action',
		data: {'id':cid},
		dataType: 'text',
		success: function(data,status){
		 
		 $('#hiddiv').html(data);		
		$('#hiddiv').dialog('open');
		
		}
		
	});
	
}


function datasave(cid){

var nameedit = $('#nameedit').val();
var mobilenumedit=$('#mobilenumedit').val();
var extend1edit=$('#extend1edit').val();
var extend2edit=$('#extend2edit').val();
var extend3edit=$('#extend3edit').val();
var typeedit=$('#typeedit1').val();
if(typeedit==""){
 	 	 $.messager.alert('警告','请选择分组！');		  
		   return false;
 	  }
if(nameedit.length>10){
	$.messager.alert('警告','姓名长度过长!最大长度是10，目前长度是'+namevalue.length+'。');
		return false;
		}
	
if(mobilenumedit==''){
		$.messager.alert('警告','手机号必须填写！');
		return false;
	}
	var mobiletest=/^((13[0-9]{1})|(15[0-9]{1})|(18[0-9]{1})|145|147)+\d{8}$/;
if(mobilenumedit!=""&&!mobiletest.test(mobilenumedit)){
	$.messager.alert('警告','请输入有效的手机号码！！');
		return false;
    }
   
     
    if(extend1edit!=""&&extend1edit.length>10){
     	$.messager.alert('警告','属性1长度过长');
     	return false;
     } 
  
     if(extend2edit!=""&&extend2edit.length>10){
     	$.messager.alert('警告','属性2长度过长');
     	return false;
     } 
    
    
     if(extend3edit!=""&&extend3edit.length>10){
     	$.messager.alert('警告','属性3长度过长');
     	return false;
     } 
     document.forms[0].hcid11.value=cid;
         document.forms[0].hname1.value=nameedit;
         document.forms[0].hattr11.value=extend1edit;
         document.forms[0].hattr21.value=extend2edit;
         document.forms[0].hattr31.value=extend3edit;
         document.forms[0].htypee1.value=typeedit;
         document.forms[0].hmobile1.value=mobilenumedit;
     
     var url='saveeditnumj.action';			
						document.forms[0].action=url;  
						document.forms[0].method="post"
						document.forms[0].target="hidden_frame";            
						document.forms[0].submit();
		                 $('#hiddiv').dialog('close');
		                window.location.reload();
		         		//parent.window.location.reload();

  
	
}

//删除号码
function remove(cid){
				 $.messager.confirm('确认','确定删除吗?',function(result){  
				 	if(result){
				 	        document.forms[0].hcid2.value=cid;
	                    	document.forms[0].action='delnumj.action';
			 				document.forms[0].submit();
			 			}
                    }
            )
		}
//新增分组
function addType(){

			$('#typewindow').window('open');
		
		}
//分组窗口取消
function cancel(){
			$('#typewindow').window('close');
		}
//删除分组
function removetype(){
		   var node = $('#typetree').tree('getSelected');
		   if(node==null){
		   $.messager.alert('警告','请选择要删除的分组！');
		   }else{
				 $.messager.confirm('确定','确定要删除吗?',function(result){  
				 	if(result){
	                	var node = $('#typetree').tree('getSelected');
						if (node){
						if(node.id!=0){
	                    	document.forms[0].action="removetype.action?cid="+node.id;
			 				document.forms[0].submit();
			 				}else{
			 				$.messager.alert('警告','不能删除未分组!');
			 				}
			 			}
                    }
            })
            }
		}
//修改分组
function edittype(){
		   var node = $('#typetree').tree('getSelected');
		   if(node==null){
		   $.messager.alert('警告','请选择要修改的分组！');
		   }else if(node.id==0){
		   $.messager.alert('警告','不能修改未分组!');
            }else{
		   document.forms[0].htypeeditid.value=node.id; 
		   document.getElementById("typeeditj").value=node.text; 
		   
				$('#typeedit').window('open');
            }
		}
//新增号码
function addNumj(){
		 var node = $('#typetree').tree('getSelected');
		   if(node==null){
		   $.messager.alert('警告','请选择要增加号码所属的分组！');		  
		   return false;
		   }else{
		   	  document.forms[0].htypeeditid.value=node.id 
		   }
 	   
	var name=document.getElementsByName("name");
	var namevalue=name[0].value;
     if(namevalue.length>10){
		$.messager.alert('警告','姓名长度过长!最大长度是10，目前长度是'+namevalue.length+'。');
		return false;
		}
		if(namevalue!=""&&namevalue.length<11){
		document.forms[0].hname.value=namevalue;
		}
	var mobile=document.getElementsByName("mobilenum");
	var mobilevalue=mobile[0].value;
	if(mobilevalue==''){
		$.messager.alert('警告','手机号必须填写！');
		return false;
	}
	var mobiletest=/^((13[0-9]{1})|(15[0-9]{1})|(18[0-9]{1})|145|147)+\d{8}$/;
       if(mobilevalue!=""&&!mobiletest.test(mobilevalue)){
		$.messager.alert('警告','请输入有效的手机号码！');       
		return false;
      }
       document.forms[0].hmobile.value=mobilevalue;
     var extend1=document.getElementsByName("extend1");
     var extend1value=extend1[0].value;
     if(extend1value!=""&&extend1value.length>10){
     	$.messager.alert('警告','属性1长度过长');
     	return false;
     } 
     if(extend1value!=""&&extend1value.length<11){
        document.forms[0].hattr1.value=extend1value;

     } 
	 var extend2=document.getElementsByName("extend2");
     var extend2value=extend2[0].value;
     if(extend2value!=""&&extend2value.length>10){
     	$.messager.alert('警告','属性2长度过长');
     	return false;
     } 
     if(extend2value!=""&&extend2value.length<11){
        document.forms[0].hattr2.value=extend2value;

     } 
     var extend3=document.getElementsByName("extend3");
     var extend3value=extend3[0].value;
     if(extend3value!=""&&extend3value.length>10){
     	w$.messager.alert('警告','属性3长度过长');
     	return false;
     } 
     if(extend3value!=""&&extend3value.length<11){
        document.forms[0].hattr3.value=extend3value;

     } 
	 	    var url='addnumj.action'
            document.forms[0].action=url;  
            document.forms[0].method="post"; 
			document.forms[0].target="hidden_frame";            
            document.forms[0].enctype="multipart/form-data"           
            document.forms[0].submit();
            alert("添加成功");
            window.location.reload();
            $("#name").val(null);
            $("#mobilenum").val(null);
            $("#extend1").val(null);
            $("#extend2").val(null);
            $("#extend3").val(null);
            
           
		}
//查询号码
function qNumj(){
          
            var node = $('#typetree').tree('getSelected');
		   if(node==null){
		  document.forms[0].htypeeditid.value="";
		   }else{
		   	  document.forms[0].htypeeditid.value=node.id 
		   }
 	   
	var name=document.getElementsByName("name");
	var namevalue=name[0].value;
    document.forms[0].hname.value=namevalue;
	
	var mobile=document.getElementsByName("mobilenum");
	var mobilevalue=mobile[0].value;
       document.forms[0].hmobile.value=mobilevalue;
     var extend1=document.getElementsByName("extend1");
     var extend1value=extend1[0].value;
     
        document.forms[0].hattr1.value=extend1value;

    
	 var extend2=document.getElementsByName("extend2");
     var extend2value=extend2[0].value;
     
        document.forms[0].hattr2.value=extend2value;

     
     var extend3=document.getElementsByName("extend3");
     var extend3value=extend3[0].value;
     
        document.forms[0].hattr3.value=extend3value;


	 	    var url='qnumload.action'
            document.forms[0].action=url;  
            document.forms[0].method="post";          
            document.forms[0].enctype="multipart/form-data"           
            document.forms[0].submit(); 
           
         
}