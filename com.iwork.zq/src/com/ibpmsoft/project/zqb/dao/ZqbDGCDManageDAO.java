package com.ibpmsoft.project.zqb.dao;

import com.iwork.commons.util.DBUTilNew;
import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

;

public class ZqbDGCDManageDAO extends HibernateDaoSupport {
    private final static String CN_FILENAME = "/common.properties";
    private static Logger logger = Logger.getLogger(ZqbDGCDManageDAO.class);

    @SuppressWarnings("unchecked")
    //获取页面的DGCDList
    public List<HashMap> getDGCDList(String DAMC, String TYPE, int pageSize, int pageNow) {
         String sql = new String("SELECT bm.ID ID,bm.DAMC DAMC,bm.COMPANYNAME COMPANYNAME,bm.JYXS JYXS,bm.DABH DABH,bm.COMPANYNO COMPANYNO,bm.WSH WSH , bm.SHTG SHTG, bm.SHZ SHZ ,bm.sum sum  FROM ( select bmm.* , rownum rn from ( " +
                " SELECT  t.ID,t.COMPANYNAME,t.DAMC,t.DABH,t.JYXS,t.COMPANYNO COMPANYNO , to_char(count(1)) sum , " +
                 "                       to_char(sum(case " +
                 "                             when (nvl(ryb.SPZT,'未审核') != '审核中'  and nvl(ryb.SPZT,'未审核') != '审核通过'  ) then " +
                 "                              1 " +
                 "                             else " +
                 "                              0 " +
                 "                           end)) as WSH, " +
                 "                       to_char(sum(case " +
                 "                             when ryb.SPZT = '审核中' then " +
                 "                              1 " +
                 "                             else " +
                 "                              0 " +
                 "                           end)) as SHZ, " +
                 "                       to_char(sum(case " +
                 "                             when ryb.SPZT = '审核通过' then " +
                 "                              1 " +
                 "                             else " +
                 "                              0 " +
                 "                           end)) as SHTG  " +
                 "                  FROM BD_ZQB_DAJYLCB t " +
                 "                  left join BD_ZQB_ZKNHXZRYB ryb on t.ID = ryb.departmentid  where 1=1  ");
         Map map=new HashMap();int i=1;
            if (TYPE != "" && TYPE != null) {
                sql += "and t.COMPANYNAME = ? ";
                map.put(i,TYPE);i++;
            }
            if (DAMC != "" && DAMC != null) {
                sql += "and t.DAMC like  '%"+DAMC+"%'  ";
            }
            sql += "group by  t.ID,t.COMPANYNAME,t.DAMC,t.DABH,t.JYXS,t.COMPANYNO  )  bmm  where  1=1 ";
        if ((pageNow > 0) && (pageSize > 0)) {
            int endRow = pageNow * pageSize;
            sql+=" and rownum <= ? ) bm where 1=1 ";
            map.put(i,endRow);i++;
            int startRow = (pageNow - 1) * pageSize;
            sql+=" and bm.rn >= ? ";
            map.put(i,startRow);i++;
        }

        sql+=" order by bm.DAMC desc ";
        List<String> list=new ArrayList<String>();
        list.add("ID");
        list.add("DAMC");
        list.add("COMPANYNAME");
        list.add("JYXS");
        list.add("DABH");
        list.add("COMPANYNO");
        list.add("WSH");
        list.add("SHTG");
        list.add("SHZ");
        list.add("sum");
        return DBUTilNew.getDataList(list,sql,map);
    }
    //在BD_ZQB_DAJYLCB中根据id查询项目名字
    public List<HashMap> getDAMCToDAJYLCBID(String ID){
        String sql="select DAMC,DABH from BD_ZQB_DAJYLCB where ID = ? ";
        HashMap map=new HashMap();
        map.put(1,ID);
        List list =new ArrayList();
        list.add("DAMC");
        list.add("DABH");
        return DBUTilNew.getDataList(list, sql, map);
    }
    //在BD_ZQB_DAJYLCB中根据id项目中的文件对应的真实文件
    public List<HashMap> getDGCDIDFileName(String ID){
        String sql="select zqb.roleid roleid , sys.FILE_SAVE_NAME uuid,sys.FILE_SRC_NAME name " +
                "   from BD_ZQB_ZKNHXZRYB zqb left join SYS_UPLOAD_FILE sys on zqb.DEPARTMENTNAME = sys.FILE_ID " +
                "where zqb.departmentid  = ? ";
        HashMap map=new HashMap();
        map.put(1,ID);
        List list =new ArrayList();
        list.add("uuid");
        list.add("name");
        list.add("roleid");
        return DBUTilNew.getDataList(list, sql, map);
    }
    //根据项目id查询有关联文件的上传文件信息(BD_ZQB_ZKNHXZRYB)
    public List<HashMap> selectBD_ZQB_ZKNHXZRYB(String XMQYID,String LCBID){
        String sql=" select * from BD_ZQB_ZKNHXZRYB t WHERE 1=1  and t.SPZT != '审核通过' ";
        HashMap map=new HashMap();
        int i=1;
        if(LCBID!=null && !LCBID.equals("")){
            sql+= " and departmentid = ? " ;
            map.put(i++,LCBID);
        }
        if(XMQYID!=null && !XMQYID.equals("")){
            sql+= " and roleid = ? " ;
            map.put(i++,XMQYID);
        }
        List list =new ArrayList();
        list.add("id");
        list.add("userid");
        list.add("username");
        list.add("mobile");//大小
        list.add("email");//
        list.add("roleid");//目录的id
        list.add("departmentname");//文件表的id
        list.add("departmentid");//项目id
        list.add("cjsj");//时间
        return DBUTilNew.getDataList(list, sql, map);
    }
    //获取页面的总数
    public  Integer totalPage(String DAMC, String TYPE){
        String sql = new String("SELECT count(*) as count FROM BD_ZQB_DAJYLCB where 1=1  ") ;
        Map map=new HashMap();int i=1;
        if (DAMC != null && !DAMC.equals("")  ) {
            sql += "and DAMC like '%"+DAMC+"%'  ";
        }
        if ( TYPE != null && !TYPE.equals("") ) {
            sql += "and COMPANYNAME = ? ";
            map.put(i,TYPE);
        }
        return DBUTilNew.getInt("count",sql,map);
    }
    //获取最大的ID 用来添加用户是的ID
    public Integer getID(){
        String sql="select ID from BD_ZQB_DAJYLCB  where  rownum <=1  order by ID DESC";
        int id = DBUTilNew.getInt("ID", sql, null);
        return id;
    }
    //添加DGCD
    public Integer InsertDGCD(Integer  ID,String  DAMC,String COMPANYNAME,String DABH,String userId,String name,String date){
        String sql="insert into BD_ZQB_DAJYLCB(ID,DAMC,COMPANYNAME,DABH,JYRID,JYRMC,GHSJ) values( ? , ? , ? , ? , ? , ? , to_date( '"+date+"' , 'yyyy-mm-dd' )  ) ";
        Map map=new HashMap();
        map.put(1,ID);
        map.put(2,DAMC);
        map.put(3,COMPANYNAME);
        map.put(4,DABH);
        map.put(5,userId);
        map.put(6,name);
        return DBUTilNew.update( sql, map);
    }
    //添加DGCD中选择项目
    public List<HashMap> dgcdInsertSelectPro(String sql){
        List list =new ArrayList();
        list.add("PROJECTNAME");
        list.add("PROJECTNO");
        return DBUTilNew.getDataList(list,sql,null);
    }

