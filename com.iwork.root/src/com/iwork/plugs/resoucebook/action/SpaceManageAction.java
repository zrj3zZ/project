package com.iwork.plugs.resoucebook.action;

import java.net.URLDecoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts2.ServletActionContext;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.core.util.ResponseUtil;
import com.iwork.plugs.resoucebook.model.IworkRmBase;
import com.iwork.plugs.resoucebook.model.IworkRmSpace;
import com.iwork.plugs.resoucebook.model.IworkRmWeb;
import com.iwork.plugs.resoucebook.service.SpaceManageService;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.log4j.Logger;
public class SpaceManageAction extends ActionSupport {
	public SpaceManageService spaceManageService;
	private static Logger logger = Logger.getLogger(SpaceManageAction.class);
	private Long id;
	private String pstrScript;
	
	private IworkRmSpace model;
	private IworkRmBase irbase;
	private IworkRmWeb irweb;
	
	// 打开空间记录
	private String cspaceid;
	private String cspacename;
	private String hspaceid;
	// 修改空间记录
	private String euserid;
	private String eusername;
	private String eresouceid;
	private String eresouce;
	private String ebegin;
	private String eend;
	private String ememo;
	private String estatus;
	// 批量修改状态
	private String estatuss;
	private String ids;
	// 批量删除记录
	private String idsr;
	// 打开基础信息
	private String bspacename;// 通过url传过的
	// 新增基础信息
	private String bspaceid;
	private String bresouceid;
	private String bresoucename;
	private String bpara1;
	private String bpara2;
	private String bpara3;
	private String bpara4;
	private String bpara5;
	private String bmemo;
	private String bstatus;
	private String bpicture;
	private String hbspaceid;
	private String hbspacename;
	// 批量删除信息
	private String hspacename;
	private String cspaceid1;
	private String cspacename1;
	// 修改基础信息
	private String bspaceid2;
	private String bspacename2;
	private String bresouceid2;
	private String bresoucename2;
	private String bpara12;
	private String bpara22;
	private String bpara32;
	private String bpara42;
	private String bpara52;
	private String bmemo2;
	private String bstatus2;
	private String bpicture2;
	// 车辆预定
	private String hid;// resouceid
	private String hname;
	private String cardate1;
	private String carhour1;
	private String carmin1;
	private String cardate2;
	private String carhour2;
	private String carmin2;
	// 资源预定查询
	private String qspaceid;
	private String qspacename;
	private String qresouceid;
	private String qresoucename;
	private String quserid;
	private String qusername;
	private String qdate;
	private String qstatus;
	private String tqspaceid;// url中传递值
	private String tqspacename;
	private String tqresouceid;
	private String tqresoucename;
	private String tquserid;
	private String tqusername;
	private String tqdate;
	private String tqstatus;

	public String index() {
		return "spacem";
	}
	/**
	 * 进入页面查询空间列表并显示
	 * 
	 * @return
	 */
	public void showJson() {
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		String userid = uc.get_userModel().getUserid();// 得到userid
		String json = "";
		json = spaceManageService.getSpaceJson(userid);
		ResponseUtil.write(json);
	}
 
	/**
	 * 新增空间
	 * 
	 * @return
	 */
	public String addSpace() {
		model = new IworkRmSpace();
		model.setStatus(new Long(1));
		model.setCycle(new Long(14));
		model.setType(new Long(2));  
		return SUCCESS;
	}
	
	/**
	 * 保存空间
	 * 
	 * @return
	 */
	public void saveSpace() {
		String msg = "error";
		if(model!=null){
			spaceManageService.addSpace(model);
			msg = SUCCESS;
		}
		ResponseUtil.write(msg);
	}

	/**
	 * 删除空间
	 * 
	 * @return
	 */
	public void removeSpace() {
		String msg = "error";
		if(id!=null){
			spaceManageService.removeSpace(id);
			msg = SUCCESS;
		}
		ResponseUtil.write(msg);
	}

	/**
	 * 修改空间
	 * 
	 * @return
	 */
	public String editSpace() {
		if (id!=null) {
			model = spaceManageService.getSpacem(id);
		}
		return SUCCESS;
	}
 
