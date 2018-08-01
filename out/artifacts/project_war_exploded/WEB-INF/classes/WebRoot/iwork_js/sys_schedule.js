// 保存任务计划
function createTask(formObj,cmd){
		formObj.target='_self';
		if(!checkInput()){
			return false;
		}else{
			formObj.cmd.value = cmd;
			formObj.submit();			
		}
}

// 检测输入交验
function checkInput(){
		if(!checkNull(document.all.planname,"计划任务名称必填!")) return false;
		if(!checkNull(document.all.classz,"任务执行类必填!")) return false;
		var key = getRuleType();
		if(key != "5"){    // 除了AWS启动时规则其他都校验小时，分钟
			if(!checkNull(document.all.hour,"小时必填!")) return false;
			if(!checkNull(document.all.minute,"分钟必填!")) return false;
		}
		
		if (!checkvalue("计划任务名称",document.all.planname.value)){
			return false;
		} 
		if (!checkvalue("任务执行类",document.all.classz.value)){
			return false;
		}
		if (!checkvalue("任务描述",document.all.plandsc.value)){
			return false;
		}

		if(key=='1' && !checkDayStr(document.all.dayOfWeekStr,'每周天',1,7)){
			return false;
		}
		if(key=='2' && !checkDayStr(document.all.dayOfMonthStr,'每月天',1,31)){
			return false;	
		}
		if(!checkStartAndEndTime()){
			return false;
		}
		return true;
}
// 取得频率规则类型
function getRuleType(){
		var ruleTypes = document.all.cycle;
		var key;
		for(var i=0;i<ruleTypes.length; i++){
			if(ruleTypes[i].checked == true){
				key = ruleTypes[i].value;
				
				break;
			}
		}	
		return key;
}
function checkNull(obj,msg){
	if(obj.value == ""){
		alert(msg);
		obj.focus();
		return false;
	}else{
		return true;	
	}
}
function checkStartAndEndTime(){
	var start = $('#usefulLife_start').datebox('getValue');	
	var end = $('#usefulLife_end').datebox('getValue');	
	if(start!=""&& end!=""){
		var s = 10000*start.substring(0,4)+100*start.substring(5,7)+1*start.substring(8,10);
		var e = 10000*end.substring(0,4)+100*end.substring(5,7)+1*end.substring(8,10);
		if(s>=e){
			alert("有效期的开始时间要小于结束时间!");
			return false;
		}
	}
	return true;
}
function checkDayStr(obj,text,minNum,maxNum){
		var numbers = obj.value.split(",");
		var mn = 0+minNum;
		var mx = 0+maxNum;
		for(var i=0;i<numbers.length; i++){
			// 测试整数
			var num = parseInt(numbers[i]);
			if(!checkNanN(num)){
				alert(text+"必须为逗号分割的数字!");
				return false;
			}
			if(num<minNum || num > maxNum){
				alert("每周天数字必须在"+minNum+"到"+maxNum+"之内!");
				return false;
			}
		}
		if(!checkSort(numbers)){
			alert("请从小到大录入!");	
			return false;
		}
		return true;
}
// 检测数字
function checkNanN(x){
	if(!isNaN(x)){
		return true;
	}else{
		return false;
	}
}

// 检测排序,是否从小到大排列
function checkSort(arryObj){
	var mn = -1;
	var mx;
	for(var i=0;i<arryObj.length; i++){
		 if(mn<parseInt(arryObj[i])){
			mn = parseInt(arryObj[i]);
		 }else{
			return false;	 
		 }
	}
	return true;
}
// 测试类有效性
function testClass(classz){
	if(classz == ""){
		alert("执行类不能为空！");
		document.forms[0].classz.focus();
		return false;
	}
	var url = 'sys_schedule_testClass.action';
	document.forms[0].classz.value = classz;
	document.forms[0].action = url;
	document.forms[0].submit();
 	return false;  	
}

