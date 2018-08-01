<!DOCTYPE html>  
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html xmlns="http://www.w3.org/1999/xhtml" lang="zh">  
<head>  
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<title><s:property value="systemTitle"/></title>
	<s:include value="link.jsp"> </s:include>
</head>
<body  >
<s:include value="topbar.jsp">
   </s:include> 
<div id="page-wrap">
        <div id="feedback" class="feedback-fixed">
            <a href="javascript:void(0)"></a>
        </div>
        <div id="main-wrap">
            <div id="st-index-grid" class="st-grid">
            <!-- 左侧菜单  -->
				<s:include value="leftmenu.jsp">
				   </s:include>
                <div id="col8" class="st-section">
                    <!--右边-->
                    <div id="col3" class="st-index-right" >
	<div class="right-wrap">
		<div model-node = 'diy_widget' model-args='diyId=1'>
		<div class="ui-state-disabled mod-person-num border" >
			<a href="javascript:redirectUrl('process_desk_index.action')">
				<h4 node-event>待办事宜</h4>
				<strong><span id="todoNum">0</span></strong>
			</a>
			<a href="https://itd.qimingdao.com/core/Profile/follower?uid=3523">
				<h4>私信</h4>
				<strong event-node ="follower_count" event-args ="uid=3523">0</strong>
			</a>
			<a href="sysm">
				<h4>系统消息</h4><strong event-node ="feed_count" event-args ="uid=3523">2</strong>
			</a>
			<a href="https://itd.qimingdao.com/core/Collection/index?uid=3523">
				<h4>任务</h4><strong event-node ="favorite_count" event-args ="uid=3523">
					0</strong>
			</a>
		</div>    数值
    <div class="ui-state-disabled m-weight border" >
    <div class="setup hover">
    <a href='javascript:;' onclick="addCountdown()" class="aicon-set font14"></a>
    <a href="javascript:void(0)" class="ico-circle-arrow-down" event-node='widget_toggle'></a>
    <a href="javascript:void(0)" class="ico-close" event-node='widget_close'></a>
    </div>
    <div class="hd">
        <h3>产品发布</h3>
            </div>
<div model-node="widget_child" class="m-w-countdown" >
    还有<span>0</span><span>0</span><span>3</span><span>0</span>天</div>
</div>
 <div class="ui-state-disabled m-weight border" model-node='widget_box'  >
    <div style="height:200px"></div>
</div>    

   <div class="ui-state-disabled m-weight border" model-node='widget_box' model-args = 'widget_user_id=1&widget_name=IofficePunch&widget_desc=IOFFICE_PUBLIC_APPNAME&app_name=ioffice&diyId=1'>
    <div class="setup hover">
        <a event-node="widget_toggle" class="ico-circle-arrow-down" href="javascript:void(0)"></a>
        <a event-node="widget_close" class="ico-close" href="javascript:void(0)"></a>
    </div>
<div class="punch-card"  model-node="widget_child" >
            <h3 class="hd">我的任务<span class="task-count"><em class="over" id="myitask_over">0</em> / <em class="todo" id="myitask_unfinished">1</em></span></h3>
</div>
</div>
   <div class="ui-state-disabled m-weight border" model-node='widget_box' model-args = 'widget_user_id=1&widget_name=MyTask&widget_desc=ITASK_MY&app_name=task&diyId=1' >
    <div class="setup hover">
        <a href="javascript:void(0)" class="ico-circle-arrow-down" event-node='widget_toggle'></a>
        <a href="javascript:void(0)" class="ico-close" event-node='widget_close'></a>
    </div>
    <h3 class="hd">我的任务<span class="task-count"><em class="over" id="myitask_over">0</em> / <em class="todo" id="myitask_unfinished">1</em></span></h3>
    <div model-node="widget_child" class="m-w-task" >
                      <input type='text' name='task' id='task' class="q-txt left" placeholder='请输入任务名称'>
              <a href="javascript:;" class="btn" id='sub' ><span>添加</span></a>    </div>
    <div id="addTaskContent" style="display:none;">
        <div class="pop-create-group">
            <dl>
            <dt>
                名称：
            <input event-node="taskTitle" type="text" class="q-txt" value="" id="task_title" />
            </dt>
           <dd>
                <a href="javascript:void(0);" class="btn btn-green mr10" onclick="myTask.doAdd()"><span>确认</span></a>
                <a href="javascript:void(0);" onclick="ui.box.close();" class="btn"><span>取消</span></a>
            </dd>
        </dl>
        </div>
    </div>

    <input type="hidden" value="TASK_UNDO,全部" id="task_today_key" />
    <input type="hidden" value="#639CF5,#E04C3E" id="task_today_color" />

