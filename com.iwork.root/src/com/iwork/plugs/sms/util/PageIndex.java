package com.iwork.plugs.sms.util;

/**
 * 生成多页分页显示的索引导航html片断，此类需要相关页面编写一个 gotoPage(frmMain,列表命令,显示哪页) 的JavaScript函数
 * 
 * @author jackliu
 * @version 2.2.1
 * @preserve 声明此方法不被JOC混淆
 */
public class PageIndex {
	// 总行数
	private int _lineCount;

	// 每页显示记录数
	private int _lineNumber;

	// 当前页码
	private int _pageNow;

	// 命令
	private String _cmd;

	private String _ext;// 扩展临时参数
	
	private String _bcid;// 扩展临时参数(oa知道分类id)
	
	private int _tagNum;// 扩展临时参数(oa知道分类标签号)

	private boolean _isShowTitle = true;

	public void setShowTitle(boolean isShowTitle) {
		_isShowTitle = isShowTitle;
	}
	
	/**
	 * 
	 */
	public PageIndex(String cmd, int pageNow, int lineCount, int lineNumber, String ext, String bcid, int tagNum) {
		this(cmd, pageNow, lineCount, lineNumber);
		_ext = ext;
		_bcid = bcid;
		_tagNum = tagNum;
	}
	
	/**
	 * 
	 */
	public PageIndex(String cmd, int pageNow, int lineCount, int lineNumber, String ext) {
		this(cmd, pageNow, lineCount, lineNumber);
		_ext = ext;
	}
	
	/**
	 * 
	 * @param cmd
	 *            执行的命令
	 * @param pageNow
	 *            当前页
	 * @param lineCount
	 *            总行数
	 * @param lineNumber
	 *            每页行数
	 * @preserve 声明此方法不被JOC混淆
	 */
	public PageIndex(String cmd, int pageNow, int lineCount, int lineNumber) {
		this._lineCount = lineCount;
		this._lineNumber = lineNumber;
		this._pageNow = pageNow;
		this._cmd = cmd;
	}

	/**
	 * @return 获得分页索引导航部分的html片断代码
	 * @preserve 声明此方法不被JOC混淆
	 */
	public String toString() {
		int pagerSize = 7;// 默认提供六页页码的索引链接
		StringBuffer sb = new StringBuffer("<style type='text/css'>\n<!--\n .pagerCurrent{border:1px solid gray;padding:1px;margin:2px;padding-left:4px;padding-right:4px;text-decoration:none;line-height:20px;background-color:#FFFFFF }\n .pagerNotCurrent{color:blue;border:1px solid gray;padding:1px;padding-left:4px;padding-right:4px;margin:2px;text-decoration:none;line-height:20px}\n   -->\n</style>\n");
		int pageCount = ((this._lineCount - 1) / this._lineNumber) + 1; // 总页码
		if (this._pageNow > 1) {
			if (_isShowTitle) {
				sb.append("页:");
			}
			// 如果当前页码已经大于1
			// 计算从哪个页开始最佳
			int pagerFirst = _pageNow;
			if ((pageCount - _pageNow) < (pagerSize - 1)) {
				pagerFirst = _pageNow + (pageCount - _pageNow) - pagerSize + 1;
			} else {
				pagerFirst = _pageNow - 1;
			}
			sb.append("<a href='' onclick=\"return gotoPage(frmMain,'").append(this._cmd).append("',").append(1).append(");return false;\"><span class='pagerNotCurrent'>|<</span></a>");
			if (_pageNow > 2 && pagerFirst > 0) {
				sb.append("<span class='pagerNotCurrent'>..</span>");
			}
			for (int i = pagerFirst; i < (pagerSize + pagerFirst); i++) {
				if (i <= 0)
					continue;
				if (_pageNow == i) {// 显示当前页，无链接
					sb.append("<span class='pagerCurrent'>").append(i).append("</span>");
				} else {
					sb.append("<a href='' onclick=\"return gotoPage(frmMain,'").append(this._cmd).append("',").append(i).append(");return false;\"><span class='pagerNotCurrent'>").append(i).append("</span></a>");
				}
			}
			if ((pagerSize + pagerFirst) < (pageCount + 1)) {
				sb.append("<span class='pagerNotCurrent'>..</span>");
			}

			
			if (this._pageNow < pageCount) {
				
				sb.append("<a href='' onclick=\"return gotoPage(frmMain,'").append(this._cmd).append("',").append(pageCount).append(");return false;\"><span class='pagerNotCurrent'>>|</span></a>");
			}
			if (_isShowTitle) {
				sb.append(" [共").append(this._lineCount).append("条记录分").append(pageCount).append("页显示，每页").append(this._lineNumber).append("条 当前第").append(this._pageNow).append("页]&nbsp;");
			}
		} else if (pageCount > 1) {
			if (_isShowTitle) {
				sb.append("页:");
			}
			// 如果总页码大于1且当前为第1页
			for (int i = 1; i < pagerSize; i++) {
				if (i > pageCount)
					break;
				if (_pageNow == i) {// 显示当前页，无链接
					sb.append("<span class='pagerCurrent'>").append(i).append("</span>");
				} else {
					sb.append("<a href='' onclick=\"return gotoPage(frmMain,'").append(this._cmd).append("',").append(i).append(");return false;\"><span class='pagerNotCurrent'>").append(i).append("</span></a>");
				}
			}
			if (pagerSize < pageCount) {
				sb.append("<span class='pagerNotCurrent'>..</span>");
			}
			
			sb.append("<a href='' onclick=\"return gotoPage(frmMain,'").append(this._cmd).append("',").append(pageCount).append(");return false;\"><span alt=aaa class='pagerNotCurrent'>>|</span></a>");
			if (_isShowTitle) {
				sb.append(" [共").append(this._lineCount).append("条记录分").append(pageCount).append("页显示，每页").append(this._lineNumber).append("条 当前第").append(this._pageNow).append("页]&nbsp;");
			}
		} else {

		}
		// sb.append("<div style='height:1px;'></div>");
		return sb.toString();
	}
	
