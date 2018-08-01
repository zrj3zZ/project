/**
	*根据传入的id显示右键菜单
	*/
	function showMenu(id){
		frmMain.id.value = id;
		if("" == id){
		    popMenu(itemMenu,150,"100");
		}else{
		    popMenu(itemMenu,150,"111");
		}
		event.returnValue=false;
		event.cancelBubble=true;
		return false;
	}
	/**
	*显示弹出菜单
	*menuDiv:右键菜单的内容
	*width:行显示的宽度
	*rowControlString:行控制字符串，0表示不显示，1表示显示，如“101”，则表示第1、3行显示，第2行不显示
	*/
	function popMenu(menuDiv,width,rowControlString){
			//创建弹出菜单
			var pop=window.createPopup();
			//设置弹出菜单的内容
			pop.document.body.innerHTML=menuDiv.innerHTML;
			var rowObjs=pop.document.body.all[0].rows;
			//获得弹出菜单的行数
			var rowCount=rowObjs.length;
			//循环设置每行的属性
			for(var i=0;i<rowObjs.length;i++)
			{
			    //如果设置该行不显示，则行数减一
			    var hide=rowControlString.charAt(i)!='1';
			    if(hide){
			        rowCount--;
			    }
			    //设置是否显示该行
			    rowObjs[i].style.display=(hide)?"none":"";
			    //设置鼠标滑入该行时的效果
			    rowObjs[i].cells[0].onmouseover=function()
			    {
			       // this.style.background="#818181";
			        this.style.color="#005080";
			    }
			    //设置鼠标滑出该行时的效果
			    rowObjs[i].cells[0].onmouseout=function(){
			       // this.style.background="#cccccc";
			        this.style.color="black";
			    }
			}
			//屏蔽菜单的菜单
			pop.document.oncontextmenu=function()
			{
			        return false;
			}
			//选择右键菜单的一项后，菜单隐藏
			pop.document.onclick=function()
			{
			        pop.hide();
			}
			//显示菜单
			pop.show(event.clientX-1,event.clientY,width,rowCount*85,document.body);
			return true;
	}
	function editDirectory(directoryid){
		createFolderDlgObj= new parent.Ext.Window({	
					layout:'fit',
					width:380,
					title: '编辑文件夹',
					iconCls: 'IconNewFolder',
					height:220,
					plain: true,
					closeAction:'hide',
					modal: true,
					keys: [{
						key: 27,
						scope: this,
						fn: function() {
					  	createFolderDlgObj.hide();
						}
					}],			
					items:[{
						html:'<iframe name=KM_CREATE_FOLDER_DLG id=KM_CREATE_FOLDER_DLG  frameborder=0 width=100% scrolling="no" height=100% src=../km_dir_edit.action?parentid='+directoryid+'></iframe>'
					}]
					});
					createFolderDlgObj.addListener('show', handleShow);	
					function handleShow(o){
						KM_CREATE_FOLDER_DLG.location = "km_dir_edit.action";						
						return;
					}
					createFolderDlgObj.show(this);
	}
	function create()
	{
		alert("create" + frmMain.id.value + "!");
	}
	function update()
	{
		var directoryid = frmMain.id.value;
		editDirectory(directoryid);
	}
	function del()
	{
	  	alert("delete" + frmMain.id.value + "!");
	}
	function clickMenu()
	{
	  	alert("you click a menu!");
	}