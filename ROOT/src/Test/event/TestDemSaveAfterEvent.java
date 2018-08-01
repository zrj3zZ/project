package Test.event;

import java.util.HashMap;


import com.iwork.core.engine.dem.trigger.DemTriggerEvent;
import com.iwork.core.organization.context.UserContext;

/**
 * 主数据模型保存后触发器
 * @author zouyalei
 *
 */
public class TestDemSaveAfterEvent extends DemTriggerEvent {

	public static final String DEM_UUID = "d149e54941b645ea95a93f4c4f248a78";
	public static final String SUBFORM_KEY = "SUBFORM_JTCYXX";
	
	private UserContext _uc;
	private HashMap<String,Object> _hash;
	public TestDemSaveAfterEvent(UserContext uc,HashMap<String,Object> hash){
		super(uc,hash);
		_uc = uc;
		_hash = hash;
	}
	
	public boolean execute(){
		
		this.getFormData();// 获取当前表单数据
		Long instanceId = this.getInstanceId(); // 获取Instanceid
		this.getFormId(); // 获取Formid
		this.getUserContext(); // 获取上下文关系
		this.getTableName();// 获取表明
		
//		HashMap<String,Object> hashdata = new HashMap<String,Object>();
//		List<HashMap> list = new ArrayList<HashMap>();
//		Long dataid = 0L;
//		
//		// 获取主数据模型
//		SysDemEngine engine = DemAPI.getInstance().getDemModel(DEM_UUID);
//		// 新增表单数据
//		DemAPI.getInstance().saveFormData(DEM_UUID, instanceId, hashdata, false);
//		// 新增子表单数据
//		DemAPI.getInstance().saveFormDatas(DEM_UUID, instanceId, SUBFORM_KEY, list, false, EngineConstants.SYS_INSTANCE_TYPE_DEM);
//		// 更新表单数据
//		DemAPI.getInstance().updateFormData(DEM_UUID, instanceId, hashdata, dataid, false);
//		// 更新子表单数据
//		Long demId = engine.getId();
//		DemAPI.getInstance().updateSubFormData(demId, SUBFORM_KEY, instanceId, hashdata, false);
//		// 查询表单数据
//		HashMap<String,Object> conditionMap = new HashMap<String,Object>();
//		DemAPI.getInstance().getList(DEM_UUID, conditionMap, "ID");
//		// 查询表单数据带分页
//		int pageSize = 1;
//		int startRow = 1;
//		DemAPI.getInstance().getList(DEM_UUID, conditionMap, "ID", pageSize, startRow);
//		// 根据Instanceid查询单条记录
//		DemAPI.getInstance().getFromData(instanceId, EngineConstants.SYS_INSTANCE_TYPE_DEM);
//		// 根据Instanceid与formKey查询子表记录
//		DemAPI.getInstance().getFromSubData(instanceId, SUBFORM_KEY, EngineConstants.SYS_INSTANCE_TYPE_DEM);
		System.out.println("这是第一个触发器，关于主数据模型保存后触发！");
		// 返回true则成功，返回fasle则不成功，可以根据具体业务判断返回true或者false
		return true;
	}
}
