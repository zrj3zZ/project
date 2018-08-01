<!--


function popupShow(msg){
if(msg==''){return false;}
var actionsoftPopupWin = document.getElementById('actionsoftPopupWin');
var content="<table width=280 border=0 cellspacing=0 cellpadding=0><tr><td><img src='app/plugs/resoucebook/images/arror1.gif' width=62 height=21></td></tr><tr><td align=left valign=top><table width=100% border=3 cellpadding=0 cellspacing=0 bordercolor=AEC6D3 bgcolor=FFFFFF><tr><td valign=top align=left><table border=0 width=100% height=100% cellpadding=0 bordercolor=FFFFFF><tr><td>"+msg+"</tr></td></table></td></tr></table></td></tr></table>";
actionsoftPopupWin.innerHTML = content;
var x = mouseX();
var y = mouseY();
actionsoftPopupWin.style.left = x + 'px';
actionsoftPopupWin.style.top = y + 'px';
actionsoftPopupWin.style.visibility='visible';
}

function popupKill(){
document.getElementById('actionsoftPopupWin').style.visibility='hidden';
}

function mouseX() {
	var e = getEvent();
	if (e.pageX){
		return e.pageX;
	}else if (window.event.clientX){
	   return window.event.clientX + (document.documentElement.scrollLeft ?
	   document.documentElement.scrollLeft :
	   document.body.scrollLeft);
	}else {
		return null;
	}
}
function mouseY() {
	var e = getEvent();
	if (e.pageY) {
		return e.pageY;
	}else if (e.clientY){
	   return e.clientY + (document.documentElement.scrollTop ?
	   document.documentElement.scrollTop :
	   document.body.scrollTop);
	}else {
		return null;
	}
}

function getEvent(){  //同时兼容ie和ff的写法
	if(document.all){
		return window.event; 
	}
	func=getEvent.caller;       
	while(func!=null){ 
		var arg0=func.arguments[0];
		if(arg0){
			if((arg0.constructor==Event || arg0.constructor ==MouseEvent) || (typeof(arg0)=="object" && arg0.preventDefault && arg0.stopPropagation)){ 
				return arg0;
			}
		}
		func=func.caller;
	}
	return null;
}

//-->