function addItem(){
			var formid = $("#formid").val();
			var demId = $("#demId").val();
			var url = 'createFormInstance.action?formid='+formid+'&demId='+demId; 
			//获取表单默认值
			var params = $("#init_params").val();
			var extparams = '';
			if(params!=''){
				var p = params.split(";");
				for(var i=0;i<p.length;++i){
					extparams+="&"+p[i];
				}
				url = url+extparams;
			}
			var target = "_blank";
			var win_width = window.screen.width;
			var page = window.open('form/loader_frame.html',target,'width='+win_width+',height=800,top=50,left=150,location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=yes');
			page.location = encodeURI(url);
			return;   
		}
function reloadGrid(){
	jQuery("#iform_grid").trigger("reloadGrid");
	
}
function openFormPage(formId,instanceId,demId){
	var url = 'openFormPage.action?formid='+formId+'&instanceId='+instanceId+'&demId='+demId;
	var target = "dem"+instanceId;

	var win_width = window.screen.width;
	var page = window.open('form/loader_frame.html',target,'width='+win_width+',height=800,top=50,left=150,location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=yes');
	page.location = url; 
	
}
//执行删除操作
function remove(){
	var ids = [];
	var rows =jQuery("#iform_grid").jqGrid('getGridParam','selarrrow');
	for(var i=0;i<rows.length;i++){
		var ret = jQuery("#iform_grid").jqGrid('getRowData',rows[i]);
		ids.push(ret.INSTANCEID);
	}
	var temp = ids.join(',');
	var fid = $('#formid').val();
	var demId = $('#demId').val();
	if(temp==''){
		art.dialog.tips('请选择您要删除的行项目!');  
		return;
	}
	art.dialog.confirm('确认删除?',function(result){  
				 	if(result){
							jQuery.ajax({
								url: 'iformDataRemove.action',
								data:{
									removeList:temp,
									formid:fid,
									demId:demId
								},
								type: 'POST',
								success: function()
								{
									removeGridItem(rows);   //由于执行删除方法一次删不干净，调用函数进行循环递归执行删除操作
								}
							});
							removeGridItem(rows);
            }
		});
		}
		
//执行页面表格删除
function removeGridItem(rows){
			for(var i=0;i<rows.length;i++){
				jQuery("#iform_grid").jqGrid('delRowData',rows[0]);
			}
			rows =jQuery("#iform_grid").jqGrid('getGridParam','selarrrow');
			if(rows.length>0){
				removeGridItem(rows);
			}
		}
function showlog(){
	var formid = $("#formid").val();
	var demId = $("#demId").val();
	var pageUrl = "sysForm_showlog.action?formid="+formid+"&modelId="+demId;
	art.dialog.open(pageUrl,{
		id:'showlogDialog',
    	title:"操作日志",
		lock:true,
		background: '#999', // 背景色
	    opacity: 0.87,	// 透明度
	    width:'90%',
	    height:'90%'
	 });
}
function impExcel(){
//导入excel
	var formid = $("#formid").val();
	var demId = $("#demId").val();
	var pageUrl = "sysDem_Excel_impPage.action?formid="+formid+"&demId="+demId;
	art.dialog.open(pageUrl,{
		id:'ExcelImpDialog',
		title:"数据导入",
		lock:true,
		background: '#999', // 背景色
	    opacity: 0.87,	// 透明度
	    width:500,
	    height:300
	 });
}
function expExcel(){
//导入excel
	var pageUrl = "sysDem_Excel_DoExp.action";
	$("#ifrmMain").attr("action",pageUrl); 
	$("#ifrmMain").submit();
}