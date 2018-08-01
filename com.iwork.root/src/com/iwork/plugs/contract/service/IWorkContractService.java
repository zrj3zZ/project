package com.iwork.plugs.contract.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.ibm.icu.math.BigDecimal;
import com.iwork.commons.util.ObjectUtil;
import com.iwork.core.db.DBUtil;
import com.iwork.core.engine.dem.model.SysDemEngine;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.core.upload.model.FileUpload;
import com.iwork.plugs.contract.model.ContractBaseInfo;
import com.iwork.plugs.fi.util.CalculateUtil;
import com.iwork.sdk.DemAPI;
import com.iwork.sdk.FileUploadAPI;
import com.iwork.sdk.ProcessAPI;
import org.apache.log4j.Logger;

public class IWorkContractService {	
	private static Logger logger = Logger.getLogger(IWorkContractService.class);
	private static final String baseUUID = "0d48d769d5734df3b94c64421440c770";  //合同基本信息主数据模型UUID
	private static final String planListUUID = "525e3a9555494ef5a052c0386a17802c";//合同计划列表主数据模型UUID
	private static final String IvoicePlanListUUID = "f76af806ee7740c19eae9be7fd0a14ec";//发票计划列表主数据模 型UUID
	private static final String deliveryPlanUUID = "f1e455a42a294d80ac99d09f4fcd7baf";  //交货计划列表主数据模型UUID
	private static final String contractPerformUUID = "c58e802fc13b499f91c7b774defe27a3";  //合同执行情况列表主数据模型UUID
	private static final String ivoicePerformUUID = "dde36307fba544c582476c92e11e280f";  //发票执行情况列表主数据模型UUID
	private static final String deliveryPerformUUID= "3b8e530169a04e9b8b0f609315cd8e52";  //交货执行情况列表主数据模型UUID
	private static final String optionsUUID = "04e81240eee5446cab7fd6ecbf42d558";//权限设置主数据模型UUID
	private static final String htzxsubformkey = "SUBFORM_HTZXJHSPB";//合同执行计划审批子表
	private static final String htfpsubformkey = "SUBFORM_HTFPJHSQB";//合同发票计划审批子表
	//获取预览表中的金额数据
	public ContractBaseInfo getPlanListContractInfo(String itemno){
		ContractBaseInfo model = new ContractBaseInfo();
		if(itemno!=null){
			List<HashMap> baseList = null;
			HashMap baseConditionMap=new HashMap();
			baseConditionMap.put("PACTNO", itemno);
			baseList =  DemAPI.getInstance().getList(baseUUID, baseConditionMap, null);
			for(HashMap hashmap:baseList){
				//hashmap.get("PACTTYPE");
				model.setCurrencyString(hashmap.get("CURRENCY").toString());
				if(hashmap.get("CURRENCY").equals("1")){
					model.setCurrencyString("人民币");
				}else if(hashmap.get("CURRENCY").equals("2")){
					model.setCurrencyString("美元");
				}else if(hashmap.get("CURRENCY").equals("3")){
					model.setCurrencyString("英镑");
				}else if(hashmap.get("CURRENCY").equals("4")){
					model.setCurrencyString("港币");
				}else if(hashmap.get("CURRENCY").equals("5")){
					model.setCurrencyString("欧元");
				}
				if(Integer.parseInt(hashmap.get("PACTTYPE").toString())==1){
					model.setPlanCollectionString("计划收款金额");
					model.setInvoicePlanCollectionString("计划开票金额");
					model.setActualCollectionString("实际收款金额");
					model.setInvoicePerformCollectionString("实际开票金额");
					model.setShouldString("应收款金额");
					model.setInvoiceBalanceString("开票剩余金额");
				}else{
					model.setPlanCollectionString("计划付款金额");
					model.setInvoicePlanCollectionString("计划收票金额");
					model.setActualCollectionString("实际付款金额");
					model.setInvoicePerformCollectionString("实际收票金额");
					model.setShouldString("应付款金额");
					model.setInvoiceBalanceString("收票剩余金额");
				}					
			}
		}
		
		//根据表单模型取出表单formid和demid
		SysDemEngine sde = DemAPI.getInstance().getDemModel(planListUUID);		
		if(sde!=null){
			//存储formid
			model.setFormId(sde.getFormid());
			//存储demid
			model.setDemId(sde.getId());
		}	
		//合同总金额
		HashMap conditionMap1 = new HashMap();
		conditionMap1.put("PACTNO", itemno);
		List<HashMap> baselist =  DemAPI.getInstance().getList(baseUUID, conditionMap1, null);
		if(baselist!=null&&baselist.size()>0){
			HashMap baseInfo = baselist.get(0);
			if(baseInfo!=null){
				Object obj = baseInfo.get("PACTSUN");
				String sumNum = ObjectUtil.getString(obj);
				model.setSumNum(sumNum);
			}
		}
		
		//合计计划总金额
		if(itemno!=null){
			List<HashMap> list = null;
			HashMap conditionMap = new HashMap();
			conditionMap.put("PACTNO", itemno);
			list =  DemAPI.getInstance().getList(planListUUID, conditionMap, null);
			long planNum =0l;
			BigDecimal bd = new BigDecimal(planNum);
			for(HashMap hash:list){
				if(hash.get("AMOUNT")!=null){
					Long tmp = Long.parseLong(hash.get("AMOUNT").toString());
					bd=bd.add(new BigDecimal(tmp));
				}
			}
			model.setPlanNum(bd.longValue());
		}
		//发票计划总金额
		if(itemno!=null){
			List<HashMap> list = null;
			HashMap conditionMap = new HashMap();
			conditionMap.put("PACTNO", itemno);
			list =  DemAPI.getInstance().getList(IvoicePlanListUUID, conditionMap, null);
			long planNum =0l;
			BigDecimal bd = new BigDecimal(planNum);
			//将子记录金额加和
			for(HashMap hash:list){
				if(hash.get("AMOUNT")!=null){
					Long tmp = Long.parseLong(hash.get("AMOUNT").toString());
					bd=bd.add(new BigDecimal(tmp));
				}
			}
			model.setTicketPlanNum(bd.longValue());
		}
		
		//实际收/付款总金额
		if(itemno!=null){
			List<HashMap> list = null;
			HashMap conditionMap = new HashMap();
			conditionMap.put("PACTNO", itemno);
			list =  DemAPI.getInstance().getList(contractPerformUUID, conditionMap, null);
			long planNum =0l;
			BigDecimal bd = new BigDecimal(planNum);
			//将子记录金额加和
			for(HashMap hash:list){
				if(hash.get("AMOUNT")!=null){
					Long tmp = Long.parseLong(hash.get("AMOUNT").toString());
					bd=bd.add(new BigDecimal(tmp));
				}
			}
			model.setRealNum(bd.longValue());		
		}
		   //發票執行总金额
		if(itemno!=null){
			List<HashMap> list = null;
			HashMap conditionMap = new HashMap();
			conditionMap.put("PACTNO", itemno);
			list =  DemAPI.getInstance().getList(ivoicePerformUUID, conditionMap, null);
			long planNum =0l;
			BigDecimal bd = new BigDecimal(planNum);
			//将子记录金额加和
			for(HashMap hash:list){
				if(hash.get("AMOUNT")!=null){
					Long tmp = Long.parseLong(hash.get("AMOUNT").toString());
					bd=bd.add(new BigDecimal(tmp));
				}
			}
			model.setTicketNum(bd.longValue());	
		}	
		if(baselist!=null&&baselist.size()>0){
			HashMap baseInfo = baselist.get(0);
			if(baseInfo!=null){
				Object obj = baseInfo.get("PACTSUN");
				long sumNum = Long.parseLong(obj.toString());
				model.setInvoiceBalance(sumNum-model.getTicketNum());
			}
		}
		return model;		
	}
	/**
	 * 获取合同Demid和Formid
	 * 
	 */
    public HashMap getDemidAndFormid(){
    	HashMap<String,Object> DemidandFormid = new HashMap<String,Object>();
    	SysDemEngine sde = DemAPI.getInstance().getDemModel(baseUUID);
		if(sde!=null){
			DemidandFormid.put("Formid", sde.getFormid());
			DemidandFormid.put("Demid",sde.getId());
		}		
		return DemidandFormid;
    }
	/**
	 * 获取设置表Demid和Formid
	 * 
	 */
    public HashMap getOptionsDemidAndFormid(){
    	HashMap<String,Object> DemidandFormid = new HashMap<String,Object>();
    	SysDemEngine sde = DemAPI.getInstance().getDemModel(optionsUUID);
		if(sde!=null){
			DemidandFormid.put("Formid", sde.getFormid());
			DemidandFormid.put("Demid",sde.getId());
		}		
		return DemidandFormid;
    }
	/**
	 * 获取合同计划执行列表
	 * @param itemno
	 * @return
	 */
	public List<HashMap> getPlanList(String itemno){
		List<HashMap> list = null;
		if(itemno!=null){
			HashMap conditionMap = new HashMap();
			conditionMap.put("PACTNO", itemno);
			list =  DemAPI.getInstance().getList(planListUUID, conditionMap, null);
		}
		  if(list!=null&&list.size()>0){
				
				for(HashMap hash:list){
					Object obj = hash.get("ATTACHMENT");
					if(obj!=null){
						StringBuffer attachhtml = new StringBuffer();
						attachhtml.append("<ul class=\"attachDiv\">");
						String attachstr = ObjectUtil.getString(obj);
						String[] attachlist = attachstr.split(",");
						for(String tmp:attachlist){
							if(tmp.trim().equals(""))continue;
							FileUpload fu = FileUploadAPI.getInstance().getFileUpload(tmp);
							if(fu!=null){
								String filename = fu.getFileSrcName();
								if(filename.length()>15){
									filename = filename.substring(0,15)+"...";
								}
								attachhtml.append("<li><a href=\"uploadifyDownload.action?fileUUID=").append(fu.getFileId()).append("\" target=\"_blank\"><img src=\"iwork_img/attach.gif\">").append(filename).append("</a></li>");
							}
						}
						attachhtml.append("</ul>");
						hash.put("ATTACHMENT", attachhtml.toString());
					}
				}				
			}
		return list;
	}

	
	/**
	 * 获取发票计划表预览数据
	 * @param itemno
	 * @return
	 */	
	public ContractBaseInfo getIvoicePlanListContractInfo(String itemno){
		ContractBaseInfo model = new ContractBaseInfo();
		if(itemno!=null){
			List<HashMap> baseList = null;
			HashMap baseConditionMap=new HashMap();
			baseConditionMap.put("PACTNO", itemno);
			baseList =  DemAPI.getInstance().getList(baseUUID, baseConditionMap, null);
			for(HashMap hashmap:baseList){
				hashmap.get("PACTTYPE");
				model.setCurrencyString(hashmap.get("CURRENCY").toString());
				if(hashmap.get("CURRENCY").equals("1")){
					model.setCurrencyString("人民币");
				}else if(hashmap.get("CURRENCY").equals("2")){
					model.setCurrencyString("美元");
				}else if(hashmap.get("CURRENCY").equals("3")){
					model.setCurrencyString("英镑");
				}else if(hashmap.get("CURRENCY").equals("4")){
					model.setCurrencyString("港币");
				}else if(hashmap.get("CURRENCY").equals("5")){
					model.setCurrencyString("欧元");
				}
				if(Integer.parseInt(hashmap.get("PACTTYPE").toString())==1){
					model.setPlanCollectionString("计划收款金额");
					model.setInvoicePlanCollectionString("计划开票金额");
					model.setActualCollectionString("实际收款金额");
					model.setInvoicePerformCollectionString("实际开票金额");
					model.setShouldString("应收款金额");
					model.setInvoiceBalanceString("开票剩余金额");
				}else{
					model.setPlanCollectionString("计划付款金额");
					model.setInvoicePlanCollectionString("计划收票金额");
					model.setActualCollectionString("实际付款金额");
					model.setInvoicePerformCollectionString("实际收票金额");
					model.setShouldString("应付款金额");
					model.setInvoiceBalanceString("收票剩余金额");
				}					
			}
		}
		
		//根据表单模型取出表单formid和demid
		SysDemEngine sde = DemAPI.getInstance().getDemModel(IvoicePlanListUUID);
		
		if(sde!=null){
			//存储formid
			model.setFormId(sde.getFormid());
			//存储demid
			model.setDemId(sde.getId());
		}	
		//合同总金额
		HashMap conditionMap1 = new HashMap();
		conditionMap1.put("PACTNO", itemno);
		List<HashMap> baselist =  DemAPI.getInstance().getList(baseUUID, conditionMap1, null);
		if(baselist!=null&&baselist.size()>0){
			HashMap baseInfo = baselist.get(0);
			if(baseInfo!=null){
				Object obj = baseInfo.get("PACTSUN");
				String sumNum = ObjectUtil.getString(obj);
				model.setSumNum(sumNum);
			}
		}
		//合计计划总金额
		if(itemno!=null){
			List<HashMap> list = null;
			HashMap conditionMap = new HashMap();
			conditionMap.put("PACTNO", itemno);
			list =  DemAPI.getInstance().getList(planListUUID, conditionMap, null);
			long planNum =0l;
			BigDecimal bd = new BigDecimal(planNum);
			for(HashMap hash:list){
				if(hash.get("AMOUNT")!=null){
					Long tmp = Long.parseLong(hash.get("AMOUNT").toString());
					bd=bd.add(new BigDecimal(tmp));
				}
			}
			model.setPlanNum(bd.longValue());
		}
		//发票计划总金额
		if(itemno!=null){
			List<HashMap> list = null;
			HashMap conditionMap = new HashMap();
			conditionMap.put("PACTNO", itemno);
			list =  DemAPI.getInstance().getList(IvoicePlanListUUID, conditionMap, null);
			long planNum =0l;
			BigDecimal bd = new BigDecimal(planNum);
			//将子记录金额加和
			for(HashMap hash:list){
				if(hash.get("AMOUNT")!=null){
					Long tmp = Long.parseLong(hash.get("AMOUNT").toString());
					bd=bd.add(new BigDecimal(tmp));
				}
			}
			model.setTicketPlanNum(bd.longValue());
		}
		
		//实际收/付款总金额
		if(itemno!=null){
			List<HashMap> list = null;
			HashMap conditionMap = new HashMap();
			conditionMap.put("PACTNO", itemno);
			list =  DemAPI.getInstance().getList(contractPerformUUID, conditionMap, null);
			long planNum =0l;
			BigDecimal bd = new BigDecimal(planNum);
			//将子记录金额加和
			for(HashMap hash:list){
				if(hash.get("AMOUNT")!=null){
					Long tmp = Long.parseLong(hash.get("AMOUNT").toString());
					bd=bd.add(new BigDecimal(tmp));
				}
			}
			model.setRealNum(bd.longValue());		
		}
		   //發票執行总金额
		if(itemno!=null){
			List<HashMap> list = null;
			HashMap conditionMap = new HashMap();
			conditionMap.put("PACTNO", itemno);
			list =  DemAPI.getInstance().getList(ivoicePerformUUID, conditionMap, null);
			long planNum =0l;
			BigDecimal bd = new BigDecimal(planNum);
			//将子记录金额加和
			for(HashMap hash:list){
				if(hash.get("AMOUNT")!=null){
					Long tmp = Long.parseLong(hash.get("AMOUNT").toString());
					bd=bd.add(new BigDecimal(tmp));
				}
			}
			model.setTicketNum(bd.longValue());	
		}		
		if(baselist!=null&&baselist.size()>0){
			HashMap baseInfo = baselist.get(0);
			if(baseInfo!=null){
				Object obj = baseInfo.get("PACTSUN");
				long sumNum = Long.parseLong(obj.toString());
				model.setInvoiceBalance(sumNum-model.getTicketNum());
			}
		}
		return model;		
	}
	/**
	 * 获取发票计划执行列表
	 * @param itemno
	 * @return
	 */
	public List<HashMap> getIvoicePlanList(String itemno){
		List<HashMap> list = null;
		if(itemno!=null){
			HashMap conditionMap = new HashMap();
			conditionMap.put("PACTNO", itemno);
			list =  DemAPI.getInstance().getList(IvoicePlanListUUID, conditionMap, null);
		}
		  if(list!=null&&list.size()>0){				
				for(HashMap hash:list){
					Object obj = hash.get("ATTACHMENT");
					if(obj!=null){
						StringBuffer attachhtml = new StringBuffer();
						attachhtml.append("<ul class=\"attachDiv\">");
						String attachstr = ObjectUtil.getString(obj);
						String[] attachlist = attachstr.split(",");
						for(String tmp:attachlist){
							if(tmp.trim().equals(""))continue;
							FileUpload fu = FileUploadAPI.getInstance().getFileUpload(tmp);
							if(fu!=null){
								String filename = fu.getFileSrcName();
								if(filename.length()>15){
									filename = filename.substring(0,15)+"...";
								}
								attachhtml.append("<li><a href=\"uploadifyDownload.action?fileUUID=").append(fu.getFileId()).append("\" target=\"_blank\"><img src=\"iwork_img/attach.gif\">").append(filename).append("</a></li>");
							}
						}
						attachhtml.append("</ul>");
						hash.put("ATTACHMENT", attachhtml.toString());
					}
				}				
			}
		return list;
	}
	
	

