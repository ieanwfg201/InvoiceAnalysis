package com.daisytech.invoice.praser.util;


import java.util.Arrays;

/**
 * Created by walter.xu on 2016/12/29.
 */
public class InvoiceCodeAndNumberGeneratorTest {

    public static void main(String[] args) throws Exception{

        testFindAndSetInvoidNumberAndCodeByNumberLength();
        testFindNextStringByPattern();
    }


    public static void testFindAndSetInvoidNumberAndCodeByNumberLength() throws Exception {
        String[] data = new String[2];
        InvoiceCodeAndNumberGenerator.findAndSetInvoidNumberAndCodeByNumberLength("ify��e??s: 031001600211", data);
        InvoiceCodeAndNumberGenerator.findAndSetInvoidNumberAndCodeByNumberLength("i%%z2,: 44615786", data);
        InvoiceCodeAndNumberGenerator.findAndSetInvoidNumberAndCodeByNumberLength("11%? E12111: 2016$09H05EI", data);
        System.out.println(Arrays.toString(data));
    }

    public static void testFindNextStringByPattern() throws Exception {

    }
}