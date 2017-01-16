package com.constantsoft.invoice.ocr.example;

import org.junit.Test;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by walter.xu on 2017/1/9.
 */
public class ThreadTest {

    @Test
    public void testThread() throws Exception {
        ExecutorService service = Executors.newFixedThreadPool(10);
        for (int i=0; i<100;i++){
            service.submit(new CallThread(i));
        }
        service.shutdown();
        while (!service.isTerminated()){Thread.sleep(100);}
        System.out.println("finished/");
    }

    private static class CallThread implements Callable<String>{
        private int index;
        public CallThread(int index){this.index = index;}

        @Override
        public String call() throws Exception {
            Thread.sleep((int)(Math.random()*10000));
            System.out.println("["+index+"] finish...");
            return "RETURN-"+index;
        }
    }
}
