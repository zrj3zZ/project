<!DOCTYPE html>  
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html xmlns="http://www.w3.org/1999/xhtml" lang="zh">  
<head>  
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<title><s:property value="systemTitle"/></title>
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
	<link rel="stylesheet" type="text/css" href="iwork_themes/easyui/gray/easyui.css">
	<link href="iwork_layout/skins2014/css/all.css" rel="stylesheet" type="text/css"/>
	<link href="iwork_layout/skins2014/css/core.css" rel="stylesheet" type="text/css"/>
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/zTreeStyle.css">
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="iwork_js/jquery.messager.js"></script> 
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.cluetip.min.js" ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.ztree.core-3.4.min.js"></script>	
	<script type="text/javascript" src="iwork_js/lhgdialog/lhgdialog.min.js?skin=default"></script> 
	<script type="text/javascript" src="iwork_js/locale/<s:property value="#session.WW_TRANS_I18N_LOCALE"/>.js"></script>    
	<script type="text/javascript" src="iwork_layout/skins2014/js/custom.js"></script> 
	<script type="text/javascript" src="iwork_layout/skins2014/js/index.js"></script> 
	<script type='text/javascript' src='https://static.qimingdao.com/apps/core/lang/zh-cn.js?v=20140422'></script>
	<script type='text/javascript' src='https://static.qimingdao.com/apps/core/static/js/all.js?v=20140422'></script>
	<script type='text/javascript' src='https://static.qimingdao.com/apps/core/static/js/core.js?v=20140422'></script>
</head>
<body  >
<s:include value="topbar.jsp">
   </s:include>
<div id="page-wrap">
        <div class="qq-fixed">
            <a href="" class="qg-ico18 ico-qq" target="_blank"></a>
        </div>
        <div id="feedback" class="feedback-fixed">
            <a href="javascript:void(0);" event-node="add_feedback_box"  class="qg-ico18 ico-edit"></a>
        </div>
        <div id="main-wrap">
            <div id="st-index-grid" class="st-grid">
            <!-- 左侧菜单  -->
				<s:include value="leftmenu.jsp">
				   </s:include>
                <div id="col8" class="st-section">
                    <!--右边-->
                    <div id="col3" class="st-index-right" >
	<div class="right-wrap">
		<div model-node = 'diy_widget' model-args='diyId=1'>
		<div class="ui-state-disabled mod-person-num border" >
			<a href="javascript:redirectUrl('process_desk_index.action')">
				<h4 node-event>待办事宜</h4>
				<strong><span id="todoNum">0</span></strong>
			</a>
			<a href="https://itd.qimingdao.com/core/Profile/follower?uid=3523">
				<h4>私信</h4>
				<strong event-node ="follower_count" event-args ="uid=3523">0</strong>
			</a>
			<a href="sysm">
				<h4>系统消息</h4><strong event-node ="feed_count" event-args ="uid=3523">2</strong>
			</a>
			<a href="https://itd.qimingdao.com/core/Collection/index?uid=3523">
				<h4>收藏</h4><strong event-node ="favorite_count" event-args ="uid=3523">
					0</strong>
			</a>
		</div>    
    <div class="ui-state-disabled m-weight border" >
    <div class="setup hover">
    <a href='javascript:;' onclick="addCountdown()" class="aicon-set font14"></a>
    <a href="javascript:void(0)" class="ico-circle-arrow-down" event-node='widget_toggle'></a>
    <a href="javascript:void(0)" class="ico-close" event-node='widget_close'></a>
    </div>
    <div class="hd">
        <h3>产品发布</h3>
            </div>
<div model-node="widget_child" class="m-w-countdown" >
    还有<span>0</span><span>0</span><span>3</span><span>0</span>天</div>
