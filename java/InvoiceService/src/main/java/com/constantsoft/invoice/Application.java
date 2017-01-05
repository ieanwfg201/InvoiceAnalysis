package com.constantsoft.invoice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.context.annotation.ComponentScan;

/**
 * Created by walter.xu on 2016/12/30.
 */
@SpringBootApplication
@ComponentScan("com.constantsoft.invoice")
public class Application implements EmbeddedServletContainerCustomizer {
    private static String path = "D:\\log\\ocr\\tessData";
    public static void main(String[] args){
        SpringApplication.run(Application.class, args);
        if (args!=null&&args.length>0) path = args[0];
    }
    @Override
    public void customize(ConfigurableEmbeddedServletContainer configurableEmbeddedServletContainer) {
        configurableEmbeddedServletContainer.setPort(9999);
    }
    public static String getDataPath(){return path;}
}
