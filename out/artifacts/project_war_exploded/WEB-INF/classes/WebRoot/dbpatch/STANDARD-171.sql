create table IWORK_MAIL_GROUPLIST  (
   ID                   number                          not null,
   USERID           varchar(100),
   GROUP_TITLE          varchar(512),
   GROUP_DESC          varchar(2000),
   constraint PK_IWORK_MAIL_GROUPLIST primary key (ID)
);




create table IWORK_MAIL_GROUP_ITEM  (
   ID                   number                          not null,
   PID          		number,
   USERID          varchar(128),
   constraint PK_IWORK_MAIL_GROUP_ITEM primary key (ID)
);

