package com.iwork.app.persion.action;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.struts2.ServletActionContext;

import com.ibpmsoft.project.zqb.util.ConfigUtil;
import com.iwork.app.conf.SystemConfig;
import com.iwork.app.persion.constant.SysPersionLayoutConstant;
import com.iwork.app.persion.constant.SysPersionOtherParamConstant;
import com.iwork.app.persion.constant.SysPersionProcessAndReportCenterConstant;
import com.iwork.app.persion.constant.SysPersionRemindConstant;
import com.iwork.app.persion.model.MutilRoleModel;
import com.iwork.app.persion.model.SysPersonConfig;
import com.iwork.app.persion.service.SysPersionService;
import com.iwork.commons.util.ShaSaltUtil;
import com.iwork.commons.util.UtilDate;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.dao.OrgUserDAO;
import com.iwork.core.organization.model.OrgUser;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.core.util.MD5;
import com.iwork.core.util.ResponseUtil;
import com.iwork.process.entrust.model.ProcessEntrustPerson;
import com.iwork.process.entrust.service.ProcessEntrustPersonService;
import com.opensymphony.xwork2.ActionSupport;

public class SysPersionAction extends ActionSupport {

	private static final long serialVersionUID = 1L;
	private SysPersionService sysPersionService ;
	private ProcessEntrustPersonService processEntrustPersonService;
	private OrgUser userModel;
	private String userid;
	private String personToolbar;
	private String txtOldPwd;
	private String txtNewPwd;
	private String menuLayoutType;//页面菜单布局
	private String menuLayoutUpdateFlag;//页面菜单布局成功标识
	private OrgUserDAO orgUserDAO;
	private String remindType;//是否邮件提醒
	private boolean isMailRemind;//是否邮件提醒
	private boolean isIMRemind;//是否IM提醒
	private boolean isSMSRemind;//是否短信提醒
	private boolean isSYSRemind;//是否系统消息提醒
	private String pageLayoutSet;//页面布局设置
	private String formLayoutSet;//表单布局
	private String skinLayoutSet;//皮肤样式布局
	private String listFormLineSet;//列表显示行设置
	private String reportFormLineSet;//报表显示行设置
	private String windowTimeOut;//窗口失效时长
	private String configID_1;//配置信息ID1
	private String configID_2;//配置信息ID2
	private String configID_3;//配置信息ID3
	private String configID_4;//配置信息ID4
	private ProcessEntrustPerson model;//委托流程设置对象
	private String curTime;//服务器当前时间
	private String nonTerminate_entrustNum;//当前非终止状态的个人委托个数
	private String nonTerminate_entrustedNum;//当前非终止状态的个人被委托个数
	private String userImgPath;
	private String isUserImageExists;
	
	private MutilRoleModel mutiModel;
	private List<MutilRoleModel> rolelist;
	private String rolelistHtml;
	private String itemid;
	private String txtConfirmPwd;
	private String persion_process_launch_center_sort;
	private String persion_report_center_sort;
	public static String CN_FILENAME = "/common.properties";// 抓取网站配置文件
	public String getTxtConfirmPwd() {
		return txtConfirmPwd;
	}

	public void setTxtConfirmPwd(String txtConfirmPwd) {
		this.txtConfirmPwd = txtConfirmPwd;
	}

	/**
	 * 加载个人设置主页面
	 * @return
	 * @throws Exception
	 */
	public String getSysPersonIndexPage() throws Exception {
		return SUCCESS;
	}
	
	/**
	 * 
	 * @return
	 */
	public String roleExchange(){
		rolelist = sysPersionService.getRoleList();
		StringBuffer rolelistHtml = new StringBuffer();
		boolean isMutl = false;
		if(rolelist!=null&&rolelist.size()>1){
			isMutl = true;
		}
		if(isMutl){
			rolelistHtml.append("<select class=\"rolelist\" name=\"rolelist\"  id=\"rolelist\">").append("\n");
		}
		 
		for(MutilRoleModel model:rolelist){
			if(model.isMain()){
				mutiModel = model;
				if(isMutl){
					StringBuffer item = new StringBuffer();
					item.append(model.getCompanyname()).append("-").append(model.getDepartmentname()).append("-").append(model.getRoleName()).append("√");
					rolelistHtml.append(" <option value =\"").append(model.getCompanyid()).append("|").append(model.getDepartmentid()).append("|").append(model.getRoleId()).append("\">").append(item).append("</option>").append("\n");
				}else{
					break;
				}
			}else{
				StringBuffer item = new StringBuffer();
				item.append(model.getCompanyname()).append("-").append(model.getDepartmentname()).append("-").append(model.getRoleName());
				rolelistHtml.append(" <option value =\"").append(model.getCompanyid()).append(",").append(model.getDepartmentid()).append(",").append(model.getRoleId()).append("\">").append(item).append("</option>").append("\n");
			}
			
		}
		if(isMutl){
			rolelistHtml.append("</select>").append("\n");
		}
		
		if(!isMutl){
			rolelistHtml.append("<div class=\"tips\"><img src=\"iwork_img/nondynamic.gif\">当前用户为单角色用户，无法切换</div>"); 
		}
		this.rolelistHtml = rolelistHtml.toString();
		return SUCCESS;
	}
	public void doExchange(){
		if(itemid!=null){
			boolean flag = sysPersionService.doMutiRole(itemid);
			if(flag){
				ResponseUtil.write(SUCCESS);
			}else{
				ResponseUtil.write(ERROR);
			}
		}
	}
	
