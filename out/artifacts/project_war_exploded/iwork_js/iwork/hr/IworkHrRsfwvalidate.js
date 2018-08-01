//add by Wangjh 2014-4-30

//页面加载事件
$(function() {
	var taskType = iformMain.taskType.value;
	var value ;
	if(taskType==1){
		value = iformMain.SQLX.value;
	}else{
		value = document.getElementById("SQLX").options[document.getElementById("SQLX").selectedIndex].text;
	}
	
		applyType();
		if(value=="申请提取住房公积金"){
			
			extractType();
			arriveType();
		}
});

function applyType(){
	var taskType = iformMain.taskType.value;
	var value ;
	//判断是否为新建状态 为1非新建  为0新建
	if(taskType==1){
		value = iformMain.SQLX.value;
	}else{
		value = document.getElementById("SQLX").options[document.getElementById("SQLX").selectedIndex].text;
	}
	if(value=='收入证明'){
		$('#type_1').show();
		$('#type_2').hide();
		$('#type_3').hide();
		$('#type_4').hide();
		$('#type_5').hide();
		$('#type_6').hide();
		$('#type_7').hide();
		$('#type_8').hide();
		$('#itemTr_1336').show();//申请内容
		$('#itemTr_1337').show();//附件
		$('#itemTr_1338').hide();//提取类型
		$('#itemTr_1339').hide();//到账类型
		$('#itemTr_1340').hide();//提取时间
		$('#itemTr_1341').hide();//房屋具体位置
		$('#flag_1').hide();
		$('#flag_2').hide();
		$('#flag_3').hide();
		nonMandatory();
	}else if(value=='在职证明'){
		$('#type_1').hide();
		$('#type_2').show();
		$('#type_3').hide();
		$('#type_4').hide();
		$('#type_5').hide();
		$('#type_6').hide();
		$('#type_7').hide();
		$('#type_8').hide();
		$('#itemTr_1336').show();
		$('#itemTr_1337').show();
		$('#itemTr_1338').hide();
		$('#itemTr_1339').hide();
		$('#itemTr_1340').hide();
		$('#itemTr_1341').hide();
		$('#flag_1').hide();
		$('#flag_2').hide();
		$('#flag_3').hide();
		nonMandatory();
	}else if(value=='介绍信'){
		$('#type_1').hide();
		$('#type_2').hide();
		$('#type_3').show();
		$('#type_4').hide();
		$('#type_5').hide();
		$('#type_6').hide();
		$('#type_7').hide();
		$('#type_8').hide();
		$('#itemTr_1336').show();
		$('#itemTr_1337').show();
		$('#itemTr_1338').hide();
		$('#itemTr_1339').hide();
		$('#itemTr_1340').hide();
		$('#itemTr_1341').hide();
		$('#flag_1').hide();
		$('#flag_2').hide();
		$('#flag_3').hide();
		nonMandatory();
	}else if(value=='实习证明'){
		$('#type_1').hide();
		$('#type_2').hide();
		$('#type_3').hide();
		$('#type_4').show();
		$('#type_5').hide();
		$('#type_6').hide();
		$('#type_7').hide();
		$('#type_8').hide();
		$('#itemTr_1336').show();
		$('#itemTr_1337').show();
		$('#itemTr_1338').hide();
		$('#itemTr_1339').hide();
		$('#itemTr_1340').hide();
		$('#itemTr_1341').hide();
		$('#flag_1').hide();
		$('#flag_2').hide();
		$('#flag_3').hide();
		nonMandatory();
	}else if(value=='初婚未育证明'){
		$('#type_1').hide();
		$('#type_2').hide();
		$('#type_3').hide();
		$('#type_4').hide();
		$('#type_5').show();
		$('#type_6').hide();
		$('#type_7').hide();
		$('#type_8').hide();
		$('#itemTr_1336').show();
		$('#itemTr_1337').show();
		$('#itemTr_1338').hide();
		$('#itemTr_1339').hide();
		$('#itemTr_1340').hide();
		$('#itemTr_1341').hide();
		$('#flag_1').hide();
		$('#flag_2').hide();
		$('#flag_3').hide();
		nonMandatory();
	}else if(value=='其他证明'){
		$('#type_1').hide();
		$('#type_2').hide();
		$('#type_3').hide();
		$('#type_4').hide();
		$('#type_5').hide();
		$('#type_6').show();
		$('#type_7').hide();
		$('#type_8').hide();
		$('#itemTr_1336').show();
		$('#itemTr_1337').show();
		$('#itemTr_1338').hide();
		$('#itemTr_1339').hide();
		$('#itemTr_1340').hide();
		$('#itemTr_1341').hide();
		$('#flag_1').hide();
		$('#flag_2').hide();
		$('#flag_3').hide();
		
		nonMandatory();
	}else if(value=='修改医保定点医院'){
		$('#type_1').hide();
		$('#type_2').hide();
		$('#type_3').hide();
		$('#type_4').hide();
		$('#type_5').hide();
		$('#type_6').hide();
		$('#type_7').show();
		$('#type_8').hide();
		$('#itemTr_1336').show();
		$('#itemTr_1337').show();
		$('#itemTr_1338').hide();
		$('#itemTr_1339').hide();
		$('#itemTr_1340').hide();
		$('#itemTr_1341').hide();
		$('#flag_1').hide();
		$('#flag_2').hide();
		$('#flag_3').hide();
		nonMandatory();
	}else if(value=='申请提取住房公积金'){
		$('#type_1').hide();
		$('#type_2').hide();
		$('#type_3').hide();
		$('#type_4').hide();
		$('#type_5').hide();
		$('#type_6').hide();
		$('#type_7').hide();
		$('#type_8').show();
		$('#itemTr_1336').hide();
		$('#itemTr_1337').hide();
		$('#itemTr_1338').show();
		$('#itemTr_1339').show();
		$('#itemTr_1340').show();
		$('#itemTr_1341').show();
		mandatory();
		extractType();
	}else{
		$('#type_1').hide();
		$('#type_2').hide();
		$('#type_3').hide();
		$('#type_4').hide();
		$('#type_5').hide();
		$('#type_6').hide();
		$('#type_7').hide();
		$('#type_8').hide();
		$('#itemTr_1336').hide();
		$('#itemTr_1337').hide();
		$('#itemTr_1338').hide();
		$('#itemTr_1339').hide();
		$('#itemTr_1340').hide();
		$('#itemTr_1341').hide();
		$('#flag_1').hide();
		$('#flag_2').hide();
		$('#flag_3').hide();
		nonMandatory();
	}
	
}