    //添加DGCD中选择项目 获取分页最大数量
    public Integer  getDGCDSelectProMaxID(String sql){
        return DBUTilNew.getInt("count",sql,null);
    }

    //修改SYS_UPLOAD_FILE表中的地址
    public Integer updateDGCDURL(String ID,String URL){
        String sql="update SYS_UPLOAD_FILE set FILE_URL = '"+URL+"' WHERE  FILE_ID = '"+ID+"'";
        int update = DBUTilNew.update(sql, null);
        return update;
    }
    //删除SYS_UPLOAD_FILE和BD_ZQB_ZKNHXZRYB文件
    public Integer deleteDGCDSYS(String[] uuid){
        String sql="delete from SYS_UPLOAD_FILE  where  FILE_ID in ( ? ";
        HashMap map=new HashMap();
        map.put(1,uuid);
        for(int i=1;i<uuid.length;i++){
            sql+=",?";
            map.put(i+1,uuid[i]);
        }
        sql+=") AND SPZT != '审核通过' ";
        int update = DBUTilNew.update(sql, map);
        return update;
    }
    //通过目录id查询目录名称
    public String getXMQYName(String XMQYID){
        String sql="select FKJD from BD_ZQB_XMQY where ID = ? ";
        HashMap map=new HashMap();
        map.put(1,XMQYID);
        return DBUTilNew.getDataStr("FKJD", sql, map);
    }
    //通过目录id查询所有目录层次名称
    public List<HashMap> getXMQYParrentName(String XMQYID){
        String sql="  SELECT  ID,FKJD  FROM BD_ZQB_XMQY START WITH ID = ? CONNECT BY ID = PRIOR XYJE ";
        HashMap map=new HashMap();
        map.put(1,XMQYID);
        List list =new ArrayList();
        list.add("ID");
        list.add("FKJD");
        return DBUTilNew.getDataList(list, sql, map);
    }
    //删除SYS_UPLOAD_FILE和BD_ZQB_ZKNHXZRYB文件
    public Integer deleteDGCDZQB(String[] uuid){
        String sql="delete from BD_ZQB_ZKNHXZRYB  where  departmentname in ( ? ";
        HashMap map=new HashMap();
        map.put(1,uuid[0]);
        for(int i=1;i<uuid.length;i++){
            sql+=",?";
            map.put(i+1,uuid[i]);
        }
        sql+=") AND SPZT != '审核通过' ";
        int update = DBUTilNew.update(sql, map);
        return update;
    }
    //根据目录BD_ZQB_XMQY.id查询
    public List<HashMap> selectDGCDFile(String XMQYID,String DAJYLCB_ID){
        String sql ="select USERID , SPZT ,  USERNAME , FILE_SRC_NAME , departmentname , cjsj  from BD_ZQB_ZKNHXZRYB b left join SYS_UPLOAD_FILE s on b.departmentname = s.file_id  WHERE roleid = ? and  departmentid = ?  ";
        HashMap map=new HashMap();
        List list=new ArrayList();
        list.add("USERID");
        list.add("SPZT");
        list.add("USERNAME");
        list.add("departmentname");
        list.add("cjsj");
        list.add("FILE_SRC_NAME");
        map.put(1,XMQYID);
        map.put(2,DAJYLCB_ID);
        return DBUTilNew.getDataList(list,sql, map);
    }
    //根据uuid来获取下载或者删除的URLdownloadDGCD FileForDGCDID
    public List<HashMap> getDeleteOrDownloadSource(String[] uuid){
        String sql="select FILE_URL,FILE_SRC_NAME from SYS_UPLOAD_FILE where  FILE_ID in ( ? ";
        HashMap map=new HashMap();
        map.put(1,uuid[0]);
        for(int i=1;i<uuid.length;i++){
            sql+=",?";
            map.put(i+1,uuid[i]);
        }
        sql+=") AND SPZT != '审核通过' ";
        List list =new ArrayList();
        list.add("FILE_URL");
        list.add("FILE_SRC_NAME");
        return DBUTilNew.getDataList(list,sql, map);
    }
    //获取上传时创建文件夹的信息
    public String uploadFile(String DAJYLCBID){
        String sql="select B.DABH from BD_ZQB_DAJYLCB B WHERE ID = ? ";
        HashMap map=new HashMap();
        map.put(1,DAJYLCBID);
        return DBUTilNew.getDataStr("DABH", sql, map);
    }
    //根据DGCDID来获取下载或者删除的URL
    public List<HashMap> downloadDGCDFileForDGCDID(String DGCDID){
        String sql="select FILE_URL,FILE_SRC_NAME from SYS_UPLOAD_FILE where  FILE_ID in ( select departmentname from BD_ZQB_ZKNHXZRYB where departmentid = ?)  ";
        HashMap map=new HashMap();
        map.put(1,DGCDID);
        List list =new ArrayList();
        list.add("FILE_URL");
        list.add("FILE_SRC_NAME");
        return DBUTilNew.getDataList(list,sql, map);
    }

