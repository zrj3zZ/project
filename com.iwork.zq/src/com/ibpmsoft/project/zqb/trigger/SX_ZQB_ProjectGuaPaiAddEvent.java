package com.ibpmsoft.project.zqb.trigger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.ibpmsoft.project.zqb.service.ZqbUpdateDataService;
import com.ibpmsoft.project.zqb.util.ConfigUtil;
import com.iwork.app.log.util.LogUtil;
import com.iwork.commons.util.ParameterMapUtil;
import com.iwork.core.db.DBUtil;
import com.iwork.core.engine.constant.EngineConstants;
import com.iwork.core.engine.dem.trigger.DemTriggerEvent;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.util.SpringBeanUtil;
import com.iwork.sdk.DemAPI;
import com.iwork.sdk.ProcessAPI;

public class SX_ZQB_ProjectGuaPaiAddEvent  extends DemTriggerEvent{
	
	private final static String CN_FILENAME = "/common.properties";// 抓取网站配置文件
	private static String KHUUID = "a243efd832bf406b9caeaec5df082e28";
	private ZqbUpdateDataService zqbUpdateDataService;
	
	public SX_ZQB_ProjectGuaPaiAddEvent(UserContext me,HashMap hash){
		super(me,hash);
	}
	/**
	 * 执行触发器
	 */
	public boolean execute(){
		boolean updateFormData = false;
		HashMap formData = this.getFormData();
		HashMap map = ParameterMapUtil.getParameterMap(formData);
		if (map != null) {
			String customerno = map.get("CUSTOMERNO").toString();
			//项目股改
			Object xmggzl = map.get("XMGGZL");
			if(xmggzl!=null&&!"".equals(xmggzl.toString())){
				Long lcggInsId = DBUtil.getLong("SELECT LCBS FROM BD_ZQB_LCGG WHERE CUSTOMERNO='"+customerno+"'","LCBS");
				HashMap<String,Object> processData = ProcessAPI.getInstance().getFromData(lcggInsId, EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
				if(processData!=null&&!processData.isEmpty()){
					String fj=processData.get("FJ")==null?"":processData.get("FJ").toString();
					if(!xmggzl.toString().equals(fj)){
						processData.put("FJ", xmggzl.toString());
						Long lcggId = Long.parseLong(processData.get("ID").toString());
						updateFormData = ProcessAPI.getInstance().updateFormData(processData.get("LCBH").toString(), lcggInsId, processData, lcggId, false, EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
					}
				}
			}
			//项目内核 SQNHZL
			Object xmnhzl = map.get("SQNHZL");
			if(xmnhzl!=null&&!"".equals(xmnhzl.toString())){
				Long xmnhInsId = DBUtil.getLong("SELECT LCBS FROM BD_ZQB_SQNH WHERE CUSTOMERNO='"+customerno+"'","LCBS");
				HashMap<String,Object> processData = ProcessAPI.getInstance().getFromData(xmnhInsId, EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
				if(processData!=null&&!processData.isEmpty()){
					String fj=processData.get("FJ")==null?"":processData.get("FJ").toString();
					if(!xmnhzl.toString().equals(fj)){
						processData.put("FJ", xmnhzl.toString());
						Long xmnhId = Long.parseLong(processData.get("ID").toString());
						updateFormData = ProcessAPI.getInstance().updateFormData(processData.get("LCBH").toString(), xmnhInsId, processData, xmnhId, false, EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
					}
				}
			}
			//内核反馈 NHFKZL
			Object nhfkzl = map.get("NHFKZL");
			if(nhfkzl!=null&&!"".equals(nhfkzl.toString())){
				Long nhfkInsId = DBUtil.getLong("SELECT LCBS FROM BD_ZQB_NHFK WHERE CUSTOMERNO='"+customerno+"'","LCBS");
				HashMap<String,Object> processData = ProcessAPI.getInstance().getFromData(nhfkInsId, EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
				if(processData!=null&&!processData.isEmpty()){
					String fj=processData.get("FJ")==null?"":processData.get("FJ").toString();
					if(!nhfkzl.toString().equals(fj)){
						processData.put("FJ", nhfkzl.toString());
						Long nhfkId = Long.parseLong(processData.get("ID").toString());
						updateFormData = ProcessAPI.getInstance().updateFormData(processData.get("LCBH").toString(), nhfkInsId, processData, nhfkId, false, EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
					}
				}
			}
			String xmlxuuid = ConfigUtil.readValue("/common.properties", "xmlxuuid");
			HashMap<String,String> xmlxMap = new HashMap<String,String>();
			xmlxMap.put("CUSTOMERNO", customerno);
			List<HashMap> xmlxList = DemAPI.getInstance().getList(xmlxuuid, xmlxMap, null);
			if(xmlxList!=null&&xmlxList.size()>=1){
				boolean flag=false;
				HashMap lxMap = xmlxList.get(0);
				String zclr = lxMap.get("ZCLR")==null?"":lxMap.get("ZCLR").toString();
				String xmclr = map.get("XMCLR")==null?"":map.get("XMCLR").toString();
				if(!xmclr.equals(zclr)){
					flag=true;
					lxMap.put("ZCLR", xmclr);
				}
				String owner = lxMap.get("OWNER")==null?"":lxMap.get("OWNER").toString();
				String dqfzr = map.get("DQFZR")==null?"":map.get("DQFZR").toString();
				if(!dqfzr.equals(owner)){
					flag=true;
					lxMap.put("OWNER", dqfzr);
				}
				String manager = lxMap.get("MANAGER")==null?"":lxMap.get("MANAGER").toString();
				String xmfzr = map.get("XMFZR")==null?"":map.get("XMFZR").toString();
				if(!xmfzr.equals(manager)){
					flag=true;
					lxMap.put("MANAGER", xmfzr);
				}
				String lxddap = lxMap.get("DDAP")==null?"":lxMap.get("DDAP").toString();
				String gpddap = map.get("DDAP")==null?"":map.get("DDAP").toString();
				if(!gpddap.equals(lxddap)){
					flag=true;
					lxMap.put("DDAP", gpddap);
				}
				String xmzh = lxMap.get("XMZH")==null?"":lxMap.get("XMZH").toString();
				String xmzk = map.get("XMZK")==null?"":map.get("XMZK").toString();
				if(!xmzk.equals(xmzh)){
					flag=true;
					lxMap.put("XMZH", xmzk);
				}
				String xmxy = lxMap.get("XMXY")==null?"":lxMap.get("XMXY").toString();
				String xmhy = map.get("XMHY")==null?"":map.get("XMHY").toString();
				if(!xmhy.equals(xmxy)){
					flag=true;
					lxMap.put("XMXY", xmhy);
				}
				String lxls = lxMap.get("XMLS")==null?"":lxMap.get("XMLS").toString();
				String gpls = map.get("XMLS")==null?"":map.get("XMLS").toString();
				if(!gpls.equals(lxls)){
					flag=true;
					lxMap.put("XMLS", gpls);
				}
				if(flag){
					Long lxInsId = Long.parseLong(lxMap.get("INSTANCEID").toString());
					Long lxId = Long.parseLong(lxMap.get("ID").toString());
					DemAPI.getInstance().updateFormData(xmlxuuid, lxInsId, lxMap, lxId, false);
				}
			}
			HashMap<String,String> khMap = new HashMap<String,String>();
			khMap.put("CUSTOMERNO", customerno);
			List<HashMap> khList = DemAPI.getInstance().getList(KHUUID, khMap, null);
			if(khList!=null&&khList.size()>=1){
				boolean flag=false;
				HashMap khListMap = khList.get(0);
				HashMap oldListMap = new HashMap();
				oldListMap.put("CUSTOMERNO", customerno);
				oldListMap.put("CUSTOMERNAME", khListMap.get("CUSTOMERNAME"));
				oldListMap.put("ZQJC", khListMap.get("ZQJC"));
				oldListMap.put("ZQDM", khListMap.get("ZQDM"));
				String customername = khListMap.get("CUSTOMERNAME")==null?"":khListMap.get("CUSTOMERNAME").toString();
				String gsmc = map.get("GSMC")==null?"":map.get("GSMC").toString();
				if(!gsmc.equals(customername)){
					flag=true;
					khListMap.put("CUSTOMERNAME", gsmc);
				}
				String khzqdm = khListMap.get("ZQDM")==null?"":khListMap.get("ZQDM").toString();
				String gpzqdm = map.get("ZQDM")==null?"":map.get("ZQDM").toString();
				if(!gpzqdm.equals(khzqdm)){
					flag=true;
					khListMap.put("ZQDM", gpzqdm);
				}
				String khzqjc = khListMap.get("ZQJC")==null?"":khListMap.get("ZQJC").toString();
				String gpzqjc = map.get("ZQJC")==null?"":map.get("ZQJC").toString();
				if(!gpzqjc.equals(khzqjc)){
					flag=true;
					khListMap.put("ZQJC", gpzqjc);
				}
				String username = khListMap.get("USERNAME")==null?"":khListMap.get("USERNAME").toString();
				String khlxr = map.get("KHLXR")==null?"":map.get("KHLXR").toString();
				if(!khlxr.equals(username)){
					flag=true;
					khListMap.put("USERNAME", khlxr);
				}
				String tel = khListMap.get("TEL")==null?"":khListMap.get("TEL").toString();
				String khlxdh = map.get("KHLXDH")==null?"":map.get("KHLXDH").toString();
				if(!khlxdh.equals(tel)){
					flag=true;
					khListMap.put("TEL", khlxdh);
				}
				String khEmail = khListMap.get("EMAIL")==null?"":khListMap.get("EMAIL").toString();
				String gpEmail = map.get("EMAIL")==null?"":map.get("EMAIL").toString();
				if(!gpEmail.equals(khEmail)){
					flag=true;
					khListMap.put("EMAIL", gpEmail);
				}
				if(flag){
					Long khInsId = Long.parseLong(khListMap.get("INSTANCEID").toString());
					Long khId = Long.parseLong(khListMap.get("ID").toString());
					DemAPI.getInstance().updateFormData(xmlxuuid, khInsId, khListMap, khId, false);
					if(zqbUpdateDataService==null){
						zqbUpdateDataService = (ZqbUpdateDataService)SpringBeanUtil.getBean("zqbUpdateDataService");
					}
					zqbUpdateDataService.updateDataCustomer(oldListMap, khListMap);
				}
			}
			HashMap hash = DemAPI.getInstance().getFromData(this.getInstanceId(),EngineConstants.SYS_INSTANCE_TYPE_DEM);
			Long dataId = Long.parseLong(hash.get("ID").toString());
			String gsmc = hash.get("GSMC").toString();
			UserContext uc = this.getUserContext();
			if (!"0".equals(map.get("instanceId").toString())) {
				LogUtil.getInstance().addLog(dataId, "项目挂牌信息维护",gsmc + "修改项目挂牌信息。");
			} else {
				LogUtil.getInstance().addLog(dataId, "项目挂牌信息维护",gsmc + "添加项目挂牌信息。");
			}
		}
		return true;
	}
	
	public Long getConfig(String parameter) {
		Map<String,String> config=ConfigUtil.readAllProperties(CN_FILENAME);//获取连接网址配置
		long result=0L;
		if(parameter!=null&&!"".equals(parameter)){
			result=Long.parseLong(config.get(parameter)==null?"0":config.get(parameter));
		}
		return result;
	}
	
	public String getConfigUUID(String parameter) {
		Map<String,String> config=ConfigUtil.readAllProperties(CN_FILENAME);//获取连接网址配置
		String result="";
		if(parameter!=null&&!"".equals(parameter)){
			result=config.get(parameter)==null?"":config.get(parameter);
		}
		return result;
	}
	
}
