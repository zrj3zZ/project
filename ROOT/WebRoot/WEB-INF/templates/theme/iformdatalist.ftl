<#assign s=JspTaglibs["/WEB-INF/struts-tags.tld"] />
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Frameset//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-frameset.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
	<link rel="stylesheet" type="text/css" href="../iwork_css/jquerycss/icon.css">
	<link rel="stylesheet" type="text/css" href="../iwork_css/jquerycss/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="../iwork_css/jquerycss/default/portal.css">
	<script type="text/javascript" src="../iwork_js/jqueryjs/jquery-3.0.4.min.js"></script>
	<script type="text/javascript" src="../iwork_js/jqueryjs/jquery.easyui.min.js" charset="gb2312"></script>
	<script type="text/javascript" src="../iwork_js/jqueryjs/plugins/jquery.portal.js"></script>
	<link href="../iwork_css/public.css" rel="stylesheet" type="text/css" />
	<script type="text/javascript" src="../iwork_js/iform_index.js" charset="gb2312"></script>
	<style type="text/css">
		.title{
			font-size:16px;
			font-weight:bold;
			padding:20px 10px;
			background:#eee;
			overflow:hidden;
			border-bottom:1px solid #ccc;
		}
		.t-list{
			padding:5px;
		}
		.formdata{
			padding-top:3px;
			padding-bottom:3px;
		}
	</style>
	<script type="text/javascript">
	$(function(){
	             $('#iform_grid').datagrid({
	             	url:"iformDataList.action?iform=<s:property value='ifromid'  escape='false'/>",
					loadMsg: "���ڼ�������...",
					fitColumns: true,
					singleSelect: true,
					idField:'ID',
					onDblClickRow:function(rowIndex){
						var row = $('#iform_grid').datagrid('getSelected');
						var url = 'sysEngineIFormMap!index.action?formid='+row.ID;
						window.parent.addTab(row.FORM_TITLE,url,'');
					}
				});
				
		})
	</script>
	</head>
<body class="easyui-layout">
<!-- TOP�� -->
	<div region="north" border="false" style="padding:0px;overflow:no"> 
		<div style="padding:2px;background:#efefef;border-bottom:1px solid #efefef">
			<a href="javascript:addMetaData();" class="easyui-linkbutton" plain="true" iconCls="icon-add">�½���</a>
			<a href="javascript:remove();" class="easyui-linkbutton" plain="true" iconCls="icon-remove">ɾ��</a>
			<a href="javascript:this.location.reload();" class="easyui-linkbutton" plain="true" iconCls="icon-reload">ˢ��</a>
			
		</div>
	</div>
	<div region="center" style="padding:3px;">
			<table id="iform_grid" style="margin:2px;"></table>
	</div>
</body>
	</head>
</html>
