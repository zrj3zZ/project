<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Frameset//EN" "http://www.w3.org/TR/html4/frameset.dtd">
<%@page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="iwork" uri="/WEB-INF/sys-commonsTags.tld" %>
<html>
<head>
</head>
<link rel="stylesheet" type="text/css" href="iwork_css/common.css">
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/process-icon.css"> 
<link href="iwork_css/public.css" rel="stylesheet" type="text/css" />
<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/> 
<script type="text/javascript" src="iwork_js/orguser_form.js"></script>
<script language="javascript" src="iwork_js/commons.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"   ></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js"  ></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.form.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.validate.js"   ></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.metadata.js"   ></script>
<script type="text/javascript" src="easyuiDemo/js/jquery.easyui.min.1.2.2.js"   ></script>
<script type="text/javascript" src="iwork_js/jqueryjs/My97DatePicker/WdatePicker.js"   ></script>
<script type="text/javascript" src="iwork_js/jqueryjs/languages/messages_cn.js"  ></script>
<script type="text/javascript" src="iwork_js/org/user_edit.js"   ></script>
<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
<script type="text/javascript">
var mainFormValidator;
var api = art.dialog.open.api, W = api.opener;
$(document).ready(function(){
		$('#Total').tabs({
		});
		mainFormValidator =  $("#editForm").validate({
			 errorPlacement: function (error, element) {
	             error.appendTo(element.parent());    //将错误信息添加当前元素的父结点后面               
	        }
	   }); 
	    mainFormValidator.resetForm();
    });
function save(){
	var valid = mainFormValidator.form(); //执行校验操作 
	if(!valid){
		 art.dialog.tips("表单验证失败，请检查信息项是否合法",2);
			return false;
	}
	 var obj = $('#editForm').serialize(); 
     $.post("user_save.action",obj,function(data){
           if(data=="success"){ 
        	   art.dialog.tips("保存成功",2);
            	api.close();
        	}
     });
}
function doDisabled(){
	 var obj = $('#editForm').serialize(); 
     $.post("user_disable.action",obj,function(data){
           if(data=="success"){ 
        	   art.dialog.tips("账户注销成功",2);
            	window.location.reload();
        	}
     });
}
function doActive(){
	var obj = $('#editForm').serialize(); 
     $.post("user_activating.action",obj,function(data){
           if(data=="success"){ 
            	art.dialog.tips("账户激活成功",2);
            	window.location.reload(); 
        	}
     }); 
}
</script>
<style type="text/css">
		.form_title{  
			font-family:黑体;
			font-size:15px;
			text-align:right;
		}
		.form_data{
			font-family:宋体;
			font-size:12px;
			text-align:left;
			color:#FF0000; 
			padding:3px; 
		}
	</style>
<body class="easyui-layout" >
<div region="north" border="false" split="false" >
	<div class="tools_nav">
			
			<s:if test="model.id!=null">
				<s:if test="model.userstate==0">
				<a href="javascript:save()" class="easyui-linkbutton"  plain="true" iconCls="icon-save" >保存</a>
			<a href="javascript:this.location.reload();" class="easyui-linkbutton"  plain="true" iconCls="icon-reload" >刷新</a>
					<a href="javascript:initpwd();" class="easyui-linkbutton"  plain="true" iconCls="icon-max" >登录口令初始化</a>
					<a href="javascript:doDisabled();" class="easyui-linkbutton"  plain="true" iconCls="icon-remove" >注销</a>
				</s:if>
				<s:else>
					<a href="javascript:doActive();" class="easyui-linkbutton"  plain="true" iconCls="icon-add" >激活</a>
				</s:else>
			</s:if>
			<s:else> 
			<a href="javascript:save()" class="easyui-linkbutton"  plain="true" iconCls="icon-save" >保存</a>
			<a href="javascript:this.location.reload();" class="easyui-linkbutton"  plain="true" iconCls="icon-reload" >刷新</a>
			</s:else>
	</div>
</div>
<div region="center" border="false" split="false" >
<s:form name="editForm" id="editForm" action="user_save"  theme="simple">
<div style="color:red">
    <s:fielderror />
