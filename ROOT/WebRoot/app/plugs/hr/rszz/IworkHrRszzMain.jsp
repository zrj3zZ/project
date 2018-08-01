<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page import="com.iwork.core.organization.context.UserContext"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String m_userid = request.getParameter("m_userid");
if(m_userid==null){
	m_userid= "";
}
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>the Employee information page</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<link rel="stylesheet" type="text/css" href="iwork_css/common.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/process-icon.css"/>
	<link rel="stylesheet" type="text/css" href="iwork_themes/easyui/bootstrap/easyui.css">
	<link rel="stylesheet" type="text/css" media="screen" href="iwork_css/jquerycss/validate/screen.css" />
	<link rel="stylesheet" type="text/css" href="iwork_css/formstyle.css"/>
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/jqgrid/ui.jqgrid.css"/>
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/jqgrid/jquery-ui-1.8.2.custom.css"/>
	<link href="iwork_css/reset.css" rel="stylesheet" type="text/css"/>
	<link href="iwork_css/pformpage.css" rel="stylesheet" type="text/css"/>
	<link rel="stylesheet" href="iwork_js/kindeditor/themes/simple/simple.css" />
	<link rel="stylesheet" href="iwork_js/kindeditor/plugins/code/prettify.css" />
	<script charset="utf-8" src="iwork_js/kindeditor/kindeditor.js"></script>
	<script charset="utf-8" src="iwork_js/kindeditor/lang/zh_CN.js"></script>
	<script charset="utf-8" src="iwork_js/kindeditor/plugins/code/prettify.js"></script>
	<script type="text/javascript" src="iwork_js/commons.js"   ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"   ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js"  ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/languages/grid.locale-cn.js"  ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.jqGrid.min.js"  > </script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.validate.js"   ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.metadata.js"   ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.form.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/My97DatePicker/WdatePicker.js"   ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/languages/messages_cn.js"  ></script>
	<script type="text/javascript" src="iwork_js/lhgdialog/lhgdialog.min.js?self=true"></script>
	<script type="text/javascript" src="iwork_js/pformpage.js"></script>
	<script type="text/javascript" src="iwork_js/json.js"></script>
	<link rel="StyleSheet" href="iwork_js/dtree/dtree.css" type="text/css" />
	<script type="text/javascript" src="iwork_js/dtree/dtree.js"></script>
	<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/>
<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
	<style type="text/css">
        html,body
        {
            height: 100%;
            margin: 0px;
        }
        .left
        {
            float: left;
            height: 100%;
            width: 20%;
        }
        .right
        {
            float: right;
            height: 100%;
            width: 80%;
        }        
    </style>
	<script type="text/javascript">
	var mainFormValidator;
	$().ready(function() {
		mainFormValidator =  $("#editForm").validate({});
		mainFormValidator.resetForm();
	});

	var cur;
	function doit(){
   		var obj = event.srcElement;
   		if(cur == obj) return false;
   		if(cur!=null) cur.style.backgroundColor = "";
   		obj.style.backgroundColor = "#AB82FF";
   		cur = obj;
   		var n = obj.innerText;
   		document.addform.key.value=obj.innerText;
	}
	window.onload=function(){
    	var tb1 = document.getElementById("tbb");
    	var a = tb1.getElementsByTagName("td");
    	for(var i=0;i<a.length;i++){
     	  a[i].onclick=doit;
    	}
	}
	function nofind(){
	  var img = event.srcElement;
	  img.src = "iwork_img/rszz/nopic.jpg";
	  img.onerror = null;//控制系统不再自动查找,容易卡死
	}	
