package com.ibpmsoft.project.zqb.service;

import com.ibm.icu.text.SimpleDateFormat;
import com.ibpmsoft.project.zqb.dao.ZqbDGCDManageDAO;
import com.iwork.commons.util.DBUtilInjection;
import com.iwork.commons.util.UtilDate;
import com.iwork.core.engine.iform.service.IFormService;
import com.iwork.core.pool.ConnectionPool;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class DGCDService {
    public ZqbDGCDManageDAO getZqbDGCDManageDAO() {
        return zqbDGCDManageDAO;
    }

    public void setZqbDGCDManageDAO(ZqbDGCDManageDAO zqbDGCDManageDAO) {
        this.zqbDGCDManageDAO = zqbDGCDManageDAO;
    }

    private ZqbDGCDManageDAO zqbDGCDManageDAO;

    //根据索引获取所有的信息底稿存档
    public List<Object> getDGCDList(String DAMC, String TYPE, int pageSize, int pageNow){
        List<Object> list=new ArrayList<Object>();
        try {
             list.add(zqbDGCDManageDAO.getDGCDList(DAMC, TYPE, pageSize, pageNow));
             list.add(zqbDGCDManageDAO.totalPage(DAMC, TYPE));
        }catch(Exception e){
            e.printStackTrace();
       }
        return list;
    }

    //添加2018-6-21  17:03
    public boolean InsertDGCD(String  DAMC,String COMPANYNAME,String DABH,String id,String name,String date){
        Integer integer = zqbDGCDManageDAO.InsertDGCD(zqbDGCDManageDAO.getID() + 1, DAMC, COMPANYNAME,DABH,id,name,date);
        if(integer>0) return true;
        return false;
    }

    public Integer getMaxIDDGCD(){
        return zqbDGCDManageDAO.getID();
    }

    //添加2018-6-21  17:03  选择项目的时候查询项目的名称
    public List<HashMap> dgcdInsertSelectPro(String ID,String name, int pageSize, int pageNow) throws Exception {
        int startRow = (pageNow - 1) * pageSize;
        int endRow = pageNow * pageSize;
        String[] sql=new String[12];
        String sql2="";
        String sql3="";
        if(name!=null && !name.equals(""))sql2 += "  AND B.PROJECTNAME like '%"+name+"%'";
        if(pageSize!=0 && pageNow!=0) sql3 += " and l.rn <= "+endRow +" and l.rn > " + startRow+" ";
        //新三板---项目基本情况查询sql
        sql[0]="           select l.* from (        " +
                "          SELECT  B.PROJECTNAME, B.PROJECTNO , rownum rn " +
                "          FROM BD_ZQB_PJ_BASE B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID = S.DATAID" +
                "          WHERE S.FORMID = ( SELECT ID" +
                "               FROM SYS_ENGINE_IFORM" +
                "               WHERE IFORM_TITLE = '项目基本情况')" +
                "               AND B.STATUS = '执行中' " +sql2+"  ) l where 1=1  "+sql3;
        //新三板---定向增发项目
        sql[1]="        select l.* from ( " +
                "       SELECT  B.PROJECTNAME, B.PROJECTNO  , rownum rn " +
                "       FROM BD_ZQB_GPFXXMB B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID = S.DATAID" +
                "       WHERE S.FORMID = ( SELECT ID" +
                "                            FROM SYS_ENGINE_IFORM" +
                "                            WHERE IFORM_TITLE = '定向增发项目')" +
                "           AND B.STATUS = '执行中' " +sql2+"  ) l where 1=1  "+sql3;
        //新三板---并购重组
        sql[2]="        select l.* from (       " +
                "       SELECT  K.CUSTOMERNAME PROJECTNAME, K.CUSTOMERNO PROJECTNO  , rownum rn " +
                "       FROM BD_ZQB_BGZZLXXX B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID = S.DATAID" +
                "       LEFT JOIN BD_ZQB_KH_BASE K ON B.COMPANYNO = K.CUSTOMERNO" +
                "       WHERE S.FORMID = ( SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE = '并购重组项目') " +
                "           AND B.XMZT = '1' " +
                "           AND B.COMPANYNAME = 'xsb' " +sql2+"  ) l where 1=1  "+sql3;
        //新三板---其他项目
        sql[3]="        select l.* from (        " +
                "       SELECT  B.PROJECTNAME, B.PROJECTNO , rownum rn  " +
                "       FROM BD_ZQB_TYXM B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID = S.DATAID " +
                "       WHERE S.FORMID = ( SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE = 'IPO项目') " +
                "           AND B.STATUS = '执行中' " +
                "           AND B.A08 = '其他' " +
                "           AND B.A01 = 'xsb'" +sql2+"  ) l where 1=1  "+sql3;
        //上市公司--首次公开
        sql[4]= "       select l.* from (            " +
                "       SELECT  B.PROJECTNAME, B.PROJECTNO  , rownum rn  " +
                "       FROM BD_ZQB_TYXM B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID = S.DATAID " +
                "       WHERE S.FORMID = ( SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE = 'IPO项目') " +
                "           AND B.STATUS = '执行中'" +
                "           AND B.A08 IN ('IPO'," +
                "                         '改制'," +
                "                         '辅导' )" +
                "           AND B.A01 = 'ssgs' " +sql2+"  ) l where 1=1  "+sql3;
        //上市公司--再融资
        sql[5]= "       select l.* from (            " +
                "       SELECT  B.PROJECTNAME, B.PROJECTNO  , rownum rn " +
                "       FROM BD_ZQB_TYXM B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID = S.DATAID " +
                "       WHERE S.FORMID =  ( SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE = 'IPO项目') " +
                "           AND B.STATUS = '执行中' " +
                "           AND B.A08 IN ( " +
                "                         '增发'," +
                "                         '配股'," +
                "                         '非公开发行'," +
                "                         '可转债')" +
                "           AND B.A01 = 'ssgs' " +sql2+"  ) l where 1=1  "+sql3;
        //上市公司--并购重组
        sql[6]= "       select l.* from (            " +
                "       SELECT K.CUSTOMERNO PROJECTNO, K.CUSTOMERNAME PROJECTNAME , rownum rn " +
                "       FROM BD_ZQB_BGZZLXXX B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID = S.DATAID " +
                "       LEFT JOIN BD_ZQB_KH_BASE K ON B.COMPANYNO = K.CUSTOMERNO " +
                "       WHERE S.FORMID = ( SELECT ID  FROM SYS_ENGINE_IFORM  WHERE IFORM_TITLE = '并购重组项目') " +
                "           AND B.XMZT = '1' " +
                "           AND B.COMPANYNAME = 'ssgs' " +sql2+"  ) l where 1=1  "+sql3;
        //上市公司--其他
        sql[7]= "       select l.* from (           " +
                "       SELECT  B.PROJECTNAME, B.PROJECTNO , rownum rn " +
                "       FROM BD_ZQB_TYXM B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID = S.DATAID " +
                "       WHERE S.FORMID = ( SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE = 'IPO项目') " +
                "           AND B.STATUS = '执行中' " +
                "           AND B.A08 IN ( '其他') " +
                "           AND B.A01 = 'ssgs' " +sql2+"  ) l where 1=1  "+sql3;
        //债券业务--公司债
        sql[8] ="       select l.* from ( " +
                "       SELECT   B.PROJECTNO PROJECTNAME ,  rownum rn ,  B.PROJECTNO PROJECTNO " +
                "       FROM BD_ZQB_TYXM B " +
                "       LEFT JOIN SYS_ENGINE_FORM_BIND S  ON B.ID = S.DATAID " +
                "       WHERE S.FORMID = ( SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE = 'IPO项目') " +
                "           AND B.STATUS = '执行中' " +
                "           AND B.A08 IN ('公司债') " +
                "           AND B.A01 = 'zq' " +sql2+"  ) l where 1=1  "+sql3;
        //债券业务--企业债
        sql[9] ="       select l.* from ( " +
                "       SELECT  B.PROJECTNO PROJECTNAME , rownum rn , B.PROJECTNO PROJECTNO " +
                "       FROM BD_ZQB_TYXM B " +
                "       LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID = S.DATAID " +
                "       WHERE S.FORMID = ( SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE = 'IPO项目') " +
                "           AND B.STATUS = '执行中' " +
                "           AND B.A08 IN ('企业债') " +
                "           AND B.A01 = 'zq' " +sql2+"  ) l where 1=1  "+sql3;
        //债券业务--可交换债
        sql[10] ="      select l.* from ( " +
                "       SELECT  B.PROJECTNO PROJECTNAME , rownum rn , B.PROJECTNO PROJECTNO  " +
                "       FROM BD_ZQB_TYXM B  LEFT JOIN SYS_ENGINE_FORM_BIND S  ON B.ID = S.DATAID " +
                "       WHERE S.FORMID = ( SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE = 'IPO项目') " +
                "           AND B.STATUS = '执行中' " +
                "           AND B.A08 IN ('可交换债') " +
                "           AND B.A01 = 'zq' " +sql2+"  ) l where 1=1  "+sql3;
        //债券业务--其他
        sql[11] ="      select l.* from (   " +
                "       SELECT  B.PROJECTNAME PROJECTNAME , rownum rn , B.PROJECTNO PROJECTNO " +
                "          FROM BD_ZQB_TYXM B  LEFT JOIN SYS_ENGINE_FORM_BIND S  ON B.ID = S.DATAID " +
                "          WHERE S.FORMID =  (SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE = 'IPO项目') " +
                "           AND B.STATUS = '执行中' " +
                "           AND B.A08 IN ('其他') " +
                "           AND B.A01 = 'zq' " +sql2+"  ) l where 1=1  "+sql3;
        try{
            return zqbDGCDManageDAO.dgcdInsertSelectPro(sql[Integer.parseInt(ID)]);
        }catch(Exception e){
            throw new Exception("添加底稿存档中选择项目异常;位置:DGCDService.java");
        }
    }

    //添加2018-6-21  17:03
    public Integer getDGCDSelectMaxID(String ID,String name) throws Exception {
        String[] sql=new String[12];
        String sql2="";
        if(name!=null && !name.equals(""))sql2 += "  AND B.PROJECTNAME like '%"+name+"%'";
        //新三板---项目基本情况查询sql
        sql[0]="        SELECT  count(*) count " +
                "          FROM BD_ZQB_PJ_BASE B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID = S.DATAID" +
                "          WHERE S.FORMID = (SELECT ID" +
                "                             FROM SYS_ENGINE_IFORM" +
                "                            WHERE IFORM_TITLE = '项目基本情况')" +
                "           AND B.STATUS = '执行中' " +sql2;
        //新三板---定向增发项目
        sql[1]="            SELECT  count(*) count " +
                "           FROM BD_ZQB_GPFXXMB B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID = S.DATAID" +
                "           WHERE S.FORMID = (SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE = '定向增发项目')" +
                "           AND B.STATUS = '执行中'" +sql2;
        //新三板---并购重组
        sql[2]="           SELECT  count(*) count " +
                "          FROM BD_ZQB_BGZZLXXX B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID = S.DATAID" +
                "          LEFT JOIN BD_ZQB_KH_BASE K ON B.COMPANYNO = K.CUSTOMERNO" +
                "          WHERE S.FORMID = (SELECT ID FROM SYS_ENGINE_IFORM  WHERE IFORM_TITLE = '并购重组项目') " +
                "           AND B.XMZT = '1' " +
                "           AND B.COMPANYNAME = 'xsb'" +sql2;
        //新三板---其他项目
        sql[3]="           SELECT  count(*) count " +
                "          FROM BD_ZQB_TYXM B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID = S.DATAID " +
                "          WHERE S.FORMID =  (SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE = 'IPO项目') " +
                "           AND B.STATUS = '执行中' " +
                "           AND B.A08 = '其他' " +
                "           AND B.A01 = 'xsb'" +sql2;
        //上市公司--首次公开
        sql[4]= "          SELECT  count(*) count " +
                "          FROM BD_ZQB_TYXM B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID = S.DATAID " +
                "          WHERE S.FORMID =  (SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE = 'IPO项目') " +
                "           AND B.STATUS = '执行中'" +
                "           AND B.A08 IN ('IPO'," +
                "                         '改制'," +
                "                         '辅导' )" +
                "           AND B.A01 = 'ssgs''" +sql2;
        //上市公司--再融资
        sql[5]= "           SELECT  count(*) count " +
                "           FROM BD_ZQB_TYXM B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID = S.DATAID " +
                "           WHERE S.FORMID =   (SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE = 'IPO项目') " +
                "           AND B.STATUS = '执行中' " +
                "           AND B.A08 IN ( " +
                "                         '增发'," +
                "                         '配股'," +
                "                         '非公开发行'," +
                "                         '可转债')" +
                "           AND B.A01 = 'ssgs'" +sql2;
        //上市公司--并购重组
        sql[6]= "           SELECT  count(*) count " +
                "           FROM BD_ZQB_BGZZLXXX B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID = S.DATAID " +
                "           LEFT JOIN BD_ZQB_KH_BASE K ON B.COMPANYNO = K.CUSTOMERNO " +
                "           WHERE S.FORMID = (SELECT ID " +
                "                             FROM SYS_ENGINE_IFORM " +
                "                            WHERE IFORM_TITLE = '并购重组项目') " +
                "           AND B.XMZT = '1' " +
                "           AND B.COMPANYNAME = 'ssgs' " +sql2;
        //上市公司--其他
        sql[7]= "          SELECT  count(*) count " +
                "          FROM BD_ZQB_TYXM B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID = S.DATAID " +
                "          WHERE S.FORMID = (SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE = 'IPO项目') " +
                "           AND B.STATUS = '执行中' " +
                "           AND B.A08 IN ( '其他') " +
                "           AND B.A01 = 'ssgs'" +sql2;
        //债券业务--公司债
        sql[8] ="          SELECT  count(*) count " +
                "          FROM BD_ZQB_TYXM B " +
                "          LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID = S.DATAID " +
                "          WHERE S.FORMID = (SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE = 'IPO项目') " +
                "           AND B.STATUS = '执行中' " +
                "           AND B.A08 IN ('公司债') " +
                "           AND B.A01 = 'zq'" +sql2;
        //债券业务--企业债
        sql[9] ="          SELECT  count(*) count " +
                "          FROM BD_ZQB_TYXM B " +
                "          LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID = S.DATAID " +
                "          WHERE S.FORMID = (SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE = 'IPO项目') " +
                "           AND B.STATUS = '执行中' " +
                "           AND B.A08 IN ('企业债') " +
                "           AND B.A01 = 'zq'" +sql2;
        //债券业务--可交换债
        sql[10] ="          SELECT  count(*) count " +
                "           FROM BD_ZQB_TYXM B  LEFT JOIN SYS_ENGINE_FORM_BIND S  ON B.ID = S.DATAID " +
                "           WHERE S.FORMID = (SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE = 'IPO项目') " +
                "           AND B.STATUS = '执行中' " +
                "           AND B.A08 IN ('可交换债') " +
                "           AND B.A01 = 'zq'" +sql2;
        //债券业务--其他
        sql[11] ="         SELECT  count(*) count " +
                "          FROM BD_ZQB_TYXM B  LEFT JOIN SYS_ENGINE_FORM_BIND S  ON B.ID = S.DATAID " +
                "          WHERE S.FORMID = (SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE = 'IPO项目') " +
                "           AND B.STATUS = '执行中' " +
                "           AND B.A08 IN ('其他') " +
                "           AND B.A01 = 'zq'" +sql2;
        try{
            return zqbDGCDManageDAO.getDGCDSelectProMaxID(sql[Integer.parseInt(ID)]);
        }catch(Exception e){
            throw new Exception("添加底稿存档中选择项目异常;位置:DGCDService.java");
        }
    }

    //上传底稿存档文件2018-6-26  12:03
    public boolean InsertDGCDFile(String  uuid,String  userName,String userID,String size,String XMQY_Id,String BD_ZQB_DAJYLCB_Id,String Date){
        Integer dgcdFileID = zqbDGCDManageDAO.getDGCDFileID();
        if(dgcdFileID!=null) dgcdFileID++;
        else dgcdFileID=1;
        Integer integer = zqbDGCDManageDAO.InsertDGCDFile( dgcdFileID,  uuid,  userName, userID, size, XMQY_Id, BD_ZQB_DAJYLCB_Id, Date);
        if(integer>0) return true;
        return false;
    }
    //根据目录查询所有的信息 2018-6-26
    public List<HashMap> selectDGCDFile(String XMQYID,String DAJYLCB_ID){
        List<HashMap> list=null;
        try{
            list = zqbDGCDManageDAO.selectDGCDFile(XMQYID,DAJYLCB_ID);
        }catch(Exception e){
            e.printStackTrace();
        }
        return list;
    }
    //根据目录ID查询目录名称 2018-6-26
    public String getXMQYName(String XMQYID){
        String str="";
        try{
            str = zqbDGCDManageDAO.getXMQYName(XMQYID);
        }catch(Exception e){
            e.printStackTrace();
        }
        return str;
    }
    //根据目录ID查询目录名称 2018-6-26
    public List<HashMap> getXMQYParrentName(String XMQYID){
        try{
            return zqbDGCDManageDAO.getXMQYParrentName(XMQYID);
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }
    //根据uuid的数组删除信息 2018-6-22
    public List<HashMap>  deleteDGCDFile(String[] uuid) {
        if(uuid.length<=0){
            return null;
        }
        List<HashMap> deleteOrDownloadSource = null;
        try {
            deleteOrDownloadSource = zqbDGCDManageDAO.getDeleteOrDownloadSource(uuid);
            Integer integer = zqbDGCDManageDAO.deleteDGCDZQB(uuid);
            Integer integer1 = zqbDGCDManageDAO.deleteDGCDSYS(uuid);
            if (integer < 0 || integer1 < 0) throw new Exception("底稿存档文件删除错误");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return deleteOrDownloadSource;
    }
    //根据uuid的数组获取下载信息并下载 2018-6-22
    public List<HashMap> downloadDGCDFile(String[] uuid){
        List<HashMap> list=new ArrayList<HashMap>();
        try{
            list=( zqbDGCDManageDAO.getDeleteOrDownloadSource(uuid));
        }catch(Exception e){
            e.printStackTrace();
        }
        return list;
    }
    //根据BD_ZQB_DAJYLCB获取DAMC 2018-6-22
    public List<HashMap> getDAMCToDAJYLCBID(String ID){
        List<HashMap>  str =null;
        try{
            str=( zqbDGCDManageDAO.getDAMCToDAJYLCBID(ID));
        }catch(Exception e){
            e.printStackTrace();
        }
        return str;
    }
    //根据项目ID下载信息并下载 2018-6-28
    public List<HashMap> downloadDGCDFileForDGCDID(String DGCDID){
        List<HashMap> list=new ArrayList<HashMap>();
        try{
            list=( zqbDGCDManageDAO.downloadDGCDFileForDGCDID(DGCDID));
        }catch(Exception e){
            e.printStackTrace();
        }
        return list;
    }
    //根据项目ID获取文件的真实name 2018-6-28
    public List<HashMap> getDGCDIDFileName(String DGCDID){
        List<HashMap> list=new ArrayList<HashMap>();
        try{
            list=( zqbDGCDManageDAO.getDGCDIDFileName(DGCDID));
        }catch(Exception e){
            e.printStackTrace();
        }
        return list;
    }
    //查询上传表的信息selectBD_ZQB_ZKNHXZRYB
    public List<HashMap> selectBD_ZQB_ZKNHXZRYB(String XMQYID,String DGCDID){
        List<HashMap> list=new ArrayList<HashMap>();
        try{
            list=( zqbDGCDManageDAO.selectBD_ZQB_ZKNHXZRYB(XMQYID,DGCDID));
        }catch(Exception e){
            e.printStackTrace();
        }
        return list;
    }
    //删除 2018-6-22
    public boolean updateDGCDURL(String ID,String URL){
        Integer integer = zqbDGCDManageDAO.updateDGCDURL(ID, URL);
        if(integer>0)return true;
        return false;
    }
    //修改底稿存档 2018-6-22
    public boolean updateDGCD(String ID,String COMPANYNAME,String DAMC,
                              String DABH,String JYSY,String JYXS,String JCSJ,String GHSJ,String JYRMC){
        Integer integer = zqbDGCDManageDAO.updateDGCD(ID, COMPANYNAME, DAMC, DABH, JYSY, JYXS, JCSJ, GHSJ, JYRMC);
        if(integer>0)return true;
        return false;
    }
    //删除底稿存档 2018-6-22
    public boolean deleteDGCD(String ID){
        Integer integer = zqbDGCDManageDAO.deleteDGCD(ID);
        if(integer>0)return true;
        return false;
    }
    //锁定底稿存档 2018-6-22
    public boolean lockDGCD(String ID,String JYXS,String userName,String date){
        Integer integer = zqbDGCDManageDAO.lockDGCD(ID, JYXS,userName,date);
        if(integer>0)return true;
        return false;
    }

    //锁表中转移信息到CGXZ表 2018-6-22
    public boolean locaCGXZ(String DGCDID,String COMPANYNAME){
        Integer maxCGXZ_id = zqbDGCDManageDAO.getMaxCGXZ_ID();
        if(maxCGXZ_id==null || maxCGXZ_id == 0 ){
            maxCGXZ_id=1;
        }
        //删除模板表中的数据
        Integer integer = zqbDGCDManageDAO.deleteTo_XMQY_for_DGCDID(COMPANYNAME);
        Integer to_xmqy_for_dgcdid = zqbDGCDManageDAO.getTo_XMQY_for_DGCDID(DGCDID, maxCGXZ_id,COMPANYNAME);
        if(to_xmqy_for_dgcdid<1){
            return false;
        }
        return true;
    }

    //获取底稿存档的全部
    public List<HashMap> getDGCDList(String ID){
        List<HashMap> list=new ArrayList<HashMap>();
        try {
            list=(zqbDGCDManageDAO.getUPDGCD(ID));
        }catch(Exception e){
            e.printStackTrace();
        }
        return list;
    }
    

	private static Logger logger = Logger.getLogger(IFormService.class);

    //导入表数据合并到主表中
    public String inuptExcel(String tablename,String xmlx){
		CallableStatement proc=null;
		String sql="{call proc_gzdgdr(?,?)}";
		Connection conn = open();
		
		try {
			proc = conn.prepareCall(sql);
			proc.setString(1, tablename);
            proc.setString(2, xmlx);
            proc.execute();
			
		} catch (SQLException e) {
			e.printStackTrace();
			return "error";
		} finally{
			close(conn, null, null);
			try {
				proc.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return "success";
	}
	
	/**
	 * excel数据读取
	 * @return
	 */
	public String subSheetExcelImp(File file){
		String msg = "success";
		InputStream is = null ;
		List list = new ArrayList();
		try {
			is = new FileInputStream(file);
				HSSFWorkbook hwk = new HSSFWorkbook(is);
				HSSFSheet sheet = hwk.getSheetAt(0);// 得到book第一个工作薄sheet
				// 第3行定义了导入动作
				int rowIndex = 0;
				HSSFRow row = sheet.getRow(rowIndex);  //获得行 row
				HSSFCell cell = null;
				Hashtable fields = new Hashtable();
				//跳过第一行
					
				String tableName = createTable();
				for (short i = 0; i < row.getLastCellNum(); i++) {
					cell = row.getCell(i);
					if(cell==null||cell.getCellType()==HSSFCell.CELL_TYPE_NUMERIC){
						continue;
					}
					String fieldName = "";
					String temp = cell.getStringCellValue();
					if (temp.indexOf("<") > 0) {
						fieldName = temp.substring(0, cell.getStringCellValue().indexOf("<"));
					} else { 
						fieldName = temp;
					}
					fields.put(new Short(i), fieldName);
				}
				if(fields.size()<1){
					msg = "typeerror";
					return msg;
				}
					// 循环excel的数据
					rowIndex++;
					StringBuffer bindData;
					int dataNum = 0;
					int erroecount = 0;   //导入异常行数
					String erroecountStr = "";   //导入异常行
					for (int i = rowIndex; i <= sheet.getLastRowNum(); i++) {
						row = sheet.getRow(i);
						bindData = new StringBuffer();
						HashMap rowdata = new HashMap();
						// 循环列
						for (short column = 0; column < row.getLastCellNum(); column++) {
							cell = row.getCell(column);
							String value = "";
							if (cell == null) {
								value = "";
							} else {
								if (cell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
									
									if (HSSFDateUtil.isCellDateFormatted(cell)) {// 日期
										value = UtilDate.dateFormat(cell.getDateCellValue());
									} else {
										value = Double.toString(cell.getNumericCellValue());
										// 排除excel的幂
										if (value != null) {
											if (value.toUpperCase().indexOf("E") > 0) {
												//  解决常整型数据出现科学计数法问题
												value = NumberFormat.getNumberInstance().format(cell.getNumericCellValue());
												value = value.replaceAll(",", "");
											}
											// 小数位数
											if (value.indexOf(".0") > 0 && value.lastIndexOf("0") == value.length() - 1) {
												value = value.substring(0, value.length() - 2);
											} 
										} 
									}
								}else if(cell.getCellType() == HSSFCell.CELL_TYPE_FORMULA) {
									try {
										 value = String.valueOf(cell.getNumericCellValue());
									} catch (Exception e) {
										 value = String.valueOf(cell.getRichStringCellValue());
									}
								} else {
									value = cell.getStringCellValue();
									if (value == null)
										value = "";
								}
							}
							value = value.trim();
							rowdata.put(column+"", value);
						}
						
						//i 是行数  准备最后插入使用
						int flage = insertData(rowdata,tableName);
						if(flage<0){
							if(erroecount>0){
								erroecount++;
								erroecountStr += i;
							}else{
								erroecount++;
								erroecountStr += ","+i;
							}
						}
					}
					if(erroecount>0){
						msg = "error";
					}
					if(erroecount>0){
						msg = "第"+erroecountStr+"条数据异常!";
					}
					if(!msg.equals("error")){
						msg = tableName ;
					}
		} catch (FileNotFoundException e) {
			msg = "error";
			logger.error(e,e);
		}catch (Exception e) {
			msg = "error";
			logger.error(e,e);
		}finally {
			try {
				is.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return msg;
	}
	
	/*
	 * 创建数据表 ，存放前缀以及目录名使用
	 * 成功返回表名，  失败返回""
	 */
	public int insertData(Map rowdata,String tableName){
		Connection conn = open();
		PreparedStatement ps = null;
		int num = 0;
		DBUtilInjection d=new DBUtilInjection();
		//创建实体临时表
		String sql = "insert into " + tableName + " (prefix,title) values ('"+rowdata.get("0")+"','"+rowdata.get("1")+"')";
		try {
			ps = conn.prepareStatement(sql);
			num = ps.executeUpdate();
		} catch (Exception e) {
			logger.error(e,e);
			tableName="";
			System.out.println(sql);
			num=-1;
		} finally{
			close(conn, ps, null);
		}
		return num;
	}
	
	/*
	 * 这个里面我要做  
	 * 1.批量添加
	 * 2.然后调用存储过程完成对比以及添加目录
	 */
	public String createTable(){
		Connection conn = open();
		PreparedStatement ps = null;
		int num = 0;
		DBUtilInjection d=new DBUtilInjection();
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");//设置日期格式
		String tableName = "TREE_EXCEL_"+(int)(1+Math.random()*10000)+df.format(new Date());
		//创建实体临时表
		String sql = "create table "+tableName+"(title varchar2(512),prefix varchar2(50))";
		try {
			ps = conn.prepareStatement(sql);
			num = ps.executeUpdate();
		} catch (Exception e) {
			logger.error(e,e);
			tableName="";
			System.out.println(sql);
		} finally{
			close(conn, ps, null);
		}
		return tableName;
	}
	
	/**
	 * 关闭数据库链接新
	 * @param conn
	 * @param ps
	 * @param rs
	 */
	public final static String CN_FILENAME = "/common.properties";// 抓取网站配置文件
	public static synchronized final void close(Connection conn,PreparedStatement ps,ResultSet rs) {
		try {
			if(rs != null){rs.close();}
			if(ps != null){ps.close();}
			if(conn != null){conn.close();}
		} catch (Exception e) {
			logger.error(e,e);
			
		}
	}
    
	public static Connection open(){
	    Connection conn = ConnectionPool.getInstance().getConnection();
	    return conn;
	}
}
