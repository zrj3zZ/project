<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>

<html>
<head><title>知识管理</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	<link rel="stylesheet" type="text/css" href="iwork_themes/easyui/gray/easyui.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/km-icon.css"> 
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/linkbutton.css">	
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/jqgrid/ui.jqgrid.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/jqgrid/jquery-ui-1.8.2.custom.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/zTreeStyle.css"> 
	<link rel="stylesheet" type="text/css" href="iwork_css/km/km_document.css">
	<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/> 
	<script type="text/javascript" src="iwork_js/commons.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.form.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.ztree.core-3.4.min.js"></script>		
    <script type="text/javascript" src="iwork_js/jqueryjs/languages/grid.locale-cn.js"  ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.jqGrid.min.js"  > </script>
	<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
	<script>
	    var expandRootFlag = false;  //展开‘根目录’标志位
	    var selectFlag = false;      //选中树节点标志位
	    var selectId = 0;            //选中树id
	    var openDirectoryFlag = false;  //双击grid中文件夹图标，打开文件夹标志位
	    var openDirectoryId = 0;         
	    var gridReloadFlag = false;      //双击‘预览’中文件夹图标，grid加载标志位,true表示加载完
	    var gridSelectId = 0;            //选中grid某行id
	    var pageFirstLoadFlag = true;    //页面是否第一次加载，第一次加载时，onTabSelect函数不执行	     
	    var artWin;
	    var setting = {
					async: {
						enable: true, 
						url:"show_dmtree_json.action",
						dataType:"json",
						autoParam:["directoryid"]
					}, 
					view: {
						dblClickExpand: false,
						selectedMulti: false
					},	
					   data: {
				            simpleData: {
				                enable: true,
				                pIdKey: "parentid",
				                idKey: "id",
				                rootPId: 0  
				            }
				        },
					callback: {						
						onClick:onClick,
						onAsyncSuccess: zTreeOnAsyncSuccess
					} 
				};
		//异步加载回调函数
		function zTreeOnAsyncSuccess(event, treeId, treeNode, msg) {
            $("#fhsjid").val(<s:property value='dirid'/>);
            $("#fhsjzt").val("1");
				 if(expandRootFlag){
				           var treeObj = $.fn.zTree.getZTreeObj("kmtree");
	    				   var node=treeObj.getNodeByParam('id',<s:property value='dirid'/>);        	   		
	   					   treeObj.expandNode(node,true);
	   					   treeObj.selectNode(node);
	   					   expandRootFlag = false;
				    }
				  if(selectFlag){
				       selectNode(selectId);
				       selectFlag = false;
				       selectId = 0;
				  }
				  if(openDirectoryFlag){
				       afterClickIcon(openDirectoryId);
				       openDirectoryFlag = false;
				       openDirectoryId = 0;
				  }				      					 	
		};				
		//初始化树		
	    function initTree(){
	    	expandRootFlag = true;
	    	$.fn.zTree.init($("#kmtree"), setting);	    		    		
	    }			
				
		$(function(){
			var p = $('body').layout('panel','west').panel({				
			});
			//手风琴
			$('#aa').accordion({
			      animate: false,
			      onSelect: onAccordionSelect
			});
			//选项卡
			$('#property').tabs({
			      onSelect: onTabSelect,
			      onLoad: onTabLoad
			});
			//表格
			jQuery("#kmlist_grid").jqGrid({
			   	url:'show_km_grid_json.action',
				datatype: "json",
				mtype: "POST",
				postData: {directoryid:<s:property value='dirid'/>,type:'folder'},  //附在url后面传给服务器的参数
				rowNum:200,
				autowidth: true,
				shrinkToFit: true,
			   	colNames:['序号','', '名称', '大小','类型','状态','更新人','更新时间','版本','文件下载','ID','类型号','父ID','状态号','参数','权限'],
			   	colModel:[
			   		{name:'INDEX',index:'1', width:50, sortable:true, align:"center"},
			   		{name:'ICON',index:'2', width:20, sortable:false, align:"left"},
			   		{name:'NAME',index:'3', width:660, sortable:true, align:"left"},
			   		{name:'SIZE',index:'4', width:80, sortable:true, align:"left"},
			   		{name:'TYPENAME',index:'5',hidden:true, width:40, sortable:true, align:"left"},
			   		{name:'STATUSICON',index:'5', width:80, sortable:true, align:"left",hidden:true},
			   		{name:'UPDATEUSER',index:'6', width:100, sortable:true, align:"left",hidden:true},
			   		{name:'UPDATEDATE',index:'7', width:100, sortable:true, align:"left"},
			   		{name:'VERSION',index:'8', width:90, sortable:true, align:"left",hidden:true},
			   		{name:'WJXZ',index:'9', width:90, sortable:true, align:"left",hidden:true},
			   		
			   		{name:'ID',index:'10',  hidden:true},
			   		{name:'TYPE',index:'11',  hidden:true},
			   		{name:'PARENTID',index:'12',  hidden:true},	
			   		{name:'STATUS',index:'13',  hidden:true},
			   		{name:'DOCENUM',index:'14',  hidden:true},
			   		{name:'PURVIEW',index:'14',  hidden:true}
			   	],
			   	height: 'auto',
			   	multiselect: false,
			   	shrinkToFit: false,
			   	jsonReader: {
			     	root: "dataRows",
			     	//page: "curPage",
			     	//total: "totalPages",
			     	//records: "totalRecords",  //与分页有关参数，不需要服务器发送过来
			     	repeatitems: false,
			     	userdata: "userdata"  //可以让服务器发送过来的不需要显示在jqgrid但可以提取在别的地方用的数据
			    },
			   	sortname: 'ID',
			   	prmNames: {page:null, rows:null, sort:null, order:null, search:null, nd:null, npage:null},//与分页有关参数，可以设为空，将不发送到服务器。
			    viewrecords: true,
			   // order:'desc',
			    sortorder: "desc",
			    onCellSelect:onCellSelect,
			    loadComplete:loadComplete
			});
			
			//初始化树			
			initTree();

		});
		//accordion选中事件
		function onAccordionSelect(title){
		     //alert(title);
		     if(title == '最近浏览'){
		         var panel = $('#aa').accordion('getPanel',title);
		         if(panel){
		              panel.panel('refresh','km_readLog_showRecentLog.action');
		         }
		     }
		     else if(title == '收藏夹'){
		     	 var panel = $('#aa').accordion('getPanel',title);
		         if(panel){
		              panel.panel('refresh','km_fav_showFav.action');
		         }
		     }
		}
		//Tab的点击事件
		function onTabSelect(title){// alert('2');
			   if(pageFirstLoadFlag){
		           pageFirstLoadFlag = false;
		           return ;
		    	} //首次加载index.jsp这个页面时，do nothing.	 
		       var rowid = jQuery("#kmlist_grid").jqGrid('getGridParam','selrow');  
		       if(rowid==null){
		        	alert("请选择一个文件");
		    		return;
		    	}
		      clickOtherCol(rowid); 
		}
		//Tab的加载完成事件
		function onTabLoad(panel){ 
		    //var title = panel.panel('options').title; 
		}		
		//树的点击事件
		function onClick(event, treeId, treeNode, clickFlag){
				$("#fhsjid").val(treeNode.id);
           		$("#fhsjzt").val("1");
				if(treeNode.isParent){	
				      //jQuery("#kmlist_grid").jqGrid('setGridParam',{postData:{directoryid:treeNode.id}}).trigger("reloadGrid");
				      var zTree = $.fn.zTree.getZTreeObj("kmtree");
				      if(!treeNode.open){
				      	 zTree.expandNode(treeNode, true, null, null, true);
				      }else{ 
				    //  	  zTree.expandNode(treeNode, false, null, null, true);
				      }
				    
				  refreshGrid2(treeNode.id,'folder');
				      
				}else{
					 refreshGrid2(treeNode.id,'folder');
				}
		}
		//判断Aid是否是Bid的父节点
        function AisBParent(Aid,Bid){
            var bool = false;
            var treeObj = $.fn.zTree.getZTreeObj("kmtree");
	   	    var Bnode=treeObj.getNodeByParam('id',Bid);
	   	    while(Bnode.parentid!=null){
	   	         var BPnode = treeObj.getNodeByParam('id',Bnode.parentid);
	   	         if(BPnode.id == Aid){
	   	             bool = true;
	   	             break;
	   	         }
	   	         Bnode = BPnode;
	   	    }
            return bool;
        }
		//jqGrid加载完成事件
		function loadComplete(){
		       if(gridReloadFlag){		      
				       selectGridRow(gridSelectId);
				       gridReloadFlag = false;
				       gridSelectId = 0;
			   }
			   else{   
			           selectGridRow(1); //选中第一行			           
			   }	   
		}
		//选中jqGrid中rowid所对应的行
        function selectGridRow(rowid){                
             var rowData = $("#kmlist_grid").jqGrid("getRowData", rowid); 
             if(typeof(rowData.ID) == 'undefined'){
                 return ;
             }
       		 $("#kmlist_grid").jqGrid("setSelection", rowid,false); 
    		 clickOtherCol(rowid); 
        }
		//表格单元格事件
		function onCellSelect(rowid,iCol,cellcontent,e){
			 var rowData = jQuery("#kmlist_grid").jqGrid('getRowData',rowid);
		      if(iCol==1){
		          clickIcon(rowid);
		      }else if(iCol==5){
			      if(rowData.PURVIEW!=1){
					return;
				 }
		      	 //文档锁定和解锁
		      	 if(rowData.TYPE=='DIR'){
			        alert("无法锁定文件夹");
			    	return;
			     } 
			     var id = rowData.ID;		      	
			     if(rowData.STATUS==0){
			       $.post('km_file_unlock.action',{id:id},function(data){
					      if(data=='ok'){
					            refreshGrid();//alert("解锁成功");
					      }
					});
			    	return;
			    }else{
			    	$.post('km_file_lock.action',{id:id},function(data){
					      if(data=='ok'){
					            refreshGrid(); // alert("锁定成功");
					      }
					});
			    }
		      }else{
		          clickOtherCol(rowid);
		          if(rowData.TYPE=='DIR')
                      refreshGrid2(rowData.ID,'folder');
		      }
		}
		//锁定
	function lockfile(){
	  var rowid = jQuery("#kmlist_grid").jqGrid('getGridParam','selrow');	  
	       if(rowid==null){
		        art.dialog.tips("请选择需要解锁的文件");
		    	return;
		    }
	       
		    var rowData = jQuery("#kmlist_grid").jqGrid('getRowData',rowid);
		    if(rowData.PURVIEW!=1){
				art.dialog.tips("无权限执行此操作");
				return;
			}
		    if(rowData.TYPE=='DIR'){
		        art.dialog.tips("文件夹不可锁定");
		    	return;
		    }
		    if(rowData.STATUS==0){
		        art.dialog.tips("该文件已处于锁定状态");
		    	return;
		    } 
		    id = rowData.ID;
			$.post('km_file_lock.action',{id:id},function(data){
			      if(data=='ok'){
			            art.dialog.tips("锁定成功");
			            refreshGrid();
			      }
			});
	}
	//解锁
	function unlockfile(){
	        var rowid = jQuery("#kmlist_grid").jqGrid('getGridParam','selrow');
		    if(rowid==null){
		        art.dialog.tips("请选择需要解锁的文件");
		    	return;
		    }
		    var rowData = jQuery("#kmlist_grid").jqGrid('getRowData',rowid);
		     if(rowData.PURVIEW!=1){
				art.dialog.tips("无权限执行此操作");
				return;
			}
		    if(rowData.TYPE=='DIR'){
		        art.dialog.tips("文件夹不可解锁");
		    	return;
		    }
		    if(rowData.STATUS==1){
		        art.dialog.tips("该文件已处于解锁状态");
		    	return;
		    }
		    var id = rowData.ID;
			$.post('km_file_unlock.action',{id:id},function(data){
			      if(data=='ok'){
			            art.dialog.tips("解锁成功");
			            refreshGrid();
			      }
			});
	}
	   //点击图标触发
	   function clickIcon(rowid){
	   			var rowData = jQuery("#kmlist_grid").jqGrid('getRowData',rowid);
	   			if(rowData.TYPE=='DIR'){
	   			   var treeObj = $.fn.zTree.getZTreeObj("kmtree");
	   			   var node=treeObj.getNodeByParam('id',rowData.ID);
	   			   var flag = true;	   			   
	   			   if(node==null){
	   			           flag = false;
	   			           var parentNode = treeObj.getNodeByParam('id',rowData.PARENTID,null);
	   			           openDirectoryFlag = true;
	   			           openDirectoryId = rowData.ID;
	   			           treeObj.expandNode(parentNode,true);  //如果还没有加载，会加载之。
	   			          // setTimeout('afterWait('+rowData.ID+');',500);	//延时500ms之后再  选中树对应的节点 展开Grid  			           	   			            
	   			    }//说明父节点还没加载，加载之。
	   			    if(flag){
	   			       treeObj.selectNode(node);   //选中树对应的节点
	   			       //jQuery("#kmlist_grid").jqGrid('setGridParam',{postData:{directoryid:rowData.ID}}).trigger("reloadGrid");//展开Grid
	   			       refreshGrid2(rowData.ID,'folder');
	   			    }	   			    	   			    	   			   	   			  
	   			}
	   } 
	 //等待树加载完毕
	 function afterClickIcon(rowId){
	 		var treeObj = $.fn.zTree.getZTreeObj("kmtree");
	   		var node=treeObj.getNodeByParam('id',rowId);
	   		treeObj.selectNode(node);  //选中树对应的节点
	   		//jQuery("#kmlist_grid").jqGrid('setGridParam',{postData:{directoryid:rowId}}).trigger("reloadGrid");//展开Grid
	   		refreshGrid2(rowId,'folder');
	 }  
	 
	 //点击其他列触发
	 function clickOtherCol(rowid){ 
	      var rowData = jQuery("#kmlist_grid").jqGrid('getRowData',rowid); 
	      var type = rowData.TYPE;	      
	      var tabPanel = $('#property').tabs('getSelected');  	 
	      if(tabPanel==null){
	          setTimeout("clickOtherCol("+rowid+")",500); 
	          return;
	      }//如果tabPanel为空，说明选项卡插件还没加载完成，那么等0.5s后再执行本函数，如此循环，一定可以等到选项卡加载完毕。    
	      var title =  tabPanel.panel('options').title;       
	      if(type=='DIR'){ 
	      	 /*  hideTab('历史版本');
	           hideTab('猜你想要');
	           if(title == '历史版本' || title == '猜你想要'){
	              $('#property').tabs('select','基本信息');
	           }*/
	      	  if(rowData.PURVIEW=="1"){
	      	  	showTab('权限信息'); 
	      	  }else{ 
	      	  	 hideTab('权限信息'); 
	      	  }
	      }
	      else{
	      	  if(rowData.PURVIEW=="1"){
	      	  	 showTab('权限信息'); 
	      	//  	 showTab('历史版本');
	      	  }else{
	      	  	 hideTab('权限信息'); 
	      	  /*	 hideTab('历史版本');
	      	  	 if(title == '历史版本' || title == '权限信息'){
	              $('#property').tabs('select','基本信息');
	           }*/
	      	  }
	         //  hideTab('猜你想要');
	      }	     
	      tabPanel = $('#property').tabs('getSelected');
	      title =  tabPanel.panel('options').title;
          var id = rowData.ID;
          
          updateTabContent(type,title,id);	            
	 }
	 
	 //更新tab内容
	 function updateTabContent(type,title,id){
	      var tabPanel = $('#property').tabs('getTab',title);
	      if(type=='DIR'){	               

              $('#property').tabs('update', {
                  tab: tabPanel,
                  options:{
                      href:'km_doc_purviewlist.action?id='+id+'&purviewGroup=folder'
                  }
              });
	      }else{	      		 	            

              $('#property').tabs('update', {
                  tab: tabPanel,
                  options:{
                      href:'km_doc_purviewlist.action?id='+id+'&purviewGroup=doc'
                  }
              });
	      }
         setTimeout("testFunction()","500");

	 }
	 function testFunction(){
         var a=$("#sqsx").html();
         if(a!=null && a!=""){
             document.getElementById('sxx').innerHTML=a;
         }
	 }
	    //隐藏tab
	    function hideTab(title){
	         var tab_option = $('#property').tabs('getTab',title).panel('options').tab;  
             tab_option.hide();
	    }
	    //显示tab
	    function showTab(title){
	        var tab_option = $('#property').tabs('getTab',title).panel('options').tab;  
            tab_option.show();
	    } 
	    //打开窗口
	    function openwin(title,pageUrl,width,height){
	    	 artWin = art.dialog.open(pageUrl,{
			    	id:'dg_win',
			    	title:title,
					lock:true,
					background: '#999', // 背景色
				    opacity: 0.87,	// 透明度
				    width:width,
				    height:height,
				    close:function(){
				    	refreshGrid();
				    }
				 });
	    } 
		//添加文件夹
		function addFolder(){
			var zTree = $.fn.zTree.getZTreeObj("kmtree");
			var nodes = zTree.getSelectedNodes();
			var parentid = 0;
			if(nodes.length>0){
				parentid = nodes[0].id;
				if(nodes[0].PURVIEW!=1){
					art.dialog.tips("您无权限在该目录下添加子目录");
					return;
				}
			}
			var pageUrl = "km_dir_add.action?parentid="+parentid;
		   // openwin("新建文件夹",pageUrl,450,300);
		    artWin = art.dialog.open(pageUrl,{
		    	id:'artWin',
		    	title:"新建文件夹",
				lock:true,
				background: '#999', // 背景色
			    opacity: 0.87,	// 透明度
			    width:450,
			    height:300,
			    close:function(){
			    	window.location.reload();
			    }
			 });
		}
		function closeWin(){
			artWin.close();
		}
		//编辑文件夹
		function editFolder(id){
			var pageUrl = "km_dir_edit.action?id="+id;
		    art.dialog.open(pageUrl,{
			    	id:'dg_win',
			    	title:'编辑文件夹',
					lock:true,
					background: '#999', // 背景色
				    opacity: 0.87,	// 透明度
				    width:500,
				    height:400,
				    close:function(){
				    	var id= art.dialog.data('model_id');
				    	var dfParentid= art.dialog.data('dfParentid');
				    	var refreshGridId = dfParentid;
				    	var nowParentid= art.dialog.data('nowParentid');
				    	if(id==null || id==''){
			      				 refreshNode(dfParentid,true,dfParentid);     //刷新dfParentid对应节点,并且刷新完成之后，选中dfParentid对应节点
			      				 refreshGridId = dfParentid;
			      		   }else{     	
			      		         if(nowParentid == dfParentid){
			      		             refreshNode(dfParentid,true,id);   //刷新dfParentid对应节点,并且刷新完成之后，选中id对应节点  
			      		             refreshGridId = id;    		             
			      		         }//没改路径
			      		         else{
			      		             var bool = AisBParent(nowParentid,dfParentid);
			      		             if(bool){
			      		                  refreshNode(nowParentid,true,nowParentid);   //刷新并选中nowParentid对应节点
			      		                  refreshGridId = nowParentid;
			      		             }
			      		             else{
			      		                 refreshNode(nowParentid,false);  //刷新nowParentid对应节点,刷新完之后，不做选中操作
			      		                 refreshNode(dfParentid,true,dfParentid);   //刷新并选中dfParentid对应节点
			      		                 refreshGridId = dfParentid;
			      		             }
			      		            
			      		         }//改变路径      		         
			      		   }
			      		   refreshGrid2(refreshGridId,'folder');      //刷新表格   
				    }
				 });
				 
		    
		}
		//删除文件夹 
		function delFolder(id){
			$.post('km_dir_isNull.action',{id:id},function(data){
		    	 		if(data=='yes'){
		    	 			art.dialog.confirm('确定删除该文件夹?', function (){
		    	 				$.post('km_dir_del.action',{id:id},function(response){
		    	 						if(response=='ok'){
		    	 								art.dialog.tips("删除成功");
		    	 								refreshSelectP();//刷新并选中父节点  
			           		    				refreshGrid();    //刷新表格
		    	 						}
		    						});
		    	 			
		    	 			});
		    	 		}else if(data=='no'){
		    	 		        art.dialog.tips("该文件夹不为空，不能被删除");
		    	 		}
		    });				    
		}
		//刷新‘根目录’
		function refreshRoot(){
		    var zTree = $.fn.zTree.getZTreeObj("kmtree");
	        var node=zTree.getNodeByParam('id',0);        	   		
	   		zTree.reAsyncChildNodes(node,"refresh",true);
		}
		//刷新当前选中节点,如果没有选中，则刷新‘根目录’(用于新增目录)
		function refreshTreeCur(){
			var zTree = $.fn.zTree.getZTreeObj("kmtree");
			var nodes = zTree.getSelectedNodes();
			if(nodes.length>0){
				node = nodes[0];
				zTree.reAsyncChildNodes(node,"refresh",true);
			}else{
			    refreshRoot(); //刷新‘根目录’
			}
			
		}
		//刷新并选中当前选中节点的父节点（用于删除目录）
		function refreshSelectP(){
			var zTree = $.fn.zTree.getZTreeObj("kmtree");
			var nodes = zTree.getSelectedNodes();
			if(nodes.length>0){		    
			       var pnode = zTree.getNodeByParam('id',nodes[0].parentid,null);
			       zTree.reAsyncChildNodes(pnode,"refresh",true);  //异步加载父节点
			       zTree.selectNode(pnode);			    				
			}			
		}
				
		//刷新id所对应的树节点
		//  refreshid 需要刷新的树节点
		//  bool      刷新之后是否需要选中某个节点
		//  selectid  需要选中的节点id 
		//
		function refreshNode(refreshid,bool,selectid){
		      selectFlag = bool;
		      selectId = selectid;
		      var zTree = $.fn.zTree.getZTreeObj("kmtree");
		      var node = zTree.getNodeByParam('id',refreshid,null);
		      zTree.reAsyncChildNodes(node,"refresh",true);		     		       
		}
		//选中id所对应的树节点
		function selectNode(id){
		      var zTree = $.fn.zTree.getZTreeObj("kmtree");
		      var node = zTree.getNodeByParam('id',id,null);
		      zTree.selectNode(node);
		}
		//刷新表格
	   function refreshGrid(){
	        var zTree = $.fn.zTree.getZTreeObj("kmtree");
			var nodes = zTree.getSelectedNodes();
			if(nodes.length>0){ 
				node = nodes[0];
				//jQuery("#kmlist_grid").jqGrid('setGridParam',{postData:{directoryid:node.id}}).trigger("reloadGrid"); 
				refreshGrid2(node.id,'folder');
			}else{
			    jQuery("#kmlist_grid").trigger("reloadGrid");
			}	   		
	   }
	   //刷新表格
	   function refreshGrid2(directoryid,type){
	        jQuery("#kmlist_grid").jqGrid('setGridParam',{postData:{directoryid:directoryid,type:type}}).trigger("reloadGrid");

	   }
		//添加文件
		function addfile(){
			var zTree = $.fn.zTree.getZTreeObj("kmtree");
			var nodes = zTree.getSelectedNodes();
			var parentid = 0;
			if(nodes.length>0 && nodes[0].type=='DIR'){
					if(nodes[0].PURVIEW!=1){
						art.dialog.tips("您无权限在该目录下添加文档");
						return;
					}
			        parentid = nodes[0].id;			    				
			}else{
				art.dialog.tips("请选择您要添加的文档目录");
				return;
			} 
			var pageUrl = "km_file_add.action?parentid="+parentid;
			var target = "_blank";
    		var win_width = window.screen.width;
    		var page = window.open('form/loader_frame.html',target,'width='+850+',height=600,top=50,left=150,location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=yes');
    		page.location = pageUrl;

		}
	  //编辑文件
		function editfile(){   
		    var rowid = jQuery("#kmlist_grid").jqGrid('getGridParam','selrow');
		    if(rowid==null){
		        art.dialog.tips("请选择需要编辑的文件");
		    	return;
		    }
		    var rowData = jQuery("#kmlist_grid").jqGrid('getRowData',rowid);
		     var id = rowData.ID;
		    if(rowData.PURVIEW!=1){
				art.dialog.tips("无权限执行此操作");
				return;
			}
			
		    if(rowData.TYPE=='DIR'){
		       editFolder(id); 
		    	return;
		    }
		    
		    if(rowData.STATUS==0){
		        art.dialog.tips("文件处于锁定状态，不能编辑");
		    	return;
		    }
		   
			var pageUrl = "km_file_edit.action?id="+id;
			//openwin("编辑文件",pageUrl,450,400);
			art.dialog.open(pageUrl,{
		    	id:'dg_win',
		    	title:"编辑文件",
				lock:true,
				background: '#999', // 背景色
			    opacity: 0.87,	// 透明度
			    width:450,
			    height:400,
			    close:function(){
				//点击右上角关闭时，若有上传的文件，则删除
					var bValue = art.dialog.data('fileuuid');
					var status = art.dialog.data('status');
					if(status!='save'){
						if(bValue!=''&&bValue!='undefined'&&typeof(bValue)!='undefined'){
							$.post('km_file_uploadifyRemove.action',{fileUUID:bValue},function(data){})
						}
					}
					art.dialog.data('status','');
					art.dialog.data('fileuuid','');
			    	refreshGrid();
			    }
			 });
		}
	//删除文件
	function delfile(){
	        var rowid = jQuery("#kmlist_grid").jqGrid('getGridParam','selrow');
		    if(rowid==null){
		       art.dialog.tips("请选择需要删除的文件");
		    	return;
		    }
		    var rowData = jQuery("#kmlist_grid").jqGrid('getRowData',rowid);
		    if(rowData.TYPE=='DIR'){
		        art.dialog.tips("请选择需要删除的文件");
		    	return;
		    }
		    if(rowData.PURVIEW!=1){
				art.dialog.tips("无权限执行此操作");
				return;
			}
		    if(rowData.STATUS==0){
		       art.dialog.tips("文件处于锁定状态，不能删除");
		    	return;
		    }
		    var id = rowData.ID;
		    art.dialog.confirm('确定删除该文件?', function (){
		    	$.post('km_file_del.action',{id:id},function(data){
			      		if(data=='ok'){
			            		art.dialog.tips("删除成功");
			            		refreshGrid();
			      		}
			     	});
		    });
	}
	//更新版本
	function updateversion(){
	        var rowid = jQuery("#kmlist_grid").jqGrid('getGridParam','selrow');
		    if(rowid==null){
		        art.dialog.tips("请选择需要更新的文件");
		    	return;
		    }
		    var rowData = jQuery("#kmlist_grid").jqGrid('getRowData',rowid);
		    if(rowData.TYPE=='DIR'){
		        art.dialog.tips("请选择需要更新的文件");
		    	return;
		    }
		    if(rowData.PURVIEW!=1){
				art.dialog.tips("无权限执行此操作");
				return;
			}
			
		    if(rowData.STATUS==0){
		        art.dialog.tips("文件处于锁定状态，不能更新");
		    	return;
		    }
		    if(rowData.TYPE!='DOC'){
		        art.dialog.tips('LINK、FORM版本更新待开发');
		        return; 
		    }
		    var id = rowData.ID;
			var pageUrl = "km_showUploadOnePage.action?sizeLimit=62914520&fileMaxNum=1&fileMaxSize=62914520&multi=true&id="+id;
			openwin("版本更新",pageUrl,450,400);
	}	
	

	function uploadifyDialog(dialogId,fieldName,divId,sizeLimit,multi,fileExt,fileDesc){
		var zTree = $.fn.zTree.getZTreeObj("kmtree");
		var nodes = zTree.getSelectedNodes();
		var parentid = 0;
		if(nodes.length>0 && nodes[0].type=='DIR'){
				if(nodes[0].PURVIEW!=1){
					alert("您无权限在该目录下添加文档");
					return;
				}
		        parentid = nodes[0].id;			    				
		}else{
			alert("请从左边的树种选择一个目录");
			return;
		}
		if(dialogId==null||dialogId==""||fieldName==null||fieldName==""||divId==null||divId==""){
			alert('参数不正确');
			return ;
		}
		var pageUrl = 'showUploadifyPage.action?parentColId='+fieldName+'&parentDivId='+divId+'&sizeLimit='+sizeLimit+'&multi='+multi+'&fileExt='+fileExt+'&fileDesc='+fileDesc+'&directoryId='+parentid;
		art.dialog.open(pageUrl,{
			id:dialogId,
			title: '上传附件',
			pading: 0,
			lock: true,
			width: 650,
			height: 500, 
			close:function(){
				refreshGrid();
			}
		}); 
		return ;
	}
	//文件下载
	function dloadfile(){
	        var rowid = jQuery("#kmlist_grid").jqGrid('getGridParam','selrow');
		    if(rowid==null){
		        art.dialog.tips("请选择需要下载的文件");
		    	return;
		    }
		    var rowData = jQuery("#kmlist_grid").jqGrid('getRowData',rowid);
		    if(rowData.TYPE!='DOC'){
		        art.dialog.tips("请选择需要下载的文件");
		        return;
		    }
		    var fileUUID = rowData.DOCENUM;
		    if(fileUUID==null || fileUUID==''){
		    	art.dialog.tips("该文档没有绑定文件");
		        return;
		    }
		    var dloadUrl = 'uploadifyDownload.action?fileUUID='+fileUUID;
	        location.href = dloadUrl;
	}
	//添加到收藏夹
	function addFav(){
	        var rowid = jQuery("#kmlist_grid").jqGrid('getGridParam','selrow');
		    if(rowid==null){
		        art.dialog.tips("请选择一个文件");
		    	return;
		    }
		    var rowData = jQuery("#kmlist_grid").jqGrid('getRowData',rowid);
		    var fid = rowData.ID;
		    var ftype = rowData.TYPE;
			$.post('km_fav_addFav.action',{fid:fid,ftype:ftype},function(data){
			      if(data=='exist'){
			            art.dialog.tips("添加失败，该文件已存在于收藏夹");			             
			      }
			      else if(data=='ok'){
			      		art.dialog.tips("添加成功");
			      }
			      else{
			            art.dialog.tips("添加失败"); 
			      }
			});
	       
	}
	//该函数用于‘收藏夹’和‘最近浏览’
	//ftype：0表示文件，1表示文件夹
	//fid：对应于文件夹 或者 文件 的id 
	function showDetail(ftype,fid){
        if(ftype==0){
            if(fid==0){
		        art.dialog.tips("该文件已被删除");
		        return ;
	        }
        	refreshGrid2(fid,'file');
        }
        else if(ftype==1){
            if(fid==0){
		        art.dialog.tips("该文件夹已被删除");
		        return ;
	        }
        	refreshGrid2(fid,'folder');
        }
    }		
    //id：授权ID   purgroup：授权范围    默认按部门进行授权
    function addPurview(id,purgroup,type){ 
			var pageUrl = "km_purview_tree.action?id="+id+"&purviewGroup="+purgroup+"&type="+type+"&purviewType=0"; 
			 var title="";
			if(type==0){
				title= "阅读权限授权";
			}else{
				title= "管理权限授权";
			}
		    art.dialog.open(pageUrl,{
			    	id:'dg_win',
			    	title:title,
					lock:true,
					background: '#999', // 背景色
				    opacity: 0.87,	// 透明度
				    width:650,
				    height:'60%',
				    close:function(){
				    	 var rowid = jQuery("#kmlist_grid").jqGrid('getGridParam','selrow');  
					      clickOtherCol(rowid); 
				    }
				 });
    }
    function sycnAddFolder(newNode){
    	 var treeObj = $.fn.zTree.getZTreeObj("kmtree");
             var nodes = treeObj.getSelectedNodes();
			var parentid = 0;
			if(nodes.length>0){
				var node = nodes[0];
				treeObj.addNodes(node, newNode);
			}
    }
    function uploadXxzcMb(id){
    	 var downUrl =encodeURI("show_km_downloadFile.action?id="+id);
		// window.location.href = downUrl;
		// var seachUrl = encodeURI("zqb_download_xpfj.action?ggid="+id);
				$.ajax({
					type: "POST",
		            url: downUrl,
		            success:function(date){
		            	if(date!="" && date!=null){
		            		window.location.href = encodeURI("show_km_zdown.action?filename="+date);
		            	}
		            },
		            complete: function () {
		            	
		            }
		         });
    }
     function returnSuperior(){
		 var rowid=$("#fhsjid").val();
         var zt=$("#fhsjzt").val();
        /* if(zt==1){
			 clickOtherCol(rowid);
			 refreshGrid2(rowid,'folder');
         }*/
         var downUrl =encodeURI("show_km_superior.action?id="+rowid);
         $.ajax({
             type: "POST",
             url: downUrl,
             success:function(date){
                 $("#fhsjid").val(date);
                 $("#fhsjzt").val(2);
                 var treeObj = $.fn.zTree.getZTreeObj("kmtree");
                 var node = treeObj.getNodeByParam("id",date);
                 treeObj.expandNode(node,true);
                 treeObj.selectNode(node);
                 clickOtherCol(date);
                 refreshGrid2(date,'folder');
             }
         });
	 }
	</script>
	
