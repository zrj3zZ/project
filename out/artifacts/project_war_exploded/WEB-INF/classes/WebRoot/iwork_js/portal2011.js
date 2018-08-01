
function yingyan(form,awsuid,sapuid){
 	//window.location.target="mainFrame";
	form.cmd.value="Kingsoft_Portal_Yingyan_User"; 	
	form.awsuid.value=awsuid;
	form.sapuid.value=sapuid;
	form.target="_blank";
 	//disableAll(form);
	frmMain.submit();
	return false;
 }

 function openInfo(form,myCmd,channelId){
 		form.cmd.value=myCmd;
 		form.channelId.value=channelId;
		form.target = "mainFrame";
		form.submit();
		return false;
 	}
	
function openAppKm(url){
		var newWin=getNewTarget();
		var page = window.open('../aws_html/wait.htm',newWin,'width=750,height=450,location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=no');
		page.location=url;
		return false;
	}
	function openAppfile(url){
		var newWin=getNewTarget();
		var page = window.open('../aws_html/wait.htm',newWin,'width=750,height=450,location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=no');
		page.location=url;
		return false;
	}
	
	function openVideoListFrame(form,myCmd){
 		form.cmd.value=myCmd;
 		form.target = "mainFrame";
		form.submit();
		return false;
 	}
	
	
	
  function change(obj){
  	/*bj.className = "selectTab";*/
  	if(obj=="t1"){
		document.getElementById("t1").className = "selectTab";
		document.getElementById("t2").className = "TabbedPanelsTab";
		document.getElementById("t3").className = "TabbedPanelsTab";
		document.getElementById("t4").className = "TabbedPanelsTab";
		document.getElementById("t5").className = "TabbedPanelsTab";
		document.getElementById("t6").className = "TabbedPanelsTab";
		document.getElementById("t7").className = "TabbedPanelsTab";
		
  		document.getElementById("bj_d1").style.display="";
  		document.getElementById("bj_d2").style.display="none";
  		document.getElementById("bj_d3").style.display="none";
  		document.getElementById("bj_d4").style.display="none";
  		document.getElementById("bj_d5").style.display="none";
  		document.getElementById("bj_d6").style.display="none";
  		document.getElementById("bj_d7").style.display="none";
  		
  		document.getElementById("zh_d1").style.display="";
  		document.getElementById("zh_d2").style.display="none";
  		document.getElementById("zh_d3").style.display="none";
  		document.getElementById("zh_d4").style.display="none";
  		document.getElementById("zh_d5").style.display="none";
  		document.getElementById("zh_d6").style.display="none";
  		document.getElementById("zh_d7").style.display="none";
  	}
  	if(obj=="t2"){
		document.getElementById("t1").className = "TabbedPanelsTab";
		document.getElementById("t2").className = "selectTab";
		document.getElementById("t3").className = "TabbedPanelsTab";
		document.getElementById("t4").className = "TabbedPanelsTab";
		document.getElementById("t5").className = "TabbedPanelsTab";
		document.getElementById("t6").className = "TabbedPanelsTab";
		document.getElementById("t7").className = "TabbedPanelsTab";
		
  		document.getElementById("bj_d1").style.display="none";
  		document.getElementById("bj_d2").style.display="";
  		document.getElementById("bj_d3").style.display="none";
  		document.getElementById("bj_d4").style.display="none";
  		document.getElementById("bj_d5").style.display="none";
  		document.getElementById("bj_d6").style.display="none";
  		document.getElementById("bj_d7").style.display="none";
  		
  		document.getElementById("zh_d1").style.display="none";
  		document.getElementById("zh_d2").style.display="";
  		document.getElementById("zh_d3").style.display="none";
  		document.getElementById("zh_d4").style.display="none";
  		document.getElementById("zh_d5").style.display="none";
  		document.getElementById("zh_d6").style.display="none";
  		document.getElementById("zh_d7").style.display="none";
  	}
  	if(obj=="t3"){
		document.getElementById("t1").className = "TabbedPanelsTab";
		document.getElementById("t2").className = "TabbedPanelsTab";
		document.getElementById("t3").className = "selectTab";
		document.getElementById("t4").className = "TabbedPanelsTab";
		document.getElementById("t5").className = "TabbedPanelsTab";
		document.getElementById("t6").className = "TabbedPanelsTab";
		document.getElementById("t7").className = "TabbedPanelsTab";
		
  		document.getElementById("bj_d1").style.display="none";
  		document.getElementById("bj_d2").style.display="none";
  		document.getElementById("bj_d3").style.display="";
  		document.getElementById("bj_d4").style.display="none";
  		document.getElementById("bj_d5").style.display="none";
  		document.getElementById("bj_d6").style.display="none";
  		document.getElementById("bj_d7").style.display="none";
  		
  		document.getElementById("zh_d1").style.display="none";
  		document.getElementById("zh_d2").style.display="none";
  		document.getElementById("zh_d3").style.display="";
  		document.getElementById("zh_d4").style.display="none";
  		document.getElementById("zh_d5").style.display="none";
  		document.getElementById("zh_d6").style.display="none";
  		document.getElementById("zh_d7").style.display="none";
  	}
  	if(obj=="t4"){
		document.getElementById("t1").className = "TabbedPanelsTab";
		document.getElementById("t2").className = "TabbedPanelsTab";
		document.getElementById("t3").className = "TabbedPanelsTab";
		document.getElementById("t4").className = "selectTab";
		document.getElementById("t5").className = "TabbedPanelsTab";
		document.getElementById("t6").className = "TabbedPanelsTab";
		document.getElementById("t7").className = "TabbedPanelsTab";
		
  		document.getElementById("bj_d1").style.display="none";
  		document.getElementById("bj_d2").style.display="none";
  		document.getElementById("bj_d3").style.display="none";
  		document.getElementById("bj_d4").style.display="";
  		document.getElementById("bj_d5").style.display="none";
  		document.getElementById("bj_d6").style.display="none";
  		document.getElementById("bj_d7").style.display="none";
  		
  		document.getElementById("zh_d1").style.display="none";
  		document.getElementById("zh_d2").style.display="none";
  		document.getElementById("zh_d3").style.display="none";
  		document.getElementById("zh_d4").style.display="";
  		document.getElementById("zh_d5").style.display="none";
  		document.getElementById("zh_d6").style.display="none";
  		document.getElementById("zh_d7").style.display="none";
  	}
  	if(obj=="t5"){
		document.getElementById("t1").className = "TabbedPanelsTab";
		document.getElementById("t2").className = "TabbedPanelsTab";
		document.getElementById("t3").className = "TabbedPanelsTab";
		document.getElementById("t4").className = "TabbedPanelsTab";
		document.getElementById("t5").className = "selectTab";
		document.getElementById("t6").className = "TabbedPanelsTab";
		document.getElementById("t7").className = "TabbedPanelsTab";
		
  		document.getElementById("bj_d1").style.display="none";
  		document.getElementById("bj_d2").style.display="none";
  		document.getElementById("bj_d3").style.display="none";
  		document.getElementById("bj_d4").style.display="none";
  		document.getElementById("bj_d5").style.display="";
  		document.getElementById("bj_d6").style.display="none";
  		document.getElementById("bj_d7").style.display="none";
  		
  		document.getElementById("zh_d1").style.display="none";
  		document.getElementById("zh_d2").style.display="none";
  		document.getElementById("zh_d3").style.display="none";
  		document.getElementById("zh_d4").style.display="none";
  		document.getElementById("zh_d5").style.display="";
  		document.getElementById("zh_d6").style.display="none";
  		document.getElementById("zh_d7").style.display="none";
  	}
  	if(obj=="t6"){
		document.getElementById("t1").className = "TabbedPanelsTab";
		document.getElementById("t2").className = "TabbedPanelsTab";
		document.getElementById("t3").className = "TabbedPanelsTab";
		document.getElementById("t4").className = "TabbedPanelsTab";
		document.getElementById("t5").className = "TabbedPanelsTab";
		document.getElementById("t6").className = "selectTab";
		document.getElementById("t7").className = "TabbedPanelsTab";
		
  		document.getElementById("bj_d1").style.display="none";
  		document.getElementById("bj_d2").style.display="none";
  		document.getElementById("bj_d3").style.display="none";
  		document.getElementById("bj_d4").style.display="none";
  		document.getElementById("bj_d5").style.display="none";
  		document.getElementById("bj_d6").style.display="";
  		document.getElementById("bj_d7").style.display="none";
  		
  		document.getElementById("zh_d1").style.display="none";
  		document.getElementById("zh_d2").style.display="none";
  		document.getElementById("zh_d3").style.display="none";
  		document.getElementById("zh_d4").style.display="none";
  		document.getElementById("zh_d5").style.display="none";
  		document.getElementById("zh_d6").style.display="";
  		document.getElementById("zh_d7").style.display="none";
  	}
  	if(obj=="t7"){
		document.getElementById("t1").className = "TabbedPanelsTab";
		document.getElementById("t2").className = "TabbedPanelsTab";
		document.getElementById("t3").className = "TabbedPanelsTab";
		document.getElementById("t4").className = "TabbedPanelsTab";
		document.getElementById("t5").className = "TabbedPanelsTab";
		document.getElementById("t6").className = "TabbedPanelsTab";
		document.getElementById("t7").className = "selectTab";
		
  		document.getElementById("bj_d1").style.display="none";
  		document.getElementById("bj_d2").style.display="none";
  		document.getElementById("bj_d3").style.display="none";
  		document.getElementById("bj_d4").style.display="none";
  		document.getElementById("bj_d5").style.display="none";
  		document.getElementById("bj_d6").style.display="none";
  		document.getElementById("bj_d7").style.display="";
  		
  		document.getElementById("zh_d1").style.display="none";
  		document.getElementById("zh_d2").style.display="none";
  		document.getElementById("zh_d3").style.display="none";
  		document.getElementById("zh_d4").style.display="none";
  		document.getElementById("zh_d5").style.display="none";
  		document.getElementById("zh_d6").style.display="none";
  		document.getElementById("zh_d7").style.display="";
  	}
  }
 
 function changeDisplay(){
    if(menu.value=="BJ"){
       document.getElementById("ZHMENU").style.display="none";
       document.getElementById("BJMENU").style.display="";
    }else if(menu.value=="ZH"){
       document.getElementById("ZHMENU").style.display="";
       document.getElementById("BJMENU").style.display="none";
    }
 }
 
  function  changetoBjMenu(){
      document.getElementById("BJMENU").style.display="";
      document.getElementById("ZHMENU").style.display="none";
     document.getElementById("navb_2").style.color="";
     document.getElementById("navb_1").style.color="brown";
 }
 
 function  changetoZhMenu(){
      document.getElementById("BJMENU").style.display="none";
      document.getElementById("ZHMENU").style.display="";
     document.getElementById("navb_1").style.color="";
     document.getElementById("navb_2").style.color="brown";
 }

  function  changetoBjTuan(){
      document.getElementById("BJTUAN").style.display="";
      document.getElementById("ZHTUAN").style.display="none";
     document.getElementById("tuan_2").style.color="";
     document.getElementById("tuan_1").style.color="brown";
 }
 
 function  changetoZhTuan(){
      document.getElementById("BJTUAN").style.display="none";
      document.getElementById("ZHTUAN").style.display="";
     document.getElementById("tuan_1").style.color="";
     document.getElementById("tuan_2").style.color="brown";
 }
 
 
 function openMore(form,mycmd){
form.cmd.value=mycmd;
form.target="mainFrame";
form.submit();
return false;
}

function getNewTarget(){
	var viewdate =new Date();
	var newWin = viewdate.getTime();	
	return "iWork_"+newWin
}

//打开团购页面
function Open_Tuan(form,myTarget,myCmd,tuanId){
	
	form.tuanId.value=tuanId;
	form.cmd.value=myCmd; 
	var new_target=getNewTarget();
	var width = 640;
	var height = 480;
	var x = (screen.width - width) / 2;
	var y = (screen.height - height) / 2;
 	window.open('../aws_html/wait.htm',new_target,'left=' + x + ',top=' + y + ',width=' + width + ',height=' + height + ',location=no,menubar=no,toolbar=no,scrollbars=yes,status=no,directories=no,resizable=yes');
	form.target=new_target;
	form.submit();
	return false;
}