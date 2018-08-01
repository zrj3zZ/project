
/*==============================================================*/
/* Table: SYS_WS_BASEINFO                                       */
/* 增加两列：是否缓存、缓存时间                                                                                                                               */
/*==============================================================*/
ALTER TABLE SYS_WS_BASEINFO
ADD IS_CACHE NUMBER
ADD CACHE_TIME NUMBER;