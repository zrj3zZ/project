		function addItem(){
			var valid = mainFormValidator.form(); //执行校验操作
			if(!valid){
					return;
			}else{
				document.forms["editForm"].action="sysEngineMetadataMap_save.action";
				document.forms["editForm"].submit();
			}
		}
		
		function modifyBaseInfo(id){
			var pageUrl = "sysEngineMetadataMap_edit.action?id="+id;
			art.dialog.open(pageUrl,{
				id:'fieldSet',
				title:'字段设置',
				lock:true,
				background: '#999', // 背景色
			    opacity: 0.87,	// 透明度
			    width:550,
			    height:380
			 });
			
		}
		//全选
		function check_all(obj,cName) 
		{ 
		    var checkboxs = document.getElementsByName(cName);
		    for(var i=0;i<checkboxs.length;i++){checkboxs[i].checked = obj.checked;}
		}
		
		function selectType(obj){
			if(obj.value=='日期'||obj.value=='日期时间'){ 
				$("#fieldLength").rules("remove","required");
				document.forms["editForm"].fieldLength.value = "";
				document.forms["editForm"].fieldLength.disabled=true;
			}else{
				//$("#fieldLength").rules("remove","required");
				//$("#fieldLength").rules("add","required:true");
				document.forms["editForm"].fieldLength.disabled="";
			}
		}
		function autoCreateKey(){
			if($("#fieldname").val()==''){
				var title = $("#fieldtitle").val();
				var metadataid = $("#metadataid").val();
				if(title!=''&&metadataid!=''){
					var pageurl = 'sysEngineMetadataMap_fieldname_create.action';
					 $.post(pageurl,{metadataid:metadataid,metaMapTitle:title},function(msg){
		       				if(msg!=''){
					               		$("#fieldname").val(msg); 
					               }else{
					               		alert("提取失败");
					               }
		   				});
						
				}
			}
		}
		//删除MAP
		function delDataMap(){
			art.dialog.confirm('确认删除?',function(result){  
				 	if(result){
	                	document.forms["editForm"].action="sysEngineMetadataMap_remove.action";
						document.forms["editForm"].submit();
                    }
            })
		}
		//退出
		function cancel(){
		//	$('#updateForm').form('clear');
			$('#metadatamap_info').window('close');
		}
		//保存提交
		function doSubmit(){
			document.forms["updateForm"].action="sysEngineMetadataMap_update.action";
			document.forms["updateForm"].submit();
		}
		//保存外观信息提交操作
		function doStyleSubmit(){
			document.forms["updateForm"].action="sysEngineMetadataMap_update.action";
			document.forms["updateForm"].submit();
		}