	/**
	 * 加载顶部标题栏 
	 * @return
	 * @throws Exception
	 */
	public String getSysPersonTopPage() throws Exception{
		return SUCCESS;
	}
	/**
	 * 加载左侧标题栏
	 * @return
	 * @throws Exception
	 */
	public String getSysPersonLeftPage() throws Exception {
		return SUCCESS;
	}
	/**
	 * 加载常规信息
	 * @return
	 * @throws Exception
	 */
	public String getMyPersionInfo() throws Exception {
		rolelistHtml="";
		//获得当前用户上下文
		UserContext uc =  UserContextUtil.getInstance().getCurrentUserContext();
		String userid = uc._userModel.getUserid();
		OrgUser model = orgUserDAO.getUserModel(userid);
		this.setUserModel(model);
		//获取用户头像名称
		String imageFileName = userid+".jpg";
		//获取用户头像存放路径
		String path = ServletActionContext.getServletContext().getRealPath(SystemConfig._fileServerConf.getUserPhotoPath()) + "/" + imageFileName;
	    //判断用户头像是否存在
		File userImage = new File(path);
		if(!userImage.exists()){
	    	this.setUserImgPath("iwork_img/default_userImg.jpg");
	    	this.setIsUserImageExists("0");
	    }else{
	    	//如果用户头像存在,将用户头像的路径传到前台
	    	String imgPath = SystemConfig._fileServerConf.getUserPhotoPath()+"/"+imageFileName;
	    	this.setUserImgPath(imgPath);
	    	this.setIsUserImageExists("1");
	    }
		return SUCCESS;
	}
	
