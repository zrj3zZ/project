<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Frameset//EN" "http://www.w3.org/TR/html4/frameset.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head> 
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<title>IWORK综合应用管理系统</title>
<link rel="stylesheet" type="text/css" href="iwork_css/common.css">
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
<link rel="stylesheet" type="text/css" href="iwork_themes/easyui/gray/easyui.css">
<script type="text/javascript" src="iwork_js/commons.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/easyui/jquery.easyui.min.js" charset="gb2312"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/languages/easyui-lang-zh_CN.js"></script>
<link href="iwork_css/public.css" rel="stylesheet" type="text/css" />
<script type="text/javascript">
 //==========================装载快捷键===============================//快捷键
    jQuery(document).bind('keydown',function (evt){		
		    if(evt.ctrlKey&&evt.shiftKey){
			return false;
		   }
		   else if(evt.ctrlKey && event.keyCode==82){ //Ctrl+r /刷新操作
			         this.location.reload(); return false;
		     } 
		  else if(evt.shiftKey && event.keyCode==78){ //Shift+n 新增操作
					add(); return false;
				}
		  else if(evt.ctrlKey && event.keyCode==83){ //ctrl+s保存
					save(); return false;
				}		
}); //快捷键
	</script>
<script type="text/javascript">
	$(function(){
	        //加载导航树
		    $('#portlettree').tree({   
                url: 'cmsPortletAction!openjson.action',
                onSelect:function(node){
                if(node.attributes.type=='portlet'){
                $('#portletgrid').datagrid('selectRecord',node.id.split('_')[0]);   
              	}
                },  
                onClick:function(node){
                if(node.attributes.type=='group'){         
                 document.location.href = "cmsPortletAction!index.action?gname="+encodeURI(encodeURI(node.text)); 
              	}
                }, 
              	onDblClick:function(node){
              	if(node.attributes.type=='group'){
              	$(this).tree('toggle', node.target);   
              	}else{
              	document.getElementById("keyval").innerHTML='';
              	document.forms[0].portletid.value=node.id==null?'':node.id;
              	document.forms[0].portletkey.value=node.attributes.portletkey==null?'':node.attributes.portletkey;
				document.forms[0].portletname.value=node.text==null?'':node.text;		
				$('#groupname').combobox('setValue',node.attributes.groupname==null?'':node.attributes.groupname);
			    document.forms[0].manager.value=node.attributes.manager==null?'':node.attributes.manager;
			    document.forms[0].template.value=node.attributes.template==null?'':node.attributes.template;
			    document.forms[0].morelink.value=node.attributes.morelink==null?'':node.attributes.morelink;
			    document.forms[0].linktarget.value=node.attributes.linktarget==null?'':node.attributes.linktarget;
			    if(node.attributes.verifytype!=null){
                  var ridaolen=document.forms[0].verifytype.length;
                  for(var i=0;i<ridaolen;i++){
                    if(node.attributes.verifytype==document.forms[0].verifytype[i].value){
                      document.forms[0].verifytype[i].checked=true;
                    }
                  }
				}
			    document.forms[0].browse.value=node.attributes.browse==null?'':node.attributes.browse;
			     if(node.attributes.portlettype!=null){
                  var ridaolen=document.forms[0].portlettype.length;
                  for(var i=0;i<ridaolen;i++){
                    if(node.attributes.portlettype==document.forms[0].portlettype[i].value){
                      document.forms[0].portlettype[i].checked=true;
                    }
                  }
				}
				document.forms[0].param.value=node.attributes.param==null?'':node.attributes.param;
				expandSub(); 
			    $('#begindate').datebox('setValue',node.attributes.begindate==null?'':node.attributes.begindate);
			    $('#enddate').datebox('setValue',node.attributes.enddate==null?'':node.attributes.enddate);
			    if(node.attributes.status!=null){
                  var ridaolen=document.forms[0].status.length;
                  for(var i=0;i<ridaolen;i++){
                    if(node.attributes.status==document.forms[0].status[i].value){
                      document.forms[0].status[i].checked=true;
                    }
                  }
				}
				document.forms[0].memo.value=node.attributes.memo==null?'':node.attributes.memo;	
				document.forms[1].prows.value=node.attributes.prows==null?'':node.attributes.prows;	
				document.forms[1].pwidth.value=node.attributes.pwidth==null?'':node.attributes.pwidth;	
				document.forms[1].pheight.value=node.attributes.pheight==null?'':node.attributes.pheight;
				if(node.attributes.isborder!=null){
                  var ridaolen=document.forms[1].isborder.length;
                  for(var i=0;i<ridaolen;i++){
                    if(node.attributes.isborder==document.forms[1].isborder[i].value){
                      document.forms[1].isborder[i].checked=true;
                    }
                  }
				}	
				if(node.attributes.istitle!=null){
                  var ridaolen=document.forms[1].istitle.length;
                  for(var i=0;i<ridaolen;i++){
                    if(node.attributes.istitle==document.forms[1].istitle[i].value){
                      document.forms[1].istitle[i].checked=true;
                    }
                  }
				}	
				document.forms[0].type.value='edit';
				$('#portletdialog').dialog('open'); 
              	}
              	},
           	    onLoadSuccess:function(node,data){
            	var groupId = "<s:property value='gname' escapeHtml='false'/>"+"_group";
                var tnode = $('#portlettree').tree('find',groupId);
                if(tnode!=null){
                $('#portlettree').tree('select',tnode.target);
                }      		
               }
            });
            
			//加载栏目表
		   	$('#portletgrid').datagrid({
            	url:"cmsPortletAction!portletgrid.action?gname="+encodeURI(encodeURI('<s:property value='gname' escapeHtml='false'/>')),
			    loadMsg: "数据加载中，请稍后...",
			    fitColumns: true,
			    singleSelect:true,
			    nowrap:false,
			    rownumbers:true,
			    idField: 'id',
			    columns:[[
			        {field:'portletkey',title:'键值',width:80},
					{field:'portletname',title:'栏目名称',width:100},				
					{field:'manager',title:'管理员',width:80},
					{field:'verifytype',title:'验证类型',width:100,
					formatter:function(value){
					    	if (value==0){						    	
						    	return '匿名访问';
					    	} else {
						    	return '登录验证';
					    	}
					  }
					},
					{field:'portlettype',title:'类型',width:100,
					formatter:function(value){
					    	if (value==0){						    	
						    	return '资讯';
					    	} else if(value==1) {
						    	return '外部链接';
					    	} else{
					    	    return '接口';
					    	}
					  }				
					},
					{field:'begindate',title:'有效期开始',width:100},
					{field:'enddate',title:'有效期结束',width:100},
					{field:'orderindex',title:'排序',width:60},
					{field:'status',title:'状态',width:60,
					formatter:function(value){
					    	if (value==0){						    	
						    	return '开启';
					    	} else {
						    	return '关闭';
					    	}
					    }
					},
					{field:'operate',title:'操作',width:100}			
				]],
				onDblClickRow:function(){
				var row = $('#portletgrid').datagrid('getSelected');
				document.getElementById("keyval").innerHTML='';
				document.forms[0].portletid.value=row.id==null?'':row.id;
				document.forms[0].portletkey.value=row.portletkey==null?'':row.portletkey;
				document.forms[0].portletname.value=row.portletname==null?'':row.portletname;		
			    $('#groupname').combobox('setValue',row.groupname==null?'':row.groupname);
			    document.forms[0].manager.value=row.manager==null?'':row.manager;
			    document.forms[0].template.value=row.template==null?'':row.template;
			    document.forms[0].morelink.value=row.morelink==null?'':row.morelink;
			    document.forms[0].linktarget.value=row.linktarget==null?'':row.linktarget;
			    document.forms[0].orderindex.value=row.orderindex==null?'':row.orderindex;
			    
			    if(row.verifytype!=null){
                  var ridaolen=document.forms[0].verifytype.length;
                  for(var i=0;i<ridaolen;i++){
                    if(row.verifytype==document.forms[0].verifytype[i].value){
                      document.forms[0].verifytype[i].checked=true;
                    }
                  }
				}
			    document.forms[0].browse.value=row.browse==null?'':row.browse;
			     if(row.portlettype!=null){
                  var ridaolen=document.forms[0].portlettype.length;
                  for(var i=0;i<ridaolen;i++){
                    if(row.portlettype==document.forms[0].portlettype[i].value){
                      document.forms[0].portlettype[i].checked=true;
                    }
                  }
				}
				document.forms[0].param.value=row.param==null?'':row.param;
				expandSub(); 
			    $('#begindate').datebox('setValue',row.begindate==null?'':row.begindate);
			    $('#enddate').datebox('setValue',row.enddate==null?'':row.enddate);
			    if(row.status!=null){
                  var ridaolen=document.forms[0].status.length;
                  for(var i=0;i<ridaolen;i++){
                    if(row.status==document.forms[0].status[i].value){
                      document.forms[0].status[i].checked=true;
                    }
                  }
				}
				document.forms[0].memo.value=row.memo==null?'':row.memo;
				document.forms[1].prows.value=row.prows==null?'':row.prows;	
				document.forms[1].pwidth.value=row.pwidth==null?'':row.pwidth;	
				document.forms[1].pheight.value=row.pheight==null?'':row.pheight;
				if(row.isborder!=null){
                  var ridaolen=document.forms[1].isborder.length;
                  for(var i=0;i<ridaolen;i++){
                    if(row.isborder==document.forms[1].isborder[i].value){
                      document.forms[1].isborder[i].checked=true;
                    }
                  }
				}	
				if(row.istitle!=null){
                  var ridaolen=document.forms[1].istitle.length;
                  for(var i=0;i<ridaolen;i++){
                    if(row.istitle==document.forms[1].istitle[i].value){
                      document.forms[1].istitle[i].checked=true;
                    }
                  }
				}		
				document.forms[0].type.value='edit';
				document.forms[0].gname.value='<s:property value='gname' escapeHtml='false'/>';
				$('#portletdialog').dialog('open'); 
			    }
		    });
		    			
			//权限设置
		   var m='<s:property value='flag' escapeHtml='false'/>';
		   if(m=='false'){
		   document.forms[0].manager.disabled=true;
		   }
			
		   //隐藏
		   $('#portletdialog').dialog('close');
			
		 
			
	});	
	
	//设置
	function Set(){
		var row = $('#portletgrid').datagrid('getSelected');
		document.getElementById("keyval").innerHTML='';
		document.forms[0].portletid.value=row.id==null?'':row.id;
		document.forms[0].portletkey.value=row.portletkey==null?'':row.portletkey;
		document.forms[0].portletname.value=row.portletname==null?'':row.portletname;		
	    $('#groupname').combobox('setValue',row.groupname==null?'':row.groupname);
	    document.forms[0].manager.value=row.manager==null?'':row.manager;
	    document.forms[0].template.value=row.template==null?'':row.template;
	    document.forms[0].morelink.value=row.morelink==null?'':row.morelink;
	    document.forms[0].linktarget.value=row.linktarget==null?'':row.linktarget;
	    if(row.verifytype!=null){
	               var ridaolen=document.forms[0].verifytype.length;
	               for(var i=0;i<ridaolen;i++){
	                 if(row.verifytype==document.forms[0].verifytype[i].value){
	                   document.forms[0].verifytype[i].checked=true;
	                 }
	               }
		}
	    document.forms[0].browse.value=row.browse==null?'':row.browse;
	     if(row.portlettype!=null){
	               var ridaolen=document.forms[0].portlettype.length;
	               for(var i=0;i<ridaolen;i++){
	                 if(row.portlettype==document.forms[0].portlettype[i].value){
	                   document.forms[0].portlettype[i].checked=true;
	                 }
	               }
		}
		document.forms[0].param.value=row.param==null?'':row.param;
		expandSub(); 
	    $('#begindate').datebox('setValue',row.begindate==null?'':row.begindate);
	    $('#enddate').datebox('setValue',row.enddate==null?'':row.enddate);
	    if(row.status!=null){
	               var ridaolen=document.forms[0].status.length;
	               for(var i=0;i<ridaolen;i++){
	                 if(row.status==document.forms[0].status[i].value){
	                   document.forms[0].status[i].checked=true;
	                 }
	               }
		}
		document.forms[0].memo.value=row.memo==null?'':row.memo;
		document.forms[1].prows.value=row.prows==null?'':row.prows;	
		document.forms[1].pwidth.value=row.pwidth==null?'':row.pwidth;	
		document.forms[1].pheight.value=row.pheight==null?'':row.pheight;
		if(row.isborder!=null){
	               var ridaolen=document.forms[1].isborder.length;
	               for(var i=0;i<ridaolen;i++){
	                 if(row.isborder==document.forms[1].isborder[i].value){
	                   document.forms[1].isborder[i].checked=true;
	                 }
	               }
		}
		if(row.istitle!=null){
	               var ridaolen=document.forms[1].istitle.length;
	               for(var i=0;i<ridaolen;i++){
	                 if(row.istitle==document.forms[1].istitle[i].value){
	                   document.forms[1].istitle[i].checked=true;
	                 }
	               }
		}		
		document.forms[0].type.value='edit';
		document.forms[0].gname.value='<s:property value='gname' escapeHtml='false'/>';
		$('#portletdialog').dialog('open'); 
	}
	
	//添加
	function add(){
	  document.getElementById("keyval").innerHTML='';
	  document.forms[0].portletid.value='';
	  document.forms[0].portletkey.value='';
	  document.forms[0].portletname.value='';		
      $('#groupname').combobox('setValue',''); 
      var m='<s:property value='flag' escapeHtml='false'/>';    
	  if(m=='false'){
	   document.forms[0].manager.disabled=false;
	   document.forms[0].manager.value='<s:property value='user' escapeHtml='false'/>';  
	   document.forms[0].manager.disabled=true;  
	  }else{
	   document.forms[0].manager.value='';
	  }   	 
	  document.forms[0].template.value='';	
	  document.forms[0].morelink.value='';	
	  document.forms[0].linktarget.value='';		    
      document.forms[0].verifytype[0].checked=true;       
	  document.forms[0].browse.value='';
	  document.forms[0].portlettype[0].checked=true;
	  document.forms[0].param.value=''; 
	  expandSub(); 
	  $('#begindate').datebox('setValue','');
	  $('#enddate').datebox('setValue','');	
      document.forms[0].status[0].checked=true;
	  document.forms[0].memo.value='';	
	  document.forms[1].prows.value='';	
	  document.forms[1].pwidth.value='';	
	  document.forms[1].pheight.value='';
      document.forms[1].isborder[0].checked=true;
      document.forms[1].istitle[0].checked=true;
	  document.forms[0].type.value='add';
	  document.forms[0].gname.value='<s:property value='gname' escapeHtml='false'/>';
	  $('#portletdialog').dialog('open');
	}
	
	//删除
	function remove(){
		//根据表
		var row = $('#portletgrid').datagrid('getSelected');
		if (row){
		    $.messager.confirm('系统提示','确认删除?',function(result){  
		    if(result){
				var index = $('#portletgrid').datagrid('getRowIndex', row);
				$('#portletgrid').datagrid('deleteRow', index);
				document.forms[0].portletid.value=row.id==null?'':row.id;
				document.forms[0].gname.value='<s:property value='gname' escapeHtml='false'/>';
				var url='cmsPortletRemove.action';	
			    document.forms[0].action=url;
			    document.forms[0].submit();
			 	return false; 
		 	}
		 	})
		}
	}
	
	//保存
   function save(){
                     var portletkey=document.forms[0].portletkey.value;
					 if(portletkey==""){
					  $.messager.alert('系统提示','键值不允许为空!','info'); 
		 			  return false;
					 }
					 if(length2(portletkey)>32){
					   $.messager.alert('系统提示','键值过长!','info'); 
		 			  return false;
					 }
					 var key=document.getElementById("keyval").innerHTML;
					 if(key!=""){
					 $.messager.alert('系统提示','键值已存在，请更换!','info'); 
		 			  return false;
					 }			 
					 if(document.forms[0].portletname.value==""){
					  $.messager.alert('系统提示','栏目名称不允许为空!','info'); 
		 			  return false;
					 }
					  if(length2(document.forms[0].portletname.value)>64){
					   $.messager.alert('系统提示','栏目名称过长!','info'); 
		 			  return false;
					 }
					 if($('#groupname').combobox('getValue')==""){
					  $.messager.alert('系统提示','分组名称不允许为空!','info'); 
		 			  return false;
					 }
					  if(length2(document.forms[0].manager.value)>256){
					   $.messager.alert('系统提示','管理员过长!','info'); 
		 			  return false;
					 }
					  if(length2(document.forms[0].browse.value)>512){
					   $.messager.alert('系统提示','浏览权限过长!','info'); 
		 			  return false;
					 }
				  	var type=document.getElementsByName("portlettype");
				    var a;
					for(var i=0;i<type.length;i++){
						if(type[i].checked){
							a=type[i].value;
						}
					}
					 if(a==1){
				 	     if(document.forms[0].param.value==""){
							  $.messager.alert('系统提示','外部链接URL不允许为空!','info'); 
				 			  return false;
						 }
						 if(length2(document.forms[0].param.value)>512){
					   $.messager.alert('系统提示','外部链接URL过长!','info'); 
		 			  return false;
					 }	if(length2(document.forms[0].morelink.value)>256){
					   $.messager.alert('系统提示','More链接过长!','info'); 
		 			  return false;
					 }	if(length2(document.forms[0].linktarget.value)>64){
					   $.messager.alert('系统提示','提交目标过长!','info'); 
		 			  return false;
					 }	
				 	}else if(a==2){
				 	     if(document.forms[0].param.value==""){
							  $.messager.alert('系统提示','接口实现类不允许为空!','info'); 
				 			  return false;
						 }
						 if(length2(document.forms[0].param.value)>512){
					   $.messager.alert('系统提示','外部链接URL过长!','info'); 
		 			  return false;
					 }	if(length2(document.forms[0].morelink.value)>256){
					   $.messager.alert('系统提示','More链接过长!','info'); 
		 			  return false;
					 }	if(length2(document.forms[0].linktarget.value)>64){
					   $.messager.alert('系统提示','提交目标过长!','info'); 
		 			  return false;
					 }	
				 	}
				 	if(length2(document.forms[0].template.value)>64){
					   $.messager.alert('系统提示','模版文件过长!','info'); 
		 			  return false;
					 }	
					 if($('#begindate').datebox('getValue')==""){
					  $.messager.alert('系统提示','开始时间不允许为空!','info'); 
		 			  return false;
					 }
			         if($('#enddate').datebox('getValue')==""){ 
			          $.messager.alert('系统提示','结束时间不允许为空!','info'); 
		 			  return false;
					 }
					if(length2(document.forms[0].memo.value)>500){
					   $.messager.alert('系统提示','备注过长!','info'); 
		 			  return false;
					 }
				
					if(document.forms[1].prows.value!=""&&document.forms[1].prows.value.match(/^\d+$/) == null){
					 $.messager.alert('系统提示','行数为正整数!','info'); 
		 			  return false;
					}
					if(document.forms[1].pwidth.value!=""&&document.forms[1].pwidth.value.match(/^\d+$/) == null){
					 $.messager.alert('系统提示','宽度为正整数!','info'); 
		 			  return false;
					}
					if(document.forms[1].pheight.value!=""&&document.forms[1].pheight.value.match(/^\d+$/) == null){
					 $.messager.alert('系统提示','高度为正整数!','info'); 
		 			  return false;
					}
					 var isborder=document.getElementsByName("isborder");
					 var b;
					 for(var i=0;i<isborder.length;i++){
						if(isborder[i].checked){
							b=isborder[i].value;
						}
					 }
					 var istitle=document.getElementsByName("istitle");
					 var t;
					 for(var i=0;i<istitle.length;i++){
						if(istitle[i].checked){
							t=istitle[i].value;
						}
					 }
					  var verifytype=document.getElementsByName("verifytype");
					 var v;
					 for(var i=0;i<verifytype.length;i++){
						if(verifytype[i].checked){
							v=verifytype[i].value;
						}
					 }
					  var portlettype=document.getElementsByName("portlettype");
					 var p;
					 for(var i=0;i<portlettype.length;i++){
						if(portlettype[i].checked){
							p=portlettype[i].value;
						}
					 }
					 
					 var status=document.getElementsByName("status");
					 var s;
					 for(var i=0;i<status.length;i++){
						if(status[i].checked){
							s=status[i].value;
						}
					 }
                     document.forms[0].manager.disabled=false;
			           $.ajax({
						type: 'POST',
						url: 'cmsPortletEdit.action',
						data: {'portletid':document.forms[0].portletid.value,'portletkey':document.forms[0].portletkey.value,'orderindex':document.forms[0].orderindex.value,'portletname':document.forms[0].portletname.value,'groupname':$('#groupname').combobox('getValue'),'manager':document.forms[0].manager.value,'template':document.forms[0].template.value,'morelink':document.forms[0].morelink.value,'linktarget':document.forms[0].linktarget.value,'verifytype':v,'browse':document.forms[0].browse.value,'portlettype':p,'param':document.forms[0].param.value,'begindate':$('#begindate').combobox('getValue'),'enddate':$('#enddate').combobox('getValue'),'status':s,'memo':document.forms[0].memo.value,'prows':document.forms[1].prows.value,'pwidth':document.forms[1].pwidth.value,'pheight':document.forms[1].pheight.value,'isborder':b,'istitle':t,'type':document.forms[0].type.value,'gname':document.forms[0].gname.value},
						dataType: 'text',
						success: function(data,status){
						// alert(data);
						}
					   });
					   $('#portletdialog').dialog('close');
                       document.location.href = "cmsPortletAction!index.action?gname="+encodeURI(encodeURI(document.forms[0].gname.value)); 
				       return false; 
					}
		    //取消			
			function cancel(){
						$('#portletdialog').dialog('close');
					}		
			function openTab(title,url){
				try{
				window.parent.addTab(title,url);
				}catch(e){
					var page = window.open('form/waiting.html',newWin,'width='+win_width+',height=650,top=50,left=50,location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=yes');
	    				page.location=url;
				}
			}		
