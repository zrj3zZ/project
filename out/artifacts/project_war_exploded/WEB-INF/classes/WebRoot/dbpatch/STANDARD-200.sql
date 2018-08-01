
  ALTER TABLE PROCESS_STEP_ROUTE
ADD  ROUTE_PARAM2 VARCHAR2(500);
--创建岗位表
create table ORG_STATION
(
  ID           NUMBER not null,
  STATION_NAME VARCHAR2(64),
  UUID         VARCHAR2(64),
  MEMO         VARCHAR2(512)
)
alter table ORG_STATION
  add constraint ORG_STATION_PK primary key (ID)
--创建岗位实例表
-- Create table
create table ORG_STATION_INS
(
  ID         NUMBER not null,
  TITLE      VARCHAR2(64),
  STATION_ID NUMBER,
  OWNERS     VARCHAR2(2000),
  START_DATE DATE,
  END_DATE   DATE,
  STATUS     NUMBER,
  ORDERINDEX NUMBER
)
alter table ORG_STATION_INS
  add constraint ORG_STATION_INS_PK primary key (ID)
  --创建岗位范围表
  create table ORG_STATION_INS_ITEM
(
  ID             NUMBER not null,
  STATION_INS_ID NUMBER,
  ORGTYPE        VARCHAR2(32),
  VAL            VARCHAR2(512),
  STATION_ID     NUMBER
)

alter table ORG_STATION_INS_ITEM
  add constraint ORG_STATION_INS_ITEM_ID primary key (ID)