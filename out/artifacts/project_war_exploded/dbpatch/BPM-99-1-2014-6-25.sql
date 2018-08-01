create table sys_calendar_base_info(
   id number(20),
   calendar_name varchar2(100),
   describ varchar2(200),
   ts varchar2(30),
   Sun varchar2(2),
   Mon varchar2(2),
   Tues varchar2(2),
   Wed varchar2(2),
   Turs varchar2(2),
   Fri varchar2(2),
   Sat varchar2(2),
   exp_date_from date,
   exp_date_to date,
   status varchar2(2),
   work_time_from number,
   work_time_to number,
   grant_users varchar2(4000),
   calendar_type varchar2(2)
   
);
comment on column sys_calendar_base_info.calendar_name is '日历标题';
comment on column sys_calendar_base_info.describ is '日历描述';
comment on column sys_calendar_base_info.ts is '时间戳';
comment on column sys_calendar_base_info.Sun is '星期日工作状态:1为工作,0为休息';
comment on column sys_calendar_base_info.Mon is '星期一工作状态:1为工作,0为休息';
comment on column sys_calendar_base_info.Tues is '星期二工作状态:1为工作,0为休息';
comment on column sys_calendar_base_info.Wed is '星期三工作状态:1为工作,0为休息';
comment on column sys_calendar_base_info.Turs is '星期四工作状态:1为工作,0为休息';
comment on column sys_calendar_base_info.Fri is '星期五工作状态:1为工作,0为休息';
comment on column sys_calendar_base_info.Sat is '星期六工作状态:1为工作,0为休息';
comment on column sys_calendar_base_info.exp_date_from is '有效日期起始';
comment on column sys_calendar_base_info.exp_date_to is '有效日期结束';
comment on column sys_calendar_base_info.work_time_from is '开始工作时间';
comment on column sys_calendar_base_info.status is '开启状态:1为开启，0为关闭';
comment on column sys_calendar_base_info.work_time_to is '结束工作时间';
comment on column sys_calendar_base_info.grant_users is '授权字符串';
comment on column sys_calendar_base_info.calendar_type is '日期类型';
alter table sys_calendar_base_info add constraint PK_CALENDAR_ID primary key(id);
alter table sys_calendar_base_info add(create_time varchar2(25));