	/**
	 * 获取交货计划表预览数据
	 * @param itemno
	 * @return
	 */	
	public ContractBaseInfo getDeliveryPlanContractInfo(String itemno){
		ContractBaseInfo model = new ContractBaseInfo();
		if(itemno!=null){
			List<HashMap> baseList = null;
			HashMap baseConditionMap=new HashMap();
			baseConditionMap.put("PACTNO", itemno);
			baseList =  DemAPI.getInstance().getList(baseUUID, baseConditionMap, null);
			for(HashMap hashmap:baseList){
				hashmap.get("PACTTYPE");
				model.setCurrencyString(hashmap.get("CURRENCY").toString());
				if(hashmap.get("CURRENCY").equals("1")){
					model.setCurrencyString("人民币");
				}else if(hashmap.get("CURRENCY").equals("2")){
					model.setCurrencyString("美元");
				}else if(hashmap.get("CURRENCY").equals("3")){
					model.setCurrencyString("英镑");
				}else if(hashmap.get("CURRENCY").equals("4")){
					model.setCurrencyString("港币");
				}else if(hashmap.get("CURRENCY").equals("5")){
					model.setCurrencyString("欧元");
				}
				if(Integer.parseInt(hashmap.get("PACTTYPE").toString())==1){
					model.setPlanCollectionString("计划收款金额");
					model.setInvoicePlanCollectionString("计划开票金额");
					model.setActualCollectionString("实际收款金额");
					model.setInvoicePerformCollectionString("实际开票金额");
					model.setShouldString("应收款金额");
					model.setInvoiceBalanceString("开票剩余金额");
				}else{
					model.setPlanCollectionString("计划付款金额");
					model.setInvoicePlanCollectionString("计划收票金额");
					model.setActualCollectionString("实际付款金额");
					model.setInvoicePerformCollectionString("实际收票金额");
					model.setShouldString("应付款金额");
					model.setInvoiceBalanceString("收票剩余金额");
				}					
			}
		}
		
		//根据表单模型取出表单formid和demid
		SysDemEngine sde = DemAPI.getInstance().getDemModel(deliveryPlanUUID);		
		if(sde!=null){
			//存储formid
			model.setFormId(sde.getFormid());
			//存储demid
			model.setDemId(sde.getId());
		}	
		//合同总金额
		HashMap conditionMap1 = new HashMap();
		conditionMap1.put("PACTNO", itemno);
		List<HashMap> baselist =  DemAPI.getInstance().getList(baseUUID, conditionMap1, null);
		if(baselist!=null&&baselist.size()>0){
			HashMap baseInfo = baselist.get(0);
			if(baseInfo!=null){
				Object obj = baseInfo.get("PACTSUN");
				String sumNum = ObjectUtil.getString(obj);
				model.setSumNum(sumNum);
			}
		}
		//合计计划总金额
		if(itemno!=null){
			List<HashMap> list = null;
			HashMap conditionMap = new HashMap();
			conditionMap.put("PACTNO", itemno);
			list =  DemAPI.getInstance().getList(planListUUID, conditionMap, null);
			long planNum =0l;
			BigDecimal bd = new BigDecimal(planNum);
			for(HashMap hash:list){
				if(hash.get("AMOUNT")!=null){
					Long tmp = Long.parseLong(hash.get("AMOUNT").toString());
					bd=bd.add(new BigDecimal(tmp));
				}
			}
			model.setPlanNum(bd.longValue());
		}
		//发票计划总金额
		if(itemno!=null){
			List<HashMap> list = null;
			HashMap conditionMap = new HashMap();
			conditionMap.put("PACTNO", itemno);
			list =  DemAPI.getInstance().getList(IvoicePlanListUUID, conditionMap, null);
			long planNum =0l;
			BigDecimal bd = new BigDecimal(planNum);
			//将子记录金额加和
			for(HashMap hash:list){
				if(hash.get("AMOUNT")!=null){
					Long tmp = Long.parseLong(hash.get("AMOUNT").toString());
					bd=bd.add(new BigDecimal(tmp));
				}
			}
			model.setTicketPlanNum(bd.longValue());
		}
		
		//实际收/付款总金额
		if(itemno!=null){
			List<HashMap> list = null;
			HashMap conditionMap = new HashMap();
			conditionMap.put("PACTNO", itemno);
			list =  DemAPI.getInstance().getList(contractPerformUUID, conditionMap, null);
			long planNum =0l;
			BigDecimal bd = new BigDecimal(planNum);
			//将子记录金额加和
			for(HashMap hash:list){
				if(hash.get("AMOUNT")!=null){
					Long tmp = Long.parseLong(hash.get("AMOUNT").toString());
					bd=bd.add(new BigDecimal(tmp));
				}
			}
			model.setRealNum(bd.longValue());		
		}
		   //發票執行总金额
		if(itemno!=null){
			List<HashMap> list = null;
			HashMap conditionMap = new HashMap();
			conditionMap.put("PACTNO", itemno);
			list =  DemAPI.getInstance().getList(ivoicePerformUUID, conditionMap, null);
			long planNum =0l;
			BigDecimal bd = new BigDecimal(planNum);
			//将子记录金额加和
			for(HashMap hash:list){
				if(hash.get("AMOUNT")!=null){
					Long tmp = Long.parseLong(hash.get("AMOUNT").toString());
					bd=bd.add(new BigDecimal(tmp));
				}
			}
			model.setTicketNum(bd.longValue());	
			
		}
		if(baselist!=null&&baselist.size()>0){
			HashMap baseInfo = baselist.get(0);
			if(baseInfo!=null){
				Object obj = baseInfo.get("PACTSUN");
				long sumNum = Long.parseLong(obj.toString());
				model.setInvoiceBalance(sumNum-model.getTicketNum());
			}
		}
		return model;		
	}
	/**
	 * 获取交货计划执行列表
	 * @param itemno
	 * @return
	 */
	public List<HashMap> getDeliveryPlanList(String itemno){
		List<HashMap> list = null;
		if(itemno!=null){
			HashMap conditionMap = new HashMap();
			conditionMap.put("PACTNO", itemno);
			list =  DemAPI.getInstance().getList(deliveryPlanUUID, conditionMap, null);
		}
		  if(list!=null&&list.size()>0){				
				for(HashMap hash:list){
					Object obj = hash.get("ATTACHMENT");
					if(obj!=null){
						StringBuffer attachhtml = new StringBuffer();
						attachhtml.append("<ul class=\"attachDiv\">");
						String attachstr = ObjectUtil.getString(obj);
						String[] attachlist = attachstr.split(",");
						for(String tmp:attachlist){
							if(tmp.trim().equals(""))continue;
							FileUpload fu = FileUploadAPI.getInstance().getFileUpload(tmp);
							if(fu!=null){
								String filename = fu.getFileSrcName();
								if(filename.length()>15){
									filename = filename.substring(0,15)+"...";
								}
								attachhtml.append("<li><a href=\"uploadifyDownload.action?fileUUID=").append(fu.getFileId()).append("\" target=\"_blank\"><img src=\"iwork_img/attach.gif\">").append(filename).append("</a></li>");
							}
						}
						attachhtml.append("</ul>");
						hash.put("ATTACHMENT", attachhtml.toString());
					}
				}
				
			}
		return list;
	}
	
	
	
	/**
	 * 获取合同执行表预览数据
	 * @param itemno
	 * @return
	 */	
	public ContractBaseInfo getContractPerformContractInfo(String itemno){
		ContractBaseInfo model = new ContractBaseInfo();
		if(itemno!=null){
			List<HashMap> baseList = null;
			HashMap baseConditionMap=new HashMap();
			baseConditionMap.put("PACTNO", itemno);
			baseList =  DemAPI.getInstance().getList(baseUUID, baseConditionMap, null);
			for(HashMap hashmap:baseList){
				hashmap.get("PACTTYPE");
				model.setCurrencyString(hashmap.get("CURRENCY").toString());
				if(hashmap.get("CURRENCY").equals("1")){
					model.setCurrencyString("人民币");
				}else if(hashmap.get("CURRENCY").equals("2")){
					model.setCurrencyString("美元");
				}else if(hashmap.get("CURRENCY").equals("3")){
					model.setCurrencyString("英镑");
				}else if(hashmap.get("CURRENCY").equals("4")){
					model.setCurrencyString("港币");
				}else if(hashmap.get("CURRENCY").equals("5")){
					model.setCurrencyString("欧元");
				}
				if(Integer.parseInt(hashmap.get("PACTTYPE").toString())==1){
					model.setPlanCollectionString("计划收款金额");
					model.setInvoicePlanCollectionString("计划开票金额");
					model.setActualCollectionString("实际收款金额");
					model.setInvoicePerformCollectionString("实际开票金额");
					model.setShouldString("应收款金额");
					model.setInvoiceBalanceString("开票剩余金额");
				}else{
					model.setPlanCollectionString("计划付款金额");
					model.setInvoicePlanCollectionString("计划收票金额");
					model.setActualCollectionString("实际付款金额");
					model.setInvoicePerformCollectionString("实际收票金额");
					model.setShouldString("应付款金额");
					model.setInvoiceBalanceString("收票剩余金额");
				}					
			}
		}
		
		//根据表单模型取出表单formid和demid
		SysDemEngine sde = DemAPI.getInstance().getDemModel(contractPerformUUID);		
		if(sde!=null){
			//存储formid
			model.setFormId(sde.getFormid());
			//存储demid
			model.setDemId(sde.getId());
		}	
		//合同总金额
		HashMap conditionMap1 = new HashMap();
		conditionMap1.put("PACTNO", itemno);
		List<HashMap> baselist =  DemAPI.getInstance().getList(baseUUID, conditionMap1, null);
		if(baselist!=null&&baselist.size()>0){
			HashMap baseInfo = baselist.get(0);
			if(baseInfo!=null){
				Object obj = baseInfo.get("PACTSUN");
				String sumNum = ObjectUtil.getString(obj);
				model.setSumNum(sumNum);
			}
		}		
		//合计计划总金额
		if(itemno!=null){
			List<HashMap> list = null;
			HashMap conditionMap = new HashMap();
			conditionMap.put("PACTNO", itemno);
			list =  DemAPI.getInstance().getList(planListUUID, conditionMap, null);
			long planNum =0l;
			BigDecimal bd = new BigDecimal(planNum);
			for(HashMap hash:list){
				if(hash.get("AMOUNT")!=null){
					Long tmp = Long.parseLong(hash.get("AMOUNT").toString());
					bd=bd.add(new BigDecimal(tmp));
				}
			}
			model.setPlanNum(bd.longValue());
		}
		//发票计划总金额
		if(itemno!=null){
			List<HashMap> list = null;
			HashMap conditionMap = new HashMap();
			conditionMap.put("PACTNO", itemno);
			list =  DemAPI.getInstance().getList(IvoicePlanListUUID, conditionMap, null);
			long planNum =0l;
			BigDecimal bd = new BigDecimal(planNum);
			//将子记录金额加和
			for(HashMap hash:list){
				if(hash.get("AMOUNT")!=null){
					Long tmp = Long.parseLong(hash.get("AMOUNT").toString());
					bd=bd.add(new BigDecimal(tmp));
				}
			}
			model.setTicketPlanNum(bd.longValue());
		}
		
		//实际收/付款总金额
		if(itemno!=null){
			List<HashMap> list = null;
			HashMap conditionMap = new HashMap();
			conditionMap.put("PACTNO", itemno);
			list =  DemAPI.getInstance().getList(contractPerformUUID, conditionMap, null);
			long planNum =0l;
			BigDecimal bd = new BigDecimal(planNum);
			//将子记录金额加和
			for(HashMap hash:list){
				if(hash.get("AMOUNT")!=null){
					Long tmp = Long.parseLong(hash.get("AMOUNT").toString());
					bd=bd.add(new BigDecimal(tmp));
				}
			}
			model.setRealNum(bd.longValue());		
		}
		   //發票執行总金额
		if(itemno!=null){
			List<HashMap> list = null;
			HashMap conditionMap = new HashMap();
			conditionMap.put("PACTNO", itemno);
			list =  DemAPI.getInstance().getList(ivoicePerformUUID, conditionMap, null);
			long planNum =0l;
			BigDecimal bd = new BigDecimal(planNum);
			//将子记录金额加和
			for(HashMap hash:list){
				if(hash.get("AMOUNT")!=null){
					Long tmp = Long.parseLong(hash.get("AMOUNT").toString());
					bd=bd.add(new BigDecimal(tmp));
				}
			}
			model.setTicketNum(bd.longValue());		
		}
		//应收/付账款
		if(itemno!=null){
			List<HashMap> list = null;
			List<HashMap> performList=null;
			HashMap conditionMap = new HashMap();
			conditionMap.put("PACTNO", itemno);			
			performList =  DemAPI.getInstance().getList(ivoicePerformUUID, conditionMap, null);			
			list =  DemAPI.getInstance().getList(contractPerformUUID, conditionMap, null);
			long planNum =0l;
			BigDecimal bd = new BigDecimal(planNum);
			BigDecimal bds = new BigDecimal(planNum);
			//获取实际收/付款金额
			for(HashMap hash:list){
				if(hash.get("AMOUNT")!=null){
					Long tmp = Long.parseLong(hash.get("AMOUNT").toString());
					bd=bd.add(new BigDecimal(tmp));
				}
			}
			//获取发票执行金额
			for(HashMap hash:performList){
				if(hash.get("AMOUNT")!=null){
					Long tmp = Long.parseLong(hash.get("AMOUNT").toString());
					bds=bds.add(new BigDecimal(tmp));
				}
			}
			Long accountsNum=0l;	
			accountsNum=bds.longValue()-bd.longValue();			
			model.setAccountsNum(accountsNum);
		}	
		if(baselist!=null&&baselist.size()>0){
			HashMap baseInfo = baselist.get(0);
			if(baseInfo!=null){
				Object obj = baseInfo.get("PACTSUN");
				long sumNum = Long.parseLong(obj.toString());
				model.setInvoiceBalance(sumNum-model.getTicketNum());
			}
		}
		return model;		
	}
	/**
	 * 获取合同执行列表
	 * @param itemno
	 * @return
	 */
	public List<HashMap> getContractPerformList(String itemno){
		List<HashMap> list = null;
		if(itemno!=null){
			HashMap conditionMap = new HashMap();
			conditionMap.put("PACTNO", itemno);
			list =  DemAPI.getInstance().getList(contractPerformUUID, conditionMap, null);
		}
		  if(list!=null&&list.size()>0){				
				for(HashMap hash:list){
					Object obj = hash.get("ATTACHMENT");
					if(obj!=null){
						StringBuffer attachhtml = new StringBuffer();
						attachhtml.append("<ul class=\"attachDiv\">");
						String attachstr = ObjectUtil.getString(obj);
						String[] attachlist = attachstr.split(",");
						for(String tmp:attachlist){
							if(tmp.trim().equals(""))continue;
							FileUpload fu = FileUploadAPI.getInstance().getFileUpload(tmp);
							if(fu!=null){
								String filename = fu.getFileSrcName();
								if(filename.length()>15){
									filename = filename.substring(0,15)+"...";
								}
								attachhtml.append("<li><a href=\"uploadifyDownload.action?fileUUID=").append(fu.getFileId()).append("\" target=\"_blank\"><img src=\"iwork_img/attach.gif\">").append(filename).append("</a></li>");
							}
						}
						attachhtml.append("</ul>");
						hash.put("ATTACHMENT", attachhtml.toString());
					}
				}
				
			}
		return list;
	}
		
