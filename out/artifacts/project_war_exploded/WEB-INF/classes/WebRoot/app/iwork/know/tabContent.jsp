<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>   
    <title>主界面 tab所对应的内容</title>   
     
	 <script type="text/javascript">
	 $(function(){ 
	        var pgnum =  '<s:property value='pageNow'/>';
	        var pgsize =  '<s:property value='pageSize'/>';
	        var pgtotal = '<s:property value='total'/>';
		    var type = '<s:property value='type'/>';
	    	$('#page'+type).pagination({				
				pageNumber:pgnum,
				pageSize:pgsize,
				total:pgtotal,
				pageList:[10,20,30,50],
				showRefresh:false,
				beforePageText:'第',
				afterPageText:'页，共{pages}页',
				displayMsg:'第{from}到{to}条，共{total}条',
				onSelectPage:function(pageNumber, pageSize){
					$('#pageNow').val(pageNumber);
					$('#pageSize').val(pageSize); 
					tabReload();
				}
			}); 
		}); 	   
	 </script>
	
  </head>
  
  <body>
       <div class="tab_container" id="tab_container" style="min-width:550px;">
		  <div id="showBox1" style="padding-left: 3px; padding-right: 2px;min-width:550px;">   <!-- class="news" -->
		  
		  <s:if test="searchResult!=null && searchResult!=''">
		  	  <div class="oasearch_result"><s:property value='searchResult' escapeHtml='false'/></div>
		      <div class="oa_news"></div>
		  </s:if>
		      
			  <!-- start -->
			  <s:if test="questionList==null || questionList.size()==0">
			          <span style="padding-left:10px;color:#333333;">未找到相关内容！</span>
			  </s:if>
			  <s:else>
			  <table width="99%" border="0" cellspacing="0" cellpadding="0" style="table-layout:fixed;">
			  <col width="11%" />
			  <col width="66%" />
			  <col width="8%" />
			  <col width="12%" />
			  <s:if test="isAdmin=='true'">
			      <col width="2%"/>
			  </s:if>
			  <tr>
			  <td align="center" style="line-height:24px; padding: 0px;" ><font color="#333333" style="font-weight:bold;">分类</font></td>
			  <td ><font color="#333333" style="font-weight:bold;">问题</font></td>
			  <td align="center" ><font color="#333333" style="font-weight:bold;">回答数</font></td>
			  <td align="center" ><font color="#333333" style="font-weight:bold;">提问时间</font></td>
			   <s:if test="isAdmin=='true'">
			      <td align="center" >&nbsp;</td>
			  </s:if>
			  </tr>
			       <s:iterator value="questionList" status="status">
			            <tr onmouseover="changeTRbgcolor(this);" onmouseout="dorpTRbgcolor(this);">
							<td align="center" style="line-height:22px;color:#056ea4;">
   								[<a style="color:#056ea4;" href="#" onClick="searchbcidAndKeyword(<s:property value='qbigc'/>);"><s:property value='className'/></a>]
							</td>
							<td class="span_con">
   								<s:if test="score>0">
   								    <img src="/iwork_img/know/repo_rep.gif" /><font color="#C0120B"><s:property value='score'/></font>&nbsp;&nbsp;
   								</s:if>
   								<a href="#" onclick="openQuestionWin(<s:property value='id'/>);"><s:property value='qcontent'/></a>
							</td>
							<td align="center" ><s:property value='answerCount'/></td>
							<td align="center" ><s:property value='beginTime'/></td>
							<s:if test="isAdmin=='true'">
							    <td align=center >
							      <a href="#" onclick="delQuestion(<s:property value='id'/>);"><img src="/iwork_img/know/delete2.gif"/></a>
							    </td>			    
							</s:if>
						</tr>
			       </s:iterator>
			  </table>			   
			  </s:else>
			  <!-- end -->
		  </div>
		  
		      <s:if test="questionList!=null && questionList.size()!=0"> <!-- 以下为分页工具 -->
		      		<div id="page<s:property value='type'/>" style="background:#efefef;border:1px solid #ccc;width:96%;position:absolute;bottom:2px;"></div>	 
		      </s:if>		      		       
		 </div>			 
  </body> 
</html>
