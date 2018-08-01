//打开添加对话框
	function add(){
	 $('#addPortletDialog').dialog('open');
	}
//添加操作
	function addSelect(){
	var node = $('#addPortlet').tree('getSelected');
	moveSelect(node,document.all.to);
	}
//添加代码
    function moveSelect(oSourceSel,oTargetSel){

       //建立存储value和text的缓存数组
        var arrSelValue = new Array();
        var arrSelText = new Array();
       //用来辅助建立缓存数组
        var index = 0;
        if(oSourceSel==null){
        $.messager.alert('系统提示','请选择栏目列表!','error');
        return false;
        }
        //获取选中节点，并存储至缓存
       	var b = $('#addPortlet').tree('isLeaf', oSourceSel.target);    	
            if(b)//判断是否为最底层节点
            {
		        //检查是否重复
			    for(var j=0;j<oTargetSel.options.length;j++){
				 if(oSourceSel.id==oTargetSel.options[j].value){				
				 $.messager.alert('系统提示','此项已存在!','error');
				 return false;
				 }
				}
				
                //存储
                arrSelValue[index] = oSourceSel.id;
                arrSelText[index] = oSourceSel.text;
                index ++;
            }else{
            $.messager.alert('系统提示','分组不可添加!','error');
				 return false;
            }
      	
        //增加缓存的数据到目的列表框中
        for(var i=0; i<arrSelText.length; i++)  
        {	  
            //增加
            var oOption = document.createElement("option");
            oOption.text = arrSelText[i];
            oOption.value = arrSelValue[i];	
            if (window.navigator.userAgent.indexOf("MSIE")>=1)
 			oTargetSel.add(oOption);
			else
            oTargetSel.appendChild(oOption);
        }	   
        }
 //删除操作
     function deleteSelect(obj){       
         deleteSelectItem(obj);
     }
 