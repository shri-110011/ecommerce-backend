package com.shrikantanand.inventoryservice;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.IntegerDeserializer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties.AckMode;
import org.springframework.kafka.support.serializer.JacksonJsonDeserializer;

import com.shrikantanand.inventoryservice.dto.ProductLifecycleEvent;

@SpringBootApplication
@EnableKafka
public class EcommInventoryServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(EcommInventoryServiceApplication.class, args);
	}
	
	@Bean
	public ConsumerFactory<Integer, ProductLifecycleEvent> productLifecycleEventConsumerFactory() {
		// Configuration details for the consumer factory
		Map<String, Object> configProps = new HashMap<>();
		configProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
		configProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
				IntegerDeserializer.class);
		configProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
				JacksonJsonDeserializer.class);
		configProps.put(JacksonJsonDeserializer.VALUE_DEFAULT_TYPE, ProductLifecycleEvent.class);
		configProps.put(ConsumerConfig.GROUP_ID_CONFIG, "inventory-service.sync-inventory");
		configProps.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
		configProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
		return new DefaultKafkaConsumerFactory<>(configProps);
	}
	
	@Bean
	public ConcurrentKafkaListenerContainerFactory<Integer, ProductLifecycleEvent> 
	productLifecycleEventListenerFactory() {
		ConcurrentKafkaListenerContainerFactory<Integer, ProductLifecycleEvent> factory = 
				new ConcurrentKafkaListenerContainerFactory<Integer, ProductLifecycleEvent>();
		factory.setConsumerFactory(productLifecycleEventConsumerFactory());
		factory.getContainerProperties().setAckMode(AckMode.MANUAL);
		return factory;
	}
	
}
