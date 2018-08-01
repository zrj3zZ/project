$().ready(function() {
	loadSetting();
})

function loadSetting(){
	var meettype = $("input[name='GSLX']:checked").val();
	if(meettype=='分公司'){
		$("#itemTr_1246").hide();
		$("#itemTr_1247").hide();
		$("#itemTr_1249").hide();
		$("itemTr_1250").hide();
	}else{
		$("#itemTr_1246").show();
		$("#itemTr_1247").show();
		$("#itemTr_1249").show();
		$("#itemTr_1250").show();
	}
}
