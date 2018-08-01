
create table sys_letters_content(
 	id number(20),
 	letter_title varchar2(2000),
 	letter_content varchar2(4000),
 	letter_level varchar2(10),
 	letter_date date,
 	create_user_id varchar2(100),
 	create_user_name varchar2(100),
 	to_user_ids varchar2(4000),
 	ts varchar2(24),
 	letter_type varchar2(10),
 	def1 varchar2(100),
 	def2 varchar2(100),
 	def3 number(20),
 	def4 date,
 	def5 varchar2(100),
 	def6 varchar2(100)
);

comment on column sys_letters_content.id is 'ID';
comment on column sys_letters_content.letter_title is '站内信标题';
comment on column sys_letters_content.letter_content is '站内信内容';
comment on column sys_letters_content.letter_level is '重要级别';
comment on column sys_letters_content.letter_date is '站内信日期';
comment on column sys_letters_content.ts is '时间戳';
comment on column sys_letters_content.letter_type is '站内信类别';
comment on column sys_letters_content.def1 is '备用字段1,文本';
comment on column sys_letters_content.def2 is '备用字段2,文本';
comment on column sys_letters_content.def3 is '备用字段3,数字';
comment on column sys_letters_content.def4 is '备用字段4,日期';
comment on column sys_letters_content.def5 is '备用字段5,文本';
comment on column sys_letters_content.def6 is '备用字段6,文本';

alter table sys_letters_content add constraint PK_LETTER_ID primary key(id);
create index letter_date_index on sys_letters_content(letter_date);
create index create_user_id_index on sys_letters_content(create_user_id);
