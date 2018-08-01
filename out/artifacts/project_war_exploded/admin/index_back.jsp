<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>KingOne系统管理平台【控制台】</title>
<link rel="stylesheet" type="text/css" href="css/theme.css" />
<link rel="stylesheet" type="text/css" href="css/style.css" />
<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/>
<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
<script> 
   var StyleFile = "theme" + document.cookie.charAt(6) + ".css";
   document.writeln('<link rel="stylesheet" type="text/css" href="css/' + StyleFile + '">');
   function openInfo(){
   }
   
   function openDialog(title,url){
   
   		art.dialog.open(url,{
					id:'adminWinDiv',
					cover:true,
					title:title,  
					loadingText:'正在加载中,请稍后...',
					bgcolor:'#999',
					rang:true,
					width:1000,
					cache:false, 
					lock: true,
					esc: true, 
					height:500,  
					iconTitle:false, 
					extendDrag:true,
					autoSize:true
				});
   }
</script>  

</head>
 
<body>
	<div id="container">
    	<div id="header">
        	<h2>KingOne系统管理平台</h2>
    		<div id="topmenu">
            	<ul>
                	<li class="current"><a href="#">运行维护</a></li>
                	 <li><a href="#">门户管理</a></li>
                    <li><a href="#">业务流程建模</a></li>
                	<li><a href="users.html">组织/权限管理</a></li>
                    <li><a href="#">系统运行监控</a></li>
                    <li><a href="#">管理员帮助手册</a></li>
                    <li><a href="#">应用商店</a></li>
              </ul>
          </div>
      </div>
        <div id="top-panel">
            <div id="panel">
                <ul>
                    <li><a href="#" onclick="openInfo();" class="report">触发器执行日志查询</a></li>
                    <li><a href="#" onclick="openDialog('导航管理','../sysNode_index.action');" class="report_seo">导航管理</a></li>
                    <li><a href="#" class="search">服务管理</a></li>
                    <li><a href="#" class="feed">计划任务</a></li>
                </ul>
            </div>
      </div>
        <div id="wrapper">
            <div id="content">
       			<div id="rightnow">
                    <h3 class="reallynow">
                        <span>预警提示</span>
                        <a href="#" class="add">Add New Product</a>
                        <a href="#" class="app_add">Some Action</a>
                        <br />
                    </h3>
				    <p class="youhave">You have <a href="#">19 new orders</a>, <a href="#">12 new users</a> and <a href="#">5 new reviews</a>, today you made <a href="#">$1523.63 in sales</a> and a total of <strong>$328.24 profit </strong>
                    </p>
			  </div>
              <div id="infowrap">
              <div id="infobox">
                    <h3>在线用户统计</h3>
                    <p><img src="img/index/graph.jpg" width="360" height="266" /></p>            
                  </div>
                  <div id="infobox" class="margin-left">
                    <h3>本周用户登录情况统计</h3> 
                    <p><img src="img/index/graph2.jpg" alt="a" width="359" height="266" /></p>
                  </div>
                  <div id="infobox">
                    <h3>系统缓存监控</h3>
                    <table>
						<thead>
							<tr>
                            	<th>缓存名称</th>
                                <th>缓存数</th>
                                <th>操作</th>
                            </tr>
						</thead>
						<tbody>
							<tr>
                            	<td><a href="#">流程节点模型缓存</a></td>
                                <td>1</td>
                                <td>清除缓存</td>
                            </tr>
							<tr>
                            	<td><a href="#">Mark Kyrnin</a></td>
                            	<td>2</td>
                                <td>34.27 €</td>
                            </tr>
							<tr>
                            	<td><a href="#">Virgílio Cezar</a></td>
                                <td>2</td>
                                <td>61.39 €</td>
                            </tr>
							<tr>
                            	<td><a href="#">Todd Simonides</a></td>
                                <td>5</td>
                                <td>1472.56 €</td>
                            </tr>
                            <tr>
                            	<td><a href="#">Carol Elihu</a></td>
                                <td>1</td>
                                <td>9.95 €</td>
                            </tr>
						</tbody>
					</table>            
                  </div>
                  <div id="infobox" class="margin-left">
                    <h3>Bestsellers</h3> 
                    <table>
						<thead>
							<tr>
                            	<th>Product Name</th>
                                <th>Price</th>
                                <th>Orders</th>
                            </tr>
						</thead>
						<tbody>
							<tr>
                            	<td><a href="#">Apple iPhone 3G 8GB</a></td>
                                <td>199.00 €</td>
                                <td>24</td>
                            </tr>
							<tr>
                            	<td><a href="#">Fuji FinePix S5800</a></td>
                            	<td>365.24 €</td>
                                <td>19</td>
                            </tr>
							<tr>
                            	<td><a href="#">Canon PIXMA MP140</a></td>
                                <td>59.50 €</td>
                                <td>12</td>
                            </tr>
							<tr>
                            	<td><a href="#">Apple iPhone 3G 16GB</a></td>
                                <td>199.00 €</td>
                                <td>10</td>
                            </tr>
                            <tr>
                            	<td><a href="#">Prenosnik HP 530 1,6GHz</a></td>
                                <td>499.00 €</td>
                                <td>6</td>
                            </tr>
						</tbody>
					</table>
                  </div>
                  <div id="infobox">
                    <h3>New Customers</h3>
                    <table>
						<thead>
							<tr>
                            	<th>Customer</th>
                                <th>Orders</th>
                                <th>Average</th>
                                <th>Total</th>
                            </tr>
						</thead>
						<tbody>
							<tr>
                            	<td><a href="#">Jennifer Kyrnin</a></td>
                                <td>1</td>
                                <td>5.6€</td>
                                <td>14.95 €</td>
                            </tr>
							<tr>
                            	<td><a href="#">Mark Kyrnin</a></td>
                            	<td>2</td>
                                <td>14.97€</td>
                                <td>34.27 €</td>
                            </tr>
							<tr>
                            	<td><a href="#">Virgílio Cezar</a></td>
                                <td>2</td>
                                <td>15.31€</td>
                                <td>61.39 €</td>
                            </tr>
							<tr>
                            	<td><a href="#">Todd Simonides</a></td>
                                <td>5</td>
                                <td>502.61€</td>
                                <td>1472.56 €</td>
                            </tr>
                            <tr>
                            	<td><a href="#">Carol Elihu</a></td>
                                <td>1</td>
                                <td>5.1€</td>
                                <td>9.95 €</td>
                            </tr>
						</tbody>
					</table>                 
                  </div>
                  <div id="infobox" class="margin-left">
                    <h3>Last 5 Reviews</h3> 
                    <table>
						<thead>
							<tr>
                            	<th>Reviewer</th>
                                <th>Product</th>
                                <th>Action</th>
                            </tr>
						</thead>
						<tbody>
							<tr>
                            	<td><a href="#">Jennifer Kyrnin</a></td>
                                <td><a href="#">Apple iPhone 3G 8GB</a></td>
                                <td><a href="#"><img src="img/index/icons/page_white_link.png" /></a><a href="#"><img src="img/index/icons/page_white_edit.png" /></a><a href="#"><img src="img/index/icons/page_white_delete.png" /></a></td>
                            </tr>
							<tr>
                            	<td><a href="#">Mark Kyrnin</a></td>
                            	<td><a href="#">Prenosnik HP 530 1,6GHz</a></td>
                                <td><a href="#"><img src="img/index/icons/page_white_link.png" /></a><a href="#"><img src="img/index/icons/page_white_edit.png" /></a><a href="#"><img src="img/index/icons/page_white_delete.png" /></a></td>
                            </tr>
							<tr>
                            	<td><a href="#">Virgílio Cezar</a></td>
                                <td><a href="#">Fuji FinePix S5800</a></td>
                                <td><a href="#"><img src="img/index/icons/page_white_link.png" /></a><a href="#"><img src="img/index/icons/page_white_edit.png" /></a><a href="#"><img src="img/index/icons/page_white_delete.png" /></a></td>
                            </tr>
							<tr>
                            	<td><a href="#">Todd Simonides</a></td>
                                <td><a href="#">Canon PIXMA MP140</a></td>
                                <td><a href="#"><img src="img/index/icons/page_white_link.png" /></a><a href="#"><img src="img/index/icons/page_white_edit.png" /></a><a href="#"><img src="img/index/icons/page_white_delete.png" /></a></td>
                            </tr>
                            <tr>
                            	<td><a href="#">Carol Elihu</a></td>
                                <td><a href="#">Prenosnik HP 530 1,6GHz</a></td>
                                <td><a href="#"><img src="img/index/icons/page_white_link.png" /></a><a href="#"><img src="img/index/icons/page_white_edit.png" /></a><a href="#"><img src="img/index/icons/page_white_delete.png" /></a></td>
                            </tr>
						</tbody>
					</table>
                  </div>
              </div>
            </div>
            <div id="sidebar">
  				<ul>
                	<li><h3><a href="#" class="house">流程管理</a></h3>
                        <ul>
                        	<li><a href="javascript:openDialog('委托管理','../syspersion_loadEntrustPage.action')" class="report">委托管理</a></li>
                    		<li><a href="#" class="report_seo">流程跟踪</a></li>
                            <li><a href="#" class="search">流程实例管理</a></li>
                        </ul>
                    </li>
                    <li><h3><a href="#" class="folder_table">权限管理</a></h3>
          				<ul>
                        	<li><a href="#" class="addorder">新增权限组</a></li>
                          <li><a href="#" class="shipping">权限维护</a></li>
                            <li><a href="#" class="invoices">查看指定用户功能权限</a></li>
                            <li><a href="#" class="invoices">按功能查看用户权限</a></li>
                        </ul>
                    </li>
                    <li><h3><a href="#" class="manage">管理维护</a></h3>
          				<ul>
          				 	<li><a href="#" class="folder">导航菜单维护</a></li>
                            <li><a href="#" class="manage_page">查看系统日志</a></li>
                            <li><a href="#" class="cart">查看错误日志</a></li>
                           
            				<li><a href="#" class="promotions">Promotions</a></li>
                        </ul>
                    </li>
                  <li><h3><a href="#" class="user">组织管理</a></h3>
          				<ul> 
                            <li><a href="javascript:openDialog('用户管理','../user_index.action')" class="useradd">添加用户</a></li>
                            <li><a href="#" class="group">添加角色</a></li>
                            <li><a href="#" class="group">添加部门</a></li>
                            <li><a href="#" class="group">添加团队</a></li>
            				<li><a href="#" class="search">添加组织单元</a></li>
                            <li><a href="#" class="online">Users online</a></li>
                        </ul>
                    </li>
				</ul>       
          </div>
      </div>
        <div id="footer">
        <div id="credits">
   		Template by <a href="http://www.bloganje.com">Bloganje</a>
        </div>
        <div id="styleswitcher">
            <ul>
                <li><a href="javascript: document.cookie='theme='; window.location.reload();" title="Default" id="defswitch">d</a></li>
                <li><a href="javascript: document.cookie='theme=1'; window.location.reload();" title="Blue" id="blueswitch">b</a></li>
                <li><a href="javascript: document.cookie='theme=2'; window.location.reload();" title="Green" id="greenswitch">g</a></li>
                <li><a href="javascript: document.cookie='theme=3'; window.location.reload();" title="Brown" id="brownswitch">b</a></li>
                <li><a href="javascript: document.cookie='theme=4'; window.location.reload();" title="Mix" id="mixswitch">m</a></li>
                <li><a href="javascript: document.cookie='theme=5'; window.location.reload();" title="Mix" id="defswitch">m</a></li>
            </ul> 
        </div><br />
        </div>
</div>
</body>
</html>
