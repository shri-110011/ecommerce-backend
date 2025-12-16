package com.shrikantanand.orderservice.api.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponse<T> {
	
	private String status;
	
	private String message;
	
	private long timestamp;
	
    private T data;
    
    private ApiError error;
    
    public static <T> ApiResponse<T> success(
    		String message,
    		T data) {
    	ApiError error = null;
    	
    	ApiResponse<T> response = new ApiResponse<T>();
        response.status = "SUCCESS";
        response.message = message;
        response.error = error;
        response.data = data;
        response.timestamp = System.currentTimeMillis();
        return response;
    }
    
    public static <T> ApiResponse<T> failure(
            String message,
            String errorCode,
            List<ErrorDetail> details) {

        ApiError error = new ApiError(errorCode, details);

        ApiResponse<T> response = new ApiResponse<>();
        response.status = "FAILED";
        response.message = message;
        response.error = error;
        response.data = null;
        response.timestamp = System.currentTimeMillis();
        return response;
    }

}
