create table sys_letters_detail_reply(
	id number(30),
	detail_data_id number(30),
	letter_id number(30),
	reply_content varchar2(4000),
	user_id varchar2(40),
	ts varchar2(30),
	def1 varchar2(100),
	def2 varchar2(100),
	def3 varchar2(100),
	def4 varchar2(100),
	def5 date,
	def6 number(25)
);
alter table sys_letters_detail_reply add constraint PK_DETAIL_REPLY_ID primary key(id);
comment on column sys_letters_detail_reply.id is 'ID';
comment on column sys_letters_detail_reply.detail_data_id is '详细信息表ID';
comment on column sys_letters_detail_reply.letter_id is '主要内容表ID';
comment on column sys_letters_detail_reply.reply_content is '回复内容';
comment on column sys_letters_detail_reply.user_id is '人员ID';
comment on column sys_letters_detail_reply.ts is '时间戳';
comment on column sys_letters_detail_reply.def1 is '备注字段1';

create index reply_data_id on sys_letters_detail_reply(detail_data_id);
create index reply_letter_id on sys_letters_detail_reply(letter_id);