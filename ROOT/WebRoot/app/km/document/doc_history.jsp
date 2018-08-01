<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>  
    <title>文档历史版本</title>
    <script type="text/javascript">
    	// 删除历史版本
    	function delfileversion(){
    		var chk_value = new Array(); 
			$('input[name="checkbox2"]:checked').each(function(){
				chk_value.push($(this).val());
			});
			if(chk_value.length==0){
				alert('请选择要删除的文件!');
				return;
			}
			if(confirm('确定删除该文件？')){
				
		    	$.post('km_file_delversion.action',{ids:chk_value.join(",")},function(data){
			    	if(data=='ok'){
			    		alert("删除成功");
			    		var currTab = $('#property').tabs('getSelected'); //获得当前tab
   				 		var url = $(currTab.panel('options').content).attr('src');
    					$('#property').tabs('update', {
     			 			tab : currTab,
      			 			options : {
       							content :''
       						}
     			 		});
			    	}
				});
		    }		
    	}
    	
    	// 复选框全选、全清
    	function selectAll(){  
    		if ($('input[name="checkbox"]').attr("checked")) {
    			$('input[name="checkbox2').attr("checked", true);
    		} else {
    			$('input[name="checkbox2').attr("checked", false);
    		}
		}
    </script>
  </head>
  <body>
    
<div class="div_des">

<div class="tools_nav">
  <ul class="sub_tab">
    <li><a href="#" onclick="updateversion();return;"><img border="0" style="padding:2px;"  src="iwork_img/but_add.gif">发布新版本</a></li>
    <li><a href="#" onclick="delfileversion();return;"><img border="0" style="padding:2px;"  src="iwork_img/del3.gif">删除版本</a></li>
  </ul>
</div>

  <div>
    <div class="div_list">
      <table border="0" cellpadding="0" cellspacing="0">       
        <tr>
          <th><input type="checkbox" name="checkbox" onclick="selectAll();"/>
          <label for="checkbox"></label></th>
          <th>文件名称</th>
          <th>发布时间</th>
          <th>更新时间</th>
          <th>发布人</th>
          <th>版本</th>
        </tr>
        
        <s:iterator value='docHisList'>
        <tr >
          <td><input type="checkbox" name="checkbox2" value="<s:property value='id'/>"/>
          <td><s:property value='docName'/></td>
          <td><s:property value='createDate'/></td>
          <td><s:property value='operationDate'/></td>
          <td><s:property value='createUser'/></td>
          <td>v<s:property value='version'/>.0</td>
        </tr>
        </s:iterator>
      </table>
    </div>
  </div>
  
</div>
      
  </body>
</html>
