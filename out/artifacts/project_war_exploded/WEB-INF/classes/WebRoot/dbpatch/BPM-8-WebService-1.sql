
/*==============================================================*/
/* Table: SYS_WS_BASEINFO                                       */
/*==============================================================*/
create table SYS_WS_BASEINFO  (
   ID                   NUMBER                          not null,
   WS_TYPE              VARCHAR2(100),
   CONTENT_TYPE         VARCHAR2(100),
   NAME                 VARCHAR2(100),
   URL                  VARCHAR2(100),
   DESCRIPTION          VARCHAR2(1000),
   CHECK_TYPE           VARCHAR2(100),
   PERMIT_IP            VARCHAR2(4000),
   FORBID_IP            VARCHAR2(4000),
   USERNAME             VARCHAR2(100),
   PASSWORD             VARCHAR2(100),
   STATUS               NUMBER,
   UUID                 VARCHAR2(100),
   GROUP_ID             NUMBER,
   constraint PK_SYS_WS_BASEINFO primary key (ID)
);

comment on table SYS_WS_BASEINFO is
'WebService管理';

comment on column SYS_WS_BASEINFO.WS_TYPE is
'接口类型';

comment on column SYS_WS_BASEINFO.STATUS is
'接口状态：开启，关闭';

comment on column SYS_WS_BASEINFO.UUID is
'唯一标识';

/*==============================================================*/
/* Table: SYS_WS_PARAMS                                         */
/*==============================================================*/
create table SYS_WS_PARAMS  (
   ID                   NUMBER                          not null,
   PID                  NUMBER,
   TITLE                VARCHAR2(1000),
   HAVING_NAME          VARCHAR2(100),
   DESCRIPTION          VARCHAR2(1000),
   NAME                 VARCHAR2(100),
   TYPE                 VARCHAR2(100),
   VALUE                VARCHAR2(100),
   REQUIRED             VARCHAR2(100),
   UUID                 VARCHAR2(100),
   INOROUT              VARCHAR2(100),
   ORDER_INDEX          NUMBER,
   constraint PK_SYS_WS_PARAMS primary key (ID)
);

comment on table SYS_WS_PARAMS is
'WebService管理-输入参数设置';

comment on column SYS_WS_PARAMS.UUID is
'唯一标识';

/*==============================================================*/
/* Table: SYS_WS_RU_LOG                                         */
/*==============================================================*/
create table SYS_WS_RU_LOG  (
   ID                   NUMBER                          not null,
   PID                  NUMBER,
   CREATEDATE           DATE,
   SHOWTIME             NUMBER,
   STATUS               NUMBER,
   LOGINFO              VARCHAR2(100),
   UUID                 VARCHAR2(100),
   constraint PK_SYS_WS_RU_LOG primary key (ID)
);

comment on table SYS_WS_RU_LOG is
'WebService管理-日志';

/*==============================================================*/
/* Table: SYS_WS_RU_PARAMS                                      */
/*==============================================================*/
create table SYS_WS_RU_PARAMS  (
   ID                   NUMBER                          not null,
   LOG_ID               NUMBER,
   IN_OR_OUT            VARCHAR2(100),
   TITLE                VARCHAR2(1000),
   NAME                 VARCHAR2(1000),
   TYPE                 VARCHAR2(100),
   VALUE                VARCHAR2(1000),
   UUID                 VARCHAR2(100),
   constraint PK_SYS_WS_RU_PARAMS primary key (ID)
);

comment on table SYS_WS_RU_PARAMS is
'WebService管理-日志';
