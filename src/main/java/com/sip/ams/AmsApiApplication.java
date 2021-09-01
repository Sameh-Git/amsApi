package com.sip.ams;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import java.io.File;
@SpringBootApplication
public class AmsApiApplication {
	public static String providerDirectory =
			System.getProperty("user.dir")+"/src/main/resources/static/images/provider";
	public static String articleDirectory =
			System.getProperty("user.dir")+"/src/main/resources/static/images/article";

	public static void main(String[] args) {
		new File(providerDirectory).mkdir();
		new File(articleDirectory).mkdir();
		SpringApplication.run(AmsApiApplication.class, args);
	}

}
