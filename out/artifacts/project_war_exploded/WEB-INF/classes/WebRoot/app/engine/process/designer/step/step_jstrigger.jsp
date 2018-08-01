<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
  <head>  
    <title><s:property value="title"/>-JS脚本设置</title>
    <link rel="stylesheet" type="text/css" href="iwork_css/common.css">
    <link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
    <link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
    <link rel="stylesheet" type="text/css" href="iwork_css/titleSelect.css">
    <link rel="stylesheet" type="text/css" href="iwork_css/process/process_step.css"> 
    <link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/> 
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js" ></script>
    <script type="text/javascript" src="iwork_js/processDesignTab.js"></script>
	<script src="iwork_js/plugs/jsbeautifier/beautify.js"></script>
	<script src="iwork_js/plugs/jsbeautifier/beautify-css.js"></script>
	<script src="iwork_js/plugs/jsbeautifier/unpackers/javascriptobfuscator_unpacker.js"></script>
	<script src="iwork_js/plugs/jsbeautifier/unpackers/urlencode_unpacker.js"></script>
	<script src="iwork_js/plugs/jsbeautifier/unpackers/p_a_c_k_e_r_unpacker.js"></script>
	<script src="iwork_js/plugs/jsbeautifier/unpackers/myobfuscate_unpacker.js"></script>
  <link type="text/css" rel="stylesheet" href="iwork_js/plugs/dp.SyntaxHighlighter/Styles/SyntaxHighlighter.css"></link>
  <script language="javascript" src="iwork_js/plugs/dp.SyntaxHighlighter/Scripts/shCore.js"></script>
  <script language="javascript" src="iwork_js/plugs/dp.SyntaxHighlighter/Scripts/shBrushJScript.js"></script>
  <script language="javascript" src="iwork_js/plugs/dp.SyntaxHighlighter/Scripts/shBrushXml.js"></script>
