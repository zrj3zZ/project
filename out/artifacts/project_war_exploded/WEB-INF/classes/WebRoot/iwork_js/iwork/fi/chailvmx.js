 $().ready(function() {
	 sumPageNum();
	 sumKXNum();
	 sumHZNum();
	 /*
	 setHsbz();
	 setTxbz();
	 setJTbz();
	 setYZbz(); 
	 setQTbz();
	 sumBZXJ();*/
	 
	// 汇总合同总金额
	 sumAll();
 }); 
 
 function setA1HzJe(){
	 //var hz = $("#HPHZJE").val();
	 //if(hz==""){
		 kz = $("#HPKZJE").val();
		 $("#HPHZJE").val(kz);
		 setA6HzJe();
	// }
 }
 function setA2HzJe(){
	 //var hz = $("#TPHZJE").val();
	 //if(hz==""){
		 kz = $("#TPKZJE").val();
		 $("#TPHZJE").val(kz);
		 setA6HzJe();
	// }
 }
 function setA3HzJe(){
	 //var hz = $("#ZSHZJE").val();
	 //if(hz==""){
		 kz = $("#ZSKZJE").val();
		 $("#ZSHZJE").val(kz);
		 setA6HzJe();
	 //}
 }
 function setA4HzJe(){
	 //var hz = $("#JTHZJE").val();
	// if(hz==""){
		 kz = $("#JTKZJE").val();
		 $("#JTHZJE").val(kz);
		 setA6HzJe();
	 //}
 }
 function setA5HzJe(){
	// var hz = $("#QTHZJE").val();
	// if(hz==""){
		 kz = $("#QTKZJE").val();
		 $("#QTHZJE").val(kz);
		 setA6HzJe();
	// }
 }
 function setA6HzJe(){
	 sumHZNum();
 }
 
 //汇总总报销金额
 function sumAll(){
	 var xjje1 = $("#XJHZJE").val();
	 // var xjje2 = $("#XJJE").val();
	 var hzje = Number(xjje1);//+Number(xjje2);
	 $("#BXJE").val(hzje);
 }
 //设置伙食补助
 function setHsbz(){
	var rs =  $("#HSBZRS").val();
	var bz =  $("#HSBZBZ").val();
	var ts =  $("#HSBZTS").val();
	var je = Number(rs)*Number(bz)*Number(ts);
	$("#HSBZJE").val(je); 
	sumBZXJ();
 }
 //设置通讯补助
 function setTxbz(){
	 var rs =  $("#TXBZRS").val();
	 var bz =  $("#TXBZBZ").val();
	 var ts =  $("#TXBZTS").val();
	 var je = Number(rs)*Number(bz)*Number(ts);
	 $("#TXBZJE").val(je); 
	 sumBZXJ();
 }
 //设置交通补助
 function setJTbz(){
	 var rs =  $("#JTBZRS").val();
	 var bz =  $("#JTBZBZ").val();
	 var ts =  $("#JTBZTS").val();
	 var je = Number(rs)*Number(bz)*Number(ts);
	 $("#JTBZJE").val(je); 
	 sumBZXJ();
 }
 //设置硬座补助
 function setYZbz(){
	 var rs =  $("#YZBZRS").val();
	 var bz =  $("#YZBZBZ").val();
	 var ts =  $("#YZBZTS").val();  
	 var je = Number(rs)*Number(bz)*Number(ts);
	 $("#YZBZJE").val(je); 
	 sumBZXJ();
 }
 //设置其他补助
 function setQTbz(){
	 var rs =  $("#QTBZRS").val();
	 var bz =  $("#QTBZBZ").val();
	 var ts =  $("#QTBZTS").val();
	 var je = Number(rs)*Number(bz)*Number(ts);
	 $("#QTBZJE").val(je); 
	 sumBZXJ();
 }
 //设置其他补助
 function sumBZXJ(){
	 var hs =  $("#HSBZJE").val();
	 var tx =  $("#TXBZJE").val();
	 var jt =  $("#JTBZJE").val();
	 var yz =  $("#YZBZJE").val();
	 var qt =  $("#QTBZJE").val();
	 var je = Number(hs)+Number(tx)+Number(jt)+Number(yz)+Number(qt);
	 if(je>0){
		 $("#XJJE").val(je);  
		 $("#labelXJJE").html(je);  
	 }
	 sumAll();
	// $("#XJJE").val(je); 
 }
 
 
 
 //汇总核准金额
 function sumHZNum(){
	 var hpNum = $("#HPHZJE").val();
	  var tpNum = $("#TPHZJE").val();
	  var zsNum = $("#ZSHZJE").val();
	  var jtNum = $("#JTHZJE").val();
	  var qtNum = $("#QTHZJE").val();
	  var numSum = Number(hpNum)+Number(tpNum)+Number(zsNum)+Number(jtNum)+Number(qtNum);
	  $("#XJHZJE").val(numSum);
	  $("#labelXJHZJE").html(numSum);
	  sumAll();
 }
 // 汇总开支金额
 function sumKXNum(){
	 var hpNum = $("#HPKZJE").val();
	 var tpNum = $("#TPKZJE").val();
	 var zsNum = $("#ZSKZJE").val();
	 var jtNum = $("#JTKZJE").val();
	 var qtNum = $("#QTKZJE").val();
	 var numSum = Number(hpNum)+Number(tpNum)+Number(zsNum)+Number(jtNum)+Number(qtNum);
	 $("#XJKZJE").val(numSum);
	 $("#labelXJKZJE").html(numSum);
 }
 // 汇总单据张数
 function sumPageNum(){
	  var hpNum = $("#HPNUM").val();
	  var tpNum = $("#TPNUM").val();
	  var zsNum = $("#ZSNUM").val();
	  var jtNum = $("#JTNUM").val();
	  var qtNum = $("#QTNUM").val();
	  var numSum = Number(hpNum)+Number(tpNum)+Number(zsNum)+Number(jtNum)+Number(qtNum);
	  $("#XJNUM").val(numSum);
	  $("#labelXJNUM").html(numSum);
 }
