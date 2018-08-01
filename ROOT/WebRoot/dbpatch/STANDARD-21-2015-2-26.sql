create table IWORK_MAIL_DATA
(
  ID           NUMBER,
  IS_SEND      NUMBER,
  MAIL_TYPE    NUMBER,
  IS_IMPORTANT NUMBER,
  MAIL_SIZE    NUMBER,
  CREATE_USER  VARCHAR2(64),
  CREATE_DATE  DATE,
  TOO          VARCHAR2(2048),
  CC           VARCHAR2(2048),
  BCC          VARCHAR2(2048),
  MAIL_FROM    VARCHAR2(2048),
  TITLE        VARCHAR2(512),
  CONTENT      VARCHAR2(2048),
  ATTACHMENTS  VARCHAR2(2048),
  MESSAGEID    NUMBER,
  RELATEDID    NUMBER
)

alter table IWORK_MAIL_DATA
  add constraint IWORK_MAIL_DATA_ID primary key (ID)
  
  create table IWORK_MAIL_BOX
(
  ID           NUMBER,
  USERID      VARCHAR2(100),
  BOXNAME    VARCHAR2(256)
)
alter table IWORK_MAIL_DATA
  add constraint IWORK_MAIL_DATA_ID primary key (ID)
  
  create table IWORK_MAIL_TASK
(
  ID           NUMBER not null,
  BIND_ID      NUMBER,
  OWNER        VARCHAR2(32),
  IS_READ       NUMBER,
  IS_DEL       NUMBER,
  IS_ARCHIVES  NUMBER,
  IS_RE        NUMBER,
  IS_IMPORTANT NUMBER,
  MAIL_SIZE    NUMBER,
  TITLE        VARCHAR2(256),
  CREATE_TIME  DATE,
  READ_TIME    DATE,
  MAIL_BOX     NUMBER
)
alter table IWORK_MAIL_TASK
  add constraint IWORK_MAIL_TASK_ID primary key (ID)
  
  
  -- Create table
create table IWORK_MAIL_FOLDER
(
  ID         NUMBER not null,
  USERID     VARCHAR2(32),
  FOLDERNAME VARCHAR2(32),
  FOLDERMEMO VARCHAR2(256),
  CREATEDATE DATE
)

alter table IWORK_MAIL_FOLDER
  add constraint IWORK_MAIL_FOLDER_ID primary key (ID)
  
    create table IWORK_MAIL_OWNER
(
  ID           NUMBER not null,
  BIND_ID      NUMBER,
  OWNER        VARCHAR2(32),
  TARGET       VARCHAR2(32),
  IS_DEL       NUMBER,
  IS_ARCHIVES  NUMBER,
  IS_RE        NUMBER,
  IS_IMPORTANT NUMBER,
  MAIL_SIZE    NUMBER,
  TITLE        VARCHAR2(256),
  CREATE_TIME  DATE,
  READ_TIME    DATE,
  MAIL_BOX     NUMBER
)
alter table IWORK_MAIL_OWNER
  add constraint IWORK_MAIL_OWNER_ID primary key (ID)
  