	/**
	 * 资源预定空间管理中的内容管理
	 * 
	 */
	public String loadContentM() {
		String cspaceid = this.getCspaceid();
		this.setCspaceid(cspaceid);
		return "contentm";
	}

	/**
	 * 资源预定空间管理-内容管理查询内容并显示
	 * 
	 * @return
	 */
	public void qRMWeb() {
		String cspaceid = this.getCspaceid();
		this.setCspaceid(cspaceid);
		String json = "";
		json = spaceManageService.getContentJson(cspaceid);
		ResponseUtil.write(json);
	}

	/**
	 * 修改记录
	 * 
	 * @return
	 */
	public String editContent() {
		String pstrScript = "<script>$(function(){";
		IworkRmWeb rmw = spaceManageService.getContentm(id);
		long statusl = Long.parseLong(this.getEstatus());
		String begin = this.getEbegin() + ":00";
		String end=this.getEend()+":00";
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date1 = null;
		Date date2 = null;
		try {
			date1 = format.parse(begin); // Wed sep 26 00:00:00 CST //												// 2007
			date2 = format.parse(end);
		} catch (ParseException e) {
			logger.error(e,e);
		}
		rmw.setResouce(this.getEresouce());
		rmw.setResouceid(this.getEresouceid());
		rmw.setUserid(this.getEuserid());
		rmw.setUsername(this.getEusername());
		rmw.setBegintime(date1);
		rmw.setEndtime(date2);
		rmw.setMemo(this.getEmemo());
		rmw.setStatus(statusl);
		spaceManageService.editContent(rmw);// 空间修改加入数据库
		pstrScript += "window.parent.$.messager.alert('系统提示', '已成功修改空间记录！', 'info');";// 弹出提示
		pstrScript += "});</script>";
		this.setPstrScript(pstrScript);
		return "spacereturn";
	}

	/**
	 * 批量修改状态
	 * 
	 * @return
	 */
	public String updateContents() {
		String pstrScript = "<script>$(function(){";
		String newstatus = this.getEstatuss();
		long lstatus = Long.parseLong(newstatus);
		String idss = this.getIds();
		String[] ids = idss.split(",");
		for (int i = 0; i < ids.length; i++) {
			String id1 = ids[i];
			IworkRmWeb rmw = spaceManageService.getContentm(Long.parseLong(id1));
			rmw.setStatus(lstatus);
			spaceManageService.editContent(rmw);// 空间修改加入数据库
		}
		pstrScript += "window.parent.$.messager.alert('系统提示', '已成功删除空间记录状态！', 'info');";// 弹出提示
		pstrScript += "});</script>";
		this.setPstrScript(pstrScript);
		return "spacereturn";
	}
 
	/**
	 * 批量删除记录
	 * 
	 * @return
	 */
	public String removeContents() {
		String a = this.getHspaceid();
		this.setCspaceid(a);
		String idsr = this.getIdsr();
		String[] ids = idsr.split(",");
		for (int i = 0; i < ids.length; i++) {
			String id1 = ids[i];
			spaceManageService.removeContents(id1);// 删除记录
		}

		return "contentm";
	}

	/**
	 * 资源预定空间管理中的基础信息
	 * 
	 */
	public String loadBaseM() {
		String cspaceid1 = this.getCspaceid();
		String cspacename1 = this.getCspacename();
		try {
			cspacename1 = URLDecoder.decode(cspacename1, "UTF-8");
		} catch (Exception e) {logger.error(e,e);
		}
		this.setCspaceid(cspaceid1);
		this.setCspacename(cspacename1);
		return "basem";
	}

	/**
	 * 资源预定空间管理-基础信息查询内容并显示
	 * 
	 * @return
	 */
	public String qRMBase() {
		String cspaceid = this.getCspaceid1();
		String ccspacename = this.getCspacename1();
		try {
			ccspacename = URLDecoder.decode(ccspacename, "UTF-8");
		} catch (Exception e) {
		}
		this.setCspaceid(cspaceid);
		this.setCspacename(ccspacename);
		String json = "";
		json = spaceManageService.getBaseJson(cspaceid);
		HttpServletRequest request = ServletActionContext.getRequest();
		request.setAttribute("rmbase", json);
		return "basemjson";
	}

