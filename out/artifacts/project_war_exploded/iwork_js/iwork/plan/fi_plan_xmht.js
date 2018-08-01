
/**
 * 
 * @param id 数据字典ID
 * @param rd 行数据
 * @return
 */
function execDictionarySel(id,rd){
	var htbh = rd.HTBH;
	var xmbh = rd.XMBH;
	var htje = rd.HTJE;
	
	$("#data_HTMC").html(rd.HTMC);
	$("#HTMC").val(rd.HTMC);
	$("#data_XMBH").html(rd.XMBH);
	$("#XMBH").val(rd.XMBH);
	$("#data_XMMC").html(rd.XMMC);
	$("#XMMC").val(rd.XMMC);
	$("#data_HTJE").html(rd.HT_XMJE);
	$("#HTJE").val(rd.HT_XMJE);
	
	var pageurl = "iwork_fi_plan_getHtZXInfo.action";
	$.post(pageurl,{htbh:htbh,xmbh:xmbh,htje:htje},function(json){
		var proInfo = JSON.parse(json);
		$("#data_HTWFJE").text(proInfo.HTWFJE);
		$("#HTWFJE").val(proInfo.HTWFJE);
		$("#data_LJZXJE").text(proInfo.LJZXJE);
		$("#LJZXJE").val(proInfo.LJZXJE);
		$("#data_BNYZXJE").text(proInfo.BNYZXJE);
		$("#BNYZXJE").val(proInfo.BNYZXJE);
		$("#data_NDFKZB").text(proInfo.NDFKZB);
		$("#NDFKZB").val(proInfo.NDFKZB);
	});
	/*
	$.getJSON(pageUrl,{htbh:htbh,xmbh:xmbh,htje:htje}, function(result) {
		alert("11111");
		$.each(result, function(i, field){ 
			$("#HTWFJE").val(field.HTWFJE);
			// $("#SKZH").val(field.YXZH);
			// $("#SKR").val(field.USERNAME);
		});
	});*/
	
}