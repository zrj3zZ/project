/*
 *����:Shack Lee
 *����:2008-05-27
 */

//�Ƿ���ȫ���ı�ʶ,Ĭ�ϵ�һ����ȫ��
var is_full_screen = true;

//ȫ��ʱframe�ĳߴ�
var full_rows = "28,*,";
var full_cols = "0,12,*,";
//�ָ�ʱframe�ĳߴ�
var resume_rows = "114,*,32";
var resume_cols = "190,12,*,";

//Ĭ����ʾΪ��󻯰�ť
var full_img = "images/but_big.gif";
//ȫ��ʱͼƬΪ��ԭ��ť
var resume_img = "images/but-small.gif";

//���˵�����ʱ��ʾ��ͼƬ
var left_hidden_img = "images/control_left2.gif";
//���˵�������ʾʱ��ͼƬ
var left_normal_img = "images/control_left.gif";

//�Ҳ�˵�����ʱ��ʾ��ͼƬ
var right_hidden_img = "../images/m_but_01.jpg";
//�Ҳ�˵�������ʾʱ��ͼƬ
var right_normal_img = "../images/m_but_02.jpg";

//���������ť����frame��id
var tree_but_frame = "tree_but";
//�Ҳ�������ť����frame��id
var info_but_frame = "info_but";

//���/��ԭ��ťͼƬid
var place_img = "place_img";
//���˵�������ťͼƬid
var left_img = "timg";
//�Ҳ�˵�������ťid
var right_img = "mimg";

var div_navigator = "div_navigator";
var div_left = "div_left";
var div_right = "div_right";
var left_isclose;
/*
*��ҳ��ȫ���ͻָ�����,������frame
*/
function zoom() {
	if (is_full_screen) {//ȫ��
		parent.parent.topFrame.rows = full_rows;
		parent.parent.treeFrame.cols = full_cols;
		reverse();
		$$(parent.frames["head"],"first").style.display = "none";
		$$(parent.frames["head"],"second").style.display = "none";
		document.getElementById("div_navigator").style.display = "none";
		document.getElementById("div_small").style.display = "block";
		$$(parent.frames[tree_but_frame],div_left).style.display = "none";
		//$$(parent.parent.frames[info_but_frame],div_right).style.display = "none";
	} else {//��ԭ
		parent.topFrame.rows = resume_rows;
		parent.treeFrame.cols = resume_cols;
		reverse();
		$$(parent.frames["head"],"first").style.display = "block";
		$$(parent.frames["head"],"second").style.display = "block";
		document.getElementById("div_small").style.display = "none";
		$(div_navigator).style.display = "block";
		$$(parent.frames[tree_but_frame],div_left).style.display = "block";
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
���˵���������ʾ
*/
function hide_left() {
//alert(cols.split(",")[0]);
	cols = parent.treeFrame.cols;
	
	if("0"==cols.split(",")[0]){//��ʾ���˵�
		cols = "190"+cols.substring(cols.indexOf(","),cols.length);
		$(div_left).style.display = "block";
	}else{//�������˵�
		cols = "0"+cols.substring(cols.indexOf(","),cols.length);
		$(div_left).style.display = "none";
	}
	parent.treeFrame.cols = cols;
}

/**
�����Ҳ��˵�
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
