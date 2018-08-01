function dropFocus(obj,str){
	if(obj.value==''){
		obj.value = str;
		obj.style.color='#D1D1D1';
	}else{
		obj.style.color = '#000';
	}
}

function getFocus(obj,str){
	if(obj.value==str){
		obj.value = '';
		obj.style.color = '#000';
	}else{
		obj.style.color = '#000';
	}
}

  function setInfo(obj){
	var info = obj.value;
	if(info=='请填写您的回答' || info=='' || info==null){
		obj.value = '';
		obj.style.color='#333333';
	}
}

function getInfo(obj){
	var info = obj.value;
	info = info.replace(new RegExp(" ","gm"),"");
	info = info.replace(new RegExp("&nbsp;","gm"),"");
	info = info.replace(new RegExp("<p></p>","gm"),"");
	info = info.replace(new RegExp("<br/>","gm"),"");
	info = info.replace(new RegExp("\r","gm"),"");
	info = info.replace(new RegExp("\n","gm"),"");
	if(info==''){
		obj.value = '请填写您的回答';
		obj.style.color='#D1D1D1';
	}
}

  function changeTRbgcolor(obj){
	obj.className='tr_bg1';
}

function dorpTRbgcolor(obj){
	obj.className='tr_bg2';
}
 function getuserbycode(obj){ 
	obj.value = obj.value.toUpperCase();
}
//点击分类时
 function searchbcidAndKeyword(bcid){
	var type = document.getElementById('tagtype').value;
	document.getElementById('bcid').value = bcid;
  	showTagAndMassage(bcid,type,'1');
 	return false;
}
 function showTagAndMassage(bcid,type,pageNow,pageflag){
	var keyword = document.frmMain.text.value;
//	if(bcid!='0' && bcid!=0){
//		keyword = '';
//	}
	var sid = document.frmMain.sid.value;
	for(var i=0;i<5;i++){
		if(i==type){
			document.getElementById('tagLeft_'+i).className='tags_left_1';
			document.getElementById('tagCenter_'+i).className='tags_center_1';
			document.getElementById('tagRight_'+i).className='tags_right_1';
			document.getElementById('mouse_p_'+i).className='select_1';
		}else{
			document.getElementById('tagLeft_'+i).className='tags_left_2';
			document.getElementById('tagCenter_'+i).className='tags_center_2';
			document.getElementById('tagRight_'+i).className='tags_right_2';
			document.getElementById('mouse_p_'+i).className='select_2';
		}
	}
	
	Ext.Ajax.request({
		url: '../ajax',
		params: {
			sid:sid,
			text:keyword,
			bcid:bcid,
			type:type,
			pageflag:pageflag,
			cmd:'oaknow_search_getone',
			pageNow:pageNow
		},
		failure:function(response,options){
			return false;
		},
		success:function(response,options){
			if(response.responseText.length>0){
				var str = response.responseText;
				document.getElementById('showBox1').innerHTML=str;
			}
		}
	});
	//document.frmMain.pageNow.value = 1;
	//setHeight();
}

//打开问题页面

