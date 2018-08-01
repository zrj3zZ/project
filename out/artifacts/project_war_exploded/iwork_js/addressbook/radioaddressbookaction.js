		$(function(){
			var  api, W;
			try{
				 api = art.dialog.open.api, W = api.opener;
			}catch(e){
				var obj = window.dialogArguments;
			}
		});
		function closeWin(){
			try{
				api = art.dialog.open.api, W = api.opener;
				api.close(); 
			}catch(e){
				window.close();
			}
		}