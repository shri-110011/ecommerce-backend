package com.shrikantanand.orderservice;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.IntegerSerializer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JacksonJsonSerializer;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.shrikantanand.orderservice.dto.OrderLifecycleEvent;

@SpringBootApplication
@EnableScheduling
public class EcommOrderServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(EcommOrderServiceApplication.class, args);
	}
	
	@Bean
	public ProducerFactory<Integer, OrderLifecycleEvent> productLifeCycleEventProducerFactory() {
		Map<String, Object> configProps = new HashMap<>();
		configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
		configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, IntegerSerializer.class);
		configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JacksonJsonSerializer.class);
		configProps.put(ProducerConfig.RETRIES_CONFIG, 0);
	    configProps.put(ProducerConfig.MAX_BLOCK_MS_CONFIG, 2000);
	    configProps.put(ProducerConfig.RECONNECT_BACKOFF_MS_CONFIG, 3000);
	    configProps.put(ProducerConfig.RECONNECT_BACKOFF_MAX_MS_CONFIG, 24000);
	    
	    // This is required to tell producer to not send the type info for the message.
	    // Basically it will tell the producer to not send __TypeId__ header info.
	    configProps.put(JacksonJsonSerializer.ADD_TYPE_INFO_HEADERS, false);
		return new DefaultKafkaProducerFactory<>(configProps);
	}
	
	@Bean
	public KafkaTemplate<Integer, OrderLifecycleEvent> productLifeCycleEventKafkaTemplate() {
	    return new KafkaTemplate<>(productLifeCycleEventProducerFactory());
	}

}