</div>
  <div class="ui-state-disabled m-weight border" model-node='widget_box' model-args = 'widget_user_id=1&widget_name=BlogMy&widget_desc=BLOG_MY_BLOG&app_name=blog&diyId=1'>
    <div class="setup hover">
        <a href=" https://itd.qimingdao.com/blog/Personal/index " target='_blank' class="qg-ico18 ico-mor"></a>
        <a event-node="widget_toggle" class="ico-circle-arrow-down" href="javascript:void(0)"></a>
        <a event-node="widget_close" class="ico-close" href="javascript:void(0)"></a>
    </div>
    <div class="hd"><h3>我的博客</h3></div>
    <div class="m-w-blog" model-node="widget_child" >
        <dl>
                </dl>
    </div>
</div>    <div class="ui-state-disabled m-weight border" model-node='widget_box' model-args = 'widget_user_id=1&widget_name=ProjectMy&widget_desc=PROJECT_MY_PROJECT&app_name=project&diyId=1'>
    <div class="setup hover">
        <a target="_blank" href="https://itd.qimingdao.com/project/Index/index" class="qg-ico18 ico-mor"></a>
        <a event-node="widget_toggle" class="ico-circle-arrow-down" href="javascript:void(0)"></a>
        <a event-node="widget_close" class="ico-close" href="javascript:void(0)"></a>
    </div>
    <div class="hd"><h3>我的项目</h3></div>
    <div class="m-w-project" model-node="widget_child" >
            </div>
</div>    <div class="ui-state-disabled m-weight border" model-node='widget_box' model-args = 'widget_user_id=1&widget_name=Meeting&widget_desc=MEETING_PUBLIC_APPNAME&app_name=meeting&diyId=1'>
    <div class="setup hover">
        <a target="_blank" href="https://itd.qimingdao.com/meeting/Index/index" class="qg-ico18 ico-mor"></a>
        <a event-node="widget_toggle" class="ico-circle-arrow-down" href="javascript:void(0)"></a>
        <a event-node="widget_close" class="ico-close" href="javascript:void(0)"></a>
    </div>
    <div class="hd"><h3>我的会议</h3><span></span></div>
    <div class="m-w-meeting" model-node="widget_child" >
            </div>
</div>
<style>
.ui-state-highlight \{background-color: red\}
</style>
<div class="weight-add m-weight" >
    <a href="javascript:;" event-node='widget_add' event-args = 'widget_user_id=1&diyId=1'>
    <i class="ico-add-green"></i>添加</a>
</div>
</div>        	</div>
</div>
                    <div id="col5" class="st-index-main">
                    <div class="border boxShadow extend-foot minh">
                        <div class="qg-poster div-focus">

                               <div class="qg-poster-tab">
                                    <div id='diffcss'>
                                        <ul>
                                            <li class="current" rel="kind-weibo"><span><i class="aicon-blog-post"></i><a href="javascript:;">记任务</a></span></li>
                                                                                        <li rel="kind-task"><span><i class="aicon-task"></i><a href="javascript:;">写日程</a></span></li>
                                                                                        <li rel="kind-blog"><span><i class="aicon-bubbles"></i><a href="javascript:;">提问题</a></span></li>
                                                                                        <li rel="kind-file"><span><i class="aicon-bubbles"></i><a href="javascript:;">提问题</a></span></li>
                                        </ul>
                                    </div>
                                </div>
                                <div rel="kind-weibo" class="qg-poster-wb block" style="display:block">
                                    <div class="pwb-hd"><em class="arrline">◆</em><span class="downline">◆</span></div>
<div class="pwb-bd" model-node="send_feed" model-args='post_event=post_feed'>
    <div class="input">
        <div class="input_before">
            <div model-node="nums_left" class="number"><span>140</span></div>
            <div model-node='initHtml' style='display:none'></div>
            <textarea class="textinput" event-node='mini_editor_textarea' rel="fangan" placeholder="记录工作的想法" event-args='has_out_user=0'></textarea>
            <div model-node="post_ok" style="display:none;text-align:center;position:absolute;left:0;top:40%;width:100%">
                <i class="ico-ok"></i>保存成功</div>
            </div>
        <div class="poster-addons">
            <div class="kind">
                <a class="btn btn-green right" href="javascript:;" title="按Ctrl+Enter可以快速提交 "><span>保存</span></a>
             
            </div>
        </div>
    </div>
