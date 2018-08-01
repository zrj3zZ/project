	var api,W;
	var mainFormValidator;
	$(function(){
		try{
			api = art.dialog.open.api;
			W = api.opener;
		}catch(e){}
		 mainFormValidator = $("#subformMain").validate({
			debug:false,
			errorPlacement: function (error, element) {
	               error.appendTo(element.parent());    //将错误信息添加当前元素的父结点后面
			   },
			submitHandler:function(form){
//				//无刷新的提交方式
//				$('#subformMain').ajaxSubmit(submitOption);
				return false;
			} 
		});
	});
	 mainFormValidator.resetForm();	
//弹出提示信息，timeLimit大于0时，限时提醒，提醒信息只保留timeLimit
function showMsg(msg,timeLimit){
			if(timeLimit>0){
				lhgdialog.tips(msg,2);
			}else{ 
				art.dialog({
				    id: 'testID',
				    content:msg,
				    button: [
				        {
				            name: '确定',
				            callback: function () {
				            },
				            focus: true
				        },
				        {
				            name: '查看详情',
				            callback: function () {
				        	showSysTips(); 
				            }
				        }
				    ]
				});
			}
			  
		}
function saveForm(){
	var valid = mainFormValidator.form(); //执行校验操作
	if(!valid){
			return;
	}else{
		try{
			//调用页面保存前脚本事件
			var flag = saveBeforeEvent();
			if(!flag){
				return ;
			}
		}catch(e){}
		$.post('subformSaveItem.action',$("#subformMain").serialize(),function(data){
			if(data=="true"){ 
				var subformkey = $("#subformkey").val();
				var subformId = "subform"+subformkey;
				art.dialog.tips("保存成功",2);
				parent.$('#'+subformId).trigger('reloadGrid'); 
				//回调页面方法
				try{
					var subformkey = $("#subformkey").val();
					parent.saveSubFormItemTrigger(subformkey);
				}catch(e){
				}
				pageClose();
			}else{
				art.dialog.tips("保存异常",2);
				showSysTips(); 
			}
		
			//pageClose(); 
		});
	}
} 
//判断页面元素是否修改
function is_form_changed(){  
			var is_changed =false;
			$("input[id!=''][name!=''],textarea[id!=''][name!=''],select[id!=''][name!='']").each(function() {
				//只校验为radio元素和被选中的radio无素，因为radio元素有相同的id
				if($(this).attr('type')!='radio'||($(this).attr('type')=='radio'&&$(this).attr('checked'))){
					var id = $(this).attr('id');
					//过滤id为jqg_和id_g等弹出子表带来的新页面元素
					if(id.indexOf('jqg_')<0&&id.indexOf('id_g')<0){
						var _v = $(this).attr('_value');
						//如果页面元素的‘value’值不存在，说明页面初始化没有该元素，不做比较（可能是弹出子表带入的新页面元素）
						if(typeof(_v) !='undefined'&&_v != $(this).val()){
							is_changed =true;
						}
					}
				}
			});
			return is_changed;
		}
window.onbeforeunload =function(){
			if(is_form_changed()){
				return '页面有修改未保存，是否关闭页面?'; 
			}
		}
//设置页面菜单布局
function setMenuLayout(){
	var pageUrl =  'menuLayoutEdit.action';
	art.dialog.open(pageUrl,{
		id:"menuLayout",
		title: '菜单布局',
		lock:true,
		background: '#999', // 背景色
	    opacity: 0.87,	// 透明度
	    width: 400,
		height: 350
	 });	
}
function showlog(){
	var instanceId = $("#instanceId").val();
	var modelId = $("#modelId").val();
	var formid = $("#formid").val(); 
	var pageUrl = "sysForm_showlog.action?formid="+formid+"&modelId="+modelId+"&instanceId="+instanceId;
	art.dialog.open(pageUrl,{
		id:'updatelogDialog',
		title:"更新日志",
		lock:true,
		background: '#999', // 背景色
	    opacity: 0.87,	// 透明度
	    width:800,
		height:520 
	 });	
}

//打开数据字典
function openDictionary(uuid,subformkey,subformid){
	var pageUrl = "sys_dictionary_runtime_show.action?dictionaryUUID="+uuid+"&subformkey="+subformkey+"&subformid="+subformid;
	art.dialog.open(pageUrl,{
		id:'DictionaryDialog',
		title:"数据选择",
		lock:true,
		background: '#999', // 背景色
	    opacity: 0.87,	// 透明度
	    width:800,
		height:520 
	 });
}

function previewPic(pageUrl){
	art.dialog.open(pageUrl,{
		id:'previewPic',
		title:"图片预览",
		lock:true,
		background: '#999', // 背景色
	    opacity: 0.87,	// 透明度
	    width:1000,
		height:600
	 });
}

function pageClose(){ 
//	try{ 
//		api.close(); 
//	}catch(e){
//		window.close();
//	} 
	if(typeof(api) =="undefined"){
		window.close();
	}else{ 
		api.close(); 
	}
}
