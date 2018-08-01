package com.iwork.commons.util;


public class WebUIUtil {
	
	private static String[] icon = {"png","avi","bmp","chm","doc","docx","dps","et","excel","exe","gif","html","htm","jpg","normal","pdf","pic","ppt","rar","txt","wps","xls","xml","zip"};
	
	/**
	 * 根据文件后缀获取文件图标
	 * @param suffix
	 * @return
	 */
	public static String getLinkIcon(String suffix){
		suffix = suffix.toLowerCase().trim();
		String icon_img = "";
		String tmp = "";
		for(int i=0;i<icon.length;i++){
			if(icon[i].equals(suffix)){
				tmp = suffix;
				break;
			}
		}
		if(tmp.equals("")){
			icon_img = "<img border=\"0\" src=\"iwork_img/file/attach.gif\">";
		}else{
			icon_img = "<img border=\"0\" src=\"iwork_img/file/"+tmp+".jpg\">"; 
		}
		return icon_img;
	}

}