	/**
	 * 获取发票执行表预览数据
	 * @param itemno
	 * @return
	 */	
	public ContractBaseInfo getIvoicePerformContractInfo(String itemno){
		ContractBaseInfo model = new ContractBaseInfo();
		if(itemno!=null){
			List<HashMap> baseList = null;
			HashMap baseConditionMap=new HashMap();
			baseConditionMap.put("PACTNO", itemno);
			baseList =  DemAPI.getInstance().getList(baseUUID, baseConditionMap, null);
			for(HashMap hashmap:baseList){
				hashmap.get("PACTTYPE");	
				model.setCurrencyString(hashmap.get("CURRENCY").toString());
				if(hashmap.get("CURRENCY").equals("1")){
					model.setCurrencyString("人民币");
				}else if(hashmap.get("CURRENCY").equals("2")){
					model.setCurrencyString("美元");
				}else if(hashmap.get("CURRENCY").equals("3")){
					model.setCurrencyString("英镑");
				}else if(hashmap.get("CURRENCY").equals("4")){
					model.setCurrencyString("港币");
				}else if(hashmap.get("CURRENCY").equals("5")){
					model.setCurrencyString("欧元");
				}
				if(Integer.parseInt(hashmap.get("PACTTYPE").toString())==1){
					model.setPlanCollectionString("计划收款金额");
					model.setInvoicePlanCollectionString("计划开票金额");
					model.setActualCollectionString("实际收款金额");
					model.setInvoicePerformCollectionString("实际开票金额");
					model.setShouldString("应收款金额");
					model.setInvoiceBalanceString("开票剩余金额");
				}else{
					model.setPlanCollectionString("计划付款金额");
					model.setInvoicePlanCollectionString("计划收票金额");
					model.setActualCollectionString("实际付款金额");
					model.setInvoicePerformCollectionString("实际收票金额");
					model.setShouldString("应付款金额");
					model.setInvoiceBalanceString("收票剩余金额");
				}	
				
				
			}
		}
		
		//根据表单模型取出表单formid和demid
		SysDemEngine sde = DemAPI.getInstance().getDemModel(ivoicePerformUUID);		
		if(sde!=null){
			//存储formid
			model.setFormId(sde.getFormid());
			//存储demid
			model.setDemId(sde.getId());
		}	
		//合同总金额
		HashMap conditionMap1 = new HashMap();
		conditionMap1.put("PACTNO", itemno);
		List<HashMap> baselist =  DemAPI.getInstance().getList(baseUUID, conditionMap1, null);
		if(baselist!=null&&baselist.size()>0){
			HashMap baseInfo = baselist.get(0);
			if(baseInfo!=null){
				Object obj = baseInfo.get("PACTSUN");
				String sumNum = ObjectUtil.getString(obj);
				model.setSumNum(sumNum);
			}
		}
		
		//合计计划总金额
		if(itemno!=null){
			List<HashMap> list = null;
			HashMap conditionMap = new HashMap();
			conditionMap.put("PACTNO", itemno);
			list =  DemAPI.getInstance().getList(planListUUID, conditionMap, null);
			long planNum =0l;
			BigDecimal bd = new BigDecimal(planNum);
			for(HashMap hash:list){
				if(hash.get("AMOUNT")!=null){
					Long tmp = Long.parseLong(hash.get("AMOUNT").toString());
					bd=bd.add(new BigDecimal(tmp));
				}
			}
			model.setPlanNum(bd.longValue());
		}
		//发票计划总金额
		if(itemno!=null){
			List<HashMap> list = null;
			HashMap conditionMap = new HashMap();
			conditionMap.put("PACTNO", itemno);
			list =  DemAPI.getInstance().getList(IvoicePlanListUUID, conditionMap, null);
			long planNum =0l;
			BigDecimal bd = new BigDecimal(planNum);
			//将子记录金额加和
			for(HashMap hash:list){
				if(hash.get("AMOUNT")!=null){
					Long tmp = Long.parseLong(hash.get("AMOUNT").toString());
					bd=bd.add(new BigDecimal(tmp));
				}
			}
			model.setTicketPlanNum(bd.longValue());
		}		
		//实际收/付款总金额
		if(itemno!=null){
			List<HashMap> list = null;
			HashMap conditionMap = new HashMap();
			conditionMap.put("PACTNO", itemno);
			list =  DemAPI.getInstance().getList(contractPerformUUID, conditionMap, null);
			long planNum =0l;
			BigDecimal bd = new BigDecimal(planNum);
			//将子记录金额加和
			for(HashMap hash:list){
				if(hash.get("AMOUNT")!=null){
					Long tmp = Long.parseLong(hash.get("AMOUNT").toString());
					bd=bd.add(new BigDecimal(tmp));
				}
			}
			model.setRealNum(bd.longValue());		
		}
		   //發票執行总金额
		if(itemno!=null){
			List<HashMap> list = null;
			HashMap conditionMap = new HashMap();
			conditionMap.put("PACTNO", itemno);
			list =  DemAPI.getInstance().getList(ivoicePerformUUID, conditionMap, null);
			long planNum =0l;
			BigDecimal bd = new BigDecimal(planNum);
			//将子记录金额加和
			for(HashMap hash:list){
				if(hash.get("AMOUNT")!=null){
					Long tmp = Long.parseLong(hash.get("AMOUNT").toString());
					bd=bd.add(new BigDecimal(tmp));
				}
			}
			model.setTicketNum(bd.longValue());	
			
			if(baselist!=null&&baselist.size()>0){
				HashMap baseInfo = baselist.get(0);
				if(baseInfo!=null){
					Object obj = baseInfo.get("PACTSUN");
					long sumNum = Long.parseLong(obj.toString());
					model.setInvoiceBalance(sumNum-model.getTicketNum());
				}
			}
		}		
		return model;		
	}
	/**
	 * 获取发票执行列表
	 * @param itemno
	 * @return
	 */
	public List<HashMap> getIvoicePerformList(String itemno){
		List<HashMap> list = null;
		if(itemno!=null){
			HashMap conditionMap = new HashMap();
			conditionMap.put("PACTNO", itemno);
			list =  DemAPI.getInstance().getList(ivoicePerformUUID, conditionMap, null);
		}		
		if(list!=null&&list.size()>0){			
			for(HashMap hash:list){
				Object obj = hash.get("ATTACHMENT");
				if(obj!=null){
					StringBuffer attachhtml = new StringBuffer();
					attachhtml.append("<ul class=\"attachDiv\">");
					String attachstr = ObjectUtil.getString(obj);
					String[] attachlist = attachstr.split(",");
					for(String tmp:attachlist){
						if(tmp.trim().equals(""))continue;
						FileUpload fu = FileUploadAPI.getInstance().getFileUpload(tmp);
						if(fu!=null){
							String filename = fu.getFileSrcName();
							if(filename.length()>15){
								filename = filename.substring(0,15)+"...";
							}
							attachhtml.append("<li><a href=\"uploadifyDownload.action?fileUUID=").append(fu.getFileId()).append("\" target=\"_blank\"><img src=\"iwork_img/attach.gif\">").append(filename).append("</a></li>");
						}
					}
					attachhtml.append("</ul>");
					hash.put("ATTACHMENT", attachhtml.toString());
				}
			}			
		}
		return list;
	}
		
