//表单自动填充补全功能
$(function(){
	//$("[form-type='hr_addressbook']").autocomplete('user_load_autocomplete_json.action', {
	/*
		$("[form-type='hr_addressbook']").autocomplete('hr_info_load_autocomplete_json.action', {
		multiple: false,
		dataType: "json", 
		minChars:2,
		 matchContains: true,    //包含匹配，就是data参数里的数据，是否只要包含文本框里的数据就显示
		 width: 500, 
		 autoFill: false,    //自动填充
		 parse: function(data) { 
			return $.map(data, function(row) { 
				return {
					data: row,
					value: row.EMPLOYEENAME,
					result: row.ACCOUNT+"["+row.EMPLOYEENAME+"]"
				}
			});
		},
		formatItem: function(item) {
			return formathr(item);
		}
	}).result(function(e, item) {
		try{
			execAutoLinkSel(this.name,item.to);
		}catch(e){
			
		}
	});
	*/ 
	$("[form-type='radioAddress']").autocomplete('user_load_autocomplete_json.action', {
		multiple: false,
		dataType: "json",
		minChars:2,
		 matchContains: true,    //包含匹配，就是data参数里的数据，是否只要包含文本框里的数据就显示
		 width: 500, 
		 autoFill: false,    //自动填充
		 parse: function(data) {
			return $.map(data, function(row) { 
				return {
					data: row,
					value: row.name,
					result: row.to
				}
			});
		},
		formatItem: function(item) {
			return format(item);
		}
	}).result(function(e, obj) {
		try{
			execAutoLinkSel(this.name,item.to);
		}catch(e){
			
		}
	});
	
	$("[form-type='multiAddress']").autocomplete('user_load_autocomplete_json.action', {
		multiple: true,
		minChars:2,
		dataType: "json",
		matchContains: true,    //包含匹配，就是data参数里的数据，是否只要包含文本框里的数据就显示
		width: 500, 
		autoFill: false,    //自动填充
		parse: function(data) {
		return $.map(data, function(row) {
			return {
				data: row,
				value: row.name,
				result: row.to
			}
		});
	},
	formatItem: function(item) {
		return format(item);
	}
	}).result(function(e, item) {
		try{
			execAutoLinkSel(this.name,item.to);
		}catch(e){
			
		}
	}); 
	$("[form-type='al_textbox']").autocomplete('loadFormInputHistoryJSON.action', {
	//$(":text").autocomplete('loadFormInputHistoryJSON.action', { 
		multiple: false,
		dataType: "json", 
		extraParams:{fieldName:function(o){ return $(event.target).attr('id'); },formid:$("#formid").val()},   
		matchContains: true,    //包含匹配，就是data参数里的数据，是否只要包含文本框里的数据就显示
		autoFill: false,    //自动填充 
		max: 12,    //列表里的条目数 
		parse: function(data) {
		return $.map(data, function(row) {
			return {
				data: row, 
				value: row,
				result: row
			}
		});
	},
	formatItem: function(item) {
		return item;
	}
	}).result(function(e, item) {
		try{
			execAutoLinkSel(this.name,item.to);
		}catch(e){
			
		}
	});
});

function format(row) {
	return row.name + " &lt;" + row.to + "&gt";
}
function formathr(row) {
	return row.EMPLOYEENAME + "[" + row.ACCOUNT + "]" + " [" + row.COMPANYNAME + "]";
}