var api = art.dialog.open.api, W = api.opener;
function menuLayoutEdit(){
	document.menuLayoutEditForm.submit();
}
function closeWin(){
	parent.$('#menuLayoutEdit').window('close');
}
function checkMenuLayoutUpdated(){
	var flag = document.getElementById('menuLayoutUpdateFlag').value;
	if('true'==flag){
		if(confirm("更新菜单布局成功,是否刷新当前页面")){
			parent.location.reload();
		}
	}
}
function closeWin(){
	api.close();
}