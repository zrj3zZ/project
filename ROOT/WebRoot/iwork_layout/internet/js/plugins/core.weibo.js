core.weibo = {
    init:function(args){    //微博初始化
        this.args      = args;
        //下面内容是会变化的，所以单独整理出来
        this.loadId   = args.loadId;    //载入的ID
        this.firstId   = 0;
        this.maxId     = args.maxId;     //最大ID
        this.feed_type = args.feed_type;
        this.canshare = args.canshare;//设置分享打勾

        if("undefined" == typeof(this.loadCount)){
            this.loadCount = 1;
        }
        if(this.args.loadmore == 1){
            this.canLoading = true; //当前是否允许加载
            core.weibo.bindScroll();
        }else{
            this.canLoading = false;    //当前是否允许加载
        }
        if("undefined" == typeof(args.initUrl)){
            this.doInitUrl();
        }

        if($('#feed-lists').length > 0 && this.args.loadmore > 0) {
            $('#feed-lists').append("<div class='loading' id='loadMore'>"+L('PUBLIC_LOADING')+"<img src='"+THEME_URL+"/image/load.gif' class='load'></div>");
            this.loadMoreFeed();
        }

    },
    doInitUrl:function(){
        if("undefined" == typeof(this.args)){
            this.args = {};
        }
        this.args.initUrl = {loadMore:U('widget/Feed/loadmore',{},0),loadNew:U('widget/Feed/loadNew',{},0),PostFeed:U('widget/Feed/postFeed')};
    },
    bindScroll:function(){  //页底加载
        var obj = this;
        $(window).bind('scroll resize',function(){
            if(obj.loadCount >=4 || obj.canLoading == false){
                return false;
            }
            var bodyTop = document.documentElement.scrollTop + document.body.scrollTop;
            var bodyHeight = $(document.body).height();
            if(bodyTop+$(window).height() >= bodyHeight-250){
                obj.loadCount = obj.loadCount+1;
                if($('#feed-lists').length > 0){
                    $('#feed-lists').append("<div class='loading' id='loadMore'>"+L('PUBLIC_LOADING')+"<img src='"+THEME_URL+"/image/load.gif' class='load'></div>");
                    core.weibo.loadMoreFeed();
                }
            }
        });
    },
    loadMoreFeed:function(){       //加载更多微博
        var obj = this;
        if(!obj.canLoading){
            return false;
        }
        obj.canLoading = false;
        var loadUrlArgs = { loadId:obj.loadId,
                            type:obj.args.feedType,
                            uid:obj.args.uid,
                            feed_type:obj.feed_type,
                            fgid:obj.args.fgid,
                            ext_id:obj.args.ext_id,
                            topic_id:obj.args.topic_id,
                            model_name:obj.args.model_name,
                            app_name:obj.args.app_name,
                            key:obj.args.key,
                            canshare:obj.args.canshare
                            };
        $.get(obj.args.initUrl.loadMore,loadUrlArgs,function(msg){
            if(msg.status == "0" || msg.status == "-1"){
                $('#loadMore').remove();
                if(msg.status == 0 && ("undefined" != typeof(msg.msg)) && obj.args.loadmore > 0){
                    if($('#feed-lists').length > 0 && $('#feed-lists').find('dl').size() < 1 ){
                        $('#feed-lists').append('<div class="loading" id="loadMore">'+msg.msg+'</div>');
                    }
                }
            }
            if(msg.status == "1"){
                if(msg.firstId > 0 && obj.args.loadnew == 1){
                    obj.firstId = msg.firstId;
                    //启动查找最新的loop
                    obj.startNewLoop();
                }
                $('#loadMore').remove();
                $('#feed-lists').append(msg.html);
                obj.canLoading = true;
                obj.loadId = msg.loadId;
                if(obj.loadCount == 4){
                    if(msg.pageHtml !=null){
                        $('#feed-lists').append('<div id="page" class="page" style="display:none;">'+msg.pageHtml+'</div>');
                    }
                    if(msg.count > 10){     // 4ping + next 说明还有30个以上
                        var href = false;
                        $('#feed-lists .page').find('a').each(function(){
                            href = $(this).attr('href');
                        });
                        //重组分页结构
                        $('#feed-lists .page').html('<a href="#body_page" class="next">'+L('PUBLIC_MORE')+'>></a>').show();
                        $('#feed-lists .page').find('.next').click(function(){
                            core.weibo.loadMoreByPage(href);
                        });
                    }else{
                        if($('#feed-lists').length > 0 && $('#feed-lists').find('dl').size() < 1 ){
                            $('#feed-lists').append('<div class="loading" id="loadMore">'+L('PUBLIC_ISNULL')+'</div>');
                        }
                    }
                }else{
                    core.weibo.bindScroll();
                }
                M(document.getElementById('feed-lists'));
            }
            if(msg.status == "-1"){//异常情况
                return false;
            }
        },'json')
        return false;
    },
    ajaxFeedPage:function(obj){
        $(obj).parent().parent().find('li').removeClass('current');
        $(obj).parent().addClass('current');
        var feed_type = $(obj).parent().attr('rel');
        this.feed_type = feed_type;//更改当前类型
        //这里的loadId用初始的默认ID
        var loadUrlArgs = { loadId:this.args.loadId,
                            type:this.args.feedType,
                            uid:this.args.uid,
                            feed_type:this.feed_type,
                            fgid:this.args.fgid,
                            ext_id:this.args.ext_id,
                            topic_id:this.args.topic_id,
                            model_name:this.args.model_name,
                            app_name:this.args.app_name,
                            p:1
                            };
        var href = this.args.initUrl.loadMore;
        for(var i in loadUrlArgs){
            href +="&"+i+"="+loadUrlArgs[i];
        }
        this.loadMoreByPage(href);
    },
    loadMoreByPage:function(href){//分页加载更多数据
        var obj = this;
        obj.canLoading = false;
        $('#feed-lists').html("<div class='loading' id='loadMore'>"+L('PUBLIC_LOADING')+"<img src='"+THEME_URL+"/image/load.gif' class='load'></div>");
        scrolltotop.scrollup();
        $.get(href,{},function(msg){
            if(msg.status == "0" || msg.status == "-1"){
                $('#feed-lists').find('.loading').html('<div class="box-none"><dt><i class="font64 aicon-bubbles"></i></dt>'+L('PUBLIC_WEIBOISNOTNEW')+'</div>');
            }else{
                $('#feed-lists').html(msg.html);
                if(msg.pageHtml != null){
                    $('#feed-lists').append('<div id="page" class="page" >'+msg.pageHtml+'</div>');
                    $('#feed-lists .page').find('a').each(function(){
                        var href = $(this).attr('href');
                        if(href){
                            $(this).attr('href','javascript:void(0);');
                            $(this).click(function(){
                                core.weibo.loadMoreByPage(href);
                            });
                        }
                    });
                }
                M(document.getElementById('feed-lists'));
            }
        },'json');
        return false;
    },
    startNewLoop:function(){
        var obj = this;
        var searchNew = function(){
            if(obj.firstId < 1){
                return false;
            }
            if(obj.feed_type != ''){
                //非全部类型页面不加载最新的内容
                return false;
            }
            var loadUrlArgs = {maxId:obj.firstId,type:'new'+obj.args.feedType,uid:obj.args.uid,model_name:obj.args.model_name,app_name:obj.args.app_name};
            $.post(obj.args.initUrl.loadNew,loadUrlArgs,function(msg){
                if(msg.status == 1 && msg.count > 0){
                    obj.showNew(msg.count);
                    obj.tempHtml = msg.html;
                    obj.tmpfirstId = msg.maxId;
                }
            },'json');
        };
        //每1分钟查找一次最新微薄
        var loop = setInterval(searchNew,60000);
    },
    showNew:function(nums){
        if($('#feed-lists').find('.notes').length > 0){
            $('#feed-lists').find('.notes').html(L('PUBLIC_WEIBO_NUM',{'sum':nums}));
        }else{
            var html = '<a href="javascript:core.weibo.showNewList()" class="notes">'+L('PUBLIC_WEIBO_NUM',{'sum':nums})+'</a>';
            $('#feed-lists').prepend(html);
        }
    },
    showNewList:function(){
        $('#feed-lists').find('.notes').remove();
        $('#feed-lists').prepend(this.tempHtml);
        this.firstId  = this.tmpfirstId;
        this.tempHtml = '';
        M(document.getElementById('feed-lists'));
    },
    afterPost:function(textarea){   //发布微博之后
        textarea.value = textarea.parentModel.childModels['initHtml'][0].innerHTML;
        if("undefined" != typeof(textarea.parentModel.childModels['nums_left'])){
            textarea.parentModel.childModels['nums_left'][0].innerHTML = L('PUBLIC_INPUT_TIPES',{'sum':'<span>'+$config['initNums']+'</span>'});
        }
        $(textarea.parentModel.childModels['post_ok'][0]).fadeOut(500);
        //修改微博数目
        updateUserData('feed_count',1);
        if("undefined" != typeof(core.uploadFile) ){
            core.uploadFile.removeParentDiv();
        }

        if("undefined" != typeof(core.vote) ){
            core.vote.deleteSendWeiboVote();
        }

        if($('#feed_list_count').length > 0){
            $('#feed_list_count').html(parseInt($('#feed_list_count').html())+1);
        }
        if($('#feed-lists').find('.notes').length > 0){
            core.weibo.showNewList();
        }
        if( $('#recipientsTips').length > 0){
            $('#recipientsTips').hide();
        }
    },
    insertToList:function(html){    // 将json数据插入到feed-lists中
        if("undefined" == typeof(html) || html ==''){
            return false;
        }
        if($('#feed-lists').length>0){
            var before = $('#feed-lists dl').eq(0);
            var _dl = document.createElement('dl');
            _dl.className = 'feed_list';
            _dl.setAttribute('className','feed_list');
            _dl.setAttribute('class','feed_list');
            _dl.setAttribute('model-node','feed_list');
            _dl.innerHTML = html;
            if(before.length>0){
                $(_dl).insertBefore(before);
            }else{
                if($('#feed-lists').find('dl').size() > 0){
                    $(_dl).prependTo( $('#feed-lists') );
                }else{
                    $('#feed-lists').html('');
                    $(_dl).prependTo( $('#feed-lists') );
                }
            }
            M(_dl);
        }
    },

    post_feed:function(_this,textarea,isbox){   //发布微博
        var obj = this;
        //避免重复发送
        if("undefined" == typeof(obj.isposting)){
            obj.isposting = true;
        }else{
            if(obj.isposting == true){
                return false;
            }
        }

        if("undefined" == typeof(isbox)){
            isbox = false;
        }
        //微博类型在此区分
        var attrs =M.getEventArgs(_this);

        var attachobj = $(_this.parentModel).find('.attach_ids');

        if(attachobj.length>0){
            var type = attachobj.attr('feedtype') == 'image' ? 'postimage' : 'postfile';
            var attach_id = attachobj.val();
        }else{
            var attach_id = '';
            var type = attrs.type;
        }

        var content = '';
        //是否有视频
        var video_info = $(_this.parentModel).find('.video_info').val();
        if( $(_this.parentModel).find('.video_info').length>0 && video_info !='' ){
            var type  = 'postvideo';
            var content = video_info;
            $(_this.parentModel).find('.video_info').val('');
        }

        var app_name = attrs.app_name;
        if(getLength(textarea.value) < 1){
            if(type == 'postimage'){
                textarea.value = L('PUBLIC_SHARE_IMAGES');
            }else if(type == 'postfile'){
                textarea.value = L('PUBLIC_SHARE_FILES');
            }else{
                flashTextarea(textarea);
                obj.isposting = false;
                return false;
            }
        }
        //为空处理
        var data = textarea.value;
        if(data == '' || data.length<0){
            ui.error( L('PUBLIC_CENTE_ISNULL') );
            obj.isposting = false;
            return false;
        }

        //同步到其他weibo
        var weibo_sync_type = [];
        $('.weiboSync').each(function(i){
            if($(this).attr("isbind") == "1" ){
                weibo_sync_type.push($(this).attr("rel"));
            }
        });

        //投票
        var vote_id = $(_this.parentModel).find('#send_weibo_vote_id').val();

        _this.className = 'btn btn-gray right';
        $(_this).html('<span>'+L('PUBLIC_CONCENTING')+'</span>');

        $.post(obj.args.initUrl.PostFeed,
            {body:data, type:type, app_name:app_name,model_name:attrs.model_name,ext_id:attrs.ext_id,content:content, attach_id:attach_id, topic_id:attrs.topic_id, vote_id:vote_id, weibo_sync_type:weibo_sync_type,feedType:obj.feedType},
            function(msg){
            obj.isposting = false;
            _this.className = 'btn btn-green right';
            $(_this).html('<span>'+L('PUBLIC_SHARE_BUTTON')+'</span>');
            if(msg.status == 1){
                if("undefined" != typeof(core.uploadFile)){
                    core.uploadFile.clean();
                }
                if("undefined" != typeof(core.uploadWeiboFile)){
                    core.uploadWeiboFile.clean();
                    core.insertImage.closeShow();
                }
                var postOk = _this.parentModel.childModels['post_ok'][0];
                $(postOk).fadeIn('fast');
                core.weibo.afterPost(textarea);
                if(!isbox){
                    core.weibo.insertToList(msg.data);
                }else{
                    setTimeout(ui.box.close,1500);
                }
                //$('#image_insert_box').remove();
            }else{
                ui.error(msg.data);
            }
        },'json');
        return false;
    },
    show_action:function(obj,feed_id){
        var offset = $(obj).offset();
        var show_action_width = $("#show_action").width();
        var screen_height = $(".qg-screen").height();
        var screen_width = $(".qg-screen").width();
        if( $(obj).hasClass("ico-flod-up") || $('#show_action').is(':hidden')){
            //否则显示
            $('#feed-lists .hover').attr('style','');
            $(obj).css("visibility","visible");
            $('#feed-lists .hover').each(function(){
                if($(this).hasClass('ico-flod-down')){
                    $(this).removeClass('ico-flod-down');
                    $(this).addClass('ico-flod-up');
                }
            });
            $(obj).removeClass("ico-flod-up").addClass('ico-flod-down');
            setTimeout(function(){
                $('#show_action').css({
                    "top":offset.top+screen_height+'px',
                    "left":offset.left-show_action_width+screen_width+'px'
                }).show();
            },150);
            $('#show_action a').attr('args',feed_id);
        }else{
            //本来显示则 关闭
            $("#show_action").hide();
            $('#feed-lists .hover').attr('style','');
            $(obj).removeClass("ico-flod-down").addClass('ico-flod-up');
        }
    },
    turn_to_task:function(obj){
        var feed_id    = $(obj).attr('args');
        var app_name   = $('#app_name').text();
        var model_name = $('#model_name').text();
        ui.box.load(U('widget/ItaskAdd/turnTask',{is_widget:'task',feed_id:feed_id,app_name:app_name,model_name:model_name},0),L('PUBLIC_TURN_TASK'));
    },
    set_feed_notice:function(obj){
        var feed_id = $(obj).attr('args');
        $.post(U('core/Do/setNotice'),{'fid':feed_id},function(d){
            if(d == 1){
                ui.success(L('PUBLIC_ADMIN_OPRETING_SUCCESS'));
                location.reload();
            }else{
                ui.error(L('PUBLIC_ADMIN_OPRETING_ERROR'));
            }
        });
    }
};