<style type="text/css">
	
	body {  
    	 font-family: 宋体,Geneva, Arial, Helvetica, sans-serif;  
     	 font-size:   100%;  
    	 *font-size:  80%;
    	 margin-left: 0px;
		 margin-top: 0px;
		 margin-right: 0px;
		 margin-bottom: 0px;  
 	}
 	.ui-jqgrid tr.jqgrow td {
	 white-space: normal !important;
	 height:28px;
	 font-size:12px;
	 vertical-align:text-middle;
	 padding-top:2px;
	 border-right:1px solid #efefef;
	 padding-left:5px;
	}
	.ui-jqgrid tr.jqgrow td a{
	 color:#666;
	 text-decoration:none;
	}
</style>

</head>


<body class="easyui-layout">
	<div region="north" border="false"  style="overflow:hidden;border-top:2px solid #fff;background-image: url(iwork_img/engine/tools_nav_bg.jpg);padding:2px;padding-left:5px;border-bottom:1px solid #cdcdcd;padding:0px auto;height:33px;">
		<a href="#"  plain="true" class="easyui-linkbutton" iconCls="icon-dir-add" onclick="addFolder();return false;" title="新建目录"></a>
		<!-- <a href="#"  plain="true" class="easyui-linkbutton" iconCls="icon-doc-add" onclick="addfile();return false;" title="新建文档"></a> -->		
		<a href="#"  plain="true" class="easyui-linkbutton" iconCls="icon-file-upload" onclick="uploadifyDialog('kmDocId','kmDocId','kmDocDivId','','true','','');" title="批量上传"></a>			
		<!--<a href="#"  plain="true" class="easyui-linkbutton" iconCls="icon-doc-edit" onclick="editfile();return false;" title="编辑文档"></a>
		<!-- <a href="#"  plain="true" class="easyui-linkbutton" iconCls="icon-dir-del" onclick="delFolder();return false;" title="删除目录"></a> -->	
		<a href="#"  plain="true" class="easyui-linkbutton" iconCls="icon-dir-del" onclick="delfile();return false;" title="删除文档"></a>
		<a href="javascript:location.reload();" class="easyui-linkbutton"  plain="true" iconCls="icon-reload" title="刷新"></a>
		<a href="#"  plain="true" class="easyui-linkbutton" iconCls="icon-lock" onclick="lockfile();return false;" title="锁定文档"></a>
		<a href="#"  plain="true" class="easyui-linkbutton" iconCls="icon-unlock" onclick="unlockfile();return false;" title="解锁文档"></a>