</script>
<script type="text/javascript" src="iwork_js/plugs/cmsportletaction.js"></script>

<s:property value='pstrScript'  escapeHtml='false'/>
</head>
<body class="easyui-layout">
    <!-- TOP区 -->
	<div region="north" border="false" class="tools_nav" style="background-color:#efefef;padding:0px;overflow:no">
	<div style="padding:2px;">
		<a href="javascript:add();" class="easyui-linkbutton" plain="true" iconCls="icon-add">添加栏目</a>
		<a href="javascript:remove();" class="easyui-linkbutton" plain="true" iconCls="icon-remove">删除栏目</a>
		<a href="javascript:this.location.reload();" class="easyui-linkbutton" plain="true" iconCls="icon-reload">刷新</a>
	</div>
	</div>
    <!-- 导航区 -->
	<div region="west"  split="false" border="false"  style="border-right:1px solid #efefef;width:200px;padding:0px;overflow:hidden;">	
	    <ul id="portlettree">
		</ul> 
    </div>
    <!-- 内容区 -->
	<div region="center" style="padding:3px;">
		<table id="portletgrid" style="margin:2px;">
		</table>
	</div>
	<!-- 编辑窗口 -->
    <div id="portletdialog" class="easyui-dialog" title="栏目信息维护" icon="icon-edit"  style="width:400px;height:480px;padding-top:10px;background: #fafafa;" buttons="#dlg-buttons">			 
      <div id="portletTab"  class="easyui-tabs" fit="true" plain="true" border="false" style="height:100%;width:380px;">
	    <div title="基础信息" style="width:100%;height:100%;"> 
        <form id="portletform" method="post"> 
        <table width="100%" border="0" cellpadding="0" cellspacing="0" style='margin-top:5px;'>
        <tr>
	    <td align='right' width='30%'>键值：</td>
        <td width='30%'><input class="easyui-validatebox" type="text" id="portletkey" name="portletkey" onKeyUp="keyVal(this.value);" required="true"></input>&nbsp;<span style='color:red'>*</span></td>
        <td><div id='keyval'></div></td>
        </tr>
        <tr>
	    <td align='right'>栏目名称：</td>
        <td colspan='2'><input class="easyui-validatebox" type="text" id="portletname" name="portletname" required="true"></input>&nbsp;<span style='color:red'>*</span></td>
        </tr>
        <tr>
	    <td align='right'>分组：</td>
        <td colspan='2'><input class="easyui-combobox" editable='false'	id="groupname" name="groupname" url="cmsPortletAction!combobox.action" valueField="text"  textField="text"  panelHeight="auto" required="true">&nbsp;<span style='color:red'>*</span></td>
        </tr>
        <tr>
	    <td align='right'>管理员：</td>
        <td colspan='2'><input class="easyui-validatebox" type="text" id="manager" name="manager"></input></td>
        </tr>
        <tr>
	    <td align='right'>验证类型：</td>
        <td colspan='2'><input type="radio" checked id="verifytype" name="verifytype" value='0'>匿名访问<input type="radio" id="verifytype" name="verifytype" value='1'>登录验证&nbsp;<span style='color:red'>*</span></td>
        </tr>
        <tr>
	    <td align='right'>浏览权限：</td>
        <td colspan='2'><input class="easyui-validatebox" type="text" id="browse" name="browse"></input></td>
        </tr>
        <tr>
	    <td align='right'>排序：</td>
        <td colspan='2'><input class="easyui-validatebox" type="text" id="orderindex" name="orderindex" required="true"></input>&nbsp;<span style='color:red'>*</span></td>
        </tr>
        <tr>  
	    <td align='right'>类型：</td>
        <td colspan='2'>
        	<input type="radio" checked id="portlettype0" name="portlettype" onclick='expandSub();' value='0' ><label for="portlettype0">资讯</lable>
        	<input type="radio" id="portlettype1" name="portlettype" onclick='expandSub();' value='1'><label for="portlettype1">外部链接</lable>
        	<input type="radio" id="portlettype2" name="portlettype" onclick='expandSub();' value='2'><label for="portlettype2">实现接口&nbsp;</lable>
        	<input type="radio" id="portlettype3" name="portlettype" onclick='expandSub();' value='3'><label for="portlettype3">RSS资讯&nbsp;</lable>
        	
        	<span style='color:red'>*</span></td>
        </tr>     
        <tbody id=GROUP_SUB>
        <tr>
	    <td align='right'><div id='paramtitle'></div></td>
        <td colspan='2'><input class="easyui-validatebox" type="text" id="param" name="param" required="true"></input>&nbsp;<span style='color:red'>*</span></td>
        </tr>
        <tr>
	    <td align='right'>More链接：</td>
        <td colspan='2'><input class="easyui-validatebox" type="text" id="morelink" name="morelink"></input></td>
        </tr>
        <tr>
	    <td align='right'>提交目标：</td>
        <td colspan='2'><input class="easyui-validatebox" type="text" id="linktarget" name="linktarget"></input></td>
        </tr>
        </tbody>
        <tr>
	    <td align='right'>扩展参数：</td>
        <td colspan='2'><input class="easyui-validatebox" type="text" id="template" name="template"></input></td>
        </tr>        
        <tr>
	    <td align='right'>有效期：</td>
        <td colspan='2'><input class="easyui-datebox"  editable='false' id="begindate" name="begindate" style="width:100px;" required="true"></input>&nbsp;至&nbsp;<input class="easyui-datebox"  editable='false' id="enddate" name="enddate" style="width:100px;" required="true"></input>&nbsp;<span style='color:red'>*</span></td>
        </tr>  
        <tr>
	    <td align='right'>状态：</td>
        <td colspan='2'><input type="radio" checked id="status" name="status" value='0'>开启<input type="radio" id="status" name="status" value='1'>关闭&nbsp;<span style='color:red'>*</span></td>
        </tr>
        <tr>
	    <td align='right'>备注：</td>
        <td colspan='2'><textarea id="memo" name="memo" style="height:60px;width:155px;"></textarea></td>
        </tr>
        </table>
        <input type='hidden' id='portletid' name='portletid'>
        <input type='hidden' id='type' name='type'>
        <input type='hidden' id='gname' name='gname'>
        
        </form>	 
        </div>
        <div title="外观信息" style="width:100%;height:100%;">
        <form id="pform" method="post"> 
        <table width="100%" border="0" cellpadding="0" cellspacing="0" style='margin-top:5px;'>
        <tbody id=ROW_SUB>
        <tr>
	    <td align='right'>行数：</td>
        <td><input class="easyui-validatebox" type="text" id="prows" name="prows"  size=10></input></td>
        </tr>
         </tbody>
        <tr>
	    <td align='right' width='24%'>宽度：</td>
        <td><input class="easyui-validatebox" type="text" id="pwidth" name="pwidth" size=10></input></td>
        </tr>
        <tr>
	    <td align='right'>高度：</td>
        <td><input class="easyui-validatebox" type="text" id="pheight" name="pheight" size=10></input></td>
        </tr>
        <tr>
	    <td align='right'>显示边框：</td>
        <td><input type="radio" checked id="isborder" name="isborder" value='0'>是<input type="radio" id="isborder" name="isborder" value='1'>否</td>
        </tr>
        <tr>
	    <td align='right'>显示标题栏：</td>
        <td><input type="radio" checked id="istitle" name="istitle" value='0'>是<input type="radio" id="istitle" name="istitle" value='1'>否</td>
        </tr>
        </table>
        </form>
        </div>
        </div>      
      </div>
      
      <div id="dlg-buttons">
		<a  href="javascript:save();" class="easyui-linkbutton" iconCls="icon-save">保存</a>
		<a href="javascript:cancel();" class="easyui-linkbutton" iconCls="icon-cancel">取消</a>
	 </div>
</body>
</html>