	/**
	 * 获得指定用户的馋鬼信息
	 * @return
	 * @throws Exception
	 */
	public String getTargetUserInfo() throws Exception {
		//获得当前用户上下文
		if(userid!=null){
			UserContext uc =  UserContextUtil.getInstance().getUserContext(userid);
			if(uc!=null){
				String userid = uc._userModel.getUserid();
				OrgUser model = orgUserDAO.getUserModel(userid);
				this.setUserModel(model);
				//获取用户头像名称
				String imageFileName = userid+".jpg";
				//获取用户头像存放路径
				String path = ServletActionContext.getServletContext().getRealPath(SystemConfig._fileServerConf.getUserPhotoPath()) + "/" + imageFileName;
				//判断用户头像是否存在
				File userImage = new File(path);
				if(!userImage.exists()){
					this.setUserImgPath("iwork_img/default_userImg.jpg");
					this.setIsUserImageExists("0");
				}else{ 
					//如果用户头像存在,将用户头像的路径传到前台
					String imgPath = SystemConfig._fileServerConf.getUserPhotoPath()+"/"+imageFileName;
					this.setUserImgPath(imgPath);
					this.setIsUserImageExists("1");
				}
				return SUCCESS;
			}
		}
		return ERROR;
		
	}
	/**
	 * 更新个人信息
	 * @return
	 */
	public String updateUserInfo() throws Exception{
		if(this.getUserModel().getMobile()!=null && !"".equals(this.getUserModel().getMobile())){
			 Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0-9]))\\d{8}$");
			 if(!p.matcher(this.getUserModel().getMobile()).matches()){
				 rolelistHtml="请输入正确的手机号！";
				 return SUCCESS;
			 }
		}
		if(this.getUserModel().getEmail()!=null && !"".equals(this.getUserModel().getEmail())){
			String pattern1 = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$"; 
			 Pattern p = Pattern.compile(pattern1);
			 if(!p.matcher(this.getUserModel().getEmail()).matches()){
				 rolelistHtml="请输入正确邮箱格式！";
				 return SUCCESS;
			 }
		}
		//获得当前用户上下文
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		String userid = uc._userModel.getUserid();
		//获得当前用户对象
		OrgUser modelTemp = orgUserDAO.getUserModel(userid);
		//将要修改的信息加载到对象中
		modelTemp.setPostsresponsibility(this.getUserModel().getPostsresponsibility());
		modelTemp.setBossid(this.getUserModel().getBossid());
		modelTemp.setSelfdesc(this.getUserModel().getSelfdesc());
		modelTemp.setWorkStatus(this.getUserModel().getWorkStatus());
		modelTemp.setMobile(this.getUserModel().getMobile());
		modelTemp.setEmail(this.getUserModel().getEmail());
		modelTemp.setOfficetel(this.getUserModel().getOfficetel());
		modelTemp.setOfficefax(this.getUserModel().getOfficefax());
		modelTemp.setJjlinkman(this.getUserModel().getJjlinkman());
		modelTemp.setJjlinktel(this.getUserModel().getJjlinktel());
		modelTemp.setQqmsn(this.getUserModel().getQqmsn());
		modelTemp.setExtend1(this.getUserModel().getExtend1());
		modelTemp.setExtend2(this.getUserModel().getExtend2());
		modelTemp.setExtend3(this.getUserModel().getExtend3());
		sysPersionService.updateUserInfo(modelTemp);
		this.setUserModel(modelTemp);
		rolelistHtml="保存成功！";
		return SUCCESS;
	}
	/**
	 * 修改密码
	 * @return
	 */
	public String pwdUpdateWeb() throws Exception{
		//清空错误提示
		this.clearErrorsAndMessages();
		return SUCCESS;
	}
	
	/**
	 * 执行密码修改
	 * @return
	 */
	public void excutePwdUpdate() throws Exception{
		String info = "";
		MD5 md5 = new MD5();
		boolean flag=false;
		if(this.txtOldPwd!=null && txtNewPwd!=null){
			if(txtConfirmPwd==null || txtConfirmPwd.equals("") || this.txtOldPwd==null || this.txtOldPwd.equals("") ||  txtNewPwd==null || txtNewPwd.equals("")){
				info="您输入的信息不完整，请重输";
			}else if(this.txtOldPwd.equals(txtNewPwd)){
				info = "您输入的密码与修改的密码一致！请重新输入";
			}else if(!txtConfirmPwd.equals(txtNewPwd)){
				info = "两次输入的密码不一样，请重输！";
			}else{
				Map<String, String> config = ConfigUtil.readAllProperties(CN_FILENAME);// 获取连接网址配置
				String zqServer = config.get("zqServer");
				String match="";
				if(zqServer!=null&&zqServer.equals("htzq")){
					match="(?=^.{12,}$)[-\\da-zA-Z`=\\\\\\[\\];',./~!@#$%^&*()_+|{}:<>?]*((\\d+[a-zA-Z]+[-`=\\\\\\[\\];',./~!@#$%^&*()_+|{}:<>?]+)|(\\d+[-`=\\\\\\[\\];',./~!@#$%^&*()_+|{}:<>?]+[a-zA-Z]+)|([a-zA-Z]+\\d+[-`=\\\\\\[\\];',./~!@#$%^&*()_+|{}:<>?]+)|([a-zA-Z]+[-`=\\\\\\[\\];',./~!@#$%^&*()_+|{}:<>?]+\\d+)|([-`=\\\\\\[\\];',./~!@#$%^&*()_+|{}:<>?]+\\d+[a-zA-Z]+)|([-`=\\\\\\[\\];',./~!@#$%^&*()_+|{}:<>?]+[a-zA-Z]+\\d+))[-\\da-zA-Z`=\\\\\\[\\];',./~!@#$%^&*()_+|{}:<>?]*";
				}else{
					if(zqServer.equals("hlzq")){
						match="(?=^.{8,}$)[-\\da-zA-Z]*((\\d+[a-zA-Z]+)|(\\d+[a-zA-Z]+)|([a-zA-Z]+\\d+)|([a-zA-Z]+\\d+)|(\\d+[a-zA-Z]+)|([a-zA-Z]+\\d+))[-\\da-zA-Z]*";

					}else{
						match="(?=^.{8,}$)[-\\da-zA-Z`=\\\\\\[\\];',./~!@#$%^&*()_+|{}:<>?]*((\\d+[a-zA-Z]+[-`=\\\\\\[\\];',./~!@#$%^&*()_+|{}:<>?]+)|(\\d+[-`=\\\\\\[\\];',./~!@#$%^&*()_+|{}:<>?]+[a-zA-Z]+)|([a-zA-Z]+\\d+[-`=\\\\\\[\\];',./~!@#$%^&*()_+|{}:<>?]+)|([a-zA-Z]+[-`=\\\\\\[\\];',./~!@#$%^&*()_+|{}:<>?]+\\d+)|([-`=\\\\\\[\\];',./~!@#$%^&*()_+|{}:<>?]+\\d+[a-zA-Z]+)|([-`=\\\\\\[\\];',./~!@#$%^&*()_+|{}:<>?]+[a-zA-Z]+\\d+))[-\\da-zA-Z`=\\\\\\[\\];',./~!@#$%^&*()_+|{}:<>?]*";

					}
				}
				boolean matches=txtNewPwd.matches(match);
				if(matches){
					flag=true;
				}else if(zqServer != null && zqServer.equals("htzq")){
					info="1、密码必须包含字母、数字、特殊符号三类字符；2、密码长度不能少于12个字符；";
				}else{
					if(zqServer.equals("hlzq")){
						info="1、密码必须包含字母、数字；2、密码长度不能少于8个字符；";
					}else{
						info="1、密码必须包含字母、数字、特殊符号三类字符；2、密码长度不能少于8个字符；";
					}
					
				}
			}
		}
		//清空错误提示
		
		if(flag){
			UserContext uc =  UserContextUtil.getInstance().getCurrentUserContext();
			this.setUserModel(uc._userModel);
			Map map = sysPersionService.getPassword$Extend3(uc._userModel.getUserid());
			Object extend3=map.get("extend3");
			String password=map.get("password").toString();
			if(extend3==null||"".equals(extend3)){
				String md5pwd=md5.getEncryptedPwd(txtOldPwd);
				if(password.equals(md5pwd)){
					String md5Newpwd = md5.getEncryptedPwd(txtNewPwd);
					if(md5pwd.equals(md5Newpwd)){
						info = "您输入的密码与修改的密码一致！请重新输入";
					}else{
						String salt = ShaSaltUtil.getStringSalt();
						String saltNewPwd=ShaSaltUtil.getEncryptedPwd(txtNewPwd, salt, true);
						userModel.setExtend3(salt);
						userModel.setPassword(saltNewPwd);
						sysPersionService.updateUserInfo(this.userModel);
						info = "密码修改成功！";
					}
				}else{
					info = "您输入的旧密码错误！";
				}
			}else{
				String saltPwd=ShaSaltUtil.getEncryptedPwd(txtOldPwd, userModel.getUserid().toUpperCase());
				if(password.equals(saltPwd)){
					String salt=ShaSaltUtil.getStringSalt();
					String saltNewPwd = ShaSaltUtil.getEncryptedPwd(txtNewPwd, salt, true);
					if(saltPwd.equals(saltNewPwd)){
						info = "您输入的密码与修改的密码一致！请重新输入";
					}else{
						userModel.setExtend3(salt);
						userModel.setPassword(saltNewPwd);
						sysPersionService.updateUserInfo(this.userModel);
						info = "密码修改成功！";
					}
				}else{
					info = "您输入的旧密码错误！";
				}
			}
		}
		ResponseUtil.write(info);
	}
	
	/**
	 * 上传照片
	 * @return
	 */
	public String upfilePhoto() throws Exception{
		if(this.getUserid()!=null){
			//获得头像文件名
			String imageFileName = userid+".jpg";
			//获得头像存储路径
			String path = ServletActionContext.getServletContext().getRealPath(SystemConfig._fileServerConf.getUserPhotoPath()) + "/" + imageFileName;
		    File userImage = new File(path);
		    //判断头像是否已经存在
		    if(!userImage.exists()){
		    	this.setUserImgPath("iwork_img/default_userImg.jpg");
		    	this.setIsUserImageExists("0");
		    }else{
		    	//如果用户头像存在,将用户头像的路径传到前台
		    	String imgPath = SystemConfig._fileServerConf.getUserPhotoPath()+"/"+imageFileName;
		    	this.setUserImgPath(imgPath);
		    	this.setIsUserImageExists("1");
		    }
		}
		return SUCCESS;
	}

	 /**
	  * 加载消息提醒设置页面
	  * @return
	  */
	public String loadMsRemindPage() throws Exception{
		//获得当前用户上下文
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		String userid = uc._userModel.getUserid();
		SysPersonConfig model = sysPersionService.getConfig(userid,  SysPersionRemindConstant.PERSION_REMIND_TYPE);
		if(model!=null){
			remindType = model.getValue();
		}
//		//装载要获得的用户的设置信息的集合
//		String[] condition = {SysPersionRemindConstant.PERSION_REMIND_MAIL,SysPersionRemindConstant.PERSION_REMIND_IM,SysPersionRemindConstant.PERSION_REMIND_SMS,SysPersionRemindConstant.PERSION_REMIND_SYS};
//		List<SysPersonConfig> list = sysPersionService.getUserAllConfig(userid,condition);
//		if(list.size() != 0){
//			for(int i=0;i<list.size();i++){
//				SysPersonConfig spc = (SysPersonConfig)list.get(i);
//				if(spc.getType().equals(SysPersionRemindConstant.PERSION_REMIND_MAIL)){
//					this.setConfigID_1(String.valueOf(spc.getId()));
//					//将用户设置的值传到前台页面
//					if(spc.getValue().equals("false")){this.setIsMailRemind(false);}
//					else if(spc.getValue().equals("true")){this.setIsMailRemind(true);}
//				}else if(spc.getType().equals(SysPersionRemindConstant.PERSION_REMIND_IM)){
//					this.setConfigID_2(String.valueOf(spc.getId()));
//					if(spc.getValue().equals("false")){this.setIsIMRemind(false);}
//					else if(spc.getValue().equals("true")){this.setIsIMRemind(true);}
//				}else if(spc.getType().equals(SysPersionRemindConstant.PERSION_REMIND_SMS)){
//					this.setConfigID_3(String.valueOf(spc.getId()));
//					if(spc.getValue().equals("false")){this.setIsSMSRemind(false);}
//					else if(spc.getValue().equals("true")){this.setIsSMSRemind(true);}
//				}else if(spc.getType().equals(SysPersionRemindConstant.PERSION_REMIND_SYS)){
//					this.setConfigID_4(String.valueOf(spc.getId()));
//					if(spc.getValue().equals("false")){this.setIsSYSRemind(false);}
//					else if(spc.getValue().equals("true")){this.setIsSYSRemind(true);}
//				}
//			}
//		}
//		//如果没有读取到信息就使用默认值
//		if(null==configID_1||"".equals(configID_1)){this.setIsMailRemind(true);}
//		if(null==configID_2||"".equals(configID_2)){this.setIsIMRemind(true);}
//		if(null==configID_3||"".equals(configID_3)){this.setIsSMSRemind(true);}
//		if(null==configID_4||"".equals(configID_4)){this.setIsSYSRemind(true);}
		return SUCCESS;
	}
	
	/**
	 * 保存消息设置页面
	 * @return
	 * @throws Exception
	 */
	public String saveMsRemindPage() throws Exception{
		//获取当前用户上下文
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		String userid = uc._userModel.getUserid();
		//保存每一条设置
		if(remindType!=null){
			sysPersionService.saveConfig(this.getConfigID_1(), userid, SysPersionRemindConstant.PERSION_REMIND_TYPE, remindType);
		} 
//		sysPersionService.saveConfig(this.getConfigID_1(), userid, SysPersionRemindConstant.PERSION_REMIND_MAIL, String.valueOf(this.getIsMailRemind()));
//		sysPersionService.saveConfig(this.getConfigID_2(), userid, SysPersionRemindConstant.PERSION_REMIND_IM, String.valueOf(this.getIsIMRemind()));
//		sysPersionService.saveConfig(this.getConfigID_3(), userid, SysPersionRemindConstant.PERSION_REMIND_SMS, String.valueOf(this.getIsSMSRemind()));
//		sysPersionService.saveConfig(this.getConfigID_4(), userid, SysPersionRemindConstant.PERSION_REMIND_SYS, String.valueOf(this.getIsSYSRemind()));
		return SUCCESS;
	}
	
	/**
	 * 加载系统样式设置页面
	 */
	public String loadSysStylePage() throws Exception{
		//获取当前用户上下文
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		String userid = uc._userModel.getUserid();
		//装载要获得的用户的设置信息的集合
		String[] condition = {SysPersionLayoutConstant.PERSION_PAGE_LAYOUT,SysPersionLayoutConstant.PERSION_FORM_LAYOUT,SysPersionLayoutConstant.PERSION_SKIN_LAYOUT};
		List<SysPersonConfig> list = sysPersionService.getUserAllConfig(userid,condition);
		if(list!=null&&list.size() != 0){
			for(int i=0;i<list.size();i++){
				SysPersonConfig spc = (SysPersonConfig)list.get(i);
				if(spc.getType().equals(SysPersionLayoutConstant.PERSION_PAGE_LAYOUT)){
					this.setConfigID_1(String.valueOf(spc.getId()));
					this.setPageLayoutSet(spc.getValue());
				}else if(spc.getType().equals(SysPersionLayoutConstant.PERSION_FORM_LAYOUT)){
					this.setConfigID_2(String.valueOf(spc.getId()));
					this.setFormLayoutSet(spc.getValue());
				}else if(spc.getType().equals(SysPersionLayoutConstant.PERSION_SKIN_LAYOUT)){
					this.setConfigID_3(String.valueOf(spc.getId()));
					this.setSkinLayoutSet(spc.getValue());
				}
			}
		}
		//如果没有读取到信息就使用默认值
		if(null==configID_1||"".equals(configID_1)){this.setPageLayoutSet(SysPersionLayoutConstant.PERSION_LAYOUT_RIGHT);}
		if(null==configID_2||"".equals(configID_2)){this.setFormLayoutSet(SysPersionLayoutConstant.PERSION_LAYOUT_RIGHT);}
		if(null==configID_3||"".equals(configID_3)){this.setSkinLayoutSet(SysPersionLayoutConstant.PERSION_LAYOUT_RIGHT);}
		return SUCCESS;
	}
	
	/**
	 * 保存样式设置页面
	 * @return
	 * @throws Exception
	 */
	public String saveSysStylePage() throws Exception{
		//获取当前用户上下文
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		String userid = uc._userModel.getUserid();
		//保存每一条设置
		sysPersionService.saveConfig(this.getConfigID_1(), userid, SysPersionLayoutConstant.PERSION_PAGE_LAYOUT, this.getPageLayoutSet());
		sysPersionService.saveConfig(this.getConfigID_2(), userid, SysPersionLayoutConstant.PERSION_FORM_LAYOUT, this.getFormLayoutSet());
		sysPersionService.saveConfig(this.getConfigID_3(), userid, SysPersionLayoutConstant.PERSION_SKIN_LAYOUT, this.getSkinLayoutSet());
		//刷新session中得变量参数
		UserContextUtil.getInstance().reloadCurrUserConfig(SysPersonConfig.SYS_CONF_TYPE_FORM_LAYOUT);
		UserContextUtil.getInstance().reloadCurrUserConfig(SysPersonConfig.SYS_CONF_TYPE_PAGE_LAYOUT);
		UserContextUtil.getInstance().reloadCurrUserConfig(SysPersonConfig.SYS_CONF_TYPE_SKINS_LAYOUT);
		return SUCCESS;
	}
	/**
	 * 加载其他参数设置页面
	 * @return
	 */
	public String loadOtherParamPage() throws Exception{
		//获取当前用户上下文
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		String userid = uc._userModel.getUserid();
		//装载要获得的用户的设置信息的集合
		String[] condition = {SysPersionOtherParamConstant.PERSION_OTHER_LISTFORMLINE,SysPersionOtherParamConstant.PERSION_OTHER_REPORTFORMLINE,SysPersionOtherParamConstant.PERSION_OTHER_WINDOWTIMEOUT};
		List<SysPersonConfig> list = sysPersionService.getUserAllConfig(userid,condition);
		if(list.size() != 0){
			for(int i=0;i<list.size();i++){
				SysPersonConfig spc = (SysPersonConfig)list.get(i);
				if(spc.getType().equals(SysPersionOtherParamConstant.PERSION_OTHER_LISTFORMLINE)){
					this.setConfigID_1(String.valueOf(spc.getId()));
					this.setListFormLineSet(spc.getValue());
				}else if(spc.getType().equals(SysPersionOtherParamConstant.PERSION_OTHER_REPORTFORMLINE)){
					this.setConfigID_2(String.valueOf(spc.getId()));
					this.setReportFormLineSet(spc.getValue());
				}else if(spc.getType().equals(SysPersionOtherParamConstant.PERSION_OTHER_WINDOWTIMEOUT)){
					this.setConfigID_3(String.valueOf(spc.getId()));
					this.setWindowTimeOut(spc.getValue());
				}
			}
		}
		//如果没有读取到信息就使用默认值
		if(null==configID_1||"".equals(configID_1)){this.setListFormLineSet(SysPersionOtherParamConstant.PERSION_OTHER_DEFAULT);}
		if(null==configID_2||"".equals(configID_2)){this.setReportFormLineSet(SysPersionOtherParamConstant.PERSION_OTHER_DEFAULT);}
		if(null==configID_3||"".equals(configID_3)){this.setWindowTimeOut(SysPersionOtherParamConstant.PERSION_OTHER_DEFAULT);}
		return SUCCESS;
	}
	/**
	 * 保存其他设置页面
	 * @return
	 * @throws Exception
	 */
	public String saveOtherParamPage() throws Exception{
		//获取当前用户上下文
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		String userid = uc._userModel.getUserid();
		//保存每一条设置
		sysPersionService.saveConfig(this.getConfigID_1(), userid, SysPersionOtherParamConstant.PERSION_OTHER_LISTFORMLINE, this.getListFormLineSet());
		sysPersionService.saveConfig(this.getConfigID_2(), userid, SysPersionOtherParamConstant.PERSION_OTHER_REPORTFORMLINE, this.getReportFormLineSet());
		sysPersionService.saveConfig(this.getConfigID_3(), userid, SysPersionOtherParamConstant.PERSION_OTHER_WINDOWTIMEOUT, this.getWindowTimeOut());
		return SUCCESS;
	}
	/**
	 * 加载个人委托页面
	 * @return
	 * @throws Exception
	 */
	public String loadEntrustPage() throws Exception{
		//获取当前用户上下文
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		this.setUserid(uc._userModel.getUserid());
		//获得现在服务器时间
		String curDate = UtilDate.getNowDatetime();
		this.setCurTime(curDate);
		int entrustSize = processEntrustPersonService.getNonTerminateSize(uc._userModel.getUserid(), "", "", "", "", curDate, "");
		int entrustedSize = processEntrustPersonService.getNonTerminateSize("", uc._userModel.getUserid(), "", "", "", curDate, "");
		this.setNonTerminate_entrustNum(String.valueOf(entrustSize));
		this.setNonTerminate_entrustedNum(String.valueOf(entrustedSize));
		return SUCCESS;
	}
	/**
	 * 加载个人被委托页面
	 * @return
	 * @throws Exception
	 */
	public String loadEntrustedPage() throws Exception{
		//获取当前用户上下文
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		this.setUserid(uc._userModel.getUserid());
		//获得现在服务器时间
		String curDate = UtilDate.getNowDatetime();
		this.setCurTime(curDate);
		int entrustSize = processEntrustPersonService.getNonTerminateSize(uc._userModel.getUserid(), "", "", "", "", curDate, "");
		int entrustedSize = processEntrustPersonService.getNonTerminateSize("", uc._userModel.getUserid(), "", "", "", curDate, "");
		this.setNonTerminate_entrustNum(String.valueOf(entrustSize));
		this.setNonTerminate_entrustedNum(String.valueOf(entrustedSize));
		return SUCCESS;
	}
	/**
	 * 加载个人委托历史记录页面
	 * @return
	 * @throws Exception
	 */
	public String loadEntrustLogPage() throws Exception{
		//获取当前用户上下文
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		this.setUserid(uc._userModel.getUserid());
		//获得现在服务器时间
		String curDate = UtilDate.getNowDatetime();
		this.setCurTime(curDate);
		int entrustSize = processEntrustPersonService.getNonTerminateSize(uc._userModel.getUserid(), "", "", "", "", curDate, "");
		int entrustedSize = processEntrustPersonService.getNonTerminateSize("", uc._userModel.getUserid(), "", "", "", curDate, "");
		this.setNonTerminate_entrustNum(String.valueOf(entrustSize));
		this.setNonTerminate_entrustedNum(String.valueOf(entrustedSize));
		return SUCCESS;
	}
	/**
	 * 个人设置新增委托
	 * @return
	 */
	public String addPersonEntrust() throws Exception{
		//获取当前用户上下文
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		this.setUserid(uc._userModel.getUserid()+"["+uc._userModel.getUsername()+"]");
		ProcessEntrustPerson p = new ProcessEntrustPerson();
		//个人设置委托,默认为草稿状态
		p.setEntrusetStatus(new Long(0L));
		//设置委托，默认为委托全部
		p.setIsentrusetall(new Long(1L));
		this.setModel(p);
		return SUCCESS;
	}
	
	/**
	 * 存储流程发起中心和报表中心的排列顺序
	 * @return
	 */
	public String saveProcessAndReportCenterSort(){
		String processId=null;
		String reportId=null;
		if(null==persion_process_launch_center_sort&&null==persion_report_center_sort){
			ResponseUtil.writeTextUTF8("error");
		}else{
			//获取当前用户上下文
			UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
			String userid = uc._userModel.getUserid();
			String[] condition = {SysPersionProcessAndReportCenterConstant.PERSION_PROCESS_LAUNCH_CENTER_SORT,SysPersionProcessAndReportCenterConstant.PERSION_REPORT_CENTER_SORT};
			List<SysPersonConfig> list = sysPersionService.getUserAllConfig(userid,condition);
			if(list.size() != 0){
				for(int i=0;i<list.size();i++){
					SysPersonConfig spc = (SysPersonConfig)list.get(i);
					if(null==spc){continue;}
					if(spc.getType().equals(SysPersionProcessAndReportCenterConstant.PERSION_PROCESS_LAUNCH_CENTER_SORT)){
						processId = spc.getId()+"";
					}
					if(spc.getType().equals(SysPersionProcessAndReportCenterConstant.PERSION_REPORT_CENTER_SORT)){
						reportId = spc.getId()+"";
					}
				}
			}
			if(null!=persion_process_launch_center_sort){
				sysPersionService.saveConfig(processId, userid, SysPersionProcessAndReportCenterConstant.PERSION_PROCESS_LAUNCH_CENTER_SORT, persion_process_launch_center_sort);
			}
			if(null!=persion_report_center_sort){
				sysPersionService.saveConfig(reportId, userid, SysPersionProcessAndReportCenterConstant.PERSION_REPORT_CENTER_SORT, persion_report_center_sort);
			}
			ResponseUtil.writeTextUTF8("success");
		}
		return null;
	}
	
