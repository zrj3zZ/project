<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
	<div class="easyui-layout" fit="true" style="background:#ccc;border:0px">
				<div region="center" style="background:#ccc;border:0px">
					<div id="sysTabs" class="easyui-tabs" fit="true" border="false" style="border:0px"> 
						<div title="我的菜单" closable="false" style="overflow:auto;padding:0px;">
							<div id="outlook"></div>
						</div>
						<div title="收藏夹" closable="false" selected="true" style="overflow:auto;padding:0px;background-image:url(iwork_img/noise.png)">
	                    	<div style="width:100%;border-bottom:1px solid #EFEFEF;background:#fff">
				            <a href="#" class="easyui-linkbutton" plain="true" iconCls="icon-add" title="整理收藏夹" onclick="arrcoll();">整理收藏夹</a>
				            <a href="#" class="easyui-linkbutton" plain="true" iconCls="icon-reload" title="刷新" onclick="reloadfav();">刷新</a>
				            </div>				
						     <ul id="favtree" class="ztree"></ul>
						</div>
						</div>
					</div>	
						
						
				</div>
				<div region="south" split="true"  style="height:42px;border:0px;border-top:1px solid #efefef;padding:5px;">
					<div class="tree_sr_box">
				       <input type="button" class="search_btn1" id="search_btn1" onclick="this.className='search_btn1'" >
 					   <input id="input_focus" class="tree_sr_input" value="输入菜单名搜索">
  					   <input type="button" class="search_btn3"  id="search_btn"> 
				    </div>
				</div>
		</div>