<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib prefix="s" uri="/struts-tags" %> 
<%@ page import="java.util.HashMap" %>
<%
String year = request.getParameter("year");
HashMap map = new HashMap();
map = (HashMap)request.getAttribute("mapHolidays");
	  String hdnWwdata1 ="";
 	  String hdnWwdata2 = "";
      String hdnWwdata3 = "";
      String hdnWwdata4 = "";
      String hdnWwdata5 = "";
      String hdnWwdata6 = "";
      String hdnWwdata7 = "";
      String hdnWwdata8 = "";
      String hdnWwdata9 = "";
      String hdnWwdata10 = "";
      String hdnWwdata11 = "";
      String hdnWwdata12 = "";
	if(map!=null){
  	   hdnWwdata1 =    (String)map.get("hdnWwdata1");
       hdnWwdata2 =    (String)map.get("hdnWwdata2");
       hdnWwdata3 =    (String)map.get("hdnWwdata3");
       hdnWwdata4 =    (String)map.get("hdnWwdata4");
       hdnWwdata5 =    (String)map.get("hdnWwdata5");
       hdnWwdata6 =    (String)map.get("hdnWwdata6");
       hdnWwdata7 =   (String) map.get("hdnWwdata7");
       hdnWwdata8 =    (String)map.get("hdnWwdata8");
       hdnWwdata9 =    (String)map.get("hdnWwdata9");
       hdnWwdata10 =   (String)map.get("hdnWwdata10");
       hdnWwdata11 =   (String)map.get("hdnWwdata11");
       hdnWwdata12 =   (String)map.get("hdnWwdata12");
}
     
      
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html xmlns="http://www.w3.org/1999/xhtml"  pageEncoding="UTF-8">  
<head><title>  
      
        假期日历设置  
