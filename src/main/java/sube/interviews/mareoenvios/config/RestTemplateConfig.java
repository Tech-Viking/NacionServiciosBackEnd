package sube.interviews.mareoenvios.config;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {
	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
	    CloseableHttpClient httpClient = HttpClients.custom()
	            .build();

	    HttpComponentsClientHttpRequestFactory requestFactory = 
	            new HttpComponentsClientHttpRequestFactory(httpClient);

	    return builder
	            .requestFactory(() -> requestFactory)
	            .build();
	}

}
