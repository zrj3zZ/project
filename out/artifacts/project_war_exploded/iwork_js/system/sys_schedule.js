$(function(){
		   //加载系统计划任务列表
			$('#sysschedulegrid').datagrid({
			    loadMsg: '数据加载中，请稍后...',
			    idField:'id',
			    url:'sys_schedule_getList.action',
				fitColumns: true,
				striped: true,
				singleSelect:true,
				nowrap:false,
				rownumbers:true,
				height:400,
				sortName: 'add_time',
				sortOrder: 'desc',
				columns:[[
					{field:'planname',title:'任务名称',width:80},
					{field:'plandesc',title:'说明',width:160},
					{field:'status',title:'状态',width:40},
					{field:'rule_type',title:'执行频率',width:40},
					{field:'add_time',title:'创建日期',width:80,align:'center'},
					{field:'operate',title:'操作',width:120,align:'center'}
				]],
				onBeforeLoad:function(){
					$(this).datagrid('rejectChanges');
				},
				onDblClickRow:function(){
					var row = $('#sysschedulegrid').datagrid('getSelected');
					$('#id').val(row.id==null?'':row.id);
					$('#planName').val(row.planname==null?'':row.planname);//计划名称
					$('#planDesc').val(row.plandesc==null?'':row.plandesc);//计划描述
					$('#planPri').val(row.planPri==null?'':row.planPri);//计划优先级
					
				    $('#usefulLife_start').datebox('setValue',row.usefulLife_start==null?'':row.usefulLife_start);//有效期开始日期
				    $('#usefulLife_end').datebox('setValue',row.usefulLife_end==null?'':row.usefulLife_end);//有效期结束日期
					$('#repeatNum').val(row.repeatNum==null?'':row.repeatNum);//失败补偿次数
					$('#classz').val(row.classz==null?'':row.classz);//执行类
					//执行频率
					var ruleType=row.rule_type_value;
					var radioLen=document.forms[0].ruleType.length;
	        		for(var i=0;i<radioLen;i++){
	            		if(ruleType==document.forms[0].ruleType[i].value){
	                		document.forms[0].ruleType[i].checked=true
	            		}
	        		}
	        		showCycleDiv(ruleType.toString());//显示相应的执行时间
	        		//执行小时
	        		$('#hour').val(row.hour==null?'':row.hour);
	        		//执行分钟
	        		$('#minute').val(row.minute==null?'':row.minute);
	        		//执行点
	        		if (ruleType == '1'){//周
	        			$('#dayOfWeekStr').val(row.execute_point==null?'':row.execute_point);
	        		}
	        		if (ruleType == '2'){//月
	        			$('#dayOfMonthStr').val(row.execute_point==null?'':row.execute_point);
	        		}
	        		if (ruleType == '3'){//季度
	        			var date = row.execute_point.split(",");
	        			$('#monthOfQuarter').val(date[0]);
	        			$('#dayOfQuarterMonth').val(date[1]);
	        		}
	        		if (ruleType == '4'){//年
	        			var date = row.execute_point.split(",");
	        			$('#monthOfYear').val(date[0]);
	        			$('#dayOfYearMonth').val(date[1]);
	        		}
	        		//间隔时间
	        		$('#intervalMinutes').val(row.intervalMinutes==null?'':row.intervalMinutes);
	        		$('#type').val('edit');
					$('#idialog').dialog('open'); 
				}							
			});	 
			
			//加载编辑对话框   
	       $('#idialog').dialog({
	            title:'编辑',
	            modal:true,
				toolbar:[{
					text:'保存',
					iconCls:'icon-save',
					handler:function(){
					
						 if(!$('#iform').form('validate')){
					        
					     }
					     else{
					        var dayOfWeekStr = $('#dayOfWeekStr').val().replace(/(^\s*)|(\s*$)/g, "");//去除前后空格
						 	var dayOfMonthStr = $('#dayOfMonthStr').val().replace(/(^\s*)|(\s*$)/g, "");//去除前后空格
						 	$('#dayOfWeekStr').val(dayOfWeekStr.replace(/\s+/g,","));//将字符中间的多个空格替换为英文逗号
						 	$('#dayOfMonthStr').val(dayOfMonthStr.replace(/\s+/g,","));//将字符中间的多个空格替换为英文逗号
							 if(checkStartAndEndTime()){//检验有效期开始时间、结束时间
						 		 var url='sys_schedule_save.action'; 
							 	document.forms[0].action=url;
					         	document.forms[0].submit();
						 	}
						 
				 		 	return false; 
					     }			
					}
				},{
					text:'取消',
					iconCls:'icon-cancel',
					handler:function(){
						 $('#idialog').dialog('close');
					}
				}]

		    });
	        //隐藏 
	        $('#idialog').dialog('close');  	             
});		

//添加
function add(){

	$('#planName').val('');
	$('#classz').val('');
	$('#planDesc').val('');
	$("input[name='ruleType'][value=0]").attr("checked",true); 
	$('#dayOfWeekStr').val('');
	$('#dayOfMonthStr').val('');
	$('#monthOfQuarter').val('1');
	$('#dayOfQuarterMonth').val('1');
	$('#monthOfYear').val('1');
	$('#dayOfYearMonth').val('1');
	$('#intervalMinutes').val('');
	$('#usefulLife_start').datebox('setValue','');
	$('#usefulLife_end').datebox('setValue','');
	$('#type').val('add');
	showCycleDiv('0');//显示每天执行时间DIV
	$('#idialog').dialog('open');		
}

//删除	
function remove(){
	var row = $('#sysschedulegrid').datagrid('getSelected');
	if (row){
		 $.messager.confirm('确认','确认删除?',function(result){
		 	if(result){
		 		var index = $('#sysschedulegrid').datagrid('getRowIndex', row);
				$('#sysschedulegrid').datagrid('deleteRow', index);	
				$('#id').val(row.id==null?'':row.id);
				$('#type').val('delete');
				var url='sys_schedule_save.action';	
			    document.forms[0].action=url;
			    document.forms[0].submit();
				return false; 
		 	}
		 })
	}	
}
//查看日志
function showlog(){
	var row = $('#sysschedulegrid').datagrid('getSelected');
	if (row){
		var index = $('#sysschedulegrid').datagrid('getRowIndex', row);
		var url='sys_schedule_showLog.action?id='+row.id;
	    window.parent.addTab(row.planname,url,'');
	}
}
