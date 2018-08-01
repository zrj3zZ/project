package com.ibpmsoft.project.zqb.service;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.util.Region;
import org.apache.struts2.ServletActionContext;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipOutputStream;
import org.springframework.util.FileCopyUtils;

import com.ibpmsoft.project.zqb.dao.ZqbFileUploadCommitDAO;
import com.ibpmsoft.project.zqb.util.ConfigUtil;
import com.ibpmsoft.project.zqb.util.UploadFileNameCodingUtil;
import com.iwork.commons.util.DBUTilNew;
import com.iwork.core.db.DBUtil;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.core.util.ResponseUtil;
import com.iwork.sdk.DemAPI;
public class ZqbFileUploadCommitService {
	private ZqbFileUploadCommitDAO zqbFileUploadCommitDAO;
	private final static String CN_FILENAME = "/common.properties";// 抓取网站配置文件
	public Long getConfig(String parameter) {
		Map<String,String> config=ConfigUtil.readAllProperties(CN_FILENAME);//获取连接网址配置
		long result=0L;
		if(parameter!=null&&!"".equals(parameter)){
			result=Long.parseLong(config.get(parameter));
		}
		return result;
	}
	
	public String getConfigUUID(String parameter) {
		Map<String,String> config=ConfigUtil.readAllProperties(CN_FILENAME);//获取连接网址配置
		String result="";
		if(parameter!=null&&!"".equals(parameter)){
			result=config.get(parameter);
		}
		return result;
	}
	private static Logger logger = Logger.getLogger(ZqbFileUploadCommitService.class);
	public boolean saveFile(String fileUUID) {
		String FILE_UUID=DBUtil.getString("SELECT UUID FROM SYS_DEM_ENGINE WHERE TITLE = '上传资料统计'", "UUID");
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		String userid=uc._userModel.getUserid();
		String username=uc._userModel.getUsername();
		Long instanceid = DemAPI.getInstance().newInstance(FILE_UUID,userid);
		HashMap hashdata = new HashMap();
		hashdata.put("FILEUUID", fileUUID);
		hashdata.put("USERNAME", userid+"["+username+"]");
		boolean saveFormData = DemAPI.getInstance().saveFormData(FILE_UUID, instanceid, hashdata, false);
		return saveFormData;
	}

	public boolean removeFile(String fileUUID) {
		Map params = new HashMap();
		params.put(1,fileUUID);
		Long instanceId = DBUTilNew.getLong("instanceid","select instanceid from BD_ZQB_WJTJ wj left join (select bind.instanceid,bind.dataid from (select ifrom.id,ifrom.metadataid from sys_dem_engine engine left join SYS_ENGINE_IFORM ifrom on engine.formid=ifrom.id where title='上传资料统计') a left join SYS_ENGINE_FORM_BIND bind on a.id=bind.formid and a.metadataid=bind.metadataid) b on wj.id=b.dataid where fileuuid= ? ", params);
		boolean removeFormData = DemAPI.getInstance().removeFormData(instanceId);
		return removeFormData;
	}

	public HashMap getFileCommit(String username,String titleName,String beginSj,String endSj,int pageNumber,int pageSize) {
		HashMap fileCommitList=zqbFileUploadCommitDAO.getFileCommit(username,titleName,beginSj,endSj,pageNumber,pageSize);
		return fileCommitList;
	}
	
	public List<String> getUserNameList(List<HashMap> fileCommitList) {
		HashSet<String> hs = new HashSet<String>();
		List<String> list=new ArrayList<String>();
		for (HashMap map : fileCommitList) {
			hs.add(map.get("USERNAME").toString());
		}
		for (String username : hs) {
			list.add(username);
		}
		return list;
	}

	public List<String> getTitleList(List<HashMap> fileCommitList) {
		HashSet<String> hs = new HashSet<String>();
		List<String> list=new ArrayList<String>();
		for (HashMap map : fileCommitList) {
			hs.add(map.get("TITLE").toString());
		}
		for (String username : hs) {
			list.add(username);
		}
		return list;
	}
	
	public HashMap getFileDetails(String username, String titleName,
			String uploadtime,int pageNow,int pageSize) {
		HashMap fileDetailsList=zqbFileUploadCommitDAO.getFileDetails(username,titleName,uploadtime,pageNow,pageSize);
		return fileDetailsList;
	}
	

	public ZqbFileUploadCommitDAO getZqbFileUploadCommitDAO() {
		return zqbFileUploadCommitDAO;
	}

	public void setZqbFileUploadCommitDAO(
			ZqbFileUploadCommitDAO zqbFileUploadCommitDAO) {
		this.zqbFileUploadCommitDAO = zqbFileUploadCommitDAO;
	}
	
	public Map zqbFileDownloadListChange(String customername,String zqdm,String gn,String sja,String sjb,int pageNumber,int pageSize,String xznr) {
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		String userid = "";
		if(uc != null){
			userid = uc.get_userModel().getUserid();
		}
		List<String> orgroleidlist = getList(userid);
		Map<String, String> config = ConfigUtil.readAllProperties(CN_FILENAME);// 获取连接网址配置
		String zqServer = config.get("zqServer");
		Map map=new HashMap();
        map = zqbFileUploadCommitDAO.zqbFileDownloadListChange(orgroleidlist,customername,zqdm,gn,sja,sjb,zqServer,pageNumber, pageSize,xznr);
		return map;
	}


