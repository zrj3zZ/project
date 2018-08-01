--扩展字段，为流程节点设置增加驳回动作，驳回至申请人后
ALTER TABLE process_step_map 
ADD IS_BACK_TO_BACK NUMBER

ALTER TABLE process_step_map 
ADD IS_BACK_SRC NUMBER

ALTER TABLE process_step_map 
ADD IS_BACK_DIY NUMBER


ALTER TABLE process_step_jstrigger 
ADD INIT_JS varchar2(2048)

