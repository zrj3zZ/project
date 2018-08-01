//返回当前页面高度
function pageHeight(){
    if ($.browser.msie) {
        return document.compatMode == "CSS1Compat" ? document.documentElement.clientHeight
                : document.body.clientHeight;
    } else {
        return self.innerHeight;
    }
    return $(window).height(); 
};
 
 
 
//返回当前页面宽度
function pageWidth(){
    if ($.browser.msie) {
        return document.compatMode == "CSS1Compat" ? document.documentElement.clientWidth
                : document.body.clientWidth;
    } else {
        return self.innerWidth;
    }
    return $(window).width();  
     
};

//判断一个串实际长度（1个中文2个单位）
function length2(txtValue){
 var cArr = txtValue.match(/[^\x00-\xff]/ig);
 return txtValue.length + (cArr == null ? 0 : cArr.length);
}
 

function trim(str){  //删除左右两端的空格
 return str.replace(/(^\s*)|(\s*$)/g, "");
}
function ltrim(str){  //删除左边的空格
 return str.replace(/(^\s*)/g,"");
}
function rtrim(str){  //删除右边的空格
 return str.replace(/(\s*$)/g,"");
}

function high(which2) { 
	theobject=which2 ; highlighting=setInterval("highlightit(theobject)",100) ; 
} 
function low(which2){  
	clearInterval(highlighting) ; 
	which2.filters.alpha.opacity=60 ; 
} 
function highlightit(cur2){  
	if (cur2.filters.alpha.opacity<100) { 
		cur2.filters.alpha.opacity+=10;
	} else if (window.highlighting){ 
		clearInterval(highlighting) ;
	} 
}  

var d=0;
function JM_fade(ob){
if (d==0) {ob.filters.alpha.opacity+=1} else {ob.filters.alpha.opacity-=1}
if (ob.filters.alpha.opacity==100){d=1;} else if (ob.filters.alpha.opacity==0){d=0}
}

function getNewTarget(){
	var viewdate =new Date();
	var newWin = viewdate.getTime();	
	return "iWork_"+newWin
}

function getCookieVal (offset) {
   var endstr = document.cookie.indexOf (";", offset);
   if (endstr == -1)
     endstr = document.cookie.length;
  return unescape(document.cookie.substring(offset, endstr));
}
function GetCookie (name) {
        var arg = name + "=";
      var alen = arg.length;
        var clen = document.cookie.length;
        var i = 0;
        while (i < clen) {
               var j = i + alen;
                if (document.cookie.substring(i, j) == arg)
                        return getCookieVal (j);
                i = document.cookie.indexOf(" ", i) + 1;
                        if (i == 0)
                                break;
               }
   return null;
}

/**
  设置Cookie
*/
function SetCookie (name, value) {
        var argv = SetCookie.arguments;
        var argc = SetCookie.arguments.length;
        var expires = (argc > 2) ? argv[2] : null;
        var path = (argc > 3) ? argv[3] : null;
        var domain = (argc > 4) ? argv[4] : null;
        var secure = (argc > 5) ? argv[5] : false;
        document.cookie = name + "=" + escape (value) +
                ((expires == null) ? "" : ("; expires=" +
expires.toGMTString())) +
                ((path == null) ? "" : ("; path=" + path)) +
                ((domain == null) ? "" : ("; domain=" + domain)) +
                ((secure == true) ? "; secure" : "");

}


/**
IsNumber: 用于判断一个数字型字符串是否为数值型，
还可判断是否是正数或负数，返回值为true或false
string: 需要判断的字符串
sign: 若要判断是正负数是使用，是正用'+'，负'-'，不用则表示不作判断
*/
function IsNumber(string,sign)
{
var number;
if (string==null) return false;
if ((sign!=null) && (sign!='-') && (sign!='+'))
{
alert('IsNumber(string,sign)的参数出错：\nsign为null或"-"或"+"');
return false;
}
number = new Number(string);
if (isNaN(number))
{
return false;
}
else if ((sign==null) || (sign=='-' && number<0) || (sign=='+' && number>0))
{
return true;
}
else
return false;
}




