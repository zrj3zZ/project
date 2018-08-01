<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    
    <title>自定义按钮管理中心</title>
    <link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
    <link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
    <script type="text/javascript" src="iwork_js/commons.js"></script>    
    <link rel="stylesheet" type="text/css" href="iwork_css/titleSelect.css">
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js" ></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.form.js"></script>
    <script type="text/javascript" src="iwork_js/lhgdialog/lhgdialog.min.js?self=true&skin=default"></script>
    <script type="text/javascript" src="iwork_js/processDesignTab.js"></script>
    <link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/>
<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
    <style type="text/css">
		.item{ 
			border-bottom:1px dotted #ccc;
			white-space:nowrap;
			vertical-align:top;
			font-size: 12px;
		}
		.item a{
			font-size: 12px;
			word-wrap:break-word;
			word-break:break-all;
			margin-right:10px;
		}
		.td_title {
				color:#004080;
				font-size: 12px;
				text-align: right;
				letter-spacing: 0.1em;
				padding-right:10px;
				vertical-align:middle;
				font-weight:700;
				vertical-align:top;
				padding-top:5px;
				white-space:nowrap;
			}
		.td_data{
			padding-top:3px;
			color:#0000FF;
			text-align:left;
			padding-left:3px;
			font-size: 12px;
			vertical-align:top;
			word-wrap:break-word;
			word-break:break-all;
			font-weight:500;
		}
		.icon-title{
			background:url('icons/page.png') no-repeat;
		}
	</style>
    
	<script type="text/javascript">
	//添加
	function add(){
	    var reportId=$('#reportId').val();
		var url = 'url:ireport_designer_dataScope_add.action?reportId='+reportId;
		openDialog('添加',url);
	}
	//编辑
	function edit(id){
	    var reportId=$('#reportId').val();
		var url = 'url:ireport_designer_dataScope_edit.action?reportId='+reportId+'&id='+id;
		openDialog('编辑',url);
	}
	//添加和编辑窗口
	function openDialog(title,url){
		art.dialog.open(url,{ 
					id:'openStepDefWinDiv',
					cover:true,
					title:title,
					loadingText:'正在加载中,请稍后...',
					bgcolor:'#999',
					rang:true,
					width:500,
					cache:false,
					lock: true,
					height:350, 
					iconTitle:false, 
					extendDrag:true,
					autoSize:true,
					close:function(){
					   location.reload();
					  
					}
				});	
	}
	//显示权限设置
	function showAuthority(id){  
	       var pageUrl =  "ireport_designer_dataScope_purview.action?id="+id;
	       art.dialog.open(pageUrl,{
						id:'db_show',
						cover:true,
						title:'查看权限设置',
						loadingText:'正在加载中,请稍后...',
						bgcolor:'#999',
						rang:true,						
						cache:false,
						lock: true,
						width:290,
						height:450,
						iconTitle:false,
						extendDrag:true,
						autoSize:false,
						max: false
					});      
	}
	//删除
	function del(id){
	      $.dialog.confirm('确定删除？',function(){
	            $.post('ireport_designer_dataScope_del.action',{id:id},function(data){
	           		 if(data=="ok"){
	                	location.reload();
	            	}
	             });
	      },function(){});    
	}
	//下移
	function moveDown(id,reportId){
	     $.post('ireport_designer_dataScope_moveDown.action',{id:id,reportId:reportId},function(data){
	            if(data=="ok"){
	                location.reload();
	            }
	     });	     
	}
   //上移
   function moveUp(id,reportId){
        $.post('ireport_designer_dataScope_moveUp.action',{id:id,reportId:reportId},function(data){
	            if(data=="ok"){
	                location.reload();
	            }
	     });
   }
	</script>
  </head>
  
  <body class="easyui-layout">

    <!-- 维护区 -->  
    <div region="center" style="padding:3px;border:0px">
        <div style="padding:2px;margin-bottom:0px;border-bottom:1px solid #efefef">
		      <a href="javascript:add();" class="easyui-linkbutton" plain="true" iconCls="icon-add">添加</a>
		      <a href="javascript:this.location.reload();" class="easyui-linkbutton" plain="true" iconCls="icon-reload">刷新</a>
         </div>
	    
	    <table border="0"  cellspacing="0" cellpadding="0"  width="100%">
	         <s:iterator value="list" status="sta">
	             <tr> 
	                  <td><img src="iwork_img/documents48.png" border="0"  style="border:1px solid #efefef;padding:3px;padding-left:13px;margin-right:13px;margin-top:13px;"></td>
	                  <td width="80%" class="item">
							<table border="0"  cellspacing="0" cellpadding="0" width="100%">
							<tr>
								<td class="td_title">数据范围SQL：</td>
								<td class="td_data">
									<s:property value="scopeSql"/>								
								</td>
							</tr> 
							<tr>
								<td class="td_title" width="15%">权限设置：</td>
								<td class="td_data" width="85%">
									<s:property value="purview"/>								
								</td>
							</tr>
							<tr>
								<td class="td_title">备注：</td>
								<td class="td_data">
									<s:property value="memo"/>
								</td>
							</tr>
						</table>
					</td>
					<td width="15%" class="item">
						<div>	
							  <a href="javascript:showAuthority(<s:property value='id'/>);"><img src='iwork_img/shield_go.png' style='border:0px solid #efefef;margin-right:3px;margin-top:3px;'>查看权限设置</a>							    							        			
					    </div>
					    <div>
					         <a href="javascript:edit(<s:property value='id'/>);"><img src='iwork_img/note_edit.png' style='border:0px solid #efefef;margin-right:3px;margin-top:3px;'>修改</a>
					         <a href="javascript:del(<s:property value='id'/>);"><img src="iwork_img/del3.gif" style="border:0px solid #efefef;margin-right:3px;margin-top:3px;">删除</a>
					    </div>
					    <div>
					        <s:if test="#sta.first">
					          <a href="javascript:moveDown(<s:property value='id'/>,<s:property value='reportId'/>);"><img title='向下移动' src='iwork_img/icon/arrow_down_blue.gif' border='0'/>向下移动</a>
					        </s:if>	
					        <s:elseif test="#sta.last">
					           <a href="javascript:moveUp(<s:property value='id'/>,<s:property value='reportId'/>);"><img title='向上移动' src='iwork_img/icon/arrow_up_blue.gif' border='0'/>向上移动</a>
					        </s:elseif>	
					        <s:else>
					           <a href="javascript:moveUp(<s:property value='id'/>,<s:property value='reportId'/>);"><img title='向上移动' src='iwork_img/icon/arrow_up_blue.gif' border='0'/>向上移动</a>
                               <a href="javascript:moveDown(<s:property value='id'/>,<s:property value='reportId'/>);"><img title='向下移动' src='iwork_img/icon/arrow_down_blue.gif' border='0'/>向下移动</a>
					        </s:else>					         
					    </div>
					</td>						                  
	             </tr>
	         </s:iterator>
	    </table>
	    
	    <s:hidden name="reportId" theme="simple"/>
    </div>
  </body>
</html>
