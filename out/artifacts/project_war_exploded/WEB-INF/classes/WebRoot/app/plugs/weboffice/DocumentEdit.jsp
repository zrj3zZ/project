<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%> 
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
 <title>在线office</title>
 <link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/process-icon.css"/>
	<link rel="stylesheet" type="text/css" href="iwork_themes/easyui/bootstrap/easyui.css">
 
 <script src="iwork_js/jqueryjs/jquery-3.0.4.min.js"></script>
 <script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js"  ></script>
 <script src="iwork_js/plugs/WebOffice.js"></script>
 <script type="text/javascript">
 $(function(){
    var isNotLoad = true;/**公共方法**/	
	$(".tableAll").click(function(){ 
        if(isNotLoad){
            isNotLoad = false;	 
			  var noneY = $(this).next().css("display");
			  $(".tableAll").next().css("display","none");
			  $(".tableAll").find('td:eq(0)').css({'background-color':'#E6DBEC'});
			  $(".tableAll").find('span:eq(0)').html('+');
				  if( noneY== 'none'){
					  var s = $(this).find('td:eq(0)').html();                
					  $(this).find('td:eq(0)').html(s.replace("+", "-")) ;                              
					  $(this).find('td:eq(0)').css({'background-color':'#FFFFFF'});
		              $(this).next().slideToggle(function(){isNotLoad = true;});
				  }else{
					  isNotLoad = true;
				  }
            }
	});
	var hide = false;	//下拉
	$("#disPlayNone").click(function(){
		 if(hide){
			 $('#showTD').width('204px');
			 $(this).siblings().css("display", "")
			 hide = false;
		 }else{	
			 $('#showTD').width('25px');
			 $(this).siblings().css("display", "none")
			 hide = true;
		 }
	});		
})
</script>
<link rel='stylesheet' type='text/css' href='iwork_css/plugs/iWebProduct.css' />


<!-- 以下为2015主要方法 -->
<script type="text/javascript">
 	var WebOffice = new WebOffice2015(); //创建WebOffice对象
