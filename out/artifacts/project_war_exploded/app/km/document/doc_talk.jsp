<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>  
    <title>文档评论</title>
    <link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/> 
    <script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
    <script type="text/javascript">
    errorFunc=function(){
           art.dialog.tips("保存失败！",2);
      }
    successFunc=function(responseText, statusText, xhr, $form){
           if(responseText=="ok"){
              art.dialog.tips("保存成功！",1);
              onTabSelect();
           }
      }
    //保存  
    function saveTalk(){
          var bool= validateTalkForm();
          if(bool){
             var options = {
				error:errorFunc,
				success:successFunc 
			   };
			 $('#talkForm').ajaxSubmit(options);
        	}
      } 
    //验证   
    function validateTalkForm(){
        var content = $.trim($('#talkForm_model_content').val());
        $('#talkForm_model_content').val(content);
        if(content==''){
              art.dialog.tips("评论内容不能为空!",2);
		      return false;
        }
        if(length2(content)>500){
              art.dialog.tips("评论内容过长!",2);
		      return false;
        }
        return true;
    }
    //是否匿名
    function isNiMing(){
        var bool = $('#NiMing').attr('checked');
        if(bool){
             $('#talkForm_model_talkType').val('1');
        }else{
             $('#talkForm_model_talkType').val('0'); 
        }
    }
    //分页
    <s:if test="list!=null && list.size()>0">
    $(function(){ 
	        var pgnum =  <s:property value='pageNow'/>;
	        var pgsize = <s:property value='pageSize'/>;
	        var pgtotal = <s:property value='total'/>;
	    	$('#page').pagination({				
				pageNumber:pgnum,
				pageSize:pgsize,
				total:pgtotal,
				showRefresh:false,
				beforePageText:'第',
				afterPageText:'页，共{pages}页',
				displayMsg:'第{from}到{to}条，共{total}条',
				onSelectPage:function(pageNumber, pageSize){   
					$('#pageNow').val(pageNumber);
					$('#pageSize').val(pageSize); 
					onTabSelect();
				}
			}); 
		});
	</s:if>	 
    </script>
  </head>
  <body>
<div class="div_des">
    <div class="report_top">
      <ul class="sub_tab">
        <li><a href="#" >管理员授权</a></li>
        <li>|</li>
        <li><a href="#" >用户管理</a></li>
      </ul>
    </div>
    
    <div class="comment">
        <div class="comment_left">
            <s:if test="list==null || list.size()==0">
            		<div style="text-align:center;padding-top:80px;">暂无评论</div>
            </s:if>
           
            <s:else>
      		<table width="100%" border="0" cellspacing="0" cellpadding="0">
      			<s:iterator value="list" status="stat">
      			    <tr style="font-weight:bold;line-height:24px;">
      			    	<td>
      			    		<s:if test="talkType==1">匿名</s:if>
      			    		<s:else><s:property value="createUserName"/>[<s:property value="departmentName"/>]</s:else>     			    	
      			    	</td>
      			    	<td align="right"><s:property value="createDate"/>&nbsp;&nbsp;&nbsp;<s:property value="total-(pageNow-1)*pageSize-#stat.count+1"/>楼</td>
      			    </tr>
      			    <tr style="line-height:18px;">
      			    	<td colspan="2" style="border-bottom:1px solid #dddddd;"><s:property value="content"/></td>
      			    </tr>
      			</s:iterator>
      		</table>     		
      		</s:else>
      </div>
      <s:if test="list!=null && list.size()>0">
      <div style="margin-right:400px;">
                <!-- 分页工具 -->
      			<div id="page" style="background:#efefef;border:1px solid #ccc;">
				</div>
      </div>
      </s:if>
      
      <div class="comment_right">
      <s:form name="talkForm" id="talkForm" method="POST" action="km_talk_save.action" theme="simple">
       <table width="100%" border="0" cellspacing="0" cellpadding="0">
  		<tr>
    		<td colspan="2" style="color:#656464; font-weight:bold;">用户评论(内容控制在500字以内)</td>
  		</tr>
  		<tr>
    		<td colspan="2"><s:textarea name="model.content" cols="45" rows="5" cssStyle="height:160px; width:345px"/></td>
    	</tr>
  		<tr>
    		<td width="50%" align="right"><s:checkbox name="NiMing" id="NiMing" value="false" fieldValue="1" onclick="isNiMing();"/>匿名</td>
    		<td width="50%" align="right"><input type="button" onclick="saveTalk();" value="提交评论" style="height:24px; width:80px; margin-top:5px; cursor:pointer" /></td>
 	    </tr>
	  </table>
	  
	  <s:hidden name="model.talkType"></s:hidden>
	  
	  <s:hidden name="model.id"></s:hidden>
	  <s:hidden name="model.docId"></s:hidden>
	  <s:hidden name="model.createUser"></s:hidden>
	  <s:hidden name="model.createDate"></s:hidden>
	  <s:hidden name="model.ipAddress"></s:hidden>
	 </s:form>  
      </div>
   </div>
    
</div>
  </body>
</html>