//解析ajax 返回的结果
/*
function process(){ 
	if (aws_ajax1_http_request.readyState == 4) { // 判断对象状态
		if (aws_ajax1_http_request.status == 200) { // 信息已经成功返回，开始处理信息
//			var xmlobj = aws_ajax1_loadXML(aws_ajax1_http_request.responseText);
			if(aws_ajax1_http_request.responseText == "0"){
				document.all.messageDiv.innerHTML = "<span style=color:#FF0000>任务执行类不存在!</span>"
			}
			if(aws_ajax1_http_request.responseText == "1"){
				document.all.messageDiv.innerHTML = "<span style=color:#FF0000>任务执行类存在,但没有实现AWSScheduleInterface用户接口！</span>"
			}
			if(aws_ajax1_http_request.responseText == "2"){
				document.all.messageDiv.innerHTML = "<span style=color:#FF0000>任务执行类测试通过.</span>"
			}

		} 
	}
}*/
//process

// 清除测试的信息
function clearMsg(){
	document.all.messageDiv.innerHTML = "";
}

function goBack(){
	//http://localhost:8088/portal/workflow/login.wf?sid=admin_1192445337998_127.0.0.1&cmd=AWS_Sys_Schedule_List
	var sid = document.all.sid.value;
	window.location.href = "./login.wf?sid="+sid+"&cmd=AWS_Sys_Schedule_List";
}

 
// 设置radio
function setRadioChecked(checkValue){
	var ruleTypeArray = document.all.cycle;
	for(var i=0; i<ruleTypeArray.length; i++){
		if(ruleTypeArray[i].value == checkValue){
			ruleTypeArray[i].checked = true;
		}else{
			ruleTypeArray[i].checked = false;
		}
	}
}
// 显示层
function showCycleDiv(key){
	switch(key){
		case '0':  {
			$("#dayDiv").show();
			$("#weekDiv").hide();
			$("#monthDiv").hide();
			$("#quarterDiv").hide();
			$("#yearDiv").hide();
			$("#awsDiv").hide();
			$("#hhmmssDiv").show();
			break;
		}
		case '1':  {
	
			$("#dayDiv").hide();
			$("#weekDiv").show();
			$("#monthDiv").hide();
			$("#quarterDiv").hide();
			$("#yearDiv").hide();
			$("#awsDiv").hide();
			$("#hhmmssDiv").show();
			break;
		}
		case '2':  {
			$("#dayDiv").hide();
			$("#weekDiv").hide();
			$("#monthDiv").show();
			$("#quarterDiv").hide();
			$("#yearDiv").hide();
			$("#awsDiv").hide();
			$("#hhmmssDiv").show();
			break;
		}
		case '3':  {
			$("#dayDiv").hide();
			$("#weekDiv").hide();
			$("#monthDiv").hide();
			$("#quarterDiv").show();
			$("#yearDiv").hide();
			$("#awsDiv").hide();
			$("#hhmmssDiv").show();
			break;
		}
		case '4':  {
			$("#dayDiv").hide();
			$("#weekDiv").hide();
			$("#monthDiv").hide();
			$("#quarterDiv").hide();
			$("#yearDiv").show();
			$("#awsDiv").hide();
			$("#hhmmssDiv").show();
			break;
		}
		case '5':  {
			$("#dayDiv").hide();
			$("#weekDiv").hide();
			$("#monthDiv").hide();
			$("#quarterDiv").hide();
			$("#yearDiv").hide();
			$("#awsDiv").show();
			$("#hhmmssDiv").hide();	
			break;
		}
	}
}