</script>
<script type="text/javascript">
 	function Load(){
 	  try{
 	  		WebOffice.WebUrl="<s:property value="url"/>";              //WebUrl:系统服务器路径，与服务器文件交互操作，如保存、打开文档，重要文件
		    WebOffice.RecordID="<s:property value="fileUUID"/>";            //RecordID:本文档记录编号
		    WebOffice.FileName="<s:property value="fileUUID"/>.doc";            //FileName:文档名称
		    WebOffice.FileType="<s:property value="fileType"/>";            //FileType:文档类型  .doc  .xls  
		    WebOffice.UserName="<s:property value="userId"/>";            //UserName:操作用户名，痕迹保留需要
		    WebOffice.AppendMenu("1","打开本地文件(&L)");    //多次给文件菜单添加
		    WebOffice.AppendMenu("2","保存本地文件(&S)");
			WebOffice.AppendMenu("3","-");
			WebOffice.AppendMenu("4","打印预览(&C)");
			WebOffice.AppendMenu("5","退出打印预览(&E)");
			WebOffice.AddCustomMenu();                       //一次性多次添加包含二次菜单
			WebOffice.Skin('purple');                        //设置皮肤
		    WebOffice.HookEnabled();
		    WebOffice.SetCaption(); 
		    WebOffice.SetUser("<s:property value="userId"/>");
		    if(WebOffice.WebOpen()){    
		   		WebOffice.setEditType("1");         //EditType:编辑类型  方式一   WebOpen之后
		   // 	WebOffice.VBASetUserName(WebOffice.UserName);    //设置用户名
			//WebOffice.ShowWritingUser(true);
			   StatusMsg(WebOffice.Status);
		    }
		    getEditVersion();//判断是否是永中office
		    WebOffice.AddToolbar();//打开文档时显示手写签批工具栏
		  //  WebOffice.ShowCustomToolbar(false);//隐藏手写签批工具栏
 	  }catch(e){
 	     alert(e.description);       
 	  }
 	}
	 //作用：保存文档
	function SaveDocument(){
	  if (WebOffice.WebSave()){    //交互OfficeServer的OPTION="SAVEFILE"
	  	WebOffice.WebOpen();
	     return true;
	  }else{
	  	 alert('文档未保存成功，请稍后重试!');
	     return false;
	  }
	}
 	
 	
 	//设置页面中的状态值
 	function StatusMsg(mValue){
 	   try{
	   document.getElementById('state').innerHTML = mValue;
	   }catch(e){
	     return false;
	   }
	}
	
	//作用：获取文档Txt正文
	function WebGetWordContent(){
	  try{
	    alert(WebOffice.WebObject.ActiveDocument.Content.Text);
	  }catch(e){alert(e.description);}
	}
	
	//作用：写Word内容
	function WebSetWordContent(){
	  var mText=window.prompt("请输入内容:","测试内容");
	  if (mText==null){
	     return (false);
	  }else{
	     WebOffice.WebObject.ActiveDocument.Application.Selection.Range.Text= mText+"\n";
	  }
	}

	//作用：获取文档页数
	function WebDocumentPageCount(){
	    if (WebOffice.FileType==".doc"||WebOffice.FileType==".docx"){
		var intPageTotal = WebOffice.WebObject.ActiveDocument.Application.ActiveDocument.BuiltInDocumentProperties(14);
		intPageTotal = WebOffice.blnIE()?intPageTotal:intPageTotal.Value();
		alert("文档页总数："+intPageTotal);
	    }
	    if (WebOffice.FileType==".wps"){
			var intPageTotal = WebOffice.WebObject.ActiveDocument.PagesCount();
			alert("文档页总数："+intPageTotal);
	    }
	}
	
	//作用：显示或隐藏痕迹[隐藏痕迹时修改文档没有痕迹保留]  true表示隐藏痕迹  false表示显示痕迹
	function ShowRevision(mValue){
	  if (mValue){
	     WebOffice.WebShow(true);
	     StatusMsg("显示痕迹...");
	  }else{
	     WebOffice.WebShow(false);
	     StatusMsg("隐藏痕迹...");
	  }
	}
	
	//接受文档中全部痕迹
	function WebAcceptAllRevisions(){
	  WebOffice.WebObject.ActiveDocument.Application.ActiveDocument.AcceptAllRevisions();
	  var mCount = WebOffice.WebObject.ActiveDocument.Application.ActiveDocument.Revisions.Count;
	  if(mCount>0){
	    return false;
	  }else{
	    return true;
	  }
	}
	
		//作用：VBA套红
	function WebInsertVBA(){
		//画线
		try{
		var object=WebOffice.WebObject.ActiveDocument;
		var myl=object.Shapes.AddLine(100,60,305,60);
		var myl1=object.Shapes.AddLine(326,60,520,60);
	   	var myRange=WebOffice.WebObject.ActiveDocument.Range(0,0);
		myRange.Select();
		var mtext="★";
		WebOffice.WebObject.ActiveDocument.Application.Selection.Range.InsertAfter (mtext+"\n");
	   	var myRange=WebOffice.WebObject.ActiveDocument.Paragraphs(1).Range;
	   	myRange.ParagraphFormat.LineSpacingRule =1.5;
	   	myRange.font.ColorIndex=6;
	   	myRange.ParagraphFormat.Alignment=1;
	   	myRange=WebOffice.WebObject.ActiveDocument.Range(0,0);
		myRange.Select();
		mtext="金格发[２０１２]１５４号";
		WebOffice.WebObject.ActiveDocument.Application.Selection.Range.InsertAfter (mtext+"\n");
		myRange=WebOffice.WebObject.ActiveDocument.Paragraphs(1).Range;
		myRange.ParagraphFormat.LineSpacingRule =1.5;
		myRange.ParagraphFormat.Alignment=1;
		myRange.font.ColorIndex=1;
		mtext="金格电子政务文件";
		WebOffice.WebObject.ActiveDocument.Application.Selection.Range.InsertAfter (mtext+"\n");
		myRange=WebOffice.WebObject.ActiveDocument.Paragraphs(1).Range;
		myRange.ParagraphFormat.LineSpacingRule =1.5;
		myRange.Font.ColorIndex=6;
		myRange.Font.Name="仿宋_GB2312";
		myRange.font.Bold=true;
		myRange.Font.Size=50;
		myRange.ParagraphFormat.Alignment=1;
		WebOffice.WebObject.ActiveDocument.PageSetup.LeftMargin=70;
		WebOffice.WebObject.ActiveDocument.PageSetup.RightMargin=70;
		WebOffice.WebObject.ActiveDocument.PageSetup.TopMargin=70;
		WebOffice.WebObject.ActiveDocument.PageSetup.BottomMargin=70;
		}catch(e){
		 alert(e);
		}
	}

	//作用：设置书签值  vbmName:标签名称，vbmValue:标签值   标签名称注意大小写
	function SetBookmarks(){
		try{
			var mText=window.prompt("请输入书签名称和书签值，以','隔开。","例如：book1,book2");
			var mValue = mText.split(",");
			BookMarkName = mValue[0];
			BookMarkValue = mValue[1];
			WebOffice.WebSetBookmarks(mValue[0],mValue[1]);
			StatusMsg("设置成功");
			return true;
		}catch(e){
			StatusMsg("书签不存在");
			return false;
		}
	}
	//打开书签窗口
	function WebOpenBookMarks(){	
			WebOffice.WebOpenBookMarks();
		 }
	//添加书签
	function WebAddBookMarks(){//书签名称，书签值
		WebOffice.WebAddBookMarks("JK","KingGrid");
	}
	 //定位书签
	function WebFindBookMarks(){
		WebOffice.WebFindBookMarks("JK");
	 }
	 //删除书签
	function WebDelBookMarks(){//书签名称，
	    WebOffice.WebDelBookMarks("JK");//删除书签
	 }
         
	function DelFile(){
	   var mFileName = WebOffice.FilePath+WebOffice.FileName;
       WebOffice.Close(); 
       WebOffice.WebDelFile(mFileName);
	}
	//作用：用Excel求和
	function WebGetExcelContent(){
	  if(!WebOffice.WebObject.ActiveDocument.Application.Sheets(1).ProtectContents){
		  WebOffice.WebObject.ExitExcelEditMode();
		  WebOffice.WebObject.Activate(true);  
		  WebOffice.WebObject.ActiveDocument.Application.Sheets(1).Select();
		  WebOffice.WebObject.ActiveDocument.Application.Range("C5").Select();
		  WebOffice.WebObject.ActiveDocument.Application.ActiveCell.FormulaR1C1 = "126";
		  WebOffice.WebObject.ActiveDocument.Application.Range("C6").Select();
		  WebOffice.WebObject.ActiveDocument.Application.ActiveCell.FormulaR1C1 = "446";
		  WebOffice.WebObject.ActiveDocument.Application.Range("C7").Select();
		  WebOffice.WebObject.ActiveDocument.Application.ActiveCell.FormulaR1C1 = "556";
		  WebOffice.WebObject.ActiveDocument.Application.Range("C5:C8").Select();
		  WebOffice.WebObject.ActiveDocument.Application.Range("C8").Activate();
		  WebOffice.WebObject.ActiveDocument.Application.ActiveCell.FormulaR1C1 = "=SUM(R[-3]C:R[-1]C)";
		  WebOffice.WebObject.ActiveDocument.Application.Range("D8").Select();
		  WebOffice.WebObject.ActiveDocument.application.sendkeys("{ESC}");
		  StatusMsg(WebOffice.WebObject.ActiveDocument.Application.Range("C8").Text);
	  }
	
	}
	
		//作用：保护工作表单元
	function WebSheetsLock(){
		 if(!WebOffice.WebObject.ActiveDocument.Application.Sheets(1).ProtectContents){
	  WebOffice.WebObject.ActiveDocument.Application.Range("A1").Select();
	  WebOffice.WebObject.ActiveDocument.Application.ActiveCell.FormulaR1C1 = "产品";
	  WebOffice.WebObject.ActiveDocument.Application.Range("B1").Select();
	  WebOffice.WebObject.ActiveDocument.Application.ActiveCell.FormulaR1C1 = "价格";
	  WebOffice.WebObject.ActiveDocument.Application.Range("C1").Select();
	  WebOffice.WebObject.ActiveDocument.Application.ActiveCell.FormulaR1C1 = "详细说明";
	  WebOffice.WebObject.ActiveDocument.Application.Range("D1").Select();
	  WebOffice.WebObject.ActiveDocument.Application.ActiveCell.FormulaR1C1 = "库存";
	  WebOffice.WebObject.ActiveDocument.Application.Range("A2").Select();
	  WebOffice.WebObject.ActiveDocument.Application.ActiveCell.FormulaR1C1 = "书签";
	  WebOffice.WebObject.ActiveDocument.Application.Range("A3").Select();
	  WebOffice.WebObject.ActiveDocument.Application.ActiveCell.FormulaR1C1 = "毛笔";
	  WebOffice.WebObject.ActiveDocument.Application.Range("A4").Select();
	  WebOffice.WebObject.ActiveDocument.Application.ActiveCell.FormulaR1C1 = "钢笔";
	  WebOffice.WebObject.ActiveDocument.Application.Range("A5").Select();
	  WebOffice.WebObject.ActiveDocument.Application.ActiveCell.FormulaR1C1 = "尺子";
	
	  WebOffice.WebObject.ActiveDocument.Application.Range("B2").Select();
	  WebOffice.WebObject.ActiveDocument.Application.ActiveCell.FormulaR1C1 = "0.5";
	  WebOffice.WebObject.ActiveDocument.Application.Range("C2").Select();
	  WebOffice.WebObject.ActiveDocument.Application.ActiveCell.FormulaR1C1 = "樱花";
	  WebOffice.WebObject.ActiveDocument.Application.Range("D2").Select();
	  WebOffice.WebObject.ActiveDocument.Application.ActiveCell.FormulaR1C1 = "300";
	
	  WebOffice.WebObject.ActiveDocument.Application.Range("B3").Select();
	  WebOffice.WebObject.ActiveDocument.Application.ActiveCell.FormulaR1C1 = "2";
	  WebOffice.WebObject.ActiveDocument.Application.Range("C3").Select();
	  WebOffice.WebObject.ActiveDocument.Application.ActiveCell.FormulaR1C1 = "狼毫";
	  WebOffice.WebObject.ActiveDocument.Application.Range("D3").Select();
	  WebOffice.WebObject.ActiveDocument.Application.ActiveCell.FormulaR1C1 = "50";
	
	  WebOffice.WebObject.ActiveDocument.Application.Range("B4").Select();
	  WebOffice.WebObject.ActiveDocument.Application.ActiveCell.FormulaR1C1 = "3";
	  WebOffice.WebObject.ActiveDocument.Application.Range("C4").Select();
	  WebOffice.WebObject.ActiveDocument.Application.ActiveCell.FormulaR1C1 = "蓝色";
	  WebOffice.WebObject.ActiveDocument.Application.Range("D4").Select();
	  WebOffice.WebObject.ActiveDocument.Application.ActiveCell.FormulaR1C1 = "90";
	
	  WebOffice.WebObject.ActiveDocument.Application.Range("B5").Select();
	  WebOffice.WebObject.ActiveDocument.Application.ActiveCell.FormulaR1C1 = "1";
	  WebOffice.WebObject.ActiveDocument.Application.Range("C5").Select();
	  WebOffice.WebObject.ActiveDocument.Application.ActiveCell.FormulaR1C1 = "20cm";
	  WebOffice.WebObject.ActiveDocument.Application.Range("D5").Select();
	  WebOffice.WebObject.ActiveDocument.Application.ActiveCell.FormulaR1C1 = "40";
	
	  //保护工作表
	  WebOffice.WebObject.ActiveDocument.Application.Range("B2:D5").Select();
	  WebOffice.WebObject.ActiveDocument.Application.Selection.Locked = false;
	  WebOffice.WebObject.ActiveDocument.Application.Selection.FormulaHidden = false;
	  WebOffice.WebObject.ActiveDocument.Application.ActiveSheet.Protect(true,true,true);
	  StatusMsg("已经保护工作表，只有B2-D5单元格可以修改。");
		 }
	}
	
	//根据当空打开的文档类型保存文档
	function WebOpenLocal(){
	   WebOffice.WebOpenLocal();
	   //WebOffice.WebDelFile(WebOffice.FilePath+WebOffice.FileName);
	   //WebOffice.FileType = WebOffice.WebGetDocSuffix();
	   //WebOffice.FileName = WebOffice.FileName.substring(0,WebOffice.FileName.lastIndexOf("."))+WebOffice.FileType;
	   //document.getElementById('FileType').value = WebOffice.FileType;
	}
	//调用模板
	function WebUseTemplate(fileName){
	    var currFilePath;
	    if(WebOffice.WebAcceptAllRevisions()){//清除正文痕迹的目的是为了避免痕迹状态下出现内容异常问题。
	       currFilePath = WebOffice.getFilePath(); //获取2015特殊路径
	       WebOffice.WebSaveLocalFile(currFilePath+WebOffice.iWebOfficeTempName);//将当前文档保存下来
	       if(WebOffice.DownloadToFile(fileName,currFilePath)){//下载服务器指定的文件
	          WebOffice.OpenLocalFile(currFilePath+fileName);//打开该文件
	          if(!WebOffice.VBAInsertFile("Content",currFilePath+WebOffice.iWebOfficeTempName)){//插入文档
	           StatusMsg("插入文档失败"); 
	           return;
	          }
	          StatusMsg("模板套红成功"); 
	          return; 
	       }
	       StatusMsg("下载文档失败"); 
	       return;
	    }
	    StatusMsg("清除正文痕迹失败，套红中止"); 
	}
	
	function HandWriting(){
	 	WebOffice.ShowToolBars(false);//签批时隐藏工具栏
	 	WebOffice.ShowMenuBar(false);//签批时隐藏菜单栏
	 	var penColor=document.getElementById("PenColor").value;
	 	var penWidth=document.getElementById("PenWidth").value;
	 	WebOffice.HandWriting(penColor,penWidth);
	}
	function getEditVersion(){
		var getVersion=WebOffice.getEditVersion(); //获取当前编辑器软件版本
		if (getVersion == "YozoWP.exe")  //如果是永中office,隐藏手写功能等
		{
		    document.getElementById("handWriting1").style.display='none';
		    document.getElementById("handWriting2").style.display='none';
		    document.getElementById("expendFunction").style.display='none';
		    document.getElementById("enableCopy1").style.display='none';
		    document.getElementById("enableCopy2").style.display='none';
		    document.getElementById("OpenBookMarks").style.display='none';
		    
		}
	}
	
	function FullSize(mValue){
		WebOffice.FullSize(mValue);//全屏
		WebOffice.ShowCustomToolbar(true);//全屏时显示手写签批工具栏
	}
