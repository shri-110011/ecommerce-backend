package com.shrikantanand.orderservice;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import lombok.Getter;
import lombok.Setter;

@Configuration
@ConfigurationProperties(prefix = "rest.template")
@Getter
@Setter
public class RestTemplateConfig {

    private int connectTimeout;
    private int readTimeout;

    @Bean
    public RestTemplate restTemplate() {
    	SimpleClientHttpRequestFactory factory =
    	        new SimpleClientHttpRequestFactory();

    	factory.setConnectTimeout(connectTimeout);
    	factory.setReadTimeout(connectTimeout);

    	RestTemplate restTemplate = new RestTemplate(factory);
    	return restTemplate;
    }
    
}
