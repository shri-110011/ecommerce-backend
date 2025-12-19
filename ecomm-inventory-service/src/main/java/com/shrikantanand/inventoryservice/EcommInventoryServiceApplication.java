package com.shrikantanand.inventoryservice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.IntegerDeserializer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties.AckMode;
import org.springframework.kafka.support.serializer.JacksonJsonDeserializer;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.shrikantanand.inventoryservice.dto.OrderLifecycleEvent;
import com.shrikantanand.inventoryservice.dto.ProductLifecycleEvent;

@SpringBootApplication
@EnableKafka
@EnableScheduling
public class EcommInventoryServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(EcommInventoryServiceApplication.class, args);
	}
	
	private Map<String, Object> baseProps() {
	    Map<String, Object> props = new HashMap<>();
	    props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
	    props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, IntegerDeserializer.class);
	    props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JacksonJsonDeserializer.class);
	    props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
	    props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
	    return props;
	}
	
	@Bean
	public ConsumerFactory<Integer, ProductLifecycleEvent> productLifecycleEventConsumerFactory() {
		// Configuration details for the consumer factory
		Map<String, Object> configProps = baseProps();
		configProps.put(JacksonJsonDeserializer.VALUE_DEFAULT_TYPE, ProductLifecycleEvent.class);
		return new DefaultKafkaConsumerFactory<>(configProps);
	}
	
	@Bean
	public ConsumerFactory<Integer, OrderLifecycleEvent> orderLifecycleEventConsumerFactory() {
		// Configuration details for the consumer factory
		Map<String, Object> configProps = baseProps();
		configProps.put(JacksonJsonDeserializer.VALUE_DEFAULT_TYPE, OrderLifecycleEvent.class);
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
	
	@Bean
	public ConcurrentKafkaListenerContainerFactory<Integer, OrderLifecycleEvent> 
	orderLifecycleEventListenerFactory() {
		ConcurrentKafkaListenerContainerFactory<Integer, OrderLifecycleEvent> factory = 
				new ConcurrentKafkaListenerContainerFactory<Integer, OrderLifecycleEvent>();
		factory.setConsumerFactory(orderLifecycleEventConsumerFactory());
		factory.getContainerProperties().setAckMode(AckMode.MANUAL);
		return factory;
	}
	
	@Bean
	public RedisTemplate<String, String> redisTemplate(
			RedisConnectionFactory connectionFactory) {
		RedisTemplate<String, String> template = new RedisTemplate<>();
	    template.setConnectionFactory(connectionFactory);
	    
	    // Store keys and values as plain strings
	    template.setKeySerializer(new StringRedisSerializer());
	    template.setValueSerializer(new StringRedisSerializer());
	    
	    return template;
	}
	
	@Bean
    public DefaultRedisScript<List> stockValidationScript() {
        DefaultRedisScript<List> script = new DefaultRedisScript<>();
        script.setLocation(new ClassPathResource("lua-scripts/validate-stock.lua"));
        script.setResultType(List.class);
        return script;
    }
	
}