</script>
<script language="javascript" for="WebOffice2015" event="OnReady()">
   WebOffice.setObj(document.getElementById('WebOffice2015'));//给2015对象赋值
   Load();//避免页面加载完，控件还没有加载情况
</script>
<script language="javascript" for="WebOffice2015" event="OnRightClickedWhenAnnotate()">
   WebOffice.ShowToolBars(true);//停止签批时显示工具栏
   WebOffice.ShowMenuBar(true);//停止签批时显示菜单栏
</script>
<script language="JavaScript" for=WebOffice2015 event="OnFullSizeBefore(bVal)">
    if(bVal == true){
    	WebOffice.ShowCustomToolbar(true);	//全屏显示控件的手写签批工具栏
    }
</script>
<script language="JavaScript" for=WebOffice2015 event="OnFullSizeAfter(bVal)">
    if(bVal == false){
    	WebOffice.ShowCustomToolbar(false);	//隐藏控件的手写签批工具栏
    }
</script>

<script language="javascript" for=WebOffice2015 event="OnRecvStart(nTotleBytes)">
    nSendTotleBytes = nTotleBytes;
    nSendedSumBytes = 0;
</script>
<script language="javascript" for=WebOffice2015 event="OnRecving(nRecvedBytes)">
    nSendedSumBytes += nRecvedBytes;
