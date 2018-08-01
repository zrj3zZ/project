package com.iwork.plugs.expression.util;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.json.JSONArray;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.iwork.app.conf.ServerConfigParser;
import com.iwork.plugs.expression.model.ExpressionModel;
import com.iwork.plugs.expression.model.ExpressionParam;
import com.iwork.plugs.expression.model.ExpressionTreeModel;
import org.apache.log4j.Logger;

public class ExpressionUtil {
	private static Logger logger = Logger.getLogger(ExpressionUtil.class);
	private static ExpressionUtil instance = null;
	private static Map<String, Integer> groupMap = new HashMap<String, Integer>();
	private static int flagCount = 1;
	private final String FUNCTIONFLAG = "%";

	private ExpressionUtil() {
	}

	public static ExpressionUtil getInstance() {
		if (instance == null) {
			instance = new ExpressionUtil();
		}
		return instance;
	}

	/**
	 * 获取配置文件地址
	 * 
	 * @return
	 */
	private String getFileName() {
		String xml = "iform-runtime-variable.xml";
		String web_inf_Path = new File(ServerConfigParser.class
				.getResource("/").getPath()).getParent();
		web_inf_Path = web_inf_Path.replace("%20", " ");// 替换空格
		return web_inf_Path + File.separator+ xml;
	}

	/**
	 * 获得节点LIST
	 * 
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	private List<ExpressionModel> getExpressionModelList() {

		List<ExpressionModel> list = new ArrayList<ExpressionModel>();
		SAXReader saxReader = new SAXReader();
		Document document;
		try {
			document = saxReader.read(new File(this.getFileName()));
			// Document
			Element root = document.getRootElement();
			// 遍历根结点的所有孩子节点 配置文件的第一条是配置信息 舍弃不解析
			int flag = 0;
			for (Iterator iter = root.elementIterator(); iter.hasNext();) {
				Element element = (Element) iter.next();
				if (flag > 0) {
					ExpressionModel model = new ExpressionModel();
					// 遍历结点的所有孩子节点并进行处理
					for (Iterator iterInner = element.elementIterator(); iterInner
							.hasNext();) {
						Element elementInner = (Element) iterInner.next();
						this.setModelValue(elementInner, model);
					}
					list.add(model);
				}
				flag++;
			}

		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			logger.error(e,e);
		}

		return list;
	}

	/**
	 * 解析子节点
	 * 
	 * @param elementInner
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	private List<ExpressionParam> getExpressionParamList(Element elementInner) {

		List<ExpressionParam> list = new ArrayList<ExpressionParam>();

		if (elementInner.getName().equals("parameters")) {
			for (Iterator parameters = elementInner.elementIterator(); parameters
					.hasNext();) {

				Element elementParams = (Element) parameters.next();
				ExpressionParam param = new ExpressionParam();
				for (Iterator parameter = elementParams.elementIterator(); parameter
						.hasNext();) {
					Element elementParam = (Element) parameter.next();
					if (elementParam.getName().equals("name")) {
						param.setName(elementParam.getText() == null ? ""
								: elementParam.getText());
					}
					if (elementParam.getName().equals("desc")) {
						param.setDesc(elementParam.getText() == null ? ""
								: elementParam.getText());
					}
					if (elementParam.getName().equals("type")) {
						param.setType(elementParam.getText() == null ? ""
								: elementParam.getText());
					}
				}
				list.add(param);
			}
		}

		return list;
	}

	/**
	 * 赋值
	 * 
	 * @param elementInner
	 * @param model
	 */
	private void setModelValue(Element elementInner, ExpressionModel model) {

		if (elementInner.getName().equals("groupname")) {
			String groupName = elementInner.getText();
			model.setGroupName(groupName == null ? "" : groupName);
			if (groupName != null && groupName.trim().length() > 0) {
				if (groupMap.get(groupName) == null) {
					groupMap.put(groupName, flagCount);
					model.setGroupId(flagCount);
					flagCount++;
				} else {
					model.setGroupId(groupMap.get(groupName));
				}
			}
		}
		if (elementInner.getName().equals("id")) {
			model.setId(elementInner.getText() == null ? "" : elementInner
					.getText());
		}
		if (elementInner.getName().equals("title")) {
			model.setTitle(elementInner.getText() == null ? "" : elementInner
					.getText());
		}
		if (elementInner.getName().equals("implements-class")) {
			model.setImplementsClass(elementInner.getText() == null ? ""
					: elementInner.getText());
		}
		if (elementInner.getName().equals("label")) {
			model.setLabel(elementInner.getText() == null ? "" : elementInner
					.getText());
		}
		if (elementInner.getName().equals("desc")) {
			model.setDesc(elementInner.getText() == null ? "" : elementInner
					.getText());
		}
		if (elementInner.getName().equals("returnType")) {
			model.setReturnType(elementInner.getText() == null ? ""
					: elementInner.getText());
		}
		if (elementInner.getName().equals("returnDesc")) {
			model.setReturnDesc(elementInner.getText() == null ? ""
					: elementInner.getText());
		}
		if (elementInner.getName().equals("eg")) {
			model.setEg(elementInner.getText() == null ? "" : elementInner
					.getText());
		}

		model.setParams(this.getExpressionParamList(elementInner));

	}

