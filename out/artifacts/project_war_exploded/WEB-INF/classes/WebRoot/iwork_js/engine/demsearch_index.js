function doSubmit(){
			document.forms["searchForm"].action="sysDem_search_add.action";
			document.form[0].submit();
}
function removeItem(){
	 art.dialog.confirm('确认删除?',function(result){  
				 	if(result){
				 		var rows = $('#iformSearch_grid').datagrid('getSelections');
				 		var ids = [];
					 		for(var i=0;i<rows.length;i++){
							ids.push(rows[i].ID);
						}
						$("#searchForm_ids").val(ids.join(','));
						document.forms["searchForm"].action="sysDem_search_remove.action";
						  var options = {
									error:errorFunc,
									success:successFunc 
							   };
							   $('#searchForm').ajaxSubmit(options);
                    }
            });
            return false;
		}
errorFunc=function(){
    alert("移除失败！");
 }
 successFunc=function(responseText, statusText, xhr, $form){
        if(responseText=="success"){
            alert("移除成功!");
            $('#iformSearch_grid').datagrid('reload');
        }
        else if(responseText=="error"){
           alert("移除失败!");
        } 
 }
//修改属性信息
function openMapModify(id){
			window.parent.openSearchItemModify(id);
		}