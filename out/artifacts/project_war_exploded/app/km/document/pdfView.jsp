<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>   
    <title>最近浏览</title>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
    <script type="text/javascript" src="iwork_js/flexpaper/flexpaper.js?v="></script>
	<script type="text/javascript" src="iwork_js/flexpaper/flexpaper_handlers.js?v="></script>
  </head>
  
  <body>
  <script type="text/javascript" src="https://static.qimingdao.com/apps/core/static/js/flexpaper-2.2.1/flexpaper.js?v="></script>
<script type="text/javascript" src="https://static.qimingdao.com/apps/core/static/js/flexpaper-2.2.1/flexpaper_handlers.js?v="></script>
<div id="viewerPlaceHolder" style="background-color:#fff;height:700px;width:"></div>
<script type="text/javascript">
    $('#viewerPlaceHolder').FlexPaperViewer({ 
        config : {
                SWFFile:"https://itd.qimingdao.com/widget/ShowDocument/getswf?attach_id=1",
                Scale : 0.6,
                ZoomTransition : 'easeOut',
                ZoomTime : 0.5,
                ZoomInterval : 0.2,
                FitPageOnLoad : true,
                FitWidthOnLoad : true,
                FullScreenAsMaxWindow : true,
                ProgressiveLoading : true,
                MinZoomSize : 0.2,
                MaxZoomSize : 5, 
                SearchMatchAll : true,
                InitViewMode : 'Portrait',
                RenderingOrder : 'flash',
                StartAtPage : '',
                key : "@c6c2643ad4111c8445a$831c4586993084725d9",
                ViewModeToolsVisible : true,
                ZoomToolsVisible : true,
                NavToolsVisible : true,
                CursorToolsVisible : true,
                SearchToolsVisible : true,
                WMode : 'window',
                localeChain: 'zh_CN'
        }
    });
</script>
                  </div>
 
  </body>
</html>
