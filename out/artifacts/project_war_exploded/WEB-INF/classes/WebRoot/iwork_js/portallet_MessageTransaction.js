  function refreshTask(form){
  	form.action="./login.wf";
  	form.cmd.value='Portal_Execute_MessageWorkFlowTransaction';
 	form.target="_self";
 	disableAll(form);
 	form.submit();
 	return false;   
  }
  
function openTask(form,task_id,message_id,openstate,mycmd){
 	form.action="./message.wf";
 	form.cmd.value=mycmd;
 	form.id.value=message_id;
 	form.task_id.value=task_id;
 	form.openstate.value=openstate;
 	var newwin ="newMessage"+message_id;
	window.open('../aws_html/wait.htm',newwin,'location=no,menubar=yes,fullscreen=yes,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=yes');
 	form.target=newwin;
 	form.submit();
 }   