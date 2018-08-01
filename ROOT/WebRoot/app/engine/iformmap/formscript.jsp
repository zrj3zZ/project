<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head> 
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<title>IWORK综合应用管理系统</title>
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/common.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/public.css" />
	<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/> 
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js" ></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.metadata.js"   ></script>
	<script type="text/javascript" src="iwork_js/commons.js" ></script> 
	<script type="text/javascript" src="iwork_js/plugs/jsbeautifier/beautify.js"></script>
	<script type="text/javascript" src="iwork_js/plugs/jsbeautifier/beautify-css.js"></script>
	<script type="text/javascript" src="iwork_js/plugs/jsbeautifier/unpackers/javascriptobfuscator_unpacker.js"></script>
	<script type="text/javascript" src="iwork_js/plugs/jsbeautifier/unpackers/urlencode_unpacker.js"></script>
	<script type="text/javascript" src="iwork_js/plugs/jsbeautifier/unpackers/p_a_c_k_e_r_unpacker.js"></script>
	<script type="text/javascript" src="iwork_js/plugs/jsbeautifier/unpackers/myobfuscate_unpacker.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.form.js"></script>
    <script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
	<script type="text/javascript">
		 var the = {
   		 	beautify_in_progress: false
     	 };
	  	if (/chrome/.test(navigator.userAgent.toLowerCase())) {
	    	String.prototype.old_charAt = String.prototype.charAt;
	    	String.prototype.charAt = function (n) { return this.old_charAt(n); }
	  	}
  	/*一些函数*/   
		//用于格式化 	 
		 function unpacker_filter(source) {
		    var trailing_comments = '';
		    var comment = '';
		    var found = false;
		    do {
		        found = false;
		        if (/^\s*\/\*/.test(source)) {
		            found = true;
		            comment = source.substr(0, source.indexOf('*/') + 2);
		            source = source.substr(comment.length).replace(/^\s+/, '');
		            trailing_comments += comment + "\n";
		        } else if (/^\s*\/\//.test(source)) {
		            found = true;
		            comment = source.match(/^\s*\/\/.*/)[0];
		            source = source.substr(comment.length).replace(/^\s+/, '');
		            trailing_comments += comment + "\n";
		        }
		    } while (found);
		    
		    if (P_A_C_K_E_R.detect(source)) {
		        // P.A.C.K.E.R unpacking may fail, even though it is detected
		        var unpacked = P_A_C_K_E_R.unpack(source);
		        if (unpacked != source) {
		            source = unpacker_filter(unpacked);
		        }
		    }
		    if (Urlencoded.detect(source)) {
		        source = unpacker_filter(Urlencoded.unpack(source))
		    }
		    if (JavascriptObfuscator.detect(source)) {
		        source = unpacker_filter(JavascriptObfuscator.unpack(source))
		    }
		    if (MyObfuscate.detect(source)) {
		        source = unpacker_filter(MyObfuscate.unpack(source))
		    }
		    return trailing_comments + source;
		 }

    

 //JS格式化
 function jsFormat(){
 	  var obj = $('#formjs');
      if ($.trim(obj.val())=="") {obj.val($.trim(obj.val()));art.dialog.tips("请先输入脚本！");obj.focus();return;}
 	  if (the.beautify_in_progress)  return;
 	  the.beautify_in_progress = true;
 	  var source = obj.val();
	    var indent_size = 4;             //1：缩进:1个tab，2：缩进2个空格，3：缩进3个空格，4：缩进4个空格，8：缩进8个空格。
	    var indent_char = indent_size == 1 ? '\t' : ' ';
	    var brace_style = 'collapse';    //collapse:'{'另起一行，expand：'{'‘}’另起一行，end-expand：‘}’另起一行。
	    var indent_scripts = 'keep';     //keep：，normal：，separate：。
	    var preserve_newlines = false;    //是否保留空白行。
	    var keep_array_indentation = false;     //数组缩进。
	    var space_before_conditional = true;    //if与()之间是否加空格。
     var opts = {
                indent_size: indent_size,
                indent_char: indent_char,
                preserve_newlines:preserve_newlines,
                brace_style: brace_style,
                keep_array_indentation:keep_array_indentation,
                space_after_anon_function:true,
                space_before_conditional: space_before_conditional,
                indent_scripts:indent_scripts
      };
    source = unpacker_filter(source);
    var v = js_beautify(source, opts);  
    obj.val(v);
    obj.focus(); 
    art.dialog.tips("成功格式化");                 
    the.beautify_in_progress = false;
 }
  function saveScript(){
		 	var formjs = $('#formjs').val();
		 	if(formjs.lenght>2000){
		 		art.dialog.tips("脚本内容超出数据库定义长度，请联系管理员"); 
		 		return false;
		 	}
		 	if(formjs==''){
		 		art.dialog.tips("表单样式脚本不允许为空"); 
		 		return false;
		 	}
		 	$('#editForm').attr('action','sysEngineIForm_formjs_save.action');
		 	 var options = {
					error:errorFunc,
					success:successFunc 
				   };
				   $('#editForm').ajaxSubmit(options);
		 }
		 errorFunc=function(){
           art.dialog.tips("保存失败！");
	      }
	      successFunc=function(responseText, statusText, xhr, $form){
	           if(responseText=="success"){
	              art.dialog.tips("保存成功！");
	           }else{
	           	  art.dialog.tips("保存失败！");
	           }
	      }
	</script>
</head>
<body class="easyui-layout"> 
<div region="north" border="false" style="height:40px;">
	<div  class="tools_nav">
		<a href="javascript:saveScript();" class="easyui-linkbutton" plain="true" iconCls="icon-save">保存</a>
		<a href="javascript:this.location.reload();" class="easyui-linkbutton" plain="true" iconCls="icon-reload">刷新</a>
		<a href="javascript:jsFormat();" class="easyui-linkbutton" plain="true" iconCls="icon-code-format">格式化</a>
	</div>
</div>
<div region="center" style="padding:3px;border-top:0px;border:0px;"> 
<s:form  id="editForm" name="editForm" action="sysEngineIForm_formjs_save.action"  theme="simple">
						 <div region="center" border="false" style="padding:0px;padding-left:2px; background:#FEFDF5; border: 0px solid #ccc;">
						 		<s:if test="formjs!=null">
						 			<s:textarea   cssStyle="width:795px;height:480px;overflow-y:visible;color:#0000FF;background-image:url(iwork_img/engine/tips_js.png);background-position:center;background-repeat:no-repeat;"  id="formjs" name="formjs">
				            		</s:textarea>
						 		</s:if>
						 		<s:else>
				            		<s:textarea  value="function diy(){

}" cssStyle="width:795px;height:480px;overflow-y:visible;color:#0000FF;background-image:url(iwork_img/engine/tips_js.png);background-position:center;background-repeat:no-repeat;"  id="formjs" name="formjs">
				            		</s:textarea>
						 		</s:else>
				            	
				         </div>
        		<s:hidden name="id"></s:hidden>
    	</s:form>	
    </div>
    
</body>
</html>
