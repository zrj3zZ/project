<#assign s=JspTaglibs["/WEB-INF/struts-tags.tld"] />
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Frameset//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-frameset.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset="UTF-8">
<title>${title}</title>
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
	<link rel="stylesheet" type="text/css" media="screen" href="iwork_css/jquerycss/validate/screen.css" />
	<link rel="stylesheet" type="text/css" href="iwork_css/formstyle.css"/>
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/jqgrid/ui.jqgrid.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/jqgrid/jquery-ui-1.8.2.custom.css">
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"   ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js"  ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/languages/grid.locale-cn.js"  ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.jqGrid.min.js"  > </script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.validate.js"   ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.metadata.js"   ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/languages/messages_cn.js"  ></script>
	
	<script type="text/javascript">
		${script} 
		$().ready(function() { 
			$("#iformMain").validate({
			debug:false,
				submitHandler:function(form){
					form.action = "saveIform.action";
					form.submit();
				} 
			});
			
		});
		function saveForm(){
		//	$.messager.alert('�����','���ڱ�������...');
			 document.getElementById("submitbtn").click();
			 /*
			jQuery.ajax({
					url: 'saveIform.action',
					data: $('#iformMain').serialize(),
					type: "POST",
					beforeSend: function()
					{  
						
					},
					success: function()
					{
						
					}
				});
				*/
		}
		
		function saveSubForm(obj){
			alert(obj.getGridParam("datatype"));
			//$.messager.alert('�����','���ڱ�������...');
			/*
			var ids = obj.getDataIDs();
			for(var i=0;i<ids.length;i++){
                    var cl = ids[i];
                    var row =  obj.getRowData(cl);
                    
            } */
			
		}
		
	</script>
	<style> 
		${style}
		#container {margin:0 auto; width:100%;}
		#iforminfo { width:200px; height:120px; background:#EDF7FE;}
		#infotitle{ background:#FEFDEF; padding:3px;border-bottom:1px solid #666;}
		.iformContent {background:#EDF7FE;padding:5px;overflow:auto;display:table;border-collapse:separate;}
		.info_row{display:table-row;}
		.info_row div {display:table-cell;border-bottom:1px dotted #666}
		.info_row .info_name {width:60px;font-size:12px;}
		.info_row .info_value {width:120px;font-size:12px;color:#E8A300;}
		.iformContent ul {margin:0 0 0 2px;padding:0;list-style:inside left;color:#E8A300;}
		.iformContent li {font-size:12px;display:block; margin:0 0 0 3px; LIST-STYLE-TYPE:disc}
		
		#iformMain label.error, #commentForm input.submit { margin-left: 253px; }
		#iformMain label.error {
			margin-left: 2px;
			width: auto;
			display: none;
			color:red;
		}
		#newsletter_topics label.error {
			display: none;
			margin-left: 103px;
		}
		.block { display: block; }
		form.iformMain label.error {display: none;}	

	</style>
</head>
<body class="easyui-layout">
	
	<div region="north" border="false" style="height:30px;background:#EFEFEF;padding-top:2px;text-align:center;padding-left:10px;">
		
	</div>
	<div region="east" split="false" title="������" style="width:220px;padding:0px;border:0px;">
		<div id="iforminfo">
							<div id="infotitle">��������������<font color="red">${info}</font></div>
								<div class="iformContent">
									<a href="javascript:saveForm();" class="easyui-linkbutton" plain="true" iconCls="icon-save">����</a>
								<a href="javascript:this.location.reload();" class="easyui-linkbutton" plain="true" iconCls="icon-reload">ˢ��</a>
								<a href="javascript:remove();" class="easyui-linkbutton" plain="true" iconCls="icon-print">��ӡ</a>
								<a href="javascript:window.close();" class="easyui-linkbutton" plain="true" iconCls="icon-cancel">�ر�</a>
								
								</div>
							</div>
						<div id="infotitle">ά����־</div>
							<div class="iformContent">
								
							</div>
						</div> 
		</div>
	</div>
	<div region="south"  style="height:15px;background:#A9FACD;border:0px;">
		
		
	</div>
	<div region="center" style="text-align:center;border-left:1px #999 dotted;border-right:1px #999 dotted;border-top:1px #999 dotted;border-bottom:0px #999 dotted;padding:2px;">
	<form   id="iformMain" name="iformMain" method="post" action='saveIform.action'>
		${content}
		<!--������-->
		<span style="display:none">
			<@s.hidden name="formid"/>
			<@s.hidden name="instanceId"/> 
			<@s.hidden name="formContent"/>
			<input name='submitbtn' id='submitbtn' type="submit" />
			${param}
		</span>
	</form>
	</div>
	
</body>
</html>
<script language=JavaScript>  
     window.opener.location.href=window.opener.location.href;  
</script> 