//全选/反向全选
function checkAllItem(){
//	var checkFlag = event.srcElement.checked;
	var checkFlag = document.all.checkAllKey.value;
	if(checkFlag == 'false'){
		document.all.checkAllKey.value = 'true';
	}else if(checkFlag == 'true'){
		document.all.checkAllKey.value = 'false';
	}
	var checkboxArray = document.all.checkItem;
	if(checkboxArray.length== undefined){
		if(checkFlag == "false"){checkboxArray.checked=false;}
		else { checkboxArray.checked=true;}
		
	}else{
		for(var i=0; i<checkboxArray.length; i++){
//			checkboxArray[i].checked=checkFlag;
			if(checkFlag == "false"){checkboxArray[i].checked=false;}
			else { checkboxArray[i].checked=true;}
		}
	}
}
function removeTask(formObj,cmd){
	var tasks = findCheckedScheduleIDs2Del();
	if(tasks == false || tasks == ""){
		alert("请选择要删除的任务!");
	}else{
		//alert(tasks + cmd + formName);
		if ( false==confirm("确认要删除这个任务吗？") ){
			return false;
		} 
		tasks = tasks.substring(0,tasks.length-1);
		var oldCmd = formObj.cmd.value ;
		formObj.cmd.value = cmd;
		formObj.tasks.value = tasks;
		formObj.target = '_self';
		formObj.submit();
	}
	
}
// 查找所有选中的任务ID
function findCheckedScheduleIDs2Del(){
	var checkboxArray = document.all.checkItem;
//	if(checkboxArray==undefined)return "";
	if(checkboxArray.length == undefined){
		if(checkboxArray.checked == true ){
			if(checkboxArray.isSystem=='0'){
				return checkboxArray.schedueID+",";
			}else{
				alert(checkboxArray.planName+"是系统级任务，不能被删除！");
				return false;
			}
		}
	}else{
		var systasks = "";
		var returnTasks = "";
		for(var i=0; i<checkboxArray.length; i++){
			if(checkboxArray[i].checked == true){
				if(checkboxArray[i].isSystem == '0'){
					returnTasks += checkboxArray[i].schedueID +",";
				}else{
					systasks += checkboxArray[i].planName +" ";
				}				
			}
		}
		if(systasks != ""){
			if(confirm(systasks+"是系统级任务，不能被删除！")){
				return returnTasks;
			}		
		}else{
			return returnTasks;
		}
		return false;
	}
}
// 跳转到创建计划任务录入界面
function open2Task(formObj,cmd){
		var oldCmd = formObj.cmd.value ;
		formObj.cmd.value = cmd;
		formObj.target = '_self';
		formObj.submit();
}

function disableTask(flag,id){
		var msg = "";
		if(flag == "0"){
			flag="1";
			msg =  "启用该任务？";
		}else if(flag == "1"){
			flag="0";
			msg =  "停用该任务？";
		}
		var oldCmd = document.forms[0].id.value;
		document.forms[0].id.value=id;
		document.forms[0].flag.value=flag;
		var url='sys_schedule_changeStatus.action';
		document.forms[0].action=url;
		if(confirm(msg)){
			document.forms[0].submit();
		}
}
//察看详细信息
function viewTaskInf(taskID){
		var url = "./login.wf?sid="+document.all.sid.value+"&cmd=AWS_Sys_Schedule_Open&planId="+taskID;
		window.location.href = url;
}

// 模拟使用
function simulate(disable,id){
		document.forms[0].id.value=id;
		document.forms[0].flag.value=disable;
		if (1== disable) {
			var url='sys_schedule_simulate.action';
			document.forms[0].action=url;
			document.forms[0].submit();
		} else {
			alert("禁用的任务不能被执行,请启用后再执行");
		}
		
}
// 察看日志
function viewTaskLog(frmMain,cmd){
	var tasks = findCheckedScheduleIDs2Del();
	if(tasks == false || tasks == "" || tasks==undefined){
		alert("请选择要查看的任务!");
	}else{
		tasks = tasks.substring(0,tasks.length-1);
		var url = "./login.wf?sid="+document.all.sid.value+"&cmd="+cmd+"&planID="+tasks;
		window.location.href = url;
	}
}