	/**
	 * 获取交货执行表预览数据
	 * @param itemno
	 * @return
	 */	
	public ContractBaseInfo getDeliveryPerformContractInfo(String itemno){
		ContractBaseInfo model = new ContractBaseInfo();
		if(itemno!=null){
			List<HashMap> baseList = null;
			HashMap baseConditionMap=new HashMap();
			baseConditionMap.put("PACTNO", itemno);
			baseList =  DemAPI.getInstance().getList(baseUUID, baseConditionMap, null);
			for(HashMap hashmap:baseList){
				hashmap.get("PACTTYPE");	
				model.setCurrencyString(hashmap.get("CURRENCY").toString());
				if(hashmap.get("CURRENCY").equals("1")){
					model.setCurrencyString("人民币");
				}else if(hashmap.get("CURRENCY").equals("2")){
					model.setCurrencyString("美元");
				}else if(hashmap.get("CURRENCY").equals("3")){
					model.setCurrencyString("英镑");
				}else if(hashmap.get("CURRENCY").equals("4")){
					model.setCurrencyString("港币");
				}else if(hashmap.get("CURRENCY").equals("5")){
					model.setCurrencyString("欧元");
				}
				if(Integer.parseInt(hashmap.get("PACTTYPE").toString())==1){
					model.setPlanCollectionString("计划收款金额");
					model.setInvoicePlanCollectionString("计划开票金额");
					model.setActualCollectionString("实际收款金额");
					model.setInvoicePerformCollectionString("实际开票金额");
					model.setShouldString("应收款金额");		
					model.setInvoiceBalanceString("开票剩余金额");
				}else{
					model.setPlanCollectionString("计划付款金额");
					model.setInvoicePlanCollectionString("计划收票金额");
					model.setActualCollectionString("实际付款金额");
					model.setInvoicePerformCollectionString("实际收票金额");
					model.setShouldString("应付款金额");
					model.setInvoiceBalanceString("收票剩余金额");
				}					
			}
		}
		
		//根据表单模型取出表单formid和demid
		SysDemEngine sde = DemAPI.getInstance().getDemModel(deliveryPerformUUID);		
		if(sde!=null){
			//存储formid
			model.setFormId(sde.getFormid());
			//存储demid
			model.setDemId(sde.getId());
		}	
		//合同总金额
		HashMap conditionMap1 = new HashMap();
		conditionMap1.put("PACTNO", itemno);
		List<HashMap> baselist =  DemAPI.getInstance().getList(baseUUID, conditionMap1, null);
		if(baselist!=null&&baselist.size()>0){
			HashMap baseInfo = baselist.get(0);
			if(baseInfo!=null){
				Object obj = baseInfo.get("PACTSUN");
				String sumNum = ObjectUtil.getString(obj);
				model.setSumNum(sumNum);
			}
		}
		
		//合计计划总金额
		if(itemno!=null){
			List<HashMap> list = null;
			HashMap conditionMap = new HashMap();
			conditionMap.put("PACTNO", itemno);
			list =  DemAPI.getInstance().getList(planListUUID, conditionMap, null);
			long planNum =0l;
			BigDecimal bd = new BigDecimal(planNum);
			for(HashMap hash:list){
				if(hash.get("AMOUNT")!=null){
					Long tmp = Long.parseLong(hash.get("AMOUNT").toString());
					bd=bd.add(new BigDecimal(tmp));
				}
			}
			model.setPlanNum(bd.longValue());
		}
		//发票计划总金额
		if(itemno!=null){
			List<HashMap> list = null;
			HashMap conditionMap = new HashMap();
			conditionMap.put("PACTNO", itemno);
			list =  DemAPI.getInstance().getList(IvoicePlanListUUID, conditionMap, null);
			long planNum =0l;
			BigDecimal bd = new BigDecimal(planNum);
			//将子记录金额加和
			for(HashMap hash:list){
				if(hash.get("AMOUNT")!=null){
					Long tmp = Long.parseLong(hash.get("AMOUNT").toString());
					bd=bd.add(new BigDecimal(tmp));
				}
			}
			model.setTicketPlanNum(bd.longValue());
		}
		
		//实际收/付款总金额
		if(itemno!=null){
			List<HashMap> list = null;
			HashMap conditionMap = new HashMap();
			conditionMap.put("PACTNO", itemno);
			list =  DemAPI.getInstance().getList(contractPerformUUID, conditionMap, null);
			long planNum =0l;
			BigDecimal bd = new BigDecimal(planNum);
			//将子记录金额加和
			for(HashMap hash:list){
				if(hash.get("AMOUNT")!=null){
					Long tmp = Long.parseLong(hash.get("AMOUNT").toString());
					bd=bd.add(new BigDecimal(tmp));
				}
			}
			model.setRealNum(bd.longValue());		
		}
		   //發票執行总金额
		if(itemno!=null){
			List<HashMap> list = null;
			HashMap conditionMap = new HashMap();
			conditionMap.put("PACTNO", itemno);
			list =  DemAPI.getInstance().getList(ivoicePerformUUID, conditionMap, null);
			long planNum =0l;
			BigDecimal bd = new BigDecimal(planNum);
			//将子记录金额加和
			for(HashMap hash:list){
				if(hash.get("AMOUNT")!=null){
					Long tmp = Long.parseLong(hash.get("AMOUNT").toString());
					bd=bd.add(new BigDecimal(tmp));
				}
			}
			model.setTicketNum(bd.longValue());					
		}
		if(baselist!=null&&baselist.size()>0){
			HashMap baseInfo = baselist.get(0);
			if(baseInfo!=null){
				Object obj = baseInfo.get("PACTSUN");
				long sumNum = Long.parseLong(obj.toString());
				model.setInvoiceBalance(sumNum-model.getTicketNum());
			}
		}
		return model;		
	}
	/**
	 * 获取交货执行列表
	 * @param itemno
	 * @return
	 */
	public List<HashMap> getDeliveryPerformList(String itemno){
		List<HashMap> list = null;
		if(itemno!=null){
			HashMap conditionMap = new HashMap();
			conditionMap.put("PACTNO", itemno);
			list =  DemAPI.getInstance().getList(deliveryPerformUUID, conditionMap, null);
		}
		    //对附件进行处理
          if(list!=null&&list.size()>0){			
			  for(HashMap hash:list){
				Object obj = hash.get("ATTACHMENT");
				if(obj!=null){
					StringBuffer attachhtml = new StringBuffer();
					attachhtml.append("<ul class=\"attachDiv\">");
					String attachstr = ObjectUtil.getString(obj);
					String[] attachlist = attachstr.split(",");
					for(String tmp:attachlist){
						if(tmp.trim().equals(""))continue;
						FileUpload fu = FileUploadAPI.getInstance().getFileUpload(tmp);
						if(fu!=null){
							String filename = fu.getFileSrcName();
							if(filename.length()>15){
								filename = filename.substring(0,15)+"...";
							}
							attachhtml.append("<li><a href=\"uploadifyDownload.action?fileUUID=").append(fu.getFileId()).append("\" target=\"_blank\"><img src=\"iwork_img/attach.gif\">").append(filename).append("</a></li>");
						}
					}
					attachhtml.append("</ul>");
					hash.put("ATTACHMENT", attachhtml.toString());
				}
			}			
		}
		return list;
	}
	/**
	 * 验证发票金额
	 * @param itemno
	 * @return
	 */
	public ContractBaseInfo getSum(String itemno,String dataid){
		ContractBaseInfo model = new ContractBaseInfo();
		Long dataId=Long.parseLong(dataid);
		//合同总金额
		HashMap conditionMap1 = new HashMap();
		conditionMap1.put("PACTNO", itemno);
		List<HashMap> baselist =  DemAPI.getInstance().getList(baseUUID, conditionMap1, null);				
		if(baselist!=null&&baselist.size()>0){
			HashMap baseInfo = baselist.get(0);
			if(baseInfo!=null){
				Object obj = baseInfo.get("PACTSUN");
				String sumNum = ObjectUtil.getString(obj);
				model.setSumNum(sumNum);
			}
		}
		  //發票執行总金额
		
		if(dataId==0){
			if(itemno!=null){
				List<HashMap> list = null;
				HashMap conditionMap = new HashMap();
				conditionMap.put("PACTNO", itemno);
				list =  DemAPI.getInstance().getList(ivoicePerformUUID, conditionMap, null);
				long planNum =0l;
				BigDecimal bd = new BigDecimal(planNum);
				//将子记录金额加和
				for(HashMap hash:list){
					if(hash.get("AMOUNT")!=null){
						Long tmp = Long.parseLong(hash.get("AMOUNT").toString());						
						bd=bd.add(new BigDecimal(tmp));
					}
				}
				model.setTicketNum(bd.longValue());	
			}
		}else{
			List<HashMap> list = null;
			HashMap conditionMap = new HashMap();
			conditionMap.put("PACTNO", itemno);
			list =  DemAPI.getInstance().getList(ivoicePerformUUID, conditionMap, null);
			long planNum =0l;
			BigDecimal bd = new BigDecimal(planNum);
			//将子记录金额加和
			for(HashMap hash:list){
				if(hash.get("AMOUNT")!=null){
					Long tmp = Long.parseLong(hash.get("AMOUNT").toString());
					hash.get("ID");
					Long id = Long.parseLong(hash.get("ID").toString());
					if(id.equals(dataId)){
						Long  eventNum= Long.parseLong(hash.get("AMOUNT").toString());
						model.setEventNum(eventNum);
					}
					
					bd=bd.add(new BigDecimal(tmp));
				}
			}
			model.setTicketNum(bd.longValue());						
		}
	
		return model;
	}

	/**
	 * 获取时时发票余额数据
	 * @param itemno
	 * @return
	 */
	public ContractBaseInfo getIvoicePerformUpdate(String itemno){
		ContractBaseInfo model = new ContractBaseInfo();
		//Long dataId=Long.parseLong(dataid);
		//合同总金额
		if(itemno!=null&&!"".equals(itemno)){
		HashMap conditionMap1 = new HashMap();
		conditionMap1.put("PACTNO", itemno);
		List<HashMap> baselist =  DemAPI.getInstance().getList(baseUUID, conditionMap1, null);				
		if(baselist!=null&&baselist.size()>0){
			HashMap baseInfo = baselist.get(0);
			if(baseInfo!=null){
				Object obj = baseInfo.get("PACTSUN");
				String sumNum = ObjectUtil.getString(obj);
				model.setSumNum(sumNum);
			}
		}
		}
		  //發票執行总金额		
		if(itemno!=null&&!"".equals(itemno)){
			List<HashMap> list = null;
			HashMap conditionMap = new HashMap();
			conditionMap.put("PACTNO", itemno);
			list =  DemAPI.getInstance().getList(ivoicePerformUUID, conditionMap, null);
			long planNum =0l;
			BigDecimal bd = new BigDecimal(planNum);
			//将子记录金额加和
			for(HashMap hash:list){
				if(hash.get("AMOUNT")!=null){
					Long tmp = Long.parseLong(hash.get("AMOUNT").toString());					
					bd=bd.add(new BigDecimal(tmp));
				}
			}
			model.setTicketNum(bd.longValue());	
		}			
		return model;
	}	
	
	/**
	 * 彻底删除合同记录
	 * 
	 * @author 杨连峰
	 * @param model
	 * @return
	 */

	public void cleanContract(String ids) {
		// Long is_del = new Long(1);
		if (ids != null && !"".equals(ids)) {
			String[] strArr = ids.split(",");
			for (int i = 0; i < strArr.length; i++) {
				Long id = Long.parseLong(strArr[i]);
				if (id > 0) {
					DemAPI.getInstance().removeFormData(id);
				}
			}
		}

	}
	
	/**
	 * 获取查询操作的总记录条数
	 * @author 杨连峰
	 * 
	 * @return
	 */
	public int getQuerySize(String pactNo,String pactName,String sum,String eventContract,String ContractProperties,String contractNature,String year){
		HashMap conditionMap = new HashMap();
		//获取合同属性
		conditionMap.put("PACTBELONG", ContractProperties);
		//获取合同性质
		conditionMap.put("PACTKIND", contractNature);
		//获取当前年份
		conditionMap.put("YEAR", year);
		//conditionMap.put("FATHERPACTNO", pactNo);
		//获取合同编号
		conditionMap.put("PACTNO", pactNo);
		//获取合同名称
		conditionMap.put("PACTTITLE", pactName);
		//获取合同类型
		if(!sum.equals("") && sum!=null && !sum.equals("undefined")){
			Long sums = Long.parseLong(sum);
			conditionMap.put("PACTTYPE", sums);
		}	  		
		if(!eventContract.equals("") && eventContract!=null && !eventContract.equals("undefined")&& !eventContract.equals("null")){
			Long tmp = Long.parseLong(eventContract);
			conditionMap.put("PACTNATURE",tmp );	
		}
		//获取当前用户是否支持子表显示
 		HashMap optionmap = new HashMap();
 		List<HashMap> optionsList =  DemAPI.getInstance().getList(optionsUUID, optionmap, null);
 		String adminname="";//所有合同管理员
 		String issupport="";//获取是否显示子合同
 		String isadminname="";//是否是合同管理员
 		if(optionsList!=null){
 			for(HashMap map:optionsList){
 				//获取所有合同管理员
 	 			adminname = String.valueOf(map.get("ADMINNAME"));
 	 			issupport = String.valueOf(map.get("ISSUPPORT"));
 	 		}
 		}
 		//获取当前用户账号是否存在合同管理权限
 		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
 		String userid1=uc.get_userModel().getUserid();
 		//获取合同设置管理员
 		String[] userlist = adminname.split(",");
 		for(String u:userlist){
 			if(u.trim().equals("")){
 				continue;
 			}
 			String userid = UserContextUtil.getInstance().getUserId(u);
 			if(userid!=null && userid.equals(userid1)){
 				isadminname = userid1;
 				break;
 			}else{
 				
 			}
 		}
 		if(isadminname!=null && isadminname.equals(userid1)){
 			
 		}else{
 			conditionMap.put("CREATEID", uc.get_userModel().getUserid());
 		}
		
		
		List<HashMap> list =  DemAPI.getInstance().getList(baseUUID, conditionMap, null);
		if(list!=null && list.size()>0){
			for(HashMap<String,Object> map:list){
				if(Integer.parseInt(map.get("PACTNATURE").toString())==1){
					map.put("CONTACTTYPE", "主合同");
				}else{
					map.put("CONTACTTYPE", "子合同");
				}
			}
		}
		return list.size();
	}	
	/**
	 * 查询合同    yanglianfeng
	 * @return
	 */
	/*public List<HashMap> getQueryContract(String pactNo,String pactName,String sum,String eventContract,String year,int pageSize,int startRow){
		HashMap conditionMap = new HashMap();
		//获取当前年份
		conditionMap.put("YEAR", year);
		//获取合同编号
		conditionMap.put("PACTNO", pactNo);
		//获取合同名称
		conditionMap.put("PACTTITLE", pactName);
		//获取合同类型
		if(!sum.equals("") && sum!=null && !sum.equals("undefined")){
			Long sums = Long.parseLong(sum);
			conditionMap.put("PACTTYPE", sums);
		}	  		
		if(!eventContract.equals("") && eventContract!=null && !eventContract.equals("undefined")&& !eventContract.equals("null")){
			Long tmp = Long.parseLong(eventContract);
			conditionMap.put("PACTNATURE",tmp );	
		}
		List<HashMap> list =DemAPI.getInstance().getList(baseUUID, conditionMap, null, pageSize, startRow);
		if(list!=null && list.size()>0){
			for(HashMap<String,Object> map:list){
				if(Integer.parseInt(map.get("PACTNATURE").toString())==1){
					map.put("CONTACTTYPE", "主合同");
				}else{
					map.put("CONTACTTYPE", "子合同");
				}
			}
		}
		return list;
	}*/
	
	/**
	 * 获取查询首页所有列表 
	 * @author 杨连峰
	 * 
	 * @return
	 */
	public List<HashMap> getAllContractList(){		
		HashMap conditionMap = new HashMap();
		List<HashMap> list =  DemAPI.getInstance().getList(baseUUID, conditionMap, null);
		if(list!=null && list.size()>0){
			for(HashMap<String,Object> map:list){
				if(Integer.parseInt(map.get("PACTNATURE").toString())==1){
					map.put("CONTACTTYPE", "主合同");
				}else{
					map.put("CONTACTTYPE", "子合同");
				}
			}
		}
		return list;
	}
	
	/**
	 * 获取基本信息表的demid和formid
	 * @author 杨连峰
	 * 
	 * @return
	 */
	public ContractBaseInfo getBaseId(){
		ContractBaseInfo model = new ContractBaseInfo();
		//根据表单模型取出表单formid和demid
		SysDemEngine sde = DemAPI.getInstance().getDemModel(baseUUID);		
		if(sde!=null){
			//存储formid
			model.setFormId(sde.getFormid());
			//存储demid
			model.setDemId(sde.getId());
		}	
		return model;
	}
	
