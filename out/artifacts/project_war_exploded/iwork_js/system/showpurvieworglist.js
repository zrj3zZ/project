    $(function(){
		   //alert(document.editForm.purviewid.value);
			$('#purviewOrgtree').tree({
				checkbox:true,
				url: 'jsonPurviewOrgTree.action?pid=0&purviewid='+document.editForm.purviewid.value,
				onClick:function(node){
					$(this).tree('toggle', node.target);
					//alert('you dbclick '+node.text);
				},onBeforeExpand:function(node){
               	 	//alert(node.attributes.type);
                     $('#purviewOrgtree').tree('options').url = "jsonPurviewOrgTree.action?pid=" + node.id+"&purviewid="+document.editForm.purviewid.value;// change the url                       
                 }
			});
			$('#tt').tabs({
				tools:[{
					iconCls:'',
					text:'',
					handler: function(){
					
					} 
				}]
			});
			$('#pgrid').datagrid({
				url: 'getPurviewOrgGridData.action?purviewid='+document.editForm.purviewid.value
			});			
			
		});
			
function collapseAll(){
			var node = $('#purviewOrgtree').tree('getSelected');
			if (node){
				$('#purviewOrgtree').tree('collapseAll', node.target);
			} else {
				$('#purviewOrgtree').tree('collapseAll');
			}
		}
function expandAll(){
			var node = $('#purviewOrgtree').tree('getSelected');
			if (node){
				$('#purviewOrgtree').tree('expandAll', node.target);
			} else {
				$('#purviewOrgtree').tree('expandAll');
			}
		}
		
//设置权限
function setPurViewOrg(){
			var nodes = $('#purviewOrgtree').tree('getChecked');
			var s = '';
			for(var i=0; i<nodes.length; i++){
				if (s != '') s += ',';
				s += nodes[i].attributes.type+"_"+nodes[i].id;
			}
			document.editForm.nodelist.value = s;
			document.editForm.action="setPurviewOrg.action";
			document.editForm.submit();
			return false;
		}
//关闭窗口
function win_close(){
            var api=art.dialog.open.api;
            api.close();
}