<!-- 		<a href="#"  plain="true" class="easyui-linkbutton" iconCls="icon-version-update" onclick="updateversion();return false;" title="更新版本"></a>
 -->		<a href="#"  plain="true" class="easyui-linkbutton" iconCls="icon-file-download" onclick="dloadfile();return false;" title="文件下载"></a>
		<!--<a href="#"  plain="true" class="easyui-linkbutton" iconCls="icon-file-search" onclick="return false;" title="文件查找"></a>
		<a href="#"  plain="true" class="easyui-linkbutton" iconCls="icon-star" onclick="addFav();return false;" title="添加到收藏夹"></a>-->
        <a href="#"  plain="true" class="easyui-linkbutton" style="background: url('iwork_themes/easyui/icons/back.png') no-repeat 7px;" onclick="returnSuperior();return false;" title="返回上一级"></a>
		<span  id="sxx"></span>
	</div>
	
	<div region="west" split="true"  style="border:0px;width:190px;padding:1px;">
		<div id="aa" class="easyui-accordion" fit="true" style="border:0px" >
			<div title="目录导航" iconCls="icon-nav" selected="true"  style="overflow:auto;background-color:#efefef">
				 <ul id="kmtree" class="ztree"></ul>
			</div>
		</div>		
	</div>
	<!-- 
	<div region="east" split="true"   style="border:1px solid #efefef;width:100px;margin-right:5px;background:#FFF;">a&nbsp;</div> -->
	 
	<div region="center"  style="border:0px;padding:0px;background:#gegege" >
			<div class="easyui-layout" fit="true" style="">
				<div region="north" style="border:0px;height:500px;overflow-x:hidden;padding:0px;" >
					<table id='kmlist_grid' style="text-align:center"></table>
				</div>
				<div region="center" split="true" style="width:200px;border:0px;display: none;" >
					<div id="property" fit="true">
					<%--	<div title="基本信息" cache="false">
						</div>
						<div title="预览"  style="padding:0px;" cache="false">
						</div>
						<div title="历史版本" style="padding:0px;" cache="false">
						</div>
						<div title="猜你想要" style="padding:0px;" cache="false">
						</div>--%>
						<div title="权限信息" style="padding:0px;" cache="false">
						</div>
						<%--<div title="评论" style="padding:0px;" cache="false">
						</div>--%>
					</div>
					
				</div>
			</div>
			
	</div>

	<s:hidden name="pageNow" id="pageNow" value="1" theme="simple"></s:hidden>
	<s:hidden name="pageSize" id="pageSize" value="10" theme="simple"></s:hidden>
    <input type="hidden" id="fhsjid"/><input type="hidden" id="fhsjzt"/>
</body> 
 
</html>