</div>
 <div class="ui-state-disabled m-weight border" model-node='widget_box' model-args = 'widget_user_id=1&widget_name=Weather&widget_desc=PUBLIC_WEATHER_SHOW&app_name=core&diyId=1' >
  <div class="weather-min">
    <span class="city-name">北京：</span>
    <span class="city-mini-weather">
        <img src="https://static.qimingdao.com/apps/core/static/image/weather/a0.jpg" width="22" height="22">36 ～24℃
    </span>
  </div>
	<div class="setup hover">
  <a href='javascript:;' onclick="setUpWeather()" class="aicon-set font14"></a>
	<a href="javascript:void(0)" class="ico-circle-arrow-down" event-node='widget_toggle'></a>
	<a href="javascript:void(0)" class="ico-close" event-node='widget_close'></a>
	</div>
    <div id='weather_loading' style="display:none;width:100%;text-align:center;padding-top:80px;height:90px;">
    <img src='https://static.qimingdao.com/apps/core/static/image/load.gif' ></div>
    <div model-node="widget_child" id='weather' >
        <div class="weather">
            <ul class="weather-detail">
                <li class="weather-item weather-today">
                    <div class="new-weather-link">
                        <img src="https://static.qimingdao.com/apps/core/static/image/weather/a0.jpg" class="weather-img">
                        <div class="weather-day"> </div>
                        <div class="weather-temp"> </div>
                        <div class="weather-condition"> </div>
                    </div>
                </li>
                <li class="weather-item weather-tomorrow">
                    <div class="new-weather-link">
                        <img src="https://static.qimingdao.com/apps/core/static/image/weather/a2.jpg" class="weather-img">
                        <div class="weather-day"> </div>
                        <div class="weather-temp"> </div>
                        <div class="weather-condition"> </div>
                    </div>
                </li>
                <li class="weather-item">
                    <div class="new-weather-link">
                        <img src="https://static.qimingdao.com/apps/core/static/image/weather/a1.jpg" class="weather-img">
                        <div class="weather-day"> </div>
                        <div class="weather-temp"> </div>
                        <div class="weather-condition"> </div>
                    </div>
                </li>
            </ul>
            <div class="weather-air">
                <div class="weather-level">轻度污染</div>
                <a href="javascript:;">今日空气质量指数(AQI)：<em class="air-serious"> </em></a>
            </div>
        </div>
        <div class="weather-setup" style="display:none">
              <ul>
              <li>选择您所在的地区</li>
              <li>
                <select id="select_pro" onchange="select_city(this.value)"></select>
                <select id="select_city"></select>
                </li>
              <li><a href="javascript:confirmWeather();" class="btn btn-green-mini mr10"><span>保存</span></a>
                    <a href="javascript:cancelWeather();" class="btn btn-mini"><span>取消</span></a></li>
              </ul>
            </div>
    </div>
    <script type="text/javascript">
    var city_code = '101010100';
    var city_data = {"\u5317\u4eac":{"\u5317\u4eac":"101010100"},"\u4e0a\u6d77":{"\u4e0a\u6d77":"101020100"},"\u5929\u6d25":{"\u5929\u6d25":"101030100"},"\u91cd\u5e86":{"\u91cd\u5e86":"101040100"},"\u9ed1\u9f99\u6c5f":{"\u54c8\u5c14\u6ee8":"101050101","\u9f50\u9f50\u54c8\u5c14":"101050201","\u7261\u4e39\u6c5f":"101050301","\u4f73\u6728\u65af":"101050401","\u7ee5\u5316":"101050501","\u9ed1\u6cb3":"101050601","\u5927\u5174\u5b89\u5cad":"101050701","\u4f0a\u6625":"101050801","\u5927\u5e86":"101050901","\u4e03\u53f0\u6cb3":"101051002","\u9e21\u897f":"101051101","\u9e64\u5c97":"101051201","\u53cc\u9e2d\u5c71":"101051301"},"\u5409\u6797":{"\u957f\u6625":"101060101","\u5409\u6797":"101060201","\u5ef6\u5409":"101060301","\u6566\u5316":"101060302","\u5b89\u56fe":"101060303","\u6c6a\u6e05":"101060304","\u548c\u9f99":"101060305","\u9f99\u4e95":"101060307","\u73f2\u6625":"101060308","\u56fe\u4eec":"101060309","\u56db\u5e73":"101060401","\u901a\u5316":"101060501","\u767d\u57ce":"101060601","\u8fbd\u6e90":"101060701","\u677e\u539f":"101060801","\u767d\u5c71":"101060901"},"\u8fbd\u5b81":{"\u6c88\u9633":"101070101","\u5927\u8fde":"101070201","\u978d\u5c71":"101070301","\u629a\u987a":"101070401","\u672c\u6eaa":"101070501","\u4e39\u4e1c":"101070601","\u9526\u5dde":"101070701","\u8425\u53e3":"101070801","\u961c\u65b0":"101070901","\u8fbd\u9633":"101071001","\u94c1\u5cad":"101071101","\u671d\u9633":"101071201","\u76d8\u9526":"101071301","\u846b\u82a6\u5c9b":"101071401"},"\u5185\u8499\u53e4":{"\u547c\u548c\u6d69\u7279":"101080101","\u5305\u5934":"101080201","\u4e4c\u6d77":"101080301","\u96c6\u5b81":"101080401","\u5353\u8d44":"101080402","\u5316\u5fb7":"101080403","\u5546\u90fd":"101080404","\u5174\u548c":"101080406","\u51c9\u57ce":"101080407","\u5bdf\u53f3\u524d\u65d7":"101080408","\u5bdf\u53f3\u4e2d\u65d7":"101080409","\u5bdf\u53f3\u540e\u65d7":"101080410","\u56db\u5b50\u738b\u65d7":"101080411","\u4e30\u9547":"101080412","\u901a\u8fbd":"101080501","\u9ad8\u529b\u677f":"101080510","\u8d64\u5cf0":"101080601","\u9102\u5c14\u591a\u65af":"101080701","\u4e34\u6cb3":"101080801","\u4e94\u539f":"101080802","\u78f4\u53e3":"101080803","\u4e4c\u524d\u65d7":"101080804","\u5927\u4f58\u592a":"101080805","\u4e4c\u4e2d\u65d7":"101080806","\u4e4c\u540e\u65d7":"101080807","\u6d77\u529b\u7d20":"101080808","\u90a3\u4ec1\u5b9d\u529b\u683c":"101080809","\u676d\u9526\u540e\u65d7":"101080810","\u9521\u6797\u6d69\u7279":"101080901","\u4e8c\u8fde\u6d69\u7279":"101080903","\u963f\u5df4\u560e":"101080904","\u82cf\u5de6\u65d7":"101080906","\u82cf\u53f3\u65d7":"101080907","\u6731\u65e5\u548c":"101080908","\u4e1c\u4e4c\u65d7":"101080909","\u897f\u4e4c\u65d7":"101080910","\u592a\u4ec6\u5bfa":"101080911","\u9576\u9ec4\u65d7":"101080912","\u6b63\u9576\u767d\u65d7":"101080913","\u6b63\u5170\u65d7":"101080914","\u591a\u4f26":"101080915","\u535a\u514b\u56fe":"101080916","\u4e4c\u62c9\u76d6":"101080917","\u547c\u4f26\u8d1d\u5c14":"101081000","\u4e4c\u5170\u6d69\u7279":"101081101","\u963f\u5c14\u5c71":"101081102","\u79d1\u53f3\u4e2d\u65d7":"101081103","\u80e1\u5c14\u52d2":"101081104","\u624e\u8d49\u7279":"101081105","\u7d22\u4f26":"101081106","\u7a81\u6cc9":"101081107","\u79d1\u53f3\u524d\u65d7":"101081109","\u963f\u5de6\u65d7":"101081201","\u963f\u53f3\u65d7":"101081202","\u989d\u6d4e\u7eb3":"101081203","\u62d0\u5b50\u6e56":"101081204","\u5409\u5170\u592a":"101081205","\u9521\u6797\u9ad8\u52d2":"101081206","\u5934\u9053\u6e56":"101081207","\u4e2d\u6cc9\u5b50":"101081208","\u8bfa\u5c14\u516c":"101081209","\u96c5\u5e03\u8d56":"101081210","\u4e4c\u65af\u6cf0":"101081211","\u5b6a\u4e95\u6ee9":"101081212"},"\u6cb3\u5317":{"\u77f3\u5bb6\u5e84":"101090101","\u4fdd\u5b9a":"101090201","\u5f20\u5bb6\u53e3":"101090301","\u627f\u5fb7":"101090402","\u5510\u5c71":"101090501","\u5eca\u574a":"101090601","\u6ca7\u5dde":"101090701","\u8861\u6c34":"101090801","\u90a2\u53f0":"101090901","\u90af\u90f8":"101091001","\u79e6\u7687\u5c9b":"101091101"},"\u5c71\u897f":{"\u592a\u539f":"101100101","\u5927\u540c":"101100201","\u9633\u6cc9":"101100301","\u664b\u4e2d":"101100401","\u957f\u6cbb":"101100501","\u664b\u57ce":"101100601","\u4e34\u6c7e":"101100701","\u8fd0\u57ce":"101100801","\u6714\u5dde":"101100901","\u5ffb\u5dde":"101101001","\u5415\u6881":"101101100"},"\u9655\u897f":{"\u897f\u5b89":"101110101","\u54b8\u9633":"101110200","\u5ef6\u5b89":"101110300","\u6986\u6797":"101110401","\u6e2d\u5357":"101110501","\u5546\u6d1b":"101110601","\u5b89\u5eb7":"101110701","\u6c49\u4e2d":"101110801","\u5b9d\u9e21":"101110901","\u94dc\u5ddd":"101111001","\u6768\u51cc":"101111101"},"\u5c71\u4e1c":{"\u6d4e\u5357":"101120101","\u9752\u5c9b":"101120201","\u6dc4\u535a":"101120301","\u5fb7\u5dde":"101120401","\u70df\u53f0":"101120501","\u6f4d\u574a":"101120601","\u6d4e\u5b81":"101120701","\u6cf0\u5b89":"101120801","\u4e34\u6c82":"101120901","\u83cf\u6cfd":"101121001","\u6ee8\u5dde":"101121101","\u4e1c\u8425":"101121201","\u5a01\u6d77":"101121301","\u67a3\u5e84":"101121401","\u65e5\u7167":"101121501","\u83b1\u829c":"101121601","\u804a\u57ce":"101121701"},"\u65b0\u7586":{"\u4e4c\u9c81\u6728\u9f50":"101130101","\u514b\u62c9\u739b\u4f9d":"101130201","\u77f3\u6cb3\u5b50":"101130301","\u660c\u5409":"101130401","\u5410\u9c81\u756a":"101130501","\u5e93\u5c14\u52d2":"101130601","\u8f6e\u53f0":"101130602","\u5c09\u7281":"101130603","\u82e5\u7f8c":"101130604","\u4e14\u672b":"101130605","\u548c\u9759":"101130606","\u7109\u8006":"101130607","\u548c\u7855":"101130608","\u5df4\u97f3\u5e03\u9c81\u514b":"101130610","\u94c1\u5e72\u91cc\u514b":"101130611","\u535a\u6e56":"101130612","\u5854\u4e2d":"101130613","\u963f\u62c9\u5c14":"101130701","\u963f\u514b\u82cf":"101130801","\u5580\u4ec0":"101130901","\u4f0a\u5b81":"101131001","\u5bdf\u5e03\u67e5\u5c14":"101131002","\u5c3c\u52d2\u514b":"101131003","\u4f0a\u5b81\u53bf":"101131004","\u5de9\u7559":"101131005","\u65b0\u6e90":"101131006","\u662d\u82cf":"101131007","\u7279\u514b\u65af":"101131008","\u970d\u57ce":"101131009","\u970d\u5c14\u679c\u65af":"101131010","\u594e\u5c6f":"101131011","\u5854\u57ce":"101131101","\u54c8\u5bc6":"101131201","\u548c\u7530":"101131301","\u963f\u52d2\u6cf0":"101131401","\u963f\u56fe\u4ec0":"101131501","\u4e4c\u6070":"101131502","\u963f\u514b\u9676":"101131503","\u963f\u5408\u5947":"101131504","\u535a\u4e50":"101131601","\u6e29\u6cc9":"101131602","\u7cbe\u6cb3":"101131603","\u963f\u62c9\u5c71\u53e3":"101131606"},"\u897f\u85cf":{"\u62c9\u8428":"101140101","\u65e5\u5580\u5219":"101140201","\u5c71\u5357":"101140301","\u6797\u829d":"101140401","\u660c\u90fd":"101140501","\u90a3\u66f2":"101140601","\u963f\u91cc":"101140701"},"\u9752\u6d77":{"\u897f\u5b81":"101150101","\u6d77\u4e1c":"101150201","\u9ec4\u5357":"101150301","\u6d77\u5357":"101150401","\u679c\u6d1b":"101150501","\u7389\u6811":"101150601","\u6d77\u897f":"101150701","\u6d77\u5317":"101150801","\u683c\u5c14\u6728":"101150901"},"\u7518\u8083":{"\u5170\u5dde":"101160101","\u5b9a\u897f":"101160201","\u5e73\u51c9":"101160301","\u5e86\u9633":"101160401","\u6b66\u5a01":"101160501","\u91d1\u660c":"101160601","\u5f20\u6396":"101160701","\u9152\u6cc9":"101160801","\u5929\u6c34":"101160901","\u6b66\u90fd":"101161001","\u6210\u53bf":"101161002","\u6587\u53bf":"101161003","\u5b95\u660c":"101161004","\u5eb7\u53bf":"101161005","\u897f\u548c":"101161006","\u793c\u53bf":"101161007","\u5fbd\u53bf":"101161008","\u4e24\u5f53":"101161009","\u4e34\u590f":"101161101","\u5408\u4f5c":"101161201","\u4e34\u6f6d":"101161202","\u5353\u5c3c":"101161203","\u821f\u66f2":"101161204","\u8fed\u90e8":"101161205","\u739b\u66f2":"101161206","\u788c\u66f2":"101161207","\u590f\u6cb3":"101161208","\u767d\u94f6":"101161301","\u5609\u5cea\u5173":"101161401"},"\u5b81\u590f":{"\u94f6\u5ddd":"101170101","\u77f3\u5634\u5c71":"101170201","\u5434\u5fe0":"101170301","\u56fa\u539f":"101170401","\u4e2d\u536b":"101170501"},"\u6cb3\u5357":{"\u90d1\u5dde":"101180101","\u5b89\u9633":"101180201","\u65b0\u4e61":"101180301","\u8bb8\u660c":"101180401","\u5e73\u9876\u5c71":"101180501","\u4fe1\u9633":"101180601","\u5357\u9633":"101180701","\u5f00\u5c01":"101180801","\u6d1b\u9633":"101180901","\u5546\u4e18":"101181001","\u7126\u4f5c":"101181101","\u9e64\u58c1":"101181201","\u6fee\u9633":"101181301","\u5468\u53e3":"101181401","\u6f2f\u6cb3":"101181501","\u9a7b\u9a6c\u5e97":"101181601","\u4e09\u95e8\u5ce1":"101181701","\u6d4e\u6e90":"101181801"},"\u6c5f\u82cf":{"\u5357\u4eac":"101190101","\u65e0\u9521":"101190201","\u9547\u6c5f":"101190301","\u82cf\u5dde":"101190401","\u5357\u901a":"101190501","\u626c\u5dde":"101190601","\u76d0\u57ce":"101190701","\u5f90\u5dde":"101190801","\u6dee\u5b89":"101190901","\u8fde\u4e91\u6e2f":"101191001","\u5e38\u5dde":"101191101","\u6cf0\u5dde":"101191201","\u5bbf\u8fc1":"101191301"},"\u6e56\u5317":{"\u6b66\u6c49":"101200101","\u8944\u9633":"101200201","\u9102\u5dde":"101200301","\u5b5d\u611f":"101200401","\u9ec4\u5188":"101200501","\u9ec4\u77f3":"101200601","\u54b8\u5b81":"101200701","\u8346\u5dde":"101200801","\u5b9c\u660c":"101200901","\u6069\u65bd":"101201001","\u5341\u5830":"101201101","\u795e\u519c\u67b6":"101201201","\u968f\u5dde":"101201301","\u8346\u95e8":"101201401","\u5929\u95e8":"101201501","\u4ed9\u6843":"101201601","\u6f5c\u6c5f":"101201701"},"\u6d59\u6c5f":{"\u676d\u5dde":"101210101","\u6e56\u5dde":"101210201","\u5609\u5174":"101210301","\u5b81\u6ce2":"101210401","\u7ecd\u5174":"101210501","\u53f0\u5dde":"101210601","\u6e29\u5dde":"101210701","\u4e3d\u6c34":"101210801","\u91d1\u534e":"101210901","\u8862\u5dde":"101211001","\u821f\u5c71":"101211101"},"\u5b89\u5fbd":{"\u5408\u80a5":"101220101","\u868c\u57e0":"101220201","\u829c\u6e56":"101220301","\u6dee\u5357":"101220401","\u9a6c\u978d\u5c71":"101220501","\u5b89\u5e86":"101220601","\u5bbf\u5dde":"101220701","\u961c\u9633":"101220801","\u4eb3\u5dde":"101220901","\u9ec4\u5c71":"101221001","\u6ec1\u5dde":"101221101","\u6dee\u5317":"101221201","\u94dc\u9675":"101221301","\u5ba3\u57ce":"101221401","\u516d\u5b89":"101221501","\u5de2\u6e56":"101221601","\u6c60\u5dde":"101221701"},"\u798f\u5efa":{"\u798f\u5dde":"101230101","\u53a6\u95e8":"101230201","\u5b81\u5fb7":"101230301","\u8386\u7530":"101230401","\u6cc9\u5dde":"101230501","\u6f33\u5dde":"101230601","\u9f99\u5ca9":"101230701","\u4e09\u660e":"101230801","\u5357\u5e73":"101230901","\u9493\u9c7c\u5c9b":"101231001"},"\u6c5f\u897f":{"\u5357\u660c":"101240101","\u4e5d\u6c5f":"101240201","\u4e0a\u9976":"101240301","\u629a\u5dde":"101240401","\u5b9c\u6625":"101240501","\u5409\u5b89":"101240601","\u8d63\u5dde":"101240701","\u666f\u5fb7\u9547":"101240801","\u840d\u4e61":"101240901","\u65b0\u4f59":"101241001","\u9e70\u6f6d":"101241101"},"\u6e56\u5357":{"\u957f\u6c99":"101250101","\u6e58\u6f6d":"101250201","\u682a\u6d32":"101250301","\u8861\u9633":"101250401","\u90f4\u5dde":"101250501","\u5e38\u5fb7":"101250601","\u76ca\u9633":"101250700","\u5a04\u5e95":"101250801","\u90b5\u9633":"101250901","\u5cb3\u9633":"101251001","\u5f20\u5bb6\u754c":"101251101","\u6000\u5316":"101251201","\u6c38\u5dde":"101251401","\u5409\u9996":"101251501","\u4fdd\u9756":"101251502","\u6c38\u987a":"101251503","\u53e4\u4e08":"101251504","\u51e4\u51f0":"101251505","\u6cf8\u6eaa":"101251506","\u9f99\u5c71":"101251507","\u82b1\u57a3":"101251508"},"\u8d35\u5dde":{"\u8d35\u9633":"101260101","\u9075\u4e49":"101260201","\u5b89\u987a":"101260301","\u90fd\u5300":"101260401","\u8d35\u5b9a":"101260402","\u74ee\u5b89":"101260403","\u957f\u987a":"101260404","\u798f\u6cc9":"101260405","\u60e0\u6c34":"101260406","\u9f99\u91cc":"101260407","\u7f57\u7538":"101260408","\u5e73\u5858":"101260409","\u72ec\u5c71":"101260410","\u4e09\u90fd":"101260411","\u8354\u6ce2":"101260412","\u51ef\u91cc":"101260501","\u5c91\u5de9":"101260502","\u65bd\u79c9":"101260503","\u9547\u8fdc":"101260504","\u9ec4\u5e73":"101260505","\u9ebb\u6c5f":"101260507","\u4e39\u5be8":"101260508","\u4e09\u7a57":"101260509","\u53f0\u6c5f":"101260510","\u5251\u6cb3":"101260511","\u96f7\u5c71":"101260512","\u9ece\u5e73":"101260513","\u5929\u67f1":"101260514","\u9526\u5c4f":"101260515","\u6995\u6c5f":"101260516","\u4ece\u6c5f":"101260517","\u94dc\u4ec1":"101260601","\u6bd5\u8282":"101260701","\u6c34\u57ce":"101260801","\u516d\u679d":"101260802","\u76d8\u53bf":"101260804","\u5174\u4e49":"101260901","\u6674\u9686":"101260902","\u5174\u4ec1":"101260903","\u8d1e\u4e30":"101260904","\u671b\u8c1f":"101260905","\u5b89\u9f99":"101260907","\u518c\u4ea8":"101260908","\u666e\u5b89":"101260909"},"\u56db\u5ddd":{"\u6210\u90fd":"101270101","\u6500\u679d\u82b1":"101270201","\u81ea\u8d21":"101270301","\u7ef5\u9633":"101270401","\u5357\u5145":"101270501","\u8fbe\u5dde":"101270601","\u9042\u5b81":"101270701","\u5e7f\u5b89":"101270801","\u5df4\u4e2d":"101270901","\u6cf8\u5dde":"101271001","\u5b9c\u5bbe":"101271101","\u5185\u6c5f":"101271201","\u8d44\u9633":"101271301","\u4e50\u5c71":"101271401","\u7709\u5c71":"101271501","\u51c9\u5c71":"101271601","\u96c5\u5b89":"101271701","\u7518\u5b5c":"101271801","\u963f\u575d":"101271901","\u5fb7\u9633":"101272001","\u5e7f\u5143":"101272101"},"\u5e7f\u4e1c":{"\u5e7f\u5dde":"101280101","\u97f6\u5173":"101280201","\u60e0\u5dde":"101280301","\u6885\u5dde":"101280401","\u6c55\u5934":"101280501","\u6df1\u5733":"101280601","\u73e0\u6d77":"101280701","\u4f5b\u5c71":"101280800","\u8087\u5e86":"101280901","\u6e5b\u6c5f":"101281001","\u6c5f\u95e8":"101281101","\u6cb3\u6e90":"101281201","\u6e05\u8fdc":"101281301","\u4e91\u6d6e":"101281401","\u6f6e\u5dde":"101281501","\u4e1c\u839e":"101281601","\u4e2d\u5c71":"101281701","\u9633\u6c5f":"101281801","\u63ed\u9633":"101281901","\u8302\u540d":"101282001","\u6c55\u5c3e":"101282101"},"\u4e91\u5357":{"\u6606\u660e":"101290101","\u5927\u7406":"101290201","\u7ea2\u6cb3":"101290301","\u66f2\u9756":"101290401","\u4fdd\u5c71":"101290501","\u6587\u5c71":"101290601","\u7389\u6eaa":"101290701","\u695a\u96c4":"101290801","\u666e\u6d31":"101290901","\u662d\u901a":"101291001","\u4e34\u6ca7":"101291101","\u6012\u6c5f":"101291201","\u9999\u683c\u91cc\u62c9":"101291301","\u5fb7\u94a6":"101291302","\u7ef4\u897f":"101291303","\u4e2d\u7538":"101291304","\u4e3d\u6c5f":"101291401","\u5fb7\u5b8f":"101291501","\u666f\u6d2a":"101291601","\u52d0\u6d77":"101291603","\u52d0\u814a":"101291605"},"\u5e7f\u897f":{"\u5357\u5b81":"101300101","\u5d07\u5de6":"101300201","\u67f3\u5dde":"101300301","\u6765\u5bbe":"101300401","\u6842\u6797":"101300501","\u68a7\u5dde":"101300601","\u8d3a\u5dde":"101300701","\u8d35\u6e2f":"101300801","\u7389\u6797":"101300901","\u767e\u8272":"101301001","\u94a6\u5dde":"101301101","\u6cb3\u6c60":"101301201","\u5317\u6d77":"101301301","\u9632\u57ce\u6e2f":"101301401"},"\u6d77\u5357":{"\u6d77\u53e3":"101310101","\u4e09\u4e9a":"101310201","\u4e1c\u65b9":"101310202","\u4e34\u9ad8":"101310203","\u6f84\u8fc8":"101310204","\u510b\u5dde":"101310205","\u660c\u6c5f":"101310206","\u767d\u6c99":"101310207","\u743c\u4e2d":"101310208","\u5b9a\u5b89":"101310209","\u5c6f\u660c":"101310210","\u743c\u6d77":"101310211","\u6587\u660c":"101310212","\u4fdd\u4ead":"101310214","\u4e07\u5b81":"101310215","\u9675\u6c34":"101310216","\u897f\u6c99":"101310217","\u5357\u6c99":"101310220","\u4e50\u4e1c":"101310221","\u4e94\u6307\u5c71":"101310222"},"\u9999\u6e2f":{"\u9999\u6e2f":"101320101"},"\u6fb3\u95e8":{"\u6fb3\u95e8":"101330101"},"\u53f0\u6e7e":{"\u53f0\u5317":"101340101","\u9ad8\u96c4":"101340201","\u53f0\u4e2d":"101340401"}};
    var flod = '0';
    $(function(){
        loadCity(city_code);
        var j = 0,_select_pro = '',_select_city = '';
        for(var i in city_data){
            _select_pro += '<option value="'+i+'">'+i+'</option>';
            if(j == 0){
                for(var _i in city_data[i]){
                    _select_city +="<option value='"+city_data[i][_i]+"'>"+_i+"</option>";
                }
            }
            j++;
        }
        $('#select_pro').html(_select_pro);
        $('#select_city').html(_select_city);
    });

    var select_city = function(city){
        var _select_city = '';
        for(var i in city_data[city]){
            _select_city +="<option value='"+city_data[city][i]+"'>"+i+"</option>";
        }
       $('#select_city').html(_select_city);
    }
    var setUpWeather = function(){
        $('.weather-setup').show();
        $('.weather').hide();
    }
    var confirmWeather = function(){
        $('.weather-setup').hide();
        $('.weather').show();
        var city_code = $('#select_city')[0].value;
        console.log(city_code);
        loadCity(city_code);
    }
    var cancelWeather = function(){
        $('.weather-setup').hide();
        $('.weather').show();
    }
    var loadCity = function(id){
        $('#weather').hide();
        if( flod ==0 ){
            $('#weather_loading').show();
        }
        $.get(U('widget/Weather/getData'),{ id:id },function(data){
            $('.city-name').html(data.city+':');
            var img_src = 'https://static.qimingdao.com/apps/core/static/image/weather/';
            $('.city-mini-weather').html('<img src="'+img_src+data.temp[0].img+'.jpg" width=22 height=22>'+data.temp[0].temp);
            $('.weather-detail').find('li').each(function(i){
                $(this).find('div').find('img').attr('src',img_src+data.temp[i].img+'.jpg');
                $(this).find('div').find('.weather-day').html(data.date[i]+'('+data.week[i]+')');
                $(this).find('div').find('.weather-temp').html(data.temp[i].temp);
                $(this).find('div').find('.weather-condition').html(data.temp[i].desc);
            });
            //需要根据等级修改颜色 data.aqi_level (.weather-aqi0)
            $('.weather-air').find('div').removeClass();
            $('.weather-air').find('div').addClass('weather-level'+data.aqi_level).html(data.aqi_note);
            $('.air-serious').html(data.aqi);
            if( flod ==0 ){
                $('#weather').show();
            }
            $('#weather_loading').hide();
        },'json');
    }
    </script>
