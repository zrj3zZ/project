function menuLayoutEdit(){
	document.menuLayoutEditForm.submit();
}
function checkMenuLayoutUpdated(){
	var flag = document.getElementById('menuLayoutUpdateFlag').value;
	if('true'==flag){
		alert('更新成功！');
	}
}