-- Create table
create table PROCESS_RU_SIGNS
(
  ID          number,
  OWNER       varchar2(64),
  CREATE_TIME date,
  ACT_DEF_ID  varchar2(128),
  PRC_DEF_ID  number,
  ACT_STEP_ID varchar2(128),
  TASKID      number,
  INSTANCEID  number,
  EXCUTIONID  number,
  TARGET_USER varchar2(128),
  TITLE       varchar2(512),
  STATUS      number,
  READTIME    date,
  SIGN_TIME   date,
  OPINION     varchar2(128),
  ATTACH      varchar2(2000),
  OPINION_DESC        varchar2(2000)
)
;
-- Create/Recreate primary, unique and foreign key constraints 
alter table PROCESS_RU_SIGNS
  add constraint PROCESS_RU_SIGNS primary key (ID)
  disable;
  
  ALTER TABLE PROCESS_STEP_MAP
ADD  IS_SIGNS NUMBER;

  ALTER TABLE PROCESS_STEP_MAP
ADD  SIGNS_DEF_USER VARCHAR2(2048);