</div>    

<script type="text/javascript">
M.addEventFns({
    'hot_change':{
        click:function(){
            var obj = this.parentModel.childModels['widget_child'][0];
            $.get(U('widget/RecommendUser/render'),{ change_type:1 },function(html){
               $(obj).html(html);
               M(obj);
            });
        }
    }
}).addModelFns({
    'recommend-user':{
        load:function(){
            var _this = this;
            if( $(this.childEvents['doFollow']).length <1 ){
                return false;
            }
            $(this.childEvents['doFollow'][0]).click(function(){
                var user_ids = '';
                $('#all_users img').each(function(){
                   user_ids += $(this).attr('uid')+',';
                });
                $.get(U('widget/RecommendUser/getOneUser'),{ user_ids:user_ids },function(data){
                    if(data == ''){
                        $(_this).remove();
                    }
                    $(_this).html(data);
                    M(_this);
                });

            });
        }
    }
});
</script>    <div class="ui-state-disabled m-weight border" model-node='widget_box' model-args = 'widget_user_id=1&widget_name=IofficePunch&widget_desc=IOFFICE_PUBLIC_APPNAME&app_name=ioffice&diyId=1'>
    <div class="setup hover">
        <a event-node="widget_toggle" class="ico-circle-arrow-down" href="javascript:void(0)"></a>
        <a event-node="widget_close" class="ico-close" href="javascript:void(0)"></a>
    </div>
