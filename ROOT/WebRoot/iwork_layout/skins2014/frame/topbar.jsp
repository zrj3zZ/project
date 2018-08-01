<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div id="qm-header">
    <div id="header">
                  <div class="qg-logo"><a href="mainAction.action"><img src="iwork_layout/skins2014/image/logo1.png"></a></div>
                        <div class="qg-version dorp-left"> 
                    <span class="txt">BPM<i class="ico-arrow-down"></i></span>
                    <div class="qg-topmenu qg-topmenu-space" model-node="drop_menu_list">
                        <dl class="my-ch">
                            <dd class="current">
                                <i class="qg-ico18 ico-choosed"></i>
                                    <a href="mainAction">BPM</a> 
                                    <div class="qg-tips" style="display:none">
                                        <div class="tips-up"><span><em>◆</em></span></div>
                                        <div class="tips-btm">默认空间</div>
                                    </div>
                            </dd>                                                        <dd class="q-func"><a href="" >添加模块到“更多”</a></dd>
                                                    </dl>
                    </div>
            </div>        <!-- 已登录 --> 
        <!-- 头部导航END  -->
        <div class="qg-person">
            <ul>
                <li class="dorp-right"><a href="mainAction.action" class="q-tab">我的首页<i class="ico-arrow-down"></i></a>
                    <div class="qg-topmenu qg-topmenu-profile" model-node="drop_menu_list">
                        <dl>
                            <dd><a href="###">本人信息</a></dd>
                            <dd><a href="##">待办任务</a></dd>
                            <dd><a href="##">已办跟踪</a></dd>
                            <dd><a href='##'>通知抄送</a></dd>
                            <dd><a href=''>办理日志</a></dd>
                           </dl>
                    </div>
                </li>

                <li class="dorp-right"><a href="javascript:;" class="q-tab msg-new"><i class="aicon-email"></i></a>
                    <div class="qg-topmenu qg-topmenu-msg" model-node="drop_menu_list">
                        <dl>
                            <dd><a href="">
                                <label id='unread_message'>
                                                                </label>
                                私信                            </a></dd>

                            <dd><a href="javascript:redirectUrl('sys_message.action')">
                                <label id='unread_notify'>
                                                            </label>
                            系统消息                            </a></dd>
                            
                        </dl>
                        <div class="q-func"><a onclick="ui.sendmessage()" href="javascript:void(0)" event-node="postMsg">发私信»</a></div>
                    </div>
                </li>
                <li class="dorp-right"><a href="javascript:;" class="q-tab"><i class="aicon-set"></i></a>
                    <div class="qg-topmenu qg-topmenu-set" model-node="drop_menu_list">
                        <dl>
                            <dd><a href="javascript:redirectUrl('syspersion_info.action')">个人设置</a></dd>
                                                        <dd>
                            </dd>                            <dd><a target="_blank" href="console.action">管理后台</a></dd>                            <dd class="qg-lan">
                        <span onclick="setLang('zh-cn')" class="cur" rel="zh-cn" title="中文简体"><i class="qg-ico18 ico-lan-cn"></i></span><%--
                        <span onclick="setLang('zh-tw')" rel="zh-tw" title="中文繁體"><i class="qg-ico18 ico-lan-tw"></i></span>
                        --%><span onclick="setLang('en')"  rel="en" title="English"><i class="qg-ico18 ico-lan-en"></i></span>
                            </dd>
                        </dl>
                        <div class="q-func"><a href="">退出</a></div>
                    </div>
                </li>
            </ul>
           </div>
            <form method='GET' action=""  class="qg-search">
            <input type="text" class="txt" placeholder="输入关键字"  name='k' value="">
            <input type="submit" class="button" value=''>
            </form>
            <div class="qg-nav">
                <ul> 
                    <li class="current">
                   	 <a href="mainAction.action" class="q-nav"><span>首页</span></a>
                    </li>                 
                    <li >
                  	  	<a href="javascript:selectTopMenuBar('process_desk_index.action')"  class="q-nav"><span>流程中心</span></a></li>                
                    <li >
                   		 <a href="javascript:selectTopMenuBar('ireport_rt_showcenter.action')"  class="q-nav"><span>报表中心</span></a>
                    </li>                <li >
                    <a href="javascript:redirectUrl2('kmdoc_Index.action')" class="q-nav"><span>知识库</span></a></li>                <li >
                    <a href="javascript:redirectUrl2('know_index.action')"  class="q-nav"><span>问答</span></a></li>                <li >
                    <li class="dorp-right"><a href="javascript:;" class="q-nav">更多<i class="ico-arrow-down"></i></a>
                    <div class="qg-topmenu qg-topmenu-nav" model-node="drop_menu_list">
                        <dl>
                          <dd>
                            <a href="https://itd.qimingdao.com/meeting/Index/index"><span></span></a>
                        </dd>                        <dd>
                            <a href="https://itd.qimingdao.com/crm/Index/index"><span>CRM</span></a>
                        </dd>                       
                         <dd>
                            <a href="https://itd.qimingdao.com/file/Index/index"><span>文件</span></a>
                        </dd>                      
                         <dd>
                            <a href="https://itd.qimingdao.com/file/Index/index"><span>文件</span></a>
                        </dd>                       
                         <dd>
                            <a href="https://itd.qimingdao.com/file/Index/index"><span>文件</span></a>
                        </dd>                      
                         <dd>
                            <a href="https://itd.qimingdao.com/file/Index/index"><span>文件</span></a>
                        </dd>                      
                         <dd>
                            <a href="https://itd.qimingdao.com/file/Index/index"><span>文件</span></a>
                        </dd>                      
                         <dd>
                            <a href="https://itd.qimingdao.com/file/Index/index"><span>文件</span></a>
                        </dd>                      
                        </dl>
                    </div>
                </li>
                                </ul>
            </div>

           <div id="message_container" class="layer-massage-box" style="display:none">
                <ul class="message_list_container" >
                    <li rel="new_folower_count" style="display:none"><span></span>，<a href="https://itd.qimingdao.com/core/Profile/follower?uid=3523">查看粉丝</a></li>
                    <li rel="unread_comment" style="display:none"><span></span>，<a href="https://itd.qimingdao.com/core/Comment/index">查看消息</a></li>
                    <li rel="unread_message" style="display:none"><span></span>，<a href="https://itd.qimingdao.com/core/Message/message" >查看消息</a></li>
                    <li rel="unread_atme" style="display:none"><span></span>，<a href="https://itd.qimingdao.com/core/Mention/index">查看消息</a></li>
                    <li rel="unread_notify" style="display:none"><span></span>，<a href="https://itd.qimingdao.com/core/Message/notify">查看消息</a></li>
                </ul>
            <a href="javascript:void(0)" onclick="core.dropnotify.closeParentObj()" class="ico-close"></a>
            </div>
            <script type="text/javascript">
            core.plugFunc('dropnotify',function(){
                setTimeout(function(){
                    core.dropnotify.init('message_list_container','message_container');
                },251);
            });
            var setLang = function(lang){
                $.post(U('core/Do/setLang'),{ lang:lang },function(){
                    location.href = location.href;
                });
            }
            </script>
                    <!-- 已登录 -->
            </div>
</div>
<!--=> hd End <=-->