	/**
	 * 基础信息-新增基础信息,同一空间中资源编号不能重复
	 * 
	 * @return
	 */
	public String addBase() {
		String pstrScript = "<script>$(function(){";
		String spaceid = this.getHbspaceid();
		String resouceid = this.getBresouceid();
		String spacename = this.getHbspacename();
		String msg = spaceManageService.checkBase(spaceid, resouceid);

		if (msg.equals("")) {
			IworkRmBase rmb = new IworkRmBase();
			long spaceidl = Long.parseLong(spaceid);
			long statusl = Long.parseLong(this.getBstatus());
			String resoucename = this.getBresoucename() == null ? "" : this
					.getBresoucename();
			String para1 = this.getBpara1() == null ? "" : this.getBpara1();
			String para2 = this.getBpara2() == null ? "" : this.getBpara2();
			String para3 = this.getBpara3() == null ? "" : this.getBpara3();
			String para4 = this.getBpara4() == null ? "" : this.getBpara4();
			String para5 = this.getBpara5() == null ? "" : this.getBpara5();
			String memo = this.getBmemo() == null ? "" : this.getBmemo();
			String pictureurl = this.getBpicture() == null ? "" : this
					.getBpicture();
			rmb.setSpaceid(spaceidl);
			rmb.setSpacename(spacename);
			rmb.setResouceid(resouceid);
			rmb.setResoucename(resoucename);
			rmb.setParameter1(para1);
			rmb.setParameter2(para2);
			rmb.setParameter3(para3);
			rmb.setParameter4(para4);
			rmb.setParameter5(para5);
			rmb.setPicture(pictureurl);
			rmb.setStatus(statusl);
			rmb.setMemo(memo);
			spaceManageService.addBase(rmb);// 分组加入数据库
			pstrScript += "window.parent.$.messager.alert('系统提示', '已成功添加资源信息！', 'info');";// 弹出提示
		} else {
			pstrScript += "window.parent.$.messager.alert('系统提示', '在"
					+ spacename + "空间中此资源编号已存在！', 'error');";// 弹出提示
		}
		pstrScript += "});</script>";
		this.setPstrScript(pstrScript);
		return "spacereturn";
	}

	/**
	 * 批量删除基础信息
	 * 
	 * @return
	 */
	public String removeBases() {
		String cspaceid = this.getHspaceid();
		String bspacename = this.getHspacename();
		try {
			bspacename = URLDecoder.decode(bspacename, "UTF-8");
		} catch (Exception e) {logger.error(e,e);
		}
		this.setCspaceid(cspaceid);
		this.setCspacename(bspacename);
		String idsr = this.getIdsr();
		String[] ids = idsr.split(",");
		for (int j = 0; j < ids.length; j++) {
			String id1 = ids[j];
			long id2 = Long.parseLong(id1);
			spaceManageService.removeBases(id2);// 删除基础信息
		}

		return "basem";
	}