	public void zqbFileDownloadZip(String filename,String fileuuidlist) {
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		String filepath = request.getSession().getServletContext().getRealPath("/");
		try {
			List<File> filearray = new ArrayList<File>();
			File file = new File(filepath+"iwork_file\\FORM_FILE\\");
			String[] filelist = file.list();
			for (int i = 0; i < filelist.length; i++) {
				filearray.addAll(readfile(filepath+"iwork_file\\FORM_FILE\\"+filelist[i]+"\\",fileuuidlist));
			}
			downLoadFiles(filearray,request,response,filename,fileuuidlist.split("//")[2].split("/-/")[0]);
			File check = new File(fileuuidlist.split("//")[2].split("/-/")[0]);
			del(check);
		}catch (Exception e) {
			logger.error(e,e);
		} 
	}
    //---------------------华丽分割线开始-------------------------
	public List<File> readfile(String filepath,String fileuuidlist) throws FileNotFoundException, Exception {
		List<File> list = new ArrayList<File>();
        try {
        	File file = new File(filepath);
            if (!file.isDirectory()) {
            } else if (file.isDirectory()) {
            	String[] filelist = file.list();
            	for (int i = 0; i < filelist.length; i++) {
            		File readfile = new File(filepath + "\\" + filelist[i]);
            		if (!readfile.isDirectory()) {
            			String[] fileuuidall = fileuuidlist.split("\\\\");
            			for (int k = 0; k < fileuuidall.length; k++) {
            				if(fileuuidall[k]!=null&!"".equals(fileuuidall[k])){
            					String[] fileuuidthisall = fileuuidall[k].split("//");
            					int index = readfile.getAbsolutePath().toString().indexOf(fileuuidthisall[0]);
            					if(index!=-1){
            						try { 
            							int bytesum = 0; 
            							int byteread = 0; 
            							File oldfile = new File(readfile.getAbsolutePath()); 
            							if (oldfile.exists()) { 
            								InputStream inStream = new FileInputStream(readfile.getAbsolutePath());
            								String [] felder = fileuuidthisall[2].split("/-/");
            								File fst;
            								if(felder.length==3){
            									fst = new File((felder[0]==null||felder[0].equals("")?"":felder[0]+"\\")+(felder[1]==null||felder[1].equals("")?"":felder[1]+"\\")+(felder[2]==null||felder[2].equals("")?"":felder[2]+"\\"));
            								}else if(felder.length==2){
            									fst = new File((felder[0]==null||felder[0].equals("")?"":felder[0]+"\\")+(felder[1]==null||felder[1].equals("")?"":felder[1]+"\\"));
            								}else{
            									fst = new File((felder[0]==null||felder[0].equals("")?"":felder[0]+"\\"));
            								}
            								if(!fst.exists()){
            									fst.mkdirs();
            								}
            								File fs = new File(fst.toString()+"\\"+fileuuidthisall[1]);
            								FileOutputStream fsot = new FileOutputStream(fs);
            								byte[] buffer = new byte[1024]; 
            								while ( (byteread = inStream.read(buffer)) != -1) { 
//            									bytesum += byteread; 
            									fsot.write(buffer, 0, byteread);
            									fsot.flush();
            								} 
            								list.add(fs);
            								inStream.close(); 
            								fsot.close();
            							}
            						}catch (Exception e) { 
            							logger.error(e,e); 
            						}
            					}
            				}
            			}
            		} else if (readfile.isDirectory()) {
            			readfile(filepath + "\\" + filelist[i],fileuuidlist);
            		}
            	}
            }
        } catch (FileNotFoundException e) {
        	logger.error(e,e);
        }
        return list;
	}

	/**
	 * 打包下载文件
	 * @param files
	 * @param request
	 * @param response
	 * @param filename
	 * @return
	 * @throws Exception
	 */
    public HttpServletResponse downLoadFiles(List<File> files,HttpServletRequest request, HttpServletResponse response,String filename,String deletefilename)throws Exception {
        try {
        	File[] filess ={
        			new File(deletefilename)
			};
        	String time = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
			File targetFile = new File(filename+time+".zip");
			zip(filess,targetFile);
            return downloadZip(targetFile,response);
        }catch (Exception e) {
                logger.error(e,e);
            }
        return response ;
    }

    public HttpServletResponse downloadZip(File file,HttpServletResponse response) {
        try {
        InputStream fis = new BufferedInputStream(new FileInputStream(file.getPath()));
        byte[] buffer = new byte[fis.available()];
        fis.read(buffer);
        fis.close();
        response.reset();

        OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
        response.setContentType("application/octet-stream");

        response.setHeader("Content-Disposition", "attachment;filename=" + UploadFileNameCodingUtil.StringEncoding(file.getName()));
        toClient.write(buffer);
        toClient.flush();
        toClient.close();
        } catch (Exception e) {
        	logger.error(e,e);
        }finally{
             try {
                    File f = new File(file.getPath());
                    f.delete();
                } catch (Exception e) {
                    logger.error(e,e);
                }
        }
        return response;
    }

