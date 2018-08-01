	var flag=false;
$(document).ready(function checkJYDX()
{
	//alert($("#SXMC").val());
	setTimeout(tsxxSc,100);
	

});
function tsxxSc(){
	var count=jQuery("#subformSUBFORM_JYDX").jqGrid('getGridParam','records');  
	if(count>0){
		for(var i=0;i<count;i++){
			

			var colModel=jQuery("#subformSUBFORM_JYDX").getRowData()[i];
			var jydxbh=colModel.JYDXBH;
			var jydxmc=colModel.JYDXMC;
			$.getJSON("zqb_event_search.action",{jydxbh:jydxbh,jydxmc:jydxmc},function(data){ 
				 $.each(data,function(idx,item){
					var gslx= item.GSLX;
					if(gslx=="分公司"||gslx=="子公司"){
						flag=true;
						$("#tsxx").text("因交易对象是分子公司，请确认该重大事项是否为关联交易!");
					}
				 });
			 }); 
			
		}
		
	}
}


