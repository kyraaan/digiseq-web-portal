package com.digiseq.digiseqwebportal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.digiseq.digiseqwebportal")
public class WebPortalApplication {

  public static void main(String[] args) {
    SpringApplication.run(WebPortalApplication.class, args);
  }
}