	/**
	 * 修改基础信息
	 * 
	 * @return
	 */
	public String editBase() {
		String pstrScript = "<script>$(function(){";
		long idl = id;
		String spaceid = this.getHbspaceid();
		String spacename = this.getHbspacename();
		String resouceid = this.getBresouceid2();
		String msg = spaceManageService.checkBase2(idl, spaceid, resouceid);

		if (msg.equals("")) {
			IworkRmBase rmb = spaceManageService.getBasem(idl);
			;
			long spaceidl = Long.parseLong(spaceid);
			long statusl = Long.parseLong(this.getBstatus2());
			String resoucename = this.getBresoucename2() == null ? "" : this
					.getBresoucename2();
			String para12 = this.getBpara12() == null ? "" : this.getBpara12();
			String para22 = this.getBpara22() == null ? "" : this.getBpara22();
			String para32 = this.getBpara32() == null ? "" : this.getBpara32();
			String para42 = this.getBpara42() == null ? "" : this.getBpara42();
			String para52 = this.getBpara52() == null ? "" : this.getBpara52();
			String memo = this.getBmemo2() == null ? "" : this.getBmemo2();
			String pictureurl = this.getBpicture2() == null ? "" : this
					.getBpicture2();
			rmb.setSpaceid(spaceidl);
			rmb.setSpacename(spacename);
			rmb.setResouceid(resouceid);
			rmb.setResoucename(resoucename);
			rmb.setParameter1(para12);
			rmb.setParameter2(para22);
			rmb.setParameter3(para32);
			rmb.setParameter4(para42);
			rmb.setParameter5(para52);
			rmb.setPicture(pictureurl);
			rmb.setStatus(statusl);
			rmb.setMemo(memo);
			spaceManageService.editBase(rmb);// 分组加入数据库
			pstrScript += "window.parent.$.messager.alert('系统提示', '已成功修改资源信息！', 'info');";// 弹出提示
		} else {
			pstrScript += "window.parent.$.messager.alert('系统提示', '在"
					+ spacename + "空间中此资源编号已存在！', 'error');";// 弹出提示
		}
		pstrScript += "});</script>";
		this.setPstrScript(pstrScript);
		return "spacereturn";
	}

	/**
	 * 车辆预定
	 * 
	 * @return
	 */
	public String addCar() {
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		String userid = uc.get_userModel().getUserid();
		String username = uc.get_userModel().getUsername();
		String resouceid = this.getHid();
		String resoucename = this.getHname();
		String cardate1 = this.getCardate1();
		String cardate2=this.getCardate2();
		String carhour1 = this.getCarhour1();
		String carmin1 = this.getCarmin1().equals("") ? "00" : this
				.getCarmin1();
		// String cardate2=this.getCardate2();
		String carhour2 = this.getCarhour2();
		String carmin2 = this.getCarmin2().equals("") ? "00" : this
				.getCarmin2();
		IworkRmWeb rmw = new IworkRmWeb();
		long spaceid=spaceManageService.getSpaceid(resouceid);//根据resouceid得到spaceid
		String spacename=spaceManageService.getSpacename(resouceid);		
		rmw.setSpaceid(spaceid);// 车辆的spaceid=1
		rmw.setSpacename(spacename);
		rmw.setResouceid(resouceid);
		rmw.setResouce(resoucename);
		rmw.setStatus(1L);// 默认状态为有效
		String begintime = "";
		String endtime = "";
		if(carmin1.length()==1){
			carmin1="0"+carmin1;
		}
		if(carmin2.length()==1){
			carmin2="0"+carmin2;
		}
		if (cardate1 != null && !"".equals(cardate1)) {
			begintime = cardate1 + " " + carhour1 + ":" + carmin1 + ":00";
			endtime = cardate2+ " " + carhour2 + ":" + carmin2 + ":00";
		} else {
			begintime = "";
			endtime = "";
		}
		if (begintime != null && !"".equals(begintime)) {
			SimpleDateFormat format = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss");
			Date date1 = null;
			Date date2 = null;
			try {
				date1 = format.parse(begintime); // Wed sep 26 00:00:00 CST
													// // 2007
				date2 = format.parse(endtime);
			} catch (ParseException e) {
				logger.error(e,e);
			}
			rmw.setBegintime(date1);
			rmw.setEndtime(date2);
		}
		rmw.setUserid(userid);
		rmw.setUsername(username);
		spaceManageService.addWeb(rmw);// 车辆预定加入数据库
		return "carlist";
	}

	/**
	 * 进入查询资源预定信息首页
	 * 
	 * @return
	 */
	public String loadQuery() {
		String spaceid = this.getQspaceid() == null ? "" : this.getQspaceid();
		String spacename = this.getQspacename() == null ? "" : this
				.getQspacename();
		String resouceid = this.getQresouceid() == null ? "" : this
				.getQresouceid();
		String resoucename = this.getQresoucename() == null ? "" : this
				.getQresoucename();
		String userid = this.getQuserid() == null ? "" : this.getQuserid();
		String username = this.getQusername() == null ? "" : this
				.getQusername();
		String status = this.getQstatus() == null ? "" : this.getQstatus();
		String date = this.getQdate() == null ? "" : this.getQdate();

		this.setTqspaceid(spaceid);
		this.setTqspacename(spacename);
		this.setTqresouceid(resouceid);
		this.setTqresoucename(resoucename);
		this.setTquserid(userid);
		this.setTqusername(username);
		this.setTqstatus(status);
		this.setTqdate(date);
		return "queryweb";
	}

