 //打开
    function openCms(infoid){
	    var url = "cmsOpen.action?infoid="+infoid;
	    var newWin="newVikm";
	    var page = window.open('form/waiting.html',newWin,'width=700,height=500,location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=yes');
	    page.location=url;
	}
//编辑
    function edit(infoid){
	    var url = "cmsUpdate.action?infoid="+infoid;
	    var newWin="newVikm";
	    var page = window.open('form/waiting.html',newWin,'width=700,height=500,location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=yes');
	    page.location=url;
	}
//执行页面表格删除
	function removeGridItem(rows){
			for(var i=0;i<rows.length;i++){
				jQuery("#info_grid").jqGrid('delRowData',rows[0]);
			}
			rows =jQuery("#info_grid").jqGrid('getGridParam','selarrrow');
			if(rows.length>0){
				removeGridItem(rows);
			}
		}
