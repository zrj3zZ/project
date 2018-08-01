
//加载远程的office文件
function loadOfficeFile(url,i,fn,t,u){  
    var officeObj = document.getElementById('WebOffice');   
	//判断是否安装了插件
	/*
	if(officeObj.WebUrl==undefined){
		document.getElementById('NoInstall').style.display='';
	}else{*/
		//判断是否要求打开模板选择对话框
		try{
				    
			officeObj.WebUrl=url;        //WebUrl:系统服务器路径，与服务器文件交互操作，如保存、打开文档，重要文件
			officeObj.RecordID=i;	       //RecordID:本文档记录编号
			officeObj.FileName=fn;	     //FileName:文档名称
			officeObj.FileType=t;        //FileType:文档类型  .doc  .xls  .wps
			officeObj.UserName=u;        //UserName:操作用户名，痕迹保留需要
			officeObj.ShowToolBar='0';   //ShowToolBar:是否显示工具栏:1显示,0不显示
			officeObj.EditType="-1,1,0,0,0,0,1,0";              //A  必须为“-1”
									                            //B  是否保护文档		“0” 不保护文档， “1” 保护文档， “2” 特殊保护
																//C  是否显示痕迹		“0” 不显示痕迹， “1” 显示痕迹
																//D  是否保留痕迹		“0” 不保留痕迹， “1” 保留痕迹
															    //E  是否打印痕迹		“0” 不打印痕迹， “1” 打印痕迹
															    //F  是否显示审阅工具	“0” 不显示工具， “1” 显示工具
																//G  是否允许拷贝操作	“0” 不允许拷贝， “1” 允许拷贝
																//H  是否允许手写批注	“0” 不可以批注， “1” 可以批注

			officeObj.MaxFileSize=60 * 1024;              //最大的文档大小控制，默认是8M，现在设置成60M。     
			
			officeObj.VisibleTools('新建文件',false);
			officeObj.VisibleTools('打开文件',false);
			officeObj.VisibleTools('保存文件',false);
			officeObj.VisibleTools('文字批注',false);
			officeObj.VisibleTools('手写批注',false);
			officeObj.VisibleTools('文档清稿',false);
			officeObj.VisibleTools('重新批注',false);
			officeObj.VisibleTools('全屏',false);
			setExtParam(officeObj);
			//此处判断打开文件的状态
			if(OFFICE_OBJ_STATUS=='WRITE'){
				//写状态，可以允许出现的菜单此处可定义
			}else if(OFFICE_OBJ_STATUS=='WRITE'){
				//读状态，可以允许出现的菜单此处可定义
			}
			
			var bool = officeObj.WebOpen();
			if(!bool) return;
			officeObj.showType='1';                       //文档显示方式  1:表示文字批注  2:表示手写批注  0:表示文档核稿
		//	document.getElementById('OLEObject').style.display='';
			
		}catch(e){
			alert('加载远程文档失败，请尝试重新打开\n'+e.description);
			window.close();
		}		
	//}
}

function setExtParam(officeObj){  
	officeObj.WebSetMsgByName("DIRECTORYNAME",OFFICE_OBJ_DIR); 	
	officeObj.WebSetMsgByName("DBSTEP","DBSTEP"); 	
}