//==================POJO============================================================
	public SysPersionService getSysPersionService() {
		return sysPersionService;
	}

	public void setSysPersionService(SysPersionService sysPersionService) {
		this.sysPersionService = sysPersionService;
	}

	public OrgUser getUserModel() {
		return userModel;
	}

	public void setUserModel(OrgUser userModel) {
		this.userModel = userModel;
	}
	public String getPersonToolbar() {
		return personToolbar;
	}

	public void setPersonToolbar(String personToolbar) {
		this.personToolbar = personToolbar;
	}
	public void setTxtOldPwd(String txtOldPwd) {
		this.txtOldPwd = txtOldPwd;
	}
	public void setTxtNewPwd(String txtNewPwd) {
		this.txtNewPwd = txtNewPwd;
	}
	public String getMenuLayoutType() {
		return menuLayoutType;
	}
	public void setMenuLayoutType(String menuLayoutType) {
		this.menuLayoutType = menuLayoutType;
	}
	public String getMenuLayoutUpdateFlag() {
		return menuLayoutUpdateFlag;
	}
	public void setMenuLayoutUpdateFlag(String menuLayoutUpdateFlag) {
		this.menuLayoutUpdateFlag = menuLayoutUpdateFlag;
	}
	public OrgUserDAO getOrgUserDAO() {
		return orgUserDAO;
	}
	public void setOrgUserDAO(OrgUserDAO orgUserDAO) {
		this.orgUserDAO = orgUserDAO;
	}

	public boolean getIsMailRemind() {
		return isMailRemind;
	}

	public void setIsMailRemind(boolean isMailRemind) {
		this.isMailRemind = isMailRemind;
	}

	public boolean getIsIMRemind() {
		return isIMRemind;
	}

	public void setIsIMRemind(boolean isIMRemind) {
		this.isIMRemind = isIMRemind;
	}

	public boolean getIsSMSRemind() {
		return isSMSRemind;
	}

	public void setIsSMSRemind(boolean isSMSRemind) {
		this.isSMSRemind = isSMSRemind;
	}

	public boolean getIsSYSRemind() {
		return isSYSRemind;
	}

	public void setIsSYSRemind(boolean isSYSRemind) {
		this.isSYSRemind = isSYSRemind;
	}

	public String getPageLayoutSet() {
		return pageLayoutSet;
	}

	public void setPageLayoutSet(String pageLayoutSet) {
		this.pageLayoutSet = pageLayoutSet;
	}

	public String getFormLayoutSet() {
		return formLayoutSet;
	}

	public void setFormLayoutSet(String formLayoutSet) {
		this.formLayoutSet = formLayoutSet;
	}

	public String getSkinLayoutSet() {
		return skinLayoutSet;
	}

	public void setSkinLayoutSet(String skinLayoutSet) {
		this.skinLayoutSet = skinLayoutSet;
	}

	public String getListFormLineSet() {
		return listFormLineSet;
	}

	public void setListFormLineSet(String listFormLineSet) {
		this.listFormLineSet = listFormLineSet;
	}

	public String getReportFormLineSet() {
		return reportFormLineSet;
	}

	public void setReportFormLineSet(String reportFormLineSet) {
		this.reportFormLineSet = reportFormLineSet;
	}

	public String getWindowTimeOut() {
		return windowTimeOut;
	}

	public void setWindowTimeOut(String windowTimeOut) {
		this.windowTimeOut = windowTimeOut;
	}
	public String getConfigID_1() {
		return configID_1;
	}

	public void setConfigID_1(String configID_1) {
		this.configID_1 = configID_1;
	}

	public String getConfigID_2() {
		return configID_2;
	}

	public void setConfigID_2(String configID_2) {
		this.configID_2 = configID_2;
	}

	public String getConfigID_3() {
		return configID_3;
	}

	public void setConfigID_3(String configID_3) {
		this.configID_3 = configID_3;
	}

	public String getConfigID_4() {
		return configID_4;
	}

	public void setConfigID_4(String configID_4) {
		this.configID_4 = configID_4;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public ProcessEntrustPerson getModel() {
		return model;
	}

	public void setModel(ProcessEntrustPerson model) {
		this.model = model;
	}

	public String getCurTime() {
		return curTime;
	}

	public void setCurTime(String curTime) {
		this.curTime = curTime;
	}

	public String getNonTerminate_entrustNum() {
		return nonTerminate_entrustNum;
	}

	public void setNonTerminate_entrustNum(String nonTerminate_entrustNum) {
		this.nonTerminate_entrustNum = nonTerminate_entrustNum;
	}

	public String getNonTerminate_entrustedNum() {
		return nonTerminate_entrustedNum;
	}

	public void setNonTerminate_entrustedNum(String nonTerminate_entrustedNum) {
		this.nonTerminate_entrustedNum = nonTerminate_entrustedNum;
	}

	public ProcessEntrustPersonService getProcessEntrustPersonService() {
		return processEntrustPersonService;
	}

	public void setProcessEntrustPersonService(
			ProcessEntrustPersonService processEntrustPersonService) {
		this.processEntrustPersonService = processEntrustPersonService;
	}
	public String getUserImgPath() {
		return userImgPath;
	}

	public void setUserImgPath(String userImgPath) {
		this.userImgPath = userImgPath;
	}

	public String getIsUserImageExists() {
		return isUserImageExists;
	}

	public void setIsUserImageExists(String isUserImageExists) {
		this.isUserImageExists = isUserImageExists;
	}

	public String getPersion_process_launch_center_sort() {
		return persion_process_launch_center_sort;
	}

	public void setPersion_process_launch_center_sort(
			String persion_process_launch_center_sort) {
		this.persion_process_launch_center_sort = persion_process_launch_center_sort;
	}

	public String getPersion_report_center_sort() {
		return persion_report_center_sort;
	}

	public void setPersion_report_center_sort(String persion_report_center_sort) {
		this.persion_report_center_sort = persion_report_center_sort;
	}

	public String getRemindType() {
		return remindType;
	}

	public void setRemindType(String remindType) {
		this.remindType = remindType;
	}

	public MutilRoleModel getMutiModel() {
		return mutiModel;
	}

	public void setMutiModel(MutilRoleModel mutiModel) {
		this.mutiModel = mutiModel;
	}

	public List<MutilRoleModel> getRolelist() {
		return rolelist;
	}

	public void setRolelist(List<MutilRoleModel> rolelist) {
		this.rolelist = rolelist;
	}

	public String getRolelistHtml() {
		return rolelistHtml;
	}

	public void setRolelistHtml(String rolelistHtml) {
		this.rolelistHtml = rolelistHtml;
	}

	public String getItemid() {
		return itemid;
	}

	public void setItemid(String itemid) {
		this.itemid = itemid;
	}
	
	
}
