
function whenerror(errcode, file, msg) {

	switch(errcode) {
		
		case -10:	// HTTP error
			alert("Error Code: HTTP Error, File name: " + file.name + ", Message: " + msg);
			break;
		
		case -20:	// No upload script specified
			alert("Error Code: No upload script, File name: " + file.name + ", Message: " + msg);
			break; 
		
		case -30:	// IOError
			alert("Error Code: IO Error, File name: " + file.name + ", Message: " + msg);
			break;
		
		case -40:	// Security error
			alert("Error Code: Security Error, File name: " + file.name + ", Message: " + msg);
			break;
 
		case -50:	// Filesize too big
			alert(file.name+"文件大小受限制, 文件大小: " + file.size +"K,不要超过"+swfu.settings['allowed_filesize']+'K');
			break;
	
	}
	
};

var isCancelAll = false;
/**
	当有文件加入队列中是触发
**/
var totalSize=0;
function fileQueuedByCustomize(file, queuelength) {
	totalSize+=file.size;
	var listingfiles = document.getElementById("fileList");	
	//取得index.html中的fileList（文件列表）对象
	var tr = listingfiles.insertRow();
	tr.id=file.id;
	
	var flag = tr.insertCell(); flag.className='flag2';
	
	var fileName = tr.insertCell();
	fileName.innerHTML=file.name;
	
	var progressTD = tr.insertCell(); progressTD.className='progressTD';
	progressTD.innerHTML='<span id="' + file.id +'progress" class="progressBar"></span><span>'+getNiceFileSize(file.size)+'</span>';
	
	var del = tr.insertCell(); 
	del.className='delectBtn';//调用css
	del.id=file.id + 'deleteGif'; 
	del.innerHTML='<a href="javascript:swfu.cancelFile(\'' + file.id + '\')"><img src="images/del_ico.gif" alt="清除" width="13" height="13" border="0" /></a>';
	showInfo(file, listingfiles.rows.length);
}

/**
	显示信息
**/
function showInfo(file,queuelength){
	var fileCount = document.getElementById("fileCount");//文件总数
	fileCount.innerText=queuelength;
	var fileTotleSize = document.getElementById("fileTotleSize");
	fileTotleSize.innerText = getNiceFileSize(totalSize);//文件大小(byte,Kb.Mb等转换)
}
/**
	某个上传文件被取消时执行方法
**/
function uploadFileCancelled(file, queuelength) {   
	var listingfiles = document.getElementById("fileList");
	
	var rows = listingfiles.rows;
	for(var i=rows.length-1;i>=0;i--){
		if(file.id==rows[i].id){
			listingfiles.deleteRow(i);
			//扣除Filesize 
			totalSize = totalSize -file.size;
			break;
		}
	}	
	showInfo(file, listingfiles.rows.length);
}

function uploadFileStart(file, position, queuelength) {
	window.status='正在上传文件'+file.name+'....';
}

/**
上传过程中返回的信息
*/
function uploadProgress(file, bytesLoaded) {
	
	var progress = document.getElementById(file.id + "progress");	
	var percent = Math.ceil((bytesLoaded / file.size) * 100)
	progress.style.backgroundPositionX=(percent-100)+'px';
	window.status='已上传'+percent+'%';
	//progress.style.background = "#f0f0f0 url(images/progressbar.png) no-repeat -" + (100 - percent) + "px 0";
}
/**
上传文件完成时
*/
function uploadFileComplete(file) {
	var tR = document.getElementById(file.id);
	tR.className += " fileUploading";
	tR.cells(0).className='flag1';
	
	var progress = document.getElementById(file.id + "progress");	
	progress.style.backgroundPositionX='0px';
	//cancelImage;
	var deleteImg = document .getElementById(file.id + 'deleteGif') ; 
  deleteImg.innerHTML = "";	
}


function uploadError(errno) {
	alert(errno)
}

/**
清除所有上传信息
**/
function cancelQueue() {
	isCancelAll  = true; 
	totalSize = 0;
	swfu.cancelQueue();	
	clearhQueue();
}
/**
	处理UI，刪除表格的所有行(文件列表)
**/
function  clearhQueue(){
	var listingfiles = document.getElementById("fileList");
	var rowLength = listingfiles.rows.length;
	while(rowLength>0){
		listingfiles.deleteRow(0);
		rowLength--;
	}

}

/**
	取消文件上传或者文件上传成功
**/
function uploadQueueComplete(file) {
	//var div = document.getElementById("queueinfo");
	if(isCancelAll == true){
		// "取消文件上传";
		showInfo(0,0)
	  }else{
	   	window.status="所有文件上传成功";
	   	//发送ajax请求，获取session的值
	   	//方式:post 成功时调用responseText,失败输出alert(...)
	   	//asynchronous:true异步提交
	   	var options = {method:"post",asynchronous:true,onSuccess:whenUploadQueueComplete,onFailure:function(){alert('failure load')}};
		//new Ajax.Request('jscripts/getuploadFileResult.do',options);
	  }
}
/**
	访问session后，关闭窗口并返回结果
**/
function whenUploadQueueComplete(http){
	ReturnValue(http.responseText)	
	//返回responseText
}
var _K = 1024;
var _M = _K*1024;
function getNiceFileSize(bitnum){
	if(bitnum<_M){
		if(bitnum<_K){
			return bitnum+'B';
		}else{
			return Math.ceil(bitnum/_K)+'K';
		}
		
	}else{
		return Math.ceil(100*bitnum/_M)/100+'M';
	}
	
}