/**
IsDate: 用于判断一个字符串是否是日期格式的字符串

返回值：
true或false

参数：
DateString： 需要判断的字符串
Dilimeter ： 日期的分隔符，缺省值为'-'
Author: PPDJ
sample:
var date = '1999-1-2';
if (IsDate(date))
{
alert('You see, the default separator is "-");
}
date = '1999/1/2";
if (IsDate(date,'/'))
{
alert('The date\'s separator is "/");
}
*/
//IsDate('2003-09-09');
function IsDate(DateString , Dilimeter)
{
if (DateString==null || DateString=='') return true;
if (Dilimeter=='' || Dilimeter==null)
Dilimeter = '-';
var tempy='';
var tempm='';
var tempd='';
var tempArray;
if (DateString.length<8 && DateString.length>10)
return false; 
tempArray = DateString.split(Dilimeter);
if (tempArray.length!=3)
return false;
if (tempArray[0].length==4)
{
tempy = tempArray[0];
tempd = tempArray[2];
}
else
{
tempy = tempArray[2];
tempd = tempArray[1];
}
tempm = tempArray[1];
var tDateString = tempy + '/'+tempm + '/'+tempd+' 8:0:0';//加八小时是因为我们处于东八区
var tempDate = new Date(tDateString);
if(tempd.substring(0,1)=='0'){
	tempd=tempd.substring(1);	
}
if(tempm.substring(0,1)=='0'){
	tempm=tempm.substring(1);	
}

if (isNaN(tempDate))
return false;
if (((tempDate.getUTCFullYear()).toString()==tempy) && (tempDate.getMonth()==parseInt(tempm)-1) && (tempDate.getDate()==parseInt(tempd)))
{
return true;
}
else
{
return false;
}
} 

/**
*校验两个日期的先后
*返回值：
*如果其中有一个日期为空，校验通过,          返回true
*如果起始日期早于等于终止日期，校验通过，   返回true
*如果起始日期晚于终止日期，                 返回false    参考提示信息： 起始日期不能晚于结束日期。
*/
function checkDateEarlier(strStart,strEnd)
{
    //如果有一个输入为空，则通过检验
    if (( strStart == "" ) || ( strEnd == "" ))
        return true;
    var arr1 = strStart.split("-");
    var arr2 = strEnd.split("-");
    var date1 = new Date(arr1[0],parseInt(arr1[1].replace(/^0/,""),10) - 1,arr1[2]);
    var date2 = new Date(arr2[0],parseInt(arr2[1].replace(/^0/,""),10) - 1,arr2[2]);
    if(arr1[1].length == 1)
        arr1[1] = "0" + arr1[1];
    if(arr1[2].length == 1)
        arr1[2] = "0" + arr1[2];
    if(arr2[1].length == 1)
        arr2[1] = "0" + arr2[1];
    if(arr2[2].length == 1)
        arr2[2]="0" + arr2[2];
    var d1 = arr1[0] + arr1[1] + arr1[2];
    var d2 = arr2[0] + arr2[1] + arr2[2];
    if(parseInt(d1,10) > parseInt(d2,10)){
       return false;
    }else{
       return true;
    }
}

function isMailAddress(name) // E-mail值检测
{
	if(name=='')return true;
	i = name.indexOf("@");
	j = name.lastIndexOf("@");
	if(i == -1)
	return false;
	if(i != j)
	return false;
	if(i == name.length)
	return false;
	return true;
}
function setFormRowMenu(obj,rowData){
	var sourceIMG=obj.src.substring(obj.src.lastIndexOf("/")+1);
	if(sourceIMG=="collapsed_button.gif"){
		obj.src="iwork_img/expanded_button.gif";
		rowData.style.display="";
	}else{
		obj.src="iwork_img/collapsed_button.gif";
		rowData.style.display="none";
	}
}

