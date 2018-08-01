<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

	<div data-role="page" data-overlay-theme="e"> 
		<div data-role="header" data-theme="b"> 
			<h1>多选地址簿</h1> 
		</div>
		<div data-role="content" data-theme="d"> 
		<DIV>
			<label for="search-mini">输入您查询的地址信息</label>
				<input type="search" name="search-mini" id="search-mini" value="" data-mini="true"  onchange="dosearchMultiAddress('<s:property value="fieldName" escapeHtml="false"/>',this.value)"/>
				<div style="text-align:left">
					<table width="100%">
						<TR>
							<TD width="50%">
								<div style="height:200px;border:1px solid #efefef;overflow:auto">
								<div   id="searchList">	</div> 
								<input type="hidden" name="selectResult" id="selectResult"/>
								</div>
							</TD>
						
						</TR>
						<tr>
							<td style="text-align:right">
								<div id="selecthtml"></div> 
								<a href="javascript:$('#selectResult').val('');alert('已清空');" data-role="button" data-icon="plus" data-inline="true" data-mini="true">清空</a>
								<a href="javascript:alert(document.getElementByName('SYRXM_searchlist').value);" data-role="button" data-icon="plus" data-inline="true" data-mini="true">确定</a>
							</td>
						</tr>
					</table>
				</div>
		</div>
			
		</div>
	</div> 