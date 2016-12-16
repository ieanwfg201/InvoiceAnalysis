package com.constantsoft.invoice.ocr.example;

import net.sourceforge.tess4j.ITessAPI;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract1;
import net.sourceforge.tess4j.Word;

import javax.imageio.ImageIO;
import javax.sound.sampled.DataLine;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;
import java.util.Scanner;

/**
 * Created by walter.xu on 2016/12/16.
 */
public class OCRTest {

    private static ITesseract instance;

    public static void main(String[] args) throws Exception{

//        doOCR(args);
        doTest();
    }

    public static void doTest() throws Exception {
        String file = "D:\\log\\pdf\\test.jpg";
        instance = new Tesseract1(); // JNA Direct Mapping
        instance.setLanguage("chi_sim");


        BufferedImage image = ImageIO.read(new File(file));
        List<Rectangle> datList = instance.getSegmentedRegions(image, ITessAPI.TessPageIteratorLevel.RIL_TEXTLINE);

        System.out.println(datList.size());
        datList.forEach(data -> {
            try {
                System.out.println(instance.doOCR(image, data));
            }catch (Exception e){
                e.printStackTrace();
            }
        });
    }

    public static void doOCR(String[] args) throws Exception{
        Scanner sc = new Scanner(System.in);
        instance = new Tesseract1(); // JNA Direct Mapping
//        instance.setLanguage("fontyp");
        instance.setLanguage("chi_sim");

        String imagePath = args==null||args.length==0?null:args[0];

        while (imagePath==null||imagePath.equals("")){
            System.out.println("Please enter a image path or directory.");
            imagePath = sc.next();
        }
        while(!new File(imagePath).exists()){
            System.out.println("Image path is not existed, please enter another one.");
            imagePath = sc.next();
        }
        File path = new File(imagePath);
        if (path.isDirectory()){
            File[] arrays = path.listFiles();
            for(File subFile: arrays){
                if (subFile.isFile()) doOcr(subFile);
            }
        }else{
            doOcr(path);
        }
    }

    public static void doOcr(File imageFile) throws Exception{
        BufferedImage image = ImageIO.read(imageFile);
        List<Word> wordList = instance.getWords(image, ITessAPI.TessPageIteratorLevel.RIL_TEXTLINE);

        StringBuilder str = new StringBuilder("File = ").append(imageFile.getName());
        str.append(": ");
        for(Word word: wordList){
            if (word.getText().indexOf("No")>0){
                String targetLine = word.getText();
                String[] arrays = targetLine.split(" ");
                String code =null, number = null;
                for(String target: arrays){
                    if (target.length()==10){
                        if (code==null) code = target;
                        else number = target;
                    }
                }
                str.append("[").append(code).append(", ").append(number);
            }
        }
        System.out.println(str);
    }
}
