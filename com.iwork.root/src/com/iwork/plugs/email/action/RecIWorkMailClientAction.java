package com.iwork.plugs.email.action;

import java.util.ArrayList;
import java.util.List;

import com.iwork.app.message.sysmsg.util.PageBean;
import com.iwork.core.mq.util.MessageQueueUtil;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.core.upload.service.FileUploadService;
import com.iwork.core.util.ResponseUtil;
import com.iwork.plugs.email.constant.MailTypeConst;
import com.iwork.plugs.email.model.MailModel;
import com.iwork.plugs.email.model.MailOwnerModel;
import com.iwork.plugs.email.model.MailTaskModel;
import com.iwork.plugs.email.service.RecIWorkMailClientService;
import com.iwork.plugs.email.util.EmailCommonTools;
import com.opensymphony.xwork2.ActionSupport;

public class RecIWorkMailClientAction extends ActionSupport{

    private static final long serialVersionUID = 1L;

    private RecIWorkMailClientService reciWorkMailClientService;
    private FileUploadService emailUploadifyService;
    private List<MailTaskModel> receivelist;
    //private List<MailModel> list;
    private List<MailOwnerModel> ownerlist;



    private List<MailModel> list;
    private int totalNum; // 总页数
    private int pageNumber; // 当前页数
    private int startRow; //
    private int currLogPage; //
    private int totalLogPage; //
    private int pageSize = 10; //
    private PageBean logListBean; //
    private Long boxType;
    private MailModel model;
    private MailTaskModel taskModel;
    private Long replyId;
    private Long taskid;
    private long ids;
    private long showRecId;
    private String Ownerid;



    private String id;//删除邮件获取的id
    private String html;
    private String attachHtml;
    private Long mailId;//转发传递的id
    private int total;
    private int type;
    private String to;//收件人
    private String cc;//抄送人
    private String tabTittle;//获取页签
    private String cn_to;
    private String cn_cc;
    /**
     *  邮箱主页面
     * @return
     */
    public String index(){
        return SUCCESS;
    }
    /**
     * 获取
     * @return
     */
    public int showRow(){
        String username = UserContextUtil.getInstance().getCurrentUserId();
        return	reciWorkMailClientService.getMsgLogRow(username);

    }


    /**
     * 获取收件邮件列表
     * @return
     */
    public String receiveList(){
        String userId = UserContextUtil.getInstance().getCurrentUserId();
//		HashMap hash = new HashMap();
//		receivelist =  iWorkMailClientService.getReceiveBoxInfo(username);
//		total
//
////		String userId = UserContextUtil.getInstance().getCurrentUserId();
////		int totalRows = iWorkMailClientService.getMsgLogRow(boxType,userId);
////		totalLogPage = PageBean.countTotalPage(pageSize, totalRows);
////		if (currLogPage == 0) {
////			currLogPage = 1;
////		}
////		final int offset = PageBean.countOffset(pageSize, currLogPage);    //当前页开始记录
////        int length = pageSize;    //每页记录数
////        final int currentPage = PageBean.countCurrentPage(currLogPage);
////        if (totalRows <= length) {
////        	length = totalRows;
////        }
////        receivelist = iWorkMailClientService.getReceiveList(userId,pageSize,currLogPage);
//		return SUCCESS;
        total = reciWorkMailClientService.countSendEail(userId);
        int startRow = 0;
        if (pageNumber == 0) {
            pageNumber = 1;
        }
        if (pageSize == 0) {
            pageSize = 10;
        }
        if (pageNumber > 1) {
            startRow = (pageNumber - 1) * pageSize;
        }
        html = reciWorkMailClientService.getReceiveListHtml(userId, pageSize,startRow);
        //	html = reciWorkMailClientService.getReceiveListEmails(userId, pageSize,
        return SUCCESS;
    }




    /**
     * 写邮件
     * @return
     */
    public String newadd(){
        model = new MailModel();
        model.set_mailType(MailTypeConst.MAIL_ACTION_TYPE_ADD);
        return SUCCESS;
    }

    /**
     * 发送邮件
     */
    public String sendEmail(){
        String msg = reciWorkMailClientService.sendEmail(model,taskid);
        return SUCCESS;
    }

