package com.shrikantanand.orderservice.client;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import com.shrikantanand.orderservice.dto.PriceValidationItem;
import com.shrikantanand.orderservice.dto.PriceValidationResponse;
import com.shrikantanand.orderservice.enumeration.OrderErrorCode;
import com.shrikantanand.orderservice.exception.DependencyFailureException;

@Component
public class ProductClient {
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Value("${gateway.service.base-url}")
	private String gatewayServiceBaseUrl;
	
	public PriceValidationResponse validateItemsPrice(List<PriceValidationItem> items) {
		try {
			final String VALIDATE_ITEMS_PRICE_ENDPOINT = 
        			gatewayServiceBaseUrl + "/api/products/price/validation";
        	ResponseEntity<PriceValidationResponse> responseEntity = 
        			 restTemplate.exchange(VALIDATE_ITEMS_PRICE_ENDPOINT, 
        					 HttpMethod.POST, new HttpEntity<>(items), 
        					 new ParameterizedTypeReference<PriceValidationResponse>() {});
        	PriceValidationResponse body = responseEntity.getBody();
            if (body == null) {
                throw new DependencyFailureException(
                        OrderErrorCode.DEPENDENCY_FAILURE,
                        responseEntity.getStatusCode().value(),
                        "product-service returned empty response body"
                );
            }
        	return body;
        } catch (HttpStatusCodeException ex) {
            throw translateAndThrow(ex);
        }
	}
	
	private RuntimeException translateAndThrow(HttpStatusCodeException ex) {
    	HttpStatusCode statusCode = ex.getStatusCode();
    	int status = statusCode.value();
    	if(statusCode.is5xxServerError()) {
    		String message = "product-service failed with server error (" + statusCode + ")";
    		if(status == 503) {
    			message = "product-service is unavailable (503)";
    		}
    		else if(status == 500) {
    			message = "product-service failed with internal error (500)";
    		}
    		throw new DependencyFailureException(
                    OrderErrorCode.DEPENDENCY_FAILURE, 
                    statusCode.value(), 
                    message
            );
    	}
    	else {
	    	throw new DependencyFailureException(
	                OrderErrorCode.DEPENDENCY_FAILURE,
	                ex.getStatusCode().value(),
	                "Unhandled product business error"
	        );
    	}
	}

}
