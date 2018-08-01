<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<title>IFORM Designer</title>
	<meta content="text/html; charset=gb2312" http-equiv="content-type"/>
	<link type="text/css" rel="stylesheet" href="/ckeditor/ckeditor/_samples/sample.css" />
	<script type="text/javascript" src="/ckeditor/ckeditor/ckeditor_source.js"></script>
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js" charset="gb2312"></script>
	<script type="text/javascript" src="iwork_js/commons.js" charset="gb2312"></script>
	<link href="iwork_css/public.css" rel="stylesheet" type="text/css" />
	
	<script type="text/javascript" src="iwork_js/engine/sysengineiformdesginer_show.js"></script>
	
</head>
<body class="easyui-layout" style="background-color:#FFFFFD">
<div region="center" style="padding:3px;border-top:0px;height:500px;">
<form name="editForm" action="sysEngineIformDesginer_save.action" method="post">
 			<div style="border-bottom:1px solid #efefef;margin-bottom:3px;background-color:#FFFFDD">
							<a href="javascript:doSubmit();" class="easyui-linkbutton" plain="true" iconCls="icon-save">保存</a>
							<a href="javascript:this.location.reload();" class="easyui-linkbutton" plain="true" iconCls="icon-reload">刷新</a>
							<a href="javascript:doPreview(<s:property value='formid'  escapeHtml='false'/>);" class="easyui-linkbutton" plain="true" iconCls="icon-reload">预览</a>
			</div>
			<textarea name="htmlEditor"  cols="80" rows="12" ><s:property value='iformPage'  escapeHtml='false'/></textarea>
				<script type="text/javascript">
					var editor = CKEDITOR.replace( 'htmlEditor',
				    {
				       // customConfig : '/ckeditor/ckeditor/config.js',
				        skin : 'kama',
				        uiColor: '#EFEFEF', 
				        extraPlugins : 'devtools',
				        extraPlugins : 'tableresize',
				        extraPlugins:'footnote'
				        
				    });
				    /*
				  editor.on( 'pluginsLoaded', function( ev )
				 {
					// If our custom dialog has not been registered, do that now.
					if ( !CKEDITOR.dialog.exists( 'myDialog' ) )
					{
						// We need to do the following trick to find out the dialog
						// definition file URL path. In the real world, you would simply
						// point to an absolute path directly, like "/mydir/mydialog.js".
						var href = document.location.href.split('/');
						href.pop();
						href.push( 'footnote', 'dialogs/footnote.js' );
						href = href.join( '/' );
						CKEDITOR.dialog.add( 'footnote', href );
					}

					editor.addCommand( 'footnote', new CKEDITOR.dialogCommand( 'footnote' ) );
					editor.ui.addButton( 'Footnote',
						{
							label : 'footnote',
							command : 'footnote',
							className:'cke_button_selectAll'
						} );
				});
				    */
				   
				</script>
				<s:hidden name="formid"></s:hidden>
			</form>
	</div>
	
</body>
</html>
