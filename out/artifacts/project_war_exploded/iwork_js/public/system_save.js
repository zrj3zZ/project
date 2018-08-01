var p=12;
var isBreakAuto=0;
function onFocus(){
		setInterval("showProcess()",150);
	}
function showProcess(){
	if(isBreakAuto==0){
		p--;
		//alert('');
		if(p<1){
			isBreakAuto=1;
			var api = frameElement.api;
            api.close();
			//parent.location.reload();
			//parent.parent.GB_hide();
		}else{
			try{
				showProcessTitle.innerText=showProcessTitle.innerText+"》";
			}catch(e){}
		}
	}
} 