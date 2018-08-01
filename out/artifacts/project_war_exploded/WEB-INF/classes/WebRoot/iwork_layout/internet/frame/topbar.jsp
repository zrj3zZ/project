<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div id="qm-header">
    <div id="header">
                  <div class="qg-logo"><a href="mainAction.action"><img src="iwork_layout/skins2014/image/logo1.png"></a></div>
                        <div class="qg-version dorp-left"> 
                    <span class="txt">BPM</span>
                    <!-- <div class="qg-topmenu qg-topmenu-space" model-node="drop_menu_list">
                        <dl class="my-ch">
                            <dd class="current">
                                <i class="qg-ico18 ico-choosed"></i>
                                    <a href="mainAction">BPM</a> 
                                    <div class="qg-tips" style="display:none">
                                        <div class="tips-up"><span><em>◆</em></span></div>
                                        <div class="tips-btm">默认空间</div>
                                    </div>
                            </dd>                                                        <dd class="q-func"><a href="javascript:arrcoll()" >添加模块到“更多”</a></dd>
                                                    </dl>
                    </div> -->
            </div>        <!-- 已登录 --> 
        <!-- 头部导航END  -->
        <div class="qg-person">
            <ul>  
                <li class="dorp-right"><a href="mainAction.action" class="q-tab">收藏夹<i class="ico-arrow-down"></i></a>
                    <div  class="qg-topmenu qg-topmenu-profile" model-node="drop_menu_list">
                        <dl id="favMenuList">
                            
                           </dl>
                           <div class="q-func"><a onclick="ui.sendmessage()" href="javascript:void(0)" event-node="postMsg"><img src="iwork_img/goto.gif">&nbsp;管理收藏夹</a></div>
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
                        <div class="q-func"><a href="javascript:exitSystem()"  id="loginOut" >退出</a></div>
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
                   <s:property value="topMenuHtml" escapeHtml="false"/> 
                    <li class="dorp-right"><a href="javascript:;" class="q-nav">更多<i class="ico-arrow-down"></i></a>
                    <div id="downMemulist" class="qg-topmenu qg-topmenu-nav" model-node="drop_menu_list">
                        <dl>
                                               
                        </dl>
                    </div>
                </li>
                                </ul>
            </div>

          
            </div>
</div>
<!--=> hd End <=-->
