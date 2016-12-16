package com.constantsoft.invoice.praser;


import java.io.File;
import java.util.Arrays;

/**
 * Created by walter.xu on 2016/12/16.
 */
public class OCRPraserTest {

    public static void main(String[] args) throws Exception {
        String file = "D:\\log\\pdf\\test.jpg";
        long start = System.currentTimeMillis();
        System.out.println(Arrays.toString(OCRPraser.praseNumberAndCode(new File(file))));
        System.out.println("Cost time = "+(System.currentTimeMillis()-start)+"ms");
        start = System.currentTimeMillis();
        System.out.println(Arrays.toString(OCRPraser.praseNumberAndCode(new File(file))));
        System.out.println("Cost time = "+(System.currentTimeMillis()-start)+"ms");
    }
}