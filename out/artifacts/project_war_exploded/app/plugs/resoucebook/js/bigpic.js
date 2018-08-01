    jQuery(function(){  
        jQuery("img").each(function(i){  
            var p = jQuery(this);  
            var strbp = p.attr("bigpic");  
            if(strbp){  
                var bp = jQuery("<div class='bigpic'></div>").appendTo("body").hide();  
                var w = this.width;  
                p.mouseover(function(){  
                    bp.fadeIn();  
                    var offset = p.offset();  
                    bp.css("top", offset.top).css("left", offset.left+w+5);  
                    if(bp.html()=="")  
                        bp.html("<img src=+p.attr('bigpic')"+" mce_src="+p.attr('bigpic')+">");  
                }).mouseout(function(){  
                    bp.fadeOut();  
                });  
            }  
        });  
    });  