</div>
</div>
                        <div rel="kind-task" class="qg-poster-t block" style="display:none">
                                    <div class="pwb-hd"><em class="arrline">◆</em><span class="downline">◆</span></div>
                                    <div class="m-p-tadd" model-node='post_task'>
    <!--下拉列表-->
	<dl>
    	<dd class="hd">
            <input event-node="itask_title" id="itask_title" event-node="itask_title" type="text left" class="q-txt left" style="width:429px" placeholder="点击这里添加一条任务...">
            <a class="btn btn-green" event-node="add_itask_title" ><span>添加</span></a>
        </dd>
    	<dd>
            <div class="qg-userlist">
                 <div class="choose-user" model-node='search_link'
model-args=''>
<ul model-node="search_list" class="user-list" style='display:none'>
</ul>
<input type="hidden" id="search_ids_joiner_uid" name="joiner_uid" value="" >
<input event-node="search_link" placeholder="责任人" type="text" class="q-txt" autocomplete="off" style="width:224px;" ></div>
<script type="text/javascript">
    $('.choose-user').bind('click',function(){
        $(this).find('.q-txt').focus();
    });
</script>            </div>
            <div class="deadline">
                             <div class="dateLine" model-node='date_select' model-args='inputId=isTime&dtype=1&timeIputId=&callback='>
    <div class="dateLine-inner" rel='manage'>
    <i class="aicon-time font16"></i>
    <input name="deadline" event-node='date_input' type="text" class="start s-txt q-txt  rcalendar_input left" id="isTime" value='2014-04-24 今天' readonly="readonly" placeholder="截止时间	 " style='width:150px' />
    </div>        <div style='display: none;left:0' model-node='date_show' class="time-picker drop_clayer">
        <div class="qm-day-inner">
        <div class="days">
        <span event-node='date_click'  event-args='date=2014-04-24&desc=今天'>今天</span>
        <span event-node='date_click'  event-args='date=2014-04-25&desc=明天'>明天</span>
        <span event-node='date_click'  event-args='date=2014-04-26&desc=后天'>后天</span>
        <span event-node='date_click'  event-args='date=2014-04-27&desc=星期日'>星期日</span>
        <span event-node='date_click'  event-args='date=2014-04-28&desc=星期一'>星期一</span>
        <span event-node='date_click'  event-args='date=2014-04-29&desc=星期二'>星期二</span>
        <span event-node='date_click'  event-args='date=2014-04-30&desc=星期三'>星期三</span>
        </div>
        <span event-node='date_click'  event-args='date=other&desc=' class="rcalendar_input other-time">其他日期&raquo;</span>
        </div>
    </div>
    </div>
          </div>
        </dd>
    	<dd>
            <textarea event-node="taskDesc" id="contents" placeholder="详细描述" name="content" cols="" rows="" class="q-textarea" style="width:490px"></textarea>
        </dd>
    </dl>
</div>
                               </div>
                                                                <!--发博客-->
                                                                <div rel="kind-blog" class="qg-poster-b block" style="display:none">
                                    <div class="pwb-hd"><em class="arrline">◆</em><span class="downline">◆</span></div>
                                    <form method="post" id="addBlogForm" action="https://itd.qimingdao.com/blog/Do/post" model-node="addBlogForm">
<div class="mod-blog-add">
   <dl>
           <dt>
                            <div class="drop-select left mr5" id="drop_select_category_id" event-node="blog_add_category_id">
                <a href="javascript:;" class="name" id="blog_select_name">通用<i class="ico-arrow-down right"></i></a>
                <ul class="select-list drop_clayer" id="blog_select_list" style="display:none">
                    <li  id='last_cate' ><a href="javascript:;" va="1">通用</a></li>                    <li><a href="javascript:;" va="0" >+添加分类</a></li>
                </ul>
                <input type="hidden" value="1" name="categoryId" event-node="categoryId" id="categoryId"/>
              </div>
              <input name="title" type="text" class="q-txt left" style="width:363px" value=""  placeholder="输入博客标题">
            </dt>
           <dd class="editor"><script type="text/javascript" src="https://static.qimingdao.com/apps/core/static/js/editor/kindeditor-4.1.1/plugins/code/prettify.js?v="></script>
