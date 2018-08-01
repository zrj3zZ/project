package com.iwork.plugs.sms.util;

import java.util.List;

public class Page {
private int pageSize;    // 每页显示的记录数
private int totalPage;    // 页数
private int rowCount;    // 总记录数
private int currentPage;    // 当前页
private int prePage;    // 上一页
private int nextPage;    // 下一页
private boolean hasNextPage;    // 是否有下一页
private boolean hasPreviousPage;    // 是否有前一页
private List list;
private int tiao;
private boolean hasTiao;
public Page(){    // 实例化一个Page对象时，初始化页面显示记录数
   this.pageSize=10;
}

public int getCurrentPage() {
   return currentPage;
}

public void setCurrentPage(int currentPage) {
   this.currentPage = currentPage;
}

public List getList() {
   return list;
}

public void setList(List list) {
   this.list = list;
}

public int getNextPage() {
   return nextPage;
}

public void setNextPage(int nextPage) {
   this.nextPage = nextPage;
}

public int getPageSize() {
   return pageSize;
}

public void setPageSize(int pageSize) {
   this.pageSize = pageSize;
}

public int getPrePage() {
   return prePage;
}

public void setPrePage(int prePage) {
   this.prePage = prePage;
}

public int getRowCount() {
   return rowCount;
}

public void setRowCount(int rowCount) {
   this.rowCount = rowCount;
}

public int getTotalPage() {
   return totalPage;
}

public void setTotalPage(int totalPage) {
   this.totalPage = totalPage;
}

public boolean isHasNextPage() {
   return hasNextPage;
}

public void setHasNextPage(boolean hasNextPage) {
   this.hasNextPage = hasNextPage;
}

public boolean isHasPreviousPage() {
   return hasPreviousPage;
}

public void setHasPreviousPage(boolean hasPreviousPage) {
   this.hasPreviousPage = hasPreviousPage;
}
public boolean isHasTiao(){
	return hasTiao;
}
public void setHasTiao(boolean hasTiao){
	this.hasTiao=hasTiao;
}

public int getTiao() {
	return tiao;
}

public void setTiao(int tiao) {
	this.tiao = tiao;
}
}

