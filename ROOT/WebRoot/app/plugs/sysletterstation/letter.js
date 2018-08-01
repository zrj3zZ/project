var rtmsg = {
 
    //请求地址
 
    uri: U("public/RtMsg/rtmsg"),
    //要获取的消息，可以根据需要动态增减，如从模板变量进行分配，这样可以减少不必要的数据浪费宽带
    items:'UnreadCount,Allnet,newfeed',
    //建立连接
    connect: function () {
        $.ajax({
            url: rtmsg.uri,
            data:{'items':rtmsg.items},
            type: 'post',
            dataType: 'json',
            success: function (res) {
                //请求成功后通过HadnleRes对返回的结果进行分配处理
                rtmsg.HandleRes(res.data);
            }
        })
    },
    //将取回的结果分配给对应的处理程序
    HandleRes: function (res) {
        for (x in res) {
            eval('rtmsg.Handle' + x + '(res[x])');
        }
    },
    //处理未读消息数提醒
    HandleUnreadCount: function (res) {
        /*…………*/
    },
    //处理全网喊话
    HandleAllnet: function (allnet) {
        /*…………*/
    },
    //最新说说处理
    HandleNewfeed: function (newfeed) {
        /*…………*/
    }
};
 
 
 
$(function () {
 
    //每隔10秒再来一次
 
    setInterval(rtmsg.connect, 10000);
 
    //页面加载完毕先来一次
 
    rtmsg.connect();
 
})