</script>
<script language="javascript" for=WebOffice2015 event="OnRecvEnd(bSucess)">

</script>
<script language="javascript" for=WebOffice2015 event="OnSendStart(nTotleBytes)">
    nSendTotleBytes = nTotleBytes;
    nSendedSumBytes = 0;
</script>
<script language="javascript" for=WebOffice2015 event="OnSending(nSendedBytes)">
    nSendedSumBytes += nSendedBytes;
</script>
<script language="javascript" for=WebOffice2015 event="OnSendEnd(bSucess)">
    if (bSucess){
        if(WebOffice.sendMode == "LoadImage"){
          var httpclient = WebOffice.WebObject.Http;
          WebOffice.hiddenSaveLocal(httpclient,WebOffice,false,false,WebOffice.ImageName);
          WebOffice.InsertImageByBookMark();
          WebOffice.ImageName = null;
          WebOffice.BookMark = null;
          StatusMsg("插入图片成功");
          return
	     } 
	      StatusMsg("插入图片失败"); 
    }
</script>
<script language="JavaScript" for=WebOffice2015 event="OnCommand(ID, Caption, bCancel)">
   switch(ID){
	    case 1:WebOpenLocal();break;//打开本地文件
	    case 2:WebOffice.WebSaveLocal();break;//另存本地文件
		case 4:WebOffice.PrintPreview();break;//启用
		case 5:WebOffice.PrintPreviewExit();WebOffice.ShowField();break;//启用
		case 17:WebOffice.SaveEnabled(true);StatusMsg("启用保存");break;//启用保存
		case 18:WebOffice.SaveEnabled(false);StatusMsg("关闭保存");break;//关闭保存
		case 19:WebOffice.PrintEnabled(true);StatusMsg("启用打印");break;//启用打印
		case 20:WebOffice.PrintEnabled(false);StatusMsg("关闭打印");break;//关闭打印
		case 301:WebOffice.HandWriting("255","4");StatusMsg("手写签批");break;//手写签批
		case 302:WebOffice.StopHandWriting();StatusMsg("停止手写签批");break;//停止手写签批
		case 303:WebOffice.TextWriting();StatusMsg("文字签名");break;//文字签名
		case 304:WebOffice.ShapeWriting();StatusMsg("图形签批");break;//图形签批
		case 305:WebOffice.RemoveLastWriting();StatusMsg("取消上一次签批");break;//取消上一次签批
		case 306:WebOffice.ShowWritingUser(false,WebOffice.UserName);StatusMsg("显示签批用户");break;//显示签批用户
		default:;return;  
  }   
