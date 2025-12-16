package com.shrikantanand.orderservice.client;

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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shrikantanand.orderservice.api.response.ApiResponse;
import com.shrikantanand.orderservice.dto.ReserveItemsRequest;
import com.shrikantanand.orderservice.dto.ReserveItemsResponse;
import com.shrikantanand.orderservice.enumeration.OrderErrorCode;
import com.shrikantanand.orderservice.exception.DependencyFailureException;
import com.shrikantanand.orderservice.exception.InsufficientStockException;

@Component
public class InventoryClient {
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@Value("${gateway.service.base-url}")
	private String gatewayServiceBaseUrl;

    public ReserveItemsResponse reserveItems(ReserveItemsRequest request) {
        try {
        	final String RESERVE_STOCK_ENDPOINT = 
        			gatewayServiceBaseUrl + "/api/inventory/stock/reserve";
        	ResponseEntity<ApiResponse<ReserveItemsResponse>> responseEntity = 
        			 restTemplate.exchange(RESERVE_STOCK_ENDPOINT, 
        					 HttpMethod.POST, new HttpEntity<>(request), 
        					 new ParameterizedTypeReference<ApiResponse<ReserveItemsResponse>>() {});
        	ApiResponse<ReserveItemsResponse> body = responseEntity.getBody();
            if (body == null) {
                throw new DependencyFailureException(
                        OrderErrorCode.DEPENDENCY_FAILURE,
                        responseEntity.getStatusCode().value(),
                        "inventory-service returned empty response body"
                );
            }
        	return body.getData();
        } catch (HttpStatusCodeException ex) {
            throw translateAndThrow(ex);
        }
    }
    
    private RuntimeException translateAndThrow(HttpStatusCodeException ex) {
    	HttpStatusCode statusCode = ex.getStatusCode();
    	int status = statusCode.value();
    	if(statusCode.is5xxServerError()) {
    		String message = "inventory-service failed with server error (" + statusCode + ")";
    		if(status == 503) {
    			message = "inventory-service is unavailable (503)";
    		}
    		else if(status == 500) {
    			message = "inventory-service failed with internal error (500)";
    		}
    		throw new DependencyFailureException(
                    OrderErrorCode.DEPENDENCY_FAILURE, 
                    statusCode.value(), 
                    message
            );
    	}

        ApiResponse<?> error = null;
		try {
			error = objectMapper.readValue(ex.getResponseBodyAsString(), ApiResponse.class);
		} catch (JsonProcessingException e) {
			throw new DependencyFailureException(
	                OrderErrorCode.DEPENDENCY_FAILURE,
	                statusCode.value(),
	                "inventory-service returned malformed error response"
	        );
		}
		
		if (error.getError() == null) {
			throw new DependencyFailureException(
	            OrderErrorCode.DEPENDENCY_FAILURE,
	            ex.getStatusCode().value(),
	            "inventory-service returned invalid error format"
	        );
	    }

        switch (error.getError().getErrorCode()) {
            case "INSUFFICIENT_STOCK":
                throw new InsufficientStockException(
                        OrderErrorCode.INSUFFICIENT_STOCK,
                        error.getError().getDetails()
                );
            default:
            	throw new DependencyFailureException(
                        OrderErrorCode.DEPENDENCY_FAILURE,
                        ex.getStatusCode().value(),
                        "Unhandled inventory business error"
                );
        }
    }

}

