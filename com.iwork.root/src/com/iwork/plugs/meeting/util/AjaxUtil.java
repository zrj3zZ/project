package com.iwork.plugs.meeting.util;

public class AjaxUtil {

	/**
	 * 返回处理结果
	 * 
	 * @param content
	 *            结果
	 * @return 动画图片的ID
	 * @author jack
	 */
	public static String responseHtml(String content, String imgObject) {
		String gif = "";
		if (imgObject != null && !imgObject.equals("")) {
			gif = "try{parent.frmMain." + imgObject
					+ ".style.display='none';}catch(e){}";
		}
		return "<div ID=AWS_AJAX_HTML_ZONE_TMP name=AWS_AJAX_HTML_ZONE_TMP>"
				+ content
				+ "</div><script>function doRelatedAction(){parent.AWS_AJAX_HTML_ZONE.innerHTML=AWS_AJAX_HTML_ZONE_TMP.innerHTML;AWS_AJAX_HTML_ZONE_TMP.innerHTML='';"
				+ gif + "}doRelatedAction();parent.unlockScreen();</script>";
	}

	/**
	 * 返回一个对话框[不可拖动]
	 * 
	 * @param title
	 *            对话框标题
	 * @param left
	 *            左位置
	 * @param top
	 *            上位置
	 * @param width
	 *            宽度
	 * @param height
	 *            高度
	 * @param content
	 *            对话框的内容
	 * @param imgObject
	 *            动画图片的ID
	 * @return 可返回给AWS Socket client的结果
	 * @author jack
	 */
	public static String responseDialog(String title, int left, int top,
			int width, int height, String content, String imgObject) {
		StringBuffer dialog = new StringBuffer();
		dialog.append("<table width=100%  height=100% align=center border=0 cellspacing=0 cellpadding=0 style='border:1px solid #CCCCCC;'>");
		dialog.append(
				"<tr bgcolor=gray><td><table width=100% align=center border=0 cellspacing=0 cellpadding=0><tr class=aws-portal-window-titlebar-title><td width=90% style ='border-bottom:1px solid #efefef;padding-left:5px;padding-top:3px;'><SPAN onMouseDown='return false;readyDragDialog()' style='width:100%;'><img src='../aws_img/arrow.png' border='0'>")
				.append(title)
				.append("</SPAN></td><td width=10% align=right style='cursor:move;border-bottom:1px solid #efefef;'><a href='' onclick=\"closeDialog();return false;\"><img src=../aws_skins/portlet/AWSDefaultPortletLF2/rem.gif border=0 alt=关闭对话框></a></td></tr></table></td></tr>");
		dialog.append(
				"<tr height=100%><td valign=top><br><div style='overflow:auto;width:100%;height:"
						+ (height - 50) + ";padding:5px;'>").append(content)
				.append("</div></td></tr></table>");
		String gif = "";
		if (imgObject != null && !imgObject.equals("")) {
			gif = "try{parent.frmMain." + imgObject
					+ ".style.display='none';}catch(e){}";
		}
		return "<div ID=AWS_AJAX_HTML_ZONE_TMP name=AWS_AJAX_HTML_ZONE_TMP>"
				+ dialog.toString()
				+ "</div><script>function doRelatedAction(){parent.AWS_AJAX_HTML_DIALOG.innerHTML=AWS_AJAX_HTML_ZONE_TMP.innerHTML;AWS_AJAX_HTML_ZONE_TMP.innerHTML='';parent.showDialog("
				+ left + "," + top + "," + width + "," + height
				+ ");parent.lockScreen();" + gif
				+ "}doRelatedAction();</script>";
	}

	/**
	 * 返回一个对话框2[可拖动]
	 * 
	 * @param title
	 *            对话框标题
	 * @param left
	 *            左位置
	 * @param top
	 *            上位置
	 * @param width
	 *            宽度
	 * @param height
	 *            高度
	 * @param content
	 *            对话框的内容
	 * @param imgObject
	 *            动画图片的ID
	 * @return 可返回给AWS Socket client的结果
	 * @author jack
	 */
	public static String responseDialog2(String title, int left, int top,
			int width, int height, String content, String imgObject) {
		StringBuffer dialog = new StringBuffer();
		dialog.append("<table width=100%  height=100% align=center border=0 cellspacing=0 cellpadding=0 style='border:1px solid #CCCCCC;'>");
		dialog.append(
				"<tr bgcolor=gray><td><table width=100% align=center border=0 cellspacing=0 cellpadding=0><tr class=aws-portal-window-titlebar-title ><td width=90% style='border-bottom:1px solid #efefef;'><SPAN onMouseDown='readyDragDialog()' style='width:100%;cursor:move;'><font color='#666666'><b><img src='../aws_img/arrow.png' border='0'>")
				.append(title)
				.append("</b></font></SPAN></td><td width=10% align=right style='cursor:move;border-bottom:1px solid #efefef;'><a href='' onclick=\"closeDialog();return false;\"><img src=../aws_skins/portlet/AWSDefaultPortletLF2/rem.gif border=0 alt=关闭对话框></a></td></tr></table></td></tr>");
		dialog.append(
				"<tr height=100%><td valign=top><br><div style='overflow:auto;padding:2px;width:100%;height:"
						+ (height - 50) + "'>").append(content)
				.append("</div></td></tr></table>");
		String gif = "";
		if (imgObject != null && !imgObject.equals("")) {
			gif = "try{parent.frmMain." + imgObject
					+ ".style.display='none';}catch(e){}";
		}
		return "<div ID=AWS_AJAX_HTML_ZONE_TMP name=AWS_AJAX_HTML_ZONE_TMP>"
				+ dialog.toString()
				+ "</div><script>function doRelatedAction(){parent.AWS_AJAX_HTML_DIALOG.innerHTML=AWS_AJAX_HTML_ZONE_TMP.innerHTML;AWS_AJAX_HTML_ZONE_TMP.innerHTML='';parent.showDialog("
				+ left + "," + top + "," + width + "," + height
				+ ");parent.lockScreen();" + gif
				+ "}doRelatedAction();</script>";
	}
}
