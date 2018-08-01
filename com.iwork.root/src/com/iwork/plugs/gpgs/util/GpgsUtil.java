package com.iwork.plugs.gpgs.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.iwork.core.db.DBUtil;
import org.apache.log4j.Logger;

public class GpgsUtil {
	private static Logger logger = Logger.getLogger(GpgsUtil.class);
	private static GpgsUtil instance = null;
	private GpgsUtil()
	{
		
	}
	public static GpgsUtil getInstance()
	{
		if(instance==null)
		{
			instance = new GpgsUtil();
		}
		return instance;
	}
	public List<HashMap> GetGPGSInfo()
	{
		StringBuffer sql = new StringBuffer();		
		sql.append("SELECT GFDM,GSJC,GSQC,DM,DMDH,Mobile,GFGSRQ,YXGSRQ,BGDZ,GPSJ,HYFL,WZ,DH,CZ,ZBQS FROM GPGSXQ ORDER BY GFDM");
		List list=new ArrayList();
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rset = null;
		try
		{
			conn = DBUtil.open();
			stmt=conn.prepareStatement(sql.toString());
			rset=stmt.executeQuery();
			while(rset.next())
			{
				String GFDM=rset.getString("GFDM");	
				String GSJC=rset.getString("GSJC");
				String GSQC=rset.getString("GSQC");
				String DM=rset.getString("DM");				
				String DMDH=rset.getString("DMDH");
				String Mobile=rset.getString("Mobile");
				Date GFGSRQ=rset.getDate("GFGSRQ");
				Date YXGSRQ=rset.getDate("YXGSRQ");
				String BGDZ=rset.getString("BGDZ");
				Date GPSJ=rset.getDate("GPSJ");
				String HYFL=rset.getString("HYFL");
				String WZ=rset.getString("WZ");
				String DH=rset.getString("DH");
				String CZ=rset.getString("CZ");	
				String ZBQS=rset.getString("ZBQS");	
				HashMap hash = new HashMap();
				hash.put("GFDM", GFDM);
				hash.put("GSJC", GSJC);
				hash.put("GSQC", GSQC);
				hash.put("DM", DM);
				hash.put("DMDH", DMDH);
				hash.put("Mobile", Mobile);
				hash.put("YXGSRQ", YXGSRQ);
				hash.put("GFGSRQ", GFGSRQ);
				hash.put("BGDZ", BGDZ);
				hash.put("GPSJ", GPSJ);
				hash.put("HYFL", HYFL);
				hash.put("WZ", WZ);
				hash.put("DH", DH);
				hash.put("CZ", CZ);
				hash.put("ZBQS", ZBQS);
				list.add(hash);
			}
		}
		catch (Exception e) {
			logger.error(e,e);
		} finally {
			DBUtil.close(conn, stmt, rset);
		}
		return list;
	}
	
}
