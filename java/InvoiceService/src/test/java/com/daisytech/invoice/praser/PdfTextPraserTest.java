package com.daisytech.invoice.praser;

import org.junit.Test;

import java.io.File;

/**
 * Created by Walter on 2017/2/15.
 */
public class PdfTextPraserTest {
    String filePath = "C:\\Users\\Walter\\Desktop\\InvoiceExample\\exampleFile\\1.pdf";

    @Test
    public void testAll() throws Exception {
        PdfTextPraser.praseNumberAndCode(new File(filePath), true);
    }
}