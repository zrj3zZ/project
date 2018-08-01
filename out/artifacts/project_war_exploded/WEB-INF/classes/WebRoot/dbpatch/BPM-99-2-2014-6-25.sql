
create table sys_calendar_detail_info(
 id number(20),
 c_date date,
 actual_day_type varchar2(2),
 common_day_type varchar2(2),
 calendar_id number(20),
 day_of_week varchar(10),
 ts varchar2(30)
);

alter table sys_calendar_detail_info add constraint PK_CALENDAR_DETAIL_ID primary key(id);
comment on column sys_calendar_detail_info.ID is '主键';
comment on column sys_calendar_detail_info.c_date is '日期';
comment on column sys_calendar_detail_info.common_day_type is '正常日期类型 1:工作日,0:周末';
comment on column sys_calendar_detail_info.actual_day_type is '实际日期类型 1:工作,0:休息';