
/**
 * 打印技改报销封面
 */
function printJGBXFM() { 
    var instanceId = $("#instanceId").val();  
    var actDefId = $("#actDefId").val();  
    var url = 'sanbu_JGBXFM_print.action?actDefId=' + actDefId + '&instanceId=' + instanceId;  
    var target = "_blank";  
    var win_width = window.screen.width;  
    var page = window.open(url, target, 'width=900,height=600,top=20,left=250,location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=yes');  
    page.location = url;  
} 
/**
 * 打印技改市内差旅费报销申请单
 * @return
 */
function printJGSNCLFBill() { 
    var instanceId = $("#instanceId").val();  
    var actDefId = $("#actDefId").val();  
    var url = 'sanbu_JGSNCLBX_print.action?actDefId=' + actDefId + '&instanceId=' + instanceId;  
    var target = "_blank";  
    var win_width = window.screen.width;  
    var page = window.open(url, target, 'width=900,height=600,top=20,left=250,location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=yes');  
    page.location = url;  
} 
/**
 * 打印技改差旅报销凭证
 * */
function printJGCLBXPZ() {
    var instanceId = $("#instanceId").val();  
    var actDefId = $("#actDefId").val();  
    var url = 'sanbu_JGCLBX_print.action?actDefId=' + actDefId + '&instanceId=' + instanceId;  
    var target = "_blank";  
    var win_width = window.screen.width;  
    var page = window.open(url, target, 'width=900,height=600,top=20,left=250,location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=yes');  
    page.location = url;  
} 
/**
 * 打印技改借款申请单
 * */
function printJgJkBill() {
	 var instanceId = $("#instanceId").val();  
	 var actDefId = $("#actDefId").val();  
	 var url = 'sanbu_printJGJKSQD_print.action?actDefId=' + actDefId + '&instanceId=' + instanceId;  
	 var target = "_blank";  
	 var win_width = window.screen.width;  
	 var page = window.open(url, target, 'width=900,height=600,top=20,left=250,location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=yes');  
	    page.location = url;
}


/**
 * 打印借款单 
 * @return
 */
function printJkBill() {  
    var instanceId = $("#instanceId").val();  
    var actDefId = $("#actDefId").val();  
    var url = 'sanbu_jiekuan_print.action?actDefId=' + actDefId + '&instanceId=' + instanceId;  
    var target = "_blank";  
    var win_width = window.screen.width;  
    var page = window.open(url, target, 'width=900,height=600,top=20,left=250,location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=yes');  
    page.location = url;  
}  
/**
 * 打印借款单 
 * @return
 */
function printHkBill() {  
    var instanceId = $("#instanceId").val();  
    var actDefId = $("#actDefId").val();  
    var url = 'sanbu_huikuan_print.action?actDefId=' + actDefId + '&instanceId=' + instanceId;  
    var target = "_blank";  
    var win_width = window.screen.width;  
    var page = window.open(url, target, 'width=900,height=600,top=20,left=250,location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=yes');  
    page.location = url;  
}  
/**
 * 打印合同审批单 
 * @return
 */
function printHtAuditBill() {  
    var instanceId = $("#instanceId").val();  
    var actDefId = $("#actDefId").val();  
    var url = 'show_contact_audit.action?actDefId=' + actDefId + '&instanceId=' + instanceId;  
    var target = "_blank";  
    var win_width = window.screen.width;  
    var page = window.open(url, target, 'width='+win_width+',height=600,top=20,left=250,location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=yes');  
    page.location = url;  
}


/**
 * 打印开发票申请单
 * @return
 */
function printKfbBill() {  
    var instanceId = $("#instanceId").val();  
    var actDefId = $("#actDefId").val();  
    var url = 'sanbu_kfb_print.action?actDefId=' + actDefId + '&instanceId=' + instanceId;  
    var target = "_blank";  
    var win_width = window.screen.width;  
    var page = window.open(url, target, 'width=900,height=600,top=20,left=250,location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=yes');  
    page.location = url;  
}  


/**
 * 打印开发票申请单
 * @return
 */
function printKfbptBill() {  
    var instanceId = $("#instanceId").val();  
    var actDefId = $("#actDefId").val();  
    var url = 'sanbu_kfb_pt_print.action?actDefId=' + actDefId + '&instanceId=' + instanceId;  
    var target = "_blank";  
    var win_width = window.screen.width;  
    var page = window.open(url, target, 'width=900,height=600,top=20,left=250,location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=yes');  
    page.location = url;  
} 

/**
 * 打印市内差旅费报销申请单
 * @return
 */
function printSNCLFBill() {  
    var instanceId = $("#instanceId").val();  
    var actDefId = $("#actDefId").val();  
    var url = 'sanbu_CLBX_print.action?actDefId=' + actDefId + '&instanceId=' + instanceId;  
    var target = "_blank";  
    var win_width = window.screen.width;  
    var page = window.open(url, target, 'width=900,height=600,top=20,left=250,location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=yes');  
    page.location = url;  
} 
/**
 * 报销封面
 */
function printBXFM() {  
    var instanceId = $("#instanceId").val();  
    var actDefId = $("#actDefId").val();  
    var url = 'sanbu_BXFM_print.action?actDefId=' + actDefId + '&instanceId=' + instanceId;  
    var target = "_blank";  
    var win_width = window.screen.width;  
    var page = window.open(url, target, 'width=900,height=600,top=20,left=250,location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=yes');  
    page.location = url;  
} 

/**
 * 差旅报销打印
 * */
function printCLBXPZ() {  
    var instanceId = $("#instanceId").val();  
    var actDefId = $("#actDefId").val();  
    var url = 'BXPZ.action?actDefId=' + actDefId + '&instanceId=' + instanceId;  
    var target = "_blank";  
    var win_width = window.screen.width;  
    var page = window.open(url, target, 'width=900,height=600,top=20,left=250,location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=yes');  
    page.location = url;  
} 


/**
 * 打印汇款单
 * */
function printHuiKuanBill() {
    var instanceId = $("#instanceId").val(); 
    var actDefId = $("#actDefId").val();   
    var url = 'sanbu_HuiKuanDan_print.action?actDefId=' + actDefId + '&instanceId=' + instanceId;  
    var target = "_blank";  
    var win_width = window.screen.width;  
    var page = window.open(url, target, 'width=900,height=600,top=20,left=250,location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=yes');  
    page.location = url; 
} 

/**
 * 打印试验伙食费单
 * */
function printSyhsfBill() {
    var instanceId = $("#instanceId").val(); 
    var actDefId = $("#actDefId").val();   
    var url = 'sanbu_syhsf_print.action?actDefId=' + actDefId + '&instanceId=' + instanceId;  
    var target = "_blank";  
    var win_width = window.screen.width;  
    var page = window.open(url, target, 'width=900,height=600,top=20,left=250,location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=yes');  
    page.location = url; 
} 
