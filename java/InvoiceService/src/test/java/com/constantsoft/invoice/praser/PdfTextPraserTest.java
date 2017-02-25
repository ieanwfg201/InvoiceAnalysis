package com.constantsoft.invoice.praser;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.interactive.digitalsignature.PDSignature;
import org.junit.Test;

import java.io.File;
import java.util.List;

import static org.junit.Assert.*;

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