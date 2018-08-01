 
$(function(){
	LoadSignature();
}); 

//作用：调入签章数据信息
function LoadSignature(){
	//
	try{
		iformMain.Consult.LoadSignature();                      //调用“会签”签章数据信息
	}catch(e){}
 // readUserSignature();
} 

function readUserSignature(){
	var userid = "ADMIN";
	var url = "http://127.0.0.1:8080/Signature/USER_SIGN/"+userid+".jpg";
	//
	try{
	iformMain.Consult.LoadPicture(0,0,url);
	}catch(e){}
}
function SaveSignature(){
if (iformMain.Consult.Modify){                    //判断签章数据信息是否有改动
  if (!iformMain.Consult.SaveSignature()){        //保存签章数据信息
    alert("保存会签签批内容失败！");
    return false;
  }else{
	  alert("保存成功");
  }
}
return true;
}

function ConsultClear(){
	  if (!(iformMain.Consult.Enabled)) {
		    alert('该签章已被锁定，无权编辑！');
		  }
		  else{
			  iformMain.Consult.Clear();
		  }
}

function loadSignPic(userid){
	var pageUrl = "Signature/USER_SIGN/"+userid+".jpg";
	art.dialog.open(pageUrl,{
		id:'groupWinDiv',
		cover:true,
		title:'新建首级目录',  
		loadingText:'正在加载中,请稍后...',
		bgcolor:'#999',
		rang:true,
		width:520,
		cache:false, 
		lock: true,
		esc: true,
		height:350,   
		iconTitle:false,  
		extendDrag:true,
		autoSize:true
	});
	dg.ShowDialog();
}

function saveJpgToServer(){
	var userid = $("#YHZH").val(); 
	iformMain.Consult.SaveAsJpgEx("ADMIN.jpg","ADMIN","REMOTE");
	alert('保存成功');
} 
function selectRadioAddress(defaultField,treeNode){ 
	$("#DEPTID").val(treeNode.deptId);
	$("#DEPTNAME").val(treeNode.deptname);
	$("#DJQZ").val(treeNode.userId); 
} 
function showSignDlg(){
	var userid = $("#YHZH").val();
	loadSignPic(userid);
	
}