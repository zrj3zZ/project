<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>  
    <title>文档预览</title>
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
<s:if test="type == 'pic'">
      $(function(){ 	
      		  $("#image").LoadImage(true,200,200,"iwork_img/wait.gif"); 
      		});
</s:if> 
//查看原图
function ViewOriginalPic(){
	var picUrl = $("#previewStr").val();
     var pageUrl = 'km_doc_preview_orgPic.action?picUrl='+encodeURI(picUrl);
     openwin("查看原图",pageUrl,550,450);
}  
//查看原文本   		
function showFullView(){  
   var picUrl = $("#previewStr").val(); 
   var pageUrl = 'km_doc_preview_orgPic.action?picUrl='+encodeURI(picUrl);   
   openwin("查看原图",pageUrl,550,450);
}     		
    </script>
  </head>
  
  <body>
  <div class="div_des">


  <s:if test="type == 'pic'">
        <div class="report_top">
  			<ul class="sub_tab">
		    	<li><a href="#" onclick="ViewOriginalPic();return false;">查看原图</a></li>
		   </ul>
		</div>

        <div style='padding-left:10px;padding-top:10px;'>
          <ul>
            <li style='width:200px;height:200px;float:left;margin-right:10px;padding:5px;background:white;position:relative;z-index:1;border:1px solid #cdcdcd'> 
                <a style='width:200px;height:200px;display:table-cell;vertical-align:middle;text-align:center;text-decoration:none;' href="#" onclick='return false;'><img style='vertical-align:middle;' id='image' src="<s:property value='previewStr'/>" border="0" /></a> 
            </li>
          </ul>
        </div> 
 </s:if>

 <s:elseif test="type == 'wenben'">
 	<!--<div class="report_top">
  			<ul class="sub_tab">
		    	<li><a href="#" onclick="showFullView();return false;">全屏显示</a></li>
		   </ul>
		</div>
       --><div style='padding-left:10px;padding-top:10px;'>
            <s:property value='previewStr' escapeHtml='false'/>
       </div>
 </s:elseif> 
 <s:elseif test="type == 'other'">
       <div style='padding-left:10px;padding-top:10px;'>
             <s:property value='previewStr' escapeHtml='false'/>
       </div>
 </s:elseif>
<s:else>
    <div style='padding-left:10px;padding-top:10px;'>
       文件不存在
    </div>   
</s:else>  
<s:hidden name="previewStr" id="previewStr"></s:hidden>

</div>   
  </body>
</html>