	public void queryWeb() {
		String spaceid = this.getTqspaceid();
		String status = this.getTqstatus();
		String date = this.getTqdate();
		String userid = this.getTquserid();
		String spacename = this.getTqspacename();
		String resouceid = this.getTqresouceid();
		String resoucename = this.getTqresoucename();
		String username = this.getTqusername();
		try {
			spacename = URLDecoder.decode(spacename, "UTF-8");
			resouceid = URLDecoder.decode(resouceid, "UTF-8");
			resoucename = URLDecoder.decode(resoucename, "UTF-8");
			username = URLDecoder.decode(username, "UTF-8");
		} catch (Exception e) {logger.error(e,e);
		}
		this.setTqspaceid(spaceid);
		this.setTqspacename(spacename);
		this.setTqresouceid(resouceid);
		this.setTqresoucename(resoucename);
		this.setTquserid(userid);
		this.setTqusername(username);
		this.setTqstatus(status);
		this.setTqdate(date);
		
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		String manager= uc.get_userModel().getUserid();// 得到userid
		String json = "";
		json = spaceManageService.getWebJson(manager,spaceid, spacename, resouceid,
				resoucename, userid, username, date, status);
		ResponseUtil.write(json);
		
	}

	public SpaceManageService getSpaceManageService() {
		return spaceManageService;
	}

	public void setSpaceManageService(SpaceManageService spaceManageService) {
		this.spaceManageService = spaceManageService;
	}

	public String getPstrScript() {
		return pstrScript;
	}

	public void setPstrScript(String pstrScript) {
		this.pstrScript = pstrScript;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}


	public String getEmemo() {
		return ememo;
	}

	public void setEmemo(String ememo) {
		this.ememo = ememo;
	}

	public String getEstatus() {
		return estatus;
	}

	public void setEstatus(String estatus) {
		this.estatus = estatus;
	}

	public String getEstatuss() {
		return estatuss;
	}

	public void setEstatuss(String estatuss) {
		this.estatuss = estatuss;
	}

	public String getIds() {
		return ids;
	}

	public void setIds(String ids) {
		this.ids = ids;
	}

	public String getIdsr() {
		return idsr;
	}

	public void setIdsr(String idsr) {
		this.idsr = idsr;
	}

	public String getCspaceid() {
		return cspaceid;
	}

	public void setCspaceid(String cspaceid) {
		this.cspaceid = cspaceid;
	}

	public String getHspaceid() {
		return hspaceid;
	}

	public void setHspaceid(String hspaceid) {
		this.hspaceid = hspaceid;
	}

	public String getBspacename() {
		return bspacename;
	}

	public void setBspacename(String bspacename) {
		this.bspacename = bspacename;
	}

	public String getBspaceid() {
		return bspaceid;
	}

	public void setBspaceid(String bspaceid) {
		this.bspaceid = bspaceid;
	}

	public String getBresouceid() {
		return bresouceid;
	}

	public void setBresouceid(String bresouceid) {
		this.bresouceid = bresouceid;
	}

	public String getBresoucename() {
		return bresoucename;
	}

	public void setBresoucename(String bresoucename) {
		this.bresoucename = bresoucename;
	}

	public String getBpara1() {
		return bpara1;
	}

	public void setBpara1(String bpara1) {
		this.bpara1 = bpara1;
	}

	public String getBpara2() {
		return bpara2;
	}

	public void setBpara2(String bpara2) {
		this.bpara2 = bpara2;
	}

	public String getBpara3() {
		return bpara3;
	}

	public void setBpara3(String bpara3) {
		this.bpara3 = bpara3;
	}

	public String getBpara4() {
		return bpara4;
	}

