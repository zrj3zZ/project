<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page import="java.text.SimpleDateFormat"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
</head>
<% 
  SimpleDateFormat  sdf=new  SimpleDateFormat("yyyy-MM-dd");
  String isModify = (String)request.getParameter("isModify");//修改标识
  String mon = (String)request.getAttribute("mon");
  String tues = (String)request.getAttribute("tues");
  String wed = (String)request.getAttribute("wed");
  String turs = (String)request.getAttribute("turs");
  String fri = (String)request.getAttribute("fri");
  String sat = (String)request.getAttribute("sat");
  String sun = (String)request.getAttribute("sun");
  Long workTimeFrom = (Long)request.getAttribute("workTimeFrom");
  Long workTimeTo = (Long)request.getAttribute("workTimeTo");
  String status = (String)request.getAttribute("status"); 
  String calendarType = (String)request.getAttribute("calendarType"); 
  if(workTimeFrom==null){
  	workTimeFrom=0L;
  }
   if(workTimeTo==null){
  	workTimeTo=0L;
  }
  if(mon==null){
  	mon="";
  }
    if(tues==null){
  	tues="";
  }
    if(wed==null){
  	wed="";
  }
    if(turs==null){
  	turs="";
  }
    if(fri==null){
  	fri="";
  }
    if(sat==null){
  	sat="";
  }
    if(sun==null){
  	sun="";
  }

 %>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link rel="stylesheet" type="text/css" href="iwork_css/common.css">
<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/> 
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.validate.js"   charset="utf-8"  ></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.validate.js"   ></script>
<script type="text/javascript" src="iwork_js/commons.js"></script>
<script type="text/javascript" src="iwork_js/jquery.form.js" ></script>
<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
<script type="text/javascript">
var mainFormValidator;
var api = art.dialog.open.api, W = api.opener;
//发送
function doAdd(){
		var obj = $('#editForm').serialize(); 
		     $.post("sysMessageAction!send.action",obj,function(data){
		           if(data){ 
	                	alert("保存成功");
	                	api.close();
	            	}
		     });
}
//清空
function resetGrantUsers(){
		var obj = document.getElementById('model.rcvRange');
		var gnl=confirm("确定要清空吗?");
		if(gnl==true){
			obj.value = '';
			return true;
		}else{
			return false;
		}
}
</script>

<body class="easyui-layout" style="overflow-y:scroll">
    <div id="sys_msg_send_window" class="easyui-window" title="发送系统消息" modal="true" closed="true" collapsible="false" minimizable="true"
        maximizable="false" icon="icon-save"  style="width: 550px; height: 430px; padding: 5px;
        background: #fafafa;">
        <div class="easyui-layout" fit="true">
            <div region="center" border="false" style="padding: 10px; background: #fff; border: 1px solid #ccc;">
            <form id="editForm" name="editForm" action="sysMessageAction!send.action"  theme="simple"  enctype="multipart/form-data" method="post">
            <fieldset>
            	<legend class="td_memo_title"><b><font color="808080" size="+0.5">发送设置</font></b></legend>
            
            			
            			<s:radio name="model.type" list="%{#{'0':'系统消息','1':'生日提醒','2':'流程提醒'}}" value="0" label="消息类型" listKey="key" key="model.type" required="true"/>
        		
		        <div>
		            <label for="name">使用权限设置:</label>
  					<textarea readonly="readonly" name="model.rcvRange" id="model.rcvRange" class = "{required:true}" style="width:250px;height:125px;hoverflow-y:visible"  ><s:property value="model.rcvRange"/></textarea>
	 				<input class="btn1" type="button" name="sq" onclick="authority_chooser(document.getElementById('model.rcvRange'));" value="授权" />
	 				<button type="button" onclick="resetGrantUsers();">清空</button>
   					
		        </div>
		         
		        <div>
            		
            		<s:radio name="model.urlTarget" list="%{#{'0':'打开新页面','1':'当前页面'}}" value="0" label="点击链接设置" listKey="key" key="model.urlTarget" required="true"/>
        		</div>
        		<div>
            		
            		<s:radio name="model.priority" list="%{#{'-1':'低','0':'中','1':'高'}}" value="0" label="优先级" listKey="key" key="model.priority" required="true"/>
        		</div>
            </fieldset>
            <br>
            <fieldset>
            	<legend class="td_memo_title"><b><font color="808080" size="+0.5">内容设置</font></b></legend>
            	
        		<tr>
		            <td>内容标题：
		             <input class="easyui-validatebox" type="text" name="model.title" required="true" style="width:190px"></input>
		        	 </td>
		        </tr><br/>	
		        <tr>
		            <td>消息正文：</td>
		            <td>
		             <textarea class="easyui-validatebox" name="model.content" style="height:100px;width:190px;" required="true"></textarea>
		             </td>
		        </tr>
		        <br/>
		        <tr>
		            <td>详 情URL：</td>
		            <td>
		             <input class="easyui-validatebox" type="text" name="model.url" style="width:190px"></input>
		        	</td>
		        </tr>		        
		        
                <s:hidden name="model.id"></s:hidden>
                
               </form> 
            </div>
            <div region="south" border="false" style="text-align: right; height: 30px; line-height: 30px;padding-top:5px;">
                <a id="btnEp" class="easyui-linkbutton" icon="icon-ok" href="javascript:doAdd();" >
                    发送</a> <a id="btnCancel" class="easyui-linkbutton" icon="icon-cancel" href="javascript:api.close();">取消</a>
            </div>
        </div>
    </div>
</body>
</html>
