package com.shrikantanand.productservice;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.IntegerSerializer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JacksonJsonSerializer;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.shrikantanand.productservice.dto.ProductEvent;

@SpringBootApplication
@EnableScheduling
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
	
	@Bean
	public ProducerFactory<Integer, ProductEvent> productAddedEventProducerFactory() {
		Map<String, Object> configProps = new HashMap<>();
		configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
		configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, IntegerSerializer.class);
		configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JacksonJsonSerializer.class);
		configProps.put(ProducerConfig.RETRIES_CONFIG, 0);
	    configProps.put(ProducerConfig.MAX_BLOCK_MS_CONFIG, 2000);
	    configProps.put(ProducerConfig.RECONNECT_BACKOFF_MS_CONFIG, 3000);
	    configProps.put(ProducerConfig.RECONNECT_BACKOFF_MAX_MS_CONFIG, 24000);
		return new DefaultKafkaProducerFactory<>(configProps);
	}
	
	@Bean
	public KafkaTemplate<Integer, ProductEvent> productAddedEventKafkaTemplate() {
	    return new KafkaTemplate<>(productAddedEventProducerFactory());
	}

}
