function authority_chooser(id) {
	var code = document.getElementById(id).value;	
	var url = "authorityAddressBookAction!index.action?code=" + code;
	var obj = new Object();
	obj.name = id;
	obj.win = window;
	window.showModalDialog(url, obj, 'modal=yes,dialogWidth:650px;dialogHeight:535px;help:no;resizable:no;status:no;location:no');
}

function radio_book(id) {
	var code = document.getElementById(id).value;	
	var url = "radioAddressBookAction!index.action?code=" + code;
	var obj = new Object();
	obj.name = id;
	obj.win = window;
	window.showModalDialog(url, obj, 'modal=yes,dialogWidth:350px;dialogHeight:535px;help:no;resizable:no;status:no;location:no');
}

function multi_book(id) {
	var code = document.getElementById(id).value;	
	var url = "multiAddressBookAction!index.action?code=" + code;	
	var obj = new Object();
	obj.name = id;
	obj.win = window;
	window.showModalDialog(url, obj, 'modal=yes,dialogWidth:650px;dialogHeight:535px;help:no;resizable:no;status:no;location:no');
}