	function alonebuildFileElementHtml(colName,fileDivId,fileName,fileUUID,fileSrc,removeFlag){
			if (fileName.length > 20) {
					fileName = fileName.substr(0,15) + '...';
			} 
			var flag = false;
			var pos = fileName.lastIndexOf('.');
			if(pos!=-1){
				var extName=fileName.substring(pos + 1,fileName.length);
				if(extName==".doc"||extName==".docx"){
					flag=true;
				}
			}
			var html = '<div  id="'+fileDivId+'" style="background-color: #F5F5F5;border-bottom: 1px solid #E5E5E5;font: 11px Verdana, Geneva, sans-serif;padding: 5px;">';
			if(removeFlag){
				html 	+= '		<table id="aloneTable"><tr id="'+fileUUID+'" align="center">';
				html 	+= '<td style="white-space:nowrap; padding: 5px;"><a href="uploadifyDownload.action?fileUUID='+fileUUID+'" target="_blank"><img src="/iwork_img/attach.png"/>'+fileName+'</a></td>';
				if(flag){
					html	+='<td style="white-space:nowrap; padding-top: 5px;"><a style="text-decoration:none;" href="javascript:updateUploadify(\''+fileUUID+'\',\''+fileName+'\');">【修改】</a>&nbsp;&nbsp;&nbsp;&nbsp;';
				}
				html	+='<a style="text-decoration:none;" href="javascript:aloneUploadifyReomve(\''+colName+'\',\''+fileUUID+'\',\''+fileDivId+'\');">【删除】</a></td></tr></table>';
			}
			html 	+= '</div>';
			return html;
	}
	