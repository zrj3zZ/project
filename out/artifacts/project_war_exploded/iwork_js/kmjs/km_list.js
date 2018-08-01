var index = 0;
var createFolderDlgObj;//创建文件夹对话框
//在当前位置创建一个新文件夹
	function newFolder(){
			    	createFolderDlgObj= new parent.Ext.Window({	
					layout:'fit',
					width:380,
					title: '新建文件夹',
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
						html:'<iframe name=KM_CREATE_FOLDER_DLG id=KM_CREATE_FOLDER_DLG  frameborder=0 width=100% scrolling="no" height=100% src=../km_dir_add.action?parentid='+frmMain.parentid.value+'></iframe>'
					}]
					});
					createFolderDlgObj.addListener('show', handleShow);	
					function handleShow(o){
						KM_CREATE_FOLDER_DLG.location = "km_dir_add.action";
						return;
					}
					createFolderDlgObj.show(this);
			
		}
		//新建文件
		function newFile(){
			    	createFolderDlgObj= new parent.Ext.Window({
					layout:'fit',
					width:500,
					title: '上传文件',
					iconCls: 'IconNewFolder',
					height:400,
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
						html:'<iframe name=KM_CREATE_FILE_DLG id=KM_CREATE_FILE_DLG  frameborder=0 width=100% scrolling="no" height=100% src=../km_file_add.action?parentid='+frmMain.parentid.value+'></iframe>'
					}]
					});
					createFolderDlgObj.addListener('show', handleShow);	
					function handleShow(o){
						KM_CREATE_FOLDER_DLG.location = "km_file_add.action";
						return;
					}
					createFolderDlgObj.show(this);
			
		}
function openFile(){
	var tabsDemo = parent.kmTabs;
	tabsDemo.add({
                title:"newtab",
                id:"newtab"+index,
                html:"new tab"+index,
                closable:true
           });
           tabsDemo.setActiveTab("newtab"+index);
	index++;
}
function openFile(docid,docname){
	var tabsDemo = parent.kmTabs;
	tabsDemo.add({
                title:docname,
                 id:"newfileTab"+docid,
                html:'<iframe  frameborder=0 width=100% scrolling="no" height=100% src=../km_doc_load.action?id='+docid+'></iframe>',
                closable:true
           });
           tabsDemo.setActiveTab("newfileTab"+docid);
}
//查看文件夹属性
function lookupDirMethd(directoryid){
	frmMain.target = "KMBASEINFO";
	frmMain.action = "../km_dir_info.action?id = "+directoryid;
	frmMain.submit();
	return false;
}
//查看文件属性
function lookupfileMethd(directoryid){
	
}