    /**
     * 获取收件箱内记录的基本信息
     */
    public String showEmailDetail(){
        if(ids!=0&&taskid!=0){
            list = reciWorkMailClientService.findReceiveEmailById(ids);
        }
        return SUCCESS;
    }

    /**
     * 删除邮件
     *
     */
    public void deleteReEmail(){
        String msg = "false";
        if(id!=null||"".equals(id)){
            reciWorkMailClientService.deleteReEmail(id);
            msg = "succ";

        }
        ResponseUtil.write(msg);
    }

    /**
     * 彻底删除邮件
     *
     */
    public void deleteAllReEmail(){
        String msg = "false";
        if(id!=null||"".equals(id)){
            reciWorkMailClientService.deleteAllReEmail(id);
            msg = "succ";

        }
        ResponseUtil.write(msg);
    }
    /**
     * 邮件标星
     *
     */
    public void setRecEmailStar(){
        String msg = "false";
        if(id!=null||"".equals(id)){
            reciWorkMailClientService.setRecStar(id);
            msg = "succ";

        }
        ResponseUtil.write(msg);
    }
    /**
     * 取消邮件标星
     *
     */
    public void cancelSetRecEmailStar(){
        String msg = "false";
        if(id!=null||"".equals(id)){
            String str[] = id.split(",");
            reciWorkMailClientService.cancelSetRecStar(id);

            msg = "succ";
        }

        ResponseUtil.write(msg);
    }
    public void ToAndCc(){
        String msg="";
        msg = reciWorkMailClientService.ToAndCc(to, cc);
        ResponseUtil.write(msg);
    }
    /**
     * 获取写信的信息并保存至邮件信息表中设置IS_ARCHIVES =0
     *
     * */
    public String saveWriteEmail(){
        String msg="";
        if(tabTittle!=null&&!"".equals(tabTittle)){
            String[] tittle=tabTittle.split(",");
            tabTittle=tittle[0];
        }

        if(model!=null && Ownerid!=null && !"".equals(Ownerid)){
            msg = reciWorkMailClientService.saveToUnsend(model,Ownerid);
        }else {
            msg = reciWorkMailClientService.saveToUnsend(model,null);
        }
        if(msg.equals("OK")){
            return SUCCESS;
        }else{
            MessageQueueUtil.getInstance().putAlertMsg("请填写有效姓名");
            return "addmail";
        }
    }
    /**
     * 跳转至转发邮件页面
     *
     * */
    public String forwardEmail(){

        // HashMap hash = new HashMap();
        List<MailModel> tempList = new ArrayList();
        if (mailId != null) {
            MailModel mm =  reciWorkMailClientService.getiWorkMailDAO().getMailModelById(mailId);
            if(mm!=null){
                model = new MailModel();
                model.set_title("转发："+mm.get_title());
                String content = reciWorkMailClientService.getContentHtmlHead(MailTypeConst.MAIL_ACTION_TYPE_FORWARD, mm);
                model.set_content(content);
                model.set_relatedId(mailId);
                model.set_mailType(MailTypeConst.MAIL_ACTION_TYPE_FORWARD);
                model.set_attachments(mm.get_attachments());
                attachHtml = reciWorkMailClientService.getAttachHtml(mm);

            }
        }
        return SUCCESS;
    }

    /**
     * 转发邮件
     * */
    public String resendEmail(){

        if(taskModel!=null){
            String msg = reciWorkMailClientService.resendEmail(taskModel,taskid);
            return SUCCESS;
        }
        return ERROR;
    }

