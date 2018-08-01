package com.iwork.plugs.expression.action;

import org.apache.log4j.Logger;
import com.iwork.commons.util.Base64;
import com.iwork.core.util.ResponseUtil;
import com.iwork.plugs.expression.service.ExpressionService;
import com.iwork.plugs.expression.util.ExpressionUtil;
import com.opensymphony.xwork2.ActionSupport;

public class ExpressionAction extends ActionSupport {
	private static Logger logger = Logger.getLogger(ExpressionAction.class);
	private static final long serialVersionUID = 1L;

	ExpressionService expressionService;
	String zNodesData;
	int itemId;
	String searchStr;
	String eId;
	String defItemId;
	String oldEid;
	String getParamsEId;
	String encodeStr;

	private final String FUNCTIONFLAG = "%";

	/**
	 * 首页ACTION 处理传入参数
	 * 
	 * @return
	 */
	public String index() {

		try {
			if (eId != null && eId.trim().length() > 0) {
				eId = new String(Base64.decode(eId.replace("*", "+").replace("-", "/")), "utf-8");
				oldEid = eId;
				if (eId.indexOf("(") != -1) {
					defItemId = eId.substring(0, eId.indexOf("(")).replace("(","");
				} else {
					defItemId = eId;
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error(e,e);
		}

		return SUCCESS;
	}

	/**
	 * 根据查询条件 构建ztree
	 * 
	 * @return
	 */
	public String getTreeJsonStr() {

		zNodesData = ExpressionUtil.getInstance().getTressShowJson(searchStr);

		if (zNodesData != null) {
			ResponseUtil.writeTextUTF8(zNodesData);
		}

		return null;
	}

	/**
	 * 打开一个树节点的详细信息
	 * 
	 * @return
	 */
	public String openTreeItem() {

		String result = "";

		if (itemId >= 0 || defItemId.trim().length() > 0) {
			result = ExpressionUtil.getInstance()
					.getOneItemHtmlStr(itemId, defItemId);
		}

		if (result != null) {
			ResponseUtil.writeTextUTF8(result);
		}

		return null;
	}

	/**
	 * 打开一个节点的表单
	 * 
	 * @return
	 */
	public String openTreeItemGrid() {
		String result = "";
		if (itemId >= 0 || (eId != null && eId.trim().length() > 0)) {
			result = ExpressionUtil.getInstance().getOneItemParamsJsonStr(
					itemId, eId, defItemId);
		}
		if (result != null && result.length() > 2) {
			ResponseUtil
					.writeTextUTF8(result.substring(1, result.length() - 1));
		}
		return null;
	}

	/**
	 * 根据打开的ID获取参数类型
	 * 
	 * @return
	 */
	public String getParamsType() {
		String result = "";

		if (getParamsEId.trim().length() > 0) {
			result = ExpressionUtil.getInstance().getParamsType(getParamsEId);
		}

		if (result != null) {
			ResponseUtil.writeTextUTF8(result);
		}

		return null;
	}

	public String getBase64Str() {

		String result = "";
		ResponseUtil.write("true");
		return null;
	}

	public String getEncodeStr() {
		return encodeStr;
	}

	public void setEncodeStr(String encodeStr) {
		this.encodeStr = encodeStr;
	}

	public String getzNodesData() {
		return zNodesData;
	}

	public void setzNodesData(String zNodesData) {
		this.zNodesData = zNodesData;
	}

	public ExpressionService getExpressionService() {
		return expressionService;
	}

	public void setExpressionService(ExpressionService expressionService) {
		this.expressionService = expressionService;
	}

	public int getItemId() {
		return itemId;
	}

	public void setItemId(int itemId) {
		this.itemId = itemId;
	}

	public String getSearchStr() {
		return searchStr;
	}

	public void setSearchStr(String searchStr) {
		this.searchStr = searchStr;
	}

	public String geteId() {
		return eId;
	}

	public void seteId(String eId) {
		this.eId = eId;
	}

	public String getDefItemId() {
		return defItemId;
	}

	public void setDefItemId(String defItemId) {
		this.defItemId = defItemId;
	}

	public String getOldEid() {
		return oldEid;
	}

	public void setOldEid(String oldEid) {
		this.oldEid = oldEid;
	}

	public String getGetParamsEId() {
		return getParamsEId;
	}

	public void setGetParamsEId(String getParamsEId) {
		this.getParamsEId = getParamsEId;
	}
}