/** 货币格式化函数 **/
function formatCurrency(num) {
    num = num.toString().replace(/\$|\,/g,'');
    if(isNaN(num))
    num = "0";
    sign = (num == (num = Math.abs(num)));
    num = Math.floor(num*100+0.50000000001);
    cents = num%100;
    num = Math.floor(num/100).toString();
    if(cents<10)
    cents = "0" + cents;
    for (var i = 0; i < Math.floor((num.length-(1+i))/3); i++)
    num = num.substring(0,num.length-(4*i+3))+','+
    num.substring(num.length-(4*i+3));
    return (((sign)?'':'-')  + num + '.' + cents);
}
/** 还原货币格式化函数 **/
function restoreFormatCurrency(num){
   	var num1=num.replace(',','').replace(/,/g,''); 
    return num1;
}

 
//字符串全部替换 
function replaceAll2(streng, soeg, erstat){
	var st = streng;
	if(soeg.length == 0)
		return st;
	var idx = st.indexOf(soeg);
	while(idx >= 0){
		st = st.substring(0,idx) + erstat + st.substr(idx+soeg.length);
		idx = st.indexOf(soeg);
	}
	return st;  
}

  function authority_chooser(obj){	  	
	  var code = obj.value;	
	  var url = "authorityAddressBookAction!index.action?code=" + encodeURI(code)+"&target="+encodeURI(obj.name); 
	  art.dialog.open(url,{
			title: '授权地址簿', 
			lock:true,
			background: '#999', // 背景色
		    opacity: 0.87,	// 透明度
		    width: 650,
			height: 550
		 });
		art.dialog.data('targetEl',obj);
  }  

  // 单选地址薄 
  //	参考录入参数列表 参数之间以;分隔，例如isOrg=true;irRole=true;currentDept=true
  //	isOrg=true可通过组织结构进行人员选择
  //	isRole=true可通过角色信息进行人员选择
  //	isGroup=true可通过团队进行人员选择
  //    startDept=部门编号   例如：startDept=1243, (注：1243为总裁办部门ID)
  //	currentDept=true |false  则只显示当前用户所在部门以下的人员列表
  //	parentDept=true |false  则只示当前用户所在部门以下的人员列表
  //	parentDept、currentDept和startDept三者设置互斥，当三个标签同时存在时，优先级依次为：parentDept、currentDept、startDept。
  //	targetUserID=字段名  点击【确定】后，将选择的帐号插入至指定字段
  //	targetUserName=字段名  点击【确定】后，将选择的姓名插入至指定字段
  //	targetDeptName=字段名   例如：targetUserName=DPINPUT
  //	targetDeptId=字段名   例如：targetUserName=IDINPUT
  //	defaultField=字段名 默认字段，如不需要配置参考录入，其他参数可为'',
  //	注：(当设置以下时属性值时，可以按照字段设置插入不同字段) (当未设置以下时属性值时，默认将“帐号<姓名>”插入字段) 
  function radio_book(parentDept, currentDept, startDept, targetUserId,targetUserNo, targetUserName, targetDeptId, targetDeptName, defaultField) {
	  var url = "radiobook_index.action?1=1"; 
		if(parentDept!=''){
			url+="&parentDept="+parentDept;
		}
		if(currentDept!=''){
			url+="&currentDept="+currentDept;
		}
		if(startDept!=''){
			url+="&startDept="+startDept;
		}
		if(targetUserId!=''){
			url+="&targetUserId="+targetUserId;
		}
		if(targetUserNo!=''){
			url+="&targetUserNo="+targetUserNo;
		}
		if(targetUserName!=''){
			url+="&targetUserName="+targetUserName;
		}
		if(targetDeptId!=''){
			url+="&targetDeptId="+targetDeptId;
		}
		if(targetDeptName!=''){
			url+="&targetDeptName="+targetDeptName;
		}
		if(defaultField!=''){
			url+="&defaultField="+defaultField;
		}
		//获得input内容
		var v = document.getElementById(defaultField);
		if(v.value!=""){
			var val  = encodeURI(v.value);
			url+="&input="+val+""; 
		}
		art.dialog.open(url,{
			id:"radioBookDialog",
			title: '单选地址簿',
			lock:true,
			background: '#999', // 背景色
		    opacity: 0.87,	// 透明度
		    width: 650,
			height: 550
		});
	}
  // 多选地址薄
  //	参考录入参数列表 参数之间以;分隔，例如isOrg=true;irRole=true;currentDept=true
  //	isOrg=true可通过组织结构进行人员选择
  //	isRole=true可通过角色信息进行人员选择
  //	isGroup=true可通过团队进行人员选择
  //    startDept=部门编号   例如：startDept=1243, (注：1243为总裁办部门ID)
  //	currentDept=true |false  则只显示当前用户所在部门以下的人员列表
  //	parentDept=true |false  则只示当前用户所在部门以下的人员列表
  //	parentDept、currentDept和startDept三者设置互斥，当三个标签同时存在时，优先级依次为：parentDept、currentDept、startDept。
  //	targetUserID=字段名  点击【确定】后，将选择的帐号插入至指定字段, 暂时为空
  //	targetUserName=字段名  点击【确定】后，将选择的姓名插入至指定字段, 暂时为空
  //	targetDeptName=字段名   例如：targetUserName=DPINPUT, 暂时为空
  //	targetDeptId=字段名   例如：targetUserName=IDINPUT, 暂时为空
  //	defaultField=字段名 默认字段
  //	注：(当设置以下时属性值时，可以按照字段设置插入不同字段) (当未设置以下时属性值时，默认将“帐号<姓名>”插入字段) 
  function multi_book(parentDept, currentDept, startDept, targetUserId,targetUserNo, targetUserName, targetDeptId, targetDeptName, defaultField) {
	  var url = "multibook_index.action?1=1";
		if(parentDept!=''){
			url+="&parentDept="+parentDept;
		}
		if(currentDept!=''){
			url+="&currentDept="+currentDept;
		}
		if(startDept!=''){
			url+="&startDept="+startDept;
		} 
		if(targetUserId!=''){
			url+="&targetUserId="+targetUserId;
		}
		if(targetUserNo!=''){
			url+="&targetUserNo="+targetUserNo;
		}
		if(targetUserName!=''){
			url+="&targetUserName="+targetUserName;
		}
		if(targetDeptId!=''){
			url+="&targetDeptId="+targetDeptId;
		}
		if(targetDeptName!=''){
			url+="&targetDeptName="+targetDeptName;
		}
		if(defaultField!=''){
			url+="&defaultField="+defaultField;
		}
		//获得input内容
		var v = document.getElementById(defaultField);
		if(v.value!=""){
			url+="&input="+v.value;
		} 
		art.dialog.open(encodeURI(url),{
			id:"multiBookDialog",
			title: '多选地址簿',
			lock:true,
			background: '#999', // 背景色
		    opacity: 0.87,	// 透明度
		    width: 650,
			height: 550
		});
	}
 
  function hr_book(targetUserId,targetUserName,defaultField) {
	  var url = "hrDataAddressBook_index.action?1=1";
	  if(targetUserId!=''){
		  url+="&targetUserId="+targetUserId;
	  }
	  if(targetUserName!=''){
		  url+="&targetUserName="+targetUserName;
	  }
	  if(defaultField!=''){
		  url+="&defaultField="+defaultField;
	  }
	  //获得input内容
	  var v = document.getElementById(defaultField);
	  if(v.value!=""){
		  url+="&input="+v.value;
	  } 
	  art.dialog.open(url,{
			id:"hrBookDialog",
			title: '人员选择',
			lock:true,
			background: '#999', // 背景色
		    opacity: 0.87,	// 透明度
		    width: 650,
			height: 550
		});
  }
  // 部门地址薄
  //	参考录入参数列表 参数之间以;分隔，例如isOrg=true;irRole=true;currentDept=true
  //	isOrg=true可通过组织结构进行人员选择
  //	isRole=true可通过角色信息进行人员选择
  //	isGroup=true可通过团队进行人员选择
  //    startDept=部门编号   例如：startDept=1243, (注：1243为总裁办部门ID)
  //	currentDept=true |false  则只显示当前用户所在部门以下的人员列表
  //	parentDept=true |false  则只示当前用户所在部门以下的人员列表
  //	parentDept、currentDept和startDept三者设置互斥，当三个标签同时存在时，优先级依次为：parentDept、currentDept、startDept。
  //	targetUserID=字段名  点击【确定】后，将选择的帐号插入至指定字段, 暂时为空
  //	targetUserName=字段名  点击【确定】后，将选择的姓名插入至指定字段, 暂时为空
  //	targetDeptName=字段名   例如：targetUserName=DPINPUT, 暂时为空
  //	targetDeptId=字段名   例如：targetUserName=IDINPUT, 暂时为空
  //	defaultField=字段名 默认字段
  //	注：(当设置以下时属性值时，可以按照字段设置插入不同字段) (当未设置以下时属性值时，默认将“帐号<姓名>”插入字段) 
  function dept_book(parentDept, currentDept, startDept, targetUserId,targetUserNo, targetUserName, targetDeptId, targetDeptName, defaultField) {
	  var url = "deptbook_index.action?1=1";
		if(parentDept!=''){
			url+=encodeURI("&parentDept="+parentDept);
		}
		if(currentDept!=''){
			url+=encodeURI("&currentDept="+currentDept);
		}
		if(startDept!=''){
			url+=encodeURI("&startDept="+startDept);
		} 
		if(targetUserId!=''){
			url+=encodeURI("&targetUserId="+targetUserId);
		}
		if(targetUserNo!=''){
			url+=encodeURI("&targetUserNo="+targetUserNo);
		}
		if(targetUserName!=''){
			url+=encodeURI("&targetUserName="+targetUserName);
		}
		if(targetDeptId!=''){
			url+=encodeURI("&targetDeptId="+targetDeptId);
		}
		if(targetDeptName!=''){
			url+=encodeURI("&targetDeptName="+targetDeptName);
		}
		if(defaultField!=''){
			url+=encodeURI("&defaultField="+defaultField);
		}
		//获得input内容
		var v = document.getElementById(defaultField);
		if(v!=null&&v.value!=""){
			url+=encodeURI("&input="+v.value);
		}
		art.dialog.open(url,{
			id:"deptBookDialog",
			title: '部门地址簿',
			lock:true,
			background: '#999', // 背景色
		    opacity: 0.87,	// 透明度
		    width: 350,
			height: 550
		});
	}
  
  function dept_multi_book(parentDept, currentDept, startDept, targetUserId,targetUserNo, targetUserName, targetDeptId, targetDeptName, defaultField) {
	  var url = "deptbook_multi_index.action?1=1";
	  if(parentDept!=''){
		  url+="&parentDept="+parentDept;
	  }
	  if(currentDept!=''){
		  url+="&currentDept="+currentDept;
	  }
	  if(startDept!=''){
		  url+="&startDept="+startDept;
	  } 
	  if(targetUserId!=''){ 
		  url+="&targetUserId="+targetUserId;
	  }
	  if(targetUserNo!=''){
		  url+="&targetUserNo="+targetUserNo;
	  }
	  if(targetUserName!=''){
		  url+="&targetUserName="+targetUserName;
	  }
	  if(targetDeptId!=''){
		  url+="&targetDeptId="+targetDeptId;
	  }
	  if(targetDeptName!=''){
		  url+="&targetDeptName="+targetDeptName;
	  }
	  if(defaultField!=''){
		  url+="&defaultField="+defaultField;
	  }
	  //获得input内容
	  var v = document.getElementById(defaultField);
	  if(v!=null&&v.value!=""){
		  url+="&input="+v.value;
	  }
	  url=encodeURI(url);
	  art.dialog.open(url,{
			id:"deptBookDialog",
			title: '多选部门地址簿',
			lock:true,
			background: '#999', // 背景色
			opacity: 0.87,	// 透明度
			width: 350,
			height: 550
		});
  }
  function real_book(targetUserId,targetUserNo, targetUserName, targetDeptId, targetDeptName, defaultField) {
		var url = "real_book_index.action?1=1";
		if(targetUserId!=''){ 
			url+="&targetUserId="+targetUserId;
		}
		if(targetUserNo!=''){
			url+="&targetUserNo="+targetUserNo;
		}
		if(targetUserName!=''){
			url+="&targetUserName="+targetUserName;
		}
		if(targetDeptId!=''){
			url+="&targetDeptId="+targetDeptId;
		}
		if(targetDeptName!=''){
			url+="&targetDeptName="+targetDeptName;
		}
		if(defaultField!=''){
			url+="&defaultField="+defaultField;
		}
		//获得input内容
		var v = document.getElementById(defaultField);
		if(v.value!=""){
			url+="&input="+v.value;
		}
		art.dialog.open(url,{
			id:"deptBookDialog",
			title: '委托地址簿',
			lock:true,
			background: '#999', // 背景色
		    opacity: 0.87,	// 透明度
		    width: 350,
			height: 550
		});
	}
	//打开数据字典
	function openDictionary(uuid,subformkey,subformid){
		var pageUrl = "sys_dictionary_runtime_show.action?dictionaryUUID="+uuid+"&subformkey="+subformkey+"&subformid="+subformid;		
		var obj = $("#iformMain").serialize();
		art.dialog.open(pageUrl,{
				id:'DictionaryDialog',
				title:"数据选择",
				lock:false,
				background: '#999', // 背景色
			    opacity: 0.87,	// 透明度
			    width:800,
				height:520
			});
	} 
