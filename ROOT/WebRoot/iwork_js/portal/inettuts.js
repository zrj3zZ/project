/*
 * Script from NETTUTS.com [by James Padolsey]
 * @requires jQuery($), jQuery UI & sortable/draggable UI modules
 */

var iNettuts = {
    jQuery : $,
    settings : {
        columns : '.column',
        widgetSelector: '.widget',
        handleSelector: '.widget-head',
        contentSelector: '.widget-content',
        widgetDefault : {
            movable: true,
            removable: true, 
            collapsible: true,
            editable: true,
            colorClasses : ['color-yellow', 'color-red', 'color-blue', 'color-white', 'color-orange', 'color-green']
        },
        widgetIndividual : {
            intro : {
                movable: false,
                removable: false,
                collapsible: false,
                editable: false
            }
        }
    },

    init : function () {
        this.attachStylesheet('../../iwork_css/portal/inettuts.js.css'); 
        this.addWidgetControls();
        this.makeSortable();
    },
    
    getWidgetSettings : function (id) {
        var $ = this.jQuery,
            settings = this.settings;
        return (id&&settings.widgetIndividual[id]) ? $.extend({},settings.widgetDefault,settings.widgetIndividual[id]) : settings.widgetDefault;
    },
    
    addWidgetControls : function () {
        var iNettuts = this,
            $ = this.jQuery,
            settings = this.settings;
            
        $(settings.widgetSelector, $(settings.columns)).each(function () {
            var thisWidgetSettings = iNettuts.getWidgetSettings(this.id);
            if (thisWidgetSettings.removable) {
            	 $('<a href="#" class="remove">关闭</a>').mousedown(function (e) {
                    e.stopPropagation();    
                }).click(function () {
                	if(confirm('确定移除当前窗口吗?')) {
                		var portletId = $(this).parent().parent().attr("id");
                		//执行数据库窗口移除
                		removePortlet(portletId);
                        $(this).parents(settings.widgetSelector).animate({
                            opacity: 0    
                        },function (obj) {
                            $(this).wrap('<div/>').parent().slideUp(function () {
                                $(this).remove();
                            });
                        });
                    }
                    return false;
                }).appendTo($(settings.handleSelector, this));
            }
            
            if (thisWidgetSettings.editable) {
            	var wid = $(this).attr("id");
            	var old_title =$('h3',this).text();
            	var old_style = $(this).attr("class");
            	 $('<a href="#" class="edit">编辑</a>').mousedown(function (e) {
                    e.stopPropagation();    
                }).toggle(function () {
                	$(this).css({"background":"url(../../iwork_img/save.gif) no-repeat"})
                        .parents(settings.widgetSelector)
                            .find('.edit-box').show().find('input').focus();
                    return false;
                },function () { 
                	 $(this).css({"background":"url(../../iwork_img/overlays.png) no-repeat"})
                        .parents(settings.widgetSelector)
                            .find('.edit-box').hide();
                	 //更新栏目设置
                	 var titlestr = $('#settitle_'+wid+'').val();
                	 var stylestr = $('#setcolor_'+wid+'').val();
                	 if(stylestr=='')stylestr = old_style;  //如果当前样式为空，则赋值原始样式
                	 if(old_title==$('#settitle_'+wid+'').val()&&old_style==stylestr){  //判断，如果设置标题与原始标题相同则不执行更新
                		 
                	 }else{ 
                		 updateColumnSet(wid,titlestr,stylestr); 
                	 } 
                	// if($('#settitle_'+wid+'').val()=='')
                	
                    return false;
                }).appendTo($(settings.handleSelector,this));
                $('<div class="edit-box" style="display:none;"/>')
                .append('<ul><li class="item"><label>标题:</label><input style="text-align:left;width:150px"  id="settitle_'+wid+'" value="' + $('h3',this).text() + '"/></li>')
                .append((function(){  
                    var colorList = '<li class="item"><label>栏目颜色:</label><ul class="colors">';
                    $(thisWidgetSettings.colorClasses).each(function () {
                        colorList += '<li class="' + this + '"/>';
                    });
                    
                    return colorList + '</ul><input type="hidden" id="setcolor_'+wid+'">';
                })())
              //  .append('<li class="item"><label>高度:</label><input style="width:50px" id="setheight" value=""/></li>')
                .append('</ul>')
                .insertAfter($(settings.handleSelector,this));
            }
            
            if (thisWidgetSettings.collapsible) {
                $('<a href="#" class="collapse">展开</a>').mousedown(function (e) {
                    e.stopPropagation();    
                }).toggle(function () {
                	 $(this).css({"background":"url(../../iwork_img/arrow_right.gif) no-repeat"})
                        .parents(settings.widgetSelector)
                            .find(settings.contentSelector).hide();
                    return false;
                },function () {
                	 $(this).css({"background":"url(../../iwork_img/arrow_down.gif) no-repeat"})
                        .parents(settings.widgetSelector)
                            .find(settings.contentSelector).show();
                    return false;
                }).prependTo($(settings.handleSelector,this));
            }
        });
        
        $('.edit-box').each(function () {
            $('input',this).keyup(function () {
                $(this).parents(settings.widgetSelector).find('h3').text( $(this).val().length>20 ? $(this).val().substr(0,20)+'...' : $(this).val() );
            });
            $('ul.colors li',this).click(function () {
                var colorStylePattern = /\bcolor-[\w]{1,}\b/,
                    thisWidgetColorClass = $(this).parents(settings.widgetSelector).attr('class').match(colorStylePattern)
                if (thisWidgetColorClass) {
                    $(this).parents(settings.widgetSelector)
                        .removeClass(thisWidgetColorClass[0])
                        .addClass($(this).attr('class').match(colorStylePattern)[0]);
                }
                $("#setcolor_"+$(this).parent().parent().parent().parent().attr("id")).val($(this).attr('class'));
                return false;
                
            });
        });
        
    },
    
    attachStylesheet : function (href) {
        var $ = this.jQuery;
        return $('<link href="' + href + '" rel="stylesheet" type="text/css" />').appendTo('head');
    },
    
    makeSortable : function () {
        var iNettuts = this,
            $ = this.jQuery,
            settings = this.settings,
            $sortableItems = (function () {
                var notSortable = null;
                $(settings.widgetSelector,$(settings.columns)).each(function (i) {
                    if (!iNettuts.getWidgetSettings(this.id).movable) {
                        if(!this.id) {
                            this.id = 'widget-no-id-' + i;
                        }
                        notSortable += '#' + this.id + ',';
                    }
                });
                return $('> li:not(' + notSortable + ')', settings.columns);
            })();
        
        $sortableItems.find(settings.handleSelector).css({
            cursor: 'move'
        }).mousedown(function (e) {
            $sortableItems.css({width:''});
            $(this).parent().css({
                width: $(this).parent().width() + 'px'
                
            });
        }).mouseup(function () {
            if(!$(this).parent().hasClass('dragging')) {
                $(this).parent().css({width:''});
            } else {
                $(settings.columns).sortable('disable');
            }
        });

        $(settings.columns).sortable({
            items: $sortableItems,
            connectWith: $(settings.columns),
            handle: settings.handleSelector,
            placeholder: 'widget-placeholder',
            forcePlaceholderSize: true,
            revert: 300,
            delay: 100,
            opacity: 0.8,
            containment: 'document',
            start: function (e,ui) {
                $(ui.helper).addClass('dragging');
            },
            stop: function (e,ui) {
                $(ui.item).css({width:''}).removeClass('dragging');
                $(settings.columns).sortable('enable');
                var wid = ui.item.attr("id");
            	var groupid = ui.item.parent().attr("id");
            	var order = new Array();
            	$(settings.widgetSelector,$(settings.columns)).each(function (i) {
            		var id = $(this).attr("id"); 
            		if($(this).parent().attr("id")==groupid){
            			var portletid = "";
            			if(id.indexOf("widget_")==0){
                			var start = id.indexOf("widget_");
                			portletid = id.substr(7);
                			order.push(portletid); 
                		} 
            			
            		}
            	});
            	movePortlet(groupid,wid,order);
            	/*$(this).parents(settings.widgetSelector)
            	 $(settings.columns).each(function (i) {
                    alert($(this).attr("id"));
                 });
            	//alert(wid+">>"+groupid);*/
            }
        });
    }, 
    showOrderlist : function () {
    	var order = "[";
    	  var iNettuts = this,
          $ = this.jQuery,
          settings = this.settings;
    	$(settings.columns).each(function (i) {
    		order+="{\""+this.id+"\":[";
    		var col = new Array();
    		$(settings.widgetSelector,this).each(function(i){
    			var str = this.id;
    			str=str.replace("widget_", "")
    			col.push("\""+str+"\"");
            }); 
    		order+=col+"]},";
		}); 
    	order+="]"
    		order=order.replace("},]","}]"); 
    	return order;
    }
  
};

iNettuts.init();