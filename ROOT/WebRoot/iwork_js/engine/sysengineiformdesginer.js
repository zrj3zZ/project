function doSubmit(){
			var content = editor.document.getBody().getHtml();
			if(content.toLowerCase().indexOf("<form") > 0||content.toLowerCase().indexOf("</form>") > 0){
				$.messager.alert("提示", "请取消模版正文中的[FORM]标签", "error");
				return;
			}
			document.forms["editForm"].action="sysEngineIformDesginer_save.action";
			document.forms["editForm"].submit();
			return;
		}
		
function doPreview(formid){
			var url = 'iform_preview.action?formid='+formid;
			var target = 'preview_'+formid;
			var page = window.open('form/loader_frame.html',target,'width='+screen.width+',height=800,top=50,left=150,location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=yes');
			page.location=url;
			return; 
		}
		
function showMaxWindow(formid){
		var url = 'sysEngineIformDesginer_show.action?formid='+formid;
			var target = 'max_'+formid;
			var page = window.open('form/loader_frame.html',target,'width='+screen.width+',height='+screen.height+',top=50,left=150,location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=yes');
			page.location=url;
			return;
		}