	public void setBpara4(String bpara4) {
		this.bpara4 = bpara4;
	}

	public String getBpara5() {
		return bpara5;
	}

	public void setBpara5(String bpara5) {
		this.bpara5 = bpara5;
	}

	public String getBmemo() {
		return bmemo;
	}

	public void setBmemo(String bmemo) {
		this.bmemo = bmemo;
	}

	public String getBstatus() {
		return bstatus;
	}

	public void setBstatus(String bstatus) {
		this.bstatus = bstatus;
	}

	public String getBpicture() {
		return bpicture;
	}

	public void setBpicture(String bpicture) {
		this.bpicture = bpicture;
	}

	public String getHbspaceid() {
		return hbspaceid;
	}

	public void setHbspaceid(String hbspaceid) {
		this.hbspaceid = hbspaceid;
	}

	public String getHbspacename() {
		return hbspacename;
	}

	public void setHbspacename(String hbspacename) {
		this.hbspacename = hbspacename;
	}

	public String getCspacename() {
		return cspacename;
	}

	public void setCspacename(String cspacename) {
		this.cspacename = cspacename;
	}

	public String getHspacename() {
		return hspacename;
	}

	public void setHspacename(String hspacename) {
		this.hspacename = hspacename;
	}

	public String getCspaceid1() {
		return cspaceid1;
	}

	public void setCspaceid1(String cspaceid1) {
		this.cspaceid1 = cspaceid1;
	}

	public String getCspacename1() {
		return cspacename1;
	}

	public void setCspacename1(String cspacename1) {
		this.cspacename1 = cspacename1;
	}

	public String getBspaceid2() {
		return bspaceid2;
	}

	public void setBspaceid2(String bspaceid2) {
		this.bspaceid2 = bspaceid2;
	}

	public String getBspacename2() {
		return bspacename2;
	}

	public void setBspacename2(String bspacename2) {
		this.bspacename2 = bspacename2;
	}

	public String getBresouceid2() {
		return bresouceid2;
	}

	public void setBresouceid2(String bresouceid2) {
		this.bresouceid2 = bresouceid2;
	}

	public String getBresoucename2() {
		return bresoucename2;
	}

	public void setBresoucename2(String bresoucename2) {
		this.bresoucename2 = bresoucename2;
	}

	public String getBpara12() {
		return bpara12;
	}

	public void setBpara12(String bpara12) {
		this.bpara12 = bpara12;
	}

	public String getBpara22() {
		return bpara22;
	}

	public void setBpara22(String bpara22) {
		this.bpara22 = bpara22;
	}

	public String getBpara32() {
		return bpara32;
	}

	public void setBpara32(String bpara32) {
		this.bpara32 = bpara32;
	}

	public String getBpara42() {
		return bpara42;
	}

	public void setBpara42(String bpara42) {
		this.bpara42 = bpara42;
	}

	public String getBpara52() {
		return bpara52;
	}

	public void setBpara52(String bpara52) {
		this.bpara52 = bpara52;
	}

	public String getBmemo2() {
		return bmemo2;
	}

	public void setBmemo2(String bmemo2) {
		this.bmemo2 = bmemo2;
	}

	public String getBstatus2() {
		return bstatus2;
	}

	public void setBstatus2(String bstatus2) {
		this.bstatus2 = bstatus2;
	}

	public String getBpicture2() {
		return bpicture2;
	}

	public void setBpicture2(String bpicture2) {
		this.bpicture2 = bpicture2;
	}

	public String getHid() {
		return hid;
	}

	public void setHid(String hid) {
		this.hid = hid;
	}

	public String getCardate1() {
		return cardate1;
	}

	public void setCardate1(String cardate1) {
		this.cardate1 = cardate1;
	}

	public String getCarhour1() {
		return carhour1;
	}

	public void setCarhour1(String carhour1) {
		this.carhour1 = carhour1;
	}

	public String getCarmin1() {
		return carmin1;
	}

	public void setCarmin1(String carmin1) {
		this.carmin1 = carmin1;
	}

	public String getCardate2() {
		return cardate2;
	}

	public void setCardate2(String cardate2) {
		this.cardate2 = cardate2;
	}

