<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>公告统计</title>
<link rel="stylesheet" type="text/css" href="iwork_css/common.css">
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/process-icon.css">
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
<link href="iwork_css/public.css" rel="stylesheet" type="text/css" />
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/jqgrid/ui.jqgrid.css">
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/jqgrid/jquery-ui-1.8.2.custom.css">
<link rel="stylesheet" type="text/css" href="iwork_css/engine/iformpage.css" />
<script language="javascript" src="iwork_js/commons.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/languages/grid.locale-cn.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.jqGrid.min.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/My97DatePicker/WdatePicker.js" charset="utf-8"></script>
<script type="text/javascript" src="iwork_js/engine/ifromworkbox.js"></script>
<script type="text/javascript" src="iwork_js/lhgdialog/lhgdialog.min.js"></script>
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/zTreeStyle.css">
<link rel="stylesheet" type="text/css" href="iwork_css/engine/sysenginemetadata.css">
<link rel="stylesheet" type="text/css" href="iwork_themes/easyui/gray/easyui.css">
<script type="text/javascript" src="iwork_js/jqueryjs/easyui/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.validate.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.metadata.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/languages/messages_cn.js"  ></script>
<script type="text/javascript" src="iwork_js/jquery.form.js"></script>
<script type="text/javascript" src="iwork_js/bindclick.js"  ></script>
<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/>
<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
<script type="text/javascript">
var mainFormValidator;
$().ready(function() {
		mainFormValidator =  $("#frmMain").validate({
		 });
		 mainFormValidator.resetForm();
	});
	$(function() {
		$('#pp').pagination({
			total : <s:property value="totalNum"/>,
			pageNumber : <s:property value="pageNumber"/>,
			pageSize : <s:property value="pageSize"/>,
			onSelectPage : function(pageNumber, pageSize) {
				submitMsg(pageNumber, pageSize);
			}
		});
	});
	function submitMsg(pageNumber, pageSize) {
		$("#pageNumber").val(pageNumber);
		$("#pageSize").val(pageSize);
		$("#frmMain").submit();
		return;
	}
	$(function() {
		//查询
		$(document).bind('keydown', function(event) {
			if (event.keyCode == "13") {
				//禁用键盘按钮按下事件
				return false;
			}
		});
		$("#search").click(
				function() {
					var valid = mainFormValidator.form(); //执行校验操作
					if(!valid){
						return;
					}
					var wgfxnr = $("#wgfxnr").val();
					var gsdm = $("#gsdm").val();
					var gsjc = $("#gsjc").val();
					var khbh =$("#khbh").val() ;
					var khmc =$("#khmc").val() ;
					var seachUrl = encodeURI("qywgfxjl.action?wgfxnr=" + wgfxnr + "&gsdm=" + gsdm+ "&gsjc=" + gsjc+ "&khbh=" + khbh+ "&khmc=" + khmc);
					var wgfxlx=$('#wgfxlx option:selected').val();
					if(wgfxlx!=null){
						seachUrl = encodeURI("qywgfxjl.action?wgfxnr=" + wgfxnr + "&gsdm=" + gsdm+ "&gsjc=" + gsjc+ "&khbh=" + khbh+ "&khmc=" + khmc+ "&wgfxlx=" + wgfxlx);
					}
					
					window.location.href = seachUrl;
				});
		});
	
	function addItem(){
		var khbh =$("#khbh").val() ;
		var khmc =$("#khmc").val() ;
		var zqjc =$("#gsjc").val() ;
		var zqdm =$("#gsdm").val() ;
		if(khbh==null || khbh==''||khbh=="undefined"||khbh=="0"||khmc==null||khmc==''||khmc=="undefined"){
		    $.messager.alert('提示信息','请选择挂牌公司!','info');  
			return;
		}
		var formid = $("#formid").val();
		var demid = $("#demid").val();
		
		var pageUrl = encodeURI("createFormInstance.action?formid="+formid+"&demId="+demid+"&YCL="+zqdm+"&TJDSH="+zqjc+"&KHMC="+khmc+"&KHBH="+khbh);
		var target = "_blank";
		var win_width = window.screen.width;
		var page = window.open('form/loader_frame.html',target,'width='+win_width+',height=800,top=50,left=150,location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=yes');
		page.location = pageUrl;		
	}	
		

	
	function updXcjc(tid,SXGY_1){
		//+"&BANKACCOUNT="+zqdmxs+"&CUSTOMERNAME="+zqjcxs
		var formid = $("#formid").val();
		var demid = $("#demid").val();
		var pageUrl = encodeURI("openFormPage.action?formid="+formid+"&instanceId="+tid+"&demId="+demid);
		var target = "_blank";
		var win_width = window.screen.width;
		var page = window.open('form/loader_frame.html',target,'width='+win_width+',height=800,top=50,left=150,location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=yes');
		page.location = pageUrl;
			
	}
	function remove() {
		/* var obj = document.getElementsByName("chk_list");
		check_val = [];
		for (k in obj) {
			if (obj[k].checked)
				check_val.push(obj[k].value);
		}
		if (check_val.length == 0 || check_val[0] == '0') {
			alert("请选择您要删除的行项目!");
		} else {
			var deleteUrl = "xcjclv_del.action";
			$.post(deleteUrl, {
				mlist : check_val
			}, function(data) {
				if (data == 'success') {
					window.location.reload();
				} else {
					alert(data);
				}
			});
		} */
		$.messager
		.confirm(
				'确认',
				'确认删除?',
				function(result) {
					 var obj = document.getElementsByName("chk_list");
					check_val = [];
					
					for(var k=0;k<obj.length;k++){
						if (obj[k].checked)
							check_val.push(obj[k].value);
					}
					if (check_val.length == 0 ) {
						$.messager.alert('提示信息',
								'请选择您要删除的行项目!', 'info');
						return;
					}
					if (result) {
						var list = $('[name=chk_list]').length;
						var a = 0;

						for (var n = 0; n < list; n++) {
							if ($('[name=chk_list]')[n].checked == false
									&& $('[name=chk_list]')[n].id != 'chkAll') {
								a++;
								
							}
							if ($('[name=chk_list]')[n].checked == true
									&& String($('[name=chk_list]')[n].id) != String('chkAll')) {
								var deleteUrl = "xcjclv_del.action";
								$
										.post(
												deleteUrl,
												{
													instanceid : $('[name=chk_list]')[n].id
												},
												function(data) {
													if (data == 'success') {
														window.location
																.reload();
													} else {
														alert(data);
													}
												});
							}
						}
					}
				});
	}
	// 全选、全清功能
	function selectAll(){
		if($("input[name='chk_list']").attr("checked")){
			$("input[name='chk_list']").attr("checked",true);
		}else{
			$("input[name='chk_list']").attr("checked",false);
		}
	}
	function uploadItem(){
		 var seachUrl ="qyToExcl.action";
		window.location.href = seachUrl;
	 }