<div class="punch-card"  model-node="widget_child" >
    <div id='punch' class="puch-head">
    <div id='date'>
    </div>
    </div>
    <div class="punch-info">
        <ul>
            <li class="punchoff right"><span class="li-left">下班：</span>
                                <span id="end_work"  >
                                <a href="javascript:;" event-node="add_end_punch" class="btn btn-green add_punch"><span>签退</span></a>                </span>
            </li>
            <li class="punchon left"><span class="li-left">上班：</span>
                <span id='start_work'  >
                                                <a href="javascript:;" class="btn btn-green add_punch"><span>签到</span></a>
                                </span>
                
            </li>
        </ul>
    </div>
</div>
</div>


<script type="text/javascript">
M.addEventFns({
    add_start_punch:{
        click:function(){
            $.post(U('ioffice/Do/doAddStartPunch'),{ },function(msg){
                if(msg.status == 1){
                    if(msg.late == 1){
                        $('#start_work').addClass('belate');
                        $('#start_work_pop').addClass('belate');
                    }
                    $('#start_work').html('<b>'+msg.data+'</b>');
                    $('#start_work_pop').html('<b>'+msg.data+'</b>');
                    $("#pop-punch-public").fadeOut(1000);
                }else{
                    ui.error(msg.info);
                }
            },'json');
        }
    },
    add_end_punch:{
        click:function(){
            var obj = this;
            $.post(U('widget/IofficePunch/doCheck'),{ is_widget:'ioffice',sign:'end' },function(res){
                if(res.status == 1){
                    ui.confirm(obj,L('IOFFICE_SURE_SIGN_OUT'),function(){
                        doAddEndPunch();
                    });

                }else{
                    doAddEndPunch();
                    $("#pop-punch-public").fadeOut(1000);
                }

            },'json');
        }
    }
});
var doAddEndPunch = function(){
    $.post(U('ioffice/Do/doAddEndPunch'),{ },function(msg){
        if(msg.status == 1){
            if(msg.late == 1){
                $('#end_work').addClass('belate');
                $('#end_work_pop').addClass('belate');
            }
            $('#end_work').html('<b>'+msg.data+'</b>');
            $('#end_work_pop').html('<b>'+msg.data+'</b>');
            if($('.add_punch').length > 1){
                $('.add_punch').remove();
                $('#start_work').addClass('belate').append('<b>'+L('IOFFICE_MISSING_CARD')+'</b>');
                $('#start_work_pop').addClass('belate').append('<b>'+L('IOFFICE_MISSING_CARD')+'</b>');
            }
        }else{
            ui.error(msg.info);
        }
    },'json');
};
$(function(){
    showDate();
    setInterval(showDate,1000);

});
var showDate = function(){
    var d = new Date();
    var month  = d.getMonth()+1;
    var day    = d.getDate();
    var hour   = d.getHours();
    var minute = d.getMinutes();
    var seconds= d.getSeconds();

    if(month < 10){
        month = '0'+month;
    }
    if(day < 10){
        day = '0'+day;
    }
    if(hour < 10){
        hour = '0'+hour;
    }
    if(minute < 10){
        minute = '0'+minute;
    }
    if(seconds < 10){
        seconds = '0'+seconds;
    }
    var seven_week = L('IOFFICE_SEVEN_WEEK');
    var str  = '<div class="date">'+d.getFullYear()+'-'+month+'-'+day+'</div>';
        str += '<div class="week">'+L('IOFFICE_WEEK')+seven_week.charAt(d.getDay())+'</div>';
        str += '<div class="time"><span class="hour">'+hour+'</span><span class="minute">'+minute+'</span>';
        str += '<span class="seconds">'+seconds+'</span>';
    if($('#date').length > 0){
        $('#date').html(str);
    }

}
</script>    <div class="ui-state-disabled m-weight border" model-node='widget_box' model-args = 'widget_user_id=1&widget_name=MyTask&widget_desc=ITASK_MY&app_name=task&diyId=1' >
    <div class="setup hover">
        <a href="javascript:void(0)" class="ico-circle-arrow-down" event-node='widget_toggle'></a>
        <a href="javascript:void(0)" class="ico-close" event-node='widget_close'></a>
    </div>
    <h3 class="hd">我的任务<span class="task-count"><em class="over" id="myitask_over">0</em> / <em class="todo" id="myitask_unfinished">1</em></span></h3>
    <div model-node="widget_child" class="m-w-task" >
                      <input type='text' name='task' id='task' class="q-txt left" placeholder='请输入任务名称'>
              <a href="javascript:;" class="btn" id='sub' ><span>添加</span></a>    </div>
    <div id="addTaskContent" style="display:none;">
        <div class="pop-create-group">
            <dl>
            <dt>
                名称：
            <input event-node="taskTitle" type="text" class="q-txt" value="" id="task_title" />
            </dt>
           <dd>
                <a href="javascript:void(0);" class="btn btn-green mr10" onclick="myTask.doAdd()"><span>确认</span></a>
                <a href="javascript:void(0);" onclick="ui.box.close();" class="btn"><span>取消</span></a>
            </dd>
        </dl>
        </div>
    </div>

    <input type="hidden" value="TASK_UNDO,全部" id="task_today_key" />
    <input type="hidden" value="#639CF5,#E04C3E" id="task_today_color" />