	/**
	 * OA知道使用分页
	 * @return 获得分页索引导航部分的html片断代码
	 * @preserve 声明此方法不被JOC混淆
	 */
	public String getPage() {
		int pagerSize = 5;// 默认提供六页页码的索引链接
		StringBuffer sb = new StringBuffer("<style type='text/css'>\n<!--\n .pagerCurrent{border:1px solid gray;padding:1px;margin:2px;padding-left:4px;padding-right:4px;text-decoration:none;line-height:20px;background-color:#FFFFFF }\n .pagerNotCurrent{color:blue;border:1px solid gray;padding:1px;padding-left:4px;padding-right:4px;margin:2px;text-decoration:none;line-height:20px}\n   -->\n</style>\n");
		int pageCount = ((this._lineCount - 1) / this._lineNumber) + 1; // 总页码
		if (this._pageNow > 1) {
			if (_isShowTitle) {
				sb.append("页:");
			}
			// 如果当前页码已经大于1
			// 计算从哪个页开始最佳
			int pagerFirst = _pageNow;
			if ((pageCount - _pageNow) < (pagerSize - 1)) {
				pagerFirst = _pageNow + (pageCount - _pageNow) - pagerSize + 1;
			} else {
				pagerFirst = _pageNow - 1;
			}
			sb.append("<a href='#' onclick=\"return gotoPage(frmMain,'").append(this._cmd).append("',").append(1).append(",'"+this._ext+"','"+this._bcid+"',"+this._tagNum).append(");return false;\"><span class='pagerNotCurrent'>|<</span></a>");
			if (_pageNow > 2 && pagerFirst > 0) {
				sb.append("<span class='pagerNotCurrent'>..</span>");
			}
			for (int i = pagerFirst; i < (pagerSize + pagerFirst); i++) {
				if (i <= 0)
					continue;
				if (_pageNow == i) {// 显示当前页，无链接
					sb.append("<span class='pagerCurrent'>").append(i).append("</span>");
				} else {
					sb.append("<a href='#' onclick=\"return gotoPage(frmMain,'").append(this._cmd).append("',").append(i).append(",'"+this._ext+"','"+this._bcid+"',"+this._tagNum).append(");return false;\"><span class='pagerNotCurrent'>").append(i).append("</span></a>");
				}
			}
			if ((pagerSize + pagerFirst) < (pageCount + 1)) {
				sb.append("<span class='pagerNotCurrent'>..</span>");
			}

			
			if (this._pageNow < pageCount) {
				
				sb.append("<a href='#' onclick=\"return gotoPage(frmMain,'").append(this._cmd).append("',").append(pageCount).append(",'"+this._ext+"','"+this._bcid+"',"+this._tagNum).append(");return false;\"><span class='pagerNotCurrent'>>|</span></a>");
			}
			if (_isShowTitle) {
				sb.append(" [共").append(this._lineCount).append("条记录分").append(pageCount).append("页显示，每页").append(this._lineNumber).append("条 当前第").append(this._pageNow).append("页]&nbsp;");
			}
		} else if (pageCount > 1) {
			if (_isShowTitle) {
				sb.append("页:");
			}
			// 如果总页码大于1且当前为第1页
			for (int i = 1; i < pagerSize; i++) {
				if (i > pageCount)
					break;
				if (_pageNow == i) {// 显示当前页，无链接
					sb.append("<span class='pagerCurrent'>").append(i).append("</span>");
				} else {
					sb.append("<a href='#' onclick=\"return gotoPage(frmMain,'").append(this._cmd).append("',").append(i).append(",'"+this._ext+"','"+this._bcid+"',"+this._tagNum).append(");return false;\"><span class='pagerNotCurrent'>").append(i).append("</span></a>");
				}
			}
			if (pagerSize < pageCount) {
				sb.append("<span class='pagerNotCurrent'>..</span>");
			}
			sb.append("<a href='#' onclick=\"return gotoPage(frmMain,'").append(this._cmd).append("',").append(pageCount).append(",'"+this._ext+"','"+this._bcid+"',"+this._tagNum).append(");return false;\"><span alt=aaa class='pagerNotCurrent'>>|</span></a>");
			if (_isShowTitle) {
				sb.append(" [共").append(this._lineCount).append("条记录分").append(pageCount).append("页显示，每页").append(this._lineNumber).append("条 当前第").append(this._pageNow).append("页]&nbsp;");
			}
		} else {

		}
		return sb.toString();
	}

	public static void main(String[] args) {
		PageIndex p = new PageIndex("AAA", 2, 21, 10);
	}
}
