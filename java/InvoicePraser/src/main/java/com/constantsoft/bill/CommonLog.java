package com.constantsoft.bill;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by walter.xu on 2016/12/2.
 */
public class CommonLog {
    private SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss sss");
    private Class clazz;

    public CommonLog(Class clazz){this.clazz = clazz;}

    public void error(String line){
        StringBuilder str = new StringBuilder();
        str.append(df.format(new Date())).append(" ").append("ERROR").append(" ").append(clazz.getPackage()).append(".").append(clazz.getName());
        str.append(" ").append(line);
    }
    public void info(String line){
        StringBuilder str = new StringBuilder();
        str.append(df.format(new Date())).append(" ").append("INFO").append(" ").append(clazz.getPackage()).append(".").append(clazz.getName());
        str.append(" ").append(line);
    }
}