</div>
<script>

$("#sub").click(function(){
var name = $("#task").val();
if(name==''){
    return false;
}
var date = '2014-04-24';
$.post(U('task/Do/doAdd'), { title:name,deadline:date }, function(msg){
    if(msg){
        ui.success( LANG.PUBLIC_SUCCESS );
        location.reload();
    }else{
        ui.error(  LANG.PUBLIC_FAIL );
    }

})
});
</script>

<script>
$(".myTaskCB").attr('checked',false);
var myTask = { };
myTask.overNum = '1';
myTask.unfinishedNum = '0';

myTask.add = function(){
    ui.box.close();
    ui.box.show( $('#addTaskContent').html(), LANG.ITASK_ADD_TASK,'afterBox()');
    return false;
};

// 关闭窗口的回调函数
function afterBox() {
    if(typeof(rcalendar_close) == 'function') {
        rcalendar_close();
    }
}
myTask.doAdd = function(){
    var title = $('#task_title').val();
    if( title.length <2 ){
        ui.error(L('PUBLIC_TITLE_ISNULL'));
        return false;
    }

    var url = U('task/Do/doAdd');
    var deadline = '2014-04-24';
    $.post(url,{ title:title,deadline:deadline },function(){
        ui.box.close();
        location.reload();
    });
};
myTask.over = function( task_id ){
    $.post(U('task/Do/doSetOver'), {  task_id:task_id, status:1 }, function(msg) {
        if( msg.status = 1 ){

            myTask.overNum++;
            myTask.unfinishedNum--;
            myTask.all_count = '1';

            $('#myitask_unfinished').html(myTask.all_count);
            $('#myitask_over').html(myTask.unfinishedNum);

            myTask.ajax();

        }else{
            ui.error(LANG.TASK_SET_ERROR);
        }
    }, 'json');
};

