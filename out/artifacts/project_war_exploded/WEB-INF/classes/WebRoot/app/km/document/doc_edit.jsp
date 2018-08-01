<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>  
    <title>编辑</title>
    <link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
    <link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
    <link href="iwork_css/reset.css" rel="stylesheet" type="text/css" />
    <link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/zTreeStyle.css"> 
    <link rel="stylesheet" type="text/css" href="iwork_css/km/km_edit.css"> 
    <link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/> 
    <script type="text/javascript" src="iwork_js/commons.js"></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js" ></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.form.js"></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/My97DatePicker/WdatePicker.js"></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.ztree.core-3.4.min.js"></script>
    <script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
    <script type="text/javascript">
      //全局变量
     var api = art.dialog.open.api, W = api.opener;
      //==========================装载快捷键===============================//快捷键
		 jQuery(document).bind('keydown',function (evt){		
		    if(evt.ctrlKey&&evt.shiftKey){
			return false;
		   }
		   else if(evt.ctrlKey && event.keyCode==83){ //Ctrl+s /保存操作
			         save(); return false;
		     }
       }); //快捷键
    //保存
    function save(){
          var bool= validate();
          if(bool){
             var options = {
				error:errorFunc,
				success:successFunc 
			   };
			 $('#editForm').ajaxSubmit(options);
        	}
          art.dialog.data('status','save');
      	//删除文件
        var delUUID = $("#delUUID").val();
        if(delUUID!=''){
        	 uploadifyReomveCom('newHidId',delUUID,delUUID,'km_file_uploadifyRemove.action');
        }
      }
      errorFunc=function(){
           art.dialog.tips("保存失败！",2);
      }
      successFunc=function(responseText, statusText, xhr, $form){
           if(responseText=="ok"){
              art.dialog.tips("保存成功！",1);
              setTimeout('closeDialog();',500);
           }
      }
            //表单验证
      function validate(){
            var docName=$.trim($('#editForm_model_docName').val());//不能为空，不能超过200        
            var keyword=$.trim($('#editForm_model_keyword').val());//不能超过200
            var memo=$.trim($('#editForm_model_memo').val());//不能超过200
            var docAddr=$('#docAddr').val();
            
            var bool0 = $('#editForm_model_docType0').attr('checked');  
            var bool1 = $('#editForm_model_docType1').attr('checked');
            var bool2 = $('#editForm_model_docType2').attr('checked');        
          $('#editForm_model_docName').val(docName);
          $('#editForm_model_keyword').val(keyword);
          $('#editForm_model_memo').val(memo);
          
          if(docName==""){
              art.dialog.tips("文档名称不能为空！",2);
               $('#editForm_model_docName').focus();
              return false;
          }
          if(docAddr==""){
               art.dialog.tips("文档路径不能为空！",2);
               $('#docAddr').focus();
              return false;
          }
          if(bool0){
              var fileUUID =  $('#editForm_model_docEnum').val();
   			   if(fileUUID==""){
   			       art.dialog.tips("请上传文件！",2); 
   			       return false;
   			   }
   			   $('#editForm_model_docEnum').val(fileUUID);  
          }else if(bool1){
               var docLink = $.trim($('#docLink').val());
               if(docLink==""){
                   art.dialog.tips("知识链接不能为空！",2); 
                   $('#docLink').focus();
   			       return false; 
               }
               $('#editForm_model_docEnum').val(docLink);
          }else if(bool2){
               
          }
           if(length2(docName)>200){
                 art.dialog.tips('文档名称过长!',2);
                 $('#editForm_model_docName').focus();
                 return false;
          }
          if(length2(keyword)>200){
                 art.dialog.tips('关键词过长!',2);
                 $('#editForm_model_keyword').focus();
                 return false;
          }
          if(length2(memo)>200){
                 art.dialog.tips('文档描述过长!',2);
                 $('#editForm_model_memo').focus();
                 return false;
          }        
          return true;
      }
      //取消
      function cancel(){
         var bool0 = $('#editForm_model_docType0').attr('checked');  //是否需要文件上传
         if(bool0){        
                        var refresh = false;            
        				var id = $('#editForm_model_id').val();                    //alert('<'+id+'>');
        				var newUUID = $('#newUUID').val();                    		//alert('<'+newUUID+'>');
        				var defaultUUID = '<s:property value="model.docEnum"/>';   //alert('<'+defaultUUID+'>');
        				var fileUUID =  $.trim($('#newHidId').val());              //alert('<'+fileUUID+'>');
        				if(id==null || id==''){
        				     if(newUUID!=''){
        				    	//删除上传的文件
        				           uploadifyReomveCom('newHidId',newUUID,newUUID,'km_file_uploadifyRemove.action');
        				     }
        				}//新增点取消
        				else{
        				    //if(defaultUUID=='' && fileUUID!=''){
        				    if(newUUID!=''){
        				           uploadifyReomveCom('newHidId',newUUID,newUUID,'km_file_uploadifyRemove.action');
        				    }//删除已经上传的文件
        				    //if(defaultUUID!='' && fileUUID!=''){
        				           //if(defaultUUID!=fileUUID){
        				           		//uploadifyReomveCom('newHidId',fileUUID,fileUUID,'km_file_uploadifyRemove.action');
        				           		//$.post('km_file_upload_reset.action',{id:id},function(data){
               							//	if(data=='ok'){
                   						//		refresh = true;
               							//	}
        	  							//}); 
        				          // }//旧的被删除了，又上传新的，点击取消时，需要删除新上传的文件，并将doc中fileEnum和fileSize清空
        				    //}
        				    //if(defaultUUID!='' && fileUUID==''){
              						//$.post('km_file_upload_reset.action',{id:id},function(data){
               						//		if(data=='ok'){
                   					//			refresh = true;
               						//		}
        	  						//}); 
        				    //}//旧的被删除了，又没上传新的，则需要将doc中fileEnum和fileSize清空
        				}//编辑点取消       				
         }
       setTimeout('closeDialog();',500);
    }
    //关闭窗口
   function closeDialog(){
   		api.close(); 
   }
      //下拉树
      var setting = {
			view: {
				dblClickExpand: false,
				selectedMulti:false
			},
			async: {
						enable: true, 
						url:"show_km_tree_json.action",
						dataType:"json",
						autoParam:["directoryid"]
					}, 
			callback: {
				onClick: onClick,
				onAsyncSuccess: onAsyncSuccess
			}
		};
	  function onClick(e, treeId, treeNode) {
			var AddrObj = $("#docAddr");
			AddrObj.attr("value", getAddress(treeNode));
			$('#editForm_model_directoryid').val(treeNode.id);
			hideMenu();
		}
		
	function onAsyncSuccess(event, treeId, treeNode, msg){ 
	                    var zTree = $.fn.zTree.getZTreeObj("treeMenu");                  
	                   
	                    var parentid = '<s:property value="model.directoryid"/>';  
	                    var node=zTree.getNodeByParam('id',parentid,null);
	                    if(node!=null){
				    	     zTree.selectNode(node);
	                    }	               		
	             }	
	  function getAddress(treeNode){
	        var address=treeNode.name;
	        while (treeNode.getParentNode()!=null){
				      address = treeNode.getParentNode().name+">>"+address;
				      treeNode = treeNode.getParentNode();
				  }
		    return address;
	  }		
      function showMenu() {
			var AddrObj = $("#docAddr");
			var AddrOffset = $("#docAddr").offset();
			$("#menuContent").css({left:AddrOffset.left + "px", top:AddrOffset.top + AddrObj.outerHeight() + "px"}).slideDown("fast");
           
			$("body").bind("mousedown", onBodyDown);
		}
	  function onBodyDown(event) {
			if (!(event.target.id == "docAddr" || event.target.id == "menuContent" || $(event.target).parents("#menuContent").length>0)) {
				hideMenu();
			}
		}
     function hideMenu() {
			$("#menuContent").fadeOut("fast");
			$("body").unbind("mousedown", onBodyDown);
		}
	$(document).ready(function(){
			$.fn.zTree.init($("#treeMenu"), setting);
			
			/*全局*/
			selType();
			isDocAddrAble();
			/*全局*/
		});	

   //文件上传
   /* function showUploadifyPage(){
        var pageUrl = 'km_showUploadifyPage.action?sizeLimit=62914520&fileMaxNum=1&fileMaxSize=62914520&multi=true';
        art.dialog.data('oldUUIDs',$("#editForm_model_docEnum").val());
        art.dialog.open(pageUrl,{
			    	id:'uploadFile',
			    	title: '上传附件',
					lock:true,
					background: '#999', // 背景色
				    opacity: 0.87,	// 透明度
				    width:600,
				    height:410,
				    close:function(){
						var bHtml = art.dialog.data('bHtml');
						var bValue = art.dialog.data('bValue');
						var fileName = art.dialog.data('fileName');
						var newUUID = art.dialog.data('newUUID');
						if (fileName !== undefined && fileName !== ''){ 
							var dname = $("input[name='model.docName']").val();
							if(dname==''){
								$("input[name='model.docName']").val(fileName)
							}
						}
						if (bHtml !== undefined && bHtml !== '') document.getElementById('parentFileDivId').innerHTML = bHtml;
						if (bValue !== undefined && bValue !== ''){ 
							art.dialog.data('fileuuid',newUUID);
							document.getElementById('editForm_model_docEnum').value = bValue;
							$("#newUUID").val(bValue);
						}
				    }
				 });
	}  */
	function uploadifyDialog(dialogId,fieldName,divId,sizeLimit,multi,fileExt,fileDesc){
		if(dialogId==null||dialogId==""||fieldName==null||fieldName==""||divId==null||divId==""){
			alert('参数不正确');
			return ;
		}
		var pageUrl = 'showUploadifyPage.action?parentColId='+fieldName+'&parentDivId='+divId+'&sizeLimit='+sizeLimit+'&multi='+multi+'&fileExt='+fileExt+'&fileDesc='+fileDesc+'';
		art.dialog.open(pageUrl,{
			id:dialogId,
			title: '上传附件',
			pading: 0,
			lock: true,
			width: 650,
			height: 500, 
			close:function(){
				this.focus();
			}
		}); 
		return ;
	}
  //验证是否已经上传过文件
   function isUploadFile(){
        var fileUUID =  $.trim($('#newHidId').val());
        if(fileUUID == null || fileUUID==''){
             return true;
        } 
   		return false;
   }
   //文档类型选择
   function selType(){  
        $('#editForm_model_docType2').attr('disabled',true); //流程表单禁用 
        var bool0 = $('#editForm_model_docType0').attr('checked');  
        var bool1 = $('#editForm_model_docType1').attr('checked');
        var bool2 = $('#editForm_model_docType2').attr('checked');
        if(bool0){
            $('#upload').css('display','');
            $('#link').css('display','none');
        }else if(bool1){
            $('#upload').css('display','none');
            $('#link').css('display','');
        }else if(bool2){
            
        }
   }
   //新建时路径不可变，编辑时路径可变
  function isDocAddrAble(){
      var id = $('#editForm_model_id').val();
      if(id==null || id==''){
      	  	$('#docAddr').css('background-color','#efefef');
      	  	$("#docAddr").removeAttr("onclick");    
      	  	$('#altAddr').css('display','none');
      }
  }  
	function uploadifyReomveCom(str1,fileUUID,fileUUID1,url){
		
		$.post(url,{fileUUID:fileUUID},function(data){
		})
	}
	//删除文件，只有在页面保存时会更新数据并删除文件	  		
	function delFile(uuid){
		art.dialog.confirm('你确认删除文件？\r\n(文件在页面保存时执行删除操作)', function(){
			document.getElementById('parentFileDivId').innerHTML = '';
			document.getElementById('docEnum').value = '';
			$("input[name='model.filesize']").val('');
			$("#delUUID").val(uuid);
		}, function(){
		});
	}		
      </script>
 
  </head>
  
    <body class="easyui-layout">
      <div region="center" border="false" style="padding:3px;margin-bottom:5px;overflow-y:auto;">
           <s:form name="editForm" id="editForm" action="km_doc_save.action" theme="simple">
               <table width="100%" border="0" cellspacing="0" cellpadding="0">
                   <tr>
                      <td class="td_title">文档名称:</td><td class="td_data"><s:textfield cssStyle="width:200px;" name="model.docName"/>&nbsp;<span style='color:red'>*</span></td>
                   </tr>
                   <tr>
                      <td class="td_title">文档路径:</td><td class="td_data">
                           <s:textfield name="model.address" id="docAddr" readonly="true" onclick="showMenu();" cssStyle="width:200px;" /><a id="altAddr" class="easyui-linkbutton" href="javascript:showMenu();" plain="true" iconCls="icon-add">更改路径</a>&nbsp;<span style='color:red'>*</span>
                           <div id="menuContent" class="menuContent" style="display:none; position: absolute;">
								<ul id="treeMenu" class="ztree" style="margin-top:0; width:180px; height: 300px;"></ul>
						   </div>
                      </td>
                   </tr>
                   <tr>
                      <td class="td_title">关键词:</td><td class="td_data"><s:textfield cssStyle="width:200px;" name="model.keyword"/></td>
                   </tr>
                    <tr>
                      <td class="td_title">文档类型:</td><td class="td_data">
  							<s:radio  name="model.docType"  listKey="key" listValue="value"  list="#{'0':'知识文档','1':'知识链接','2':'流程表单'}" onclick="selType();"/>
 					  </td>
                   </tr>
