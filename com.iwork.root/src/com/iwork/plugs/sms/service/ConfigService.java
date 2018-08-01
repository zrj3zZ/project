package com.iwork.plugs.sms.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import com.iwork.plugs.sms.bean.LogMst;
import com.iwork.plugs.sms.dao.ConfigDao;
import com.iwork.plugs.sms.util.MsgConst;

public class ConfigService {

	MsgConst msgconst = new MsgConst();
	private ConfigDao configDao;

	public void setConfigDao(ConfigDao ConfigDao) {
		this.configDao = ConfigDao;
	}

	public String configall() {
		return configDao.configall();
	}

	public String configall2(String typen) {
		return configDao.configall2(typen);
	}

	/**
	 * 系统设置类别显示
	 * 
	 * @param list
	 * @return
	 */
	public String getconfiglist(List list) {
		StringBuffer sb = new StringBuffer();
		if (list != null && list.size() > 0) {
			Iterator it = list.iterator();
			sb
					.append("<select class ='font' name=typename1 id=typename1 onchange=\"selectchange(this.value)\">");
			int i = 1;
			while (it.hasNext()) {
				Hashtable hs = (Hashtable) it.next();
				String type = hs.get("TYPE").toString();
				String typename = type + "_NAME";
				// String a=msgconst.hsdb.get("MOBILE_SP_NAME").toString();
				String name = msgconst.hsdb.get(typename).toString();
				if (i == 1) {
					sb.append("<option value='").append(
							hs.get("TYPE").toString()).append("' selected>")
							.append(name).append("</option>");
				} else {
					sb.append("<option value='").append(
							hs.get("TYPE").toString()).append("'>")
							.append(name).append("</option>");
				}
				i++;
			}
			sb.append("</select>");
		}
		return sb.toString();
	}

	/**
	 * 系统设置类别显示(被选中的类别的显示)
	 * 
	 * @param list
	 * @return
	 */
	public String getconfiglist2(List list, String typen) {
		StringBuffer sb = new StringBuffer();
		if (list != null && list.size() > 0) {
			Iterator it = list.iterator();
			sb
					.append("<select name=typename1 id=typename1 onchange=\"selectchange(this.value)\">");
			int i = 1;
			while (it.hasNext()) {
				Hashtable hs = (Hashtable) it.next();
				String type = hs.get("TYPE").toString();
				String typename = type + "_NAME";
				// String a=msgconst.hsdb.get("MOBILE_SP_NAME").toString();
				String name = msgconst.hsdb.get(typename).toString();
				String typeo = hs.get("TYPE").toString();
				if (typeo.equals(typen)) {
					sb.append("<option value='").append(
							hs.get("TYPE").toString()).append("' selected>")
							.append(name).append("</option>");
				} else {
					sb.append("<option value='").append(
							hs.get("TYPE").toString()).append("'>")
							.append(name).append("</option>");
				}
				i++;
			}
			sb.append("</select>");
		}
		return sb.toString();
	}

	/**
	 * 查询默认通道的列表
	 * 
	 * @param type1
	 * @return
	 */
	public String qtypelist(String type1) {
		return configDao.qtypelist(type1);
	}

	/**
	 * 类型1的列表显示
	 * 
	 * @param list
	 * @return
	 */
	public String getlistc(ArrayList list) {
		StringBuffer sb = new StringBuffer();
		if (list != null && list.size() > 0) {
			Iterator it = list.iterator();
			int i = 1;
			while (it.hasNext()) {
				Hashtable ht = (Hashtable) it.next();
				String cid = ht.get("CID").toString();
				String updatenum = "<a  href='###' onclick=\"dataEditc('" + cid
						+ "')\">修改</a>";
				String delnum = "<a href='delnumc.action?cid=" + cid
						+ "'>删除</a>";
				sb.append("<tr>\n");
				sb.append("<td class='font' align='center'>"
						+ i + "</td>");
				sb.append("<td align='center' class='font' nowrap>"
						+ ht.get("PARAMETERID").toString() + "</td>\n");
				sb.append("<td align='center' class='font' nowrap>"
						+ ht.get("PARAMETERNAME").toString() + "</td>\n");
				sb.append("<td class='font' align='center'>").append(
						"&nbsp;").append(updatenum).append("&nbsp;").append(
						delnum).append("&nbsp;").append("</td>");
				sb.append("</tr>\n");
				i++;
			}

		}
		return sb.toString();
	}

	/**
	 * 新增加到数据库
	 * 
	 * @param type
	 * @param parid
	 * @param par
	 * @return
	 */
	public String addconfig(String type, String parid, String par) {
		return this.addconfig2(type, parid, par);
	}

