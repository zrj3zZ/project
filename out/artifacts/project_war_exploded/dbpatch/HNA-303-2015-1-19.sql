--扩展字段，为流程节点设置增加驳回动作，驳回至申请人后
ALTER TABLE SYS_DICTIONARY_BASEINFO 
ADD IS_DEM NUMBER


ALTER TABLE SYS_DICTIONARY_BASEINFO 
ADD DEM_UUID VARCHAR2(256)