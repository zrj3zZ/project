package com.ibpmsoft.project.zqb.event;

import com.iwork.app.schedule.IWorkScheduleInterface;
import com.iwork.app.schedule.ScheduleException;
import com.iwork.commons.util.DBUTilNew;
import com.iwork.commons.util.DBUtil;

import java.io.File;
import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DelCompressionFileEvent implements IWorkScheduleInterface {
    @Override
    public boolean executeBefore() throws ScheduleException {
        return true;
    }

    @Override
    public boolean executeOn() throws ScheduleException {

        SendMsg();
        return true;
    }

    @Override
    public boolean executeAfter() throws ScheduleException {
        return true;
    }
    public void SendMsg(){
        List lables = new ArrayList();
        lables.add("ysbmc");
        lables.add("wjcmc");
        String sql="select s.ysbmc,s.wjcmc from bd_zqb_dbwj s";
        Connection conn = DBUtil.open();
        Statement ps = null;
        try {
            conn.setAutoCommit(false);
            ps = conn.createStatement();
            List<HashMap> list = DBUtil.getDataList(lables,sql, null);
            for (HashMap data : list) {
                String ysbmc=data.get("ysbmc")==null?"":data.get("ysbmc").toString();
                String wjcmc=data.get("wjcmc")==null?"":data.get("wjcmc").toString();
                File targetFile = new File(ysbmc);
                File f = new File(targetFile.getPath());
                if(f.exists())
                    f.delete();
                File wjc = new File(wjcmc);
                if(wjc.exists())
                    deleteDir(wjc);
            }
            ps.executeBatch();
            conn.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            DBUTilNew.update("delete from BD_ZQB_DBWJ ",null);
            DBUtil.close(conn, null, null);
            try {
                if(ps!=null) ps.close();
            } catch (Exception e) {

            }
        }
    }
    private static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i=0; i<children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        return dir.delete();
    }
}
