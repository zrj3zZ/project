//頁面出事化
$(function() {
	var value ;
		value = document.getElementById("STATUS").options[document.getElementById("STATUS").selectedIndex].value;
		applyType();
});

function applyType(){
	var value ;
	value = document.getElementById("STATUS").options[document.getElementById("STATUS").selectedIndex].value;
	//查看状态 0:仅查看人员 1：查看部门下所有人员 2：不可查看人员3：不可查看部门
	if(value==0||value==2){
		$('#itemTr_4').hide();
		$('#itemTr_6').show();
	}else if(value==1||value==3){
		$('#itemTr_4').show();
		$('#itemTr_6').hide();
	}else{
		$('#itemTr_4').show();
		$('#itemTr_6').show();
	}
}