</script>

<script language="javascript" for=WebOffice2015 event="OnQuit()">
//DelFile();
</script>


<!--以下是多浏览器的事件方法 -->
<script >
function OnReady(){
 WebOffice.setObj(document.getElementById('WebOffice2015'));//给2015对象赋值
 //Load();//避免页面加载完，控件还没有加载情况
 window.onload = function(){Load();} //IE和谷歌可以直接调用Load方法，火狐要在页面加载完后去调用
}
//停止签批时显示工具栏和菜单栏
function OnRightClickedWhenAnnotate(){
	WebOffice.ShowToolBars(true);
    WebOffice.ShowMenuBar(true);
}
//全屏显示控件的手写签批工具栏
function OnFullSizeBefore(bVal){
	 if(bVal == true){
    	WebOffice.ShowCustomToolbar(true);	
    }
}
//退出全屏隐藏控件的手写签批工具栏
function OnFullSizeAfter(bVal){
	if(bVal == false){
    	WebOffice.ShowCustomToolbar(false);	
    }
}
//上传下载回调用事件
function OnSendStart(nTotleBytes){
 nSendTotleBytes = nTotleBytes;nSendedSumBytes = 0;
}
function OnSending(nSendedBytes){
        nSendedSumBytes += nSendedBytes;
}
//异步上传
function OnSendEnd() {
    if(WebOffice.sendMode == "LoadImage"){
    	var httpclient = WebOffice.WebObject.Http;
    	WebOffice.hiddenSaveLocal(httpclient,WebOffice,false,false,WebOffice.ImageName);
     	WebOffice.InsertImageByBookMark();
        WebOffice.ImageName = null;
        WebOffice.BookMark = null;
        StatusMsg("插入图片成功");
        return;
	} 
	StatusMsg("插入图片失败"); 
}
function OnRecvStart(nTotleBytes){
    nSendTotleBytes = nTotleBytes;nSendedSumBytes = 0;
}
function OnRecving(nRecvedBytes){
   nSendedSumBytes += nRecvedBytes;
}
//异步下载
function OnRecvEnd() {
}
function OnCommand(ID, Caption, bCancel){
   switch(ID){
	    case 1:WebOpenLocal();break;//打开本地文件
	    case 2:WebOffice.WebSaveLocal();break;//另存本地文件
		case 4:WebOffice.PrintPreview();break;//启用
		case 5:WebOffice.PrintPreviewExit();WebOffice.ShowField();break;//启用
		case 17:WebOffice.SaveEnabled(true);StatusMsg("启用保存");break;//启用保存
		case 18:WebOffice.SaveEnabled(false);StatusMsg("关闭保存");break;//关闭保存
		case 19:WebOffice.PrintEnabled(true);StatusMsg("启用打印");break;//启用打印
		case 20:WebOffice.PrintEnabled(false);StatusMsg("关闭打印");break;//关闭打印
		case 301:WebOffice.HandWriting("255","4");StatusMsg("手写签批");break;//手写签批
		case 302:WebOffice.StopHandWriting();StatusMsg("停止手写签批");break;//停止手写签批
		case 303:WebOffice.TextWriting();StatusMsg("文字签名");break;//文字签名
		case 304:WebOffice.ShapeWriting();StatusMsg("图形签批");break;//图形签批
		case 305:WebOffice.RemoveLastWriting();StatusMsg("取消上一次签批");break;//取消上一次签批
		case 306:WebOffice.ShowWritingUser(false,WebOffice.UserName);StatusMsg("显示签批用户");break;//显示签批用户
		default:;return;  
  }   
}
     
