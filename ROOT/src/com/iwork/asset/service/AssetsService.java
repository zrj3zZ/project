package com.iwork.asset.service;

import java.util.HashMap;
import java.util.List;

public interface AssetsService {
	/**
	 * 获得资产卡片列表
	 * @param pageSize
	 * @param startRow
	 * @return
	 */
	public List<HashMap> getAssetsCardList(String userId,int pageSize, int startRow);
	
	/**
	 * 获得资产卡片数量
	 * @return
	 */
	public int getAssetsCardSize(String userId);
}
