<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<div id="mainFrameTab"  class="easyui-tabs" fit="true" border="false"  style="width:100%;height:auto">
				<div title="首页"  cache="false" > 
					<iframe width='100%' height='99%'  src='iworkMainPage.action?channelid=0' frameborder=0  scrolling=yes  marginheight=0 marginwidth=0></iframe>
				</div>
				<div title="我的桌面"  cache="false" > 
				<iframe width='100%' height='100%'  src='pt_person_index.action' frameborder=0  scrolling=yes  marginheight=0 marginwidth=0></iframe>
				</div> 
				<div title="鹰眼检索"  cache="false" > 
				<iframe width='100%' height='100%'  src='eaglesSearch_Index.action' frameborder=0  scrolling=yes  marginheight=0 marginwidth=0></iframe>
				</div> 
			</div>