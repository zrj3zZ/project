package com.iwork.plugs.sms.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import com.iwork.plugs.sms.bean.LogMst;
import com.iwork.plugs.sms.bean.MsgMst;
import com.iwork.plugs.sms.dao.MsgmanageDao;
import com.iwork.plugs.sms.util.MsgConst;
import com.iwork.plugs.sms.util.MsgResults;
import com.iwork.plugs.sms.util.SendSMSFactory;
import com.iwork.plugs.sms.util.model.ReturnModel;
import org.apache.log4j.Logger;
public class MsgmanageService {
	private static Logger logger = Logger.getLogger(MsgmanageService.class);
	private MsgmanageDao msgmanageDao;

	public void setMsgmanageDao(MsgmanageDao MsgmanageDao) {
		this.msgmanageDao = MsgmanageDao;
	}
	/**
	 * 分页
	 * @param userid
	 * @return
	 */
	public int getRows(String userid) {
		// TODO Auto-generated method stub
		return msgmanageDao.getRows(userid);
	}
	/**
	 * 分页
	 * @param userid
	 * @return
	 */
	public int getRows2(String sender,String supplier,String chanel,
			String mobilenum,String keywords,String status, String begintime,String endtime, String batch) {
		// TODO Auto-generated method stub
		return msgmanageDao.getRows2(sender, supplier, chanel,
				mobilenum, keywords, status, begintime, endtime, batch);
	}	
	/**
	 * 数据库里取通道
	 * 
	 * @return
	 */
	public String getchannelall() {
		return msgmanageDao.getchannelall();
	}

	public String getchannelall2() {
		return msgmanageDao.getchannelall2();
	}

	/**
	 * 获得通道列表
	 * 
	 * @param list
	 * @return
	 */
	public String getsplist(ArrayList list) {
		StringBuffer sb = new StringBuffer();
		if (list != null && list.size() > 0) {
			Iterator it = list.iterator();
			while (it.hasNext()) {
				Hashtable hs = (Hashtable) it.next();
				sb.append("<option value='").append(
						hs.get("CHANNELID").toString()).append("'>").append(
						hs.get("IDNAME").toString()).append("</option>");
			}
		}
		return sb.toString();
	}

	/**
	 * 修改短信获得通道列表
	 * 
	 * @param list
	 * @return
	 */
	public String getsplist2(ArrayList list) {
		StringBuffer sb = new StringBuffer();
		if (list != null && list.size() > 0) {
			Iterator it = list.iterator();
			sb
					.append("<select name='updatechanel' id='updatechanel'><option value='' selected>不修改</option>");
			while (it.hasNext()) {
				Hashtable hs = (Hashtable) it.next();
				sb.append("<option value='").append(hs.get("CID").toString())
						.append("'>").append(hs.get("IDNAME").toString())
						.append("</option>");
			}
			sb.append("</select>");
		}
		return sb.toString();
	}

	/**
	 * 数据库里取供应商
	 * 
	 * @return
	 */
	public String getspall() {
		return msgmanageDao.getspall();
	}

	/**
	 * 获取供应商列表
	 * 
	 * @param list
	 * @return
	 */
	public String getspnamelist(ArrayList list) {
		StringBuffer sb = new StringBuffer();
		if (list != null && list.size() > 0) {
			Iterator it = list.iterator();
			while (it.hasNext()) {
				Hashtable hs = (Hashtable) it.next();
				sb.append("<option value='").append(hs.get("SPID").toString())
						.append("'>").append(hs.get("NAME").toString()).append(
								"</option>");
			}
		}
		return sb.toString();
	}

	public String getQuery(int pageSize,int startRow,String sender, String supplier, String chanel,
			String mobilenum, String keywords, String status, String begintime,
			String endtime, String batch) {
		return msgmanageDao.getQuery(pageSize,startRow,sender, supplier, chanel, mobilenum,
				keywords, status, begintime, endtime, batch);
	}

	/**
	 * 查询短信列表显示
	 * 
	 * @param list
	 * @return
	 */