	/**
	 * 验证总合同金额
	 * @param itemno
	 * @return
	 */	
	public ContractBaseInfo getContractPerformContractInfo(String itemno,String dataid,String formid){	
		ContractBaseInfo model = new ContractBaseInfo();
		Long dataId=Long.parseLong(dataid);		
		if(dataId==0){
			//合同总金额
			HashMap conditionMap1 = new HashMap();
			conditionMap1.put("PACTNO", itemno);
			List<HashMap> baselist =  DemAPI.getInstance().getList(baseUUID, conditionMap1, null);
			if(baselist!=null&&baselist.size()>0){
				HashMap baseInfo = baselist.get(0);
				if(baseInfo!=null){
					Object obj = baseInfo.get("PACTSUN");
					String sumNum = ObjectUtil.getString(obj);
					model.setSumNum(sumNum);
				}
			}
			
			//合计计划总金额
			if(itemno!=null){
				List<HashMap> list = null;
				HashMap conditionMap = new HashMap();
				conditionMap.put("PACTNO", itemno);
				list =  DemAPI.getInstance().getList(planListUUID, conditionMap, null);
				long planNum =0l;
				BigDecimal bd = new BigDecimal(planNum);
				for(HashMap hash:list){
					if(hash.get("AMOUNT")!=null){
						Long tmp = Long.parseLong(hash.get("AMOUNT").toString());
						bd=bd.add(new BigDecimal(tmp));
					}
				}
				model.setPlanNum(bd.longValue());
			}
			//发票计划总金额
			if(itemno!=null){
				List<HashMap> list = null;
				HashMap conditionMap = new HashMap();
				conditionMap.put("PACTNO", itemno);
				list =  DemAPI.getInstance().getList(IvoicePlanListUUID, conditionMap, null);
				long planNum =0l;
				BigDecimal bd = new BigDecimal(planNum);
				//将子记录金额加和
				for(HashMap hash:list){
					if(hash.get("AMOUNT")!=null){
						Long tmp = Long.parseLong(hash.get("AMOUNT").toString());
						bd=bd.add(new BigDecimal(tmp));
					}
				}
				model.setTicketPlanNum(bd.longValue());
			}
			
			//实际收/付款总金额
			if(itemno!=null){
				List<HashMap> list = null;
				HashMap conditionMap = new HashMap();
				conditionMap.put("PACTNO", itemno);
				list =  DemAPI.getInstance().getList(contractPerformUUID, conditionMap, null);
				long planNum =0l;
				BigDecimal bd = new BigDecimal(planNum);
				//将子记录金额加和
				for(HashMap hash:list){
					if(hash.get("AMOUNT")!=null){
						Long tmp = Long.parseLong(hash.get("AMOUNT").toString());
						bd=bd.add(new BigDecimal(tmp));
					}
				}
				model.setRealNum(bd.longValue());		
			}
			 //發票執行总金额
			if(itemno!=null){
				List<HashMap> list = null;
				HashMap conditionMap = new HashMap();
				conditionMap.put("PACTNO", itemno);
				list =  DemAPI.getInstance().getList(ivoicePerformUUID, conditionMap, null);
				long planNum =0l;
				BigDecimal bd = new BigDecimal(planNum);
				//将子记录金额加和
				for(HashMap hash:list){
					if(hash.get("AMOUNT")!=null){
						Long tmp = Long.parseLong(hash.get("AMOUNT").toString());
						bd=bd.add(new BigDecimal(tmp));
					}
				}
				model.setTicketNum(bd.longValue());		
			}
			//应收/付账款
			if(itemno!=null){
				List<HashMap> list = null;
				List<HashMap> performList=null;
				HashMap conditionMap = new HashMap();
				conditionMap.put("PACTNO", itemno);				
				performList =  DemAPI.getInstance().getList(ivoicePerformUUID, conditionMap, null);				
				list =  DemAPI.getInstance().getList(contractPerformUUID, conditionMap, null);
				long planNum =0l;
				BigDecimal bd = new BigDecimal(planNum);
				BigDecimal bds = new BigDecimal(planNum);
				//获取实际收/付款金额
				for(HashMap hash:list){
					if(hash.get("AMOUNT")!=null){
						Long tmp = Long.parseLong(hash.get("AMOUNT").toString());
						bd=bd.add(new BigDecimal(tmp));
					}
				}				
				Long accountsNum=0l;	
				accountsNum=bds.longValue()-bd.longValue();				
				model.setAccountsNum(accountsNum);			
			}						
		}else{
			//合同总金额
			HashMap conditionMap1 = new HashMap();
			conditionMap1.put("PACTNO", itemno);
			List<HashMap> baselist =  DemAPI.getInstance().getList(baseUUID, conditionMap1, null);
			if(baselist!=null&&baselist.size()>0){
				HashMap baseInfo = baselist.get(0);
				if(baseInfo!=null){
					Object obj = baseInfo.get("PACTSUN");
					String sumNum = ObjectUtil.getString(obj);
					model.setSumNum(sumNum);
				}
			}
			
			if(itemno!=null){
				//根据表单模型取出表单formid和demid
				SysDemEngine sde = DemAPI.getInstance().getDemModel(planListUUID);				
				if(sde!=null){
					//存储formid
					model.setFormId(sde.getFormid());
					//存储demid
					model.setDemId(sde.getId());
				}	
				List<HashMap> list = null;
				HashMap conditionMap = new HashMap();
				conditionMap.put("PACTNO", itemno);
				list =  DemAPI.getInstance().getList(planListUUID, conditionMap, null);
				long planNum =0l;
				BigDecimal bd = new BigDecimal(planNum);
				//将子记录金额加和
				for(HashMap hash:list){
					if(hash.get("AMOUNT")!=null){
						Long tmp = Long.parseLong(hash.get("AMOUNT").toString());
						hash.get("ID");
						Long id = Long.parseLong(hash.get("ID").toString());
						if(id.equals(dataId)&&formid.equals(model.getFormId().toString())){
							Long  eventNum= Long.parseLong(hash.get("AMOUNT").toString());
							model.setEventNum(eventNum);
						}
						bd=bd.add(new BigDecimal(tmp));
					}
				}
				model.setPlanNum(bd.longValue());		
				
			}
		
			if(itemno!=null){
				//根据表单模型取出表单formid和demid
				SysDemEngine sde = DemAPI.getInstance().getDemModel(IvoicePlanListUUID);				
				if(sde!=null){
					//存储formid
					model.setFormId(sde.getFormid());
					//存储demid
					model.setDemId(sde.getId());
				}	
				List<HashMap> list = null;
				HashMap conditionMap = new HashMap();
				conditionMap.put("PACTNO", itemno);
				list =  DemAPI.getInstance().getList(IvoicePlanListUUID, conditionMap, null);
				long planNum =0l;
				BigDecimal bd = new BigDecimal(planNum);
				//将子记录金额加和
				for(HashMap hash:list){
					if(hash.get("AMOUNT")!=null){
						Long tmp = Long.parseLong(hash.get("AMOUNT").toString());
						hash.get("ID");
						Long id = Long.parseLong(hash.get("ID").toString());
						
						if(id.equals(dataId)&&formid.equals(model.getFormId().toString())){
							Long  eventNum= Long.parseLong(hash.get("AMOUNT").toString());
							model.setEventNum(eventNum);
						}
						
						bd=bd.add(new BigDecimal(tmp));
					}
				}
				model.setTicketPlanNum(bd.longValue());						
			}
			
			if(itemno!=null){
				//根据表单模型取出表单formid和demid
				SysDemEngine sde = DemAPI.getInstance().getDemModel(contractPerformUUID);				
				if(sde!=null){
					//存储formid
					model.setFormId(sde.getFormid());
					//存储demid
					model.setDemId(sde.getId());
				}	
				List<HashMap> list = null;
				HashMap conditionMap = new HashMap();
				conditionMap.put("PACTNO", itemno);
				list =  DemAPI.getInstance().getList(contractPerformUUID, conditionMap, null);
				long planNum =0l;
				BigDecimal bd = new BigDecimal(planNum);
				//将子记录金额加和
				for(HashMap hash:list){
					if(hash.get("AMOUNT")!=null){
						Long tmp = Long.parseLong(hash.get("AMOUNT").toString());
						hash.get("ID");
						Long id = Long.parseLong(hash.get("ID").toString());
						if(id.equals(dataId)&&formid.equals(model.getFormId().toString())){
							Long  eventNum= Long.parseLong(hash.get("AMOUNT").toString());
							model.setEventNum(eventNum);
						}
						;
						bd=bd.add(new BigDecimal(tmp));
					}
				}
				model.setRealNum(bd.longValue());						
			}
			
			if(itemno!=null){
				//根据表单模型取出表单formid和demid
				SysDemEngine sde = DemAPI.getInstance().getDemModel(ivoicePerformUUID);				
				if(sde!=null){
					//存储formid
					model.setFormId(sde.getFormid());
					//存储demid
					model.setDemId(sde.getId());
				}	
				List<HashMap> list = null;
				HashMap conditionMap = new HashMap();
				conditionMap.put("PACTNO", itemno);
				list =  DemAPI.getInstance().getList(ivoicePerformUUID, conditionMap, null);
				long planNum =0l;
				BigDecimal bd = new BigDecimal(planNum);
				//将子记录金额加和
				for(HashMap hash:list){
					if(hash.get("AMOUNT")!=null){
						Long tmp = Long.parseLong(hash.get("AMOUNT").toString());
						hash.get("ID");
						Long id = Long.parseLong(hash.get("ID").toString());
						if(id.equals(dataId)&&formid.equals(model.getFormId().toString())){
							Long  eventNum= Long.parseLong(hash.get("AMOUNT").toString());
							model.setEventNum(eventNum);
						}						
						bd=bd.add(new BigDecimal(tmp));
					}
				}
				model.setTicketNum(bd.longValue());						
			}						
		}
		return model;
		
	}
	/**
	 * 获取子合同列表html
	 * @param contractNo 合同编号
	 * @return
	 */
	public String getSubContrctListHtml(String contractNo){
		StringBuffer html = new StringBuffer();
		HashMap conditionMap1 = new HashMap();
		conditionMap1.put("FATHERPACTNO", contractNo);
		List<HashMap> baselist =  DemAPI.getInstance().getList(baseUUID, conditionMap1, null);
		html.append("<table width=\"90%\"  class=\"subTable\">").append("\n");
		html.append("<tr>").append("\n");
		html.append("            		<th style=\"width:2%\"></th>").append("\n");
		html.append("	            	<th style=\"width:15%\">合同编号</th>").append("\n");
		html.append("	            	<th style=\"width:40%\">合同名称</th>").append("\n");
		html.append("	            	<th style=\"width:10%\">负责人</th>").append("\n");
		html.append("	            	<th style=\"width:10%\">合同总金额</th>").append("\n");
		html.append("	            	<th style=\"width:10%\">合同截止日期</th>").append("\n");
		html.append(" </tr>").append("\n");
		for(HashMap hash:baselist){
			if(hash.get("FATHERPACTNO")!=null){
				html.append("<validationUpdatevalidationUpdatevalidationUpdatetr>").append("\n");
				html.append("            		<td></td>").append("\n");
				html.append("            		<td>").append("<a href='").append("iwork_contract_ContractInstanceid.action?instanceId=").append(hash.get("INSTANCEID")).append("&itemno=").append(hash.get("PACTNO")).append("&itemname=").append(hash.get("PACTTITLE")).append("'").append(" target='_self'>").append(hash.get("PACTNO")).append("</a>").append("</td>").append("\n");
				html.append("            		<td>").append(hash.get("PACTTITLE")).append("</td>").append("\n");
				html.append("            		<td>").append(hash.get("ADMIN")).append("</td>").append("\n");
				html.append("            		<td>").append(hash.get("PACTSUN")).append("</td>").append("\n");
				html.append("            		<td>").append(hash.get("FROMDATE")).append("</td>").append("\n");
				html.append("            	</tr>").append("\n");
			}
		}
		html.append("</table>").append("\n");
		return html.toString();
	}
	/**
	 * 分页按年份查询合同记录条数
	 */
	public int getContractListSize(String itemno,String itemname,String year){
		HashMap conditionMap = new HashMap();
		if(year!=null){
			conditionMap.put("YEAR", year);
		}
		if(itemno!=null && !"".equals(itemno)){
			conditionMap.put("PACTNO", itemno);
		}
		if(itemname!=null && !"".equals(itemname)){
			conditionMap.put("PACTTITLE", itemname);
		}
 		//获取当前用户是否支持子表显示
 		HashMap optionmap = new HashMap();
 		List<HashMap> optionsList =  DemAPI.getInstance().getList(optionsUUID, optionmap, null);
 		String adminname="";//所有合同管理员
 		String issupport="";//获取是否显示子合同
 		String isadminname="";//是否是合同管理员
 		if(optionsList!=null){
 			for(HashMap map:optionsList){
 				//获取所有合同管理员
 	 			adminname = String.valueOf(map.get("ADMINNAME"));
 	 			issupport = String.valueOf(map.get("ISSUPPORT"));
 	 		}
 		}
 		//获取当前用户账号是否存在合同管理权限
 		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
 		String userid1=uc.get_userModel().getUserid();
 		//获取合同设置管理员
 		String[] userlist = adminname.split(",");
 		for(String u:userlist){
 			if(u.trim().equals("")){
 				continue;
 			}
 			String userid = UserContextUtil.getInstance().getUserId(u);
 			if(userid!=null && userid.equals(userid1)){
 				isadminname = userid1;
 				break;
 			}else{
 				
 			}
 		}
 		if(isadminname!=null && isadminname.equals(userid1)){
 			
 		}else{
 			conditionMap.put("CREATEID", uc.get_userModel().getUserid());
 		}
		//获取所有合同数据
		List<HashMap> list1 = new ArrayList<HashMap>();
		conditionMap.put("FATHERPACTNO", "[空]");
		List<HashMap> list =  DemAPI.getInstance().getList(baseUUID, conditionMap, null);
		return list.size();
	}
	
	
	public List<HashMap> getQueryContract(String pactNo,String pactName,String sum,String eventContract,String ContractProperties,String contractNature,String year,int pageSize,int startRow){
		HashMap conditionMap = new HashMap();
		//获取合同属性
		conditionMap.put("PACTBELONG", ContractProperties);
		//获取合同性质
		conditionMap.put("PACTKIND", contractNature);
		//获取当前年份
		conditionMap.put("YEAR", year);
		//获取合同编号
		conditionMap.put("PACTNO", pactNo);
		//获取合同名称
		conditionMap.put("PACTTITLE", pactName);
		//获取合同类型
		if(!sum.equals("") && sum!=null && !sum.equals("undefined")){
			Long sums = Long.parseLong(sum);
			conditionMap.put("PACTTYPE", sums);
		}	  		
		if(!eventContract.equals("") && eventContract!=null && !eventContract.equals("undefined")&& !eventContract.equals("null")){
			Long tmp = Long.parseLong(eventContract);
			conditionMap.put("PACTNATURE",tmp );	
		}
		//获取当前用户是否支持子表显示
 		HashMap optionmap = new HashMap();
 		List<HashMap> optionsList =  DemAPI.getInstance().getList(optionsUUID, optionmap, null);
 		String adminname="";//所有合同管理员
 		String issupport="";//获取是否显示子合同
 		String isadminname="";//是否属于合同管理员
 		if(optionsList!=null){
 			for(HashMap map:optionsList){
 				//获取所有合同管理员
 	 			adminname = String.valueOf(map.get("ADMINNAME"));
 	 			issupport = String.valueOf(map.get("ISSUPPORT"));
 	 		}
 		}
 		//获取当前用户账号是否存在合同管理权限
 		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
 		String userid1=uc.get_userModel().getUserid();
 		//获取合同设置管理员
 		String[] userlist = adminname.split(",");
 		for(String u:userlist){
 			if(u.trim().equals("")){
 				continue;
 			}
 			String userid = UserContextUtil.getInstance().getUserId(u);
 			if(userid!=null && userid.equals(userid1)){
 				isadminname = userid1;
 				break;
 			}else{
 				
 			}
 		}
 		if(isadminname!=null && isadminname.equals(userid1)){
 			
 		}else{
 			conditionMap.put("CREATEID", uc.get_userModel().getUserid());
 		}
		//获取所有合同数据
		List<HashMap> list1 = new ArrayList<HashMap>();
		//conditionMap.put("FATHERPACTNO", "[空]");
		List<HashMap> list = DemAPI.getInstance().getList(baseUUID, conditionMap, null, pageSize, startRow);
		//DemAPI.getInstance().getList(baseUUID, conditionMap, null);
		
		for(HashMap map:list){
			HashMap map1 = new HashMap();
			map1.put("FATHERPACTNO", map.get("PACTNO"));
			list1 =  DemAPI.getInstance().getList(baseUUID, map1, null);
			Long isSub  = new Long(0);
			//判断当前用户是否支持显示子表数据
			if(issupport!=null&&!issupport.equals("")&&issupport.equals("1")){
				if(list1!=null&&list1.size()>0){
					isSub = new Long(1);
					map.put("IS_SUB", isSub);
				}else{
					map.put("IS_SUB", new Long(0));
				}
			}else{
				map.put("IS_SUB", new Long(0));
			}
		}
		if(list!=null && list.size()>0){
			for(HashMap<String,Object> map:list){
				if(Integer.parseInt(map.get("PACTNATURE").toString())==1){
					map.put("CONTACTTYPE", "主合同");
				}else{
					map.put("CONTACTTYPE", "子合同");
				}
			}
		}
		return list;
	}
	
	
	
	
	/**
	 * 查询所有合同列表
	 * @return
	 */
	public List<HashMap> getContractList(String itemno,String itemname,String year,int pageSize,int startRow){
 		HashMap conditionMap = new HashMap();
 		if(year!=null){
			conditionMap.put("YEAR", year);
		}
 		if(itemno!=null && !"".equals(itemno)){
			conditionMap.put("PACTNO", itemno);
		}
		if(itemname!=null && !"".equals(itemname)){
			conditionMap.put("PACTTITLE", itemname);
		}
 		//获取当前用户是否支持子表显示
 		HashMap optionmap = new HashMap();
 		List<HashMap> optionsList =  DemAPI.getInstance().getList(optionsUUID, optionmap, null);
 		String adminname="";//所有合同管理员
 		String issupport="";//获取是否显示子合同
 		String isadminname="";//是否属于合同管理员
 		if(optionsList!=null){
 			for(HashMap map:optionsList){
 				//获取所有合同管理员
 	 			adminname = String.valueOf(map.get("ADMINNAME"));
 	 			issupport = String.valueOf(map.get("ISSUPPORT"));
 	 		}
 		}
 		//获取当前用户账号是否存在合同管理权限
 		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
 		String userid1=uc.get_userModel().getUserid();
 		//获取合同设置管理员
 		String[] userlist = adminname.split(",");
 		for(String u:userlist){
 			if(u.trim().equals("")){
 				continue;
 			}
 			String userid = UserContextUtil.getInstance().getUserId(u);
 			if(userid!=null && userid.equals(userid1)){
 				isadminname = userid1;
 				break;
 			}else{
 				
 			}
 		}
 		if(isadminname!=null && isadminname.equals(userid1)){
 			
 		}else{
 			conditionMap.put("CREATEID", uc.get_userModel().getUserid());
 		}
		//获取所有合同数据
		List<HashMap> list1 = new ArrayList<HashMap>();
		conditionMap.put("FATHERPACTNO", "[空]");
		List<HashMap> list = DemAPI.getInstance().getList(baseUUID, conditionMap, null, pageSize, startRow);
		//DemAPI.getInstance().getList(baseUUID, conditionMap, null);
		
		for(HashMap map:list){
			HashMap map1 = new HashMap();
			map1.put("FATHERPACTNO", map.get("PACTNO"));
			list1 =  DemAPI.getInstance().getList(baseUUID, map1, null);
			Long isSub  = new Long(0);
			//判断当前用户是否支持显示子表数据
			if(issupport!=null&&!issupport.equals("")&&issupport.equals("1")){
				if(list1!=null&&list1.size()>0){
					isSub = new Long(1);
					map.put("IS_SUB", isSub);
				}else{
					map.put("IS_SUB", new Long(0));
				}
			}else{
				map.put("IS_SUB", new Long(0));
			}
		}
		return list;
	}
	/**
	 * 查询设置表中数据
	 */
	public HashMap getOptions(){
		HashMap optionsMap = new HashMap();
		List<HashMap> list =  DemAPI.getInstance().getList(optionsUUID, optionsMap, null);
		for(HashMap optionsMap1:list){
			optionsMap.put("ADMINNAME", optionsMap1.get("ADMINNAME"));
			optionsMap.put("ID", optionsMap1.get("ID"));
			optionsMap.put("ISSUPPORT", optionsMap1.get("ISSUPPORT"));
			optionsMap.put("INSTANCEID", optionsMap1.get("INSTANCEID"));
		}
		return optionsMap;
	}
    /**
     * 查看合同基本信息
     * 
     */
	public HashMap getBaseContract(String itemno){
		HashMap contractMap = new HashMap();
		if(itemno!=null){
			HashMap conditionMap = new HashMap();
			conditionMap.put("PACTNO", itemno);
			List<HashMap> list =  DemAPI.getInstance().getList(baseUUID, conditionMap, null);
			for(HashMap map:list){
				if(itemno.equals(map.get("PACTNO"))){
					contractMap.put("CREATENAME", map.get("CREATENAME"));
					contractMap.put("CREATEID", map.get("CREATEID"));
					contractMap.put("CREATEDATE", map.get("CREATEDATE"));
					contractMap.put("CREATE_DEPT_NO", map.get("CREATE_DEPT_NO"));
					contractMap.put("CREATE_DEPT_NAME", map.get("CREATE_DEPT_NAME"));
					contractMap.put("PACTTITLE", map.get("PACTTITLE"));
					contractMap.put("PACTNO", map.get("PACTNO"));
					contractMap.put("FATHERPACTNO", map.get("FATHERPACTNO"));
					contractMap.put("PACTNATURE", map.get("PACTNATURE"));
					contractMap.put("PACTTYPE", map.get("PACTTYPE"));
					contractMap.put("PACTOWNER", map.get("PACTOWNER"));
					contractMap.put("PACTPARTY", map.get("PACTPARTY"));
					contractMap.put("PACTSUN", map.get("PACTSUN"));
					contractMap.put("CURRENCY", map.get("CURRENCY"));
					contractMap.put("PARIT", map.get("PARIT"));
					contractMap.put("CONVERT", map.get("CONVERT"));
					contractMap.put("SAYTOTAL", map.get("SAYTOTAL"));
					contractMap.put("CONTRACTDATE", map.get("CONTRACTDATE"));
					contractMap.put("FROMDATE", map.get("FROMDATE"));
					contractMap.put("STATE", map.get("STATE"));
					contractMap.put("ADMIN", map.get("ADMIN"));
					contractMap.put("ATTACHMENT", map.get("ATTACHMENT"));
					contractMap.put("TAG", map.get("TAG"));
					contractMap.put("TEXT", map.get("TEXT"));
					contractMap.put("instanceId", map.get("INSTANCEID"));
					contractMap.put("SAYTOTAL", map.get("SAYTOTAL"));
					contractMap.put("PARITIES", map.get("PARITIES"));
						Object obj = map.get("ATTACHMENT");
						if(obj!=null){
							StringBuffer attachhtml = new StringBuffer();
							attachhtml.append("<ul class=\"attachDiv\">");
							String attachstr = ObjectUtil.getString(obj);
							String[] attachlist = attachstr.split(",");
							for(String tmp:attachlist){
								if(tmp.trim().equals(""))continue;
								FileUpload fu = FileUploadAPI.getInstance().getFileUpload(tmp);
								if(fu!=null){
									String filename = fu.getFileSrcName();
									if(filename.length()>15){
										filename = filename.substring(0,15)+"...";
									}
									attachhtml.append("<li><a href=\"uploadifyDownload.action?fileUUID=").append(fu.getFileId()).append("\" target=\"_blank\"><img src=\"iwork_img/attach.gif\">").append(filename).append("</a></li>");
								}
							}
							attachhtml.append("</ul>");
							contractMap.put("ATTACHMENT", attachhtml.toString());
						}
				}
			}
		}
		return contractMap;
	}
	/**
	 * 取合同管理员
	 * @return
	 */
	public HashMap getFatherpactno(){
		HashMap conditionMap = new HashMap();
		String pactno=null;
		conditionMap.put("FATHERPACTNO", "[空]");
		List<HashMap> list =  DemAPI.getInstance().getList(baseUUID, conditionMap, null);
		for(HashMap map:list){
			pactno+=map.get("PACTNO")+","; 
		}
		return conditionMap;
		
	}
	/**
	 * 获取合同管理员
	 * @return
	 */
	public HashMap getContractOptions(){
		HashMap hashMap = new HashMap();
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		//获取设置表中合同管理员名单
		List<HashMap> optionsList =  DemAPI.getInstance().getList(optionsUUID, hashMap, null);
		String adminname = "";
		if(optionsList!=null){
			for(HashMap map:optionsList){
				adminname=String.valueOf(map.get("ADMINNAME"));
			}
		}
		String userid=uc.get_userModel().getUserid();
		if(adminname!=null){
			String[] adminList = adminname.split(",");
			Long isSub = new Long(0);
			for(String adminstr:adminList){
				if(adminstr.trim().equals("")){
	 				continue;
	 			}
	 			String admin1 = UserContextUtil.getInstance().getUserId(adminstr);
	 			//对比如果登录账号在合同管理员名单则是合同管理员
				if(admin1.equals(userid)){
					isSub = new Long(1);
					hashMap.put("IS_SUB", isSub);
					return hashMap;
				}else{
					hashMap.put("IS_SUB", new Long(0));
				}
			}
		}
		return hashMap;
	}
	
