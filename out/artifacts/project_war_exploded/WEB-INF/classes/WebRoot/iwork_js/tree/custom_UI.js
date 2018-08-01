/*
 *作者:David.yang
 *日期:2008-05-27
 */

//是否是全屏的标识,默认第一次是全屏
var is_full_screen = true;

//全屏时frame的尺寸
var full_rows = "28,*,";
var full_cols = "0,12,*,";
//恢复时frame的尺寸
var resume_rows = "114,*,32";
var resume_cols = "190,12,*,";

//默认显示为最大化按钮
var full_img = "images/but_big.gif";
//全屏时图片为还原按钮
var resume_img = "images/but-small.gif";

//左侧菜单隐藏时显示的图片
var left_hidden_img = "images/control_left2.gif";
//左侧菜单正常显示时的图片
var left_normal_img = "images/control_left.gif";

//右侧菜单隐藏时显示的图片
var right_hidden_img = "../images/m_but_01.jpg";
//右侧菜单正常显示时的图片
var right_normal_img = "../images/m_but_02.jpg";

//左侧收缩按钮所在frame的id
var tree_but_frame = "tree_but";
//右侧收缩按钮所在frame的id
var info_but_frame = "info_but";

//最大化/还原按钮图片id
var place_img = "place_img";
//左侧菜单收缩按钮图片id
var left_img = "timg";
//右侧菜单收缩按钮id
var right_img = "mimg";

var div_navigator = "div_navigator";
var div_left = "div_left";
var div_right = "div_right";
/*
*主页面全屏和恢复功能,适用于frame
*/
function zoom() {
	if (is_full_screen) {//全屏
		parent.parent.topFrame.rows = full_rows;
		parent.parent.treeFrame.cols = full_cols;
		reverse();
		$$(parent.parent.frames["head"],"first").style.display = "none";
		$$(parent.parent.frames["head"],"second").style.display = "none";
		$(div_navigator).style.display = "none";
		$$(parent.parent.frames[tree_but_frame],div_left).style.display = "none";
		//$$(parent.parent.frames[info_but_frame],div_right).style.display = "none";
	} else {//还原
		parent.parent.topFrame.rows = resume_rows;
		parent.parent.treeFrame.cols = resume_cols;
		reverse();
		$$(parent.parent.frames["head"],"first").style.display = "block";
		$$(parent.parent.frames["head"],"second").style.display = "block";
		$(div_navigator).style.display = "block";
		$$(parent.parent.frames[tree_but_frame],div_left).style.display = "block";
		//$$(parent.parent.frames[info_but_frame],div_right).style.display = "block";
		
	}

	/*initFrameHeight();*/
}
function reverse(){
	is_full_screen = !is_full_screen;
}

/*function initFrameHeight(){
	var frame = parent.document.frames("comtentFrame").frames("ageFrame");
	var height = frame.parent.document.getElementById("ageFrame").height;			
	var iframe =  frame.document.getElementById("kunFrame");
	iframe.style.height=height;
	alert(iframe.style.height);
}*/


/**
左侧菜单隐藏与显示
*/
function hide_left() {
//alert(cols.split(",")[0]);
	cols = parent.treeFrame.cols;
	
	if("0"==cols.split(",")[0]){//显示左侧菜单
		cols = "190"+cols.substring(cols.indexOf(","),cols.length);
		$(div_left).style.display = "block";
	}else{//隐藏左侧菜单
		cols = "0"+cols.substring(cols.indexOf(","),cols.length);
		$(div_left).style.display = "none";
	}
	parent.treeFrame.cols = cols;
}
function show_left(){
		cols = parent.treeFrame.cols;
		cols = "190"+cols.substring(cols.indexOf(","),cols.length);
		$(div_left).style.display = "block";
		parent.treeFrame.cols = cols;
}

/**
隐藏右侧侧菜单
*/
function hide_right(flag) {
	cols = parent.bottomFrame.cols;
	if(flag||"165"==cols.split(",")[4]){
		cols = cols.substring(0,cols.lastIndexOf(","))+",0";
		$(div_right).style.display = "none";
	}else{
		cols = cols.substring(0,cols.lastIndexOf(","))+",165";
		$(div_right).style.display = "block";
		window.setTimeout("hide_right(true)",5000);
		
	}
	parent.bottomFrame.cols = cols;
}
function $(id) {
	return document.getElementById(id);
}
function $$(obj,id) {
	return obj.document.getElementById(id);
}