</script>
<!--End以下是多浏览器的事件方法 -->



<!--End 为2015主要方法 -->


</head>
<body onresize="init()"  style="overflow-y:hidden;overflow-x:hidden" onUnload="WebOffice.WebClose()">
<table id="maintable"  cellspacing='0' cellpadding='0' >
 <!-- head 
 <tr><td colspan="2"  height="61px"><table cellspacing='0' cellpadding='0' cellspacing='0' cellpadding='0'  id="header"><tr ><td ><img src="css/logo.jpg"/></td><td><span>　iWebOffice2015</span> 在线管理中间件示例程序</td></tr></table></td></tr> 
 <tr><td height="34px" class="title" width="80%">
    <span>主题：<input type=text name="Subject1" id="Subject1"   onchange=" document.getElementById('Subject').value = this.value;" value="" class="InputLine2"  onblur="if(this.value.length>20){this.value='请输入主题';StatusMsg('主题不能超过50个字');}"/> </span>
	<span style="margin-left: 50px;">作者：<input type=text onchange=" document.getElementById('Author').value = this.value;" name="Author1"  id="Author1" value="体验用户36" class="InputLine2" onblur="if(this.value.length>8){this.value='体验用户';StatusMsg('作者不能超过8个字');}" /></span>
 </td>
 <td class="title"><span><a href="#" onclick="SaveDocument()">保存文档</a></span><span><a href="#" onclick="window.opener.location.reload();window.close()">返回列表</a></span></td>
 </tr> 
 <!-- end head -->
 <tr>
 	<td>
 		<a  href="#" style="margin-left:1px;margin-right:1px"  onclick='SaveDocument()' text='Ctrl+s' class="easyui-linkbutton" plain="true" iconCls="icon-save">保存</a>
 		<a href="#" style="margin-left:1px;margin-right:1px"  onclick='location.reload();' class="easyui-linkbutton" plain="true" iconCls="icon-reload">刷新</a>
 		
 	</td>
 </tr>
 <!-- showList -->
 <tr><td id="showtr" colspan="2"  valign="top">      
      <script src="iwork_js/plugs/iWebOffice2015.js"></script>
 </td></tr>
 <tr>
 	<td>
 		<table id="functionTable"   cellspacing='4' cellpadding='0'  >
		  <tr><td>&nbsp;</td></tr>     
        		   	<tr>
					<td style="border: 0">&nbsp;
					<form id="iWebOfficeForm"   method="post" action="iwork_weboffice2015_save.action" >
					    <input type="hidden" name="RecordID" value="1449826093266"/>
					    <input type="hidden" name="Template" value=""/>
					    <input type="hidden" id="FileType" name="FileType" value=".doc"/>
					    <input type="hidden" name="EditType" value="0"/> 
					    <input type="hidden" name="HTMLPath" value=""/>
					    <input type="hidden" id="Subject" name="Subject" value=""/>
					    <input type="hidden" id="Author" name="Author" value="null"/> 
                   </form></td>
				  </tr>
       </table>
 	</td>
 </tr>
 <!-- end showList -->
  
</table>
</body> 
</html>
 <script language="javascript">
 self.moveTo(0,0);
 self.resizeTo(screen.availWidth,screen.availHeight);
 init();
 function init(){
   document.getElementById('WebOffice2015').height =document.documentElement.clientHeight +"px";
   var functionTableLength = getHeight('showTD')-document.getElementById("functionTable").rows.length*32;
   for(var i =0;i<document.getElementById("functionTable").rows.length-3;i++){
       try{
        var readivLength = document.getElementById("readT"+i).rows.length*30;
      
	    if(readivLength+30 < functionTableLength){
	      document.getElementById('readT'+i).style.height =  readivLength+ 8 + "px";
	      document.getElementById('read'+i).style.height =  readivLength+ 8 + "px";
	    }else{
	        document.getElementById('readT'+i).style.height =  functionTableLength-50 + "px";
	        document.getElementById('read'+i).style.height =  functionTableLength -50 + "px";
	    }
	   }catch(e){
         continue;
        }
    }
    $("tr").remove(".disabled");
  }
  //获取id的高度
  function  getHeight(id){
    return document.getElementById(id).offsetHeight; 
  }
 </script>