myTask.reflushPoit = function() {
    var args = { };
    args.type = 'pieChart';
    args.id = 'task';
    args.key =  $('#task_today_key').val();
    args.value = myTask.overNum + ',' + myTask.unfinishedNum;
    //alert(args.value);
    args.color = $('#task_today_color').val();

    (function(){
        var test = new plotChart();
        test.showPlot(args);
    })();

};
myTask.ajax = function(){
     // 刷新下右边区域
    $.get(U('widget/MyTask/todayTask'),{ is_widget:'task' },function(msg){
        if(msg.status ==0){
            var html=" <div>"+"今天没有待办任务"+"</div>";
            $("#itask_list").html( html );
        }else{
            //有数据
            $("#itask_list").html( msg.data );
        }
        myTask.reflushPoit( );
    },'json');
}

</script>    <div class="ui-state-disabled m-weight border" model-node='widget_box' model-args = 'widget_user_id=1&widget_name=BlogMy&widget_desc=BLOG_MY_BLOG&app_name=blog&diyId=1'>
    <div class="setup hover">
        <a href=" https://itd.qimingdao.com/blog/Personal/index " target='_blank' class="qg-ico18 ico-mor"></a>
        <a event-node="widget_toggle" class="ico-circle-arrow-down" href="javascript:void(0)"></a>
        <a event-node="widget_close" class="ico-close" href="javascript:void(0)"></a>
    </div>
    <div class="hd"><h3>我的博客</h3></div>
    <div class="m-w-blog" model-node="widget_child" >
        <dl>
                </dl>
    </div>
</div>    <div class="ui-state-disabled m-weight border" model-node='widget_box' model-args = 'widget_user_id=1&widget_name=ProjectMy&widget_desc=PROJECT_MY_PROJECT&app_name=project&diyId=1'>
    <div class="setup hover">
        <a target="_blank" href="https://itd.qimingdao.com/project/Index/index" class="qg-ico18 ico-mor"></a>
        <a event-node="widget_toggle" class="ico-circle-arrow-down" href="javascript:void(0)"></a>
        <a event-node="widget_close" class="ico-close" href="javascript:void(0)"></a>
    </div>
    <div class="hd"><h3>我的项目</h3></div>
    <div class="m-w-project" model-node="widget_child" >
            </div>
</div>    <div class="ui-state-disabled m-weight border" model-node='widget_box' model-args = 'widget_user_id=1&widget_name=Meeting&widget_desc=MEETING_PUBLIC_APPNAME&app_name=meeting&diyId=1'>
    <div class="setup hover">
        <a target="_blank" href="https://itd.qimingdao.com/meeting/Index/index" class="qg-ico18 ico-mor"></a>
        <a event-node="widget_toggle" class="ico-circle-arrow-down" href="javascript:void(0)"></a>
        <a event-node="widget_close" class="ico-close" href="javascript:void(0)"></a>
    </div>
    <div class="hd"><h3>我的会议</h3><span></span></div>
    <div class="m-w-meeting" model-node="widget_child" >
            </div>
</div>
<style>
.ui-state-highlight \{background-color: red\}
</style>
<div class="weight-add m-weight" >
    <a href="javascript:;" event-node='widget_add' event-args = 'widget_user_id=1&diyId=1'>
    <i class="ico-add-green"></i>添加</a>
</div>
</div>        	</div>
</div>
                    <div id="col5" class="st-index-main">
                    <div class="border boxShadow extend-foot minh">
                        <div class="qg-poster div-focus">

                               <div class="qg-poster-tab">
                                    <div id='diffcss'>
                                        <ul>
                                            <li class="current" rel="kind-weibo"><span><i class="aicon-blog-post"></i><a href="javascript:;">随手记</a></span></li>
                                                                                        <li rel="kind-task"><span><i class="aicon-task"></i><a href="javascript:;">发私信</a></span></li>
                                                                                        <li rel="kind-blog"><span><i class="aicon-bubbles"></i><a href="javascript:;">提问题</a></span></li>
                                                                                        <li rel="kind-file"><span><i class="aicon-bubbles"></i><a href="javascript:;">提问题</a></span></li>
                                        </ul>
                                    </div>
                                </div>
                                <div rel="kind-weibo" class="qg-poster-wb block" style="display:block">
                                    <div class="pwb-hd"><em class="arrline">◆</em><span class="downline">◆</span></div>
