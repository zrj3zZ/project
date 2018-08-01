<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Frameset//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-frameset.dtd">

<html>
  <head>
    <title>流程讨论区</title>
     <link rel="stylesheet" type="text/css" href="iwork_css/common.css">
     <link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/process-icon.css"/>
    <link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
    <link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/> 
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js" ></script>
   <script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
    <script type="text/javascript">
    //确认关闭讨论区
    function closeTalkSpace(id){
        var actDefId=document.getElementById('actDefId').value;
        var proDefId=document.getElementById('proDefId').value;
        var instanceId=document.getElementById('instanceId').value;
        art.dialog.confirm('确定关闭该讨论区吗？', function(){ 
               location.href="process_talk_closeSpace.action?actDefId="+actDefId+"&proDefId="+proDefId+"&instanceId="+instanceId+"&talkSpaceId="+id;
          });
    }
    //talkId表示回复空间的id，即发起人的id;reid表示被回复的人的id，若回复主题，则为0
    function replyToOthers(talkId,reid){
        var actDefId=document.getElementById('actDefId').value;       //用于存放附件
        var instanceId=document.getElementById('instanceId').value;   //用于存放附件
        var uploadMaxSize=document.getElementById('uploadMaxSize').value; //上传附件的大小限制
        var pageUrl = 'process_talk_reply.action?talkId='+talkId+'&reid='+reid+'&actDefId='+encodeURI(actDefId)+'&instanceId='+instanceId+'&uploadMaxSize='+uploadMaxSize;
   		art.dialog.open(pageUrl,{
						id:'dg_replyTalk',  
	       				title:'流程讨论回复',
						lock:true,
						background: '#999', // 背景色
						opacity: 0.87,	// 透明度
						width:550,
			            height:450,
			            close:function(){
			               location.reload();
			            }      
					 }); 
    }
    //发起一个新的流程讨论
    function newTalk(){
        var actDefId=$('#actDefId').val();
        var proDefId=$('#proDefId').val();
        var instanceId=$('#instanceId').val();
        var stepId=$('#newStepId').val();
        var stepName=$('#newStepName').val();       
        var processTitle=$('#newProcessTitle').val(); 
        var pageUrl ='process_talk_startTalk.action?actDefId='+encodeURI(actDefId)+'&proDefId='+proDefId+'&stepId='+encodeURI(stepId)+'&stepName='+encodeURI(stepName)+'&instanceId='+instanceId+'&processTitle='+encodeURI(processTitle);
        art.dialog.open(pageUrl,{
						id:'dg_replyTalk',  
	       				title:'发起流程讨论',
						lock:true,
						background: '#999', // 背景色
						opacity: 0.87,	// 透明度
						width:550,
			            height:450,
			            close:function(){
			               location.reload();
			            }      
					 }); 
    }
    //刷新
    function toReload(){
        location.reload();
    }
    </script>
      <style>
<!--
	#container {width:100%; border:1px dotted #CCCCCC; margin-top:10px; margin-bottom:10px;}
	#title {font-weight:bolder; font-family:"微软雅黑"; font-size:12px; text-align:left;}
	#topdiv { margin:10px;background:#FFFFFF;font-size:12px; font-family:"宋体";}
	#baseinfo { height:30px;padding:5px;}
	#downdiv { margin:10px;background:#FFFFFF;font-size:12px; font-family:"宋体";}
	#talkzone{margin:5px;margin-bottom:10px; border:1px solid #CCCCCC;}
	#starterdiv {font-size:12px; font-family:"宋体";padding:5px;background:#FFFFEE;}
	#talkContent {word-break: break-all;padding:2px;padding-top:5px; padding-left:5px; margin-left:40px;background:#FFFFFF;border:1px solid #E6E6E6;}
	#talkTitle {font-weight:bold;}	
	#subContent{padding:5px;}
	#replydiv{
	         word-break: break-all;
		     padding:2px;
		     padding-top:5px;
		     font-size:12px;
		     font-family:"宋体"; 
		     margin:5px;
		     margin-left:40px;
		     color:#333333; 
		     border-left:1px solid #e3e3e3;
		     border-right:1px solid #e3e3e3; 
		     border-bottom:1px solid #e3e3e3;
		     border-top:1px solid #4cbe00;
		     }
	#reply_title{
		padding-left:5px;
		border-bottom:1px solid #e3e3e3;
		background-color:#f8fef6;
		font-weight:bold;
		height:16px;	
	}
	#reply_content{
		padding-left:25px;
	    background-color:#FFF;			 
	}
	.td_data {
	color:#0000FF;
	line-height: 23px;
	text-align: left;
	padding-left: 3px;
	font-size: 12px;
	font-family:"宋体";
	vertical-align:middle;
	word-wrap:break-word;
	word-break:break-all;
	font-weight:500;
	line-height:15px;
	padding-top:5px;
	}
	.pagetoolbar{
	width:100%;
	background-image:url(../iwork_img/bg_bg1.gif);
	background-color:#E3E3E3;
	margin-top:0px;
 	height:28px;
 	border:1px #FFFFFF solid;
 	border-bottom:1px #efefef solid;
    }
	#talkToolbar{ text-align:right;font-size:12px; font-family:"宋体"; border-bottom:1px dotted #D4D4D4; margin-bottom:5px; margin-top:5px;}
	#replyToolbar{text-align:right;font-size:12px; font-family:"宋体"; margin-bottom:0px; margin-top:5px;}
    #container A{font-size:12px; font-family:"宋体";}
    #container A:link {text-decoration: none;	color: #1E4871}
    #container A:visited {text-decoration: none;color: #336699}
    #container A:Hover {text-decoration: underline;color: #AA0606;}
    body{
    position:relative;
	overflow-x:hidden;
	overflow-y:auto;
	margin:0;
	padding:0;
	}
