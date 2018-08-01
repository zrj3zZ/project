/*
 * Copyright 2003,2004,2005,2006,2007,2008 Actionsoft Co.,Ltd
 * AWS BPM(Business Process Management) PLATFORM Source 
 * AWS is a application middleware for BPM system,
 * Powered by actionsoft,China.
 * 
 * 本软件工程版权归属北京炎黄盈动科技发展有限责任公司
 * 所有，受国家版权局及相关法律保护，未经书面法律许可，
 * 任何单位或个人都不得泄漏和公开此源码全部或部分文件。
 * 属于北京炎黄盈动公司机密知识产权，违者必究。
 * 
 * http://www.actionsoft.com.cn
 * 
 */

package com.iwork.app.conf;


/**
 * 日志配置参数
 *
 * @author David.yang
 * @preserve 声明此方法不被JOC混淆
 */
public class LogConf extends Config {
    private String runtimeFileName;
    private String errorFileName;
    private String runtimeServer;

    /**
     * @preserve 声明此方法不被JOC混淆
     */
    public String getRuntimeServer() {
        return (this.runtimeServer == null) ? "off" : this.runtimeServer;
    }

    /**
     * @preserve 声明此方法不被JOC混淆
     */
    public void setRuntimeServer(String runtimeServer) {
        this.runtimeServer = runtimeServer;
    }

    /**
     * @preserve 声明此方法不被JOC混淆
     */
    public String getErrorFileName() {
        return (this.errorFileName == null) ? "" : this.errorFileName;
    }

    /**
     * @preserve 声明此方法不被JOC混淆
     */
    public void setErrorFileName(String errorFileName) {
        this.errorFileName = errorFileName;
    }

    /**
     * @preserve 声明此方法不被JOC混淆
     */
    public String getRuntimeFileName() {
        return (this.runtimeFileName == null) ? "" : this.runtimeFileName;
    }

    /**
     * @preserve 声明此方法不被JOC混淆
     */
    public void setRuntimeFileName(String runtimeFileName) {
        this.runtimeFileName = runtimeFileName;
    }
}