	/**
	 * 新增加到数据库
	 * 
	 * @param type
	 * @param parid
	 * @param par
	 * @return
	 */
	public String addconfig2(String typeadd, String keyadd, String valueadd) {
		if (!this.valueidFormatCheck(keyadd)) {
			return "参数ID格式错误！";
		}
		String returnvalue = "";
		String param1 = "";
		String param2 = "";
		if (typeadd.equals("FILTER_WORD")) {
			String sqlrepeat1 = "from ConfigMst where type=? and key=?";// 过滤词中key不能重复
			param1=typeadd;
			param2=keyadd;
			int numberrepeat1 = configDao.getrepeatnum(sqlrepeat1,param1,param2);
			String sqlrepeat2 = "from ConfigMst where type=? and value=?";// 过滤词中value不能重复
			param1=typeadd;
			param2=valueadd;
			int numberrepeat2 = configDao.getrepeatnum(sqlrepeat2,param1,param2);
			if (numberrepeat1 != 0) {
				returnvalue = "参数ID已存在!";
			} else if (numberrepeat2 != 0) {
				returnvalue = "参数值已存在!";
			} else {
				configDao.addconfigDB(typeadd, keyadd, valueadd);
				// 写入日志
				String logvalue = "增加配置参数 type:" + typeadd + ", key:" + keyadd
						+ ", value:" + valueadd;
				LogMst lm = new LogMst();
				Date date = new Date();
				lm.setLogtime(date);
				lm.setLogtype("4");// LOG_TYPE_MSG_CONFIG = 4
				lm.setUserid("test");
				lm.setValue(logvalue);
				configDao.savelog(lm);
			}
		} else {
			String sqlrepeat = " from ConfigMst where type=? and key=?";
			param1=typeadd;
			param2=keyadd;
			int numberrepeat = configDao.getrepeatnum(sqlrepeat,param1,param2);

			if (numberrepeat != 0) {
				returnvalue = "参数ID已存在!";

			} else {
				configDao.addconfigDB(typeadd, keyadd, valueadd);
				// 写入日志
				String logvalue = "增加配置参数 type:" + typeadd + ", key:" + keyadd
						+ ", value:" + valueadd;
				LogMst lm = new LogMst();
				Date date = new Date();
				lm.setLogtime(date);
				lm.setLogtype("4");// LOG_TYPE_MSG_CONFIG = 4
				lm.setUserid("test");
				lm.setValue(logvalue);
				configDao.savelog(lm);
			}
		}
		return returnvalue;
	}

	public static boolean valueidFormatCheck(String valueid) {
		return valueid.matches("^\\d+$");
	}

	public String delnumc(String cid) {
		return configDao.delnumc(cid);
	}

	/**
	 * 修改条目之前查询老的类型，key，value
	 * 
	 * @param cid
	 * @return
	 */
	public Hashtable qdelnum(String cid) {
		return configDao.qdelnum(cid);
	}

	public String editconfig(String cid, String oldtype, String oldkey,
			String loldvalue, String keychange, String valuechange) {
		if (!this.valueidFormatCheck(keychange)) {
			return "参数ID格式错误!";
		}
		String param1 = "";
		String param2 = "";
		String returnvalue = "";
		if (oldtype.equals("FILTER_WORD")) {
			String sqlrepeat1 = "from ConfigMst where type=? and key=?";
			param1 = oldtype;
			param2 = keychange;
			int numberrepeat1 = configDao.getrepeatnum(sqlrepeat1,param1,param2);
			String sqlrepeat2 = "from ConfigMst where type=? and value=?";
			param1 = oldtype;
			param2 = valuechange;
			int numberrepeat2 = configDao.getrepeatnum(sqlrepeat2,param1,param2);
			if (numberrepeat1 != 0) {
				returnvalue = "参数ID已存在!";
			} else if (numberrepeat2 != 0) {
				returnvalue = "参数值已存在!";
			} else {
				String sql = "update ConfigMst set KEY= ? ,VALUE= ?  where cid= ? ";
				List param=new ArrayList();
				param.add(keychange);
				param.add(valuechange);
				param.add(cid);
				configDao.edit(sql,param);

				// 写入日志
				String logvalue = "修改配置参数 type:" + oldtype + ", id:" + cid
						+ ", key:" + oldkey + " -> " + keychange + ", value:"
						+ loldvalue + " -> " + valuechange;
				LogMst lm = new LogMst();
				Date date = new Date();
				lm.setLogtime(date);
				lm.setLogtype("4");// LOG_TYPE_MSG_CONFIG = 4
				lm.setUserid("test");
				lm.setValue(logvalue);
				configDao.savelog(lm);
			}
		} else {
			String sqlrepeatcheck = " from ConfigMst where KEY=? and TYPE=?)";// 检查重复性
			param1 = keychange;
			param2 = oldtype;
			int number = configDao.getrepeatnum(sqlrepeatcheck,param1,param2);
			if (number != 0 && !oldkey.equals(keychange)) {
				returnvalue = "参数ID已存在";
				// return getsavepageweb(page, returnvalue, oldtype);
			} else {
				String sql = "update ConfigMst set KEY= ? ,VALUE= ?  where cid= ? ";
				List param=new ArrayList();
				param.add(keychange);
				param.add(valuechange);
				param.add(cid);
				configDao.edit(sql,param);
				String logvalue = "修改配置参数 type:" + oldtype + ", id:" + cid
						+ ", key:" + oldkey + " -> " + keychange + ", value:"
						+ loldvalue + " -> " + valuechange;
				LogMst lm = new LogMst();
				Date date = new Date();
				lm.setLogtime(date);
				lm.setLogtype("4");// LOG_TYPE_MSG_CONFIG = 4
				lm.setUserid("test");
				lm.setValue(logvalue);
				configDao.savelog(lm);

				// return getsavepageweb(page, "", oldtype);
			}
		}
		return returnvalue;
	}

}
