$(function(){
			 $('#groupwindow').window('close');
			//加载导航树
			$('#grouptree').tree({   
                 url: 'sysEngineGroup!openjson.action',   
               	 onBeforeExpand:function(node){
               	 	//alert(node.attributes.type);
                     $('#grouptree').tree('options').url = "sysEngineGroup!openjson.action?parentid=" + node.id;// change the url                       
                 },             
               onClick:function(node){
               	//	alert(node.state);
               		if(node.state=='closed'){
               			$(this).tree('toggle', node.target);
               		}else if(node.state=='open'){
               			$(this).tree('toggle', node.target);
               		}else{
               			addTab(node.text,node.attributes.url,node.iconCls);
               		}      
               },
               onDblClick:function(node){
               		updateItem(node);
               }
                 
             });
		});
		
		function updateItem(node){
			//alert(node.attributes.parentid);
			document.forms[0].sysEngineGroup_model_parentid.value=node.attributes.parentid;
			document.forms[0].sysEngineGroup_model_id.value=node.id;
			document.forms[0].sysEngineGroup_model_groupmemo.value=node.attributes.groupmemo;
			document.forms[0].sysEngineGroup_model_master.value=node.attributes.master;
			document.forms[0].sysEngineGroup_model_groupname.value=node.text;
			var parent_node = getParentNode(node);
		//	alert(parent_node);
			if(node.attributes.parentid!="0")
				document.forms[0].parentname.value=parent_node.text;
			else{
				document.forms[0].parentname.value="";
			}
			$('#groupwindow').window('open');
		}
		
		//添加首级目录
		function addItem(){
			document.forms[0].sysEngineGroup_model_parentid.value="0";
			document.forms[0].sysEngineGroup_model_id.value="0";
			document.forms[0].parentname.value = "";
			$('#groupwindow').window('open');
		}
		//添加子目录
		function addSubItem(){
			var node = getSelected();
			if(node==null){
				alert("请在左侧结构树中选择父目录");
				return ;
			}else{
				document.forms[0].parentname.value = node.text;
				document.forms[0].sysEngineGroup_model_id.value = "0";
				document.forms[0].sysEngineGroup_model_parentid.value = node.id;
			}
			$('#groupwindow').window('open');
		}
		//获得当前选中节点
		function getSelected(){
			var node = $('#grouptree').tree('getSelected');
			return node;
		}
		function getChildren(node){
			return $('#grouptree').tree('getChildren', node.target);
		}
		
		//获得当前选中节点
		function getParentNode(node){
			var pnode = $('#grouptree').tree('getParent', node.target);
			return pnode;
		}
		//退出
		function cancel(){
			$('#groupwindow').window('close');
		}
		//保存提交
		function doSubmit(){
			$('#sysEngineGroup').submit();
		}
		//执行删除操作
		function remove(){
				 art.dialog.confirm('确认删除?',function(result){  
				 	if(result){
	                	var node = getSelected();
	                	var sub_node = getChildren(node);
	                    	document.forms[0].action="sysEngineGroup!remove.action?id="+node.id;
			 				document.forms[0].submit();
                    }
            })
            /*
			var node = getSelected();
			var sub_node = getChildren(node);
			var r= false;
			if(sub_node==""){
				 r=confirm("您确认删除名称为["+node.text+"]的分类吗？")
			}else{
			 	r=confirm("您确认删除名称为["+node.text+"]的分类吗？执行删除后子分类将一并删除")
			}
			
			  if (r==true){
			  
			  }else{
			    retrun;
			  }
			  */
		//	document.forms[0].submit();
		}
		
		 $(document).ready(function(){
              $('#sysEngineGroup').validate({meta:"validate"});
           });