	public String getList(ArrayList list) {
		StringBuffer sb = new StringBuffer();
		if (list != null && list.size() > 0) {
			Iterator it = list.iterator();			 
			int i = 1;
			// int i = (pageNow - 1) * lineNumber + 1;
			while (it.hasNext()) {
				Hashtable ht = (Hashtable) it.next();
				String id = ht.get("ID").toString();
				sb.append("<tr>\n");
				sb
						.append("<td class ='font1' align='center'>"
								+ i
								+ "&nbsp;<input type=checkbox name='chk' id='chk' class ='font1' value ="
								+ ht.get("ID").toString() + " ></td>"); // sb.append("<td
				// class
				// ='actionsoftReportData'
				// align='center'>"
				// + i + "</td>");
				sb.append(
						"<td align='center' class ='font1' nowrap>"
								+ ht.get("BATCHNUM1").toString())
						.append("<br>");
				sb.append(ht.get("BATCHNUM2").toString()).append("</td>\n");
				sb.append(
						"<td align='center' class ='font1'  nowrap>"
								+ ht.get("SUBMITTIME1").toString()).append(
						"<br>");
				sb.append(ht.get("SUBMITTIME2").toString()).append("</td>\n");
				sb.append(
						"<td align='center' class ='font1' nowrap>"
								+ ht.get("SENDTIME1").toString())
						.append("<br>");
				sb.append(ht.get("SENDTIME2").toString()).append("</td>\n");
				if ((ht.get("STATUS").toString()).equals("成功")
						|| (ht.get("STATUS").toString()).equals("已改成功")) {
					sb.append("<td align='center' class ='font1' nowrap>"
							+ ht.get("STATUS").toString() + "</td>\n");
				} else if ((ht.get("STATUS").toString()).equals("失败")
						|| (ht.get("STATUS").toString()).equals("其他失败")
						|| (ht.get("STATUS").toString()).equals("已改失败")) {
					sb.append("<td align='center' class ='font1'><font color='#FF0000'>"
							+ ht.get("STATUS").toString() + "</font></td>\n");
				} else {
					sb.append("<td align='center' class ='font1'><font color='#0099FF'>"
							+ ht.get("STATUS").toString() + "</font></td>\n");
				}

				sb.append(
						"<td align='center' class ='font1' nowrap>"
								+ ht.get("USER1").toString()).append("<br>");
				// sb.append(ht.get("USEREND").toString()).append(
				// "</td>\n");
				sb.append("</td>\n");
				sb.append("<td align='center'class ='font1'  nowrap>"
						+ ht.get("MOBILENUM").toString() + "</td>\n");
				sb.append("<td  class ='font1'>" + ht.get("CONTENT").toString()
						+ "</td>\n");

				sb.append(
						"<td align='center' class ='font1'>" + ht.get("PATHNAME1").toString())
						.append("<br>");
				sb.append(ht.get("PATHNAME2").toString()).append("</td>\n");
				sb.append("<td align='center' class ='font1' nowrap>"
						+ ht.get("CHANNELID").toString() + "</td>\n");
				sb.append("<td align='center' class ='font1' nowrap>"
						+ ht.get("MSGSP").toString() + "</td>\n");

				sb.append("</tr>\n");
				i++;
			}
			
		}
		return sb.toString();
	}