<tr id="upload"><td class="td_title" align=right >文件上传:</td>
	<td align=left valign=bottom class="td_data">
		<div id='parentFileDivId'>
			<div>
				<s:hidden id='newHidId' name='newHidId' value='%{model.docEnum}'/>
			<div>
				<input type=button value='上传附件' class='Btn_s_a' onclick='showUploadifyPageXGZL()' border='0'>
				&nbsp;(只能上传一个不超过60M的附件)
				<span style='color:red'>*</span>
			</div>
			<script>
				function showUploadifyPageXGZL(){
					/* mainFormAlertFlag=false;
					saveSubReportFlag=false;
					var valid = mainFormValidator.form();
					if(!valid){
						return false;
					}
					mainFormAlertFlag=false;
					saveSubReportFlag=false; */
					uploadifyDialog('newHidId','newHidId','parentFileDivId','','true','','');
				}
			</script>
			<s:iterator value='files'>
				<div id="<s:property value='fileId'/>" style="background-color: #F5F5F5;border-bottom: 1px solid #E5E5E5;font: 11px Verdana, Geneva, sans-serif;padding: 5px;width: 200px">
					<div style="align:right;float: right;">
						<a href="javascript:uploadifyReomveCom('newHidId','<s:property value='fileId'/>','<s:property value='fileId'/>','km_file_uploadifyRemove.action');">
							<img src="/iwork_img/del3.gif" border="0"/>
							</a>
					</div>
					<span>
						<a href="km_file_uploadifyDownload.action?fileUUID=<s:property value='fileId'/>" target="_blank">
							<img src="/iwork_img/attach.png" border="0"/><s:property value='fileSrcName'/>
						</a>
					</span>
				</div>
			</s:iterator>	   
		</div>
	</td>
