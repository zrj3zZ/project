
create table sys_letters_detail_info(
 	id number(20),
 	letter_id number(20),
 	check_status varchar2(5),
 	letter_date date,
 	sent_user_id varchar2(100),
 	receive_user_id varchar2(100),
 	create_user_id varchar2(100),
 	sent_user_name varchar2(100),
 	receive_user_name varchar2(100),
 	create_user_name varchar2(100),
 	ts varchar2(24),
 	letter_type varchar2(10),
 	def1 varchar2(100),
 	def2 varchar2(100),
 	def3 number(20),
 	def4 date,
 	def5 varchar2(100),
 	def6 varchar2(100)
);

comment on column sys_letters_detail_info.id is 'ID';
comment on column sys_letters_detail_info.letter_id is '站内信ID';
comment on column sys_letters_detail_info.check_status is '站内信内容';
comment on column sys_letters_detail_info.letter_date is '站内信日期';
comment on column sys_letters_detail_info.ts is '时间戳';
comment on column sys_letters_detail_info.sent_user_id is '发送人ID';
comment on column sys_letters_detail_info.receive_user_id is '接收人ID';
comment on column sys_letters_detail_info.create_user_id is '创建人ID';
comment on column sys_letters_detail_info.letter_type is '站内信类别';
comment on column sys_letters_detail_info.def1 is '备用字段1,文本';
comment on column sys_letters_detail_info.def2 is '备用字段2,文本';
comment on column sys_letters_detail_info.def3 is '备用字段3,数字';
comment on column sys_letters_detail_info.def4 is '备用字段4,日期';
comment on column sys_letters_detail_info.def5 is '备用字段5,文本';
comment on column sys_letters_detail_info.def6 is '备用字段6,文本';

alter table sys_letters_detail_info add constraint PK_LETTER_DETAIL_ID primary key(id);
alter table sys_letters_detail_info add(owner_id varchar2(100));
create index detail_letter_id on sys_letters_detail_info(letter_id);
create index detail_create_user_id_index on sys_letters_detail_info(create_user_id);
create index detail_sent_user_id_index on sys_letters_detail_info(sent_user_id);
create index detail_receive_user_id_index on sys_letters_detail_info(receive_user_id);
create index detail_stauts_index on sys_letters_detail_info(check_status);
create index detail_letter_date_index on sys_letters_detail_info(letter_date);