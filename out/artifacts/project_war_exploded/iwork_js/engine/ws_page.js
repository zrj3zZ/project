	var mainFormValidator;
	$().ready(function() {
			 var setting = {
				async: {
						enable: true, 
						url:"sysEngineGroup!openjson.action",
						dataType:"json"
					},
				view: {   
					dblClickExpand: false
				},
				data: {
					simpleData: {
						enable: true
					}
				},
				callback: {
					//beforeClick: beforeClick,
					onClick: onClick
				}
			};
			 setInterfaceType();
			 setContentType();
			 initCheckType();
			 setCheckType();
			 setIsCache();
			$.fn.zTree.init($("#treeDemo"), setting);
			mainFormValidator =  $("#editForm").validate({
				debug:false,
				errorPlacement: function (error, element) { //指定错误信息位置
				      if (element.is(':radio') || element.is(':checkbox')) {
				          var eid = element.attr('name');
				          error.appendTo(element.parent());
				      } else {
				          error.insertAfter(element);
				     }
				 } 
			 });
			 mainFormValidator.resetForm();
			 
		});
		function doSubmit(){ 
			var valid = mainFormValidator.form(); //执行校验操作
			if(!valid){ 
					return;
				} 
			var options = {
				error:errorFunc,
				success:successFunc 
			   }; 
			$('#editForm').ajaxSubmit(options);
		}
        errorFunc=function(){
           alert("保存失败！");
        }
	    successFunc=function(responseText, statusText, xhr, $form){
	           if(responseText=="success"){
	        	   art.dialog.tips("保存成功",2);
	        	   reload();
	        	   //api.close();
	           }
	           else if(responseText=="error"){
	              alert("保存失败！");
	           } 
	    }
	    //根据不同接口类型，显示不同参数界面
	    function setInterfaceType(){
	    	var val= $('input[name="model.wsType"]:checked').val();
	    	if(val=='1'){
				$("#name_tr").show(); 
	    		$("#url_tr").hide(); 
	    		$("#contentTypeTr").show(); 
	    	}else if(val=='2'){
				$("#name_tr").hide(); 
	    		$("#url_tr").show(); 
	    		$("#contentTypeTr").hide(); 
	    	}
	    }

		// 设置是否缓存
	    function setIsCache(){
	    	var val= $('input[name="model.isCache"]:checked').val();
			
			if(val=='1'){
				$("#cacheTimeTr").show(); 
	    	}else if(val=='0'){
				$("#cacheTimeTr").hide(); 
	    	}
	    }
		
		
		//根据不同数据类型，显示不同参数界面
	    function setContentType(){
	    	var val= $('input[name="model.contentType"]:checked').val();
	    }
		
		
		//根据不同的验证方式，显示不同输入区域
	    function initCheckType(){
			$("[name='checkTypeList']").each(function(){
			
				if(this.value == 1){
					if(this.checked == true){
						$("#permitIpTr").show();
						$("#forbidIpTr").show();
					}else{
						$("#permitIpTr").hide();
						$("#forbidIpTr").hide();
					}
				}
				
				if(this.value == 2){
					if(this.checked == true){
						$("#usernameTr").show();
						$("#passwordTr").show();
					}else{
						$("#usernameTr").hide();
						$("#passwordTr").hide();
					}
				}
			})
	    }
		
		//根据不同的验证方式，显示不同输入区域
	    function setCheckType(){
			$("[name='checkTypeList']").each(function(){
				var id = this.id;
				
				$("#"+id).click(function(){
					if(this.value == 1){
						if(this.checked == true){
							$("#permitIpTr").show();
							$("#forbidIpTr").show();
						}else{
							$("#permitIpTr").hide();
							$("#forbidIpTr").hide();
						}
					}
					
					if(this.value == 2){
						if(this.checked == true){
							$("#usernameTr").show();
							$("#passwordTr").show();
						}else{
							$("#usernameTr").hide();
							$("#passwordTr").hide();
						}
					}
				
				});
			})
	    }

		//关闭窗口
		function cancel(){
			api.close();
		}
		function beforeClick(treeId, treeNode) {
			var check = (treeNode && !treeNode.isParent);
			if (!check) alert("请选择目录...");
			return check;
		}
		
		function onClick(e, treeId, treeNode) {
			var zTree = $.fn.zTree.getZTreeObj("treeDemo"),
			nodes = zTree.getSelectedNodes(),
			v = "";
			var id = "";
			nodes.sort(function compare(a,b){return a.id-b.id;});
			for (var i=0, l=nodes.length; i<l; i++) {
				v += nodes[i].name + ",";
				id+= nodes[i].id + ",";
			}
			if (v.length > 0 ) v = v.substring(0, v.length-1);
			if (id.length > 0 ) id = id.substring(0, id.length-1);
			var cityObj = $("#citySel");
			cityObj.attr("value", v);
			$("#groupid").val(id); 
			hideMenu();
		}
		function showMenu() {
			var cityObj = $("#citySel"); 
			var cityOffset = $("#citySel").offset();
			$("#menuContent").css({left:cityOffset.left + "px", top:cityOffset.top + cityObj.outerHeight() + "px"}).slideDown("fast");
 
			$("body").bind("mousedown", onBodyDown);
			return false;
		}
		function hideMenu() {
			$("#menuContent").fadeOut("fast");
			$("body").unbind("mousedown", onBodyDown);
		}
		function onBodyDown(event) {
			if (!(event.target.id == "menuBtn" || event.target.id == "menuContent" || $(event.target).parents("#menuContent").length>0)) {
				hideMenu();
			}
		}
		
		function reload(){
			W.reload();
		}