	/**
	 * 修改短信通道或状态
	 * 
	 * @return
	 */
	public String getupdate(String tvalue, String updatestatus,
			String updatechanel) {
		StringBuffer where = new StringBuffer();
		StringBuffer wherelog = new StringBuffer();
		String updatestatus1 = "";
		StringBuffer rvalue1 = new StringBuffer();
		StringBuffer rvalue2 = new StringBuffer();
		if (updatestatus.equals("1")) {
			updatestatus1 = "11";
		}
		if (updatestatus.equals("2")) {
			updatestatus1 = "12";
		}
		if (updatestatus.equals("6")) {
			updatestatus1 = "13";
		}
		if (updatestatus != null && !"".equals(updatestatus)
				&& updatechanel != null && !"".equals(updatechanel)) {
			where.append(" set status=?,channelid=?");
			String sql = "select value  from ConfigMst where type='MSG_STATUS' and key=?";
			List<Object> params = new ArrayList<Object>();
			params.add(updatestatus1);
			String staname = msgmanageDao.getcahennelvlue(sql,params);
			// wherelog.append("状态改为"+updatestatus1+"',通道改为"+updatechanel+"'.");
			wherelog.append("状态改为" + staname + "',通道改为" + updatechanel + "'.");
		}
		if (updatestatus != null && !"".equals(updatestatus)
				&& (updatechanel == null || "".equals(updatechanel))) {
			where.append(" set status=?");
			String sql2 = "select value  from ConfigMst where type='MSG_STATUS' and key=?";
			List<Object> params = new ArrayList<Object>();
			params.add(updatestatus1);
			String staname = msgmanageDao.getcahennelvlue(sql2,params);
			wherelog.append("状态改为" + staname + ".");
		}
		if (updatechanel != null && !"".equals(updatechanel)
				&& (updatestatus == null || "".equals(updatestatus))) {
			where.append(" set channelid=?");
			wherelog.append("通道改为" + updatechanel + ".");
		}
		String t[] = tvalue.split(" ");
		int j = 0;
		for (int i = 0; i < t.length; i++) {
			// String oldsql = "select userid as uidname,STATUS as
			// oldstatus,CHANNELID as
			// oldchanel from BO_MOBILE_MSG where id='"
			// + t[i] + "'";
			String oldsql = " from MsgMst where cid=?";
			Hashtable htold = new Hashtable();
			List<Object> paramshtold = new ArrayList<Object>();
			paramshtold.add(t[i]);
			htold = msgmanageDao.getoldmsg(oldsql,paramshtold);
			String oldsta = (String) htold.get("oldstatus");
			String oldcha = (String) htold.get("oldchanel");
			String sql1 = "select value from ConfigMst where type='MSG_STATUS' and key=?";
			List<Object> params = new ArrayList<Object>();
			params.add(oldsta);
			String staname1 = msgmanageDao.getcahennelvlue(sql1,params);
			String logvalue = "修改条目的id是" + t[i] + ",userid是" + "test"
					+ "原来的状态是" + staname1 + ",通道是" + oldcha + ".所做的修改是："
					+ wherelog.toString();
			// 加入日志
			LogMst lm=new LogMst();
			Date date=new Date();
			lm.setLogtime(date);
			lm.setLogtype("3");//LOG_TYPE_MSG_MANAGE = 3;
			lm.setUserid("test");
			lm.setValue(logvalue);
			msgmanageDao.savelog(lm);
			String sqlex = "update MsgMst" + where.toString() + " where cid=?";
			List<Object> paramsex = new ArrayList<Object>();
			if (updatestatus != null && !"".equals(updatestatus)
					&& updatechanel != null && !"".equals(updatechanel)) {
				paramsex.add(updatestatus1);
			}
			if (updatestatus != null && !"".equals(updatestatus)
					&& (updatechanel == null || "".equals(updatechanel))) {
				paramsex.add(updatechanel);
			}
			if (updatechanel != null && !"".equals(updatechanel)
					&& (updatestatus == null || "".equals(updatestatus))) {
				paramsex.add(updatechanel);
			}
			msgmanageDao.update(sqlex,paramsex);
			if (updatestatus.equals("1")) {// 待发状态的要重新发送
				String sqlcm = "from MsgMst where id='" + t[i] + "'";
				List<Object> paramscm = new ArrayList<Object>();
				paramscm.add(t[i]);
				Hashtable htcm = new Hashtable();
				htcm = msgmanageDao.getmsgagain(sqlcm,paramscm);
				String contentagain = (String) htcm.get("contentagain");
				String mobilenumagain = (String) htcm.get("mobilenumagain");
				String useridagain = (String) htcm.get("useridagain");
				String batchnum = (String) htcm.get("batchnum");
				String ret = this.handleMsgUtilagain(contentagain,
						mobilenumagain, batchnum, useridagain);
				ret = String.format("短信批号=%s,接收号码=%s,%s", batchnum,
						mobilenumagain, ret);
				rvalue2.append(ret).append("\n");
				j++;
			}
		}
		if (j == 0) {
			return "";
		} else {
			rvalue1.append("一共重新发送").append(j).append("条。具体信息如下：\n");
			rvalue1.append(rvalue2.toString());
			return rvalue1.toString();
		}
	}