function requestConfigDialog(form,mycmd){
 	form.cmd.value=mycmd;
	form.target="hrygzz";
 	//form.submit();
 	return false;
 }
	
	//弹出上传头像窗口
	function add_image(){
		var userid='ADMIN' //= $('#userid').val();
		var url = "syspersion_photo.action?userid="+userid;
		art.dialog.open(url,{ 
	        id:'dg_addImage',  
	        title:'上传头像',
	        iconTitle:false, 
	        width:500,
	        heigh:600,
	        close:function(){
	        	window.location.reload();
	        }
		});
	}
	
	function update_infomation(){
		var url = "syspersion_generalSet.action";
		art.dialog.open(url,{ 
	        id:'dg_updateInfo',  
	        title:'更新资料',
	        iconTitle:false, 
	        width:600,
	        heigh:700,
	        close:function(){
	        	window.location.reload();
	        }
		});
	}	
 
 function changePage(url){
 	var obj=document.getElementById("hrygzz");
 	var m_userid = document.getElementById("m_userid").value;
 	obj.src = url+"?m_userid="+m_userid;
 }
</script>

  </head>
<form action="" method=post name=frmMain >
 <!--<div class="${isleft }" style="${isShowFlag} " >
 <table height="100%">
 <tr height="100%" >
 <td  align="top" >
 	 <iframe name="hrygzz1" id="hrygzz1"  marginheight="0" marginwidth="0" frameborder="0"  src="/app/plugs/hr/rszz/IworkHrDtree.jsp" align="top" width=100% height="100%" onload="document.all['hrygzz'].style.height=hrygzz.document.body.scrollHeight+20;">
        		 </iframe>
 </td>
 </tr>
 </table>