    /**
     * 回复邮件
     *
     *
     * */
    public String replyEmail(){
        StringBuffer fieldHtml = new StringBuffer();
        List<MailModel> tempList = new ArrayList();
        if (replyId != null) {
            model =  reciWorkMailClientService.getiWorkMailDAO().getMailModelById(replyId);//用taskid查找mailId
            if(model!=null){
                model.set_title("回复："+model.get_title());
                String createUser  = model.get_createUser();
                String content = reciWorkMailClientService.getContentHtmlHead(MailTypeConst.MAIL_ACTION_TYPE_REPLY, model);
                model.set_content(content);
                model.set_createUser(null);
                model.set_cc(null);

                if(model.get_cc()==null){
                    fieldHtml.append("<script>").append("$(function(){ $('#_cs').hide(); $('#addcs').show();});").append("</script>");
                }
                attachHtml=fieldHtml.toString();
                cn_to = EmailCommonTools.getInstance().buildUserList(createUser);
                model.set_to(UserContextUtil.getInstance().getFullUserAddress(createUser));
                model.set_attachments("");
                model.set_relatedId(mailId);
                model.set_mailType(MailTypeConst.MAIL_ACTION_TYPE_REPLY);
            }
        }
        return SUCCESS;
    }
    /**
     * 回复全部邮件
     *
     * */
    public String replyEmailAll() {
        StringBuffer fieldHtml = new StringBuffer();
        List<MailModel> tempList = new ArrayList();
        List<String> list = new ArrayList<String>();
        if (replyId != null) {
            model =  reciWorkMailClientService.getiWorkMailDAO().getMailModelById(replyId);//用taskid查找mailId
            //获取当前用户
            String currentUserName = UserContextUtil.getInstance().getCurrentUserFullName();
            if(model!=null){
                model.set_title("回复："+model.get_title());
                String content = reciWorkMailClientService.getContentHtmlHead(MailTypeConst.MAIL_ACTION_TYPE_REPLYALL, model);
                model.set_content(content);
                String createUser  = model.get_createUser();
                model.set_createUser(null);
                //将收件人、发件人、抄送人拼成串
                String ccUser = model.get_cc()==null?"":model.get_cc().toString();
                String toUser = model.get_to()==null?"":model.get_to().toString();
                StringBuffer sb = new StringBuffer();
                if(!"".equals(ccUser)){
                    sb.append(ccUser).append(",");
                }
                if(!"".equals(toUser)){
                    toUser = model.get_mailFrom()+","+toUser;
                    StringBuffer userSb = new StringBuffer();
                    String[] toUsers = toUser.split(",");
                    for(String user:toUsers){
                        if(user != null && !"".equals(user)){
                            if(user.equals(currentUserName)){
                                continue;
                            }else{
                                if(userSb == null || "".equals(userSb) || userSb.length()==0){
                                    userSb.append(user);
                                }else{
                                    userSb.append(",").append(user);
                                }
                            }
                        }
                    }
                    toUser = userSb.toString();
                    sb.append(toUser).append(",");
                }
                sb.append(UserContextUtil.getInstance().getFullUserAddress(createUser));
                String replyUserNameString = sb.toString();

                //添加收件人
                model.set_to(toUser);
                model.set_cc(ccUser);
                cn_to = EmailCommonTools.getInstance().buildUserList(toUser);
                cn_cc = EmailCommonTools.getInstance().buildUserList(ccUser);

                model.set_attachments("");
                model.set_relatedId(mailId);
                model.set_mailType(MailTypeConst.MAIL_ACTION_TYPE_REPLY);
                if(model.get_cc()==null){
                    fieldHtml.append("<script>").append("$(function(){ $('#_cs').hide(); $('#addcs').show();});").append("</script>");

                }else{
                    fieldHtml.append("<script>").append("$(function(){  $('#delcs').show();});").append("</script>");
                }
                attachHtml=fieldHtml.toString();
            }
        }
        return SUCCESS;
    }

    /**
     * 分页显示
     *
     * */
    public String receiveListEmails() {
        String userId = UserContextUtil.getInstance().getCurrentUserId();
        // 获取邮件的总记录数
        total = reciWorkMailClientService.countSendEail(userId);
        int startRow = 0;
        if (pageNumber == 0) {
            pageNumber = 1;
        }
        if (pageSize == 0) {
            pageSize = 10;
        }
        if (pageNumber > 1) {
            startRow = (pageNumber - 1) * pageSize;
        }
        html = reciWorkMailClientService.getReceiveListHtml(userId, pageSize,startRow);
        //receivelist = reciWorkMailClientService.getReceiveListEmails(userId, pageSize,
        //		startRow);
        return SUCCESS;
    }

    public List<MailModel> getList() {
        return list;
    }