	public String getCarhour2() {
		return carhour2;
	}

	public void setCarhour2(String carhour2) {
		this.carhour2 = carhour2;
	}

	public String getCarmin2() {
		return carmin2;
	}

	public void setCarmin2(String carmin2) {
		this.carmin2 = carmin2;
	}

	public String getHname() {
		return hname;
	}

	public void setHname(String hname) {
		this.hname = hname;
	}

	public String getQspaceid() {
		return qspaceid;
	}

	public void setQspaceid(String qspaceid) {
		this.qspaceid = qspaceid;
	}

	public String getQspacename() {
		return qspacename;
	}

	public void setQspacename(String qspacename) {
		this.qspacename = qspacename;
	}

	public String getQresouceid() {
		return qresouceid;
	}

	public void setQresouceid(String qresouceid) {
		this.qresouceid = qresouceid;
	}

	public String getQresoucename() {
		return qresoucename;
	}

	public void setQresoucename(String qresoucename) {
		this.qresoucename = qresoucename;
	}

	public String getQuserid() {
		return quserid;
	}

	public void setQuserid(String quserid) {
		this.quserid = quserid;
	}

	public String getQusername() {
		return qusername;
	}

	public void setQusername(String qusername) {
		this.qusername = qusername;
	}

	public String getQdate() {
		return qdate;
	}

	public void setQdate(String qdate) {
		this.qdate = qdate;
	}

	public String getQstatus() {
		return qstatus;
	}

	public void setQstatus(String qstatus) {
		this.qstatus = qstatus;
	}

	public String getTqspaceid() {
		return tqspaceid;
	}

	public void setTqspaceid(String tqspaceid) {
		this.tqspaceid = tqspaceid;
	}

	public String getTqspacename() {
		return tqspacename;
	}

	public void setTqspacename(String tqspacename) {
		this.tqspacename = tqspacename;
	}

	public String getTqresouceid() {
		return tqresouceid;
	}

	public void setTqresouceid(String tqresouceid) {
		this.tqresouceid = tqresouceid;
	}

	public String getTqresoucename() {
		return tqresoucename;
	}

	public void setTqresoucename(String tqresoucename) {
		this.tqresoucename = tqresoucename;
	}

	public String getTquserid() {
		return tquserid;
	}

	public void setTquserid(String tquserid) {
		this.tquserid = tquserid;
	}

	public String getTqusername() {
		return tqusername;
	}

	public void setTqusername(String tqusername) {
		this.tqusername = tqusername;
	}

	public String getTqdate() {
		return tqdate;
	}

	public void setTqdate(String tqdate) {
		this.tqdate = tqdate;
	}

	public String getTqstatus() {
		return tqstatus;
	}

	public void setTqstatus(String tqstatus) {
		this.tqstatus = tqstatus;
	}

	public String getEuserid() {
		return euserid;
	}

	public void setEuserid(String euserid) {
		this.euserid = euserid;
	}

	public String getEusername() {
		return eusername;
	}

	public void setEusername(String eusername) {
		this.eusername = eusername;
	}

	public String getEresouceid() {
		return eresouceid;
	}

	public void setEresouceid(String eresouceid) {
		this.eresouceid = eresouceid;
	}

	public String getEresouce() {
		return eresouce;
	}

	public void setEresouce(String eresouce) {
		this.eresouce = eresouce;
	}

	public String getEbegin() {
		return ebegin;
	}

	public void setEbegin(String ebegin) {
		this.ebegin = ebegin;
	}

	public String getEend() {
		return eend;
	}

	public void setEend(String eend) {
		this.eend = eend;
	}

	public IworkRmSpace getModel() {
		return model;
	}

	public void setModel(IworkRmSpace model) {
		this.model = model;
	}

	public IworkRmBase getIrbase() {
		return irbase;
	}

	public void setIrbase(IworkRmBase irbase) {
		this.irbase = irbase;
	}

	public IworkRmWeb getIrweb() {
		return irweb;
	}

	public void setIrweb(IworkRmWeb irweb) {
		this.irweb = irweb;
	}

}
