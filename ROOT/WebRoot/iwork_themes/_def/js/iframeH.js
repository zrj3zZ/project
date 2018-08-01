
/*	function iframe_resize(){ 
		var frame =  document.getElementById("kunFrame");
		frame.style.height= frame.contentWindow.document.body.scrollHeight;
		return true; 
	}
	
*/	
	function initFrameHeight(){
            var frame = document.getElementById("ageFrame");
            var height = frame.height;
            var iframe =  document.getElementById("kunFrame");
	    iframe.style.height=height-50;
	}


	function initFrameHeightBig(){
		var frame = document.frames("comtentFrame").frames("ageFrame");
		var height = frame.parent.document.getElementById("ageFrame").height;			
		var iframe =  frame.document.getElementById("kunFrame");
		iframe.style.height=height+70;
	//	alert(iframe.style.height);
	}

	
	function initFrameHeightSm(){
		var frame = document.frames("comtentFrame").frames("ageFrame");
		var height = frame.parent.document.getElementById("ageFrame").height;			
		var iframe =  frame.document.getElementById("kunFrame");
		iframe.style.height=height-170;
		//alert(iframe.style.height);
	}
