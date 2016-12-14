package com.constantsoft.bill;

import com.constantsoft.bill.praser.BillPraser;

import java.io.File;
import java.util.Scanner;

/**
 * Created by walter.xu on 2016/11/30.
 */
public class MainGenerate {

    public static void main(String[] args){
        String targetFilePath = "";
        Scanner scan = new Scanner(System.in);
        
        while(true){
        	System.out.println("Please enter a valid pdf file to praser bill code and number. Enter 'exit' to exit");
        	targetFilePath = scan.nextLine();
        	while((!new File(targetFilePath).exists()||new File(targetFilePath).isDirectory())&&!"exit".equalsIgnoreCase(targetFilePath)){
                System.out.println("Target file path is not exist or not a file");
                targetFilePath = scan.nextLine();
            }
        	
        	if ("exit".equalsIgnoreCase(targetFilePath)) {
				break;
			}

            String[] array = BillPraser.getInstance().generateBillCodeAndNumber(targetFilePath);
            if (array==null||array.length==1)
                System.out.println("Could not analysis file: "+targetFilePath);
            else
            System.out.println("Analysis success, Bill code = "+array[0]+", Bill number: "+array[1]);
        }
        System.out.println("Closing application...");
        scan.close();
        
    }
}
