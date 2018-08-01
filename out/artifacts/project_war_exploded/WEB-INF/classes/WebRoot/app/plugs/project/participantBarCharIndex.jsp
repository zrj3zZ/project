<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Frameset//EN" "http://www.w3.org/TR/html4/frameset.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
  <head>  
    <title>项目管理</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
    <link rel="stylesheet" type="text/css" href="iwork_css/common.css">
	<link rel="stylesheet" type="text/css" href="iwork_themes/easyui/gray/easyui.css">
	<link href="iwork_css/public.css" rel="stylesheet" type="text/css" />
	<link href="iwork_css/analytics/home_page.css" rel="stylesheet" type="text/css" /> 
	<link href="iwork_css/analytics/menu.css" rel="stylesheet" type="text/css" />
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/zTreeStyle.css">
	<link href="iwork_css/analytics/process.css" rel="stylesheet" type="text/css" />
	<link href="iwork_css/jquerycss/jquery.jqplot.min.css" rel="stylesheet" type="text/css" /> 
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/excanvas.js"></script> 
	<script type="text/javascript" src="iwork_js/jqueryjs/jqPlot/jquery.jqplot.min.js"></script> 
	<script type="text/javascript" src="iwork_js/jqueryjs/jqPlot/plugins/jqplot.barRenderer.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jqPlot/plugins/jqplot.dateAxisRenderer.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jqPlot/plugins/jqplot.canvasTextRenderer.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jqPlot/plugins/jqplot.canvasAxisTickRenderer.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jqPlot/plugins/jqplot.categoryAxisRenderer.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jqPlot/plugins/jqplot.barRenderer.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jqPlot/plugins/jqplot.pieRenderer.min.js"></script>
	<script language="javascript" type="text/javascript" src="iwork_js/jqueryjs/jqPlot/plugins/jqplot.pointLabels.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js"></script> 
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.ztree.core-3.4.min.js"></script>	
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.ztree.excheck-3.4.min.js"></script>  	
	<script type="text/javascript" src="iwork_js/lhgdialog/lhgdialog.min.js?skin=default"></script>
	<script type="text/javascript" src="iwork_js/analytics_index.js"></script>
	<script type="text/javascript">
		$(document).ready(function(){
		 
    // For horizontal bar charts, x an y values must will be "flipped"
    // from their vertical bar counterpart.
    var data = <s:property value="typeBarData" escapeHtml="false"/>;
    var label = <s:property value="typeBarLabel" escapeHtml="false"/>;
      $("#c2").height(label.length*60);
    /* var plot2 = jQuery.jqplot ('c2', [data], 
					    { 
					    title: {
					        text: '按项目参与人统计',   // 设置当前图的标题
					        show: true,//设置当前标题是否显示
					        fontSize:30,
					         textAlign:'center'
    					},
					   seriesDefaults: {
            renderer:$.jqplot.BarRenderer,
            // Show point labels to the right ('e'ast) of each bar.
            // edgeTolerance of -15 allows labels flow outside the grid
            // up to 15 pixels.  If they flow out more than that, they 
            // will be hidden.
            pointLabels: { show: true, location: 'e', edgeTolerance: -15},
            // Rotate the bar shadow as if bar is lit from top right.
            shadowAngle: 135,
            // Here's where we tell the chart it is oriented horizontally.
            rendererOptions: {
                barDirection: 'horizontal'
            }
        }, 
          legend: {
            show: true,
            location: 'ne',  
            placement: 'outsideGrid',
            showLabels:true,
            labels:['负责或参与的项目数量'],
              marginRight: '40px'
        },
        axes: {
        xaxis : {  
                   min : 0,  
                    tickInterval : '50',  
                },
            yaxis: {
              ticks:label,
                renderer: $.jqplot.CategoryAxisRenderer
            },pad: 1.05,
                tickOptions: {formatString: '$%d'}
        } 
					  }); */
					  var plot2=$.jqplot('c2', [data], {
	    	               seriesDefaults: {
	    	   	            renderer:$.jqplot.BarRenderer,
	    	   	            pointLabels: { show: true, location: 'e', edgeTolerance: -15},
	    	   	            shadowAngle: 135,
	    	   	            rendererOptions: {
	    	   	                barDirection: 'horizontal'
	    	   	            }
	    	   	        },
	    	             axes: {
	    	     	        xaxis : {  
	    	     	                   min : 0,  
	    	     	                    tickInterval : '50',  
	    	     	                },
	    	     	            yaxis: {
	    	     	              ticks:label,
	    	     	                renderer: $.jqplot.CategoryAxisRenderer
	    	     	            },pad: 1.05,
	    	     	                tickOptions: {fontFamily: 'Georgia'}
	    	     	        }
	    	          });
      $("#c2").bind('jqplotDataClick', function (ev, seriesIndex, pointIndex, data) {  
    		//var index = data.substring(data.indexOf(","));
    		showChartInfo(label[pointIndex]);      
	});
});
function showChartInfo(username){
			 var title = "";
			 var height=580;
			 var width = 900;
			 var pageurl = encodeURI("zqb_project_participantBarChartInfo.action?username="+username);
			 var dialogId = "projectItem"; 
			 parent.openWin(title,height,width,pageurl,this.location,dialogId);
}
		
	</script>
  		<style type="text/css"> 
  			#c2 .jqplot-point-label {
			  border: 1.5px solid #aaaaaa;
			  padding: 1px 3px;
			  background-color: #eeccdd;
			}
			.title{
				font-size:30px;
				text-align:center;
			}
  		
  		</style>
	</style>	
  </head>
    <body class="easyui-layout">
      <div region="north" border="false" > 
      <div class="tools_nav">
      	<span style="float:right;padding-right:10px">图表|<a href="zqb_project_participantBarIndex.action">表格</a></span>
      	</div>
      </div>
      <div region="center"  border="false" >
      	<table width="100%">
      		<tr>
      			<td style="font-size:30px;text-align: center;">按项目参与人统计</td>
      		</tr>
      		<tr>
				<td style="padding:10px;"><div id="c2" ></div></td>
      		</tr>
      		
      	</table>
		</div>
  </body>
</html>
<!-- 新增查询过滤SQL注入关键字 -->
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