</tr>
                   <tr id="link">
                      <td class="td_title">知识链接:</td><td class="td_data"><s:textfield cssStyle="width:300px;" value='%{model.docEnum}' name="docLink" id="docLink"/>&nbsp;<span style='color:red'>*</span></td>
                   </tr>
                   <tr>
                      <td class="td_title">有效期:</td><td class="td_data">
                      		<s:textfield readonly="true" cssStyle="width:150px;" name="model.startDate" onclick="WdatePicker({minDate:'%y-%M-%d',maxDate:'#F{$dp.$D(\\'editForm_model_endDate\\')||\\'9999-12-31\\'}'})"/>到<s:textfield readonly="true" cssStyle="width:150px;" name="model.endDate" onclick="WdatePicker({minDate:'#F{$dp.$D(\\'editForm_model_startDate\\')||\\'%y-%M-%d\\'}',maxDate:'9999-12-31'})"/>
                      </td>
                   </tr>
                   <tr>
                      <td class="td_title">文档描述:</td><td class="td_data"><s:textarea name="model.memo" cssStyle="width:300px;height:100px"/></td>
                   </tr>
               </table>
                <s:hidden name="model.docEnum"/>
               
               <s:hidden name="model.id"/>
               <s:hidden name="model.directoryid"/>
               <s:hidden name="model.priority"/>
               <s:hidden name="model.filesize"/>
               <s:hidden name="model.createUser"/>
               <s:hidden name="model.createDate"/>
               <s:hidden name="model.version"/>
               <s:hidden name="model.status"/>
               <s:hidden name="model.orderIndex"/>
           </s:form>
      </div>
      
      <div region="south" border="false" style="text-align: right; height: 50px; line-height: 30px;padding-top:5px;padding-right:15px;">
           <a class="easyui-linkbutton" href="javascript:save();" iconCls="icon-save">保存</a>
           <a class="easyui-linkbutton" href="javascript:cancel();" iconCls="icon-cancel">取消</a>
      </div>
      <input type="hidden" id="newUUID" value="" />
      <input type="hidden" id="delUUID" value="" />
  </body>
</html>
