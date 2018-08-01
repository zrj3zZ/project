var api = frameElement.api, W = api.opener, D = document;
//上传照片
function doUpFilePersion(url){
		var userid = $('#saveImageForm_userid').val();
		if(url==''){
			return false;
		}
		uploadForm.action = "syspersion_photo_UpFile.action?url=" + encodeURI(url)+"&userid="+userid;
		uploadForm.submit();
	    return false;
	}
function autoUpload(){
		doUpFilePersion(uploadForm.myFile.value);
	}
//预览
jQuery(function($){

      // Create variables (in this scope) to hold the API and image size
      var jcrop_api, boundx, boundy;
      
      $('#target').Jcrop({
        onChange: updatePreview,
        onSelect: updatePreview,
        aspectRatio: 1
      },function(){
        // Use the API to get the real image size
        var bounds = this.getBounds();
        boundx = bounds[0];
        boundy = bounds[1];
        // Store the API in the jcrop_api variable
        jcrop_api = this;
      });

      function updatePreview(c)
      {
        if (parseInt(c.w) > 0)
        {
          var rx = 100 / c.w;
          var ry = 100 / c.h;

          $('#preview').css({
            width: Math.round(rx * boundx) + 'px',
            height: Math.round(ry * boundy) + 'px',
            marginLeft: '-' + Math.round(rx * c.x) + 'px',
            marginTop: '-' + Math.round(ry * c.y) + 'px'
          });
        }
        $('#imgInfo').val(boundx+"_"+c.x+"_"+c.y+"_"+c.w+"_"+c.h);
      };

    });
function saveImage(){
    	var imgInfo = $('#imgInfo').val();
    	if(""==imgInfo || null== imgInfo){
    		setTimeout('api.close();',200);
    	}else{
	   		var options = {
			error:errorFunc,
			success:showResponse 
			};
			$("#saveImageForm").ajaxSubmit(options);
    	}
	}
errorFunc = function(){
		lhgdialog.tips("头像保存失败，返回值异常(错误号:ERROR-1001)",2);
	}
showResponse = function(responseText, statusText, xhr, $form){
		if(responseText=="success"){
			lhgdialog.tips("头像保存成功",2);
			setTimeout('api.close();',2000);
		}else if(responseText=="ERROR-1002"){
			lhgdialog.tips("原图大小小于截取图片大小,保存失败(错误号:ERROR-1002)",2);
		}else if(responseText=="ERROR-1003"){
			lhgdialog.tips("为保证头像显示效果,请勿截取过小的图片(错误号:ERROR-1003)",2);
		}else{
			lhgdialog.tips("头像保存失败，返回值异常(错误号:ERROR-1001)",2);
		}
	}