	function tr_bgcolor(c){
		var tr = c.parentNode.parentNode;
		tr.rowIndex % 2 == 0 ? tr.style.backgroundColor = c.checked ? '#add6a6' : '#eee' : tr.style.backgroundColor = c.checked ? '#add6d6' : '';
}
function selall(obj){
	for (var i=0; i<obj.form.elements.length; i++)
	if (obj.form.elements[i].type == 'checkbox' && obj.form.elements[i] != obj){
		obj.form.elements[i].checked = obj.checked;
		tr_bgcolor(obj.form.elements[i]);
	}
}

//右键菜单