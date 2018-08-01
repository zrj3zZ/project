package com.iwork.plugs.contract.event;

import java.util.HashMap;
import java.util.List;

import com.ibm.icu.math.BigDecimal;
import com.iwork.core.mq.util.MessageQueueUtil;
import com.iwork.core.organization.context.UserContext;
import com.iwork.plugs.contract.model.ContractBaseInfo;
import com.iwork.process.runtime.pvm.trigger.ProcessStepTriggerEvent;
import com.iwork.sdk.DemAPI;
import org.apache.log4j.Logger;
public class IWorkContractFormValidateEvent extends ProcessStepTriggerEvent {
	private static final String baseUUID = "0d48d769d5734df3b94c64421440c770";  //合同基本信息主数据模型UUID
	private static final String planListUUID = "525e3a9555494ef5a052c0386a17802c";//合同计划列表主数据模型UUID
	private static final String IvoicePlanListUUID = "f76af806ee7740c19eae9be7fd0a14ec";//发票计划列表主数据模 型UUID
	private static final String deliveryPlanUUID = "f1e455a42a294d80ac99d09f4fcd7baf";  //交货计划列表主数据模型UUID
	private static final String contractPerformUUID = "c58e802fc13b499f91c7b774defe27a3";  //合同执行情况列表主数据模型UUID
	private static final String ivoicePerformUUID = "dde36307fba544c582476c92e11e280f";  //发票执行情况列表主数据模型UUID
	private static final String deliveryPerformUUID= "3b8e530169a04e9b8b0f609315cd8e52";  //交货执行情况列表主数据模型UUID
	private static final String optionsUUID = "04e81240eee5446cab7fd6ecbf42d558";//权限设置主数据模型UUID
	private static Logger logger = Logger.getLogger(IWorkContractFormValidateEvent.class);
	public IWorkContractFormValidateEvent(UserContext uc, HashMap hash) {
		super(uc, hash);
	}
	public boolean execute() {
		boolean flag=true;
		//获取表单数据
		HashMap hash=this.getFormData();
		//獲取合同编号
		String pactNo= cStrArrToSingleStr(hash.get("PACTNO"));
		ContractBaseInfo model = new ContractBaseInfo();
		Long instanceId = this.getInstanceId();
		Long formId=this.getFormId();
		//获取页面的所填写的金额
		String amounts= cStrArrToSingleStr(hash.get("AMOUNT"));
		Long amount = Long.parseLong(amounts);
		List<HashMap> baseList = null;
		HashMap baseConditionMap=new HashMap();
		baseConditionMap.put("PACTNO", pactNo);
		//获取合同基本信息表中的总金额
		baseList =  DemAPI.getInstance().getList(baseUUID, baseConditionMap, null);		
		Long sum = Long.parseLong(baseList.get(0).get("PACTSUN").toString());
		//获取合同计划的总金额
		if(pactNo!=null){
		    List<HashMap> contractPlanSums=DemAPI.getInstance().getList(planListUUID, baseConditionMap, null);
		    long planNum =0l;
		    BigDecimal bd = new BigDecimal(planNum);
		      for(HashMap hashContractPlan:contractPlanSums){
			      if(hashContractPlan.get("AMOUNT")!=null){
				      Long tmp = Long.parseLong(hashContractPlan.get("AMOUNT").toString());
				   bd=bd.add(new BigDecimal(tmp));
			      }
		     }
		      model.setPlanNum(bd.longValue());
		  }
		//获取发票计划金额
		if(pactNo!=null){
		    List<HashMap> invoidcePlanSums=DemAPI.getInstance().getList(IvoicePlanListUUID, baseConditionMap, null);
		    long planNum =0l;
		    BigDecimal bd = new BigDecimal(planNum);
		      for(HashMap hashInvoicePlan:invoidcePlanSums){
			      if(hashInvoicePlan.get("AMOUNT")!=null){
				      Long tmp = Long.parseLong(hashInvoicePlan.get("AMOUNT").toString());
				   bd=bd.add(new BigDecimal(tmp));
			      }
		     }
		      model.setTicketPlanNum(bd.longValue());
		  }
		//获取合同执行总金额
		if(pactNo!=null){
		    List<HashMap> contractPerformSums=DemAPI.getInstance().getList(contractPerformUUID, baseConditionMap, null);
		    long planNum =0l;
		    BigDecimal bd = new BigDecimal(planNum);
		      for(HashMap hashContractPerform:contractPerformSums){
			      if(hashContractPerform.get("AMOUNT")!=null){
				      Long tmp = Long.parseLong(hashContractPerform.get("AMOUNT").toString());
				   bd=bd.add(new BigDecimal(tmp));
			      }
		     }
		      model.setRealNum(bd.longValue());
		  }
		//获取合同执行总金额
		if(pactNo!=null){
		    List<HashMap> invoicePerformSums=DemAPI.getInstance().getList(ivoicePerformUUID, baseConditionMap, null);
		    long planNum =0l;
		    BigDecimal bd = new BigDecimal(planNum);
		      for(HashMap hashIvoicePerform:invoicePerformSums){
			      if(hashIvoicePerform.get("AMOUNT")!=null){
				      Long tmp = Long.parseLong(hashIvoicePerform.get("AMOUNT").toString());
				   bd=bd.add(new BigDecimal(tmp));
			      }
		     }
		      model.setTicketNum(bd.longValue());
		  }
		long counts;
		counts=sum-model.getPlanNum()-model.getRealNum()-model.getTicketNum()-model.getTicketPlanNum();
		if(counts<amount){
			
			MessageQueueUtil.getInstance().putAlertMsg("所填金额不可以大于合同总金额");
			flag=false;
		}/*else{
			flag = ProcessAPI.getInstance().updateFormData(this.getActDefId(),
					this.getInstanceId(), hash,
					CalculateUtil.changeTypeToLong(cStrArrToSingleStr(hash.get("ID"))), false,
					EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
		}*/
		return flag;
	}
	
	public String cStrArrToSingleStr(Object formMap){
		String strValue = "";
		String[] strArr ;
		if(formMap!=null && formMap.getClass().isArray()){
			try{
				strArr = (String[])formMap;
				strValue = strArr[0];
			}catch(Exception e){
				logger.error(e,e);
			}
		}
		return strValue;
	} 
}
