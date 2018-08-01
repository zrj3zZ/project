﻿<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <title>底稿存档添加表单</title>
    <link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
    <link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/zTreeStyle.css">
    <link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
    <link href="iwork_css/public.css" rel="stylesheet" type="text/css" />
    <link href="iwork_css/common.css" rel="stylesheet" type="text/css" />
    <link rel="stylesheet" type="text/css" href="iwork_css/titleSelect.css">
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js" ></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.validate.js"   ></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.metadata.js"   ></script>
    <script type="text/javascript" src="iwork_js/jquery.form.js"></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.ztree.core-3.5.35min.js"></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.ztree.excheck-3.4.min.js"></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/fuzzysearch.js"></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.ztree.exhide.js"></script>
    <script type="text/javascript" src="iwork_js/lhgdialog/lhgdialog.min.js?self=false&skin=default"></script>

    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.ztree.exedit-3.4.min.js"></script>
    <style>
    	.ztree li span.button.add {
			background-position:-144px 0; 
			vertical-align:top; 
			*vertical-align:middle;
			margin-right: 3px;
			margin-left: 3px;
		}
		.ztree li span.button.insert {
			background-position:-127px -48px; 
			vertical-align:top; 
			*vertical-align:middle;
			margin-right: 3px;
			margin-left: 3px;
		}
		
		.ztree li span.button.edit {
			margin-right: 3px;
			margin-left: 3px;
		}
		
		.ztree li span.button.remove {
			margin-right: 3px;
			margin-left: 3px;
		}
        #All {
            margin-left: 299px;
        }

        #left {
            float: left;
            height: 700px;
            width: 500px;
            border: 1px solid #0f0f0f;
        }

        #right {
            float: left;
            height: 700px;
            width: 860px;
            border: 1px solid #0f0f0f;
        }

        .none_i {
            display: none;
        }
        .choisePro{
            display:none;
        }

    </style>
    <script>
        //加载导航树
        $(function(){
            initTree();
            
        });
        function initTree(treeNodeId){
        	var COMPANYNAME = $('#COMPANYNAME').val();
            var setting = {
                callback: {
                    onClick:onClick,
                    beforeRemove: zTreeBeforeRemove,
                    beforeRename: zTreeBeforeRename,
                    onExpand: zTreeOnExpand
                },
                view:{
                    addHoverDom: addHoverDom,
                    removeHoverDom: removeHoverDom,
                    fontCss:setFontCss
                },
                data: {
                    simpleData: {
                        enable:true,
                        idKey: "id",
                        pIdKey: "pId"
                    }
                },
                edit: {
                    enable: true,
                    showRemoveBtn: true,
                    showRenameBtn: true
                }
            };
            
            var zNodes =[];
            $.ajax({
            	url:'dg_dgcdLeftIframe.action',
            	async:false,
            	dataType:'json',
            	data:{COMPANYNAME:COMPANYNAME},
            	success:function(data){
            		if(data==""){
            			alert("项目id为空");
            		}else{
            			zNodes = data ;
            		}
                }
            	
            });
            var treeObj = $.fn.zTree.init($("#leftIframe"), setting , zNodes);//加载导航树
            fuzzySearch('leftIframe','#key',null,true); //初始化模糊搜索方法
            
            function setFontCss(treeId, treeNode) {
                if(treeNode.classification=='正常'){
                    return {}
                }
                if(treeNode.classification=='高风险'){
                    return {color:"#FF0000"}
                }
                if(treeNode.classification=='次高风险'){
                    return {color:"#EE9572"}
                }
                if(treeNode.classification=='关注'){
                    return {color:"#6495ED"}
                }
            }
        }


        function addDiyDom(treeId, treeNode) {
        	
        	if($("#" + treeNode.tId + "_ico").lenght=0){
        		return;
        	}
        	for(var i=0;i<treeNode.children.length;i++){
        		var qianzhui = $("#" + treeNode.children[i].tId + "_ico").attr("qianzhui");
        		if($("#" + treeNode.children[i].tId + "_ico").length<=0){
    				return ;
    			}
        		var item1 = treeNode.children[i].prefix;
        		if(qianzhui==undefined){
        			$("#" + treeNode.children[i].tId + "_ico").after("<span>"+item1+"</span>");
                    $("#" + treeNode.children[i].tId + "_ico").attr("qianzhui","1");
                }
                if(treeNode.children[i].children!=undefined){
                    if(treeNode.children[i].children.length>0){
                    	addDiyDom(treeNode.children[i].id,treeNode.children[i]);
                    }
                }
        	}
        };
        function zTreeOnExpand(event, treeId, treeNode) {
            //查找所有子标签
            var list=$('span[id^=leftIframe_][id$=_ico]');
            $(list[0]).attr("qianzhui","1");
            var nums =0;
            for(var i=0;i<list.length;i++){
                var qianzhui = $(list[i]).attr("qianzhui");
                if(qianzhui==undefined){
                    var item = treeNode.children[nums].prefix;
                    $(list[i]).after("<span>"+item+"</span>");
                    $(list[i]).attr("qianzhui","1");
                    nums++;
                }

            }

            //追加语句
            //$(list[0]).after("<span>Appended item</span>");
        };
        //自定义的按钮
        function addHoverDom(treeId, treeNode) {
            var sObj = $("#" + treeNode.tId + "_span");
            if (treeNode.editNameFlag || $("#addBtn_"+treeNode.tId).length>0) return;
            var addStr = "<span class='button add' id='addBtn_" + treeNode.tId
                + "' title='添加记录' onclik='' onfocus='this.blur();'></span>";
            sObj.after(addStr);
            var addStr2 = "<span class='button insert' id='insertBtn_" + treeNode.tId
                + "' title='向上插入目录' onfocus='this.blur();'></span>";
            sObj.after(addStr2);
            var btn = $("#addBtn_"+treeNode.tId);
            if (btn) btn.bind("click", function(){
                //添加的action需要写在这里
                //var zTree = $.fn.zTree.getZTreeObj("leftIframe");
                //var tt = zTree.addNodes(treeNode, {id:(100 + newCount), pId:treeNode.id, name:"new node" + (newCount++)});
                add(treeNode);
                return false;
            });
            var btn2 = $("#insertBtn_"+treeNode.tId);
            if (btn2) btn2.bind("click", function(){
                //添加的action需要写在这里
                //var zTree = $.fn.zTree.getZTreeObj("leftIframe");
                //var tt = zTree.addNodes(treeNode, {id:(100 + newCount), pId:treeNode.id, name:"new node" + (newCount++)});
                insert(treeNode);
                return false;
            });
        };

        //选中节点之上插入数据
        function insert(e) {
            var zTree = $.fn.zTree.getZTreeObj("leftIframe"),
                isParent = e.pId,
                nodes = zTree.getSelectedNodes(),
                treeNode = nodes[0],
                indexId = zTree.getNodeIndex(treeNode),
                DDJE = treeNode.DDJE,
                newId=0;

            var COMPANYNAME = $('#COMPANYNAME').val();
            var node = zTree.getNodesByFilter(
                function (node) {
                    return (node.id == isParent);
                }, true); // 仅查找一个节点

            if(treeNode!=null){
                $.ajax({
                    dataType:"json",
                    url:"dg_dgcdZreeInsert.action",
                    async:false,
                    data:{
                        XYJE:isParent,
                        DDJE:DDJE,
                        COMPANYNAME:COMPANYNAME
                    },
                    success:function(data){
                        if(data.reslut!=0||data.reslut!=""){
                            newId=data.reslut;
                        }
                    }
                });
            }else{
                alert("请选中要添加插入的目录！");
                return ;
            }

            if(newId>0){
                treeNode = zTree.addNodes(node,indexId, {id:newId, pId:isParent, name:"默认目录名"});
                if (treeNode) {
                    zTree.editName(treeNode[0]);
                }
                
            }else{
                alert("插入失败（根目录上面不能插入目录），请刷新页面重试");
            }
        };

        //添加子节点
        function add(e) {
            /*  参数  id这个是返回的   		pid传参
            */
            var zTree = $.fn.zTree.getZTreeObj("leftIframe"),
                isParent = e.id,
                nodes = zTree.getSelectedNodes(),
                treeNode = nodes[0],
                newId=0;
            var COMPANYNAME = $('#COMPANYNAME').val();
            if(treeNode!=null){
                $.ajax({
                    dataType:"json",
                    url:"dg_dgcdZreeAdd.action",
                    async:false,
                    data:{
                        XYJE:isParent,
                        COMPANYNAME:COMPANYNAME
                    },
                    success:function(data){
                        if(data.reslut!=0||data.reslut!=""){
                            newId=data.reslut;
                        }
                    }

                });
            }else{
                alert("请选中要添加的父目录！");
                return ;
            }
            if(newId>0){
                if (treeNode) {
                    treeNode = zTree.addNodes(treeNode, {id:newId, pId:treeNode.id, name:"默认目录名"});
                } else {
                    treeNode = zTree.addNodes(null, {id:newId, pId:0, name:"默认目录名"});
                }
                if (treeNode) {
                    zTree.editName(treeNode[0]);
                }
            }else{
                alert("添加失败请刷新后重试");
            }
        };
		
        //重新加载并且打开指定的节点
        function openNode(treeNodeId){
        	initTree();
        	var treeObj = $.fn.zTree.getZTreeObj("leftIframe");
        	var node = treeObj.getNodeByParam("id", treeNodeId, null);
        	var node0 = treeObj.getNodeByParam("id", 0, null);
        	treeObj.expandNode(node, true, true, true);
        	addDiyDom(node0.id,node0);
        }
        
        function removeHoverDom(treeId, treeNode) {
            $("#addBtn_"+treeNode.tId).unbind().remove();
            $("#insertBtn_"+treeNode.tId).unbind().remove();
        };

        //删除某个指定节点
        function zTreeBeforeRemove(event,treeNode,treeId){
            var zTreeId = treeNode.id;
            var flage = 1;
            async:false,
                $.ajax({
                    dataType:"json",
                    url:"dg_dgcdZreeDelete.action",
                    async:false,
                    data:{
                        zTreeId:zTreeId
                    },
                    success:function(data){
                        alert(data.message);
                        if(data.reslut=="error"){
                            flage=0;
                        }
                    }

                });
            if(flage==1){
            	openNode(treeNode.pId);
                return true;
            }else{
                return false;
            }
        }
        //改名操作  编写action 修改成功则改了，不成功就提示一下然后不改
        function zTreeBeforeRename(event,treeNode,treeNodeName,isCancel){
            var flage = 1;
            var zTreeId = treeNode.id;
            $.ajax({
                dataType:"json",
                url:"dg_dgcdZreeRename.action",
                async:false,
                data:{
                    zTreeId:zTreeId,
                    zTreeName:treeNodeName
                },
                success:function(data){
                    alert(data.message);
                    if(data.reslut=="error"){
                        var treeObj = $.fn.zTree.getZTreeObj("leftIframe");
                        treeObj.cancelEditName();
                        if(data.reslut=="error"){
                            flage=0;
                        }
                    }
                }

            });
            if(flage==1){
            	openNode(treeNode.pId);
                return true;
            }else{
                return false;
            }
        }

        //点击事件
        function onClick(event, treeId,treeNode, clickFlag){
            var XMQYID = treeNode.id;
            window.parent.$("#XMQY_ID").val(XMQYID);
            //var pageUrl = "sysDem_DataList.action?demUUID=1dfe958166a347188339af1337e25fb7&KHBH="+khbh+"&init_params=KHBH="+khbh+";KHMC="+encodeURI(khmc);
            window.parent.document.getElementById('rightIframe').contentWindow.selectFile(XMQYID);

        }
      //导入excel
        function openImpExcel(instanceId,subformid,subformkey,engineType){
        	var modelId = $("#modelId").val();
        	var pageUrl = "dg_dgcdZreeExcelImp.action?modelId="+modelId+"&instanceId="+instanceId+"&subformid="+subformid+"&subformkey="+subformkey+"&engineType="+engineType;
        	art.dialog.open(pageUrl,{
        		id:'DictionaryDialog',
        		title:"数据选择",
        		lock:true,
        		background: '#999', // 背景色
        	    opacity: 0.87,	// 透明度
        	    width:'90%',
        	    height:'90%'
        	 });
        }
    </script>
</head>
<body>
    <div region="west" style="width:100%;height:100%;background-color:#efefef" border="false" class="layout-body">
    <div style="padding-left:12px;padding-top:14px;">目录关键字：<input type="text" class="{maxlength:128,required:false,string:true}" style="width:100px" class="empty" id="key" value=""></div>
		<ul id="leftIframe" class="ztree" ></ul>
	</div>
	<s:hidden name="COMPANYNAME" id="COMPANYNAME"></s:hidden> 
</body>
<script>
</script>
</html>