<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
    <style type="text/css">
    .box {
        font-family:微软雅黑; 
        font-size:12px; 
        text-align:left;
		height:auto;
		}
	fieldset{
	      width:95%;
	    }
	.dp-highlighter{
	     font-family: Monaco, Consolas, monospace;
	     margin: 2px 0 2px 0!important;
         height:150px;
         overflow:auto; 
         background-color:#efefef;   
         border:1 dotted #cdcdcd;
	    }   
    </style>
    <script type="text/javascript">
    /*全局变量*/
    // var api = frameElement.api, W = api.opener;
	 var the = {
   		 beautify_in_progress: false
     };
  	if (/chrome/.test(navigator.userAgent.toLowerCase())) {
    	String.prototype.old_charAt = String.prototype.charAt;
    	String.prototype.charAt = function (n) { return this.old_charAt(n); }
  	}
 
 /*一些函数*/    
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
    }//用于格式化

 function jsFormat(){ 
 	  if (the.beautify_in_progress)  return;
 	  the.beautify_in_progress = true;
 	  var source_init = $('#hid_init').val();
 	  var source_sj = $('#hid_sj').val();
 	  var source_tj = $('#hid_tj').val();
 	  
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
  
    source_init = unpacker_filter(source_init);
    source_sj = unpacker_filter(source_sj);
    source_tj = unpacker_filter(source_tj);
    var v_init = js_beautify(source_init, opts);  
    var v_sj = js_beautify(source_sj, opts);  
    var v_tj = js_beautify(source_tj, opts);
    $('#output_init1').val(v_init);
    $('#output_init2').html(v_init);   
    $('#output_sj1').val(v_sj);
    $('#output_sj2').html(v_sj);   
    $('#output_tj1').val(v_tj);
    $('#output_tj2').html(v_tj);                     
    the.beautify_in_progress = false;
 }//JS格式化
 	
 function edit(){
        var editId = $('#editId').val();
        var actDefId=$('#model_actDefId').val();
        var actStepDefId=$('#model_actStepId').val();
        var prcDefId=$('#model_prcDefId').val();
        var pageindex=$('#pageindex').val();
		var pageUrl =  "sysFlowDef_stepScriptTrigger_edit.action?editId="+editId+"&actDefId="+encodeURI(actDefId)+"&actStepDefId="+encodeURI(actStepDefId)+"&prcDefId="+prcDefId+"&pageindex="+pageindex;
		this.location = pageUrl;
    }//编辑
    
 	$(document).ready(function(){
 	     jsFormat(); 
 	     dp.SyntaxHighlighter.ClipboardSwf = 'iwork_js/dp.SyntaxHighlighter/Scripts/clipboard.swf';
 	     if(navigator.userAgent.indexOf("MSIE")>0){
             dp.SyntaxHighlighter.HighlightAll('code1','true','false');//这里的 ‘code1’ 为上面放置代码的容器
             $('#output_init2').css('display','none');
             $('#output_sj2').css('display','none');
             $('#output_tj2').css('display','none');
         }
         else{
             dp.SyntaxHighlighter.HighlightAll('code2','true','false');//这里的 ‘code2’ 为上面放置代码的容器
             $('#output_init1').css('display','none');
             $('#output_sj1').css('display','none');
             $('#output_tj1').css('display','none');
         }
          
 	}); 	
  </script>
  </head>
  
  <body class="easyui-layout">
       <!-- TOP区 -->
    <div region="west" border="false" style="width:180px;padding:3px;border-right:1px solid #efefef">
		<s:property value="stepToolbar" escapeHtml="false"/>
	</div>
	<div region="north" border="false" style="height:40px">
		<div class="stepTitle">
			<s:property value="title"/><img src="iwork_img/gear.gif" style="float:right;height:25px" alt="节点设置"/>
		</div>
	</div>
     <!-- 脚本编辑区 -->
    <div region="center" style="padding:0px;border:0px;">
    <div class="tools_nav">
              <a href="javascript:edit();" class="easyui-linkbutton" plain="true" iconCls="icon-edit">编辑</a>
		      <a href="javascript:this.location.reload();" class="easyui-linkbutton" plain="true" iconCls="icon-reload">刷新</a>	      
         </div>
         <div class="box">
          <fieldset>
             <legend><b><font color="808080" >初始化事件脚本接口</font></b></legend>
             
             	<!-- IE浏览器用textarea显示 -->
	  		 	<textarea  cols="60" rows="10" name="code1" class="js" id="output_init1"></textarea>
  	  		 	<!-- 其他浏览器用pre显示 -->
  	  		 	<pre id="output_init2" name="code2" class="js"></pre>
  	  		
          </fieldset>
         </div>
         <br/>
        <div class="box">
          <fieldset>
             <legend><b><font color="808080" >保存事件脚本接口</font></b></legend>
             
             	<!-- IE浏览器用textarea显示 -->
	  		 	<textarea  cols="60" rows="10" name="code1" class="js" id="output_sj1"></textarea>
  	  		 	<!-- 其他浏览器用pre显示 -->
  	  		 	<pre id="output_sj2" name="code2" class="js"></pre>
  	  		
          </fieldset>
         </div>
         <br/>
         <div class="box"> 
          <fieldset>
             <legend><b><font color="808080" >顺序办理事件脚本接口</font></b></legend>
            
            	<!-- IE浏览器用textarea显示 -->
	  			<textarea  cols="60" rows="10" name="code1" class="js" id="output_tj1"></textarea>
  	  			<!-- 其他浏览器用pre显示 -->
  	  			<pre id="output_tj2" name="code2" class="js"></pre>
  	  			
          </fieldset>
        </div>  
        
        <s:hidden name="model.id" id="editId" theme="simple"/>
        <s:hidden name="model.actDefId" theme="simple"/>
        <s:hidden name="model.actStepId" theme="simple"/>
        <s:hidden name="model.prcDefId" theme="simple"/>
        <s:hidden name="model.saveJs" id="hid_sj" theme="simple"/>
        <s:hidden name="model.tranJs" id="hid_tj" theme="simple"/>  
        <s:hidden name="model.initJs" id="hid_init" theme="simple"/>  
        <s:hidden name="pageindex" theme="simple"/>
    </div>
  </body>
</html>
