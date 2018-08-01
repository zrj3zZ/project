function clean(inputId){
$("input[name="+inputId+"]").val("")
if($("[name="+inputId+"DQ]")[0]!=null&&$("[name="+inputId+"DQ]")[0].checked){
$("input[name="+inputId+"DQ]").attr("checked",false);
}
}