package com.example.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Data
@JsonIgnoreProperties({ "cause", "stack_trace", "suppressed", "localized_message", "stackTrace", "localizedMessage" })
public class GenericError extends RuntimeException {

	private HttpStatus code;

	private String message;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
	private LocalDateTime timestamp;

	public GenericError(HttpStatus code, String message) {
		this.code = code;
		this.message = message;
		this.timestamp = LocalDateTime.now();
	}

}