	/**
	 * 待发-重新发送短信
	 * 
	 * @param content
	 * @param nums
	 * @param batchnum
	 * @param uid
	 * @return
	 */
	public String handleMsgUtilagain(String content, String nums,
			String batchnum, String uid) {
		String ret = "";
		// 检查用户限额
		ret = this.limitCheckagain(batchnum, uid);
		// 短信送入数据库里
		try {
			ret = this.batchReadyagain(batchnum, uid, nums, content);
		} catch (Exception e) {
			logger.error(e,e);
		}
		// 短信发送结果集合
		MsgResults results = new MsgResults();
		// 发送短信
		ret = this.sendagain(batchnum, uid, nums, results);
		if (ret.length() > 0) {
			return ret;
		}
		// 减用户余额
		int limit = this.reduceLimit(uid, results.getOkCount());
		ret = this.getResultStr(uid, results, limit);
		results.clear();
		return ret;
	}

	public String getResultStr(String uid, MsgResults r, int limit) {
		String ret = "";
		int okCount = r.getOkCount();
		int errCount = r.getErrCount();
		int msgCount = r.getMsgCount();

		if (okCount > 0) {
			ret = String.format("发送成功%d人", okCount);
		}
		if (errCount > 0) {
			if (ret.length() > 0) {
				ret = String.format("%s，失败%d人", ret, errCount);
			} else {
				ret = String.format("失败%d人", errCount);
			}
		}
		if (msgCount > 0) {
			if (ret.length() > 0) {
				ret = String.format("%s。折合短信%d条", ret, msgCount);
			} else {
				ret = String.format("折合短信%d条", msgCount);
			}
		}
		if (ret.length() > 0) {
			ret = String.format("%s。剩余短信额度%d条。", ret, limit);
		} else {
			ret = String.format("剩余短信额度%d条。", limit);
		}
		if (errCount > 0) {
			ret = String.format("%s\n失败原因如下：\n%s", ret, r.getErrStr());
		}

		return ret;
	}

	// 检查用户限额
	public String limitCheckagain(String batchnum, String uid) {
		String ret = "";
		int limit = 0;
		// int count = 0;
		String sql1 = "select limit from LimitMst ";// where userid='test'";
		// String sql2 = "select count(*) from bo_mobile_msg_temp where
		// batchnum=?";
		List<Object> params1 = new ArrayList<Object>();
		limit = msgmanageDao.qchannel(sql1,params1);
		if (limit < 1) {
			ret = "用户短信余额不足";
		}

		return ret;
	}

	// 短信送入待发队列
	public String batchReadyagain(String batchnum, String uid,
			String mobilenum, String content) throws Exception {
		String ret = "";
		int limit = 0;
		int count = 0;
		String oldsql = "from MsgMst where batchnum=? and mobilenum=? and content=?";
		Hashtable ht = new Hashtable();
		ht = msgmanageDao.qoldmsg(oldsql,batchnum,mobilenum,content);
		String oldchannelid = (String)ht.get("channelid");
		String oldmsp =(String) ht.get("msgsp");
		String oldsign =(String) ht.get("sign");
		String oldbiling = (String)ht.get("billinglen");
		String oldprice = (String)ht.get("price");
		String submits=(String)ht.get("submit");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date submit = sdf.parse(submits);
		Date senddate=new Date();
		
		MsgMst mm=new MsgMst();
		mm.setMobilenum(mobilenum);
		mm.setContent(content);
		mm.setUserid(uid);
		mm.setStatus(1);
		mm.setMsgsp(Integer.parseInt(oldmsp));
		mm.setSendtime(senddate);
		mm.setSubmittime(submit);
		mm.setBatchnum(batchnum);
		mm.setChannelid(Integer.parseInt(oldchannelid));
		mm.setSign(oldsign);
		mm.setBillinglen(Integer.parseInt(oldbiling));
		mm.setPrice(Double.valueOf(oldprice));
		msgmanageDao.savemsg(mm);
		return ret;

	}

	// 发送一条短信
	public String sendagain(String batchnum, String uid, String mobileNum,
			MsgResults results) {
		String ret = "";
		String content = "";
		double price = 0;
		int billinglen = 0;
		// String sql = "select content,price,billinglen from bo_mobile_msg
		// where batchnum='"+batchnum+"' and mobilenum='"+mobileNum+"'";
		String sql = "from MsgMst where batchnum=? and mobilenum=?";
		Hashtable ht = new Hashtable();
		ht = msgmanageDao.qmsg1(sql,batchnum,mobileNum);

		content = (String) ht.get("content");
		price = Double.parseDouble((String) ht.get("price"));
		billinglen = Integer.parseInt((String) ht.get("billinglen"));

		if (content.length() > 0) {
			ret = send(batchnum, content, mobileNum, uid);
			if (ret.equals(MsgConst.SEND_INFO_OK)) {
				results.put(mobileNum, MsgConst.SEND_FLAG_OK);
				int msg_count = calcMsgCount(content.length(), billinglen);
				results.addMsgCount(msg_count);
				results.addFee(mobileNum, price * msg_count);
			} else {
				results.put(mobileNum, ret);
			}
		} else {
			ret = "内容为空";
			// MsgLog.add(uid, MsgConst.LOG_TYPE_SEND_ERR, "内容为空。批次：" + batchnum
			// + "，接收号码：" + mobileNum);
		}

		// 更新短信发送状态
		updateMsgStatus(batchnum, mobileNum, ret);

		return ret;

	}