</div> 
                    <div id="Total"  style="width:845px;margin-left:3px;border:0px">
                    <div title="常规信息" selected="true" class="winBackground"  >
                      <table width="100%" border="0" style="height:460px" cellpadding="0" cellspacing="0" align="center">
                        <tr  >
                          <td colspan="2" width="60%" style="vertical-align:top;padding-top:30px"><table width="100%" border="0" cellspacing="0" cellpadding="0">
                              <tr>
                                <td width="1"  ></td>
                                <td width="99%"  ><table width="100%" border="0" cellspacing="0" cellpadding="0">
                                    <tr>
                                      <td width="2%">&nbsp;</td>
                                      <td width="30%" align="right" class="form_title">所在部门:</td>
                                      <td width="74%" class="form_data"><s:textfield theme="simple" readonly="true" cssClass="no_input" cssStyle="width:80px;color:#999999" name="model.departmentid" value="%{departmentid}"/>/<s:textfield  cssClass="{maxlength:32,required:true}"  theme="simple" readonly="true"  cssStyle="width:80px;color:#999999" name="model.departmentname" value="%{departmentname}"/>&nbsp;<a href="###" onclick="dept_book('','','','','','','editForm_model_departmentid','editForm_model_departmentname','editForm_model_departmentname');" title="部门地址薄" class="easyui-linkbutton" plain="true" iconCls="icon-deptbook"></a><span style='color:red'>*</span></td>
                                      <td width="2%">&nbsp;</td>
                                    </tr>
                                    <tr height="1">
                                      <td colspan="4" align="center"  ></td>
                                    </tr> 
                                    <tr>
                                      <td width="2%">&nbsp;</td>
                                      <td width="30%" class="form_title">登录帐户:</td>
                                      <td width="74%"  class="form_data">
                                      <!-- 约束账户名修改权限 -->
                                      <s:if test="null == model">
									 	 <s:textfield theme="simple" cssClass="{maxlength:32,required:true}" id="userid"  name="model.userid" value="%{userid}"/> 
									 </s:if>	 	
								     <s:else>
									 	 <s:textfield theme="simple" id="userid"  readonly="true" cssStyle="background:#efefef;border:1px solid #999;color:#999" cssClass="{maxlength:32,required:true}" name="model.userid" value="%{userid}"/> 
									 </s:else>
                                      <span style='color:red'>*</span>
                                      </td>
                                      <td width="2%">&nbsp;</td>
                                    </tr>
                                     <tr>
                                      <td width="2%">&nbsp;</td>
                                      <td width="30%" class="form_title">姓名:</td>
                                      <td width="74%"  class="form_data"><s:textfield theme="simple"  cssClass="{maxlength:32,required:true}" name="model.username"/>
                                      &nbsp;<span style='color:red'>*</span></td>
                                      <td width="2%">&nbsp;</td>
                                    </tr>
                                    <tr height="1">
                                      <td colspan="4" align="center"  ></td>
                                    </tr>
                                    <tr>
                                      <td width="2%">&nbsp;</td>
                                      <td width="30%" class="form_title">角色:</td>
                                      <td width="74%" class="form_data">
                                     <s:select name="model.orgroleid"  cssClass="{maxlength:32}" list="%{availableItems}" listValue="rolename" value="model.orgroleid" listKey="id"  headerKey="" headerValue="--请选择--" theme="simple"  >
		 							</s:select>  &nbsp;<span style='color:red'>*</span> 
                                      </td>
                                      <td width="2%">&nbsp;</td>
                                    </tr>
                                    <tr height="1">
                                      <td colspan="4" align="center"  ></td>
                                    </tr>
                                    <tr style="display:none">
                                      <td width="2%">&nbsp;</td>
                                      <td width="30%" class="form_title">上级领导帐号:</td>
                                      <td width="74%" class="form_data"><s:textfield theme="simple"   cssClass="{maxlength:32}" name="model.bossid"/></td>
                                      <td width="2%">&nbsp;</td>
                                    </tr>
                                    <tr height="1">
                                      <td colspan="4" align="center"  ></td>
                                    </tr>
                                    <tr>
                                      <td width="2%">&nbsp;</td>
                                      <td width="30%" class="form_title">是否本部门管理者:</td>
                                      <td width="74%"  class="form_data">
                                      <s:select value="model.ismanager"  cssClass="{required:true}" list="#{'0':'否','1':'是'}" name="model.ismanager" theme="simple"></s:select>
                                       </td>
                                      <td width="2%">&nbsp;</td>
                                    </tr>
                                    <tr height="1">
                                      <td colspan="4" align="center"  ></td>
                                    </tr>
                                    <tr>
                                      <td width="2%">&nbsp;</td>
                                      <td width="30%" class="form_title">用户类型:</td>
                                      <td width="74%"  class="form_data"><s:select  cssClass="{required:true}" cssClass="select" list="#{'0':'组织用户','1':'系统用户','2':'外部用户'}" value="model.usertype" name="model.usertype" theme="simple"></s:select> </td>
                                      <td width="2%">&nbsp;</td>
                                    </tr>
                                    <tr>
                                      <td width="2%">&nbsp;</td>
                                      <td width="30%" class="form_title">员工编号:</td>
                                      <td width="74%"  class="form_data"><s:textfield theme="simple"  cssClass="{maxlength:64,required:true}" name="model.userno"/>&nbsp;<span style='color:red'>*</span></td>
                                      <td width="2%">&nbsp;</td>
                                    </tr>
                                      <tr height="1">
                                      <td colspan="4" align="center"  ></td>
                                    </tr>
                                     <tr>
                                        <td width="2%">&nbsp;</td>
                                        <td width="22%" class="form_title">工作组编号:</td>
                                        <td width="74%" class="form_data"><s:textfield theme="simple"  cssClass="{maxlength:32}"  cssStyle="width:100px" name="model.extend1"/></td>
                                        <td width="2%">&nbsp;</td>
                                      </tr>
                                    <tr height="1">
                                      <td colspan="4" align="center"  ></td>
                                    </tr>
                                      <tr>
                                        <td width="2%">&nbsp;</td>
                                        <td width="22%" class="form_title">工作组:</td>
                                        <td width="74%" class="form_data"><s:textfield theme="simple"   cssClass="{maxlength:32}"   cssStyle="width:200px" name="model.extend2"/></td>
                                        <td width="2%">&nbsp;</td>
                                      </tr>
                                      <tr height="1">
                                        <td colspan="4" align="center"  ></td>
                                      </tr>
                                      <tr>
                                        <td width="2%">&nbsp;</td>
                                        <td width="22%" class="form_title">员工财务部编号:</td>
                                        <td width="74%" class="form_data"><s:textfield theme="simple"   cssClass="{maxlength:32}"  cssStyle="width:200px" name="model.extend3"/></td>
                                        <td width="2%">&nbsp;</td> 
                                      </tr>
                                      <tr height="1">
                                        <td colspan="4" align="center"  ></td>
                                      </tr>
                                      <tr>
                                        <td width="2%">&nbsp;</td>
                                        <td width="22%" class="form_title">身份证编号:</td>
                                        <td width="74%" class="form_data"><s:textfield theme="simple"    cssClass="{maxlength:32}"  cssStyle="width:200px" name="model.extend4"/></td>
                                        <td width="2%">&nbsp;</td>
                                      </tr>
                                   <tr >
                                      <td width="2%">&nbsp;</td>
                                      <td width="30%" class="form_title">微信账号:</td>
                                      <td width="74%"  class="form_data"><s:label theme="simple" name="model.weixinCode"></s:label>次</td>
                                      <td width="2%">&nbsp;</td>
                                    </tr>
                                    <tr height="1">
                                      <td colspan="4" align="center"  ></td>
                                    </tr>
                                     <tr>
                                      <td width="2%">&nbsp;</td>
                                      <td width="30%" class="form_title">用户有效期:</td>
                                      <td width="74%" class="form_data">
                                     <s:textfield name="model.startdate"  onfocus="WdatePicker({dateFmt:'yyyy-MM-dd'})" theme="simple" >
                                     	 <s:param name="value"><s:date name="model.startdate" format="yyyy-MM-dd" /></s:param>
                                     </s:textfield>到
                                     <s:if test="model.enddate==null">
	                                     <s:textfield name="model.enddate" cssClass="{dateISO:true,required:true}" theme="simple" value="9999-12-31" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd'})">
	                                     </s:textfield></td>
                                     </s:if> 
                                     <s:else>
                                     <s:textfield name="model.enddate" cssClass="{dateISO:true,required:true}" theme="simple"  onfocus="WdatePicker({dateFmt:'yyyy-MM-dd'})">
                                     	 <s:param name="value"><s:date name="model.enddate" format="yyyy-MM-dd" /></s:param>
                                     </s:textfield>
                                     </s:else>
                                     </td>
                                      <td width="2%">&nbsp;</td>
                                    </tr> 
                                    <tr height="1">
                                      <td colspan="4" align="center"  ></td>
                                    </tr>
                                    
                                    <tr  style="display:none">
                                      <td width="2%">&nbsp;</td>
                                      <td width="30%" class="form_title">登陆次数:</td>
                                      <td width="74%"  class="form_data"><s:label theme="simple" name="model.logincounter"></s:label>次</td>
                                      <td width="2%">&nbsp;</td>
                                    </tr>
                                    <tr style="display:none">
                                      <td width="2%">&nbsp;</td>
                                      <td width="30%" class="form_title">账号优先因子:</td>  
                                      <td width="74%"  class="form_data"><s:textfield theme="simple" cssClass="{number:true,max:100,min:0}"name="model.priority"/></td> 
                                      <td width="2%">&nbsp;</td> 
                                    </tr>  
                                    <tr height="1">
                                      <td colspan="4" align="center"  ></td>
                                    </tr>
                                </table></td>
                                <td width="1"></td>
                              </tr>
                          </table></td>
                          <td width="40%"  style="vertical-align:top;padding-top:30px">
							<div id="user_img" >
								<table width="100%">
									<tr>
										<td style="text-align: center;vertical-align:middle">
											<div id="user_image_div">
												<img width="150" id="user_image" src='iwork_img/nopic.gif' alt='用户照片' title='用户照片' onerror="this.src='iwork_img/nopic.gif'" name='photoUpload' id='photoUpload' style="border:1px solid #e5e5e5;margin:3px;">
											</div>
										</td>
									</tr>
									<tr>
										<td style="text-align: center;"><a href="javascript:add_image();" class="easyui-linkbutton" iconCls="icon-add">修改</a><a href="javascript:delete_image();" class="easyui-linkbutton" iconCls="icon-remove">删除</a></td>
									</tr>
								</table>
							</div>
						</td>
                        </tr>
                      </table>
                      </div>
                      <div title="扩展信息"  class="winBackground" >
                        <table width="80%" style="height:460px" border="0" cellpadding="0" cellspacing="0" align="center">
                          <tr  id="rowData2" >
                            <td colspan="3"><table width="100%" border="0" cellspacing="0" cellpadding="0">
                                <tr>
                                  <td width="1"  ></td>
                                  <td width="99%"  ><table width="100%" border="0" cellspacing="0" cellpadding="0">
                                      <tr>
                                        <td width="2%">&nbsp;</td>
                                        <td width="22%">&nbsp;</td>
                                        <td width="74%">&nbsp;</td>
                                        <td width="2%">&nbsp;</td>
                                      </tr>
                                      <tr height="1">
                                        <td colspan="4" align="center"  ></td>
                                      </tr>
                                      <tr>
                                        <td width="2%">&nbsp;</td>
                                        <td width="22%" class="form_title">岗位编号:</td>
                                        <td width="74%" class="form_data"><s:textfield theme="simple"  cssClass="{maxlength:32}"   name="model.postsid"/></td>
                                        <td width="2%">&nbsp;</td>
                                      </tr>
                                      <tr height="1">
                                        <td colspan="4" align="center"  ></td>
                                      </tr>
                                      <tr>
                                        <td width="2%">&nbsp;</td>
                                        <td width="22%" class="form_title">岗位名称:</td>
                                        <td width="74%" class="form_data"><s:textfield theme="simple"   cssClass="{maxlength:32}"   name="model.postsname"/>&nbsp;-例如数据库管理员、行政助理</td>
                                        <td width="2%">&nbsp;</td>
                                      </tr>
                                      <tr>
                                        <td width="2%">&nbsp;</td>
                                        <td width="22%" class="form_title">岗位职责:</td>
                                        <td width="74%" class="form_data"><s:textfield theme="simple"  cssClass="{maxlength:64}"  name="model.postsresponsibility"/></td>
                                        <td width="2%">&nbsp;</td>
                                      </tr>
                                      <tr height="1">
                                        <td colspan="4" align="center"  ></td>
                                      </tr>
                                      <tr>
                                        <td width="2%">&nbsp;</td>
                                        <td width="22%" class="form_title">成本中心编号/成本中心名称:</td>
                                        <td width="74%" class="form_data"><s:textfield theme="simple"   cssClass="{maxlength:32}"   cssStyle="width:100px" name="model.costcenterid"/>&nbsp;/<s:textfield theme="simple"   cssClass="{maxlength:32}"   name="model.costcentername"/></td>
                                        <td width="2%">&nbsp;</td>
                                      </tr>
                                      <tr height="1">
                                        <td colspan="4" align="center"  ></td>
                                      </tr>
                                     
                                      <tr height="1">
                                        <td colspan="4" align="center"  ></td>
                                      </tr>
                                    
                                      <tr height="1">
                                        <td colspan="4" align="center"  ></td>
                                      </tr>
                                      <tr>
                                        <td width="2%">&nbsp;</td>
                                        <td width="22%" class="form_title">开户行:</td>
                                        <td width="74%" class="form_data"><s:textfield theme="simple"    cssClass="{maxlength:32}"  cssStyle="width:100px" name="model.extend5"/></td>
                                        <td width="2%">&nbsp;</td>
                                      </tr>
                                      <tr height="1">
                                        <td colspan="4" align="center"  ></td>
                                      </tr>
                                      <tr>
                                        <td width="2%">&nbsp;</td>
                                        <td width="22%" class="form_title">银行账号:</td>
                                        <td width="74%" class="form_data"><s:textfield theme="simple"   cssClass="{maxlength:32}"  cssStyle="width:100px" name="model.extend6"/></td>
                                        <td width="2%">&nbsp;</td>
                                      </tr>
                                      <tr height="1">
                                        <td colspan="4" align="center"  ></td>
                                      </tr>
                                      <tr>
                                        <td width="2%">&nbsp;</td>
                                        <td width="22%" class="form_title">人员类型:</td>
                                        <td width="74%" class="form_data"><s:textfield theme="simple"  cssClass="{maxlength:32}"  cssStyle="width:100px" name="model.extend7"/></td>
                                        <td width="2%">&nbsp;</td>
                                      </tr>
                                      <tr height="1">
                                        <td colspan="4" align="center"  ></td>
                                      </tr>
                                      <tr>
                                        <td width="2%">&nbsp;</td>
                                        <td width="22%" class="form_title">扩展信息八:</td>
                                        <td width="74%" class="form_data"><s:textfield theme="simple"  cssClass="{maxlength:32}"  cssStyle="width:100px" name="model.extend8"/></td>
                                        <td width="2%">&nbsp;</td>
                                      </tr>
                                      <tr height="1">
                                        <td colspan="4" align="center"  ></td>
                                      </tr>
                                      <tr>
                                        <td width="2%">&nbsp;</td>
                                        <td width="22%" class="form_title">扩展信息九:</td>
                                        <td width="74%" class="form_data"><s:textfield theme="simple"  cssClass="{maxlength:32}" cssStyle="width:100px" name="model.extend9"/></td>
                                        <td width="2%">&nbsp;</td>
                                      </tr>
                                      <tr height="1">
                                        <td colspan="4" align="center"  ></td>
                                      </tr>
                                      <tr>
                                        <td width="2%">&nbsp;</td>
                                        <td width="22%" class="form_title">扩展信息十:</td>
                                        <td width="74%" class="form_data"><s:textfield theme="simple"   cssClass="{maxlength:32}"  cssStyle="width:100px" name="model.extend10"/></td>
                                        <td width="2%">&nbsp;</td>
                                      </tr>
                                      <tr height="1">
                                        <td colspan="4" align="center"  ></td>
                                      </tr>
                                  </table></td>
                                  <td width="1"></td>
                                </tr>
                            </table></td>
                          </tr>
                        </table></div>
                        <div title="兼任职位" >
                     			<table width="100%"  style="height:460px" border="0" cellspacing="0" cellpadding="0">
                                      <tr>
                                        <td colspan="4" style="vertical-align:top">
                                        <iframe align="middle" height="230px;" id="cwin" name="cwin" frameborder="0"  width="100%" onload="javascript:SetCwinHeight(this)"  src="<s:url action="usermap_list.action" ><s:param name="userid" value="model.userid" /></s:url>"></iframe> 
                                        </td>
                                      </tr>
                                  </table>
                        </div>
                        <!-- 
                        <div title="登录控制"  class="winBackground" style="display:none" >
                     		<table width="100%" style="height:460px" border="0" cellspacing="0" cellpadding="0">
                                <tr>
                                  <td width="1"  ></td>
                                  <td width="99%" style="vertical-align: top;padding-top:50px" ><table width="100%" border="0" cellspacing="0" cellpadding="0">
                                      <tr>
                                        <td width="2%">&nbsp;</td>
                                        <td width="22%" class="form_title">是否为游离帐户:</td>
                                        <td width="74%"><s:select value="model.isroving"  cssClass="{required:true}"  list="#{'1':'是','0':'否'}" value="model.isroving" theme="simple"></s:select></td>
                                        <td width="2%">&nbsp;</td>
                                      </tr>
                                      <tr height="1">
                                        <td colspan="4" align="center"  ></td>
                                      </tr>
                                      <tr>
                                        <td width="2%">&nbsp;</td>
                                        <td width="22%" class="form_title">是否控制单点登录:</td>
                                        <td width="74%"><s:textfield theme="simple" cssClass="{number:true}"   name="model.issinglelogin"/></td>
                                        <td width="2%">&nbsp;</td>
                                      </tr>
                                      <tr height="1">
                                        <td colspan="4" align="center"  ></td>
                                      </tr>
                                      <tr>
                                        <td width="2%">&nbsp;</td>
                                        <td width="22%" class="form_title">门户模版:</td>
                                        <td width="74%"><s:textfield theme="simple" cssClass="{maxlength:64}"  name="model.portalmodel"/></td>
                                        <td width="2%">&nbsp;</td>
                                      </tr>
                                  </table>
                          </tr>
                        </table></div> -->
                        <div title="可自助补充的信息"  class="winBackground" >
                      <table width="100%" border="0"  style="height:460px" cellpadding="0" cellspacing="0" align="center">
                          <tr  id="rowData4">
                            <td colspan="3" style="vertical-align:top;padding-top:50px"><table width="100%" border="0" cellspacing="0" cellpadding="0">
                                      <tr>
                                        <td width="2%">&nbsp;</td>
                                        <td width="22%" class="form_title">办公室电话:</td>
                                        <td width="74%" class="form_data"><s:textfield theme="simple"   cssClass="{maxlength:32}"  name="model.officetel"/></td>
                                        <td width="2%">&nbsp;</td>
                                      </tr>
                                      <tr height="1">
                                        <td colspan="4" align="center"  ></td>
                                      </tr>
                                      <tr>
                                        <td width="2%">&nbsp;</td>
                                        <td width="22%" class="form_title">办公室传真:</td>
                                        <td width="74%" class="form_data"><s:textfield theme="simple"     cssClass="{maxlength:32}"  name="model.officefax"/></td>
                                        <td width="2%">&nbsp;</td>
                                      </tr>
                                      <tr>
                                        <td width="2%">&nbsp;</td>
                                        <td width="22%" class="form_title">家庭电话:</td>
                                        <td width="74%" class="form_data"><s:textfield theme="simple"   cssClass="{maxlength:32}"  name="model.hometel"/></td>
                                        <td width="2%">&nbsp;</td>
                                      </tr>
                                      <tr height="1">
                                        <td colspan="4" align="center"  ></td>
                                      </tr>
                                      <tr>
                                        <td width="2%">&nbsp;</td>
                                        <td width="22%" class="form_title">手机:</td>
                                        <td width="74%" class="form_data"><s:textfield theme="simple"   cssClass="{maxlength:32}"   name="model.mobile"/></td>
                                        <td width="2%">&nbsp;</td>
                                      </tr>
                                      <tr height="1">
                                        <td colspan="4" align="center"  ></td>
                                      </tr>
                                      <tr>
                                        <td width="2%">&nbsp;</td>
                                        <td width="22%" class="form_title">邮件:</td> 
                                        <td width="74%" class="form_data"><s:textfield theme="simple"    cssClass="{maxlength:64}"  name="model.email"/></td>
                                        <td width="2%">&nbsp;</td>
                                      </tr>
                                      <tr>
                                        <td width="2%">&nbsp;</td>
                                        <td width="22%" class="form_title">QQ/MSN</td>
                                        <td width="74%"><s:textfield theme="simple"   cssClass="{maxlength:32}"   name="model.qqmsn"/></td>
                                        <td width="2%">&nbsp;</td>
                                      </tr>
                                      <tr height="1">
                                        <td colspan="4" align="center"  ></td>
                                      </tr>
                                      <tr>
                                        <td width="2%">&nbsp;</td>
                                        <td width="22%" class="form_title">紧急联系人:</td>
                                        <td width="74%" class="form_data"><s:textfield theme="simple"  cssClass="{maxlength:32}"   name="model.jjlinkman"/></td>
                                        <td width="2%">&nbsp;</td>
                                      </tr>
                                      <tr height="1">
                                        <td colspan="4" align="center"  ></td>
                                      </tr>
                                      <tr>
                                        <td width="2%">&nbsp;</td>
                                        <td width="22%" class="form_title">紧急联系人电话:</td>
                                        <td width="74%" class="form_data"><s:textfield theme="simple"   cssClass="{maxlength:32}"   name="model.jjlinktel"/></td>
                                        <td width="2%">&nbsp;</td>
                                      </tr>
                                      <tr>
                                        <td width="2%">&nbsp;</td>
                                        <td width="22%" class="form_title">工作状态:</td>
                                        <td width="74%" class="form_data"><s:textfield theme="simple"   cssClass="{maxlength:32}" name="model.workStatus"/></td>
                                        <td width="2%">&nbsp;</td>
                                      </tr>
                                      <tr>
                                        <td width="2%">&nbsp;</td>
                                        <td width="22%" class="form_title">自我描述:</td>
                                        <td width="74%" class="form_data"><s:textarea theme="simple" cssClass="{maxlength:128}"  cssStyle="width:250px;resize: none;"  name="model.selfdesc"/></td>
                                        <td width="2%">&nbsp;</td>
                                      </tr> 
                                  </table></td>
                                  <td width="1"></td>
                                </tr>
                            </table></div></div>
		<s:if test="null == model">
		 	<s:hidden name="model.id" value="%{id}"/>
		 	<s:hidden name="model.orderindex" value="%{id}"/>
		 </s:if>	 	
	     <s:else>
		 	<s:hidden name="model.id" />
		 	<s:hidden name="model.orderindex"/>
		 </s:else>
		 <s:hidden name="model.logincounter" />
		 <s:hidden name="model.userstate" />
		 <s:hidden name="queryValue" />	    
		 <s:hidden name="model.layer" value="%{layer}"/>
</s:form>
</div>
</body>
</html>
