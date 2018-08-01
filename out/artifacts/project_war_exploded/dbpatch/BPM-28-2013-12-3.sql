create table PROCESS_STEP_SUMMARY  (
   ID                   number                          not null,
   PRC_DEF_ID           NUMBER,
   ACT_DEF_ID           varchar(100),
   ACT_STEP_ID          varchar(100),
   FIELD_NAME           varchar(100),
   FIELD_TITLE          varchar(100),
   FIELD_WIDTH             NUMBER,
   IS_LINK             NUMBER,
   FORMID               NUMBER,
   METADATAID             NUMBER,
   GROUP_ID             NUMBER,
   ORDER_INDEX          NUMBER,
   constraint PK_PROCESS_STEP_SUMMARY primary key (ID)
);