</title>
  	
    <script type="text/javascript" src="iwork_js/jqueryjs/myCalendar/jquery.js"></script>
    <script language="javascript" type="text/javascript" src="iwork_js/jqueryjs/My97DatePicker/WdatePicker.js"></script>  
    <script src="iwork_js/jqueryjs/myCalendar/jquery-ui-1.8.18.custom.min.js" type="text/javascript"></script>
    <link href="iwork_js/jqueryjs/myCalendar/jquery-ui-1.8.18.custom.css" rel="stylesheet" type="text/css" /> 
    <link href="iwork_js/jqueryjs/myCalendar/jquery.autocomplete.css" rel="stylesheet" type="text/css" />
    <script src="iwork_js/jqueryjs/myCalendar/jquery.autocomplete.js" type="text/javascript"></script>
    <script src="iwork_js/jqueryjs/myCalendar/jquery.messager.js" type="text/javascript"></script>
    <script language="javascript" type="text/javascript" src="iwork_js/jqueryjs/myCalendar/jquery.blockUI.js"></script>
     <script type="text/javascript">  
        //     $(document).ready(function() { initCustomEdit();});  
        var currentIframeDocument = null;  
        var currentDataM = null;  
        var currentDataD = null;  
        //初始化12个日历  
        function InitDate(nowyear) {  
            for (i = 1; i <= 12; i++) {
                WdatePicker({ eCont: 'month' + i,  
                    startDate: nowyear+'-' + i + '-1',  
                    minDate: nowyear+'-' + i + '-1',  
                    maxDate: nowyear+'-' + i + '-%ld',  
                    onpicked: function(dp) {  
                        $("#txtSelectDate").val(dp.cal.getDateStr('yyyy-MM-dd'));  
                        currentDataM = dp.cal.getDateStr('M');  
                        currentDataD = dp.cal.getDateStr('d');  
                        currentIframeDocument = $($("iframe")[parseInt(currentDataM) - 1].contentDocument)  
                        UpdatePickerWwday(currentDataD);  
                    }  
                });  
  
            }  
        }  
        //更新选中项  
        function UpdatePickerWwday(obj) {  
            $.each(currentIframeDocument.find("td[class='Wwday']"),  
                            function(i, n) {  
                                $(n).attr("class", "Wday"); 
                                //2014-6-3修改当点击日期后，鼠标移至设工作日处会恢复原样BUG 
                                $(n).mouseover(function(event) { this.className = 'WdayOn' })  
                                $(n).mouseout(function(event) { this.className = 'Wday' });  
                                //END
                            }  
                           );  
            $.each(currentIframeDocument.find("td[class='Wday']"),  
        			function(i, n) {  
            			var freeDays = $("#hdnWwdata" + currentDataM).val().split(",");  
            			var k = 0;  
            			for (k = 0; k < freeDays.length; k++) {  
                			if ($(n).text() == freeDays[k]) {  
                    			$(n).attr("class", "Wwday");  
                    			$(n).mouseover(function(event) { this.className = 'WwdayOn' })  
                    			$(n).mouseout(function(event) { this.className = 'Wwday' });  
                			}  
            			}  
        			}  
       			);  
        }  
        //初始化节假日  
        function InitSelect() {  
            var action = 'EDIT';  
            if (action == "EDIT") {  
                var k = 0;  
                try {  
                    for (k = 0; k < 12; k++) {  
                        var iObj = $($("iframe")[k].contentDocument);  
                        $.each(iObj.find("td[class='Wwday']"),  
                                function(i, n) {  
                                    $(n).attr("class", "Wday");  
                                    $(n).mouseover(function(event) { this.className = 'WdayOn' })  
                                    $(n).mouseout(function(event) { this.className = 'Wday' });  
                                }  
                               );  
  
                        var selday = iObj.find("td[class='Wselday']");  
                        selday.attr("class", "Wday");  
                        selday.mouseout(function(eent) { this.className = 'Wday' });  
                        $.each(iObj.find("td[class='Wday']"),  
                                function(i, n) {  
                                    var freeDays = $("#hdnWwdata" + (k + 1)).val().split(",");  
                                    var j = 0;  
                                    for (j = 0; j < freeDays.length; j++) {  
                                        if ($(n).text() == freeDays[j]) {  
                                            $(n).attr("class", "Wwday");  
                                            $(n).mouseover(function(event) { this.className = 'WwdayOn' })  
                                            $(n).mouseout(function(event) { this.className = 'Wwday' });  
                                        }  
                                    }  
                                }  
                               );  
  
                    }  
                } catch (err) {  
                    document.getElementById("lblMsg").innerText = "Initialize data fail, please try it again";  
                } finally {  
                    $.unblockUI();  
                }  
            } else {  
                var k = 0;  
                try {  
                    for (k = 0; k < 12; k++) {  
                        var iObj = $($("iframe")[k].contentDocument);  
                        $.each(iObj.find("td[class='Wwday']"),  
                                function(i, n) {  
                                    $("#hdnWwdata" + (k + 1)).val($("#hdnWwdata" + (k + 1)).val() + "," + $(n).text());  
                                }  
                               );  
                        var selday = iObj.find("td[class='Wselday']");  
                        if ($("#hdnWwdata" + (k + 1)).val().charAt(1) == "7") {  
                            selday.attr("class", "Wwday");  
                            selday.mouseover(function(event) { this.className = 'WwdayOn' })  
                            selday.mouseout(function(event) { this.className = 'Wwday' });  
                        } else if ($("#hdnWwdata" + (k + 1)).val().charAt(1) == "2" && $("#hdnWwdata" + (k + 1)).val().charAt(3) != "3") {  
                            selday.attr("class", "Wwday");  
                            selday.mouseover(function(event) { this.className = 'WwdayOn' })  
                            selday.mouseout(function(event) { this.className = 'Wwday' });  
                        } else {  
                            selday.attr("class", "Wday");  
                            selday.mouseover(function(event) { this.className = 'WdayOn' })  
                            selday.mouseout(function(event) { this.className = 'Wday' });  
                        }  
                    }  
                } catch (err) {  
                    document.getElementById("lblMsg").innerText = "Initialize data fail, please try it again";  
                } finally {  
                    $.unblockUI();  
                }  
                for (k = 1; k < 13; k++) {//去掉第一个逗号  
                    $("#hdnWwdata" + k).val($("#hdnWwdata" + k).val().substring(1));  
                    if ($("#hdnWwdata" + k).val().substring(0, 1) == "2" || $("#hdnWwdata" + k).val().substring(0, 1) == "7") {//如果当月的1号是星期天，或者是星期六则选不中，需要手动添加  
                        $("#hdnWwdata" + k).val("1," + $("#hdnWwdata" + k).val())  
                    }  
                }  
            }  
        }  
  
        $(document).ready(function() {  
            $.blockUI({ message: '<h3>加载中...</h3>' });  
            //add by 2014-5-28 获得年份
            var year;
            if(<%=year%>==null){
            	year = getYearNow();
            }else{
            	year = <%=year%>
            }
            //end
            InitDate(year);  
            setTimeout("InitSelect()", 2000);  
        });  
        //添加休假日  
        function AddWwDate_Click() {  
            if (check()) {  
                var obj = currentIframeDocument.find("td[class='Wselday']");  
                obj.attr("class", "Wwday");  
                obj.mouseover(function(event) { this.className = 'WwdayOn' })  
                obj.mouseout(function(event) { this.className = 'Wwday' });  
                AddFreeDay();  
                //add by 2014-5-30 增加设置休假日插入库
                addHoliday($("#txtSelectDate").val());
                //end
                $("#txtSelectDate").val("");  
            } else {  
                document.getElementById("lblMsg").innerText = "please select date";  
            }  
        }  
        //添加工作日  
        function AddWDate_Click() {  
            if (check()) {  
                var obj = currentIframeDocument.find("td[class='Wselday']");  
                obj.attr("class", "Wday");  
                obj.mouseover(function(event) { this.className = 'WdayOn' })  
                obj.mouseout(function(event) { this.className = 'Wday' });  
                AddWorkDay();  
                 //add by 2014-5-30 增加设置工作日插入库
                addJobDay($("#txtSelectDate").val());
                //end
                $("#txtSelectDate").val("");  
            } else {  
                document.getElementById("lblMsg").innerText = "please select date";  
            }  
        }  
        function check() {  
            if ($("#txtSelectDate").val() == "") {  
                return false;  
            } else {  
                return true;  
            }  
        }  
        function AddWorkDay() {  
            var freeDays = $("#hdnWwdata" + currentDataM).val().split(",");  
            var i = 0;  
            var count = freeDays.length;  
            for (i = 0; i < freeDays.length; i++) {  
                if (currentDataD == freeDays[i]) {  
                    freeDays.splice(i, 1);  
                    break;  
                }  
            }  
            $("#hdnWwdata" + currentDataM).val(freeDays);  
        }  
  
        function AddFreeDay() {  
            var freeDays = $("#hdnWwdata" + currentDataM).val().split(",");  
            var i = 0;  
            var count = freeDays.length;  
            for (i = 0; i < freeDays.length; i++) {  
                if (currentDataD == freeDays[i]) {  
                    break;  
                }  
            }  
            if (i == count) {  
                freeDays.push(currentDataD);  
            }  
            $("#hdnWwdata" + currentDataM).val(freeDays);  
  
        }  
        function UpdateFreeDay() {  
            var freeDays = $("#hdnWwdata" + currentDataM).val().split(",");  
            var i = 0;  
            var count = freeDays.length;  
            for (i = 0; i < freeDays.length; i++) {  
                if (currentDataD == freeDays[i]) {  
                    freeDays.splice(i, 1);  
                    break;  
                }  
            }  
            if (i == count) {  
                document.getElementById("lblMsg").innerText = "add " + currentDataD;  
                freeDays.push(currentDataD);  
            }  
            $("#hdnWwdata" + currentDataM).val(freeDays);  
  
        }  
  	//add by 2014-5-28 获得当前年份		
      function getYearNow(){
      	var d = new Date();
        var nowYear = d.getFullYear();
        document.getElementById('theYear').value=nowYear;
        return nowYear;
      }
      //增加年份
      function addYearChange(){
      	var year = document.getElementById('theYear').value;
      	var tempYear = parseInt(year) + 1;
      	year = tempYear;
      	document.getElementById('theYear').value=year;
      	redirct(tempYear);
      	//reload();
      }
      //减少年份
      function downYearChange(){
      	var year = document.getElementById('theYear').value;
      	var tempYear = parseInt(year) - 1;
      	year = tempYear;
      	document.getElementById('theYear').value=year;
      	redirct(tempYear);
      	//reload();
      }
      //点击后重新加载
      function reload(){
      	var theYear = document.getElementById('theYear').value;
      	$.blockUI({ message: '<h3>加载中...</h3>' });  
        InitDate(theYear);  
        setTimeout("InitSelect()", 2000); 
      }
       
       function redirct(tempYear){
      	var form = document.getElementById('form1');
      	var url = "iwork_sys_get_holidays_map.action?year="+tempYear;
      	//var url = "iwork_sys_calendar_get_holidays.action?year="+theYear;
      	form.action = url;
      	//form.target="_self";
		form.submit();
      }
      //增加节假日入库
      function addHoliday(date){
      	    var id = document.getElementById('id').value;
			var pageurl = "iwork_sys_calendar_add_holidays.action?_date="+date+"&id="+id;
			$.ajax({ 
	            type:'POST',
	            url:pageurl,
	            success:function(msg){ 
	            	//  if(msg!=null&&msg!=""){
	            	//  	alert('设置成功');
	                  	//reload();
	                //  }else{
	                //  	alert('设置失败')
	                //  }
	            }
	        });
		
	}
	 //增加工作日入库
      function addJobDay(date){
      	    var id = document.getElementById('id').value;
			var pageurl = "iwork_sys_calendar_add_workdays.action?_date="+date+"&id="+id;
			$.ajax({ 
	            type:'POST',
	            url:pageurl,
	            success:function(msg){ 
	            	 // if(msg!=null&&msg!=""){
	            	 // 	 lert('设置成功');
	                  	//reload();
	                 // }else{
	                 // 	 alert('设置失败');
	                 // }
	            }
	        });
		
	}
    </script>  
  
    <script type="text/javascript" src="../Js/util.js"></script>  
  
    <style type="text/css">  
        .style1  
        {  
            width: 200px;  
        }  
        .style2  
        {  
            width: 404px;  
        }  
    </style>  