-->
</style>
  </head>
  
  <body> 
     <div class="tools_nav" style="padding-bottom:5px;">
         <a href="javascript:newTalk();" class="easyui-linkbutton"  iconCls="icon-process-talk" plain="true">发起讨论</a>
	     <a href="javascript:toReload();"  class="easyui-linkbutton"  iconCls="icon-reload" plain="true">刷新</a>
     </div>
     
     <div id="container">
		<div id="topdiv">
		<fieldset>
		<legend id="title">基本信息</legend>
		    <div id="baseinfo">
				流程标题:《流程标题》
           <br/>创建来自:《创建来自》    
			</div>
		</fieldset>	
		</div>
		
	    <div id="downdiv">
	    <fieldset>
		<legend id="title">讨论区</legend>       
			
  <s:iterator value="map" id="outMap"> 
     <div id="talkzone">
	    <s:iterator value="key" id="authors">
				   <div id="starterdiv">
				       <div style="float:left;">发 起 人：<s:property value="createUser"/>&nbsp;</div>
				       <div style="float:right;">【<img  src="iwork_img/organization.gif"/>节点：<s:property value="stepName"/>】</div>
				       <br><div>参与人员：<s:property value="talkUsers"/></div>
				       <div>创建日期：<s:date name="createTime" format="yyyy-MM-dd HH:mm:ss"></s:date></div>
				       <div>讨论主题：</div>
				       <div id = "talkContent">
				       <div id = "talkTitle"><s:property value="title"/></div>
				       <DIV>
				              <div id="subContent"><s:property value="content"/></div>
				              <s:if test="attachFile!=null">			         
				              <table  border="0" cellspacing="0" cellpadding="0">				                  
				                  <tr><td class="td_data"><img src="iwork_img/attach.gif" border="0"/>附件：</td></tr>
				                  <s:generator val="attachFile" separator=","> 
				                    <s:iterator>
				                        <s:set name="fj"/>
				                        <%
				                           String fujian=(String)request.getAttribute("fj"); 
				                           String suffix=fujian.substring(fujian.lastIndexOf(".")+1);
				                           String realName=fujian.substring(0,fujian.lastIndexOf('('))+fujian.substring(fujian.lastIndexOf(')')+1);
				                        %>
				                     <tr><td class="td_data">				                     
				                        <img onerror="this.src='iwork_img/attach.gif'" src='iwork_img/attach.gif'><b> 
				                        <a target='_blank' href='process_talk_download.action?dloadfileFileName=<%=fujian %>&actDefId=<s:property value="#authors.actDefId"/>&instanceId=<s:property value="#authors.instanceId"/>'><%=realName %></a></b>				                    
				                     </td></tr>
				                   </s:iterator>
				                   </s:generator>				                      
				              </table>
				             </s:if>
                      </DIV>
                      </div>
                     <div id="talkToolbar">     
                     	<s:if test="talkUsers!=null">                 
                            <s:if test="talkUsers.indexOf(openerIdName)!=-1||openerIdName==createUser">
                            		<s:if test="status==1">
                                  		 讨论已关闭
                       				</s:if>  
                       				<s:else>         
                                    	<a href='javascript:replyToOthers(<s:property value="id"/>,0);'>参与讨论</a>
                               			<s:if test="openerIdName==createUser">
                                  			&nbsp;&nbsp;<a  href='javascript:closeTalkSpace(<s:property value="id"/>);'>关闭讨论</a>
                               			</s:if>
                                    </s:else>
                           </s:if>
                        </s:if>                        
                    </div>
                 </div> 
        </s:iterator>
        <s:iterator value="value" id="innerMap">
            <s:iterator value="key" id="replyer">
            <div id="replydiv">
               <div id="reply_title">
                    <s:if test="reid==0">
                       <div style="float:left;"><img src="iwork_img/arrow_right.gif" style="padding-right:2px;" border="0"/>
                          <s:if test="createUser==openerIdName">我</s:if><s:else><s:property value="createUser"/></s:else>&nbsp;在&nbsp;<s:date name="createTime" format="yyyy-MM-dd HH:mm:ss"></s:date>&nbsp;发言：
                       </div>
                    </s:if>
                    <s:else>
                      <div style="float:left;"><img  src="iwork_img/arrow_right.gif" style="padding-right:2px;" border="0"/>
                          <s:if test="createUser==openerIdName">我</s:if><s:else><s:property value="createUser"/></s:else>&nbsp;回复&nbsp;<s:if test="value==openerIdName">我</s:if><s:else><s:property value="value" /></s:else>：
                      </div>
                      <div style="float:right;"><s:date name="createTime" format="yyyy-MM-dd HH:mm:ss"></s:date></div>
                    </s:else>      
              </div>
              <DIV id="reply_content">
                      <div id="subContent"><s:property value="content"/></div>
                      <s:if test="attachFile!=null">
                      <table border="0" cellspacing="0" cellpadding="0">
                                 <tr><td class="td_data"><img src="iwork_img/attach.gif" border="0"/>附件：</td></tr>
                                 <s:generator val="attachFile" separator=","> 
				                    <s:iterator>
				                        <s:set name="fj"/>
				                        <%
				                           String fujian=(String)request.getAttribute("fj"); 
				                           String suffix=fujian.substring(fujian.lastIndexOf(".")+1);
				                           String realName=fujian.substring(0,fujian.lastIndexOf('('))+fujian.substring(fujian.lastIndexOf(')')+1);
				                        %>
                                     <tr><td class="td_data">
                                       <img onerror="this.src='iwork_img/attach.gif'" src='iwork_img/attach.gif'><b> 
                                       <a target='_blank' href='process_talk_download.action?dloadfileFileName=<%=fujian %>&actDefId=<s:property value="#authors.actDefId"/>&instanceId=<s:property value="#authors.instanceId"/>'><%=realName %></a></b>
                                     </td></tr>
                                   </s:iterator>
                                </s:generator>   
                      </table>
                      </s:if>
              </DIV>
              <div id="replyToolbar">
                   <s:if test="#authors.status==1">
                   </s:if>
                   <s:else>
                        <s:if test="createUser==openerIdName"></s:if>
                        <s:else>
                   			<s:if test="#authors.talkUsers.indexOf(openerIdName)!=-1||openerIdName==#authors.createUser"> 
                    			 <a href='javascript:replyToOthers(<s:property value="#authors.id"/>,<s:property value="id"/>);'>回复</a>
                  			</s:if> 
                  		</s:else> 
                   </s:else>
              </div>
           </div>
           </s:iterator>            
      </s:iterator>  
    </div>
 </s:iterator>       
                   
            </fieldset>
            </div>
    </div>
      
      <s:hidden name="actDefId" value="%{actDefId}"/>
	  <s:hidden name="proDefId" value="%{proDefId}"/>
      <s:hidden name="instanceId" value="%{instanceId}"/>
      <s:hidden name="uploadMaxSize" value="%{uploadMaxSize}"/>
      <s:hidden name="newStepId" value="newStepId"/>
      <s:hidden name="newStepName" value="新讨论节点名称"/>
      <s:hidden name="newProcessTitle" value="新讨论主题"/>
      
  </body>
</html>