//打开数据字典
	function openSubDictionary(id,rowIdPrefix){
		var obj = $("#iformMain").serialize();
		var pageUrl = "sys_dictionary_runtime_show.action?dictionaryId="+id+"&rowIdPrefix="+rowIdPrefix;
		art.dialog.open(pageUrl,{
			id:'DictionaryDialog',
			title:"数据选择",
			lock:true,
			background: '#999', // 背景色
		    opacity: 0.87,	// 透明度
		    width:800,
			height:520
		});
	}
	//每个附件在页面上的显示样式
	function buildFileElementHtml(colName,fileDivId,fileName,fileUUID,fileSrc,removeFlag){
			/*if (fileName.length > 20) {
					fileName = fileName.substr(0,15) + '...';
			} */
			var html = '<div  id="'+fileDivId+'" style="background-color: #F5F5F5;border-bottom: 1px solid #E5E5E5;font: 11px Verdana, Geneva, sans-serif;padding: 5px;width: 200px">';
			html 	+= '	<div style="align:right;float: right;">';
			if(removeFlag){
				html 	+= '		<a href="javascript:uploadifyReomve(\''+colName+'\',\''+fileUUID+'\',\''+fileDivId+'\');"><img src="/iwork_img/del3.gif"/></a>';
			}
			html 	+= '	</div>';
			html 	+= '	<span><a href="uploadifyDownload.action?fileUUID='+fileUUID+'" target="_blank"><img src="/iwork_img/attach.png"/>'+fileName+'</a></span>';
			html 	+= '</div>';
			return html;
	}
	//点击附件上传按钮调用的方法，用于弹出上传附件窗口
	function uploadifyDialog(dialogId,fieldName,divId,sizeLimit,multi,fileExt,fileDesc){
		if(dialogId==null||dialogId==""||fieldName==null||fieldName==""||divId==null||divId==""){
			alert('参数不正确');
			return false;
		}
		var pageUrl = 'showUploadifyPage.action?test=1';
		if(fieldName!=null||fieldName!=""){
			pageUrl+=('&parentColId='+fieldName);
		}
		if(divId!=null||divId!=""){
			pageUrl+=('&parentDivId='+divId);
		}
		if(sizeLimit!=null||sizeLimit!=""){
			pageUrl+=('&sizeLimit='+sizeLimit);
		}
		if(multi!=null||multi!=""){
			pageUrl+=('&multi='+multi);
		}
		if(fileExt!=null||fileExt!=""){
			pageUrl+=('&fileExt='+fileExt);
		}
		if(fileDesc!=null||fileDesc!=""){
			pageUrl+=('&fileDesc='+fileDesc);
		}
		art.dialog.open(pageUrl,{
			id:dialogId,
			title: '上传附件',
			lock:true,
			background: '#999', // 背景色
		    opacity: 0.87,	// 透明度
		    width:650,
			height:510
		}); 
		return ;
	}
	
	//点击附件上传按钮调用的方法，用于弹出上传附件窗口
	function showBaseUploadDlg(dialogId,fieldName,divId){
		if(dialogId==null||dialogId==""||fieldName==null||fieldName==""||divId==null||divId==""){
			alert('参数不正确');
			return false;
		}
		var pageUrl = 'show_base_upload.action?parentColId='+fieldName+'&parentDivId='+divId;
		art.dialog.open(pageUrl,{
			id:dialogId,
			title: '上传附件',
			lock:true,
			background: '#999', // 背景色
		    opacity: 0.87,	// 透明度
		    width:650,
			height:510
		 });
		return;
	}
	
	//删除附件，点击删除附件所调用的方法
	function uploadifyReomve(colName,uuid,divId){
		if(uuid!=null&&uuid!=""){
			if(colName=="SMJ"){
				if(confirm("确定要删除该扫描件吗？")){
					var flag = uploadifyRemoveServer(uuid);
					if(flag){
						removeFileObject(uuid);
						uploadifyRemovePage(divId);
						reSetUUIDs(colName,uuid);
						if($("#submitbtn")!=null){
							if(typeof(mainFormAlertFlag)!='undefined'){
								mainFormAlertFlag=false;
							}
							if(typeof(saveSubReportFlag)!='undefined'){
								saveSubReportFlag=false;
							}
							saveForm();//$("#submitbtn").click();
						}
					}else{
						alert("删除失败");
					}
				}
			}else if(colName=="ZDFJ"){
				var flag = uploadifyRemoveServer(uuid);
				if(flag){
					removeFileObject(uuid);
					uploadifyRemovePage(divId);
					reSetUUIDs(colName,uuid);
					if($("#submitbtn")!=null){
						if(typeof(mainFormAlertFlag)!='undefined'){
							mainFormAlertFlag=false;
						}
						if(typeof(saveSubReportFlag)!='undefined'){
							saveSubReportFlag=false;
						}
						saveForm();
						$("#ZDMC").val("");
						$("button").show();
					}
				}else{
					alert("删除失败");
				}
			}else{
				var flag = uploadifyRemoveServer(uuid);
				if(flag){
					removeFileObject(uuid);
					uploadifyRemovePage(divId);
					reSetUUIDs(colName,uuid);
					if($("#submitbtn")!=null){
						if(typeof(mainFormAlertFlag)!='undefined'){
							mainFormAlertFlag=false;
						}
						if(typeof(saveSubReportFlag)!='undefined'){
							saveSubReportFlag=false;
						}
						saveForm();//$("#submitbtn").click();
					}
				}else{
					alert("删除失败");
				}
			}
		}
	}
	function uploadifyRemoveImg(colName,uuid){ 
		var flag = uploadifyRemoveServer(uuid);
		if(flag){
			reSetUUIDs(colName,uuid);
			saveForm();
			this.location.reload();
		}else{
			alert("删除失败");
		}
	}
	//删除后台附件
	function uploadifyRemoveServer(uuid){
		var flag = true;
		try{
			flag = removeUploadFileItemEvent(uuid);
		}catch(e){}
		if(!flag){
			return false;
		}
		$.ajax({
			   type: "POST",
			   async : false,
			   url: "uploadifyRemove.action",
			   data: "fileUUID="+uuid,
			   success: function(msg){
				 if(msg!=null&&msg!=""){
					var resJson = strToJson(msg);
					var r = resJson.flag;
					if(r==true||r=="true"){
						flag=true;
					}else{
						flag=false;
					}
				 }
			   },
			   error:function(msg){
					flag=false;
			   }
		});
		return flag;
	}
	//删除页面附件显示
	function uploadifyRemovePage(divId){
		if($("#"+divId)!=null){
			$("#"+divId).remove();
		}
	}
	//从原有的uuid串中删除指定的uuid串
	function removeUUID(srcUUIDS,newUUIDS){
		if(srcUUIDS.indexOf(","+newUUIDS+",")>0){
			srcUUIDS = srcUUIDS.replace(","+newUUIDS+",",",");
		}else if(srcUUIDS.indexOf(","+newUUIDS)>0){
			srcUUIDS = srcUUIDS.replace(","+newUUIDS,"");
		}else if(srcUUIDS.indexOf(newUUIDS+",")==0){
			srcUUIDS = srcUUIDS.replace(newUUIDS+",","");
		}else if(srcUUIDS==newUUIDS){
			srcUUIDS="";
		}
		return srcUUIDS;
	}
	//往原有的UUID串中增加uuid串
	function insertUUID(srcUUIDS,newUUIDS){
		if(srcUUIDS==""){
			srcUUIDS=newUUIDS;
		}else{
			srcUUIDS+=","+newUUIDS;
		}
		return srcUUIDS;
	}
	//重置附件字段UUID的值
	function reSetUUIDs(colName,fileUUID){
		var uuids = $("#"+colName).attr("value");
		var newuuids = removeUUID(uuids,fileUUID);
		$("#"+colName).attr("_value",newuuids);
		$("#"+colName).attr("value",newuuids);
	}
	//把json字符串转换成json对象
	function strToJson(str){
	    var json = (new Function("return " + str))();
	    return json;
	}
	 
	//加载系统提示
	function showSysTips(){
		$.post("sysmq_showtip.action",{type:"tip"},function(data){
	         if(data!=''){
	        	 try{
	        		art.dialog.tips(data,2);
	        	 }catch(e){
	        		 alert(data);  
	        	 }
	         }
	    });
	}
	//把对象转换成json字符串，【支持object,array,string,function,number,boolean,regexp *】
	function objToJsonString(object){
		var type = typeof object;
	     if ('object' == type) { 
	       if (Array == object.constructor) type = 'array'; 
	       else if (RegExp == object.constructor) type = 'regexp'; 
	       else type = 'object'; 
	     } 
	     switch (type) { 
			 case 'undefined': 
			 case 'unknown': 
			   return; 
			   break; 
			 case 'function': 
			 case 'boolean': 
			 case 'regexp': 
			   return object.toString(); 
			   break; 
			 case 'number': 
			   return isFinite(object) ? object.toString() : 'null'; 
			   break; 
			 case 'string': 
			   return '"' + object.replace(/(\\|\")/g, "\\$1").replace(/\n|\r|\t/g, function() { 
				 var a = arguments[0]; 
				 return (a == '\n') ? '\\n': (a == '\r') ? '\\r': (a == '\t') ? '\\t': "" 
			   }) + '"'; 
			   break; 
			 case 'object': 
			   if (object === null) return 'null'; 
			   var results = []; 
			   for (var property in object) { 
				 var value = objToJsonString(object[property]); 
				 if (value !== undefined){
				 	if(value!="[]"){
				 		results.push(objToJsonString(property) + ':' + value); 
				 	}
				 }
			   } 
			   return '{' + results.join(',') + '}'; 
			   break; 
			 case 'array': 
			   var results = []; 
			   for (var i = 0; i < object.length; i++) { 
				 var value = objToJsonString(object[i]); 
				 if (value !== undefined) results.push(value); 
			   } 
			   return '[' + results.join(',') + ']'; 
			   break; 
	     } 
	}
	function setSubFormField(index,fieldName,value){
		$("#"+index+"_"+fieldName).val(value);
	}
	function updateUploadify(fileUUID,filename){
	
		
		
		
		
			
		/*//金格插件	
		var pageUrl=encodeURI("openFormPage.action?formid=156&demId=71&INSTANCEID=99999&NOTICETEXT="+fileUUID+"&NOTICEFILE="+fileUUID+"&FILENAME="+filename);
	
		var win_width = window.screen.width;
		
		var target = "_blank";
		var page = window.open('form/loader_frame.html',target,'width='+win_width+',height=800,top=50,left=150,location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=yes');
		*/
		/*卓正插件*/
		var pageUrl=encodeURI("testIndex.action?NOTICEFILE="+fileUUID);
		var target = "_blank";
		var win_width =100;
		 //获得窗口的垂直位置 
        var iTop = (window.screen.availHeight - 30 - 100) / 2; 
        //获得窗口的水平位置 
        var iLeft = (window.screen.availWidth - 10 - 100) / 2; 
		var page = window.open('form/loader_frame.html',target,'width='+win_width+',left='+iLeft+',top='+iTop+',height=100,location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=yes');
		page.location = pageUrl;
		
		return;
	}
	
	function aloneWebOpenVersion(uuid,username,url,fj){
		var pageUrl =encodeURI("readLsbb.action?noticetext="+uuid+"&fj="+fj);
		art.dialog.open(pageUrl,{
			id:'Category_show',
			title:"历史版本",
			lock:true,
			background: '#999', // 背景色
		    opacity: 0.87,	// 透明度
		    width:'90%',
		    height:'90%'
		 });
		}
	function aloneUploadifyReomve(colName,uuid,divId){
		window.onbeforeunload = null;
		$.messager.confirm('确认','确认删除?',function(result){
		 	if(result){
		 		if(uuid!=null&&uuid!=""){
					var flag = uploadifyRemoveServer(uuid);
					if(flag){
						removeFileObject(uuid);
						uploadifyRemovePage(divId);
						reSetUUIDs(colName,uuid);
						if($("#submitbtn")!=null){
							if(typeof(mainFormAlertFlag)!='undefined'){
								mainFormAlertFlag=false;
							}
							if(typeof(saveSubReportFlag)!='undefined'){
								saveSubReportFlag=false;
							}
						}
					if($("#aloneTable tr").length<=1){
							uploadifyRemovePage("aloneTable");
						}
					saveForm();
					//window.location.reload();
					}else{
						alert("删除失败");
					}
				}
		 	}
		 });
		
	}
	function xpbcUploadifyReomve(colName,uuid,divId){
		/*$.messager.confirm('确认','确认删除?',function(result){*/
		 	/*if(result){*/
		 		if(uuid!=null&&uuid!=""){
					var flag = uploadifyRemoveServer(uuid);
					if(flag){
						removeFileObject(uuid);
						uploadifyRemovePage(divId);
						reSetUUIDs(colName,uuid);
						if($("#submitbtn")!=null){
							if(typeof(mainFormAlertFlag)!='undefined'){
								mainFormAlertFlag=false;
							}
							if(typeof(saveSubReportFlag)!='undefined'){
								saveSubReportFlag=false;
							}
						}
					if($("#aloneTable tr").length<=1){
							uploadifyRemovePage("aloneTable");
						}
					window.location.reload();
					}else{
						alert("删除失败");
					}
				}
		 	/*}*/
		 /*});*/
		
	}
	
	function removeFileObject(uuid){
		$.ajax({
			   type: "POST",
			   async : false,
			   url: "zqb_file_remove.action",
			   data: {fileUUID:uuid},
			   success: function(msg){
				 if(msg!=null&&msg!=""){
					 if(msg=='success'){
						flag=true;
					 }else{
						flag=false;
					 }
				 }
			   },
			   error:function(msg){
					flag=false;
			   }
		});
	}
	
	function setSubFormField(index,fieldName,value){
		$("#"+index+"_"+fieldName).val(value);
	}