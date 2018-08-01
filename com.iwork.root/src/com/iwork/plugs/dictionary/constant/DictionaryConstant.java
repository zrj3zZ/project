package com.iwork.plugs.dictionary.constant;

public class DictionaryConstant {
	public static String[] dictionaryTab =
	{
		"基本设置",
		"查询条件设置",
		"数据选择设置"
	};
	public static String[] dictionaryUrl =
	{
		"sys_dictionary_design_baseInfo.action",
		"sys_dictionary_condition.action",
		"sys_dictionary_showField.action"
	};
	/**
	 * 单选字典
	 */
	public static final Long DICTIONARY_TYPE_RADIO = new Long(1); 
	/**
	 * 多选字典
	 */
	public static final Long DICTIONARY_TYPE_MULTI = new Long(2); 
	/**
	 * 子表字典
	 */
	public static final Long DICTIONARY_TYPE_SUBGRID = new Long(3); 
	
	
	/**
	 * 条件数据源类型：用户录入
	 */
	public static final Long CONDITIONS_DATA_TYPE_INPUT = new Long(1);
	/**
	 * 条件数据源类型从表单中提取
	 */
	public static final Long CONDITIONS_DATA_TYPE_FORM =  new Long(2);
	/**
	 * 条件数据源类型：从表单中提取后，用户可编辑
	 */
	public static final Long CONDITIONS_DATA_TYPE_FORMINPUT =  new Long(3);
	
	/**
	 * 条件比较类型：等于
	 */
	public static final String CONDITIONS_COMPARE_TYPE_EQUAL = "equal";
	/**
	 * 条件比较类型：大于
	 */
	public static final String CONDITIONS_COMPARE_TYPE_BIGTHAN = "bigThan";
	/**
	 * 条件比较类型：小于
	 */
	public static final String CONDITIONS_COMPARE_TYPE_SMALLTHAN = "smallThan";
	/**
	 * 条件比较类型：大于等于
	 */
	public static final String CONDITIONS_COMPARE_TYPE_BIGEQUAL = "bigEqual";
	/**
	 * 条件比较类型：小于等于
	 */
	public static final String CONDITIONS_COMPARE_TYPE_SMALLEQUAL = "smallEqual";
	/**
	 * 条件比较类型：包含于（模糊匹配）
	 */
	public static final String CONDITIONS_COMPARE_TYPE_FUZZYMATCH = "included(fuzzyMatch)";
	/**
	 * 条件比较类型：包含于（从第一个位置开始模糊匹配）
	 */
	public static final String CONDITIONS_COMPARE_TYPE_FIRSTFUZZYMATCH = "included(firstFuzzyMatch)";
	
	
	/**
	 * 条件字段类型-字符
	 */
	public static final Long CONDITIONS_FIELD_TYPE_STRING = new Long(1);
	/**
	 * 条件字段类型-数值
	 */
	public static final Long CONDITIONS_FIELD_TYPE_INT =  new Long(2);
	/**
	 * 条件字段类型-日期
	 */
	public static final Long CONDITIONS_FIELD_TYPE_DATE =  new Long(3);

}
