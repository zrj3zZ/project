<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>查看原图</title>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
    <script type="text/javascript">
    /**
 * 图片预加载等比例缩放
 */
jQuery.fn.LoadImage=function(scaling,width,height,loadpic){
    if(loadpic==null)loadpic="iwork_img/wait.gif";
	return this.each(function(){
		var t=$(this);
		var src=$(this).attr("src")
		var img=new Image();
		img.src=src;
		//自动缩放图片
		var autoScaling=function(){
			if(scaling){
			
				if(img.width>0 && img.height>0){ 
			        if(img.width/img.height>=width/height){ 
			            if(img.width>width){ 
			                t.width(width); 
			                t.height((img.height*width)/img.width); 
			            }else{ 
			                t.width(img.width); 
			                t.height(img.height); 
			            } 
			        } 
			        else{ 
			            if(img.height>height){ 
			                t.height(height); 
			                t.width((img.width*height)/img.height); 
			            }else{ 
			                t.width(img.width); 
			                t.height(img.height); 
			            } 
			        } 
			    } 
			}	
		}
		if(img.complete){
			autoScaling();
		    return;
		}
		$(this).attr("src","");
		var loading=$("<img alt=\"加载中...\" title=\"图片加载中...\" src=\""+loadpic+"\" />");
		
		t.hide();
		t.after(loading);
		$(img).load(function(){
			autoScaling();
			loading.remove();
			t.attr("src",this.src);
			loading.remove();
			t.show();
		});
		
	});
 }
    $(function(){ 	
      		  $("#image").LoadImage(true,400,400,"iwork_img/wait.gif"); 
      		});
    </script>
  </head>
  
  <body style='text-align:center;'>
    <div style='width:420px;height:420px;margin:0 auto;'>
          <ul>
            <li style='width:400px;height:400px;float:left;margin-right:10px;padding:5px;background:white;position:relative;z-index:1;border:1px solid #cdcdcd'> 
                <a style='width:400px;height:400px;display:table-cell;vertical-align:middle;text-align:center;text-decoration:none;' href="#" onclick='return false;'><img style='vertical-align:middle;' id='image' src="<s:property value='picUrl'/>" border="0" /></a> 
            </li>
          </ul>
    </div> 
  </body>
</html>