	/**
	 * 根据查询条件构建tree
	 * 
	 * @param searchStr
	 * @return
	 */
	public String getTressShowJson(String searchStr) {

		List<ExpressionTreeModel> list = new ArrayList<ExpressionTreeModel>();
		List<ExpressionModel> models = this.getExpressionModelList();
		List<ExpressionModel> tempModels = this.getExpressionModelList();
		ExpressionTreeModel model = new ExpressionTreeModel();
		boolean openFlag = false;
		Map<Integer, Boolean> groupIdMap = new HashMap<Integer, Boolean>();

		// 筛选规则
		if (searchStr != null && searchStr.trim().length() > 0) {
			if (models != null && models.size() > 0) {
				for (int i = 0; i < models.size(); i++) {
					ExpressionModel expressionModel = models.get(i);
					if (expressionModel.getTitle().indexOf(searchStr) == -1) {
						tempModels.add(expressionModel);
					}
					if (expressionModel.getId().indexOf(searchStr) != -1) {
						tempModels.remove(expressionModel);
					}
				}
				openFlag = true;
			}
		}

		models.removeAll(tempModels);

		// 子节点
		int flag = 0;
		for (ExpressionModel eModel : models) {
			model = new ExpressionTreeModel();
			model.setId(10000 + flag);
			model.setpId(1 * 10 + eModel.getGroupId());
			model.setName(eModel.getTitle() + "(" + eModel.getId() + ")");
			model.setOpen(openFlag);
			model.setClick("openTreeItem('" + flag + "')");
			model.setMethodName(eModel.getId());
			model.seteModelId(eModel.getId());
			groupIdMap.put(1 * 10 + eModel.getGroupId(), true);
			flag++;
			list.add(model);
		}

		// 父节点
		Set<Map.Entry<String, Integer>> set = groupMap.entrySet();
		for (Iterator<Map.Entry<String, Integer>> it = set.iterator(); it
				.hasNext();) {
			Map.Entry<String, Integer> entry = (Map.Entry<String, Integer>) it
					.next();
			model = new ExpressionTreeModel();
			model.setId(1 * 10 + entry.getValue());
			model.setpId(1);
			model.setName(entry.getKey());
			model.setOpen(openFlag);
			model.setClick(false);

			Boolean groupFlag = groupIdMap.get(1 * 10 + entry.getValue());
			if (groupFlag != null && groupFlag) {
				list.add(model);
			}
		}

		JSONArray json = JSONArray.fromObject(list);
		return json.toString();
	}

