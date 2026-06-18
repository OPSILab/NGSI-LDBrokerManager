package it.eng.ngsild.broker.manager.configuration;

import java.nio.charset.StandardCharsets;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;

@Configuration
@OpenAPIDefinition(
		  info = @Info(title = "NGSI-LD Broker Manager", version = "v2"),

		  servers = {@Server(url = "/", description = "Default Server URL")}
	)

public class AppConfig {
	@Bean
    public RestTemplate restTemplate() {

		HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
		  requestFactory.setReadTimeout(600000);
		  requestFactory.setConnectTimeout(600000);
		  RestTemplate restTemplate = new RestTemplate(requestFactory);

		  // Force UTF-8 for String request/response bodies. The default
		  // StringHttpMessageConverter uses ISO-8859-1, so JSON posted to Orion
		  // (application/json without an explicit charset) was being written as
		  // Latin-1: accented characters (e.g. "qualità") ended up stored as
		  // single high bytes and broke strict UTF-8 read-back. Replacing the
		  // converter fixes the write side at the source.
		  restTemplate.getMessageConverters().removeIf(c -> c instanceof StringHttpMessageConverter);
		  StringHttpMessageConverter utf8StringConverter =
		      new StringHttpMessageConverter(StandardCharsets.UTF_8);
		  utf8StringConverter.setWriteAcceptCharset(false);
		  restTemplate.getMessageConverters().add(0, utf8StringConverter);

		  return restTemplate;
    }
}