	// 更新表中短信状态
	public void updateMsgStatus(String batchnum, String mobileNum, String ret) {
		int status = 0;
		if (ret.equals(MsgConst.SEND_INFO_OK))
			status = MsgConst.SEND_STATUS_OK;
		else if (ret.equals(MsgConst.SEND_INFO_EXIST_FILTER_WORD)
				|| ret.equals(MsgConst.SEND_INFO_INVALID_CONTENT))
			status = MsgConst.SEND_STATUS_INVALID_CONTENT;
		else if (ret.equals(MsgConst.SEND_INFO_INVALID_NUM))
			status = MsgConst.SEND_STATUS_INVALID_NUM;
		else if (ret.equals(MsgConst.SEND_INFO_NO_MONEY))
			status = MsgConst.SEND_STATUS_NO_MONEY;
		else if (ret.equals(MsgConst.SEND_INFO_INVALID_PARAM)
				|| ret.equals(MsgConst.SEND_INFO_INVALID_USER)
				|| ret.equals(MsgConst.SEND_INFO_PERMISSION_DENY))
			status = MsgConst.SEND_STATUS_API_ERR;
		else if (ret.equals(MsgConst.SEND_INFO_EXCEED_LIMIT))
			status = MsgConst.SEND_STATUS_EXCEED_LIMIT;
		else if (ret.equals(MsgConst.SEND_INFO_INVALID_SERVICE))
			status = MsgConst.SEND_STATUS_NO_SERVICE;
		else
			status = MsgConst.SEND_STATUS_OTHER_ERR;

		String sql = "update MsgMst set status=?,sendtime=sysdate where status=1 and batchnum=? and mobilenum=?";
		List<Object> params = new ArrayList<Object>();
		params.add(status);
		params.add(batchnum);
		params.add(mobileNum);
		msgmanageDao.doupdate1(sql,params);
	}

	// 减用户余额
	public int reduceLimit(String uid, int reduction) {
		int ret = 0;
		String sql0 = "update LimitMst set limit=limit-? where userid=?";
		List<Object> params0 = new ArrayList<Object>();
		params0.add(reduction);
		params0.add(uid);
		msgmanageDao.doupdate1(sql0,params0);
		String sql1 = "select limit from LimitMst where userid=?";
		List<Object> params1 = new ArrayList<Object>();
		params1.add(uid);
		ret = msgmanageDao.qchannel(sql1,params1);
		return ret;
	}

	// 发送一条短信
	public String send(String batchnum, String content,
			String mobileNum, String uid) {
		String function = "LY";
		// content = UtilCode.decode(content);
		// mobileNum = UtilCode.decode(mobileNum);
		String successtitle = MsgConst.SEND_INFO_OK;
		String functionkey = "FUNCTION_" + function;
		SendSMSFactory factory = new SendSMSFactory();
		if (MsgConst.SEND_ON) {
			ReturnModel model = factory.factory(functionkey, content,
					mobileNum, successtitle);
			String err = model.getReturnvalue();
			if (err.equals(MsgConst.ERR_INFO_500)) {
				err = MsgConst.ERR_INFO_TOO_LONG;
			}
			// if (!MsgConst.SEND_INFO_OK.equals(err))
			// MsgLog.add(uid, MsgConst.LOG_TYPE_SEND_ERR, err + "。批次：" +
			// batchnum
			// + "，接收号码：" + mobileNum);
			return err;
		} else {
			return MsgConst.SEND_INFO_OK;
		}
	}

	public int calcMsgCount(int content_len, int billinglen) {
		int msg_count = (int) Math.ceil(((double) content_len / billinglen));
		return msg_count;
	}
}
