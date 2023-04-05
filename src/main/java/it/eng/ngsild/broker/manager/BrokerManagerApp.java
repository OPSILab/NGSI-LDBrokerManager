package it.eng.ngsild.broker.manager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

import it.eng.ngsild.broker.manager.configuration.AppConfig;
@Import(AppConfig.class)
@SpringBootApplication
@ComponentScan(basePackages = {"it.eng.ngsild.broker.manager"})
public class BrokerManagerApp {

	public static void main(String[] args) {
		SpringApplication.run(BrokerManagerApp.class, args);
	}

}
