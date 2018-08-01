package com.iwork.core.upload.util;


import org.apache.commons.lang.StringUtils;
import org.json.JSONObject;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/***
 *
 * 下载文件或者下载打包文件工具类/返回json
 */
public class DownloadFileUtil {

    /***
     *  String path : 项目根目录的绝对路径
     *  String ZipPath: zip的路径 压缩文件生成的 /  单个文件就是: 传给前台文件的路径
     *  String zipName : 多个文件就是压缩文件的名称/单个文件就是传到前台的文件名字
     *  File mkdirFile: 临时文件的file;可以通过传多个文件的路径,然后生成一个临时文件夹,压缩临时文件生成压缩文件
     *  String mkdir :设置临时文件夹的的相对路径
     *  error : 错误信息;无用
     *  success : 运行信息;无用
     *  List<HashMap> fileZip  这个包含要下载文件的目录id和名字;用于替换文件夹的名称,返回前台的压缩文件按照目录的形式;
     */
    static final int BUFFER = 8192;
    private Integer i=0;
    private String path;
    private String ZipPath;
    private String zipName;
    private HttpServletResponse response;
    private String mkdir;
    private File mkdirFile;
    private List<File> fileList = new ArrayList<File>();
    private List<String> fileNameList = new ArrayList<String>();
    private String error;
    private String success;
    private List<HashMap> map;
    private List<HashMap> fileReplaceNameData;
    private String projectName;
    private ZipOutputStream zipOutPutStream;


    /**
     *      这个构造用法:直接一个目录;
     * @param zipname  前台显示的名字/压缩文件的名字
     * @param path     项目根目录的绝对路径
     * @param mkdir    要压缩的文件夹相对路径
     * @param response
     */
    public DownloadFileUtil(String path,String zipname,String mkdir, HttpServletResponse response) {
        this.zipName=zipname;
        this.path=path;
        this.mkdir=path+mkdir;
        this.ZipPath = path+zipname;
        this.response = response;
    }
    /**
     *      这个构造用法:
     * @param path     项目根目录的绝对路径
     * @param zipname  前台显示的名字/压缩文件的名字
     * @param projectName 项目的名称
     * @param response
     */
    public DownloadFileUtil(String path,String zipname,HttpServletResponse response,String projectName) {
        this.projectName=projectName;
        this.zipName=zipname;
        this.path=path;
        //压缩文件的路径
        this.ZipPath = path+zipname;
        this.response = response;
    }

    /**
     *  作用通过这个可以向前台返回json字符串
     * @param response
     */
    public DownloadFileUtil(HttpServletResponse response) {
        this.response = response;
    }

    /**
     *  这种是: 下载单个文件
     * @param path      下载单个文件的路径;
     * @param zipname   前台显示的名字
     * @param response  对象
     */
    public DownloadFileUtil(String path,String zipname,HttpServletResponse response) {
        this.zipName=zipname;
        this.ZipPath=path;
        this.response = response;
    }


    /**
     *  创建并返回压缩文件的ZipOutputStream
     * @param fileName 创建的zip文件名字
     */
    public ZipOutputStream  getZipOutPutStream(String fileName) throws FileNotFoundException {
        this.ZipPath=this.path+File.separator+fileName;
        File file = new File(this.ZipPath);
        FileOutputStream out=new FileOutputStream(file);
        ZipOutputStream zip=new ZipOutputStream(out);
        zip.setComment(new String(("压缩文件").getBytes()));//设置注释
        return zip;
    }
    /**
     * 通过构造给的路径
     * @return 创建并返回压缩文件的ZipOutputStream
     * @throws FileNotFoundException
     */
    public ZipOutputStream  getZipOutPutStream() throws FileNotFoundException {
        if (this.zipOutPutStream==null){
            File file = new File(this.ZipPath);
            FileOutputStream out=new FileOutputStream(file);
            ZipOutputStream zip=new ZipOutputStream(out);
            zip.setComment(new String(("压缩文件").getBytes()));//设置注释
            return zip;
        }
        return zipOutPutStream;
    }


    /***
     *  作用:通过文件集合生成一个新的临时文件夹
     * @param files 传需要压缩文件的集合,
     * @param names 在数据库中需要与要压缩文件一一对应对应的名字
     * @throws IOException
     */
    public boolean setDirectoryFile(List<File> files, List<String> names) throws Exception {
        byte[] b = new byte[1024];
        int a;
        FileInputStream fis = null;
        FileOutputStream fos = null;
        if (new File(this.mkdir).exists()){
            String newmkdir = this.mkdir+"yyyy---xxxx+20";
            this.error +="--"+this.mkdir+":文件夹已存在--\n";
            this.mkdir=newmkdir;
        }
        this.mkdirFile = new File(this.mkdir );
        this.mkdirFile.mkdir();
        String filepath = this.mkdirFile.getAbsolutePath();
        int i = 0;
        for (File fileo : files) {
            try {
                if (!fileo.exists()){
                    this.error="--"+fileo.getName()+":文件不存在无法复制到临时文件--\n";
                    continue;
                }
                File filenew = null;
                if (new File(filepath + File.separator + names.get(i)).exists()) {
                    String houzhui = names.get(i).substring(names.get(i).lastIndexOf("."), names.get(i).length());
                    String name = names.get(i).substring(0, names.get(i).lastIndexOf("."));
                    filenew = new File(filepath +File.separator+ name + i + "--文件名字重复"+i+"--" + houzhui);
                    this.success="--"+names.get(i)+":文件名重复--更改后的文件名字:"+name + "--"+i+i+"--" + houzhui+"\n";
                } else {
                    filenew = new File(filepath + File.separator + names.get(i));
                }
                fis = new FileInputStream(fileo);
                fos = new FileOutputStream(filenew);
                while ((a = fis.read(b)) != -1) fos.write(b, 0, a);
                i++;
            } catch (Exception e) {
                e.printStackTrace();
                this.error="--文件复制到临时文件夹错误--";
                return false;
            } finally {
                if (fis != null) fis.close();
                if (fos != null) fos.close();
            }
        }
        return true;
    }

    /***
     *  通过给出的文件路径获取文件File集合
     *  @param list 这个代表的是传入的下载路径,在HashMap中有两个key:
     *             1.是FILE_URL这个value代表的是文件的路径(包括名字);
     *             2.是FILE_SRC_NAME这个value代表的是文件的名字(代表的是数据库中文件保存的名字)
     */
    public boolean getFiles(List<HashMap> list) throws Exception {
        int j = 0;
        int i = 0;
        for (; i < list.size(); i++) {
            if (new File(this.path + (String) File.separator + list.get(i).get("FILE_URL")).exists()) {
                this.fileList.add(new File(this.path + (String) File.separator + list.get(i).get("FILE_URL")));
                this.fileNameList.add((String)list.get(i).get("FILE_SRC_NAME"));
            } else {
                j++;
            }
        }
        this.success+="--查询到的文件:复制成功:"+(i-j)+"\n";
        this.success+="--查询到的文件:复制失败(不存在):"+(j)+"\n";
        if(this.fileList.size()<=0){
            this.error="--文件为空无法压缩--"+"\n";
            return false;
        }
        return setDirectoryFile(this.fileList,this.fileNameList);
    }

    /***
     * 传给前台
     * 需要名字和文件路径
     * this.ZipPath  传送文件的路径
     * this.zipName  传送文件文件显示的名字
     * @return
     * @throws IOException
     */
    public boolean toQianTai() throws IOException {
        OutputStream toClient = null;
        InputStream fis = null;
        FileInputStream fileInputStream = null;
        ServletOutputStream outputStream = null;
        try {
            File file = new File(this.ZipPath);
            fileInputStream = new FileInputStream(file);
            fis = new BufferedInputStream(fileInputStream);
            this.response.reset();// 清空response
            // 设置response的Header  new String(((String) list.get(0).get("FILE_SRC_NAME")).getBytes(),"ISO8859-1");
            this.response.addHeader("Content-Disposition", "attachment;filename=" + new String((this.zipName).getBytes(), "ISO8859-1"));
            this.response.addHeader("Content-Length", "" + file.length());
            this.response.setContentType("application/octet-stream");
            outputStream = this.response.getOutputStream();
            toClient = new BufferedOutputStream(outputStream);
            //this.response.setContentType("application/x-msdownload");
            //压缩文件
            byte[] buffer = new byte[2048];
            int len;
            while ((len = fis.read(buffer)) > -1) {
                toClient.write(buffer, 0, len);
            }
            toClient.flush();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
            //出现错误
        } finally {
            if (toClient != null) toClient.close();
            if (fis != null) fis.close();
            if (fileInputStream != null) fileInputStream.close();
            if (outputStream != null) outputStream.close();
        }
        return true;
    }

    /**
     * 删除临时文件;这种临时文件是通过工具类创建的,如果只用到传单个文件到前台就不需要删除临时文件;
     */

    public void deleteTemporary() {
        if (mkdirFile!=null && mkdirFile.isDirectory()) {
            File lists[] = mkdirFile.listFiles();
            for (File file : lists) {
                if (file.exists()) ;
                file.delete();
            }
            this.mkdirFile.delete();
        }
        File filezip = new File(ZipPath);
        filezip.delete();
    }