	/**
	 * 打开一个节点的内容说明
	 * 
	 * @param itemId
	 * @param eId
	 * @return
	 */
	public String getOneItemHtmlStr(int itemId, String defItemId) {

		List<ExpressionModel> models = this.getExpressionModelList();
		ExpressionModel model = new ExpressionModel();
		if (itemId < 0) {
			for (ExpressionModel expressionModel : models) {
				if (expressionModel.getId().equals(defItemId)) {
					model = expressionModel;
					break;
				}
			}
		} else {
			model = models.get(itemId);
		}

		StringBuffer sb = new StringBuffer();
		sb.append("<ul>");
		if (model.getDesc() != null) {
			sb.append("<li>").append("描述:").append(model.getDesc())
					.append("</li>");
		}
		List<ExpressionParam> params = model.getParams();
		if (params != null && params.size() > 0) {
			for (ExpressionParam param : params) {
				sb.append("<li>").append("参数:")
						.append("{" + param.getType() + "}")
						.append(param.getDesc()).append("</li>");
			}
		}

		if (model.getReturnType() != null) {
			sb.append("<li>").append("返回值:")
					.append("{" + model.getReturnType() + "}").append("</li>");
		}

		if (model.getEg() != null) {
			sb.append("<li>").append("案例:").append(model.getEg())
					.append("</li>");
		}

		sb.append("</ul>");
		return sb.toString();
	}

	/**
	 * 打开一个节点的参数Grid
	 * 
	 * @param itemId
	 * @param eId
	 * @return
	 */
	public String getOneItemParamsJsonStr(int itemId, String eId,
			String defItemId) {

		HashMap<String, Object> total = new HashMap<String, Object>();
		List<ExpressionModel> models = this.getExpressionModelList();
		ExpressionModel model = new ExpressionModel();
		if (itemId < 0) {
			for (ExpressionModel expressionModel : models) {
				if (expressionModel.getId().equals(defItemId)) {
					model = expressionModel;
					break;
				}
			}
		} else {
			model = models.get(itemId);
		}

		// Grid 设置
		List<ExpressionParam> params = model.getParams();
		List<Map<String, Object>> datalist = new ArrayList<Map<String, Object>>();
		String[] titles = { "NAME", "VALUE", "DESC" };
		String[] defValues = {};

		if (eId != null && eId.trim().length() > 0 && eId.indexOf("(") != -1) {
			defValues = eId.substring(eId.indexOf("(")).replace("(", "")
					.replace(")", "").replace("'", "").split(",");
		}
		int defCount = 0;

		// Grid 传值
		if (params != null && params.size() > 0) {
			for (ExpressionParam param : params) {

				Map<String, Object> map = new HashMap<String, Object>();

				map.put(titles[0],
						param.getName() == null ? "" : param.getName());

				if (defValues.length == params.size()) {
					map.put(titles[1], defValues[defCount].trim());
					defCount++;
				} else {
					map.put(titles[1], "");
				}

				map.put(titles[2],
						param.getDesc() == null ? "" : param.getDesc());
				datalist.add(map);
			}
			total.put("total", params.size());
			total.put("curPageNo", 1);
			total.put("pageSize", 20);
			total.put("totalPages", 1);
			total.put("totalRecords", params.size());
			total.put("dataRows", datalist);
		}

		JSONArray json = JSONArray.fromObject(total);
		return json.toString();
	}

	/**
	 * 获取参数类型
	 * 
	 * @param getParamsEId
	 * @return
	 */
	public String getParamsType(String getParamsEId) {

		StringBuffer sb = new StringBuffer();

		List<ExpressionModel> models = this.getExpressionModelList();
		ExpressionModel model = new ExpressionModel();
		for (ExpressionModel expressionModel : models) {
			if (expressionModel.getId().equals(getParamsEId)) {
				model = expressionModel;
				break;
			}
		}

		List<ExpressionParam> params = model.getParams();
		if (params != null && params.size() > 0) {
			int flag = 0;
			for (ExpressionParam param : params) {
				if (flag != 0) {
					sb.append("|");
				}
				sb.append(param.getType());
				flag++;
			}
		}
		return sb.toString();
	}

	// AAAAAAAAAAAAAAAAAAABAgMAAAECAwA=

}
