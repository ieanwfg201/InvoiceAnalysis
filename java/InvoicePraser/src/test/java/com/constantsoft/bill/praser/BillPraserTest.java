package com.constantsoft.bill.praser;

import java.nio.charset.Charset;
import java.util.Arrays;

/**
 * Created by walter.xu on 2016/11/30.
 */
public class BillPraserTest {

    public static void main(String[] args) throws Exception {
//        String[] arrays = BillPraser.getInstance().generateBillCodeAndNumber("D:\\log\\pdf\\031001600211_46627938(1).pdf");
        String[] arrays = BillPraser.getInstance().generateBillCodeAndNumber("D:\\log\\pdf\\liantong.pdf");
        System.out.println(Arrays.toString(arrays));
//        testFindNextStr();
    }

    public static void testFindNextStr(){
        String line = "测试数据:     0 123456789  123aaaa";
        System.out.println(BillPraser.getInstance().findNextStringByPattern(line, "测试数据", -1, null));

        for(int i=0;i < 100; i++){
            final int index = i;
            new Thread(){
                @Override public void run(){
                    long start = System.currentTimeMillis();
                    int totalSize = 1000000;
                    int count = 0;
                    while(count++<totalSize){
                        String result = BillPraser.getInstance().findNextStringByPattern("测试数据:      123456789  123aaaa", "测试数据", -1, null);
                        if (!result.equals("123456789")) System.out.println("Wrong: ");
                    }
                    System.out.println("THREAD-"+index+", cost="+(System.currentTimeMillis()-start));
                }
            }.start();
        }

    }

}