$(function(){
			$('#addresstree').tree({
				checkbox:true,
				url: 'jsonAddresstree.action',
				onClick:function(node){
					$(this).tree('toggle', node.target);
					//alert('you dbclick '+node.text);
				},onBeforeExpand:function(node){
               	 	//alert(node.attributes.type);
                     $('#addresstree').tree('options').url = "jsonAddresstree.action?pid=" + node.id;// change the url                       
                 }
			});
		});