</head>  
<body>  
    <form name="form1" method="post" action="Calendar.aspx?GID=DBA.2012&action=edit" id="form1">  
    <div id="top">  
        <table>  
            <tr>  
                <td>  
                </td>  
                <td style="padding-left: 5px;">  
                </td>  
            </tr>  
        </table>  
        <input type="hidden" id="id" name="id" value="${id }"/>
        <input type="hidden" name="hdnWwdata1" id="hdnWwdata1" value="<%=hdnWwdata1 %>" />  
        <input type="hidden" name="hdnWwdata2" id="hdnWwdata2" value="<%=hdnWwdata2 %>" />  
        <input type="hidden" name="hdnWwdata3" id="hdnWwdata3" value="<%=hdnWwdata3 %>" />  
        <input type="hidden" name="hdnWwdata4" id="hdnWwdata4" value="<%=hdnWwdata4 %>" />  
        <input type="hidden" name="hdnWwdata5" id="hdnWwdata5" value="<%=hdnWwdata5 %>" />  
        <input type="hidden" name="hdnWwdata6" id="hdnWwdata6" value="<%=hdnWwdata6 %>" />  
        <input type="hidden" name="hdnWwdata7" id="hdnWwdata7" value="<%=hdnWwdata7 %>" />  
        <input type="hidden" name="hdnWwdata8" id="hdnWwdata8" value="<%=hdnWwdata8 %>" />  
        <input type="hidden" name="hdnWwdata9" id="hdnWwdata9" value="<%=hdnWwdata9 %>" />  
        <input type="hidden" name="hdnWwdata10" id="hdnWwdata10" value="<%=hdnWwdata10 %>" />  
        <input type="hidden" name="hdnWwdata11" id="hdnWwdata11" value="<%=hdnWwdata11 %>" />  
        <input type="hidden" name="hdnWwdata12" id="hdnWwdata12" value="<%=hdnWwdata12 %>" />  
    </div>  
    <div id="lblMsg" class="label_message">  
    </div>  
    <div id="top_input">  
        <div style="float: left; width: 8px;" class="tabL">  
        </div>  
        <div class="tabLabelCont tabCur" onclick="" id="div1">  
            <input type="button" value="《《上一年" onclick="javascript:downYearChange();"><input type="button" value="下一年》》" onclick="javascript:addYearChange();"/>
        </div>  
        <div style="float: left;" class="tabR">  
        </div>  
        
    </div>  
    <div id="autodiv_input">  
        <table cellpadding="0" cellspacing="0" style="margin-left: 5px; width: 750px; margin-right: 3px;  
            margin-top: 3px; margin-bottom: 3px;">  
            <tr>  
                <td width="150">  
                    <input name="txtSelectDate" type="hidden" id="txtSelectDate" class="bigInputbox" />
                     <input type="hidden" name="theYear" id="theYear" value="<%=year %>"/>
                </td>  
                <td class="style1" style="padding-left: 25px;">  
                    <input id="btnAddWwDate" type="button" value="设置为节假日" onclick="AddWwDate_Click()" class="enButton" />  
                    
                </td>  
                <td class="style2">  
                    <input id="btnAddWDate" type="button" value="设置为工作日" onclick="AddWDate_Click()" class="enButton" /> 
                </td>  
                <td>  
                       
                </td>  
            </tr>  
            <tr>  
                <td colspan="4">  
                       
                </td>  
            </tr>  
            <tr>  
                <td colspan="4">  
                    <table style="width: 100%;">  
                        <tr height="190">  
                            <td>  
                                <div id="month1">  
                                </div>  
                            </td>  
                            <td>  
                                <div id="month2">  
                                </div>  
                            </td>  
                            <td>  
                                <div id="month3">  
                                </div>  
                            </td>  
                            <td>  
                                <div id="month4">  
                                </div>  
                            </td>  
                        </tr>  
                        <tr height="190">  
                            <td>  
                                <div id="month5">  
                                </div>  
                            </td>  
                            <td>  
                                <div id="month6">  
                                </div>  
                            </td>  
                            <td>  
                                <div id="month7">  
                                </div>  
                            </td>  
                            <td>  
                                <div id="month8">  
                                </div>  
                            </td>  
                        </tr>  
                        <tr height="190">  
                            <td>  
                                <div id="month9">  
                                </div>  
                            </td>  
                            <td>  
                                <div id="month10">  
                                </div>  
                            </td>  
                            <td>  
                                <div id="month11">  
                                </div>  
                            </td>  
                            <td>  
                                <div id="month12">  
                                </div>  
                            <br></td>  
                        </tr>  
                    </table>  
                </td>  
            </tr>  
        </table>  
    </div>  
    </form>  
</body>  
</html>  