package com.ibpmsoft.project.xiaomi.trigger;

import java.util.HashMap;
import com.iwork.connection.ConnectionFactory;
import com.iwork.connection.ConnectionInterface;
import com.iwork.connection.constants.ConnectionConstants;
import com.iwork.core.engine.constant.EngineConstants;
import com.iwork.core.organization.context.UserContext;
import com.iwork.process.runtime.pvm.trigger.ProcessStepTriggerEvent;
import com.iwork.sdk.ProcessAPI;
import org.apache.log4j.Logger;
public class ProcessStepTriggerSetSapEvent extends ProcessStepTriggerEvent {
	private static Logger logger = Logger.getLogger(ProcessStepTriggerSetSapEvent.class);
	private UserContext _me;
	private HashMap params;
	public ProcessStepTriggerSetSapEvent(UserContext me,HashMap hash){
		super(me,hash);
		_me = me;
		params = hash;
	}
	public boolean execute() {
		boolean flag = false;
		if(params!=null){
			try{
				//获取SAP连接对象
				ConnectionInterface impl = ConnectionFactory.getConnectionInterface(ConnectionConstants.CONN_PLUGS_TYPE_SAP);
				HashMap returnData = new HashMap(); 
				Long instanceId = this.getInstanceId();
				HashMap formData = ProcessAPI.getInstance().getFromData(instanceId, EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
				impl.execute("8834d37f49274f9ea16ba35ccbff5c3c",formData); 
			}catch(Exception e){logger.error(e,e);}
		}
		return flag;
	}
	
}
