package com.iwork.asset.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.iwork.sdk.DemAPI;

public class AssetsServiceImpl implements AssetsService{
	//资产卡片主数据UUID
	private static final String CARDLISTUUID="de88c23443554475bfb8bd1612987d1d";
	
	public List<HashMap> getAssetsCardList(String userId,int pageSize, int startRow) {
		// TODO Auto-generated method stub
		List<HashMap> list = new ArrayList<HashMap>();
		
		//设置查询条件
		HashMap conditionMap = new HashMap();
		conditionMap.put("OUID", userId);
		list = DemAPI.getInstance().getList(CARDLISTUUID, conditionMap, null,pageSize,startRow);
		return list;
	}

	public int getAssetsCardSize(String userId) {
		
		//设置查询条件
		HashMap conditionMap = new HashMap();
		conditionMap.put("OUID", userId);
		return DemAPI.getInstance().getListSize(CARDLISTUUID, conditionMap, null);
	}

}
