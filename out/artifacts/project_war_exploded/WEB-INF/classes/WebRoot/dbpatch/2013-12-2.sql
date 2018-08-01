-- Create table
create table SYS_GE_PROPERTY
(
  NAME    VARCHAR2(128) not null,
  VALUE   VARCHAR2(512),
  VERSION VARCHAR2(128),
  MEMO    VARCHAR2(512)
)
alter table SYS_GE_PROPERTY
  add constraint SYS_GE_PROPERTY primary key (NAME)
  using index 
  tablespace USERS
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );