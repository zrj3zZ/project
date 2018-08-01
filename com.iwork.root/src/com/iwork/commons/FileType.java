

package com.iwork.commons;

/**
 * AWF识别操作系统各类文件类型类工具
 * 
 * @author David.Yang
 * @version 2.2.1
 * @preserve 声明此方法不被JOC混淆
 */
public class FileType {

	/**
	 * 根据文件名获得描述改类文件的图标文件名
	 * 
	 * @param fn
	 *            带后缀的文件名
	 * @return 一个URL图形文件
	 * @preserve 声明此方法不被JOC混淆
	 */
	public static String getFileIcon(String fn) {
		int p = fn.lastIndexOf(".");
		String icon = "iwork_img/file/normal.jpg";
		if (p > 0) {
			String ext = fn.substring(p + 1, fn.length());
			ext = ext.toLowerCase();
			if (ext.equals("doc") || ext.equals("rtf")  || ext.equals("docx")) {
				icon = "iwork_img/file/word.jpg";
			} else if (ext.equals("xls") || ext.equals("xlt") || ext.equals("xlw")  || ext.equals("xlsx")) {
				icon = "iwork_img/file/excel.jpg";
			} else if (ext.equals("htm") || ext.equals("html") || ext.equals("xml")) {
				icon = "iwork_img/file/html.jpg";
			} else if (ext.equals("png") || ext.equals("pcx") || ext.equals("psd")) {
				icon = "iwork_img/file/pic.jpg";
			} else if (ext.equals("ppt") || ext.equals("pot") || ext.equals("pps")  || ext.equals("ppt")) {
				icon = "iwork_img/file/pic.jpg";
			} else if (ext.equals("rar") || ext.equals("tar") || ext.equals("gz")) {
				icon = "iwork_img/file/rar.jpg";
			} else if (ext.equals("jpg") || ext.equals("jpeg")) {
				icon = "iwork_img/file/jpg.jpg";
			} else if (ext.equals("txt") || ext.equals("logging") || ext.equals("ini")) {
				icon = "iwork_img/file/txt.gif";
			} else if (ext.equals("avi")) {
				icon = "iwork_img/file/avi.jpg";
			} else if (ext.equals("exe")) {
				icon = "iwork_img/file/exe.jpg";
			} else if (ext.equals("gif")) {
				icon = "iwork_img/file/gif.jpg";
			} else if (ext.equals("bmp")) {
				icon = "iwork_img/file/bmp.jpg";
			} else if (ext.equals("pdf")) {
				icon = "iwork_img/file/pdf.gif";
			} else if (ext.equals("chm")) {
				icon = "iwork_img/file/chm.jpg";
			}else{
				icon = "iwork_img/file/attach.gif";
			}
		}
		return icon;
	}

}