<div class="editable-outer">
    <textarea  class="in_put" name="blog_content" id="blog_content" style="width:498px;_width:498px;height:250px" ></textarea>
    <div class="ke-action-wrap">
        <div class="ke-tips">注：支持Firefox浏览器下直接粘贴图片</div>
        <div class="ke-empty-record"><a href="javascript:;" onclick="Editor_blog_content.clean()" class="aicon-delete font16"></a></div>
        <div id="editor_save_info_blog_content" class="ke-draft-controls left"></div>
        <div class="qg-tips" style='display:none' >
            <div class="tips-up"><span><em>◆</em></span></div><div class="tips-btm">清空内容</div>
        </div>
    </div>

</div>
</dd>

           <input type='hidden' name="is_original" value='1'/>
           <dd class="actionBtn">
            <a href="javascript:void(0)" class="btn btn-green " event-args="ajax=true" event-node="submit_blog"><span>发表博客</span></a>
            <a event-node="submit_btn" event-args="ajax=true" style="display:none"></a>
          </dd>
          <input type="hidden" name="blog_id" value=""/>
          <input type="hidden" name="edit" value=""/>
          <input type="hidden" name="uid"  value="" />
   </dl>
</div>
</form>
                               </div>
                                <!--传文件-->
                                <div rel="kind-file" class="qg-poster-f block" style="display:none">
                                    <div class="pwb-hd"><em class="arrline">◆</em><span class="downline">◆</span></div>
                                    <!-- 发布微博/微博 -->
									<div class="mod-file-add pwb-bd" model-node="send_feed" model-args='post_event=post_feed'>
									    <dl>
									        <dt><div class="btn-upload">
									        <i class="aicon-plus"></i>
									            <FORM style='display:inline;padding:0;margin:0;border:0' >
									                    <input type="file" name="attach" inputname='attach' urlquery='attach_type=feed_file' event-node="add_file"  onchange="core.plugInit('uploadFile',this,'','all','','',$('#parentFile'));" id='attach'>
									                    </FORM>
									                </div>
									        <span>请选择您要上传的文件</span></dt>
									        <dd id='parentFile'></dd>
									        <dd class="action">
									    <div class="input">
									        <div class="input_before">
									            <div model-node="nums_left" class="number"><span>140</span></div>
									            <div model-node='initHtml'></div>
									            <textarea class="textinput" event-node='mini_editor_textarea'  rel="fangan" placeholder="记录工作的想法" event-args='has_out_user=0'></textarea>
									            <div model-node="post_ok" style="display:none;text-align:center;position:absolute;left:0;top:40%;width:100%">
									                <i class="ico-ok"></i>分享成功</div>
									        </div>
									        <div class="poster-addons">
									            <div class="kind">
									                <a class="btn btn-gray right" event-node='post_feed' event-args='type=post&app_name=core&topic_id=&add=&ext_id=&model_name=feed' href="javascript:;"><span>发布</span></a>
									                <div class="acts">
									                    <a event-node="insert_face" class="face-block" href="javascript:;"><i class="aicon-face"></i></a><a event-node="insert_at" class="face-block" href="javascript:;"><i class="aicon-at"></i></a><a event-node="insert_topic" class="face-block" href="javascript:;"><i class="aicon-topic"></i></a>                </div>
									            </div>
									        </div>
									    </div>
									    </dd>
									    </dl>
									</div> 
                                </div>
                        </div>
                    <!--feednav-->
                    <div class="feed-nav mt15">
                    <!--tab menu-->
                    <div class="tab-menu tab-animate">
                        <div class="feed-group">
                                                <i class="ico-circle-arrow-down" event-node='feed_tab_btn' title="展开"></i>
                                                </div>
                         <ul id="column_tab">
                         
                          </ul>
                        <div class="tab-animate-block"></div>
                    </div>
                    </div>
                    <!--资讯列表-->
                    <div id="feed-lists"  style="padding-left:15px;padding-right:10px;">
                   		
					</div>
					</div>
<!--=> ft <=-->
<div id="footer">
    <div class="client">
        <ul>
            <li><a href=""><i class="aicon-android"></i>Android</a></li>
            <li><a href=""><i class="aicon-ios"></i>iPhone</a></li>
              </ul>
    </div>
    <div class="copyright">
        <p>Copyright(C) 2014 BPM</p>
    </div>
</div>
<!--=> ft End <=-->
</body>
</html>