<div class="pwb-bd" model-node="send_feed" model-args='post_event=post_feed'>
    <div class="input">
        <div class="input_before">
            <div model-node="nums_left" class="number"><span>140</span></div>
            <div model-node='initHtml' style='display:none'></div>
            <textarea class="textinput" event-node='mini_editor_textarea' rel="fangan" placeholder="记录工作的想法" event-args='has_out_user=0'></textarea>
            <div model-node="post_ok" style="display:none;text-align:center;position:absolute;left:0;top:40%;width:100%">
                <i class="ico-ok"></i>保存成功</div>
            </div>
        <div class="poster-addons">
            <div class="kind">
                <a class="btn btn-green right" href="javascript:;" title="按Ctrl+Enter可以快速提交 "><span>保存</span></a>
              <div class="acts">
                    <a event-node="insert_face" class="face-block" href="javascript:;"><i class="aicon-face"></i></a><a event-node="insert_at" class="face-block" href="javascript:;"><i class="aicon-at"></i></a><a event-node="insert_topic" class="face-block" href="javascript:;"><i class="aicon-topic"></i></a>
                                        <a href="javascript:;" class="image-block" event-node='image_block' event-args='imagelimit=4'><i class="aicon-pic" ></i></a>
                    <div class="tips-img" style="display:none"><dl><dd><i class="ico-arrow-up"></i>gif,jpg,jpeg,png,bmp</dd></dl></div>
                    
                                        <a class="file-block" href="javascript:;"><i class="aicon-file"></i>
                     <FORM style='display:inline;padding:0;margin:0;border:0' >
                    <input type="file" name="attach" inputname='attach' urlquery='attach_type=feed_file' event-node="add_file"   onchange="core.plugInit('uploadFile',this,'','all');" id='attach'>
                    </FORM>
                    </a>
                                                                <a event-node="insert_video" href="javascript:;" class="face-block"><i class="aicon-video"></i></a>
                        <input type="hidden" name='video_info' style='display:none' value='' class='video_info'>
                                                                <a event-node="weibo_insert_vote" href="javascript:;" class="face-block" event-args="app_name=core&table_name=feed&row_id=&can_vote=&edit_vote=1&export_vote=1&callback=core.vote.weiboAddVoteCallback"><i class="aicon-vote"></i></a>

                                    </div>
            </div>
        </div>
    </div>
</div>
</div>
                                <!--记任务-->
                        <div rel="kind-task" class="qg-poster-t block" style="display:none">
                                    <div class="pwb-hd"><em class="arrline">◆</em><span class="downline">◆</span></div>
                                    <div class="m-p-tadd" model-node='post_task'>
    <!--下拉列表-->
	<dl>
    	<dd class="hd">
            <input event-node="itask_title" id="itask_title" event-node="itask_title" type="text left" class="q-txt left" style="width:429px" placeholder="点击这里添加一条任务...">
            <a class="btn btn-green" event-node="add_itask_title" ><span>添加</span></a>
        </dd>
    	<dd>
            <div class="qg-userlist">
                 <div class="choose-user" model-node='search_link'
model-args='max=1&model_name=SpaceUser&app_name=task&search_method=getSearchList&default_ids=&inputname=joiner_uid&list_width=0&search_tips=&mulit=0'>
<ul model-node="search_list" class="user-list" style='display:none'>
</ul>
<input type="hidden" id="search_ids_joiner_uid" name="joiner_uid" value="" >
<input event-node="search_link" placeholder="责任人" type="text" class="q-txt" autocomplete="off" style="width:224px;" ></div>
<script type="text/javascript">
    $('.choose-user').bind('click',function(){
        $(this).find('.q-txt').focus();
    });
</script>            </div>
            <div class="deadline">
                             <div class="dateLine" model-node='date_select' model-args='inputId=isTime&dtype=1&timeIputId=&callback='>
    <div class="dateLine-inner" rel='manage'>
    <i class="aicon-time font16"></i>
    <input name="deadline" event-node='date_input' type="text" class="start s-txt q-txt  rcalendar_input left" id="isTime" value='2014-04-24 今天' readonly="readonly" placeholder="截止时间	 " style='width:150px' />
    </div>        <div style='display: none;left:0' model-node='date_show' class="time-picker drop_clayer">
        <div class="qm-day-inner">
        <div class="days">
        <span event-node='date_click'  event-args='date=2014-04-24&desc=今天'>今天</span>
        <span event-node='date_click'  event-args='date=2014-04-25&desc=明天'>明天</span>
        <span event-node='date_click'  event-args='date=2014-04-26&desc=后天'>后天</span>
        <span event-node='date_click'  event-args='date=2014-04-27&desc=星期日'>星期日</span>
        <span event-node='date_click'  event-args='date=2014-04-28&desc=星期一'>星期一</span>
        <span event-node='date_click'  event-args='date=2014-04-29&desc=星期二'>星期二</span>
        <span event-node='date_click'  event-args='date=2014-04-30&desc=星期三'>星期三</span>
        </div>
        <span event-node='date_click'  event-args='date=other&desc=' class="rcalendar_input other-time">其他日期&raquo;</span>
        </div>
    </div>
    </div>
<script type="text/javascript">
    bindDateSelect();
    //日期
    var GetDateStr = function(AddDayCount){
        var dd = new Date();
        //获取AddDayCount天后的日期
        dd.setDate(dd.getDate()+AddDayCount);
        var y = dd.getFullYear();
        //获取当前月份的日期
        var m = dd.getMonth()+1;
        var d = dd.getDate();
        if(m<10){
            m = '0'+m;
        }
        if(d < 10){
            d = '0'+d;
        }
        return y+"-"+m+"-"+d;
    }

    M.addEventFns({
        'date_click':{
            click:function(){
                var args  = M.getEventArgs(this);
                var pObj  = this.parentModel.parentModel;
                var pArgs = M.getModelArgs(pObj);
                $(this).parent().find('span').not('.rcalendar_input').attr('class','');
                $(this).parent().find('i').remove() ;

                if(args.date == 'other'){
                    var obj = this;
                    core.rcalendar(document.getElementById(pArgs.inputId),pArgs.dtype,function(){
                        $(obj.parentModel).hide();
                        //日期处理
                        var value         = $('#'+pArgs.inputId).val();
                        //今天
                        var today         = GetDateStr(0);
                        //明天
                        var tomorrow      = GetDateStr(1);
                        //后天
                        var aftertomorrow = GetDateStr(2);

                        if( value == today ){
                            var str = value + " "+L("PUBLIC_W_TODAY");
                            $('#'+pArgs.inputId).val(str);
                        }
                        if( value == tomorrow ){
                            var str = value+" "+L("PUBLIC_W_TOMORROW");
                            $('#'+pArgs.inputId).val(str);
                        }
                        if( value == aftertomorrow ){
                            var str = value+" "+L("PUBLIC_W_AFTER_TOMORROW");
                            $('#'+pArgs.inputId).val(str);
                        }

                    });

                }else{
                    $(this).attr('class','current');
                    $(this).prepend("<i class='ico-selected'></i>");
                    $('#'+pArgs.inputId).val(args.date+' '+args.desc);
                    $(this.parentModel).hide();

                }
            }
        },
        'date_input':{
            click:function(){
                $(".time-picker").hide();
                $(this.parentModel.childModels['date_show'][0]).show();
            }
        },
        'time_input':{
            click:function(){
                $(".time-picker").hide();
                
                $(this.parentModel.childModels['time_show'][0]).css("right","0").show();
                var height = $(this.parentModel.childModels['time_show'][0]).find('.qm-time-inner')[0].scrollHeight;

                var count = $(this.parentModel.childModels['time_show'][0]).find('.qm-time-inner').children().length;
                var c_count = 0;
                $(this.parentModel.childModels['time_show'][0]).find('.qm-time-inner').children().each(function(i){
                    if( $(this).attr('class')=='current' ){
                        c_count = i;
                    }
                });
                var to_height = (c_count/count)*height;
                $(this.parentModel.childModels['time_show'][0]).find('.qm-time-inner').scrollTop(to_height);
            }
        },
        'time_click':{
            click:function(){
                $(this).addClass('current').siblings().removeClass('current');
                $(this).parent().find('i').remove() ;
                $(this).prepend("<i class='ico-selected'></i>");

                var tObj  = this.parentModel.parentModel;
                var pArgs = M.getModelArgs(tObj);
                $(tObj.childEvents['time_input'][0]).find('label').text($(this).text());
                $('#'+pArgs.timeIputId).val($(this).text());
                $(this.parentModel).hide();

                if(pArgs.callback != ''){
                    if("function"==typeof(pArgs.callback)){
                        pArgs.callback();
                    }else{
                        eval(pArgs.callback);
                    }
                }
            }
        }
    });
