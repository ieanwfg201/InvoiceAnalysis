package com.constantsoft.invoice.service.util;

import org.junit.Test;

/**
 * Created by Walter on 2017/2/18.
 */
public class PDFInvoiceInforGenerateFactoryTest {

    @Test
    public void testFindStrByAvailableWords() throws Exception {
        PDFInvoiceInforGenerateFactory factory = new PDFInvoiceInforGenerateFactory();
        String line = "北京增值税电子普通发票 011001600111\n" +
                "统一发票\n" +
                "发票代码 ：\n" +
                "监\n" +
                "国 制 发票号码 ：23037182\n" +
                "北   京\n" +
                "国 开票日期 ：2016年08月17日家税务局监制\n" +
                "机器编号  ：499099703317 校   验      码 ：03562 19533 95545 80577\n" +
                "购 名              称     ： 庄蔚 密 033>/80138<5+2-6*142/<+-536*\n" +
                "买 纳税人识别号\n" +
                "： 6/79<*40/93>/80138<5+2-6307>\n" +
                "码\n" +
                "地  址、电  话： *13>/80138<5+2-6*14-6+<9<-0-\n" +
                "方\n" +
                "开户行及账号 ： 区 9>06<+7>390116>>193679873765\n" +
                "货物或应税劳务、服务名称    规格型号 单位 数  量 单  价 金    额 税率 税    额\n" +
                "通信服务费 164.00 * *\n" +
                "合              计  164.00 *\n" +
                "价税合计 (大写） 壹佰陆拾肆元整 （小写）  164.00\n" +
                "名              称     ： 中国联合网络通信有限公司北京市分公司 业务号码:13146559236;  账期:201607;销\n" +
                "纳税人识别号：1 \n" +
                "备\n" +
                "10102801657272\n" +
                "售\n" +
                "地  址、电  话： 北京市西城区复兴门南大街6号,10010\n" +
                "方 开户行及账号 ： 注中国工商银行北京市长安支行 0200003319210012727\n" +
                "收款人： 复核： 开票人：jinxin88 销售方：  （ 章）\n" +
                "全\n" +
                "章\n";
        String[] arrays = line.split("\n");
        for(String str: arrays){
            System.out.println(str);
            factory.executeLine(str);
        }
        System.out.println(factory);
    }

    @Test
    public void testMethod(){
        PDFInvoiceInforGenerateFactory factory = new PDFInvoiceInforGenerateFactory();
        String item = "\"    机器编号:661565721381 校 验 码: 59957 20760 01303 22796\"";
        System.out.println(factory.findStrByAvailableWords(item, 10,20,"1234567890 "));
    }
}