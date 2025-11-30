package com.shrikantanand.productservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@SpringBootApplication
public class EcommProductServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(EcommProductServiceApplication.class, args);
	}
	
	@Bean
	public RedisTemplate<String, Object> redisTemplate(
			RedisConnectionFactory connectionFactory) {
		RedisTemplate<String, Object> template = new RedisTemplate<>();
	    template.setConnectionFactory(connectionFactory);
	    
	    // Store keys and values as plain strings
	    template.setKeySerializer(new StringRedisSerializer());
	    template.setValueSerializer(new StringRedisSerializer());
	    
	    // Store hash keys and hash values as plain strings
	    template.setHashKeySerializer(new StringRedisSerializer());
	    template.setHashValueSerializer(new StringRedisSerializer());
	    return template;
	}

}
