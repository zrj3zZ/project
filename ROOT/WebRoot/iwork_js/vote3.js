function selVote3Checkboxs(check_name,size){
	var result="";
	for(var i=0;i<size;i++){
		var checkbox_element=eval("frmMain." + check_name +i);
		alert(checkbox_element);
		if(checkbox_element.checked==true){
			var vote_id=checkbox_element.value;
			result=result+vote_id+",";
			}	
		}
	frmMain.vote_list.value=result;
}

//修改投票状态
function vote3AlterStatus(form,voteId,myCmd){
	form.cmd.value=myCmd; 
	form.vote_id.value=voteId; 
	form.target="_self";
	form.submit();
	return false;
}

//删除评论
function deleteVote(form,myCmd){
	form.cmd.value=myCmd; 
	form.target="_self";
	form.submit();
	return false;
}

//创建投票
function createVote(form,myCmd){
 	form.cmd.value=myCmd;
 	form.id.value=0;
 	targetWin='newMessage'+new Date().getTime();
	window.open('../aws_html/wait.htm',targetWin,'location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=yes,width=860,height=500');
 	form.target=targetWin;
 	form.submit();
}

//修改投票
function alterVote(form,bindid,openstate,myCmd){
  form.cmd.value=myCmd;
 	form.id.value=bindid;
 	form.openstate.value=openstate;
 	var newwin ='newMessage'+new Date().getTime();
	window.open('../aws_html/wait.htm',newwin,'location=no,menubar=yes,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=yes');
 	form.target=newwin;
 	form.submit();
}

//提交评论
function submitTalk(form,myCmd,jumpCmd){
	form.cmd.value=myCmd;
	form.jumpCmd.value = jumpCmd;
	form.TALKCONTENT.value = FCKeditorAPI.GetInstance('htmlEditOfTALKCONTENT').GetXHTML(true);
	form.target= "_self";
	form.submit();
	return false;
}

//提交评论_查看投票结果界面
function submitTalkVoteResult(form,myCmd,sort_type){
	form.cmd.value=myCmd;
	form.sort_type.value = sort_type;
	form.TALKCONTENT.value = FCKeditorAPI.GetInstance('htmlEditOfTALKCONTENT').GetXHTML(true);
	form.target= "_self";
	form.submit();
	return false;
}

//删除评论
function vote3DeleteTalk(form,myCmd,talkId){
	form.cmd.value=myCmd; 
	form.talkId.value=talkId;
	form.target="_self";
	form.submit();
	return false;
}

function vote3_open_list(form,myTarget,myCmd,vote_type){
	form.cmd.value=myCmd; 
	form.target=myTarget;
	form.vote_type.value=vote_type;
	form.submit();
	return false;
}

function vote3_Jump(form,myTarget,myCmd,vote_id,jumpCmd){
	form.cmd.value=myCmd; 
	form.jumpCmd.value=jumpCmd;
	form.vote_id.value=vote_id;
	form.target=myTarget;
	form.submit();
	return false;
}

function vote3TalkPaging(form,myTarget,myCmd,vote_id,jumpCmd){
	form.cmd.value=myCmd; 
	form.tJumpCmd.value=jumpCmd;
	form.vote_id.value=vote_id;
	form.target=myTarget;
	form.submit();
	return false;
}

//打开投票结果
function vote3_open_vote_result(form,myTarget,myCmd,vote_id,sort_type){
	
	form.vote_id.value=vote_id;
	form.cmd.value=myCmd; 
	form.sort_type.value=sort_type; 
	var new_target=getNewTarget();
	var width = 820;
	var height = 480;
	var x = (screen.width - width) / 2;
	var y = (screen.height - height) / 2;
 	window.open('../aws_html/wait.htm',new_target,'left=' + x + ',top=' + y + ',width=' + width + ',height=' + height + ',location=no,menubar=no,toolbar=no,scrollbars=yes,status=no,directories=no,resizable=yes');
	form.target=new_target;
	form.submit();
	return false;
}

