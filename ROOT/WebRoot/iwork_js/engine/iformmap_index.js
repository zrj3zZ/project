$(document).ready(function(){
		var lastIndex;
			$('#iformmap_grid').datagrid({
				onBeforeLoad:function(){
					$(this).datagrid('rejectChanges');
				},
				onDblClickRow:function(rowIndex){
				var row = $('#iformmap_grid').datagrid('getSelected');
					openMapModify(row.ID); 
				}
			});
			$('#subTable_grid').datagrid({
				idField:'ID',
				onBeforeLoad:function(){
					$(this).datagrid('rejectChanges');
				} 
			});
		});		
		//保存提交基础信息设置
		function doSubmit(){
			if($("#baseInfoForm_sysEngineIform_iformTitle").val()==''){
				art.dialog.tips("提示", "“表单名称”不能为空");
				return;
			}
			if($("#baseInfoForm_sysEngineIform_visitType").val()==''){
				art.dialog.tips("提示", "“访问类型”不能为空");
				return;
			}
			if($("#baseInfoForm_sysEngineIform_master").val()==''){
				art.dialog.tips("提示", "“管理员”不能为空");
				return;
			}
			if($("#baseInfoForm_sysEngineIform_iformTemplate").val()==''){
				art.dialog.tips("提示", "“表单模版”不能为空");
				return;
			}
			document.forms["baseInfoForm"].action="sysEngineIFormMap_saveBaseInfo.action";
			document.forms["baseInfoForm"].submit();

		}
		
		function addFieldItem(){
			var formid = $("#formid").val();
			var pageUrl = 'sysEngineIFormMap_add.action?formid='+formid;
			 art.dialog.open(pageUrl,{
				 id:'formMapIndexWinDiv',
				 title:'新增虚拟表单域',
				 lock:true,
				 background: '#999', // 背景色
			     opacity: 0.87,	// 透明度
			     width:600,
			     height:450
			 });
		}
		//修改属性信息
		function openMapModify(id){
			var pageUrl = 'sysEngineIFormMap_show.action?id='+id;
			 art.dialog.open(pageUrl,{
				 id:'formMapIndexWinDiv',
				 title:'表单域设置',
				 lock:true,
				 background: '#999', // 背景色
			     opacity: 0.87,	// 透明度
			     width:600,
			     height:450
			 });
			/*
			$('#updateForm').form('clear');
			//每次生成新的url避免调用重复URL对结果进行缓存，造成数据滞后
			var viewdate =new Date();
			var time = viewdate.getTime();
			$('#updateStyleForm').form('load','sysEngineIFormMap!loadMapInfo.action?id='+id+"&datetime="+time);
			$('#ifrom_mapstyle').window('open');
			*/
		}
		function removeFieldItem(){
			var rows = $('#iformmap_grid').datagrid('getSelections');
			var ids = [];
	 		for(var i=0;i<rows.length;i++){
	 			ids.push(rows[i].ID);
	 		}
			   if(rows.length==0){ 
				   art.dialog.tips('警告','请选择要移除的表单域!');
			   }else{
				   if(confirm("确认删除选中的表单域吗?")){  
						var pageurl = "sysEngineIFormMap_remove.action?ids="+ids.join(',');
						$.ajax({   
				            type:'POST',
				            url:pageurl,
				            success:function(msg){ 
				            	  if(msg=="success"){
				                  	alert("移除成功!");
				                  	refrenshGrid();
				                  }  
				                  else if(responseText=="error"){
				                     alert("移除失败!");
				                  } 
				            }
				        });
					}
	            }
		}
		function refrenshGrid(){
			$('#iformmap_grid').datagrid('reload');
		}
		//修改查询条件属性信息
		function openSearchItemModify(id){ 
			$('#updateSearchForm').form('clear');
			//每次生成新的url避免调用重复URL对结果进行缓存，造成数据滞后
			var viewdate =new Date();
			var time = viewdate.getTime();
			$('#updateSearchForm').form('load','iformSearchMap_load.action?id='+id+"&datetime="+time);
			$('#ifrom_search').window('open'); 
		}
		
		//保存提交SearchMAP外观设置
		function doSearchMapSubmit(){
			if($("#iformSearchMap_isnull").val()==''){
				art.dialog.tips("提示", "“是否允许为空”不能为空");
				return;
			}
			
			if($("#iformSearchMap_inputHeight").val()==''){
				art.dialog.tips("提示", "“录入高度”不能为空");
				return;
			}
			if($("#iformSearchMap_inputWidth").val()==''){
				art.dialog.tips("提示", "“录入宽度”不能为空");
				return;
			}
			if($("#iformSearchMap_displayType").val()==''){
				art.dialog.tips("提示", "“外观组件”不能为空");
				return;
			}
			if($("#iformSearchMap_compareType").val()==''){
				art.dialog.tips("提示", "“符号”不能为空");
				return;
			}	
			var formid = $('#formid').val();
			document.forms["updateSearchForm"].action="iformSearchMap_update.action?formid="+formid;
			document.forms["updateSearchForm"].submit();  
		}
		function doSearchMapcancel(){
			$('#ifrom_search').window('close');
		}
		
		//保存提交MAP外观设置
		function doStyleSubmit(){
			if($("#iformStyleMap_fieldTitle").val()==''){
				art.dialog.tips("提示", "域“标题”不能为空");
				return;
			}
			
			if($("#iformStyleMap_displayType").val()==''){
				art.dialog.tips("提示", "域“外观类型”不能为空");
				return;
			}
			if($("#iformStyleMap_displayType").val()==''){
				art.dialog.tips("提示", "显示“宽度”不能为空");
				return;
			}			
			document.forms["updateStyleForm"].action="sysEngineIFormMap_update.action";
			document.forms["updateStyleForm"].submit();
		}
		//添加子表
		function addSubForm(){
			$('#addSubForm_window').window('open');
		}
		function addSubFormcancel(){
			$('#addSubForm_window').window('close');
		}
		
		//提交子表添加
		function addSubFormSubmit(){
			if($("#subformTitle").val()==''){
				art.dialog.tips("提示", "“关联子表”不能为空");
				return;
			}
			if($("#subForm_title").val()==''){
				art.dialog.tips("提示", "“子表标题”不能为空");
				return;
			}
			document.forms["addSubForm"].action="sysEngineSubform!save.action";
			document.forms["addSubForm"].submit();
		}
		
		//删除子表
		function removeSubForm(){
			art.messager.confirm('确认删除?',function(result){  
				var iformid = $('#formid').val();
				 	if(result){
				 		var rows = $('#subTable_grid').datagrid('getSelections');
				 		var ids = [];
					 		for(var i=0;i<rows.length;i++){
							ids.push(rows[i].ID);
						}
	                	document.forms["addSubForm"].action="sysEngineSubform!remove.action?formid="+iformid+"&id="+ids.join(',');
						document.forms["addSubForm"].submit();
                    }
            })
		}
		
		function openSubFormTree(){
		$('#subformTreewindow').window('open');
			$('#subformtree').tree({   
	                 url: 'sysEngineIForm!openjson.action',   
	               	onClick:function(node){
	               		if(node.attributes.type=='iform'){
	               			$("#subformid").val(node.id);
	               			$("#subformTitle").val(node.text);
	               			$("#subForm_title").val(node.text);
	               			$('#subformTreewindow').window('close');
	               		}
	               }
	             });
		}
		
		function isResizeChange(){
			var sel =  $('#isResize').val();
			if(sel=='0'){
				tr_height.style.display="none";
			}else{
				tr_height.style.display="";
			}
			
		}
		function isAutoWidth(){
			var sel =  $('#autoWidth').val();
			if(sel=='0'){
				document.getElementById("tr_gridWidth").style.display="none";
			}else{
				document.getElementById("tr_gridWidth").style.display="";
			}
		}
		function stylecancel(){
			$('#ifrom_mapstyle').window('close');
		}
		function arrow_up(id,upid){
			var row = $('#iformmap_grid').datagrid('selectRow',id);
			alert(row[0].name);
		}
		
		function moveUp(){
		    var formid=$('#formid').val();
		    var rows = $('#iformmap_grid').datagrid('getSelections');
		    if(rows.length==0){
		        alert("请选择要移动的行记录");
		         return;
		    }
		    if(rows.length>1){
		        alert("选择行过多");
		         return;
		    }
		    var url='ifromMap_arrow_up.action';
		    $.post(url,{mapid:rows[0].ID,formid:formid},function(data){
		         if(data=='ok'){
		             $('#iformmap_grid').datagrid('reload');
		         }
		    });
		}
		function moveDown(){
		    var formid=$('#formid').val();
		     var rows = $('#iformmap_grid').datagrid('getSelections');
		    if(rows.length==0){
		        alert("请选择要移动的行记录");
		         return;
		    }
		    if(rows.length>1){
		        alert("选择行过多");
		         return;
		    }
		    var url='ifromMap_arrow_down.action';
		    $.post(url,{mapid:rows[0].ID,formid:formid},function(data){
		         if(data=='ok'){
		             $('#iformmap_grid').datagrid('reload');
		         }
		    });
		}
		function moveTop(){
			var formid=$('#formid').val();
		    var rows = $('#iformmap_grid').datagrid('getSelections');
		    if(rows.length==0){
		        alert("请选择要移动的行记录");
		         return;
		    }
		    if(rows.length>1){
		        alert("选择行过多");
		         return;
		    }
		    var url='ifromMap_arrow_first.action';
		    $.post(url,{mapid:rows[0].ID,formid:formid},function(data){
		         if(data=='ok'){
		            $('#iformmap_grid').datagrid('reload');
		         }
		    });
		}
		function moveBottom(){
			var formid=$('#formid').val();
		    var rows = $('#iformmap_grid').datagrid('getSelections');
		    if(rows.length==0){
		        alert("请选择要移动的行记录");
		         return;
		    }
		    if(rows.length>1){
		        alert("选择行过多");
		         return; 
		    }
		    var url='ifromMap_arrow_last.action';
		    $.post(url,{mapid:rows[0].ID,formid:formid},function(data){
		         if(data=='ok'){
		             $('#iformmap_grid').datagrid('reload');
		         }
		    });
		}
