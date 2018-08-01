function doCmd(taskId, cmd, hasUser) {
	$.ajax({
		type : 'POST',
		url : 'processManagement!doCmd.action?taskId=' + taskId + '&cmd=' + cmd + "&hasUser=" + hasUser,
		success : function(msg) {
			msg = msg.replace(/^\s+|\s+$/g, "")
			if (msg == 'nouser') {
				document.forms[0].cmd.value = cmd;
				document.forms[0].hasUser.value = hasUser;
				$('#next_user_window').window('open');
			} else {
				 window.parent.$('#mainFrameTab').tabs("close",window.parent.$('#mainFrameTab').tabs('getSelected').panel('options').title);
			}
		}
	});
}