//打开投票页面
function vote3_open_vote(form,myTarget,myCmd,vote_id,curPage, jumpCmd, optIds, tJumpCmd, tCurrentPage){
	
	form.vote_id.value=vote_id;
	form.cmd.value=myCmd; 
	form.curPage.value=curPage; 
	form.jumpCmd.value=jumpCmd; 
	form.optIds.value=optIds; 
	form.tJumpCmd.value=tJumpCmd; 
	form.tCurrentPage.value=tCurrentPage; 
	var new_target=getNewTarget();
	var width = 820;
	var height = 480;
	var x = (screen.width - width) / 2;
	var y = (screen.height - height) / 2;
 	window.open('../aws_html/wait.htm',new_target,'left=' + x + ',top=' + y + ',width=' + width + ',height=' + height + ',location=no,menubar=no,toolbar=no,scrollbars=yes,status=no,directories=no,resizable=yes');
	form.target=new_target;
	form.submit();
	return false;
}


//排序投票结果
function sort_vote_result(form,myTarget,myCmd,vote_id,sort_type){
	form.vote_id.value=vote_id;
	form.cmd.value=myCmd; 
	form.sort_type.value=sort_type; 
  form.target="_self";
 	form.submit();
	return false;
}

//放弃提交
function vote3_cancel_vote(form,myTarget,myCmd){
	form.cmd.value=myCmd; 
	form.target=myTarget;
	form.submit();
	return false;
}

//提交调查
function vote3_submit_vote(form,myTarget,myCmd,size){
	form.cmd.value=myCmd; 
	form.target=myTarget;
	form.submit();
	return false;
}

//portal上的提交调查
function vote3_submit_vote_portal(form,myTarget,myCmd){
	form.cmd.value=myCmd; 
	form.target=myTarget;
	form.submit();
	return false;
}

//单选
function vote3_set_radio_val(hidden_var_name,opt_id,ids){
		var result = eval("frmMain." + hidden_var_name);
	  result.value=opt_id;
	  var myArray = ids.split(",");
    for(var i=0;i<myArray.length;i++){
	  	 if(myArray[i]!= opt_id){
	  	 	var radio = eval("frmMain." + myArray[i]);
	  	 	if(radio){
	  	 		radio.checked = false;
	  	 	}
	  	}
    }
}

//从res中除去id后的结果
function myfun(res,id){
	var cur = ","+res;
	var id = "," + id;
	var index = cur.indexOf(id);
	if(index > -1){
	  var res = cur.substring(1,index + 1);
	  var nex= cur.substring(index + id.length);
	  return res + nex;
	}else{
		return res;
	}
}

//多选
function vote3_set_checkbox_val(hidden_var_name,opt_id){
	var result = eval("frmMain." + hidden_var_name);
	var optId = opt_id + ",";
	var checkbox_element=eval("frmMain." + opt_id);
	if(checkbox_element.checked==true){
     result.value = result.value + optId;
	}else{
		 result.value = myfun(result.value,optId);
	}
}

//预览记事内容
 function vote3_perviewOpt(preObj,imgObj){
 	if(preObj.style.display=='none'){//隐藏的
 		preObj.style.display=''; 		
 		//imgObj.src='../aws_img/arrow_down.gif';
 		imgObj.src='../aws_img/VOITE_HIDDEN.jpg';
 		imgObj.alt='隐藏投票单项内容';
 	}else{
 		preObj.style.display='none';
 		//imgObj.src='../aws_img/VOITE_HIDDEN.jpg';
 		imgObj.src='../aws_img/VOITE_SEE.jpg';
 		imgObj.alt='显示完整投票单项内容';
 	}
 }
 
 function vote3_openAc_define(form,tableName,mycmd){
	form.cmd.value=mycmd; 
	form.resourceId.value=form.meId.value;
	form.tableName.value=tableName;
	window.open('../aws_html/wait.htm','setVoteManager','left=50,top=50,width=520,height=480,location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=yes');
	form.target = "setVoteManager";
	form.submit();	
 	return false; 
 }
 
 function openVote3ManageAc(form,resourceId,tableName,mycmd){
	form.cmd.value=mycmd; 
	form.resourceId.value=resourceId;
	form.tableName.value=tableName;
	window.open('../aws_html/wait.htm','setVoteManager','left=50,top=50,width=520,height=480,location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=yes');
	form.target = "setVoteManager";
 	form.submit();	
 	return false; 
 }