    /**
     *
     * 作用:压缩文件   这个用于压缩一个目录中的文件,压缩完之后自动关闭压缩文件的流
     * @param  listName 要压缩目录中的文件名是uuid需要替换,如果不需要替换传null就可以
     * @throws IOException
     */
    public void compressDir(List<HashMap> listName) throws IOException {
        this.map=listName;
        try {
            String basedir = "";
            File file=new File(this.mkdir);
            this.zipOutPutStream = getZipOutPutStream();
            //压缩文件   ,  流  ,  压缩的层级
            compress(file,this.zipOutPutStream , basedir);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }finally{
            if(this.zipOutPutStream!=null)this.zipOutPutStream.close();
        }
    }
    /**
     *
     * 作用:压缩文件  这个可以持续上传文件用于压缩,压缩完之后需要调用关闭方法关闭流文件
     * @param  listName 要压缩目录中的文件名是uuid需要替换,如果不需要替换传null就可以
     * @param  CompressCatalog 文件夹的地址的地址
     * @param  CompressCatalogPath 要压缩文件在zip中父目录,例如:"aa/bb"这个就代表文件夹地址中所有文件压缩到zip压缩文件中,在压缩文件中aa文件夹下的bb文件夹下的文件.
     * @throws IOException
     */
    public void compressDir(List<HashMap> listName,String CompressCatalog,String CompressCatalogPath) throws IOException {
        this.map=listName;
        try {
            String basedir = CompressCatalogPath;
            File file=new File(CompressCatalog);
            if(this.zipOutPutStream==null){
                this.zipOutPutStream = getZipOutPutStream();
            }
            compress(file,this.zipOutPutStream , basedir);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void closeZipOutPutStream() throws IOException {
        if(this.zipOutPutStream!=null){
            this.zipOutPutStream.close();
        }
    }

    /**
     *
     * file:要压缩的文件
     * basedir:zip压缩文件中的路径;用于区别目录即在zip中的目录
     * 判断然后递归
     */

    private void compress(File file, ZipOutputStream out, String basedir) throws IOException {
        /* 判断是目录还是文件 */
        if (file.isDirectory()) {
                this.compressDirectory(file, out, basedir);
        } else {
            this.compressFile(file, out, basedir);
        }
    }

    /** 判断是否是目录然后回调 */
    private void compressDirectory(File dir, ZipOutputStream out, String basedir) throws IOException {
        if (!dir.exists()){
            this.error +="--"+dir.getName()+"--目录不存在;--";
            return;
        }
        File[] files = dir.listFiles();
        for (int i = 0; i < files.length; i++) {
            /* 递归 */
            compress(files[i], out, basedir);
        }
    }

    /** 压缩一个文件 */
    private void compressFile(File file, ZipOutputStream out, String basedir) throws IOException {
        if (!file.exists()) {
            this.error +="--"+file.getName()+"--文件不存在;--\n";
            return;
        }
        BufferedInputStream bis=null;
        try {
            file.getName();
            bis= new BufferedInputStream( new FileInputStream(file));
            ZipEntry entry =null;
            entry = removeNameRepeat(file,basedir,entry);
            out.putNextEntry(entry);
            int count;
            byte data[] = new byte[BUFFER];
            while ((count = bis.read(data, 0, BUFFER)) != -1) {
                out.write(data, 0, count);
            }
            this.success +="--"+file.getName()+"--文件压缩成功;--\n";
        } catch (Exception e) {
            this.error +="--"+file.getName()+"--文件压缩失败;--\n";
            throw new RuntimeException(e);
        }finally{
            if(bis!=null)bis.close();
        }
    }

    /**
     *  作用:底稿存档特定因为目录文件名是uuid需要替换,而且有可能重复所以需要这个方法来过滤下
     * @param file
     * @param basedir
     * @param entry
     * @return
     */
    public ZipEntry removeNameRepeat(File file,String basedir,ZipEntry entry) {
        Boolean  flag=true;
        if(map!=null){
            for (HashMap hashMap : map) {
                if ((hashMap.get("uuid")).equals(file.getName())) {
                    String houzhui = ((String) hashMap.get("name")).substring(((String) hashMap.get("name")).lastIndexOf("."), ((String) hashMap.get("name")).length());
                    String name = ((String) hashMap.get("name")).substring(0, ((String) hashMap.get("name")).lastIndexOf("."));
                    entry = new ZipEntry(basedir + name + "---"+(i++)+ houzhui);
                    flag=false;
                    break;
                }
            }
        }
        if(flag)return entry = new ZipEntry(basedir + file.getName());
        return entry;

    }

    public boolean setQianTaiDate(HashMap map) throws IOException {
        PrintWriter writer = null;
        try {
            this.response.setContentType("text/html; charset=utf-8");
            JSONObject json = new JSONObject(map);
            writer = this.response.getWriter();
            writer.write(json.toString());
            writer.flush();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            writer.close();
        }
    }




    public String getZipPath() {
        return ZipPath;
    }

    public void setZipPath(String zipPath) {
        ZipPath = zipPath;
    }

    public String getZipName() {
        return zipName;
    }

    public void setZipName(String zipName) {
        this.zipName = zipName;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }
}
