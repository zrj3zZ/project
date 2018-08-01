package com.iwork.app.conf;

import java.lang.reflect.Field;

public class Config {
    /**
     * 重写了toString() Method
     *
     * @return
     * @preserve 声明此方法不被JOC混淆
     */
    public String toString() {
        Field[] fields = this.getClass().getFields();
        StringBuffer sb = new StringBuffer();

        try {
            for (int i = 0; i < fields.length; i++) {
                sb.append(fields[i] + "=" + fields[i].get(this) + "\n");
            }
        } catch (Exception e) {
        }

        return sb.toString();
    }
}