	/**
	 * 获取合同的所有金额
	 */
	public HashMap getFundAmount(Long instanceId){
		HashMap hashMap = new HashMap();
		//获取合同执行计划审批的所有金额
		long planNum =0l;
		BigDecimal bd = new BigDecimal(planNum);
		List<HashMap> list = ProcessAPI.getInstance().getFromSubData(instanceId, htzxsubformkey);
		for(HashMap map:list){
			if(map.get("AMOUNT")!=null){
				Long tmp = Long.parseLong(map.get("AMOUNT").toString());
				bd=bd.add(new BigDecimal(tmp));
			}
		}
		//获取合同发票计划审批的所有金额
		long planNum1 =0l;
		BigDecimal bd1 = new BigDecimal(planNum1);
		List<HashMap> list1 = ProcessAPI.getInstance().getFromSubData(instanceId, htfpsubformkey);
		for(HashMap map:list1){
			if(map.get("AMOUNT")!=null){
				Long tmp = Long.parseLong(map.get("AMOUNT").toString());
				bd1=bd1.add(new BigDecimal(tmp));
			}
		}
		hashMap.put("AMOUNT", bd.add(bd1).doubleValue());
		return hashMap;
	}
	
	
	/**
	 * 
	 * 按客户统计查询
	 * @return
	 */
	