</script>
<style type="text/css">
.searchtitle {
	text-align: right;
	padding: 5px;
}

.ui-jqgrid tr.jqgrow td {
	white-space: normal !important;
	height: 28px;
	font-size: 12px;
	vertical-align: text-middle;
	padding-top: 2px;
}

.cell td{
			margin:0;
			padding:3px 4px;
			height:25px;
			font-size:12px;
			word-wrap:normal;
			overflow:hidden;
			text-align:left;
			border-bottom:1px dotted #eee;
			border-top:1px dotted #fff;
			border-right:1px dotted #eee;
		}
		.cell:hover{
			background-color:#F0F0F0;
		}
 .header td{
			height:35px;
			font-size:12px;
			padding:3px;
			white-space:nowrap;
			padding-left:5px;
			background:url('../../iwork_img/engine/tools_nav_bg.jpg') repeat-x left bottom;
			border-top:1px dotted #ccc;
			border-right:1px solid #eee;
		} 
</style>
</head>
<body class="easyui-layout">
	<div region="north" border="false">
		<div class="tools_nav">
		<a href="javascript:addItem();" class="easyui-linkbutton" plain="true" iconCls="icon-add">新增</a>
		
		<a id="deljclv" href="javascript:remove();" class="easyui-linkbutton" plain="true" iconCls="icon-add">删除</a>
		<a href="javascript:uploadItem();" class="easyui-linkbutton" plain="true" iconCls="icon-add">导出</a>
		<a href="javascript:window.location.reload();" class="easyui-linkbutton" plain="true" iconCls="icon-reload">刷新</a>
		</div>
	</div>
	<div region="center"
		style="padding-left:0px;padding-right:0px;border:0px;background-position:top">
		<form action="qywgfxjl.action" method=post name=frmMain id=frmMain>
			<div style="padding:5px">
				<div
					style="padding:0px;border:1px solid #ccc;margin-bottom:5px;background:#FFFFEE;">
					<table width='100%' border='0' cellpadding='0' cellspacing='0'>
						<tr>
							<td style='padding-top:10px;padding-bottom:10px;'>
								<table width='100%' border='0' cellpadding='0' cellspacing='0'>
									<tr>
										<td class="searchtitle">公司代码</td>
										<td class="searchdata"><input type='text' class = '{maxlength:128,required:false,string:true}'  style="width:100px;color: gray;"  name='gsdm' id='gsdm' value='<s:property value="gsdm"/>' form-type='al_textbox'></td>
										<td s class="searchtitle">公司简称</td>
										<td>
											<td class="searchdata"><input type='text' class = '{maxlength:128,required:false,string:true}'  style="width:100px;color: gray;"  name='gsjc' id='gsjc' value='<s:property value="gsjc"/>' form-type='al_textbox'></td>
										</td>
										<td class="searchtitle">违规风险内容</td>
										<td>
											<td class="searchdata"><input type='text' class='{string:true,maxlength:64}' style="width:100px;" name='wgfxnr' id='wgfxnr' value='<s:property value="wgfxnr"/>' form-type='al_textbox'></td>
										</td>
									</tr>
										<tr>
										<td class="searchtitle">违规风险类型</td>
										<td class="searchdata" colspan="5">
											<select name="wgfxlx" id="wgfxlx">
												<option value="" >-全部-</option>
												<option value="违规占用" <s:if test="wgfxlx=='违规占用'">selected</s:if> >违规占用</option>
												<option value="股票发行违规行为" <s:if test="wgfxlx=='股票发行违规行为'">selected</s:if> >股票发行违规行为</option>
												<option value="违规对外担保" <s:if test="wgfxlx=='违规对外担保'">selected</s:if> >违规对外担保</option>
												<option value="违规拆出资金" <s:if test="wgfxlx=='违规拆出资金'">selected</s:if> >违规拆出资金</option>
												<option value="违规对外投资及购买/出售资产" <s:if test="wgfxlx=='违规对外投资及购买/出售资产'">selected</s:if> >违规对外投资及购买/出售资产</option>
												<option value="关联交易违规行为③" <s:if test="wgfxlx=='关联交易违规行为③'">selected</s:if> >关联交易违规行为③</option>
												<option value="信息披露违规行为" <s:if test="wgfxlx=='信息披露违规行为'">selected</s:if> >信息披露违规行为</option>
												<option value="重大资产重组违规行为" <s:if test="wgfxlx=='重大资产重组违规行为'">selected</s:if> >重大资产重组违规行为</option>
												<option value="权益变动违规行为" <s:if test="wgfxlx=='权益变动违规行为'">selected</s:if> >权益变动违规行为</option>
												<option value="承诺未能及时履行情况" <s:if test="wgfxlx=='承诺未能及时履行情况'">selected</s:if> >承诺未能及时履行情况</option>
												<option value="会计估计更正" <s:if test="wgfxlx=='会计估计更正'">selected</s:if> >会计估计更正</option>
												<option value="会计政策变更" <s:if test="wgfxlx=='会计政策变更'">selected</s:if> >会计政策变更</option>
												<option value="前期差错更正" <s:if test="wgfxlx=='前期差错更正'">selected</s:if> >前期差错更正</option>
												<option value="合并范围调整事项及影响" <s:if test="wgfxlx=='合并范围调整事项及影响'">selected</s:if> >合并范围调整事项及影响</option>
												<option value="盈利预测等事项及其实现情况④" <s:if test="wgfxlx=='盈利预测等事项及其实现情况④'">selected</s:if> >盈利预测等事项及其实现情况④</option>
												<option value="挂牌公司相关业务开展情况" <s:if test="wgfxlx=='挂牌公司相关业务开展情况'">selected</s:if> >挂牌公司相关业务开展情况</option>
												<option value="控股股东、实际控制人失联" <s:if test="wgfxlx=='控股股东、实际控制人失联'">selected</s:if> >控股股东、实际控制人失联</option>
												<option value="持续经营能力存在不确定性事项" <s:if test="wgfxlx=='持续经营能力存在不确定性事项'">selected</s:if> >持续经营能力存在不确定性事项</option>
												<option value="涉及重大诉讼情况" <s:if test="wgfxlx=='涉及重大诉讼情况'">selected</s:if> >涉及重大诉讼情况</option>
												<option value="司法冻结事项" <s:if test="wgfxlx=='司法冻结事项'">selected</s:if> >司法冻结事项</option>
												<option value="重大舆情事项（媒体质疑）" <s:if test="wgfxlx=='重大舆情事项（媒体质疑）'">selected</s:if> >重大舆情事项（媒体质疑）</option>
												<option value="大额股权质押事项" <s:if test="wgfxlx=='大额股权质押事项'">selected</s:if> >大额股权质押事项</option>
												<option value="控股股东、实际控制人变动情况" <s:if test="wgfxlx=='控股股东、实际控制人变动情况'">selected</s:if> >控股股东、实际控制人变动情况</option>
												<option value="法定代表人、董事长、总经理、董事会秘书、财务总监更换情况" <s:if test="wgfxlx=='法定代表人、董事长、总经理、董事会秘书、财务总监更换情况'">selected</s:if> >法定代表人、董事长、总经理、董事会秘书、财务总监更换情况</option>
												<option value="券商风险提示公告事项" <s:if test="wgfxlx=='券商风险提示公告事项'">selected</s:if> >券商风险提示公告事项</option>
												<option value="公告事项" <s:if test="wgfxlx=='公告事项'">selected</s:if> >公告事项</option>
												<option value="会计师出具非标审计意见情况" <s:if test="wgfxlx=='会计师出具非标审计意见情况'">selected</s:if> >会计师出具非标审计意见情况</option>
												<option value="被中国证监会及证监局采取的行政处罚、监管措施或其他措施" <s:if test="wgfxlx=='被中国证监会及证监局采取的行政处罚、监管措施或其他措施'">selected</s:if> >被中国证监会及证监局采取的行政处罚、监管措施或其他措施</option>
												<option value="被全国股转公司采取的纪律处分或自律监管措施" <s:if test="wgfxlx=='被全国股转公司采取的纪律处分或自律监管措施'">selected</s:if> >被全国股转公司采取的纪律处分或自律监管措施</option>
												<option value="受到刑事处罚情况" <s:if test="wgfxlx=='受到刑事处罚情况'">selected</s:if> >受到刑事处罚情况</option>
												<option value="被其他部门采取行政处罚情况" <s:if test="wgfxlx=='被其他部门采取行政处罚情况'">selected</s:if> >被其他部门采取行政处罚情况</option>
												<option value="被中国证监会立案调查" <s:if test="wgfxlx=='被中国证监会立案调查'">selected</s:if> >被中国证监会立案调查</option>
												<option value="被全国股转公司公开问询或被证监局发函问询事项" <s:if test="wgfxlx=='被全国股转公司公开问询或被证监局发函问询事项'">selected</s:if> >被全国股转公司公开问询或被证监局发函问询事项</option>
												<option value="中国证监会及证监局或全国股转公司要求核查的信访举报事项" <s:if test="wgfxlx=='中国证监会及证监局或全国股转公司要求核查的信访举报事项'">selected</s:if> >中国证监会及证监局或全国股转公司要求核查的信访举报事项</option>
												<option value="其他事项" <s:if test="wgfxlx=='其他事项'">selected</s:if> >其他事项</option>
												<option value="挂牌承办人员所在业务部门⑤" <s:if test="wgfxlx=='挂牌承办人员所在业务部门⑤'">selected</s:if> >挂牌承办人员所在业务部门⑤</option>
												<option value="拖欠持续督导费用情况" <s:if test="wgfxlx=='拖欠持续督导费用情况'">selected</s:if> >拖欠持续督导费用情况</option>
												<option value="静默期买卖股票" <s:if test="wgfxlx=='静默期买卖股票'">selected</s:if> >静默期买卖股票</option>
												<option value="提前使用募集资金" <s:if test="wgfxlx=='提前使用募集资金'">selected</s:if> >提前使用募集资金</option>
												<option value="变更资金用途未审议" <s:if test="wgfxlx=='变更资金用途未审议'">selected</s:if> >变更资金用途未审议</option>
											</select>
										</td>
										<td></td>
									</tr>
								</table>
							</td>
							<td valign='bottom' style='padding-bottom:15px;'>
								<a id="search" class="easyui-linkbutton" icon="icon-search" href="javascript:void(0);">查询</a>
							</td>
						<tr>
					</table>
				</div>
			</div>
		</form>
		<div style="padding:5px">
	<table WIDTH="100%" style="border:1px solid #efefef">
			<TR  class="header" style="text-align: center;">
				<TD style="width:4%"><input type="checkbox"  name="chk_list" id="chkAll" onclick="selectAll()" value="0"></TD>
				<TD style="width:8%">公司代码</TD>
				<TD style="width:8%">公司简称</TD>
				<TD style="width:8%">填报人</TD>
				<TD style="width:8%">发现日期</TD>
				<TD style="width:20%">违规风险类型</TD>
				<TD style="width:44%">违规风险内容</TD>
			</TR>
			<s:iterator value="list"  status="status">
				<TR class="cell" >
				<TD style="text-align: center;"><input type="checkbox" name="chk_list" value="<s:property value="insId"/>" id="<s:property value="insId"/>"></TD>
				
				<TD style="text-align: center;"><s:property value="ycl"/></TD>
				<TD style="text-align: center;"><a href="javascript:updXcjc('<s:property value="insId"/>','<s:property value="sxgy_1"/>')"><s:property value="tjdsh"/></a></TD>
				<TD style="text-align: center;"><s:property value="sxmc"/></TD>
				<TD style="text-align: center;"><s:property value="sbrq"/></TD>
				<TD><s:property value="sxgy_1"/></TD>
				<TD ><s:property value="sxgy"/></TD>
				
			</TR>
			</s:iterator>
		</table>
			<form action="qywgfxjl.action" method=post name=frmMain id=frmMain>
				<s:hidden name="formid" id="formid"></s:hidden>
				<s:hidden name="demid" id="demid"></s:hidden>
				<s:hidden name="pageNumber" id="pageNumber"></s:hidden>
				<s:hidden name="pageSize" id="pageSize"></s:hidden>
				<s:hidden name="khbh" id="khbh"></s:hidden>
				<s:hidden name="khmc" id="khmc"></s:hidden>
				<s:hidden name="gsjc" id="gsjc"></s:hidden>
				<s:hidden name="gsdm" id="gsdm"></s:hidden>
			</form>
			<div id='prowed_ifrom_grid'></div>
		</div>
	</div>
	<div region="south"
		style="vertical-align:bottom;height:40px;border-top:1px solid #efefef;color:#0000FF;font-size:12px;padding-top:10px;padding-left:10px;"
		border="false">
		<div style="padding:5px">
			<s:if test="totalNum==0">

			</s:if>
			<s:else>
				<div id="pp"
					style="background:#efefef;text-align:right;border:1px solid #ccc;"></div>
			</s:else>
		</div>
	<script type="text/javascript">
	$(function(){
		$("#TZBT").val($("#tzbt").val());
		$("#SPZT").val($("#spzt").val());
		$("#TZLX").val($("#tzlx").val());
	});
	</script>
</body>
</html>
<script language="JavaScript"> 
  jQuery.validator.addMethod("string", function(value, element) {
          var sqlstr=[" and "," exec ", " count ", " chr ", " mid ", " master ", " or ", " truncate ", " char ", " declare ", " join ","insert ", "select ", "delete ", "update ","create ","drop "]
          var patrn=/[`~!#$%^&*+<>?"{},;'[\]]/im;
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