</script>            </div>
        </dd>
    	<dd>
            <textarea event-node="taskDesc" id="contents" placeholder="详细描述" name="content" cols="" rows="" class="q-textarea" style="width:490px"></textarea>
        </dd>
    </dl>
</div>
                               </div>
                                                                <!--发博客-->
                                                                <div rel="kind-blog" class="qg-poster-b block" style="display:none">
                                    <div class="pwb-hd"><em class="arrline">◆</em><span class="downline">◆</span></div>
                                    <form method="post" id="addBlogForm" action="https://itd.qimingdao.com/blog/Do/post" model-node="addBlogForm">
<div class="mod-blog-add">
   <dl>
           <dt>
                            <div class="drop-select left mr5" id="drop_select_category_id" event-node="blog_add_category_id">
                <a href="javascript:;" class="name" id="blog_select_name">通用<i class="ico-arrow-down right"></i></a>
                <ul class="select-list drop_clayer" id="blog_select_list" style="display:none">
                    <li  id='last_cate' ><a href="javascript:;" va="1">通用</a></li>                    <li><a href="javascript:;" va="0" >+添加分类</a></li>
                </ul>
                <input type="hidden" value="1" name="categoryId" event-node="categoryId" id="categoryId"/>
              </div>
              <input name="title" type="text" class="q-txt left" style="width:363px" value=""  placeholder="输入博客标题">
            </dt>
           <dd class="editor"><script type="text/javascript" src="https://static.qimingdao.com/apps/core/static/js/editor/kindeditor-4.1.1/plugins/code/prettify.js?v="></script>
<div class="editable-outer">
    <textarea  class="in_put" name="blog_content" id="blog_content" style="width:498px;_width:498px;height:250px" ></textarea>
    <div class="ke-action-wrap">
        <div class="ke-tips">注：支持Firefox浏览器下直接粘贴图片</div>
        <div class="ke-empty-record"><a href="javascript:;" onclick="Editor_blog_content.clean()" class="aicon-delete font16"></a></div>
        <div id="editor_save_info_blog_content" class="ke-draft-controls left"></div>
        <div class="qg-tips" style='display:none' >
            <div class="tips-up"><span><em>◆</em></span></div><div class="tips-btm">清空内容</div>
        </div>
    </div>

</div>
</dd>

           <input type='hidden' name="is_original" value='1'/>
           <dd class="actionBtn">
            <a href="javascript:void(0)" class="btn btn-green " event-args="ajax=true" event-node="submit_blog"><span>发表博客</span></a>
            <a event-node="submit_btn" event-args="ajax=true" style="display:none"></a>
          </dd>
          <input type="hidden" name="blog_id" value=""/>
          <input type="hidden" name="edit" value=""/>
          <input type="hidden" name="uid"  value="" />
   </dl>
</div>
</form>
                               </div>
                                <!--传文件-->
                                <div rel="kind-file" class="qg-poster-f block" style="display:none">
                                    <div class="pwb-hd"><em class="arrline">◆</em><span class="downline">◆</span></div>
                                    <!-- 发布微博/微博 -->
									<div class="mod-file-add pwb-bd" model-node="send_feed" model-args='post_event=post_feed'>
									    <dl>
									        <dt><div class="btn-upload">
									        <i class="aicon-plus"></i>
									            <FORM style='display:inline;padding:0;margin:0;border:0' >
									                    <input type="file" name="attach" inputname='attach' urlquery='attach_type=feed_file' event-node="add_file"  onchange="core.plugInit('uploadFile',this,'','all','','',$('#parentFile'));" id='attach'>
									                    </FORM>
									                </div>
									        <span>请选择您要上传的文件</span></dt>
									        <dd id='parentFile'></dd>
									        <dd class="action">
									    <div class="input">
									        <div class="input_before">
									            <div model-node="nums_left" class="number"><span>140</span></div>
									            <div model-node='initHtml'></div>
									            <textarea class="textinput" event-node='mini_editor_textarea'  rel="fangan" placeholder="记录工作的想法" event-args='has_out_user=0'></textarea>
									            <div model-node="post_ok" style="display:none;text-align:center;position:absolute;left:0;top:40%;width:100%">
									                <i class="ico-ok"></i>分享成功</div>
									        </div>
									        <div class="poster-addons">
									            <div class="kind">
									                <a class="btn btn-gray right" event-node='post_feed' event-args='type=post&app_name=core&topic_id=&add=&ext_id=&model_name=feed' href="javascript:;"><span>发布</span></a>
									                <div class="acts">
									                    <a event-node="insert_face" class="face-block" href="javascript:;"><i class="aicon-face"></i></a><a event-node="insert_at" class="face-block" href="javascript:;"><i class="aicon-at"></i></a><a event-node="insert_topic" class="face-block" href="javascript:;"><i class="aicon-topic"></i></a>                </div>
									            </div>
									        </div>
									    </div>
									    </dd>
									    </dl>
									</div> 
                                </div>
                        </div>
                    <!--feednav-->
                    <div class="feed-nav mt15">
                    <!--tab menu-->
                    <div class="tab-menu tab-animate">
                        <div class="feed-group">
                                                <i class="ico-circle-arrow-down" event-node='feed_tab_btn' title="展开"></i>
                                                </div>
                         <ul id="column_tab">
                         
                            <%--<li class="current" model-node ="myfollow"><span><a href="javascript:void(0);" >新闻公告<i class="ico-arrow-down"></i></a></span></li>
                                                        <li  ><span><a href="">动态</a></span></li>
                                                        <li  ><span><a href="">行业新闻</a></span></li>
                                                        <li><span><a href="">知道</a></span></li>                                                  
                          --%></ul>
                        <div class="tab-animate-block"></div>
                    </div>
                    </div>
                    <!--资讯列表-->
                    <div id="feed-lists"  style="padding-left:15px;padding-right:10px;">
                   		
					</div>
					</div>
<!--=> ft <=-->
<div id="footer">
    <div class="client">
        <ul>
            <li><a href=""><i class="aicon-android"></i>Android</a></li>
            <li><a href=""><i class="aicon-ios"></i>iPhone</a></li>
              </ul>
    </div>
    <div class="copyright">
        <p>Copyright(C) 2014 BPM</p>
    </div>
</div>
<!--=> ft End <=-->
</body>
</html>

<script type="text/javascript">
    $(function(){
        $('.boxy-modal-blackout').css({ 'height':$('body').height(),'width':$('body').width() });
        $('.guide-close').bind('click',function(){
            skip();
        });
        $('.guide-skip').bind('click',function(){
            skip();
        });
        $('.btn-orange').bind('click',function(){
            if($(this).attr('rel') > 0){
                $('.guide-step'+$(this).attr('rel')).hide();
                $('.guide-step'+(parseInt($(this).attr('rel'))+1)).show();
            }
        });
    });
    function skip(){
        $('.boxy-modal-blackout').remove();
        $('.guide-tips ').remove();
    }
            $('.mod-sync .sync-sina').each(function (i, n) {
            var offset = $(this).offset();
            $(this).bind({
                mouseenter: function (e) {
                    $('.float-tip').css({
                        "top":offset.top+20+'px',
                        "left":offset.left-5+'px'
                    }).show();
                },
                mouseleave: function () {
                    $('.float-tip').hide();
                }
            });
        });
            $('.mod-sync .sync-qq').each(function (i, n) {
            var offset = $(this).offset();
            $(this).bind({
                mouseenter: function (e) {
                    $('.float-tipt').css({
                        "top":offset.top+20+'px',
                        "left":offset.left-5+'px'
                    }).show();
                },
                mouseleave: function () {
                    $('.float-tipt').hide();
                }
            });
        });
</script>