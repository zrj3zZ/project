$(document).ready(function() {
	var instanceId = $("#instanceId").val();
	// 根据isntanceId判断打开页面是新增还是编辑
		// 如果instanceId是0则为新增，否则为编辑
		if (instanceId != 0) {
			// 编辑页面年份与月份为不可编辑状态
			var year = $('#YEAR').val();
			var month = $('#MONTH').val();
			$("#data_YEAR").hide();
			$("#data_MONTH").hide();
			$("#dataYY").show();
			$("#dataMM").show();
			$("#dataYY").append(year);
			$("#dataMM").append(month);
		}
		choose();
		// insert();
	});
// 控制编号
function insert() {
	var today = new Date();
	var month = parseInt(today.getMonth()) + 1;
	var year = parseInt(today.getFullYear());
	var item = $('#YEAR').val();
	var year1 = parseInt(item);
	var item1 = $('#MONTH').val();
	var month1 = parseInt(item1);
	if (year > item) {
		alert("请输入大于当前年份的时间");
		$("#YEAR").val('');
		return false;
	}else{
		if(year==item){
			if (month1 < month) {
				alert("请输入大于当前月份的时间");
				$("#MONTH").val('');
				return false;
			}
		}
	}
	
	// var item3 = $("select[type=select] option[selected]").val();
	$("#MONTH").val(item1);
	$('#labelYSKZBH').text(item + item1);
	$('#YSKZBH').val(item + item1);
}

jQuery("#MONTH").change(function() {
	var checkText = jQuery("#MONTH:selected").val();
	jQuery("#MONTH").val(checkText);
});
// 控制按钮框
function choose() {
	var item = $("input[name='STATUS']:checked").val();

	// alert(item);
	if (String(item) == '启动编报') {
		// alert(1);
	} else if (String(item) == '编报结束') {
		// alert(2);
		$("#STATUS0").attr("disabled", true);
		// $("input[type=radio]").attr("checked",'月度封账');
		// $("input[name='STATUS'][value='启动编报']").attr("checked",false);

	} else {
		$("#STATUS1").attr("disabled", true);
		$("#STATUS0").attr("disabled", true);
		//alert(3);
		//$("input[type=radio]").attr("checked",'月度封账');
	}
}