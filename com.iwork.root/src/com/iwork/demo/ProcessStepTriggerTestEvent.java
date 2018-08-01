package com.iwork.demo;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import com.iwork.connection.impl.ConnectionSAPImpl;
import com.iwork.connection.plugs.sap.jco.RfcManager;
import com.iwork.core.mq.util.MessageQueueUtil;
import com.iwork.core.organization.context.UserContext;
import com.iwork.process.runtime.pvm.trigger.ProcessStepTriggerEvent;
/**
 * 节点触发器
 * @author davidyang
 *
 */
public class ProcessStepTriggerTestEvent extends ProcessStepTriggerEvent {
	private static Logger logger = Logger.getLogger(ProcessStepTriggerTestEvent.class);

	private UserContext _me;
	private HashMap params;
	public ProcessStepTriggerTestEvent(UserContext me,HashMap hash){
		super(me,hash);
		_me = me;
		params = hash;
	}
	public boolean execute() {
		if(params!=null){
			try{
				boolean flag = RfcManager.ping();
				if(flag){
					MessageQueueUtil.getInstance().putAlertMsg("SAP 链接成功");
				} 
				ConnectionSAPImpl impl = new ConnectionSAPImpl();
				HashMap params = new HashMap();
				params.put("IP_PERNR","00003927");
				params.put("IP_BUKRS","8300");
				List<HashMap> list = impl.execute("8834d37f49274f9ea16ba35ccbff5c3c", params);
				for(HashMap returnData:list){
					Iterator iterator = returnData.entrySet().iterator();
					while(iterator.hasNext()){
						java.util.Map.Entry entry = (java.util.Map.Entry)iterator.next();
					}
				}
			}catch(Exception e){logger.error(e,e);}			
		} 
		return true;
	}
	
}