//提取类型
function extractType(){
	
	var tqlx = document.getElementsByName("TQLX"); 
	var value;
	for(var i=0;i<tqlx.length;i++){
		if(tqlx[i].checked){
			value = tqlx[i].value;
		}
	}
	if(value=="二手房提取"){
		$('#flag_1').show();
		$('#flag_2').hide();
		$('#flag_3').hide();
		resetCh();//重置
		$('#ESF1-0').rules("add",{ required: true});
		$('#ESF2-0').rules("add",{ required: true});
		$('#ESF3-0').rules("add",{ required: true});
		
	}else if(value=="期房/现房提取"){
		$('#flag_1').hide();
		$('#flag_2').show();
		$('#flag_3').hide();
		resetCh();//重置
		$('#QFXF1-0').rules("add",{ required: true});
		$('#QFXF2-0').rules("add",{ required: true});
		$('#QFXF3-0').rules("add",{ required: true});
		$('#QFXF4-0').rules("add",{ required: true});
	}else if(value=="租房提取"){
		$('#flag_1').hide();
		$('#flag_2').hide();
		$('#flag_3').show();
		resetCh();//重置
		$('#ZF1-0').rules("add",{ required: true});
		$('#ZF2-0').rules("add",{ required: true});
		$('#ZF3-0').rules("add",{ required: true});
		$('#ZF4-0').rules("add",{ required: true});
		$('#ZF5-0').rules("add",{ required: true});
	}else{
		$('#flag_1').hide();
		$('#flag_2').hide();
		$('#flag_3').hide();
	}
}

//到帐类型
function arriveType(){
	var dzlx = document.getElementsByName("DZLX"); 
	var value;
	for(var i=0;i<dzlx.length;i++){
		if(dzlx[i].checked){
			value = dzlx[i].value;
		}
	}

	if(value=="工资卡卡号"){
		document.getElementById("data_YXKH").style.display = ''; 
		$("input[name='TQSJ']").rules("remove","required");
		document.getElementById("title_TQSJ").style.display = 'none';
		document.getElementById("data_TQSJ").style.display = 'none';
	}else{
		document.getElementById("data_YXKH").style.display = '';
		document.getElementById("data_TQSJ").style.display = '';
		document.getElementById("title_TQSJ").style.display = '';
		$("input[name='TQSJ']").rules("add",{ required: true});
	}
}

//检查手机号
function checkMobile(){
	var str = iformMain.SJHM.value;
	if(str!=null && str!=""){
		var reg = /(^0{0,1}1[3|4|5|6|7|8|9][0-9]{9}$)/;
		if(!reg.exec(str)){
			alert("请填写正确的手机号");
			iformMain.SJHM.value='';
//			var isChrome = /chrome/.test(navigator.userAgent.toLowerCase());
//			if (isChrome) {
//				window.setTimeout(function(){document.getElementsByName('SJHM').select;},1000 ); 				
//			}else {
				iformMain.SJHM.focus();
//			}
		}
	}
}

////动态非必填项
function nonMandatory(){
	$('#TQLX0').rules("remove","required");
	$('#TQLX1').rules("remove","required");
	$('#TQLX2').rules("remove","required");
	$('#DZLX1').rules("remove","required");
	$('#DZLX0').rules("remove","required");
	$('#YXKH').rules("remove","required");
	$('#TQSJ0').rules("remove","required");
	$('#TQSJ1').rules("remove","required");
	$('#FWDZ').rules("remove","required");
	resetCh();
}

////动态必填项
function mandatory(){
	$("input[name='TQLX']").rules("add",{ required: true});
	$("input[name='DZLX']").rules("add",{ required: true});
	$("input[name='YXKH']").rules("add",{ required: true});
	$("input[name='TQSJ']").rules("add",{ required: true});
	$("input[name='FWDZ']").rules("add",{ required: true});
}
//重置信息值
function resetCh(){
	$('#QFXF1-0').rules("remove","required");
	$('#QFXF2-0').rules("remove","required");
	$('#QFXF3-0').rules("remove","required");
	$('#QFXF4-0').rules("remove","required");
	$('#ZF1-0').rules("remove","required");
	$('#ZF2-0').rules("remove","required");
	$('#ZF3-0').rules("remove","required");
	$('#ZF4-0').rules("remove","required");
	$('#ZF5-0').rules("remove","required");
	$('#ESF1-0').rules("remove","required");
	$('#ESF2-0').rules("remove","required");
	$('#ESF3-0').rules("remove","required");
}

