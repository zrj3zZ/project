	//是否在拖动
	var isDragedCell=false;
	//被拖动的窗口ID
	var dragedCellId;
	//影子
	AWSDragDIV=null;
	//插入点的html语法，需要解析这段语法得出位置
	var movePointHtml;	
	//开始拖
	function dragStart(){
		ao=event.srcElement;
		if((ao.tagName=="TD")||(ao.tagName=="TR"))
			ao=ao.offsetParent;
		else 
			return;
		//被拖动的窗口ID
		dragedCellId=ao.id;
		isDragedCell=true;
		AWSDragDIV=document.createElement("div");
		AWSDragDIV.innerHTML=ao.outerHTML;
		AWSDragDIV.style.display="block";
		AWSDragDIV.style.position="absolute";
		AWSDragDIV.style.filter="alpha(opacity=70)";
		AWSDragDIV.style.cursor="move";
		AWSDragDIV.style.width=ao.offsetWidth;
		AWSDragDIV.style.height=ao.offsetHeight;
		AWSDragDIV.style.top=getInfo(ao).top;
		AWSDragDIV.style.left=getInfo(ao).left;
		document.body.appendChild(AWSDragDIV);
		lastX=event.clientX;
		lastY=event.clientY;
		lastLeft=AWSDragDIV.style.left;
		lastTop=AWSDragDIV.style.top;
		
		try{
			ao.dragDrop();    
		}catch(e){}
		getNewPosition();
	}
	

	
	//正在拖，判断MOUSE的位置
	function draging(){
		if(!isDragedCell)return;
		var tX=event.clientX;
		var tY=document.body.scrollTop+event.clientY;
		AWSDragDIV.style.left=parseInt(lastLeft)+tX-lastX;
		AWSDragDIV.style.top=tY;
		for(var i=0;i<PortalsTable.cells.length;i++){
			var parentCell=getInfo(PortalsTable.cells[i]);
			if(tX>=parentCell.left&&tX<=parentCell.right&&tY>=parentCell.top&&tY<=parentCell.bottom){
				var subTables=PortalsTable.cells[i].getElementsByTagName("table");
				if(subTables.length==0){
					if(tX>=parentCell.left&&tX<=parentCell.right&&tY>=parentCell.top&&tY<=parentCell.bottom){
						PortalsTable.cells[i].appendChild(ao);
						//movePointHtml=PortalsTable.cells[i].outerHTML;
					}
					break;
				}
				for(var j=0;j<subTables.length;j++){
					var subTable=getInfo(subTables[j]);
					if(tX>=subTable.left&&tX<=subTable.right&&tY>=subTable.top&&tY<=subTable.bottom){
						PortalsTable.cells[i].insertBefore(ao,subTables[j]);										
						//movePointHtml=PortalsTable.cells[i].outerHTML;
						break;
					}else{
						PortalsTable.cells[i].appendChild(ao);	
						//movePointHtml=PortalsTable.cells[i].outerHTML;												
					}    
				}
			}
			
		}
	}
	
	//放下
	function dragEnd(){
	    if(!isDragedCell)return;
	    isDragedCell=false;
	    mm=ff(150,15);
	}
	
	//取得坐标
	function getInfo(o){
		var to=new Object();
		to.left=to.right=to.top=to.bottom=0;
		var twidth=o.offsetWidth;
		var theight=o.offsetHeight;
		while(o!=document.body){
			to.left+=o.offsetLeft;
			to.top+=o.offsetTop;
			o=o.offsetParent;
		}
		to.right=to.left+twidth;
		to.bottom=to.top+theight;
		return to;
	}
	
	//恢复位置
	function ff(aa,ab){
		var ac=parseInt(getInfo(AWSDragDIV).left);
		var ad=parseInt(getInfo(AWSDragDIV).top);
		var ae=(ac-getInfo(ao).left)/ab;
		var af=(ad-getInfo(ao).top)/ab;
		return setInterval(function(){
			if(ab<1){
				clearInterval(mm);
				AWSDragDIV.removeNode(true);
				ao=null;
				getNewPosition();
				return
			}
			ab--;
			ac-=ae;
			ad-=af;
			AWSDragDIV.style.left=parseInt(ac)+"px";
			AWSDragDIV.style.top=parseInt(ad)+"px"
		},aa/ab)
					
	}
	
	//初始化
	function inint(){
		for(var i=0;i<PortalsTable.cells.length;i++){
			var subTables=PortalsTable.cells[i].getElementsByTagName("table");			
			for(var j=0;j<subTables.length;j++){
				if(subTables[j].className!="dragTable")break;
				subTables[j].id="Portlet"+i;
				subTables[j].rows[0].className="dragTR";
				subTables[j].rows[0].attachEvent("onmousedown",dragStart);
				subTables[j].attachEvent("ondrag",draging);
				subTables[j].attachEvent("ondragend",dragEnd);
			}
		}
	}
	
	//计算改变后的位置
	function getNewPosition(){
		var positionStr="";
		for(var i=0;i<PortalsTable.cells.length;i++){
			var subTables=PortalsTable.cells[i].getElementsByTagName("table");			
			for(var j=0;j<subTables.length;j++){
				if(subTables[j].className=="poolTable"){
					positionStr=positionStr+"_"+subTables[j].id+":";
				}
				if(subTables[j].className=="dragTable"){					
					positionStr=positionStr+subTables[j].id+" ";
				}
			}
		}
		if(frmMain.defaultPosition.value!=positionStr){
			frmMain.defaultPosition.value=positionStr;
			frmMain.newPosition.value=positionStr;
			//提交到服务器
			requestSaveNewPosition(frmMain,'CmChannel_Manager_SaveNewPosition');
		}
		
	}