</div>
--><!--<div class="${isright }">
 --><table width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
  <tr>
    <td height="3">
    	<table width="100%" height="39" border="0" align="center" cellpadding="0" cellspacing="0">
      	  <tr>
        	<td width="18%"  align="left" valign="top">
        		<table width="109%" border="0" cellspacing="0" cellpadding="0">
  					<tr>
    					<td  align="center" class="picTd">
						<!-- 系统加载当前人照片 -->
							<div id="picDIV">
								<img name="subPicture1" width=120,height=120 src="" onerror="nofind();"/>
    						</div>
    					</td>
  					</tr>
  					<tr>
  						<td class ="td_title" nowrap style="padding-left:10px;"><div align="right" class="sl">
      						<div align="left">
      						    <a href="javascript:add_image();" class="" ><img src="iwork_img/rszz/attach.gif" border="0">上传照片</a>&nbsp;
        						<a href='javascript:update_infomation();'  ><img src="iwork_img/rszz/man.gif" border="0">修改信息</a></div>
        						
    						</div>
						</td>
  					</tr>
				</table>
			</td>
			<td width="34%"  valign="top" style=" padding-left:10px">
				<table  border="0" cellspacing="0" cellpadding="0" width="100%" class="ke-zeroborder">
					<tr>
              			<td style="border:1px">
              				<table width="100%" border="0" cellspacing="0" cellpadding="0">
              					<tr>
                    				<td width="18%" nowrap class="bottom_border"><img src="/iwork_img/rszz/application_view_list.gif" border="0">
                    					我的信息
                    				</td>
                    				<td width="82%" nowrap valign="bottom">
                    					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<font color="#D20000" style="font-size:12px;"><!--  (关注：<span style=" font-weight:bold;">x</span>)--></font>
  									</td>
  								</tr>
  								<tr>
                    				<td colspan="2" nowrap class="sl">
                    					<table width="100%" border="0" cellspacing="0" cellpadding="0">
                    						<tr>
                        						<td width="22%" align="center" valign="top" nowrap class="td_title">　　员工编号：</td>
                        						<td width="78%"class="td_data">${usercode}&nbsp;</td>
                      						</tr>
                      						<tr>
                        						<td align="center" valign="top" nowrap class="td_title" >　　员工姓名：</td>
                        						<td class="td_data">${username}&nbsp;</td>
                      						</tr>
                      						<tr>
                        						<td align="center" valign="top" nowrap class="td_title">常驻办公地点：</td>
                        						<td class="td_data">${workaddress}&nbsp;</td>
                      						</tr>
                      						<tr>
                        						<td align="center" valign="top" nowrap class="td_title">　　　　部门：</td>
                        						<td class="td_data">金山软件/KS集团/KS信息技术部/&nbsp;</td>
                      						</tr>
                      						<tr>
                        						<td align="center" valign="top" nowrap class="td_title">　　　　职位：</td>
                        						<td class="td_data" >${postsresponsibility }&nbsp;</td>
                      						</tr>
                      						<!-- 
                      						<tr>
                        						<td valign="top" nowrap class="td_title">　　直接上级：</td>
                        						<td class="td_data">&nbsp;</td>
                      						</tr>
                      						
                      						<tr>
													<td width="22%"  valign="top" nowrap class="td_title">
														　　岗位职责：
													</td>
													<td width="78%" class="td_data">
														<div style="overflow:hidden;height:20px;width:200px;line-height:20px;font-size:12px;" id="ScrollBox">
    														<div id="holder" style="width:200px;word-break:break-all;word-wrap:break-word;"></div>
														</div>
											</tr>
											<tr>
													<td width="22%" align="left" valign="top" nowrap class="td_title">兴趣爱好：</td>
													<td width="78%" class="td_data">
														<div style="overflow:hidden;width:200px;height:20px;line-height:20px;font-size:12px;" id="ScrollBoxTemp">
    														<div id="holdertemp" style="width:200px;word-break:break-all;word-wrap:break-word;">&nbsp;</div>
														</div>
											</tr>
											 -->
                    					</table>                      
               						</td>
          						</tr>
              				</table>
             			</td>
            		</tr>
        		</table>
        	</td>
        	<td width="51%" align="right" valign="top" style="margin:10px;">	
        		<fieldset style="padding:8px;width:90%;border:1px solid #ACBCC9; margin-left:10px; margin-right:10px;line-height:2.0; text-align:left;" >
        			<legend style="vertical-align:middle; text-align:left">
        				<img src="iwork_img/rszz/online.gif" width="16" height="16" border="0">员工主数据分类查询
        			</legend>
        			<table id="tbb" width="100%" border="0" cellpadding="0" cellspacing="0" bordercolor="#ACBCC9" style="border-collapse:collapse" bgcolor="#B9D3EE">
        				<tr>
							<td colspan=4 height=3></td>
						</tr>
						${html}
						
				 	</table>
	     		</fieldset>
	   		</td>
   	 	</tr>
    	<tr align="center">
       		 <td colspan="3"class="main" >
       		  	 <iframe name="hrygzz" id="hrygzz"  marginheight="0" marginwidth="0" frameborder="0"  src="iwork_hr_emphobbies.action" align=center width=100% onload="document.all['hrygzz'].style.height=hrygzz.document.body.scrollHeight+20;">
        		 </iframe>
        	</td>
    	</tr>
    	
     </table>
 	</td>
   </tr>
   <input type="hidden" id="userid" name="userid" value="${userid}" class="{string:true}" />
   <input type="hidden" id="username" name="username" value="${username}" class="{string:true}" />
   <input type="hidden" id="usercode" name="usercode" value="${usercode}" class="{string:true}" />
   <input type="hidden" id="workaddress" name="workaddress" value="${workaddress}" class="{string:true}" />
   <input type="hidden" id="m_userid" name="m_userid" value="${m_userid}" class="{string:true}" />
   
   
   <input type="hidden" id="postsresponsibility" name="postsresponsibility" value="${postsresponsibility }" class="{string:true}" />
  </table>  
<!--</div>

			
--></form>
  
</html>
<script language="JavaScript">
	jQuery.validator.addMethod("string", function(value, element) {
		var sqlstr=[" and "," exec ", " count ", " chr ", " mid ", " master ", " or ", " truncate ", " char ", " declare ", " join ","insert ", "select ", "delete ", "update ","create ","drop "]
		var patrn=/[“”`~!#$%^&*+<>?"{},;'[]（）—。[\]]/im;
		if(patrn.test(value)){
    	}else{
    		var flag = false;
    		var tmp = value.toLowerCase();
    		for(var i=0;i<sqlstr.length;i++){
    			var str = sqlstr[i];
    			if(tmp.indexOf(str)>-1){
    				flag = true;
    				break;
    			}
    		}
    		if(!flag){
    			return "success";
    		}
    	}
    }, "包含非法字符!");
</script>
