package com.shrikantanand.productservice.outbox;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.shrikantanand.productservice.dao.ProductEventOutboxRepository;
import com.shrikantanand.productservice.dto.ProductEvent;
import com.shrikantanand.productservice.entity.ProductEventOutbox;

@Component
public class ProductEventOutboxService {
	
	@Autowired
	private ProductEventOutboxRepository productEventOutboxRepository;
	
	@Autowired
	private ProductEventProducer productEventProducer;
	
	@Transactional
	public void processPendingProductEvents() {
		List<ProductEventOutbox> events = 
				productEventOutboxRepository.getAllPendingProductEvents(PageRequest.ofSize(100));
		Map<Integer,ProductEventOutbox> latestProductEventsMap = events.stream()
				.collect(Collectors.groupingBy(
						ProductEventOutbox::getProductId,
						Collectors.collectingAndThen(
								Collectors.maxBy(
										Comparator.comparing(ProductEventOutbox::getCreatedDateTime)
								),
								e -> e.get()
						)
				));
		Set<Integer> processedProductIds = new HashSet<>();
		final String lastUpdateddBy = "ADMIN";
		latestProductEventsMap.entrySet()
		.stream()
		.forEach(e -> {
			ProductEvent event = new ProductEvent();
			event.setProductId(e.getKey());
			event.setEventType(e.getValue().getEventType());
			// We are waiting for the producer to get acknowledgement from 
			// message broker after it has published the message.
			boolean result = productEventProducer.publishProductEvent(event);
			if(result) processedProductIds.add(e.getValue().getProductId());
		});
		for(ProductEventOutbox event : events) {
			final LocalDateTime now = LocalDateTime.now();
			if(processedProductIds.contains(event.getProductId())) {
				event.setIsProcessed('Y');
			}
			else {
				int curRetryCount = event.getRetryCount();
				event.setRetryCount(curRetryCount + 1);
				LocalDateTime nextRetryAt = getNextRetryAt(curRetryCount);
				event.setNextRetryAt(nextRetryAt);
			}
			event.setLastUpdatedDateTime(now);
			event.setLastUpdatedBy(lastUpdateddBy);
		}
	}
	
	private LocalDateTime getNextRetryAt(int retryCount) {
		final long BASE_DELAY_MS  = 2000;
		final long MAX_DELAY_MS = 60000;
		long delay = BASE_DELAY_MS  * (1L << retryCount);
		delay = Math.min(delay, MAX_DELAY_MS);
		// We are using ThreadLocalRandom.current() because it will give each 
		// thread a random number generator.
		// jitter value would range from 0 to delay and follows uniform distribution.
		long jitter = ThreadLocalRandom.current().nextLong(delay);
		LocalDateTime nextRetryAt = LocalDateTime.now().plusNanos(jitter * 1_000_000);
		return nextRetryAt;
	}

}