    public void setList(List<MailModel> list) {
        this.list = list;
    }


    public int getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(int totalNum) {
        this.totalNum = totalNum;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public int getStartRow() {
        return startRow;
    }

    public void setStartRow(int startRow) {
        this.startRow = startRow;
    }

    public int getCurrLogPage() {
        return currLogPage;
    }

    public void setCurrLogPage(int currLogPage) {
        this.currLogPage = currLogPage;
    }

    public int getTotalLogPage() {
        return totalLogPage;
    }

    public void setTotalLogPage(int totalLogPage) {
        this.totalLogPage = totalLogPage;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public PageBean getLogListBean() {
        return logListBean;
    }

    public void setLogListBean(PageBean logListBean) {
        this.logListBean = logListBean;
    }

    public Long getBoxType() {
        return boxType;
    }

    public void setBoxType(Long boxType) {
        this.boxType = boxType;
    }


    public List<MailTaskModel> getReceivelist() {
        return receivelist;
    }

    public void setReceivelist(List<MailTaskModel> receivelist) {
        this.receivelist = receivelist;
    }

    public MailModel getModel() {
        return model;
    }

    public void setModel(MailModel model) {
        this.model = model;
    }

    public List<MailOwnerModel> getOwnerlist() {
        return ownerlist;
    }


    public long getIds() {
        return ids;
    }


    public void setIds(long ids) {
        this.ids = ids;
    }
    public RecIWorkMailClientService getreciWorkMailClientService() {
        return reciWorkMailClientService;
    }


    public void setreciWorkMailClientService(
            RecIWorkMailClientService reciWorkMailClientService) {
        this.reciWorkMailClientService = reciWorkMailClientService;
    }

    public int getTotal() {
        return total;
    }


    public void setTotal(int total) {
        this.total = total;
    }


    public Long getTaskid() {
        return taskid;
    }


    public void setTaskid(Long taskid) {
        this.taskid = taskid;
    }


    public Long getReplyId() {
        return replyId;
    }


    public void setReplyId(Long replyId) {
        this.replyId = replyId;
    }


    public MailTaskModel getTaskModel() {
        return taskModel;
    }


    public void setTaskModel(MailTaskModel taskModel) {
        this.taskModel = taskModel;
    }
    public long getShowRecId() {
        return showRecId;
    }
    public void setShowRecId(long showRecId) {
        this.showRecId = showRecId;
    }





    public Long getMailId() {
        return mailId;
    }
    public void setMailId(Long mailId) {
        this.mailId = mailId;
    }
    public String getId() {
        return id;
    }


    public void setId(String id) {
        this.id = id;
    }

    public RecIWorkMailClientService getReciWorkMailClientService() {
        return reciWorkMailClientService;
    }

    public void setReciWorkMailClientService(RecIWorkMailClientService reciWorkMailClientService) {
        this.reciWorkMailClientService = reciWorkMailClientService;
    }
    public int getType() {
        return type;
    }
    public void setType(int type) {
        this.type = type;
    }
    public void setEmailUploadifyService(FileUploadService emailUploadifyService) {
        this.emailUploadifyService = emailUploadifyService;
    }
    public String getAttachHtml() {
        return attachHtml;
    }
    public void setAttachHtml(String attachHtml) {
        this.attachHtml = attachHtml;
    }
    public String getOwnerid() {
        return Ownerid;
    }
    public void setOwnerid(String ownerid) {
        Ownerid = ownerid;
    }
    public String getTo() {
        return to;
    }
    public void setTo(String to) {
        this.to = to;
    }
    public String getCc() {
        return cc;
    }
    public void setCc(String cc) {
        this.cc = cc;
    }
    public String getTabTittle() {
        return tabTittle;
    }
    public void setTabTittle(String tabTittle) {
        this.tabTittle = tabTittle;
    }
    public String getHtml() {
        return html;
    }
    public void setHtml(String html) {
        this.html = html;
    }
    public String getCn_to() {
        return cn_to;
    }
    public void setCn_to(String cnTo) {
        cn_to = cnTo;
    }
    public String getCn_cc() {
        return cn_cc;
    }
    public void setCn_cc(String cnCc) {
        cn_cc = cnCc;
    }

}
