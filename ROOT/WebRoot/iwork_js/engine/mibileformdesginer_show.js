/**
 * 
 */
	var dialog;
	var editor1;
	KindEditor.ready(function(K) { 
			 editor1 = K.create('textarea[name="htmlEditor"]', { 
				cssPath : 'iwork_css/formMobile.css',
				uploadJson : '../jsp/upload_json.jsp',
				fileManagerJson : '../jsp/file_manager_json.jsp', 
				//allowFileManager : true, 
				width : '100%', 
				themeType:'simple',
				items : [ 
					'save','lookupformmap','lookupmetadatamap','refresh','rebuildtemplate','source' ,'|', 'preview', 'selectfield', '|', 'undo', 'redo' ,'template', 'code','|', 'cut', 'copy', 'paste',
					'plainpaste', 'wordpaste', '|', '/', 'formatblock', 'fontname', 'fontsize', '|', 'forecolor', 'hilitecolor', 'bold',
					'italic', 'underline', 'strikethrough', 'lineheight', 'removeformat', '|','justifyleft', 'justifycenter', 'justifyright',
					'justifyfull', 'insertorderedlist', 'insertunorderedlist', 'indent', 'outdent', 'subscript',
					'superscript', 'clearhtml', 'quickformat', 'selectall',   
					'flash', 'media', 'insertfile', 'table', 'hr',   'pagebreak',
					'anchor', 'link', 'unlink'
				],
				colorTable : [
				['#E53333', '#E56600', '#FF9900', '#64451D', '#DFC5A4', '#FFE500',  '#642100', '#6C3365', '#003D79', '#3C3C3C', '#5B4B00', '#007500'],
				['#009900', '#006600', '#99BB00', '#B8D100', '#60D978', '#00D5FF',  '#A23400', '#8F4586', '#005AB5', '#4F4F4F', '#AE8F00', '#00BB00'],
				['#337FE5', '#003399', '#4C33E5', '#9933E5', '#CC33E5', '#EE33EE',  '#D94600', '#AE57A4', '#0080FF', '#6C6C6C', '#D9B300', '#00EC00'],
				['#FFFFFF', '#CCCCCC', '#999999', '#666666', '#333333', '#000000',  '#FF5809', '#C07AB8', '#46A3FF', '#8E8E8E', '#FFD306', '#79FF79'],
				['#8080C0', '#336699', '#6699CC', '#479AC7', '#66CCCC', '#00B271',  '#FF8F59', '#CA8EC2', '#84C1FF', '#ADADAD', '#FFE66F', '#A6FFA6'],
				['#DDF3FF', '#D5F3F4', '#D7FFF0', '#E8D098', '#F4A460', '#B45B3E',  '#FFAD86', '#D2A2CC', '#ACD6FF', '#BEBEBE', '#FFF0AC', '#CEFFCE'],
				['#F1FAFA', '#E8FFE8', '#E8E8FF', '#F2F1D7', '#EFEFDA', '#F0DAD2',  '#FFCBB3', '#E2C2DE', '#D2E9FF', '#E0E0E0', '#FFF8D7', '#DFFFDF'],
				['#EEEEEE', '#F0F8FF', '#FFFACD', '#F5FFFA', '#E6E6FA', '#C0C0C0',  '#FFE6D9', '#EBD3E8', '#ECF5FF', '#F0F0F0', '#FFFCEC', '#F0FFF0']
				],
				fullscreenMode:true,
				newlineTag:'br',
				height : '460px',
				filterMode:false,
				afterCreate : function() {
					var self = this;
					K.ctrl(document, 13, function() {
						self.sync();
						document.forms['editForm'].submit();
					});
					K.ctrl(self.edit.doc, 13, function() {
						self.sync();
						document.forms['editForm'].submit();
					});
				}
			});
			prettyPrint(); 
		});
		// 自定义插件 #1
			KindEditor.lang({
				save:'保存表单模板',
				rebuildtemplate:'重新生成表单',
				refresh:'刷新',
				lookupformmap:'钻取表单设置',
				selectfield:'插入表单域',
				lookupmetadatamap:'钻取存储设置'
			});
			KindEditor.plugin('save', function(K) {
				var self = this, name = 'save';
				self.clickToolbar(name, function() {
					self.sync();
					doSubmit();//保存
					return false;
				});
			});
			//重新构建模板
			KindEditor.plugin('rebuildtemplate', function(K) {
				var self = this, name = 'rebuildtemplate';
				self.clickToolbar(name, function() {
					var fid = $('#formid').val();
					refreshTemplate(fid); 
				});
			});
			//刷新
			KindEditor.plugin('refresh', function(K) {
					var self = this, name = 'refresh';
					self.clickToolbar(name, function() {
						 window.location.reload();
					});
				});
				//预览
			KindEditor.plugin('preview', function(K) {
					var self = this, name = 'preview';
					self.clickToolbar(name, function() {
					var fid = $('#formid').val();
						doPreview(fid);
					});
				});
			KindEditor.plugin('selectfield', function(K) {
				var self = this, name = 'selectfield';
				self.clickToolbar(name, function() {
				//获取formid;
				var formid = $('#formid').val();
				//修改属性信息
				dialog = K.dialog({
					width : 600,
					height : 450,
					title : '插入表单域',
					body : '<iframe width="100%" height="100%" frameborder="0" src="sysEngineIFormMap_Tree.action?formid='+formid+'"></iframe>',
					closeBtn : {  
						name : '关闭',
						click : function(e) {
							close(); 
						}
					}
			    });
				});
			});
			//查找表单域
			KindEditor.plugin('lookupformmap', function(K) {
				var self = this, name = 'lookupformmap';
				self.clickToolbar(name, function() {
					var txt = self.selectedHtml();
					//正则表达式匹配
					txt = txt.replace(/<img[^>]*class="formObj"?[^>]*>/ig, function(full) {
						var imgAttrs = _getAttrList(full);
						return unescape(imgAttrs['data-ke-name']);
					});
					var isch_zn = isChineseChar(txt);       //判断是否有中文
					var isfull  = isFullwidthChar(txt);     //判断是否有全角符号
					if(txt!=''&&txt.indexOf(">")<0&&txt.indexOf("<")<0&&!isch_zn&&!isfull){
						txt = txt.replace("$","");
						txt = txt.replace("{","");
						txt = txt.replace("}","");
						txt = txt.replace("&nbsp;",""); 
						//获取formid;
						var formid = $('#formid').val();
						//修改属性信息
						dialog = K.dialog({
							width : 600,
							height : 450,
							title : '表单域设置',
							body : '<iframe width="100%" height="100%" frameborder="0" src="sysEngineIFormMap_show.action?formid='+formid+'&fieldName='+txt+'"></iframe>',
							closeBtn : { 
								name : '关闭',
								click : function(e) {
									close(); 
								}
							}
					    });
					}else if(txt==''){
						alert("请在表单中选择您要钻取的域标记");
					}else{
						alert("选中信息不合法");
					}
				});
			});
			//查找存储域
			KindEditor.plugin('lookupmetadatamap', function(K) {
				var self = this, name = 'lookupmetadatamap';
				self.clickToolbar(name, function() {
					var txt = self.selectedHtml();
					//正则表达式匹配
					txt = txt.replace(/<img[^>]*class="formObj"?[^>]*>/ig, function(full) {
						var imgAttrs = _getAttrList(full);
						return unescape(imgAttrs['data-ke-name']);
					});
					var isch_zn = isChineseChar(txt);       //判断是否有中文
					var isfull  = isFullwidthChar(txt);     //判断是否有全角符号
					if(txt!=''&&txt.indexOf(">")<0&&txt.indexOf("<")<0&&!isch_zn&&!isfull){
						txt = txt.replace("$","");
						txt = txt.replace("{","");
						txt = txt.replace("}","");
						txt = txt.replace("&nbsp;",""); 
						//获取formid;
						var metadataid = $('#metadataid').val();
						//修改属性信息
						dialog = K.dialog({
							width : 600,
							height : 450,
							title : '存储设置',
							body : '<iframe width="100%" height="100%" frameborder="0" src="sysEngineMetadataMap_edit.action?metadataid='+metadataid+'&fieldName='+txt+'"></iframe>',
							closeBtn : {  
								name : '关闭',
								click : function(e) {
									close(); 
								}
							}
						});
					}else if(txt==''){
						alert("请在表单中选择您要钻取的域标记");
					}else{
						alert("选中信息不合法");
					}
				});
			});
		function insertField(val){ 
			editor1.insertHtml(val);
			close();
		} 
		//g关闭
		function close(){     
			dialog.remove();
		} 
		function _getAttrList(tag) {
			var list = {},
				reg = /\s+(?:([\w\-:]+)|(?:([\w\-:]+)=([^\s"'<>]+))|(?:([\w\-:"]+)="([^"]*)")|(?:([\w\-:"]+)='([^']*)'))(?=(?:\s|\/|>)+)/g,
				match;
			while ((match = reg.exec(tag))) {
				var key = (match[1] || match[2] || match[4] || match[6]).toLowerCase(),
					val = (match[2] ? match[3] : (match[4] ? match[5] : match[7])) || '';
				list[key] = val;
			}
			return list;
		}
		function showDialog(title,pageUrl,width,height){
			alert(pageUrl);
			var d = KindEditor.dialog({
		        width : width,
		        height : height, 
		        title : title,
				body : '<iframe width="100%" height="100%" frameborder="0" src="'+pageUrl+'"></iframe>',
		        closeBtn : {
		                name : '关闭',
		                click : function(e) {
		                        d.remove();
		                }
		        },
		        yesBtn : {
		                name : '确定',
		                click : function(e) {
		                        alert(this.value);
		                }
		        }
		});
			
		}
		//是否含有中文（也包含日文和韩文）  
		function isChineseChar(str){     
			var reg = /[\u4E00-\u9FA5\uF900-\uFA2D]/;  
			return reg.test(str);  
		}  
		//同理，是否含有全角符号的函数  
		function isFullwidthChar(str){  
		   var reg = /[\uFF00-\uFFEF]/;  
		   return reg.test(str);  
		}  
		function doSubmit(){
			var content = $("#htmlEditor").val().toUpperCase();
			//判断是否包含form域
			if(content.indexOf("<FORM")>0){
				alert('表单中不允许包含<form>域标记,请取消后保存!');
				return; 
			}
			var url = "sysEngineIformDesginer_save.action";
			$('form').attr('action',url); 
			$('form').attr('target',"_self");
			$('form').submit(); 
		}
		function refreshTemplate(formid){
			var url = "sysEngineIformTemplate_show.action";
			$('form').attr('action',url); 
			$('form').attr('target',"_self");
			$('form').submit(); 
		}
		
		function doPreview(formid){
			var url = "iform_preview.action";
			$('form').attr('action',url); 
			$('form').attr('target',"_blank");
			$('form').submit(); 
		}
		
		function showMaxWindow(formid){
			var target = 'max_'+formid;
			var page = window.open('form/loader_frame.html',target,'width='+screen.width+',height='+screen.height+',top=50,left=150,location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=yes');
			page.location=this.location;
			return;
		} 