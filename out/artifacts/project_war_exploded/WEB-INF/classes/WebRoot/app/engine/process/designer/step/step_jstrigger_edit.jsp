<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
  <head>
    <title>脚本格式化文本</title>
    <link rel="stylesheet" type="text/css" href="iwork_css/common.css">
    <link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
    <link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
    <link rel="stylesheet" type="text/css" href="iwork_css/titleSelect.css">
    <link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/> 
    <script type="text/javascript" src="iwork_js/commons.js"></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js" ></script> 
    <script type="text/javascript" src="iwork_js/processDesignTab.js"></script>
    <script src="iwork_js/plugs/jsbeautifier/beautify.js"></script>
<script src="iwork_js/plugs/jsbeautifier/beautify-css.js"></script>
<script src="iwork_js/plugs/jsbeautifier/unpackers/javascriptobfuscator_unpacker.js"></script>
<script src="iwork_js/plugs/jsbeautifier/unpackers/urlencode_unpacker.js"></script>
<script src="iwork_js/plugs/jsbeautifier/unpackers/p_a_c_k_e_r_unpacker.js"></script>
<script src="iwork_js/plugs/jsbeautifier/unpackers/myobfuscate_unpacker.js"></script>
<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
<script type="text/javascript">
    /*全局变量*/
     var api = frameElement.api, W = api.opener;
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
 function jsFormat(obj){
      if ($.trim(obj.val())=="") {obj.val($.trim(obj.val()));art.dialog.tips("请先输入脚本！",2);obj.focus();return;}
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
    art.dialog.tips("格式化完成！",2);                 
    the.beautify_in_progress = false;
 }
   //保存
    function save(){
      var editInitJs=$.trim($('#editInitJs').val());
      var editSaveJs=$.trim($('#editSaveJs').val());
      var editTranJs=$.trim($('#editTranJs').val());
      $('#editSaveJs').val(editSaveJs);
      $('#editTranJs').val(editTranJs);
     
      if(length2(editInitJs)>2000){
          art.dialog.tips("页面初始化事件脚本过长！",2);
          $('#editInitJs').focus();
          return;
      }
      if(length2(editSaveJs)>2000){
          art.dialog.tips("保存事件脚本过长！",2);
          $('#editSaveJs').focus();
          return;
      }
      if(length2(editTranJs)>2000){
          art.dialog.tips("顺序办理事件脚本过长！",2);
          $('#editTranJs').focus();
          return;
      }
	  $('#editForm').submit();
			
    }

    //取消
    function calcel(){
        var actDefId=$('#editForm_model_actDefId').val();
        var actStepDefId=$('#editForm_model_actStepId').val();
        var prcDefId=$('#editForm_model_prcDefId').val();
        var pageindex=$('#editForm_pageindex').val();
		var pageUrl =  "sysFlowDef_stepScriptTrigger_load.action?actDefId="+encodeURI(actDefId)+"&actStepDefId="+encodeURI(actStepDefId)+"&prcDefId="+prcDefId+"&pageindex="+pageindex;	
		this.location = pageUrl;
    }
</script>
<style type="text/css">
    .box{
        margin-top:5px;
        font-weight:bolder; 
        font-family:微软雅黑; 
        font-size:12px; 
        text-align:left;
		width:100%;
		}
	textarea{
	        font-size:12px;
			font-family: Monaco, Consolas, monospace;
			width:100%;
			height:100px;
			border:1px #C0C0C0 solid;
			}
    </style>
  </head>
  
  <body class="easyui-layout">
        <!-- TOP区 -->
     <div region="west" border="false" style="width:180px;padding:3px;border-right:1px solid #efefef">
          <s:property value="stepToolbar" escapeHtml="false"/>
    </div> 
        <!-- 脚本编辑区 -->  
    <div region="center" style="padding:0px;border:0px;">
       <div class="tools_nav">
		      <a href="javascript:save();" class="easyui-linkbutton" plain="true" iconCls="icon-save">保存</a>
		      <a href="javascript:this.location.reload();" class="easyui-linkbutton" plain="true" iconCls="icon-reload">刷新</a>
		      <a href="javascript:calcel();" class="easyui-linkbutton" plain="true" iconCls="icon-back">取消</a>
         </div>
      <s:form name="editForm" id="editForm" theme="simple" action="sysFlowDef_stepScriptTrigger_save.action">
        <div class="box">
            <div style="clear:both;width:95%;"><a style="float:right;margin-right:5px;" href="javascript:jsFormat($('#editInitJs'));" >格式化</a></div>      	
        	<fieldset style="clear:both;width:95%;">
        		<legend><b><font color="808080" >界面初始化事件脚本接口</font></b></legend>	
        		<s:textarea id="editInitJs" name="model.initJs"></s:textarea>
        	</fieldset> </div>     
        	
        <div class="box">
            <div style="clear:both;width:95%;"><a style="float:right;margin-right:5px;" href="javascript:jsFormat($('#editSaveJs'));" >格式化</a></div>      	
        	<fieldset style="clear:both;width:95%;">
        		<legend><b><font color="808080" >保存事件脚本接口</font></b></legend>	
        		<s:textarea id="editSaveJs" name="model.saveJs"></s:textarea>
        	</fieldset> </div>     
        
        <div class="box"> 
            <div style="clear:both;width:95%;"><a style="float:right;margin-right:5px;" href="javascript:jsFormat($('#editTranJs'));" >格式化</a></div>
        	<fieldset style="clear:both;width:95%;">
        		<legend><b><font color="808080" >顺序办理事件脚本接口</font></b></legend>	
       			 <s:textarea id="editTranJs" name="model.tranJs"></s:textarea>
       	 	</fieldset> 
        </div>
        <div style="color:red">注：注册脚本触发器时，如脚本过长，建议填写调用函数名</div>
        <s:hidden name="model.id" />
        <s:hidden name="model.actDefId" />
        <s:hidden name="model.actStepId" />
        <s:hidden name="model.prcDefId" />
        <s:hidden name="pageindex"/>
      </s:form> 
    </div>
      
  </body>
</html>