	public List<HashMap> getqueryCustome(String pactOwner,String ownerNo,String year,String contrctDate,String fromDate,int pageNumber,int pageSize){
		HashMap conditionMap = new HashMap();
		//获取合同类型
	    conditionMap.put("PACTTYPE",1);
		//获取当前年份
		conditionMap.put("YEAR", year);
		//获取合同甲方
		conditionMap.put("PACTOWNER", pactOwner);
		//获取合同甲方编号
		conditionMap.put("OWNERNO", ownerNo);
		//获取合同开始日期
		conditionMap.put("CONTRACTDATE",DBUtil.convertShortDate(contrctDate));
		//获取合同截止日期
		conditionMap.put("FROMDATE", DBUtil.convertShortDate(fromDate));
		List params = new ArrayList();
        List<HashMap> allList = new ArrayList<HashMap>();
        String finalSql = null;
	    allList = new ArrayList<HashMap>();
		HashMap starMap =null;
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet rset = null;
		try {
			conn = DBUtil.open();
			StringBuffer sql = new StringBuffer("");
			if(pageNumber != 0 && pageSize != 0){
				sql.append("select * from(select IWORK_CM_BASE.*, rownum rn from (");
					sql.append("select t.OWNERNO as userno,t.PACTOWNER as username ,count(*) as shuliang,nvl(sum(t.pactsun),0) as zje,sum(m.AMOUNT) as ysje,sum(tt.AMOUNT) as fpje from IWORK_CM_BASE t left join IWORK_CM_REAL_M m on t.pactno = m.pactno left join IWORK_CM_REAL_T tt on t.pactno=tt.pactno where PACTTYPE=1");
					if(conditionMap.get("YEAR") != null&&!"".equals(conditionMap.get("YEAR"))){
						sql.append(" and t.YEAR=?");
						params.add(conditionMap.get("YEAR"));
					}
					if(conditionMap.get("PACTOWNER") != null&&!"".equals(conditionMap.get("PACTOWNER"))){
						sql.append(" and t.PACTOWNER  like ?");
						params.add("%"+conditionMap.get("PACTOWNER")+"%");
					}
					if(conditionMap.get("OWNERNO") != null&&!"".equals(conditionMap.get("OWNERNO"))){
						sql.append(" and t.OWNERNO  like ?");
						params.add("%"+conditionMap.get("OWNERNO")+"%");
					}
					if(conditionMap.get("CONTRACTDATE") != null&&!"".equals(conditionMap.get("CONTRACTDATE"))){
						sql.append(" and t.CONTRACTDATE=?");
						params.add(conditionMap.get("CONTRACTDATE"));
					}
					if(conditionMap.get("FROMDATE")!= null&&!"".equals(conditionMap.get("FROMDATE"))){
						sql.append(" and t.FROMDATE=?");
						params.add(conditionMap.get("FROMDATE"));
					}
					sql.append(" group by t.OWNERNO,t.PACTOWNER");
				sql.append(") IWORK_CM_BASE where rownum <= ?) where rn > ?");
				params.add(pageSize*pageNumber);
				params.add((pageNumber-1)*pageSize);
			}
			//String sql = "select t.OWNERNO as userno,t.PACTOWNER as username ,count(*) as shuliang,nvl(sum(t.pactsun),0) as zje,sum(m.AMOUNT) as ysje,sum(tt.AMOUNT) as fpje from IWORK_CM_BASE t left join IWORK_CM_REAL_M m on t.pactno = m.pactno left join IWORK_CM_REAL_T tt on t.pactno=tt.pactno where 1=1 group by t.OWNERNO,t.PACTOWNER";
			stat = conn.prepareStatement(sql.toString());
			for (int i = 0; i < params.size(); i++) {
				stat.setObject(i+1, params.get(i));
			}
			rset = stat.executeQuery();
			while(rset.next()){
				Double  wskje=0.0;
				starMap = new HashMap();
				starMap.put("OWNERNO", rset.getString("USERNO"));//客户编号
				starMap.put("PACTOWNER", rset.getString("USERNAME"));//客户名称
				starMap.put("COUNTS", rset.getString("SHULIANG"));//合同数量
				starMap.put("PACTSUNS", rset.getString("ZJE"));//合同总金额
				starMap.put("YSZK", rset.getString("YSJE"));//已收款金额
				starMap.put("FPJE", rset.getString("FPJE"));//发票金额
				if(starMap.get("YSZK")==null||"".equals(starMap.get("YSZK"))){
					
					starMap.put("YSZK", 0);//已收款金额
				}
				//获取未收款金额
				 wskje=CalculateUtil.cToDouble(starMap.get("PACTSUNS"))-CalculateUtil.cToDouble(starMap.get("YSZK"));
				starMap.put("NOPAY", wskje);//未还款金额
				allList.add(starMap);
			}
		} catch (Exception e) {
			logger.error(e,e);
		}finally{
			DBUtil.close(conn, stat, rset);
		}	
		      return allList;
	}
	/**
	 * 获取查询操作的总记录条数
	 * @author 杨连峰
	 * 
	 * @return
	 */
	public int getCustomeSize(String pactOwner,String ownerNo,String contrctDate,String fromDate,String year){
		HashMap conditionMap = new HashMap();
		
		//获取当前年份
		conditionMap.put("YEAR", year);
		//获取合同甲方
		conditionMap.put("PACTOWNER", pactOwner);
		//获取合同甲方编号
		conditionMap.put("OWNERNO", ownerNo);
		//获取合同开始日期
		conditionMap.put("CONTRACTDATE", DBUtil.convertShortDate(contrctDate));
		//获取合同截止日期
		conditionMap.put("FROMDATE", DBUtil.convertShortDate(fromDate));
		List params = new ArrayList();
        List<HashMap> allList = new ArrayList<HashMap>();
        String finalSql = null;
	    allList = new ArrayList<HashMap>();
		HashMap starMap =null;
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet rset = null;
		try {
			conn = DBUtil.open();
			StringBuffer sql = new StringBuffer("select t.OWNERNO as userno,t.PACTOWNER as username ,count(*) as shuliang,nvl(sum(t.pactsun),0) as zje,sum(m.AMOUNT) as ysje,sum(tt.AMOUNT) as fpje from IWORK_CM_BASE t left join IWORK_CM_REAL_M m on t.pactno = m.pactno left join IWORK_CM_REAL_T tt on t.pactno=tt.pactno where PACTTYPE=1");
			if(conditionMap.get("YEAR") != null&&!"".equals(conditionMap.get("YEAR"))){
				sql.append(" and t.YEAR=?");
				params.add(conditionMap.get("YEAR"));
			}
			if(conditionMap.get("PACTOWNER") != null&&!"".equals(conditionMap.get("PACTOWNER"))){
				sql.append(" and t.PACTOWNER  like ?");
				params.add("%"+conditionMap.get("PACTOWNER")+"%");
			}
			if(conditionMap.get("OWNERNO") != null&&!"".equals(conditionMap.get("OWNERNO"))){
				sql.append(" and t.OWNERNO  like ?");
				params.add("%"+conditionMap.get("OWNERNO")+"%");
			}
			if(conditionMap.get("CONTRACTDATE") != null&&!"".equals(conditionMap.get("CONTRACTDATE"))){
				sql.append(" and t.CONTRACTDATE=?");
				params.add(conditionMap.get("CONTRACTDATE"));
			}
			if(conditionMap.get("FROMDATE")!= null&&!"".equals(conditionMap.get("FROMDATE"))){
				sql.append(" and t.FROMDATE=?");
				params.add(conditionMap.get("FROMDATE"));
			}
			sql.append("group by t.OWNERNO,t.PACTOWNER");
			//String sql = "select t.OWNERNO as userno,t.PACTOWNER as username ,count(*) as shuliang,nvl(sum(t.pactsun),0) as zje,sum(m.AMOUNT) as ysje,sum(tt.AMOUNT) as fpje from IWORK_CM_BASE t left join IWORK_CM_REAL_M m on t.pactno = m.pactno left join IWORK_CM_REAL_T tt on t.pactno=tt.pactno where 1=1 group by t.OWNERNO,t.PACTOWNER";
			stat = conn.prepareStatement(sql.toString());
			for (int i = 0; i < params.size(); i++) {
				stat.setObject(i+1, params.get(i));
			}
			rset = stat.executeQuery();
			while(rset.next()){
				starMap = new HashMap();
				starMap.put("OWNERNO", rset.getString("USERNO"));
				starMap.put("PACTOWNER", rset.getString("USERNAME"));
				allList.add(starMap);
			}
		} catch (Exception e) {
			logger.error(e,e);
		}finally{
			DBUtil.close(conn, stat, rset);
		}	
		      return allList.size();
	}	

	
	/**
	 * 
	 * 按供应商统计查询
	 * @return
	 */
	
	public List<HashMap> getquerySpplier(String partyNo,String pactParty,String year,String contrctDate,String fromDate,int pageNumber,int pageSize){
		HashMap conditionMap = new HashMap();
		//获取当前年份
		conditionMap.put("YEAR", year);
		//获取合同乙方编号
		conditionMap.put("PARTYNO", partyNo);
		//获取合同乙方名称
		conditionMap.put("PACTPARTY", pactParty);
		//获取合同开始日期
		conditionMap.put("CONTRACTDATE", DBUtil.convertShortDate(contrctDate));
		//获取合同截止日期
		conditionMap.put("FROMDATE", DBUtil.convertShortDate(fromDate));
		List params = new ArrayList();
        List<HashMap> allList = new ArrayList<HashMap>();
        String finalSql = null;
	    allList = new ArrayList<HashMap>();
		HashMap starMap =null;
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet rset = null;
		try {
			conn = DBUtil.open();
			StringBuffer sql = new StringBuffer("");
			if(pageNumber != 0 && pageSize != 0){
				sql.append("select * from(select IWORK_CM_BASE.*, rownum rn from (");
					sql.append("select t.PARTYNO as userno,t.PACTPARTY as username ,count(*) as shuliang,nvl(sum(t.pactsun),0) as zje,sum(m.AMOUNT) as ysje,sum(tt.AMOUNT) as fpje from IWORK_CM_BASE t left join IWORK_CM_REAL_M m on t.pactno = m.pactno left join IWORK_CM_REAL_T tt on t.pactno=tt.pactno where PACTTYPE=2");
					if(conditionMap.get("YEAR") != null&&!"".equals(conditionMap.get("YEAR"))){
						sql.append(" and t.YEAR=?");
						params.add(conditionMap.get("YEAR"));
					}
					if(conditionMap.get("PACTPARTY") != null&&!"".equals(conditionMap.get("PACTPARTY"))){
						sql.append(" and t.PACTPARTY  like ?");
						params.add("%"+conditionMap.get("PACTPARTY")+"%");
					}
					if(conditionMap.get("") != null&&!"".equals(conditionMap.get("PARTYNO"))){
						sql.append(" and t.PARTYNO like ?");
						params.add("%"+conditionMap.get("PARTYNO")+"%");
					}
					if(conditionMap.get("CONTRACTDATE") != null&&!"".equals(conditionMap.get("CONTRACTDATE"))){
						sql.append(" and t.CONTRACTDATE=?");
						params.add(conditionMap.get("CONTRACTDATE"));
					}
					if(conditionMap.get("FROMDPARTYNOATE")!= null&&!"".equals(conditionMap.get("FROMDATE"))){
						sql.append(" and t.FROMDATE=?");
						params.add(conditionMap.get("FROMDATE"));
					}
					
					sql.append("group by t.PARTYNO,t.PACTPARTY");
				sql.append(") IWORK_CM_BASE where rownum <= ?) where rn > ?");
				params.add(pageSize*pageNumber);
				params.add((pageNumber-1)*pageSize);
			}
			//String sql = "select t.OWNERNO as userno,t.PACTOWNER as username ,count(*) as shuliang,nvl(sum(t.pactsun),0) as zje,sum(m.AMOUNT) as ysje,sum(tt.AMOUNT) as fpje from IWORK_CM_BASE t left join IWORK_CM_REAL_M m on t.pactno = m.pactno left join IWORK_CM_REAL_T tt on t.pactno=tt.pactno where 1=1 group by t.OWNERNO,t.PACTOWNER";
			stat = conn.prepareStatement(sql.toString());
			for (int i = 0; i < params.size(); i++) {
				stat.setObject(i+1, params.get(i));
			}
			rset = stat.executeQuery();
			while(rset.next()){
				Double  wskje=0.0;
				starMap = new HashMap();
				starMap.put("PARTYNO", rset.getString("USERNO"));//客户编号
				starMap.put("PACTPARTY", rset.getString("USERNAME"));//客户名称
				starMap.put("COUNTS", rset.getString("SHULIANG"));//合同数量
				starMap.put("PACTSUNS", rset.getString("ZJE"));//合同总金额
				starMap.put("YFZK", rset.getString("YSJE"));//已收款金额
				starMap.put("FPJE", rset.getString("FPJE"));//发票金额
				if(starMap.get("YFZK")==null||"".equals(starMap.get("YFZK"))){
					
					starMap.put("YFZK", 0);//已收款金额
				}
				//获取未收款金额
				 wskje=CalculateUtil.cToDouble(starMap.get("PACTSUNS"))-CalculateUtil.cToDouble(starMap.get("YFZK"));
				starMap.put("NOPAY", wskje);//未还款金额
				allList.add(starMap);
			}
		} catch (Exception e) {
			logger.error(e,e);
		}finally{
			DBUtil.close(conn, stat, rset);
		}	
		      return allList;
	}
	/**
	 * 获取查询操作的总记录条数（按供应商查询）
	 * @author 杨连峰
	 * 
	 * @return
	 */
	public int getSpplierSize(String pactParty,String partyNo,String contrctDate,String fromDate,String year){
		HashMap conditionMap = new HashMap();
		//获取当前年份
		conditionMap.put("YEAR", year);
		//获取合同乙方编号
		conditionMap.put("PARTYNO", partyNo);
		//获取合同乙方名称
		conditionMap.put("PACTPARTY", pactParty);
		//获取合同开始日期
		conditionMap.put("CONTRACTDATE", DBUtil.convertShortDate(contrctDate));
		//获取合同截止日期
		conditionMap.put("FROMDATE", DBUtil.convertShortDate(fromDate));
		List params = new ArrayList();
        List<HashMap> allList = new ArrayList<HashMap>();
        String finalSql = null;
	    allList = new ArrayList<HashMap>();
		HashMap starMap =null;
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet rset = null;
		try {
			conn = DBUtil.open();
			StringBuffer sql = new StringBuffer("select t.PARTYNO as userno,t.PACTPARTY as username ,count(*) as shuliang,nvl(sum(t.pactsun),0) as zje,sum(m.AMOUNT) as ysje,sum(tt.AMOUNT) as fpje from IWORK_CM_BASE t left join IWORK_CM_REAL_M m on t.pactno = m.pactno left join IWORK_CM_REAL_T tt on t.pactno=tt.pactno where PACTTYPE=2");
			if(conditionMap.get("YEAR") != null&&!"".equals(conditionMap.get("YEAR"))){
				sql.append(" and t.YEAR=?");
				params.add(conditionMap.get("YEAR"));
			}
			if(conditionMap.get("PACTPARTY") != null&&!"".equals(conditionMap.get("PACTPARTY"))){
				sql.append(" and t.PACTPARTY  like ?");
				params.add("%"+conditionMap.get("PACTPARTY")+"%");
			}
			if(conditionMap.get("") != null&&!"".equals(conditionMap.get("PARTYNO"))){
				sql.append(" and t.PARTYNO like ?");
				params.add("%"+conditionMap.get("PARTYNO")+"%");
			}
			if(conditionMap.get("CONTRACTDATE") != null&&!"".equals(conditionMap.get("CONTRACTDATE"))){
				sql.append(" and t.CONTRACTDATE=?");
				params.add(conditionMap.get("CONTRACTDATE"));
			}
			if(conditionMap.get("FROMDPARTYNOATE")!= null&&!"".equals(conditionMap.get("FROMDATE"))){
				sql.append(" and t.FROMDATE=?");
				params.add(conditionMap.get("FROMDATE"));
			}
			
			sql.append("group by t.PARTYNO,t.PACTPARTY");
			//String sql = "select t.OWNERNO as userno,t.PACTOWNER as username ,count(*) as shuliang,nvl(sum(t.pactsun),0) as zje,sum(m.AMOUNT) as ysje,sum(tt.AMOUNT) as fpje from IWORK_CM_BASE t left join IWORK_CM_REAL_M m on t.pactno = m.pactno left join IWORK_CM_REAL_T tt on t.pactno=tt.pactno where 1=1 group by t.OWNERNO,t.PACTOWNER";
			stat = conn.prepareStatement(sql.toString());
			for (int i = 0; i < params.size(); i++) {
				stat.setObject(i+1, params.get(i));
			}
			rset = stat.executeQuery();
			while(rset.next()){
				starMap = new HashMap();
				starMap.put("PARTYNO", rset.getString("USERNO"));
				starMap.put("PACTPARTY", rset.getString("USERNAME"));
				allList.add(starMap);
			}
		} catch (Exception e) {
			logger.error(e,e);
		}finally{
			DBUtil.close(conn, stat, rset);
		}	
		      return allList.size();
	}	
	
	
	/**
	 * 
	 * 按应收账款统计查询
	 * @return
	 */
	public List<HashMap> getQueryRceivable(String pactNo,String pactName,String sum,String eventContract,String ContractProperties,String contractNature,String year,int pageSize,int startRow){
		HashMap conditionMap = new HashMap();
		//获取合同属性
		conditionMap.put("PACTBELONG", ContractProperties);
		//获取合同性质
		conditionMap.put("PACTKIND", contractNature);
		//获取当前年份
		conditionMap.put("YEAR", year);
		//获取合同编号
		conditionMap.put("PACTNO", pactNo);
		//获取合同名称
		conditionMap.put("PACTTITLE", pactName);
		//获取合同类型
	    conditionMap.put("PACTTYPE", 1);
		if(!eventContract.equals("") && eventContract!=null && !eventContract.equals("undefined")&& !eventContract.equals("null")){
			Long tmp = Long.parseLong(eventContract);
			conditionMap.put("PACTNATURE",tmp );	
		}
		//获取当前用户是否支持子表显示
 		HashMap optionmap = new HashMap();
 		List<HashMap> optionsList =  DemAPI.getInstance().getList(optionsUUID, optionmap, null);
 		String adminname="";//所有合同管理员
 		String issupport="";//获取是否显示子合同
 		String isadminname="";//是否属于合同管理员
 		if(optionsList!=null){
 			for(HashMap map:optionsList){
 				//获取所有合同管理员
 	 			adminname = String.valueOf(map.get("ADMINNAME"));
 	 			issupport = String.valueOf(map.get("ISSUPPORT"));
 	 		}
 		}
 		//获取当前用户账号是否存在合同管理权限
 		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
 		String userid1=uc.get_userModel().getUserid();
 		//获取合同设置管理员
 		String[] userlist = adminname.split(",");
 		for(String u:userlist){
 			if(u.trim().equals("")){
 				continue;
 			}
 			String userid = UserContextUtil.getInstance().getUserId(u);
 			if(userid!=null && userid.equals(userid1)){
 				isadminname = userid1;
 				break;
 			}else{
 				
 			}
 		}
 		if(isadminname!=null && isadminname.equals(userid1)){
 			
 		}else{
 			conditionMap.put("CREATEID", uc.get_userModel().getUserid());
 		}
		//获取所有合同数据
		List<HashMap> list1 = new ArrayList<HashMap>();
		//conditionMap.put("FATHERPACTNO", "[空]");
		List<HashMap> list = DemAPI.getInstance().getList(baseUUID, conditionMap, null, pageSize, startRow);
		//DemAPI.getInstance().getList(baseUUID, conditionMap, null);
		
		for(HashMap map:list){
			HashMap map1 = new HashMap();
			map1.put("FATHERPACTNO", map.get("PACTNO"));
			list1 =  DemAPI.getInstance().getList(baseUUID, map1, null);
			Long isSub  = new Long(0);
			//判断当前用户是否支持显示子表数据
			if(issupport!=null&&!issupport.equals("")&&issupport.equals("1")){
				if(list1!=null&&list1.size()>0){
					isSub = new Long(1);
					map.put("IS_SUB", isSub);
				}else{
					map.put("IS_SUB", new Long(0));
				}
			}else{
				map.put("IS_SUB", new Long(0));
			}
		}
		if(list!=null && list.size()>0){
			for(HashMap<String,Object> map:list){
				if(Integer.parseInt(map.get("PACTNATURE").toString())==1){
					map.put("CONTACTTYPE", "主合同");
				}else{
					map.put("CONTACTTYPE", "子合同");
				}
				 String  htbh=map.get("PACTNO").toString();
				//以下获取应收账款金额

					if(htbh!=null){
						List<HashMap> listRceivable = null;
						List<HashMap> performList=null;
						//HashMap conditionMap = new HashMap();
						conditionMap.put("PACTNO", htbh);			
						performList =  DemAPI.getInstance().getList(ivoicePerformUUID, conditionMap, null);			
						listRceivable =  DemAPI.getInstance().getList(contractPerformUUID, conditionMap, null);
						long planNum =0l;
						BigDecimal bd = new BigDecimal(planNum);
						BigDecimal bds = new BigDecimal(planNum);
						//获取实际收/付款金额
						for(HashMap hash:listRceivable){
							if(hash.get("AMOUNT")!=null){
								Long tmp = Long.parseLong(hash.get("AMOUNT").toString());
								bd=bd.add(new BigDecimal(tmp));
							}
						}
						//获取发票执行金额
						for(HashMap hash:performList){
							if(hash.get("AMOUNT")!=null){
								Long tmp = Long.parseLong(hash.get("AMOUNT").toString());
								bds=bds.add(new BigDecimal(tmp));
							}
						}
						Long accountsNum=0l;	
						accountsNum=bds.longValue()-bd.longValue();	
						//应收款金额
						map.put("YSZK", accountsNum);
						//已收款金额
						map.put("YSKJE", bds.longValue());
						 //以下获取未收账款金额
						BigDecimal wskje = new BigDecimal(CalculateUtil.cToDouble(map.get("PACTSUN")));
						map.put("WSKJE",wskje.longValue() -bds.longValue());
						
					}	
				
			}
		}
		return list;
	}
	