    //上传DGCD文件
    public Integer InsertDGCDFile(Integer ID,String  uuid,String  userName,String userID,String size,String XMQY_Id,String BD_ZQB_DAJYLCB_Id,String Date){
        String sql="insert into BD_ZQB_ZKNHXZRYB(ID,USERID,USERNAME,MOBILE,ROLEID,DEPARTMENTNAME,DEPARTMENTID,CJSJ) values("+ID+",'"+userID+"','"+userName+"','"+size+"',"+XMQY_Id+",'"+uuid+"',"+BD_ZQB_DAJYLCB_Id+",to_date('"+Date+"','yyyy-mm-dd'))";
        return DBUTilNew.update( sql, null);
    }
    //获取BD_ZQB_ZKNHXZRYB上传表中最大的id
    public Integer getDGCDFileID(){
        String sql="select ID from BD_ZQB_ZKNHXZRYB  where  rownum <=1  order by ID DESC ";
        return DBUTilNew.getInt("ID", sql, null);
    }

    //获取BD_GF_CGXZ表中最大的id
    public Integer getMaxCGXZ_ID(){
        String sql="select ID from BD_GF_CGXZ  where  rownum <=1  order by ID DESC ";
        return DBUTilNew.getInt("ID", sql, null);
    }

    //获取锁表的信息BD_ZQB_ZKNHXZRYB表根据DGCDID并且直接添加
    public Integer  getTo_XMQY_for_DGCDID(String LCBID,Integer ID,String COMPANYNAME){
        String sql="insert into BD_GF_CGXZ(ID,NAME,ZHIWU,CGS,CGBL,XSGFS,WXSGF,KHBH,KHMC) ";
         sql +="select ROWNUM+"+ID+" AS ID, mqy.tbrid as NAME , mqy.tbsj as ZHIWU ,mqy.id as  CGS , mqy.xyje as CGBL , mqy.ddje as XSGFS , "+LCBID+" as WXSGF , mqy.xmlx as KHBH ,  mqy.fkjd as KHMC " +
                "   from  BD_ZQB_XMQY mqy where  xmlx = ?  ";
        HashMap map=new HashMap();
        map.put(1,COMPANYNAME);
        return DBUTilNew.update( sql, map);
    }

