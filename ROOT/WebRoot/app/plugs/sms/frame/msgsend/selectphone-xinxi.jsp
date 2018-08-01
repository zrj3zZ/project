<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">

<html>
	<head>
		<title>号码簿选号信息</title>
		<meta http-equiv="Content-Type" content="text/html; charset=gb2312">

	</head>
	<body>
		<s:form>
			<div id="win" style='width: 630px; margin-top: 0px;'></div>
			<table width="100%" border="1" cellspacing="0" cellpadding="2"
				bordercolorlight="#4A9FB5" bordercolordark="#FFFFFF" align="center">
				<tr>
					<td width="100%" align="center" valign="top">
						<table width="100%" border="1" cellspacing="0" cellpadding="0"
							bordercolorlight="#CCCCCC" bordercolordark="#FFFFFF">
							<tr>
								<td width="5%" class="actionsoftReportTitle" nowrap>
									<div align="center">
										<input type=checkbox value="全选"
											onClick="this.value=check(this.form.chk)">
									</div>
								</td>
								<td width="15%" class="actionsoftReportTitle" nowrap>
									<div align="center">
										分组
									</div>
								</td>
								<td width="5%" class="actionsoftReportTitle" nowrap>
									<div align="center">
										姓名
									</div>
								</td>
								<td width="10%" class="actionsoftReportTitle" nowrap>
									<div align="center">
										手机号
									</div>
								</td>
								<td width="20%" class="actionsoftReportTitle" nowrap>
									<div align="center">
										属性一
									</div>
								</td>
								<td width="20%" class="actionsoftReportTitle" nowrap>
									<div align="center">
										属性二
									</div>
								</td>
								<td width="20%" class="actionsoftReportTitle" nowrap>
									<div align="center">
										属性三
									</div>
								</td>
							</tr>
							<s:property value='model.list' escapeHtml='false'/>
						</table>
					</td>
				</tr>
			</table>
			<center>
				<div style='margin-top: 5px; margin-bottom: 0px;'>
					<input type=button value="选 号" onClick="phonebookAddnum('<s:property value="model.bphone"/>','<s:property value="model.bcontent"/>')">
					
					<input type=button value="退 出"
						onClick='parent.window.close();return false;' name='cancel'
						class='input' border='0'>
				</div>
			</center>

			<input type=hidden name=tvalue>


		</s:form>
	</body>
	
	<script type="text/javascript" src="iwork_js/plugs/qselectnum.js"></script>
	
</html>