	/**
	 * 按应收账款统计查询
	 * @author 杨连峰
	 * 
	 * @return
	 */
	public int getQueryRceivableSize(String pactNo,String pactName,String sum,String eventContract,String ContractProperties,String contractNature,String year){
		HashMap conditionMap = new HashMap();
		//获取合同属性
		conditionMap.put("PACTBELONG", ContractProperties);
		//获取合同性质
		conditionMap.put("PACTKIND", contractNature);
		//获取当前年份
		conditionMap.put("YEAR", year);
		//conditionMap.put("FATHERPACTNO", pactNo);
		//获取合同编号
		conditionMap.put("PACTNO", pactNo);
		//获取合同名称
		conditionMap.put("PACTTITLE", pactName);
		//获取合同类型
	    conditionMap.put("PACTTYPE", 1);	  		
		if(!eventContract.equals("") && eventContract!=null && !eventContract.equals("undefined")&& !eventContract.equals("null")){
			Long tmp = Long.parseLong(eventContract);
			conditionMap.put("PACTNATURE",tmp );	
		}
		//获取当前用户是否支持子表显示
 		HashMap optionmap = new HashMap();
 		List<HashMap> optionsList =  DemAPI.getInstance().getList(optionsUUID, optionmap, null);
 		String adminname="";//所有合同管理员
 		String issupport="";//获取是否显示子合同
 		String isadminname="";//是否是合同管理员
 		if(optionsList!=null){
 			for(HashMap map:optionsList){
 				//获取所有合同管理员
 	 			adminname = String.valueOf(map.get("ADMINNAME"));
 	 			issupport = String.valueOf(map.get("ISSUPPORT"));
 	 		}
 		}
 		//获取当前用户账号是否存在合同管理权限
 		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
 		String userid1=uc.get_userModel().getUserid();
 		//获取合同设置管理员
 		String[] userlist = adminname.split(",");
 		for(String u:userlist){
 			if(u.trim().equals("")){
 				continue;
 			}
 			String userid = UserContextUtil.getInstance().getUserId(u);
 			if(userid!=null && userid.equals(userid1)){
 				isadminname = userid1;
 				break;
 			}else{
 				
 			}
 		}
 		if(isadminname!=null && isadminname.equals(userid1)){
 			
 		}else{
 			conditionMap.put("CREATEID", uc.get_userModel().getUserid());
 		}
		List<HashMap> list =  DemAPI.getInstance().getList(baseUUID, conditionMap, null);
		if(list!=null && list.size()>0){
			for(HashMap<String,Object> map:list){
				if(Integer.parseInt(map.get("PACTNATURE").toString())==1){
					map.put("CONTACTTYPE", "主合同");
				}else{
					map.put("CONTACTTYPE", "子合同");
				}
			}
		}
		return list.size();
	}	
	/**
	 * 
	 * 按应付账款统计查询
	 * @return
	 */
	public List<HashMap> getQueryPayList(String pactNo,String pactName,String sum,String eventContract,String ContractProperties,String contractNature,String year,int pageSize,int startRow){
		HashMap conditionMap = new HashMap();
		//获取合同属性
		conditionMap.put("PACTBELONG", ContractProperties);
		//获取合同性质
		conditionMap.put("PACTKIND", contractNature);
		//获取当前年份
		conditionMap.put("YEAR", year);
		//获取合同编号
		conditionMap.put("PACTNO", pactNo);
		//获取合同名称
		conditionMap.put("PACTTITLE", pactName);
		//获取合同类型
	    conditionMap.put("PACTTYPE", 2);
		if(!eventContract.equals("") && eventContract!=null && !eventContract.equals("undefined")&& !eventContract.equals("null")){
			Long tmp = Long.parseLong(eventContract);
			conditionMap.put("PACTNATURE",tmp );	
		}
		//获取当前用户是否支持子表显示
 		HashMap optionmap = new HashMap();
 		List<HashMap> optionsList =  DemAPI.getInstance().getList(optionsUUID, optionmap, null);
 		String adminname="";//所有合同管理员
 		String issupport="";//获取是否显示子合同
 		String isadminname="";//是否属于合同管理员
 		if(optionsList!=null){
 			for(HashMap map:optionsList){
 				//获取所有合同管理员
 	 			adminname = String.valueOf(map.get("ADMINNAME"));
 	 			issupport = String.valueOf(map.get("ISSUPPORT"));
 	 		}
 		}
 		//获取当前用户账号是否存在合同管理权限
 		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
 		String userid1=uc.get_userModel().getUserid();
 		//获取合同设置管理员
 		String[] userlist = adminname.split(",");
 		for(String u:userlist){
 			if(u.trim().equals("")){
 				continue;
 			}
 			String userid = UserContextUtil.getInstance().getUserId(u);
 			if(userid!=null && userid.equals(userid1)){
 				isadminname = userid1;
 				break;
 			}else{
 				
 			}
 		}
 		if(isadminname!=null && isadminname.equals(userid1)){
 			
 		}else{
 			conditionMap.put("CREATEID", uc.get_userModel().getUserid());
 		}
		//获取所有合同数据
		List<HashMap> list1 = new ArrayList<HashMap>();
		//conditionMap.put("FATHERPACTNO", "[空]");
		List<HashMap> list = DemAPI.getInstance().getList(baseUUID, conditionMap, null, pageSize, startRow);
		//DemAPI.getInstance().getList(baseUUID, conditionMap, null);
		
		for(HashMap map:list){
			HashMap map1 = new HashMap();
			map1.put("FATHERPACTNO", map.get("PACTNO"));
			list1 =  DemAPI.getInstance().getList(baseUUID, map1, null);
			Long isSub  = new Long(0);
			//判断当前用户是否支持显示子表数据
			if(issupport!=null&&!issupport.equals("")&&issupport.equals("1")){
				if(list1!=null&&list1.size()>0){
					isSub = new Long(1);
					map.put("IS_SUB", isSub);
				}else{
					map.put("IS_SUB", new Long(0));
				}
			}else{
				map.put("IS_SUB", new Long(0));
			}
		}
		if(list!=null && list.size()>0){
			for(HashMap<String,Object> map:list){
				if(Integer.parseInt(map.get("PACTNATURE").toString())==1){
					map.put("CONTACTTYPE", "主合同");
				}else{
					map.put("CONTACTTYPE", "子合同");
				}
				 String  htbh=map.get("PACTNO").toString();
				//以下获取应收账款金额

					if(htbh!=null){
						List<HashMap> listRceivable = null;
						List<HashMap> performList=null;
						//HashMap conditionMap = new HashMap();
						conditionMap.put("PACTNO", htbh);			
						performList =  DemAPI.getInstance().getList(ivoicePerformUUID, conditionMap, null);			
						listRceivable =  DemAPI.getInstance().getList(contractPerformUUID, conditionMap, null);
						long planNum =0l;
						BigDecimal bd = new BigDecimal(planNum);
						BigDecimal bds = new BigDecimal(planNum);
						//获取实际收/付款金额
						for(HashMap hash:listRceivable){
							if(hash.get("AMOUNT")!=null){
								Long tmp = Long.parseLong(hash.get("AMOUNT").toString());
								bd=bd.add(new BigDecimal(tmp));
							}
						}
						//获取发票执行金额
						for(HashMap hash:performList){
							if(hash.get("AMOUNT")!=null){
								Long tmp = Long.parseLong(hash.get("AMOUNT").toString());
								bds=bds.add(new BigDecimal(tmp));
							}
						}
						Long accountsNum=0l;	
						accountsNum=bds.longValue()-bd.longValue();			
						map.put("YFZK", accountsNum);	
						//已收款金额
						map.put("YFKJE", bds.longValue());
						 //以下获取未收账款金额
						BigDecimal wskje = new BigDecimal(CalculateUtil.cToDouble(map.get("PACTSUN")));
						map.put("WFKJE",wskje.longValue() -bds.longValue());
					}		
				 
			}
		}
		return list;
	}
	
	/**
	 * 按应收账款统计查询
	 * @author 杨连峰
	 * 
	 * @return
	 */
	public int getQueryPay(String pactNo,String pactName,String sum,String eventContract,String ContractProperties,String contractNature,String year){
		HashMap conditionMap = new HashMap();
		//获取合同属性
		conditionMap.put("PACTBELONG", ContractProperties);
		//获取合同性质
		conditionMap.put("PACTKIND", contractNature);
		//获取当前年份
		conditionMap.put("YEAR", year);
		//conditionMap.put("FATHERPACTNO", pactNo);
		//获取合同编号
		conditionMap.put("PACTNO", pactNo);
		//获取合同名称
		conditionMap.put("PACTTITLE", pactName);
		//获取合同类型
	    conditionMap.put("PACTTYPE", 2);	  		
		if(!eventContract.equals("") && eventContract!=null && !eventContract.equals("undefined")&& !eventContract.equals("null")){
			Long tmp = Long.parseLong(eventContract);
			conditionMap.put("PACTNATURE",tmp );	
		}
		//获取当前用户是否支持子表显示
 		HashMap optionmap = new HashMap();
 		List<HashMap> optionsList =  DemAPI.getInstance().getList(optionsUUID, optionmap, null);
 		String adminname="";//所有合同管理员
 		String issupport="";//获取是否显示子合同
 		String isadminname="";//是否是合同管理员
 		if(optionsList!=null){
 			for(HashMap map:optionsList){
 				//获取所有合同管理员
 	 			adminname = String.valueOf(map.get("ADMINNAME"));
 	 			issupport = String.valueOf(map.get("ISSUPPORT"));
 	 		}
 		}
 		//获取当前用户账号是否存在合同管理权限
 		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
 		String userid1=uc.get_userModel().getUserid();
 		//获取合同设置管理员
 		String[] userlist = adminname.split(",");
 		for(String u:userlist){
 			if(u.trim().equals("")){
 				continue;
 			}
 			String userid = UserContextUtil.getInstance().getUserId(u);
 			if(userid!=null && userid.equals(userid1)){
 				isadminname = userid1;
 				break;
 			}else{
 				
 			}
 		}
 		if(isadminname!=null && isadminname.equals(userid1)){
 			
 		}else{
 			conditionMap.put("CREATEID", uc.get_userModel().getUserid());
 		}
		List<HashMap> list =  DemAPI.getInstance().getList(baseUUID, conditionMap, null);
		if(list!=null && list.size()>0){
			for(HashMap<String,Object> map:list){
				if(Integer.parseInt(map.get("PACTNATURE").toString())==1){
					map.put("CONTACTTYPE", "主合同");
				}else{
					map.put("CONTACTTYPE", "子合同");
				}
			}
		}
		return list.size();
	}	
}

