package com.ibpmsoft.project.xiaomi.trigger;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;

import com.iwork.connection.ConnectionFactory;
import com.iwork.connection.ConnectionInterface;
import com.iwork.connection.constants.ConnectionConstants;
import com.iwork.connection.impl.ConnectionSAPImpl;
import com.iwork.connection.plugs.sap.jco.RfcManager;
import com.iwork.core.engine.constant.EngineConstants;
import com.iwork.core.mq.util.MessageQueueUtil;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.util.ResponseUtil;
import com.iwork.process.runtime.pvm.trigger.ProcessStepTriggerEvent;
import com.iwork.process.runtime.pvm.trigger.ProcessTriggerEvent;
import com.iwork.sdk.ProcessAPI;
import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoParameterList;
import com.sap.conn.jco.JCoTable;
import org.apache.log4j.Logger;
/**
 * 设置订单为有效状态
 * @author davidyang
 *
 */
public class ProcessTriggerSetSapEvent extends ProcessTriggerEvent {
	private static Logger logger = Logger.getLogger(ProcessTriggerSetSapEvent.class);
	private UserContext _me;
	private HashMap params; 
	public ProcessTriggerSetSapEvent(UserContext me,HashMap hash){
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
				List<HashMap> returnParam = impl.execute("8834d37f49274f9ea16ba35ccbff5c3c",formData);
				for(HashMap hash:returnParam){
					if(hash.get("E_RET")!=null){
						String rvalue = hash.get("E_RET").toString();
						if(rvalue!=null&&rvalue.equals("SUCCESS")){
							flag = true;
							break;
						}
					}
				}
			}catch(Exception e){logger.error(e,e);
				MessageQueueUtil.getInstance().putAlertMsg("同步SAP失败");
			}
		}
		return flag;
	}
	
}