    public void zip(File[] files,File targetFile) throws IOException{
		files = toAbs(files);
		targetFile = targetFile.getAbsoluteFile();
		String parentPath = files[0].getParent()+"\\";
		files = eachFiles(files);
		ZipOutputStream zos = new ZipOutputStream(targetFile);
		zos.setEncoding("GBK");
		try{
			byte[] buf = new byte[1024*1024];
		for(File f:files){
			zipFile(f,zos,parentPath,buf);
		}
		
		} catch(Exception e){}finally{
			zos.close();
		}
	}
    private File[] eachFiles(File[] files) {
		List<File>  list = new ArrayList<File>();
		LinkedList<File> tasks = new LinkedList<File>(Arrays.asList(files));
		while( !tasks.isEmpty() ) {
			File task = tasks.remove();
			list.add(task);
			if( !task.isDirectory() ) {
				continue;
			}
			for( File c : task.listFiles() ) {
				tasks.add(c);
			}
		}
		return list.toArray(new File[0]);
	}
    private void doZip(File f, ZipOutputStream zos, byte[] buf) throws Exception {
		FileInputStream fis = new FileInputStream(f);
		int len;
		try{
		while((len=fis.read(buf))!=-1){
			zos.write(buf, 0, len);
		}
		}finally{
			fis.close();
		}
	}
    private void zipFile(File f, ZipOutputStream zos, String parentPath, byte[] buf) throws Exception {
		String filename = f.toString().replace(parentPath, "");
		if(f.isDirectory()){
			filename+="/";
		}
		ZipEntry ze = new ZipEntry(filename);
		zos.putNextEntry(ze);
		try{
		if(f.isFile()){
			doZip(f,zos,buf);
		}
		}finally{
		zos.closeEntry();
		}
	}
    private File[] toAbs(File[] files) {
		ArrayList<File> list = new ArrayList<File>();
		for(File f:files){
			list.add(f.getAbsoluteFile());
		}
		return list.toArray(new File[0]);
	}
    public void del(File file) {
        File[] files = file.listFiles();
        if (files != null)
            for (File f : files)
                del(f);
        file.delete();
    }
    public static List<List<HashMap>> mapToList(Map map) {
        List<List<HashMap>> list = new ArrayList<List<HashMap>>();
        Iterator iter = map.entrySet().iterator(); // 获得map的Iterator
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();

            //取出所有值
            list.add((List<HashMap>) entry.getValue());
        }
        return list;
    }
    //---------------------华丽分割线结束-------------------------
    //---------------------华丽分割线改开始-------------------------
    //新审批意见
    public List<File> createNewExcle(List<HashMap> fileuuid,String zipAndDeleteFilename){
        List<File> filelist = new ArrayList<File>();
        Map track=new HashMap();
        Map<String, List<HashMap>> resultMap = new TreeMap<String, List<HashMap>>();
        for (int i = 0; i < fileuuid.size(); i++) {
            track = fileuuid.get(i);
            if (resultMap.containsKey(track.get("INSTANCEID").toString())) {
                resultMap.get(track.get("INSTANCEID").toString()).add((HashMap) track);
            } else {
                List<HashMap> list = new ArrayList<HashMap>();
                list.add((HashMap) track);
                resultMap.put( track.get("INSTANCEID").toString(), list);
            }
        }
        List<List<HashMap>> tss=mapToList(resultMap);

        for(int i=0;i<tss.size();i++){
            List<HashMap> yxlist=tss.get(i);
            // 第一步，创建一个webbook，对应一个Excel文件
            HSSFWorkbook wb = new HSSFWorkbook();
            // 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
            HSSFSheet sheet = wb.createSheet("审批意见");


            HSSFRow row = sheet.createRow((int) 0);
            row.setHeightInPoints(30);
            // 第四步，创建单元格，并设置值表头 设置表头居中
            HSSFCellStyle style = wb.createCellStyle();
            style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
            style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);

            style.setBorderBottom((short) 1);
            style.setBorderLeft((short) 1);
            style.setBorderRight((short) 1);
            style.setBorderTop((short) 1);
            style.setWrapText(true);
            HSSFCellStyle style1 = wb.createCellStyle();
            style1.setBorderBottom((short) 1);
            style1.setBorderLeft((short) 1);
            style1.setBorderRight((short) 1);
            style1.setBorderTop((short) 1);
            style1.setWrapText(true);
            style1.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
            HSSFCellStyle style2 = wb.createCellStyle();
            style2.setBorderBottom((short) 1);
            style2.setBorderLeft((short) 1);
            style2.setBorderRight((short) 1);
            style2.setBorderTop((short) 1);
            style2.setWrapText(true);
            style2.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
            style2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
            HSSFCellStyle style3 = wb.createCellStyle();
            HSSFFont font = wb.createFont();
            style3.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
            style3.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
            font.setFontHeightInPoints((short) 12);
            font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
            style3.setFont(font);
            HSSFFont font2 = wb.createFont();
            font2.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
            style.setFont(font2);
            //yearCell.setCellStyle(style3);
            HSSFCell cell = row.createCell((short) 0);

            cell.setCellValue("名称");
            cell.setCellStyle(style);
            cell = row.createCell((short) 1);

            cell.setCellValue("日期");
            cell.setCellStyle(style);
            cell = row.createCell((short) 2);

            cell.setCellValue("意见详情");
            cell.setCellStyle(style);
            cell = row.createCell((short) 3);

            cell.setCellValue("意见详情");
            cell.setCellStyle(style);
            cell = row.createCell((short) 4);

            cell.setCellValue("意见详情");
            cell.setCellStyle(style);
            cell = row.createCell((short) 5);

            cell.setCellValue("意见详情");





            HSSFRow row1 = sheet.createRow(1);
            HSSFCell c5 = row1.createCell(2);
            c5.setCellValue(new HSSFRichTextString("操作"));
            c5.setCellStyle(style2);

            HSSFCell c6 = row1.createCell(3);
            c6.setCellValue(new HSSFRichTextString("意见描述"));
            c6.setCellStyle(style2);

            HSSFCell c7 = row1.createCell(4);
            c7.setCellValue(new HSSFRichTextString("办理人"));
            c7.setCellStyle(style2);

            HSSFCell c8 = row1.createCell(5);
            c8.setCellValue(new HSSFRichTextString("办理时间"));
            c8.setCellStyle(style2);


            Region region1 = new Region(0, (short)0, 1, (short)0);
            Region region2 = new Region(0, (short)1, 1, (short)1);
            Region region4 = new Region(0, (short)2, 0, (short)6);

            sheet.addMergedRegion(region1);
            sheet.addMergedRegion(region2);
            sheet.addMergedRegion(region4);

            int n = 2;
            int m = 0;
            Map person = new HashMap();
            for (int j=0;j<yxlist.size();j++){
                Map map = (HashMap) yxlist.get(j);
                row = sheet.createRow((int) n++);


                HSSFCell cell1 = row.createCell((short) 0);
                cell1.setCellValue(map.get("NAME").toString());
                cell1.setCellStyle(style2);
                // 如何记录已显示人员的map里没有记录，或者不等于当前的用户
                if (person.get("NAME") == null|| !person.get("NAME").toString().equals((map.get("NAME") != null ? map.get("NAME").toString() : ""))) {
                    // 单元格合并
                    // 四个参数分别是：起始行，起始列，结束行，结束列
                    if (person.get("NAME") != null) {
                        sheet.addMergedRegion(new Region(Integer.parseInt(person.get("begin").toString()), (short) 0, n - 2,(short) 0));
                        sheet.addMergedRegion(new Region(Integer.parseInt(person.get("begin").toString()), (short) 1, n - 2,(short) 1));
                    }
                    person.put("NAME",	(map.get("NAME") != null ? map.get("NAME").toString() : "")	);
                    person.put("begin", n - 1);
                    // 再把式样设置到cell中：
                }



                HSSFCell cell2 = row.createCell((short) 1);
                cell2.setCellValue(map.get("CREATETIME")==null?"":map.get("CREATETIME").toString());
                cell2.setCellStyle(style2);
                HSSFCell cell3 = row.createCell((short) 2);
                cell3.setCellValue(map.get("ACTION")==null?"":map.get("ACTION").toString());
                cell3.setCellStyle(style2);
                HSSFCell cell4 = row.createCell((short) 3);
                cell4.setCellValue(map.get("CONTENT")==null?"":map.get("CONTENT").toString());
                cell4.setCellStyle(style2);



                HSSFCell cell6 = row.createCell((short) 4);
                cell6.setCellValue(map.get("USERNAME")==null?"":map.get("USERNAME").toString());
                cell6.setCellStyle(style2);
                HSSFCell cell7 = row.createCell((short) 5);
                cell7.setCellValue(map.get("CREATETIME")==null?"":map.get("CREATETIME").toString());
                cell7.setCellStyle(style1);


                m++;
            }
            if(yxlist!=null  && yxlist.size()>0){
                sheet.addMergedRegion(new Region(Integer.parseInt(person.get("begin")==null?"0":person.get("begin").toString()), (short) 0, n - 1, (short) 0));
                sheet.addMergedRegion(new Region(Integer.parseInt(person.get("begin")==null?"0":person.get("begin").toString()), (short) 1, n - 1, (short) 1));
            }
            sheet.setColumnWidth(0, 12000);
            sheet.setColumnWidth(1, 4500);
            sheet.setColumnWidth(2, 4500);
            sheet.setColumnWidth(3, 12000);
            sheet.setColumnWidth(4, 4500);
            sheet.setColumnWidth(5, 4500);

            String filenewpath = (zipAndDeleteFilename==null?"":zipAndDeleteFilename+"/")
                    +(((yxlist.get(0).get("ZQDM")!=null&&!yxlist.get(0).get("ZQDM").toString().equals(""))&&(yxlist.get(0).get("ZQJC")!=null&&!yxlist.get(0).get("ZQJC").toString().equals("")))?(yxlist.get(0).get("ZQDM").toString()+"("+yxlist.get(0).get("ZQJC").toString()+")"+"/"):(yxlist.get(0).get("CUSTOMERNAME")==null?"":yxlist.get(0).get("CUSTOMERNAME").toString()+"/"))
                    +(yxlist.get(0).get("GN")==null?"":(yxlist.get(0).get("GN").toString()+"/"))
                    +(!yxlist.get(0).get("XFMK").toString().contains("/")?yxlist.get(0).get("XFMK").toString()+"/":((yxlist.get(0).get("GN").toString().equals("挂牌项目")||yxlist.get(0).get("GN").toString().equals("定增项目(200人以内)")||yxlist.get(0).get("GN").toString().equals("定增项目(200人以上)")||yxlist.get(0).get("GN").toString().equals("收购项目")||yxlist.get(0).get("GN").toString().equals("重组项目")||yxlist.get(0).get("GN").toString().equals("其他项目")||yxlist.get(0).get("GN").toString().equals("一般性财务顾问项目")?yxlist.get(0).get("XFMK").toString():yxlist.get(0).get("XFMK").toString().substring(0, yxlist.get(0).get("XFMK").toString().indexOf("/")))+"/"));
            filenewpath=filenewpath.replaceAll("(?:\\*|\\?|\"|<|>|:|)","")+"审批意见/";
            File fst = new File(filenewpath);
            if(!fst.exists()){
                fst.mkdirs();
            }
            File fs = new File(filenewpath+"审批意见.xls");
            FileOutputStream out=null;
            try {
                out = new FileOutputStream(fs);
                wb.write(out);
            } catch (Exception e) {
                logger.error(e,e);
            }finally {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            filelist.add(fs);
        }
        return filelist;
    }

    public  List<HashMap> zpbDownLoadItem(int pageSize, int pageNumber){
        return zqbFileUploadCommitDAO.getList(pageSize,pageNumber);
    }
    public int zpbDownLoadItemSize(){
        return zqbFileUploadCommitDAO.getListSize().size();
    }
    /**
     * 压缩附件
     * @param customername
     * @param zqjc
     * @param zqdm
     * @param gn
     * @param sja
     * @param sjb
     * @param xznr
     */
    public void zpbFileYsZip(String customername,String zqjc,String zqdm,String gn,String sja,String sjb,String xznr,String gsmc) {
        UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        String filename = zqdm+"("+zqjc+")";
        String filepath = request.getSession().getServletContext().getRealPath("/");
        String userid = "";
        if(uc != null){
            userid = uc.get_userModel().getUserid();
        }


        List<String> orgroleidlist = getList(userid);
        Map<String, String> config = ConfigUtil.readAllProperties(CN_FILENAME);// 获取连接网址配置
        String zqServer = config.get("zqServer");
        String fileuuidlist="";

        final List<String> orgroleidlists=orgroleidlist;
        final String customernames=customername;
        final String zqjcs=zqjc;
        final String zqdms=zqdm;
        final String gns=gn;
        final String sjas=sja;
        final String sjbs=sjb;
        final String zqServers=zqServer;
        final String xznrs=xznr;

        final String filepaths=filepath;
        final String filenames=filename;
        final String gsmcs =gsmc;
        final UserContext ucs=uc;
        final HttpServletRequest requests =request;
        final HttpServletResponse responses =response;
        Runnable myRunnable = new Runnable(){
            public void run(){
                List<HashMap> fileuuid = zqbFileUploadCommitDAO.zqbFileDownloadListPackage(orgroleidlists,customernames,zqjcs,zqdms,gns,sjas,sjbs,zqServers,customernames,xznrs,0L);
                try {
                    List<File> filearray = new ArrayList<File>();
                    filearray.addAll(readYswjfileChange(fileuuid,filepaths));
                    if(filearray.size()!=0){
                        downLoadYswj(filearray,requests,responses,filenames,filenames,zqjcs,zqdms,gns,sjas,sjbs,gsmcs,ucs);
                        File check = new File(filenames);
                        delChange(check);
                    }
                } catch (Exception e) {
                    logger.error(e,e);
                }
            }
        };
        Thread thread = new Thread(myRunnable);
        thread.start();

    }
    public void dowmfile(String  filename) {
        File file = new File(filename);
        try {
            HttpServletResponse response = ServletActionContext.getResponse();
            HttpServletRequest request=ServletActionContext.getRequest();
            FileInputStream is = new FileInputStream(file);
            response.setContentLength(is.available());
            response.setContentType("application/force-download");//应用程序强制下载
           // response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition", "attachment;filename=" + UploadFileNameCodingUtil.StringEncoding(file.getName()));

            FileCopyUtils.copy(is, response.getOutputStream());

        } catch (Exception ex) {
        }finally{
        }
    }
    public List<File> readYswjfileChange(List<HashMap> fileuuid,String filepath) throws FileNotFoundException, Exception {
        List<File> filelist = new ArrayList<File>();

        List s = new ArrayList();
        for (HashMap filemsg : fileuuid) {
            String fileabsolutelypath = filepath+filemsg.get("FILE_URL");
            File thisfile = new File(fileabsolutelypath);
            if(thisfile.exists()){
                FileChannel inputChannel= new FileInputStream(fileabsolutelypath).getChannel();
                String filenewpath = (((filemsg.get("ZQDM")!=null&&!filemsg.get("ZQDM").toString().equals(""))&&(filemsg.get("ZQJC")!=null&&!filemsg.get("ZQJC").toString().equals("")))?(filemsg.get("ZQDM").toString()+"("+filemsg.get("ZQJC").toString()+")"+"/"):(filemsg.get("CUSTOMERNAME").toString()+"/"))
                        +(filemsg.get("GN")==null?"":(filemsg.get("GN").toString()+"/"))
                        +(filemsg.get("XFMK")==null?"":(filemsg.get("XFMK").toString()+"/"));
                File fst = new File(filenewpath);
                if(!fst.exists()){
                    fst.mkdirs();
                }
                File fs = new File(filenewpath+filemsg.get("FILE_SRC_NAME").toString());
                FileChannel outputChannel = new FileOutputStream(fs).getChannel();
                outputChannel.transferFrom(inputChannel, 0, inputChannel.size());
                inputChannel.close();
                outputChannel.close();
                filelist.add(fs);
            }
        }
        return filelist;
    }
    public HttpServletResponse downLoadYswj(List<File> files,HttpServletRequest request, HttpServletResponse response,String filename,String deletefilename,String zqjc,String zqdm,String gn,String sja,String sjb,String gsmc, UserContext uc )throws Exception {
        try {

            File[] filess ={
                    new File(deletefilename)
            };
            String time = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
            File targetFile = new File(filename+time+".zip");
            zipChange(filess,targetFile);
            Map params=new HashMap();
            params.put(1,zqdm==null?"":zqdm);
            params.put(2,gsmc);
            params.put(3,gn==null?"":gn);
            params.put(4,sja==null?"":sja);
            params.put(5,sjb==null?"":sjb);
            params.put(6,uc._userModel.getUserid());
            params.put(7,uc._userModel.getUsername());
            params.put(8,filename+time+".zip");
            params.put(9,zqjc==null?"":zqjc);
            params.put(10,filename);
            DBUTilNew.update("insert into BD_ZQB_DBWJ values(dbwj_sequence.nextval,?,?,?,?,?,?,?,sysdate,?,?,?)",params);
        }catch (Exception e) {
            logger.error(e,e);
        }
        return response ;
    }
    	
    public void zqbFileDownloadZipChange2(String customername,String zqjc,String zqdm,String gn,String sja,String sjb,String xznr) {
    	UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
    	HttpServletRequest request = ServletActionContext.getRequest();
    	HttpServletResponse response = ServletActionContext.getResponse();
    	String filename = zqdm+"("+zqjc+")";
    	String filepath = request.getSession().getServletContext().getRealPath("/");
		String userid = "";
		if(uc != null){
			userid = uc.get_userModel().getUserid();
		}
		List<String> orgroleidlist = getList(userid);
		Map<String, String> config = ConfigUtil.readAllProperties(CN_FILENAME);// 获取连接网址配置
		String zqServer = config.get("zqServer");
    	String fileuuidlist="";
    	List<HashMap> fileuuid = zqbFileUploadCommitDAO.zqbFileDownloadListPackage(orgroleidlist,customername,zqjc,zqdm,gn,sja,sjb,zqServer,customername,xznr,0L);
    	
		try {
			List<File> filearray = new ArrayList<File>();
            if("所有附件".equals(xznr)){
                filearray.addAll(readfileChange2(fileuuid,filepath));
            }else{
                filearray.addAll(readfilespyj2(fileuuid, filepath, filename));
            }

			if(filearray.size()!=0){
				downLoadFilesChange(filearray,request,response,filename,filename);
				File check = new File(filename);
				delChange(check);
			}else{
				ResponseUtil.write("0");
			}
		} catch (Exception e) {
			logger.error(e,e);
		} 
	}
    public List<File> readfileChange2(List<HashMap> fileuuid,String filepath) throws FileNotFoundException, Exception {
    	List<File> filelist = new ArrayList<File>();
    	
    	List s = new ArrayList();
    	for (HashMap filemsg : fileuuid) {
    		String fileabsolutelypath = filepath+filemsg.get("FILE_URL");
    		File thisfile = new File(fileabsolutelypath);
    		if(thisfile.exists()){
                FileChannel inputChannel= new FileInputStream(fileabsolutelypath).getChannel();
				String filenewpath = (((filemsg.get("ZQDM")!=null&&!filemsg.get("ZQDM").toString().equals(""))&&(filemsg.get("ZQJC")!=null&&!filemsg.get("ZQJC").toString().equals("")))?(filemsg.get("ZQDM").toString()+"("+filemsg.get("ZQJC").toString()+")"+"/"):(filemsg.get("CUSTOMERNAME").toString()+"/"))
									+(filemsg.get("GN")==null?"":(filemsg.get("GN").toString()+"/"))
									+(filemsg.get("XFMK")==null?"":(filemsg.get("XFMK").toString()+"/"));
    			File fst = new File(filenewpath);
				if(!fst.exists()){
					fst.mkdirs();
				}
				File fs = new File(filenewpath+filemsg.get("FILE_SRC_NAME").toString());
                FileChannel outputChannel = new FileOutputStream(fs).getChannel();
                outputChannel.transferFrom(inputChannel, 0, inputChannel.size());
				filelist.add(fs);
    		}
		}
        return filelist;
	}
    
    
    public void zqbFileDownloadZipChange(String customername,String zqjc,String zqdm,String gn,String sja,String sjb) {
    	UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
    	HttpServletRequest request = ServletActionContext.getRequest();
    	HttpServletResponse response = ServletActionContext.getResponse();
    	String filename = (zqjc!=null&&!zqjc.equals("")&&zqdm!=null&&!zqdm.equals(""))?zqdm+"("+zqjc+")":customername;
    	String filepath = request.getSession().getServletContext().getRealPath("/");
		String userid = "";
		if(uc != null){
			userid = uc.get_userModel().getUserid();
		}
		List<String> orgroleidlist = getList(userid);
		Map<String, String> config = ConfigUtil.readAllProperties(CN_FILENAME);// 获取连接网址配置
		String zqServer = config.get("zqServer");
    	String fileuuidlist="";
    	List<HashMap> fileuuid = zqbFileUploadCommitDAO.zqbFileDownloadListPackage(orgroleidlist,customername,zqjc,zqdm,gn,sja,sjb,zqServer,"","",0L);
    	for (HashMap map : fileuuid) {
			fileuuidlist+=((map.get("FILEUUID")==null?"":map.get("FILEUUID").toString())+"//"
						 +(map.get("FILE_SRC_NAME")==null?"":map.get("FILE_SRC_NAME").toString())+"//"
						 +((map.get("ZQDM")!=null&&map.get("ZQJC")!=null&&!map.get("ZQDM").toString().equals("")&&!map.get("ZQJC").toString().equals(""))?(map.get("ZQDM").toString()+"("+map.get("ZQJC").toString()+")"):(map.get("CUSTOMERNAME")==null?"":map.get("CUSTOMERNAME").toString()))+"/-/"
						 +(map.get("GN")==null?"":map.get("GN").toString())+"/-/"
						 +(map.get("XFMK")==null?"":((map.get("XFMK").toString()).split("/-/")).length==2?(map.get("XFMK").toString()).split("/-/")[0]+"/-/"+(map.get("XFMK").toString()).split("/-/")[1]:map.get("XFMK").toString())+"\\\\");
		}
		try {
			List<File> filearray = new ArrayList<File>();
			File file = new File(filepath+"iwork_file\\FORM_FILE\\");
			String[] filelist = file.list();
			for (int i = 0; i < filelist.length; i++) {
				filearray.addAll(readfileChange(filepath+"iwork_file\\FORM_FILE\\"+filelist[i]+"\\",fileuuidlist));
			}
			filearray.addAll(readfileChange(filepath+"iwork_file\\FORM_FILE\\",fileuuidlist));
			if(filearray.size()!=0){
				downLoadFilesChange(filearray,request,response,filename,filename);
				File check = new File(filename);
				delChange(check);
			}else{
                response.getWriter().print("<script>alert('暂无文件！');history.go(-1);</script>");
			}
		} catch (Exception e) {
			logger.error(e,e);
		} 
	}
    public List<File> readfileChange(String filepath,String fileuuidlist) throws FileNotFoundException, Exception {
		List<File> list = new ArrayList<File>();
        try {
        	File file = new File(filepath);
            if (!file.isDirectory()) {
            } else if (file.isDirectory()) {
            	String[] filelist = file.list();
            	for (int i = 0; i < filelist.length; i++) {
            		File readfile = new File(filepath + "\\" + filelist[i]);
            		if (!readfile.isDirectory()) {
            			String[] fileuuidall = fileuuidlist.split("\\\\");
            			for (int k = 0; k < fileuuidall.length; k++) {
            				if(fileuuidall[k]!=null&!"".equals(fileuuidall[k])){
            					String[] fileuuidthisall = fileuuidall[k].split("//");
            					int index = readfile.getAbsolutePath().toString().indexOf(fileuuidthisall[0]);
            					if(index!=-1){
            						try { 
            							int bytesum = 0; 
            							int byteread = 0; 
            							File oldfile = new File(readfile.getAbsolutePath()); 
            							if (oldfile.exists()) { 
            								InputStream inStream = new FileInputStream(readfile.getAbsolutePath());
            								String [] felder = fileuuidthisall[2].split("/-/");
            								File fst;
            								if(felder.length==4){
            									fst = new File((felder[0]==null||felder[0].equals("")?"":felder[0]+"\\")+(felder[1]==null||felder[1].equals("")?"":felder[1]+"\\")+(felder[2]==null||felder[2].equals("")?"":felder[2]+"\\")+(felder[3]==null||felder[3].equals("")?"":felder[3]+"\\"));
            								}else if(felder.length==3){
            									fst = new File((felder[0]==null||felder[0].equals("")?"":felder[0]+"\\")+(felder[1]==null||felder[1].equals("")?"":felder[1]+"\\")+(felder[2]==null||felder[2].equals("")?"":felder[2]+"\\"));
            								}else if(felder.length==2){
            									fst = new File((felder[0]==null||felder[0].equals("")?"":felder[0]+"\\")+(felder[1]==null||felder[1].equals("")?"":felder[1]+"\\"));
            								}else{
            									fst = new File((felder[0]==null||felder[0].equals("")?"":felder[0]+"\\"));
            								}
            								if(!fst.exists()){
            									fst.mkdirs();
            								}
            								File fs = new File(fst.toString()+"\\"+fileuuidthisall[1]);
            								FileOutputStream fsot = new FileOutputStream(fs);
            								byte[] buffer = new byte[1444]; 
            								while ( (byteread = inStream.read(buffer)) != -1) { 
            									bytesum += byteread; 
            									fsot.write(buffer, 0, byteread);
            								} 
            								fsot.flush();
            								list.add(fs);
            								inStream.close(); 
            								fsot.close();
            							}
            						}catch (Exception e) { 
            							logger.error(e,e); 
            						}
            					}
            				}
            			}
            		} else if (readfile.isDirectory()) {
            			readfileChange(filepath + "\\" + filelist[i],fileuuidlist);
            		}
            	}
            }
        } catch (FileNotFoundException e) {
        	logger.error(e,e);
        }
        return list;
	}

	/**
	 * 打包下载文件
	 * @param files
	 * @param request
	 * @param response
	 * @param filename
	 * @return
	 * @throws Exception
	 */
    public HttpServletResponse downLoadFilesChange(List<File> files,HttpServletRequest request, HttpServletResponse response,String filename,String deletefilename)throws Exception {
        try {
        	File[] filess ={
        			new File(deletefilename)
			};
        	String time = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
			File targetFile = new File(filename+time+".zip");
			zipChange(filess,targetFile);
            return downloadZipChange(targetFile,response);
        }catch (Exception e) {
                logger.error(e,e);
            }
        return response ;
    }

    public HttpServletResponse downloadZipChange(File file,HttpServletResponse response) {
        try {
        	FileInputStream is = new FileInputStream(file);
			response.setContentLength(is.available());

	        response.setContentType("application/octet-stream");
	        if((file.getName()).matches(".*\\(.*\\).*")){
	    		String[] str=(file.getName()).split("[()]");
	    		response.setHeader("Content-Disposition", "attachment;filename=" +str[0]+"("+UploadFileNameCodingUtil.StringEncoding(str[1])+")"+str[2]);
	        }else{
	        	response.setHeader("Content-Disposition", "attachment;filename=" + UploadFileNameCodingUtil.StringEncoding(file.getName()));
	        }
	        FileCopyUtils.copy(is, response.getOutputStream());

        } catch (Exception ex) {
        }finally{
             try {
                    File f = new File(file.getPath());
                    f.delete();
                } catch (Exception e) {
                    logger.error(e,e);
                }
        }
        return response;
    }

    public void zipChange(File[] files,File targetFile) throws IOException{
		files = toAbsChange(files);
		targetFile = targetFile.getAbsoluteFile();
		String parentPath = files[0].getParent()+"\\";
		files = eachFilesChange(files);
		ZipOutputStream zos = new ZipOutputStream(targetFile);
		zos.setEncoding("GBK");
		try{
			byte[] buf = new byte[1024*1024];
		for(File f:files){
			zipFileChange(f,zos,parentPath,buf);
		}		
		}catch(Exception e){} 
		finally{
			zos.close();
		}
	}
    private File[] eachFilesChange(File[] files) {
		List<File>  list = new ArrayList<File>();
		LinkedList<File> tasks = new LinkedList<File>(Arrays.asList(files));
		while( !tasks.isEmpty() ) {
			File task = tasks.remove();
			list.add(task);
			if( !task.isDirectory() ) {
				continue;
			}
			for( File c : task.listFiles() ) {
				tasks.add(c);
			}
		}
		return list.toArray(new File[0]);
	}
    private void doZipChange(File f, ZipOutputStream zos, byte[] buf) throws Exception {
		FileInputStream fis = new FileInputStream(f);
		int len;
		try{
		while((len=fis.read(buf))!=-1){
			zos.write(buf, 0, len);
		}
		}finally{
			fis.close();
		}
	}
    private void zipFileChange(File f, ZipOutputStream zos, String parentPath, byte[] buf) throws Exception {
		String filename = f.toString().replace(parentPath, "");
		if(f.isDirectory()){
			filename+="/";
		}
		ZipEntry ze = new ZipEntry(filename);
		zos.putNextEntry(ze);
		try{
		if(f.isFile()){
			doZipChange(f,zos,buf);
		}
		}finally{
		zos.closeEntry();
		}
	}
    private File[] toAbsChange(File[] files) {
		ArrayList<File> list = new ArrayList<File>();
		for(File f:files){
			list.add(f.getAbsoluteFile());
		}
		return list.toArray(new File[0]);
	}
    public void delChange(File file) {
        File[] files = file.listFiles();
        if (files != null)
            for (File f : files)
                delChange(f);
        file.delete();
    }
    //---------------------华丽分割线改结束-------------------------
    
	public List<String> getList(String userid) {
		List<String> orgRoleidList = new ArrayList<String>();
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT ORGROLEID FROM ORGUSERMAP WHERE ORGROLEID IN('5','9','3','4','6','7','11') AND ORGROLEID IS NOT NULL AND USERID=?");
		Connection conn = DBUtil.open();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement(sql.toString());
			ps.setString(1, userid);
			rs = ps.executeQuery();
			while(rs.next()){
				orgRoleidList.add(rs.getBigDecimal("ORGROLEID").toString());
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally{
			DBUtil.close(conn, ps, rs);
		}
		return orgRoleidList;
	}
    public void delfj(String name,String filename){
        String[] names =name.split(",");
        String[] filen =filename.split(",");
        Map params=null;
        try {
            if(names!=null && names.length>0){
                for (int i=0;i<names.length;i++){
                    params=new HashMap();
                    params.put(1,names[i]);
                    DBUTilNew.update("delete from BD_ZQB_DBWJ s where s.id=?",params);
                }
            }
            ResponseUtil.write("0");
            final String[] filens=filen;
            Runnable myRunnable = new Runnable(){
                public void run(){
                    for (int i=0;i<filens.length;i++){
                        File targetFile = new File(filens[i]);
                        File f = new File(targetFile.getPath());
                        if(f.exists())
                            f.delete();
                        String wjcmc=filens[i].split("\\)")[0]+")";
                        File wjc = new File(wjcmc);
                        if(wjc.exists())
                            deleteDir(wjc);                    }
                }
            };
            Thread thread = new Thread(myRunnable);
            thread.start();
        } catch (Exception e) {
            e.printStackTrace();
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
    //批量下载
    public void zqbFileDownloadListZip(String chkvalue,String gn, String sja,String sjb,String xznr,Long qb){
        //    0309测试1/-/0309测试1/-/03091/-/信披公告/-/2016-03-01/-/2016-03-23\0309测试2/-/0309测试2/-/03092/-/信披公告/-/2016-03-01/-/2016-03-23\
        //    customername zqjc      zqdm       gn        sja           sjb
        UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        String filepath = request.getSession().getServletContext().getRealPath("/");
        File file = new File(filepath+"iwork_file\\FORM_FILE\\");
        String zipAndDeleteFilename = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss").format(new Date());
        List<File> filearray = new ArrayList<File>();
        String userid = "";
        if(uc != null){
            userid = uc.get_userModel().getUserid();
        }
        List<String> orgroleidlist = getList(userid);
        Map<String, String> config = ConfigUtil.readAllProperties(CN_FILENAME);// 获取连接网址配置
        String zqServer = config.get("zqServer");

        String customername = "";
        String zqjc = "";
        String zqdm = "";


        List<HashMap> fileuuid = zqbFileUploadCommitDAO.zqbFileDownloadListPackage(orgroleidlist, customername, zqjc, zqdm, gn, sja, sjb, zqServer,chkvalue,xznr,qb);
        try {
            if("所有附件".equals(xznr)){
                filearray.addAll(readfileChangeList2(fileuuid, filepath, zipAndDeleteFilename));
            }else{
                filearray.addAll(readfilespyj2(fileuuid, filepath, zipAndDeleteFilename));
            }
        } catch (Exception e) {
            logger.error(e, e);
        }



        //------
        if(filearray.size()!=0){
            try {
                downLoadFilesChangeList(filearray,request,response,zipAndDeleteFilename,zipAndDeleteFilename);
            } catch (Exception e) {
                logger.error(e,e);
            }
            File check = new File(zipAndDeleteFilename);
            delChange(check);
        }else{
            ResponseUtil.write("<script>alert('暂无文件！');window.close();</script>");
        }
    }

	public HttpServletResponse downLoadFilesChangeList(List<File> files,HttpServletRequest request, HttpServletResponse response,String filename,String deletefilename)throws Exception {
        try {
        	File[] filess ={new File(deletefilename)};
			File targetFile = new File(filename+".zip");
			zipChange(filess,targetFile);
            return downloadZipChange(targetFile,response);
        }catch (Exception e) {
                logger.error(e,e);
            }
        return response ;
    }
    /**
     * 审批意见
     * @param fileuuid
     * @param filepath
     * @param zipAndDeleteFilename
     * @return
     * @throws FileNotFoundException
     * @throws Exception
     */
    public List<File> readfilespyj2(List<HashMap> fileuuid,String filepath,String zipAndDeleteFilename) throws FileNotFoundException, Exception {
        List<HashMap> tmpList=new ArrayList();
        Set<String> keysSet = new HashSet<String>();
        for(HashMap collisionMap : fileuuid){
            String keys = collisionMap.get("ID").toString();
            int beforeSize = keysSet.size();
            keysSet.add(keys);
            int afterSize = keysSet.size();
            if(afterSize == beforeSize + 1){
                tmpList.add((HashMap) collisionMap);
            }
        }
        List<File> filelist = createNewExcle(tmpList,zipAndDeleteFilename);
        for (HashMap filemsg : fileuuid) {
    		if(filemsg.get("FILE_URL")==null || "".equals(filemsg.get("FILE_URL"))) continue;
            String fileabsolutelypath = filepath+filemsg.get("FILE_URL");
            File thisfile = new File(fileabsolutelypath);
            if(thisfile.exists()){
                FileChannel inputChannel= new FileInputStream(fileabsolutelypath).getChannel();
                String filenewpath =zipAndDeleteFilename+"/"
                        +(((filemsg.get("ZQDM")!=null&&!filemsg.get("ZQDM").toString().equals(""))&&(filemsg.get("ZQJC")!=null&&!filemsg.get("ZQJC").toString().equals("")))?(filemsg.get("ZQDM").toString()+"("+filemsg.get("ZQJC").toString()+")"+"/"):(filemsg.get("CUSTOMERNAME").toString()+"/"))
                        +(filemsg.get("GN")==null?"":(filemsg.get("GN").toString()+"/"))
                        +(filemsg.get("XFMK")==null?"":(filemsg.get("XFMK").toString()+"/"));
                File fst = new File(filenewpath);
                if(!fst.exists()){
                    fst.mkdirs();
                }
                File fs = new File(filenewpath+filemsg.get("FILE_SRC_NAME").toString());
                FileChannel outputChannel = new FileOutputStream(fs).getChannel();
                outputChannel.transferFrom(inputChannel, 0, inputChannel.size());
                inputChannel.close();
                outputChannel.close();
                filelist.add(fs);
            }
        }
        return filelist;
    }
    /**
     * 所有附件
     * @param fileuuid
     * @param filepath
     * @param zipAndDeleteFilename
     * @return
     * @throws FileNotFoundException
     * @throws Exception
     */
    public List<File> readfileChangeList2(List<HashMap> fileuuid,String filepath,String zipAndDeleteFilename) throws FileNotFoundException, Exception {
        List<File> filelist = new ArrayList<File>();
        List s = new ArrayList();
        for (HashMap filemsg : fileuuid) {
            String fileabsolutelypath = filepath+filemsg.get("FILE_URL");
            File thisfile = new File(fileabsolutelypath);
            if(thisfile.exists()){
                FileChannel inputChannel= new FileInputStream(fileabsolutelypath).getChannel();
                String filenewpath =zipAndDeleteFilename+"/"
                        +(((filemsg.get("ZQDM")!=null&&!filemsg.get("ZQDM").toString().equals(""))&&(filemsg.get("ZQJC")!=null&&!filemsg.get("ZQJC").toString().equals("")))?(filemsg.get("ZQDM").toString()+"("+filemsg.get("ZQJC").toString()+")"+"/"):(filemsg.get("CUSTOMERNAME").toString()+"/"))
                        +(filemsg.get("GN")==null?"":(filemsg.get("GN").toString()+"/"))
                        +(filemsg.get("XFMK")==null?"":(filemsg.get("XFMK").toString()+"/"));
                File fst = new File(filenewpath);
                if(!fst.exists()){
                    fst.mkdirs();
                }
                File fs = new File(filenewpath+filemsg.get("FILE_SRC_NAME").toString());
                FileChannel outputChannel = new FileOutputStream(fs).getChannel();
                outputChannel.transferFrom(inputChannel, 0, inputChannel.size());
                filelist.add(fs);
            }
        }
        return filelist;
    }
	public List<File> readfileChangeList(String filepath,String fileuuidlist,String zipAndDeleteFilename) throws FileNotFoundException, Exception {
		List<File> list = new ArrayList<File>();
        try {
        	File file = new File(filepath);
            if (!file.isDirectory()) {
            } else if (file.isDirectory()) {
            	String[] filelist = file.list();
            	for (int i = 0; i < filelist.length; i++) {
            		File readfile = new File(filepath + "\\" + filelist[i]);
            		if (!readfile.isDirectory()) {
            			String[] fileuuidall = fileuuidlist.split("\\\\");
            			for (int k = 0; k < fileuuidall.length; k++) {
            				if(fileuuidall[k]!=null&!"".equals(fileuuidall[k])){
            					String[] fileuuidthisall = fileuuidall[k].split("//");
            					int index = readfile.getAbsolutePath().toString().indexOf(fileuuidthisall[0]);
            					if(index!=-1){
            						try { 
            							int bytesum = 0; 
            							int byteread = 0; 
            							File oldfile = new File(readfile.getAbsolutePath()); 
            							if (oldfile.exists()) { 
            								InputStream inStream = new FileInputStream(readfile.getAbsolutePath());
            								String [] felder = fileuuidthisall[2].split("/-/");
            								File fst;
            								if(felder.length==4){
            									fst = new File(zipAndDeleteFilename+"\\"+(felder[0]==null||felder[0].equals("")?"":felder[0]+"\\")+(felder[1]==null||felder[1].equals("")?"":felder[1]+"\\")+(felder[2]==null||felder[2].equals("")?"":felder[2]+"\\")+(felder[3]==null||felder[3].equals("")?"":felder[3]+"\\"));
            								}else if(felder.length==3){
            									fst = new File(zipAndDeleteFilename+"\\"+(felder[0]==null||felder[0].equals("")?"":felder[0]+"\\")+(felder[1]==null||felder[1].equals("")?"":felder[1]+"\\")+(felder[2]==null||felder[2].equals("")?"":felder[2]+"\\"));
            								}else if(felder.length==2){
            									fst = new File(zipAndDeleteFilename+"\\"+(felder[0]==null||felder[0].equals("")?"":felder[0]+"\\")+(felder[1]==null||felder[1].equals("")?"":felder[1]+"\\"));
            								}else{
            									fst = new File(zipAndDeleteFilename+"\\"+(felder[0]==null||felder[0].equals("")?"":felder[0]+"\\"));
            								}
            								if(!fst.exists()){
            									fst.mkdirs();
            								}
            								File fs = new File(fst.toString()+"\\"+fileuuidthisall[1]);
            								FileOutputStream fsot = new FileOutputStream(fs);
            								byte[] buffer = new byte[1444]; 
            								while ( (byteread = inStream.read(buffer)) != -1) { 
            									bytesum += byteread; 
            									fsot.write(buffer, 0, byteread);
            								} 
            								fsot.flush();
            								list.add(fs);
            								inStream.close(); 
            								fsot.close();
            							}
            						}catch (Exception e) { 
            							logger.error(e,e); 
            						}
            					}
            				}
            			}
            		} else if (readfile.isDirectory()) {
            			readfileChangeList(filepath + "\\" + filelist[i],fileuuidlist,zipAndDeleteFilename);
            		}
            	}
            }
        } catch (FileNotFoundException e) {
        	logger.error(e,e);
        }
        return list;
	}
}