    //锁表添加数据之前进行删除数据
    public Integer deleteTo_XMQY_for_DGCDID(String COMPANYNAME){
        String sql="delete from BD_GF_CGXZ where KHBH = ?  ";
        HashMap map=new HashMap();
        map.put(1,COMPANYNAME);
        return DBUTilNew.update(sql, map);
    }

    //修改DGCD
    public Integer updateDGCD(String ID,String COMPANYNAME,String DAMC,
                              String DABH,String JYSY,String JYXS,String JCSJ,String GHSJ,String JYRMC){
        String sql=" update BD_ZQB_DAJYLCB set  ";
        String sql1="";
        if(COMPANYNAME!=null && COMPANYNAME!=""){
            sql1+=" ,COMPANYNAME = '" + COMPANYNAME+"'";
        }
        if(DAMC!=null && DAMC!=""){
            sql1+=" ,DAMC = '" + DAMC+"'";
        }
        if(DABH!=null && DABH!=""){
            sql1+=" ,DABH = '" + DABH+"'";
        }
        if(JYSY!=null && JYSY!=""){
            sql1+=" ,JYSY = '"+JYSY+"'";
        }
        if(JYXS!=null && JYXS!=""){
            sql1+=" ,JYXS = '" +JYXS+"'";
        }
        if(JCSJ!=null && JCSJ!=""){
            sql1+=" ,JCSJ = to_date('"+JCSJ+"','yyyy-mm-dd')";
        }
        if(GHSJ!=null && GHSJ!=""){
            sql1+=" ,GHSJ = to_date('"+GHSJ+"','yyyy-mm-dd')";
        }
        if(JYRMC!=null && JYRMC!="") {
            sql1 += " ,JYRMC =  '" + JYRMC+"'";
        }
        sql1=sql1.substring(2,sql1.length());
        sql1+= " where ID = "+ID ;
        return  DBUTilNew.update(sql + sql1, null);
    }
    //根据ID删除
    public Integer deleteDGCD(String ID){
        String sql="delete from BD_ZQB_DAJYLCB where ID = ? ";
        HashMap hash=new HashMap();
        hash.put(1,ID);
        return DBUTilNew.update(sql, hash);
    }
    //根据ID锁定
    public Integer lockDGCD(String ID,String JYXS,String userName,String date){
        String sql="update BD_ZQB_DAJYLCB set JYXS = ? , JYSY = ? , JCSJ = to_date( ? , 'yyyy-mm-dd' )  where ID = ? ";
        HashMap hash=new HashMap();
        hash.put(1,JYXS);
        hash.put(2,userName);
        hash.put(3,date);
        hash.put(4,ID);
        return DBUTilNew.update(sql, hash);
    }

    //根据ID查找一个DGCD返回到修改页面
    public List<HashMap> getUPDGCD(String ID) {
        String sql = new String(" SELECT t.ID,t.DAMC,t.INSTANCEID,to_char(t.JCSJ,'yyyy-mm-dd ') JCSJ,to_char(t.GHSJ,'yyyy-mm-dd ') GHSJ,t.JYRID,t.JYRMC,t.JYSY,t.COMPANYNAME,t.JYXS,t.DABH,t.COMPANYNO  FROM BD_ZQB_DAJYLCB t  ") ;
        Map map=new HashMap();
            sql += "where ID = ? ";
            map.put(1,ID);
        List<String> list=new ArrayList<String>();
        list.add("ID");
        list.add("DAMC");
        list.add("INSTANCEID");
        list.add("JCSJ");
        list.add("GHSJ");
        list.add("JYRID");
        list.add("JYSY");
        list.add("JYRMC");
        list.add("COMPANYNAME");
        list.add("JYXS");
        list.add("DABH");
        list.add("COMPANYNO");
        return DBUTilNew.getDataList(list,sql,map);
    }
    private SessionFactory sessionFactory;

    public void setSessionFacotry(SessionFactory sessionFacotry) {
        super.setSessionFactory(sessionFacotry);
    }

}
