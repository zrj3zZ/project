$(function(){
         //车辆预订申请
			/*$('#caradd').dialog({
			    title:"车辆预定",
			    resizable:true,
	            buttons:[{
					text:'保存',
					iconCls:'icon-ok',
					handler:function(){	
					   var nowDate = new Date();
					   var year=nowDate.getFullYear();
					   var month=nowDate.getMonth()+1;      //获取当前月份(0-11,0代表1月)  
                       var day=nowDate.getDate();
                       var hour=nowDate.getHours();      //获取当前小时数(0-23)  
   					   var min=nowDate.getMinutes(); 
   					   if(month<10){
   					    month="0"+month;
   					   }
   					   if(day<10){
   					   day="0"+day;
   					   }
   					   var mydate=year+"-"+month+"-"+day;//当前日期
					    var date1=$('#cardate1').datebox("getValue");
					    var date2=$('#cardate2').datebox("getValue");
						var hour1=$('#carhour1').val();	
                        var hour2=$('#carhour2').val();
                        var min1=$('#carmin1').val();	
                        var min2=$('#carmin2').val();													
						if(date1==""){	
							$.messager.alert('警告','请选择开始日期!');	
							}else if(mydate>date1){
							$.messager.alert('警告','开始日期不能小于当前日期!');	
							
							}
					    else if(hour1==""){
					    $.messager.alert('警告','请填写开始时间!');	
					    }else if(hour1<0||hour1>23){
					    $.messager.alert('警告','请填写正确的开始小时!');
					    }else if(min1==""){
					    $.messager.alert('警告','请填写开始时间!');						    
					    }else if(min1.length>2||min1<0||min1>59){
					    $.messager.alert('警告','请填写正确的开始分钟!');						    
					    }else if(date1==mydate&&hour1<hour){
					     $.messager.alert('警告','开始时间不能小于当前时间!');						    
					    }else if(date1==mydate&&hour1==hour&&min1<min){
					     $.messager.alert('警告','开始时间不能小于当前时间!');
					    }else if(date2==""){	
						 $.messager.alert('警告','请选择开始日期!');	
						}else if(date1>date2){
					     $.messager.alert('警告','开始日期不能大于结束日期!');						    
					    }else if(hour2==""){
					    $.messager.alert('警告','请填写结束时间!');	
					    }else if(hour2<0||hour2>23){
					    $.messager.alert('警告','请填写正确的结束小时!');
					    }else if(min2==""){
					    $.messager.alert('警告','请填写开始时间!');						    
					    }else if(min2.length>2||min2<0||min2>59){
					    $.messager.alert('警告','请填写正确的开始时间!');						    
					    }else if(date1==date2&&hour1>=hour2){
					     $.messager.alert('警告','开始时间必须小于结束时间!');						    
					    }else if(date1==date2&&hour1==hour2&&min1<=min2){
					     $.messager.alert('警告','开始时间必须小于结束时间!');						    
					    }else{																
						var url='addCar.action';			
						document.forms[0].action=url;  
						document.forms[0].method="post"
		                document.forms[0].target="hidden_frame";
		                document.forms[0].submit();
		                $('#caradd').dialog('close');	
		                }					
					}
				},{
					text:'取消',
					handler:function(){
						$('#caradd').dialog('close');
					}
				}]
			      });
			$('#caradd').dialog('close');*/
			});
function buttonGo()
{	
 var obj = window.event.srcElement;  
 obj.runtimeStyle.cssText = "background-color:#FFFFFF;font-weight: bolder";
}


//启动流程
function createProcess(spaceId,processid,resouceid,resoucename,para1,para2,para3){
	var url = 'processRuntimeStartInstance.action?actDefId='+processid+"&SPACEID="+spaceId+"&RESOURCEID="+resouceid+"&RESOURCENAME="+encodeURI(resoucename);
	var target = getNewTarget(); 
	var page = window.open('form/loader_frame.html',target,'width='+(screen.width-100)+',height='+(screen.height-150)+',top=20,left=50,location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=yes');
	page.location=url;

}
  
//添加预定
//addApply(1,'007','宇通','车牌号码：京E 26185','车辆颜色：白色','准乘人数：19')
function addApply(spaceId,resouceid,resoucename,para1,para2,para3){ 
	var spaceName = $("#editForm_spaceName").val();
	var pageUrl = 'resbook_runtime_add.action?spaceId='+spaceId+'&resouceid='+resouceid+'&spaceName='+spaceName+'&resoucename='+encodeURI(resoucename);
	var artWin = art.dialog.open(pageUrl,{
		id:'addWebWinDiv',
		title: '添加资源预定',
		lock:true,
		background: '#999', // 背景色
	    opacity: 0.87,	// 透明度
	    width:500,
		height:400,
		close:function(){
			window.location.reload();
		}
	 });

	/*art.dialog.open(pageUrl,{
				id:'addWebWinDiv', 
				cover:true,
				title:'添加资源预定',  
				loadingText:'正在加载中,请稍后...',
				bgcolor:'#999',
				rang:true,
				width:500,
				cache:false, 
				lock: true,
				esc: true,
				height:400,  
				iconTitle:false, 
				extendDrag:true,
				autoSize:true
			});
	dg.ShowDialog();*/
}

function closeWin(){
	alert(1)
}

function next(){
	$('#editForm').attr('action','resbook_runtime_next.action');
   	$('#editForm').submit();
}
function previous(){
	$('#editForm').attr('action','resbook_runtime_previous.action');
   	$('#editForm').submit(); 
}